package kr.ac.hanyang.model;

/**
 * Created by blainechai on 2016. 6. 9..
 */

import loot.ImageResourceManager;
import loot.graphics.DrawableObject;
import kr.ac.hanyang.values.Constants;

/**
 * '��' �ϳ��� ǥ���ϴ� Ŭ�����Դϴ�.<br>
 * LOOT ���̺귯���� �ִ� DrawableObject class�� ��ӹ޾�<br>
 * ��ġ, �ӵ�, ���ӵ� ���� �߰��� �����Ͽ� ����մϴ�.<br>
 * <br>
 * ������ x, y�� int �����̶� �Ҽ��� ���� ������ ���� �� ���⿡<br>
 * ���� ȯ�濡�� ����ϱ⿡�� �������� �ʽ��ϴ�.<br>
 * �׷��Ƿ� p_x, p_y�� ������ �����Ͽ� ��ġ ������ �ٷ��<br>
 * �� �����Ӹ��� p_x, p_y�� ���� �����Ͽ� x, y�� ��� �� ���� 'ȭ����� ��ġ'�� �����մϴ�.
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
