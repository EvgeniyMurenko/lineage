package com.lineage.script;

import com.fazecast.jSerialComm.SerialPort;
import com.lineage.domain.Key;
import com.lineage.domain.Profile;
import com.lineage.util.Utils;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Summoner extends AbstractScript {

    private static final LocalTime BUFF_DURATION_TIME = LocalTime.of(0, 18, 30);
    private static final int BUFF_CAST_IN_MILLISECONDS = 7500;

    private final Color petColorHealsPoint = Color.rgb(111, 23, 19);
    private final int xPetPos = 510;
    private final int yPetPos = 867;

    private final LocalTime professiBuffTime = LocalTime.of(0, 4, 0);
    private LocalDateTime professiLastBuff;

    public Summoner(String windowNum, Robot robot, SerialPort serialPort, Profile profile, String playerName) {
        super(windowNum, robot, serialPort, profile, playerName, BUFF_DURATION_TIME, BUFF_CAST_IN_MILLISECONDS);
    }

    @Override
    public void process() {

        if (professiLastBuff == null || LocalDateTime.now().isAfter(professiLastBuff.plusMinutes(professiBuffTime.getMinute()).plusSeconds(professiBuffTime.getSecond()))) {
            sendCommand(Key.KEY_F6); // need send some code of button
            professiLastBuff = LocalDateTime.now().plusSeconds(Utils.getRandomSeconds(MAX_DELAY_SEC));
            Utils.delay(2000);
            System.out.println("Send command to press summon professi Buff! time = " + professiLastBuff);
        }

        if (isBuffNeed()) {
            startBuff();
        }

        if (isNeedHealPet()) {
            System.out.println("Heal pet!");
            sendCommand(Key.KEY_F7); // heal pet
            sendCommand(Key.KEY_F8); // resolve pet
        }

        if (isInFight()) {
            sendAttack();
            if (!checkMonsterHealPoints()) {
                preparationForLoot();
            }
        } else {
            sendNextTarget();
        }
    }

    // начинаем бафать
    @Override
    public void startBuff() {
        sendCommand(Key.KEY_F5); // need send some code of button
        Utils.delay(BUFF_CAST_IN_MILLISECONDS);
        sendCommand(Key.KEY_F9); // банка хасты
        Utils.delay();
        sendCommand(Key.KEY_F9); // банка хасты
        Utils.delay();
        sendCommand(Key.KEY_F9); // банка хасты
        Utils.delay();
        setLastBuff(LocalDateTime.now().plusSeconds(Utils.getRandomSeconds(MAX_DELAY_SEC)));
        System.out.println("Send command to press summon buff! time = " + getLastBuff());
    }

    // проверяем нужно ли хилять пета
    private boolean isNeedHealPet() {
        return !isHealsPointPresent(xPetPos, yPetPos, petColorHealsPoint);
    }
}
