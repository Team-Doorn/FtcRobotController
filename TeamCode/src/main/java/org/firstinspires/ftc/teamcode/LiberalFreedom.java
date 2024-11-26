package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

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
        while (opModeIsActive()) {
            double seconds = elapsed.time();

            if (touchlinks.isPressed() && touchrechts.isPressed() && !hasHitScoreBar) {
                hasHitScoreBar = true;
                fixedSeconds = seconds;
            }

            if (touchachter.isPressed() && hasHitScoreBar && !hasHitBorder) {
                hasHitBorder = true;
                fixedSeconds = seconds;
            }

            if (!hasHitScoreBar) {
                powerSupply.setPower(0f, -1f, 0f, .3f);
            } else if (seconds - fixedSeconds < 0.05 && !hasHitBorder) {
                powerSupply.setPower(0f, 1f, 0f, .3f);
            } else if (seconds - fixedSeconds < 3 && !hasHitBorder) {
                powerSupply.stop();
                arm.setPower(-0.8);
            } else if (!hasHitBorder) {
                arm.setPower(0);
                grijper.setPosition(0);
                powerSupply.setPower(0f, 1f, 0f, .3f);
            } else if (seconds - fixedSeconds < 3) {
                powerSupply.setPower(1f, 0f, 0f, .3f);
            } else {
                powerSupply.stop();
            }

            linksvoor.setPower(powerSupply.frontLeftPower);
            linksachter.setPower(powerSupply.backLeftPower);
            rechtsvoor.setPower(powerSupply.frontRightPower);
            rechtsachter.setPower(powerSupply.backRightPower);
        }
    }
}
