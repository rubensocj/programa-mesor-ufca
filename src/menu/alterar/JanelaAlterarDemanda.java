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

import menu.PainelEquipamento;
import menu.PainelDemanda;
import menu.JanelaAdicionarAlterar;
import menu.adicionar.JanelaAdicionarDemanda;

import conexaoSql.ModeloTabela;

/**
 * JanelaAlterarDemanda.java
 * 
 * @version 1.0 3/2/2017
 * @author Rubens Jr
 */
public class JanelaAlterarDemanda extends JanelaAdicionarDemanda {
    
    private final PainelDemanda pnlDemanda;
    
    private final JPanel pnlTabelaUnidade, pnlTabelaDemanda;
    
    /**
     * Construtor.
     */
    public JanelaAlterarDemanda() {
        
        /* Cria os botões antes de montar o painel Principal */
        criarBotoesOpcoes();
        criarBotoesAlterar();
        
        /**
         * Inicializa objeto da classe PainelUnidade e um JPanel que recebe o
         * painel com as tabelas dos equipamentos.
         */
        pnlTabelaUnidade = pnlUnidade.painelTabelas();
        
        /* Inicializa objeto da classe PainelDemanda */
        pnlDemanda = new PainelDemanda();
        pnlDemanda.tamanhoDaTabela(new Dimension(600,420));
        
        /* Painel com a tabela das demandas */
        pnlTabelaDemanda = painelTabelaDemanda();
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
        /* Botão que busca as demandas do equipamento selecionado */
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(new Buscar());
        btnBuscar.setPreferredSize(new Dimension(75, 20));
        
        /**
         * Botão que limpa a busca, usado para selecionar uma demanda sem um
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
         * demanda e o painel com o botão que salva as alterações feitas.
         */
        JPanel pnlAlterarDemanda = new JPanel(new BorderLayout());
        pnlAlterarDemanda.add(painelDemanda(), BorderLayout.CENTER);
        pnlAlterarDemanda.add(pnlSalvarAlteracao, BorderLayout.SOUTH);
        
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
        pnlAlterar.add(pnlAlterarDemanda);
        
        /**
         * Painel principal: painel com abas e tabelas no lado esquerdo e painel
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
        /* Adiciona os componentes ao mapa */
        mapaComponentes.put(0, lblData);
        mapaComponentes.put(1, tfdData);
        mapaComponentes.put(2, lblHora);
        mapaComponentes.put(3, horaFormatada.getJComboBoxHora());
        mapaComponentes.put(4, horaFormatada.getJComboBoxMinuto());
        mapaComponentes.put(5, modo1);
        mapaComponentes.put(6, modo2);
        mapaComponentes.put(7, modo3);
        mapaComponentes.put(8, lblImpacto);
        mapaComponentes.put(9, cbxImpacto);
        mapaComponentes.put(10, lblCondicaoOperacao);
        mapaComponentes.put(11, cbxCondicaoOperacao);
        mapaComponentes.put(12, cbxCausaCategoria);
    }
    
    @Override
    public void confirmar() {
        if(pnlDemanda.tabDemanda.getSelectedRowCount() != 0) {
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
            // Data da demanda
            String dataString = tfdData.getText().substring(6,10).concat("-" + 
                        tfdData.getText().substring(3,5)).concat("-" + 
                        tfdData.getText().substring(0,2)) + " " +
                        horaFormatada.getHora();
            
            // Executa a operação
            demanda.setData(dataString);
            demanda.setModo(modo);
            demanda.setImpacto(impacto);
            demanda.setCausa(causa);
            demanda.setModoOperacional(condicao);
            demanda.setIdBD((int) pnlDemanda.tabDemanda.getValueAt(
                        pnlDemanda.tabDemanda.getSelectedRow(), 0));

            demanda.alteraDemanda();
            
            /**
             * Limpa o texto dos JTextField ou JFormattedTextFields, e as
             * seleções das JComboBoxes e JCheckBoxes usados para a alteração.
             */     
            limparTexto();
            
            /* Limpa a seleção de todas as tabelas */
            pnlUnidade.limpaSelecao();
            pnlDemanda.tabDemanda.clearSelection();
            
            habilitarEdicao(false);
            
            /**
            * Recebe o JTableModel como objeto da classe
            * sql.ModeloTabelaSQL, de acordo com a tabela selecionada.
            */
            ModeloTabela tb;

            /**
             * Obtém o TableModel tabDemanda como objeto da classe
             * sql.ModeloTabelaSQL e define nova consulta com
             * setQuery, atualizando a tabela.
             */
            tb = (ModeloTabela) pnlDemanda.tabDemanda.getModel();
            
            /* Atualiza a tabela de demandas */
            try {
                tb.setQuery("SELECT * FROM demanda");
            } catch (SQLException ex) {ex.printStackTrace();}
            
            pnlDemanda.habilitarTabela(true);
            pnlDemanda.atualizarAparenciaDaTabela();
            pnlUnidade.habilitarTabela(PainelEquipamento.TAB_UNIDADE);
        }
    }
    
