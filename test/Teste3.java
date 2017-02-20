/**
 *
 * @author Rubens Oliveira da Cunha Júnior
 */
import menu.adicionar.JanelaAdicionarIntervencao;
import equipamento.Unidade;
import java.awt.*;
import java.text.ParseException;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import menu.HoraFormatada;

public class Teste3 {
    private JPanel pnlUniGeral, pnlUni1, pnlUniOp, pnlUni2, pnlSubInfo, pnl1,
        pnl2, pnlCompInfo, pnl3, pnl4, pnlParte, pnl5, pnl6, pnlFinal,
        pnlEsquerda, pnlDireita, pnlFundo, pnl7, pnl8, pnl9;
    
    private JLabel lblUniCategoria, lblUniClasse, lblUniTipo, lblUniFabricante,
        lblUniEtiqueta, lblUniLocal, lblUniData, lblUniEstado, lblUniDataIn,
        lblUniEntreFalhas, lblUniDemanda, lblSubDescricao, lblCompDescricao,
        lblPteDescricao;
    
    private JTextField tfdUniCategoria, tfdUniClasse, tfdUniTipo,
        tfdUniFabricante, tfdUniEtiqueta, tfdUniLocal, tfdUniEstado,
        tfdUniEntreFalhas, tfdUniDemanda, tfdSubDescricao, tfdCompDescricao,
        tfdPteDescricao;
    
    private JFormattedTextField tfdUniDataAq, tfdUniDataIn;
    
    private JButton btnSubAdicionar, btnSubRemover, btnCompAdicionar,
        btnCompRemover, btnPteAdicionar, btnPteRemover, btnSalvar, btnCancelar;
    
    private JComboBox cbxSubunidade, cbxComponente, cbxParte;
    
    private final JFrame frameEquipamento;
    
    public Unidade unidade = new Unidade();
    
    private final String eventSelected = "ActionEvent";
    
    HoraFormatada entradaHora;
        
    public Teste3() {
        
        entradaHora = new HoraFormatada();
        
        // Cria os botões "Salvar" e "Cancelar"
        btnSalvar = new JButton("Salvar");
        //btnSalvar.addActionListener(new JanelaEquipamento.BotaoSalvar());
        btnCancelar = new JButton("Cancelar");
        //btnCancelar.addActionListener(new JanelaEquipamento.BotaoCancelar());

        pnlEsquerda = new JPanel(new BorderLayout());
        pnlEsquerda.add(painelGeral(), BorderLayout.NORTH);
        pnlEsquerda.add(painelOperacional(), BorderLayout.CENTER);
        
        pnlDireita = new JPanel(new FlowLayout());
        pnlDireita.add(painelSubunidades());
        
        pnlFundo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlFundo.add(btnSalvar);
        pnlFundo.add(btnCancelar);
        
        frameEquipamento = new JFrame("Adicionar nova unidade de equipamento");
        frameEquipamento.setLayout(new BorderLayout());
        frameEquipamento.add(pnlEsquerda, BorderLayout.LINE_START);
        frameEquipamento.add(pnlDireita, BorderLayout.LINE_END);
        frameEquipamento.add(pnlFundo, BorderLayout.PAGE_END);
        frameEquipamento.setVisible(true);
        frameEquipamento.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameEquipamento.setResizable(true);
        frameEquipamento.pack();
        frameEquipamento.setLocationRelativeTo(null);
    }
    
