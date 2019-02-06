package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.UsesHardware;
import org.firstinspires.ftc.teamcode.baseopmodes.AutonomousBaseOpMode;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by gescalona on 1/23/19.
 */

public final class FindMineralRunnable implements Runnable {
    private static Position position;
    private static boolean alreadyRunning;
    private static boolean recordData;
    private static boolean stop = false;
    @Override
    public void run() {
        //Get current Hardware Map
        alreadyRunning = true;
        UsesHardware usesHardware = RobotDriver.getDriver().getHardwareMap();
        OpMode opMode = (OpMode) usesHardware;
        if (usesHardware != null && usesHardware instanceof LinearOpMode) {
            LinearOpMode linearOpMode = (LinearOpMode) usesHardware;
            while (!linearOpMode.opModeIsActive()) {
                linearOpMode = (LinearOpMode) RobotDriver.getDriver().getHardwareMap();
                if(stop || linearOpMode.isStopRequested() || RobotDriver.getDriver().getHardwareMap() == null) {
                    alreadyRunning = false;
                    return;
                }
                TFObjectDetector tfod = ((UsesHardware) opMode).getTfod();
                tfod.activate();
                if (tfod != null) {
                    Telemetry telemetry = opMode.telemetry;
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() == 2) {
                            int goldMineralX = -1;
                            int silverMineral1X = -1;
                            int silverMineral2X = -1;
                            for (Recognition recognition : updatedRecognitions) {
                                if (recognition.getLabel().equals(((UsesHardware) opMode).getLabelGoldMineral())) {
                                    goldMineralX = (int) recognition.getLeft();
                                } else if (silverMineral1X == -1) {
                                    silverMineral1X = (int) recognition.getLeft();
                                } else {
                                    silverMineral2X = (int) recognition.getLeft();
                                }
                            }
                            if(goldMineralX == -1 && silverMineral1X !=  -1 && silverMineral2X != -1){
                                position = Position.LEFT;
                            }else if(goldMineralX != -1){
                                if(goldMineralX < silverMineral1X || goldMineralX < silverMineral2X){
                                    position = Position.CENTER;
                                }else if(goldMineralX > silverMineral1X || goldMineralX > silverMineral2X){
                                    position = Position.RIGHT;
                                }
                            }else position = Position.NULL;
                        } else position = Position.NULL;
                        if (recordData) {
                            telemetry.addData("Gold Mineral Position", position.toString());
                            telemetry.update();
                        }
                    }
                }
            }
            alreadyRunning = false;
        }else {
            TFObjectDetector tfod = ((UsesHardware) opMode).getTfod();
            tfod.deactivate();
        }
    }


    public static Position getCurrentPosition(){
        return position;
    }

    public static boolean isAlreadyRunning() {
        return alreadyRunning;
    }

    public static boolean isRecordData() {
        return recordData;
    }

    public static void setRecordData(boolean recordData) {
        FindMineralRunnable.recordData = recordData;
    }

    public static void reset(){
        stop = false;
    }
    public static void forceStop(){
        stop = true;
    }
}