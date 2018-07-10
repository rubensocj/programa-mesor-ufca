package mesor.menu.painel.taxonomia;

import mesor.equipamento.Unidade;
import mesor.equipamento.Subunidade;
import mesor.equipamento.Parte;
import mesor.equipamento.Componente;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.sql.SQLException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import mesor.menu.DialogoAviso;

import mesor.sql.ModeloTabela;
import mesor.menu.DivisorSplitPane;
import mesor.menu.Janela;

/**
 * PainelEquipamento.java
 * 
 * @version 1.0 7/2/2017
 * @author Rubens Jr
 */
public class PainelEquipamento {

    private String selecao1;
    private String selecao2;
    private String selecao3;
    private String selecao4;
    
    public static final int TAB_UNIDADE = 0;
    public static final int TAB_SUBUNIDADE = 1;
    public static final int TAB_COMPONENTE = 2;
    public static final int TAB_PARTE = 3;
    public static final int TAB_NULL = 4;
    
    private boolean editavel;
    
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
    
    private ModeloTabela modelo;
    
    private final DefaultTableCellRenderer render;
    
    public Unidade unidade = new Unidade();
    public Subunidade subunidade = new Subunidade();
    public Componente componente = new Componente();
    public Parte parte = new Parte();
    
    private final JPanel pnlSub;
    private final JPanel pnlComp;
    private final JPanel pnlPte;
    
    private final JPanel pnlSelecao;
    private final JPanel pnlSelecionadosExcluir;
    private final JPanel pnlUniFinal;
    
    private JTextField tfdBuscaUni, tfdBuscaSub, tfdBuscaCmp, tfdBuscaPte;
    private JButton btnBuscaUni, btnBuscaSub, btnBuscaCmp, btnBuscaPte;
    
    private final JSplitPane sptPaneUniSub, sptPaneSubCmp, sptPaneCmpPte;
    private final DivisorSplitPane divUniSub, divSubCmp, divCmpPte;
    
    private TableRowSorter<TableModel> ordUni, ordSub, ordCmp, ordPte;
    
    private final JButton btnExcluir = new JButton("Excluir item");
    
