package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.UsesHardware;

/**
 * Created by gescalona on 9/28/18
 */

public class RobotDriver {
    private static volatile RobotDriver driver = null;
    private UsesHardware opmode;
    private HardwareMap hardwareMap = null;

    private final double CORRECT_ANGLE_RANGE = 0.1;
    private Orientation currentAngle;

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
    public void gyroTurn(int angle){
        resetAngle();
        if(angle != 0){
            return;
        }
        final double leftfrontpower, leftbackpower, rightfrontpower, rightbackpower;
        if(angle < 0){
            leftfrontpower = 1;
            leftbackpower = 1;
            rightfrontpower = -1;
            rightbackpower = -1;
        }else if (angle > 90){
            leftfrontpower = -1;
            leftbackpower = -1;
            rightfrontpower = 1;
            rightbackpower = 1;
        }
        new Thread(new Runnable() {
            private volatile boolean running = true;
            private int requiredAngle = angle;
            @Override
            public void run() {
                while(running){
                    opmode.getLeftFrontDrive().setPower(leftfrontpower);
                    opmode.getLeftBackDrive().setPower(leftbackpower);
                    opmode.getRightFrontDrive().setPower(rightfrontpower);
                    opmode.getRightBackDrive().setPower(rightbackpower);
                    if(getRelativeAngle() > CORRECT_ANGLE_RANGE && getRelativeAngle() < CORRECT_ANGLE_RANGE){
                        running = false;
                        opmode.getLeftFrontDrive().setPower(0);
                        opmode.getLeftBackDrive().setPower(0);
                        opmode.getRightFrontDrive().setPower(0);
                        opmode.getRightBackDrive().setPower(0);
                        return;
                    }
                }
            }
        }).start();
    }

    public void resetAngle(){
        currentAngle = opmode.getImu().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
    }
    public float correctAngle(float firstAngle){
        if(firstAngle < 0){
            firstAngle += 360;
        }
        return firstAngle;
    }
    public float getCurrentAngle(){
        return currentAngle.firstAngle;
    }

    public float getAngle(boolean overwrite){
        Orientation angles = this.opmode.getImu().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        if(overwrite) currentAngle = angles;
        return angles.firstAngle;
    }

    public float getRelativeAngle(){
        /*
        In relative to the current angle
        MUST BE CORRECTED
         */
        return  correctAngle(getAngle(false) -  correctAngle(getCurrentAngle()));
    }

    /*
    End
     */
    private RobotDriver(){

    }

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

    public final void setHardwareMap(UsesHardware opMode, HardwareMap map){
        this.opmode = opMode;
        this.hardwareMap = map;
        resetAngle();
    }
}
