
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.media.opengl.*;
import com.sun.opengl.util.Animator;

public class Bouncing extends JFrame {

    static Animator animator = null;

    public static void main(String[] args) {
        final Bouncing app = new Bouncing();


        SwingUtilities.invokeLater (
            new Runnable() {
                public void run() {
                    app.setVisible(true);
                }
            }
        );
        

        SwingUtilities.invokeLater (
            new Runnable() {
                public void run() {
                    animator.start();
                    
                }
            }
        );
    }
    
    public Bouncing() {

        super("Pong Oyunu");
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        BouncingDisplay display = new BouncingDisplay();
        

        GLCapabilities glcaps = new GLCapabilities();
        GLDrawableFactory gldFactory = 
           GLDrawableFactory.getFactory();
        GLCanvas glcanvas = new GLCanvas(glcaps);
        glcanvas.addGLEventListener(display);
        glcanvas.addKeyListener(display);
        
        

        animator = new Animator(glcanvas);
        
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        setSize(640, 480);
        

        centerWindow(this);
    }
    
    public void centerWindow(Component frame) {
        Dimension screenSize = 
           Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize  = frame.getSize();

        if (frameSize.width  > screenSize.width ) 
           frameSize.width  = screenSize.width;
        if (frameSize.height > screenSize.height) 
           frameSize.height = screenSize.height;

        frame.setLocation (
            (screenSize.width  - frameSize.width ) >> 1, 
            (screenSize.height - frameSize.height) >> 1
        );
    }    
}
