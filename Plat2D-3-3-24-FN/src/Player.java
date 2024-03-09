public class Player extends Creature {
	
	// Ground Speed Handling
	public static final float MaxSpeed = 14; // top speed a player can achieve
	public static final float Acceleration = .25f; 
	public static final float GroundDeceleration = Acceleration/2;
	
	// Air Speed Handling
	public static final float AirDeceleration = GroundDeceleration/2;
	public static final float JumpPower = 120;
	public static final float JumpEndEarlyGravityModifier = 3;
	
	// Fall Handling
	public static final float MaxFallSpeed = 40;
	public static final float FallAcceleration = 110;
	
	// Input Handling
	public static final float JumpBuffer = .2f;
	public static final float CoyoteTime = .15f;
	
	// Player States
	public static final int STATE_NORMAL  = 0;
	public static final int STATE_JUMPING = 1;
	
	// Get if player is attempting to move
	public boolean MOV_LT, MOV_RT;
	
	// Player Controller (Temporary)
	private PlayerController pc; 
	
	public Player(int x, int y, int width, int height) 
	{
		super(x, y, width, height);
	}
	
	public int GetState() { return state; }
	public void SetState(int state) { this.state = state; }
	
	public void Jump() 		
	{ 
		pc.JumpHandler();
		state = STATE_JUMPING;
	}
	
	public void MoveLeft()  { this.MoveLT( pc.velocity.x ); }
	public void MoveRight() { this.MoveRT( pc.velocity.x ); }
	
}
