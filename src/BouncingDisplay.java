import java.awt.*;
import java.awt.event.*;

import javax.management.timer.Timer;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.j2d.TextRenderer;

public class BouncingDisplay implements GLEventListener,KeyListener {
    
    float a = 120;
    float b = 180;
    
    float slope = 7.0f/6.0f;
    
    float x = a; 
    float y = b; 
    float px = 100;
    float min_py = 180;
    int plen = 70;
    float margin = .3f;
    int speed = 25;
    
    int life = 5;
    
    long start;
    long end;
    long elapsed;
    double second;
    int minute;
    String diff_name;
    
    TextRenderer render_life = new TextRenderer(new Font("SansSerif", Font.BOLD, 48));
    TextRenderer render_time = new TextRenderer(new Font("SansSerif", Font.BOLD, 48));
    TextRenderer render_diff = new TextRenderer(new Font("SansSerif", Font.BOLD, 48));
    
    boolean finished = false;
    boolean pause = true;
    boolean movingRight = true;
    boolean movingUp = true;
    boolean easy = true;
    boolean over = false;
        public void init(GLAutoDrawable gld) {
        
        start = System.nanoTime();
    	GL gl = gld.getGL();
        GLU glu = new GLU();
        
        
        gl.glClearColor(0f, 0f, 0f, 0.0f);
        
        
        gl.glPointSize(10.0f);
        
        gl.glViewport(0, 0, 640, 480);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluOrtho2D(0, 640, 0, 480);
        
    }
    
    public void display(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        end = System.nanoTime();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);


        float red = 1f;
        float green = 1f;
        float blue = 1f;
        gl.glColor3f(red, green, blue);
        
		if (easy){
			margin = .3f;
			speed = 25;
			diff_name = "Kolay";
		}
		else {
			margin = .5f;
			speed = 15;
			diff_name = "Zor";
			
		}
        
        elapsed = end - start;
		second = elapsed / 1E09;
		
    	render_time.beginRendering(640,480); 
    	render_time.setColor(1.0f, 1.0f, 1.0f, 1.f); 
        render_time.draw(Integer.toString(((int)second)/60)+":"+Integer.toString(((int)second)%60),500,420);
        render_time.endRendering();
        
    	render_life.beginRendering(640,480); 
    	render_life.setColor(1.0f, 1.0f, 1.0f, 1.f); 
        render_life.draw(Integer.toString(life),350,420);
        render_life.endRendering();
        
    	render_diff.beginRendering(640,480); 
    	render_diff.setColor(1.0f, 1.0f, 1.0f, 1.f);
        render_diff.draw(diff_name,10,420);
        render_diff.endRendering();


	        for (int i=0;i<=5;i++){
		        gl.glBegin(GL.GL_LINES);
		        gl.glVertex2d(0,400+i);
		        gl.glVertex2d(640,400+i);
		        gl.glEnd();
		        
		    }


        drawPaddle(gld);
        
        if (!pause){
        y = (slope * (x - a) + b);
        

		if (movingRight) {
			
			if (x  < 640) {
                x += margin; 
            } else {
                movingRight = false;
                slope *= -1;
                a = x;
                b = y;
            }
        }
        if (! movingRight) {
            if (x > 0) {
                x -= margin;
            } else {
                movingRight = true;
                slope *= -1;
                a = x;
                b = y;
            }
        }
        
		if (movingUp) {
            if (((int)y > 400)) {
            	slope *= -1;
                a = x;
                b = y;
                movingUp = false;
                System.out.println("ASAGI SEKTI"+slope);
            }
            if (y< 0) movingUp=false;
        }
        if (! movingUp) {
            if (((int)y < 0 )) {
                y = 0;
            	slope *= -1;
                a = x;
                b = y;
                movingUp = true;
                System.out.println("YUKARI SEKTI"+slope);
            }
            if (y>402 && y < 403){ 
            	if (slope > 0){
            		a=x;
                	b=y;
            		slope *=-1;
            	}
            }
        }
        if ((int)x == ((int)px)%640 && y > min_py%400 && y < (min_py+plen)%400 ){
        	slope *= -1;
            a = x;
            b = y;
            movingRight = true;
        }
        
        if ((int)x < 90)
        {
        	life -= 1;
        	if(!over) pause = true;
        	else over = false;
        	
        	movingRight = true;

        	x = 120;
        	y = 180;

        }
        
        System.out.println("y="+y+" x="+x+" m="+slope+" a="+a+" b="+b);

	   gl.glBegin(GL.GL_POINTS);
          gl.glVertex2d(x, y);
        gl.glEnd();

    }
      if (pause && life == 0){
    	  gl.glClear(GL.GL_COLOR_BUFFER_BIT);
      	  render_life.beginRendering(640,480); 
      	  render_life.setColor(1.0f, 1.0f, 1.0f, 1.f); 
          render_life.draw("Oyun Bitti",150,240);
          render_life.endRendering();
          start = end;
          finished = true;
      }
    }
    public void drawPaddle(GLAutoDrawable gld){
    	GL gl = gld.getGL();
    	for (int i=0;i<=plen;i++){
    		   gl.glBegin(GL.GL_POINTS);
    	          gl.glVertex2d(px, (min_py+i)%400);
    	        gl.glEnd();
    	}
    	
    }

    public void reshape(
                        GLAutoDrawable drawable, 
                        int x, 
                        int y, 
                        int width, 
                        int height
                      ) {}

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		
		if (finished){
			life = 5;
			finished = false;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_LEFT){
			x = 80;
			life +=1;
			over = true;
			movingUp = true;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_UP){
			min_py += speed;
			pause = false;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_SPACE){
			pause = true;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_DOWN){
			min_py -= speed;
			pause = false;
			if (min_py < 0) min_py = 400;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_K){
			easy = true;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_Z){
			easy = false;
		}
		
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
    }

