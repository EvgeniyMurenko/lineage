package com.lineage;

import com.fazecast.jSerialComm.SerialPort;

public class Utils {

    public static void delay(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sendCommand(SerialPort serialPort, int code) {
        try {
            Utils.delay(500);
            serialPort.getOutputStream().write(code);
            serialPort.getOutputStream().flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
