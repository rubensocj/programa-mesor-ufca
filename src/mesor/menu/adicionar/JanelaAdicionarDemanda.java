package mesor.menu.adicionar;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mesor.intervencao.Demanda;

import java.io.File;
import java.io.IOException;

import java.sql.SQLException;
import mesor.menu.DialogoAviso;

import mesor.menu.HoraFormatada;
import mesor.menu.painel.taxonomia.PainelEquipamento;
import mesor.menu.Janela;

/**
 * JanelaAdicionarDemanda.Java
 * 
 * @version 1.0 8/2/2017
 * @author Rubens Jr
 */
public class JanelaAdicionarDemanda extends Janela {
    
    public JLabel lblData, lblHora, lblImpacto, lblCondicaoOperacao;
    
    public JFormattedTextField tfdData;
        
    public Demanda demanda = new Demanda();
    
    public HoraFormatada horaFormatada;
    
    public PainelEquipamento pnlUnidade = new PainelEquipamento();
    
    public ItemEventSistema eventSistema = new ItemEventSistema(pnlUnidade);
    
    public JPanel pnlHoraFormatada;
        
    public JCheckBox modo1, modo2, modo3;
    public JComboBox cbxImpacto, cbxCausaCategoria, cbxCausaSubdivisao,
                cbxCondicaoOperacao;
    
    public String modo = null, impacto = null, causa = null, condicao = null;
    
    /**
     * Construtor.
     */
    public JanelaAdicionarDemanda() {}
    
    // -------------------------------------------------------------------------
    // Métodos sobrepostos.
    // -------------------------------------------------------------------------
    
    @Override
    public void criarBotoesOpcoes() {
        btn1 = new JButton("Confirmar");
        btn2 = new JButton("Cancelar");
        btn3 = criarBotaoAjuda();
        options = new Object[] {this.btn1, this.btn2, this.btn3};
        
        btn1.addActionListener(new Salvar());
        
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("opção CANCELAR selecionada");
                frm.dispose();
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
            cbxSistema.addItemListener(eventSistema);
        } catch (SQLException ex) {
            DialogoAviso.show(ex.getMessage());
            ex.printStackTrace();
        }
        pnlPrincipal.add(pnlUnidade.painelTabelas(), BorderLayout.CENTER);
        pnlPrincipal.add(painelDemanda(), BorderLayout.EAST);
        
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
        // Data da demanda
        String dataString = tfdData.getText().substring(6,10).concat(
            "-" + tfdData.getText().substring(3,5)).concat(
            "-" + tfdData.getText().substring(0,2)) + " " + 
            horaFormatada.getHora();
        
        // Executa a operação
        demanda.setData(dataString);
        demanda.setModo(modo);
        demanda.setImpacto(impacto);
        demanda.setCausa(causa);
        demanda.setModoOperacional(condicao);

        demanda.setUnidade(pnlUnidade.unidade);
        demanda.setSubunidade(pnlUnidade.subunidade);
        demanda.setComponente(pnlUnidade.componente);
        demanda.setParte(pnlUnidade.parte);
        
