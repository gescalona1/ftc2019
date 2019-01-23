package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;

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
<<<<<<< HEAD
    DcMotor getRotate();
    DcMotor getExtend();

    DcMotor getRightpuldaun();
    DcMotor getLeftpuldaun();

    Servo getIntake();
=======
    DcMotor getLift();
    DcMotor getIntake();
>>>>>>> 682450fc4cab78de012be13d76cce6d6eb04e7da

    DcMotor getRightpuldaun();
    DcMotor getLeftpuldaun();

    Servo getBucket();
    Servo getMarker();
    BNO055IMU getImu();

    int getCameraViewId();
    VuforiaLocalizer getVuforia();
    TFObjectDetector getTfod();

    String getTfodModelAsset();
    String getLabelGoldMineral();
    String getLabelSilverMineral();

    boolean playRick();
    boolean playUSSR();
}