package org.firstinspires.ftc.teamcode.util;

/**
 * Created by gescalona on 11/9/18.
 */

public enum Position {
    LEFT("LEFT"), CENTER("CENTER"), RIGHT("RIGHT");

    private String name;
    Position(String name){
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
