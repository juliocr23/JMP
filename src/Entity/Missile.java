package Entity;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Missile extends Rectangle {

    private BufferedImage missile;

    private double velocity = 2;
    private double acceleration = 0.02;
    private boolean flag;                  //To determine whether to shoot down or up

    public Missile(double x, double y,boolean f){
    	
       super(x,y,0,0);

       flag = f;
        try {
            missile = ImageIO.read(new File("Resources/Background/Missiles.png"));

            width =  (int)(missile.getWidth()*0.4);
            height = (int)(missile.getHeight()*0.2);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void draw(Graphics g){
        g.drawImage(missile,(int)x,(int)y,width,height, null);
    }

    public void launch(){

        if(velocity <= 4)
            velocity += acceleration;

        if(flag)
        	y -= velocity;
        else
        	y+= velocity;
    }

    public void moveLeftBy(int dx){
        x -= dx;
    }

    public void moveRightBy(int dx){
        x += dx;
    }

    public void moveUpBy(int dy){
        y-= dy;
    }

    public void moveDownBy(int dy){
        y+= dy;
    }
}
