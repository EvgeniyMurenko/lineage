package com.lineage;

import com.fazecast.jSerialComm.SerialPort;
import com.lineage.domain.Key;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;

public class Utils {

    public static void delay(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sendCommand(SerialPort serialPort, Key key) {
        try {
            serialPort.getOutputStream().write(key.getKeyCode());
            serialPort.getOutputStream().flush();
            delay(300);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void switchWindow(Robot robot, KeyCode winKeyCode) {
        delay(500);
        robot.keyPress(KeyCode.WINDOWS);
        robot.keyPress(winKeyCode);
        robot.keyRelease(winKeyCode);
        robot.keyRelease(KeyCode.WINDOWS);
        delay(500);
    }

    public static int getRandomSeconds(Integer maxDelay) {
        return 1 + (int)  (Math.random() * maxDelay);
    }
}
