package com.lineage.domain;

import com.fazecast.jSerialComm.SerialPort;
import com.lineage.Utils;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;

public class Summoner implements Account {

    private final KeyCode winKeyCode;
    private final Robot robot;
    private final SerialPort serialPort;

    private static final Color COLOR_HEALS_POINT = Color.rgb(111, 23, 19);
//    private static final Color COLOR_HEALS_POINT = Color.rgb(127, 70, 68);
    private static final int POS_X =  791;
    private static final int POS_Y =  51;
    private boolean inFight = false;

    public Summoner(String windowNum, Robot robot, SerialPort serialPort) {
        this.winKeyCode = KeyCode.getKeyCode(windowNum);
        this.robot = robot;
        this.serialPort = serialPort;
    }

    @Override
    public Account process(Account activeWindow) {
        if (activeWindow == null || !activeWindow.equals(this)) {
            switchWindow();
            activeWindow = this;
        }

        if (inFight) {
            if (!COLOR_HEALS_POINT.equals(robot.getPixelColor(POS_X, POS_Y))) {
                Utils.delay(500);
                inFight = false;
            }
        } else {
            sendNextTarget();
        }
        return activeWindow;
    }

    @Override
    public void switchWindow() {
        robot.keyPress(KeyCode.WINDOWS);
        robot.keyPress(winKeyCode);
        robot.keyRelease(winKeyCode);
        robot.keyRelease(KeyCode.WINDOWS);
    }

    private void sendNextTarget() {
        System.out.println("Next target");
        Utils.sendCommand(serialPort, 49);
        Utils.delay(500);
        if (COLOR_HEALS_POINT.equals(robot.getPixelColor(POS_X, POS_Y))) {
            System.out.println("Attack!");
            Utils.sendCommand(serialPort, 50);
            inFight = true;
        }
    }

    public KeyCode getWinKeyCode() {
        return winKeyCode;
    }
}
