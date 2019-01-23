package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.UsesHardware;

/**
 * Ultro
 * RobotDriver.java
 * Purpose: Singleton for movement for the robot (motors)
 *
 * @version 1.0 10/11/2018
 */
public class RobotDriver {
    private static volatile RobotDriver driver = null;
    private UsesHardware opmode;
    private Telemetry telemetry;
    private boolean gyroTurning = false;
    private Orientation currentAngle;

    private final double DRIVE_COUNTS_PER_MOTOR_HEX  =    1120;
    private final double DRIVE_DRIVE_GEAR_REDUCTION  =   24 / 15;
    private final double DRIVE_WHEEL_DIAMETER_INCHES =   4;
    private final double DRIVE_COUNTS_PER_INCH       =   DRIVE_COUNTS_PER_MOTOR_HEX * (DRIVE_DRIVE_GEAR_REDUCTION/DRIVE_WHEEL_DIAMETER_INCHES);

    private final double  BAR_COUNTS_PER_MOTOR_REV  =    2240;
    private final double  BAR_DRIVE_GEAR_REDUCTION  =   1;
    private final double  BAR_WHEEL_DIAMETER_INCHES =   1.326;
    private final double  BAR_COUNTS_PER_INCH       =   BAR_COUNTS_PER_MOTOR_REV * (BAR_DRIVE_GEAR_REDUCTION/BAR_WHEEL_DIAMETER_INCHES)
                                                        / (Math.PI);

