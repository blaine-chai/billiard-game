package kr.ac.hanyang.util;

import kr.ac.hanyang.model.Ball;
import kr.ac.hanyang.values.Constants;

/**
 * Created by blainechai on 2016. 6. 12..
 */
public class PhysicsUtil {
    public static boolean isCollide(Ball i, Ball j) {
        double dx = i.p_x - j.p_x;
        double dy = i.p_y - j.p_y;

        double d2 = dx * dx + dy * dy;
        return d2 < Constants.ball_width * Constants.ball_width;
    }

    public static void fixOverlap(Ball ball1, Ball ball2, double interval) {
        recalculateBallState(ball1, ball2, calculateCollidedTime(ball1, ball2, interval));
    }

    private static double calculateCollidedTime(Ball ball1, Ball ball2, double interval) {
        double dx = ball1.p_x - ball2.p_x;
        double dy = ball1.p_y - ball2.p_y;
        double ddx = ball1.v_x - ball2.v_x;
        double ddy = ball1.v_y - ball2.v_y;

        double root1, root2;
        double t;

        root1 = (-(dx * ddx + dy * ddy) + Math.sqrt((dx * ddx + dy * ddy) * (dx * ddx + dy * ddy) - ((ddx * ddx) + (ddy * ddy)) * (dx * dx + dy * dy - (double) Constants.ball_width * Constants.ball_width))) / ((ddx * ddx) + (ddy * ddy));

        root2 = (-(dx * ddx + dy * ddy) - Math.sqrt(((dx * ddx + dy * ddy) * (dx * ddx + dy * ddy)) - (((ddx * ddx) + (ddy * ddy)) * (dx * dx + dy * dy - (double) Constants.ball_width * Constants.ball_width)))) / ((ddx * ddx) + (ddy * ddy));
        if (ddx == 0 && ddy == 0) {
            t = 0;
        } else if (-interval < root1 && root1 < 0) {
            t = root1;
        } else if (-interval < root2 && root2 < 0) {
            t = root2;
        } else {
            t = 0;
        }
        return t;
    }

    private static void recalculateBallState(Ball ball1, Ball ball2, double t) {
        double ip_x = ball1.p_x + t * ball1.v_x;
        double ip_y = ball1.p_y + t * ball1.v_y;
        double jp_x = ball2.p_x + t * ball2.v_x;
        double jp_y = ball2.p_y + t * ball2.v_y;

        double dx = ip_x - jp_x;
        double dy = ip_y - jp_y;

        double d2 = dx * dx + dy * dy;
        double kii, kji, kij, kjj;
        kji = (dx * ball1.v_x + dy * ball1.v_y) / d2; // k of j due to i
        kii = (dx * ball1.v_y - dy * ball1.v_x) / d2; // k of i due to i
        kij = (dx * ball2.v_x + dy * ball2.v_y) / d2; // k of i due to j
        kjj = (dx * ball2.v_y - dy * ball2.v_x) / d2; // k of j due to j

        // set velocity of i
        ball1.v_y = kij * dy + kii * dx;
        ball1.v_x = kij * dx - kii * dy;
//        if (ball1.v_x >= Constants.max_velocity_x) ball1.v_x %= Constants.max_velocity_x;
//        if (ball1.v_y >= Constants.max_velocity_y) ball1.v_y %= Constants.max_velocity_y;

        // set velocity of j
        ball2.v_y = kji * dy + kjj * dx;
        ball2.v_x = kji * dx - kjj * dy;
//        if (ball2.v_x >= Constants.max_velocity_x) ball2.v_x %= Constants.max_velocity_x;
//        if (ball2.v_y >= Constants.max_velocity_y) ball2.v_y %= Constants.max_velocity_y;

        ball1.p_x = ip_x + ball1.v_x * (-t);
        ball1.p_y = ip_y + ball1.v_y * (-t);
        ball2.p_x = jp_x + ball2.v_x * (-t);
        ball2.p_y = jp_y + ball2.v_y * (-t);
    }
}