    @Override
    public void habilitarEdicao(boolean escolha) {
        if(escolha == true) {
            for(int i = 0; i <= 12; i++) {
                mapaComponentes.get(i).setEnabled(true); // Habilita.
            }
        } else {
            for(int i = 0; i <= 12; i++) {
                mapaComponentes.get(i).setEnabled(false); // Desabilita.
            }
        }
        
        edicaoPermitida = escolha;
    }
    
    @Override
    public void limparTexto() {
        tfdData.setText("");
        horaFormatada.getJComboBoxHora().setSelectedIndex(0);
        horaFormatada.getJComboBoxMinuto().setSelectedIndex(0);
        modo1.setSelected(false);
        modo2.setSelected(false);
        modo3.setSelected(false);
        cbxImpacto.setSelectedIndex(0);
        cbxCondicaoOperacao.setSelectedIndex(0);
        cbxCausaCategoria.setSelectedIndex(0);
    }
    
    @Override
    public JTabbedPane painelAbas() {
        /**
         * Painel com abas. Uma com a tabela dos equipamentos e outra com a
         * tabela das demandas.
         */
        tbdAbas = new JTabbedPane(JTabbedPane.TOP);
        
        tbdAbas.addTab("Selecionar equipamento", pnlTabelaUnidade);
        tbdAbas.addTab("Selecionar demanda", pnlTabelaDemanda);
        
        return tbdAbas;
    }
    
    // -------------------------------------------------------------------------
    // Métodos privados.
    // -------------------------------------------------------------------------
    
