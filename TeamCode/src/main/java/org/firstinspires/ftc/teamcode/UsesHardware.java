package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

/**
 * Created by gescalona on 10/2/18.
 */

public interface UsesHardware {

    void hardwareInit();

    DcMotor getLeftFrontDrive();
    DcMotor getRightFrontDrive();
    DcMotor getLeftBackDrive();
    DcMotor getRightBackDrive();
    DcMotor getLift();

    BNO055IMU getImu();

    int getCameraViewId();
    VuforiaLocalizer getVuforia();
    TFObjectDetector getTfod();

    String getTfodModelAsset();
    String getLabelGoldMineral();
    String getLabelSilverMineral();
}