        demanda.sqlInserir();
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /**
     * Monta a área para alteração dos dados da demanda.
     * 
     * @return Um JPanel.
     */
    public JPanel painelDemanda() {
        lblData = new JLabel("Data: *");
        lblHora = new JLabel("Hora: *");
        lblImpacto = new JLabel("Impacto no funcionamento da unidade: *");
        lblCondicaoOperacao = new JLabel(
                    "Modo de operação durante a falha:   ");
        
        // Campo de inserção de hora formatado.
        horaFormatada = new HoraFormatada();
        pnlHoraFormatada = horaFormatada.adicionar();
        
        /* Modo */
        modo1 = new JCheckBox("Função desejada não obtida.");
        modo1.setActionCommand("1");
        modo1.addActionListener(new ActionModo());
        
        modo2 = new JCheckBox("Função perdida ou fora do limite de operação.");
        modo2.setActionCommand("2");
        modo2.addActionListener(new ActionModo());
        
        modo3 = new JCheckBox(
                    "Indicativo de falha observado, sem impacto imediato.");
        modo3.setActionCommand("3");
        modo3.addActionListener(new ActionModo());
        
        /* Cancela a seleção dos demais caso for selecionado */
        modo1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(modo1.isSelected()) {
                    modo2.setSelected(false);
                    modo3.setSelected(false);}}});
        
        modo2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(modo2.isSelected()) {
                    modo1.setSelected(false);
                    modo3.setSelected(false);}}});
        
        modo3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(modo3.isSelected()) {
                    modo1.setSelected(false);
                    modo2.setSelected(false);}}});
        
        JPanel pnlModo = new JPanel(new GridLayout(0,1,0,-3));  // Painel Modo.
        pnlModo.add(modo1);
        pnlModo.add(modo2);
        pnlModo.add(modo3);
        pnlModo.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEmptyBorder(), "Modo de falha: * "));
        
        /* Impacto */
        cbxImpacto = new JComboBox(new Object[] {"Selecione...",
            "Crítico", "Não crítico", "Degradado", "Incipiente"});
        cbxImpacto.setPrototypeDisplayValue("XXXXXXXXX");
        cbxImpacto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cbxImpacto.getSelectedIndex()!=0) {
                    // Se o item selecionado não for "Selecione...".
                    impacto = cbxImpacto.getSelectedItem().toString();
                } else {impacto = null;} // Caso contrário, impacto = null.
            }
        });
        
        /* Causa */
        cbxCausaCategoria = new JComboBox(new Object[] { // JComboBox Categoria.
            "Selecione uma categoria principal...",
            "1. Falha relacionada ao projeto",
            "2. Falha relacionada a fabricação/instalação",
            "3. Falha relacionada a operação/manutenção",
            "4. Falha relacionada a gestão",
            "5. Diversos"});
        cbxCausaCategoria.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXX");
        
        cbxCausaSubdivisao = new JComboBox(); // JComboBox Subdivisões.
        cbxCausaSubdivisao.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXX");
        cbxCausaSubdivisao.setEnabled(false);
        cbxCausaSubdivisao.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(cbxCausaSubdivisao.getSelectedIndex()!=0) {
                    // Se o item selecionado não for "Selecione...".
                    String causaCbx;
                    causaCbx = cbxCausaSubdivisao.getSelectedItem().toString();
                    causa = causaCbx.substring(0,3);
            } else {causa = null;} // Caso contrário, causa = null.
        }});
        
        // Monta cbxCausaSubdivisoes conforme a seleção em cbxCausaCategoria.
        cbxCausaCategoria.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch (cbxCausaCategoria.getSelectedIndex()) {
                    case 0: cbxCausaSubdivisao.setModel(
                        new DefaultComboBoxModel(new Object[] {}));                        
                        cbxCausaSubdivisao.setEnabled(false);
                    
                        break;
                        
                    case 1: cbxCausaSubdivisao.setModel(
                        new DefaultComboBoxModel(new Object[] {
                            "Selecione uma subdivisão...",
                            "1.0 Geral",
                            "1.1 Capacidade inadequada",
                            "1.2 Material inadequado"
                        }));                        
                        cbxCausaSubdivisao.setEnabled(true);
                    
                        break;
                        
                    case 2: cbxCausaSubdivisao.setModel(
                        new DefaultComboBoxModel(new Object[] {
                            "Selecione uma subdivisão...",
                            "2.0 Geral",
                            "2.1 Erro de fabricação",
                            "2.2 Erro de instalação"
                        }));                    
                        cbxCausaSubdivisao.setEnabled(true);
                        
                        break;
                        
                    case 3: cbxCausaSubdivisao.setModel(
                        new DefaultComboBoxModel(new Object[] {
                            "Selecione uma subdivisão...",
                            "3.0 Geral",
                            "3.1 Serviço fora de projeto",
                            "3.2 Erro de operação",
                            "3.3 Erro de manutenção",
                            "3.4 Desgaste e quebra esperados"
                        }));
                        cbxCausaSubdivisao.setEnabled(true);
                        
                        break;
                        
                    case 4: cbxCausaSubdivisao.setModel(
                        new DefaultComboBoxModel(new Object[] {
                            "Selecione uma subdivisão...",
                            "4.0 Geral",
                            "4.1 Erro de documentação",
                            "4.2 Erro de gestão"
                        }));
                        cbxCausaSubdivisao.setEnabled(true);
                        
                        break;
                        
                    case 5: cbxCausaSubdivisao.setModel(
                        new DefaultComboBoxModel(new Object[] {
                            "Selecione uma subdivisão...",
                            "5.0 Geral",
                            "5.1 Causa não encontrada",
                            "5.2 Causa comum",
                            "5.3 Causas combinadas",
                            "5.4 Outra",
                            "5.5 Desconhecida"
                        }));
                        cbxCausaSubdivisao.setEnabled(true);
                        
                        break;
                        
                    default:break;
                }
            }
        });
        
        JPanel pnlCausa = new JPanel(new GridLayout(0,1,0,4)); // Painel Causa.
        pnlCausa.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createEmptyBorder(), "Causa: "));
        pnlCausa.add(cbxCausaCategoria);
        pnlCausa.add(cbxCausaSubdivisao);
        
        /* Condição de operação da unidade durante a falha */
        cbxCondicaoOperacao = new JComboBox(new Object[] {
            "Selecione...", "Executando", "Inicializando", "Testando",
            "Inativo", "Em espera"});
        cbxCondicaoOperacao.setPrototypeDisplayValue("XXXXXXXXX");
        cbxCondicaoOperacao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cbxCondicaoOperacao.getSelectedIndex()!= 0) {
                    // Se o item selecionado não for "Selecione...".
                    condicao = cbxCondicaoOperacao.getSelectedItem().toString();
                } else {condicao = null;} // Caso contrário, condicao = null.
            }
        });
        
        /* Data da demanda */
        tfdData = new JFormattedTextField();
        tfdData.setColumns(6);
        tfdData.setFocusLostBehavior(JFormattedTextField.PERSIST);
        tfdData.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                tfdData.setSelectionStart(0);
                tfdData.setSelectionEnd(10);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if(tfdData.getText().contains(" ")) {tfdData.setText("");}
            }});
        try {
            javax.swing.text.MaskFormatter mf = new 
                        javax.swing.text.MaskFormatter("##/##/####");
            mf.install(tfdData);
            mf.setPlaceholder("DD/MM/AAAA");
            mf.setValidCharacters("0123456789");
        } catch (java.text.ParseException ex) {
            DialogoAviso.show(ex.getMessage());
            ex.getErrorOffset();
        }
        
        // Painel com data e hora.
        JPanel pnlDem1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlDem1.add(lblData); pnlDem1.add(tfdData);
        pnlDem1.add(lblHora); pnlDem1.add(pnlHoraFormatada);
        
        JPanel pnlSuperior = new JPanel(new BorderLayout(0, 5));
        pnlSuperior.add(pnlDem1, "North");
        pnlSuperior.add(pnlModo, "Center");
        
        // Painel com JLabel Impacto e Condicao de Operaçao.
        JPanel pnllblImpactoCondicao = new JPanel(new GridLayout(0,1,0,8));
        pnllblImpactoCondicao.add(lblImpacto);
        pnllblImpactoCondicao.add(lblCondicaoOperacao);
        
        // Painel com JComboBoxes Impacto e Condicao de Operaçao.
        JPanel pnlcbxImpactoCondicao;
        pnlcbxImpactoCondicao = new JPanel(new GridLayout(0,1,0,4));
        pnlcbxImpactoCondicao.add(cbxImpacto);
        pnlcbxImpactoCondicao.add(cbxCondicaoOperacao);
        
        JPanel pnlInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlInferior.add(pnllblImpactoCondicao);
        pnlInferior.add(pnlcbxImpactoCondicao);
        
        JPanel pnlDemanda1 = new JPanel(new BorderLayout(0,10));
        pnlDemanda1.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Informações gerais"));
        pnlDemanda1.add(pnlSuperior, BorderLayout.NORTH);
        pnlDemanda1.add(pnlInferior, BorderLayout.CENTER);
        pnlDemanda1.add(pnlCausa, BorderLayout.SOUTH);
        
        JPanel pnlDemanda2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlDemanda2.setOpaque(true);
        pnlDemanda2.add(pnlDemanda1);
        return pnlDemanda2;
    }
    
    // -------------------------------------------------------------------------
    // Classes.
    // -------------------------------------------------------------------------
    
    /**
     * Determina o modo da falha de acordo com o JCheckBox selecionado.
     * 
     * 1: Função desejada não obtida (ex. falha ao iniciar).
     * 2: Função específica perdida ou fora dos limites de operação aceitáveis.
     * 3: Indicativo de falha observado, mas não há impacto imediato e crítico
     * no funcionamento do equipamento (falhas tipicamente não críticas
     * relacionadas a alguma degradação ou condição de falha incipiente).
     */
    private class ActionModo implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            modo = event.getActionCommand();
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
            } else if(pnlUnidade.unidade.getIdBD() == 0) {
                mensagemErro = "Selecione ao menos uma unidade.";
            }
            
            /* Se não houver erro, executa a operação */
            if(mensagemErro.isEmpty()) {
                confirmar();
                dialog.dispose();
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