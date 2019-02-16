package org.firstinspires.ftc.teamcode.baseopmodes;


import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.VARIABLES;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Ultro
 * HardwareMap.java
 * Purpose: Mapping of all the objects/machines of the robot
 *
 * @version 1.0 10/11/2018
 */

public final class HardwareMap {


    private com.qualcomm.robotcore.hardware.HardwareMap hardwareMap;
    public HardwareMap(com.qualcomm.robotcore.hardware.HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }

    public com.qualcomm.robotcore.hardware.HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    private int cameraViewId;

    private BNO055IMU imu;

    private DcMotor leftFrontDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightBackDrive = null;

    private DcMotor rotate = null;
    private DcMotor extend = null;


    private DcMotor rightpuldaun = null;
    private DcMotor leftpuldaun = null;

    private Servo intake = null;
    private Servo buck = null;
    private Servo marker = null;

    private Rev2mDistanceSensor frontDSensor;
    private Rev2mDistanceSensor leftDSensor;
    private Rev2mDistanceSensor rightDSensor;

    int rickSoundID;
    boolean rickFound;
    int ussrSoundID;
    boolean ussrFound;

    // Tensorflow
    private final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private static final String VUFORIA_KEY = VARIABLES.VUFORIA_KEY;
    private VuforiaLocalizer phoneVuforia;
    private VuforiaLocalizer cameraVuforia;
    private TFObjectDetector tfod;

    private int cameraMonitorViewId;
    private WebcamName webcamName;
    private final File captureDirectory = AppUtil.ROBOT_DATA_DIR;


    private VuforiaTrackable relicTemplate;

    OpenGLMatrix lastLocation = null;

    public void hardwareInit(Telemetry telemetry){
        cameraViewId = hardwareMap.appContext.getResources().getIdentifier("9ED9A76F", "id", hardwareMap.appContext.getPackageName());
        //<editor-fold desc="ImuConfiguration">

        imuInit(telemetry);
        dsensorInit();
        //<editor-fold desc="DcMotorSetup">
        motorInit();
        //</editor-fold>
        servoInit();

        //<editor-fold desc="Tensor Flow configuration">
        initPhoneCamera(telemetry);
        //</editor-fold>
        //</editor-fold>

    }


    public void driverHardwareInit(Telemetry telemetry){
        //<editor-fold desc="DcMotorSetup">
        motorInit();
        //</editor-fold>
        servoInit();
        //<editor-fold desc="ImuConfiguration">
        imuInit(telemetry);
        //</editor-fold>
        telemetry.addLine("Setting up sound");
        telemetry.update();
        //initSound();
        telemetry.addLine("Finished up sound");
        telemetry.update();
    }


    public void motorInit(){
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "LF");
        rightFrontDrive  = hardwareMap.get(DcMotor.class, "RF");
        leftBackDrive = hardwareMap.get(DcMotor.class, "LB");
        rightBackDrive = hardwareMap.get(DcMotor.class, "RB");
        rotate = hardwareMap.get(DcMotor.class, "rotate");
        rotate.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightpuldaun = hardwareMap.get(DcMotor.class, "RP");
        leftpuldaun = hardwareMap.get(DcMotor.class, "LP");
        extend = hardwareMap.get(DcMotor.class, "extend");

        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightpuldaun = hardwareMap.get(DcMotor.class, "RP");
        leftpuldaun = hardwareMap.get(DcMotor.class, "LP");
        intake = hardwareMap.get(Servo.class, "intake");


