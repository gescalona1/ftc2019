package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.baseopmodes.AutonomousBaseOpMode;
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
@Autonomous(name = "Land Sample TeamMarker", group = "auto")
public class LandSampleTeamMarker extends AutonomousBaseOpMode {
    RobotDriver driver = RobotDriver.getDriver();
    private Position position;
    private final double SPEED = 0.75d;
    private final int CHECKS = 5;
    private AutoHelper autoHelper = new AutoHelper(this);
    /*
    Before waitforStart()
     */
    @Override
    protected void prerun() {
        FindMineralRunnable.setRecordData(true);
    }
    /*
    After waitForStart()
     */
    @Override
    protected void run() {
        int i = 0;
        position = FindMineralRunnable.getCurrentPosition();
        while(true){
            if(isStopRequested()) break;
            if(i >= CHECKS) {
                FindMineralRunnable.forceStop();
                telemetry.addLine(String.format("Passed accuracy check: Position %s", position));
                break;
            }
            if(position == FindMineralRunnable.getCurrentPosition()) i++;
            else {
                run();
                return; // make sure it only runs this once
            }
            if(getRuntime() > 5){ //if 5 seconds elapsed without it being accurate, just set it
                position = FindMineralRunnable.getCurrentPosition();
                telemetry.addLine("Runtime too long, going over");
                break;
            }
        }
        autoHelper.land();
        autoHelper.knock(position);
        switch (position){
            case CENTER:
                driver.mecanumDriveForward(1);
                while(!(getFrontDSensor().getDistance(DistanceUnit.INCH) < 20.0)){
                    driver.mecanumDriveForward(0);
                }
                break;
        }
        driver.mecanumDriveBackward(9, 1);
        driver.mecanumDriveLeft(1);
        while(!(getLeftDSensor().getDistance(DistanceUnit.INCH) < 4.0)){
            driver.mecanumDriveLeft(6, 1);
        }

        sleep(500);
        driver.mecanumDriveRight(1, 1);
        driver.mecanumDriveForward(1);
        while(!(getFrontDSensor().getDistance(DistanceUnit.INCH) < 20.0)){
            driver.mecanumDriveForward(0);
        }
        getMarker().setPosition(1);
        driver.mecanumDriveRight(1, 1);
        driver.mecanumDriveBackward(35, 1);
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
