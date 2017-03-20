package menu.painel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.sql.SQLException;

import conexaoSql.ModeloTabela;

/**
 * PainelIntervencao.java
 * 
 * @version 1.0 5/2/2017
 * @author Rubens Jr
 */
public class PainelIntervencao {
    
    public JTable tabIntervencao;
    
    private final DefaultTableCellRenderer render;
    
    private final JPanel pnlIntervencao;
    private final JScrollPane iPane;
    
    /**
     * Construtor.
     */
    public PainelIntervencao() {
        
        /**
         * Renderizador de célula da tabela.
         */
        render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        
        /**
         * Cria a tabela com uma consulta em SQL.
         */
        try {
            tabIntervencao = new JTable(
                        new ModeloTabela("SELECT * FROM intervencao"));
            
            atualizarAparenciaDaTabela();
            
        } catch (SQLException ex) { ex.getErrorCode();}
        
        /**
         * Cria o JScrollPane da tabela.
         */
        iPane = new JScrollPane(tabIntervencao,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabIntervencao.setFillsViewportHeight(true);
        
        pnlIntervencao = new JPanel(new FlowLayout());
        pnlIntervencao.add(iPane);
        pnlIntervencao.setOpaque(true);
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /**
     * 
     * @return Um JPanel com a tabela.
     */
    public JPanel painelTabelas() {
        return pnlIntervencao;
    }
    
    /**
     * Atualiza parâmetros da tabela como renderizador das células e largura das
     * colunas.
     */
    public void atualizarAparenciaDaTabela() {

        tabIntervencao.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabIntervencao.getTableHeader().setReorderingAllowed(false);
        tabIntervencao.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tabIntervencao.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tabIntervencao.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabIntervencao.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabIntervencao.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabIntervencao.getColumnModel().getColumn(3).setPreferredWidth(160);
        tabIntervencao.getColumnModel().getColumn(4).setPreferredWidth(160);
        tabIntervencao.getColumnModel().getColumn(5).setPreferredWidth(70);
        tabIntervencao.getColumnModel().getColumn(6).setPreferredWidth(100);
        tabIntervencao.getColumnModel().getColumn(7).setPreferredWidth(100);
        tabIntervencao.getColumnModel().getColumn(8).setPreferredWidth(60);
        tabIntervencao.getColumnModel().getColumn(9).setPreferredWidth(100);

        tabIntervencao.getColumnModel().getColumn(0).setCellRenderer(render);
        tabIntervencao.getColumnModel().getColumn(3).setCellRenderer(render);
        tabIntervencao.getColumnModel().getColumn(5).setCellRenderer(render);
        tabIntervencao.getColumnModel().getColumn(6).setCellRenderer(render);
        tabIntervencao.getColumnModel().getColumn(7).setCellRenderer(render);
        tabIntervencao.getColumnModel().getColumn(8).setCellRenderer(render);
        tabIntervencao.getColumnModel().getColumn(9).setCellRenderer(render);
    }
    
    /**
     * Habilita ou desabilita a tabela. Permite a seleção de linhas e altera a
     * cor da fonte dependendo do seu estado.
     * 
     * @param habilita - booleano
     */
    public void habilitarTabela(boolean habilita) {
        tabIntervencao.setEnabled(habilita);
        //tabIntervencao.setRowSelectionAllowed(habilita);
        
        if(habilita == true) {
            tabIntervencao.setForeground(Color.BLACK);
        } else if(habilita == false) {
            tabIntervencao.setForeground(Color.LIGHT_GRAY);
        }
    }
    
    /**
     * Define o tamanho da tabela, alterando o tamanho preferencial do
     * JScrollPane que a recebe.
     * 
     * @param dim - objeto da classe Dimension
     */
    public void tamanhoDaTabela(Dimension dim) {
        iPane.setPreferredSize(dim);
    }
}