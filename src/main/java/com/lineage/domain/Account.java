package com.lineage.domain;

import javafx.scene.input.KeyCode;

public interface Account {

    Account process(Account activeWindow);

    void switchWindow();

    void sendCommand(int code);

    KeyCode getWinKeyCode();
}
