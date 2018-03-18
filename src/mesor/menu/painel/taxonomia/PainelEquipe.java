package mesor.menu.painel.taxonomia;

import java.awt.BorderLayout;
import mesor.sql.ModeloTabela;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.SQLException;
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
import mesor.menu.Janela;

/**
 * PainelEquipe.java
 *
 * @version 1.0 19/02/2017
 * @author Rubens Jr
 */
public class PainelEquipe {

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
    private Tabela tipoDeTabela;
    
    public PainelEquipe(int x, int y) {
        
        lblEqp = new JLabel(" Equipes:");
        
        /**
         * Renderizador de célula da tabela Demanda.
         */
        render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        
        // Cria as TABELAS com informações do banco de dados.
        try {
            // Cria o modelo da tabela pela classe ModeloTabela
            ModeloTabela modeloEqp = new ModeloTabela(
                        "SELECT id, objetivo_geral FROM equipe;");
            
            // Cria objeto da classe TableRowSorter, para ordenar os valores
            // das linhas com o clique no cabeçalho da coluna
            ordEqp = new TableRowSorter<>(modeloEqp);

            // Cria a tabela Interventor
            tabEquipe = new JTable(modeloEqp);
            
            // Define o ordenador para esta coluna
            tabEquipe.setRowSorter(ordEqp);
            
            tabEquipe.setCellSelectionEnabled(false);
            atualizarAparenciaDaTabela();
            
            habilitarTabela();
        }
        catch(SQLException ex) { ex.printStackTrace();}
        
        // Interventor
        ePane = new JScrollPane(tabEquipe,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabEquipe.setFillsViewportHeight(true);
        tabEquipe.setPreferredScrollableViewportSize(new Dimension(x,y));
        
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
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
        return pnlEqpFinal;
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
        
//        switch(this.tipoDeTabela) {
//            case equipe:
                tabEquipe.getColumnModel().getColumn(0).setPreferredWidth(40);
                tabEquipe.getColumnModel().getColumn(1).setPreferredWidth(273);

                tabEquipe.getColumnModel().getColumn(0).setCellRenderer(render);
                
//                break;
//                
//            case interventor:
//                tabEquipe.getColumnModel().getColumn(0).setPreferredWidth(40);
//                tabEquipe.getColumnModel().getColumn(1).setPreferredWidth(70);
//                tabEquipe.getColumnModel().getColumn(2).setPreferredWidth(280);
//                tabEquipe.getColumnModel().getColumn(3).setPreferredWidth(70);
//                tabEquipe.getColumnModel().getColumn(4).setPreferredWidth(100);
//                tabEquipe.getColumnModel().getColumn(5).setPreferredWidth(100);
//                tabEquipe.getColumnModel().getColumn(6).setPreferredWidth(180);
//                tabEquipe.getColumnModel().getColumn(7).setPreferredWidth(180);
//                tabEquipe.getColumnModel().getColumn(8).setPreferredWidth(90);
//                tabEquipe.getColumnModel().getColumn(9).setPreferredWidth(90);
//                tabEquipe.getColumnModel().getColumn(10).setPreferredWidth(280);
//                tabEquipe.getColumnModel().getColumn(11).setPreferredWidth(150);
//                
//                tabEquipe.getColumnModel().getColumn(0).setCellRenderer(render);
//                tabEquipe.getColumnModel().getColumn(1).setCellRenderer(render);
//                tabEquipe.getColumnModel().getColumn(4).setCellRenderer(render);
//                tabEquipe.getColumnModel().getColumn(5).setCellRenderer(render);
//                break;
//                
//            default:break;
//        }
    }
    
    /**
     * Inicializa os campos de busca nas tabelas e os botões realizam a busca.
     */
    private void inicializaEConfiguraBusca(){
        // Inicializa JTextFields
        tfdBuscaEqp = new JTextField();        
        tfdBuscaEqp.setColumns(10);
        
        // Inicializa JButtons
        btnBuscaEqp = new JButton(new ImageIcon(
            Janela.LOCAL + "\\icone\\busca.png"));
        
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

