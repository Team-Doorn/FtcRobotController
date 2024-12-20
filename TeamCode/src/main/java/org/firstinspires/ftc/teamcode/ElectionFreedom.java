package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class ElectionFreedom extends LinearOpMode {
    private DcMotor linksachter;
    private DcMotor linksvoor;
    private DcMotor rechtsvoor;
    private DcMotor rechtsachter;
    private DcMotor arm;
    private Servo elleboog;
    private Servo grijper;

    @Override
    public void runOpMode() throws InterruptedException {
        linksachter = hardwareMap.get(DcMotor.class, "linksachter");
        linksvoor = hardwareMap.get(DcMotor.class, "linksvoor");
        rechtsvoor = hardwareMap.get(DcMotor.class, "rechtsvoor");
        rechtsachter = hardwareMap.get(DcMotor.class, "rechtsachter");
        arm = hardwareMap.get(DcMotor.class, "arm");
        elleboog = hardwareMap.get(Servo.class, "elleboog");
        grijper = hardwareMap.get(Servo.class, "grijper");

        linksachter.setDirection(DcMotorSimple.Direction.REVERSE);
        rechtsachter.setDirection(DcMotorSimple.Direction.REVERSE);

        elleboog.scaleRange(0.42, 0.735);
        elleboog.setPosition(1);
        grijper.scaleRange(0.3, 0.55);
        grijper.setPosition(1);

        waitForStart();

        if (isStopRequested()) return;

        ElapsedTime elapsed = new ElapsedTime(ElapsedTime.Resolution.SECONDS);

        Utils.PowerSupply powerSupply = new Utils.PowerSupply();
        boolean armOn = false;
        while (opModeIsActive()) {
            double seconds = elapsed.time();
            if (seconds < .7) {
                powerSupply.setPower(0f, 1f, 0f, .2f);
            } else if (seconds < .95) {
                powerSupply.stop();
            } else if (seconds < 3.2) {
                powerSupply.setPower(0f, 1f, 0f, .3f);
            } else if (seconds < 4.75) {
                powerSupply.stop();
                armOn = true;
            } else {
                armOn = false;
            }

            if (!armOn) {
                grijper.setPosition(1);
            }

            linksvoor.setPower(powerSupply.frontLeftPower);
            linksachter.setPower(powerSupply.backLeftPower);
            rechtsvoor.setPower(powerSupply.frontRightPower);
            rechtsachter.setPower(powerSupply.backRightPower);

            arm.setPower(armOn ? -.8 : 0);
        }
    }
}
