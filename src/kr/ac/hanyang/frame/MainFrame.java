package kr.ac.hanyang.frame;

import kr.ac.hanyang.GameFrameManager;
import loot.GameFrame;
import loot.GameFrameSettings;
import loot.InputManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by blainechai on 2016. 6. 12..
 */
public class MainFrame extends GameFrame {
    private GameFrameManager gm;
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


	/* -------------------------------------------
     *
	 * 메서드 정의 부분
	 *
	 */

    public MainFrame(GameFrameSettings settings, GameFrameManager gm) {
        super(settings);

        this.gm = gm;
        inputs.BindKey(KeyEvent.VK_SPACE, 0);                //스페이스 바를 누른 순간 모든 공의 속도가 0이 됨
        inputs.BindKey(KeyEvent.VK_CONTROL, 1);                //컨트롤 키를 누르는 동안 모든 공의 속도 / 가속도가 일시적으로 0이 됨
        inputs.BindMouseButton(MouseEvent.BUTTON1, 2);        //마우스 왼쪽 버튼을 누르는 동안 인력 작용, 떼는 순간 척력 작용
        inputs.BindMouseButton(MouseEvent.BUTTON2, 3);
        inputs.BindKey(KeyEvent.VK_ENTER, 4);

//        images.LoadImage("Images/ball_fixed.png", "ball");
//        images.LoadImage("Images/ball2.png", "redball");
    }

    @Override
    public boolean Initialize() {

        //FPS 출력에 사용할 색 및 글자체 가져오기
        LoadColor(Color.black);
        LoadFont("돋움체 BOLD 24");
        LoadFont("맑은고딕 BOLD 24");

        return true;
    }

    @Override
    public boolean Update(long timeStamp) {
        /*
         * 입력 처리
		 */

        inputs.AcceptInputs();


        for (InputManager.ButtonState input : inputs.buttons)
            if (input.IsReleasedNow()) {
                gm.drawGameFrame(new int[6][3]);
                gm.endPrevFrame();
//                gm.drawVoteFrame();
//                gm.endPrevFrame();
            }
        return true;
    }

    @Override
    public void Draw(long timeStamp) {
        //그리기 작업 시작 - 이 메서드는 Draw()의 가장 위에서 항상 호출해 주어야 함
        BeginDraw();

        //화면을 다시 배경색으로 채움
        ClearScreen();
        SetFont("맑은고딕 BOLD 70");
        DrawString(120, 300, "Couple Manger");

        SetFont("맑은고딕 BOLD 40");
        DrawString(250, 500, "press any key");

        SetFont("맑은고딕 BOLD 25");
        DrawString(300, 700, "powered by OJS");

        //그리기 작업 끝 - 이 메서드는 Draw()의 가장 아래에서 항상 호출해 주어야 함
        EndDraw();
    }

}