    private JPanel painelGeral() {
        /** Painel "Dados gerais de Unidade" -------------------------------- **/
        // Cria JLabels e JTextFields
        lblUniClasse = new JLabel("Classe *: ");
        lblUniTipo = new JLabel("Tipo *: ");
        lblUniFabricante = new JLabel("Fabricante *: ");
        lblUniEtiqueta = new JLabel("Identificação *: ");
        lblUniCategoria = new JLabel("Categoria: ");
        lblUniLocal = new JLabel("Localização: ");
        
        tfdUniClasse = new JTextField(20);
        tfdUniTipo = new JTextField(20);
        tfdUniFabricante = new JTextField(20);
        tfdUniEtiqueta = new JTextField(20);
        tfdUniCategoria = new JTextField(20);
        tfdUniLocal = new JTextField(20);
        
        pnlUniGeral = new JPanel(new GridLayout(0,2,0,5));
        pnlUniGeral.add(lblUniClasse);    pnlUniGeral.add(tfdUniClasse);
        pnlUniGeral.add(lblUniTipo);      pnlUniGeral.add(tfdUniTipo);
        pnlUniGeral.add(lblUniFabricante);pnlUniGeral.add(tfdUniFabricante);
        pnlUniGeral.add(lblUniEtiqueta);  pnlUniGeral.add(tfdUniEtiqueta);
        pnlUniGeral.add(lblUniCategoria); pnlUniGeral.add(tfdUniCategoria);
        pnlUniGeral.add(lblUniLocal);     pnlUniGeral.add(tfdUniLocal); 
                
        // Painel "Unidade"
        pnlUni1 = new JPanel(new FlowLayout());
        pnlUni1.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                            "Geral"));
        pnlUni1.setOpaque(true);
        pnlUni1.add(pnlUniGeral);
        return pnlUni1;
    }
    private JPanel painelOperacional() {
        /** Painel "Dados operacionais de Unidade" -------------------------------- **/
        // Cria JLabels e JTextFields
        lblUniEstado = new JLabel("Estado operacional normal *: ");
        lblUniDataIn = new JLabel("Data de início de operação *: ");
        lblUniEntreFalhas = new JLabel("Período entre falhas *: ");
        lblUniDemanda = new JLabel("Demandas no período entre falhas *: ");
        lblUniData = new JLabel("Data de aquisição *: ");
        JLabel lbltst = new JLabel("teste");
        
        tfdUniEstado = new JTextField(20);
        tfdUniEntreFalhas = new JTextField(20);
        tfdUniDemanda = new JTextField(10);
                
        tfdUniDataIn = new JFormattedTextField();
        tfdUniDataIn.setFocusLostBehavior(JFormattedTextField.PERSIST);
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.install(tfdUniDataIn);
            mf.setPlaceholder("DD/MM/AAAA");
            mf.setValidCharacters("0123456789");
        } catch (ParseException ex) { ex.printStackTrace();}
        
        tfdUniDataAq = new JFormattedTextField();
        tfdUniDataAq.setFocusLostBehavior(JFormattedTextField.PERSIST);
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.install(tfdUniDataAq);
            mf.setPlaceholder("DD/MM/AAAA");
            mf.setValidCharacters("0123456789");
        } catch (ParseException ex) { ex.printStackTrace();}
        
        pnlUniOp = new JPanel(new GridLayout(0,2,0,5));
        pnlUniOp.add(lblUniEstado);         pnlUniOp.add(tfdUniEstado);
        pnlUniOp.add(lblUniDataIn);         pnlUniOp.add(tfdUniDataIn);
        pnlUniOp.add(lblUniEntreFalhas);    pnlUniOp.add(tfdUniEntreFalhas);
        pnlUniOp.add(lblUniDemanda);        pnlUniOp.add(tfdUniDemanda);
        pnlUniOp.add(lblUniData);           pnlUniOp.add(tfdUniDataAq);
        
        JPanel pnltst = new JPanel( new FlowLayout());
