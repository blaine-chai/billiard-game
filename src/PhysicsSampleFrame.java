import loot.GameFrame;
import loot.GameFrameSettings;
import loot.graphics.DrawableObject;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * LOOT ���̺귯���� ������ ���� ȯ��� �Բ� ����� �����Դϴ�.
 *
 * @author Racin
 */
@SuppressWarnings("serial")
public class PhysicsSampleFrame extends GameFrame {
    /*
     * ����:
	 * 
	 * �����̽� ��:
	 * 
	 * 		���� ���� ��� ���� �ӵ��� 0���� ����
	 * 
	 * ��Ʈ�� Ű:
	 * 
	 * 		������ ���� ��� ���� �ӵ� / ���ӵ��� �Ͻ������� 0���� ����
	 * 
	 * ���콺 ���� ��ư:
	 * 
	 * 		������ �ִ� ���� ���콺 ������ ��ġ�� ���ϴ� �η� ����
	 * 		���� ����, �׵��� ������ �ִ� �ð��� ����Ͽ� ô�� ����
	 * 
	 */

	/* -------------------------------------------
     *
	 * ��� �� ���� Ŭ���� ���� �κ�
	 * 
	 */

    static final int numberOfBalls = 100;                //ȭ�� ���� �ִ� ���� ��(�ʹ� ������ FPS ������ ������ ��)
    static final int ball_width = 30;                    //�� �ϳ��� ���� ����(������ �ȼ�)
    static final int ball_height = 30;                    //�� �ϳ��� ���� ����(������ �ȼ�)

    static final double max_gravitation = 10;            //�η��� �ִ밪(������ �ȼ�/ms^2)
    static final double max_repulsion = 50.0;            //ô���� �ִ밪(������ �ȼ�/ms^2)
    static final double max_velocity_x = 50;            //X���� �ӷ��� �ִ밪(������ �ȼ�/ms)
    static final double max_velocity_y = 50;            //X���� �ӷ��� �ִ밪(������ �ȼ�/ms)

    static final double coef_gravitation = 300.0;        //�η��� ����� �� ����ϴ� ���
    static final double coef_repulsion = 0.1;            //ô���� ����� �� ����ϴ� ���
    //    static final double coef_friction = -0.0001;            //�������� �����ϱ� ���� ���(�ӵ��� �� ���� ���� ��ŭ�� �������� ��. ���� �� ���� �������� ��. �������� ������ �ȼ�/ms^2)
    static final double coef_friction = -0.0005;            //�������� �����ϱ� ���� ���(�ӵ��� �� ���� ���� ��ŭ�� �������� ��. ���� �� ���� �������� ��. �������� ������ �ȼ�/ms^2)

    static final int COLLIDE_WITH_INIT = Integer.MAX_VALUE;

    /**
     * '��' �ϳ��� ǥ���ϴ� Ŭ�����Դϴ�.<br>
     * LOOT ���̺귯���� �ִ� DrawableObject class�� ��ӹ޾�<br>
     * ��ġ, �ӵ�, ���ӵ� ���� �߰��� �����Ͽ� ����մϴ�.<br>
     * <br>
     * ������ x, y�� int �����̶� �Ҽ��� ���� ������ ���� �� ���⿡<br>
     * ���� ȯ�濡�� ����ϱ⿡�� �������� �ʽ��ϴ�.<br>
     * �׷��Ƿ� p_x, p_y�� ������ �����Ͽ� ��ġ ������ �ٷ��<br>
     * �� �����Ӹ��� p_x, p_y�� ���� �����Ͽ� x, y�� ��� �� ���� 'ȭ����� ��ġ'�� �����մϴ�.
     *
     * @author Racin
     */
    public class Ball extends DrawableObject {
        public double p_x;
        public double p_y;
        public double v_x;
        public double v_y;
        public double a_x;
        public double a_y;
        public int collideWith = COLLIDE_WITH_INIT;

        public Ball(int x, int y) {
            super(x, y, ball_width, ball_height, images.GetImage("ball"));
            p_x = x;
            p_y = y;
        }