    /**
     * Updated mecanum drive function this year (math is ? ?? ? )
     * @param left_stick_x gamepadleftX
     * @param left_stick_y gamepadleftY
     * @param right_stick_x gamepadrightX
     */
    public void mecanumDrive(double left_stick_y, double left_stick_x, double right_stick_x){
        double LB = Range.clip(-left_stick_y - left_stick_x - right_stick_x, -1, 1);
        double LF = Range.clip(-left_stick_y + left_stick_x - right_stick_x, -1, 1);
        double RB = Range.clip(-left_stick_y + left_stick_x + right_stick_x, -1, 1);
        double RF = Range.clip(-left_stick_y - left_stick_x + right_stick_x, -1, 1);
        opmode.getLeftBackDrive().setPower(LB);
        opmode.getLeftFrontDrive().setPower(LF);
        opmode.getRightBackDrive().setPower(RB);
        opmode.getRightFrontDrive().setPower(RF);
    }
    /**
     * @deprecated
     * Old mecanum drive function from last year
     * @param leftX gamepadleftX
     * @param leftY gamepadleftY
     * @param rightX gamepadrightX
     */
    @Deprecated
    public void oldMecanumDrive(double leftX, double leftY, double rightX){
        //taken from last year's code
        // how much to amplify the power
        double r = Math.hypot(leftY, leftX);
        //calculates the angle of the joystick - 45 degrees
        double robotAngle = Math.atan2(leftY, leftX) - Math.PI / 4;
        // rotation
        final double v1 = r * Math.cos(robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;
        opmode.getLeftFrontDrive().setPower(v1);
        opmode.getRightFrontDrive().setPower(v2);
        opmode.getLeftBackDrive().setPower(v3);
        opmode.getRightBackDrive().setPower(v4);
    }

    public void mecanumDriveForward(double inches, double speed){
        //<editor-fold desc="Motors: leftBack, leftFront, rightBack, rightFront">
        DcMotor leftBack = opmode.getLeftBackDrive();
        DcMotor leftFront = opmode.getLeftFrontDrive();
        DcMotor rightBack = opmode.getRightBackDrive();
        DcMotor rightFront = opmode.getRightFrontDrive();
        //</editor-fold>
        int newleftBackTarget = leftBack.getCurrentPosition() + (int)(inches * DRIVE_COUNTS_PER_INCH);
        int newrightFrontTarget = rightFront.getCurrentPosition() + (int)(inches * DRIVE_COUNTS_PER_INCH);
        int newleftFrontTarget = leftFront.getCurrentPosition() + (int)(inches * DRIVE_COUNTS_PER_INCH);
        int newrightBackTarget = rightBack.getCurrentPosition() + (int)(inches * DRIVE_COUNTS_PER_INCH);
        //<editor-fold desc="Setting to RUN_TO_POSITION">
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //</editor-fold>
        //<editor-fold desc="Setting Target Positions">
        leftBack.setTargetPosition(newleftBackTarget);
        leftFront.setTargetPosition(newleftFrontTarget);
        rightBack.setTargetPosition(newrightBackTarget);
        rightFront.setTargetPosition(newrightFrontTarget);
        //</editor-fold>
        //<editor-fold desc="Setting Power">
        leftBack.setPower(speed);
        rightFront.setPower(speed);
        leftFront.setPower(speed);
        rightBack.setPower(speed);
        //</editor-fold>
        while(leftBack.isBusy() && rightFront.isBusy() && leftFront.isBusy() && rightBack.isBusy()){
            if(opmode instanceof OpMode){
                ((OpMode) opmode).telemetry.addData("Speed", speed);
                ((OpMode) opmode).telemetry.addData("Currently going:", "right");
            }
        }
        //<editor-fold desc="Setting Power to 0">
        leftBack.setPower(0);
        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        //</editor-fold>
        //<editor-fold desc="Setting to RUN_WITHOUT_ENCODER">
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //</editor-fold>
    }
    public void mecanumDriveForward(double speed){
        //<editor-fold desc="Motors: leftBack, leftFront, rightBack, rightFront">
        DcMotor leftBack = opmode.getLeftBackDrive();
        DcMotor leftFront = opmode.getLeftFrontDrive();
        DcMotor rightBack = opmode.getRightBackDrive();
        DcMotor rightFront = opmode.getRightFrontDrive();
        //</editor-fold>
        leftBack.setPower(speed);
        rightFront.setPower(speed);
        leftFront.setPower(speed);
        rightBack.setPower(speed);
    }

    public void mecanumDriveBackward(double speed){
        mecanumDriveForward(-speed);
    }
    public void mecanumDriveBackward(int inches, double speed){
        mecanumDriveForward(-inches, speed);
    }

    public void mechanumDriveRight(int inches, double speed){
        //<editor-fold desc="Motors: leftBack, leftFront, rightBack, rightFront">
        DcMotor leftBack = opmode.getLeftBackDrive();
        DcMotor leftFront = opmode.getLeftFrontDrive();
        DcMotor rightBack = opmode.getRightBackDrive();
        DcMotor rightFront = opmode.getRightFrontDrive();
        //</editor-fold>
        int newleftBackTarget = leftBack.getCurrentPosition() - (int)(inches * DRIVE_COUNTS_PER_INCH);
        int newrightFrontTarget = rightFront.getCurrentPosition() - (int)(inches * DRIVE_COUNTS_PER_INCH);
        int newleftFrontTarget = leftFront.getCurrentPosition() + (int)(inches * DRIVE_COUNTS_PER_INCH);
        int newrightBackTarget = rightBack.getCurrentPosition() + (int)(inches * DRIVE_COUNTS_PER_INCH);
        if(inches < 0){
            speed *= -1;
        }
        //<editor-fold desc="Setting to RUN_TO_POSITION">
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //</editor-fold>
        //<editor-fold desc="Setting Target Positions">
        leftBack.setTargetPosition(newleftBackTarget);
        leftFront.setTargetPosition(newleftFrontTarget);
        rightBack.setTargetPosition(newrightBackTarget);
        rightFront.setTargetPosition(newrightFrontTarget);
        //</editor-fold>
        //<editor-fold desc="Setting Power">
        leftFront.setPower(speed);
        rightBack.setPower(speed);
        //</editor-fold>
        while(leftBack.isBusy() && rightFront.isBusy() && leftFront.isBusy() && rightBack.isBusy()){
            if(opmode instanceof OpMode){
                ((OpMode) opmode).telemetry.addData("Speed", speed);
                ((OpMode) opmode).telemetry.addData("Currently going:", "right");
            }
                telemetry.addData("Speed", speed);
                telemetry.update();
        }
        //<editor-fold desc="Setting Power to 0">
        leftBack.setPower(0);
        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        //</editor-fold>
        //<editor-fold desc="Setting to RUN_WITHOUT_ENCODER">
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //</editor-fold>
    }



    public void mecanumDriveLeft(int inches, double speed) {
        //<editor-fold desc="Motors: leftBack, leftFront, rightBack, rightFront">
        DcMotor leftBack = opmode.getLeftBackDrive();
        DcMotor leftFront = opmode.getLeftFrontDrive();
        DcMotor rightBack = opmode.getRightBackDrive();
        DcMotor rightFront = opmode.getRightFrontDrive();
        //</editor-fold>
        int newleftBackTarget = leftBack.getCurrentPosition() + (int) (inches * DRIVE_COUNTS_PER_INCH);
        int newrightFrontTarget = rightFront.getCurrentPosition() + (int) (inches * DRIVE_COUNTS_PER_INCH);
        int newleftFrontTarget = leftFront.getCurrentPosition() - (int) (inches * DRIVE_COUNTS_PER_INCH);
        int newrightBackTarget = rightBack.getCurrentPosition() - (int) (inches * DRIVE_COUNTS_PER_INCH);
        //<editor-fold desc="Setting to RUN_TO_POSITION">
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //</editor-fold>
        if (inches < 0) {
            speed *= -1;
        }
        //<editor-fold desc="Setting Target Positions">
        leftBack.setTargetPosition(newleftBackTarget);
        leftFront.setTargetPosition(newleftFrontTarget);
        rightBack.setTargetPosition(newrightBackTarget);
        rightFront.setTargetPosition(newrightFrontTarget);
        //</editor-fold>
        //<editor-fold desc="Setting Power">
        leftBack.setPower(speed);
        rightFront.setPower(speed);
        leftFront.setPower(-speed);
        rightBack.setPower(-speed);
        //</editor-fold>
        while (leftBack.isBusy() && rightFront.isBusy() && leftFront.isBusy() && rightBack.isBusy()) {
            if (opmode instanceof OpMode) {
                ((OpMode) opmode).telemetry.addData("Speed", speed);
                ((OpMode) opmode).telemetry.addData("Currently going:", "left");
            }
            leftBack.setPower(-speed);
            rightFront.setPower(-speed);
            leftFront.setPower(speed);
            rightBack.setPower(speed);
            //</editor-fold>
            while (leftBack.isBusy() && rightFront.isBusy() && leftFront.isBusy() && rightBack.isBusy()) {
                telemetry.addData("newleftBackTarget", newleftBackTarget);
                telemetry.addData("newleftBackTarget", newrightFrontTarget);
                telemetry.addData("newleftBackTarget", newleftFrontTarget);
                telemetry.addData("newleftBackTarget", newrightBackTarget);
                telemetry.addData("Speed", speed);
                telemetry.update();
            }
            //<editor-fold desc="Setting Power to 0">
            leftBack.setPower(0);
            rightFront.setPower(0);
            leftFront.setPower(0);
            rightBack.setPower(0);
            //</editor-fold>
            //<editor-fold desc="Setting to RUN_WITHOUT_ENCODER">

            //</editor-fold>
        }
    }
    public void encoderTurnLeft45(double speed){
        DcMotor leftBack = opmode.getLeftBackDrive();
        DcMotor leftFront = opmode.getLeftFrontDrive();
        DcMotor rightBack = opmode.getRightBackDrive();
        DcMotor rightFront = opmode.getRightFrontDrive();

        //</editor-fold>
        int newleftBackTarget = leftBack.getCurrentPosition() - (int)(40 * DRIVE_COUNTS_PER_INCH);
        int newrightFrontTarget = rightFront.getCurrentPosition() + (int)(40 * DRIVE_COUNTS_PER_INCH);
        int newleftFrontTarget = leftFront.getCurrentPosition() - (int)(40 * DRIVE_COUNTS_PER_INCH);
        int newrightBackTarget = rightBack.getCurrentPosition() + (int)(40 * DRIVE_COUNTS_PER_INCH);

        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftBack.setTargetPosition(newleftBackTarget);
        leftFront.setTargetPosition(newleftFrontTarget);
        rightBack.setTargetPosition(newrightBackTarget);
        rightFront.setTargetPosition(newrightFrontTarget);

        leftBack.setPower(speed);
        rightFront.setPower(speed);
        leftFront.setPower(speed);
        rightBack.setPower(speed);

        while(leftBack.isBusy() && rightFront.isBusy() && leftFront.isBusy() && rightBack.isBusy()){
            if(opmode instanceof OpMode){
                ((OpMode) opmode).telemetry.addData("Speed", speed);
                ((OpMode) opmode).telemetry.addData("Currently going:", "Turning");
            }
        }
        //<editor-fold desc="Setting Power to 0">
        leftBack.setPower(0);
        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);

    }

