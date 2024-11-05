package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Freedom")
public class Freedom extends LinearOpMode {
    private DcMotor linksachter;
    private DcMotor linksvoor;
    private DcMotor rechtsvoor;
    private DcMotor rechtsachter;
    private DcMotor arm;
    private Servo elleboog;
    private Servo grijper;

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

        linksachter.setDirection(DcMotorSimple.Direction.REVERSE);
        rechtsachter.setDirection(DcMotorSimple.Direction.REVERSE);

        elleboog.scaleRange(0.42, 0.735);
        elleboog.setPosition(1);
        grijper.scaleRange(0.3, 0.55);

        // Put initialization blocks here.
        waitForStart();

        if (isStopRequested()) return;

        Utils.PowerSupply mainPowerSupply = new Utils.PowerSupply();
        Utils.PowerSupply secondaryPowerSupply = new Utils.PowerSupply();


        float elleboogPos = 1f;
        boolean dpadPressed = false;
        while (opModeIsActive()) {
            mainPowerSupply.getPower(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            secondaryPowerSupply.getPower(gamepad2.left_stick_x, gamepad2.left_stick_y, gamepad2.right_stick_x, 0.1f);

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
                grijper.setPosition(0);
            } else if (gamepad2.b) {
                grijper.setPosition(1);
            }

        }
    }
}
