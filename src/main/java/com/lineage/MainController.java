package com.lineage;

import com.fazecast.jSerialComm.SerialPort;
import com.lineage.domain.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.robot.Robot;

import java.util.ArrayList;
import java.util.Arrays;
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
    private ComboBox<Role> getRole1;

    @FXML
    private ComboBox<Role> getRole2;

    @FXML
    private ComboBox<Role> getRole3;

    private List<Account> accounts;

    private SerialPort serialPort;

    private Robot robot;

    private Worker worker;

    public void initialize() {
        System.out.println("initialize");
        accounts = new ArrayList<>();
        robot = new Robot();
        serialPort = SerialPort.getCommPort("COM9");
        serialPort.setComPortParameters(9600, 8, 1, 0);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);

        getRole1.setItems(FXCollections.observableArrayList(Arrays.asList(Role.values())));
        getRole1.getSelectionModel().selectFirst();
        getRole2.setItems(FXCollections.observableArrayList(Arrays.asList(Role.values())));
        getRole2.getSelectionModel().selectFirst();
        getRole3.setItems(FXCollections.observableArrayList(Arrays.asList(Role.values())));
        getRole3.getSelectionModel().selectFirst();
    }

    @FXML
    private void startTask() {
        System.out.println("Start!");
        setDisable(true, win1, win2, win3, getRole1, getRole2, getRole3, start);
        stop.setDisable(false);

        if (Objects.nonNull(getRole1.getValue())) createAccount(getRole1.getValue(), win1.getText());
        if (Objects.nonNull(getRole2.getValue())) createAccount(getRole2.getValue(), win2.getText());
        if (Objects.nonNull(getRole3.getValue())) createAccount(getRole3.getValue(), win3.getText());

        accounts.forEach(it -> System.out.println("win = " + it.getWinKeyCode() + " role = " + it.getClass().getSimpleName()));

        serialPort.openPort();

        Utils.delay(500);
        worker = new Worker(accounts);
        worker.run();
    }

    @FXML
    private void stopTask() {
        setDisable(false, win1, win2, win3, getRole1, getRole2, getRole3, start);
        stop.setDisable(true);
        System.out.println("Stop!!" + accounts.size());

//        sendCommand(48); // 2
        serialPort.closePort();
        worker.stop();
    }

    private void createAccount(Role role, String winNum) {
        Account account;
        switch (role) {
            case SUMM: account = new Summoner(winNum, robot, serialPort); accounts.add(account);  break;
            case BD: account = new Bladedancer(winNum, robot, serialPort); accounts.add(account);  break;
            case PP: account = new Prophet(winNum, robot, serialPort); accounts.add(account);  break;
            case SHK: account = new ShillienKnight(winNum, robot, serialPort); accounts.add(account);  break;
            default: System.out.println("Role not supported! Role = " + role);
        }
    }

    private void setDisable(boolean b, Control... controls) {
        for (Control control : controls) {
            control.setDisable(b);
        }
    }
}
