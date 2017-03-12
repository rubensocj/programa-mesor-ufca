package menu;

import conexaoSql.ModeloTabela;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * PainelEquipe.java
 *
 * @version 1.0 19/02/2017
 * @author Rubens Jr
 */
public class PainelEquipe {

    public enum Tabela {interventor, equipe}
    public JTable tabEquipe;
    public JPanel pnlEquipe;
    
    private final DefaultTableCellRenderer render;   
    private final JScrollPane ePane; 
    private ModeloTabela modelo;
    private Tabela tipoDeTabela;
    
    /**
     * Construtores.
     * @param tabela
     */
    public PainelEquipe(Tabela tabela) {
        setTabela(tabela);
        
        /**
         * Renderizador de célula da tabela.
         */
        render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        
        try {
            tabEquipe = new JTable(new ModeloTabela(
                        "SELECT * FROM " + String.valueOf(this.tipoDeTabela)));
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Cria o JScrollPane da tabela.
         */
        ePane = new JScrollPane(tabEquipe,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabEquipe.setFillsViewportHeight(true);
        
        pnlEquipe = new JPanel(new FlowLayout());
        pnlEquipe.add(ePane);
        pnlEquipe.setOpaque(true);
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
     
    public void setTabela(Tabela tabela) {
        this.tipoDeTabela = tabela;
    }
    
    /**
     * 
     * @return Um JPanel com a tabela.
     */
    public JPanel painelTabelas() {
        return pnlEquipe;
    }
    
    /**
     * Reinicia a tabela através de uma consulta SQL {@code SELECT * FROM
     * demanda}.
     * 
     * @throws SQLException 
     */
    public void reiniciarTabela() throws SQLException {
        modelo = (ModeloTabela) tabEquipe.getModel();
        modelo.setQuery("SELECT * FROM " + String.valueOf(this.tipoDeTabela));
    }
    
    /**
     * Busca pelos inteventores da equipe selecionada, através de uma cosulta
     * SQL.
     * 
     * @param idEquipe
     * @throws SQLException 
     */
    public void buscarEquipeInterventor(Object idEquipe) throws SQLException {
        modelo = (ModeloTabela) tabEquipe.getModel();
        modelo.setQuery("SELECT interventor.* FROM equipe, interventor " + 
            "WHERE interventor.id_equipe = " + String.valueOf(idEquipe) + " " +
            "GROUP BY interventor.id;");
    }
    
    /**
     * Atualiza parâmetros da tabela como renderizador das células e largura das
     * colunas.
     */
    public void atualizarAparenciaDaTabela() {
        tabEquipe.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabEquipe.getTableHeader().setReorderingAllowed(false);
        tabEquipe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabEquipe.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        switch(this.tipoDeTabela) {
            case equipe:
                tabEquipe.getColumnModel().getColumn(0).setPreferredWidth(40);
                tabEquipe.getColumnModel().getColumn(1).setPreferredWidth(260);
                tabEquipe.getColumnModel().getColumn(2).setPreferredWidth(100);

                tabEquipe.getColumnModel().getColumn(0).setCellRenderer(render);
                tabEquipe.getColumnModel().getColumn(2).setCellRenderer(render);
                
                break;
                
            case interventor:
                tabEquipe.getColumnModel().getColumn(0).setPreferredWidth(40);
                tabEquipe.getColumnModel().getColumn(1).setPreferredWidth(70);
                tabEquipe.getColumnModel().getColumn(2).setPreferredWidth(280);
                tabEquipe.getColumnModel().getColumn(3).setPreferredWidth(70);
                tabEquipe.getColumnModel().getColumn(4).setPreferredWidth(100);
                tabEquipe.getColumnModel().getColumn(5).setPreferredWidth(100);
                tabEquipe.getColumnModel().getColumn(6).setPreferredWidth(180);
                tabEquipe.getColumnModel().getColumn(7).setPreferredWidth(180);
                tabEquipe.getColumnModel().getColumn(8).setPreferredWidth(90);
                tabEquipe.getColumnModel().getColumn(9).setPreferredWidth(90);
                tabEquipe.getColumnModel().getColumn(10).setPreferredWidth(280);
                tabEquipe.getColumnModel().getColumn(11).setPreferredWidth(150);
                
                tabEquipe.getColumnModel().getColumn(0).setCellRenderer(render);
                tabEquipe.getColumnModel().getColumn(1).setCellRenderer(render);
                tabEquipe.getColumnModel().getColumn(4).setCellRenderer(render);
                tabEquipe.getColumnModel().getColumn(5).setCellRenderer(render);
                break;
                
            default:break;
        }
    }
    
    /**
     * Habilita ou desabilita a tabela. Permite a seleção de linhas e altera a
     * cor da fonte dependendo do seu estado.
     * 
     * @param habilita - booleano
     */
    public void habilitarTabela(boolean habilita) {
        tabEquipe.setEnabled(habilita);
        
        if(habilita == true) {
            tabEquipe.setForeground(Color.BLACK);
        } else if(habilita == false) {
            tabEquipe.setForeground(Color.LIGHT_GRAY);
        }
    }
    
    /**
     * Define o tamanho da tabela, alterando o tamanho preferencial do
     * JScrollPane que a recebe.
     * 
     * @param dim - objeto da classe Dimension
     */
    public void tamanhoDaTabela(Dimension dim) {
        ePane.setPreferredSize(dim);
    }
}

