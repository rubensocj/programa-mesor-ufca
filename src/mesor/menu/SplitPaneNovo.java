/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mesor.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.JSplitPane;

/**
 * SplitPaneNovo.java
 *
 * @version 1.0 26/08/2017
 * @author Rubens Júnior
 */
public class SplitPaneNovo extends JSplitPane {
    
    private final BasicSplitPaneDivider divisor;
    private final JSplitPane splitPane;

    /**
     * Construtores.
//     * @param o
//     * @param esq
//     * @param dir
//     * @param s
     */
    public SplitPaneNovo() {
//    public SplitPaneNovo(int o, Component esq, Component dir, int s) {
//        super(new FlowLayout());
        
        splitPane = new JSplitPane();
//        splitPane = new JSplitPane(o, esq, dir);
//    public SplitPaneNovo() {
//        super(o, esq, dir);
//        setLeftComponent(esq);
//        setRightComponent(dir);
//        setOrientation(o);
        
        /** 
         * Pega o divisor do JSplitPane como objeto
         * da classe BasicSplitPaneDivider, usando getComponent(index)
         */
        divisor = (BasicSplitPaneDivider) getComponent(2);
        
        // Define a largura, em pixel, do divisor
        setLarguraDivisor(5);
        
        // Define a nova cor da borda externa do divisor
        Color cor = new Color(172, 172, 172);
        
        /**
         * Define a nova borda do divisor do JSplitPane, 
         * com a cor e a largura especificadas
         */
        divisor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 1, cor),
            BorderFactory.createEmptyBorder(
                        0, divisor.getDividerSize(), 0, 0)));
        
        splitPane.setOpaque(true);
        splitPane.setOneTouchExpandable(true);
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------

    private void setLarguraDivisor(int i) {
        divisor.setDividerSize(i);
    }
    
    public JSplitPane splitPane() {
        return splitPane;
    }
}