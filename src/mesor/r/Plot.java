/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mesor.r;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import mesor.menu.principal.PainelPrincipal;

/**
 * Plot.java
 *
 * @version 1.0 22/10/2017
 * @author Rubens Júnior
 */
public class Plot extends JPanel {
    
    private static JLabel imageLabel;
    private static Graphics2D g2d;
    private static final BufferedImage BFFRD_IMAGEM = new BufferedImage(
                                        1024, 768, BufferedImage.TYPE_INT_RGB);
    private static BufferedImage img = null;
    
    private double h1, w1; // Dimensões da imagem original plotada no R
    private double w2, h2; // Dimensões da imagem exibida na tela
    private double ratio;  // Aspect ratio da imagem original
    private int wPnl;      // Dimensões do painel em abas que abrigará o plot
    
    // -------------------------------------------------------------------------
    // Construtores.
    // -------------------------------------------------------------------------
    
    /**
     * Construtor padrão.
     */
    public Plot() {
        try {
            img = ImageIO.read(new File(mesor.menu.Janela.LOCAL + "wgrp_plot.png"));
        } catch (IOException e) {e.printStackTrace();}
        
//        btn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//            }
//        });
        
//        addComponentListener(new ComponentListener() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                int largura = getWidth();
//                int altura = getHeight();
//                int alt = e.getComponent().getHeight();
//                int lar = e.getComponent().getWidth();
//                Image timg = img.getScaledInstance(lar, alt, Image.SCALE_SMOOTH);
//                Graphics2D g5d = img.createGraphics();
//                g5d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//                g5d.drawImage(timg, 0, 0, null);
//                g5d.dispose();
//                
////                BufferedImage img1 = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
////                Graphics2D g2d = img.createGraphics();
////                Graphics g1 = img1.getGraphics();
////                g1.fillOval(0, 0, 20, 20);
////                Graphics g = img.getGraphics();
////                g.fillOval(0, 0, 20, 20);
////                paint(g);
////                paint(g1, img);
//                
//                System.out.println(altura + " - " + largura);
//            }
//
//            @Override
//            public void componentMoved(ComponentEvent e) {
//            }
//
//            @Override
//            public void componentShown(ComponentEvent e) {
//            }
//
//            @Override
//            public void componentHidden(ComponentEvent e) {
//            }
//        });
    }
    
    /**
     * Exibe o plot do R na tela. Redimensiona a imagem exibida de acordo com
     * as dimensões da aba onde será mostrada, mantendo o aspect ratio original.
     * 
     * @param x
     * @param y 
     */
    public Plot(int x, int y) {
        // Pega a largura da aba que mostrará o plot
        wPnl = x;
        
        try {
            // Cria a imagem a partir do arquivo
            // Pega o aspect ratio da imagem importada
            BufferedImage p = ImageIO.read(new File(mesor.menu.Janela.LOCAL + "\\wgrp_plot.png"));
            h1 = p.getHeight(null);
            w1 = p.getWidth(null);
            ratio = h1/w1;
            
            // Largura da nova imagem
            // Altura da nova imagem, em função da largura: y = ratio * x
            w2 = x;
            h2 = (w2*ratio);
            
            // Procura a altura certa mantendo o ratio
            // A largura foi fixada como a largura do painel onde a imagem está
            // Loop infinito que quebra quando a nova altura for menor que 
            // a altura do painel onde a imagem foi adicionada
            // O loop tambem altera a nova largura, para manter o ratio
            while(1 == 1) {
                if(h2 < y) { h2 = h2 - 40.0; break; }
                else { w2 = w2 - 1.0; h2 = w2*ratio; }
            }
            
            // Inicializa "img" como vazia
            img = new BufferedImage((int) w2, (int) h2, 1);
            
            // Pega o Graphic2D da "img"
            // Configura a renderização
            // Desenha a nova imagem e limpa a tela
            Graphics2D g = img.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.drawImage(p, 0, 0, (int) w2, (int) h2, null);
            g.dispose();
        } catch (IOException e) {e.printStackTrace();}
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /**
     * Pinta o componente. Define a cor do gráfico como branco e sem seguida
     * preenche um retângulo com as dimensões do painel, fazendo todo ele
     * ficar de cor branca. Então desenha a imagem centralizada horizontalmente.
     * 
     * @param g 
     */
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        g.drawImage(img, (int) ((wPnl - w2)/2.0), 0, null);
    }
        
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(img.getWidth(null), img.getHeight(null));
    }
    
  /**
  * This methog resizes an image by resampling its pixels
  * @param src The image to be resized
  * @return The resized image
  */

  private BufferedImage resizeImage(BufferedImage src) {
      
      int screenWidth = 400;
      int screenHeight = 600;
      
      int srcWidth = src.getWidth(null);
      int srcHeight = src.getHeight(null);
//      Image tmp = Image.createImage(srcWidth, srcHeight);
      BufferedImage tmp = new BufferedImage(
                  screenWidth, srcHeight, BufferedImage.TYPE_INT_RGB);
      Graphics g = tmp.getGraphics();
      int ratio = (srcWidth << 16) / screenWidth;
      int pos = ratio/2;

      //Horizontal Resize        

      for (int x = 0; x < screenWidth; x++) {
          g.setClip(x, 0, 1, srcHeight);
          g.drawImage(src, x - (pos >> 16), 0, null);
          pos += ratio;
      }

      BufferedImage resizedImage = new BufferedImage(
                  screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
      g = resizedImage.getGraphics();
      ratio = (srcHeight << 16) / screenHeight;
      pos = ratio/2;        

      //Vertical resize

      for (int y = 0; y < screenHeight; y++) {
          g.setClip(0, y, screenWidth, 1);
          g.drawImage(tmp, 0, y - (pos >> 16), null);
          pos += ratio;
      }
      return resizedImage;

  }//resize image 
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JFrame frm1 = new JFrame();
        frm1.add(new Plot());
        frm1.setVisible(true);
        frm1.pack();
        frm1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}