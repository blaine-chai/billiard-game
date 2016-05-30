import loot.GameFrame;
import loot.GameFrameSettings;
import loot.graphics.DrawableObject;

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

    static final int numberOfBalls = 100;                //화면 내에 있는 공의 수(너무 많으면 FPS 저하의 원인이 됨)
    static final int ball_width = 30;                    //공 하나의 가로 길이(단위는 픽셀)
    static final int ball_height = 30;                    //공 하나의 세로 길이(단위는 픽셀)

    static final double max_gravitation = 10;            //인력의 최대값(단위는 픽셀/ms^2)
    static final double max_repulsion = 50.0;            //척력의 최대값(단위는 픽셀/ms^2)
    static final double max_velocity_x = 50;            //X방향 속력의 최대값(단위는 픽셀/ms)
    static final double max_velocity_y = 50;            //X방향 속력의 최대값(단위는 픽셀/ms)

    static final double coef_gravitation = 300.0;        //인력을 계산할 때 사용하는 계수
    static final double coef_repulsion = 0.1;            //척력을 계산할 때 사용하는 계수
    //    static final double coef_friction = -0.0001;            //마찰력을 적용하기 위한 계수(속도에 이 값을 곱한 만큼이 마찰력이 됨. 따라서 이 값은 음수여야 함. 마찰력의 단위는 픽셀/ms^2)
    static final double coef_friction = -0.0005;            //마찰력을 적용하기 위한 계수(속도에 이 값을 곱한 만큼이 마찰력이 됨. 따라서 이 값은 음수여야 함. 마찰력의 단위는 픽셀/ms^2)

    static final int COLLIDE_WITH_INIT = Integer.MAX_VALUE;

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
	 * 필드 선언 부분
	 * 
	 */

    Ball[] balls = new Ball[numberOfBalls];                    //화면 내에 있는 공 목록
    long startTime_pressing;                                //마우스 왼쪽 버튼을 누르기 시작한 시각

    long timeStamp_firstFrame = 0;                            //첫 프레임의 timeStamp -> 실행 이후로 경과된 시간 계산에 사용
    long timeStamp_lastFrame = 0;                            //직전 프레임의 timeStamp -> 물리량 계산에 사용

	
	/* -------------------------------------------
     *
	 * 메서드 정의 부분
	 * 
	 */

    public PhysicsSampleFrame(GameFrameSettings settings) {
        super(settings);

        inputs.BindKey(KeyEvent.VK_SPACE, 0);                //스페이스 바를 누른 순간 모든 공의 속도가 0이 됨
        inputs.BindKey(KeyEvent.VK_CONTROL, 1);                //컨트롤 키를 누르는 동안 모든 공의 속도 / 가속도가 일시적으로 0이 됨
        inputs.BindMouseButton(MouseEvent.BUTTON1, 2);        //마우스 왼쪽 버튼을 누르는 동안 인력 작용, 떼는 순간 척력 작용

        images.LoadImage("Images/ball_fixed.png", "ball");
        images.LoadImage("Images/ball2.png", "redball");
    }

    @Override
    public boolean Initialize() {
        Random rand = new Random();

        //각 공을 랜덤 위치에 배치
        balls[0] = new RedBall(rand.nextInt(settings.canvas_width - ball_width - 2) + 1, rand.nextInt(settings.canvas_height - ball_height - 2) + 1);
        for (int iBall = 1; iBall < balls.length; ++iBall)
            balls[iBall] = new Ball(rand.nextInt(settings.canvas_width - ball_width - 2) + 1, rand.nextInt(settings.canvas_height - ball_height - 2) + 1);

        //FPS 출력에 사용할 색 및 글자체 가져오기
        LoadColor(Color.black);
        LoadFont("돋움체 BOLD 24");

        return true;
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
        boolean isRepulsionRequested;

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
        isRepulsionRequested = inputs.buttons[2].IsReleasedNow();
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

            //마우스 버튼을 누르고 있다면 인력 적용
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

            //마우스 버튼을 뗄때 힘 적용
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


            //이번 프레임에 마우스 버튼을 뗐다면 척력 적용ddd
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

            //컨트롤 키가 눌려 있지 않다면 속도 / 가속도 반영
            if (isPauseRequested == false) {
                //마찰력 계산
                balls[i].a_x += coef_friction * interval * balls[i].v_x;
                balls[i].a_y += coef_friction * interval * balls[i].v_y;

                //가속도를 속도에 적용 - 가속도의 경우 미리 시간을 곱했으므로 여기서 더 곱하지는 않음
                balls[i].v_x += balls[i].a_x;
                balls[i].v_y += balls[i].a_y;

                //이 예제에서는 창 가장자리에 부딪히면 반사하므로 속도의 절대값이 특정 값보다 작아지도록 보정
                balls[i].v_x %= max_velocity_x;
                balls[i].v_y %= max_velocity_y;

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

                //마지막으로, 공의 위치를 기반으로 해당 공을 그릴 픽셀값 설정
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

        for (Ball ball : balls)
            ball.Draw(g);

        DrawString(24, 48, "FPS:  %.2f", loop.GetFPS());
        DrawString(24, 78, "Time: %dms", (int) (timeStamp_lastFrame - timeStamp_firstFrame));

        //그리기 작업 끝 - 이 메서드는 Draw()의 가장 아래에서 항상 호출해 주어야 함
        EndDraw();
    }

    public boolean calcCollision(Ball i, Ball j) {
        double dx = i.p_x - j.p_x;
        double dy = i.p_y - j.p_y;

        double d2 = dx * dx + dy * dy;
        return d2 < ball_width * ball_width;
    }

    //수정이 필요함
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
