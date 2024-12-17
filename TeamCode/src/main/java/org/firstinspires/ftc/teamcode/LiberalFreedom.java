package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "LiberalFreedom")
public class LiberalFreedom extends LinearOpMode {
    private DcMotor linksachter;
    private DcMotor linksvoor;
    private DcMotor rechtsvoor;
    private DcMotor rechtsachter;
    private DcMotor arm;
    private Servo elleboog;
    private Servo grijper;
    private TouchSensor touchlinks;
    private TouchSensor touchrechts;
    private TouchSensor touchachter;
    private DistanceSensor afstandachter;
    private DistanceSensor afstandrechts;


    @Override
    public void runOpMode() throws InterruptedException {
        linksachter = hardwareMap.get(DcMotor.class, "linksachter");
        linksvoor = hardwareMap.get(DcMotor.class, "linksvoor");
        rechtsvoor = hardwareMap.get(DcMotor.class, "rechtsvoor");
        rechtsachter = hardwareMap.get(DcMotor.class, "rechtsachter");
        arm = hardwareMap.get(DcMotor.class, "arm");
        elleboog = hardwareMap.get(Servo.class, "elleboog");
        grijper = hardwareMap.get(Servo.class, "grijper");
        touchlinks = hardwareMap.get(TouchSensor.class, "touchlinks");
        touchrechts = hardwareMap.get(TouchSensor.class, "touchrechts");
        touchachter = hardwareMap.get(TouchSensor.class, "touchachter");
        afstandachter = hardwareMap.get(DistanceSensor.class, "afstandachter");
        afstandrechts = hardwareMap.get(DistanceSensor.class, "afstandrechts");

        linksachter.setDirection(DcMotorSimple.Direction.REVERSE);
        rechtsachter.setDirection(DcMotorSimple.Direction.REVERSE);

        elleboog.scaleRange(0.41, 0.735);
        elleboog.setPosition(1);
        grijper.scaleRange(0.3, 0.55);
        grijper.setPosition(1);

        waitForStart();

        if (isStopRequested()) return;

        ElapsedTime elapsed = new ElapsedTime(ElapsedTime.Resolution.SECONDS);

        Utils.PowerSupply powerSupply = new Utils.PowerSupply();
        boolean armOn = false;
        boolean hasHitScoreBar = false;
        boolean hasHitBorder = false;
        double fixedSeconds = 0;
        double stoppedAt = 0;
        while (opModeIsActive()) {
            double seconds = elapsed.time();

            if (touchlinks.isPressed() && touchrechts.isPressed() && !hasHitScoreBar) {
                hasHitScoreBar = true;
                fixedSeconds = seconds;
            }

            if ((touchachter.isPressed() || afstandachter.getDistance(DistanceUnit.CM) < 10) && hasHitScoreBar && !hasHitBorder) {
                hasHitBorder = true;
                fixedSeconds = seconds;
            }

            if (!hasHitScoreBar) {
                powerSupply.setPower(0f, -1f, 0f, .3f);
            } else if (seconds - fixedSeconds < 0.09 && !hasHitBorder) {
                powerSupply.setPower(0f, 1f, 0f, .3f);
            } else if (seconds - fixedSeconds < 3 && !hasHitBorder) {
                powerSupply.stop();
                arm.setPower(-0.8);
            } else if (!hasHitBorder) {
                arm.setPower(0);
                grijper.setPosition(0);
                powerSupply.setPower(0f, 1f, 0f, .3f);
            } else if (afstandrechts.getDistance(DistanceUnit.CM) > 10 && seconds - fixedSeconds < 10) {
                powerSupply.setPower(1f, 0f, 0f, .3f);
            } else {
                powerSupply.stop();
                if (stoppedAt == 0) {
                    stoppedAt = afstandrechts.getDistance(DistanceUnit.CM);
                }
            }

            telemetry.addData("achter", afstandachter.getDistance(DistanceUnit.CM));
            telemetry.addData("rechts", afstandrechts.getDistance(DistanceUnit.CM));
            telemetry.addData("stoppedat", stoppedAt);
            telemetry.update();

            linksvoor.setPower(powerSupply.frontLeftPower);
            linksachter.setPower(powerSupply.backLeftPower);
            rechtsvoor.setPower(powerSupply.frontRightPower);
            rechtsachter.setPower(powerSupply.backRightPower);
        }
    }
}
