import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Iterator;

/* Things this Game can do:
 * Load Maps from text files
 * Place the player
 * Move the player left and right
 * Apply Gravity to the player
 * 
 * What it currently Can't do:
 * Allow the player to jump
 * Assign Animations to the player and tiles
 * Assign Sprites to the player and tiles
 * 
 * Working on making documentation more coherent, 
 * but have projects I've been neglecting
 */

public class GameManager extends Plat2D{
	
	// Managers
	protected InputManager inputManager;
	protected PlayerController pc;
	protected ResourceManager resourceManager;
	protected TileMapRenderer renderer;
	
	// Map
	private TileMap map;
	
	// GameActions and input manager
	private GameAction   CMD_JP,
				 		 CMD_LT,
				 		 CMD_RT;
				 	    // CMD_PA;
	
	// Point Cache
	private Point pointCache = new Point();
	
	public static void main(String[] args) 
	{
        new GameManager().run();
    }
	
	// Initialization Process
	public void Init() 
	{
		super.Init();
		
		// Start Input Manager
		InitInput();
		System.out.println("Input Manager Loaded");
		
		// Start Resource Manager
		resourceManager = new ResourceManager();
		System.out.println("Resource Manager Loaded");
		
		// Load Resources
		renderer = new TileMapRenderer();
		// renderer.SetBackground(null);
		System.out.println("TileMap Renderer Loaded");
		
		// Load First Map
		map = resourceManager.LoadNextMap();
		
		// Assign Player Controller to Player
		pc = new PlayerController( map.GetPlayer() );
		
	}
	
	// Creation of Player Input Actions
	private void InitInput() 
	{
		CMD_JP = new GameAction("Jump", GameAction.INIT_PRESS_ONLY);
		CMD_LT = new GameAction("MoveLeft");
		CMD_RT = new GameAction("MoveRight");
		// CMD_PA = new GameAction("Pause", GameAction.INIT_PRESS_ONLY);
		
		inputManager = new InputManager(sm.GetWindow());
		// CMD_PA = new GameAction("Pause", GameAction.INIT_PRESS_ONLY);
		
		// inputManager.MapToKey(CMD_PA, KeyEvent.VK_ESCAPE);
		inputManager.MapToKey(CMD_JP, KeyEvent.VK_SPACE	);
		inputManager.MapToKey(CMD_LT, KeyEvent.VK_LEFT	);
		inputManager.MapToKey(CMD_RT, KeyEvent.VK_RIGHT	);
	}
	
	// Check player inputs
	private void CheckInput(float deltaTime)
	{
		Player player = (Player) map.GetPlayer();
		
		if (player.IsAlive()) 
		{
			if (CMD_LT.IsPressed()) { player.MoveLT( pc.velocity.x ); pc.MOV_LT = true; }
			else { pc.MOV_LT = false; }
			
			if (CMD_RT.IsPressed()) { player.MoveRT( pc.velocity.x ); pc.MOV_RT = true; }
			else { pc.MOV_RT = false;}
			
			if (CMD_JP.IsPressed()) { pc.JumpHandler(); }
		}
	}
	
	public void Draw(Graphics2D pen) 
	{
		renderer.Draw(pen, map, sm.GetWidth(), sm.GetHeight());
	}
	
	public TileMap GetMap() { return map; }
	
	public Point GetTileCollision(Rect sprite, float newX, float newY) 
	{
		float fromX = Math.min(sprite.GetX(), newX);
	    float fromY = Math.min(sprite.GetY(), newY);
	    
	    float toX = Math.max(sprite.GetX(), newX);
	    float toY = Math.max(sprite.GetY(), newY);

	    // get the tile locations
	    int fromTileX = TileMapRenderer.PixelsToTiles(fromX);
	    int fromTileY = TileMapRenderer.PixelsToTiles(fromY);
	    
	    int toTileX = TileMapRenderer.PixelsToTiles( toX + sprite.GetW() - 1 );
	    int toTileY = TileMapRenderer.PixelsToTiles( toY + sprite.GetH() - 1 );

	    // check each tile for a collision
	    for (int x=fromTileX; x<=toTileX; x++)
	    {
	    	for (int y=fromTileY; y<=toTileY; y++) 
	    	{
	    		if (x < 0 || x >= map.GetWidth() || map.GetTile(x, y) != null )
	            {
	    			System.out.println("There's a tile collision!");
	    			// collision found, return the tile
	                pointCache.setLocation(x, y);
	                return pointCache;
	            }
	        }
	    }

	    // no collision found
	    return null;
	}
	
