package mesor.menu.adicionar;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.MaskFormatter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.text.*;
import java.text.ParseException;

import mesor.intervencao.Interventor;
import mesor.intervencao.Equipe;

import java.io.File;
import java.io.IOException;

import mesor.menu.JanelaAdicionarAlterar;

/**
 * JanelaAdicionarEquipe.java
 * 
 * @versio 1.0 16/02/2017
 * @author Rubens Jr
 */
public class JanelaAdicionarEquipe extends JanelaAdicionarAlterar {
    
    public JPanel pnlObjetivosEspecificos, pnlHabilidadesRequeridas,
            pnlExperienciasRequeridas;
    
    public JLabel lblNome, lblSexo, lblNascimento, lblAdmissao, lblCargo,
            lblFormacao, lblRemuneracao, lblEstadoCivil, lblEndereco, lblCidade;
    
    public JTextField tfdObjetivoGeral, tfdObjEspecificos, tfdHabRequeridas,
            tfdExpRequeridas, tfdNome, tfdSexo, tfdCargo, tfdFormacao,
            tfdEstadoCivil, tfdEndereco, tfdCidade;
    
    public JFormattedTextField tfdNascimento, tfdAdmissao, tfdRemuneracao;
    
    public JTable tabEquipe;
    
    public DefaultTableModel modeloTabInterventor;
    
    public JList<String> listaObjEspecificos, listaHabRequeridas, 
                listaExpRequeridas;
    
    public Equipe equipe = new Equipe();
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
    public JanelaAdicionarEquipe() {}    
    
    // -------------------------------------------------------------------------
    // Métodos sobrepostos.
    // -------------------------------------------------------------------------
    
