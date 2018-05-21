package mesor.menu.adicionar;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.MaskFormatter;
import javax.swing.table.DefaultTableModel;

import java.text.*;
import java.text.ParseException;

import mesor.intervencao.Interventor;
import mesor.intervencao.Equipe;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mesor.menu.DialogoAviso;

import mesor.menu.Janela;
import mesor.menu.painel.taxonomia.PainelInterventor;

/**
 * JanelaAdicionarEquipe.java
 * 
 * @versio 1.0 16/02/2017
 * @author Rubens Jr
 */
public class JanelaAdicionarInterventor extends Janela {
    
    public JPanel pnlObjetivosEspecificos, pnlHabilidadesRequeridas,
            pnlExperienciasRequeridas;
    
    public JLabel lblNome, lblSexo, lblNascimento, lblAdmissao, lblCargo,
            lblFormacao, lblEsp, lblRemuneracao, lblEstadoCivil, lblEndereco,
            lblCidade, lblEstado, lblContato;
    
    public JTextField tfdObjetivoGeral, tfdObjEspecificos, tfdHabRequeridas,
            tfdExpRequeridas, tfdNome, tfdSexo, tfdCargo, tfdFormacao,
            tfdEsp, tfdEstadoCivil, tfdEndereco, tfdCidade;
    
    public JFormattedTextField tfdNascimento, tfdAdmissao, tfdRemuneracao, tfdContato;
    
    public JComboBox cbxEstado, cbxSexo, cbxECivil;
    
    public JTable tabEquipe;
    
    public DefaultTableModel modeloTabInterventor;
    
    public JList<String> listaObjEspecificos, listaHabRequeridas, 
                listaExpRequeridas;
    
    public Equipe equipe = new Equipe();
    public PainelInterventor pnlIntv = new PainelInterventor(600,400);
    public Interventor interventor;
    
    public JPanel pnlBtnEqp;
    public JPanel pnlNovoInterventor;
    
    public JButton btnAddExperiencia, btnRemExperiencia, btnAddHabilidade,
                btnRemHabilidade, btnAddObjetivo, btnRemObjetivo,
                btnAddInterventor, btnRemInterventor;
    
    public DefaultListModel modeloListaObjEspecificos;
    public DefaultListModel modeloListaHabRequeridas;
    public DefaultListModel modeloListaExpRequeridas;
    
