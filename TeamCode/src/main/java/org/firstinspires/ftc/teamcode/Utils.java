package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;
import static java.lang.Math.max;

import java.util.Arrays;
import java.util.Optional;

public class Utils {
    public static float closestToZero(Float[] nums) {
        Optional<Float> num = Arrays.stream(nums).filter(i -> i != 0)
                .reduce((a, b) -> abs(a) < abs(b) ? a : (abs(a) == abs(b) ? max(a, b) : b));
        return num.orElse(0f);
    }

    //public static

    public static class PowerSupply {
        double frontLeftPower;
        double backLeftPower;
        double frontRightPower;
        double backRightPower;

        public PowerSupply getPower(Float x, Float y, Float rx) {
            y = -y;
            x = x * 1.1f;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            frontLeftPower = (y + x + rx) / denominator;
            backLeftPower = (y - x + rx) / denominator;
            frontRightPower = (y - x - rx) / denominator;
            backRightPower = (y + x - rx) / denominator;

            return this;
        }
    }
}
