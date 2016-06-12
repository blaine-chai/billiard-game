package kr.ac.hanyang.frame;

import kr.ac.hanyang.GameFrameManager;
import kr.ac.hanyang.Util.PhysicsUtil;
import loot.GameFrame;
import loot.GameFrameSettings;
import kr.ac.hanyang.model.Ball;
import kr.ac.hanyang.model.RedBall;
import kr.ac.hanyang.values.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * LOOT 라이브러리를 간단한 물리 환경과 함께 사용한 예제입니다.
 *
 * @author Racin
 */
@SuppressWarnings("serial")
public class PhysicsSampleFrame extends GameFrame {
    /*
     * 조작:
	 * 
	 * 스페이스 바:
	 * 
	 * 		누른 순간 모든 공의 속도를 0으로 만듦
	 * 
	 * 컨트롤 키:
	 * 
	 * 		누르는 동안 모든 공의 속도 / 가속도를 일시적으로 0으로 만듦
	 * 
	 * 마우스 왼쪽 버튼:
	 * 
	 * 		누르고 있는 동안 마우스 포인터 위치로 향하는 인력 적용
	 * 		떼는 순간, 그동안 누르고 있던 시간에 비례하여 척력 적용
	 * 
	 */

	/* -------------------------------------------
     *
	 * 상수 및 내부 클래스 선언 부분
	 * 
	 */


    /**
     * '공' 하나를 표현하는 클래스입니다.<br>
     * LOOT 라이브러리에 있는 DrawableObject class를 상속받아<br>
     * 위치, 속도, 가속도 값을 추가로 선언하여 사용합니다.<br>
     * <br>
     * 기존의 x, y는 int 형식이라 소숫점 이하 정보는 담을 수 없기에<br>
     * 물리 환경에서 사용하기에는 적절하지 않습니다.<br>
     * 그러므로 p_x, p_y를 별도로 선언하여 위치 정보를 다루고<br>
     * 매 프레임마다 p_x, p_y의 값을 버림하여 x, y에 담아 각 공의 '화면상의 위치'를 지정합니다.
     *
     * @author Racin
     */

	/* -------------------------------------------
     *
	 * 필드 선언 부분
	 * 
	 */

    Ball[] balls = new Ball[Constants.numberOfBalls];                    //화면 내에 있는 공 목록
    Ball[] players = new Ball[Constants.numberOfBalls];
    int turnNum;
    long startTime_pressing;                                //마우스 왼쪽 버튼을 누르기 시작한 시각

    long timeStamp_firstFrame = 0;                            //첫 프레임의 timeStamp -> 실행 이후로 경과된 시간 계산에 사용
    long timeStamp_lastFrame = 0;                            //직전 프레임의 timeStamp -> 물리량 계산에 사용

    GameFrameManager gm;
    /* -------------------------------------------
     *
	 * 메서드 정의 부분
	 * 
	 */

    public PhysicsSampleFrame(GameFrameSettings settings, GameFrameManager gm) {
        super(settings);
        this.gm = gm;
        inputs.BindKey(KeyEvent.VK_SPACE, 0);                //스페이스 바를 누른 순간 모든 공의 속도가 0이 됨
        inputs.BindKey(KeyEvent.VK_CONTROL, 1);                //컨트롤 키를 누르는 동안 모든 공의 속도 / 가속도가 일시적으로 0이 됨
        inputs.BindMouseButton(MouseEvent.BUTTON1, 2);        //마우스 왼쪽 버튼을 누르는 동안 인력 작용, 떼는 순간 척력 작용

        images.LoadImage("Images/ball_fixed.png", "ball");
        images.LoadImage("Images/ball2.png", "redball");
        
        images.LoadImage("Images/bin2.png", "bin2");
        images.LoadImage("Images/joongki2.png", "joongki2");
        images.LoadImage("Images/minho2.png", "minho2");
        images.LoadImage("Images/sejung2.png", "sejung2");
        images.LoadImage("Images/suji2.png", "suji2");
        images.LoadImage("Images/sulhyun2.png", "sulhyun2");
    }