    public void turnLeft(double speed){
        int newLeftTarget;
        int newRightTarget;
        int newLeftBottomTarget;
        int newRightBottomTarget;
        DcMotor leftBack = opmode.getLeftBackDrive();
        DcMotor leftFront = opmode.getLeftFrontDrive();
        DcMotor rightBack = opmode.getRightBackDrive();
        DcMotor rightFront = opmode.getRightFrontDrive();

        // Determine new target position, and pass to motor controller
        newLeftTarget = leftFront.getCurrentPosition() + (int)(20 * DRIVE_COUNTS_PER_INCH);
        newLeftBottomTarget = leftBack.getCurrentPosition() + (int)(20 * DRIVE_COUNTS_PER_INCH);
        newRightTarget = rightFront.getCurrentPosition() + (int)(-20 * DRIVE_COUNTS_PER_INCH);
        newRightBottomTarget = rightBack.getCurrentPosition() + (int)(-20 * DRIVE_COUNTS_PER_INCH);
        leftFront.setTargetPosition(newLeftTarget);
        leftBack.setTargetPosition(newLeftBottomTarget);
        rightFront.setTargetPosition(newRightTarget);
        rightBack.setTargetPosition(newRightBottomTarget);

        // Turn On RUN_TO_POSITION
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
//        runtime.reset();
        leftFront.setPower(Math.abs(speed));
        leftBack.setPower(Math.abs(speed));
        rightFront.setPower(Math.abs(speed));
        rightBack.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.

        while(leftBack.isBusy() && rightFront.isBusy() && leftFront.isBusy() && rightBack.isBusy()){
            if(opmode instanceof OpMode){
                ((OpMode) opmode).telemetry.addData("Speed", speed);
                ((OpMode) opmode).telemetry.addData("Currently going:", "Turning");
            }
            ((OpMode) opmode).telemetry.update();
        }
        // Stop all motion;
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);

