/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package menu.principal;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

/**
 * MenuJTree.java
 *
 * @version 1.0 18/08/2017
 * @author Rubens Júnior
 */
public class MenuJTree extends JPanel implements TreeSelectionListener {
    
    private JTree tree;
    private DefaultMutableTreeNode top;

    /**
     * Construtores.
     */
    public MenuJTree() {
        
        top = new DefaultMutableTreeNode("Sistema");
        
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------

    @Override
    public void valueChanged(TreeSelectionEvent e) {
    }
    
}