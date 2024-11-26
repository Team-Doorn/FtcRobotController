package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp(name = "Freedom")
public class Freedom extends LinearOpMode {
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

    /**
     * This function is executed when this OpMode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {
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

        elleboog.setPosition(0.78);
        elleboog.scaleRange(0.41, 0.735);
        grijper.scaleRange(0.3, 0.55);

        // Put initialization blocks here.
        waitForStart();

        if (isStopRequested()) return;

        Utils.PowerSupply mainPowerSupply = new Utils.PowerSupply();
        Utils.PowerSupply secondaryPowerSupply = new Utils.PowerSupply();

        float power = .2f;


        float elleboogPos = 1f;
        boolean dpadPressed = false;
        boolean aPressed = false;
        while (opModeIsActive()) {
            if (gamepad2.dpad_left) {
                power = 0.2f;
            }
            if (gamepad2.dpad_right) {
                power = 1f;
            }

            mainPowerSupply.setPower(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            secondaryPowerSupply.setPower(gamepad2.left_stick_x, gamepad2.left_stick_y, gamepad2.right_stick_x, power);

            Utils.PowerSupply chosenPowerSupply = secondaryPowerSupply.isActive() ? secondaryPowerSupply : mainPowerSupply;

            linksvoor.setPower(chosenPowerSupply.frontLeftPower);
            linksachter.setPower(chosenPowerSupply.backLeftPower);
            rechtsvoor.setPower(chosenPowerSupply.frontRightPower);
            rechtsachter.setPower(chosenPowerSupply.backRightPower);

            float armPower = Utils.closestToZero(new Float[]{-gamepad2.left_trigger, gamepad2.right_trigger});
            arm.setPower(armPower);

            if (gamepad2.dpad_down) {
                if (!dpadPressed) {
                    dpadPressed = true;
                    elleboogPos = Math.max(elleboogPos - 0.25f, 0);
                    elleboog.setPosition(elleboogPos);
                }
            } else if (gamepad2.dpad_up) {
                if (!dpadPressed) {
                    dpadPressed = true;
                    elleboogPos = Math.min(elleboogPos + 0.25f, 1);
                    elleboog.setPosition(elleboogPos);
                }
            } else {
                dpadPressed = false;
            }

            if (gamepad2.a) {
                if (!aPressed) {
                    grijper.setPosition(grijper.getPosition() == 0 ? 1 : 0);
                }
                aPressed = true;
            } else {
                aPressed = false;
            }

        }
    }
}
