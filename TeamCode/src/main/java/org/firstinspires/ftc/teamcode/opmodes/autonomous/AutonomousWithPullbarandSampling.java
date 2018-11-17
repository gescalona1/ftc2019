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
@Autonomous(name = "Main Autonomous with pullbar and sampling", group = "auto")
public class AutonomousWithPullbarandSampling extends AutonomousBaseOpMode {
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
                    telemetry.addLine("Ready, press init!");
                    telemetry.update();
                }
            }
        }
        resetStartTime();
    }
    /*
    After waitForStart()
     */
    @Override
    protected void run() {
        Position lastPos;
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
                            lastPos = position;
                            if(lastPos != null && position == lastPos) {
                                telemetry.addLine("Accurate.");
                                telemetry.update();
                                break;
                            }
                        }
                    }
                    if(this.getRuntime() >= 7){
                        telemetry.addLine("Breaking");
                        int randomInt = (int) Math.floor(Math.random() * 3);
                        switch(randomInt) {
                            case 0:
                                position = Position.LEFT;
                            case 1:
                                position = Position.CENTER;
                            case 2:
                                position = Position.RIGHT;
                        }
                        break;
                    }
                    telemetry.update();
                }
            }
        }
        driver.extendPullDownBar(6.1, 0.8);
        driver.mecanumDriveRight(3, 0.7);
        new Thread(() -> {
            driver.extendPullDownBar(-5, 1);
            if (getTfod() != null) {
                getTfod().shutdown();
            }
        }).start();
        driver.mecanumDriveForward(2, 0.5);
        switch(this.position){
            case LEFT:
                telemetry.addLine("Going LEFT");
                driver.mecanumDriveLeft(6, 0.7);
                sleep(500);
                break;
            case RIGHT:
                telemetry.addLine("Going RIGHT");
                driver.mecanumDriveRight(6, 0.7);
                sleep(500);
                break;
            case CENTER:
                telemetry.addLine("Going CENTER");
                sleep(500);
                break;
        }
        telemetry.update();

        driver.mecanumDriveForward(7, 0.5);
        driver.mecanumDriveForward(-3, 0.5);
        telemetry.addData("Elapsed Time", this.getRuntime());
        telemetry.update();
        driver.mecanumDriveLeft(18, 1);
        sleep(3000);
        driver.gyroTurn(180,0.45);
        driver.gyroTurn(40,0.45);
        driver.mecanumDriveRight(3, 1);
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
