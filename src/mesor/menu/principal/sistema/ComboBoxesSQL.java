/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mesor.menu.principal.sistema;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import mesor.menu.DialogoAviso;
import mesor.menu.HoraFormatada;
import mesor.menu.painel.aba.PainelConteudo;
import mesor.menu.painel.taxonomia.PainelDemanda;
import mesor.menu.painel.taxonomia.PainelIntervencao;
import mesor.sql.Lista;
import mesor.sql.Query;
import static mesor.menu.principal.PainelPrincipal.pnlAbaNordeste;
import static mesor.menu.principal.PainelPrincipal.pnlAbaSudeste;
import mesor.r.Plot;
import mesor.r.TabelaParametrosEICs;

/**
 * ArvoreSQL.java
 *
 * @version 1.0 30/03/2018
 * @author Rubens Júnior
 */
public class ComboBoxesSQL extends JPanel {
    
    public JLabel lblSis, lblUni, lblSub, lblCmp, lblPte, lblAcao;
    
    public ComboBoxSql cbxSis, cbxUni, cbxSub, cbxCmp, cbxPte, cbxAcao;
    
    private DefaultComboBoxModel mSis = null, mUni = null, mSub = null, mCmp = null, mPte = null;

    public JButton btnAcao;
    
    private String nivel = "", CODIGO_SELECAO = "";
    
    public int[] arrayItem = new int[] {0,0,0,0,0};
    
    public int NIVEL_SELECAO = 0;
    
    /**
     * Construtores.
     */
    public ComboBoxesSQL() {
        super(new BorderLayout());
        
        // Inicializa labels
        lblSis = new JLabel("Sistemas");
        lblUni = new JLabel("Unidades");
        lblSub = new JLabel("Subunidades");
        lblCmp = new JLabel("Componentes");
        lblPte = new JLabel("Partes");
        lblAcao = new JLabel("Ação");
        
        // Inicializa ComboBoxes
        cbxSis = new ComboBoxSql();
        cbxUni = new ComboBoxSql();
        cbxSub = new ComboBoxSql();
        cbxCmp = new ComboBoxSql();
        cbxPte = new ComboBoxSql();
        cbxAcao = new ComboBoxSql();
        cbxAcao.setModel(new DefaultComboBoxModel(
                new Object[] {"Selecione...", "Previsão", "Demandas", "Intervenções", "Equipes"}));
       
        Lista lSis = new Lista("SELECT CONCAT_WS(\" - \", id, nome) FROM sistema;");
        if(lSis.size() != 0) {
            cbxSis.setEnabled(true);
            mSis = new DefaultComboBoxModel(lSis.toVector());
            cbxSis.setModel(mSis);
            
            cbxSis.addItemListener(new ItemEventSis());
            cbxUni.addItemListener(new ItemEventUni());
            cbxSub.addItemListener(new ItemEventSub());
            cbxCmp.addItemListener(new ItemEventCmp());
        }
        
        btnAcao = new JButton("Confirmar");
        btnAcao.setEnabled(false);
        btnAcao.addActionListener(new ActionPrincipal());
        
        JPanel pnl1 = new JPanel(new GridLayout(12,1));
        pnl1.add(lblSis);
        pnl1.add(cbxSis);
        pnl1.add(lblUni);
        pnl1.add(cbxUni);
        pnl1.add(lblSub);
        pnl1.add(cbxSub);
        pnl1.add(lblCmp);
        pnl1.add(cbxCmp);
        pnl1.add(lblPte);
        pnl1.add(cbxPte);
        pnl1.add(lblAcao);
        pnl1.add(cbxAcao);
        
        JPanel pnl2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnl2.add(btnAcao);
        
        add(pnl1, "North");
        add(pnl2, "Center");
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
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
        ComboBoxesSQL newContentPane = new ComboBoxesSQL();
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

    private class ActionPrincipal implements ActionListener {

        public ActionPrincipal() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(cbxAcao.isEnabled() && cbxUni.getSelectedObjects().length != 0
                && !cbxAcao.getSelectedItem().equals("Selecione...")) {
                String a = cbxAcao.getSelectedItem().toString();
                String s = "", t = "";
                
                // Identifica qual o último nível taxonômico selecionado
                // Pega a identificação do item
                switch(nivel) {
                    case "unidade": 
                        s = cbxUni.getSelectedItem().toString();
                        break;
                    case "subunidade": 
                        s = cbxSub.getSelectedItem().toString();
                        break;
                    case "componente": 
                        s = cbxCmp.getSelectedItem().toString();
                        break;
                    case "parte": 
                        s = cbxPte.getSelectedItem().toString();
                        break;
                }
                
                // Ações
                switch(a) {
                    case "Previsão": 
                        String hoje = HoraFormatada.hoje();
                        String p = "Gráfico " + hoje + " " + s;
                        String g = "Parâmetros " + hoje + " " + s;

                        pnlAbaNordeste.addTab(p, null,
                                new PainelConteudo(
                                    new Plot(pnlAbaNordeste.getWidth(), pnlAbaNordeste.getHeight(), arrayItem, p)
                                    ),
                                p);
                        pnlAbaSudeste.addTab(g, null,
                                    new PainelConteudo(new TabelaParametrosEICs()), g);
                        break;
                    case "Demandas": 
                        // Cria um painel demanda do item selecionado na tree
                        PainelDemanda pnlDemanda = new PainelDemanda(
                                    NIVEL_SELECAO, CODIGO_SELECAO);

                        t = "Demandas de " + s;
                        pnlAbaNordeste.addTab(t, null,
                                        new PainelConteudo(pnlDemanda.getTabela()), t);
                        break;
                    case "Intervenções": 
                        // Cria um painel intervenção do item selecionado na tree
                        PainelIntervencao pnlIntervencao = new PainelIntervencao(
                                    NIVEL_SELECAO, CODIGO_SELECAO);

                        t = "Intervenções de " + s;
                        pnlAbaNordeste.addTab(t, null,
                                        new PainelConteudo(pnlIntervencao.getTabela()), t);
                        break;
                    case "Equipes": break;
                }
            } else {
                DialogoAviso.show("Informe uma ação e ao menos a unidade");
            }
        }
    }
    
