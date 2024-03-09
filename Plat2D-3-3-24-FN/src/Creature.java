import java.lang.reflect.Constructor;

/* Abstract class that defines what a creature is,
 * creatures are entities which can move around,
 * and are effected by physics unless otherwise stated
 */
public abstract class Creature extends Rect{
	
	// Physics Numbers
	public static final float Gravity = 1.5f;
	
	// Generic states in which a creature can remain in
	public static final int STATE_NORMAL = 0;
	public static final int STATE_DYING = 1;
	public static final int STATE_DEAD = 2;
	
	// time to die
	private static final int DIE_TIME = 100;
	
	// Character States
	public  int  state;
	private long stateTime;
	
	// For Character Jumping
	public boolean grounded;
	
	// Animation Assigning
	private Animation idleLeft;
	private Animation idleRight;
	private Animation moveLeft;
	private Animation moveRight;
	
	
	// Constructor
	public Creature(int x, int y, int width, int height) { super(x, y, width, height); }
	
	public float GetMaxSpeed() {return 0;}
	
	public void SetState(int state) 
	{
		if (this.state != state)
		{
			this.state = state;
			stateTime = 0;
			if (state == STATE_DYING)
			{
				// TODO: Stop the creature from moving
			}
		}
	}
	
	// Applying Animations to a Creature
	public void AssignAnimation(Animation idleLeft, Animation idleRight, Animation moveLeft, Animation moveRight) 
	{
		this.idleLeft = idleLeft;
		this.idleRight = idleRight;
		this.moveLeft = moveLeft;
		this.moveRight = moveRight;
		
		super.AssignAnimation(idleRight);
		
	}
	
	public Object Clone()
	{
		Constructor constructor = getClass().getConstructors()[0];
		try 
		{
			return constructor.newInstance(new Object[] 
			{
				(Animation) idleLeft.Clone(),
				(Animation) idleRight.Clone(),
				(Animation) moveLeft.Clone(),
				(Animation) moveRight.Clone()
			});
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	
	// Start Moving Left
	public void WakeUp() 
	{
		if (GetState() == STATE_NORMAL && GetVelX() == 0) 
		{
			SetVelX( -GetMaxSpeed() );
		}
	}
	
	public void Update(float deltaTime) 
	{
		/* TODO: Animation Handling
		Animation newAnim = anim;
		
		if (GetVelX() < 0) { newAnim = moveLeft; }
		else
		if (GetVelX() > 0) { newAnim = moveRight; }
		
		if (dx == 0 && newAnim == moveLeft)  { newAnim = idleLeft;  }
		else
		if (dx == 0 && newAnim == moveRight) { newAnim = idleRight; }
		
		if (anim != null) 
		{
			if (anim != newAnim)
			{
				anim = newAnim;
				anim.Start();
			}
		
			else { anim.Update(deltaTime); }
		}
		*/
		stateTime += deltaTime;
		if (state == STATE_DYING && stateTime >= DIE_TIME) { SetState(STATE_DEAD); }
	}

	public void CollideHorizontal() { SetVelX(-GetVelX()); }
	public void CollideVertical() 	{ SetVelY(0); }
	
	
	public int GetState() 	 {return state;}
	public boolean IsAlive() {return state == STATE_NORMAL;}

	public boolean IsFlying() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
