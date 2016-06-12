package kr.ac.hanyang.model;

/**
 * Created by blainechai on 2016. 6. 9..
 */

import loot.ImageResourceManager;
import loot.graphics.DrawableObject;
import kr.ac.hanyang.values.Constants;

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
    public int collideWith = Constants.COLLIDE_WITH_INIT;
    public int sex;
    public int name;
    public int preference[];
    public boolean myTurn;

    public Ball(int x, int y, ImageResourceManager images) {
        super(x, y, Constants.ball_width, Constants.ball_height, images.GetImage("ball"));
        p_x = x;
        p_y = y;
        this.preference = new int[3];
        this.myTurn = false;
    }

    public Ball(int x, int y, String imageName, ImageResourceManager images) {
        super(x, y, Constants.ball_width, Constants.ball_height, images.GetImage(imageName));
        p_x = x;
        p_y = y;
        this.preference = new int[3];
        this.myTurn = false;
    }

    @Override
    public String toString() {
        return "p_x : " + p_x + "\n" + "p_y : " + p_y + "\n" + "v_x : " + v_x + "\n" + "v_y : " + v_y + "\n" + "a_y : " + a_y + "\n" + "a_y : " + a_y + "\n" + "x : " + x + "\n" + "y : " + y;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public int getSex()
    {
        return this.sex;
    }

    public void setPreference(int preference[])
    {
        for(int i = 0; i < 3; i++) {
            this.preference[i] = preference[i];
        }
    }

    public int getPreference(int opponentNum)
    {
        return this.preference[opponentNum];
    }

    public void setName(int nameNum)
    {
        this.name = nameNum;
    }

    public int getName()
    {
        return this.name;
    }

}
