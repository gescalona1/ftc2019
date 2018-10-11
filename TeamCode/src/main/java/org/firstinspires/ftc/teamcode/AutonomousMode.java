package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.baseopmodes.AutonomousBaseOpMode;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;

/**
 * Ultro
 * AutonomousMode.java
 * Purpose: Extends from AutonomousBaseOpMode
 *
 * @version 1.0 10/11/2018
 */
@Autonomous(name = "Autonomous", group = "auto")
public class AutonomousMode extends AutonomousBaseOpMode {

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
        RobotDriver.getDriver().gyroTurn(90, 0.5);
    }
}
