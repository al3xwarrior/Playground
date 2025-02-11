package com.al3x.housing2.Enums;

public enum HousePrivacy {
    PUBLIC("&aPUBLIC"),
    PRIVATE("&cPRIVATE"), //Eventually also add others if we add friends/party systems
    ;

    String display;
    HousePrivacy(String display) {
        this.display = display;
    }

    public String asString() {
        return display;
    }
}
