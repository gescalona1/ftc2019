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
import org.firstinspires.ftc.teamcode.util.FindMineralRunnable;
import org.firstinspires.ftc.teamcode.util.Position;
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
    public Position position = null;
    private final int CHECKS = 5;
    private final double SPEED = 0.6;

    @Override
    protected void prerun() {
        resetStartTime();
    }

    @Override
    public void run() {
        int i = 0;
        position = FindMineralRunnable.getCurrentPosition();
        while(true){
            if(i >= CHECKS) {
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
                position = (position == Position.NULL) ? position : Position.RIGHT;
                telemetry.addLine("Runtime too long, going over");
                break;
            }
        }
        telemetry.update();
        switch (position){
            case RIGHT:
                driver.mecanumDriveForward(-25, SPEED);
                driver.mecanumDriveLeft(40, SPEED);
                driver.mecanumDriveForward(-25, SPEED);
                break;
            case LEFT:
                driver.mecanumDriveForward(-25, SPEED);
                driver.mecanumDriveRight(40, SPEED);
                driver.mecanumDriveForward(-25, SPEED);
                break;
            case CENTER:
                driver.mecanumDriveForward(-25, SPEED);
                driver.mecanumDriveForward(-25, SPEED);
                break;
        }
        driver.mecanumDriveForward(25, SPEED);
        driver.mecanumDriveRight(250, SPEED);
        driver.mecanumDriveForward(200, SPEED);
        driver.mecanumDriveRight(100, SPEED);
        driver.mecanumDriveForward(-25, SPEED);
        driver.mecanumDriveLeft(40, SPEED);
        driver.turnRight(SPEED);
        switch (position){
            case LEFT:
                driver.mecanumDriveLeft(40, SPEED);
                break;
            case RIGHT:
                driver.mecanumDriveRight(40, SPEED);
                break;
        }
        driver.mecanumDriveForward(-25, SPEED);

//        sleep(10000);
        if (getTfod() != null) {
            getTfod().shutdown();
        }
    }

}
