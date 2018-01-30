/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.rosuda.javaGD.JGDBufferedPanel;

/**
 * Plot.java
 *
 * @version 1.0 22/10/2017
 * @author Rubens Júnior
 */
public class Plot1 extends JPanel {
    
    private static JLabel imageLabel;
    private static Graphics2D g2d;
    private static final JGDBufferedPanel bfdgd = new JGDBufferedPanel(500,500);
    private static final BufferedImage BFFRD_IMAGEM = new BufferedImage(
                                        1024, 768, BufferedImage.TYPE_INT_RGB);
    private static BufferedImage img = null;
    
    /**
     * Construtores.
     */
    public Plot1() {        
        try {
            img = ImageIO.read(new File("wgrp_plot.png"));
        } catch (IOException e) {e.printStackTrace();}
    }
        
//        teste();
//        imageLabel = new JLabel(new ImageIcon(BFFRD_IMAGEM));
//        setLayout(new BorderLayout());        
//        add(imageLabel, BorderLayout.CENTER);
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
//    public static void teste() {
//        g2d = BFFRD_IMAGEM.createGraphics();
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                    RenderingHints.VALUE_ANTIALIAS_ON);
//        
//        try {
//            img = ImageIO.read(
//                        new File(CamadaR.R_PATH.concat("\\wgrp_plot.png")));
//        } catch (IOException e) {e.printStackTrace();}
//        g2d.drawImage(img, 1024, 768, null);
//        imageLabel = new JLabel(new ImageIcon(BFFRD_IMAGEM));
//    }
    
    @Override
    public void paint(Graphics g) {
       g.drawImage(img, 0, 0, null);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(img.getWidth(null), img.getHeight(null));
    }

    public void getHorizontalScrollBarPolicy() {
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        JFrame frm1 = new JFrame();
        frm1.add(new Plot1());
        frm1.setVisible(true);
        frm1.pack();
        frm1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}