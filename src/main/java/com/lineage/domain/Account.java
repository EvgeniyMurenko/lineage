package com.lineage.domain;

public interface Account {

    Account process(Account activeWindow);

    void switchWindow();

    void sendCommand(int code);
}
