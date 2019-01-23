package org.firstinspires.ftc.teamcode.opmodes.autonomous.finalop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.baseopmodes.AutonomousBaseOpMode;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;
import org.firstinspires.ftc.teamcode.util.Position;

/**
 * Ultro
 * AutonomousMode.java
 * Purpose: Extends from AutonomousBaseOpMode
 *
 * @version 1.0 10/11/2018
 */
@Autonomous(name = "TeamMarkerFront", group = "auto")
public class TeamMarkerFrontAuto extends AutonomousBaseOpMode {
    RobotDriver driver = RobotDriver.getDriver();
    private Position position;
    /*
    Before waitforStart()
     */
    @Override
    protected void prerun() {
        getBucket().setPosition(0.5);
        getMarker().setPosition(0.7);
        telemetry.addLine("Finding Orientation of the gold mineral");
        telemetry.update();
        resetStartTime();
    }
    /*
    After waitForStart()
     */
    @Override
    protected void run() {
        driver.extendPullDownBar(6.2, 0.7);
        driver.mecanumDriveRight(4, 0.7);
        getIntake().setPower(0.9);
        new Thread(() -> {
            driver.extendPullDownBar(-5, 1);
        }).start();
        driver.mecanumDriveForward(9, 0.7);
        //driver.gyro
        //                  ```````Turn(-15, 0.45);
        sleep(250);
        driver.mecanumDriveRight(22, 0.7);
        sleep(250);
        driver.mecanumDriveForward(20, 0.7);
        sleep(250);
        driver.mecanumDriveBackward(2, 0.7);
        driver.mecanumDriveRight(4, 0.7);
        getIntake().setPower(0);
        sleep(250);
        getMarker().setPosition(0.45); // ????
        sleep(2000);
        getMarker().setPosition(0.7);
        driver.mecanumDriveBackward(35,1);
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