    @Override
    public boolean Initialize() {
        Random rand = new Random();

        /*
        //각 공을 랜덤 위치에 배치
        balls[0] = new RedBall(rand.nextInt(settings.canvas_width - ball_width - 2) + 1, rand.nextInt(settings.canvas_height - ball_height - 2) + 1);
        for (int iBall = 1; iBall < balls.length; ++iBall)
            balls[iBall] = new Ball(rand.nextInt(settings.canvas_width - ball_width - 2) + 1, rand.nextInt(settings.canvas_height - ball_height - 2) + 1);
        */

       // balls[0] = new RedBall(400, 400, images);
        //balls[1] = new Ball(455, 455, images);
        //balls[2] = new Ball(0, 0, images);

        
        
        balls[0] = new RedBall(350, 350, images);
        balls[1] = new Ball(150, 200, "bin2", images);
        balls[2] = new Ball(350, 200, "joongki2", images);
        balls[3] = new Ball(550, 200, "minho2", images);
        balls[4] = new Ball(150, 500, "sejung2", images);
        balls[5] = new Ball(350, 500, "suji2", images);
        balls[6] = new Ball(550, 500, "sulhyun2", images);
        
        myInit();

        //FPS 출력에 사용할 색 및 글자체 가져오기
        LoadColor(Color.black);
        LoadFont("돋움체 BOLD 24");

        LoadColor(Color.black);
        LoadFont("돋움체 BOLD 24");
        LoadFont("맑은고딕 BOLD 24");

        return true;
    }

    public void myInit() {
    	turnNum = 1;
        int redballPref[] = new int[3];
        int opponentPref[] = new int[3];
        int pref1[] = new int [3];
        int pref2[] = new int [3];
        int pref3[] = new int [3];

        redballPref[0] = 1;
        redballPref[1] = 1;
        redballPref[2] = 1;
        pref1[0] = 0;
        pref1[1] = 0;
        pref1[2] = 0;

        pref2[0] = 1;
        pref2[1] = 0;
        pref2[2] = -1;

        pref3[0] = -1;
        pref3[1] = 0;
        pref3[2] = 1;
        

        balls[0].setName("");
        balls[0].setSex(Constants.M);
        balls[0].setPreference(redballPref);

        balls[1].setName("bin");
        balls[1].setSex(Constants.M);
        balls[1].setPreference(pref1);

        balls[2].setName("joongki");
        balls[2].setSex(Constants.M);
        balls[2].setPreference(pref2);
        
        balls[3].setName("minho");
        balls[3].setSex(Constants.M);
        balls[3].setPreference(pref3);
        
        balls[4].setName("sejung");
        balls[4].setSex(Constants.W);
        balls[4].setPreference(pref3);

        balls[5].setName("suji");
        balls[5].setSex(Constants.W);
        balls[5].setPreference(pref2);

        
        balls[6].setName("sulhyun");
        balls[6].setSex(Constants.W);
        balls[6].setPreference(pref1);
        

        balls[0].myTurn = true;
        setBallInfo(turnNum);


    }
    
    public boolean isAllStop() {
    	boolean ret = true;
    	
    	// 모든 공에 대해 움직이는 공이 있는지 판단.
    	for( int i = 0; i < balls.length; i ++) {
    		if ( balls[i].v_x > 0.001 && balls[i].v_y > 0.001 ) ret = false;
    	}
    	
    	return ret;
    }
    
    // 한 턴이 진행된 후 해당 플레이어의 정보를 수구에 적용.
    public void setBallInfo(int turnNum) {
    	int playerIndex = 0;

    	playerIndex %= turnNum;
    	
		balls[0].setPreference( balls[playerIndex].getPreferenceAll()  );
		balls[0].setSex( balls[playerIndex].getSex() );
		balls[0].setName( balls[playerIndex].getName() );
    }

