package com.lineage.domain;

public enum Key {
    KEY_ESC(177),
    KEY_F1(194),
    KEY_F2(195),
    KEY_F3(196),
    KEY_F4(197),
    KEY_F5(198);

    private final Integer keyCode;

    Key(Integer keyCode) {
        this.keyCode = keyCode;
    }

    public Integer getKeyCode() {
        return keyCode;
    }
}
