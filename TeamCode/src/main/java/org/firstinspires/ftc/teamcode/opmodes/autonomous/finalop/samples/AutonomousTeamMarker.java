package org.firstinspires.ftc.teamcode.opmodes.autonomous.finalop.samples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.baseopmodes.AutonomousBaseOpMode;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;
import org.firstinspires.ftc.teamcode.util.Position;

import java.util.List;

/**
 * Ultro
 * AutonomousMode.java
 * Purpose: Extends from AutonomousBaseOpMode
 *
 * @version 1.0 10/11/2018
 */
@Disabled
@Autonomous(name = "Main Autonomous with Teammark", group = "auto")
public class AutonomousTeamMarker extends AutonomousBaseOpMode {
    RobotDriver driver = RobotDriver.getDriver();
    private Position position;
    /*
    Before waitforStart()
     */
    @Override
    protected void prerun() {
        telemetry.addLine("Finding Orientation of the gold mineral");
        telemetry.update();
        getBucket().setPosition(0.5);
        if (getTfod() != null) {
            getTfod().activate();
        }
        resetStartTime();
    }
    /*
    After waitForStart()
     */
    @Override
    protected void run() {
        driver.mecanumDriveForward(3, 0.5);
        driver.mecanumDriveForward(-3, 0.5);
        telemetry.addData("Elapsed Time", this.getRuntime());
        telemetry.update();
        driver.mecanumDriveLeft(18, 1);
        sleep(3000);
        driver.gyroTurn(180,0.45);
        driver.gyroTurn(40,0.45);
        driver.mecanumDriveRight(4, 1);
        driver.mecanumDriveForward(16, 1);
        sleep(3000);
        driver.mecanumDriveBackward(20,1);
        /*
        if (opModeIsActive()) {

            driver.mechanumDriveForward(1);
            sleep(5 * 1000);
            driver.mechanumDriveForward(0);
            if (getTfod() != null) {
                getTfod().shutdown();
            }
        }
        */
    }
}
