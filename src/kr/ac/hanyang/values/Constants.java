package kr.ac.hanyang.values;

/**
 * Created by blainechai on 2016. 6. 9..
 */
public class Constants {
    //static final int numberOfBalls = 100;                //화면 내에 있는 공의 수(너무 많으면 FPS 저하의 원인이 됨)
    public static final int numberOfBalls = 7;                //화면 내에 있는 공의 수(너무 많으면 FPS 저하의 원인이 됨)
    public static final int ball_width = 50;                    //공 하나의 가로 길이(단위는 픽셀)
    public static final int ball_height = 50;                    //공 하나의 세로 길이(단위는 픽셀)
    public static final int effect_width = 50;
    public static final int effect_height = 50;
    public static final int TABLE_HEIGHT = 600;


    public static final double max_hit_force = 1;            //인력의 최대값(단위는 픽셀/ms^2)
    public static final double max_gravitation = 10;            //인력의 최대값(단위는 픽셀/ms^2)
    public static final double max_repulsion = 50.0;            //척력의 최대값(단위는 픽셀/ms^2)
    public static final double max_velocity_x = 3;            //X방향 속력의 최대값(단위는 픽셀/ms)
    public static final double max_velocity_y = 3;            //X방향 속력의 최대값(단위는 픽셀/ms)

    public static final double coef_gravitation = 10000.0;        //인력을 계산할 때 사용하는 계수
    public static final double coef_repulsion = 0.1;            //척력을 계산할 때 사용하는 계수
    //    static final double coef_friction = -0.0001;            //마찰력을 적용하기 위한 계수(속도에 이 값을 곱한 만큼이 마찰력이 됨. 따라서 이 값은 음수여야 함. 마찰력의 단위는 픽셀/ms^2)
    public static final double coef_friction = -0.0005;            //마찰력을 적용하기 위한 계수(속도에 이 값을 곱한 만큼이 마찰력이 됨. 따라서 이 값은 음수여야 함. 마찰력의 단위는 픽셀/ms^2)

    public static final int COLLIDE_WITH_INIT = Integer.MAX_VALUE;

    public static final int M = 0;
    public static final int W = 1;

    public static final String REDBALL_FILE_NAME_DEFAULT = "redball";
}