        public Ball(int x, int y, String imageName) {
            super(x, y, ball_width, ball_height, images.GetImage(imageName));
            p_x = x;
            p_y = y;
        }

        @Override
        public String toString() {
            return "p_x : " + p_x + "\n" + "p_y : " + p_y + "\n" + "v_x : " + v_x + "\n" + "v_y : " + v_y + "\n" + "a_y : " + a_y + "\n" + "a_y : " + a_y + "\n" + "x : " + x + "\n" + "y : " + y;
        }
    }

    public class RedBall extends Ball {
        public RedBall(int x, int y) {
            super(x, y, "redball");
        }
    }

	/* -------------------------------------------
     *
	 * �ʵ� ���� �κ�
	 * 
	 */

    Ball[] balls = new Ball[numberOfBalls];                    //ȭ�� ���� �ִ� �� ���
    long startTime_pressing;                                //���콺 ���� ��ư�� ������ ������ �ð�

    long timeStamp_firstFrame = 0;                            //ù �������� timeStamp -> ���� ���ķ� ����� �ð� ��꿡 ���
    long timeStamp_lastFrame = 0;                            //���� �������� timeStamp -> ������ ��꿡 ���

	
	/* -------------------------------------------
     *
	 * �޼��� ���� �κ�
	 * 
	 */

    public PhysicsSampleFrame(GameFrameSettings settings) {
        super(settings);

        inputs.BindKey(KeyEvent.VK_SPACE, 0);                //�����̽� �ٸ� ���� ���� ��� ���� �ӵ��� 0�� ��
        inputs.BindKey(KeyEvent.VK_CONTROL, 1);                //��Ʈ�� Ű�� ������ ���� ��� ���� �ӵ� / ���ӵ��� �Ͻ������� 0�� ��
        inputs.BindMouseButton(MouseEvent.BUTTON1, 2);        //���콺 ���� ��ư�� ������ ���� �η� �ۿ�, ���� ���� ô�� �ۿ�

        images.LoadImage("Images/ball_fixed.png", "ball");
        images.LoadImage("Images/ball2.png", "redball");
    }

    @Override
    public boolean Initialize() {
        Random rand = new Random();

        //�� ���� ���� ��ġ�� ��ġ
        balls[0] = new RedBall(rand.nextInt(settings.canvas_width - ball_width - 2) + 1, rand.nextInt(settings.canvas_height - ball_height - 2) + 1);
        for (int iBall = 1; iBall < balls.length; ++iBall)
            balls[iBall] = new Ball(rand.nextInt(settings.canvas_width - ball_width - 2) + 1, rand.nextInt(settings.canvas_height - ball_height - 2) + 1);

        //FPS ��¿� ����� �� �� ����ü ��������
        LoadColor(Color.black);
        LoadFont("����ü BOLD 24");

        return true;
    }

