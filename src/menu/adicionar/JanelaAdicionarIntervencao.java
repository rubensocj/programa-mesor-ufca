package menu.adicionar;

import intervencao.Demanda;
import intervencao.Intervencao;
import javax.swing.*;
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.awt.event.*;

import java.text.ParseException;


import java.io.File;
import java.io.IOException;

import java.sql.SQLException;

import menu.HoraFormatada;
import menu.painel.PainelEquipamento;
import menu.painel.PainelDemanda;
import menu.JanelaAdicionarAlterar;

import conexaoSql.ModeloTabela;

public class JanelaAdicionarIntervencao extends JanelaAdicionarAlterar {
    
    public PainelEquipamento pnlUnidade = new PainelEquipamento();
    public PainelDemanda pnlDemanda = new PainelDemanda();
    
    public JPanel pnlTabelaDemanda;
    
    public JLabel lblAtividade, lblCategoria, lblDataInicio,
            lblDataTermino, lblHoraInicio, lblHoraTermino;
    
    public JComboBox cbxCategoria, cbxAtividade;
    
    public JFormattedTextField tfdDataInicio, tfdDataTermino;
    
    /**
     * Quando selecionado, habilita os componentes referentes à data e hora de
     * término da intervenção. Como estas informações não são obrigatórias na
     * adição de uma intervenção, pode-se desmarcar esta JCheckBox para
     * desabilitar estes componentes.
     */
    public JCheckBox ckboxTermino;
    
    public JTable tabDemanda;
    
    public Intervencao intervencao = new Intervencao();
    public Demanda demanda = new Demanda();
    public HoraFormatada horaInicio, horaTermino;
    
    private JButton btnBuscarDemanda;
        
    // Construtor --------------------------------------------------------------
    public JanelaAdicionarIntervencao() {}
    
    // -------------------------------------------------------------------------
    // Métodos sobrepostos.
    // -------------------------------------------------------------------------
    
