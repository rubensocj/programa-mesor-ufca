/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package menu.principal;

import menu.principal.sistema.ArvoreMenu;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import menu.DivisorSplitPane;

import menu.painel.aba.PainelConteudo;
import menu.painel.aba.PainelEmAbas;

import r.Plot;
import r.TabelaParametrosEICs;

/**
 * PainelPrincipal.java
 *
 * @version 1.0 20/10/2017
 * @author Rubens Júnior
 */
public class PainelPrincipal extends JPanel {

    public static JPanel pnlOeste, pnlNordeste, pnlSudeste;
    public static PainelEmAbas pnlAbaNordeste, pnlAbaSudeste;
    
    private static DivisorSplitPane divSptHorizontal, divSptVertical;    
    private static JScrollPane paneOeste, paneNordeste, paneSudeste;
    private static JSplitPane sptHorizontal, sptVertical;
    
    private static ArvoreMenu treeSistema;
    public JButton btnTeste = new JButton("Adicionar aba");
    
    /**
     * Construtores.
     */
    public PainelPrincipal() {        
        // Inicializa os paineis com abas
        pnlAbaNordeste = new PainelEmAbas();
        pnlAbaSudeste = new PainelEmAbas();
        
        // Inicializa a JTree
        treeSistema = new ArvoreMenu();
        // ----
        
        // Inicializa os paineis principais        
        pnlNordeste = new JPanel(new BorderLayout());
        pnlNordeste.add(pnlAbaNordeste);
        
        pnlOeste = new JPanel(new BorderLayout());
        pnlOeste.add(treeSistema.getTree(), BorderLayout.CENTER);

        pnlSudeste = new JPanel(new BorderLayout());
        pnlSudeste.add(pnlAbaSudeste);

        // ----
        // Inicializa e configura os JScrollPanes
//        paneNordeste = new JScrollPane(pnlAbaNordeste,
//                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
//                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
//        paneSudeste = new JScrollPane(pnlAbaSudeste,
//                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
//                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
//        paneOeste = new JScrollPane(btnTeste,
//                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
//                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        // ----
        // Inicializa e configura JSplitPanes
        // JSplitPane vertical: pnlNordeste, pnlSudeste
        sptVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
                                                pnlNordeste, pnlSudeste);
        sptVertical.setOpaque(true);
        sptVertical.setOneTouchExpandable(true);
        sptVertical.setDividerLocation(450);
        divSptVertical = new DivisorSplitPane(false, sptVertical);
        
        // JSplitPane horizontal: pnlOeste, sptVertical
        sptHorizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
                                                pnlOeste, sptVertical);
        sptHorizontal.setOpaque(true);
        sptHorizontal.setOneTouchExpandable(true);
        sptHorizontal.setDividerLocation(250);
        divSptHorizontal = new DivisorSplitPane(true, sptHorizontal);
        // ----
        
        // Define o layout do conteiner como BorderLayout
        // Adiciona o SplitPane centralizado, desse modo ele ocupa todo o espaço
        setLayout(new BorderLayout());
        add(sptHorizontal, BorderLayout.CENTER);
        setOpaque(true);
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------

    private static class ActionAdicionarAba implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
//            pnlAbaNordeste.adicionarAba(
//                        String.valueOf(e.getWhen()),
//                        new PainelConteudo(new Plot()),
//                        String.valueOf(e.getWhen()));
//            pnlAbaSudeste.adicionarAba(
//                        String.valueOf(e.getWhen()),
//                        new PainelConteudo(TabelaParametrosEICs.getTabelaICsEParameters()),
//                        String.valueOf(e.getWhen()));
        }
    }
    
}