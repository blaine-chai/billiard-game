package kr.ac.hanyang;

import kr.ac.hanyang.frame.PhysicsSampleFrame;
import loot.GameFrame;
import loot.GameFrameSettings;

public class Program {

    public static void main(String[] args) {
       /* int mode = 0;
        GameFrameSettings settings = new GameFrameSettings();
        settings.window_title = "Meeting";
        settings.canvas_width = 800;
        settings.canvas_height = 800;

//			settings.gameLoop_interval_ns = 10000000;		//100FPS�� �ش� - �̸� ��ƿ �� �ִ� ��ǻ�ʹ� �׸� ���� �����״� ���� ���� FPS�� �̺��� �������� ��
        settings.gameLoop_interval_ns = 16666666;        //�� 60FPS�� �ش�
        //settings.gameLoop_interval_ns = 100000000;	//10FPS�� �ش� - ȭ���� �ʴ� 10���ۿ� ���ŵ��� ������ �����Ÿ��°� ���� ����

        settings.gameLoop_use_virtualTimingMode = false;
        settings.numberOfButtons = 3;

        GameFrame window = new PhysicsSampleFrame(settings);
        window.setVisible(true);*/

        GameFrameManager manager = new GameFrameManager();

        manager.drawMainFrame();
//        manager.drawGameFrame();
    }
}
