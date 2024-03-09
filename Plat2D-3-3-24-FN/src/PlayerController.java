import java.awt.event.*;

public class PlayerController{
	
	private Rect good;
	
	private float time;
	private float timeJumpWasPressed;
	
	public Vector2D velocity = new Vector2D(0,0);
	
	// Booleans for Jumping
	public boolean endJumpEarly, jumpToConsume, bufferedJumpUsable;
	public boolean grounded, holdingWall = false;
	
	private boolean hasBufferedJump = (bufferedJumpUsable && time < timeJumpWasPressed + Player.JumpBuffer);
	private boolean coyoteUsable    = (bufferedJumpUsable && time < timeJumpWasPressed + Player.JumpBuffer);
	
	public boolean MOV_LT, MOV_RT;
	
	// Constructor
	public PlayerController(Player good) 
	{
		this.good = good; // Get Player Collision Detector
	}

	// Update Event
	public void Update() 
	{
		time += Plat2D.DeltaTime();
		MovementHandler();
		MovePlayer();
	}
	
	// Things to do
	public void MovePlayer() 
	{
		if (MOV_RT) {good.MoveRT( velocity.x );}
		if (MOV_LT) {good.MoveLT( velocity.x );}
	}
	
	
	// Handling behind player acceleration and deceleration
	public void MovementHandler() 
	{
		if (!(MOV_LT || MOV_RT))
		{
			float deceleration;
			
			if (grounded) {deceleration = Player.GroundDeceleration;}
			else 		  {deceleration = Player.AirDeceleration;}
			
			// Stop moving infintesmally small amounts
			if (velocity.x < 0.05f)	{velocity.x = 0;}
			
			velocity.x = Vector2D.Lerp(velocity.x, 0, deceleration);
		}
		else
		{
			velocity.x = Vector2D.Lerp(velocity.x, Player.MaxSpeed, Player.Acceleration);
		}
	}	
	
	// Handling physics behind player jumping
	public void JumpHandler() 
	{
		/* TODO: Implement
		// The player has released the jump button mid air 
		if ( !(endJumpEarly && grounded && velocity.y > 0 && JP_Pressed) )
		{
			endJumpEarly = true;
		}
		
		
		// The player cannot jump
		if (!(jumpToConsume && hasBufferedJump)) {return;}
		*/
		
		System.out.println("JP_PRESSED");
		// The player can jump
		if (grounded || holdingWall) {ExecuteJump();}
		
		// jumpToConsume = false;
	}
	
	// Jumping Function
	public void ExecuteJump() 
	{
		endJumpEarly = false;
		timeJumpWasPressed = 0;
		coyoteUsable = false;
		bufferedJumpUsable = false;
		
		// Wall-Jumping
		if (holdingWall) { good.SetVelX(-Player.JumpPower); }
		
		good.SetVelY(-Player.JumpPower);
	}
	
	/*
	// Wall Jumping (old)
	public void CollisionDetection(Rect[] neighbors) 
	{
		// grounded = false; 
		holdingWall = false;
		
		for (Rect neighbor : neighbors) {
			if (neighbor instanceof Wall && good.Overlaps(neighbor)) 
			{ 
				if (good.CollidesDN(neighbor)) { grounded = true; }
				if (good.CollidesRT(neighbor)) { holdingWall = true; }
				if (good.CollidesLT(neighbor)) { holdingWall = true; }
			}
		}
		
	}
	*/
	
	/* TODO:
	 * - Fix Jumping, WallJumping
	 * -- Make Ending Jumps early shorten the jump
	 * -- Add in Coyote Time, Jump Buffering
	 * -- De-bloat this class
	 * -- Fix Collisions 
	 */
}
