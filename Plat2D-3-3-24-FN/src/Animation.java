
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation {
	private ArrayList frames;
	private int currFrameIndex;
	private long animTime;
	private long totalDuration;
	
	// Creates a new, empty Animation
	public Animation() { 
		this(new ArrayList(), 0); 
	}
	
	private Animation(ArrayList frames, long totalDuration) 
	{
		this.frames = frames;
		this.totalDuration = totalDuration;
		Start();
	}
	
	// Adds an Image to the Animation with the Specified Duration (time to display image)
	public synchronized void AddFrame(Image img, long duration) 
	{
		totalDuration += duration;
		frames.add(new AnimFrame(img, totalDuration));
	}
	
	// Starts the animation from the beginning
	public synchronized void Start()
	{
		animTime = 0;
		currFrameIndex = 0;
	}
	
	// Updates current image in animation if necessary
	public synchronized void Update(float deltaTime) 
	{
		if (frames.size() > 1) 
		{
			animTime += deltaTime;
			if (animTime >= totalDuration)
			{
				animTime = animTime % totalDuration;
				currFrameIndex = 0;
			}
			while (animTime > GetFrame(currFrameIndex).endTime) {currFrameIndex++;}
		}
	}
	
	// Gets animation's current image, returns null if animation has no images
	public synchronized Image GetImage()
	{
		if 	 (frames.size() == 0) { return null; }
		else { return GetFrame(currFrameIndex).image; }
	}
	
	private AnimFrame GetFrame(int i) { return (AnimFrame) frames.get(i); }
	
	private class AnimFrame
	{
		Image image;
		long  endTime;
		
		public AnimFrame(Image image, long endTime) 
		{
			this.image 	 = image;
			this.endTime = endTime;
		}
	}
	
	// Creates a duplicate of the animation. 
	public Object Clone() { return new Animation(frames, totalDuration); }
	
	// Processes a sprite-sheet into an animation.
	// Takes an empty animation object (anim) and processes an image (spritesheet) to make a full animation
	
	public static Animation ReadSheet(Animation anim, Image spritesheet, int tileHeight, int tileWidth, int duration)
	{
		int sheetWidth 	= spritesheet.getWidth(null)  / tileWidth;
		int sheetHeight = spritesheet.getHeight(null) / tileHeight; 
		BufferedImage sheet = (BufferedImage) spritesheet;
		
		for (int i = 0; i < sheetWidth; i++) 
		{
			for (int j = 0; j < sheetHeight; j++) 
			{
				anim.AddFrame( (Image) sheet.getSubimage(i * tileWidth, j * tileHeight, tileWidth, tileHeight), duration );
			}
		}
		
		return anim;
	}
}
