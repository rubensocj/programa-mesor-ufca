/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package menu.painel.aba;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

/**
 * PainelEmAbas.java
 *
 * @version 1.0 23/10/2017
 * @author Rubens Júnior
 */
public class PainelEmAbas extends JTabbedPane {

    /**
     * Construtor - Cria um JTabbedPane
     */
    public PainelEmAbas() {
        setTabPlacement(JTabbedPane.TOP);
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        setOpaque(true);
        setPreferredSize(new Dimension(400, 200));
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    @Override
    public void addTab(String titulo, Icon i, Component c, String n) {
        super.addTab(titulo, i, c, n);
        int ind = this.getTabCount() - 1;
        setTabComponentAt(ind, new BotaoAba(titulo, c));        
    }
//    /**
//     * Adiciona um aba ao painel com abas
//     * 
//     * @param titulo - Texto exibido na aba e no TipText
//     * @param componente - Componente a ser abrigado no painel, ex.: tabela
//     * ou gráfico
//     * @param nome - Nome dos componentes. Vem de Event.getWhen
//     */
//    public void adicionarAba(String titulo, Component componente, String nome) {
//        // Insere uma aba com o título e tipText passados no parâmetro
//        // e com o Component especificado.
//        insertTab(titulo, null, componente, titulo, getTabCount());
//        
//        // Verifica a quantidade de abas no JTabbedPane
//        // Define a nova aba como selecionada
//        if(getTabCount() != 0) {
//            index = getTabCount() - 1;
//            setSelectedIndex(index);
//        }
//        
//        // Cria o painel do botão para esta aba com título e nome especificados
//        BotaoAba btn = new BotaoAba(titulo, nome, componente);
//        
//        // Adiciona o botão à aba
//        setTabComponentAt(index, btn);
//        
////        abaArray1[index] = btn;
//        abaArray.add(btn);
//        System.out.println(abaArray.size());
//        
//        // Identifica em qual aba o botão foi adicionado
////        Component c = getTabComponentAt(index);
////        c.setName(titulo);
////        btn.setTabComponent(c, index);
//    }
    
//    public void removerAba(int i) {
//        abaArray.remove(i);
//        removeTabAt(i);
//    }
    
//    public class ComponentEventDemo implements ComponentListener {
//
//        @Override
//        public void componentResized(ComponentEvent e) {
//        }
//
//        @Override
//        public void componentMoved(ComponentEvent e) {
//        }
//
//        @Override
//        public void componentShown(ComponentEvent e) {
//        }
//
//        @Override
//        public void componentHidden(ComponentEvent e) {
//        }
//    }
//    
//    public class ActionComponentEventDemo implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//        }
//        
//    }
}

