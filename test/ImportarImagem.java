//
//import java.awt.Component;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.net.MalformedURLException;
//import java.net.URI;
//import java.nio.file.Paths;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.imageio.ImageIO;
//import javax.swing.ImageIcon;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
///**
// *
// * @author Rubens Oliveira da Cunha Júnior
// */
//public class ImportarImagem extends Component {
//    
//    public ImportarImagem() {
////        JLabel lblImagem = new JLabel();
////        String imgSource = "C:\\Users\\Rubens%20Jr\\Documents\\NetBeansProjects\\Programa\\wgrp_plot.png";
////        String imgSource = "C:/Users/Rubens%20Jr/Documents/NetBeansProjects/Programa/wgrp_plot.png";
//        // Converte o path da imagem em URI
////        URI uriImgSource = URI.create(imgSource);
//        
//        try {
//            // Cria um JLabel com a imagem no path definido
////            lblImagem.setIcon(new ImageIcon(imgSource));
//
//            BufferedImage bi = ImageIO.read(new File("wgrp_plot.png"));
//            
//            // Exibe uma janela com a imagem criada
////            JFrame frm1 = new JFrame();
////            frm1.add(lblImagem);
////            frm1.add(bi);
////            frm1.setVisible(true);
////            frm1.pack();
////            frm1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        } catch (Exception ex) {ex.printStackTrace();}
//        
//        
//    }
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        // TODO code application logic here
//        JFrame f = new JFrame("Teste");
////        ImportarImagem im = new ImportarImagem();
//        f.add(new ImportarImagem());
//        f.pack();
//        f.setVisible(true);
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
//
//}

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
 
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import mesor.menu.DialogoAviso;
 
/**
 * This class demonstrates how to load an Image from an external file
 */
public class ImportarImagem extends JPanel {
           
    BufferedImage img;
 
    @Override
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
 
    public ImportarImagem() {
       try {
           img = ImageIO.read(new File("wgrp_plot.png"));
       } catch (IOException e) {
           DialogoAviso.show(e.getMessage());
       }
 
    }
 
//    public Dimension getPreferredSize() {
//        if (img == null) {
//             return new Dimension(100,100);
//        } else {
//           return new Dimension(img.getWidth(null), img.getHeight(null));
//       }
//    }
 
    public static void main(String[] args) {
 
        JFrame f = new JFrame("Load Image Sample");
             
//        f.addWindowListener(new WindowAdapter(){
//                public void windowClosing(WindowEvent e) {
//                    System.exit(0);
//                }
//            });
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new ImportarImagem());
        f.pack();
        f.setVisible(true);
    }
}