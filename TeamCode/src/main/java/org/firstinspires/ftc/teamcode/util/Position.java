package org.firstinspires.ftc.teamcode.util;

/**
 * Created by gescalona on 11/9/18.
 */

public enum Position {
    LEFT, CENTER, RIGHT;

    @Override
    public String toString() {
        switch (this){
            case LEFT:
                return "LEFT";
            case CENTER:
                return "CENTER";
            case RIGHT:
                return "RIGHT";
        }
        return null;
    }
}