        // Turn off RUN_TO_POSITION
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void turnRight(double speed){
        int newLeftTarget;
        int newRightTarget;
        int newLeftBottomTarget;
        int newRightBottomTarget;
        DcMotor leftBack = opmode.getLeftBackDrive();
        DcMotor leftFront = opmode.getLeftFrontDrive();
        DcMotor rightBack = opmode.getRightBackDrive();
        DcMotor rightFront = opmode.getRightFrontDrive();

        // Determine new target position, and pass to motor controller
        newLeftTarget = leftFront.getCurrentPosition() + (int)(-20 * DRIVE_COUNTS_PER_INCH);
        newLeftBottomTarget = leftBack.getCurrentPosition() + (int)(-20 * DRIVE_COUNTS_PER_INCH);
        newRightTarget = rightFront.getCurrentPosition() + (int)(20 * DRIVE_COUNTS_PER_INCH);
        newRightBottomTarget = rightBack.getCurrentPosition() + (int)(20 * DRIVE_COUNTS_PER_INCH);
        leftFront.setTargetPosition(newLeftTarget);
        leftBack.setTargetPosition(newLeftBottomTarget);
        rightFront.setTargetPosition(newRightTarget);
        rightBack.setTargetPosition(newRightBottomTarget);

        // Turn On RUN_TO_POSITION
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
//        runtime.reset();
        leftFront.setPower(Math.abs(speed));
        leftBack.setPower(Math.abs(speed));
        rightFront.setPower(Math.abs(speed));
        rightBack.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.

        while(leftBack.isBusy() && rightFront.isBusy() && leftFront.isBusy() && rightBack.isBusy()){
            if(opmode instanceof OpMode){
                ((OpMode) opmode).telemetry.addData("Speed", speed);
                ((OpMode) opmode).telemetry.addData("Currently going:", "Turning");
            }
            ((OpMode) opmode).telemetry.update();
        }
        // Stop all motion;
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);

