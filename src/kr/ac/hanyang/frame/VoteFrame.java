package kr.ac.hanyang.frame;

import kr.ac.hanyang.GameFrameManager;
import loot.GameFrame;
import loot.GameFrameSettings;

/**
 * Created by blainechai on 2016. 6. 12..
 */
public class VoteFrame extends GameFrame{
    private GameFrameManager gm;
    public VoteFrame(GameFrameSettings settings, GameFrameManager gm){
        super(settings);
        this.gm = gm;
    }
    @Override
    public boolean Initialize() {
        return false;
    }

    @Override
    public boolean Update(long timeStamp) {
        return false;
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
