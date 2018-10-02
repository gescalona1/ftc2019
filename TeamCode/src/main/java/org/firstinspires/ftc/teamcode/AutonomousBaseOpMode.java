package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;

/**
 * Created by gescalona on 10/2/18.
 */

public class AutonomousBaseOpMode extends LinearOpMode implements UsesHardware {

    protected IntegratingGyroscope gyro;
    protected ModernRoboticsI2cGyro modernRoboticsI2cGyro;

    protected DcMotor leftFrontDrive = null;
    protected DcMotor rightFrontDrive = null;
    protected DcMotor leftBackDrive = null;
    protected DcMotor rightBackDrive = null;


    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Hardware Initialization:", "Not done");
        hardwareInit();
        telemetry.addData("Hardware Initialization:", "Finished");
        waitForStart();
    }

    @Override
    public void hardwareInit(){
        //<editor-fold desc="DcMotorSetup">
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "leftfront_drive");
        rightFrontDrive  = hardwareMap.get(DcMotor.class, "rightfront_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "leftback_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "rightback_drive");
        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        //</editor-fold>
        //<editor-fold desc="GyroConfiguration">
        modernRoboticsI2cGyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");
        gyro = (IntegratingGyroscope)modernRoboticsI2cGyro;
        modernRoboticsI2cGyro.calibrate();
        //</editor-fold>
    }
    @Override
    public DcMotor getLeftFrontDrive() {
        return leftFrontDrive;
    }

    @Override
    public DcMotor getRightFrontDrive() {
        return rightFrontDrive;
    }

    @Override
    public DcMotor getLeftBackDrive() {
        return leftBackDrive;
    }

    @Override
    public DcMotor getRightBackDrive() {
        return rightBackDrive;
    }
}
