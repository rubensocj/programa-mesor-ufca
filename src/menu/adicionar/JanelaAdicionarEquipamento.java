package menu.adicionar;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.text.MaskFormatter;

import java.text.ParseException;

import equipamento.*;

import menu.JanelaAdicionarAlterar;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * JanelaAdicionarEquipamento.Java
 * 
 * @version 1.0 31/01/2017
 * @author Rubens Jr
 */
public class JanelaAdicionarEquipamento extends JanelaAdicionarAlterar {
    
    public JPanel pnlUniGeral, pnlUni1, pnlUniOp, pnlUni2, pnlSubInfo, pnl1,
        pnl2, pnlCompInfo, pnl3, pnl4, pnlParte, pnl5, pnl6, pnlFinal,
        pnlEsquerda, pnl7, pnl8, pnl9;
    
    public JLabel lblUniCategoria, lblUniClasse, lblUniTipo, lblUniFabricante,
        lblUniIdentificacao, lblUniLocal, lblUniDataAq, lblUniDataOp,
        lblUniModo;
    
    public JTextField tfdUniCategoria, tfdUniClasse, tfdUniTipo,
        tfdUniFabricante, tfdUniIdentificacao, tfdUniLocal, tfdSubDescricao,
        tfdCompDescricao, tfdPteDescricao;
    
    public JFormattedTextField tfdUniDataAq, tfdUniDataOp;
    
    private JButton btnSubAdicionar, btnSubRemover, btnCompAdicionar,
        btnCompRemover, btnPteAdicionar, btnPteRemover;
    
    public JComboBox cbxModo, cbxSubunidade, cbxComponente, cbxParte;
    
    public Unidade unidade = new Unidade();    
    private final String eventSelected = "ActionEvent";
    
