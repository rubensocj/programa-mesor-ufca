package mesor.menu.principal.sistema;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import mesor.equipamento.Unidade;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import mesor.menu.HoraFormatada;

import mesor.menu.painel.aba.PainelConteudo;
import mesor.menu.painel.taxonomia.PainelDemanda;
import mesor.menu.painel.taxonomia.PainelIntervencao;
import mesor.menu.principal.MenuPrincipal;
import mesor.menu.principal.PainelPrincipal;
import static mesor.menu.principal.PainelPrincipal.pnlAbaNordeste;
import static mesor.menu.principal.PainelPrincipal.pnlAbaSudeste;

import mesor.r.Plot;
import mesor.r.TabelaParametrosEICs;

import mesor.sql.Lista;
/**
 *
 * @author Rubens Oliveira da Cunha Júnior
 */
public class ArvoreMenu {
    
    static JTree tree;
    static DefaultMutableTreeNode noSistema;
    static DefaultMutableTreeNode[] arrayNOSistema;
    static DefaultMutableTreeNode[][] arrayNodeUnidade;
    
    static JScrollPane scrPaneTree;
    
    static JLabel jlab;
    static mesor.sql.Lista lsSistemas;
    static mesor.sql.Lista[] lsUni;
    static mesor.sql.Lista[] lsUniCod;
    
    static mesor.sql.Lista lsSub;
    static mesor.sql.Lista lsSubCod;
    static mesor.sql.Lista lsComp;
    static mesor.sql.Lista lsCompCod;
    static mesor.sql.Lista lsPte;
    static mesor.sql.Lista lsPteCod;
    
    static int s, u, sb;
    
    static int NIVEL_SELECAO_NO = 0;
    static String CODIGO_SELECAO_NO = "";
    static String NOME_SELECAO_NO = "";
    