//        entradaHora.adicionar(pnltst);
        
        // Painel "Unidade"
        pnlUni2 = new JPanel(new FlowLayout());
        pnlUni2.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                            "Operação"));
        pnlUni2.setOpaque(true);
        pnlUni2.add(pnlUniOp);
        pnlUni2.add(pnltst);
        return pnlUni2;
    }
    
    private JPanel painelSubunidades() {
        /** Painel "Informações de Subunidade" ----------------------------- **/
        // Cria JLabel
        lblSubDescricao = new JLabel(" Descrição: ");
        // Cria JTextField
        tfdSubDescricao = new JTextField(25);
        //tfdSubDescricao.addActionListener(new JanelaEquipamento.AdicionarSubunidade());
        // Cria JComboBox
        cbxSubunidade = new JComboBox(unidade.getVetorSubunidade());
        //cbxSubunidade.addItemListener(new JanelaEquipamento.ItemEventSubunidade());
        cbxSubunidade.setMaximumRowCount(5);
        cbxSubunidade.setEditable(false);
        cbxSubunidade.setPrototypeDisplayValue(
                "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        // Cria JButtons "Adicionar" e "Remover"
        btnSubAdicionar = new JButton("Adicionar");
        //btnSubAdicionar.addActionListener(new JanelaEquipamento.AdicionarSubunidade());
        btnSubRemover = new JButton("Remover");
        //btnSubRemover.addActionListener(new JanelaEquipamento.RemoverSubunidade());
        // Painel que guarda JTextField "Descrição" e JButton "Adicionar"
        pnl1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl1.add(tfdSubDescricao);
        pnl1.add(btnSubAdicionar);
        // Painel que guarda JComboBox "Subunidades" e JButton "Remover"
        pnl2 = new JPanel(new FlowLayout(0,7,0));
        pnl2.add(cbxSubunidade);
        pnl2.add(btnSubRemover);
        // Painel superior
        pnlSubInfo = new JPanel(new GridLayout(2,0,0,0));
        pnlSubInfo.add(pnl1);
        pnlSubInfo.add(pnl2);
        
        pnl7 = new JPanel(new BorderLayout());
        pnl7.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                            "Configurar subunidades"));
        pnl7.add(lblSubDescricao, BorderLayout.NORTH);
        pnl7.add(pnlSubInfo);
        
        /** Painel "Informações de Componente" ----------------------------- **/
        // Cria JLabel
        lblCompDescricao = new JLabel(" Descrição: ");
        // Cria JTextField
        tfdCompDescricao = new JTextField(25);
        //tfdCompDescricao.addActionListener(new JanelaEquipamento.AdicionarComponente());
        // Cria JComboBox
        cbxComponente = new JComboBox();
        //cbxComponente.addItemListener(new JanelaEquipamento.ItemEventComponente());
        cbxComponente.setMaximumRowCount(5);
        cbxComponente.setEditable(false);
        cbxComponente.setPrototypeDisplayValue(
                "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        // Cria JButtons "Adicionar" e "Remover"
        btnCompAdicionar = new JButton("Adicionar");
       // btnCompAdicionar.addActionListener(new JanelaEquipamento.AdicionarComponente());
        btnCompRemover = new JButton("Remover");
       // btnCompRemover.addActionListener(new JanelaEquipamento.RemoverComponente());
        // Painel que guarda JTextField "Descrição" e JButton "Adicionar"
        pnl3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl3.add(tfdCompDescricao);
        pnl3.add(btnCompAdicionar);
        // Painel que guarda JComboBox "Componentes" e JButton "Remover"
        pnl4 = new JPanel(new FlowLayout(0,7,0));
        pnl4.add(cbxComponente);
        pnl4.add(btnCompRemover);
        // Painel superior
        pnlCompInfo = new JPanel(new GridLayout(2,0,0,0));
        pnlCompInfo.add(pnl3);
        pnlCompInfo.add(pnl4);
        
        pnl8 = new JPanel(new BorderLayout());
        pnl8.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                            "Configurar componentes"));
        pnl8.add(lblCompDescricao, BorderLayout.NORTH);
        pnl8.add(pnlCompInfo);
        
        /** Painel "Informações de Parte" ---------------------------------- **/
        // Cria JLabel
        lblPteDescricao = new JLabel(" Descrição: ");
        // Cria JTextField
        tfdPteDescricao = new JTextField(25);
        //tfdPteDescricao.addActionListener(new JanelaEquipamento.AdicionarParte());
        // Cria JComboBox
        cbxParte = new JComboBox();
        cbxParte.setMaximumRowCount(5);
        cbxParte.setEditable(false);
        cbxParte.setPrototypeDisplayValue(
                "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        // Cria JButtons "Adicionar" e "Remover"
        btnPteAdicionar = new JButton("Adicionar");
        //btnPteAdicionar.addActionListener(new JanelaEquipamento.AdicionarParte());
        btnPteRemover = new JButton("Remover");
       // btnPteRemover.addActionListener(new JanelaEquipamento.RemoverParte());
        // Painel que guarda JTextField "Descrição" e JButton "Adicionar"
        pnl5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl5.add(tfdPteDescricao);
        pnl5.add(btnPteAdicionar);
        // Painel que guarda JComboBox "Partes" e JButton "Remover"
        pnl6 = new JPanel(new FlowLayout(0,7,0));
        pnl6.add(cbxParte);
        pnl6.add(btnPteRemover);
        // Painel superior
        pnlParte = new JPanel(new GridLayout(2,0,0,0));
        pnlParte.add(pnl5);  
        pnlParte.add(pnl6);
        
        pnl9 = new JPanel(new BorderLayout());
        pnl9.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(),
                                "Configurar partes"));
        pnl9.add(lblPteDescricao, BorderLayout.NORTH);
        pnl9.add(pnlParte);
        
        // Painel "Final"
        pnlFinal = new JPanel(new GridLayout(3,0));        
                
        pnlFinal.add(pnl7);
        pnlFinal.add(pnl8);
        pnlFinal.add(pnl9);
        
        return pnlFinal;
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        //Teste3 tfd = new Teste3();
        Teste3 inv = new Teste3();
    }
}