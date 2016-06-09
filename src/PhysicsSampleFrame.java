import loot.GameFrame;
import loot.GameFrameSettings;
import loot.graphics.DrawableObject;
import model.Ball;
import model.RedBall;
import values.Constants;

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

	/* -------------------------------------------
     *
	 * �ʵ� ���� �κ�
	 * 
	 */

    Ball[] balls = new Ball[Constants.numberOfBalls];                    //ȭ�� ���� �ִ� �� ���
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

        /*
        //�� ���� ���� ��ġ�� ��ġ
        balls[0] = new RedBall(rand.nextInt(settings.canvas_width - ball_width - 2) + 1, rand.nextInt(settings.canvas_height - ball_height - 2) + 1);
        for (int iBall = 1; iBall < balls.length; ++iBall)
            balls[iBall] = new Ball(rand.nextInt(settings.canvas_width - ball_width - 2) + 1, rand.nextInt(settings.canvas_height - ball_height - 2) + 1);
        */
        
        balls[0] = new RedBall(400, 400, images);
        balls[1] = new Ball(455, 455, images);
        balls[2] = new Ball(0, 0, images);
        myInit();

        //FPS ��¿� ����� �� �� ����ü ��������
        LoadColor(Color.black);
        LoadFont("����ü BOLD 24");

        return true;
    }
    
    public void myInit()
    {
    	int redballPref[] = new int[3];
    	int opponentPref[] = new int[3];
    	redballPref[0] = 5;
    	redballPref[1] = 5;
    	redballPref[2] = 5;
    	opponentPref[0] = 0;
    	opponentPref[1] = 0;
    	opponentPref[2] = 0;

    	balls[0].setName(1);
    	balls[0].setSex(0);
    	balls[0].setPreference(redballPref);

    	balls[1].setName(1);
    	balls[1].setSex(1);
    	balls[1].setPreference(opponentPref);
    	
    	balls[2].setName(2);
    	balls[2].setSex(1);
    	balls[2].setPreference(opponentPref);

    	balls[0].myTurn = true;
    	
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

            for (int j = i + 1; j < balls.length; j++) {
            	// ���� �÷����ϴ� ���� ����
            	if(balls[i].myTurn == true) {
            		int sex = balls[i].getSex();
            		// ���� �����̸� ���
            		if (balls[j].getSex() == sex) continue;

            		int preference = balls[j].getPreference(i) - 3;


            		// 3�� �̻� �� ��� ȣ��, �η� ����
            		if(preference >= 0) {
            			double displacement_xx = balls[j].p_x - balls[i].p_x;// - ball_width / 2;
            			double displacement_yy = balls[j].p_y - balls[i].p_y;//- ball_height / 2;
            			double squaredDistance1 = displacement_xx * displacement_xx + displacement_yy * displacement_yy;
            			//double gravitation1 = coef_gravitation/1000 * interval / squaredDistance1;
            			double gravitation1 = Constants.coef_gravitation * interval/50000000;
            			
            			if ( Math.sqrt(squaredDistance1) >= 150 ) continue;

            			if (gravitation1 > Constants.max_gravitation)
            				gravitation1 = Constants.max_gravitation;

            			balls[i].a_x = gravitation1 * displacement_xx / Math.sqrt(squaredDistance1);
            			balls[i].a_y = gravitation1 * displacement_yy / Math.sqrt(squaredDistance1);
            		}
            		// 3�� ���� �� ��� ��ȣ��, ô�� ����
            		else {
            			double displacement_xx = balls[j].p_x - balls[i].p_x;// - ball_width / 2;
            			double displacement_yy = balls[j].p_y - balls[i].p_y;//- ball_height / 2;
            			double squaredDistance1 = displacement_xx * displacement_xx + displacement_yy * displacement_yy;
            			//double gravitation1 = coef_gravitation/1000 * interval / squaredDistance1;
            			double repulsion = Constants.coef_repulsion * (timeStamp - startTime_pressing) / squaredDistance1;
            			
            			if ( Math.sqrt(squaredDistance1) >= 150 ) continue;


            			if (repulsion > Constants.max_repulsion)
            				repulsion = Constants.max_repulsion;

            			balls[i].a_x = -100.0 * repulsion * displacement_xx / Math.sqrt(squaredDistance1);
            			balls[i].a_y = -100.0 * repulsion * displacement_yy / Math.sqrt(squaredDistance1);
            		}

            	}
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
                double displacement_x = inputs.pos_mouseCursor.x - balls[i].p_x - Constants.ball_width / 2;
                double displacement_y = inputs.pos_mouseCursor.y - balls[i].p_y - Constants.ball_height / 2;
                double squaredDistance = displacement_x * displacement_x + displacement_y * displacement_y;
                double gravitation = Constants.coef_gravitation * interval / squaredDistance;

                balls[i].collideWith = Constants.COLLIDE_WITH_INIT;

                if (gravitation > Constants.max_gravitation)
                    gravitation = Constants.max_gravitation;

//                ball.a_x = gravitation * displacement_x / Math.sqrt(squaredDistance);
                if (Math.sqrt(squaredDistance) / 200 > Constants.max_hit_force) {
                    balls[i].a_x = displacement_x / Math.sqrt(squaredDistance) * Constants.max_hit_force;
                    balls[i].a_y = displacement_y / Math.sqrt(squaredDistance) * Constants.max_hit_force;
//                    System.out.println(balls[i].a_x + "," + balls[i].a_y + "," + Math.sqrt(squaredDistance));
                } else {
                    balls[i].a_x = displacement_x / 200;
                    balls[i].a_y = displacement_y / 200;
                }
//                ball.a_y = gravitation * displacement_y / Math.sqrt(squaredDistance);


                //System.out.println(balls[i].a_x + "," + balls[i].a_y);
            }


            //�̹� �����ӿ� ���콺 ��ư�� �ôٸ� ô�� ����
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

			/*for (int j = i + 1; j < balls.length; j++) {
				// ���� �÷����ϴ� ���� ����
				if(balls[i].myTurn == true) {
					int sex = balls[i].getSex();
					// ���� �����̸� ���
					if (balls[j].getSex() == sex) continue;
					
					int preference = balls[j].getPreference(i) - 3;
					
					
					// 3�� �̻� �� ��� ȣ��, �η� ����
					if(preference >= 0) {
						double displacement_x = balls[i].p_x - balls[j].p_x - ball_width / 2;
						double displacement_y = balls[i].p_y - balls[j].p_y - ball_height / 2;
						double squaredDistance = displacement_x * displacement_x + displacement_y * displacement_y;
						double gravitation = coef_gravitation * interval / squaredDistance;

						if (gravitation > max_gravitation)
							gravitation = max_gravitation;

						balls[i].a_x = gravitation * displacement_x / Math.sqrt(squaredDistance);
						balls[i].a_y = gravitation * displacement_y / Math.sqrt(squaredDistance);
					}
						// 3�� ���� �� ��� ��ȣ��, ô�� ����
					else {
						
					}
					
				}
			}*/

            //��Ʈ�� Ű�� ���� ���� �ʴٸ� �ӵ� / ���ӵ� �ݿ�
            if (isPauseRequested == false) {
                //������ ���
                balls[i].a_x += Constants.coef_friction * interval * balls[i].v_x;
                balls[i].a_y += Constants.coef_friction * interval * balls[i].v_y;

                //���ӵ��� �ӵ��� ���� - ���ӵ��� ��� �̸� �ð��� �������Ƿ� ���⼭ �� �������� ����
                balls[i].v_x += balls[i].a_x;
                balls[i].v_y += balls[i].a_y;

                //�� ���������� â �����ڸ��� �ε����� �ݻ��ϹǷ� �ӵ��� ���밪�� Ư�� ������ �۾������� ����
                balls[i].v_x %= Constants.max_velocity_x;
                balls[i].v_y %= Constants.max_velocity_y;

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
                        balls[i].collideWith = Constants.numberOfBalls;
                    }

                    if (balls[i].p_x >= settings.canvas_width - Constants.ball_width) {
                        balls[i].v_x = -balls[i].v_x;
                        balls[i].p_x = 2 * (settings.canvas_width - Constants.ball_width) - balls[i].p_x;
                        isWithinCanvas = false;
                        balls[i].collideWith = Constants.numberOfBalls + 1;
                    }

                    if (balls[i].p_y < 0) {
                        balls[i].v_y = -balls[i].v_y;
                        balls[i].p_y = -balls[i].p_y;
                        isWithinCanvas = false;
                        balls[i].collideWith = Constants.numberOfBalls + 2;
                    }

                    if (balls[i].p_y >= settings.canvas_height - Constants.ball_height) {
                        balls[i].v_y = -balls[i].v_y;
                        balls[i].p_y = 2 * (settings.canvas_height - Constants.ball_height) - balls[i].p_y;
                        isWithinCanvas = false;
                        balls[i].collideWith = Constants.numberOfBalls + 3;
                    }
                }
                while (isWithinCanvas == false);

                //����������, ���� ��ġ�� ������� �ش� ���� �׸� �ȼ��� ����
