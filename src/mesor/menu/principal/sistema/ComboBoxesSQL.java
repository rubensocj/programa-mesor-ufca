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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import mesor.menu.DialogoAviso;
import mesor.menu.HoraFormatada;
import mesor.menu.painel.aba.PainelConteudo;
import mesor.menu.painel.taxonomia.PainelDemanda;
import mesor.menu.painel.taxonomia.PainelIntervencao;
import mesor.sql.Lista;
import static mesor.menu.principal.PainelPrincipal.pnlAbaNordeste;
import static mesor.menu.principal.PainelPrincipal.pnlAbaSudeste;
import mesor.r.CamadaR;
import mesor.r.Plot;
import mesor.r.TabelaICs;
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
       
        atualizarModelo();
        
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
        
        setMinimumSize(new Dimension(300,800));
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------

    /**
     * Monta o modelo dos JComboBox adicionando o item "Selecione..." no início
     * das opções
     * @param l Lista com os resultados da consulta SQL
     * @return Um array object com as opções de seleção do JComboBox
     */
    private Object[] comporComboBoxModel(Lista l) {
        Object[] o = new Object[l.size() + 1];
        o[0] = "Selecione...";
        for(int i = 0; i < l.size(); i++) {
            o[i + 1] = l.get(i);
        }
        return o;
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
                        
                        String cod = String.valueOf(arrayItem[0]);
                        int i = 1;
                        while(arrayItem[i] != 0) {
                            cod = cod.concat("-".concat(String.valueOf(arrayItem[i])));
                            i++;
                        }
                        
                        String hoje = HoraFormatada.hoje();
                        
                        // Títulos das abas
                        // ex.: Gráfico MOTOR 1-3-15
                        String tituloAbaGrafico = "Gráfico de " + s;    
                        String tituloAbaParametros = "Parâmetros de " + s;
                        String tituloAbaPrevisao = "Previsão de " + s;
                        
                        // Títulos dos arquivos
                        // ex.: _1-3-15_25082018
                        String p = "_" + cod + "_" + hoje;

                        // Gráfico
                        pnlAbaNordeste.addTab(tituloAbaGrafico, null,
                            new PainelConteudo(
                                new Plot(pnlAbaNordeste.getWidth(), pnlAbaNordeste.getHeight(), arrayItem, p)
                                ),
                            p);
                        
                        // Tabela parâmetros
                        pnlAbaSudeste.addTab(tituloAbaParametros, null,
                            new PainelConteudo(new TabelaParametrosEICs(p)), p);
                        
                        // Título previsão
                        pnlAbaSudeste.addTab(tituloAbaPrevisao, null,
                            new PainelConteudo(new TabelaICs(p)), p);
                        
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
                mUni = new DefaultComboBoxModel(comporComboBoxModel(lUni));
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
                mSub = new DefaultComboBoxModel(comporComboBoxModel(lSub));
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
                mCmp = new DefaultComboBoxModel(comporComboBoxModel(lCmp));
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
                mPte = new DefaultComboBoxModel(comporComboBoxModel(lPte));
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
    
    private Lista getSistemas() {
        return new Lista("SELECT CONCAT_WS(\" - \", id, nome) FROM sistema;");
    }
    
    public void atualizarModelo() {
        Lista lSis = getSistemas();
        if(lSis.size() != 0) {
            cbxSis.setEnabled(true);
            cbxUni.setEnabled(false);
            cbxSub.setEnabled(false);
            cbxCmp.setEnabled(false);
            cbxPte.setEnabled(false);
            
            mSis = new DefaultComboBoxModel(comporComboBoxModel(lSis));
            cbxSis.setModel(mSis);
            
            cbxSis.addItemListener(new ItemEventSis());
            cbxUni.addItemListener(new ItemEventUni());
            cbxSub.addItemListener(new ItemEventSub());
            cbxCmp.addItemListener(new ItemEventCmp());
        }
    }
}