        leftpuldaun.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightpuldaun.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rotate.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftpuldaun.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightpuldaun.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extend.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rotate.setDirection(DcMotor.Direction.REVERSE);
        extend.setDirection(DcMotor.Direction.REVERSE);
        rightpuldaun.setDirection(DcMotor.Direction.FORWARD);
        leftpuldaun.setDirection(DcMotor.Direction.REVERSE);

    }

    public void dsensorInit(){
        frontDSensor = (Rev2mDistanceSensor) hardwareMap.get(DistanceSensor.class, "frontD");
        leftDSensor = (Rev2mDistanceSensor) hardwareMap.get(DistanceSensor.class, "leftD");
        rightDSensor = (Rev2mDistanceSensor) hardwareMap.get(DistanceSensor.class, "rightD");

        List<Rev2mDistanceSensor> hello = new ArrayList<>();
        hello.add(frontDSensor);
        hello.add(leftDSensor);
        hello.add(rightDSensor);
        for(Rev2mDistanceSensor sensor : hello){
            sensor.initialize();
        }

    }

    public void servoInit(){
        intake = hardwareMap.get(Servo.class, "intake");
        buck = hardwareMap.get(Servo.class, "bucket");
        marker = hardwareMap.get(Servo.class, "marker");
    }
    public void imuInit(Telemetry telemetry){
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = true;

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        //</editor-fold>
        int i = 0;
        while(!imu.isGyroCalibrated()  && RobotDriver.getDriver().getHardwareMap() instanceof LinearOpMode) {
            if(!((LinearOpMode) RobotDriver.getDriver().getHardwareMap()).opModeIsActive()) break;
            telemetry.addData(i + " Gyro Calibrated:", imu.isGyroCalibrated());
            telemetry.update();
            i++;
        }
    }

    public void initCamera(Telemetry telemetry){
        webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = webcamName;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        //parameters.cameraName = webcamName;
        cameraVuforia = ClassFactory.getInstance().createVuforia(parameters);
        //  Instantiate the Vuforia engine
        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
        VuforiaTrackables relicTrackables = this.cameraVuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
    }

    public void initPhoneCamera(Telemetry telemetry){
        initPhoneVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) initTfod();
        else telemetry.addData("Sorry!", "This device is not compatible with TFOD");
    }


    private void initPhoneVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        phoneVuforia = ClassFactory.getInstance().createVuforia(parameters);
        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, phoneVuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    public void initSound(){

        ussrSoundID = hardwareMap.appContext.getResources().getIdentifier("ussr_theme", "raw", hardwareMap.appContext.getPackageName());
        rickSoundID = hardwareMap.appContext.getResources().getIdentifier("ricka", "raw", hardwareMap.appContext.getPackageName());

        if(rickSoundID != 0) rickFound = SoundPlayer.getInstance().preload(hardwareMap.appContext, rickSoundID);
        if(ussrSoundID != 0) ussrFound = SoundPlayer.getInstance().preload(hardwareMap.appContext, ussrSoundID);

    }
    public final DcMotor getLeftFrontDrive() {
        return leftFrontDrive;
    }
    public final DcMotor getRightFrontDrive() {
        return rightFrontDrive;
    }
    public final DcMotor getLeftBackDrive() {
        return leftBackDrive;
    }
    public final DcMotor getRightBackDrive() {
        return rightBackDrive;
    }

    public DcMotor getExtend() {
        return extend;
    }


    public final DcMotor getRightpuldaun() {
        return rightpuldaun;
    }
    public final DcMotor getLeftpuldaun() {
        return leftpuldaun;
    }

    public final BNO055IMU getImu() {
        return imu;
    }

    public final int getCameraViewId(){
        return cameraViewId;
    }
    public final VuforiaLocalizer getPhoneVuforia() {
        return phoneVuforia;
    }
    public final TFObjectDetector getTfod() {
        return tfod;
    }
//    public WebcamName getWebcamName() {
//        return webcamName;
//    }
    public final WebcamName getWebcamName() {
        return webcamName;
    }

    public VuforiaTrackable getRelicTemplate() {
        return relicTemplate;
    }

    public final String getTfodModelAsset() {
        return TFOD_MODEL_ASSET;
    }
    public final String getLabelGoldMineral() {
        return LABEL_GOLD_MINERAL;
    }
    public final String getLabelSilverMineral() {
        return LABEL_SILVER_MINERAL;
    }

    public DcMotor getRotate() {
        return rotate;
    }

    public Servo getIntake() {
        return intake;
    }

    public final Servo getBucket() {
        return buck;
    }

    public final Servo getMarker() {
        return marker;
    }

    public boolean playRick(){
        SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, rickSoundID);
        return rickFound;
    }
    public boolean playUSSR(){
        SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, ussrSoundID);
        return ussrFound;
    }

    public Rev2mDistanceSensor getFrontDSensor() {
        return frontDSensor;
    }

    public Rev2mDistanceSensor getLeftDSensor() {
        return leftDSensor;
    }

    public Rev2mDistanceSensor getRightDSensor() {
        return rightDSensor;
    }

    public void stopPlayingAllSounds(){
        SoundPlayer.getInstance().stopPlayingAll();
    }


}
