package com.lineage.domain;

public enum Key {
    KEY_ESC(177),
    KEY_F1(194), // некст таргет
    KEY_F2(195), // макрос с таргетом
    KEY_F3(196), // атака
    KEY_F4(197), // пикап
    KEY_F5(198), // бафф скрипт
    KEY_F6(199), // пет професси
    KEY_F7(200), // хеал пета
    KEY_F8(201), // перевызов пета
    KEY_F9(202); // пет соски

    private final Integer keyCode;

    Key(Integer keyCode) {
        this.keyCode = keyCode;
    }

    public Integer getKeyCode() {
        return keyCode;
    }
}
