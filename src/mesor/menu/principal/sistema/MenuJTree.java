/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mesor.menu.principal.sistema;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import mesor.menu.Janela;

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

    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */
    /**
     * BotaoTabbedPane.java
     *
     * @version 1.0 25/02/2017
     * @author Rubens Jr
     */
    public static class BotaoTabbedPane extends JPanel {

        private JButton btnFechar;

        /**
         * Construtores.
         * @param titulo
         */
        public BotaoTabbedPane(String titulo) {
            super(new BorderLayout());
            // Inicializa o botão que fecha a aba do JTabbedPane
            configuraBotao();
            add(new JLabel(titulo), BorderLayout.WEST);
            add(painelBotao(), BorderLayout.EAST);
            setOpaque(false);
        }

        // -------------------------------------------------------------------------
        // Métodos.
        // -------------------------------------------------------------------------
        private void configuraBotao() {
            this.btnFechar = new JButton();
            this.btnFechar.setIcon(new ImageIcon(Janela.LOCAL + "\\icone\\fechar_2.png"));
            this.btnFechar.setOpaque(false);
            this.btnFechar.setBackground(new Color(178, 178, 178));
            this.btnFechar.setBorder(BorderFactory.createEmptyBorder());
            this.btnFechar.setPreferredSize(new Dimension(14, 14));
            this.btnFechar.addActionListener(new ActionFechar());
        }

        private JPanel painelBotao() {
            JPanel pnlBotao = new JPanel(new FlowLayout());
            pnlBotao.setOpaque(false);
            pnlBotao.add(this.btnFechar);
            return pnlBotao;
        }

        private static class ActionFechar implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
            }
        }
    }
    
}