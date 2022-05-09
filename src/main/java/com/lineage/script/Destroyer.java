package com.lineage.script;

import com.fazecast.jSerialComm.SerialPort;
import com.lineage.domain.Profile;
import javafx.scene.robot.Robot;

import java.time.LocalTime;

public class Destroyer extends AbstractScript {

    private static final LocalTime BUFF_DURATION_TIME = LocalTime.of(0, 18, 30);
    private static final int BUFF_CAST_IN_MILLISECONDS = 1500;

    public Destroyer(String windowNum, Robot robot, SerialPort serialPort, Profile profile, String playerName) {
        super(windowNum, robot, serialPort, profile, playerName, BUFF_DURATION_TIME, BUFF_CAST_IN_MILLISECONDS);
    }

    @Override
    public void process() {

        if (isBuffNeed()) {
            startBuff();
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
}
