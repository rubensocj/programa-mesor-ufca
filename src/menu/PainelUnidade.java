package menu;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.sql.SQLException;

import conexaoSql.ModeloTabela;

import equipamento.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

/**
 * PainelUnidade.java
 * 
 * @version 1.0 7/2/2017
 * @author Rubens Jr
 */
public class PainelUnidade {

    private String selecao1;
    private String selecao2;
    private String selecao3;
    private String selecao4;
    
    public static final int TAB_UNIDADE = 0;
    public static final int TAB_SUBUNIDADE = 1;
    public static final int TAB_COMPONENTE = 2;
    public static final int TAB_PARTE = 3;
    public static final int TAB_NULL = 4;
    
    public static final int TAB_EDITAVEL = 0;
    public static final int TAB_NAO_EDITAVEL = 1;
    
    private int editavel;
    
    //private final JButton btnExcluir, btnAdicionar;
    
    public String tabelaSelecionada;
    public String idSelecionado;
                
    private JLabel lblSelecionados;
    private JLabel selecao;
    
    private final JLabel lblUni;
    private final JLabel lblSub;
    private final JLabel lblComp;
    private final JLabel lblPte;
        
    public JTable tabUnidade;
    public JTable tabSubunidade;
    public JTable tabComponente;
    public JTable tabParte;
    
    private final DefaultTableCellRenderer render;
    
    public Unidade unidade = new Unidade();
    public Subunidade subunidade = new Subunidade();
    public Componente componente = new Componente();
    public Parte parte = new Parte();
    
    private final JPanel pnlSub;
    private final JPanel pnlComp;
    private final JPanel pnlPte;
    private final JPanel pnlDivisoes;
    
    private final JPanel pnlSelecao;
    private final JPanel pnlUniFinal;
    
    private final JSplitPane splitPane;
    private final BasicSplitPaneDivider divisor;
    
