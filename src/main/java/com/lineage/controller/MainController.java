package com.lineage.controller;

import com.fazecast.jSerialComm.SerialPort;
import com.lineage.Worker;
import com.lineage.domain.Profile;
import com.lineage.domain.Role;
import com.lineage.script.Account;
import com.lineage.script.Fishing;
import com.lineage.script.Spoiler;
import com.lineage.script.Summoner;
import com.lineage.util.Utils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainController {

    @FXML
    private Button startBtn;

    @FXML
    private Button stopBtn;

    @FXML
    private TextField win;

    @FXML
    private ComboBox<Role> getRole;


    @FXML
    private ListView<Profile> profilesListView;

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

        getRole.setItems(FXCollections.observableArrayList(Arrays.asList(Role.values())));
        getRole.getSelectionModel().selectLast();

        List<Profile> profilesList = createProfiles();
        profilesListView.setItems(FXCollections.observableArrayList(profilesList));
        profilesListView.getSelectionModel().selectFirst();
    }

    private List<Profile> createProfiles() {
        Profile pcProfile = new Profile("pc", 899, 51, Color.rgb(111, 23, 19));
        Profile laptopProfile = new Profile("laptop", 716, 46, Color.rgb(135, 29, 24));
        Profile half = new Profile("half", 24, 146, Color.rgb(135, 28, 24));
        return List.of(pcProfile, laptopProfile, half);
    }

    @FXML
    private void startTask() {
        System.out.println("Start!");
        setDisable(true, win, getRole, startBtn);
        stopBtn.setDisable(false);

        Profile profile = profilesListView.getSelectionModel().getSelectedItem();

        if (Objects.nonNull(getRole.getValue())) createAccount(getRole.getValue(), win.getText(), profile);

        accounts.forEach(it -> System.out.println("win = " + it.getWinKeyCode() + " role = " + it.getClass().getSimpleName()));

        serialPort.openPort();

        Utils.delay(500);
        worker = new Worker(accounts);
        worker.run();
    }

    @FXML
    private void stopTask() {
        setDisable(false, win, getRole, startBtn);
        stopBtn.setDisable(true);
        System.out.println("Stop!!" + accounts.size());

        serialPort.closePort();
        worker.stop();
    }

    private void createAccount(Role role, String winNum, Profile profile) {
        Account account;
        switch (role) {
            case SUMM: account = new Summoner(winNum, robot, serialPort, profile); accounts.add(account);  break;
            case SPOIL: account = new Spoiler(winNum, robot, serialPort, profile); accounts.add(account);  break;
            case FISHING: account = new Fishing(winNum, robot, serialPort, profile); accounts.add(account);  break;
            default: System.out.println("Role not supported! Role = " + role);
        }
    }

    private void setDisable(boolean b, Control... controls) {
        for (Control control : controls) {
            control.setDisable(b);
        }
    }
}
