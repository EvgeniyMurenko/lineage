package com.lineage;

import com.lineage.domain.Account;

import java.util.List;

public class Worker extends Thread {

    private final List<Account> accounts;
    private Account activeWindow = null;

    public Worker(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public void run() {
        System.out.println("Worker is running!");
        while (true) {
            for (Account account : accounts) {
                activeWindow = account.process(activeWindow);
            }
            Utils.delay(500);
        }
    }
}
