package org.firstinspires.ftc.teamcode.opmodes.autonomous;

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
@Autonomous(name = "Main Autonomous without pullbar", group = "auto")
public class AutonomousWithoutPullbarMode extends AutonomousBaseOpMode {
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
        while(!isStarted()){
            if (getTfod() != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = getTfod().getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    if (updatedRecognitions.size() == 3) {
                        int goldMineralX = -1;
                        int silverMineral1X = -1;
                        int silverMineral2X = -1;
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(getLabelGoldMineral())) {
                                goldMineralX = (int) recognition.getLeft();
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getLeft();
                            } else {
                                silverMineral2X = (int) recognition.getLeft();
                            }
                        }
                        Position currentPosition;
                        if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                            if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                                //code for driving left
                                currentPosition = Position.LEFT;
                            } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                //code for driving right
                                currentPosition = Position.RIGHT;
                            } else {
                                currentPosition = Position.CENTER;
                                //code for driving center
                            }
                            telemetry.addData("Gold Mineral Position", currentPosition.toString());
                            break;
                        }
                    }
                    telemetry.update();
                }
            }
        }
    }
    /*
    After waitForStart()
     */
    @Override
    protected void run() {
        new Thread(() -> {
                while(opModeIsActive()){
                    if (getTfod() != null) {
                        getTfod().shutdown();
                    }
                }
        }).start();
        while (opModeIsActive()) {
            if (getTfod() != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = getTfod().getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    if (updatedRecognitions.size() == 3) {
                        int goldMineralX = -1;
                        int silverMineral1X = -1;
                        int silverMineral2X = -1;
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(getLabelGoldMineral())) {
                                goldMineralX = (int) recognition.getLeft();
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getLeft();
                            } else {
                                silverMineral2X = (int) recognition.getLeft();
                            }
                        }
                        if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                            if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                                //code for driving left
                                position = Position.LEFT;
                            } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                //code for driving right
                                position = Position.RIGHT;
                            } else {
                                position = Position.CENTER;
                                //code for driving center
                            }
                            telemetry.addData("Gold Mineral Position", position.toString());
                            break;
                        }
                    }
                    telemetry.update();
                }
            }
        }
        driver.mecanumDriveForward(0.25);
        sleep(750);
        driver.mecanumDriveForward(0);
        switch(this.position){
            case LEFT:
                telemetry.addLine("Going LEFT");
                driver.mecanumDriveLeft(6, 0.7);
                sleep(500);
                driver.gyroTurn(15, 0.5);
            case RIGHT:
                telemetry.addLine("Going RIGHT");
                driver.mecanumDriveRight(6, 0.7);
                sleep(500);
                driver.gyroTurn(-15, 0.5);
            case CENTER:
                telemetry.addLine("Going CENTER");
                sleep(500);
        }
        telemetry.update();
        driver.mecanumDriveForward(12, 0.5);
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