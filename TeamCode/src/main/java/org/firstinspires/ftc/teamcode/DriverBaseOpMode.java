package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;

/**
 * Created by gescalona on 9/28/18.
 */

public abstract class DriverBaseOpMode extends OpMode implements UsesHardware {
    protected ElapsedTime runtime = new ElapsedTime();

    protected int cameraViewId;

    protected BNO055IMU imu;

    protected DcMotor leftFrontDrive = null;
    protected DcMotor rightFrontDrive = null;
    protected DcMotor leftBackDrive = null;
    protected DcMotor rightBackDrive = null;


    @Override
    public void init() {
        RobotDriver.getDriver().setHardwareMap(this, hardwareMap);
        inita();
        initb();
    }

    private void inita(){
        this.runtime.reset();
        telemetry.addData("Status", "Initialized");
        hardwareInit();
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    abstract void initb();

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
        //<editor-fold desc="GyroConfiguration">
        while (!imu.isGyroCalibrated()) {
            try {
                wait(50);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
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

    @Override
    public BNO055IMU getImu(){
        return imu;
    }

    @Override
    public int getCameraViewId(){
        return cameraViewId;
    }

}
