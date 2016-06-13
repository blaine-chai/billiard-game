package kr.ac.hanyang.frame;

import kr.ac.hanyang.GameFrameManager;
import kr.ac.hanyang.model.Ball;
import kr.ac.hanyang.util.PhysicsUtil;
import kr.ac.hanyang.values.Constants;
import loot.GameFrame;
import loot.GameFrameSettings;
import loot.ImageResourceManager;
import loot.graphics.DrawableObject;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * LOOT ���̺귯���� ������ ���� ȯ��� �Բ� ����� �����Դϴ�.
 *
 * @author Racin
 */
@SuppressWarnings("serial")
public class PhysicsFrame extends GameFrame {
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

    ArrayList<Ball> playBalls = new ArrayList<>();
    ArrayList<MatchedBall> matchedBalls = new ArrayList<>();
    ArrayList<CollideEffect> effectArrayList = new ArrayList<>();
    Ball[] balls = new Ball[Constants.numberOfBalls];                    //ȭ�� ���� �ִ� �� ���
    Ball[] players = new Ball[Constants.numberOfBalls];
    long startTime_pressing;                                //���콺 ���� ��ư�� ������ ������ �ð�

    long timeStamp_firstFrame = 0;                            //ù �������� timeStamp -> ���� ���ķ� ����� �ð� ��꿡 ���
    long timeStamp_lastFrame = 0;                            //���� �������� timeStamp -> ������ ��꿡 ���

    static int turn = 0;

    Ball curTurnBall;

    GameFrameManager gm;
    DrawableObject footer;

    private int preference[][];

    /* -------------------------------------------
     *
	 * �޼��� ���� �κ�
	 * 
	 */

    public PhysicsFrame(GameFrameSettings settings, GameFrameManager gm, int[][] preference) {
        super(settings);
        this.gm = gm;
        inputs.BindKey(KeyEvent.VK_SPACE, 0);                //�����̽� �ٸ� ���� ���� ��� ���� �ӵ��� 0�� ��
        inputs.BindKey(KeyEvent.VK_CONTROL, 1);                //��Ʈ�� Ű�� ������ ���� ��� ���� �ӵ� / ���ӵ��� �Ͻ������� 0�� ��
        inputs.BindMouseButton(MouseEvent.BUTTON1, 2);        //���콺 ���� ��ư�� ������ ���� �η� �ۿ�, ���� ���� ô�� �ۿ�

        images.LoadImage("Images/ball_fixed.png", "ball");
        images.LoadImage("Images/ball2.png", "redball");
        images.LoadImage("Images/collide_effect.png", "collide_effect");
        images.LoadImage("Images/bin2.png", "bin2");
        images.LoadImage("Images/joongki2.png", "joongki2");
        images.LoadImage("Images/minho2.png", "minho2");
        images.LoadImage("Images/sejung2.png", "sejung2");
        images.LoadImage("Images/suji2.png", "suji2");
        images.LoadImage("Images/sulhyun2.png", "sulhyun2");
        images.LoadImage("Images/footer.png", "footer");


        this.preference = preference;
    }

    @Override
    public boolean Initialize() {
//        Random rand = new Random();

        /*
        //�� ���� ���� ��ġ�� ��ġ
        balls[0] = new RedBall(rand.nextInt(settings.canvas_width - ball_width - 2) + 1, rand.nextInt(settings.canvas_height - ball_height - 2) + 1);
        for (int iBall = 1; iBall < balls.length; ++iBall)
            balls[iBall] = new Ball(rand.nextInt(settings.canvas_width - ball_width - 2) + 1, rand.nextInt(settings.canvas_height - ball_height - 2) + 1);
        */
        // balls[0] = new RedBall(400, 400, images);
        //balls[1] = new Ball(455, 455, images);
        //balls[2] = new Ball(0, 0, images);

        balls[0] = new Ball(10, 10, "redball", images);
        balls[1] = new Ball(150, 200, "bin2", images);
        balls[2] = new Ball(350, 200, "joongki2", images);
        balls[3] = new Ball(550, 200, "minho2", images);
        balls[4] = new Ball(150, 500, "sejung2", images);
        balls[5] = new Ball(350, 500, "suji2", images);
        balls[6] = new Ball(550, 500, "sulhyun2", images);
        footer = new DrawableObject(0, 600, 800, 200, images.GetImage("footer"));

        myInit();

        //FPS ��¿� ����� �� �� ����ü ��������
        LoadColor(Color.black);
        LoadFont("����ü BOLD 24");

        LoadColor(Color.black);
        LoadFont("����ü BOLD 24");
        LoadFont("������� BOLD 24");

//        Collections.addAll(playBalls, balls);

//        playBalls.addAll(balls);
        for (int i = 0; i < balls.length; i++) {
            playBalls.add(balls[i]);
        }


        return true;
    }