    public ArvoreMenu() {
        try {
            // Faz consulta SQL e cria uma lista com os dados de uma coluna
            lsSistemas = new Lista("SELECT nome FROM sistema");
            
            // Pega o comprimento do vetor com os dados da consulta
            s = lsSistemas.size();
                        
            // Inicializa o array de listas. Cada entrada do array
            // correponde a uma lista composta pelas unidades de cada sistema
            // Serão s listas
            // Lista também os ids(códigos) das unidades
            lsUni = new Lista[s];
            lsUniCod = new Lista[s];
                    
            // A classe Query realiza uma determinada consulta SQL
            // Neste caso, retorna a contagem de linhas da tabela UNIDADE
            u = mesor.sql.Query.consultaSQLInt("SELECT COUNT(*) FROM unidade;");
                    
            // Neste caso, retorna a contagem de linhas da tabela SUBUNIDADE
            sb = mesor.sql.Query.consultaSQLInt("SELECT COUNT(*) FROM subunidade;");
            
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
            noSistema.add(arrayNOSistema[i]);
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
        arrayNOSistema = new DefaultMutableTreeNode[s];
        
        int i = 0, j = 0;
        while(i < lsSistemas.size()) {
            // Preenche o array dos nós (sistemas)
            arrayNOSistema[i] = new DefaultMutableTreeNode(lsSistemas.get(i));

            // String com o nome do sistema corrente no loop
            String n = arrayNOSistema[i].toString();

            // Lista resultado da consulta SQL: 
            // Unidades do sistema corrente
            // id (códigos) das unidades
            lsUni[i] = new Lista(
                "SELECT classe FROM unidade, sistema " +
                " WHERE sistema.nome = '" + n +
                "' AND unidade.id_sistema = sistema.id");
            lsUniCod[i] = new Lista(
                "SELECT unidade.id FROM unidade, sistema " +
                " WHERE sistema.nome = '" + n +
                "' AND unidade.id_sistema = sistema.id");
            
            while(j < lsUni[i].size()) {
                // Fixa-se o sistema e o loop em j cria nós para as unidades
                // e as adiciona ao nó do sistema corrente
                // Nó na forma "id(codigo) - classe"
            
                String uD = lsUni[i].getString(j);
                String uId = lsUniCod[i].getString(j);
                DefaultMutableTreeNode no = new DefaultMutableTreeNode(
                                (Object) uId + " - " + uD, true);
                
                lsSub = new Lista(
                    "SELECT subunidade.descricao FROM subunidade, unidade " +
                    " WHERE unidade.id = '" + uId +
                    "' AND subunidade.id_unidade = unidade.id");
                lsSubCod = new Lista(
                    "SELECT subunidade.id FROM subunidade, unidade " +
                    " WHERE unidade.id = '" + uId +
                    "' AND subunidade.id_unidade = unidade.id");
                
                int k = 0;
                while(k < lsSub.size()) {
                    String sD = lsSub.getString(k);
                    String sId = lsSubCod.getString(k);
                    DefaultMutableTreeNode noS = new DefaultMutableTreeNode(
                                    (Object) sId + " - " + sD, true);
                    
                    lsComp = new Lista(
                    "SELECT componente.descricao FROM componente, subunidade " +
                    " WHERE subunidade.id = '" + sId +
                    "' AND componente.id_subunidade = subunidade.id");
                    lsCompCod = new Lista(
                    "SELECT componente.id FROM componente, subunidade " +
                    " WHERE subunidade.id = '" + sId +
                    "' AND componente.id_subunidade = subunidade.id");
                    
                    int l = 0;
                    while(l < lsComp.size()) {
                        String cD = lsComp.getString(l);
                        String cId = lsCompCod.getString(l);
                        DefaultMutableTreeNode noC = new DefaultMutableTreeNode(
                                        (Object) cId + " - " + cD, true);

                        lsPte = new Lista(
                        "SELECT parte.descricao FROM parte, componente " +
                        " WHERE componente.id = '" + cId +
                        "' AND parte.id_componente = componente.id");
                        lsPteCod = new Lista(
                        "SELECT parte.id FROM parte, componente " +
                        " WHERE componente.id = '" + cId +
                        "' AND parte.id_componente = componente.id");

                        int m = 0;
                        while(m < lsPte.size()) {
                            String pD = lsPte.getString(m);
                            String pId = lsPteCod.getString(m);
                            DefaultMutableTreeNode noP = new DefaultMutableTreeNode(
                                    (Object) pId + " - " + pD, true);
                        
                            noC.add(noP);
                            m++;
                        }
                    
                        noS.add(noC);
                        l++;
                    }
                    
                    no.add(noS);
                    k++;
                }
                arrayNOSistema[i].add(no);
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
        tree.addMouseListener(new cliqueDireito());
        
        // Define o tipo de seleção da tree: seleção única
        tree.getSelectionModel().setSelectionMode(
                    TreeSelectionModel.SINGLE_TREE_SELECTION);
        
        // Adiciona o listener de seleção da tree
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode no = (DefaultMutableTreeNode)
                            tree.getLastSelectedPathComponent();
                
                // Se nada for selecionado
                if(no == null) return;
                
                // Se a seleção for no nível 2 da tree
                if(no.getLevel() >= 2) {
                    // Pega o nó que foi selecionado
                    // Divide a descrição do nó em dois String usando split
                    // Pega o id(código) e classe da unidade
                    String descricaoNo = no.getUserObject().toString();
                    String[] splitDescricao = descricaoNo.split(" - ");
                    CODIGO_SELECAO_NO = splitDescricao[0];
                    NOME_SELECAO_NO = splitDescricao[1];
                    System.out.println("id:" + CODIGO_SELECAO_NO);
                    System.out.println("info:" + NOME_SELECAO_NO);
                }

                // Pega o nível
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
                        jmiEquipes;
        
        public PopupUnidades() {
            // Cria os itens do menu popup
            jmiPrevisao = new JMenuItem("Previsão");
            jmiDemandas = new JMenuItem("Demandas");
            jmiIntervencao = new JMenuItem("Intervenções");
            jmiEquipes = new JMenuItem("Equipes");

            // Adiciona os ActionListeners aos itens do menu popup
            jmiPrevisao.addActionListener(new ActionPrevisao());
            jmiDemandas.addActionListener(new ActionTabelaDemanda());
            jmiIntervencao.addActionListener(new ActionTabelaIntervencao());
    //        jmiEquipes.addActionListener(new ActionTabelaEquipe());

            // Adiciona os itens ao menu popup            
            add(jmiPrevisao);
            addSeparator();
            add(jmiDemandas);
            add(jmiIntervencao);
            add(jmiEquipes);
        }    
    }
    
    // -------------------------------------------------------------------------
    // Classes privadas.
    // -------------------------------------------------------------------------

    /**
     * 
     */
    private static class cliqueDireito extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            // Verifica se o clique veio do botão direito do mouse
            if(SwingUtilities.isRightMouseButton(e)) {
                // Pega o item da tree pela localização
                // Define este item como o selecionado
                // Desse modo, pode-se selecionar usando botão direito
                TreePath pathSelecao = tree.getClosestPathForLocation(
                                                        e.getX(), e.getY());
                tree.setSelectionPath(pathSelecao);
            }
            if(e.isPopupTrigger()) {
                try {
                    mostrarPopupMenu(e);
                } catch(SQLException ex) {ex.printStackTrace();}
            }}
        
        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.isPopupTrigger()) {
                try {
                    mostrarPopupMenu(e);
                } catch(SQLException ex) {ex.printStackTrace();}
            }
        }
        