    @Override
    public void criarBotoesOpcoes() {
        btn1 = new JButton("Confirmar");
        btn2 = new JButton("Cancelar");
        btn3 = new JButton("Ajuda");
        options = new Object[] {this.btn1, this.btn2, this.btn3};
        
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mensagemErro = "";
                // Possíveis erros.
                if(tfdObjetivoGeral.getText().isEmpty() || 
                            tfdObjetivoGeral.getText().startsWith(" ")) {
                    mensagemErro = "Informe um objetivo geral válido.";                    
                }
                else if(tabEquipe.getRowCount() == 0) {
                    mensagemErro = "Adicione ao menos um interventor.";
                }
                // Se não houver erro, executa a operação.
                if(mensagemErro.isEmpty()) {
                    confirmar();                    
                    dialog.dispose();
                } else {
                    // Se houver erro, exibe mensagem de erro.
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
        });
        
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        
        // Painel Final
        pnlPrincipal = new JPanel(new FlowLayout());
        pnlPrincipal.add(painelListas());
        pnlPrincipal.add(painelInterventor());
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
        tfdRemuneracao.setText("0,00");
        tfdEstadoCivil.setText("");
        tfdEndereco.setText("");
        tfdCidade.setText("");
    }
    
    @Override
    public void confirmar() {
        // Define o objetivo geral da equipe
        equipe.setObjetivoGeral(tfdObjetivoGeral.getText());
        
        // Define os objetivos específicos da equipe: os objetivos listados
        for(int ob = 0; ob < listaObjEspecificos.getModel().getSize(); ob++) {
            equipe.setObjetivoEspecifico(
                        listaObjEspecificos.getModel().getElementAt(ob));
        }
        
        // Define as experiências requeridas da equipe: as experiências listadas
        for(int er = 0; er < listaExpRequeridas.getModel().getSize(); er++) {
            equipe.setExperiencia(listaExpRequeridas.getModel().getElementAt(er));
        }
        
        // Define as habilidades requeridas da equipe, conforme as habilidades listadas
        for(int hr = 0; hr < listaHabRequeridas.getModel().getSize(); hr++) {
            equipe.setHabilidade(
                        listaHabRequeridas.getModel().getElementAt(hr));
        }
        
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
            interventor.setRemuneracao(
                        String.valueOf(tabEquipe.getValueAt(it, 6)));
            interventor.setEstadoCivil(
                        String.valueOf(tabEquipe.getValueAt(it, 7)));
            interventor.setEndereco(
                        String.valueOf(tabEquipe.getValueAt(it, 8)));
            interventor.setCidade(
                        String.valueOf(tabEquipe.getValueAt(it, 9)));
            
            // Adiciona o interventor ao Vetor de interventores.
            equipe.setInterventor(interventor);
        }
        
        equipe.adicionaEquipe();
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /**
     * Monta o painel com campos para criação, alteração e/ou exclusão de
     * experiências requeridas da equipe de intervenção.
     * 
     * @return Um JPanel
     */
    public JPanel painelExperienciasRequeridas() {
        // Cria o modelo da lista.
        modeloListaExpRequeridas = new DefaultListModel();
        
        // Cria a lista de experiências requeridas.
        listaExpRequeridas = new JList<>(modeloListaExpRequeridas);
        listaExpRequeridas.setSelectionMode(
                    ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listaExpRequeridas.addListSelectionListener(
            new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    String itemSelecionado;
                    itemSelecionado = listaExpRequeridas.getSelectedValue();
                    tfdExpRequeridas.setText(itemSelecionado);
                }
            }
        );

        // Cria ScrollPane.
        JScrollPane ePane = new JScrollPane(listaExpRequeridas,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        ePane.setPreferredSize(new Dimension(230,100));
        
        // Cria JTextField.
        tfdExpRequeridas = new JTextField(15);
        tfdExpRequeridas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modeloListaExpRequeridas.addElement(tfdExpRequeridas.getText());
                tfdExpRequeridas.setText("");
            }
        });
        
        // Cria botão "Adicionar".
        btnAddExperiencia = new JButton("Adicionar");
        btnAddExperiencia.setPreferredSize(new Dimension(90, 20));
        btnAddExperiencia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tfdExpRequeridas.getText().isEmpty() == false) {
                    modeloListaExpRequeridas.addElement(tfdExpRequeridas.getText());
                    tfdExpRequeridas.setText("");
                }
            }
        });
        
        // Cria botão "Remover".
        btnRemExperiencia = new JButton("Remover");
        btnRemExperiencia.setPreferredSize(new Dimension(90, 20));
        btnRemExperiencia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listaExpRequeridas.isSelectionEmpty() == false) {
                    modeloListaExpRequeridas.removeElementAt(
                                listaExpRequeridas.getSelectedIndex());
                } else {}
            }
        });
        
        // Painel com JTextField e Botões.
        JPanel pnlBtnExp = new JPanel(new FlowLayout());
        pnlBtnExp.add(tfdExpRequeridas);
        pnlBtnExp.add(btnAddExperiencia);
        pnlBtnExp.add(btnRemExperiencia);
        
        // Painel Habilidades requeridas.
        pnlExperienciasRequeridas = new JPanel(new BorderLayout());
        pnlExperienciasRequeridas.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createEtchedBorder(),
                            "Experiências requeridas"));
        pnlExperienciasRequeridas.add(pnlBtnExp, BorderLayout.NORTH);
        pnlExperienciasRequeridas.add(ePane, BorderLayout.CENTER);
        
        return pnlExperienciasRequeridas;
    }
    
    /**
     * Monta o painel com campos para criação, alteração e/ou exclusão de
     * habilidades requeridas da equipe de intervenção.
     * 
     * @return Um JPanel
     */
    public JPanel painelHabilidadesRequeridas() {
        // Cria o modelo da lista.
        modeloListaHabRequeridas = new DefaultListModel();
        
        // Cria a lista de habilidades requeridas.
        listaHabRequeridas = new JList<>(modeloListaHabRequeridas);
        listaHabRequeridas.setSelectionMode(
                    ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listaHabRequeridas.addListSelectionListener(
            new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    String itemSelecionado;
                    itemSelecionado = listaHabRequeridas.getSelectedValue();
                    tfdHabRequeridas.setText(itemSelecionado);
                }
            }
        );

        // Cria ScrollPane.
        JScrollPane hPane = new JScrollPane(listaHabRequeridas,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        hPane.setPreferredSize(new Dimension(230,100));
        
        // Cria JTextField.
        tfdHabRequeridas = new JTextField(15);
        tfdHabRequeridas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modeloListaHabRequeridas.addElement(tfdHabRequeridas.getText());
                tfdHabRequeridas.setText("");
            }
        });
        
        // Cria botão "Adicionar".
        btnAddHabilidade = new JButton("Adicionar");
        btnAddHabilidade.setPreferredSize(new Dimension(90, 20));
        btnAddHabilidade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tfdHabRequeridas.getText().isEmpty() == false) {
                    modeloListaHabRequeridas.addElement(tfdHabRequeridas.getText());
                    tfdHabRequeridas.setText("");
                }
            }
        });
        
        // Cria botão "Remover"
        btnRemHabilidade = new JButton("Remover");
        btnRemHabilidade.setPreferredSize(new Dimension(90, 20));
        btnRemHabilidade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listaHabRequeridas.isSelectionEmpty() == false) {
                    modeloListaHabRequeridas.removeElementAt(
                                listaHabRequeridas.getSelectedIndex());
                } else {}
            }
        });
        
        // Painel com JTextField e Botões.
        JPanel pnlBtnHab = new JPanel(new FlowLayout());
        pnlBtnHab.add(tfdHabRequeridas);
        pnlBtnHab.add(btnAddHabilidade);
        pnlBtnHab.add(btnRemHabilidade);
        
        // Painel Habilidades requeridas.
        pnlHabilidadesRequeridas = new JPanel(new BorderLayout());
        pnlHabilidadesRequeridas.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createEtchedBorder(),
                            "Habilidades requeridas"));
        pnlHabilidadesRequeridas.add(pnlBtnHab, BorderLayout.NORTH);
        pnlHabilidadesRequeridas.add(hPane, BorderLayout.CENTER);
        
        return pnlHabilidadesRequeridas;
    }
    
    /**
     * Monta o painel com campos para criação, alteração e/ou exclusão de
     * objetivos específicos da equipe de intervenção.
     * 
     * @return Um JPanel
     */
    public JPanel painelObjetivosEspecificos() {
        // Cria o modelo da lista.
        modeloListaObjEspecificos = new DefaultListModel();
        
        // Cria a lista de objetivos específicos.
        listaObjEspecificos = new JList<>(modeloListaObjEspecificos);
        listaObjEspecificos.setSelectionMode(
                    ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listaObjEspecificos.addListSelectionListener(
            new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    String itemSelecionado;
                    itemSelecionado = listaObjEspecificos.getSelectedValue();
                    tfdObjEspecificos.setText(itemSelecionado);
                }
            }
        );

        // Cria ScrollPane.
        JScrollPane oPane = new JScrollPane(listaObjEspecificos,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        oPane.setPreferredSize(new Dimension(230,100));
        
        // Cria JTextField.
        tfdObjEspecificos = new JTextField(15);
        tfdObjEspecificos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modeloListaObjEspecificos.addElement(tfdObjEspecificos.getText());
                tfdObjEspecificos.setText("");
            }
        });
        
        // Cria botão "Adicionar".
        btnAddObjetivo = new JButton("Adicionar");
        btnAddObjetivo.setPreferredSize(new Dimension(90, 20));
        btnAddObjetivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tfdObjEspecificos.getText().isEmpty() == false) {
                    modeloListaObjEspecificos.addElement(tfdObjEspecificos.getText());
                    tfdObjEspecificos.setText("");
                }
            }
        });
        
        // Cria botão "Remover".
        btnRemObjetivo = new JButton("Remover");
        btnRemObjetivo.setPreferredSize(new Dimension(90, 20));
        btnRemObjetivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listaObjEspecificos.isSelectionEmpty() == false) {
                    modeloListaObjEspecificos.removeElementAt(
                                listaObjEspecificos.getSelectedIndex());
                } else {}
            }
        });
        
        // Painel com JTextField e Botões.
        JPanel pnlBtnObj = new JPanel(new FlowLayout());
        pnlBtnObj.add(tfdObjEspecificos);
        pnlBtnObj.add(btnAddObjetivo);
        pnlBtnObj.add(btnRemObjetivo);
        
        // Painel Objetivos específicos.
        pnlObjetivosEspecificos = new JPanel(new BorderLayout());
        pnlObjetivosEspecificos.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createEtchedBorder(),
                            "Objetivos específicos"));
        pnlObjetivosEspecificos.add(pnlBtnObj, BorderLayout.NORTH);
        pnlObjetivosEspecificos.add(oPane, BorderLayout.CENTER);
        
        return pnlObjetivosEspecificos;
    }
    
    /**
     * Monta o painel com os campos para criação, alteração e/ou exclusão de
     * interventores da equipe. Também adiciona a este painel a tabela da
     * equipe, chamada pelo método {@code painelTabelaEquipe()}.
     * 
     * @see JanelaAdicionarEquipe#painelTabelaEquipe()
     * 
     * @return Um JPanel.
     */
    public JPanel painelInterventor() {
        lblNome = new JLabel("Nome: *");
        lblSexo = new JLabel("Sexo: ");
        lblNascimento = new JLabel("Data de nascimento: ");
        lblAdmissao = new JLabel("Data de admissão: ");
        lblCargo = new JLabel("Cargo: ");
        lblFormacao = new JLabel("Formação: ");
        lblRemuneracao = new JLabel("Remuneração: ");
        lblEstadoCivil = new JLabel("Estado civil: ");
        lblEndereco = new JLabel("Endereço: ");
        lblCidade = new JLabel("Cidade: ");
        
        tfdNome = new JTextField(20);    tfdEstadoCivil = new JTextField(20);
        tfdSexo = new JTextField(20);    tfdEndereco = new JTextField(20);
        tfdCargo = new JTextField(20);   tfdFormacao = new JTextField(20);
        tfdCidade = new JTextField(20);
        
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
        
        // Monta o painel com JLabels e JTextFields.
        JPanel pnlEqp1 = new JPanel(new GridLayout(0,1,0,8));
        pnlEqp1.add(lblNome);
        pnlEqp1.add(lblSexo);
        pnlEqp1.add(lblNascimento);
        pnlEqp1.add(lblAdmissao);
        pnlEqp1.add(lblCargo);
        pnlEqp1.add(lblFormacao);
        pnlEqp1.add(lblRemuneracao);
        pnlEqp1.add(lblEstadoCivil);
        pnlEqp1.add(lblEndereco);
        pnlEqp1.add(lblCidade);
        
        JPanel pnlEqp2 = new JPanel(new GridLayout(0,1,0,4));
        pnlEqp2.add(tfdNome);
        pnlEqp2.add(tfdSexo);
        pnlEqp2.add(tfdNascimento);
        pnlEqp2.add(tfdAdmissao);
        pnlEqp2.add(tfdCargo);
        pnlEqp2.add(tfdFormacao);
        pnlEqp2.add(tfdRemuneracao);
        pnlEqp2.add(tfdEstadoCivil);
        pnlEqp2.add(tfdEndereco);
        pnlEqp2.add(tfdCidade);
        
        JPanel pnlEqp3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlEqp3.add(pnlEqp1);
        pnlEqp3.add(pnlEqp2);
        
        // Botão "Adicionar" interventor.
        btnAddInterventor = new JButton("Adicionar");
        btnAddInterventor.setPreferredSize(new Dimension(90, 20));
        btnAddInterventor.addActionListener(new AddInterventor());
        
        // Botão "Remover" interventor.
        btnRemInterventor = new JButton("Remover");
        btnRemInterventor.setPreferredSize(new Dimension(90, 20));
        btnRemInterventor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tabEquipe.getSelectedRowCount() == 1) {
                    modeloTabInterventor.removeRow(tabEquipe.getSelectedRow());
                    limparTexto();
                }
            }
        });

        // Painel com botões.
        pnlBtnEqp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtnEqp.add(btnAddInterventor);
        pnlBtnEqp.add(btnRemInterventor);
        
        // Painel "Interventor".
        JPanel pnlEqp4 = new JPanel(new BorderLayout());
        pnlEqp4.setOpaque(true);
        pnlEqp4.add(pnlEqp3, BorderLayout.PAGE_START);
        pnlEqp4.add(pnlBtnEqp, BorderLayout.PAGE_END);
        pnlEqp4.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createEtchedBorder(), "Interventor"));
        
        pnlNovoInterventor = new JPanel(new BorderLayout());
        pnlNovoInterventor.setOpaque(true);
        pnlNovoInterventor.add(pnlEqp4, BorderLayout.NORTH);
        pnlNovoInterventor.add(painelTabelaEquipe(), BorderLayout.CENTER);
        return pnlNovoInterventor;
    }
    
    /**
     * Monta o painel com a tabela da equipe de intervenção.
     * 
     * @return 
     */
    public JPanel painelTabelaEquipe() {
        tabEquipe = new JTable();
        
        JScrollPane ePane = new JScrollPane(tabEquipe,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabEquipe.setFillsViewportHeight(true);
        tabEquipe.setPreferredScrollableViewportSize(new Dimension(350, 200));
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
        
        /**
         * MouseListener define as informações da linha selecionada como texto
         * dos jtextfields do painelInterventor.
         */
        tabEquipe.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(tabEquipe.getSelectedRowCount() == 1) {
                    tfdNome.setText(String.valueOf(
                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),0)));
                    tfdSexo.setText(String.valueOf(
                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),1)));
                    tfdNascimento.setText(String.valueOf(
                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),2)));
                    tfdAdmissao.setText(String.valueOf(
                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),3)));
                    tfdCargo.setText(String.valueOf(
                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),4)));
                    tfdFormacao.setText(String.valueOf(
                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),5)));
                    tfdRemuneracao.setText(String.valueOf(
                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),6)));
                    tfdEstadoCivil.setText(String.valueOf(
                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),7)));
                    tfdEndereco.setText(String.valueOf(
                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),8)));
                    tfdCidade.setText(String.valueOf(
                        tabEquipe.getValueAt(tabEquipe.getSelectedRow(),9)));
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}}); // Fim do MouseListener
                
        JPanel pnlTabEqp = new JPanel(new FlowLayout());
        pnlTabEqp.setOpaque(true);
        pnlTabEqp.add(ePane);
        pnlTabEqp.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createEtchedBorder(),
                            "Interventores"));
        return pnlTabEqp;
    }
    
    /**
     * Monta um painel com os três paineis de atributos da equipe: 
     * 
     * {@code painelObjetivosEspecificos()}; 
     * {@code painelHabilidadesRequeridas()}; 
     * {@code painelExperienciasRequeridas()}; 
     * 
     * @return Um JPanel
     */
    public JPanel painelListas() {
        tfdObjetivoGeral = new JTextField(30);
        
        JPanel pnlObjetivoGeral = new JPanel(new FlowLayout());
        pnlObjetivoGeral.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(), "Objetivo geral"));
        pnlObjetivoGeral.add(tfdObjetivoGeral);        
        
        JPanel pnlListas1 = new JPanel(new BorderLayout());
        
        pnlListas1.add(painelObjetivosEspecificos(), BorderLayout.NORTH);
        pnlListas1.add(painelHabilidadesRequeridas(), BorderLayout.CENTER);
        pnlListas1.add(painelExperienciasRequeridas(), BorderLayout.SOUTH);
        
        JPanel pnlListas2 = new JPanel(new BorderLayout());
        
        pnlListas2.add(pnlObjetivoGeral, BorderLayout.NORTH);
        pnlListas2.add(pnlListas1, BorderLayout.CENTER);
        
        return pnlListas2;
    }
    
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionSalvar
     */
    private class AddInterventor extends JanelaAdicionarAlterar.ActionSalvar {
        @Override
        public void actionPerformed(ActionEvent event) {
            /* Lida com erros de dados do usuário */
            String mensagemErroInterventor;

            if(tfdNome.getText().isEmpty() || 
                        tfdNome.getText().startsWith(" ")) {

                mensagemErroInterventor = "Informe um nome válido.";

                /* Se houver erro, exibe mensagem de erro */
                JOptionPane mPane = new JOptionPane();
                mPane.setMessage(mensagemErroInterventor);
                mPane.setOptionType(JOptionPane.PLAIN_MESSAGE);
                mPane.setMessageType(JOptionPane.WARNING_MESSAGE);

                JDialog mDialog = mPane.createDialog(mPane, "Aviso");
                mDialog.pack();
                mDialog.setVisible(true);
                mDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            } else {
                // Se não houver erro, executa a operação.                    
                interventor = new Interventor();
                interventor.setNome(tfdNome.getText());
                interventor.setSexo(tfdSexo.getText());
                interventor.setNascimento(tfdNascimento.getText());
                interventor.setAdmissao(tfdAdmissao.getText());
                interventor.setCargo(tfdCargo.getText());
                interventor.setFormacao(tfdFormacao.getText());
                interventor.setRemuneracao(tfdRemuneracao.getText());
                interventor.setEstadoCivil(tfdEstadoCivil.getText());
                interventor.setEndereco(tfdEndereco.getText());
                interventor.setCidade(tfdCidade.getText());

                /**
                 * Converte os textos no formato String inseridos nos
                 * JTextFields do painel interventor em variáveis do tipo
                 * Object para a tabela adicioná-los a tabela da equipe.
                 */
                modeloTabInterventor.addRow(interventor.toObject());

                limparTexto();
            }
        }
    }
}