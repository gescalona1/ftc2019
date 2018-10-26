package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.UsesHardware;
import org.firstinspires.ftc.teamcode.util.RobotThread;
import org.firstinspires.ftc.teamcode.util.ThreadManager;

/**
 * Ultro
 * RobotDriver.java
 * Purpose: Singleton for movement for the robot (motors)
 *
 * @version 1.0 10/11/2018
 */
public class RobotDriver {
    private static volatile RobotDriver driver = null;
    private UsesHardware opmode;
    private boolean gyroTurning = false;

    private final double CORRECT_ANGLE_RANGE = 2.5;
    private Orientation currentAngle;
    /**
     * Updated mecanum drive function this year (math is ? ?? ? )
     * @param left_stick_x gamepadleftX
     * @param left_stick_y gamepadleftY
     * @param right_stick_x gamepadrightX
     */
    public void mecanumDrive(double left_stick_y, double left_stick_x, double right_stick_x){
        double LB = Range.clip(-left_stick_y - left_stick_x + right_stick_x, -1, 1);
        double LF = Range.clip(-left_stick_y + left_stick_x + right_stick_x, -1, 1);
        double RB = Range.clip(-left_stick_y + left_stick_x - right_stick_x, -1, 1);
        double RF = Range.clip(-left_stick_y - left_stick_x - right_stick_x, -1, 1);
        opmode.getLeftBackDrive().setPower(LB);
        opmode.getLeftFrontDrive().setPower(LF);
        opmode.getRightBackDrive().setPower(RB);
        opmode.getRightFrontDrive().setPower(RF);
    }
    /**
     * @deprecated
     * Old mecanum drive function from last year
     * @param leftX gamepadleftX
     * @param leftY gamepadleftY
     * @param rightX gamepadrightX
     */
    @Deprecated
    public void oldMecanumDrive(double leftX, double leftY, double rightX){
        //taken from last year's code
        // how much to amplify the power
        double r = Math.hypot(leftY, leftX);
        //calculates the angle of the joystick - 45 degrees
        double robotAngle = Math.atan2(leftY, leftX) - Math.PI / 4;
        // rotation
        final double v1 = r * Math.cos(robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;
        opmode.getLeftFrontDrive().setPower(v1);
        opmode.getRightFrontDrive().setPower(v2);
        opmode.getLeftBackDrive().setPower(v3);
        opmode.getRightBackDrive().setPower(v4);
    }
    /**
     * Turns the machine using the built in imu, uses a thread to constantly check if the correct angle is met
     * @param angle required angle to rotate [-180, 180]
     * @param power how much speed will the robot move
     */
    public void gyroTurn(int angle, double power){
        if(!gyroTurning) {
            if (angle == 0) {
                return;
            }
            if (angle > 360) {
                angle = angle - 360;
            } else if (angle > 0) {
                angle = 360 - angle;
            }
            gyroTurning = true;
            resetAngle();
            final double leftfrontpower, leftbackpower, rightfrontpower, rightbackpower;
            if (angle < 0) {
                leftfrontpower = -power;
                leftbackpower = -power;
                rightfrontpower = power;
                rightbackpower = power;
            } else if (angle > 0) {
                leftfrontpower = power;
                leftbackpower = power;
                rightfrontpower = -power;
                rightbackpower = -power;
            } else {
                leftfrontpower = 0;
                leftbackpower = 0;
                rightfrontpower = 0;
                rightbackpower = 0;
            }
            final int qangle = angle;
            Thread t = new Thread(new Runnable() {
                private volatile boolean running = true;
                private final int requiredAngle = qangle;
                @Override
                public void run() {
                    int i = 0;/*
                    opmode.getLeftFrontDrive().setPower(leftfrontpower);
                    opmode.getLeftBackDrive().setPower(leftbackpower);
                    opmode.getRightFrontDrive().setPower(rightfrontpower);
                    opmode.getRightBackDrive().setPower(rightbackpower);*/
                    while (running) {
                        if (opmode instanceof OpMode) {
                            ((OpMode) opmode).telemetry.addData("difference", getRelativeAngle());
                            ((OpMode) opmode).telemetry.addData("requiredAngle with correction", (requiredAngle - CORRECT_ANGLE_RANGE) + " to " + (requiredAngle + CORRECT_ANGLE_RANGE));
                            ((OpMode) opmode).telemetry.addData("difference boolean", (getRelativeAngle() > requiredAngle + CORRECT_ANGLE_RANGE && getRelativeAngle() < requiredAngle - CORRECT_ANGLE_RANGE));
                            ((OpMode) opmode).telemetry.addData("checked how many times:", i);
                            ((OpMode) opmode).telemetry.update();
                        }
                        if (getRelativeAngle() > requiredAngle - CORRECT_ANGLE_RANGE && getRelativeAngle() < requiredAngle + CORRECT_ANGLE_RANGE) {
                            running = false;
                            /*
                            opmode.getLeftFrontDrive().setPower(0);
                            opmode.getLeftBackDrive().setPower(0);
                            opmode.getRightFrontDrive().setPower(0);
                            opmode.getRightBackDrive().setPower(0);
                            */
                            gyroTurning = false;
                            return;
                        }
                        i++;
                    }
                }

            });
            ThreadManager.getInstance().addThread(t);
            t.start();
        }
    }
    /**
     * Reset the current angle to the current updated angle
     */
    public void resetAngle(){
        currentAngle = opmode.getImu().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
    }
    /**
     * Correct the angle by turning from the axis -180/180 axis to 0/360
     * @param firstAngle the angle returned by the Orientation class
     * @return float
     */
    public float correctAngle(float firstAngle){
        if(firstAngle < 0){
            firstAngle += 360;
        }
        return firstAngle;
    }
    public float getCurrentAngle(){
        return currentAngle.firstAngle;
    }
    /**
     * Get the updated angle
     * @param overwrite if true, overwrite the current angle
     * @return double
     */
    public float getAngle(boolean overwrite){
        Orientation angles = this.opmode.getImu().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        if(overwrite) currentAngle = angles;
        return angles.firstAngle;
    }
    /**
     * Redundant method that does not overwrite the currentAngle variable
     *
     * @return double
     */
    public float getAngle(){
        return getAngle(false);
    }
    /**
     * Get the delta of the current angle and the updated angle
     * @return double
     */
    public float getRelativeAngle(){
        /*
        In relative to the current angle
        MUST BE CORRECTED
         */
        return correctAngle(correctAngle(getAngle()) -  correctAngle(getCurrentAngle()));
    }

    public boolean IsGyroTurning(){
        return gyroTurning;
    }
    /**
     * Private constructor to prevent instantiating this utility class.
     */
    private RobotDriver(){

    }
    /**
     * Get instance of this class (singleton)
     * @return RobotDriver
     */
    public static RobotDriver getDriver(){
        if(driver == null) {
            synchronized (RobotDriver.class){
                if(driver == null){
                    driver = new RobotDriver();
                }
            }
        }
        return driver;
    }
    /**
     * Set the current opmode that is being used (useful for overwriting opmodes)
     * @param opMode The opmode that will be used
     */
    public final void setHardwareMap(UsesHardware opMode){
        /*
        THIS MUST BE DONE
         */
        this.opmode = opMode;

    }
}