    public void myInit() {
        int redballPref[] = new int[3];
        int opponentPref[] = new int[3];
        int binPref[] = new int[3];
        redballPref[0] = 0;
        redballPref[1] = 0;
        redballPref[2] = 0;
        opponentPref[0] = 0;
        opponentPref[1] = 0;
        opponentPref[2] = 0;
//        redballPref[0] = 5;
//        redballPref[1] = 5;
//        redballPref[2] = 5;
//        opponentPref[0] = 0;
//        opponentPref[1] = 0;
//        opponentPref[2] = 0;


        balls[0].setName(1);
        balls[0].setSex(Constants.M);
        balls[0].setPreference(redballPref);

        balls[1].setName(1);
        balls[1].setSex(Constants.M);
        balls[1].setPreference(opponentPref);

        balls[2].setName(2);
        balls[2].setSex(Constants.W);
        balls[2].setPreference(opponentPref);

        balls[3].setName(2);
        balls[3].setSex(Constants.W);
        balls[3].setPreference(opponentPref);


        balls[0].myTurn = false;
        curTurnBall = balls[1];

    }

    public boolean isAllStop() {
        boolean ret = true;

        // ��� ���� ���� �����̴� ���� �ִ��� �Ǵ�.
        for (int i = 0; i < balls.length; i++) {
            if (balls[i].v_x > 0.001 || balls[i].v_y > 0.001) ret = false;
        }

        return ret;
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
        boolean isRepulsionRequested = false;

        //�̹� �����ӿ� �����̽� �ٸ� �����ٸ� ��� ���� �ӵ��� 0���� ����
        isStopRequested = inputs.buttons[0].IsPressedNow();

        //��Ʈ�� Ű�� ������ �ִٸ� ��� ���� �ӵ� �� ���ӵ��� �Ͻ������� 0���� ����
        isPauseRequested = inputs.buttons[1].isPressed;
//        System.out.println(playBalls.size());
        if (isAllStop() && curTurnBall != null && curTurnBall.collideWithSet.size() == 2) {
//            System.out.println(curTurnBall.getName());
            Iterator<Integer> it = curTurnBall.collideWithSet.iterator();
            it.next();
            int i = it.next();

            if (curTurnBall.collideWithSet.contains(0)) {
//                System.out.println(curTurnBall.collideWithSet.toArray().length);
                Ball matchedBall = null;
                for(int j =0;j<playBalls.size();j++){
                    if(playBalls.get(j).id == i){
                        matchedBall = playBalls.get(j);
                    }
                }
                matchedBalls.add(new MatchedBall(curTurnBall, matchedBall));
                playBalls.remove(curTurnBall);
                playBalls.remove(matchedBall);
//                System.out.println(playBalls.size());
//                System.out.println(playBalls.remove(playBalls.get((Integer) curTurnBall.collideWithSet.toArray()[1])));
//                System.out.println(playBalls.remove(curTurnBall));
//                System.out.println(playBalls.size());
//                System.out.println(matchedBalls.size());
                curTurnBall.myTurn = false;

                System.out.println("hi");
                for (Ball ball : playBalls) {
                    ball.collideWithSet.clear();
                }
            }
        }

        if (isAllStop() == true && inputs.buttons[2].IsReleasedNow()) {
            isRepulsionRequested = true;
            System.out.println(turn);
            turn++;
            curTurnBall.myTurn = false;
            curTurnBall = playBalls.get(turn % (playBalls.size()));
            System.out.println(curTurnBall);
            curTurnBall.myTurn = true;
//            System.out.println(turn);
        } else {
            isPauseRequested = false;
        }
//        isRepulsionRequested = inputs.buttons[2].isPressed;

		/*
         * �Է� ����
		 * 
		 * -	�Է� ���� �۾��� �ڵ� ������ ���� ������ �� ������
		 * 		�Ϲ����� ��� �̷��� �̸� �˻� �� ���� �۾��� �����ϴ°� ���� ������ ������ ��
		 */

        //��Ʈ�� Ű�� ������ �ִٸ� ���� �η� / ô���� ����� �ʿ䰡 �����Ƿ� �ش� ���� �缳��
        if (isPauseRequested == true) {
//            isGravitationRequested = false;
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

        for (int i = 0; i < playBalls.size(); i++) {
            playBalls.get(i).a_x = 0;
            playBalls.get(i).a_y = 0;

            //�̹� �����ӿ� �����̽� �ٸ� �����ٸ� �ӵ��� 0���� ����
            if (isStopRequested == true) {
                playBalls.get(i).v_x = 0;
                playBalls.get(i).v_y = 0;
            }

            for (int j = i + 1; j < playBalls.size(); j++) {
                // ���� �÷����ϴ� ���� ����
                if (playBalls.get(i).myTurn == true) {
                    int sex = playBalls.get(i).getSex();
                    // ���� �����̸� ���
                    if (playBalls.get(j).getSex() == sex) continue;

//                    System.out.println(i + "," + j);
                    int preference = playBalls.get(i).getPreference((j + 1) % 3);

                    // 3�� �̻� �� ��� ȣ��, �η� ����
                    if (preference >= 3) {
                        double displacement_xx = playBalls.get(j).p_x - playBalls.get(i).p_x;// - ball_width / 2;
                        double displacement_yy = playBalls.get(j).p_y - playBalls.get(i).p_y;//- ball_height / 2;
                        double squaredDistance1 = displacement_xx * displacement_xx + displacement_yy * displacement_yy;
                        //double gravitation1 = coef_gravitation/1000 * interval / squaredDistance1;
                        double gravitation1 = Constants.coef_gravitation * interval / 50000000;

                        if (Math.sqrt(squaredDistance1) >= 150) continue;

                        if (gravitation1 > Constants.max_gravitation)
                            gravitation1 = Constants.max_gravitation;

                        playBalls.get(i).a_x += gravitation1 * displacement_xx / Math.sqrt(squaredDistance1);
                        playBalls.get(i).a_y += gravitation1 * displacement_yy / Math.sqrt(squaredDistance1);
                    }
                    // 3�� ���� �� ��� ��ȣ��, ô�� ����
                    else if (preference <= -3) {
                        double displacement_xx = playBalls.get(j).p_x - playBalls.get(i).p_x;// - ball_width / 2;
                        double displacement_yy = playBalls.get(j).p_y - playBalls.get(i).p_y;//- ball_height / 2;
                        double squaredDistance1 = displacement_xx * displacement_xx + displacement_yy * displacement_yy;
                        //double gravitation1 = coef_gravitation/1000 * interval / squaredDistance1;
                        double repulsion = Constants.coef_repulsion * (timeStamp - startTime_pressing) / squaredDistance1;

                        if (Math.sqrt(squaredDistance1) >= 150) continue;


                        if (repulsion > Constants.max_repulsion)
                            repulsion = Constants.max_repulsion;

                        playBalls.get(i).a_x += -1.0 * repulsion * displacement_xx / Math.sqrt(squaredDistance1);
                        playBalls.get(i).a_y += -1.0 * repulsion * displacement_yy / Math.sqrt(squaredDistance1);
                    }

                }
            }

            //���콺 ��ư�� ���� �� ����
            if (isRepulsionRequested == true && playBalls.get(i).myTurn) {
                double displacement_x = inputs.pos_mouseCursor.x - playBalls.get(i).p_x - Constants.ball_width / 2;
                double displacement_y = inputs.pos_mouseCursor.y - playBalls.get(i).p_y - Constants.ball_height / 2;
                double squaredDistance = displacement_x * displacement_x + displacement_y * displacement_y;
                double gravitation = Constants.coef_gravitation * interval / squaredDistance;

                if (gravitation > Constants.max_gravitation)
                    gravitation = Constants.max_gravitation;

//                ball.a_x = gravitation * displacement_x / Math.sqrt(squaredDistance);
                if (Math.sqrt(squaredDistance) / 200 > Constants.max_hit_force) {
                    playBalls.get(i).a_x = displacement_x / Math.sqrt(squaredDistance) * Constants.max_hit_force;
                    playBalls.get(i).a_y = displacement_y / Math.sqrt(squaredDistance) * Constants.max_hit_force;
//                    System.out.println(balls[i].a_x + "," + playBalls.get(i).a_y + "," + Math.sqrt(squaredDistance));
                } else {
                    playBalls.get(i).a_x = displacement_x / 200;
                    playBalls.get(i).a_y = displacement_y / 200;
                }
//                ball.a_y = gravitation * displacement_y / Math.sqrt(squaredDistance);


                //System.out.println(playBalls.get(i).a_x + "," + playBalls.get(i).a_y);
            }


            //��Ʈ�� Ű�� ���� ���� �ʴٸ� �ӵ� / ���ӵ� �ݿ�
            if (isPauseRequested == false) {
                //������ ���
                playBalls.get(i).a_x += Constants.coef_friction * interval * playBalls.get(i).v_x;
                playBalls.get(i).a_y += Constants.coef_friction * interval * playBalls.get(i).v_y;

                //���ӵ��� �ӵ��� ���� - ���ӵ��� ��� �̸� �ð��� �������Ƿ� ���⼭ �� �������� ����
                playBalls.get(i).v_x += playBalls.get(i).a_x;
                playBalls.get(i).v_y += playBalls.get(i).a_y;

                //�� ���������� â �����ڸ��� �ε����� �ݻ��ϹǷ� �ӵ��� ���밪�� Ư�� ������ �۾������� ����
                playBalls.get(i).v_x %= Constants.max_velocity_x;
                playBalls.get(i).v_y %= Constants.max_velocity_y;

                //�ӵ��� ��ġ�� ���� - �� ���� �ð��� ���Ͽ� ����
                playBalls.get(i).p_x += playBalls.get(i).v_x * interval;
                playBalls.get(i).p_y += playBalls.get(i).v_y * interval;

				/*
                 * �ݻ� üũ
				 * 
				 * -	������ �浹�� �Ͼ �ð��� ���� �ľ��Ͽ� ���ӵ��� �ݻ� ��/�ķ� ������ �����ؾ� ������
				 * 		������ ȯ��(Ư��, �����°� ���� �׷��� �����ϴ� ȯ��)������ �׷��� ���� �ʾƵ� ū ������ ����
				 */
                boolean isWithinCanvas = true;


                do {
                    isWithinCanvas = true;
                    if (playBalls.get(i).p_x < 0) {
                        playBalls.get(i).v_x = -playBalls.get(i).v_x;
                        playBalls.get(i).p_x = -playBalls.get(i).p_x;
                        isWithinCanvas = false;
//                        playBalls.get(i).collideWith = Constants.numberOfBalls;
                    }

                    if (playBalls.get(i).p_x >= settings.canvas_width - Constants.ball_width) {
                        playBalls.get(i).v_x = -playBalls.get(i).v_x;
                        playBalls.get(i).p_x = 2 * (settings.canvas_width - Constants.ball_width) - playBalls.get(i).p_x;
                        isWithinCanvas = false;
//                        playBalls.get(i).collideWith = Constants.numberOfBalls + 1;
                    }

                    if (playBalls.get(i).p_y < 0) {
                        playBalls.get(i).v_y = -playBalls.get(i).v_y;
                        playBalls.get(i).p_y = -playBalls.get(i).p_y;
                        isWithinCanvas = false;
//                        playBalls.get(i).collideWith = Constants.numberOfBalls + 2;
                    }

                    if (playBalls.get(i).p_y >= Constants.TABLE_HEIGHT - Constants.ball_height) {
                        playBalls.get(i).v_y = -playBalls.get(i).v_y;
                        playBalls.get(i).p_y = 2 * (Constants.TABLE_HEIGHT - Constants.ball_height) - playBalls.get(i).p_y;
                        isWithinCanvas = false;
//                        playBalls.get(i).collideWith = Constants.numberOfBalls + 3;
                    }
                }
                while (isWithinCanvas == false);

                for (int j = i + 1; j < playBalls.size(); j++) {
                    if (PhysicsUtil.isCollide(playBalls.get(i), playBalls.get(j))
//                            && (playBalls.get(i).collideWith != j || playBalls.get(j).collideWith != i)) {
                            ) {
                        effectArrayList.add(new CollideEffect((playBalls.get(i).x + playBalls.get(j).x) / 2, (playBalls.get(i).y + playBalls.get(j).y) / 2, images, timeStamp + 300));
                        PhysicsUtil.fixOverlap(playBalls.get(i), playBalls.get(j), interval);
                        playBalls.get(i).collideWithSet.add(j);
                        playBalls.get(j).collideWithSet.add(i);
                    }
                }

                playBalls.get(i).x = (int) playBalls.get(i).p_x;
                playBalls.get(i).y = (int) playBalls.get(i).p_y;
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

        footer.Draw(g);
        DrawString(330, 640, "'��' ��Ʈ�� '��'");
        for (int i = 0; i < matchedBalls.size(); i++) {
            matchedBalls.get(i).b1.x = 50 + 100 * i;
            matchedBalls.get(i).b1.p_x = 50 + 100 * i;
            matchedBalls.get(i).b1.y = 700;
            matchedBalls.get(i).b1.p_y = 700;

            matchedBalls.get(i).b2.x = 70 + 100 * i;
            matchedBalls.get(i).b2.p_x = 70 + 100 * i;
            matchedBalls.get(i).b2.y = 700;
            matchedBalls.get(i).b2.p_y = 700;

            matchedBalls.get(i).b1.Draw(g);
            matchedBalls.get(i).b2.Draw(g);
        }

        for (Ball ball : playBalls) {
            ball.Draw(g);
        }


        for (int i = 0; i < effectArrayList.size(); i++) {
            if (effectArrayList.get(i).disappearTime < timeStamp) {
                effectArrayList.get(i).trigger_remove = true;
                effectArrayList.remove(i);
            } else
                effectArrayList.get(i).Draw(g);
        }

//        DrawString(24, 48, "FPS:  %.2f", loop.GetFPS());
//        DrawString(24, 78, "Time: %dms", (int) (timeStamp_lastFrame - timeStamp_firstFrame));

        //�׸��� �۾� �� - �� �޼���� Draw()�� ���� �Ʒ����� �׻� ȣ���� �־�� ��
        EndDraw();
    }

    private class CollideEffect extends DrawableObject {

        public double disappearTime = 0;

        public CollideEffect(int x, int y, ImageResourceManager images, double disappearTime) {
            super(x, y, Constants.effect_width, Constants.effect_height, images.GetImage("collide_effect"));
            this.disappearTime = disappearTime;
        }

        @Override
        public String toString() {
            return "p_x : " + x + "\n" + "p_y : " + y;
        }
    }

    class MatchedBall {
        Ball b1;
        Ball b2;

        public MatchedBall(Ball b1, Ball b2) {
            this.b1 = b1;
            this.b2 = b2;
        }
    }
}
