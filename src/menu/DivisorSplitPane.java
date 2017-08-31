/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

/**
 * DivisorSplitPane.java
 *
 * @version 1.0 26/08/2017
 * @author Rubens Júnior
 */
public class DivisorSplitPane {

    private final BasicSplitPaneDivider divisor;
    /**
     * Construtores.
     * @param p
     * @param d
     */
    public DivisorSplitPane(boolean p, JSplitPane d) {
        
        /** 
         * Pega o divisor do JSplitPane como objeto
         * da classe BasicSplitPaneDivider, usando getComponent(index)
         */
        divisor = (BasicSplitPaneDivider) d.getComponent(2);
        
        // Define a largura, em pixel, do divisor
        int largura = 5;
        divisor.setDividerSize(largura);
        
        // Define a nova cor da borda externa do divisor
        Color cor = new Color(172, 172, 172);
        
        /**
         * Define a nova borda do divisor do JSplitPane, 
         * com a cor e a largura especificadas
         */
        if(p == true) {        
        // SplitPane horizontal = divsor vertical
        divisor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 1, cor),
                BorderFactory.createEmptyBorder(0, largura, 0, 0)));
        }
        if(p == false) {
        // SplitPane vertical = divsor horizontal
        divisor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, cor),
                BorderFactory.createEmptyBorder(largura, 0, largura, 0)));
        }
        
        divisor.addMouseListener(new DivisorSplitPaneListener(d));
    }

    // -------------------------------------------------------------------------
    // Classes privadas.
    // -------------------------------------------------------------------------
    
    /**
     * Atende aos eventos com o mouse no divisor dos JSplitPane.
     */
    private static class DivisorSplitPaneListener implements MouseListener {

        private final JSplitPane s;
        private final int l;
        
        // Construtor
        public DivisorSplitPaneListener(JSplitPane d) {
            s = d;
            
            // Pega a localização incial do divisor
            l = s.getDividerLocation();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // Evento ao duplo clique do mouse
            if(e.getClickCount() >= 2) {
                // Atualiza a localização do divisor com o valor antigo,
                // pego anteriormente
                s.setDividerLocation(l);
            }
        }
        
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }
}