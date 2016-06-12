package kr.ac.hanyang;

import kr.ac.hanyang.frame.MainFrame;
import kr.ac.hanyang.frame.PhysicsFrame;
import kr.ac.hanyang.frame.VoteFrame;
import loot.GameFrame;
import loot.GameFrameSettings;

/**
 * Created by blainechai on 2016. 6. 12..
 */
public class GameFrameManager {
    private GameFrameSettings settings;
    private GameFrame window;
    private GameFrame preWindow;

    public GameFrameManager() {
        this.settings = new GameFrameSettings();
        this.settings.window_title = "Meeting";
        this.settings.canvas_width = 800;
        this.settings.canvas_height = 800;
        this.settings.gameLoop_interval_ns = 16666666;        //약 60FPS에 해당
        settings.gameLoop_use_virtualTimingMode = false;
        settings.numberOfButtons = 6;
    }

    public void drawGameFrame(int[][] preference) {
        preWindow = window;
        window = new PhysicsFrame(settings, this, preference);
        window.setVisible(true);
//        drawVoteFrame();
    }

    public void endPrevFrame() {
        preWindow.setVisible(false);
        preWindow = null;
    }

    public void drawMainFrame() {
        window = new MainFrame(settings, this);
        window.setVisible(true);
    }

    public void drawVoteFrame() {
        preWindow = window;
        window = new VoteFrame(settings, this);
        window.setVisible(true);
    }
}
