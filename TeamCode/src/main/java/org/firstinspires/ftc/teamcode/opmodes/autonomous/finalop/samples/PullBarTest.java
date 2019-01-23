package org.firstinspires.ftc.teamcode.opmodes.autonomous.finalop.samples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.baseopmodes.AutonomousBaseOpMode;
import org.firstinspires.ftc.teamcode.baseopmodes.HardwareMap;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;

import java.util.List;

/**
 * Ultro
 * AutonomousMode.java
 * Purpose: Extends from AutonomousBaseOpMode
 *
 * @version 1.0 10/11/2018
 */
@Disabled
@Autonomous(name = "PULL BAR TEST", group = "auto")
public class PullBarTest extends AutonomousBaseOpMode {

    @Override
    public void runOpMode(){
        map = new HardwareMap(hardwareMap);
        telemetry.addData("Hardware Initialization:", "Not done");
        telemetry.update();
        map.motorInit();
        map.initCamera(telemetry);
        RobotDriver.getDriver().setHardwareMap(this);
        telemetry.addData("Hardware Initialization:", "Finished");
        telemetry.update();
        prerun();
        waitForStart();
        double speed = 5.5;
        RobotDriver.getDriver().extendPullDownBar(6, 0.8);
        telemetry.addLine("" + speed);
        telemetry.update();
        RobotDriver.getDriver().mecanumDriveRight(3, 0.7);
        sleep(2000);
        RobotDriver.getDriver().extendPullDownBar(-5.5, 0.8);
        RobotDriver.getDriver().setHardwareMap(null);
    }
    /*
    Before waitforStart()
     */
    @Override
    protected void prerun() {

    }
    /*
    After waitForStart()
     */
    @Override
    protected void run() {

    }
}
