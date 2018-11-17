package org.firstinspires.ftc.teamcode.opmodes.autonomous.finalop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

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
@Autonomous(name = "TeamMarkerFrontAuto", group = "auto")
public class TeamMarkerFrontAuto extends AutonomousBaseOpMode {
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
        resetStartTime();
    }
    /*
    After waitForStart()
     */
    @Override
    protected void run() {
        driver.extendPullDownBar(6.2, 0.8);
        driver.mecanumDriveRight(6, 0.7);
        new Thread(() -> {
            driver.extendPullDownBar(-5, 1);
        }).start();
        driver.mecanumDriveForward(23, 0.7);
        driver.mecanumDriveRight(12, 1);
        driver.mecanumDriveBackward(2, 1);
        getMarker().setPosition(0); // ????
        new Thread(() -> {
            getMarker().setPosition(0.5); // ???
        }).start();
        driver.mecanumDriveBackward(55,1);
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
