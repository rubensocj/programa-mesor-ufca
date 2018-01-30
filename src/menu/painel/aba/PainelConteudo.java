/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package menu.painel.aba;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * PainelConteudo.java
 *
 * @version 1.0 27/10/2017
 * @author Rubens Júnior
 */
public class PainelConteudo extends JPanel {

    private static JScrollPane scrPane;
    
    /**
     * Construtores.
     * @param conteudo - Conteúdo a ser exibido na aba adicionado ao JTabbedPane
     */
    public PainelConteudo(Component conteudo) {
        // Inicializa o JScrollPane com o componente passado no parâmetro 
        // do construtor, com barra de scroll horizontaç e vertical
        // quando necessárias.
        scrPane = new JScrollPane(conteudo,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        setLayout(new BorderLayout());
        add(scrPane, BorderLayout.CENTER);
    }
}