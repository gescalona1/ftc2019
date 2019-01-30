package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.UsesHardware;
import org.firstinspires.ftc.teamcode.baseopmodes.AutonomousBaseOpMode;
import org.firstinspires.ftc.teamcode.util.FindMineralRunnable;

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

    private final double  BAR_COUNTS_PER_MOTOR_REV  =    1120;
    private final double  BAR_DRIVE_GEAR_REDUCTION  =   1;
    private final double  BAR_WHEEL_DIAMETER_INCHES =   1.428;
    private final double  BAR_COUNTS_PER_INCH       =   BAR_COUNTS_PER_MOTOR_REV * (BAR_DRIVE_GEAR_REDUCTION/BAR_WHEEL_DIAMETER_INCHES)
            / (Math.PI*4);

    private final double DRIVE_COUNTS_PER_MOTOR_HEX  =    1120;
    private final double DRIVE_DRIVE_GEAR_REDUCTION  =   15 / 24;
    private final double DRIVE_WHEEL_DIAMETER_INCHES =   4;
    private final double DRIVE_COUNTS_PER_INCH       =   DRIVE_COUNTS_PER_MOTOR_HEX * (DRIVE_DRIVE_GEAR_REDUCTION/DRIVE_WHEEL_DIAMETER_INCHES)
            / (Math.PI*4);

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
                ((OpMode) opmode).telemetry.update();
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

    public void mecanumDriveBackward(int inches, double speed){
        mecanumDriveForward(-inches, speed);
    }
    public void mecanumDriveBackward(double speed){
        mecanumDriveForward(-speed);
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
                ((OpMode) opmode).telemetry.addData("Currently going: ", (inches < 0) ? "right" : "left");
            }
        }
        //</editor-fold>
        //<editor-fold desc="Setting Power to 0">
        leftBack.setPower(0);
        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        //</editor-fold>

        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }
    public void mecanumDriveRight(int inches, double speed){
        mecanumDriveLeft(-inches, speed);
    }

    public void extendPullDownBar(double inches, double speed){
        double aBAR_COUNTS_PER_MOTOR_REV  =    2240;
        double aBAR_DRIVE_GEAR_REDUCTION  =   1;
        double aBAR_WHEEL_DIAMETER_INCHES =   1.326;
        double aBAR_COUNTS_PER_INCH       =   aBAR_COUNTS_PER_MOTOR_REV * (aBAR_DRIVE_GEAR_REDUCTION/aBAR_WHEEL_DIAMETER_INCHES)
                / (Math.PI);

        int newLeftTarget = opmode.getLeftpuldaun().getCurrentPosition() + (int)(inches * aBAR_COUNTS_PER_INCH);
        int newRightTarget = opmode.getRightpuldaun().getCurrentPosition() + (int)(inches * aBAR_COUNTS_PER_INCH);

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
                ((OpMode) opmode).telemetry.addData("Delta left Left:", Math.abs(newLeftTarget - opmode.getLeftpuldaun().getCurrentPosition()));
                ((OpMode) opmode).telemetry.addData("Delta right Right:", Math.abs(newRightTarget - opmode.getRightpuldaun().getCurrentPosition()));
                ((OpMode) opmode).telemetry.update();
            }
        }
        opmode.getLeftpuldaun().setPower(0);
        opmode.getRightpuldaun().setPower(0);

        opmode.getLeftpuldaun().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        opmode.getRightpuldaun().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void mecanumDriveRight(double speed){
        //<editor-fold desc="Motors: leftBack, leftFront, rightBack, rightFront">
        DcMotor leftBack = opmode.getLeftBackDrive();
        DcMotor leftFront = opmode.getLeftFrontDrive();
        DcMotor rightBack = opmode.getRightBackDrive();
        DcMotor rightFront = opmode.getRightFrontDrive();
        //</editor-fold>
        leftBack.setPower(speed);
        rightFront.setPower(speed);
        leftFront.setPower(-speed);
        rightBack.setPower(-speed);
    }
    public void mecanumDriveLeft(double speed){
        this.mecanumDriveRight(-speed);
    }


    /**
     * Turns the machine using the built in imu, constantly checks if the correct angle is met
     * @param angle required angle to rotate [-180, 180]
     * @param power how much speed will the robot move
     * @param CORRECT_ANGLE_RANGE to the extent of how much it will check if it achieves the angle
     */
    public void gyroTurn(double angle, double power, final double CORRECT_ANGLE_RANGE){
        if(opmode instanceof LinearOpMode){
            if(((LinearOpMode) opmode).isStopRequested()) return;
        }
        if(!gyroTurning) { // to make sure gyroTurn is not called again while it is already being done
            if (angle == 0) return; // if the angle is 0, just don't do anything
            try { //Opmode sleep function
                Thread.sleep(750);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (angle >= 360) {
                /*
                    if the angle is more than 360, just subtract 360
                    to avoid going into a full circle and
                    re-run the function
                 */
                angle = angle - 360;
                gyroTurn(angle, power);
                return; //make sure this is ran once
            }
            // initiates powers
            double leftfrontpower = 0, leftbackpower = 0, rightfrontpower = 0, rightbackpower = 0;
            if (angle > 0) {
                /*
                    If the angle goes to the left,
                    set the positive power to the right side and set the negative power to left side.
                    This allows the robot to rotate left.
                */
                leftfrontpower = -power;
                leftbackpower = -power;
                rightfrontpower = power;
                rightbackpower = power;
            } else if (angle < 0) {
                /*
                    If the angle goes to the right,
                    set the positive power to the left side and set the negative power to right side.
                    This allows the robot to rotate right.
                */
                leftfrontpower = power;
                leftbackpower = power;
                rightfrontpower = -power;
                rightbackpower = -power;
            }
            if (angle > 0) {
                /*
                    Since the imu gyro goes from -pi to pi (-180 to 180, where 0 is the center),
                    what we did was translate the output
                    so that it becomes a range of 0-359 (counter-clockwise).
                 */
                angle = 360 - angle;
            }else if(angle < 0){
                /*
                    If the angle is negative, adjust accordingly
                 */
                angle = Math.abs(angle);
            }
            gyroTurning = true;
            resetAngle(); //setting the angle to 0, so that we can easily check for the difference
            //Setting powers
            opmode.getLeftFrontDrive().setPower(leftfrontpower);
            opmode.getLeftBackDrive().setPower(leftbackpower);
            opmode.getRightFrontDrive().setPower(rightfrontpower);
            opmode.getRightBackDrive().setPower(rightbackpower);
            int i = 0; //See later code
            final double qangle = angle;
            /* Sample simulation
            if qangle = 90 and CORRECT_ANGLE_RANGE = 2.5
            92.5 > relativeangle of 90 > 87.5 == true
            true = while loop breaks
            false = while loop still goes
             */
            while(!(qangle + CORRECT_ANGLE_RANGE > getRelativeAngle() &&
                    getRelativeAngle() > qangle - CORRECT_ANGLE_RANGE)){
                /*
                    Below is just telemetry output so that we can see what's happening
                 */
                telemetry.addData("continue", !(qangle + CORRECT_ANGLE_RANGE >
                        getRelativeAngle() && getRelativeAngle() > qangle - CORRECT_ANGLE_RANGE));
                telemetry.addData("relative angle", getRelativeAngle());
                telemetry.addData("requiredAngle with correction",
                        (qangle - CORRECT_ANGLE_RANGE) + " to " + (qangle + CORRECT_ANGLE_RANGE));
                telemetry.addData("difference actual", Math.abs(getRelativeAngle() - qangle));
                telemetry.addData("checked how many times:", i);
                telemetry.update();
                if(i >= 1500) {
                    break; //if it checks too many times, break out
                }
                i++;
            }
            // Cleaning up, setting motors' powers to 0
            opmode.getLeftFrontDrive().setPower(0);
            opmode.getLeftBackDrive().setPower(0);
            opmode.getRightFrontDrive().setPower(0);
            opmode.getRightBackDrive().setPower(0);
            gyroTurning = false;
        }
    }
    public void gyroTurn(double angle, double power){
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
        if(opMode instanceof AutonomousBaseOpMode || opMode instanceof LinearOpMode) {
            FindMineralRunnable.reset();
            Thread t = new Thread(new FindMineralRunnable());

            this.telemetry.addLine("Find Mineral starting thread");
            if (!FindMineralRunnable.isAlreadyRunning()) {
                t.start();
                this.telemetry.addLine("Find Mineral is now running");
            }

        }
        this.telemetry.addLine("Set hardware map was successful");
    }

    public UsesHardware getHardwareMap() {
        return opmode;
    }
}
