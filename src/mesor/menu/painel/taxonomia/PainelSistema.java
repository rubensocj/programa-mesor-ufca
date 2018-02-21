package mesor.menu.painel.taxonomia;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.sql.SQLException;

import mesor.sql.ModeloTabela;

/**
 * PainelDemanda.java
 * 
 * @version 1.0 2/2/2017
 * @author Rubens Jr
 */
public class PainelSistema {
    
    public JTable tabSistema;
    
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
        
        /**
         * Cria a tabela com uma consulta em SQL.
         */
        try {
            tabSistema = new JTable(
                        new ModeloTabela("SELECT * FROM sistema"));
            
            atualizarAparenciaDaTabela();
            
        } catch (SQLException ex) { ex.getErrorCode();}
        
        /**
         * Cria o JScrollPane da tabela.
         */
        sPane = new JScrollPane(tabSistema,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabSistema.setFillsViewportHeight(true);
        
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
     * @throws SQLException 
     */
    public void reiniciarTabela() throws SQLException {
        modelo = (ModeloTabela) tabSistema.getModel();
        modelo.setQuery("SELECT * FROM sistema");
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
        
        modelo = (ModeloTabela) tabSistema.getModel();
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

        tabSistema.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabSistema.getTableHeader().setReorderingAllowed(false);
        tabSistema.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tabSistema.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tabSistema.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabSistema.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabSistema.getColumnModel().getColumn(2).setPreferredWidth(300);

        tabSistema.getColumnModel().getColumn(0).setCellRenderer(render);
    }
    
    /**
     * Habilita ou desabilita a tabela. Permite a seleção de linhas e altera a
     * cor da fonte dependendo do seu estado.
     * 
     * @param habilita - booleano
     */
    public void habilitarTabela(boolean habilita) {
        tabSistema.setEnabled(habilita);
        
        if(habilita == true) {
            tabSistema.setForeground(Color.BLACK);
        } else if(habilita == false) {
            tabSistema.setForeground(Color.LIGHT_GRAY);
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