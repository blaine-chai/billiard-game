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


	/* -------------------------------------------
     *
	 * �޼��� ���� �κ�
	 *
	 */

    public MainFrame(GameFrameSettings settings, GameFrameManager gm) {
        super(settings);

        this.gm = gm;
        inputs.BindKey(KeyEvent.VK_SPACE, 0);                //�����̽� �ٸ� ���� ���� ��� ���� �ӵ��� 0�� ��
        inputs.BindKey(KeyEvent.VK_CONTROL, 1);                //��Ʈ�� Ű�� ������ ���� ��� ���� �ӵ� / ���ӵ��� �Ͻ������� 0�� ��
        inputs.BindMouseButton(MouseEvent.BUTTON1, 2);        //���콺 ���� ��ư�� ������ ���� �η� �ۿ�, ���� ���� ô�� �ۿ�
        inputs.BindMouseButton(MouseEvent.BUTTON2, 3);
        inputs.BindKey(KeyEvent.VK_ENTER, 4);

//        images.LoadImage("Images/ball_fixed.png", "ball");
//        images.LoadImage("Images/ball2.png", "redball");
    }

    @Override
    public boolean Initialize() {

        //FPS ��¿� ����� �� �� ����ü ��������
        LoadColor(Color.black);
        LoadFont("����ü BOLD 24");
        LoadFont("������� BOLD 24");

        return true;
    }

    @Override
    public boolean Update(long timeStamp) {
        /*
         * �Է� ó��
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
