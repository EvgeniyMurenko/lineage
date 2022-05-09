package com.lineage.script;

import javafx.scene.input.KeyCode;

public interface Script {

    void process();

    void startScript();

    void stopScript();

    void startBuff();

    KeyCode getWinKeyCode();
}
