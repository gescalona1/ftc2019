package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.baseopmodes.AutonomousBaseOpMode;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;

import java.util.List;

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
        telemetry.addLine("Ready");
        telemetry.update();
    }
    /*
    After waitForStart()
     */
    @Override
    protected void run() {

        if (opModeIsActive()) {
            /** Activate Tensor Flow Object Detection. */
            if (getTfod() != null) {
                getTfod().activate();
            }
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
                                } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                    //code for driving right
                                } else {
                                    //code for driving center
                                }
                                break;
                            }
                        }
                        telemetry.update();
                    }
                }
            }

            if (getTfod() != null) {
                getTfod().shutdown();
            }
        }
    }
}
