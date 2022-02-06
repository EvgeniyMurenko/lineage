package com.lineage.script;

import com.fazecast.jSerialComm.SerialPort;
import com.lineage.domain.Key;
import com.lineage.domain.Profile;
import com.lineage.util.Utils;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractScript implements Script {

    protected static final int MAX_DELAY_SEC = 60;

    // имя игрока для отправки нотификации в чат бот
    private final String playerName;

    // номер окна в панеле
    private final KeyCode winKeyCode;
    // для работы с окном
    private final Robot robot;
    // для работы с ардуино
    private final SerialPort serialPort;
    // хп моба
    private final Color colorMonsterHealsPoint;
    // крайние не закрашеные координата Х моба
    private final int posX;
    // крайние не закрашеные координата Y моба
    private final int posY;

    // длительность баффа
    private final LocalTime buffDurationTime;
    // длительность каста баффа
    private final int buffCastInMilliseconds;

    private boolean isRunning = false;
    private boolean inFight = false;

    private LocalDateTime lastBuff;

    public AbstractScript(String windowNum, Robot robot, SerialPort serialPort, Profile profile, String playerName, LocalTime buffDurationTime, int buffCastInMilliseconds) {
        this.winKeyCode = KeyCode.getKeyCode(windowNum);
        this.robot = robot;
        this.serialPort = serialPort;
        this.colorMonsterHealsPoint = profile.getColor();
        this.posX = profile.getXCord();
        this.posY = profile.getYCord();
        this.buffDurationTime = buffDurationTime;
        this.buffCastInMilliseconds = buffCastInMilliseconds;
        this.playerName = playerName;
    }

    @Override
    public void startScript() {
        isRunning = true;
        switchWindow();
        while (isRunning) {
            process();
        }
    }

    @Override
    public void stopScript() {
        isRunning = false;
    }

    @Override
    public KeyCode getWinKeyCode() {
        return winKeyCode;
    }

    @Override
    // начинаем бафать по дефолту
    public void startBuff() {
        Utils.sendCommand(serialPort, Key.KEY_F5); // need send some code of button
        Utils.delay(buffCastInMilliseconds);
        lastBuff = LocalDateTime.now().plusSeconds(Utils.getRandomSeconds(MAX_DELAY_SEC));
        System.out.println("Send command to press buff! time = " + lastBuff);
    }

    private void switchWindow() {
        Utils.switchWindow(robot, winKeyCode);
    }

    // метод для подбора лута
    protected void pickUpLoot() {
        for (int i = 0; i < 5; i++) {
            Utils.delay(100);
            Utils.sendCommand(serialPort, Key.KEY_F4); // pickUp
        }
    }

    // проверка хм моба
    protected boolean checkMonsterHealPoints() {
        return isHealsPointPresent(posX, posY, colorMonsterHealsPoint);
    }

    // проверить нужно ли бафатся
    protected boolean isBuffNeed() {
        return lastBuff == null || LocalDateTime.now().isAfter(lastBuff.plusMinutes(buffDurationTime.getMinute()).plusSeconds(buffDurationTime.getSecond()));
    }

    // находим следующий таргет
    protected void sendNextTarget() {
        System.out.println("Next target");
//        Utils.sendCommand(serialPort, Key.KEY_ESC);
        Utils.sendCommand(serialPort, Key.KEY_F1); // next target

        if (!checkMonsterHealPoints()) {
            Utils.sendCommand(serialPort, Key.KEY_F2); // target solina
        }

        if (checkMonsterHealPoints()) {
            System.out.println("Attack!");
//            Utils.delay(2000);
            Utils.sendCommand(serialPort, Key.KEY_F3); // attack sum + per
            inFight = true;
        }
    }

    // отправляем атаковать
    protected void sendAttack() {
        Utils.sendCommand(serialPort, Key.KEY_F3);
    }

    // если хм у моба мало готовимся лутать
    protected void preparationForLoot () {
        Utils.delay(500);
        inFight = false;
        pickUpLoot();
        Utils.sendCommand(serialPort, Key.KEY_ESC);
    }

    protected void sendCommand(Key key) {
        Utils.sendCommand(serialPort, key);
    }

    // проверка хп по координатам
    protected boolean isHealsPointPresent(int xPetPos, int yPetPos, Color color) {
        AtomicBoolean result = new AtomicBoolean(false);
        Platform.runLater(() -> {
            result.set(color.equals(robot.getPixelColor(xPetPos, yPetPos)));
        });
        return result.get();
    }

    // getters
    protected boolean isInFight() {
        return inFight;
    }

    protected LocalDateTime getLastBuff() {
        return lastBuff;
    }

    // setters
    protected void setLastBuff(LocalDateTime lastBuff) {
        this.lastBuff = lastBuff;
    }
}
