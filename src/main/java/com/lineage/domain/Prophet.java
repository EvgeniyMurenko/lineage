package com.lineage.domain;

import com.fazecast.jSerialComm.SerialPort;
import com.lineage.Utils;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;

import java.time.LocalTime;

public class Prophet implements Account {

    private final KeyCode winKeyCode;
    private final Robot robot;
    private final SerialPort serialPort;

    private static final int MAX_DELAY_SEC = 60;

    private LocalTime buffTime = LocalTime.of(0, 18, 30);
    private LocalTime lastBuff;

    public Prophet(String windowNum, Robot robot, SerialPort serialPort) {
        this.winKeyCode = KeyCode.getKeyCode(windowNum);
        this.robot = robot;
        this.serialPort = serialPort;
    }

    @Override
    public Account process(Account activeWindow) {
        if (lastBuff == null || LocalTime.now().isAfter(lastBuff.plusMinutes(buffTime.getMinute()).plusSeconds(buffTime.getSecond()))) {
            if (activeWindow == null || !activeWindow.equals(this)) {
                switchWindow();
                activeWindow = this;
            }
            Utils.sendCommand(serialPort, 0); // need send some code of button
            lastBuff = LocalTime.now().plusSeconds(getRandomSeconds());
            System.out.println("Send command to press buff! time = " + lastBuff);
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

    private int getRandomSeconds() {
        return 1 + (int) (Math.random() * MAX_DELAY_SEC);
    }

    public KeyCode getWinKeyCode() {
        return winKeyCode;
    }
}
