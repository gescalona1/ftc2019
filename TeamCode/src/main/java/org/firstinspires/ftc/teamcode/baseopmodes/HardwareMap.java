package org.firstinspires.ftc.teamcode.baseopmodes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.VARIABLES;

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
    private int cameraViewId;

    private BNO055IMU imu;

    private DcMotor leftFrontDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor lift = null;

    // Tensorflow
    private final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private static final String VUFORIA_KEY = VARIABLES.VUFORIA_KEY;
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    public void hardwareInit(Telemetry telemetry){
        cameraViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        //<editor-fold desc="DcMotorSetup">
        motorInit();
        //</editor-fold>
        //<editor-fold desc="ImuConfiguration">
        imuInit(telemetry);
        //</editor-fold>
        //<editor-fold desc="Tensor Flow configuration">
        initCamera(telemetry);
        //</editor-fold>
    }

    public void motorInit(){
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "LF");
        rightFrontDrive  = hardwareMap.get(DcMotor.class, "RF");
        leftBackDrive = hardwareMap.get(DcMotor.class, "LB");
        rightBackDrive = hardwareMap.get(DcMotor.class, "RB");
        lift = hardwareMap.get(DcMotor.class, "lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.REVERSE);
        lift.setDirection(DcMotor.Direction.FORWARD);

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
        synchronized (this){
            int i = 0;
            while(!imu.isGyroCalibrated()) {
                try {
                    telemetry.addData(i + " Gyro Calibrated:", imu.isGyroCalibrated());
                    telemetry.update();
                    i++;
                    this.wait(50);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void initCamera(Telemetry telemetry){
        initVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
    }
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    public DcMotor getLeftFrontDrive() {
        return leftFrontDrive;
    }
    public DcMotor getRightFrontDrive() {
        return rightFrontDrive;
    }
    public DcMotor getLeftBackDrive() {
        return leftBackDrive;
    }
    public DcMotor getRightBackDrive() {
        return rightBackDrive;
    }
    public DcMotor getLift() {
        return lift;
    }

    public BNO055IMU getImu() {
        return imu;
    }

    public int getCameraViewId(){
        return cameraViewId;
    }
    public VuforiaLocalizer getVuforia() {
        return vuforia;
    }
    public TFObjectDetector getTfod() {
        return tfod;
    }

    public String getTfodModelAsset() {
        return TFOD_MODEL_ASSET;
    }
    public String getLabelGoldMineral() {
        return LABEL_GOLD_MINERAL;
    }
    public String getLabelSilverMineral() {
        return LABEL_SILVER_MINERAL;
    }
}
