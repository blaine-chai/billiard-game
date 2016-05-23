import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;

import loot.*;
import loot.graphics.DrawableObject;

/**
 * LOOT ���̺귯���� ������ ���� ȯ��� �Բ� ����� �����Դϴ�.
 * 
 * @author Racin
 *
 */
@SuppressWarnings("serial")
public class PhysicsSampleFrame extends GameFrame
{
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
	
	static final int numberOfBalls = 5;				//ȭ�� ���� �ִ� ���� ��(�ʹ� ������ FPS ������ ������ ��)
	static final int ball_width = 30;					//�� �ϳ��� ���� ����(������ �ȼ�)
	static final int ball_height = 30;					//�� �ϳ��� ���� ����(������ �ȼ�)
	
	static final double max_gravitation = 0.1;			//�η��� �ִ밪(������ �ȼ�/ms^2)
	static final double max_repulsion = 10.0;			//ô���� �ִ밪(������ �ȼ�/ms^2)
	static final double max_velocity_x = 50;			//X���� �ӷ��� �ִ밪(������ �ȼ�/ms)
	static final double max_velocity_y = 50;			//X���� �ӷ��� �ִ밪(������ �ȼ�/ms)
	
	static final double coef_gravitation = 300.0;		//�η��� ����� �� ����ϴ� ���
	static final double coef_repulsion = 0.1;			//ô���� ����� �� ����ϴ� ���
	static final double coef_friction = -0.001;			//�������� �����ϱ� ���� ���(�ӵ��� �� ���� ���� ��ŭ�� �������� ��. ���� �� ���� �������� ��. �������� ������ �ȼ�/ms^2)

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
	 *
	 */
	public class Ball extends DrawableObject
	{
		public double p_x;
		public double p_y;
		public double v_x;
		public double v_y;
		public double a_x;
		public double a_y;
		
		public Ball(int x, int y)
		{
			super(x, y,	ball_width, ball_height, images.GetImage("ball"));
			
			p_x = x;
			p_y = y;
		}
	}
	
	/* -------------------------------------------
	 * 
	 * �ʵ� ���� �κ�
	 * 
	 */
	
	Ball[] balls = new Ball[numberOfBalls];					//ȭ�� ���� �ִ� �� ���
	long startTime_pressing;								//���콺 ���� ��ư�� ������ ������ �ð�
	
	long timeStamp_firstFrame = 0;							//ù �������� timeStamp -> ���� ���ķ� ����� �ð� ��꿡 ���
	long timeStamp_lastFrame = 0;							//���� �������� timeStamp -> ������ ��꿡 ���
	
	
	/* -------------------------------------------
	 * 
	 * �޼��� ���� �κ�
	 * 
	 */
	
	public PhysicsSampleFrame(GameFrameSettings settings)
	{
		super(settings);
		
		inputs.BindKey(KeyEvent.VK_SPACE, 0);				//�����̽� �ٸ� ���� ���� ��� ���� �ӵ��� 0�� ��
		inputs.BindKey(KeyEvent.VK_CONTROL, 1);				//��Ʈ�� Ű�� ������ ���� ��� ���� �ӵ� / ���ӵ��� �Ͻ������� 0�� ��
		inputs.BindMouseButton(MouseEvent.BUTTON1, 2);		//���콺 ���� ��ư�� ������ ���� �η� �ۿ�, ���� ���� ô�� �ۿ�
		
		images.LoadImage("Images/ball.png", "ball");
	}
	
	@Override
	public boolean Initialize()
	{
		Random rand = new Random();
		
		//�� ���� ���� ��ġ�� ��ġ
		for ( int iBall = 0; iBall < balls.length; ++iBall )
			balls[iBall] = new Ball(rand.nextInt(settings.canvas_width - ball_width - 2) + 1, rand.nextInt(settings.canvas_height - ball_height - 2) + 1);
		
		//FPS ��¿� ����� �� �� ����ü ��������
		LoadColor(Color.black);
		LoadFont("����ü BOLD 24");
		
		return true;
	}