    /**
     * Construtor.
     */
    public PainelEquipamento() {
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
        
        ModeloTabela modeloUni = new ModeloTabela("SELECT * FROM unidade;");
        ordUni = new TableRowSorter<>(modeloUni);
        tabUnidade = new JTable(modeloUni);
        tabUnidade.setRowSorter(ordUni);
        tabUnidade.setCellSelectionEnabled(false);
        atualizarAparenciaDaTabela(TAB_UNIDADE);
        tabSubunidade = new JTable(new ModeloTabela(
                    "SELECT * FROM subunidade;"));
        tabSubunidade.setCellSelectionEnabled(false);
        atualizarAparenciaDaTabela(TAB_SUBUNIDADE);
        tabComponente = new JTable(new ModeloTabela(
                    "SELECT * FROM componente;"));
        tabComponente.setCellSelectionEnabled(false);
        atualizarAparenciaDaTabela(TAB_COMPONENTE);
        tabParte = new JTable(new ModeloTabela(
                    "SELECT * FROM parte;"));
        tabParte.setCellSelectionEnabled(false);
        atualizarAparenciaDaTabela(TAB_PARTE);
        habilitarTabela(TAB_UNIDADE);
        
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
                    
                    tabSubunidade.setModel(new ModeloTabela(
                                "SELECT * FROM subunidade "
                                            + "WHERE id_unidade = "
                                            + String.valueOf(tabUnidade.getValueAt(
                                                        tabUnidade.getSelectedRow(), 0))));
                    atualizarAparenciaDaTabela(TAB_SUBUNIDADE);
                    habilitarTabela(TAB_SUBUNIDADE);
                        
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
                    
                    tabComponente.setModel(new ModeloTabela(
                                "SELECT * FROM componente "
                                            + "WHERE id_subunidade = "
                                            + String.valueOf(tabSubunidade.getValueAt(
                                                        tabSubunidade.getSelectedRow(), 0))));
                    atualizarAparenciaDaTabela(TAB_COMPONENTE);
                    habilitarTabela(TAB_COMPONENTE);
                        
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
                    
                    tabParte.setModel(new ModeloTabela(
                                "SELECT * FROM parte WHERE "
                                            + "id_componente = "
                                            + String.valueOf(tabComponente.getValueAt(
                                                        tabComponente.getSelectedRow(), 0))));
                    atualizarAparenciaDaTabela(TAB_PARTE);
                    habilitarTabela(TAB_PARTE);
                        
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
        Dimension dim = new Dimension(290, 90);
        
        // Unidade
        JScrollPane uPane = new JScrollPane(tabUnidade,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabUnidade.setFillsViewportHeight(true);
        tabUnidade.setPreferredScrollableViewportSize(dim);
        
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
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabParte.setFillsViewportHeight(true);
        tabParte.setPreferredScrollableViewportSize(dim);
        
        // Cria os paineis e organiza o painel maior.
        
        inicializaEConfiguraBusca();
        
        // Subunidade
        pnlSub = new JPanel(new BorderLayout(0,5));
        pnlSub.add(painelBusca(lblSub, tfdBuscaSub, btnBuscaSub), BorderLayout.PAGE_START);
        pnlSub.add(sPane, BorderLayout.CENTER);
        
        // Componente
        pnlComp = new JPanel(new BorderLayout(0,5));
        pnlComp.add(painelBusca(lblComp, tfdBuscaCmp, btnBuscaCmp), BorderLayout.PAGE_START);
        pnlComp.add(cPane, BorderLayout.CENTER);
        
        // Parte
        pnlPte = new JPanel(new BorderLayout(0,5));
        pnlPte.add(painelBusca(lblPte, tfdBuscaPte, btnBuscaPte), BorderLayout.PAGE_START);
        pnlPte.add(pPane, BorderLayout.CENTER);

        // Painel Unidade
        
        JPanel pnlUni = new JPanel(new BorderLayout(0,5));
        pnlUni.add(painelBusca(lblUni, tfdBuscaUni, btnBuscaUni), BorderLayout.NORTH);
        pnlUni.add(uPane, BorderLayout.CENTER);

        /**
         * Monta a combinação de 3 JSplitPanes
         *
         * 1º vertical (sptPaneCmpPte) para tabelas componente e parte
         * 2º vertical (sptPaneSubCmp) para tabela subunidade e sptPaneCmpPte
         * 3º horizontal (sptPaneUniSub) para a tabela unidade e sptPaneSubCmp
         */
        sptPaneCmpPte = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
                                   pnlComp, pnlPte);
        sptPaneCmpPte.setOpaque(true);
//        sptPaneCmpPte.addMouseListener(new DivisorSplitPaneListener(sptPaneCmpPte));
        sptPaneCmpPte.setOneTouchExpandable(true);
        sptPaneCmpPte.setDividerLocation(170);
        divCmpPte = new DivisorSplitPane(false, sptPaneCmpPte);
        
        sptPaneSubCmp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
                                   pnlSub, sptPaneCmpPte);
        sptPaneSubCmp.setOpaque(true);
        sptPaneSubCmp.setOneTouchExpandable(true);
        sptPaneSubCmp.setDividerLocation(170);        
        divSubCmp = new DivisorSplitPane(false, sptPaneSubCmp);
        
