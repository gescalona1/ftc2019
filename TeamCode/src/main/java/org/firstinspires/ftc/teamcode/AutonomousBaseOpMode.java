package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;

/**
 * Created by gescalona on 10/2/18.
 */

public abstract class AutonomousBaseOpMode extends LinearOpMode implements UsesHardware {

    protected int cameraViewId;


    protected BNO055IMU imu;

    protected DcMotor leftFrontDrive = null;
    protected DcMotor rightFrontDrive = null;
    protected DcMotor leftBackDrive = null;
    protected DcMotor rightBackDrive = null;


    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Hardware Initialization:", "Not done");
        telemetry.update();
        hardwareInit();
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
        cameraViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
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
        //<editor-fold desc="ImuConfiguration">
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = true;

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        //</editor-fold>
        while (!isStopRequested() && !imu.isGyroCalibrated())
        {
            sleep(50);
            idle();
        }

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

    @Override
    public BNO055IMU getImu() {
        return imu;
    }

    @Override
    public int getCameraViewId(){
        return cameraViewId;
    }
}
