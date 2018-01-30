package menu.principal.sistema;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import equipamento.Unidade;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import menu.painel.aba.PainelConteudo;
import menu.painel.elementos.PainelDemanda;

import static menu.principal.PainelPrincipal.pnlAbaNordeste;
import static menu.principal.PainelPrincipal.pnlAbaSudeste;

import r.Plot;
import r.TabelaParametrosEICs;

import sql.Lista;
/**
 *
 * @author Rubens Oliveira da Cunha J�nior
 */
public class ArvoreMenu {
    
    static JTree tree;
    static DefaultMutableTreeNode noSistema;
    static DefaultMutableTreeNode[] arrayNOSistema;
    static DefaultMutableTreeNode[][] arrayNodeUnidade;
    
    static JScrollPane scrPaneTree;
    
    static JLabel jlab;
    static sql.Lista lsSistemas;
    static sql.Lista[] lsUni;
    static sql.Lista[] lsUniCod;
    static sql.Lista lsSub;
    static sql.Lista[] listaSubunidadesCodigos;
    
    static int s, u, sb;
    
    static int NIVEL_SELECAO_NO = 0;
    static String CODIGO_SELECAO_NO = "";
    static String NOME_SELECAO_NO = "";
    
    public ArvoreMenu() {
        try {
            // Faz consulta SQL e cria uma lista com os dados de uma coluna
            lsSistemas = new Lista("SELECT nome FROM sistema");
            
            // Pega o comprimento do vetor com os dados da consulta
            s = lsSistemas.toVector().size();
                        
            // Inicializa o array de listas. Cada entrada do array
            // correponde a uma lista composta pelas unidades de cada sistema
            // Ser�o s listas
            // Lista tamb�m os ids(c�digos) das unidades
            lsUni = new Lista[s];
            lsUniCod = new Lista[s];
                    
            // A classe Query realiza uma determinada consulta SQL
            // Neste caso, retorna a contagem de linhas da tabela UNIDADE
            u = sql.Query.consultaSQLInt("SELECT COUNT(*) FROM unidade;");
            System.out.println(String.valueOf(u));
                    
            // Neste caso, retorna a contagem de linhas da tabela SUBUNIDADE
            sb = sql.Query.consultaSQLInt("SELECT COUNT(*) FROM subunidade;");
            System.out.println(String.valueOf(sb));
            
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
            noSistema.add(arrayNOSistema[i]);
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
        arrayNOSistema = new DefaultMutableTreeNode[s];
        
        // Inicializa o array bidimensional de n�s das unidades
        // S�o s sistemas, e cada sistema ter� no m�ximo u unidades
        arrayNodeUnidade = new DefaultMutableTreeNode[s][u];
        
        int i = 0, j = 0;
        while(i < lsSistemas.toVector().size()) {
            // Preenche o array dos n�s (sistemas)
            arrayNOSistema[i] = new DefaultMutableTreeNode(
                        lsSistemas.toVector().get(i));

            // String com o nome do sistema corrente no loop
            String n = arrayNOSistema[i].toString();

            // Lista resultado da consulta SQL: 
            // Unidades do sistema corrente
            // id (c�digos) das unidades
            lsUni[i] = new Lista(
                "SELECT classe FROM unidade, sistema " +
                " WHERE sistema.nome = '" + n +
                "' AND unidade.id_sistema = sistema.id");
            lsUniCod[i] = new Lista(
                "SELECT unidade.id FROM unidade, sistema " +
                " WHERE sistema.nome = '" + n +
                "' AND unidade.id_sistema = sistema.id");
            
            while(j < lsUni[i].toVector().size()) {
                // Fixa-se o sistema e o loop em j cria n�s para as unidades
                // e as adiciona ao n� do sistema corrente
                // N� na forma "id(codigo) - classe"
            
                String uniDescricao = lsUni[i].toVector().get(j);
                String uniIdStr = lsUniCod[i].toVector().get(j);
                DefaultMutableTreeNode no = new DefaultMutableTreeNode(
                                (Object) uniIdStr + " - " + uniDescricao, true);

                int numSubunidade = sql.Query.consultaSQLInt(
                    "SELECT COUNT(*) FROM subunidade WHERE id_unidade = " + uniIdStr);
                
                lsSub = new Lista(
                    "SELECT subunidade.descricao FROM subunidade, unidade " +
                    " WHERE unidade.id = '" + uniIdStr +
                    "' AND subunidade.id_unidade = unidade.id");
                
                int k = 0;
                while(k < lsSub.toVector().size()) {
                    System.out.println(lsSub.toVector().get(k));
//                    lsSub[j] = new Lista(
//                        "SELECT subunidade.descricao FROM subunidade, unidade "+
//                        " WHERE unidade.id = '" + uniIdStr +
//                        "' AND subunidade.id_unidade = unidade.id");
                    no.add(new DefaultMutableTreeNode((Object)
                            lsSub.toVector().get(k)));
                    k++;
                }
                arrayNOSistema[i].add(no);
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
        tree.addMouseListener(new cliqueDireito());
        
        // Define o tipo de sele��o da tree: sele��o �nica
        tree.getSelectionModel().setSelectionMode(
                    TreeSelectionModel.SINGLE_TREE_SELECTION);
        
        // Adiciona o listener de sele��o da tree
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode no = (DefaultMutableTreeNode)
                            tree.getLastSelectedPathComponent();
                
                // Se nada for selecionado
                if(no == null) return;
                
                // Se a sele��o for no n�vel 2 da tree
                if(no.getLevel() == 2) {
                    // Pega o n� que foi selecionado
                    // Divide a descri��o do n� em dois String usando split
                    // Pega o id(c�digo) e classe da unidade
                    String descricaoNo = no.getUserObject().toString();
                    String[] splitDescricao = descricaoNo.split(" - ");
                    CODIGO_SELECAO_NO = splitDescricao[0];
                    NOME_SELECAO_NO = splitDescricao[1];
                    System.out.println("id:" + CODIGO_SELECAO_NO);
                    System.out.println("info:" + NOME_SELECAO_NO);
                }

                // Pega o n�vel
                NIVEL_SELECAO_NO = no.getLevel();                
                System.out.println("nivel:" + String.valueOf(NIVEL_SELECAO_NO));
            }
        });
        
        // Adiciona a tree a um JScrollPane
        scrPaneTree = new JScrollPane(tree);
        scrPaneTree.setVerticalScrollBarPolicy(
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrPaneTree.setHorizontalScrollBarPolicy(
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        return scrPaneTree;
    }
    
    /**
     * 
     */
    public static class PopupUnidades extends JPopupMenu {
    
        static JMenuItem jmiPrevisao, jmiDemandas, jmiIntervencao,
                        jmiSubunidade, jmiEquipes;
        
        public PopupUnidades() {
            // Cria os itens do menu popup
            jmiPrevisao = new JMenuItem("Previs�o");
            jmiDemandas = new JMenuItem("Demandas");
            jmiIntervencao = new JMenuItem("Interven��es");
            jmiSubunidade = new JMenuItem("Subunidades");
            jmiEquipes = new JMenuItem("Equipes");

            // Adiciona os ActionListeners aos itens do menu popup
            jmiPrevisao.addActionListener(new ActionPrevisao());
            jmiDemandas.addActionListener(new ActionTabelaDemanda());
    //        jmiIntervencao.addActionListener(new ActionTabelaIntervencao());
    //        jmiSubunidade.addActionListener(new ActionTabelaSubunidade());
    //        jmiEquipes.addActionListener(new ActionTabelaEquipe());

            // Adiciona os itens ao menu popup            
            add(jmiPrevisao);
            addSeparator();
            add(jmiDemandas);
            add(jmiIntervencao);
            add(jmiSubunidade);
            add(jmiEquipes);
        }
    }

    /**
     * 
     */
    private static class cliqueDireito extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            // Verifica se o clique veio do bot�o direito do mouse
            if(SwingUtilities.isRightMouseButton(e)) {
                // Pega o item da tree pela localiza��o
                // Define este item como o selecionado
                // Desse modo, pode-se selecionar usando bot�o direito
                TreePath pathSelecao = tree.getClosestPathForLocation(
                                                        e.getX(), e.getY());
                tree.setSelectionPath(pathSelecao);
            }
            if(e.isPopupTrigger()) {
                mostrarPopupMenu(e);
            }}
        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.isPopupTrigger()) {
                mostrarPopupMenu(e);
            }
        }
        
        // Mostra o menu popup
        private void mostrarPopupMenu(MouseEvent e) {
            // Testa qual o n�vel do n� selecionado
            // N�vel 0: "Sistemas"
            // N�vel 1: n�s com os nomes dos sistemas
            // N�vel 2: n�s com os nomes das unidades
            if(NIVEL_SELECAO_NO == 2) {
                // Cria um menu popup da classe PopUnidades
                // Exibe o menu no componente que deu origem � a��o
                // e nas coordenadas do evento
                PopupUnidades popupUni = new PopupUnidades();
                popupUni.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
    
    // -------------------------------------------------------------------------
    // Classes p�blicas.
    // -------------------------------------------------------------------------
        
    /**
     * 
     */
    public static class ActionPrevisao implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            pnlAbaNordeste.adicionarAba(
                        String.valueOf(e.getWhen()),
                        new PainelConteudo(new Plot()),
                        String.valueOf(e.getWhen()));
            pnlAbaSudeste.adicionarAba(
                        String.valueOf(e.getWhen()),
                        new PainelConteudo(
                                TabelaParametrosEICs.getTabelaICsEParameters()),
                        String.valueOf(e.getWhen()));
        }
    }
    
    public static class ActionTabelaDemanda implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Cria um painel demanda da unidade selecionada na tree
            PainelDemanda pnlDemanda = new PainelDemanda(
                        new Unidade(NOME_SELECAO_NO, 
                                    Integer.parseInt(CODIGO_SELECAO_NO)));

            pnlAbaNordeste.adicionarAba(
                        String.valueOf(e.getWhen()),
                        new PainelConteudo(pnlDemanda.getTabela()),
                        String.valueOf(e.getWhen()));
        }
    }
    
    
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        try {
//            // Faz consulta SQL e cria uma lista com os dados de uma coluna
//            lsSistemas = new Lista("SELECT nome FROM sistema");
//            
//            // Pega o comprimento do vetor com os dados da consulta
//            s = lsSistemas.toVector().size();
//                        
//            // Inicializa o array de listas. Cada entrada do array
//            // correponde a uma lista composta pelas unidades de cada sistema
//            // Ser�o s listas
//            lsUni = new Lista[s];
//                    
//            // A classe Query realiza uma determinada consulta SQL
//            // Neste caso, retorna a contagem de linhas da tabela UNIDADE
//            u = sql.Query.consultaSQLInt("SELECT COUNT(*) FROM unidade;");
//            System.out.println(String.valueOf(u));
//            
//            // Inicializa o array de n�s da �rvore sistema
//            // Cada n� criado corresponde a um elemento da lista SQL
//            arrayNOSistema = new DefaultMutableTreeNode[s];
//            
//            // Inicializa o array bidimensional de n�s das unidades
//            // S�o s sistemas, e cada sistema ter� no m�ximo u unidades
//            arrayNodeUnidade = new DefaultMutableTreeNode[s][u];
//            
//            int i = 0, j = 0;
//            while(i < lsSistemas.toVector().size()) {
//                // Preenche o array dos n�s (sistemas)
//                arrayNOSistema[i] = new DefaultMutableTreeNode(
//                            lsSistemas.toVector().get(i));
//                
//                // String com o nome do sistema corrente no loop
//                String n = arrayNOSistema[i].toString();
//                
//                // Lista resultado da consulta SQL: Unidades do sistema corrente
//                lsUni[i] = new Lista(
//                    "SELECT classe FROM unidade, sistema " +
//                    " WHERE sistema.nome = '" + n +
//                    "' AND unidade.id_sistema = sistema.id");
//                while(j < lsUni[i].toVector().size()) {
//                    // Fixa-se o sistema e o loop em j cria n�s para as unidades
//                    // e as adiciona ao n� do sistema corrente
//                    arrayNOSistema[i].add(new DefaultMutableTreeNode(
//                                        lsUni[i].toVector().get(j)));
////                    arrayNodeUnidade[i][j] = new DefaultMutableTreeNode(
////                                        lsUni[i].toVector().get(j));
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
//            top.add(arrayNOSistema[i]);
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


