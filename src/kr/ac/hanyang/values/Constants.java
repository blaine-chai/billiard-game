package kr.ac.hanyang.values;

/**
 * Created by blainechai on 2016. 6. 9..
 */
public class Constants {
    //static final int numberOfBalls = 100;                //ȭ�� ���� �ִ� ���� ��(�ʹ� ������ FPS ������ ������ ��)
    public static final int numberOfBalls = 7;                //ȭ�� ���� �ִ� ���� ��(�ʹ� ������ FPS ������ ������ ��)
    public static final int ball_width = 50;                    //�� �ϳ��� ���� ����(������ �ȼ�)
    public static final int ball_height = 50;                    //�� �ϳ��� ���� ����(������ �ȼ�)
    public static final int effect_width = 50;
    public static final int effect_height = 50;
    public static final int TABLE_HEIGHT = 600;


    public static final double max_hit_force = 1;            //�η��� �ִ밪(������ �ȼ�/ms^2)
    public static final double max_gravitation = 10;            //�η��� �ִ밪(������ �ȼ�/ms^2)
    public static final double max_repulsion = 50.0;            //ô���� �ִ밪(������ �ȼ�/ms^2)
    public static final double max_velocity_x = 3;            //X���� �ӷ��� �ִ밪(������ �ȼ�/ms)
    public static final double max_velocity_y = 3;            //X���� �ӷ��� �ִ밪(������ �ȼ�/ms)

    public static final double coef_gravitation = 10000.0;        //�η��� ����� �� ����ϴ� ���
    public static final double coef_repulsion = 0.1;            //ô���� ����� �� ����ϴ� ���
    //    static final double coef_friction = -0.0001;            //�������� �����ϱ� ���� ���(�ӵ��� �� ���� ���� ��ŭ�� �������� ��. ���� �� ���� �������� ��. �������� ������ �ȼ�/ms^2)
    public static final double coef_friction = -0.0005;            //�������� �����ϱ� ���� ���(�ӵ��� �� ���� ���� ��ŭ�� �������� ��. ���� �� ���� �������� ��. �������� ������ �ȼ�/ms^2)

    public static final int COLLIDE_WITH_INIT = Integer.MAX_VALUE;

    public static final int M = 0;
    public static final int W = 1;

    public static final String REDBALL_FILE_NAME_DEFAULT = "redball";
}
