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
			settings.window_title = "LOOT占쏙옙 占쏙옙占쏙옙占� 占쏙옙占쏙옙 占쏙옙占쏙옙";
			settings.canvas_width = 800;
			settings.canvas_height = 600;
			
			//settings.gameLoop_interval_ns = 10000000;		//100FPS占쏙옙 占쌔댐옙 - 占싱몌옙 占쏙옙틸 占쏙옙 占쌍댐옙 占쏙옙퓨占싶댐옙 占쌓몌옙 占쏙옙占쏙옙 占쏙옙占쏙옙占쌓댐옙 占쏙옙占쏙옙 占쏙옙占쏙옙 FPS占쏙옙 占싱븝옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙
			settings.gameLoop_interval_ns = 16666666;		//占쏙옙 60FPS占쏙옙 占쌔댐옙
			//settings.gameLoop_interval_ns = 100000000;	//10FPS占쏙옙 占쌔댐옙 - 화占쏙옙占쏙옙 占십댐옙 10占쏙옙占쌜울옙 占쏙옙占신듸옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占신몌옙占승곤옙 占쏙옙占쏙옙 占쏙옙占쏙옙
			
			settings.gameLoop_use_virtualTimingMode = false;
			settings.numberOfButtons = 3;
			
			GameFrame window = new PhysicsSampleFrame(settings);
			window.setVisible(true);
		}	
		if ( mode == 1 )
		{
			GameFrameSettings settings = new GameFrameSettings();
			settings.window_title = "LOOT占쏙옙 占쏙옙占쏙옙占� 占쌉뤄옙 占쏙옙占쏙옙";
			settings.canvas_width = 400;
			settings.canvas_height = 600;
			
			//settings.gameLoop_interval_ns = 10000000;		//100FPS占쏙옙 占쌔댐옙 - '占쏙옙占시울옙 키占쏙옙 占쌉뤄옙'占싹댐옙 占쏙옙황占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙占� 占쏙옙占쏙옙 占쏙옙占쏙옙占�
			settings.gameLoop_interval_ns = 16666666;		//占쏙옙 60FPS占쏙옙 占쌔댐옙
			//settings.gameLoop_interval_ns = 100000000;	//10FPS占쏙옙 占쌔댐옙 - '占쏙옙占시울옙 키占쏙옙 占쌉뤄옙'占싹댐옙 占쏙옙황占쏙옙 占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙占�
			
			settings.gameLoop_use_virtualTimingMode = false;
			settings.numberOfButtons = 5;
			
			GameFrame window = new InputSampleFrame(settings);
			window.setVisible(true);
		}
		
		if ( mode == 2 )
		{
			GameFrameSettings settings = new GameFrameSettings();
			settings.window_title = "LOOT占쏙옙 占쏙옙占쏙옙占� 占쏙옙占쏙옙 占쏙옙占� 占쏙옙占쏙옙";
			settings.canvas_width = 160;
			settings.canvas_height = 60;
			
			settings.gameLoop_interval_ns = 10000000;		//100FPS占쏙옙 占쌔댐옙 - 占쏙옙 占쏙옙占쏙옙占쏙옙 占쌓몌옙占쏙옙 占쌜억옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙占실뤄옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占신곤옙占� 占쏙옙占쏙옙 占싱뤄옙占쏙옙 占쏙옙占쏙옙
			
			settings.gameLoop_use_virtualTimingMode = false;
			settings.numberOfButtons = 1;
			
			GameFrame window = new AudioSampleFrame(settings);
			window.setVisible(true);
		}
	}
}
