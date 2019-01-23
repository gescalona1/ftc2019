package org.firstinspires.ftc.teamcode.opmodes.autonomous.finalop.samples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.baseopmodes.AutonomousBaseOpMode;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;
import org.firstinspires.ftc.teamcode.util.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Ultro
 * AutonomousMode.java
 * Purpose: Extends from AutonomousBaseOpMode
 *
 * @version 1.0 10/11/2018
 */
@Disabled
@Autonomous(name = "Main Autonomous", group = "auto")
public class AutonomousMode extends AutonomousBaseOpMode {
    RobotDriver driver = RobotDriver.getDriver();
    private double lastTime;
    private Position position;
    /*
       The higher number this is, the more accurate autonomous is
     */
    private final int CHECKS = 3;
    /*
    Before waitforStart()
     */
    @Override
    protected void prerun() {
        if (getTfod() != null) {
            getTfod().activate();
        }else{
            prerun();
        }
        getBucket().setPosition(0.5);
        telemetry.addLine("Ready, press init!");
        telemetry.update();
    }
    /*
    After waitForStart()
     */
    @Override
    protected void run() {
        List<Position> positions = new ArrayList<>();
        lastTime = getRuntime();
        while (opModeIsActive() && getRuntime() - lastTime > 6) {
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
                            positions.add(position);
                            telemetry.addData("Gold Mineral Position", position.toString());
                            break;
                        /*
                        if(positions.size() == CHECKS){
                            try {
                                for(Position p : positions){
                                    if(p == positions.get(positions.indexOf(p) + 1)){
                                        telemetry.addData("Increments of checks for gold Mineral Position: " + p.toString(),
                                                                    positions.indexOf(p));
                                        telemetry.update();
                                    }else{
                                        positions.clear();
                                    }
                                }
                            }catch (ArrayIndexOutOfBoundsException e) {
                                break;
                            }
                        }*/
                        }
                    }
                    telemetry.update();
                }
            }
        }
        driver.extendPullDownBar(3, 0.8);
        new Thread(() -> {
            driver.extendPullDownBar(-3, 0.7);
            while(opModeIsActive()){
                if (getTfod() != null) {
                    getTfod().shutdown();
                }
            }
        }).start();
        driver.mecanumDriveForward(2, 0.5);
        if(position != null) {
            switch (position) {
                case LEFT:
                    driver.mecanumDriveLeft(2, 0.5);
                    telemetry.addLine("Going LEFT");
                    break;
                case RIGHT:
                    driver.mecanumDriveRight(2, 0.5);
                    telemetry.addLine("Going RIGHT");
                    break;
                case CENTER:
                    telemetry.addLine("Going CENTER");
                    break;
            }
        }
        telemetry.update();
        driver.mecanumDriveForward(1, 1);
        driver.gyroTurn(-50, 1);
        driver.mecanumDriveRight(10, 1);
        driver.mecanumDriveForward(1);
        sleep(1500);
        driver.mecanumDriveForward(0);
        getBucket().setPosition(0.9d);
        new Thread(() -> {
            getBucket().setPosition(0.3d);
        }).start();
        driver.mecanumDriveRight(3, 1);
        driver.mecanumDriveBackward(1);
        sleep(6000);
        driver.mecanumDriveBackward(0);
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
