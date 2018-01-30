
import java.awt.Canvas;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rubens Oliveira da Cunha Júnior
 */
public class TesteGraphicsDevice {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        GraphicsDevice[] gs = ge.getScreenDevices();
//        for (int j = 0; j < gs.length; j++) {
//           GraphicsDevice gd = gs[j];
//           GraphicsConfiguration[] gc =
//           gd.getConfigurations();
//           for (int i=0; i < gc.length; i++) {
//              JFrame f = new
//              JFrame(gs[j].getDefaultConfiguration());
//              Canvas c = new Canvas(gc[i]);
//              Rectangle gcBounds = gc[i].getBounds();
//              int xoffs = gcBounds.x;
//              int yoffs = gcBounds.y;
//              f.getContentPane().add(c);
//              f.setLocation((i*50)+xoffs, (i*60)+yoffs);
//              f.show();
//           }
//        }



      Rectangle virtualBounds = new Rectangle();
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsDevice[] gs = ge.getScreenDevices();
      for (int j = 0; j < gs.length; j++) {
          GraphicsDevice gd = gs[j];
          GraphicsConfiguration[] gc = gd.getConfigurations();
          Frame f = new Frame(gc[0]);
      Rectangle bounds = gc[0].getBounds();
      f.setLocation(10 + bounds.x, 10 + bounds.y);
      f.setVisible(true);
      }
    }

}
