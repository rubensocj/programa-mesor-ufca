package mesor.menu.alterar;

import mesor.equipamento.Subunidade;
import mesor.equipamento.Parte;
import mesor.equipamento.Componente;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.text.SimpleDateFormat;

import java.io.File;
import java.io.IOException;

import java.util.*;

import mesor.menu.painel.taxonomia.PainelEquipamento;
import mesor.menu.Janela;
import mesor.menu.adicionar.JanelaAdicionarEquipamento;

import java.sql.SQLException;

import mesor.sql.ModeloTabela;

/**
 * JanelaAlterarEquipamento.java
 * 
 * @version 1.0 1/2/2017
 * @author Rubens Jr
 */
public class JanelaAlterarEquipamento extends JanelaAdicionarEquipamento {
    private final JPanel pnlTabela;
    
    private JTextField tfdDescricao;
    public PainelEquipamento pnlUnidade = new PainelEquipamento();
    
    private static final int ALTERAR_UNIDADE = 0;
    private static final int ALTERAR_ITEM = 1;
    private static final int ALTERAR_NULL = 2;
    
    /**
     * Construtor.
     */
    public JanelaAlterarEquipamento() {
        
        /* Cria os botões antes de montar o painel Principal */
        criarBotoesOpcoes();
        criarBotoesAlterar();
              
        /**
         * Define o painelUnidade como editável, permitindo a exclusão de itens.
         * 
         * Inicializa objeto da classe PainelUnidade e um JPanel que recebe o
         * painel com as tabelas dos equipamentos.
         */
        pnlUnidade.setEditavel();
        pnlTabela = pnlUnidade.painelTabelas();
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
                frm.dispose();
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
                } catch (IOException ex) {ex.printStackTrace();}
                
                JScrollPane aPane = new JScrollPane(p,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                p.setPreferredSize(new Dimension(400,500));
                
                JDialog janelaAjuda = new JDialog(frm);
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
         * Botão "Escolher" define o texto dos JTextFields de acordo com o
         * selecionado nas tabelas.
         */
        btnEscolher = new JButton();
        btnEscolher.addActionListener(new Escolher());
        btnEscolher.setPreferredSize(new Dimension(35, 25));
        btnEscolher.setIcon(Janela.criarIcon("/res/icone/seta_dupla_direita.png"));
        
        /**
         * Botão "Voltar" limpa o texto dos JTextFields e cancela a alteração.
         */
        btnVoltar = new JButton();
        btnVoltar.addActionListener(new Voltar());
        btnVoltar.setPreferredSize(new Dimension(35, 25));
        btnVoltar.setIcon(Janela.criarIcon("/res/icone/seta_dupla_esquerda.png"));
        
        /* Botão "Salvar alterações" executa a operação */
        btnSalvarAlteracao = new JButton("Salvar alterações");
        btnSalvarAlteracao.setPreferredSize(new Dimension(150,20));
        btnSalvarAlteracao.addActionListener(new Salvar());
    }
    
    @Override
    public JPanel montarPainelPrincipal() {
        /**
         * Painel com os botões de escolher e voltar.
         */
        JPanel pnlBtnAlterar = new JPanel(new BorderLayout(0,10));
        pnlBtnAlterar.add(btnEscolher, BorderLayout.NORTH);
        pnlBtnAlterar.add(btnVoltar, BorderLayout.CENTER);
        
        /* Painel com campos para alterações */
        JPanel pnlAlterar1 = new JPanel(new BorderLayout());
        pnlAlterar1.add(painelGeral(), BorderLayout.NORTH);
        pnlAlterar1.add(painelOperacional(), BorderLayout.CENTER);
        pnlAlterar1.add(painelDescricao(), BorderLayout.SOUTH);
        
        JPanel pnlSalvarAlteracao = new JPanel(new FlowLayout(2));
        pnlSalvarAlteracao.add(btnSalvarAlteracao);
        
        JPanel pnlAlterar2 = new JPanel(new BorderLayout());
        pnlAlterar2.add(pnlAlterar1, BorderLayout.CENTER);
        pnlAlterar2.add(pnlSalvarAlteracao, BorderLayout.SOUTH);
        
        inicializarMapaComponentes();
        mapearComponentes();
        
        /* Desabilita a edição */
        habilitarEdicao(ALTERAR_NULL);
        
        pnlAlterar = new JPanel(new FlowLayout());
        pnlAlterar.add(pnlBtnAlterar);
        pnlAlterar.add(pnlAlterar2);
        
        /**
         * Painel final: painel com tabelas no lado esquerdo e painel de escolha
         * e alteração completo à direita.
         */
        pnlPrincipal = new JPanel(new BorderLayout());
        try {
            pnlPrincipal.add(painelSistema(), BorderLayout.NORTH);
            /* Adiciona o ItemListener a cbxSistema */
            cbxSistema.addItemListener(new ItemEventSistema(pnlUnidade));            
        } catch (SQLException ex) {ex.printStackTrace();}
        pnlPrincipal.add(pnlTabela, BorderLayout.WEST);
        pnlPrincipal.add(pnlAlterar, BorderLayout.EAST);
        
        return pnlPrincipal;
    }
    
