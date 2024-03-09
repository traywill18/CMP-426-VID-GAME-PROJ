
public class Wall extends Rect {

	public Wall(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	// Collision Overrides
	
	public void OnCollisionRT(Rect neighbor) {neighbor.PushbackFrmRT(this);}
	public void OnCollisionLT(Rect neighbor) {neighbor.PushbackFrmLT(this);}
	public void OnCollisionUP(Rect neighbor) {neighbor.PushbackFrmUP(this);}
	public void OnCollisionDN(Rect neighbor) {neighbor.PushbackFrmDN(this);}
}
