package com.lineage.controller;

import com.lineage.domain.Profile;
import com.lineage.service.ProfileService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProfileController {

    @FXML
    private TextField profileNameTextField;

    @FXML
    private TextField monHpPosXTextField;

    @FXML
    private TextField monHpPosYTextField;

    @FXML
    private TextField monHpColRTextField;

    @FXML
    private TextField monHpColJTextField;

    @FXML
    private TextField monHpColBTextField;

    private ProfileService profileService;

    public void initialize() {
        profileService = new ProfileService();
    }

    public void initData(Profile profile) {
        profileNameTextField.setText(profile.getName());
        monHpPosXTextField.setText(profile.getXCord().toString());
        monHpPosYTextField.setText(profile.getYCord().toString());

        monHpColRTextField.setText(profile.getRColor().toString());
        monHpColJTextField.setText(profile.getJColor().toString());
        monHpColBTextField.setText(profile.getBColor().toString());

        profileNameTextField.setDisable(true);
    }

    public void saveProfile(ActionEvent event) {
        try {
            Profile profile = new Profile();
            profile.setName(profileNameTextField.getText());
            profile.setXCord(Integer.parseInt(monHpPosXTextField.getText()));
            profile.setYCord(Integer.parseInt(monHpPosYTextField.getText()));

            profile.setRColor(Integer.parseInt(monHpColRTextField.getText()));
            profile.setJColor(Integer.parseInt(monHpColJTextField.getText()));
            profile.setBColor(Integer.parseInt(monHpColBTextField.getText()));

            profileService.saveProfile(profile);
        } catch (Exception ignored) {
            System.out.println("Exception ignored");
        } finally {
            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        }
    }

    public void cancelProfile(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }
}
