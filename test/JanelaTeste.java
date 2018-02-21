/**
 * Autor Rubens Oliveira da Cunha Júnior
 */

import mesor.equipamento.Unidade;
import mesor.equipamento.Subunidade;
import mesor.equipamento.Parte;
import mesor.equipamento.Componente;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.DefaultComboBoxModel;

public class JanelaTeste {
    private final JPanel pnlUniInfo, pnlUni, pnlCategoria1, pnlCategoria2,
        pnlClasse1, pnlClasse2, pnlTipo1, pnlTipo2, pnlDescricao1,
        pnlDescricao2, pnlFabricante1, pnlFabricante2, pnlEtiqueta1,
        pnlEtiqueta2, pnlLocal1, pnlLocal2, pnlInfo1, pnlInfo2, pnlData1,
        pnlData2, pnl3, pnlSubInfo, pnl4, pnl5, pnl6, pnlCompInfo, pnl7, pnl8,
        pnl9, pnlParte, pnl10, pnl11, pnlFinal;
    private final JLabel lblUniCategoria, lblUniClasse, lblUniTipo,
        lblUniDescricao, lblUniFabricante, lblUniEtiqueta, lblUniLocal, 
        lblUniInfo, lblUniData, lblSubDescricao, lblCompDescricao,
        lblPteDescricao, lblVazio1, lblVazio2, lblVazio3;
    private final JTextField tfdUniCategoria, tfdUniClasse, tfdUniTipo,
        tfdUniDescricao, tfdUniFabricante, tfdUniEtiqueta, tfdUniLocal,
        tfdUniInfo, tfdUniData, tfdSubDescricao, tfdCompDescricao, 
        tfdPteDescricao;
    private final JButton btnSubAdicionar, btnSubRemover, btnCompAdicionar,
        btnCompRemover, btnPteAdicionar, btnPteRemover, btnSalvar, btnCancelar;
    private final JComboBox cbxSubunidade, cbxComponente, cbxParte;
    private final JTabbedPane tbdPanel;
    private JFrame frameEquipamento;
    public Unidade unidade = new Unidade();
    private final String eventSelected = "ActionEvent";
    