    /**
     * Construtor.
     */
    public JanelaAdicionarEquipamento() {}
    
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
                            LOCAL + "\\ajuda\\janelaAdicionarEquipamento.html");
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
        /*
        
        pnlEsquerda = new JPanel(new BorderLayout());
        pnlEsquerda.add(painelGeral(), BorderLayout.NORTH);
        pnlEsquerda.add(painelOperacional(), BorderLayout.CENTER);
                
        pnlPrincipal = new JPanel(new BorderLayout());
        pnlPrincipal.add(pnlEsquerda, BorderLayout.WEST);
        pnlPrincipal.add(painelSubunidades(), BorderLayout.CENTER);
        
        */
        pnlEsquerda = new JPanel(new BorderLayout());
        pnlEsquerda.add(painelGeral(), BorderLayout.NORTH);
        pnlEsquerda.add(painelOperacional(), BorderLayout.CENTER);
        
        pnlPrincipal = new JPanel(new BorderLayout());
        try {
            pnlPrincipal.add(painelSistema(), BorderLayout.NORTH);
        } catch (SQLException ex) {ex.printStackTrace();}
        pnlPrincipal.add(pnlEsquerda, BorderLayout.WEST);
        pnlPrincipal.add(painelSubunidades(), BorderLayout.CENTER);
        return pnlPrincipal;
    }
    
    @Override
    public void confirmar() {
        // Se não for informada a data de aquisição, String dataAquisição = "".
        String dataAquisicao;
        if(tfdUniDataAq.getText().contains(" ")) {
            dataAquisicao = "";
        } else {
            dataAquisicao = tfdUniDataAq.getText().substring(6,10).concat(
            "-" + tfdUniDataAq.getText().substring(3,5)).concat(
            "-" + tfdUniDataAq.getText().substring(0,2));
        }
        
        // Se não for informada a data de aquisição, String dataAquisição = "".
        String dataInicio;
        if(tfdUniDataOp.getText().contains(" ")) {
            dataInicio = "";
        } else {
            dataInicio = tfdUniDataOp.getText().substring(6,10).concat(
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
        unidade.setModoOperacional((String) cbxModo.getSelectedItem());
        unidade.setDataInicioOperacao(dataInicio);

        unidade.adicionaUnidade();
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /**
     * Monta a área para inserção de informações gerais.
     * 
     * @return JPanel.
     */
    public JPanel painelGeral() {
        lblUniClasse = new JLabel("Classe: *");
        lblUniTipo = new JLabel("Tipo: *");
        lblUniFabricante = new JLabel("Fabricante: *");
        lblUniIdentificacao = new JLabel("Identificação: *");
        lblUniCategoria = new JLabel("Categoria: ");
        lblUniLocal = new JLabel("Localização: ");
        lblUniDataAq = new JLabel("Data de aquisição: ");
        
        tfdUniClasse = new JTextField(20);
        tfdUniTipo = new JTextField(20);
        tfdUniFabricante = new JTextField(20);
        tfdUniIdentificacao = new JTextField(20);
        tfdUniCategoria = new JTextField(20);
        tfdUniLocal = new JTextField(20);
        
        tfdUniDataAq = new JFormattedTextField();
        tfdUniDataAq.setFocusLostBehavior(JFormattedTextField.PERSIST);
        tfdUniDataAq.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                tfdUniDataAq.setSelectionStart(0);
                tfdUniDataAq.setSelectionEnd(10);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if(tfdUniDataAq.getText().contains(" ")) {
                    tfdUniDataAq.setText("");
                }
            }});
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.install(tfdUniDataAq);
            mf.setPlaceholder("DD/MM/AAAA");
            mf.setValidCharacters("0123456789");
        } catch (ParseException ex) { ex.printStackTrace();}
        
        // Adiciona os componentes ao JPanel.
        JPanel pnlGeral1 = new JPanel(new GridLayout(0,1,0,8));
        pnlGeral1.add(lblUniClasse);
        pnlGeral1.add(lblUniTipo);
        pnlGeral1.add(lblUniFabricante);
        pnlGeral1.add(lblUniIdentificacao);
        pnlGeral1.add(lblUniCategoria);
        pnlGeral1.add(lblUniLocal);
        pnlGeral1.add(lblUniDataAq);
        
        JPanel pnlGeral2 = new JPanel(new GridLayout(0,1,0,4));
        pnlGeral2.add(tfdUniClasse);
        pnlGeral2.add(tfdUniTipo);
        pnlGeral2.add(tfdUniFabricante);
        pnlGeral2.add(tfdUniIdentificacao);
        pnlGeral2.add(tfdUniCategoria);
        pnlGeral2.add(tfdUniLocal);
        pnlGeral2.add(tfdUniDataAq);
        
        pnlUniGeral = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlUniGeral.add(pnlGeral1);      pnlUniGeral.add(pnlGeral2);
        
        pnlUni1 = new JPanel(new FlowLayout());
        pnlUni1.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                            "Informações gerais"));
        pnlUni1.setOpaque(true);
        pnlUni1.add(pnlUniGeral);
        return pnlUni1;
    }
    
    /**
     * Monta a área para inserção de informações de operação.
     * 
     * @return Um JPanel.
     */
    public JPanel painelOperacional() {
        lblUniModo = new JLabel("Modo operacional normal: *");
        lblUniDataOp = new JLabel("Data de início de operação: *");
        
        cbxModo = new JComboBox(
                    new Object[] {
                            "Selecionar...", "Executando", "Inicializando",
                            "Testando", "Inativo", "Em espera"});
        cbxModo.setPreferredSize(new Dimension(80, 20));
        
        tfdUniDataOp = new JFormattedTextField();
        tfdUniDataOp.setColumns(15);
        tfdUniDataOp.setFocusLostBehavior(JFormattedTextField.PERSIST);
        tfdUniDataOp.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                tfdUniDataOp.setSelectionStart(0);
                tfdUniDataOp.setSelectionEnd(10);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if(tfdUniDataOp.getText().contains(" ")) {
                    tfdUniDataOp.setText("");
                }
            }});
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.install(tfdUniDataOp);
            mf.setPlaceholder("DD/MM/AAAA");
            mf.setValidCharacters("0123456789");
        } catch (ParseException ex) { ex.printStackTrace();}
        
        pnlUniOp = new JPanel(new GridLayout(0,2,0,5));
        pnlUniOp.add(lblUniModo);       pnlUniOp.add(cbxModo);
        pnlUniOp.add(lblUniDataOp);     pnlUniOp.add(tfdUniDataOp);
        
        pnlUni2 = new JPanel(new FlowLayout());
        pnlUni2.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                            "Informações de operação"));
        pnlUni2.setOpaque(true);
        pnlUni2.add(pnlUniOp);
        return pnlUni2;
    }
    
    /**
     * Monta a área para definição dos níveis taxonômicos mais baixos:
     * subunidade, componentes e partes.
     * 
     * @return Um JPanel.
     */
    private JPanel painelSubunidades() {
        
        /* Gerenciar subunidades */
        tfdSubDescricao = new JTextField(25);
        tfdSubDescricao.addActionListener(new AdicionarSubunidade());
        
        cbxSubunidade = new JComboBox(unidade.getVetorSubunidade());
        cbxSubunidade.addItemListener(new ItemEventSubunidade());
        cbxSubunidade.setMaximumRowCount(5);
        cbxSubunidade.setEditable(false);
        cbxSubunidade.setPreferredSize(new Dimension(275, 20));
        
        // Cria JButtons "Adicionar" e "Remover".
        btnSubAdicionar = new JButton("Adicionar");
        btnSubAdicionar.addActionListener(new AdicionarSubunidade());
        btnSubAdicionar.setPreferredSize(new Dimension(90, 20));
        btnSubRemover = new JButton("Remover");
        btnSubRemover.addActionListener(new RemoverSubunidade());
        btnSubRemover.setPreferredSize(new Dimension(85, 20));
        
        // Painel que guarda JTextField "Descrição" e JButton "Adicionar".
        pnl1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl1.add(tfdSubDescricao);
        pnl1.add(btnSubAdicionar);
        
        // Painel que guarda JComboBox "Subunidades" e JButton "Remover".
        pnl2 = new JPanel(new FlowLayout(0,8,0));
        pnl2.add(cbxSubunidade);
        pnl2.add(btnSubRemover);
        
        // Painel final.
        pnlSubInfo = new JPanel(new GridLayout(2,0,0,0));
        pnlSubInfo.add(pnl1);
        pnlSubInfo.add(pnl2);
        
        pnl7 = new JPanel(new BorderLayout());
        pnl7.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                            "Definir subunidades"));
        pnl7.add(pnlSubInfo);
        
        /* Gerenciar componentes */
        tfdCompDescricao = new JTextField(25);
        tfdCompDescricao.addActionListener(new AdicionarComponente());
        
        cbxComponente = new JComboBox();
        cbxComponente.addItemListener(new ItemEventComponente());
        cbxComponente.setMaximumRowCount(5);
        cbxComponente.setEditable(false);
        cbxComponente.setPreferredSize(new Dimension(275, 20));
        
        // Cria JButtons "Adicionar" e "Remover".
        btnCompAdicionar = new JButton("Adicionar");
        btnCompAdicionar.addActionListener(new AdicionarComponente());
        btnCompAdicionar.setPreferredSize(new Dimension(90, 20));
        btnCompRemover = new JButton("Remover");
        btnCompRemover.addActionListener(new RemoverComponente());
        btnCompRemover.setPreferredSize(new Dimension(90, 20));
        
        // Painel que guarda JTextField "Descrição" e JButton "Adicionar".
        pnl3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl3.add(tfdCompDescricao);
        pnl3.add(btnCompAdicionar);
        
        // Painel que guarda JComboBox "Componentes" e JButton "Remover".
        pnl4 = new JPanel(new FlowLayout(0,7,0));
        pnl4.add(cbxComponente);
        pnl4.add(btnCompRemover);
        
        // Painel final.
        pnlCompInfo = new JPanel(new GridLayout(2,0,0,0));
        pnlCompInfo.add(pnl3);
        pnlCompInfo.add(pnl4);
        
        pnl8 = new JPanel(new BorderLayout());
        pnl8.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                            "Definir componentes"));
        pnl8.add(pnlCompInfo);
        
        /* Gerenciar partes */
        tfdPteDescricao = new JTextField(25);
        tfdPteDescricao.addActionListener(new AdicionarParte());
        
        cbxParte = new JComboBox();
        cbxParte.setMaximumRowCount(5);
        cbxParte.setEditable(false);
        cbxParte.setPreferredSize(new Dimension(275, 20));
        
        // Cria JButtons "Adicionar" e "Remover".
        btnPteAdicionar = new JButton("Adicionar");
        btnPteAdicionar.addActionListener(new AdicionarParte());
        btnPteAdicionar.setPreferredSize(new Dimension(90, 20));
        btnPteRemover = new JButton("Remover");
        btnPteRemover.addActionListener(new RemoverParte());
        btnPteRemover.setPreferredSize(new Dimension(90, 20));
        
        // Painel que guarda JTextField "Descrição" e JButton "Adicionar".
        pnl5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl5.add(tfdPteDescricao);
        pnl5.add(btnPteAdicionar);
        
        // Painel que guarda JComboBox "Partes" e JButton "Remover".
        pnl6 = new JPanel(new FlowLayout(0,7,0));
        pnl6.add(cbxParte);
        pnl6.add(btnPteRemover);
        
        // Painel final.
        pnlParte = new JPanel(new GridLayout(2,0,0,0));
        pnlParte.add(pnl5);  
        pnlParte.add(pnl6);
        
        pnl9 = new JPanel(new BorderLayout());
        pnl9.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                "Definir partes"));
        pnl9.add(pnlParte);
        
        pnlFinal = new JPanel(new GridLayout(3,0));
        pnlFinal.add(pnl7);
        pnlFinal.add(pnl8);
        pnlFinal.add(pnl9);
        
        return pnlFinal;
    }
    
    // -------------------------------------------------------------------------
    // Classes.
    // -------------------------------------------------------------------------
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionSalvar
     */
    private class Salvar extends JanelaAdicionarAlterar.ActionSalvar {
        @Override
        public void actionPerformed(ActionEvent e) {
            String mensagemErro = "";
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
    
    /**
     * Adiciona um elemento ao vetor de subunidades do objeto da classe Unidade,
     * através do construtor {@code Subunidade(String descricao)}.
     * A descrição é o texto inserido no JTextField tfdSubDescricao.
     */
    private class AdicionarSubunidade implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(tfdSubDescricao.getText().isEmpty() == false || 
                        tfdSubDescricao.getText().startsWith(" ") == false) {
                
                Subunidade sub = new Subunidade(tfdSubDescricao.getText());

                unidade.setSubunidade(sub);
                
                /**
                 * Define o elemento recém inserido como item selecionado no
                 * JComboBox das subunidades.
                 */
                cbxSubunidade.setSelectedItem(
                        unidade.getVetorSubunidade().lastElement());
                
                // Limpa o JTextField.
                tfdSubDescricao.setText("");
            }
        }
    }
    
    /**
     * Toma como objeto Subunidade o item selecionado na JComboBox 
     * cbxSubunidade. Adiciona um elemento ao vetor de componentes deste objeto
     * Subunidade, através do construtor {@code Componente(String descricao)}.
     * A descrição é o texto inserido no JTextField tfdCompDescricao.
     */
    private class AdicionarComponente implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(eventSelected.equals("ActionEvent")) {
                Componente comp = new Componente(
                    tfdCompDescricao.getText());

                Subunidade sub = (Subunidade) cbxSubunidade.getSelectedItem();
                sub.setComponente(comp);
                
                /**
                 * Define o elemento recém inserido como item selecionado no
                 * JComboBox dos componentes.
                 */
                cbxComponente.setSelectedItem(
                        sub.getVetorComponente().lastElement());
                
                // Limpa o JTextField.
                tfdCompDescricao.setText("");
            }
        }
    }
    
    /**
     * Toma como objeto Componente o item selecionado na JComboBox 
     * cbxComponente. Adiciona um elemento ao vetor de partes deste objeto
     * Componente, através do construtor {@code Parte(String descricao)}.
     * A descrição é o texto inserido no JTextField tfdPteDescricao.
     */
    private class AdicionarParte implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(eventSelected.equals("ActionEvent")) {
                Parte pt = new Parte(tfdPteDescricao.getText());
                
                Componente comp = (Componente) cbxComponente.getSelectedItem();
                comp.setParte(pt);
                
                /**
                 * Define o elemento recém inserido como item selecionado no
                 * JComboBox das partes.
                 */
                cbxParte.setSelectedItem(comp.getVetorParte().lastElement());
                
                // Limpa o JTextField.
                tfdPteDescricao.setText("");
            }
        }
    }
    
    /**
     * Remove uma subunidade. Remove o elemento do vetor subunidades localizado
     * na posição do item selecionado na JComboBox de subunidades.
     */
    private class RemoverSubunidade implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(eventSelected.equals("ActionEvent")) {
                Subunidade sub = (Subunidade) cbxSubunidade.getSelectedItem();
                
                if(unidade.getVetorSubunidade().size() > 0){
                    // Posição do item selecionado na JComboBox subunidades.
                    int index = unidade.getVetorSubunidade().indexOf(sub);
                    
                    /**
                     * Remove o elemento do vetor subunidades localizado na
                     * mesma posição do item selecionado da JComboBox.
                     */
                    unidade.getVetorSubunidade().removeElement(sub);
                    
                    /**
                     * Define o elemento anterior ao recém excluido como item
                     * selecionado no JComboBox das subunidades.
                     */
                    cbxSubunidade.setSelectedIndex(index - 1);
                }
            }
        }
    }
    
    /**
     * Remove um componente. Remove o elemento do vetor componentes localizado
     * na posição do item selecionado na JComboBox de componentes.
     */
    private class RemoverComponente implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(eventSelected.equals("ActionEvent")) {
                Subunidade sub = (Subunidade) cbxSubunidade.getSelectedItem();
                Componente comp = (Componente) cbxComponente.getSelectedItem();
                
                if(sub.getVetorComponente().size() > 0) {
                    // Posição do item selecionado na JComboBox subunidades.
                    int indexSub = cbxSubunidade.getSelectedIndex();
                    // Posição do item selecionado na JComboBox componentes.
                    int indexCp = sub.getVetorComponente().indexOf(comp);
                    
                    /**
                     * Remove o elemento do vetor componentes localizado na 
                     * mesma posição do item selecionado da JComboBox de
                     * componentes, da subunidade selecionada na JComboBox
                     * de subunidades.
                     */
                    unidade.getVetorSubunidade().elementAt(
                                indexSub).getVetorComponente().remove(comp);
                    
                    /**
                     * Define o elemento anterior ao recém excluido como item
                     * selecionado no JComboBox dos componentes.
                     */
                    cbxComponente.setSelectedIndex(indexCp - 1);
                }
            }
        }
    }
    
    /**
     * Remove uma parte. Remove o elemento do vetor partes localizado
     * na posição do selecionado na JComboBox de partes.
     */
    private class RemoverParte implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(eventSelected.equals("ActionEvent")) {
                Subunidade sub = (Subunidade) cbxSubunidade.getSelectedItem();
                Componente comp = (Componente) cbxComponente.getSelectedItem();
                Parte pt = (Parte) cbxParte.getSelectedItem();
                
                if(comp.getVetorParte().size() > 0) {
                    // Posição do item selecionado na JComboBox subunidades.
                    int indexSub = cbxSubunidade.getSelectedIndex();
                    // Posição do item selecionado na JComboBox componentes.
                    int indexCp = sub.getVetorComponente().indexOf(comp);
                    // Posição do item selecionado na JComboBox partes.
                    int indexPt = comp.getVetorParte().indexOf(pt);
                    
                    /**
                     * Remove o elemento do vetor partes localizado na mesma
                     * posição do item selecionado da JComboBox de partes,
                     * do componente selecionado na JComboBox de componentes,
                     * da subunidade selecionada na JComboBox de subunidades.
                     */
                    unidade.getVetorSubunidade().elementAt(
                                indexSub).getVetorComponente().elementAt(
                                            indexCp).getVetorParte().remove(pt);
                    
                    /**
                     * Define o elemento anterior ao recém excluido como item
                     * selecionado no JComboBox das partes.
                     */
                    cbxParte.setSelectedIndex(indexPt - 1);
                }
            }
        }
    }
    
    /**
     * Define um novo JComboBoxModel para a JComboBox de componentes baseado na
     * subunidade selecionada na JComboBox de subunidades. Este novo modelo é
     * formado pelo vetor de componentes da subunidade.
     */
    private class ItemEventSubunidade implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Subunidade sub = (Subunidade) e.getItem();
                
                DefaultComboBoxModel cbxCp;
                cbxCp = new DefaultComboBoxModel(sub.getVetorComponente());
                cbxComponente.setModel(cbxCp);
            }
        }
    }
    
    /**
     * Define um novo JComboBoxModel para a JComboBox de partes baseado no
     * componente selecionado na JComboBox de componentes. Este novo modelo é
     * formado pelo vetor de partes do componente.
     */
    private class ItemEventComponente implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            Componente comp;
            comp = (Componente) e.getItem();
            
            if (e.getStateChange() == ItemEvent.SELECTED) {
                comp = (Componente) e.getItem();
            }

            DefaultComboBoxModel cbxPt;
            cbxPt = new DefaultComboBoxModel(comp.getVetorParte());
            cbxParte.setModel(cbxPt);
        }
    }
}
