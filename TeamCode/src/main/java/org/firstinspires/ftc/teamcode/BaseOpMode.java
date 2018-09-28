package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;

/**
 * Created by gescalona on 9/28/18.
 */

public abstract class BaseOpMode extends OpMode {
    protected ElapsedTime runtime = new ElapsedTime();

    protected DcMotor leftTopDrive = null;
    protected DcMotor leftBottomDrive = null;
    protected DcMotor rightTopDrive = null;
    protected DcMotor rightBottomDrive = null;


    @Override
    public void init() {
        RobotDriver.getDriver().setHardwareMap(this, hardwareMap);
        inita();
        initb();
    }

    private void inita(){
        this.runtime.reset();
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftTopDrive  = hardwareMap.get(DcMotor.class, "lefttop_drive");
        leftBottomDrive = hardwareMap.get(DcMotor.class, "leftbottom_drive");
        rightTopDrive  = hardwareMap.get(DcMotor.class, "righttop_drive");
        rightBottomDrive = hardwareMap.get(DcMotor.class, "rightbottom_drive");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftTopDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBottomDrive.setDirection(DcMotor.Direction.FORWARD);
        rightTopDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBottomDrive.setDirection(DcMotor.Direction.FORWARD);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

    }

    abstract void initb();

    public DcMotor getLeftTopDrive() {
        return leftTopDrive;
    }

    public DcMotor getLeftBottomDrive() {
        return leftBottomDrive;
    }

    public DcMotor getRightTopDrive() {
        return rightTopDrive;
    }

    public DcMotor getRightBottomDrive() {
        return rightBottomDrive;
    }

}
