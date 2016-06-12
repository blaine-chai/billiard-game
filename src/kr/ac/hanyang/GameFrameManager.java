package kr.ac.hanyang;

import kr.ac.hanyang.frame.MainFrame;
import kr.ac.hanyang.frame.PhysicsSampleFrame;
import kr.ac.hanyang.frame.VoteFrame;
import loot.GameFrame;
import loot.GameFrameSettings;

/**
 * Created by blainechai on 2016. 6. 12..
 */
public class GameFrameManager {
    private GameFrameSettings settings;
    int mode = 0;
//    GameFrame gameFrameWindow;
    GameFrame window;
    GameFrame mainFrameWindow;

    public GameFrameManager() {
        this.settings = new GameFrameSettings();
        this.settings.window_title = "Meeting";
        this.settings.canvas_width = 800;
        this.settings.canvas_height = 800;
        this.settings.gameLoop_interval_ns = 16666666;        //�� 60FPS�� �ش�
        settings.gameLoop_use_virtualTimingMode = false;
        settings.numberOfButtons = 6;
    }

    public void drawGameFrame() {


//			settings.gameLoop_interval_ns = 10000000;		//100FPS�� �ش� - �̸� ��ƿ �� �ִ� ��ǻ�ʹ� �׸� ���� �����״� ���� ���� FPS�� �̺��� �������� ��

        //settings.gameLoop_interval_ns = 100000000;	//10FPS�� �ش� - ȭ���� �ʴ� 10���ۿ� ���ŵ��� ������ �����Ÿ��°� ���� ����


        window = new PhysicsSampleFrame(settings, this);
        window.setVisible(true);
        voteFrame();
    }

    public void endMainFrame() {
        window.setVisible(false);
    }

    public void drawMainFrame() {
        window = new MainFrame(settings, this);
        window.setVisible(true);
    }

    public void voteFrame(){
        this.settings.window_title = "Meeting";
        this.settings.canvas_width = 300;
        this.settings.canvas_height = 300;
        this.settings.gameLoop_interval_ns = 16666666;        //�� 60FPS�� �ش�
        settings.gameLoop_use_virtualTimingMode = false;
        mainFrameWindow = new VoteFrame(settings,this);
        mainFrameWindow.setVisible(true);
    }
}
