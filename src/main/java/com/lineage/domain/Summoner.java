package com.lineage.domain;

public class Summoner implements Account {

    private Role role = Role.SUMM;
    private String windowNum;

    @Override
    public boolean support(Role role) {
        return role.equals(this.role);
    }

    public Summoner(String windowNum) {
        this.windowNum = windowNum;
    }
}
