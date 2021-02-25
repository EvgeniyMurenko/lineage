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

    private static final Color COLOR_HEAL_POINT_MIDDLE = Color.valueOf("0x3c3f41ff");
    private static final int MIN_X =  789;
    private static final int MAX_X =  1200;
    private static final int POS_Y =  50;
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
            if (COLOR_HEAL_POINT_MIDDLE.equals(robot.getPixelColor(MIN_X, POS_Y))) {
                Utils.delay(500);
                inFight = false;
                sendNextTarget();
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

    @Override
    public void sendCommand(int code) {
        try {
            serialPort.getOutputStream().write(code);
            serialPort.getOutputStream().flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void sendNextTarget() {
        System.out.println("Next target");
        sendCommand(0);
        if (COLOR_HEAL_POINT_MIDDLE.equals(robot.getPixelColor(MAX_X, POS_Y))) {
            System.out.println("Attack!");
            sendCommand(0);
            inFight = true;
        }
    }
}
