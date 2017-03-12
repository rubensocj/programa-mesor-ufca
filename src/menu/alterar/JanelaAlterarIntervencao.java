package menu.alterar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;

import javax.swing.*;

import java.io.File;
import java.io.IOException;

import java.sql.SQLException;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.GregorianCalendar;

import menu.PainelUnidade;
import menu.PainelIntervencao;
import menu.JanelaAdicionarAlterar;
import menu.adicionar.JanelaAdicionarIntervencao;

import conexaoSql.ModeloTabela;

/**
 * JanelaAlterarIntervencao.java
 * 
 * @version 1.0 3/2/2017
 * @author Rubens Jr
 */
public class JanelaAlterarIntervencao extends JanelaAdicionarIntervencao {
    
    private final PainelIntervencao pnlIntervencao;
    
    private JButton btnBuscarIntervencao;
    
    private final JPanel pnlTabelaUnidade, pnlTabelaIntervencao;
    
    /**
     * Construtor.
     */
    public JanelaAlterarIntervencao() {        
        /* Cria os botões antes de montar o painel Principal */
        criarBotoesOpcoes();
        criarBotoesAlterar();
        
        /**
         * Inicializa objeto da classe PainelUnidade e um JPanel que recebe o
         * painel com as tabelas dos equipamentos.
         */
        pnlTabelaUnidade = pnlUnidade.painelTabelas();
        
        /* Inicializa objeto da classe PainelIntervencao */
        pnlIntervencao = new PainelIntervencao();
        pnlIntervencao.tamanhoDaTabela(new Dimension(600,420));
        
        /* Painel com a tabela das intervenções */        
        pnlTabelaIntervencao = painelTabelaIntervencao();
    }
    
    // -------------------------------------------------------------------------
    // Métodos sobrepostos.
    // -------------------------------------------------------------------------
    