    @Override
    public boolean Update(long timeStamp) {
        /*
         * �Է� ó��
		 */

        //�Է��� ��ư�� �ݿ�. �� �޼���� �׻� Update()�� ���� �κп��� ȣ���� �־�� ��
        inputs.AcceptInputs();

        //�� ��ư�� ���¸� �˻��Ͽ� �� ���� � �۾��� �����ؾ� �ϴ��� üũ
        boolean isStopRequested;
        boolean isPauseRequested;
        boolean isGravitationRequested;
        boolean isRepulsionRequested;

        //�̹� �����ӿ� �����̽� �ٸ� �����ٸ� ��� ���� �ӵ��� 0���� ����
        isStopRequested = inputs.buttons[0].IsPressedNow();

        //��Ʈ�� Ű�� ������ �ִٸ� ��� ���� �ӵ� �� ���ӵ��� �Ͻ������� 0���� ����
        isPauseRequested = inputs.buttons[1].isPressed;

        //�̹� �����ӿ� ���콺 ��ư�� �����ٸ� ���� �ð� ��� -> ô�� ��꿡 ����
        if (inputs.buttons[2].IsPressedNow() == true)
            startTime_pressing = timeStamp;

        //���콺 ��ư�� ������ �ִٸ� �η� ����
//        isGravitationRequested = inputs.buttons[2].isPressed;

        //�̹� �����ӿ� ���콺 ��ư�� �ôٸ� ô�� ����
        isRepulsionRequested = inputs.buttons[2].IsReleasedNow();
//        isRepulsionRequested = inputs.buttons[2].isPressed;

		/*
         * �Է� ����
		 * 
		 * -	�Է� ���� �۾��� �ڵ� ������ ���� ������ �� ������
		 * 		�Ϲ����� ��� �̷��� �̸� �˻� �� ���� �۾��� �����ϴ°� ���� ������ ������ ��
		 */

        //��Ʈ�� Ű�� ������ �ִٸ� ���� �η� / ô���� ����� �ʿ䰡 �����Ƿ� �ش� ���� �缳��
        if (isPauseRequested == true) {
            isGravitationRequested = false;
            isRepulsionRequested = false;
        }

        //(���� ��Ģ���� ���� ������) �� �� ���� ������ �����ϱ� ���� ô���� �ۿ��ϱ� ������ ��� ���� ���� �ӵ��� 0���� ����
        if (isRepulsionRequested == true)
            isStopRequested = true;

		/*
         * �Է� ����
		 */

        //���� ������ ���ķ� ����� �ð� ����
        double interval = timeStamp - timeStamp_lastFrame;

        //��� ���� ����

        for (int i = 0; i < balls.length; i++) {
            balls[i].a_x = 0;
            balls[i].a_y = 0;

            //�̹� �����ӿ� �����̽� �ٸ� �����ٸ� �ӵ��� 0���� ����
            if (isStopRequested == true) {
                balls[i].v_x = 0;
                balls[i].v_y = 0;
            }

            //���콺 ��ư�� ������ �ִٸ� �η� ����
            /*if (isGravitationRequested == true&&ball.equals(balls[0])ball.equals(balls[0])) {
                double displacement_x = inputs.pos_mouseCursor.x - ball.p_x - ball_width / 2;
                double displacement_y = inputs.pos_mouseCursor.y - ball.p_y - ball_height / 2;
                double squaredDistance = displacement_x * displacement_x + displacement_y * displacement_y;
                double gravitation = coef_gravitation * interval / squaredDistance;

                if (gravitation > max_gravitation)
                    gravitation = max_gravitation;

                ball.a_x = gravitation * displacement_x / Math.sqrt(squaredDistance);
                ball.a_y = gravitation * displacement_y / Math.sqrt(squaredDistance);
            }*/

            //���콺 ��ư�� ���� �� ����
            if (isRepulsionRequested == true && balls[i].equals(balls[0])) {
                double displacement_x = inputs.pos_mouseCursor.x - balls[i].p_x - ball_width / 2;
                double displacement_y = inputs.pos_mouseCursor.y - balls[i].p_y - ball_height / 2;
                double squaredDistance = displacement_x * displacement_x + displacement_y * displacement_y;
                double gravitation = coef_gravitation * interval / squaredDistance;

                balls[i].collideWith = COLLIDE_WITH_INIT;

                if (gravitation > max_gravitation)
                    gravitation = max_gravitation;

//                ball.a_x = gravitation * displacement_x / Math.sqrt(squaredDistance);
                balls[i].a_x = displacement_x / 200;
//                ball.a_y = gravitation * displacement_y / Math.sqrt(squaredDistance);
                balls[i].a_y = displacement_y / 200;

                System.out.println(balls[i].a_x + "," + balls[i].a_y);
            }


            //�̹� �����ӿ� ���콺 ��ư�� �ôٸ� ô�� ����ddd
            /*if (isRepulsionRequested == true && ball.equals(balls[0])) {
                double displacement_x = inputs.pos_mouseCursor.x - ball.p_x - ball_width / 2;
                double displacement_y = inputs.pos_mouseCursor.y - ball.p_y - ball_height / 2;
                double squaredDistance = displacement_x * displacement_x + displacement_y * displacement_y;
                double repulsion = coef_repulsion * (timeStamp - startTime_pressing) / squaredDistance;

                if (repulsion > max_repulsion)
                    repulsion = max_repulsion;

//                ball.a_x = -1.0 * repulsion * displacement_x / Math.sqrt(squaredDistance);
                ball.a_x = -100.0 * repulsion * displacement_x / Math.sqrt(squaredDistance);
//                ball.a_y = -1.0 * repulsion * displacement_y / Math.sqrt(squaredDistance);
                ball.a_y = -100.0 * repulsion * displacement_y / Math.sqrt(squaredDistance);
            }*/

            //��Ʈ�� Ű�� ���� ���� �ʴٸ� �ӵ� / ���ӵ� �ݿ�
            if (isPauseRequested == false) {
                //������ ���
                balls[i].a_x += coef_friction * interval * balls[i].v_x;
                balls[i].a_y += coef_friction * interval * balls[i].v_y;

                //���ӵ��� �ӵ��� ���� - ���ӵ��� ��� �̸� �ð��� �������Ƿ� ���⼭ �� �������� ����
                balls[i].v_x += balls[i].a_x;
                balls[i].v_y += balls[i].a_y;

                //�� ���������� â �����ڸ��� �ε����� �ݻ��ϹǷ� �ӵ��� ���밪�� Ư�� ������ �۾������� ����
                balls[i].v_x %= max_velocity_x;
                balls[i].v_y %= max_velocity_y;

                //�ӵ��� ��ġ�� ���� - �� ���� �ð��� ���Ͽ� ����
                balls[i].p_x += balls[i].v_x * interval;
                balls[i].p_y += balls[i].v_y * interval;

				/*
                 * �ݻ� üũ
				 * 
				 * -	������ �浹�� �Ͼ �ð��� ���� �ľ��Ͽ� ���ӵ��� �ݻ� ��/�ķ� ������ �����ؾ� ������
				 * 		������ ȯ��(Ư��, �����°� ���� �׷��� �����ϴ� ȯ��)������ �׷��� ���� �ʾƵ� ū ������ ����
				 */
                boolean isWithinCanvas = true;


                do {
                    isWithinCanvas = true;
                    if (balls[i].p_x < 0) {
                        balls[i].v_x = -balls[i].v_x;
                        balls[i].p_x = -balls[i].p_x;
                        isWithinCanvas = false;
                        balls[i].collideWith = numberOfBalls;
                    }

                    if (balls[i].p_x >= settings.canvas_width - ball_width) {
                        balls[i].v_x = -balls[i].v_x;
                        balls[i].p_x = 2 * (settings.canvas_width - ball_width) - balls[i].p_x;
                        isWithinCanvas = false;
                        balls[i].collideWith = numberOfBalls + 1;
                    }

                    if (balls[i].p_y < 0) {
                        balls[i].v_y = -balls[i].v_y;
                        balls[i].p_y = -balls[i].p_y;
                        isWithinCanvas = false;
                        balls[i].collideWith = numberOfBalls + 2;
                    }

                    if (balls[i].p_y >= settings.canvas_height - ball_height) {
                        balls[i].v_y = -balls[i].v_y;
                        balls[i].p_y = 2 * (settings.canvas_height - ball_height) - balls[i].p_y;
                        isWithinCanvas = false;
                        balls[i].collideWith = numberOfBalls + 3;
                    }
                }
                while (isWithinCanvas == false);

                //����������, ���� ��ġ�� ������� �ش� ���� �׸� �ȼ��� ����
//                ball.x = (int) ball.p_x;
//                ball.y = (int) ball.p_y;

                for (int j = i + 1; j < balls.length; j++) {
                    if (calcCollision(balls[i], balls[j])
                            && (balls[i].collideWith != j || balls[j].collideWith != i)) {
                        balls[i].collideWith = j;
                        balls[j].collideWith = i;
                        System.out.println(i + "," + j);
                        fixOverlap(balls[i], balls[j], interval);
                    }
                }

                balls[i].x = (int) balls[i].p_x;
                balls[i].y = (int) balls[i].p_y;
            }
        }
        //�̹��� ù �������̾��ٸ� ���� �ð� ���
        if (timeStamp_firstFrame == 0)
            timeStamp_firstFrame = timeStamp;

        //���� '���� ������'�� �� �̹� �������� ���� �ð� ���
        timeStamp_lastFrame = timeStamp;

        return true;
    }

