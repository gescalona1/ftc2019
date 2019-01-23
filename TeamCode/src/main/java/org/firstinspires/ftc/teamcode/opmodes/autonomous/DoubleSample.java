/* Copyright (c) 2018 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.baseopmodes.AutonomousBaseOpMode;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;

import java.util.List;

/**
 * This 2018-2019 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the gold and silver minerals.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Autonomous(name = "Double Sample", group = "auto")
//@Disabled
public class DoubleSample extends AutonomousBaseOpMode {
    RobotDriver driver = RobotDriver.getDriver();
    public static String position = null;

    @Override
    protected void prerun() {
        if (getTfod() != null) {
            getTfod().activate();
        }

        while (!opModeIsActive()) {
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
                                telemetry.addData("Gold Mineral Position", "Left");
                                position = "Left";
                            } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                telemetry.addData("Gold Mineral Position", "Right");
                                position = "Right";
                            } else {
                                telemetry.addData("Gold Mineral Position", "Center");
                                position = "Center";
                            }
                        }
                    }
                    telemetry.update();
                }
//                }
            }
        }
        telemetry.update();
    }

    @Override
    public void run() {
        telemetry.addData("Position", position);
        telemetry.update();
        if (position == "Right") {
            driver.mecanumDriveForward(-25, 0.5);
            driver.mecanumDriveLeft(40, 0.5);
            driver.mecanumDriveForward(-25, 0.5);

        }
        if (position == "Left") {
            driver.mecanumDriveForward(-25, 0.5);
            driver.mecanumDriveRight(40, 0.5);
            driver.mecanumDriveForward(-25, 0.5);
        }
        if (position == "Center") {
            driver.mecanumDriveForward(-25, 0.5);
            driver.mecanumDriveForward(-25, 0.5);
        }
        driver.mecanumDriveForward(25, 0.5);
        driver.mecanumDriveRight(250, 0.5);
        driver.mecanumDriveForward(200, 0.5);
        driver.mecanumDriveRight(100, 0.5);
        driver.mecanumDriveForward(-25, 0.5);
        driver.mecanumDriveLeft(40, 0.5);
        driver.turnRight(0.5);
        if (position == "Left") {
            driver.mecanumDriveLeft(40, 0.5);
        }
        if (position == "Right") {
            driver.mecanumDriveRight(40, 0.5);
        }
        driver.mecanumDriveForward(-25, 0.5);

//        sleep(10000);
        if (getTfod() != null) {
            getTfod().shutdown();
        }
    }

//    @Override
//    public void stop(){
//
//    }
    /**
     * Initialize the Vuforia localization engine.
     */

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
}