    public JanelaTeste() {
        // JLabel vazio para compor o GridLayout()
        lblVazio1 = new JLabel(); lblVazio2 = new JLabel(); 
        lblVazio3 = new JLabel();
        
        /** Painel "Informações de Unidade" -------------------------------- **/
        // Cria JLabels e JTextFields
        lblUniCategoria = new JLabel(" Categoria: ");
        lblUniClasse = new JLabel(" Classe: ");
        lblUniTipo = new JLabel(" Tipo: ");
        lblUniDescricao = new JLabel(" Descrição: ");
        lblUniFabricante = new JLabel(" Fabricante: ");
        lblUniEtiqueta = new JLabel(" Número de etiqueta: ");
        lblUniLocal = new JLabel(" Localização: ");
        lblUniInfo = new JLabel(" Informações adicionais: ");
        lblUniData = new JLabel(" Data de aquisição: ");
        
        tfdUniCategoria = new JTextField(25);
        tfdUniClasse = new JTextField(25);
        tfdUniTipo = new JTextField(25);
        tfdUniDescricao = new JTextField(25);
        tfdUniFabricante = new JTextField(25);
        tfdUniEtiqueta = new JTextField(25);
        tfdUniLocal = new JTextField(25);
        tfdUniInfo = new JTextField(25); 
        tfdUniData = new JTextField(10);
        
        // Paineis que guardam JLabel e JTextField "Categoria"
        pnlCategoria1 = new JPanel(new FlowLayout(FlowLayout.LEFT));       
        pnlCategoria1.add(lblUniCategoria);
        pnlCategoria2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlCategoria2.add(tfdUniCategoria);
        
        // Paineis que guardam JLabel e JTextField "Classe"
        pnlClasse1 = new JPanel(new FlowLayout(FlowLayout.LEFT));       
        pnlClasse1.add(lblUniClasse);
        pnlClasse2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlClasse2.add(tfdUniClasse);
        
        // Paineis que guardam JLabel e JTextField "Tipo"
        pnlTipo1 = new JPanel(new FlowLayout(FlowLayout.LEFT));       
        pnlTipo1.add(lblUniTipo);
        pnlTipo2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTipo2.add(tfdUniTipo);
        
        // Paineis que guardam JLabel e JTextField "Descrição"
        pnlDescricao1 = new JPanel(new FlowLayout(FlowLayout.LEFT));       
        pnlDescricao1.add(lblUniDescricao);
        pnlDescricao2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlDescricao2.add(tfdUniDescricao);
        
        // Paineis que guardam JLabel e JTextField "Fabricante"
        pnlFabricante1 = new JPanel(new FlowLayout(FlowLayout.LEFT));       
        pnlFabricante1.add(lblUniFabricante);
        pnlFabricante2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlFabricante2.add(tfdUniFabricante);
        
        // Paineis que guardam JLabel e JTextField "Etiqueta"
        pnlEtiqueta1 = new JPanel(new FlowLayout(FlowLayout.LEFT));       
        pnlEtiqueta1.add(lblUniEtiqueta);
        pnlEtiqueta2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlEtiqueta2.add(tfdUniEtiqueta);
        
        // Paineis que guardam JLabel e JTextField "Localização"
        pnlLocal1 = new JPanel(new FlowLayout(FlowLayout.LEFT));       
        pnlLocal1.add(lblUniLocal);
        pnlLocal2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlLocal2.add(tfdUniLocal);
        
        // Paineis que guardam JLabel e JTextField "Informação adicional"
        pnlInfo1 = new JPanel(new FlowLayout(FlowLayout.LEFT));       
        pnlInfo1.add(lblUniInfo);
        pnlInfo2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlInfo2.add(tfdUniInfo);
        
        // Paineis que guardam JLabel e JTextField "Data de aquisição"
        pnlData1 = new JPanel(new FlowLayout(FlowLayout.LEFT));       
        pnlData1.add(lblUniData);
        pnlData2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlData2.add(tfdUniData);
        
        /**
         * Usando paineis menores com FlowLayout, o Grid Layout do painel
         * superior funciona sem deixar todos os componentes do mesmo tamanho
        **/
        pnlUniInfo = new JPanel(new GridLayout(0,2));
        pnlUniInfo.add(pnlCategoria1);
        pnlUniInfo.add(pnlCategoria2);
        pnlUniInfo.add(pnlClasse1);
        pnlUniInfo.add(pnlClasse2);
        pnlUniInfo.add(pnlTipo1);
        pnlUniInfo.add(pnlTipo2);
        pnlUniInfo.add(pnlDescricao1);
        pnlUniInfo.add(pnlDescricao2);
        pnlUniInfo.add(pnlFabricante1);
        pnlUniInfo.add(pnlFabricante2);
        pnlUniInfo.add(pnlEtiqueta1);
        pnlUniInfo.add(pnlEtiqueta2);
        pnlUniInfo.add(pnlLocal1);
        pnlUniInfo.add(pnlLocal2);
        pnlUniInfo.add(pnlInfo1);
        pnlUniInfo.add(pnlInfo2); 
        pnlUniInfo.add(pnlData1);
        pnlUniInfo.add(pnlData2);
        
        /** Painel "Informações de Subunidade" ----------------------------- **/
        // Cria JLabel
        lblSubDescricao = new JLabel(" Descrição: ");
        // Cria JTextField
        tfdSubDescricao = new JTextField(25);
        tfdSubDescricao.addActionListener(new AdicionarSubunidade());
        // Cria JComboBox
        cbxSubunidade = new JComboBox(unidade.getVetorSubunidade());
        cbxSubunidade.addItemListener(new ItemEventSubunidade());
        cbxSubunidade.setMaximumRowCount(5);
        cbxSubunidade.setEditable(false);
        cbxSubunidade.setPrototypeDisplayValue(
                "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        // Cria JButtons "Adicionar" e "Remover"
        btnSubAdicionar = new JButton("Adicionar");
        btnSubAdicionar.addActionListener(new AdicionarSubunidade());
        btnSubRemover = new JButton("Remover");
        btnSubRemover.addActionListener(new RemoverSubunidade());
        // Painel que guarda JLabel "Descrição"
        pnl3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl3.add(lblSubDescricao);
        // Painel que guarda JTextField "Descrição" e JButton "Adicionar"
        pnl4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl4.add(tfdSubDescricao);
        pnl4.add(btnSubAdicionar);
        // Painel que guarda JComboBox "Subunidades" e JButton "Remover"
        pnl5 = new JPanel(new FlowLayout(0,7,0));
        pnl5.add(cbxSubunidade);
        pnl5.add(btnSubRemover);
        // Painel superior
        pnlSubInfo = new JPanel(new GridLayout(2,2,0,0));
        pnlSubInfo.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                            "Configurar subunidade"));
        pnlSubInfo.add(pnl3);
        pnlSubInfo.add(pnl4);
        pnlSubInfo.add(lblVazio1);
        pnlSubInfo.add(pnl5);
        