	public boolean IsCollision(Rect s1, Rect s2) 
	{
        // if the Sprites are the same, return false
        if (s1 == s2) { return false; }

        
        // Check if the sprite is grounded
        // if (s1 instanceof Creature && s2 instanceof Wall && s1.CollidesDN(s2)) { ((Creature)s1).grounded = true; }
        // if (s2 instanceof Creature && s1 instanceof Wall && s2.CollidesDN(s1)) { ((Creature)s2).grounded = true; }
        
        /* Brackeen Implementation
         * Kept for Posterity and Debugging
        
        // get the pixel location of the Rectangles
        int s1x = Math.round(s1.GetX());
        int s1y = Math.round(s1.GetY());
        int s2x = Math.round(s2.GetX());
        int s2y = Math.round(s2.GetY());

        // check if the two rectangles' boundaries intersect
        return (s1x < s2x + s2.GetW()  &&
        		s2x < s1x + s1.GetW()  &&
        		s1y < s2y + s2.GetH()  &&
        		s2y < s1y + s1.GetH() );
        */
        
        return s1.Overlaps(s2);
    }
	
	public Rect GetSpriteCollision(Rect sprite) 
	{
        // run through the list of Sprites
        Iterator i = map.GetEntities();
        while (i.hasNext()) 
        {
            Rect otherSprite = (Rect) i.next();
            if ( IsCollision(sprite, otherSprite) ) 
            {
                // collision found, return the Sprite
                return otherSprite;
            }
        }

        // no collision found
        return null;
    }
	
	public void Update(float deltaTime)
	{
		Creature player = (Creature) map.GetPlayer();
		System.out.println("Update DelTime: " + deltaTime);
		// Player is Dead! Restart
		if (player.GetState() == Creature.STATE_DEAD) 
		{
			map = resourceManager.ReloadMap();
		}
		
		CheckInput(deltaTime);
		
		UpdateCreature(player, deltaTime);
		
		player.Update(deltaTime);
		
		// Player Controller Update
		pc.Update();
		
		Iterator i = map.GetEntities();
		
		while (i.hasNext())
		{
			Rect entity = (Rect) i.next();
			
			if (entity instanceof Creature) 
			{
				Creature creature = (Creature) entity;
				
				if (creature.GetState() == Creature.STATE_DEAD) { i.remove(); }
				else { UpdateCreature(creature, deltaTime); }
			}
			
			entity.Update(deltaTime);
		}
		
	}
	
	public void UpdateCreature(Creature creature, float deltaTime) 
	{
		System.out.println("Update Creature Entered");
		// Apply Gravity;
		if (!creature.IsFlying()) { GravityHandler(creature); }
		
		// Change X Coordinate
		float dx = creature.GetVelX();
		float oldX = creature.GetX();
		float newX = oldX + dx;
		
		Point tile = GetTileCollision(creature, newX, creature.GetY());
		
		if (tile == null) { creature.SetX( newX ); }
		else
		{
			if (dx > 0) // Line up with Tile Boundary
			{ 
				creature.SetX( TileMapRenderer.TilesToPixels(tile.x) - creature.GetW() ); 
			}
			else
			if (dx < 0) 
			{ 
				creature.SetX( TileMapRenderer.TilesToPixels(tile.x + 1) ); 
			}
			
			creature.CollideHorizontal();
		}
		
		System.out.println("Vel X: " + dx);
		System.out.println("New X: " + newX);
		
		if (creature instanceof Player) { CheckPlayerCollision((Player) creature, false); }
		
		// Change Y Coordinate
		float dy   = creature.GetVelY();
		float oldY = creature.GetY();
		float newY = oldY + dy;
		
		tile = GetTileCollision(creature, creature.GetX(), newY);

		// Not grounded until proven otherwise
		creature.grounded = false;
		
		if (tile == null) 
		{ 
			creature.SetY( newY ); 
		}
		else
		{
			if (dy > 0) 
			{ 
				// Line up with Tile Boundary
				creature.SetY( TileMapRenderer.TilesToPixels(tile.y) - creature.GetH() ); 
				
				// Creature is proven to be grounded
				creature.grounded = true;
			}
			else
			if (dy < 0) 
			{ 
				creature.SetY( TileMapRenderer.TilesToPixels(tile.y + 1) ); 
			}
			
			creature.CollideVertical();
		}
		
		System.out.println("Pos Y: " + creature.GetY());
		System.out.println("Vel Y: " + dy);
		System.out.println("New Y: " + newY);
		System.out.println("Grounded: " + creature.grounded);
	}
	
	// Check for player collision with other sprites
	public void CheckPlayerCollision(Player player, boolean canKill) 
	{
		if (!player.IsAlive()) {return;}
		
		Rect collisionSprite = GetSpriteCollision(player);
		if (collisionSprite instanceof Creature)
		{
			Creature badguy = (Creature) collisionSprite;
			if (canKill) {}
			else { player.SetState(Creature.STATE_DYING); }
		}
		
	}
	
	// Crunch the numbers for gravity
	public void GravityHandler (Creature creature) 
	{
		if (creature.grounded) { creature.SetVelY (creature.Gravity); }
		else
		{
			float aerialVelocity = Player.FallAcceleration;
			if (creature instanceof Player) 
			{
				if (pc.endJumpEarly && creature.dy > 0) { aerialVelocity *= Player.JumpEndEarlyGravityModifier; }
			}
			
			creature.SetVelY( Vector2D.MoveTowards(creature.dy, Player.MaxFallSpeed, aerialVelocity * Plat2D.DeltaTime()) );
		}
	}
}