        // Mostra o menu popup
        private void mostrarPopupMenu(MouseEvent e) throws SQLException {
            // Testa qual o nível do nó selecionado
            // Nível 0: "Sistemas"
            // Nível 1: nós com os nomes dos sistemas
            // Nível 2: nós com os nomes das unidades
//            String t = null, c = null, id = CODIGO_SELECAO_NO;
//            switch(NIVEL_SELECAO_NO) {
//                case 2: t = "unidade"; c = "id_sistema"; break;
//                case 3: t = "subunidade"; c = "id_unidade"; break;
//                case 4: t = "componente"; c = "id_subunidade"; break;
//                case 5: t = "parte"; c = "id_componente"; break;
//                default: break;
//            }
//            
//            // Compoe a consulta SQL
//            // Consulta retorna a contagem de linhas na tabela especificada
//            // Verifica se há demandas 
//            String q = "SELECT COUNT(*) FROM " + t + " WHERE " + c + " = " + id;
//            int ct = sql.Query.consultaSQLInt(q);

            //
            if(NIVEL_SELECAO_NO >= 2) {
                // Cria um menu popup da classe PopUnidades
                // Exibe o menu no componente que deu origem à ação
                // e nas coordenadas do evento
                PopupUnidades popupUni = new PopupUnidades();
                popupUni.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
    
    /**
     * 
     */
    private static class ActionPrevisao implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            String hoje = HoraFormatada.hoje();
            String s = tree.getLastSelectedPathComponent().toString();
            String p = "Gráfico " + hoje + " " + s;
            String g = "Parâmetros " + hoje + " " + s;
            
            pnlAbaNordeste.addTab(p, null,
                        new PainelConteudo(
                            new Plot(pnlAbaNordeste.getWidth(),
                                    pnlAbaNordeste.getHeight())), p);
            pnlAbaSudeste.addTab(g, null,
                        new PainelConteudo(new TabelaParametrosEICs()), g);
        }
    }
    
    private static class ActionTabelaDemanda implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Cria um painel demanda do item selecionado na tree
            PainelDemanda pnlDemanda = new PainelDemanda(
                        NIVEL_SELECAO_NO, CODIGO_SELECAO_NO);

            String t = "Demandas de " + 
                        tree.getLastSelectedPathComponent().toString();
            pnlAbaNordeste.addTab(t, null,
                            new PainelConteudo(pnlDemanda.getTabela()), t);
        }
    }
    
    private static class ActionTabelaIntervencao implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Cria um painel intervenção do item selecionado na tree
            PainelIntervencao pnlIntervencao = new PainelIntervencao(
                        NIVEL_SELECAO_NO, CODIGO_SELECAO_NO);

            String t = "Intervenções de " + 
                        tree.getLastSelectedPathComponent().toString();
            pnlAbaNordeste.addTab(t, null,
                            new PainelConteudo(pnlIntervencao.getTabela()), t);
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
//            // Serão s listas
//            lsUni = new Lista[s];
//                    
//            // A classe Query realiza uma determinada consulta SQL
//            // Neste caso, retorna a contagem de linhas da tabela UNIDADE
//            u = sql.Query.consultaSQLInt("SELECT COUNT(*) FROM unidade;");
//            System.out.println(String.valueOf(u));
//            
//            // Inicializa o array de nós da árvore sistema
//            // Cada nó criado corresponde a um elemento da lista SQL
//            arrayNOSistema = new DefaultMutableTreeNode[s];
//            
//            // Inicializa o array bidimensional de nós das unidades
//            // São s sistemas, e cada sistema terá no máximo u unidades
//            arrayNodeUnidade = new DefaultMutableTreeNode[s][u];
//            
//            int i = 0, j = 0;
//            while(i < lsSistemas.toVector().size()) {
//                // Preenche o array dos nós (sistemas)
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
//                    // Fixa-se o sistema e o loop em j cria nós para as unidades
//                    // e as adiciona ao nó do sistema corrente
//                    arrayNOSistema[i].add(new DefaultMutableTreeNode(
//                                        lsUni[i].toVector().get(j)));
////                    arrayNodeUnidade[i][j] = new DefaultMutableTreeNode(
////                                        lsUni[i].toVector().get(j));
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
//            top.add(arrayNOSistema[i]);
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


