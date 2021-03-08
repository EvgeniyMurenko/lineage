package com.lineage.domain;

import com.fazecast.jSerialComm.SerialPort;
import com.lineage.Utils;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;

import java.time.LocalTime;

public class Summoner implements Account {

    private final KeyCode winKeyCode;
    private final Robot robot;
    private final SerialPort serialPort;
    private final Color COLOR_HEALS_POINT;
    private final int POS_X;
    private final int POS_Y;

    private final Color COLOR_HEALS_POINT_PET = Color.rgb(135, 29, 24);
    private final int POS_PET_X = 602;
    private final int POS_PET_Y = 869;

    private boolean inFight = false;

    private static final int MAX_DELAY_SEC = 60;

    private final LocalTime buffTime = LocalTime.of(0, 18, 30);
    private LocalTime lastBuff;

    private final LocalTime professiBuffTime = LocalTime.of(0, 4, 0);
    private LocalTime professiLastBuff;

    public Summoner(String windowNum, Robot robot, SerialPort serialPort, Profile profile) {
        this.winKeyCode = KeyCode.getKeyCode(windowNum);
        this.robot = robot;
        this.serialPort = serialPort;
        this.COLOR_HEALS_POINT = profile.getColor();
        this.POS_X = profile.getxCord();
        this.POS_Y = profile.getyCord();
    }

    @Override
    public Account process(Account activeWindow) {

        activeWindow = checkAndSetActiveWindow(activeWindow);

        if (professiLastBuff == null || LocalTime.now().isAfter(professiLastBuff.plusMinutes(professiBuffTime.getMinute()).plusSeconds(professiBuffTime.getSecond()))) {
            Utils.sendCommand(serialPort, Key.KEY_F6); // need send some code of button
            professiLastBuff = LocalTime.now().plusSeconds(Utils.getRandomSeconds(MAX_DELAY_SEC));
            Utils.delay(2000);
            System.out.println("Send command to press summon professi Buff! time = " + professiLastBuff);
        }

        if (lastBuff == null || LocalTime.now().isAfter(lastBuff.plusMinutes(buffTime.getMinute()).plusSeconds(buffTime.getSecond()))) {
            Utils.sendCommand(serialPort, Key.KEY_F5); // need send some code of button
            lastBuff = LocalTime.now().plusSeconds(Utils.getRandomSeconds(MAX_DELAY_SEC));
            Utils.delay(5500);
            System.out.println("Send command to press summon buff! time = " + lastBuff);
        }

        if (!COLOR_HEALS_POINT_PET.equals(robot.getPixelColor(POS_PET_X, POS_PET_Y))) {
            System.out.println("Heal pet!");
            Utils.sendCommand(serialPort, Key.KEY_F7); // heal pet
            Utils.sendCommand(serialPort, Key.KEY_F8); // resolve pet
        }

        if (inFight) {
            Utils.sendCommand(serialPort, Key.KEY_F3);
            if (!checkMonsterHealPoints()) {
                Utils.delay(500);
                inFight = false;
                pickUpLoot();
            }
        } else {
            sendNextTarget();
        }
        return activeWindow;
    }

    private Account checkAndSetActiveWindow(Account activeWindow) {
        if (activeWindow == null || !activeWindow.equals(this)) {
            Utils.switchWindow(robot, winKeyCode);
            activeWindow = this;
        }
        return activeWindow;
    }

    private void sendNextTarget() {
        System.out.println("Next target");
//        Utils.sendCommand(serialPort, Key.KEY_ESC);
        Utils.sendCommand(serialPort, Key.KEY_F1); // next target

        if (!checkMonsterHealPoints()) {
            Utils.sendCommand(serialPort, Key.KEY_F2); // target solina
        }

        if (checkMonsterHealPoints()) {
            System.out.println("Attack!");
            Utils.sendCommand(serialPort, Key.KEY_F3); // attack sum + per
            inFight = true;
        }
    }

    private boolean checkMonsterHealPoints() {
        return COLOR_HEALS_POINT.equals(robot.getPixelColor(POS_X, POS_Y));
    }

    private void pickUpLoot() {
        for (int i = 0; i < 5; i++) {
            Utils.sendCommand(serialPort, Key.KEY_F4); // pickUp
        }
    }

    public KeyCode getWinKeyCode() {
        return winKeyCode;
    }
}
