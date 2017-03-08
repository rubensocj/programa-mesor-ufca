
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
import javax.swing.*;

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
public class BotaoTabbedPane extends JPanel {

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
        
        this.btnFechar.setIcon(
                    new ImageIcon(menu.JanelaAdicionarAlterar.LOCAL + 
                                "\\icone\\fechar_2.png"));
        this.btnFechar.setOpaque(false);
        this.btnFechar.setBackground(new Color(178,178,178));
        this.btnFechar.setBorder(BorderFactory.createEmptyBorder());
        this.btnFechar.setPreferredSize(new Dimension(14,14));
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