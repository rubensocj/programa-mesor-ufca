/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.*;

import java.sql.SQLException;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import sql.Lista;
/**
 *
 * @author Rubens Oliveira da Cunha J�nior
 */
public class ArvoreMenu {
    
    static JTree tree;
    static DefaultMutableTreeNode noSistema;
    static DefaultMutableTreeNode[] arrayNodeSistema;
    static DefaultMutableTreeNode[][] arrayNodeUnidade;
    
    static JScrollPane scrPaneTree;
    
    static JLabel jlab;
    static sql.Lista listaSistemas;
    static sql.Lista[] listaUnidades;
    
    static int s, t;
    
    public ArvoreMenu() {
        try {
            // Faz consulta SQL e cria uma lista com os dados de uma coluna
            listaSistemas = new Lista("SELECT nome FROM sistema");
            
            // Pega o comprimento do vetor com os dados da consulta
            s = listaSistemas.toVector().size();
                        
            // Inicializa o array de listas. Cada entrada do array
            // correponde a uma lista composta pelas unidades de cada sistema
            // Ser�o s listas
            listaUnidades = new Lista[s];
                    
            // A classe Query realiza uma determinada consulta SQL
            // Neste caso, retorna a contagem de linhas da tabela UNIDADE
            t = sql.Query.consultaSQLInt("SELECT COUNT(*) FROM unidade;");
            System.out.println(String.valueOf(t));
            
            // Cria os arrays de n�s fazendo as consultas ao banco
            criarNoDeConsultaSQL();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        // Cria n� sistema
        noSistema = new DefaultMutableTreeNode("Sistemas");
        
        // Adiciona os n�s dos sistemas � �rvore
        int i = 0;
        while(i < s) {
            noSistema.add(arrayNodeSistema[i]);
            i++;
        }
    }
    
    /**
     * Cria os arrays de n�s e os preenche com os resultados da consulta SQL
     * @throws SQLException 
     */
    private void criarNoDeConsultaSQL() throws SQLException {
        // Inicializa o array de n�s da �rvore sistema
        // Cada n� criado corresponde a um elemento da lista SQL
        arrayNodeSistema = new DefaultMutableTreeNode[s];

        // Inicializa o array bidimensional de n�s das unidades
        // S�o s sistemas, e cada sistema ter� no m�ximo t unidades
        arrayNodeUnidade = new DefaultMutableTreeNode[s][t];

        int i = 0, j = 0;
        while(i < listaSistemas.toVector().size()) {
            // Preenche o array dos n�s (sistemas)
            arrayNodeSistema[i] = new DefaultMutableTreeNode(
                        listaSistemas.toVector().get(i));

            // String com o nome do sistema corrente no loop
            String n = arrayNodeSistema[i].toString();

            // Lista resultado da consulta SQL: Unidades do sistema corrente
            listaUnidades[i] = new Lista(
                "SELECT classe FROM unidade, sistema " +
                " WHERE sistema.nome = '" + n +
                "' AND unidade.id_sistema = sistema.id");
            while(j < listaUnidades[i].toVector().size()) {
                // Fixa-se o sistema e o loop em j cria n�s para as unidades
                // e as adiciona ao n� do sistema corrente
                arrayNodeSistema[i].add(new DefaultMutableTreeNode(
                                    listaUnidades[i].toVector().get(j)));
//                    arrayNodeUnidade[i][j] = new DefaultMutableTreeNode(
//                                        listaUnidades[i].toVector().get(j));
                j++;
            }
            // Reinicia a vari�vel j
            j = 0;
            i++;
        }
    }

    /**
     * 
     * @return 
     */
    public JScrollPane getTree() {
        // Cria a tree
        tree = new JTree(noSistema);
        
        // Adiciona a tree a um JScrollPane
        scrPaneTree = new JScrollPane(tree);
        
        return scrPaneTree;
    }
    
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        try {
//            // Faz consulta SQL e cria uma lista com os dados de uma coluna
//            listaSistemas = new Lista("SELECT nome FROM sistema");
//            
//            // Pega o comprimento do vetor com os dados da consulta
//            s = listaSistemas.toVector().size();
//                        
//            // Inicializa o array de listas. Cada entrada do array
//            // correponde a uma lista composta pelas unidades de cada sistema
//            // Ser�o s listas
//            listaUnidades = new Lista[s];
//                    
//            // A classe Query realiza uma determinada consulta SQL
//            // Neste caso, retorna a contagem de linhas da tabela UNIDADE
//            t = sql.Query.consultaSQLInt("SELECT COUNT(*) FROM unidade;");
//            System.out.println(String.valueOf(t));
//            
//            // Inicializa o array de n�s da �rvore sistema
//            // Cada n� criado corresponde a um elemento da lista SQL
//            arrayNodeSistema = new DefaultMutableTreeNode[s];
//            
//            // Inicializa o array bidimensional de n�s das unidades
//            // S�o s sistemas, e cada sistema ter� no m�ximo t unidades
//            arrayNodeUnidade = new DefaultMutableTreeNode[s][t];
//            
//            int i = 0, j = 0;
//            while(i < listaSistemas.toVector().size()) {
//                // Preenche o array dos n�s (sistemas)
//                arrayNodeSistema[i] = new DefaultMutableTreeNode(
//                            listaSistemas.toVector().get(i));
//                
//                // String com o nome do sistema corrente no loop
//                String n = arrayNodeSistema[i].toString();
//                
//                // Lista resultado da consulta SQL: Unidades do sistema corrente
//                listaUnidades[i] = new Lista(
//                    "SELECT classe FROM unidade, sistema " +
//                    " WHERE sistema.nome = '" + n +
//                    "' AND unidade.id_sistema = sistema.id");
//                while(j < listaUnidades[i].toVector().size()) {
//                    // Fixa-se o sistema e o loop em j cria n�s para as unidades
//                    // e as adiciona ao n� do sistema corrente
//                    arrayNodeSistema[i].add(new DefaultMutableTreeNode(
//                                        listaUnidades[i].toVector().get(j)));
////                    arrayNodeUnidade[i][j] = new DefaultMutableTreeNode(
////                                        listaUnidades[i].toVector().get(j));
//                    j++;
//                }
//                // Reinicia a vari�vel j
//                j = 0;
//                i++;
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        
//        // Cria n� sistema
//        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Sistemas");
//        
//        // Adiciona os n�s dos sistemas � �rvore
//        int i = 0, j = 0;
//        while(i < s) {
//            top.add(arrayNodeSistema[i]);
//            i++;
//        }
//        
////        // Cria sub�rvore de "A"
////        DefaultMutableTreeNode a = new DefaultMutableTreeNode("A");
////        top.add(a); // Adiciona ao n� "top"
////        DefaultMutableTreeNode a1 = new DefaultMutableTreeNode("A1");
////        a.add(a1);  // Adiciona ao subn� "a"
////        DefaultMutableTreeNode a2 = new DefaultMutableTreeNode("A2");
////        a.add(a2);  // Adiciona ao subn� "a"
////        
////        // Cria sub�rvore de "B"
////        DefaultMutableTreeNode b = new DefaultMutableTreeNode("B");
////        top.add(b); // Adiciona ao n� "top"
////        DefaultMutableTreeNode b1 = new DefaultMutableTreeNode("B1");
////        b.add(b1);  // Adiciona ao subn� "b"
////        DefaultMutableTreeNode b2 = new DefaultMutableTreeNode("B2");
////        b.add(b2);  // Adiciona ao subn� "b"
////        DefaultMutableTreeNode b3 = new DefaultMutableTreeNode("B3");
////        b.add(b3);  // Adiciona ao subn� "b"
//        
//        // Cria a tree
//        tree = new JTree(top);
//        
//        // Adiciona a tree a um JScrollPane
//        JScrollPane jsp = new JScrollPane(tree);
//        
//        // Cria um JPanel
//        JPanel pnl = new JPanel(new BorderLayout());
//        
//        // Adiciona o JScrollPane ao conteudo do Applet
//        pnl.add(jsp, BorderLayout.NORTH);
//        
//        // Adiciona o JLabel ao conteudo do Applet
//        jlab = new JLabel("Selection is: ");
//        pnl.add(jlab, BorderLayout.CENTER);
//        
//        // Lida com eventos de sele��o da tree
//        tree.addTreeSelectionListener(new TreeSelectionListener() {
//            @Override
//            public void valueChanged(TreeSelectionEvent e) {
//                jlab.setText("Selection is: " + e.getPath());
//            }
//        });
//        
//        JFrame frm = new JFrame("Teste JTree");
//        frm.add(pnl);
//        frm.pack();
//        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frm.setVisible(true);
//    }
}


