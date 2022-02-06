package com.lineage.controller;

import com.fazecast.jSerialComm.SerialPort;
import com.lineage.domain.Profile;
import com.lineage.domain.ScriptType;
import com.lineage.script.Destroyer;
import com.lineage.script.Script;
import com.lineage.script.Summoner;
import com.lineage.script.old.Fishing;
import com.lineage.script.old.Spoiler;
import com.lineage.service.ProfileService;
import com.lineage.util.Utils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.robot.Robot;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainController {

    @FXML
    private Button startBtn;

    @FXML
    private Button stopBtn;

    @FXML
    private TextField winTextField;

    @FXML
    private ComboBox<ScriptType> scriptsComboBox;

    @FXML
    private ListView<Profile> profilesListView;

    @FXML
    public TextField playerNameTextField;

    private Script script;

    private SerialPort serialPort;

    private Robot robot;

    private ProfileService profileService;

    public void initialize() {
        System.out.println("initialize");
        robot = new Robot();
        profileService = new ProfileService();

        serialPort = SerialPort.getCommPort("COM9");
        serialPort.setComPortParameters(9600, 8, 1, 0);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);

        scriptsComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(ScriptType.values())));
        scriptsComboBox.getSelectionModel().selectLast();

        List<Profile> profilesList = profileService.getProfiles();
        profilesListView.setItems(FXCollections.observableArrayList(profilesList));
        profilesListView.getSelectionModel().selectFirst();
    }

    @FXML
    private void startTask() {
        System.out.println("Start!");

        setDisable(true, winTextField, scriptsComboBox, startBtn);
        stopBtn.setDisable(false);

        Profile profile = profilesListView.getSelectionModel().getSelectedItem();

        script = createScript(scriptsComboBox.getValue(), winTextField.getText(), profile);
        System.out.println("win = " + script.getWinKeyCode() + " script = " + script.getClass().getSimpleName());

        serialPort.openPort();
        Utils.delay(500);
        new Thread(() -> script.startScript()).start();
    }

    @FXML
    private void stopTask() {
        setDisable(false, winTextField, scriptsComboBox, startBtn);
        stopBtn.setDisable(true);
        System.out.println("Stop!!");

        serialPort.closePort();
        new Thread(() -> script.stopScript()).start();
    }

    public void refreshProfile() {
        List<Profile> profilesList = profileService.getProfiles();
        profilesListView.setItems(FXCollections.observableArrayList(profilesList));
        profilesListView.getSelectionModel().selectFirst();
    }

    @FXML
    private void addProfile() throws IOException {

        Parent root = new FXMLLoader(getClass().getResource("/template/Profile.fxml")).load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("New profile");
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(scene);
        stage.showAndWait();
        refreshProfile();
    }
    @FXML
    private void editProfile() throws IOException {
        Profile profile = profilesListView.getSelectionModel().getSelectedItem();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/template/Profile.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle(profile.getName());
        stage.initModality(Modality.APPLICATION_MODAL);

        ProfileController controller = loader.getController();
        controller.initData(profile);

        stage.setScene(scene);
        stage.showAndWait();
        refreshProfile();
    }

    @FXML
    private void removeProfile() {
        Profile profile = profilesListView.getSelectionModel().getSelectedItem();
        profileService.deleteProfile(profile);
        refreshProfile();
    }

    private Script createScript(ScriptType scriptType, String winNum, Profile profile) {
        String playerName = playerNameTextField.getText();
        switch (scriptType) {
            case SUMM: return new Summoner(winNum, robot, serialPort, profile, playerName);
            case DESTR: return new Destroyer(winNum, robot, serialPort, profile, playerName);
            case SPOIL: return new Spoiler(winNum, robot, serialPort, profile);
            case FISHING: return new Fishing(winNum, robot, serialPort, profile);
            default: throw new IllegalArgumentException("Role not supported! Role = " + scriptType);
        }
    }

    private void setDisable(boolean b, Control... controls) {
        for (Control control : controls) {
            control.setDisable(b);
        }
    }
}