        // Turn off RUN_TO_POSITION
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void extendPullDownBar(int inches, int speed){
        int newLeftTarget = opmode.getLeftpuldaun().getCurrentPosition() + (int)(inches * BAR_COUNTS_PER_INCH);
        int newRightTarget = opmode.getRightpuldaun().getCurrentPosition() + (int)(inches * BAR_COUNTS_PER_INCH);

        opmode.getLeftpuldaun().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        opmode.getRightpuldaun().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        opmode.getLeftpuldaun().setTargetPosition(newLeftTarget);
        opmode.getRightpuldaun().setTargetPosition(newRightTarget);
        speed = Math.abs(speed);

        opmode.getLeftpuldaun().setPower(speed);
        opmode.getRightpuldaun().setPower(speed);
        while(opmode.getLeftpuldaun().isBusy() && opmode.getRightpuldaun().isBusy()){
            if(opmode instanceof OpMode){
                ((OpMode) opmode).telemetry.addData("Current Position Left :", opmode.getLeftpuldaun().getCurrentPosition());
                ((OpMode) opmode).telemetry.addData("Current Position Right:", opmode.getRightpuldaun().getCurrentPosition());
                ((OpMode) opmode).telemetry.addData("Expected Position Left :", newLeftTarget);
                ((OpMode) opmode).telemetry.addData("Expected Position Right:", newRightTarget);
            }
        }
        opmode.getLeftpuldaun().setPower(0);
        opmode.getRightpuldaun().setPower(0);

        opmode.getLeftpuldaun().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        opmode.getRightpuldaun().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void mecanumDriveRight(int inches, double speed){
        mecanumDriveLeft(-inches, speed);
    }

    public void extendPullDownBar(double inches, double speed){
        //<editor-fold desc="Motors: newLeftTarget, newRightTarget">
        int newLeftTarget = opmode.getLeftpuldaun().getCurrentPosition() + (int)((inches * BAR_COUNTS_PER_INCH));
        int newRightTarget = opmode.getRightpuldaun().getCurrentPosition() + (int)((inches * BAR_COUNTS_PER_INCH));
        //</editor-fold>
        //<editor-fold desc="Setting to RUN_TO_POSITION">
        opmode.getLeftpuldaun().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        opmode.getRightpuldaun().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //</editor-fold>
        //<editor-fold desc="Setting Target Positions">
        opmode.getLeftpuldaun().setTargetPosition(newLeftTarget);
        opmode.getRightpuldaun().setTargetPosition(newRightTarget);
        //</editor-fold>
        if (inches < 0){
            speed *= -1;
        }
        //<editor-fold desc="Setting Power">
        opmode.getLeftpuldaun().setPower(speed);
        opmode.getRightpuldaun().setPower(speed);
        //</editor-fold>
        double pastTime = ((OpMode) opmode).getRuntime();
        while(opmode.getLeftpuldaun().isBusy() && opmode.getRightpuldaun().isBusy()){
            telemetry.addData("Current Position Left :", opmode.getLeftpuldaun().getCurrentPosition());
            telemetry.addData("Current Position Right:", opmode.getRightpuldaun().getCurrentPosition());
            telemetry.addData("Expected Position Left :", newLeftTarget);
            telemetry.addData("Expected Position Right:", newRightTarget);
            telemetry.update();
            if(((OpMode) opmode).getRuntime() - pastTime >= 9){
                break;
            }
        }
        //<editor-fold desc="Setting Power to 0">
        opmode.getLeftpuldaun().setPower(0);
        opmode.getRightpuldaun().setPower(0);
        //</editor-fold>
        //<editor-fold desc="Setting RunMode to RUN_USING_ENCODER">
        opmode.getLeftpuldaun().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        opmode.getRightpuldaun().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //</editor-fold>
    }

    /**
     * Turns the machine using the built in imu, uses a thread to constantly check if the correct angle is met
     * @param angle required angle to rotate [-180, 180]
     * @param power how much speed will the robot move
     */
    public void gyroTurn(float angle, double power, final double CORRECT_ANGLE_RANGE){
        if(!gyroTurning) {
            if (angle == 0) {
                return;
            }
            if (angle > 360) {
                angle = angle - 360;
            } else if (angle > 0) {
                angle = 360 - angle;
            }
            gyroTurning = true;
            resetAngle();
            double leftfrontpower = 0, leftbackpower = 0, rightfrontpower = 0, rightbackpower = 0;
            if (angle < 0) {
                leftfrontpower = -power;
                leftbackpower = -power;
                rightfrontpower = power;
                rightbackpower = power;
            } else if (angle > 0) {
                leftfrontpower = power;
                leftbackpower = power;
                rightfrontpower = -power;
                rightbackpower = -power;
            }
            final float qangle = angle;
            int i = 0;
            opmode.getLeftFrontDrive().setPower(leftfrontpower);
            opmode.getLeftBackDrive().setPower(leftbackpower);
            opmode.getRightFrontDrive().setPower(rightfrontpower);
            opmode.getRightBackDrive().setPower(rightbackpower);
            /*
            if qangle = 90
            102.5 > relativeangle of 90 > 77.5 == true
            true = while loop breaks
            false = while loop still goes
             */
            while(!(qangle + CORRECT_ANGLE_RANGE > getRelativeAngle() && getRelativeAngle() > qangle - CORRECT_ANGLE_RANGE)){
                telemetry.addData("continue", !(qangle + CORRECT_ANGLE_RANGE > getRelativeAngle() && getRelativeAngle() > qangle - CORRECT_ANGLE_RANGE));
                telemetry.addData("relative angle", getRelativeAngle());
                telemetry.addData("requiredAngle with correction", (qangle - CORRECT_ANGLE_RANGE) + " to " + (qangle + CORRECT_ANGLE_RANGE));
                telemetry.addData("difference actual", Math.abs(getRelativeAngle() - qangle));
                telemetry.addData("checked how many times:", i);
                telemetry.update();
                if(i >= 1500) {
                    break;
                }
                i++;
            }
            opmode.getLeftFrontDrive().setPower(0);
            opmode.getLeftBackDrive().setPower(0);
            opmode.getRightFrontDrive().setPower(0);
            opmode.getRightBackDrive().setPower(0);
            gyroTurning = false;
            /*
            Thread t = new Thread(new Runnable() {
                private volatile boolean running = true;
                private final int requiredAngle = qangle;
                @Override
                public void run() {
                    int i = 0;
                    opmode.getLeftFrontDrive().setPower(leftfrontpower);
                    opmode.getLeftBackDrive().setPower(leftbackpower);
                    opmode.getRightFrontDrive().setPower(rightfrontpower);
                    opmode.getRightBackDrive().setPower(rightbackpower);
                    while (running) {
                        if (opmode instanceof OpMode) {
                            ((OpMode) opmode).telemetry.addData("difference", getRelativeAngle());
                            ((OpMode) opmode).telemetry.addData("requiredAngle with correction", (requiredAngle - CORRECT_ANGLE_RANGE) + " to " + (requiredAngle + CORRECT_ANGLE_RANGE));
                            ((OpMode) opmode).telemetry.addData("difference boolean", (getRelativeAngle() > requiredAngle + CORRECT_ANGLE_RANGE && getRelativeAngle() < requiredAngle - CORRECT_ANGLE_RANGE));
                            ((OpMode) opmode).telemetry.addData("checked how many times:", i);
                            ((OpMode) opmode).telemetry.update();
                        }
                        if (getRelativeAngle() > requiredAngle - CORRECT_ANGLE_RANGE && getRelativeAngle() < requiredAngle + CORRECT_ANGLE_RANGE) {
                            running = false;
                            opmode.getLeftFrontDrive().setPower(0);
                            opmode.getLeftBackDrive().setPower(0);
                            opmode.getRightFrontDrive().setPower(0);
                            opmode.getRightBackDrive().setPower(0);
                            gyroTurning = false;
                            return;
                        }
                        i++;
                    }
                }

            });
            ThreadManager.getInstance().addThread(t);
            t.start();*/
        }
    }
    public void gyroTurn(float angle, double power){
        this.gyroTurn(angle, power, 2.5);
    }
    /**
     * Reset the current angle to the current updated angle
     */
    public void resetAngle(){
        currentAngle = opmode.getImu().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
    }
    /**
     * Correct the angle by turning from the axis -180/180 axis to 0/360
     * @param firstAngle the angle returned by the Orientation class
     * @return float
     */
    public float correctAngle(float firstAngle){
        if(firstAngle < 0){
            firstAngle += 360;
        }
        return firstAngle;
    }
    public float getCurrentAngle(){
        return currentAngle.firstAngle;
    }
    /**
     * Get the updated angle
     * @param overwrite if true, overwrite the current angle
     * @return double
     */
    public float getAngle(boolean overwrite){
        Orientation angles = this.opmode.getImu().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        if(overwrite) currentAngle = angles;
        return angles.firstAngle;
    }
    /**
     * Redundant method that does not overwrite the currentAngle variable
     *
     * @return double
     */
    public float getAngle(){
        return getAngle(false);
    }
    /**
     * Get the delta of the current angle and the updated angle
     * @return double
     */
    public float getRelativeAngle(){
        /*
        In relative to the current angle
        MUST BE CORRECTED
         */
        return correctAngle(correctAngle(getAngle()) -  correctAngle(getCurrentAngle()));
    }

    public boolean IsGyroTurning(){
        return gyroTurning;
    }
    /**
     * Private constructor to prevent instantiating this utility class.
     */
    private RobotDriver(){

    }
    /**
     * Get instance of this class (singleton)
     * @return RobotDriver
     */
    public static RobotDriver getDriver(){
        if(driver == null) {
            synchronized (RobotDriver.class){
                if(driver == null){
                    driver = new RobotDriver();
                }
            }
        }
        return driver;
    }
    /**
     * Set the current opmode that is being used (useful for overwriting opmodes)
     * @param opMode The opmode that will be used
     */
    public final void setHardwareMap(UsesHardware opMode){
        /*
        THIS MUST BE DONE
         */
        this.opmode = opMode;
        if(opMode instanceof OpMode){
            this.telemetry = ((OpMode) opmode).telemetry;
        }

    }
}