	@Override
	public boolean Update(long timeStamp)
	{
		/*
		 * �Է� ó��
		 */
		
		//�Է��� ��ư�� �ݿ�. �� �޼���� �׻� Update()�� ���� �κп��� ȣ���� �־�� ��
		inputs.AcceptInputs();
		
		//�� ��ư�� ���¸� �˻��Ͽ� �� ���� � �۾��� �����ؾ� �ϴ��� üũ
		boolean isStopRequested;
		boolean isPauseRequested;
		boolean isGravitationRequested;
		boolean isRepulsionRequested;
		
		//�̹� �����ӿ� �����̽� �ٸ� �����ٸ� ��� ���� �ӵ��� 0���� ����
		isStopRequested = inputs.buttons[0].IsPressedNow();

		//��Ʈ�� Ű�� ������ �ִٸ� ��� ���� �ӵ� �� ���ӵ��� �Ͻ������� 0���� ����
		isPauseRequested = inputs.buttons[1].isPressed;

		//�̹� �����ӿ� ���콺 ��ư�� �����ٸ� ���� �ð� ��� -> ô�� ��꿡 ����
		if ( inputs.buttons[2].IsPressedNow() == true )
			startTime_pressing = timeStamp;
		
		//���콺 ��ư�� ������ �ִٸ� �η� ����
		isGravitationRequested = inputs.buttons[2].isPressed;
		
		//�̹� �����ӿ� ���콺 ��ư�� �ôٸ� ô�� ����
		isRepulsionRequested = inputs.buttons[2].IsReleasedNow();

		
		/*
		 * �Է� ����
		 * 
		 * -	�Է� ���� �۾��� �ڵ� ������ ���� ������ �� ������
		 * 		�Ϲ����� ��� �̷��� �̸� �˻� �� ���� �۾��� �����ϴ°� ���� ������ ������ ��
		 */
		
		//��Ʈ�� Ű�� ������ �ִٸ� ���� �η� / ô���� ����� �ʿ䰡 �����Ƿ� �ش� ���� �缳��
		if ( isPauseRequested == true )
		{
			isGravitationRequested = false;
			isRepulsionRequested = false;
		}
		
		//(���� ��Ģ���� ���� ������) �� �� ���� ������ �����ϱ� ���� ô���� �ۿ��ϱ� ������ ��� ���� ���� �ӵ��� 0���� ����
		if ( isRepulsionRequested == true )
			isStopRequested = true;
		
		/*
		 * �Է� ����
		 */

		//���� ������ ���ķ� ����� �ð� ����
		double interval = timeStamp - timeStamp_lastFrame;
		
		//��� ���� ����
		for ( Ball ball : balls )
		{
			ball.a_x = 0;
			ball.a_y = 0;
			
			//�̹� �����ӿ� �����̽� �ٸ� �����ٸ� �ӵ��� 0���� ����
			if ( isStopRequested == true )
			{
				ball.v_x = 0;
				ball.v_y = 0;
			}
			
			//���콺 ��ư�� ������ �ִٸ� �η� ����
			if ( isGravitationRequested == true )
			{
				double displacement_x = inputs.pos_mouseCursor.x - ball.p_x - ball_width / 2;
				double displacement_y = inputs.pos_mouseCursor.y - ball.p_y - ball_height / 2;
				double squaredDistance = displacement_x * displacement_x + displacement_y * displacement_y;
				double gravitation = coef_gravitation * interval / squaredDistance;
				
				if ( gravitation > max_gravitation )
					gravitation = max_gravitation;
				
				ball.a_x = gravitation * displacement_x / Math.sqrt(squaredDistance);
				ball.a_y = gravitation * displacement_y / Math.sqrt(squaredDistance);
			}
			
			//�̹� �����ӿ� ���콺 ��ư�� �ôٸ� ô�� ����
			if ( isRepulsionRequested == true )
			{
				double displacement_x = inputs.pos_mouseCursor.x - ball.p_x - ball_width / 2;
				double displacement_y = inputs.pos_mouseCursor.y - ball.p_y - ball_height / 2;
				double squaredDistance = displacement_x * displacement_x + displacement_y * displacement_y;
				double repulsion = coef_repulsion * ( timeStamp - startTime_pressing ) / squaredDistance;

				if ( repulsion > max_repulsion )
					repulsion = max_repulsion;
				
				ball.a_x = -1.0 * repulsion * displacement_x / Math.sqrt(squaredDistance);
				ball.a_y = -1.0 * repulsion * displacement_y / Math.sqrt(squaredDistance);
			}
			
			//��Ʈ�� Ű�� ���� ���� �ʴٸ� �ӵ� / ���ӵ� �ݿ�
			if ( isPauseRequested == false )
			{
				//������ ���
				ball.a_x += coef_friction * interval * ball.v_x;
				ball.a_y += coef_friction * interval * ball.v_y;
				
				//���ӵ��� �ӵ��� ���� - ���ӵ��� ��� �̸� �ð��� �������Ƿ� ���⼭ �� �������� ����
				ball.v_x += ball.a_x;
				ball.v_y += ball.a_y;
				
				//�� ���������� â �����ڸ��� �ε����� �ݻ��ϹǷ� �ӵ��� ���밪�� Ư�� ������ �۾������� ����
				ball.v_x %= max_velocity_x;
				ball.v_y %= max_velocity_y;
				
				//�ӵ��� ��ġ�� ���� - �� ���� �ð��� ���Ͽ� ����
				ball.p_x += ball.v_x * interval;
				ball.p_y += ball.v_y * interval;
				
				/*
				 * �ݻ� üũ
				 * 
				 * -	������ �浹�� �Ͼ �ð��� ���� �ľ��Ͽ� ���ӵ��� �ݻ� ��/�ķ� ������ �����ؾ� ������
				 * 		������ ȯ��(Ư��, �����°� ���� �׷��� �����ϴ� ȯ��)������ �׷��� ���� �ʾƵ� ū ������ ����
				 */
				boolean isWithinCanvas = true;
				
				do
				{
					isWithinCanvas = true;
					try{
						for(int i=0;i<balls.length ;i++) {
							if ( ball.p_x == balls[i].p_x && ball.p_y == balls[i].p_y ) {

							}
							if((ball.p_x-balls[i].p_x)*(ball.p_x-balls[i].p_x)+(ball.p_y-balls[i].p_y)*(ball.p_y-balls[i].p_y) < 900 && ball!=balls[i]){
//								ball.v_x = -ball.v_x;
//								ball.p_x = 10;
//								//balls[i].p_x = -balls[i].p_x;
//								balls[i].v_x = - balls[i].v_x;
//								ball.v_y = -ball.v_y;
//								balls[i].v_y = - balls[i].v_y;
								System.out.println(i);
//								isWithinCanvas = false;
//								System.out.println(ball.v_x);
//								System.out.println(ball.v_y);
//								System.out.println(ball.p_x);
//								System.out.println(ball.p_y);
//								System.out.println(balls[i].v_x);
//								System.out.println(balls[i].v_y);
//								System.out.println(balls[i].p_x);
//								System.out.println(balls[i].p_y);
								// ��� �ӵ� ���ϱ�
							    double vx = ball.v_x - balls[i].v_x;
							    double vy = ball.v_y - balls[i].v_y;
//							    if(vx==0&&vy==0) return;
							    
							    // ���� ���� ��� ���� ����.
							    double alpha = Math.atan2(vy,vx);
							    
							    // ���� ��� ���� ���⿡ ���� ������ ���� ���ϱ�.
							    double x = balls[i].p_x - ball.p_x;
							    double y = balls[i].p_y - ball.p_y;
							    double theta = Math.atan2(y,x)-alpha;
							    
							    // �浹�� ���� ���� ȸ��. (�����..)
//							    alpha = alpha - (M_PI_2-theta);
							    
							    // ����� ���� �ӵ�.
							    double v = Math.sqrt(vx*vx + vy*vy);
							    
							    // �浹�� ����� ���� �ӵ�.
							    v = v*Math.sin(theta);
							    
							    // ����� ���� �ӵ�
							    vx = v * Math.cos(alpha);
							    vy = v * Math.sin(alpha);
							    
							    // ������ ���� �ӵ�.
							    vx = vx + balls[i].v_x;
							    vy = vy + balls[i].v_y;
 
							    // ��� ������ �ݿ�.
							    ball.v_x = -vx;
							    ball.v_y = -vy;
							    
							    
							    /* ���� ���� �浹 �� ������ ����߸���.*/
							    x = ball.p_x - balls[i].p_x;
							    y = ball.p_y - balls[i].p_y;
							    // ��� ������ �� ������ ����.
//							    theta = Math.atan2(y,x);
							    
							    // ���� �̵����� ������.
							    ball.p_x = Math.cos(theta)*(ball_width/2*1.001) + (balls[i].p_x+ball.p_x)/2;
							    ball.p_y = Math.sin(theta)*(ball_width/2*1.001) + (balls[i].p_y+ball.p_y)/2;
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					//�ݻ簡 �߻��� ������ �ӵ� ����, ��ġ ����
//					System.out.println("hi");
					
					if ( ball.p_x < 0 )
					{
						ball.v_x = -ball.v_x;
						ball.p_x = -ball.p_x;
						isWithinCanvas = false;
					}
					
					if ( ball.p_x >= settings.canvas_width - ball_width )
					{
						ball.v_x = -ball.v_x;
						ball.p_x = 2 * ( settings.canvas_width - ball_width ) - ball.p_x;
						isWithinCanvas = false;
					}
					
					if ( ball.p_y < 0 )
					{
						ball.v_y = -ball.v_y;
						ball.p_y = -ball.p_y;
						isWithinCanvas = false;
					}
					
					if ( ball.p_y >= settings.canvas_height - ball_height )
					{
						ball.v_y = -ball.v_y;
						ball.p_y = 2 * ( settings.canvas_height - ball_height ) - ball.p_y;
						isWithinCanvas = false;
					}
				}
				while ( isWithinCanvas == false );
				
				//����������, ���� ��ġ�� ������� �ش� ���� �׸� �ȼ��� ����
				ball.x = (int)ball.p_x;
				ball.y = (int)ball.p_y;
			}
		}

		//�̹��� ù �������̾��ٸ� ���� �ð� ���
		if ( timeStamp_firstFrame == 0 )
			timeStamp_firstFrame = timeStamp;
		
		//���� '���� ������'�� �� �̹� �������� ���� �ð� ���
		timeStamp_lastFrame = timeStamp;
		
		return true;
	}

	@Override
	public void Draw(long timeStamp)
	{
		//�׸��� �۾� ���� - �� �޼���� Draw()�� ���� ������ �׻� ȣ���� �־�� ��
		BeginDraw();
		
		//ȭ���� �ٽ� �������� ä��
		ClearScreen();

		for ( Ball ball : balls )
			ball.Draw(g);
		
		DrawString(24, 48, "FPS:  %.2f", loop.GetFPS());
		DrawString(24, 78, "Time: %dms", (int)(timeStamp_lastFrame - timeStamp_firstFrame));
		
		//�׸��� �۾� �� - �� �޼���� Draw()�� ���� �Ʒ����� �׻� ȣ���� �־�� ��
		EndDraw();
	}
}
