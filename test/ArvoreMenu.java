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
 * @author Rubens Oliveira da Cunha Júnior
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
            // Serão s listas
            listaUnidades = new Lista[s];
                    
            // A classe Query realiza uma determinada consulta SQL
            // Neste caso, retorna a contagem de linhas da tabela UNIDADE
            t = sql.Query.consultaSQLInt("SELECT COUNT(*) FROM unidade;");
            System.out.println(String.valueOf(t));
            
            // Cria os arrays de nós fazendo as consultas ao banco
            criarNoDeConsultaSQL();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        // Cria nó sistema
        noSistema = new DefaultMutableTreeNode("Sistemas");
        
        // Adiciona os nós dos sistemas à árvore
        int i = 0;
        while(i < s) {
            noSistema.add(arrayNodeSistema[i]);
            i++;
        }
    }
    
    /**
     * Cria os arrays de nós e os preenche com os resultados da consulta SQL
     * @throws SQLException 
     */
    private void criarNoDeConsultaSQL() throws SQLException {
        // Inicializa o array de nós da árvore sistema
        // Cada nó criado corresponde a um elemento da lista SQL
        arrayNodeSistema = new DefaultMutableTreeNode[s];

        // Inicializa o array bidimensional de nós das unidades
        // São s sistemas, e cada sistema terá no máximo t unidades
        arrayNodeUnidade = new DefaultMutableTreeNode[s][t];

        int i = 0, j = 0;
        while(i < listaSistemas.toVector().size()) {
            // Preenche o array dos nós (sistemas)
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
                // Fixa-se o sistema e o loop em j cria nós para as unidades
                // e as adiciona ao nó do sistema corrente
                arrayNodeSistema[i].add(new DefaultMutableTreeNode(
                                    listaUnidades[i].toVector().get(j)));
//                    arrayNodeUnidade[i][j] = new DefaultMutableTreeNode(
//                                        listaUnidades[i].toVector().get(j));
                j++;
            }
            // Reinicia a variável j
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
//            // Serão s listas
//            listaUnidades = new Lista[s];
//                    
//            // A classe Query realiza uma determinada consulta SQL
//            // Neste caso, retorna a contagem de linhas da tabela UNIDADE
//            t = sql.Query.consultaSQLInt("SELECT COUNT(*) FROM unidade;");
//            System.out.println(String.valueOf(t));
//            
//            // Inicializa o array de nós da árvore sistema
//            // Cada nó criado corresponde a um elemento da lista SQL
//            arrayNodeSistema = new DefaultMutableTreeNode[s];
//            
//            // Inicializa o array bidimensional de nós das unidades
//            // São s sistemas, e cada sistema terá no máximo t unidades
//            arrayNodeUnidade = new DefaultMutableTreeNode[s][t];
//            
//            int i = 0, j = 0;
//            while(i < listaSistemas.toVector().size()) {
//                // Preenche o array dos nós (sistemas)
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
//                    // Fixa-se o sistema e o loop em j cria nós para as unidades
//                    // e as adiciona ao nó do sistema corrente
//                    arrayNodeSistema[i].add(new DefaultMutableTreeNode(
//                                        listaUnidades[i].toVector().get(j)));
////                    arrayNodeUnidade[i][j] = new DefaultMutableTreeNode(
////                                        listaUnidades[i].toVector().get(j));
//                    j++;
//                }
//                // Reinicia a variável j
//                j = 0;
//                i++;
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        
//        // Cria nó sistema
//        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Sistemas");
//        
//        // Adiciona os nós dos sistemas à árvore
//        int i = 0, j = 0;
//        while(i < s) {
//            top.add(arrayNodeSistema[i]);
//            i++;
//        }
//        
////        // Cria subárvore de "A"
////        DefaultMutableTreeNode a = new DefaultMutableTreeNode("A");
////        top.add(a); // Adiciona ao nó "top"
////        DefaultMutableTreeNode a1 = new DefaultMutableTreeNode("A1");
////        a.add(a1);  // Adiciona ao subnó "a"
////        DefaultMutableTreeNode a2 = new DefaultMutableTreeNode("A2");
////        a.add(a2);  // Adiciona ao subnó "a"
////        
////        // Cria subárvore de "B"
////        DefaultMutableTreeNode b = new DefaultMutableTreeNode("B");
////        top.add(b); // Adiciona ao nó "top"
////        DefaultMutableTreeNode b1 = new DefaultMutableTreeNode("B1");
////        b.add(b1);  // Adiciona ao subnó "b"
////        DefaultMutableTreeNode b2 = new DefaultMutableTreeNode("B2");
////        b.add(b2);  // Adiciona ao subnó "b"
////        DefaultMutableTreeNode b3 = new DefaultMutableTreeNode("B3");
////        b.add(b3);  // Adiciona ao subnó "b"
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
//        // Lida com eventos de seleção da tree
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


