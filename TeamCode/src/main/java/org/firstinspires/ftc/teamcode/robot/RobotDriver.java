package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.UsesHardware;

/**
 * Created by gescalona on 9/28/18obot.
 */

public class RobotDriver {
    private static volatile RobotDriver driver = null;
    private UsesHardware opmode;
    private HardwareMap hardwareMap = null;

    private Orientation currentAngle;
    public void mecanumDrive(double leftX, double leftY, double rightX){
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
        if(angle != 0){
            return;
        }
        boolean turnRight = true;
        if(angle < 0){
            turnRight = false;
        }
        Thread thread = new Thread(new Runnable() {
            private volatile boolean running = true;
            private int requiredAngle = angle;
            @Override
            public void run() {
                if(running){

                }
            }
        });
        thread.start();
    }

    public float getCurrentAngle(){
        return currentAngle.firstAngle;
    }

    public float getAngle(boolean overwrite){
        Orientation angles = this.opmode.getImu().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        if(overwrite) currentAngle = angles;
        return angles.firstAngle;
    }

    public float getCorrectedAngle(float angle){

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
    }
}
