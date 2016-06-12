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
        //�׸��� �۾� ���� - �� �޼���� Draw()�� ���� ������ �׻� ȣ���� �־�� ��
        BeginDraw();

        //ȭ���� �ٽ� �������� ä��
        ClearScreen();
        SetFont("������� BOLD 70");
        DrawString(120, 300, "Couple Manger");

        SetFont("������� BOLD 40");
        DrawString(250, 500, "press any key");

        SetFont("������� BOLD 25");
        DrawString(300, 700, "powered by OJS");

        //�׸��� �۾� �� - �� �޼���� Draw()�� ���� �Ʒ����� �׻� ȣ���� �־�� ��
        EndDraw();
    }
}