    @Override
    public void mapearComponentes() {
        /* Adiciona os componentes ao mapa */
        mapaComponentes.put(0, lblUniClasse);
        mapaComponentes.put(1, tfdUniClasse);
        mapaComponentes.put(2, lblUniTipo);
        mapaComponentes.put(3, tfdUniTipo);
        mapaComponentes.put(4, lblUniFabricante);
        mapaComponentes.put(5, tfdUniFabricante);
        mapaComponentes.put(6, lblUniIdentificacao);
        mapaComponentes.put(7, tfdUniIdentificacao);
        mapaComponentes.put(8, lblUniCategoria);
        mapaComponentes.put(9, tfdUniCategoria);
        mapaComponentes.put(10, lblUniLocal);
        mapaComponentes.put(11, tfdUniLocal);
        mapaComponentes.put(12, lblUniDataAq);
        mapaComponentes.put(13, tfdUniDataAq);
        mapaComponentes.put(14, lblUniModo);
        mapaComponentes.put(15, cbxModo);
        mapaComponentes.put(16, lblUniDataOp);
        mapaComponentes.put(17, tfdUniDataOp);
        mapaComponentes.put(18, tfdDescricao);
        
        // Define todos os componentes como desabilitado.
        for(int i = 0; i <= 18; i++) {
            mapaComponentes.get(i).setEnabled(false);
        }
    }
    
