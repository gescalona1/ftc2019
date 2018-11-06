package org.firstinspires.ftc.teamcode.baseopmodes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;
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
        map.driverhardwareinit(telemetry);
    }

    @Override
    public final DcMotor getLeftFrontDrive() {
        return map.getLeftFrontDrive();
    }

    @Override
    public final DcMotor getRightFrontDrive() {
        return map.getRightFrontDrive();
    }

    @Override
    public final DcMotor getLeftBackDrive() {
        return map.getLeftBackDrive();
    }

    @Override
    public final DcMotor getRightBackDrive() {
        return map.getRightBackDrive();
    }


    @Override
    public final DcMotor getRightpuldaun() {
        return map.getRightpuldaun();
    }
    @Override
    public final DcMotor getLeftpuldaun() {
        return map.getLeftpuldaun();
    }

    @Override
    public final DcMotor getIntake() {
        return map.getIntake();
    }

    @Override
    public final DcMotor getLift() { return map.getLift(); }

    @Override
    public final Servo getBucket() {
        return map.getBucket();
    }

    @Override
    public final BNO055IMU getImu() {
        return map.getImu();
    }

    @Override
    public final int getCameraViewId(){
        return map.getCameraViewId();
    }

    @Override
    public final VuforiaLocalizer getVuforia() {
        return map.getVuforia();
    }

    @Override
    public final TFObjectDetector getTfod() {
        return map.getTfod();
    }

    public final String getTfodModelAsset() {
        return map.getTfodModelAsset();
    }
    public final String getLabelGoldMineral() {
        return map.getLabelGoldMineral();
    }
    public final String getLabelSilverMineral() {
        return map.getLabelSilverMineral();
    }

}
