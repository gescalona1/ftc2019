package org.firstinspires.ftc.teamcode.baseopmodes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;

import org.firstinspires.ftc.teamcode.UsesHardware;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;

/**
 * Ultro
 * AutonomousBaseOpMode.java
 * Purpose: Base OP mode for all autonomous classes
 *
 * @version 1.0 10/11/2018
 */
public abstract class AutonomousBaseOpMode extends LinearOpMode implements UsesHardware {
    protected HardwareMap map;
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Hardware Initialization:", "Not done");
        telemetry.update();
        hardwareInit();
        RobotDriver.getDriver().setHardwareMap(this);
        RobotDriver.getDriver().resetAngle();
        telemetry.addData("Hardware Initialization:", "Finished");
        telemetry.update();
        prerun();
        waitForStart();
        run();
    }
    protected abstract void prerun();
    protected abstract void run();

    @Override
    public void hardwareInit(){
        map = new HardwareMap(hardwareMap);
        map.hardwareInit(telemetry);
    }

    @Override
    public DcMotor getLeftFrontDrive() {
        return map.getLeftFrontDrive();
    }

    @Override
    public DcMotor getRightFrontDrive() {
        return map.getRightFrontDrive();
    }

    @Override
    public DcMotor getLeftBackDrive() {
        return map.getLeftBackDrive();
    }

    @Override
    public DcMotor getRightBackDrive() {
        return map.getRightBackDrive();
    }

    @Override
    public BNO055IMU getImu() {
        return map.getImu();
    }

    @Override
    public int getCameraViewId(){
        return map.getCameraViewId();
    }
}
