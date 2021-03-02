package com.lineage.domain;

public enum Role {
    NONE, SUMM, BD, PP, SHK;

    public static Role ofName(String name) {
        if (name == null) {
            System.out.println("Name == null");
            return NONE;
        }

        for (Role role : values()) {
            if (role.name().equalsIgnoreCase(name)) {
                return role;
            }
        }
        System.out.println("Role not found by name = " + name);
        return NONE;
    }

}