    @Override
    public boolean Update(long timeStamp) {
        /*
         * 입력 처리
		 */

        //입력을 버튼에 반영. 이 메서드는 항상 Update()의 시작 부분에서 호출해 주어야 함
        inputs.AcceptInputs();

        //각 버튼의 상태를 검사하여 각 공에 어떤 작업을 수행해야 하는지 체크
        boolean isStopRequested;
        boolean isPauseRequested;
        boolean isGravitationRequested;
        boolean isRepulsionRequested = false;

        //이번 프레임에 스페이스 바를 눌렀다면 모든 공의 속도를 0으로 만듦
        isStopRequested = inputs.buttons[0].IsPressedNow();

        //컨트롤 키를 누르고 있다면 모든 공의 속도 및 가속도를 일시적으로 0으로 만듦
        isPauseRequested = inputs.buttons[1].isPressed;

        //이번 프레임에 마우스 버튼을 눌렀다면 현재 시각 기록 -> 척력 계산에 사용됨
        if (inputs.buttons[2].IsPressedNow() == true)
            startTime_pressing = timeStamp;
        

        //마우스 버튼을 누르고 있다면 인력 적용
//        isGravitationRequested = inputs.buttons[2].isPressed;

        //이번 프레임에 마우스 버튼을 뗐다면 척력 적용
        /// 이 함수에서 각 공의 allmyhit, collisioncount변수 초기화 필요.
        if (isAllStop() == true) {
        	
        	
        	
        	setBallInfo(turnNum);
			isRepulsionRequested = inputs.buttons[2].IsReleasedNow();
			turnNum++;
			
        }
//        isRepulsionRequested = inputs.buttons[2].isPressed;

		/*
         * 입력 검증
		 * 
		 * -	입력 검증 작업은 코드 구성에 따라 생략할 수 있지만
		 * 		일반적인 경우 이렇게 미리 검사 및 보정 작업을 수행하는게 오류 절감에 도움이 됨
		 */

        //컨트롤 키를 누르고 있다면 굳이 인력 / 척력을 계산할 필요가 없으므로 해당 변수 재설정
        if (isPauseRequested == true) {
            isGravitationRequested = false;
            isRepulsionRequested = false;
        }

        //(물리 법칙과는 맞지 않지만) 좀 더 예쁜 폭발을 연출하기 위해 척력이 작용하기 직전에 모든 공의 현재 속도를 0으로 만듦
        if (isRepulsionRequested == true)
            isStopRequested = true;

		/*
         * 입력 적용
		 */
        

        //지난 프레임 이후로 경과된 시간 측정
        double interval = timeStamp - timeStamp_lastFrame;

        //모든 공에 대해

        for (int i = 0; i < balls.length; i++) {
            balls[i].a_x = 0;
            balls[i].a_y = 0;

            //이번 프레임에 스페이스 바를 눌렀다면 속도를 0으로 만듦
            if (isStopRequested == true) {
                balls[i].v_x = 0;
                balls[i].v_y = 0;
            }

            for (int j = i + 1; j < balls.length; j++) {
                // 현재 플레이하는 공에 대해
                if (balls[i].myTurn == true) {
                    int sex = balls[i].getSex();
                    // 같은 성별이면 통과
                    if (balls[j].getSex() == sex) continue;

                    int preference = balls[j].getPreference(i);


                    // 3점 이상 준 경우 호감, 인력 적용
                    if (preference >= 0) {
                        double displacement_xx = balls[j].p_x - balls[i].p_x;// - ball_width / 2;
                        double displacement_yy = balls[j].p_y - balls[i].p_y;//- ball_height / 2;
                        double squaredDistance1 = displacement_xx * displacement_xx + displacement_yy * displacement_yy;
                        //double gravitation1 = coef_gravitation/1000 * interval / squaredDistance1;
                        double gravitation1 = Constants.coef_gravitation * interval / 50000000;

                        if (Math.sqrt(squaredDistance1) >= 150) continue;

                        if (gravitation1 > Constants.max_gravitation)
                            gravitation1 = Constants.max_gravitation;

                        balls[i].a_x = gravitation1 * displacement_xx / Math.sqrt(squaredDistance1);
                        balls[i].a_y = gravitation1 * displacement_yy / Math.sqrt(squaredDistance1);
                    }
                    // 3점 이하 준 경우 비호감, 척력 적용
                    else {
                        double displacement_xx = balls[j].p_x - balls[i].p_x;// - ball_width / 2;
                        double displacement_yy = balls[j].p_y - balls[i].p_y;//- ball_height / 2;
                        double squaredDistance1 = displacement_xx * displacement_xx + displacement_yy * displacement_yy;
                        //double gravitation1 = coef_gravitation/1000 * interval / squaredDistance1;
                        double repulsion = Constants.coef_repulsion * (timeStamp - startTime_pressing) / squaredDistance1;

                        if (Math.sqrt(squaredDistance1) >= 150) continue;


                        if (repulsion > Constants.max_repulsion)
                            repulsion = Constants.max_repulsion;

                        balls[i].a_x = -1.0 * repulsion * displacement_xx / Math.sqrt(squaredDistance1);
                        balls[i].a_y = -1.0 * repulsion * displacement_yy / Math.sqrt(squaredDistance1);
                    }

                }
            }

            //마우스 버튼을 뗄때 힘 적용
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


            //컨트롤 키가 눌려 있지 않다면 속도 / 가속도 반영
            if (isPauseRequested == false) {
                //마찰력 계산
                balls[i].a_x += Constants.coef_friction * interval * balls[i].v_x;
                balls[i].a_y += Constants.coef_friction * interval * balls[i].v_y;

                //가속도를 속도에 적용 - 가속도의 경우 미리 시간을 곱했으므로 여기서 더 곱하지는 않음
                balls[i].v_x += balls[i].a_x;
                balls[i].v_y += balls[i].a_y;

                //이 예제에서는 창 가장자리에 부딪히면 반사하므로 속도의 절대값이 특정 값보다 작아지도록 보정
                balls[i].v_x %= Constants.max_velocity_x;
                balls[i].v_y %= Constants.max_velocity_y;

                //속도를 위치에 적용 - 이 때는 시간을 곱하여 적용
                balls[i].p_x += balls[i].v_x * interval;
                balls[i].p_y += balls[i].v_y * interval;

				/*
                 * 반사 체크
				 * 
				 * -	원래는 충돌이 일어난 시각을 먼저 파악하여 가속도를 반사 전/후로 나누어 적용해야 하지만
				 * 		간단한 환경(특히, 마찰력과 같은 항력이 존재하는 환경)에서는 그렇게 하지 않아도 큰 지장은 없음
				 */
                boolean isWithinCanvas = true;


                do {
                    isWithinCanvas = true;
                    if (balls[i].p_x < 0) {
                        balls[i].v_x = -balls[i].v_x;
                        balls[i].p_x = -balls[i].p_x;
                        isWithinCanvas = false;
//                        balls[i].collideWith = Constants.numberOfBalls;
                    }

                    if (balls[i].p_x >= settings.canvas_width - Constants.ball_width) {
                        balls[i].v_x = -balls[i].v_x;
                        balls[i].p_x = 2 * (settings.canvas_width - Constants.ball_width) - balls[i].p_x;
                        isWithinCanvas = false;
//                        balls[i].collideWith = Constants.numberOfBalls + 1;
                    }

                    if (balls[i].p_y < 0) {
                        balls[i].v_y = -balls[i].v_y;
                        balls[i].p_y = -balls[i].p_y;
                        isWithinCanvas = false;
//                        balls[i].collideWith = Constants.numberOfBalls + 2;
                    }

                    if (balls[i].p_y >= settings.canvas_height - Constants.ball_height) {
                        balls[i].v_y = -balls[i].v_y;
                        balls[i].p_y = 2 * (settings.canvas_height - Constants.ball_height) - balls[i].p_y;
                        isWithinCanvas = false;
//                        balls[i].collideWith = Constants.numberOfBalls + 3;
                    }
                }
                while (isWithinCanvas == false);

                for (int j = i + 1; j < balls.length; j++) {
                    if (PhysicsUtil.calcCollision(balls[i], balls[j])
//                            && (balls[i].collideWith != j || balls[j].collideWith != i)) {
                            ) {
//                        balls[i].collideWith = j;
//                        balls[j].collideWith = i;
                        PhysicsUtil.fixOverlap(balls[i], balls[j], interval);
                    }

					//현재 공이 수구이고 내 공과 충돌 시 유효 충돌.
					if ( PhysicsUtil.calcCollision(balls[0], balls[j]) ) {
						balls[j].hitMyBall = true;

					}
					
					// 다른 성별의 공과 충돌 시 유효 충돌. 여기에서 맨 처음 맞춘 이성이 아니라면 무효처
					if ( balls[i].hitMyBall == true ) {
						balls[j].collisionCount++;
					}
                }

                balls[i].x = (int) balls[i].p_x;
                balls[i].y = (int) balls[i].p_y;
            }
        }
        //이번이 첫 프레임이었다면 시작 시각 기록
        if (timeStamp_firstFrame == 0)
            timeStamp_firstFrame = timeStamp;

        //이제 '직전 프레임'이 될 이번 프레임의 시작 시각 기록
        timeStamp_lastFrame = timeStamp;

        return true;
    }

    @Override
    public void Draw(long timeStamp) {
        //그리기 작업 시작 - 이 메서드는 Draw()의 가장 위에서 항상 호출해 주어야 함
        BeginDraw();

        //화면을 다시 배경색으로 채움
        ClearScreen();

        for (Ball ball : balls) {
            ball.Draw(g);
        }

        DrawString(24, 48, "FPS:  %.2f", loop.GetFPS());
        DrawString(24, 78, "Time: %dms", (int) (timeStamp_lastFrame - timeStamp_firstFrame));

        //그리기 작업 끝 - 이 메서드는 Draw()의 가장 아래에서 항상 호출해 주어야 함
        EndDraw();
    }


}