    @Override
    public void criarBotoesOpcoes() {
        btn1 = new JButton("Confirmar");
        btn2 = new JButton("Cancelar");
        btn3 = new JButton("Ajuda");
        options = new Object[] {this.btn1, this.btn2, this.btn3};
        
        btn1.addActionListener(new Salvar());
        
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("opção CANCELAR selecionada");
                dialog.dispose();
            }
        });
        
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("opção AJUDA selecionada");
                JEditorPane p = new JEditorPane();
                p.setContentType("text/html");
                p.setEditable(false);
                File ajudaHTML = new File(
                            LOCAL + "\\ajuda\\janelaAdicionarIntervencao.html");
                try {
                    p.setPage(ajudaHTML.toURL());
                } catch (IOException ex) {
                    ex.printStackTrace();
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
    public JPanel montarPainelPrincipal() {
        /* Cria os botões antes de montar o painel Principal */
        criarBotoesOpcoes();
        
        pnlPrincipal = new JPanel(new BorderLayout());
        try {
            pnlPrincipal.add(painelSistema(), BorderLayout.NORTH);
            /* Adiciona o ItemListener a cbxSistema */
            cbxSistema.addItemListener(new ItemEventSistema(pnlUnidade));
        } catch (SQLException ex) {ex.printStackTrace();}
        pnlPrincipal.add(pnlUnidade.painelTabelas(), BorderLayout.CENTER);
        pnlPrincipal.add(painelIntervencao(), BorderLayout.EAST);
        return pnlPrincipal;
    }
    
    @Override
    public void confirmar() {
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
        String dataInicioString = tfdDataInicio.getText().substring(6,10).concat(
            "-" + tfdDataInicio.getText().substring(3,5)).concat(
            "-" + tfdDataInicio.getText().substring(0,2)) + " " + horaInicio.getHora();
        
        String dataTerminoString;
        if(tfdDataTermino.getText().startsWith(" ")) {
            // Se não houver data e hora de término, dataTerminoString = ""
            dataTerminoString = "";
        } else {
            // Caso contrário, monta o "String"
            dataTerminoString = tfdDataTermino.getText().substring(6,10).concat(
            "-" + tfdDataTermino.getText().substring(3,5)).concat(
            "-" + tfdDataTermino.getText().substring(0,2)) + " " + horaTermino.getHora();
        }
        
        intervencao.setCategoria(String.valueOf(cbxCategoria.getSelectedItem()));
        intervencao.setAtividade((String) cbxAtividade.getSelectedItem());
        intervencao.setInicio(dataInicioString);
        intervencao.setTermino(dataTerminoString);
        intervencao.setUnidade(pnlUnidade.unidade);
        intervencao.setSubunidade(pnlUnidade.subunidade);
        intervencao.setComponente(pnlUnidade.componente);
        intervencao.setParte(pnlUnidade.parte);
        
        // setIdBD atribui o id selecionado na tabela Demanda ao objeto demanda
        if(pnlDemanda.tabDemanda.getSelectedRowCount() != 0) {
            demanda.setIdBD((int) pnlDemanda.tabDemanda.getValueAt(
                        pnlDemanda.tabDemanda.getSelectedRow(), 0));
        }
        
        System.out.println(intervencao.toString());
        
        intervencao.setDemanda(demanda);

        // CHAMA MÉTODO QUE INSERE NO BANCO DE DADOS
        intervencao.adicionaIntervencao();
    } // Fim do método confirmar()
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    // Painel para adicionar propriedades da intervenção -----------------------
    public JPanel painelIntervencao() {
        /* Painel selecionar categoria de intervenção */
        cbxCategoria = new JComboBox(
                    new Object[] {
                "Selecionar...", "Corretiva", "Preventiva"});
        cbxCategoria.setPrototypeDisplayValue("XXXXX");
        cbxCategoria.addItemListener(new ItemEventCategoria());
        cbxCategoria.setPreferredSize(new Dimension(220, 20));
        
        cbxAtividade = new JComboBox(new Object[] {});
        cbxAtividade.setPrototypeDisplayValue("XXXXX");
        cbxAtividade.setEnabled(false);
        cbxAtividade.setPreferredSize(new Dimension(220, 20));
        
        lblCategoria = new JLabel(" Categoria: *");
        lblAtividade = new JLabel(" Atividade:  *");
        lblAtividade.setEnabled(false);
        
        JPanel pnlAtividadeCategoria1 = new JPanel(new GridLayout(0,1,0,8));
        pnlAtividadeCategoria1.add(lblCategoria);
        pnlAtividadeCategoria1.add(lblAtividade);
        
        JPanel pnlAtividadeCategoria2 = new JPanel(new GridLayout(0,1,0,4));
        pnlAtividadeCategoria2.add(cbxCategoria);
        pnlAtividadeCategoria2.add(cbxAtividade);
        
        JPanel pnlCategoriaAtividade = new JPanel(
                    new FlowLayout(FlowLayout.LEFT));
        pnlCategoriaAtividade.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                            "Informações gerais"));
        pnlCategoriaAtividade.add(pnlAtividadeCategoria1);
        pnlCategoriaAtividade.add(pnlAtividadeCategoria2);
        
        /* Data de início da intervenção */
        lblDataInicio = new JLabel("Data: *");
        
        tfdDataInicio = new JFormattedTextField();
        tfdDataInicio.setColumns(6);
        tfdDataInicio.setFocusLostBehavior(JFormattedTextField.PERSIST);
        tfdDataInicio.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                tfdDataInicio.setSelectionStart(0);
                tfdDataInicio.setSelectionEnd(10);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if(tfdDataInicio.getText().contains(" ")) {tfdDataInicio.setText("");
            }}});
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.install(tfdDataInicio);
            mf.setPlaceholder("DD/MM/AAAA");
            mf.setValidCharacters("0123456789");
        } catch (ParseException ex) { ex.printStackTrace();}
        
        /* Hora de início da intervenção */
        lblHoraInicio = new JLabel("Hora: *");
        horaInicio = new HoraFormatada();
        
        /* Painel Início */
        JPanel pnlIntInicio = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlIntInicio.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                            "Início"));
        pnlIntInicio.add(lblDataInicio);
        pnlIntInicio.add(tfdDataInicio);
        pnlIntInicio.add(lblHoraInicio);
        pnlIntInicio.add(horaInicio.adicionar());
        
        /* JCheckBox habilitar informar data de término */
        ckboxTermino = new JCheckBox();
        
        // ActionListener da JCheckBox.
        ckboxTermino.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ckboxTermino.isSelected()) {
                    // Selecionar a JCheckBox habilita o painel de término.
                    lblDataTermino.setEnabled(true);
                    tfdDataTermino.setEnabled(true);
                    lblHoraTermino.setEnabled(true);
                    horaTermino.setEnabled(true);
                } else {
                    // Selecionar a JCheckBox desabilita o painel de término.
                    lblDataTermino.setEnabled(false);
                    tfdDataTermino.setEnabled(false);
                    lblHoraTermino.setEnabled(false);
                    horaTermino.setEnabled(false);
                }
            }
        });
        
        /* Data de término da intervenção */
        lblDataTermino = new JLabel("Data:  ");
        lblDataTermino.setEnabled(false);
        
        tfdDataTermino = new JFormattedTextField();
        tfdDataTermino.setEnabled(false);
        tfdDataTermino.setColumns(6);
        tfdDataTermino.setFocusLostBehavior(JFormattedTextField.PERSIST);
        tfdDataTermino.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                tfdDataTermino.setSelectionStart(0);
                tfdDataTermino.setSelectionEnd(10);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if(tfdDataTermino.getText().contains(" ")) {
                    tfdDataTermino.setText("");
            }}});
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.install(tfdDataTermino);
            mf.setPlaceholder("DD/MM/AAAA");
            mf.setValidCharacters("0123456789");
        } catch (ParseException ex) { ex.printStackTrace();}
        
        /* Hora de término da intervenção */
        lblHoraTermino = new JLabel("Hora:  ");
        lblHoraTermino.setEnabled(false);
        horaTermino = new HoraFormatada();
        horaTermino.setEnabled(false);
                
        // Painel Término
        JPanel pnlIntTermino = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlIntTermino.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                            "Término"));
        pnlIntTermino.add(ckboxTermino);
        pnlIntTermino.add(lblDataTermino);
        pnlIntTermino.add(tfdDataTermino);
        pnlIntTermino.add(lblHoraTermino);
        pnlIntTermino.add(horaTermino.adicionar());
        
        // Painel que guarda os paineis menores
        JPanel pnlIntervencao1 = new JPanel(new BorderLayout());
        pnlIntervencao1.add(pnlCategoriaAtividade, "North");
        pnlIntervencao1.add(pnlIntInicio, "Center");
        pnlIntervencao1.add(pnlIntTermino, "South");
        
        JPanel pnlIntervencao2 = new JPanel(new BorderLayout());
        pnlIntervencao2.add(pnlIntervencao1, BorderLayout.NORTH);
        pnlIntervencao2.add(painelDemanda(), BorderLayout.CENTER);
        
        // Painel Propriedades
        JPanel pnlIntervencao3 = new JPanel(new FlowLayout());
        pnlIntervencao3.add(pnlIntervencao2);
        return pnlIntervencao3;
    }
    
    public JPanel painelDemanda() {
        /* Painel com a tabela das demandas */
        pnlDemanda.tamanhoDaTabela(new Dimension(300,250));
        pnlDemanda.habilitarTabela(false);
        pnlTabelaDemanda = pnlDemanda.painelTabelas();
        
        JLabel lblDemanda = new JLabel("Demanda: ");
        
        // Botão "Buscar" demanda
        btnBuscarDemanda = new JButton("Buscar");
        btnBuscarDemanda.addActionListener(new ActionListener() {
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
            }
            else {
                
                pnlDemanda.tabDemanda.clearSelection();
                pnlDemanda.habilitarTabela(true);
                
                try {
                switch (pnlUnidade.tabelaSelecionada) {
                    case "unidade":
                        pnlDemanda.tabDemanda.setModel(new ModeloTabela(
                            "SELECT demanda.* FROM " +
                                "demanda, unidade WHERE demanda.id_unidade = " +
                                String.valueOf(pnlUnidade.unidade.getIdBD()) +
                                " GROUP BY demanda.id"));
                        break;
                    case "subunidade":
                        pnlDemanda.tabDemanda.setModel(new ModeloTabela(
                            "SELECT demanda.* FROM " +
                                "demanda, unidade, subunidade WHERE demanda.id_unidade = " +
                                String.valueOf(pnlUnidade.unidade.getIdBD()) +
                                " AND demanda.id_subunidade = " +
                                pnlUnidade.idSelecionado +
                                " GROUP BY demanda.id"));
                        break;
                    case "componente":
                        pnlDemanda.tabDemanda.setModel(new ModeloTabela(
                            "SELECT demanda.* FROM " +
                                "demanda, unidade, subunidade, componente WHERE demanda.id_unidade = " +
                                String.valueOf(pnlUnidade.unidade.getIdBD()) +
                                " AND demanda.id_subunidade = " +
                                String.valueOf(pnlUnidade.subunidade.getIdBD()) +
                                " AND demanda.id_componente = " +
                                pnlUnidade.idSelecionado +
                                " GROUP BY demanda.id"));
                        break;
                    case "parte":
                        pnlDemanda.tabDemanda.setModel(new ModeloTabela(
                            "SELECT demanda.* FROM " +
                                "demanda, unidade, subunidade, componente, parte WHERE demanda.id_unidade = " +
                                String.valueOf(pnlUnidade.unidade.getIdBD()) +
                                " AND demanda.id_subunidade = " +
                                String.valueOf(pnlUnidade.subunidade.getIdBD()) +
                                " AND demanda.id_componente = " +
                                String.valueOf(pnlUnidade.componente.getIdBD()) +
                                " AND demanda.id_parte = " +
                                pnlUnidade.idSelecionado +
                                " GROUP BY demanda.id"));
                        break;
                    default:
                        break;
                } // Fim do Switch
                } catch(SQLException ex) {
                    ex.printStackTrace();
                }
                
                pnlDemanda.atualizarAparenciaDaTabela();
            }
        }});
        
        // Painel do Botão "Buscar"
        JPanel pnlbtnBuscarDemanda = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlbtnBuscarDemanda.add(btnBuscarDemanda);
        
        JPanel pnlSuperiorDem = new JPanel(new BorderLayout());
        pnlSuperiorDem.add(lblDemanda, BorderLayout.WEST);        
        pnlSuperiorDem.add(pnlbtnBuscarDemanda, BorderLayout.EAST);
        
        // Painel demanda
        JPanel pnlDemanda1 = new JPanel(new BorderLayout());
        pnlDemanda1.add(pnlSuperiorDem, BorderLayout.PAGE_START);
        pnlDemanda1.add(pnlTabelaDemanda, BorderLayout.CENTER);
        
        return pnlDemanda1;
    }
    
    // -------------------------------------------------------------------------
    // Classes.
    // -------------------------------------------------------------------------

    /**
     * @Override itemStateChanged(ItemEvent e)
     * Cria a String "categoria" instanciando o item selecionado na JComboBox
     * "cbxIntCategoria", através do método cbxIntCategoria.getSelectedItem().
     * Cria o DefaultComboBoxModel "cbxAtv", que tem como itens os tipos de
     * atividades possíveis para cada categoria de intervenção. Define o modelo
     * de JComboBox de cbxIntAtividade de acordo com a categoria selecionada.
    **/
    private class ItemEventCategoria implements ItemListener {
        @Override
        @SuppressWarnings("null")
        public void itemStateChanged(ItemEvent e) {
            String categoria = (String) cbxCategoria.getSelectedItem();
        
            /*
            "Substituição"  (C, P)
            "Reparo"        (C)
            "Modificação"   (C, P)
            "Ajuste"        (C, P)
            "Conserto"      (C, P)
            "Checagem"      (C)
            "Serviço"       (P)
            "Teste"         (P)
            "Inspeção"      (P)
            "Revisão"       (C, P)
            */

            DefaultComboBoxModel cbxAtv;
            cbxAtv = new DefaultComboBoxModel(new Object[] {});
            
            try {
                switch (categoria) {
                    case "Corretiva":
                        cbxAtv = new DefaultComboBoxModel(
                                    new Object[] {
                                        "Selecionar...",
                                        "Substituição",
                                        "Reparo",
                                        "Modificação",
                                        "Ajuste",
                                        "Conserto",
                                        "Checagem",
                                        "Revisão"});
                        cbxAtividade.setEnabled(true);
                        lblAtividade.setEnabled(true);
                        break;
                        
                    case "Preventiva":
                        cbxAtv = new DefaultComboBoxModel(
                                    new Object[] {
                                        "Selecionar...",
                                        "Substituição",
                                        "Modificação",
                                        "Ajuste",
                                        "Conserto",
                                        "Serviço",
                                        "Teste",
                                        "Inspeção",
                                        "Revisão"});
                        cbxAtividade.setEnabled(true);
                        lblAtividade.setEnabled(true);
                        break;
                    case "Selecionar...":
                        cbxAtividade.setEnabled(false);
                        lblAtividade.setEnabled(false);
                        break;
                    default:
                        break;
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
            
            cbxAtividade.setModel(cbxAtv);
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
            if(pnlUnidade.unidade.getIdBD() == 0) {
                mensagemErro = "Selecione ao menos uma unidade.";
            }
            else if(cbxCategoria.getSelectedItem().equals(
                        "Selecionar...")) {
                mensagemErro = "Selecione uma categoria.";
            }
            else if(cbxAtividade.getSelectedItem().equals(
                        "Selecionar...")) {
                mensagemErro = "Selecione uma atividade.";
            }
            else if(tfdDataInicio.getText().contains(" ")) {
                mensagemErro = "Informe uma data de início válida.";
            }
            else if(horaInicio.eValido() == false) {
                mensagemErro = "Informe uma hora de início válida.";
            }
            else if(ckboxTermino.isSelected() &&
                (tfdDataTermino.getText().contains(" ") || 
                    horaTermino.eValido() == false)) {

                mensagemErro = "Caso informado, o término da intervenção " +
                            "deve ser composto por data e hora.";
            }
            // Se não houver erro, executa a operação
            if(mensagemErro.isEmpty()) {
                System.out.println("opção CONFIRMAR selecionada");
                confirmar();
                dialog.dispose();
            } else {
                // Se houver erro, exibe mensagem de erro
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