    @Override
    public void Draw(long timeStamp) {
        //�׸��� �۾� ���� - �� �޼���� Draw()�� ���� ������ �׻� ȣ���� �־�� ��
        BeginDraw();

        //ȭ���� �ٽ� �������� ä��
        ClearScreen();

        for (Ball ball : balls)
            ball.Draw(g);

        DrawString(24, 48, "FPS:  %.2f", loop.GetFPS());
        DrawString(24, 78, "Time: %dms", (int) (timeStamp_lastFrame - timeStamp_firstFrame));

        //�׸��� �۾� �� - �� �޼���� Draw()�� ���� �Ʒ����� �׻� ȣ���� �־�� ��
        EndDraw();
    }

    public boolean calcCollision(Ball i, Ball j) {
        double dx = i.p_x - j.p_x;
        double dy = i.p_y - j.p_y;

        double d2 = dx * dx + dy * dy;
        return d2 < ball_width * ball_width;
    }

    //������ �ʿ���
    public void fixOverlap(Ball i, Ball j, double interval) {
        double dx = i.p_x - j.p_x;
        double dy = i.p_y - j.p_y;
        double ddx = i.p_x - j.v_x;
        double ddy = i.p_y - j.v_y;

//        double root1, root2;
        double t;

//        root1 = ((dx * ddx + dy * ddy) + Math.sqrt((dx * ddx + dy * ddy) * (dx * ddx + dy * ddy) - ((ddx * ddx) + (ddy * ddy)) * (dx * dx + dy * dy - (double) ball_width * ball_width))) / ((ddx * ddx) * (ddx * ddx) + (ddy * ddy) * (ddy * ddy));

//        root2 = ((dx * ddx + dy * ddy) - Math.sqrt((dx * ddx + dy * ddy) * (dx * ddx + dy * ddy) - ((ddx * ddx) + (ddy * ddy)) * (dx * dx + dy * dy - (double) ball_width * ball_width))) / ((ddx * ddx) * (ddx * ddx) + (ddy * ddy) * (ddy * ddy));
        t = ((dx * ddx + dy * ddy) - Math.sqrt((dx * ddx + dy * ddy) * (dx * ddx + dy * ddy) - ((ddx * ddx) + (ddy * ddy)) * (dx * dx + dy * dy - (double) ball_width * ball_width))) / ((ddx * ddx) * (ddx * ddx) + (ddy * ddy) * (ddy * ddy));
        //check if root is -interval < root1 && root1 < 0
        /*if (-interval < root1 && root1 < 0) {
            t = root1;
            System.out.println(1);
        } else if (-interval < root2 && root2 < 0) {
            t = root2;
            System.out.println(2);
        } else*/
//            t = root2;

//        System.out.println(i);
//        System.out.println(j);
//        System.out.println(t);
//        System.out.println(interval);

        double ip_x = i.p_x + t * i.v_x;
        double ip_y = i.p_y + t * i.v_y;
        double jp_x = j.p_x + t * j.v_x;
        double jp_y = j.p_y + t * j.v_y;

        dx = ip_x - jp_x;
        dy = ip_y - jp_y;

        double d2 = dx * dx + dy * dy;
        double kii, kji, kij, kjj;
        kji = (dx * i.v_x + dy * i.v_y) / d2; // k of j due to i
        kii = (dx * i.v_y - dy * i.v_x) / d2; // k of i due to i
        kij = (dx * j.v_x + dy * j.v_y) / d2; // k of i due to j
        kjj = (dx * j.v_y - dy * j.v_x) / d2; // k of j due to j

        // set velocity of i
        i.v_y = kij * dy + kii * dx;
        i.v_x = kij * dx - kii * dy;

        // set velocity of j
        j.v_y = kji * dy + kjj * dx;
        j.v_x = kji * dx - kjj * dy;
    }
}