    /**
     * Construtor.
     */
    public PainelUnidade() {
        lblUni = new JLabel(" Unidade: *");
        lblSub = new JLabel(" Subunidade:");
        lblComp = new JLabel(" Componente:");
        lblPte = new JLabel(" Parte:");
        
        selecao1 = "";
        selecao2 = "";
        selecao3 = "";
        selecao4 = "";
        
        tabelaSelecionada = "";
        
        lblSelecionados = new JLabel("Itens selecionados: ");
        selecao = new JLabel();
        
        /**
         * Renderizador de célula da tabela Demanda.
         */
        render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        
        // Cria as TABELAS com informações do banco de dados.
        try {
            // Unidade
            tabUnidade = new JTable(new ModeloTabela(
                        "SELECT * FROM unidade;"));
            
            tabUnidade.setCellSelectionEnabled(false);
            atualizarAparenciaDaTabela(TAB_UNIDADE);
            
            // Subunidade
            tabSubunidade = new JTable(new ModeloTabela(
                        "SELECT * FROM subunidade;"));
            
            tabSubunidade.setCellSelectionEnabled(false);
            atualizarAparenciaDaTabela(TAB_SUBUNIDADE);

            // Componente
            tabComponente = new JTable(new ModeloTabela(
                        "SELECT * FROM componente;"));
            
            tabComponente.setCellSelectionEnabled(false);
            atualizarAparenciaDaTabela(TAB_COMPONENTE);

            // Parte
            tabParte = new JTable(new ModeloTabela(
                        "SELECT * FROM parte;"));
            
            tabParte.setCellSelectionEnabled(false);
            atualizarAparenciaDaTabela(TAB_PARTE);
            
            habilitarTabela(TAB_UNIDADE);
        }
        catch(SQLException ex) { ex.printStackTrace();}
        
        // Adiciona os MOUSELISTENER às painelTabelas.
        tabUnidade.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(tabUnidade.getSelectedRowCount() == 1 &&
                                            tabUnidade.isEnabled()) {
                    tabelaSelecionada = "unidade";
                    
                    unidade.setIdBD((int) tabUnidade.getValueAt(
                                tabUnidade.getSelectedRow(), 0));
                    idSelecionado = String.valueOf(unidade.getIdBD());
                    
                    subunidade.setIdBD(0);
                    componente.setIdBD(0);
                    parte.setIdBD(0);
                    
                    try {
                        tabSubunidade.setModel(new ModeloTabela(
                            "SELECT * FROM subunidade "
                            + "WHERE id_unidade = "
                            + String.valueOf(tabUnidade.getValueAt(
                                        tabUnidade.getSelectedRow(), 0))));
                        
                        atualizarAparenciaDaTabela(TAB_SUBUNIDADE);
                        habilitarTabela(TAB_SUBUNIDADE);
                        
                    } catch(SQLException ex) { ex.getErrorCode();}
                        
                    selecao1 = String.valueOf(tabUnidade.getValueAt(
                                tabUnidade.getSelectedRow(), 1)) + " / ";
                    selecao.setText(selecao1);
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
        
        tabSubunidade.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(tabSubunidade.getSelectedRowCount() == 1 &&
                                        tabSubunidade.isEnabled()) {
                    tabelaSelecionada = "subunidade";
                    
                    subunidade.setIdBD((int) tabSubunidade.getValueAt(
                                        tabSubunidade.getSelectedRow(), 0));
                    idSelecionado = String.valueOf(subunidade.getIdBD());
                    
                    componente.setIdBD(0);
                    parte.setIdBD(0);
                    
                    try {
                        tabComponente.setModel(new ModeloTabela(
                            "SELECT * FROM componente "
                            + "WHERE id_subunidade = "
                            + String.valueOf(tabSubunidade.getValueAt(
                                        tabSubunidade.getSelectedRow(), 0))));
                        
                        atualizarAparenciaDaTabela(TAB_COMPONENTE);
                        habilitarTabela(TAB_COMPONENTE);
                        
                    } catch(SQLException ex) { ex.getErrorCode();}
                        
                    selecao2 = String.valueOf(tabSubunidade.getValueAt(
                                tabSubunidade.getSelectedRow(), 1)) + " / ";
                    selecao.setText(selecao1 + selecao2);
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
        
        tabComponente.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(tabComponente.getSelectedRowCount() == 1 && 
                                        tabComponente.isEnabled()) {
                    tabelaSelecionada = "componente";
                    
                    componente.setIdBD((int) tabComponente.getValueAt(
                                        tabComponente.getSelectedRow(), 0));
                    idSelecionado = String.valueOf(componente.getIdBD());
                    
                    parte.setIdBD(0);
                    
                    try {
                        tabParte.setModel(new ModeloTabela(
                            "SELECT * FROM parte WHERE "
                            + "id_componente = "
                            + String.valueOf(tabComponente.getValueAt(
                                        tabComponente.getSelectedRow(), 0))));
                        
                        atualizarAparenciaDaTabela(TAB_PARTE);
                        habilitarTabela(TAB_PARTE);
                        
                    } catch(SQLException ex) { ex.getErrorCode();}
                        
                    selecao3 = String.valueOf(tabComponente.getValueAt(
                                tabComponente.getSelectedRow(), 1)) + " / ";
                    selecao.setText(selecao1 + selecao2 + selecao3);
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
        
        tabParte.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(tabParte.getSelectedRowCount()==1 && tabParte.isEnabled()) {
                    tabelaSelecionada = "parte";
                    
                    parte.setIdBD((int) tabParte.getValueAt(
                                tabParte.getSelectedRow(), 0));
                    idSelecionado = String.valueOf(parte.getIdBD());
                    
                    selecao4 = String.valueOf(tabParte.getValueAt(
                                tabParte.getSelectedRow(), 1)) + " / ";
                    selecao.setText(selecao1 + selecao2 + selecao3 + selecao4);
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
        
        // Cria JSCROLLPANE e configura as TABELAS.
        Dimension dim = new Dimension(290, 100);
        
        // Unidade
        JScrollPane uPane = new JScrollPane(tabUnidade,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabUnidade.setFillsViewportHeight(true);
        tabUnidade.setPreferredScrollableViewportSize(new Dimension(290,300));
        
        // Subunidade
        JScrollPane sPane = new JScrollPane(tabSubunidade,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabSubunidade.setFillsViewportHeight(true);
        tabSubunidade.setPreferredScrollableViewportSize(dim);
        
        // Componente
        JScrollPane cPane = new JScrollPane(tabComponente,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabComponente.setFillsViewportHeight(true);
        tabComponente.setPreferredScrollableViewportSize(dim);
        
        // Parte
        JScrollPane pPane = new JScrollPane(tabParte,
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabParte.setFillsViewportHeight(true);
        tabParte.setPreferredScrollableViewportSize(dim);
        
        // Cria os paineis e organiza o painel maior.
        // Subunidade
        pnlSub = new JPanel(new BorderLayout(0,5));
        pnlSub.add(lblSub, BorderLayout.PAGE_START);
        pnlSub.add(sPane, BorderLayout.CENTER);
        
        // Componente
        pnlComp = new JPanel(new BorderLayout(0,5));
        pnlComp.add(lblComp, BorderLayout.PAGE_START);
        pnlComp.add(cPane, BorderLayout.CENTER);
        
        // Parte
        pnlPte = new JPanel(new BorderLayout(0,5));
        pnlPte.add(lblPte, BorderLayout.PAGE_START);
        pnlPte.add(pPane, BorderLayout.CENTER);

        // Painel divisões (subunidade, componente e parte)
        pnlDivisoes = new JPanel(new GridLayout(0,1));
        pnlDivisoes.add(pnlSub);
        pnlDivisoes.add(pnlComp);
        pnlDivisoes.add(pnlPte);
                
        // Painel Unidade
        JPanel pnlUni = new JPanel(new BorderLayout(0,5));
        pnlUni.add(lblUni, BorderLayout.NORTH);
        pnlUni.add(uPane, BorderLayout.CENTER);        
                        
        // SpliPanel horizontal Unidade / Subunidade, Componente, Parte
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                   pnlUni, pnlDivisoes);
        splitPane.setOpaque(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(290);
        
        /** 
         * Pega o divisor do JSplitPane como objeto
         * da classe BasicSplitPaneDivider, usando getComponent(index)
         */
        divisor = (BasicSplitPaneDivider) splitPane.getComponent(2);
        
        // Define a largura, em pixel, do divisor
        int largura = 5;
        divisor.setDividerSize(largura);
        
        // Define a nova cor da borda externa do divisor
        Color cor = new Color(172, 172, 172);
        
        /**
         * Define a nova borda do divisor do JSplitPane, 
         * com a cor e a largura especificadas
         */
        divisor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 1, cor),
                BorderFactory.createEmptyBorder(0, largura, 0, 0)));
                
        // PainelUnidade "Itens selecionados"
        pnlSelecao = new JPanel(new FlowLayout());
        pnlSelecao.add(lblSelecionados);
        pnlSelecao.add(selecao);
        
        // PainelUnidade final
        pnlUniFinal = new JPanel(new BorderLayout(10,0));
        pnlUniFinal.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createEtchedBorder(), "Equipamento"));
        pnlUniFinal.add(splitPane, BorderLayout.CENTER);
        pnlUniFinal.add(pnlSelecao, BorderLayout.PAGE_END);
        pnlUniFinal.setOpaque(true);
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /**
     * @deprecated 
     * @param e 
     */
    private void setEditavel(int e) {
        switch(e) {
            case TAB_EDITAVEL:
                
                break;
                
            case TAB_NAO_EDITAVEL:
                
                break;
                
            default: break;
        }
        
        editavel = e;
    }
    
    /**
     * 
     * @return Um JPanel com as tabelas.
     */
    public JPanel painelTabelas() {
        return pnlUniFinal;
    }
    
    /**
     * 
     * @return Um JLabel com os elementos selecionados nas tabelas.
     */
    public JLabel selecao() {
        return selecao;
    }
    
    /**
     * Atualiza parâmetros da tabela como renderizador das células e largura das
     * colunas.
     * 
     * @param tabela 
     */
    public void atualizarAparenciaDaTabela(int tabela) {
        switch(tabela) {
            case TAB_UNIDADE:

                tabUnidade.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                tabUnidade.getTableHeader().setReorderingAllowed(false);
                tabUnidade.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
                tabUnidade.getColumnModel().getColumn(0).setPreferredWidth(40);
                tabUnidade.getColumnModel().getColumn(1).setPreferredWidth(250);
                tabUnidade.getColumnModel().getColumn(2).setPreferredWidth(150);
                tabUnidade.getColumnModel().getColumn(3).setPreferredWidth(150);
                tabUnidade.getColumnModel().getColumn(4).setPreferredWidth(100);
                tabUnidade.getColumnModel().getColumn(5).setPreferredWidth(150);
                tabUnidade.getColumnModel().getColumn(6).setPreferredWidth(120);
                tabUnidade.getColumnModel().getColumn(7).setPreferredWidth(100);
                tabUnidade.getColumnModel().getColumn(8).setPreferredWidth(120);
                tabUnidade.getColumnModel().getColumn(9).setPreferredWidth(120);
                tabUnidade.getColumnModel().getColumn(10).setPreferredWidth(80);

                tabUnidade.getColumnModel().getColumn(0).setCellRenderer(render);
                tabUnidade.getColumnModel().getColumn(7).setCellRenderer(render);
                tabUnidade.getColumnModel().getColumn(9).setCellRenderer(render);
                tabUnidade.getColumnModel().getColumn(10).setCellRenderer(render);
                
                break;
                
            case TAB_SUBUNIDADE:

                tabSubunidade.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                tabSubunidade.getTableHeader().setReorderingAllowed(false);
                tabSubunidade.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                tabSubunidade.getColumnModel().getColumn(0).setPreferredWidth(40);
                tabSubunidade.getColumnModel().getColumn(1).setPreferredWidth(250);
                tabSubunidade.getColumnModel().getColumn(2).setPreferredWidth(80);

                tabSubunidade.getColumnModel().getColumn(0).setCellRenderer(render);
                tabSubunidade.getColumnModel().getColumn(2).setCellRenderer(render);

                break;
                
            case TAB_COMPONENTE:

                tabComponente.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                tabComponente.getTableHeader().setReorderingAllowed(false);
                tabComponente.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                tabComponente.getColumnModel().getColumn(0).setPreferredWidth(40);
                tabComponente.getColumnModel().getColumn(1).setPreferredWidth(250);
                tabComponente.getColumnModel().getColumn(2).setPreferredWidth(100);

                tabComponente.getColumnModel().getColumn(0).setCellRenderer(render);
                tabComponente.getColumnModel().getColumn(2).setCellRenderer(render);

                break;
                
            case TAB_PARTE:

                tabParte.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                tabParte.getTableHeader().setReorderingAllowed(false);
                tabParte.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                tabParte.getColumnModel().getColumn(0).setPreferredWidth(40);
                tabParte.getColumnModel().getColumn(1).setPreferredWidth(250);
                tabParte.getColumnModel().getColumn(2).setPreferredWidth(100);

                tabParte.getColumnModel().getColumn(0).setCellRenderer(render);
                tabParte.getColumnModel().getColumn(2).setCellRenderer(render);

                break;
                
            default: break;
        }
    }
    
    /**
     * Habilita a tabela especificada, permitindo a seleção de linhas.
     * Desabilita todas as tabelas caso tabela == PainelUnidade.TAB_NULL.
     * 
     * @param tabela 
     */
    public void habilitarTabela(int tabela) {
        switch(tabela) {
            
            case PainelUnidade.TAB_UNIDADE: 
                
                tabUnidade.setEnabled(true);
                tabUnidade.setRowSelectionAllowed(true);
                tabUnidade.setForeground(Color.BLACK);
                
                tabSubunidade.setEnabled(false);
                tabSubunidade.setRowSelectionAllowed(false);
                tabSubunidade.setForeground(Color.LIGHT_GRAY);

                tabComponente.setEnabled(false);
                tabComponente.setRowSelectionAllowed(false);
                tabComponente.setForeground(Color.LIGHT_GRAY);

                tabParte.setEnabled(false);
                tabParte.setRowSelectionAllowed(false);
                tabParte.setForeground(Color.LIGHT_GRAY);
                
                break;
                
            case PainelUnidade.TAB_SUBUNIDADE:
                
                tabSubunidade.setEnabled(true);
                tabSubunidade.setRowSelectionAllowed(true);
                tabSubunidade.setForeground(Color.BLACK);
                tabSubunidade.clearSelection();
                
                tabComponente.setEnabled(false);
                tabComponente.setRowSelectionAllowed(false);
                tabComponente.setForeground(Color.LIGHT_GRAY);
                tabComponente.clearSelection();
                
                tabParte.setEnabled(false);
                tabParte.setRowSelectionAllowed(false);
                tabParte.setForeground(Color.LIGHT_GRAY);
                tabParte.clearSelection();
                
                break;
                
            case PainelUnidade.TAB_COMPONENTE:
                
                tabComponente.setEnabled(true);                        
                tabComponente.setRowSelectionAllowed(true);
                tabComponente.setForeground(Color.BLACK);
                tabComponente.clearSelection();

                tabParte.setEnabled(false);                        
                tabParte.setRowSelectionAllowed(false);
                tabParte.setForeground(Color.LIGHT_GRAY);
                tabParte.clearSelection();
                
                break;
                
            case PainelUnidade.TAB_PARTE:
                
                tabParte.setEnabled(true);                        
                tabParte.setRowSelectionAllowed(true);
                tabParte.setForeground(Color.BLACK);
                tabParte.clearSelection();
                
                break;
            
            case PainelUnidade.TAB_NULL:
                
                tabUnidade.setEnabled(false);
                tabUnidade.setForeground(Color.LIGHT_GRAY);
                
                tabSubunidade.setEnabled(false);
                tabSubunidade.setForeground(Color.LIGHT_GRAY);

                tabComponente.setEnabled(false);
                tabComponente.setForeground(Color.LIGHT_GRAY);

                tabParte.setEnabled(false);
                tabParte.setForeground(Color.LIGHT_GRAY);
                
                break;
                
            default: break;
        }
    }
    
    /**
     * Limpa a seleção das tabelas.
     */
    public void limpaSelecao() {
        
        tabUnidade.clearSelection();
        tabSubunidade.clearSelection();
        tabComponente.clearSelection();
        tabParte.clearSelection();

        tabelaSelecionada = "";
        
        selecao.setText("");
    }
}