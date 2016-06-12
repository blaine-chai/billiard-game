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

//			settings.gameLoop_interval_ns = 10000000;		//100FPS에 해당 - 이를 버틸 수 있는 컴퓨터는 그리 많지 않을테니 보통 실제 FPS는 이보다 떨어지게 됨
        settings.gameLoop_interval_ns = 16666666;        //약 60FPS에 해당
        //settings.gameLoop_interval_ns = 100000000;	//10FPS에 해당 - 화면이 초당 10번밖에 갱신되지 않으면 버벅거리는게 눈에 보임

        settings.gameLoop_use_virtualTimingMode = false;
        settings.numberOfButtons = 3;

        GameFrame window = new PhysicsSampleFrame(settings);
        window.setVisible(true);*/

        GameFrameManager manager = new GameFrameManager();

        manager.drawMainFrame();
//        manager.drawGameFrame();
    }
}