//                ball.x = (int) ball.p_x;
//                ball.y = (int) ball.p_y;

                for (int j = i + 1; j < balls.length; j++) {
                    if (calcCollision(balls[i], balls[j])
//                            && (balls[i].collideWith != j || balls[j].collideWith != i)) {
                    		){
                        balls[i].collideWith = j;
                        balls[j].collideWith = i;
                        //System.out.println(i + "," + j);
//                        System.out.println(i + "," + j);
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
        return d2 < Constants.ball_width * Constants.ball_width;
    }

    //������ �ʿ���
    public void fixOverlap(Ball i, Ball j, double interval) {
        double dx = i.p_x - j.p_x;
        double dy = i.p_y - j.p_y;
        double ddx = i.v_x - j.v_x;
        double ddy = i.v_y - j.v_y;

        double root1, root2;
        double t;

        root1 = (-(dx * ddx + dy * ddy) + Math.sqrt((dx * ddx + dy * ddy) * (dx * ddx + dy * ddy) - ((ddx * ddx) + (ddy * ddy)) * (dx * dx + dy * dy - (double) Constants.ball_width * Constants.ball_width))) / ((ddx * ddx)  +  (ddy * ddy));

        root2 = (-(dx * ddx + dy * ddy) - Math.sqrt(((dx * ddx + dy * ddy) * (dx * ddx + dy * ddy)) - (((ddx * ddx) + (ddy * ddy)) * (dx * dx + dy * dy - (double) Constants.ball_width * Constants.ball_width)))) / ((ddx * ddx) + (ddy * ddy));
//        t = ((dx * ddx + dy * ddy) - Math.sqrt((dx * ddx + dy * ddy) * (dx * ddx + dy * ddy) - ((ddx * ddx) + (ddy * ddy)) * (dx * dx + dy * dy - (double) ball_width * ball_width))) / ((ddx * ddx) * (ddx * ddx) + (ddy * ddy) * (ddy * ddy));
        //check if root is -interval < root1 && root1 < 0
        if (ddx == 0 && ddy == 0) {
            t = 0;
//            System.out.println(0);
        } else if (-interval < root1 && root1 < 0) {
            t = root1;
//            System.out.println(1);
        } else if (-interval < root2 && root2 < 0) {
            t = root2;
//            System.out.println(2);
        } else {
            t = 0;
//            System.out.println(3);
        }

//        System.out.println(i);
//        System.out.println(j);
//        System.out.println("root1: "+root1);
//        System.out.println("root2: "+root2);
//        System.out.println("distance:"+((ddx * ddx) * (ddx * ddx) + (ddy * ddy) * (ddy * ddy)));
//        System.out.println("t: " + t);
//        System.out.println("interval: "+interval);

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
        if (i.v_x >= Constants.max_velocity_x) i.v_x %= Constants.max_velocity_x;
        if (i.v_y >= Constants.max_velocity_y) i.v_y %= Constants.max_velocity_y;

        // set velocity of j
        j.v_y = kji * dy + kjj * dx;
        j.v_x = kji * dx - kjj * dy;
        if (j.v_x >= Constants.max_velocity_x) j.v_x %= Constants.max_velocity_x;
        if (j.v_y >= Constants.max_velocity_y) j.v_y %= Constants.max_velocity_y;

        i.p_x = ip_x + i.v_x * (-t);
        i.p_y = ip_y + i.v_y * (-t);
        j.p_x = jp_x + j.v_x * (-t);
        j.p_y = jp_y + j.v_y * (-t);
    }
}
