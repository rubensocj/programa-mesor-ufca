package mesor.menu.painel.taxonomia;

import java.awt.BorderLayout;
import mesor.sql.ModeloTabela;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import mesor.menu.DialogoAviso;
import mesor.menu.Janela;

/**
 * PainelEquipe.java
 *
 * @version 1.0 19/02/2017
 * @author Rubens Jr
 */
public class PainelEquipe extends JPanel {

    private JLabel lblEqp;
    private TableRowSorter<ModeloTabela> ordEqp;
    private JPanel pnlEqpFinal;
    private JTextField tfdBuscaEqp;
    private JButton btnBuscaEqp;

    public enum Tabela {interventor, equipe}
    public JTable tabEquipe;
    public JPanel pnlEquipe;
    
    private final DefaultTableCellRenderer render;   
    private final JScrollPane ePane; 
    private ModeloTabela modelo;
    
    private int largura, altura;
    
    public PainelEquipe(int x, int y) {
        
        largura = x;
        altura = y;
        
        lblEqp = new JLabel(" Equipes:");
        
        /**
         * Renderizador de célula da tabela Demanda.
         */
        render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        
        ModeloTabela modeloEqp = new ModeloTabela(
                    "SELECT id, objetivo_geral FROM equipe;");
        ordEqp = new TableRowSorter<>(modeloEqp);
        tabEquipe = new JTable(modeloEqp);
        tabEquipe.setRowSorter(ordEqp);
        tabEquipe.setCellSelectionEnabled(false);
        atualizarAparenciaDaTabela();
        habilitarTabela();
        
        // Interventor
        ePane = new JScrollPane(tabEquipe,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabEquipe.setFillsViewportHeight(true);
        tabEquipe.setPreferredScrollableViewportSize(new Dimension(largura, altura));
        
        inicializaEConfiguraBusca();
        
        // Painel Interventor        
        JPanel pnlEqp = new JPanel(new BorderLayout(0,5));
        pnlEqp.add(PainelInterventor.painelBusca(
                        lblEqp, tfdBuscaEqp, btnBuscaEqp), BorderLayout.NORTH);
        pnlEqp.add(ePane, BorderLayout.CENTER);
        
        // PainelInterventor final
        pnlEqpFinal = new JPanel(new BorderLayout(10,0));
        pnlEqpFinal.add(pnlEqp, BorderLayout.CENTER);
        pnlEqpFinal.setOpaque(true);
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------

    /**
     * 
     * @return Um JPanel com a tabela.
     */
    public JPanel painelTabelas() {
        return pnlEqpFinal;
    }
    
    /**
     * Reinicia a tabela através de uma consulta SQL {@code SELECT * FROM
     * demanda}.
     * 
     */
    public void reiniciarTabela() {
        modelo = (ModeloTabela) tabEquipe.getModel();
        try {
            modelo.setQuery("SELECT id, objetivo_geral FROM equipe;");
        } catch (SQLException ex) {
            DialogoAviso.show("SQLExcpetion: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IllegalStateException ex) {
            DialogoAviso.show("IllegalStateException: " + ex.getMessage());
            ex.printStackTrace();
        }
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

        tabEquipe.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabEquipe.getColumnModel().getColumn(1).setPreferredWidth(largura - 40);

        tabEquipe.getColumnModel().getColumn(0).setCellRenderer(render);
    }
    
    /**
     * Inicializa os campos de busca nas tabelas e os botões realizam a busca.
     */
    private void inicializaEConfiguraBusca(){
        // Inicializa JTextFields
        tfdBuscaEqp = new JTextField();        
        tfdBuscaEqp.setColumns(10);
        
        // Inicializa JButtons
        btnBuscaEqp = new JButton(Janela.criarIcon("/res/icone/busca.png"));
        
        // Define o tamanho dos botões
        btnBuscaEqp.setPreferredSize(new Dimension(20,20));

        // Define os ActionListeners
        btnBuscaEqp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = tfdBuscaEqp.getText();
                if(texto.length() == 0) {
                    ordEqp.setRowFilter(null);
                } else {
                    try {
                        // Atribui o filtro nas linhas usando RowFilter
                        // (?i) torna a busca case insensitive
                        ordEqp.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
                    } catch(PatternSyntaxException ex) {
                        DialogoAviso.show(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        });
        
    }
    
    /**
     * Habilita ou desabilita a tabela. Permite a seleção de linhas e altera a
     * cor da fonte dependendo do seu estado.
     * 
     */
    public void habilitarTabela() {
        tabEquipe.setEnabled(true);
        tabEquipe.setRowSelectionAllowed(true);
        tabEquipe.setForeground(Color.BLACK);
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

