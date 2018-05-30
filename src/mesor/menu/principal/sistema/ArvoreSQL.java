/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mesor.menu.principal.sistema;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import mesor.sql.Lista;
import mesor.sql.Query;

/**
 * ArvoreSQL.java
 *
 * @version 1.0 30/03/2018
 * @author Rubens Júnior
 */
public class ArvoreSQL extends JPanel {
    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    
    public JLabel jlab;
    public mesor.sql.Lista lsSistemas;
    public mesor.sql.Lista[] lsUni;
    public mesor.sql.Lista[] lsUniCod;
    
    public DefaultMutableTreeNode[] arrayNOSistema;
    public DefaultMutableTreeNode[][] arrayNodeUnidade;
    
    public int s, u, sb;
    
    public mesor.sql.Lista lsSub;
    public mesor.sql.Lista lsSubCod;
    public mesor.sql.Lista lsComp;
    public mesor.sql.Lista lsCompCod;
    public mesor.sql.Lista lsPte;
    public mesor.sql.Lista lsPteCod;
    
    String root;

    /**
     * Construtores.
     */
    public ArvoreSQL() {
        super(new GridLayout(1,0));
        
        // Cria nó sistema
        rootNode = new DefaultMutableTreeNode("Sistemas");
        povoarRoot();
        
        // Cria o modelo
        treeModel = new DefaultTreeModel(rootNode);
	treeModel.addTreeModelListener(new MyTreeModelListener());

        // Cria a árvore
        tree = new JTree(treeModel);
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);

        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);
        
        JButton btn = new JButton("OK");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarRoot();
            }
        });
        add(btn);
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------

    private class MyTreeModelListener implements TreeModelListener {

        public MyTreeModelListener() {
        }

        @Override
        public void treeNodesChanged(TreeModelEvent e) {
//            povoarRoot();
            treeModel.reload(rootNode);
            treeModel.setRoot(null);
            treeModel.setRoot(rootNode);
        }

        @Override
        public void treeNodesInserted(TreeModelEvent e) {
        }

        @Override
        public void treeNodesRemoved(TreeModelEvent e) {
        }

        @Override
        public void treeStructureChanged(TreeModelEvent e) {
        }
    }

    private void getComprimentosChildren() {
        lsSistemas = new Lista("SELECT nome FROM sistema");
        s = lsSistemas.size();
        lsUni = new Lista[s];
        lsUniCod = new Lista[s];
        u = Query.consultaSQLInt("SELECT COUNT(*) FROM unidade;");
        sb = Query.consultaSQLInt("SELECT COUNT(*) FROM subunidade;");
    }
    
    private void povoarChidren() {
        getComprimentosChildren();
        
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
    
    public void povoarRoot() {
        povoarChidren();
        
        // Adiciona os nós dos sistemas à árvore
        int i = 0;
        while(i < s) {
            rootNode.add(arrayNOSistema[i]);
            i++;
        }
    }
    
    public void atualizarRoot() {
//        treeModel.nodeChanged(rootNode);
        treeModel.nodeChanged(rootNode);
//        treeModel.reload(rootNode);
//        treeModel.setRoot(rootNode);
    }
    //
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("DynamicTreeDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        ArvoreSQL newContentPane = new ArvoreSQL();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}