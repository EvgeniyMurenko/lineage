package com.lineage.domain;

import com.fazecast.jSerialComm.SerialPort;
import com.lineage.Utils;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;

import java.util.List;


public class Fishing implements Account {

    private final KeyCode winKeyCode;
    private final Robot robot;
    private final SerialPort serialPort;
    private final Color COLOR_HEALS_POINT;
    private final int POS_X;
    private final int POS_Y;

    private final List<Color> FISHING_PANE_COLOR = List.of(Color.rgb(36, 30, 21),
            Color.rgb(38, 31, 24));
    private final int FISHING_PANE_POS_X = 370;
    private final int FISHING_PANE_POS_Y = 143;

    private final int COR_X = 329;
    private final int COR_Y = 378;

    private boolean inFight = false;
    private final PictureAnalytic pictureAnalytic;
    private FishStatus fishStatus = FishStatus.STOP;

    private final List<Color> PROCESS_BAR_COLOR_TONE = List.of(
            Color.rgb(0, 103, 159),
            Color.rgb(0, 104, 159),
            Color.rgb(0, 105, 159),
            Color.rgb(32, 107, 151)
    );

    public Fishing(String windowNum, Robot robot, SerialPort serialPort, Profile profile) {
        this.winKeyCode = KeyCode.getKeyCode(windowNum);
        this.robot = robot;
        this.serialPort = serialPort;
        this.COLOR_HEALS_POINT = profile.getColor();
        this.POS_X = profile.getxCord();
        this.POS_Y = profile.getyCord();
        pictureAnalytic = new PictureAnalytic();
    }

    @Override
    public Account process(Account activeWindow) {

        activeWindow = checkAndSetActiveWindow(activeWindow);

        if (!inFight && FishStatus.STOP.equals(fishStatus)) {
            // start fishing
            Utils.sendCommand(serialPort, Key.KEY_F1);
            fishStatus = FishStatus.START;
        }

        if (FishStatus.START.equals(fishStatus)) {
            // check line color
            Color pixelColor = robot.getPixelColor(COR_X, COR_Y);
            if (PROCESS_BAR_COLOR_TONE.contains(pixelColor)) {
                fishStatus = FishStatus.IN_PROCESS;
            }
        }

        if (FishStatus.IN_PROCESS.equals(fishStatus)) {
            if (pictureAnalytic.checkPicture()) {
                Utils.sendCommand(serialPort, Key.KEY_F2);
            } else {
                Utils.sendCommand(serialPort, Key.KEY_F3);
            }

            if (!fishingIconChange()) {
                Utils.delay(2000);
            }
        }

        if (FishStatus.END.equals(fishStatus)) {
            sendNextTarget();
            fishStatus = FishStatus.STOP;
        }

        if (inFight) {
            Utils.sendCommand(serialPort, Key.KEY_F7);
            if (!checkMonsterHealPoints()) {
                Utils.delay(500);
                inFight = false;
                pickUpLoot();
                Utils.sendCommand(serialPort, Key.KEY_ESC);
            }
        }

        return activeWindow;
    }

    private boolean fishingIconChange() {
        if (!FISHING_PANE_COLOR.contains(robot.getPixelColor(FISHING_PANE_POS_X, FISHING_PANE_POS_Y))) {
            fishStatus = FishStatus.END;
            return true;
        }
        return false;
    }

    private Account checkAndSetActiveWindow(Account activeWindow) {
        if (activeWindow == null || !activeWindow.equals(this)) {
            Utils.switchWindow(robot, winKeyCode);
            activeWindow = this;
        }
        return activeWindow;
    }

    private void sendNextTarget() {
        System.out.println("Next target");
        Utils.sendCommand(serialPort, Key.KEY_F6); // next target

        if (checkMonsterHealPoints()) {
            System.out.println("Attack!");
            Utils.sendCommand(serialPort, Key.KEY_F7); // attack sum + per
            inFight = true;
        }
    }

    private boolean checkMonsterHealPoints() {
        return COLOR_HEALS_POINT.equals(robot.getPixelColor(POS_X, POS_Y));
    }

    private void pickUpLoot() {
        for (int i = 0; i < 5; i++) {
            Utils.delay(100);
            Utils.sendCommand(serialPort, Key.KEY_F4); // pickUp
        }
    }

    public KeyCode getWinKeyCode() {
        return winKeyCode;
    }
}
