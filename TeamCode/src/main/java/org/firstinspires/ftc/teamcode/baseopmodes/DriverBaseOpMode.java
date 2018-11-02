package org.firstinspires.ftc.teamcode.baseopmodes;

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
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.UsesHardware;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;

/**
 * Ultro
 * DriverBaseOpMode.java
 * Purpose: Base OP mode for all driver classes
 *
 * @version 1.0 10/11/2018
 */

public abstract class DriverBaseOpMode extends OpMode implements UsesHardware {
    protected ElapsedTime runtime = new ElapsedTime();
    protected org.firstinspires.ftc.teamcode.baseopmodes.HardwareMap map;


    @Override
    public void init() {
        hardwareInit();
        RobotDriver.getDriver().setHardwareMap(this);
        RobotDriver.getDriver().resetAngle();
        inita();
        initb();
    }

    private void inita(){
        this.runtime.reset();
        telemetry.addData("Status", "Initializing");
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    public abstract void initb();

    @Override
    public void hardwareInit(){
        map = new org.firstinspires.ftc.teamcode.baseopmodes.HardwareMap(hardwareMap);
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
    public DcMotor getLift() { return map.getLift(); }

    @Override
    public BNO055IMU getImu() {
        return map.getImu();
    }

    @Override
    public int getCameraViewId(){
        return map.getCameraViewId();
    }

    @Override
    public VuforiaLocalizer getVuforia() {
        return map.getVuforia();
    }

    @Override
    public TFObjectDetector getTfod() {
        return map.getTfod();
    }

    public String getTfodModelAsset() {
        return map.getTfodModelAsset();
    }
    public String getLabelGoldMineral() {
        return map.getLabelGoldMineral();
    }
    public String getLabelSilverMineral() {
        return map.getLabelSilverMineral();
    }

}