    /**
     * Monta o painel exibido na aba "Selecionar demanda" do JTabbedPane.
     * 
     * @return Um JPanel.
     */
    private JPanel painelTabelaDemanda() {
        JPanel pnlBusca = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBusca.add(btnBuscar);
        pnlBusca.add(btnLimparBusca);
        
        JPanel pnl1 = new JPanel(new BorderLayout());
        pnl1.add(pnlBusca, BorderLayout.PAGE_START);
        pnl1.add(pnlDemanda.painelTabelas(), BorderLayout.CENTER);
        
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
            if(pnlUnidade.unidade.getIdBD() == 0) {
                
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
                 * Obtém o TableModel tabDemanda como objeto da classe
                 * sql.ModeloTabelaSQL e define nova consulta com
                 * setQuery, atualizando a tabela.
                 */
                tb = (ModeloTabela) pnlDemanda.tabDemanda.getModel();
                
                /**
                 * Define um novo modelo para JTable demanda com as demandas do
                 * item selecionado: unidade, subunidade, componente ou parte.
                 */
                switch (pnlUnidade.tabelaSelecionada) {
                    case "unidade":
                        try {
                            tb.setQuery(
                            "SELECT demanda.* FROM " + 
                            "demanda, unidade " +
                            "WHERE demanda.id_unidade = " +
                                String.valueOf(pnlUnidade.unidade.getIdBD()) +
                            " GROUP BY demanda.id");
                        } catch (SQLException ex) {ex.printStackTrace();}
                        
                        break;
                    case "subunidade":
                        try {
                            tb.setQuery(
                            "SELECT demanda.* FROM " + 
                            "demanda, unidade, subunidade " +
                            "WHERE demanda.id_unidade = " +
                                String.valueOf(pnlUnidade.unidade.getIdBD()) +
                            " AND demanda.id_subunidade = " +
                                pnlUnidade.idSelecionado +
                            " GROUP BY demanda.id");
                        } catch (SQLException ex) {ex.printStackTrace();}
                        
                        break;
                    case "componente":
                        try {
                            tb.setQuery(
                            "SELECT demanda.* FROM " + 
                            "demanda, unidade, subunidade, componente " +
                            "WHERE demanda.id_unidade = " +
                                String.valueOf(pnlUnidade.unidade.getIdBD()) +
                            " AND demanda.id_subunidade = " +
                                String.valueOf(pnlUnidade.subunidade.getIdBD())+
                            " AND demanda.id_componente = " +
                                pnlUnidade.idSelecionado +
                            " GROUP BY demanda.id");
                        } catch (SQLException ex) {ex.printStackTrace();}
                        
                        break;
                    case "parte":
                        try {
                            tb.setQuery(
                            "SELECT demanda.* FROM " + 
                            "demanda, unidade, subunidade, componente, parte " +
                            "WHERE demanda.id_unidade = " +
                                String.valueOf(pnlUnidade.unidade.getIdBD()) +
                            " AND demanda.id_subunidade = " +
                                String.valueOf(pnlUnidade.subunidade.getIdBD())+
                            " AND demanda.id_componente = " +
                                String.valueOf(pnlUnidade.componente.getIdBD())+
                            " AND demanda.id_parte = " +
                                pnlUnidade.idSelecionado +
                            " GROUP BY demanda.id");
                        } catch (SQLException ex) {ex.printStackTrace();}
                        
                        break;
                        
                    default: break;
                } // Fim do Switch.

                pnlDemanda.atualizarAparenciaDaTabela();
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
            if(pnlDemanda.tabDemanda.getSelectedRowCount() != 0){
                
                habilitarEdicao(true);
                limparTexto();
                
                /* Data da demanda */
                if(pnlDemanda.tabDemanda.getValueAt(
                        pnlDemanda.tabDemanda.getSelectedRow(), 1) == null) {
                    
                    tfdData.setText("");
                } else {
                    Date dataDate = (Date) pnlDemanda.tabDemanda.getValueAt(
                                    pnlDemanda.tabDemanda.getSelectedRow(), 1);
                    SimpleDateFormat formatoData;
                    formatoData = new SimpleDateFormat("dd/MM/yyyy");
                    String dataAq = formatoData.format(dataDate);
                    tfdData.setText(dataAq);
                }
                
                /* Hora da demanda */
                if(pnlDemanda.tabDemanda.getValueAt(
                        pnlDemanda.tabDemanda.getSelectedRow(), 1) != null) {
                    
                    Date dataDate = (Date) pnlDemanda.tabDemanda.getValueAt(
                                    pnlDemanda.tabDemanda.getSelectedRow(), 1);
                    
                    Calendar calendario = new GregorianCalendar();
                    calendario.setTime(dataDate);
                    
                    horaFormatada.getJComboBoxHora().setSelectedIndex(
                                (int) calendario.get(Calendar.HOUR_OF_DAY) + 1);
                    horaFormatada.getJComboBoxMinuto().setSelectedIndex(
                                (int) calendario.get(Calendar.MINUTE) + 1);
                }
                
                /* Modo da falha */
                if(pnlDemanda.tabDemanda.getValueAt(
                        pnlDemanda.tabDemanda.getSelectedRow(), 2) != null) {
                    
                    String modoSQL;
                    modoSQL = String.valueOf(pnlDemanda.tabDemanda.getValueAt(
                                    pnlDemanda.tabDemanda.getSelectedRow(), 2));
                    
                    switch(modoSQL) {
                        case "1":
                            modo1.setSelected(true);
                            modo2.setSelected(false);
                            modo3.setSelected(false);
                            
                            modo = modo1.getActionCommand();
                            break;
                        case "2":
                            modo1.setSelected(false);
                            modo2.setSelected(true);
                            modo3.setSelected(false);
                            
                            modo = modo2.getActionCommand();
                            break;
                        case "3":
                            modo1.setSelected(false);
                            modo2.setSelected(false);
                            modo3.setSelected(true);
                            
                            modo = modo3.getActionCommand();
                            break;
                        default: break;
                    }
                }
                
                /* Impacto da demanda na unidade de equipamento */
                if(pnlDemanda.tabDemanda.getValueAt(
                        pnlDemanda.tabDemanda.getSelectedRow(), 3) != null) {
                    
                    String impacto;
                    impacto = String.valueOf(pnlDemanda.tabDemanda.getValueAt(
                                    pnlDemanda.tabDemanda.getSelectedRow(), 3));
                    
                    cbxImpacto.setSelectedItem(impacto);
                }
                
                /* Causa da falha */
                if(pnlDemanda.tabDemanda.getValueAt(
                        pnlDemanda.tabDemanda.getSelectedRow(), 4) != null) {
                    
                    String causa;
                    causa = String.valueOf(pnlDemanda.tabDemanda.getValueAt(
                                    pnlDemanda.tabDemanda.getSelectedRow(), 4));
                    
                    String[] indicesCausa = causa.split("\\.");
                    
                    int categoria = Integer.parseInt(indicesCausa[0]);
                    int subdivisao = Integer.parseInt(indicesCausa[1]);
                    
                    cbxCausaCategoria.setSelectedIndex(categoria);
                    cbxCausaSubdivisao.setSelectedIndex(subdivisao + 1);
                }
                
                /* Condição de operação da unidade no momento da falha */
                if(pnlDemanda.tabDemanda.getValueAt(
                        pnlDemanda.tabDemanda.getSelectedRow(), 5) != null) {
                    
                    String condicao;
                    condicao = String.valueOf(pnlDemanda.tabDemanda.getValueAt(
                                    pnlDemanda.tabDemanda.getSelectedRow(), 5));
                    
                    cbxCondicaoOperacao.setSelectedItem(condicao);
                }
                
                pnlUnidade.habilitarTabela(PainelEquipamento.TAB_NULL);
                pnlDemanda.habilitarTabela(false);
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
            if(!pnlUnidade.tabelaSelecionada.equals("")) {
                /**
                 * Recebe o JTableModel como objeto da classe
                 * sql.ModeloTabelaSQL, de acordo com a tabela selecionada.
                 */
                ModeloTabela tb;
                
                /**
                 * Obtém o TableModel tabDemanda como objeto da classe
                 * sql.ModeloTabelaSQL e define nova consulta com
                 * setQuery, atualizando a tabela.
                 */
                tb = (ModeloTabela) pnlDemanda.tabDemanda.getModel();
                
                /**
                 * Define um novo modelo para JTable demanda com todas as
                 * demandas do banco de dados.
                 */
                try {
                    tb.setQuery("SELECT demanda.* FROM demanda");
                } catch (SQLException ex) {ex.printStackTrace();}

                pnlUnidade.limpaSelecao();
                pnlUnidade.habilitarTabela(PainelEquipamento.TAB_UNIDADE);
                pnlDemanda.atualizarAparenciaDaTabela();
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
                if(tfdData.getText().contains(" ")) {
                    mensagemErro = "Informe uma data válida.";
                } else if(horaFormatada.eValido() == false) {
                    mensagemErro = "Informe uma hora válida.";
                } else if(modo1.isSelected() == false &&
                            modo2.isSelected() == false &&
                            modo3.isSelected() == false) {
                    mensagemErro = "Informe um modo de falha.";
                } else if(cbxImpacto.getSelectedIndex() == 0) {
                    mensagemErro = "Informe o impacto no funcionameto "
                                + "da unidade";
                }
                
                /* Se não houver erro, executa a operação */
                if(mensagemErro.isEmpty()) {
                    System.out.println("opção CONFIRMAR selecionada");
                    confirmar();
                } else {
                    /* Se houver erro, exibe mensagem de erro */
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
                        || pnlDemanda.tabDemanda.getSelectedRowCount() != 0) {
                
                pnlUnidade.habilitarTabela(PainelEquipamento.TAB_UNIDADE);
                pnlDemanda.habilitarTabela(true);
                limparTexto();
                habilitarEdicao(false);
            }
        }
    }
}