package com.lineage.domain;

public class Summoner implements Account {

    private final Role role = Role.SUMM;
    private final String windowNum;

    @Override
    public boolean support(Role role) {
        return role.equals(this.role);
    }

    public Summoner(String windowNum) {
        this.windowNum = windowNum;
    }

    public Role getRole() {
        return role;
    }

    public String getWindowNum() {
        return windowNum;
    }
}