    /**
     * Construtor.
     */
    public JanelaAdicionarInterventor() {}    
    
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
                            LOCAL + "\\ajuda\\janelaAdicionarTime.html");
                try {
                    p.setPage(ajudaHTML.toURL());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                
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
    public JPanel montarPainelPrincipal() {
        /* Cria os botões antes de montar o painel Principal */
        criarBotoesOpcoes();
        
        // Painel Final
        pnlPrincipal = new JPanel(new FlowLayout());
        pnlPrincipal.add(painelGeralInterventor());
        return pnlPrincipal;
    }
    
    @Override
    public void limparTexto() {
        tfdNome.setText("");
        tfdSexo.setText("");
        tfdNascimento.setText("");
        tfdAdmissao.setText("");
        tfdCargo.setText("");
        tfdFormacao.setText("");
        tfdEsp.setText("");
        tfdRemuneracao.setText("0,00");
        tfdEstadoCivil.setText("");
        tfdEndereco.setText("");
        tfdCidade.setText("");
        tfdContato.setText("");
    }
    
    @Override
    public void confirmar() {        
        // Define os interventores da equipe: os adicionados a tabela
        for(int it = 0; it < tabEquipe.getRowCount(); it++) {
            /**
             * Cria um objeto da classe Interventor para cada linha da tabela.
             * Configura este objeto com os dados da linha correspondente.
             */
            interventor = new Interventor();
            interventor.setNome(
                        String.valueOf(tabEquipe.getValueAt(it, 0)));
            interventor.setSexo(
                        String.valueOf(tabEquipe.getValueAt(it, 1)));
            interventor.setNascimento(
                        String.valueOf(tabEquipe.getValueAt(it, 2)));
            interventor.setAdmissao(
                        String.valueOf(tabEquipe.getValueAt(it, 3)));
            interventor.setCargo(
                        String.valueOf(tabEquipe.getValueAt(it, 4)));
            interventor.setFormacao(
                        String.valueOf(tabEquipe.getValueAt(it, 5)));
            interventor.setEspecializacao(
                        String.valueOf(tabEquipe.getValueAt(it, 6)));
            interventor.setRemuneracao(
                        String.valueOf(tabEquipe.getValueAt(it, 7)));
            interventor.setEstadoCivil(
                        String.valueOf(tabEquipe.getValueAt(it, 8)));
            interventor.setEndereco(
                        String.valueOf(tabEquipe.getValueAt(it, 9)));
            interventor.setCidade(
                        String.valueOf(tabEquipe.getValueAt(it, 10)));
            
            // Adiciona o interventor ao Vetor de interventores.
            equipe.setInterventor(interventor);
        }
        
        equipe.sqlInserir();
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
        
    /**
     * Monta o painel com os campos para criação, alteração e/ou exclusão de
     * interventores da equipe. Também adiciona a este painel a tabela da
     * equipe, chamada pelo método {@code painelTabelaInterventores()}.
     * 
     * @see JanelaAdicionarEquipe#painelTabelaEquipe()
     * 
     * @return Um JPanel.
     */
    public JPanel painelGeralInterventor() {
        pnlNovoInterventor = new JPanel(new BorderLayout());
        pnlNovoInterventor.setOpaque(true);
        pnlNovoInterventor.add(painelAdicionarInterventor(), BorderLayout.WEST);
        pnlNovoInterventor.add(painelTabelaInterventores(), BorderLayout.EAST);
        return pnlNovoInterventor;
    }
    
    /**
     * Monta o painel com campos de dados dos interventores.
     * 
     * @return 
     */
    public JPanel painelInterventor() {
        lblNome = new JLabel("Nome: *");
        lblSexo = new JLabel("Sexo: ");
        lblNascimento = new JLabel("Data de nascimento: ");
        lblAdmissao = new JLabel("Data de admissão: ");
        lblCargo = new JLabel("Cargo: ");
        lblFormacao = new JLabel("Formação: ");
        lblEsp = new JLabel("Especializações: ");
        lblRemuneracao = new JLabel("Remuneração: ");
        lblEstadoCivil = new JLabel("Estado civil: ");
        lblEndereco = new JLabel("Endereço: ");
        lblCidade = new JLabel("Cidade: ");
        lblEstado = new JLabel("Estado: ");
        lblContato = new JLabel("Contato: ");
        
        tfdNome = new JTextField(20);   tfdEstadoCivil = new JTextField(20);
        tfdSexo = new JTextField(20);   tfdEndereco = new JTextField(20);
        tfdCargo = new JTextField(20);  tfdFormacao = new JTextField(20);
        tfdEsp = new JTextField(20);    tfdCidade = new JTextField(20);
        
        MaskFormatter mask = null;
        try {
            mask = new MaskFormatter("(##) #####-####");
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
//        mask.setValidCharacters("0123456789");
        tfdContato = new JFormattedTextField(mask);
        
        // Define o fomato numérico do JFormattedTextField tfdRemuneração.
        DecimalFormat formatoDecimal = new DecimalFormat();
        formatoDecimal.setMaximumFractionDigits(2);
        formatoDecimal.setMinimumFractionDigits(2);
        
        NumberFormatter numFormatter = new NumberFormatter(formatoDecimal);
        numFormatter.setMinimum(0.0);
        numFormatter.setMaximum(1000000000.0);
        numFormatter.setAllowsInvalid(false);
        numFormatter.setOverwriteMode(true);
                
        // Cria o JFormattedTextField tfdRemuneração.
        tfdRemuneracao = new JFormattedTextField(numFormatter);
        tfdRemuneracao.setText("0,00");
        tfdRemuneracao.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                tfdRemuneracao.setSelectionStart(0);
                tfdRemuneracao.setSelectionEnd(
                            tfdRemuneracao.getText().length());
            }
            @Override
            public void focusLost(FocusEvent e) {}});
        
        // Cria o JFormattedTextField tfdNascimento.
        tfdNascimento = new JFormattedTextField();
        tfdNascimento.setFocusLostBehavior(JFormattedTextField.PERSIST);
                
        tfdNascimento.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                tfdNascimento.setSelectionStart(0);
                tfdNascimento.setSelectionEnd(10);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if(tfdNascimento.getText().contains(" ")) {
                    tfdNascimento.setText("");
                }
            }});
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.install(tfdNascimento);
            mf.setPlaceholder("DD/MM/AAAA");
            mf.setValidCharacters("0123456789");
        } catch (ParseException ex) { ex.printStackTrace();}
        
        // Cria o JFormattedTextField tfdAdmissao.
        tfdAdmissao = new JFormattedTextField();
        tfdAdmissao.setFocusLostBehavior(JFormattedTextField.PERSIST);
        tfdAdmissao.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                tfdAdmissao.setSelectionStart(0);
                tfdAdmissao.setSelectionEnd(10);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if(tfdAdmissao.getText().contains(" ")) {
                    tfdAdmissao.setText("");
                }
            }});
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.install(tfdAdmissao);
            mf.setPlaceholder("DD/MM/AAAA");
            mf.setValidCharacters("0123456789");
        } catch (ParseException ex) { ex.printStackTrace();}
        
        // Cria a JComboBox de seleção do sexo
        cbxSexo = new JComboBox(new Object[] {
                    "Selecionar...", "Feminino", "Masculino", "Outro"});
        cbxSexo.setEditable(false);
        cbxSexo.setPreferredSize(new Dimension(275, 20));
        
        // Cria a JComboBox de seleção do estado civil
        cbxECivil = new JComboBox(new Object[] {
                    "Selecionar...", "Solteiro(a)", "Casado(a)", "Separado(a)",
                    "Divorciado(a)", "Viúvo(a)", "Outro"});
        cbxECivil.setEditable(false);
        cbxECivil.setPreferredSize(new Dimension(275, 20));
        
        // Cria a JComboBox de seleção do estado
        cbxEstado = new JComboBox(new Object[] {
            "Selecionar...","AC","AL","AP","AM","BA","CE","DF","ES","GO","MA",
            "MT","MS","MG","PA","PB","PR","PE","PI","RJ","RN","RS","RO","RR",
            "SC","SP","SE","TO"});
        cbxEstado.setEditable(false);
        cbxEstado.setPreferredSize(new Dimension(275, 20));
        
        // Monta o painel com JLabels e JTextFields.
        JPanel pnlEqp1 = new JPanel(new GridLayout(0,1,0,8));
        pnlEqp1.add(lblNome);
        pnlEqp1.add(lblSexo);
        pnlEqp1.add(lblNascimento);
        pnlEqp1.add(lblAdmissao);
        pnlEqp1.add(lblCargo);
        pnlEqp1.add(lblFormacao);
        pnlEqp1.add(lblEsp);
        pnlEqp1.add(lblRemuneracao);
        pnlEqp1.add(lblEstadoCivil);
        pnlEqp1.add(lblEndereco);
        pnlEqp1.add(lblCidade);
        pnlEqp1.add(lblEstado);
        pnlEqp1.add(lblContato);
        
        JPanel pnlEqp2 = new JPanel(new GridLayout(0,1,0,4));
        pnlEqp2.add(tfdNome);
        pnlEqp2.add(cbxSexo);
        pnlEqp2.add(tfdNascimento);
        pnlEqp2.add(tfdAdmissao);
        pnlEqp2.add(tfdCargo);
        pnlEqp2.add(tfdFormacao);
        pnlEqp2.add(tfdEsp);
        pnlEqp2.add(tfdRemuneracao);
        pnlEqp2.add(cbxECivil);
        pnlEqp2.add(tfdEndereco);
        pnlEqp2.add(tfdCidade);
        pnlEqp2.add(cbxEstado);
        pnlEqp2.add(tfdContato);
        
        JPanel pnlEqp3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlEqp3.add(pnlEqp1);
        pnlEqp3.add(pnlEqp2);
        
        return pnlEqp3;
    }
    
    public JPanel painelAdicionarInterventor() {
        // Botão "Adicionar" interventor.
        btnAddInterventor = new JButton("Adicionar");
        btnAddInterventor.setPreferredSize(new Dimension(90, 20));
        btnAddInterventor.addActionListener(new AddInterventor());

        // Painel com botões.
        pnlBtnEqp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtnEqp.add(btnAddInterventor);
//        pnlBtnEqp.add(btnRemInterventor);
        
        // Painel "Interventor".
        JPanel pnlEqp4 = new JPanel(new BorderLayout());
        pnlEqp4.setOpaque(true);
        pnlEqp4.add(painelInterventor(), BorderLayout.NORTH);
        pnlEqp4.add(pnlBtnEqp, BorderLayout.CENTER);
        
        // Painel auxiliar com FlowLayout
        JPanel pnlEqp5 = new JPanel(new FlowLayout());
        pnlEqp5.setOpaque(true);
        pnlEqp5.add(pnlEqp4);
        
        return pnlEqp5;
    }
    
    public JPanel painelExcluirInterventor() {
        // Botão "Remover" interventor.
        btnRemInterventor = new JButton("Remover");
        btnRemInterventor.setPreferredSize(new Dimension(90, 20));
        btnRemInterventor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(pnlIntv.tabInterventor.getSelectedRowCount() == 1) {
                    modeloTabInterventor.removeRow(pnlIntv.tabInterventor.getSelectedRow());
                    limparTexto();
                }
            }
        });

        // Painel com botões.
        pnlBtnEqp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtnEqp.add(btnRemInterventor);
