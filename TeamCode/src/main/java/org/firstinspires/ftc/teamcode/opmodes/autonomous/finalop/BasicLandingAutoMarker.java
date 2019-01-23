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
@Autonomous(name = "BasicLandingAuto (Marker front)", group = "auto")
public class BasicLandingAutoMarker extends AutonomousBaseOpMode {
    RobotDriver driver = RobotDriver.getDriver();
    private Position position;
    /*
    Before waitforStart()
     */
    @Override
    protected void prerun() {
        getBucket().setPosition(0.5);
        getMarker().setPosition(0.7);
        resetStartTime();
    }
    /*
    After waitForStart()
     */
    @Override
    protected void run() {
        getIntake().setPower(1);
        driver.extendPullDownBar(6.1, 0.8);
        driver.mecanumDriveRight(3, 0.7);
        new Thread(() -> {
            driver.extendPullDownBar(-5, 1);
        }).start();
        driver.mecanumDriveForward(17, 1);
        driver.mecanumDriveLeft(7, 0.7);
        driver.mecanumDriveForward(3,0.7);
        driver.mecanumDriveBackward(3,0.7);
        getMarker().setPosition(0.45);
        sleep(2000);
        getMarker().setPosition(0.7);
        driver.mecanumDriveBackward(4, 0.7);
        getIntake().setPower(0);
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