        sptPaneUniSub = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
                                   pnlUni, sptPaneSubCmp);
        sptPaneUniSub.setOpaque(true);
        sptPaneUniSub.setOneTouchExpandable(true);
        sptPaneUniSub.setDividerLocation(290);
        divUniSub = new DivisorSplitPane(true, sptPaneUniSub);

        // PainelEquipamento "Itens selecionados"
        pnlSelecao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSelecao.add(lblSelecionados);
        pnlSelecao.add(selecao);
        
        // Painel inferior, que mostra itens selecionados e, caso o painel
        // for editável, mostra opção de exluir itens
        pnlSelecionadosExcluir = new JPanel(new BorderLayout());
        pnlSelecionadosExcluir.add(pnlSelecao, BorderLayout.WEST);
        
        // PainelEquipamento final
        pnlUniFinal = new JPanel(new BorderLayout(5,0));
        pnlUniFinal.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createEtchedBorder(), "Equipamento"));
        pnlUniFinal.add(sptPaneUniSub, BorderLayout.CENTER);
        pnlUniFinal.add(pnlSelecionadosExcluir, BorderLayout.PAGE_END);
        pnlUniFinal.setOpaque(true);
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
                    try {
                        switch(tabelaSelecionada) {
                            case "unidade":
                                unidade.sqlExcluir();
                                reiniciarTabela(TAB_UNIDADE);
                                atualizarAparenciaDaTabela(TAB_UNIDADE);
                                break;
                            case "subunidade":
                                subunidade.sqlExcluir();
                                reiniciarTabela(TAB_SUBUNIDADE);
                                atualizarAparenciaDaTabela(TAB_SUBUNIDADE);
                                habilitarTabela(TAB_UNIDADE);
                                break;
                            case "componente":
                                componente.sqlExcluir();
                                reiniciarTabela(TAB_COMPONENTE);
                                atualizarAparenciaDaTabela(TAB_COMPONENTE);
                                habilitarTabela(TAB_SUBUNIDADE);
                                break;
                            case "parte":
                                parte.sqlExcluir();
                                reiniciarTabela(TAB_PARTE);
                                atualizarAparenciaDaTabela(TAB_PARTE);
                                habilitarTabela(TAB_COMPONENTE);
                                break;
                            default: break;
                        }
                    } catch(SQLException ex) {
                        DialogoAviso.show(ex.getMessage());
                        ex.printStackTrace();
                    }
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
        pnlSelecionadosExcluir.add(pnlBtnExcluir, BorderLayout.EAST);
    }
    
    /**
     * Reinicia a tabela através de uma consulta SQL {@code SELECT * FROM
     * demanda}.
     * 
     * @param tabela Inteiro com a tabela reiniciada {@code TAB_UNIDADE = 0,
     * TAB_SUBUNIDADE = 1, TAB_COMPONENTE = 2, TAB_PARTE = 3}.
     * @throws SQLException 
     */
    public void reiniciarTabela(int tabela) throws SQLException {
        switch(tabela) {
            case TAB_UNIDADE:
                modelo = (ModeloTabela) tabUnidade.getModel();
                modelo.setQuery("SELECT * FROM unidade");
                break;
            case TAB_SUBUNIDADE:
                modelo = (ModeloTabela) tabSubunidade.getModel();
                modelo.setQuery("SELECT * FROM subunidade");
                break;
            case TAB_COMPONENTE:
                modelo = (ModeloTabela) tabComponente.getModel();
                modelo.setQuery("SELECT * FROM componente");
                break;
            case TAB_PARTE:
                modelo = (ModeloTabela) tabParte.getModel();
                modelo.setQuery("SELECT * FROM parte");
                break;
            default: break;
        }
    }
    
    /**
     * Busca por unidade no sistema selecionado, através de uma cosulta SQL.
     * 
     * @param idSistema 
     * @throws SQLException 
     */
    public void buscarUnidade(int idSistema) throws SQLException {
        modelo = (ModeloTabela) tabUnidade.getModel();
        if(idSistema != 0) {
            modelo.setQuery("SELECT * FROM unidade WHERE id_sistema = " +
                    String.valueOf(idSistema));
        } else {
            modelo.setQuery("SELECT * FROM unidade");
        }
        
        habilitarTabela(TAB_UNIDADE);
        atualizarAparenciaDaTabela(TAB_UNIDADE);
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

                // Inicializa e define o ordenador das colunas
                // Ordenas as colunas em ordem crescente ou decrescente
                ordSub = new TableRowSorter<>(
                            tabSubunidade.getModel());
                tabSubunidade.setRowSorter(ordSub);
                
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

                // Inicializa e define o ordenador das colunas
                // Ordenas as colunas em ordem crescente ou decrescente
                ordCmp = new TableRowSorter<>(
                            tabComponente.getModel());
                tabComponente.setRowSorter(ordCmp);

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

                // Inicializa e define o ordenador das colunas
                // Ordenas as colunas em ordem crescente ou decrescente
                ordPte = new TableRowSorter<>(
                            tabParte.getModel());
                tabParte.setRowSorter(ordPte);

                break;
                
            default: break;
        }
    }
    
    /**
     * Habilita a tabela especificada, permitindo a seleção de linhas.
     * Desabilita todas as tabelas caso tabela == PainelEquipamento.TAB_NULL.
     * 
     * @param tabela 
     */
    public void habilitarTabela(int tabela) {
        switch(tabela) {
            
            case PainelEquipamento.TAB_UNIDADE: 
                
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
                
            case PainelEquipamento.TAB_SUBUNIDADE:
                
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
                
            case PainelEquipamento.TAB_COMPONENTE:
                
                tabComponente.setEnabled(true);                        
                tabComponente.setRowSelectionAllowed(true);
                tabComponente.setForeground(Color.BLACK);
                tabComponente.clearSelection();

                tabParte.setEnabled(false);                        
                tabParte.setRowSelectionAllowed(false);
                tabParte.setForeground(Color.LIGHT_GRAY);
                tabParte.clearSelection();
                
                break;
                
            case PainelEquipamento.TAB_PARTE:
                
                tabParte.setEnabled(true);                        
                tabParte.setRowSelectionAllowed(true);
                tabParte.setForeground(Color.BLACK);
                tabParte.clearSelection();
                
                break;
            
            case PainelEquipamento.TAB_NULL:
                
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
    
    /**
     * Inicializa os campos de busca nas tabelas e os botões realizam a busca.
     */
    private void inicializaEConfiguraBusca(){
        // Inicializa JTextFields
        tfdBuscaUni = new JTextField();
        tfdBuscaSub = new JTextField();
        tfdBuscaCmp = new JTextField();
        tfdBuscaPte = new JTextField();
        
        tfdBuscaUni.setColumns(10);
        tfdBuscaSub.setColumns(10);
        tfdBuscaCmp.setColumns(10);
        tfdBuscaPte.setColumns(10);
        
        // Inicializa JButtons
        btnBuscaUni = new JButton(Janela.criarIcon("/res/icone/busca.png"));
        btnBuscaSub = new JButton(Janela.criarIcon("/res/icone/busca.png"));
        btnBuscaCmp = new JButton(Janela.criarIcon("/res/icone/busca.png"));
        btnBuscaPte = new JButton(Janela.criarIcon("/res/icone/busca.png"));
        
        // Define o tamanho dos botões
        btnBuscaUni.setPreferredSize(new Dimension(20,20));
        btnBuscaSub.setPreferredSize(new Dimension(20,20));
        btnBuscaCmp.setPreferredSize(new Dimension(20,20));
        btnBuscaPte.setPreferredSize(new Dimension(20,20));
        
        // Define os ActionListeners
        btnBuscaUni.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = tfdBuscaUni.getText();
                if(texto.length() == 0) {
                    ordUni.setRowFilter(null);
                } else {
                    try {
                        // Atribui o filtro nas linhas usando RowFilter
                        // (?i) torna a busca case insensitive
                        ordUni.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
                    } catch(PatternSyntaxException ex) {
                        DialogoAviso.show(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        });
        
        btnBuscaSub.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = tfdBuscaSub.getText();
                if(texto.length() == 0) {
                    ordSub.setRowFilter(null);
                } else {
                    try {
                        // Atribui o filtro nas linhas usando RowFilter
                        // (?i) torna a busca case insensitive
                        ordSub.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
                    } catch(PatternSyntaxException ex) {
                        DialogoAviso.show(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        });
        
        btnBuscaCmp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = tfdBuscaCmp.getText();
                if(texto.length() == 0) {
                    ordCmp.setRowFilter(null);
                } else {
                    try {
                        // Atribui o filtro nas linhas usando RowFilter
                        // (?i) torna a busca case insensitive
                        ordCmp.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
                    } catch(PatternSyntaxException ex) {
                        DialogoAviso.show(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        });
        
        btnBuscaPte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = tfdBuscaPte.getText();
                if(texto.length() == 0) {
                    ordPte.setRowFilter(null);
                } else {
                    try {
                        // Atribui o filtro nas linhas usando RowFilter
                        // (?i) torna a busca case insensitive
                        ordPte.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
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
    private JPanel painelBusca(JLabel lbl, JTextField tfd, JButton btn) {
        JPanel pnlBusca1 = new JPanel(new FlowLayout());
        pnlBusca1.add(tfd);
        pnlBusca1.add(btn);
        
        JPanel pnlBusca = new JPanel(new BorderLayout());
        pnlBusca.add(pnlBusca1, BorderLayout.EAST);
        pnlBusca.add(lbl, BorderLayout.WEST);
        return pnlBusca;
    }
}