//        pnlBtnEqp.add(btnRemInterventor);
        
        // Painel "Interventor".
        JPanel pnlEqp4 = new JPanel(new BorderLayout());
        pnlEqp4.setOpaque(true);
        pnlEqp4.add(painelInterventor(), BorderLayout.NORTH);
        pnlEqp4.add(pnlBtnEqp, BorderLayout.CENTER);
        
        // Painel auxiliar com FlowLayout
        JPanel pnlEqp5 = new JPanel(new FlowLayout());
        pnlEqp5.setOpaque(true);
        pnlEqp5.add(pnlEqp4);
        
        return pnlEqp5;
    }
    
    /**
     * Monta o painel com a tabela da equipe de intervenção.
     * 
     * @return 
     */
    public JPanel painelTabelaInterventores() {
        tabEquipe = new JTable();
        
        JScrollPane ePane = new JScrollPane(pnlIntv.painelTabelas(),
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabEquipe.setFillsViewportHeight(true);
        tabEquipe.setPreferredScrollableViewportSize(new Dimension(600, 400));
        tabEquipe.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabEquipe.setCellSelectionEnabled(false);
        tabEquipe.setShowGrid(true);
        tabEquipe.setRowSelectionAllowed(true);
        
        // Cria o DefaultTableModel "modeloTabInterventor"
        modeloTabInterventor = new DefaultTableModel(
            // Nomes das colunas.
            new Object[]{
                "Nome", "Sexo", "Data de nascimento", "Data de admissão",
                "Cargo", "Formação", "Remuneração", "Estado Civil", "Endereço",
                "Cidade"}, 0) {
                // Sobrescreve o método booleano isCellEditable.
                @Override
                public boolean isCellEditable(int row, int column) {
                    //todas as células não são editáveis.
                    return false;
                }
            };
        
        tabEquipe.setModel(modeloTabInterventor);
        
        // Ajusta a largura padrão das colunas da tabela.
        tabEquipe.getColumnModel().getColumn(0).setPreferredWidth(280);
        tabEquipe.getColumnModel().getColumn(1).setPreferredWidth(70);
        tabEquipe.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabEquipe.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabEquipe.getColumnModel().getColumn(4).setPreferredWidth(180);
        tabEquipe.getColumnModel().getColumn(5).setPreferredWidth(180);
        tabEquipe.getColumnModel().getColumn(6).setPreferredWidth(90);
        tabEquipe.getColumnModel().getColumn(7).setPreferredWidth(90);
        tabEquipe.getColumnModel().getColumn(8).setPreferredWidth(280);
        tabEquipe.getColumnModel().getColumn(9).setPreferredWidth(150);
        
//        /**
//         * MouseListener define as informações da linha selecionada como texto
//         * dos jtextfields do painelInterventor.
//         */
//        tabEquipe.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if(tabEquipe.getSelectedRowCount() == 1) {
//                    tfdNome.setText(String.valueOf(
//                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),0)));
//                    tfdSexo.setText(String.valueOf(
//                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),1)));
//                    tfdNascimento.setText(String.valueOf(
//                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),2)));
//                    tfdAdmissao.setText(String.valueOf(
//                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),3)));
//                    tfdCargo.setText(String.valueOf(
//                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),4)));
//                    tfdFormacao.setText(String.valueOf(
//                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),5)));
//                    tfdRemuneracao.setText(String.valueOf(
//                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),6)));
//                    tfdEstadoCivil.setText(String.valueOf(
//                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),7)));
//                    tfdEndereco.setText(String.valueOf(
//                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),8)));
//                    tfdCidade.setText(String.valueOf(
//                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),9)));
//                }
//            }
//            @Override
//            public void mousePressed(MouseEvent e) {}
//            @Override
//            public void mouseReleased(MouseEvent e) {}
//            @Override
//            public void mouseEntered(MouseEvent e) {}
//            @Override
//            public void mouseExited(MouseEvent e) {}}); // Fim do MouseListener
                
        JPanel pnlTabEqp = new JPanel(new FlowLayout());
        pnlTabEqp.setOpaque(true);
        pnlTabEqp.add(ePane);
        return pnlTabEqp;
    }
        
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionSalvar
     */
    private class AddInterventor extends Janela.ActionSalvar {
        @Override
        public void actionPerformed(ActionEvent event) {
            /* Lida com erros de dados do usuário */
            String mensagemErroInterventor;

            if(tfdNome.getText().isEmpty() || 
                        tfdNome.getText().startsWith(" ")) {

                DialogoAviso.show("Informe um nome válido");
                
            } else {
            
//            String regex1 = "[a-zA-Z]";
//            String regex2 = "\\s";
//            Pattern p1 = Pattern.compile(regex1);
//            Pattern p2 = Pattern.compile(regex2);
//            Matcher m1 = p1.matcher(tfdContato.getText());
//            Matcher m2 = p2.matcher(tfdContato.getText());
//            if(m1.find() || m2.find()) {
//                DialogoAviso.show("Informe um contato válido. Utilize apenas números.");
//            } else {
                // Se não houver erro, executa a operação.                    
                interventor = new Interventor();
                interventor.setNome(tfdNome.getText());
                interventor.setSexo(cbxSexo.getSelectedItem().toString());
                interventor.setNascimento(tfdNascimento.getText());
                interventor.setAdmissao(tfdAdmissao.getText());
                interventor.setCargo(tfdCargo.getText());
                interventor.setFormacao(tfdFormacao.getText());
                interventor.setEspecializacao(tfdEsp.getText());
                interventor.setRemuneracao(tfdRemuneracao.getText());
                interventor.setEstadoCivil(cbxECivil.getSelectedItem().toString());
                interventor.setEndereco(tfdEndereco.getText());
                interventor.setEstado(cbxEstado.getSelectedItem().toString());
                interventor.setCidade(tfdCidade.getText());
                interventor.setContato(tfdContato.getText());

                interventor.sqlInserir();
                pnlIntv.reiniciarTabela();
                pnlIntv.atualizarAparenciaDaTabela();

                limparTexto();
            }
        }
    }
}