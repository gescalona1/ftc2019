package org.firstinspires.ftc.teamcode.baseopmodes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
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
    public void runOpMode(){
        telemetry.addData("Hardware Initialization:", "Not done");
        telemetry.update();
        hardwareInit();
        RobotDriver.getDriver().setHardwareMap(this);
        RobotDriver.getDriver().resetAngle();
        telemetry.addData("Hardware Initialization:", "Finished");
        telemetry.addData("All resources", hardwareMap.appContext.getResources());
        telemetry.update();
        prerun();
        waitForStart();
        run();
        RobotDriver.getDriver().setHardwareMap(null);
        if(isStopRequested()){
            if (getTfod() != null) {
                getTfod().shutdown();
            }
        }
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
    public DcMotor getRightpuldaun() {
        return map.getRightpuldaun();
    }

    @Override
    public DcMotor getLeftpuldaun() {
        return map.getLeftpuldaun();
    }

    @Override
    public DcMotor getIntake() {
        return map.getIntake();
    }

    @Override
    public DcMotor getLift() { return map.getLift(); }

    @Override
    public Servo getBucket() {
        return map.getBucket();
    }

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
