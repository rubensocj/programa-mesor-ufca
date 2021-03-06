/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mesor.menu.principal;

import mesor.menu.principal.sistema.ArvoreMenu;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import mesor.menu.DialogoAviso;

import mesor.menu.DivisorSplitPane;
import mesor.menu.Janela;

import mesor.menu.painel.aba.PainelConteudo;
import mesor.menu.painel.aba.PainelEmAbas;
import mesor.menu.principal.sistema.ComboBoxesSQL;

import mesor.r.Plot;
import mesor.menu.ImagemImportada;
import mesor.r.TabelaParametrosEICs;

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
    
    public static ArvoreMenu treeSistema;
    public static ComboBoxesSQL cbxSQL;
    public JButton btnTeste = new JButton("Adicionar aba");
    
    /**
     * Construtores.
     */
    public PainelPrincipal() {        
        // Inicializa o painel Nordeste com abas e importa a imagem de boas vindas
        pnlAbaNordeste = new PainelEmAbas();
        pnlAbaNordeste.setBorder(BorderFactory.createEmptyBorder());
        pnlAbaNordeste.addTab("Bem-vindo", null, (Component) new PainelConteudo(new ImagemImportada()), "");

        // Inicializa o painel sudeste com abas
        pnlAbaSudeste = new PainelEmAbas();
        pnlAbaSudeste.setBorder(BorderFactory.createEmptyBorder());
        
        // Inicializa a JTree
        cbxSQL = new ComboBoxesSQL();
        // ----
        
        // Inicializa os paineis principais        
        pnlNordeste = new JPanel(new BorderLayout());
        pnlNordeste.add(pnlAbaNordeste);
        
        pnlOeste = new JPanel(new FlowLayout());
        pnlOeste.setBorder(BorderFactory.createEmptyBorder());
        pnlOeste.add(cbxSQL);

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

    public static void atualizarArvore() {
    }
    
    private static JPanel montarPainelInicial() {
        JPanel pnl = new JPanel(new FlowLayout());
        BufferedImage img = null;
        
        img = (BufferedImage) Janela.criarIcon("res/icone/mesor.jpg").getImage();
        
        return pnl;
    }
}