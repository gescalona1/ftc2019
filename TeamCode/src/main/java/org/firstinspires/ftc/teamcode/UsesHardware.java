package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;

/**
 * Created by gescalona on 10/2/18.
 */

public interface UsesHardware {

    void hardwareInit();

    DcMotor getLeftFrontDrive();
    DcMotor getRightFrontDrive();
    DcMotor getLeftBackDrive();
    DcMotor getRightBackDrive();

    BNO055IMU getImu();

    int getCameraViewId();
}