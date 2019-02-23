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
        position = (position != Position.NULL) ? position : Position.CENTER;
        autoHelper.land();
        FindMineralRunnable.forceStop();
        autoHelper.knock(position);

        switch (position){
            case LEFT:
                driver.mecanumDriveForward(12, 0.5);
                driver.gyroTurn(20, 0.2, 3);
                driver.mecanumDriveLeft(0.5);
                while(getLeftDSensor().getDistance(DistanceUnit.INCH) > 2.1){
                    if(!opModeIsActive()) break;
                }
                driver.mecanumDriveLeft(0);
                driver.mecanumDriveForward(0.5);
                double current = getRuntime();
                while(getFrontDSensor().getDistance(DistanceUnit.INCH) > 20.0){
                    if(!opModeIsActive()) break;
                    if(getRuntime() - current > 6 ){
                        driver.mecanumDriveBackward(6, SPEED);
                        break;
                    }
                }
                driver.mecanumDriveForward(0);
                getMarker().setPosition(1);
                driver.mecanumDriveLeft(6, 1);
                driver.mecanumDriveBackward(6, 0.5);
                break;

            case CENTER:
                driver.mecanumDriveForward(0.5);
                while(getFrontDSensor().getDistance(DistanceUnit.INCH) > 30.0){
                    if(!opModeIsActive()) break;
                }
                driver.mecanumDriveForward(0);

                driver.gyroTurn(18, 0.2, 3.5);
                getMarker().setPosition(1);
                driver.mecanumDriveLeft(0.5);
                while(getLeftDSensor().getDistance(DistanceUnit.INCH) > 2){
                    if(!opModeIsActive()) break;
                }
                driver.mecanumDriveLeft(0);
                break;
            case RIGHT:
                driver.mecanumDriveForward(15, 0.5);
                driver.mecanumDriveBackward(9, 0.5);
                driver.gyroTurn(driver.getAngle(), 0.2, 3);
                driver.gyroTurn(30, 0.2, 3);
                driver.mecanumDriveForward(0.5);
                while(getFrontDSensor().getDistance(DistanceUnit.INCH) > 4){
                    if(!opModeIsActive()) break;
                }
                driver.mecanumDriveForward(0);
                driver.mecanumDriveBackward(1, 0.5);
                driver.mecanumDriveLeft(0.5);
                while(getLeftDSensor().getDistance(DistanceUnit.INCH) > 2.6){}
                driver.mecanumDriveLeft(0);
                getMarker().setPosition(1);
                sleep(250);
                driver.mecanumDriveBackward(1);
                while(getFrontDSensor().getDistance(DistanceUnit.INCH) < 17){

                }
                driver.mecanumDriveBackward(0);

                driver.mecanumDriveLeft(6, 1);
                break;
        }
        driver.mecanumDriveBackward(35, 0.9);
        driver.mecanumDriveLeft(18, 0.8);
        driver.mecanumDriveBackward(25, 0.24);
        /*
        driver.mecanumDriveBackward(9, 1);
        driver.gyroTurn(-90, 0.3, 2.5);

        driver.mecanumDriveForward(1);
        while(!(getFrontDSensor().getDistance(DistanceUnit.INCH) < 10.0)){

        }
        driver.mecanumDriveLeft(6, 1);
        sleep(500);
        driver.mecanumDriveRight(1, 1);
        driver.mecanumDriveForward(1);
        while(!(getFrontDSensor().getDistance(DistanceUnit.INCH) < 20.0)){

        }
        driver.mecanumDriveForward(0);
        getMarker().setPosition(1);
        sleep(1500);
        driver.mecanumDriveRight(1, 1);
        driver.mecanumDriveBackward(35, 1);
        */
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
