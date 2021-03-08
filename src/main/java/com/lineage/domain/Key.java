package com.lineage.domain;

public enum Key {
    KEY_ESC(177),
    KEY_F1(194),
    KEY_F2(195),
    KEY_F3(196),
    KEY_F4(197),
    KEY_F5(198),
    KEY_F6(199),
    KEY_F7(200),
    KEY_F8(201),
    KEY_F9(202);

    private final Integer keyCode;

    Key(Integer keyCode) {
        this.keyCode = keyCode;
    }

    public Integer getKeyCode() {
        return keyCode;
    }
}
