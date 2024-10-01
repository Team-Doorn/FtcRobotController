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
}
