package com.lineage.domain;

import com.fazecast.jSerialComm.SerialPort;
import com.lineage.Utils;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;

import java.time.LocalTime;

public class ShillienKnight implements Account {

    private final KeyCode winKeyCode;
    private final Robot robot;
    private final SerialPort serialPort;
    private final Color COLOR_HEALS_POINT;
    private final int POS_X;
    private final int POS_Y;
    private boolean inFight = false;
    private static final int MAX_DELAY_SEC = 60;

    private final LocalTime buffTime = LocalTime.of(0, 18, 30);
    private LocalTime lastBuff;

    public ShillienKnight(String windowNum, Robot robot, SerialPort serialPort, Profile profile) {
        this.winKeyCode = KeyCode.getKeyCode(windowNum);
        this.robot = robot;
        this.serialPort = serialPort;
        this.COLOR_HEALS_POINT = profile.getColor();
        this.POS_X = profile.getxCord();
        this.POS_Y = profile.getyCord();
    }

    @Override
    public Account process(Account activeWindow) {

        if (lastBuff == null || LocalTime.now().isAfter(lastBuff.plusMinutes(buffTime.getMinute()).plusSeconds(buffTime.getSecond()))) {
            if (activeWindow == null || !activeWindow.equals(this)) {
                Utils.switchWindow(robot, winKeyCode);
                activeWindow = this;
            }
            Utils.sendCommand(serialPort, Key.KEY_F5); // need send some code of button
            lastBuff = LocalTime.now().plusSeconds(Utils.getRandomSeconds(MAX_DELAY_SEC));
            System.out.println("Send command to press buff! time = " + lastBuff);
        }

        if (activeWindow == null || !activeWindow.equals(this)) {
            Utils.switchWindow(robot, winKeyCode);
            activeWindow = this;
        }

        if (inFight) {
            if (!COLOR_HEALS_POINT.equals(robot.getPixelColor(POS_X, POS_Y))) {
                Utils.delay(500);
                inFight = false;
                pickUpLoot();
            }
        } else {
            assistSummoner();
        }
        return activeWindow;
    }

    private void pickUpLoot() {
        for (int i = 0; i < 10; i++) {
            Utils.sendCommand(serialPort, Key.KEY_F3); // pickUp
            Utils.delay(100);
        }
    }

    private void assistSummoner() {
        Utils.sendCommand(serialPort, Key.KEY_F1);
        inFight = true;
        Utils.delay(500);
        if (COLOR_HEALS_POINT.equals(robot.getPixelColor(POS_X, POS_Y))) {
            inFight = true;
        }
    }

    public KeyCode getWinKeyCode() {
        return winKeyCode;
    }
}