        /** Painel "Informações de Componente" ----------------------------- **/
        // Cria JLabel
        lblCompDescricao = new JLabel(" Descrição: ");
        // Cria JTextField
        tfdCompDescricao = new JTextField(25);
        tfdCompDescricao.addActionListener(new AdicionarComponente());
        // Cria JComboBox
        cbxComponente = new JComboBox();
        cbxComponente.addItemListener(new ItemEventComponente());
        cbxComponente.setMaximumRowCount(5);
        cbxComponente.setEditable(false);
        cbxComponente.setPrototypeDisplayValue(
                "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        // Cria JButtons "Adicionar" e "Remover"
        btnCompAdicionar = new JButton("Adicionar");
        btnCompAdicionar.addActionListener(new AdicionarComponente());
        btnCompRemover = new JButton("Remover");
        btnCompRemover.addActionListener(new RemoverComponente());
        // Painel que guarda JLabel "Descrição"
        pnl6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl6.add(lblCompDescricao);
        // Painel que guarda JTextField "Descrição" e JButton "Adicionar"
        pnl7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl7.add(tfdCompDescricao);
        pnl7.add(btnCompAdicionar);
        // Painel que guarda JComboBox "Componentes" e JButton "Remover"
        pnl8 = new JPanel(new FlowLayout(0,7,0));
        pnl8.add(cbxComponente);
        pnl8.add(btnCompRemover);
        // Painel superior
        pnlCompInfo = new JPanel(new GridLayout(2,2,0,0));
        pnlCompInfo.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                            "Configurar componentes"));
        pnlCompInfo.add(pnl6);
        pnlCompInfo.add(pnl7);
        pnlCompInfo.add(lblVazio2);
        pnlCompInfo.add(pnl8);
        
        /** Painel "Informações de Parte" ---------------------------------- **/
        // Cria JLabel
        lblPteDescricao = new JLabel(" Descrição: ");
        // Cria JTextField
        tfdPteDescricao = new JTextField(25);
        tfdPteDescricao.addActionListener(new AdicionarParte());
        // Cria JComboBox
        cbxParte = new JComboBox();
        cbxParte.setMaximumRowCount(5);
        cbxParte.setEditable(false);
        cbxParte.setPrototypeDisplayValue(
                "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        // Cria JButtons "Adicionar" e "Remover"
        btnPteAdicionar = new JButton("Adicionar");
        btnPteAdicionar.addActionListener(new AdicionarParte());
        btnPteRemover = new JButton("Remover");
        btnPteRemover.addActionListener(new RemoverParte());
        // Painel que guarda JLabel "Descrição"
        pnl9 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl9.add(lblPteDescricao);
        // Painel que guarda JTextField "Descrição" e JButton "Adicionar"
        pnl10 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl10.add(tfdPteDescricao);
        pnl10.add(btnPteAdicionar);
        // Painel que guarda JComboBox "Partes" e JButton "Remover"
        pnl11 = new JPanel(new FlowLayout(0,7,0));
        pnl11.add(cbxParte);
        pnl11.add(btnPteRemover);
        // Painel superior
        pnlParte = new JPanel(new GridLayout(2,2,0,0));
        pnlParte.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                "Configurar partes"));
        pnlParte.add(pnl9);
        pnlParte.add(pnl10);       
        pnlParte.add(lblVazio3);       
        pnlParte.add(pnl11);
        
        /** Aba nº 1 do JTabbedPane ---------------------------------------- **/
        // Painel "Unidade"
        pnlUni = new JPanel(new BorderLayout());
        pnlUni.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                            "Configurar nova unidade"));
        pnlUni.setOpaque(true);
        pnlUni.add(pnlUniInfo, BorderLayout.CENTER);

        /** Aba nº 2 do JTabbedPane ---------------------------------------- **/
        // Painel "Final"
        pnlFinal = new JPanel(new FlowLayout());
        
        // Cria os botões "Salvar" e "Cancelar"
        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(new BotaoSalvar());
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new BotaoCancelar());
        
        pnlFinal.add(pnlSubInfo);
        pnlFinal.add(pnlCompInfo);
        pnlFinal.add(pnlParte);
        // Adiciona os botões
        pnlFinal.add(btnSalvar);
        pnlFinal.add(btnCancelar);
            
        /** JTabbedPane "tbdPane" ------------------------------------------ **/
        tbdPanel = new JTabbedPane(1);
        tbdPanel.addTab("Geral", pnlUni);
        tbdPanel.addTab("Subunidades", pnlFinal);
        tbdPanel.setMnemonicAt(0, KeyEvent.VK_G);
        tbdPanel.setMnemonicAt(1, KeyEvent.VK_S);
    }
    
    // Métodos de criação e exibição da janela ---------------------------------
    // Mostra o JFrame a ser exibido e adiciona o JTabbedPane "tbdPanel"
    public void mostraJanelaTeste() {
        frameEquipamento = new JFrame("Criar equipamento");
        frameEquipamento.setSize(800,420);
        frameEquipamento.add(tbdPanel);
        frameEquipamento.setVisible(true);
        frameEquipamento.setLocationRelativeTo(null);
        frameEquipamento.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Métodos ActionListener --------------------------------------------------
    // Botão "Adicionar" subunidade
    /**
     * @Override actionPerformed(ActionEvent event)
     * Cria o objeto "sub" da classe Subunidade com o construtor 
     * Subunidade(String pDescrição), onde "pDescrição" recebe o texto digitado
     * no JTextField "tfdDescricaoSubunidade".
     * Define "sub" como uma subunidade do objeto "unidade" da classe Unidade.
     * Define a útima subunidade adicionada a "unidade" como o item selecionado
     * da JComboBox "cbxSubunidade".
     * Apaga o texto em "tfdDescricaoSubunidade" com o método setText().
    **/
    class AdicionarSubunidade implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(eventSelected.equals("ActionEvent")) {
                Subunidade sub = new Subunidade(
                        tfdSubDescricao.getText());
                
                unidade.setSubunidade(sub);
                
                cbxSubunidade.setSelectedItem(
                        unidade.getVetorSubunidade().lastElement());
                tfdSubDescricao.setText("");
            }
        }
    }
    // Botão "Adicionar" Componente
    /**
     * @Override actionPerformed(ActionEvent event)
     * Cria o objeto "comp" da classe "Componente" com o construtor 
     * Componente(String pDescricao, onde "pDescrição" recebe o texto digitado
     * na JComboBox "cbxComponente".
     * Cria o objeto "sub" da classe Subunidade, que instancia a subunidade
     * selecionada na JComboBox "cbxSubunidade".
     * Define "comp" como um componente do objeto "sub".
     * Define o último componente adicionado a "sub" como o item selecionado na
     * JComboBox "cbxComponente".
     * Apaga o texto em "tfdDescricaoComponente" com o método setText().
     **/
    class AdicionarComponente implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(eventSelected.equals("ActionEvent")) {
                Componente comp = new Componente(
                    tfdCompDescricao.getText());

                Subunidade sub = (Subunidade) cbxSubunidade.getSelectedItem();
                sub.setComponente(comp);

                cbxComponente.setSelectedItem(
                        sub.getVetorComponente().lastElement());
                tfdCompDescricao.setText("");
            }
        }
    }
    // Botão "Adicionar" Parte
    /**
     * @Override actionPerformed(ActionEvent event)
     * Cria o objeto "pt" da classe "Parte" com o construtor 
     * Parte(String pDescricao), onde "pDescrição" recebe o texto digitado
     * na JComboBox "cbxParte".
     * Cria o objeto "comp" da classe Componente, que instancia o componente
     * selecionado na JComboBox "cbxComponente".
     * Define "pt" como uma parte do objeto "comp".
     * Define a última parte adicionada a "comp" como o item selecionado na
     * JComboBox "cbxParte".
     * Apaga o texto em "tfdDescricaoParte" com o método setText().
     **/
    class AdicionarParte implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(eventSelected.equals("ActionEvent")) {
                Parte pt = new Parte(tfdPteDescricao.getText());
                
                Componente comp = (Componente) cbxComponente.getSelectedItem();
                comp.setParte(pt);
                    
                cbxParte.setSelectedItem(comp.getVetorParte().lastElement());
                tfdPteDescricao.setText("");
            }
        }
    }
    
    // Botão "Remover subunidade"
    class RemoverSubunidade implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(eventSelected.equals("ActionEvent")) {
                Subunidade sub = (Subunidade) cbxSubunidade.getSelectedItem();
                unidade.getVetorSubunidade().removeElement(sub);            }
        }
    }
    // Botão "Remover Componente"
    class RemoverComponente implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(eventSelected.equals("ActionEvent")) {
            }
        }
    }
    // Botão "Remover parte"
    class RemoverParte implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(eventSelected.equals("ActionEvent")) {
            }
        }
    }
    // Botão "Salvar"
    class BotaoSalvar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(eventSelected.equals("ActionEvent")) {
                unidade.setCategoria(tfdUniCategoria.getText());
                unidade.setClasse(tfdUniClasse.getText());
                unidade.setTipo(tfdUniTipo.getText());
                //unidade.setDescricao(tfdUniDescricao.getText());
                unidade.setFabricante(tfdUniFabricante.getText());
                unidade.setIdentificacao(tfdUniEtiqueta.getText());
                unidade.setLocal(tfdUniLocal.getText());
                unidade.setDataAquisicao(tfdUniData.getText());
                //unidade.setInfoAdicional(tfdUniInfo.getText());
                
                System.out.println(unidade.toString());
                JOptionPane.showConfirmDialog(null, 
                                        "Deseja dicionar unidade?\n\n" + 
                                                unidade.toString(), 
                                        "Confirmar", 
                                        JOptionPane.OK_CANCEL_OPTION);
            }
        }
    }
    //Botão "Cancelar"
    class BotaoCancelar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(eventSelected.equals("ActionEvent")) {
                System.out.println("Botão cancelar clicado");
                //unidade.removeTudo();
            }
        }
    }
    
    // Métodos ItemListener ----------------------------------------------------
    // Método ItemEventSubunidade
    /**
     * @Override itemStateChanged(ItemEvent e)
     * Cria o objeto "sub" da classe Subunidade, instanciando o item selecionado
     * na JComboBox "cbxSubunidade", através do método e.getItem(). 
     * Cria "cbxCp", um DefaultComboBoxModel, que tem como itens o vetor de
     * componentes de "sub", com o método asVetorComponente().
     **/
    class ItemEventSubunidade implements ItemListener {
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
    // Método ItemEventComponente
    /**
     * @Override itemStateChanged(ItemEvent e)
     * Cria o objeto "comp" da classe Componente, instanciando o item 
     * selecionado na JComboBox "cbxComponente", através do método e.getItem().
     * Cria "cbxPt" um DefaultComboBoxModel, que tem como itens o vetor de 
     * partes de "comp", com o método asVetorParte().
    **/
    class ItemEventComponente implements ItemListener {
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