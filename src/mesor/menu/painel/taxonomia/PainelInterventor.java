package mesor.menu.painel.taxonomia;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import mesor.intervencao.Interventor;
import mesor.menu.DialogoAviso;

import mesor.sql.ModeloTabela;
import mesor.menu.Janela;

/**
 * PainelEquipamento.java
 * 
 * @version 1.0 7/2/2017
 * @author Rubens Jr
 */
public class PainelInterventor {

    private String selecao1;
    
    public static final int TAB_UNIDADE = 0;
    public static final int TAB_SUBUNIDADE = 1;
    public static final int TAB_COMPONENTE = 2;
    public static final int TAB_PARTE = 3;
    public static final int TAB_NULL = 4;
    
    public String tabelaSelecionada;
    public String idSelecionado;
                
    
    private final JLabel lblIntv;
        
    public JTable tabInterventor;
    public JTable tabSubunidade;
    public JTable tabComponente;
    public JTable tabParte;
    
    private ModeloTabela modelo;
    
    private final DefaultTableCellRenderer render;
    
    public Interventor interventor = new Interventor();
    
    private final JPanel pnlIntvFinal;
    
    private JTextField tfdBuscaIntv;
    private JButton btnBuscaIntv;
    
    private TableRowSorter<TableModel> ordIntv;
    
    private final JButton btnExcluir = new JButton("Excluir item");
    
