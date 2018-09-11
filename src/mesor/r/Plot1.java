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
import java.awt.Toolkit;
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
import mesor.menu.DialogoAviso;
import mesor.menu.Janela;
import mesor.menu.principal.PainelPrincipal;

/**
 * Plot.java
 *
 * @version 1.0 22/10/2017
 * @author Rubens Júnior
 */
public class Plot1 extends JPanel {
    
    private static JLabel imageLabel;
    private static Graphics2D g2d;
    private static final BufferedImage BFFRD_IMAGEM = new BufferedImage(
                                        1024, 768, BufferedImage.TYPE_INT_RGB);
    private static BufferedImage img = null;
    
    private double h1, w1; // Dimensões da imagem original plotada no R
    private double w2, h2; // Dimensões da imagem exibida na tela
    private double ratio;  // Aspect ratio da imagem original
    private int wPnl;      // Dimensões do painel em abas que abrigará o plot
    
    private SerieTemporal serie; // Serie temporal
    
    // -------------------------------------------------------------------------
    // Construtores.
    // -------------------------------------------------------------------------
    
    /**
     * Construtor padrão.
     */
    public Plot1() {
        try {
            img = ImageIO.read(Toolkit.getDefaultToolkit().getClass().getResource("/res/icone/bemvindo700.png"));
        } catch (IOException e) {
            DialogoAviso.show(e.getMessage());
            e.printStackTrace();}
    }
    
    /**
     * Exibe o plot do R na tela. Redimensiona a imagem exibida de acordo com
     * as dimensões da aba onde será mostrada, mantendo o aspect ratio original.
     * 
     * @param x
     * @param y 
     */
    public Plot1(int x, int y) {
        // Pega a largura da aba que mostrará o plot
        wPnl = x;
        
        try {
            // Cria a imagem a partir do arquivo
            // Pega o aspect ratio da imagem importada
            BufferedImage p = ImageIO.read(Toolkit.getDefaultToolkit().getClass().getResource("/res/icone/bemvindo700.png"));
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
            g.drawImage(img, 0, 0, (int) w2, (int) h2, null);
            g.dispose();
        } catch (IOException e) {
            DialogoAviso.show(e.getMessage());
            e.printStackTrace();
        }
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
        g.setColor(new Color(255,245,0));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        g.drawImage(img, 0, 0, null);
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
}