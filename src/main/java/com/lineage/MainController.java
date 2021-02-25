package com.lineage;

import com.fazecast.jSerialComm.SerialPort;
import com.lineage.domain.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.robot.Robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainController {

    @FXML
    private Button start;

    @FXML
    private Button stop;

    @FXML
    private TextField win1;

    @FXML
    private TextField win2;

    @FXML
    private TextField win3;

    @FXML
    private ComboBox<String> getRole1;

    @FXML
    private ComboBox<String> getRole2;

    @FXML
    private ComboBox<String> getRole3;

    private List<Account> accounts;

    private SerialPort serialPort;

    private Robot robot;

    private Worker worker;

    public void initialize() {
        System.out.println("initialize");
        accounts = new ArrayList<>();
        robot = new Robot();
        serialPort = SerialPort.getCommPort("COM7");
        serialPort.setComPortParameters(9600, 8, 1, 0);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
    }

    @FXML
    private void startTask() {
        System.out.println("Start!");
        System.out.println("win1 = " + win1.getText() + " role1 = " + getRole1.getValue());
        System.out.println("win2 = " + win2.getText() + " role2 = " + getRole2.getValue());
        System.out.println("win3 = " + win3.getText() + " role3 = " + getRole3.getValue());

        setDisable(true, win1, win2, win3, getRole1, getRole2, getRole3, start);
        stop.setDisable(false);

        if (Objects.nonNull(getRole1.getValue())) createAccount(Role.ofName(getRole1.getValue()), win1.getText());
        if (Objects.nonNull(getRole2.getValue())) createAccount(Role.ofName(getRole2.getValue()), win2.getText());
        if (Objects.nonNull(getRole3.getValue())) createAccount(Role.ofName(getRole3.getValue()), win3.getText());

        serialPort.openPort();
        sendCommand(49); // 1

        Utils.delay(2000);
        worker = new Worker(accounts);
        worker.run();
    }

    @FXML
    private void stopTask() {
        setDisable(false, win1, win2, win3, getRole1, getRole2, getRole3, start);
        stop.setDisable(true);
        System.out.println("Stop!!" + accounts.size());

        sendCommand(48); // 2
        serialPort.closePort();

        worker.stop();
    }

    private void createAccount(Role role, String winNum) {
        Account account;
        switch (role) {
            case SUMM: account = new Summoner(winNum, robot, serialPort); accounts.add(account);  break;
            case BD: account = new Bladedancer(winNum, robot, serialPort); accounts.add(account);  break;
            case PP: account = new Prophet(winNum, robot, serialPort); accounts.add(account);  break;
            default:
                System.out.println("Role not supported! Role = " + role);
        }
    }

    private void setDisable(boolean b, Control... controls) {
        for (Control control : controls) {
            control.setDisable(b);
        }
    }

    private void sendCommand(int code) {
        try {
            serialPort.getOutputStream().write(code);
            serialPort.getOutputStream().flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
