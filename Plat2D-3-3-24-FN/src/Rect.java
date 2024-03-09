import java.awt.Graphics;
import java.awt.Image;

public class Rect 
{
	float x, y, dx, dy; 
	
	int width, height;
	float oldX, oldY;
	
	boolean overlap;
	protected Animation anim;
	
	public Rect(float x, float y, int width, int height) 
	{
		
		this.x = x;
		this.y = y;
		
		this.width  = width;
		this.height = height;
		
		oldX =  x;
		oldY =  y;
	}
	
	public void AssignAnimation(Animation anim) {this.anim = anim;}
	
	public void ResizeBy(int dw, int dh) 
	{
		width += dw;
		height+= dh;
	}
	
	public void Draw(Graphics pen) 
	{
		pen.drawRect( (int) x, (int) y, width, height);
	}
	
	public boolean Overlaps(Rect r) 
	{
		// Get top left and bottom right of both rectangles.
		float cornerX = x + width;
		float cornerY = y + height;
		
		float rCornerX = r.x + r.width;
		float rCornerY = r.y + r.height;
		
		return (	(cornerX  >= r.x) 
				&& 	(cornerY  >= r.y) 
				&& 	(rCornerX >= x)	
				&& 	(rCornerY >= y)  );
		
	}
	

	// Pushback function for Collisions with impermeable objects
	public void PushbackFrmLT(Rect r) {x = r.x -   width  - 1;}
	public void PushbackFrmRT(Rect r) {x = r.x + r.width  + 1;}
	public void PushbackFrmUP(Rect r) {y = r.y -   height - 1;}
	public void PushbackFrmDN(Rect r) {y = r.y + r.height + 1;}

	// Movement Functions
	public void MoveUP(float dy) 
	{
		oldY = y;
		y -= dy;
	}
	public void MoveDN(float dy) 
	{
		oldY = y;
		y += dy;
	}
	public void MoveRT(float dx) 
	{
		oldX = x;
		x += dx;
	}
	public void MoveLT(float dx) 
	{
		oldX = x;
		x -= dx;
	}

	// Updates this entity's Animation and it's position based on velocity
	public void Update(float deltaTime) 
	{
		if (anim != null) {anim.Update(deltaTime);}
	}
	
	// Detecting where a collision happened
	public boolean CollidesLT(Rect r) {return oldX + width  < r.x + 1;}
	public boolean CollidesRT(Rect r) {return oldX + 1 > r.x + r.width - 1;}
	public boolean CollidesDN(Rect r) {return oldY + height < r.y;}
	public boolean CollidesUP(Rect r) {return oldY - 1 > r.y + r.height;}
	
	// Do something when a Collision Happens
	public void CheckCollision(Rect neighbor) 
	{
		if (neighbor.CollidesLT(this)) {System.out.println(this + " ColLT: " + neighbor); OnCollisionLT(neighbor);}
		if (neighbor.CollidesRT(this)) {System.out.println(this + " ColRT: " + neighbor); OnCollisionRT(neighbor);}
		if (neighbor.CollidesDN(this)) {System.out.println(this + " ColDN: " + neighbor); OnCollisionUP(neighbor);}
		if (neighbor.CollidesUP(this)) {System.out.println(this + " ColUP: " + neighbor); OnCollisionDN(neighbor);}
	}
	
	// Override-able for Collision Handling
	public void OnCollisionLT(Rect neighbor) {}
	public void OnCollisionRT(Rect neighbor) {}
	public void OnCollisionDN(Rect neighbor) {}
	public void OnCollisionUP(Rect neighbor) {}
	
	// Getting Values of a Rectangle
	public float GetX() {return x;}
	public float GetY() {return y;}
	public float GetW() {return width;}
	public float GetH() {return height;}

	// Setting Positions on the Screen
	public void SetX(float x) {this.x = x;}
	public void SetY(float y) {this.y = y;}
	public void SetW(int w) {this.width  = w;}
	public void SetH(int h) {this.height = h;}
	
	// Getting and Setting Velocity
	public float GetVelX() {return dx;}
	public float GetVelY() {return dy;}
	public void  SetVelX( float dx ) { this.dx = dx; }
	public void  SetVelY( float dy ) { this.dy = dy; }
	
	// Get Animation Image
	public Image GetImage() { return anim.GetImage(); }
	
	// Cloning this Object
	public Object Clone() 
	{ 
		Rect cloneRect = new Rect(x,y, width, height);
		if (anim != null) 
		{
			cloneRect.AssignAnimation(anim);
		}
		return cloneRect; 
	}
}
