import loot.GameFrame;
import loot.GameFrameSettings;

public class Program
{

	public static void main(String[] args)
	{
		int mode = 0;

		if ( mode == 0 )
		{
			GameFrameSettings settings = new GameFrameSettings();
			settings.window_title = "LOOT�� ����� ���� ����";
			settings.canvas_width = 800;
			settings.canvas_height = 600;

//			settings.gameLoop_interval_ns = 10000000;		//100FPS�� �ش� - �̸� ��ƿ �� �ִ� ��ǻ�ʹ� �׸� ���� �����״� ���� ���� FPS�� �̺��� �������� ��
//			settings.gameLoop_interval_ns = 1000000;		//100FPS�� �ش� - �̸� ��ƿ �� �ִ� ��ǻ�ʹ� �׸� ���� �����״� ���� ���� FPS�� �̺��� �������� ��
			settings.gameLoop_interval_ns = 16666666;		//�� 60FPS�� �ش�
			//settings.gameLoop_interval_ns = 100000000;	//10FPS�� �ش� - ȭ���� �ʴ� 10���ۿ� ���ŵ��� ������ �����Ÿ��°� ���� ����

			settings.gameLoop_use_virtualTimingMode = false;
			settings.numberOfButtons = 3;

			GameFrame window = new PhysicsSampleFrame(settings);
			window.setVisible(true);
		}

		if ( mode == 1 )
		{
			GameFrameSettings settings = new GameFrameSettings();
			settings.window_title = "LOOT�� ����� �Է� ����";
			settings.canvas_width = 400;
			settings.canvas_height = 600;

			settings.gameLoop_interval_ns = 10000000;		//100FPS�� �ش� - '���ÿ� Ű�� �Է�'�ϴ� ��Ȳ�� ��������� ���� �����
//			settings.gameLoop_interval_ns = 16666666;		//�� 60FPS�� �ش�
			//settings.gameLoop_interval_ns = 100000000;	//10FPS�� �ش� - '���ÿ� Ű�� �Է�'�ϴ� ��Ȳ�� �� ���� �����

			settings.gameLoop_use_virtualTimingMode = false;
			settings.numberOfButtons = 5;

			GameFrame window = new InputSampleFrame(settings);
			window.setVisible(true);
		}

		if ( mode == 2 )
		{
			GameFrameSettings settings = new GameFrameSettings();
			settings.window_title = "LOOT�� ����� ���� ��� ����";
			settings.canvas_width = 160;
			settings.canvas_height = 60;

			settings.gameLoop_interval_ns = 10000000;		//100FPS�� �ش� - �� ������ �׸��� �۾��� ���� �����Ƿ� �������� ������ �����Ű�� ���� �̷��� ����

			settings.gameLoop_use_virtualTimingMode = false;
			settings.numberOfButtons = 1;

			GameFrame window = new AudioSampleFrame(settings);
			window.setVisible(true);
		}
	}
}
