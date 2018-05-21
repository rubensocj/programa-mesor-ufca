package mesor.menu.painel.taxonomia;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.sql.SQLException;
import mesor.menu.DialogoAviso;

import mesor.sql.ModeloTabela;

/**
 * PainelDemanda.java
 * 
 * @version 1.0 2/2/2017
 * @author Rubens Jr
 */
public class PainelSistema {
    
    public JTable tab;
    
    private final DefaultTableCellRenderer render;
    private ModeloTabela modelo;
    private final JPanel pnlSistema;
    private final JScrollPane sPane;
    
    /**
     * Construtor.
     */
    public PainelSistema() {
        
        /**
         * Renderizador de célula da tabela.
         */
        render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        
        tab = new JTable(
                    new ModeloTabela("SELECT * FROM sistema"));
        atualizarAparenciaDaTabela();
        
        /**
         * Cria o JScrollPane da tabela.
         */
        sPane = new JScrollPane(tab,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tab.setFillsViewportHeight(true);
        
        pnlSistema = new JPanel(new FlowLayout());
        pnlSistema.add(sPane);
        pnlSistema.setOpaque(true);
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /**
     * 
     * @return Um JPanel com a tabela.
     */
    public JPanel painelTabelas() {
        return pnlSistema;
    }
    
    /**
     * Reinicia a tabela através de uma consulta SQL {@code SELECT * FROM
     * demanda}.
     * 
     */
    public void reiniciarTabela() {
        try {
            modelo = (ModeloTabela) tab.getModel();
            modelo.setQuery("SELECT * FROM sistema");
        } catch (SQLException ex) {
            DialogoAviso.show("SQLExcpetion: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Busca por demandas nos equipamentos selecionados, através de uma cosulta
     * SQL.
     * 
     * @deprecated 
     * @param idUnidade
     * @param idSubunidade
     * @param idComponente
     * @param idParte
     * @throws SQLException 
     */
    public void buscarSistema(Object idUnidade, Object idSubunidade,
                Object idComponente, Object idParte) throws SQLException {
        
        modelo = (ModeloTabela) tab.getModel();
        if(idSubunidade == null) {
            modelo.setQuery(
                "SELECT demanda.* FROM " +
                    "demanda, unidade " +
                "WHERE demanda.id_unidade = " + String.valueOf(idUnidade) + 
                " GROUP BY demanda.id;");
        }
        else if(idComponente == null) {
            modelo.setQuery(
                "SELECT demanda.* FROM " +
                    "demanda, unidade, subunidade " +
                "WHERE demanda.id_unidade = " + String.valueOf(idUnidade) +
                " AND demanda.id_subunidade = " + String.valueOf(idSubunidade) + 
                " GROUP BY demanda.id;");
        }
        else if(idParte == null) {
            modelo.setQuery(
                "SELECT demanda.* FROM " +
                    "demanda, unidade, subunidade, componente " +
                "WHERE demanda.id_unidade = " + String.valueOf(idUnidade) +
                " AND demanda.id_subunidade = " + String.valueOf(idSubunidade) +
                " AND demanda.id_componente = " + String.valueOf(idComponente) + 
                " GROUP BY demanda.id;");
        } 
        else {
            modelo.setQuery(
                "SELECT demanda.* FROM " +
                    "demanda, unidade, subunidade, componente, parte " +
                "WHERE demanda.id_unidade = " + String.valueOf(idUnidade) +
                " AND demanda.id_subunidade = " + String.valueOf(idSubunidade) +
                " AND demanda.id_componente = " + String.valueOf(idComponente) +
                " AND demanda.id_parte = " + String.valueOf(idParte) +
                " GROUP BY demanda.id;");
        }
    }
    
    /**
     * Atualiza parâmetros da tabela como renderizador das células e largura das
     * colunas.
     */
    public void atualizarAparenciaDaTabela() {

        tab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tab.getTableHeader().setReorderingAllowed(false);
        tab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tab.getColumnModel().getColumn(0).setPreferredWidth(40);
        tab.getColumnModel().getColumn(1).setPreferredWidth(250);
        tab.getColumnModel().getColumn(2).setPreferredWidth(300);

        tab.getColumnModel().getColumn(0).setCellRenderer(render);
    }
    
    /**
     * Habilita ou desabilita a tabela. Permite a seleção de linhas e altera a
     * cor da fonte dependendo do seu estado.
     * 
     * @param habilita - booleano
     */
    public void habilitarTabela(boolean habilita) {
        tab.setEnabled(habilita);
        
        if(habilita == true) {
            tab.setForeground(Color.BLACK);
        } else if(habilita == false) {
            tab.setForeground(Color.LIGHT_GRAY);
        }
    }
    
    /**
     * Define o tamanho da tabela, alterando o tamanho preferencial do
     * JScrollPane que a recebe.
     * 
     * @param dim - objeto da classe Dimension
     */
    public void tamanhoDaTabela(Dimension dim) {
        sPane.setPreferredSize(dim);
    }
}