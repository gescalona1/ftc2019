package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.robotcore.external.Consumer;
import org.firstinspires.ftc.teamcode.BaseOpMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gescalona on 9/28/18obot.
 */

public class RobotDriver {
    private static volatile RobotDriver driver = null;
    private BaseOpMode opmode;
    private HardwareMap hardwareMap = null;

    public void drive(){
        for (DcMotor motor : new DcMotor[]{opmode.getLeftBottomDrive(), opmode.getRightBottomDrive(), opmode.getLeftTopDrive(), opmode.getRightTopDrive()}) {
            motor.setPower(1);
        }
    }

    public void turn(){

    }
    public void gyroTurn(int angle){
        Thread thread = new Thread(new Runnable() {
            private volatile boolean running = true;
            private int requiredAngle = angle;
            @Override
            public void run() {
                while(running){

                }
            }
        });
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

    public void setHardwareMap(BaseOpMode opMode, HardwareMap map){
        this.opmode = opMode;
        this.hardwareMap = map;
    }
}
