import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/* RESOURCEMANAGER.JAVA
 * Loads Images, Creates animations and Sprites, and Loads levels
 */

public class ResourceManager {

    private ArrayList tiles;
    private int currentMap;

    // host sprites used for cloning
    // TODO: Change to player/rects
    private Player 	player = new Player(32, 32, 0, 0);
    private Wall 	wall   = new Wall(64, 64, 0, 0);
    
    /**
        Creates a new ResourceManager with the specified
        GraphicsConfiguration.
    */

    public ResourceManager(/*GraphicsConfiguration gc*/) 
    {
    	System.out.println("Trying to load Tile Images");
    	// loadTileImages();
    	System.out.println("Tileimages Loaded");
        LoadCreatureSprites();
    	System.out.println("Creature Sprites Loaded");
    }


    // Gets an image from the Assets/ directory.
    public Image loadImage(String name) 
    {
        String filename = "Assets/" + name;
        return new ImageIcon(filename).getImage();
    }

    // Mirrors an Image
    public Image getMirrorImage (Image image) { return getScaledImage(image, -1, 1); }

    // Flips an Image Upside Down
    public Image getFlippedImage(Image image) { return getScaledImage(image, 1, -1); }


    private Image getScaledImage(Image image, float x, float y) 
    {
        // set up the transform
        AffineTransform transform = new AffineTransform();
        transform.scale(x, y);
        transform.translate
        (
            (x-1) * image.getWidth (null) / 2,
            (y-1) * image.getHeight(null) / 2
        );

        // create a transparent (not translucent) image
        Image newImage = new BufferedImage( image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // draw the transformed image
        Graphics2D g = (Graphics2D)newImage.getGraphics();
        g.drawImage(image, transform, null);
        g.dispose();

        return newImage;
    }


    public TileMap LoadNextMap() 
    {

		System.out.println("Attempting to Load Next Map");
        TileMap map = null;
        
        while (map == null) 
        {
            currentMap++;
            try { map = LoadMap( "Maps/map" + currentMap + ".txt" ); }
            catch (IOException ex) 
            {
                // no maps to load!
                if (currentMap == 1) { return null; }
                currentMap = 0;
                map = null;
            }
        }

        return map;
    }


    public TileMap ReloadMap() {
        try { return LoadMap( "Maps/map" + currentMap + ".txt"); }
        catch (IOException ex) 
        {
            ex.printStackTrace();
            return null;
        }
    }

    
    // Loading Tile-Map Text Files
    private TileMap LoadMap(String filename) throws IOException
    {
        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;

        // Read every line in the text file into the list
        BufferedReader reader = new BufferedReader ( new FileReader(filename) );
        
        while (true) {
        	String line = reader.readLine();
            
            // no more lines to read
            if (line == null) 
            {
                reader.close();
                break;
            }

            // add every line except for comments
            if (!line.startsWith("#")) 
            {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }

        // parse the lines to create a TileEngine
        height = lines.size();

        TileMap newMap = new TileMap(width, height);
        
        for (int y = 0; y < height; y++) 
        {
            String line = (String)lines.get(y);
            
            for (int x = 0; x < line.length(); x++) 
            {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C etc.
                int tile = ch - 'A';
                
                if (tile >= 0) 
                {
                    newMap.SetTile(x, y, new Wall(TileMapRenderer.TilesToPixels(x), TileMapRenderer.TilesToPixels(y), 64, 64) );
                }

                // check if the char represents a sprite
                else 
                if (ch == 'o') { /* addSprite(newMap, coinSprite, x, y);  */ }
                else 
                if (ch == '!') { /* addSprite(newMap, musicSprite, x, y); */ }
                else 
                if (ch == '*') { /* addSprite(newMap, goalSprite, x, y); */  }
                else 
                if (ch == '1') { /* addSprite(newMap, grubSprite, x, y); */  }
                else 
                if (ch == '2') { /* addSprite(newMap, flySprite, x, y); */ 	 }
            }
        }

        player.SetX( TileMapRenderer.TilesToPixels(3) );
        player.SetY(0);
        newMap.SetPlayer(player);

        return newMap;
    }


    private void AddRect(TileMap map, Rect hostSprite, int tileX, int tileY)
    {
        if (hostSprite != null) {
            // clone the sprite from the "host"
            Rect sprite = (Rect) hostSprite.Clone();

            // center the sprite
            sprite.SetX( (int) (TileMapRenderer.TilesToPixels(tileX) + (TileMapRenderer.TilesToPixels(1) - sprite.GetW()) / 2) );

            // bottom-justify the sprite
            sprite.SetY( (int) (TileMapRenderer.TilesToPixels(tileY + 1) - sprite.GetH()) );

            // add it to the map
            map.AddRect(sprite);
        }
    }


    // -----------------------------------------------------------
    // code for loading sprites and images
    // -----------------------------------------------------------


    public void loadTileImages() 
    {
        // keep looking for tile A,B,C, etc. this makes it
        // easy to drop new tiles in the images/ directory
        tiles = new ArrayList();
        char ch = 'A';
        while (true) {
        	
        	// Currently, load rectangles
        	tiles.add(new Wall(75, 75, 75, 75) );
        	ch++;
            
        	/* TODO: Brackeen Implementation, somehow fix this
        	String name = "tile_" + ch + ".png";
            File file = new File("Assets/" + name);
            if (!file.exists()) {
                break;
            }
            
            tiles.add(loadImage(name));
            ch++;
            */
        }
    }


    public void LoadCreatureSprites() {
    	
    	// Load Rectangles for now
    	// We're only loading the player, so just make the player
    	player = new Player(100, 100, 5, 5);
    	
    	/* TODO: Brackeen Implementation, adapt to rect class
        Image[][] images = new Image[4][];

        // load left-facing images
        images[0] = new Image[] 
        {
            loadImage("player1.png"),
            loadImage("player2.png"),
            loadImage("player3.png")
        };
        
        
        images[1] = new Image[images[0].length];
        images[2] = new Image[images[0].length];
        images[3] = new Image[images[0].length];
        
        for (int i = 0; i < images[0].length; i++) 
        {
            // right-facing images
            images[1][i] = getMirrorImage(images[0][i]);
            // left-facing "dead" images
            images[2][i] = getFlippedImage(images[0][i]);
            // right-facing "dead" images
            images[3][i] = getFlippedImage(images[1][i]);
        }

        // create creature animations
        Animation[] playerAnim = new Animation[4];

        // Create creature Sprites
        
        // Brackeen Implementation
        // playerSprite = new Player(playerAnim[0], playerAnim[1], playerAnim[2], playerAnim[3]);
        
        // Assigning Animations to Player
        player.AssignAnimation(playerAnim[0], playerAnim[1], playerAnim[2], playerAnim[3]);
       */
    }


    private Animation CreatePlayerAnim(Image player1, Image player2, Image player3)
    {
        Animation anim = new Animation();
        anim.AddFrame(player1, 250);
        anim.AddFrame(player2, 150);
        anim.AddFrame(player1, 150);
        anim.AddFrame(player2, 150);
        anim.AddFrame(player3, 200);
        anim.AddFrame(player2, 150);
        return anim;
    }


}
