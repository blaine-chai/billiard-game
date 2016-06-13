package kr.ac.hanyang.frame;

import kr.ac.hanyang.GameFrameManager;
import loot.GameFrame;
import loot.GameFrameSettings;
import loot.ImageResourceManager;
import loot.graphics.DrawableObject;

import java.awt.event.MouseEvent;

/**
 * Created by blainechai on 2016. 6. 12..
 */
public class VoteFrame extends GameFrame {
    private GameFrameManager gm;
    private int preference[][] = new int[6][3];
    private int turn = 0;
    private Player players[] = new Player[6];

    private Button buttons[] = new Button[10];

    public VoteFrame(GameFrameSettings settings, GameFrameManager gm) {
        super(settings);
        this.gm = gm;
        inputs.BindMouseButton(MouseEvent.BUTTON1, 2);
        for (int i = 0; i < preference.length; i++) {
            for (int j = 0; j < preference[0].length; j++) {
                preference[i][j] = -1;
            }
        }
    }

    @Override
    public boolean Initialize() {
        images.LoadImage("Images/0.png", "0");
        images.LoadImage("Images/+.png", "+");
        images.LoadImage("Images/-.png", "-");
        images.LoadImage("Images/accept.png", "accept");

        images.LoadImage("Images/bin2.png", "bin2");
        images.LoadImage("Images/joongki2.png", "joongki2");
        images.LoadImage("Images/minho2.png", "minho2");
        images.LoadImage("Images/sejung2.png", "sejung2");
        images.LoadImage("Images/suji2.png", "suji2");
        images.LoadImage("Images/sulhyun2.png", "sulhyun2");


        players[0] = new Player(200, 300, "bin2", images);
        players[1] = new Player(200, 400, "joongki2", images);
        players[2] = new Player(200, 500, "minho2", images);
        players[3] = new Player(200, 300, "sejung2", images);
        players[4] = new Player(200, 400, "suji2", images);
        players[5] = new Player(200, 500, "sulhyun2", images);

        buttons[0] = new Button(300, 300, "-", images);
        buttons[1] = new Button(400, 300, "0", images);
        buttons[2] = new Button(500, 300, "+", images);
        buttons[3] = new Button(300, 400, "-", images);
        buttons[4] = new Button(400, 400, "0", images);
        buttons[5] = new Button(500, 400, "+", images);
        buttons[6] = new Button(300, 500, "-", images);
        buttons[7] = new Button(400, 500, "0", images);
        buttons[8] = new Button(500, 500, "+", images);
        buttons[9] = new AcceptButton(500, 600, "accept", images);

        return true;
    }

    @Override
    public boolean Update(long timeStamp) {
        inputs.AcceptInputs();
        if (inputs.buttons[2].IsReleasedNow() || inputs.buttons[0].IsReleasedNow() || inputs.buttons[1].IsReleasedNow()) {
            int i = onClick(inputs.pos_mouseCursor.x, inputs.pos_mouseCursor.y);
            if (i < 9) {
                preference[turn][i / 3] = i % 3;
                for (int[] k : preference) {
                    for (int j : k) {
                        System.out.print(j);
                    }
                    System.out.println();
                }
            } else if (i == 9 && preference[turn][0] >= 0 && preference[turn][1] >= 0 && preference[turn][2] >= 0) {
                if (turn < 5)
                    turn++;
                else {
                    for (i = 0; i < preference.length; i++) {
                        for (int j = 0; j < preference[0].length; j++) {
                            preference[i][j] -= 1;
                        }
                    }
                    gm.drawGameFrame(preference);
                    gm.endPrevFrame();
                }

            }
        }

        return true;
    }

    private int onClick(int x, int y) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].x < x && x < buttons[i].x + buttons[i].width
                    && buttons[i].y < y && y < buttons[i].y + buttons[i].height) {
                System.out.println(i);
                return i;
            }
        }
        return buttons.length;
    }

    @Override
    public void Draw(long timeStamp) {
        //그리기 작업 시작 - 이 메서드는 Draw()의 가장 위에서 항상 호출해 주어야 함
        BeginDraw();

        //화면을 다시 배경색으로 채움
        ClearScreen();

        for (Button button : buttons) {
            button.Draw(g);
        }

        SetFont("맑은고딕 BOLD 70");
        DrawString(230, 150, "호감도 선택");

        SetFont("맑은고딕 BOLD 50");
        DrawString(300, 220, "Player " + (turn + 1));
        if (turn / 3 == 0) {
            for (int i = 0; i < 3; i++) {
                players[i].Draw(g);
            }
        } else {

            for (int i = 3; i < 6; i++) {
                players[i].Draw(g);
            }
        }
        EndDraw();
    }

    class Button extends DrawableObject {
        public Button(int x, int y, String imageName, ImageResourceManager images) {
            super(x, y, 50, 50, images.GetImage(imageName));
        }

        public Button(int x, int y, int sizeX, int sizeY, String imageName, ImageResourceManager images) {
            super(x, y, sizeX, sizeY, images.GetImage(imageName));
        }
    }

    class AcceptButton extends Button {
        public AcceptButton(int x, int y, String imageName, ImageResourceManager images) {
            super(x, y, 60, 40, imageName, images);
        }
    }

    class Player extends DrawableObject {
        public Player(int x, int y, String imageName, ImageResourceManager images) {
            super(x, y, 70, 70, images.GetImage(imageName));
        }
    }
}
