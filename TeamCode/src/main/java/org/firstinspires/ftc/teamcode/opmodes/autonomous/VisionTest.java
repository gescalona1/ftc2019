package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.baseopmodes.AutonomousBaseOpMode;
import org.firstinspires.ftc.teamcode.baseopmodes.HardwareMap;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;
import org.firstinspires.ftc.teamcode.util.FindMineralRunnable;
import org.firstinspires.ftc.teamcode.util.Position;

/**
 * Ultro
 * AutonomousMode.java
 * Purpose: Extends from AutonomousBaseOpMode
 *
 * @version 1.0 10/11/2018
 */
@Autonomous(name = "Vision Test", group = "auto")
public class VisionTest extends AutonomousBaseOpMode {
    RobotDriver driver = RobotDriver.getDriver();
    private Position position;
    private final double SPEED = 0.75d;

    /*
    Before waitforStart()
     */
    @Override
    protected void prerun() {
        FindMineralRunnable.setRecordData(true);
        resetStartTime();
    }
    /*
    After waitForStart()
     */
    @Override
    protected void run() {
    }
}
