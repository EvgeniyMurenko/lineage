package com.lineage.domain;

import javafx.scene.input.KeyCode;

public interface Account {

    Account process(Account activeWindow);

    KeyCode getWinKeyCode();
}
