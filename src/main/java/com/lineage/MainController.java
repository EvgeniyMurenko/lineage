package com.lineage;

import com.fazecast.jSerialComm.SerialPort;
import com.lineage.domain.Account;
import com.lineage.domain.Role;
import com.lineage.domain.Summoner;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
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
    private ComboBox getRole1;

    @FXML
    private ComboBox getRole2;

    @FXML
    private ComboBox getRole3;

    private List<Account> accounts;

    private SerialPort serialPort;

    public void initialize() {
        accounts = new ArrayList<>();
        System.out.println("initialize");

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

        win1.setDisable(true);
        win2.setDisable(true);
        win3.setDisable(true);

        getRole1.setDisable(true);
        getRole2.setDisable(true);
        getRole3.setDisable(true);

        start.setDisable(true);
        stop.setDisable(false);

        if (Objects.nonNull(getRole1.getValue())) createAccount(Role.ofName(getRole1.getValue().toString()), win1.getText());
        if (Objects.nonNull(getRole2.getValue())) createAccount(Role.ofName(getRole2.getValue().toString()), win2.getText());
        if (Objects.nonNull(getRole3.getValue())) createAccount(Role.ofName(getRole3.getValue().toString()), win3.getText());

        serialPort.openPort();
        sendCommand(49); // 1
    }

    @FXML
    private void stopTask() {
        win1.setDisable(false);
        win2.setDisable(false);
        win3.setDisable(false);

        getRole1.setDisable(false);
        getRole2.setDisable(false);
        getRole3.setDisable(false);

        start.setDisable(false);
        stop.setDisable(true);
        System.out.println("Stop!!" + accounts.size());

        sendCommand(48); // 2
        serialPort.closePort();
    }

    private void createAccount(Role role, String winNum) {
        Account account;
        switch (role) {
            case SUMM: account = new Summoner(winNum); accounts.add(account); break;
            default: System.out.println("Role not supported! Role = "+ role);
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
