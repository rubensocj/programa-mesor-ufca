


import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.rosuda.JRI.Rengine;
import org.rosuda.JRI.REXP;
import org.rosuda.javaGD.GDCanvas;
import org.rosuda.javaGD.GDInterface;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rubens Oliveira da Cunha Júnior
 */
public class TesteJavaGD {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Plot pl = new Plot();
        pl.setVisible(true);
        pl.plotar();
    }
    
    public static class Plot extends JFrame {
    
        private Rengine re;
        public static GDCanvas gdc;
        
        public Plot() {
            rSeesion();
            jGUI();
        }            
    
        private void rSeesion() {
            // Checa se as atuais versões do Java e do JRI são compatíveis.
            if (!Rengine.versionCheck()) {
                System.err.println("** Erro de compatibilidade Java/JRI-REngine.");
                System.exit(1);
            }

            System.out.println("\n-------------------");
            System.out.println("Criando JRI REngine");

            // Argumentos passados para a sessão do R
            String[] args = new String[1];
            args[0] = "--vanilla";

            // Inicia o Rengine (JRI)
            re = new Rengine(args, false, null);

            // Checa o estado do R, retorna true se está executando
            System.out.println("REngine criada, esperando pelo R...");
            if (!re.waitForR()) {
                System.out.println("Não foi possível carregar R");
            } else {
                System.out.println("REngine pronta.");
                System.out.println("-------------------\n");
            }
            
            re.eval("Sys.setenv('JAVAGD_CLASS_NAME'='r/TesteJavaGD2')");
            re.eval("library(JavaGD)");
        }
    
        private void jGUI() {
            gdc = new GDCanvas(400,400);
            
            JPanel p = new JPanel();
            p.add(gdc);
            
            this.add(p);
            this.setTitle("JavaGD plot");
            this.pack();
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);         
        }
        
        public void plotar() {
            re.eval("JavaGD(\"JavaGD\",400,400,12)");
            re.eval("plot(c(1,2,3))");
            
//            gdc.initRefresh();
        }
        
        @Override
        public void paint(Graphics g) {
            BufferedImage img = null;
            try {
            img = ImageIO.read(new File("wgrp_plot.png"));
        } catch (IOException e) {e.printStackTrace();}
            g.drawImage(img, 0, 0, null);
        }
        
    }
}

class TesteJavaGD2 extends GDInterface {
    @Override
    public void gdOpen(double w, double h) {
        c = TesteJavaGD.Plot.gdc;
    }
}