    private class ComboBoxSql extends JComboBox {
        public ComboBoxSql() {
            setPreferredSize(new Dimension(275, 20));
            setMaximumRowCount(10);
            setEnabled(false);
        }
    }
    
    private class ItemEventSis implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String n = (String) e.getItem().toString().split(" - ")[0];
                Lista lUni = new Lista(
                    "SELECT CONCAT_WS(\" - \", unidade.id, unidade.classe) FROM " +
                    "unidade, sistema WHERE sistema.id = '" + n +
                    "' AND unidade.id_sistema = sistema.id");
                
                cbxUni.setEnabled(true);
                cbxSub.setEnabled(false);
                cbxCmp.setEnabled(false);
                cbxPte.setEnabled(false);
                mUni = new DefaultComboBoxModel(lUni.toVector());
                cbxUni.setModel(mUni);
                
                arrayItem[0] = Integer.parseInt(n);
            }
        }
    }
    
    private class ItemEventUni implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String n = (String) e.getItem().toString().split(" - ")[0];
                Lista lSub = new Lista(
                    "SELECT CONCAT_WS(\" - \", subunidade.id, subunidade.descricao) FROM " +
                    "subunidade, unidade WHERE unidade.id = '" + n +
                    "' AND subunidade.id_unidade = unidade.id");
                
                cbxSub.setEnabled(true);
                cbxCmp.setEnabled(false);
                cbxPte.setEnabled(false);
                cbxAcao.setEnabled(true);
                btnAcao.setEnabled(true);
                mSub = new DefaultComboBoxModel(lSub.toVector());
                cbxSub.setModel(mSub);
                
                nivel = "unidade";
                arrayItem[1] = Integer.parseInt(n);
                CODIGO_SELECAO = n;
                NIVEL_SELECAO = 2;
            }
        }
    }
    
    private class ItemEventSub implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String n = (String) e.getItem().toString().split(" - ")[0];
                Lista lCmp = new Lista(
                    "SELECT CONCAT_WS(\" - \", componente.id, componente.descricao) FROM " +
                    "componente, subunidade WHERE subunidade.id = '" + n +
                    "' AND componente.id_subunidade = subunidade.id");
                
                cbxCmp.setEnabled(true);
                cbxPte.setEnabled(false);
//                Object[] obj = new Object[lCmp.size() + 1];
//                obj[0] = "Selecione...";
//                for(int i = 1; i < obj.length; i++) {
//                    obj[i] = lCmp.toArray()[i - 1];
//                }
                mCmp = new DefaultComboBoxModel(lCmp.toVector());
                cbxCmp.setModel(mCmp);
                
                nivel = "subunidade";
                arrayItem[2] = Integer.parseInt(n);
                CODIGO_SELECAO = n;
                NIVEL_SELECAO = 3;
            }
        }
    }
    
    private class ItemEventCmp implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String n = (String) e.getItem().toString().split(" - ")[0];
                Lista lPte = new Lista(
                    "SELECT CONCAT_WS(\" - \", parte.id, parte.descricao) FROM " +
                    "parte, componente WHERE componente.id = '" + n +
                    "' AND parte.id_componente = componente.id");
                
                cbxPte.setEnabled(true);
//                Object[] obj = new Object[lCmp.size() + 1];
//                obj[0] = "Selecione...";
//                for(int i = 1; i < obj.length; i++) {
//                    obj[i] = lCmp.toArray()[i - 1];
//                }
                mPte = new DefaultComboBoxModel(lPte.toVector());
                cbxPte.setModel(mPte);
                
                nivel = "componente";
                arrayItem[3] = Integer.parseInt(n);
                CODIGO_SELECAO = n;
                NIVEL_SELECAO = 4;
            }
        }
    }
    private class ItemEventPte implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String n = (String) e.getItem().toString().split(" - ")[0];
                nivel = "parte";
                arrayItem[4] = Integer.parseInt(n);
                CODIGO_SELECAO = n;
                NIVEL_SELECAO = 5;
            }
        }
    }
}