    @Override
    public void criarBotoesOpcoes() {
        btn1 = new JButton("OK");
        btn2 = new JButton("Ajuda");
        options = new Object[] {this.btn1, this.btn2};
        
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("opção AJUDA selecionada");
                JEditorPane p = new JEditorPane();
                p.setContentType("text/html");
                p.setEditable(false);
                File ajudaHTML = new File(
                            LOCAL + "\\ajuda\\janelaAdicionarEquipamento.html");
                try {
                    p.setPage(ajudaHTML.toURL());
                } catch (IOException ex) { ex.printStackTrace();
                }
                
                JScrollPane aPane = new JScrollPane(p,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                p.setPreferredSize(new Dimension(400,500));
                
                JDialog janelaAjuda = new JDialog(dialog);
                janelaAjuda.add(aPane);
                janelaAjuda.setTitle("Ajuda");
                janelaAjuda.pack();
                janelaAjuda.setLocationRelativeTo(null);
                janelaAjuda.setMinimumSize(new Dimension(400,500));
                
                janelaAjuda.setVisible(true);
            }
        });
    }
    
    @Override
    public void criarBotoesAlterar() {
        /**
         * Botão que busca as intervenções do equipamento selecionado.
         */
        btnBuscarIntervencao = new JButton("Buscar");
        btnBuscarIntervencao.addActionListener(new Buscar());
        btnBuscarIntervencao.setPreferredSize(new Dimension(75, 20));
        
        /**
         * Botão que limpa a busca, usado para selecionar uma intervenção sem um
         * equipamento selecionado. 
         */
        btnLimparBusca = new JButton("Limpar busca");
        btnLimparBusca.addActionListener(new Limpar());
        btnLimparBusca.setPreferredSize(new Dimension(115, 20));
        
        /**
         * Botão "Escolher" define o texto dos JTextFields de acordo com o
         * selecionado nas tabelas.
         */
        btnEscolher = new JButton();
        btnEscolher.addActionListener(new Escolher());
        btnEscolher.setPreferredSize(new Dimension(35, 25));
        btnEscolher.setIcon(
                    new ImageIcon(LOCAL + "\\icone\\seta_dupla_direita.png"));
        
        /**
         * Botão "Voltar" limpa o texto dos JTextFields e cancela a alteração.
         */
        btnVoltar = new JButton();
        btnVoltar.addActionListener(new Voltar());
        btnVoltar.setPreferredSize(new Dimension(35, 25));
        btnVoltar.setIcon(
                    new ImageIcon(LOCAL + "\\icone\\seta_dupla_esquerda.png"));
        
        /* Botão "Salvar alterações" executa a operação */
        btnSalvarAlteracao = new JButton("Salvar alterações");
        btnSalvarAlteracao.setPreferredSize(new Dimension(150,20));
        btnSalvarAlteracao.addActionListener(new Salvar());
    }
    
    @Override
    public JPanel montarPainelPrincipal() {
        /* Painel com os botões de escolher e voltar */
        JPanel pnlBtnAlterar = new JPanel(new BorderLayout(0,10));
        pnlBtnAlterar.add(btnEscolher, BorderLayout.NORTH);
        pnlBtnAlterar.add(btnVoltar, BorderLayout.CENTER);
        
        JPanel pnlSalvarAlteracao = new JPanel(new FlowLayout(2));
        pnlSalvarAlteracao.add(btnSalvarAlteracao);
        
        /**
         * Painel de alteração completo, que agrupa o painel de alterações da
         * intervenção e o painel com o botão que salva as alterações feitas.
         */
        JPanel pnlAlterarIntervencao = new JPanel(new BorderLayout());
        pnlAlterarIntervencao.add(painelIntervencao(), BorderLayout.NORTH);
        pnlAlterarIntervencao.add(pnlTabelaDemanda, BorderLayout.CENTER);
        pnlAlterarIntervencao.add(pnlSalvarAlteracao, BorderLayout.SOUTH);
        
        inicializarMapaComponentes();
        mapearComponentes();
        
        /* Desabilita a edição */
        habilitarEdicao(false);
        
        /**
         * Painel que agrupa o painel com os botões "Escolher" e "Voltar", e o
         * painel de alteração completo (com o botão).
         */
        pnlAlterar = new JPanel(new FlowLayout());
        pnlAlterar.add(pnlBtnAlterar);
        pnlAlterar.add(pnlAlterarIntervencao);
        
        /**
         * Painel final: painel com abas e tabelas no lado esquerdo e painel
         * de escolha e alteração completo à direita.
         */
        pnlPrincipal = new JPanel(new BorderLayout(3,0));
        try {
            pnlPrincipal.add(painelSistema(), BorderLayout.NORTH);
        } catch (SQLException ex) {ex.printStackTrace();}
        pnlPrincipal.add(painelAbas(), BorderLayout.WEST);
        pnlPrincipal.add(pnlAlterar, BorderLayout.EAST);
        return pnlPrincipal;
    }
    
    @Override
    public void mapearComponentes() {
        /*Adiciona os componentes ao mapa */
        mapaComponentes.put(0, lblCategoria);
        mapaComponentes.put(1, cbxCategoria);
        mapaComponentes.put(2, lblAtividade);
        mapaComponentes.put(3, cbxCategoria);
        mapaComponentes.put(4, lblDataInicio);
        mapaComponentes.put(5, tfdDataInicio);
        mapaComponentes.put(6, lblHoraInicio);
        mapaComponentes.put(7, horaInicio.getJComboBoxHora());
        mapaComponentes.put(8, horaInicio.getJComboBoxMinuto());
        mapaComponentes.put(9, ckboxTermino);
        mapaComponentes.put(10, pnlDemanda.tabDemanda);
        mapaComponentes.put(11, lblDataTermino);
        mapaComponentes.put(12, tfdDataTermino);
        mapaComponentes.put(13, lblHoraTermino);
        mapaComponentes.put(14, horaTermino.getJComboBoxHora());
        mapaComponentes.put(15, horaTermino.getJComboBoxMinuto());
    }
    
    @Override
    public void confirmar() {
        if(pnlIntervencao.tabIntervencao.getSelectedRowCount() != 0) {
            /**
             * Converte a data (dd/mm/aaaa) e a hora (hh:mm) nos JTextfields
             * para o formato SQL Timestamp:
             * 
             * DATA + HORA (campoFormatadoHora.getHora())
             * 
             * yyyy-MM-dd HH:mm:ss
             * 
             * em formato "String"
             */
            String dataInicioString;
            dataInicioString = tfdDataInicio.getText().substring(6,10).concat(
                    "-" + tfdDataInicio.getText().substring(3,5)).concat(
                    "-" + tfdDataInicio.getText().substring(0,2)) +
                    " " + horaInicio.getHora();
        
            String dataTerminoString;
            if(tfdDataTermino.getText().startsWith(" ")) {
                // Se não houver data e hora de término, dataTerminoString = ""
                dataTerminoString = "";
            } else {
                // Caso contrário, monta o "String".
                dataTerminoString = tfdDataTermino.getText().substring(
                    6,10).concat("-" + tfdDataTermino.getText().substring(
                    3,5)).concat("-" + tfdDataTermino.getText().substring(
                    0,2)) + " " + horaTermino.getHora();
            }

            intervencao.setCategoria(String.valueOf(
                                            cbxCategoria.getSelectedItem()));
            intervencao.setAtividade((String) cbxAtividade.getSelectedItem());
            intervencao.setInicio(dataInicioString);
            intervencao.setTermino(dataTerminoString);
            intervencao.setIdBD((int) 
                    pnlIntervencao.tabIntervencao.getValueAt(
                            pnlIntervencao.tabIntervencao.getSelectedRow(), 0));
            
            if(pnlDemanda.tabDemanda.getSelectedRowCount() != 0) {
                demanda.setIdBD((int) 
                    pnlDemanda.tabDemanda.getValueAt(
                                pnlDemanda.tabDemanda.getSelectedRow(), 0));
            }

            intervencao.setDemanda(demanda);
            
            intervencao.alteraIntervencao();
             
            limparTexto();
            
            /* Limpa a seleção de todas as tabelas */
            pnlUnidade.limpaSelecao();
            pnlIntervencao.tabIntervencao.clearSelection();
            
            pnlUnidade.habilitarTabela(PainelUnidade.TAB_UNIDADE);
            
            habilitarEdicao(false);
            
            ModeloTabela tb;
            tb = (ModeloTabela) pnlIntervencao.tabIntervencao.getModel();
            try {
                tb.setQuery("SELECT * FROM intervencao");
            } catch (SQLException ex) {ex.printStackTrace();}
            
            pnlIntervencao.atualizarAparenciaDaTabela();
            pnlIntervencao.habilitarTabela(true);

            try {
                pnlDemanda.reiniciarTabela();
            } catch (SQLException ex) {ex.printStackTrace();}

            pnlDemanda.atualizarAparenciaDaTabela();
            pnlDemanda.habilitarTabela(false);
            
            for(int i = 11; i <= 15; i++) {
                mapaComponentes.get(i).setEnabled(false);
            }
            ckboxTermino.setSelected(false);
        }
    }
    
    @Override
    public void habilitarEdicao(boolean escolha) {
        if(escolha == true) {
            for(int i = 0; i <= 10; i++) {
                mapaComponentes.get(i).setEnabled(true); // Habilita.
            }
        } else {
            for(int i = 0; i <= 10; i++) {
                mapaComponentes.get(i).setEnabled(false); // Desabilita.
            }
        }
        
        edicaoPermitida = escolha;
    }
    
    @Override
    public void limparTexto() {
        cbxCategoria.setSelectedIndex(0);
        tfdDataInicio.setText("");
        horaInicio.getJComboBoxHora().setSelectedIndex(0);
        horaInicio.getJComboBoxMinuto().setSelectedIndex(0);
        tfdDataTermino.setText("");
        horaTermino.getJComboBoxHora().setSelectedIndex(0);
        horaTermino.getJComboBoxMinuto().setSelectedIndex(0);
    }
    
    @Override
    public JTabbedPane painelAbas() {/**
         * Painel com abas. Uma com a tabela dos equipamentos e outra com a
         * tabela das intervenções.
         */
        tbdAbas = new JTabbedPane(JTabbedPane.TOP);
        
        tbdAbas.addTab("Selecionar equipamento",pnlTabelaUnidade);
        tbdAbas.addTab("Selecionar intervenção", pnlTabelaIntervencao);
        
        return tbdAbas;
    }
    
    @Override
    public JPanel painelDemanda() {
        /* Painel com a tabela das demandas */
        pnlDemanda.tamanhoDaTabela(new Dimension(300,250));
        pnlDemanda.habilitarTabela(false);
        pnlTabelaDemanda = pnlDemanda.painelTabelas();
        return pnlTabelaDemanda;
    }
    
    // -------------------------------------------------------------------------
    // Métodos privados.
    // -------------------------------------------------------------------------
    
    /**
     * Monta o painel exibido na aba "Selecionar intervenção" do JTabbedPane.
     * 
     * @return Um JPanel.
     */
    private JPanel painelTabelaIntervencao() {
        JPanel pnlBusca = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBusca.add(btnBuscarIntervencao);
        pnlBusca.add(btnLimparBusca);
        
        JPanel pnl1 = new JPanel(new BorderLayout());
        pnl1.add(pnlBusca, BorderLayout.PAGE_START);
        pnl1.add(pnlIntervencao.painelTabelas(), BorderLayout.CENTER);
        
        return pnl1;
    }
    
    // -------------------------------------------------------------------------
    // Classes.
    // -------------------------------------------------------------------------
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionBuscar
     */
    private class Buscar extends JanelaAdicionarAlterar.ActionBuscar {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Se nem uma unidade for selecionado, exibe JDialog com aviso.
            if(pnlUnidade.unidade.getIdBD() == 0 ||
                        pnlUnidade.tabelaSelecionada.equals("")) {
                
                JOptionPane dPane = new JOptionPane();
                dPane.setMessage("Selecione ao menos uma unidade.");
                dPane.setOptionType(JOptionPane.PLAIN_MESSAGE);
                dPane.setMessageType(JOptionPane.WARNING_MESSAGE);

                JDialog dDialog = dPane.createDialog(dPane, "Aviso");
                dDialog.pack();
                dDialog.setVisible(true);
                dDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            } else {
                /**
                 * Recebe o JTableModel como objeto da classe
                 * sql.ModeloTabelaSQL, de acordo com a tabela selecionada.
                 */
                ModeloTabela tb;
                
                /**
                 * Obtém o TableModel tabIntervencao como objeto da classe
                 * sql.ModeloTabelaSQL e define nova consulta com
                 * setQuery, atualizando a tabela.
                 */
                tb = (ModeloTabela) pnlIntervencao.tabIntervencao.getModel();
                
                /**
                 * Define um novo modelo para JTable intervenção com as
                 * intervenções do item selecionado: unidade, subunidade,
                 * componente ou parte.
                 */
                switch (pnlUnidade.tabelaSelecionada) {
                    case "unidade":
                        try {
                            tb.setQuery(
                            "SELECT intervencao.* FROM " + 
                            "intervencao, unidade " +
                            "WHERE intervencao.id_unidade = " +
                                String.valueOf(pnlUnidade.unidade.getIdBD()) +
                            " GROUP BY intervencao.id");
                        } catch (SQLException ex) {ex.printStackTrace();}
                        
                        break;
                    case "subunidade":
                        try {
                            tb.setQuery(
                            "SELECT intervencao.* FROM " + 
                            "intervencao, unidade, subunidade " +
                            "WHERE intervencao.id_unidade = " +
                                String.valueOf(pnlUnidade.unidade.getIdBD()) +
                            " AND intervencao.id_subunidade = " +
                                pnlUnidade.idSelecionado +
                            " GROUP BY intervencao.id");
                        } catch (SQLException ex) {ex.printStackTrace();}
                        
                        break;
                    case "componente":
                        try {
                            tb.setQuery(
                            "SELECT intervencao.* FROM " + 
                            "intervencao, unidade, subunidade, componente " +
                            "WHERE intervencao.id_unidade = " +
                                String.valueOf(pnlUnidade.unidade.getIdBD()) +
                            " AND intervencao.id_subunidade = " +
                                String.valueOf(pnlUnidade.subunidade.getIdBD())+
                            " AND intervencao.id_componente = " +
                                pnlUnidade.idSelecionado +
                            " GROUP BY intervencao.id");
                        } catch (SQLException ex) {ex.printStackTrace();}
                        
                        break;
                    case "parte":
                        try {
                            tb.setQuery(
                            "SELECT intervencao.* FROM " + 
                            "intervencao, unidade, subunidade, " +
                                                        "componente, parte " +
                            "WHERE intervencao.id_unidade = " +
                                String.valueOf(pnlUnidade.unidade.getIdBD()) +
                            " AND intervencao.id_subunidade = " +
                                String.valueOf(pnlUnidade.subunidade.getIdBD())+
                            " AND intervencao.id_componente = " +
                                String.valueOf(pnlUnidade.componente.getIdBD())+
                            " AND intervencao.id_parte = " +
                                pnlUnidade.idSelecionado +
                            " GROUP BY intervencao.id");
                        } catch (SQLException ex) {ex.printStackTrace();}
                        
                        break;
                        
                    default: break;
                } // Fim do Switch.

                pnlIntervencao.atualizarAparenciaDaTabela();
            } // Fim do if-else.
        }
    }
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionEscolher
     */
    private class Escolher extends JanelaAdicionarAlterar.ActionEscolher {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(pnlIntervencao.tabIntervencao.getSelectedRowCount() != 0){
                
                pnlDemanda.tabDemanda.clearSelection();
                ckboxTermino.setSelected(false);
                habilitarEdicao(true);
                limparTexto();
                
                /* Categoria da intervenção */
                if(pnlIntervencao.tabIntervencao.getValueAt(
                    pnlIntervencao.tabIntervencao.getSelectedRow(),2) != null) {
                    
                    String categoriaSQL = String.valueOf(
                        pnlIntervencao.tabIntervencao.getValueAt(
                            pnlIntervencao.tabIntervencao.getSelectedRow(),2));
                    
                    cbxCategoria.setSelectedItem(categoriaSQL);
                }
                
                /* Atividade da intervanção */
                if(pnlIntervencao.tabIntervencao.getValueAt(
                    pnlIntervencao.tabIntervencao.getSelectedRow(),3) != null) {
                    
                    String atividadeSQL = String.valueOf(
                        pnlIntervencao.tabIntervencao.getValueAt(
                            pnlIntervencao.tabIntervencao.getSelectedRow(),3));
                    
                    cbxAtividade.setSelectedItem(atividadeSQL);
                }
                
                /* Data de início da intervenção */
                if(pnlIntervencao.tabIntervencao.getValueAt(
                    pnlIntervencao.tabIntervencao.getSelectedRow(),4) == null) {
                    
                    tfdDataInicio.setText("");
                } else {
                    Date dataDate;
                    dataDate = (Date) pnlIntervencao.tabIntervencao.getValueAt(
                            pnlIntervencao.tabIntervencao.getSelectedRow(),4);
                    
                    SimpleDateFormat formatoData;
                    formatoData = new SimpleDateFormat("dd/MM/yyyy");
                    String dataAq = formatoData.format(dataDate);
                    tfdDataInicio.setText(dataAq);
                }
                
                /* Hora de início da intervenção */
                if(pnlIntervencao.tabIntervencao.getValueAt(
                    pnlIntervencao.tabIntervencao.getSelectedRow(),4) != null) {
                    
                    Date dataDate;
                    dataDate = (Date) pnlIntervencao.tabIntervencao.getValueAt(
                            pnlIntervencao.tabIntervencao.getSelectedRow(),4);
                    
                    Calendar calendario = new GregorianCalendar();
                    calendario.setTime(dataDate);
                    
                    horaInicio.getJComboBoxHora().setSelectedIndex(
                                (int) calendario.get(Calendar.HOUR_OF_DAY) + 1);
                    horaInicio.getJComboBoxMinuto().setSelectedIndex(
                                (int) calendario.get(Calendar.MINUTE) + 1);
                }
                
                /* Data de término da intervenção */
                if(pnlIntervencao.tabIntervencao.getValueAt(
                    pnlIntervencao.tabIntervencao.getSelectedRow(),5) != null) {
                    
                    ckboxTermino.setSelected(true);
                    for(int i = 11; i <= 15; i++) {
                        mapaComponentes.get(i).setEnabled(true);
                    }
                    
                    Date dataDate;
                    dataDate = (Date) pnlIntervencao.tabIntervencao.getValueAt(
                            pnlIntervencao.tabIntervencao.getSelectedRow(),5);
                    
                    SimpleDateFormat formatoData;
                    formatoData = new SimpleDateFormat("dd/MM/yyyy");
                    String dataAq = formatoData.format(dataDate);
                    tfdDataTermino.setText(dataAq);
                } else {
                    for(int i = 11; i <= 15; i++) {
                        mapaComponentes.get(i).setEnabled(false);
                    }
                }
                
                /* Hora de término da intervenção */
                if(pnlIntervencao.tabIntervencao.getValueAt(
                    pnlIntervencao.tabIntervencao.getSelectedRow(),5) != null) {
                    
                    Date dataDate;
                    dataDate = (Date) pnlIntervencao.tabIntervencao.getValueAt(
                            pnlIntervencao.tabIntervencao.getSelectedRow(),5);
                    
                    Calendar calendario = new GregorianCalendar();
                    calendario.setTime(dataDate);
                    
                    horaTermino.getJComboBoxHora().setSelectedIndex(
                                (int) calendario.get(Calendar.HOUR_OF_DAY) + 1);
                    horaTermino.getJComboBoxMinuto().setSelectedIndex(
                                (int) calendario.get(Calendar.MINUTE) + 1);
                }
                
                if(pnlIntervencao.tabIntervencao.getValueAt(
                    pnlIntervencao.tabIntervencao.getSelectedRow(),10) != null){
                    
                    pnlDemanda.habilitarTabela(true);
                    
                    Object idUni;
                    idUni = pnlIntervencao.tabIntervencao.getValueAt(
                            pnlIntervencao.tabIntervencao.getSelectedRow(), 6);

                    Object idSub;
                    idSub = pnlIntervencao.tabIntervencao.getValueAt(
                            pnlIntervencao.tabIntervencao.getSelectedRow(), 7);

                    Object idCom;
                    idCom = pnlIntervencao.tabIntervencao.getValueAt(
                            pnlIntervencao.tabIntervencao.getSelectedRow(), 8);

                    Object idPte;
                    idPte = pnlIntervencao.tabIntervencao.getValueAt(
                            pnlIntervencao.tabIntervencao.getSelectedRow(), 9);
                    
                    /* Atualiza a tabela de demandas */
                    try {
                        pnlDemanda.buscarDemanda(idUni, idSub, idCom, idPte);
                    } catch (SQLException ex) {ex.printStackTrace();}
                    
                    /**
                     * Na tabela de demandas, seleciona a demanda relacionada a
                     * intervenção escolhida para ser alterada.
                     */
                    Object idDem;
                    idDem = pnlIntervencao.tabIntervencao.getValueAt(
                            pnlIntervencao.tabIntervencao.getSelectedRow(), 10);
                    
                    int numLinha = pnlDemanda.tabDemanda.getRowCount();
                    int linha = 0;
                    if(idDem != null && numLinha != 0) {
                        int i;
                        for(i = 0; i <= numLinha; i++) {
                            if(pnlDemanda.tabDemanda.getValueAt(i,0) == idDem) {
                                linha = i;
                                break;
                            }
                        }
                    }

                    pnlDemanda.atualizarAparenciaDaTabela();
                        
                    pnlDemanda.tabDemanda.setRowSelectionInterval(linha, linha);
                    pnlDemanda.tabDemanda.setColumnSelectionInterval(
                                0, pnlDemanda.tabDemanda.getColumnCount() - 1);
                } else {
                    
                    try {
                        pnlDemanda.reiniciarTabela();
                    } catch (SQLException ex) {ex.printStackTrace();}
                    
                    pnlDemanda.atualizarAparenciaDaTabela();
                    pnlDemanda.habilitarTabela(false);
                }
                
                pnlUnidade.habilitarTabela(PainelUnidade.TAB_NULL);
                pnlIntervencao.habilitarTabela(false);
            }
        }
    }
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionLimpar
     */
    private class Limpar extends JanelaAdicionarAlterar.ActionLimpar {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Se nem uma unidade for selecionado, exibe JDialog com aviso.
            if(!pnlUnidade.tabelaSelecionada.equals("")||
                        pnlUnidade.tabelaSelecionada.equals("")) {
                
                ModeloTabela tb;
                tb = (ModeloTabela) pnlIntervencao.tabIntervencao.getModel();
                try {
                    tb.setQuery("SELECT * FROM intervencao");
                } catch (SQLException ex) {ex.printStackTrace();}
                
                pnlIntervencao.atualizarAparenciaDaTabela();
                pnlUnidade.limpaSelecao();
            }
        }
    }
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionSalvar
     */
    private class Salvar extends JanelaAdicionarAlterar.ActionSalvar {
        @Override
        public void actionPerformed(ActionEvent e) {
            String mensagemErro = "";
            if(edicaoPermitida == true) {
                if(cbxCategoria.getSelectedItem().equals("Selecionar...")) {
                    mensagemErro = "Selecione uma categoria.";
                } else if(cbxAtividade.getSelectedItem().equals("Selecionar...")) {
                    mensagemErro = "Selecione uma atividade.";
                } else if(tfdDataInicio.getText().contains(" ")) {
                    mensagemErro = "Informe uma data de início válida.";
                } else if(horaInicio.eValido() == false) {
                    mensagemErro = "Informe uma hora de início válida.";
                } else if(ckboxTermino.isSelected() &&
                            (tfdDataTermino.getText().contains(" ") || 
                                horaTermino.eValido() == false)) {
                    mensagemErro = "Caso informado, o término da intervenção " +
                                "deve ser composto por data e hora.";
                }
                if(mensagemErro.isEmpty()) {
                    System.out.println("opção CONFIRMAR selecionada");
                    confirmar();
                } else {
                    JOptionPane mPane = new JOptionPane();
                    mPane.setMessage(mensagemErro);
                    mPane.setOptionType(JOptionPane.PLAIN_MESSAGE);
                    mPane.setMessageType(JOptionPane.WARNING_MESSAGE);

                    JDialog mDialog = mPane.createDialog(mPane, "Aviso");
                    mDialog.pack();
                    mDialog.setVisible(true);
                    mDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                }
            }
        }
    }

    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionVoltar
     */
    private class Voltar extends JanelaAdicionarAlterar.ActionVoltar {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!pnlUnidade.tabelaSelecionada.equals("")
                || pnlIntervencao.tabIntervencao.getSelectedRowCount() != 0) {
                
                pnlUnidade.habilitarTabela(PainelUnidade.TAB_UNIDADE);
                limparTexto();
                habilitarEdicao(false);
                
                for(int i = 11; i <= 15; i++) {
                    mapaComponentes.get(i).setEnabled(false);
                }
                ckboxTermino.setSelected(false);

                try {
                    pnlDemanda.reiniciarTabela();
                } catch (SQLException ex) {ex.printStackTrace();}

                pnlDemanda.tabDemanda.clearSelection();
                pnlDemanda.atualizarAparenciaDaTabela();
                pnlDemanda.habilitarTabela(false);
               
                pnlIntervencao.habilitarTabela(true);
            }
        }
    }
}