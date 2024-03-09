import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public abstract class Plat2D implements Runnable
{
	// Intended Aspect Ratio is 4:3
	// Intended Resolution is 960 x 672
	
	protected ScreenManager sm;
	
	// Used for Delta Time (Time between last frame and current frame
	static long lastTime = System.nanoTime();
	
	// What starts the thread
	public void Init() 
	{
		// Window
		sm = new ScreenManager();
		
		// Thread t = new Thread(this);
		// t.start();
	}
		
	// What runs inside the thread
	public void run() 
	{
		System.out.println(TileMapRenderer.TilesToPixels(3));
		try 
		{
			Init();
			GameLoop();
		}
		finally {}
	}
	
	// Defined Functions
	
	// Get DeltaTime
	// DeltaTime is the time between the current frame and the last frame.
	
	public static float DeltaTime() {
		long time = System.nanoTime();
		float deltaTime = ((time - lastTime) / 100000f); // Nanoseconds to ms
		lastTime = time;
		return deltaTime;		
	}
	
	public void GameLoop() 
	{
		long currTime = 0;
		float deltaTime = 0;
		// Game Loop
		while(true) 
		{
			// Get Delta Time & Current Time
			currTime += DeltaTime();
			deltaTime = DeltaTime();
			Update( deltaTime );
			// Drawing to the Screen
			Graphics2D pen = sm.GetGraphics();
			Draw(pen);
			pen.dispose();
			sm.Update();
			
			try   { Thread.sleep(16); } 
			catch (Exception e) {}
		}
	}
	
	// Updates state of game/animation based on amt of elapsed time that has passed
	public void Update(float deltaTime) {}
	
	// Draws to the screen. Subclasses must Override this method
	public abstract void Draw (Graphics2D pen);
	
}