    /**
     * Construtor.
     * @param x
     * @param y
     */
    public PainelInterventor(int x, int y) {
        
        lblIntv = new JLabel(" Interventores:");
        
        /**
         * Renderizador de célula da tabela Demanda.
         */
        render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        
        ModeloTabela modeloIntv = new ModeloTabela("SELECT * FROM interventor;");
        ordIntv = new TableRowSorter<>(modeloIntv);
        tabInterventor = new JTable(modeloIntv);
        tabInterventor.setRowSorter(ordIntv);
        tabInterventor.setCellSelectionEnabled(false);
        atualizarAparenciaDaTabela();
        habilitarTabela(true);
        
        // Adiciona os MOUSELISTENER às painelTabelas.
        tabInterventor.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(tabInterventor.getSelectedRowCount() == 1 &&
                                            tabInterventor.isEnabled()) {
                                            
                    selecao1 = String.valueOf(
                            tabInterventor.getValueAt(
                                    tabInterventor.getSelectedRow(), 1));
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}});

        // Interventor
        JScrollPane iPane = new JScrollPane(tabInterventor,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabInterventor.setFillsViewportHeight(true);
        tabInterventor.setPreferredScrollableViewportSize(new Dimension(x,y));
        
        inicializaEConfiguraBusca();
        
        // Painel Interventor        
        JPanel pnlIntv = new JPanel(new BorderLayout(0,5));
        pnlIntv.add(painelBusca(lblIntv, tfdBuscaIntv, btnBuscaIntv), BorderLayout.NORTH);
        pnlIntv.add(iPane, BorderLayout.CENTER);
        
        // PainelInterventor final
        pnlIntvFinal = new JPanel(new BorderLayout(10,0));
        pnlIntvFinal.add(pnlIntv, BorderLayout.CENTER);
        pnlIntvFinal.setOpaque(true);
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /**
     */
    public void setEditavel() {
        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!tabelaSelecionada.equals("")) {
                    interventor.sqlExcluir();
                    reiniciarTabela();
                    atualizarAparenciaDaTabela();
                } else {
                    // Bip do mouse ao clicar no botão
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        
        // Adiciona o botão "Excluir" ao painel de selecionados, no fim do
        // painel principal.
        JPanel pnlBtnExcluir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtnExcluir.add(btnExcluir);
    }
    
    /**
     * Reinicia a tabela através de uma consulta SQL {@code SELECT * FROM
     * demanda}.
     * 
     */
    public void reiniciarTabela() {
        try {
            modelo = (ModeloTabela) tabInterventor.getModel();
            modelo.setQuery("SELECT * FROM interventor");
        } catch (SQLException e) {
            DialogoAviso.show("Erro ao atualizar tabela. " + e.getMessage());
        }
    }
    
    /**
     * Busca por interventor no sistema selecionado, através de uma cosulta SQL.
     * 
     * @param idSistema 
     * @throws SQLException 
     */
    public void buscarUnidade(int idSistema) throws SQLException {
        modelo = (ModeloTabela) tabInterventor.getModel();
        if(idSistema != 0) {
            modelo.setQuery("SELECT * FROM unidade WHERE id_sistema = " +
                    String.valueOf(idSistema));
        } else {
            modelo.setQuery("SELECT * FROM unidade");
        }
        
        habilitarTabela(true);
        atualizarAparenciaDaTabela();
    }
    
    /**
     * 
     * @return Um JPanel com as tabelas.
     */
    public JPanel painelTabelas() {
        return pnlIntvFinal;
    }
    
    /**
     * 
     * @param t
     * @return Um JPanel com as tabelas.
     */
    public JPanel painelTabelas(boolean t) {
        if(t == true) {
            tabInterventor.setSelectionMode(
                        ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        }
        return pnlIntvFinal;
    }
    
    /**
     * Atualiza parâmetros da tabela como renderizador das células e largura das
     * colunas.
     * 
     */
    public void atualizarAparenciaDaTabela() {
        tabInterventor.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabInterventor.getTableHeader().setReorderingAllowed(false);
        tabInterventor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabInterventor.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabInterventor.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabInterventor.getColumnModel().getColumn(2).setPreferredWidth(80);
        tabInterventor.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabInterventor.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabInterventor.getColumnModel().getColumn(5).setPreferredWidth(200);
        tabInterventor.getColumnModel().getColumn(6).setPreferredWidth(200);
        tabInterventor.getColumnModel().getColumn(7).setPreferredWidth(200);
        tabInterventor.getColumnModel().getColumn(8).setPreferredWidth(100);
        tabInterventor.getColumnModel().getColumn(9).setPreferredWidth(150);
        tabInterventor.getColumnModel().getColumn(10).setPreferredWidth(250);
        tabInterventor.getColumnModel().getColumn(11).setPreferredWidth(250);
        tabInterventor.getColumnModel().getColumn(12).setPreferredWidth(60);
        tabInterventor.getColumnModel().getColumn(13).setPreferredWidth(100);

        tabInterventor.getColumnModel().getColumn(0).setCellRenderer(render);
        tabInterventor.getColumnModel().getColumn(2).setCellRenderer(render);
        tabInterventor.getColumnModel().getColumn(3).setCellRenderer(render);
        tabInterventor.getColumnModel().getColumn(4).setCellRenderer(render);
        tabInterventor.getColumnModel().getColumn(8).setCellRenderer(render);
        tabInterventor.getColumnModel().getColumn(9).setCellRenderer(render);
        tabInterventor.getColumnModel().getColumn(12).setCellRenderer(render);
        tabInterventor.getColumnModel().getColumn(13).setCellRenderer(render);
    }
    
    /**
     * Habilita a tabela especificada, permitindo a seleção de linhas.
     * Desabilita todas as tabelas caso tabela == PainelEquipamento.TAB_NULL.
     * 
     * @param e
     */
    public void habilitarTabela(boolean e) {
        if(e == true) {
            tabInterventor.setEnabled(true);
            tabInterventor.setRowSelectionAllowed(true);
            tabInterventor.setForeground(Color.BLACK);
        } else {
            tabInterventor.setEnabled(false);
            tabInterventor.setRowSelectionAllowed(false);
            tabInterventor.setForeground(Color.LIGHT_GRAY);
        }
    }
    
    /**
     * Limpa a seleção das tabelas.
     */
    public void limpaSelecao() {
        tabInterventor.clearSelection();
        tabelaSelecionada = "";
    }
    
    /**
     * Inicializa os campos de busca nas tabelas e os botões realizam a busca.
     */
    private void inicializaEConfiguraBusca(){
        // Inicializa JTextFields
        tfdBuscaIntv = new JTextField();
        
        tfdBuscaIntv.setColumns(10);
        
        // Inicializa JButtons
        btnBuscaIntv = new JButton(Janela.criarIcon("/res/icone/busca.png"));
        
        // Define o tamanho dos botões
        btnBuscaIntv.setPreferredSize(new Dimension(20,20));

        // Define os ActionListeners
        btnBuscaIntv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = tfdBuscaIntv.getText();
                if(texto.length() == 0) {
                    ordIntv.setRowFilter(null);
                } else {
                    try {
                        // Atribui o filtro nas linhas usando RowFilter
                        // (?i) torna a busca case insensitive
                        ordIntv.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
                    } catch(PatternSyntaxException ex) {
                        DialogoAviso.show(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        });
        
    }
    
    /**
     * 
     * @param lbl
     * @param tfd
     * @param btn
     * @return 
     */
    public static JPanel painelBusca(JLabel lbl, JTextField tfd, JButton btn) {
        JPanel pnlBusca1 = new JPanel(new FlowLayout());
        pnlBusca1.add(tfd);
        pnlBusca1.add(btn);
        
        JPanel pnlBusca = new JPanel(new BorderLayout(5,0));
        pnlBusca.add(pnlBusca1, BorderLayout.EAST);
        pnlBusca.add(lbl, BorderLayout.WEST);
        return pnlBusca;
    }
}