    @Override
    public void confirmar() {
        if(!pnlUnidade.tabelaSelecionada.equals("") &&
                    campoEditavel != ALTERAR_NULL) {
            
            /**
             * Recebe o JTableModel como objeto da classe sql.ModeloTabelaSQL,
             * de acordo com a tabela selecionada.
             */
            ModeloTabela tb;
            
            switch(pnlUnidade.tabelaSelecionada) {
                case "unidade":
                    // Se não for informada a data de aquisição,
                    // String dataAquisição = "".
                    String dataAquisicao;
                    if(tfdUniDataAq.getText().contains(" ")) {
                        dataAquisicao = "";
                    } else {
                        dataAquisicao = 
                            tfdUniDataAq.getText().substring(6,10).concat(
                        "-" + tfdUniDataAq.getText().substring(3,5)).concat(
                        "-" + tfdUniDataAq.getText().substring(0,2));
                    }
                    
                    // Se não for informada a data de início de operação,
                    // String dataAquisição = "".
                    String dataInicio;
                    if(tfdUniDataOp.getText().contains(" ")) {
                        dataInicio = "";
                    } else {
                        dataInicio = 
                            tfdUniDataOp.getText().substring(6,10).concat(
                        "-" + tfdUniDataOp.getText().substring(3,5)).concat(
                        "-" + tfdUniDataOp.getText().substring(0,2));
                    }

                    unidade.setCategoria(tfdUniCategoria.getText());
                    unidade.setClasse(tfdUniClasse.getText());
                    unidade.setTipo(tfdUniTipo.getText());
                    unidade.setFabricante(tfdUniFabricante.getText());
                    unidade.setIdentificacao(tfdUniIdentificacao.getText());
                    unidade.setLocal(tfdUniLocal.getText());
                    unidade.setDataAquisicao(dataAquisicao);
                    unidade.setModoOperacional((String)
                                cbxModo.getSelectedItem());
                    unidade.setDataInicioOperacao(dataInicio);
                    unidade.setIdBD((int)pnlUnidade.tabUnidade.getValueAt(
                                    pnlUnidade.tabUnidade.getSelectedRow(),0));

                    unidade.sqlAlterar();
                    
                    /**
                     * Obtém o TableModel tabUnidade como sql.ModeloTabelaSQL
                     * e define nova consulta com setQuery, atualizando a
                     * tabela.
                     */
                    tb = (ModeloTabela) pnlUnidade.tabUnidade.getModel();
                    try {
                        tb.setQuery("SELECT * FROM unidade;");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    
                    // Reajusta a aparência da tabela.
                    pnlUnidade.atualizarAparenciaDaTabela(PainelEquipamento.TAB_UNIDADE);
                    pnlUnidade.atualizarAparenciaDaTabela(PainelEquipamento.TAB_SUBUNIDADE);
                    
                    break;
                
                case "subunidade":
                    Subunidade subunidade = new Subunidade();
                    subunidade.setIdBD(
                            (int) pnlUnidade.tabSubunidade.getValueAt(
                                pnlUnidade.tabSubunidade.getSelectedRow(),0));
                    subunidade.setDescricao(tfdDescricao.getText());
                    
                    subunidade.sqlAlterar();
                    
                    /**
                     * Obtém o TableModel da tabSubunidade como 
                     * sql.ModeloTabelaSQL e define nova consulta com setQuery,
                     * atualizando a tabela.
                     */
                    tb = (ModeloTabela) pnlUnidade.tabSubunidade.getModel();
                    try {
                        tb.setQuery(
                            "SELECT id, descricao FROM subunidade " +
                            "WHERE id_unidade = " + String.valueOf(
                            pnlUnidade.tabUnidade.getValueAt(
                                    pnlUnidade.tabUnidade.getSelectedRow(),0)));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    
                    // Reajusta a aparência da tabela.
                    pnlUnidade.atualizarAparenciaDaTabela(PainelEquipamento.TAB_SUBUNIDADE);
                    
                    break;

                case "componente":
                    Componente componente = new Componente();
                    componente.setIdBD((int)pnlUnidade.tabComponente.getValueAt(
                                pnlUnidade.tabComponente.getSelectedRow(),0));
                    componente.setDescricao(tfdDescricao.getText());
                    
                    componente.sqlAlterar();
                    
                    /**
                     * Obtém o TableModel da tabComponente como 
                     * sql.ModeloTabelaSQL e define nova consulta com setQuery,
                     * atualizando a tabela.
                     */
                    tb = (ModeloTabela) pnlUnidade.tabComponente.getModel();
                    try {
                        tb.setQuery(
                            "SELECT id, descricao FROM componente " +
                            "WHERE id_subunidade = " + String.valueOf(
                            pnlUnidade.tabSubunidade.getValueAt(
                                pnlUnidade.tabSubunidade.getSelectedRow(),0)));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    
                    // Reajusta a aparência da tabela.
                    pnlUnidade.atualizarAparenciaDaTabela(PainelEquipamento.TAB_COMPONENTE);
                    
                    break;

                case "parte":
                    Parte parte = new Parte();
                    parte.setIdBD((int) pnlUnidade.tabParte.getValueAt(
                                pnlUnidade.tabParte.getSelectedRow(),0));
                    parte.setDescricao(tfdDescricao.getText());
                    
                    parte.sqlAlterar();
                    
                    /**
                     * Obtém o TableModel da tabSubunidade como 
                     * sql.ModeloTabelaSQL e define nova consulta com setQuery,
                     * atualizando a tabela.
                     */
                    tb = (ModeloTabela) pnlUnidade.tabParte.getModel();
                    try {
                        tb.setQuery(
                            "SELECT id, descricao FROM parte " +
                            "WHERE id_componente = " + String.valueOf(
                            pnlUnidade.tabComponente.getValueAt(
                                pnlUnidade.tabComponente.getSelectedRow(),0)));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    
                    // Reajusta a aparência da tabela.
                    pnlUnidade.atualizarAparenciaDaTabela(PainelEquipamento.TAB_PARTE);
                    
                    break;                
            }
            
            /**
             * Limpa o texto dos JTextField ou JFormattedTextFields
             * usados para a alteração.
             */     
            limparTexto(0);
            limparTexto(1);
            
            /**
             * Limpa a seleção de todas as tabelas.
             */
            pnlUnidade.limpaSelecao();
            
            habilitarEdicao(ALTERAR_NULL);
            
            pnlUnidade.habilitarTabela(PainelEquipamento.TAB_UNIDADE);
        }
    }
    
    // -------------------------------------------------------------------------
    // Métodos privados.
    // -------------------------------------------------------------------------
    
    /**
     * Habilita ou desabilita os campos de edição.
     * 
     * @param escolha 
     */
    private void habilitarEdicao(int escolha) {
        switch(escolha) {
            case ALTERAR_ITEM: 
                // Desabilita painel unidade. Habilita painel subunidade,
                // componente e parte.
                for(int i = 0; i <= 18; i++) {
                    if(i == 18) {
                        mapaComponentes.get(i).setEnabled(true);
                    } else {
                        mapaComponentes.get(i).setEnabled(false);
                    }
                }
                
                campoEditavel = ALTERAR_ITEM;
                break;
                
            case ALTERAR_UNIDADE:
                // Habilita painel unidade. Desabilita painel subunidade,
                // componente e parte.
                for(int i = 0; i <= 18; i++) {
                    if(i == 18) {
                        mapaComponentes.get(i).setEnabled(false);
                    } else {
                        mapaComponentes.get(i).setEnabled(true);
                    }
                }
                
                campoEditavel = ALTERAR_UNIDADE;
                break;
                
            case ALTERAR_NULL:
                // Desabilita tudo.
                for(int i = 0; i <= 18; i++) {
                    mapaComponentes.get(i).setEnabled(false);
                }
                
                campoEditavel = ALTERAR_NULL;
                break;
                
            default: break;
        }
    }
    
    /**
     * Limpa o texto de JTextFields e JFormattedTextField.
     * 
     * @param escolha 
     */
    private void limparTexto(int escolha) {
        switch(escolha) {
            case 0:
                // Define o texto dos JTextFields e JFormattedTextField
                // de unidade como "".
                tfdUniClasse.setText("");
                tfdUniTipo.setText("");
                tfdUniFabricante.setText("");
                tfdUniIdentificacao.setText("");
                tfdUniCategoria.setText("");
                tfdUniLocal.setText("");
                tfdUniDataAq.setText(""); 
                tfdUniDataOp.setText("");
                cbxModo.setSelectedIndex(0);
                break;
                
            case 1:
                // Define o texto do JTextField de subunidade, comoponente e
                // parte como "".
                tfdDescricao.setText("");
                break;
                
            default: break;
        }
    }
    
    /**
     * Monta a área para alteração dos dados de subunidades, componentes e
     * partes.
     * 
     * @return Um JPanel.
     */
    private JPanel painelDescricao() {
        // Cria JTextField.
        tfdDescricao = new JTextField(30);
        
        // Painel "Descrição".
        JPanel pnlDescricao = new JPanel(new FlowLayout());
        pnlDescricao.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                            "Descrição"));
        pnlDescricao.add(tfdDescricao);
        
        return pnlDescricao;
    }
    
    // -------------------------------------------------------------------------
    // Classes.
    // -------------------------------------------------------------------------
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionEscolher
     */
    private class Escolher extends Janela.ActionEscolher {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(!pnlUnidade.tabelaSelecionada.equals("")) {
                switch(pnlUnidade.tabelaSelecionada) {
                    case "unidade":
                        // Habilita apenas os Componentes correspondentes
                        // a tabela selecionada: Unidade.
                        habilitarEdicao(ALTERAR_UNIDADE);
                        
                        // Define "" como o texto dos JTextFields que não
                        // correspondem a tabela selecionada.
                        limparTexto(1);
                        
                        // Define os textos dos JTextFields bem como os
                        // valores correspondentes na tabela selecionada.
                        tfdUniClasse.setText((String)
                            pnlUnidade.tabUnidade.getValueAt(
                                    pnlUnidade.tabUnidade.getSelectedRow(), 1));
                        tfdUniTipo.setText((String)
                            pnlUnidade.tabUnidade.getValueAt(
                                    pnlUnidade.tabUnidade.getSelectedRow(), 2));
                        tfdUniFabricante.setText((String)
                            pnlUnidade.tabUnidade.getValueAt(
                                    pnlUnidade.tabUnidade.getSelectedRow(), 3));
                        tfdUniIdentificacao.setText((String)
                            pnlUnidade.tabUnidade.getValueAt(
                                    pnlUnidade.tabUnidade.getSelectedRow(), 4));
                        tfdUniCategoria.setText((String)
                            pnlUnidade.tabUnidade.getValueAt(
                                    pnlUnidade.tabUnidade.getSelectedRow(), 5));
                        tfdUniLocal.setText((String)
                            pnlUnidade.tabUnidade.getValueAt(
                                    pnlUnidade.tabUnidade.getSelectedRow(), 6));
                        if(pnlUnidade.tabUnidade.getValueAt(
                            pnlUnidade.tabUnidade.getSelectedRow(),7) == null) {
                            
                            tfdUniDataAq.setText("");
                        } else {
                            Date dataDate;
                            dataDate = (Date) pnlUnidade.tabUnidade.getValueAt(
                                    pnlUnidade.tabUnidade.getSelectedRow(), 7);
                            
                            SimpleDateFormat formatoData;
                            formatoData = new SimpleDateFormat("dd/MM/yyyy");
                            
                            String dataAq = formatoData.format(dataDate);
                            
                            tfdUniDataAq.setText(dataAq);
                        }
                        if(pnlUnidade.tabUnidade.getValueAt(
                            pnlUnidade.tabUnidade.getSelectedRow(),8) != null) {
                            
                            String modo;
                            modo = String.valueOf(
                                pnlUnidade.tabUnidade.getValueAt(
                                    pnlUnidade.tabUnidade.getSelectedRow(),8));
                            
                            cbxModo.setSelectedItem(modo);
                        }
                        if(pnlUnidade.tabUnidade.getValueAt(
                            pnlUnidade.tabUnidade.getSelectedRow(),9) == null) {
                            
                            tfdUniDataOp.setText("");
                        } else {
                            Date dataDate;
                            dataDate = (Date) pnlUnidade.tabUnidade.getValueAt(
                                    pnlUnidade.tabUnidade.getSelectedRow(), 9);
                            
                            SimpleDateFormat formatoData;
                            formatoData = new SimpleDateFormat("dd/MM/yyyy");
                            
                            String dataOp = formatoData.format(dataDate);
                            
                            tfdUniDataOp.setText(dataOp);
                        }
                        
                        break;
                        
                    case "subunidade":
                        // Habilita apenas os Componentes correspondentes
                        // a tabela selecionada: Subunidade.
                        habilitarEdicao(ALTERAR_ITEM);
                        
                        // Define "" como o texto dos JTextFields que não
                        // correspondem a tabela selecionada.
                        limparTexto(0);
                        
                        // Define os textos dos JTextFields bem como os
                        // valores correspondentes na tabela selecionada.
                        tfdDescricao.setText((String)
                            pnlUnidade.tabSubunidade.getValueAt(
                                pnlUnidade.tabSubunidade.getSelectedRow(), 1));
                        
                        break;
                        
                    case "componente":
                        // Habilita apenas os Componentes correspondentes
                        // a tabela selecionada: Componente.
                        habilitarEdicao(ALTERAR_ITEM);
                        
                        // Define "" como o texto dos JTextFields que não
                        // correspondem a tabela selecionada.
                        limparTexto(0);
                        
                        // Define os textos dos JTextFields bem como os
                        // valores correspondentes na tabela selecionada.
                        tfdDescricao.setText((String)
                            pnlUnidade.tabComponente.getValueAt(
                                pnlUnidade.tabComponente.getSelectedRow(), 1));
                        
                        break;
                        
                    case "parte":
                        // Habilita apenas os Componentes correspondentes
                        // a tabela selecionada: Parte.
                        habilitarEdicao(ALTERAR_ITEM);
                        
                        // Define "" como o texto dos JTextFields que não
                        // correspondem a tabela selecionada.
                        limparTexto(0);
                        
                        // Define os textos dos JTextFields bem como os
                        // valores correspondentes na tabela selecionada.
                        tfdDescricao.setText((String)
                            pnlUnidade.tabParte.getValueAt(
                                pnlUnidade.tabParte.getSelectedRow(), 1));
                        
                        break;
                }
                
                pnlUnidade.habilitarTabela(PainelEquipamento.TAB_NULL);
            }
        }
    }
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionSalvar
     */
    private class Salvar extends Janela.ActionSalvar {
        @Override
        public void actionPerformed(ActionEvent e) {
            String mensagemErro = "";
            if(campoEditavel == ALTERAR_UNIDADE) {
                if(tfdUniClasse.getText().isEmpty()) {
                    mensagemErro = "Informe uma classe de equipamento válida.";                    
                } else if(tfdUniTipo.getText().isEmpty()) {
                    mensagemErro = "Informe um tipo de equipamento válido.";
                } else if(tfdUniFabricante.getText().isEmpty()) {
                    mensagemErro = "Informe um fabricante.";
                } else if(tfdUniIdentificacao.getText().isEmpty()) {
                    mensagemErro = "Informe uma identificação do equipamento";
                } else if(cbxModo.getSelectedIndex() == 0) {
                    mensagemErro = "Informe o modo operacional.";
                } else if(tfdUniDataOp.getText().contains(" ")) {
                    mensagemErro = "Informe uma data de início de "
                            + "operação válida.";
                }
            } else if(campoEditavel == ALTERAR_ITEM) {
                if(tfdDescricao.getText().isEmpty()) {
                    mensagemErro = "Informe a descrição.";
                }
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
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionVoltar
     */
    private class Voltar extends Janela.ActionVoltar {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!pnlUnidade.tabelaSelecionada.equals("")) {
                pnlUnidade.limpaSelecao();
                pnlUnidade.habilitarTabela(PainelEquipamento.TAB_UNIDADE);
                limparTexto(0);
                limparTexto(1);
                habilitarEdicao(ALTERAR_NULL);
            }
        }
    }
}