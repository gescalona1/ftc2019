package org.firstinspires.ftc.teamcode.opmodes.autonomous.finalop.samples;

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
@Autonomous(name = "Sample TURN AUTO", group = "auto")
public class SAMPLEAUTOTurn extends AutonomousBaseOpMode {
    RobotDriver driver = RobotDriver.getDriver();
    private Position position;
    /*
    Before waitforStart()
     */
    @Override
    protected void prerun() {
        getMarker().setPosition(0.7);
        resetStartTime();
        while(!isStarted()){
            telemetry.addData("getAngle", driver.getAngle());
            telemetry.update();
        }
    }
    /*
    After waitForStart()
     */
    @Override
    protected void run() {
        driver.mecanumDriveLeft(10,1);
    }
}
