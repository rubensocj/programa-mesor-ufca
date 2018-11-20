package mesor.menu.principal;

import mesor.menu.alterar.JanelaAlterarIntervencao;
import mesor.menu.alterar.JanelaAlterarEquipe;
import mesor.menu.alterar.JanelaAlterarEquipamento;
import mesor.menu.alterar.JanelaAlterarDemanda;
import mesor.menu.alterar.JanelaAlterarSistema;

import mesor.menu.adicionar.JanelaAdicionarIntervencao;
import mesor.menu.adicionar.JanelaAdicionarEquipe;
import mesor.menu.adicionar.JanelaAdicionarEquipamento;
import mesor.menu.adicionar.JanelaAdicionarDemanda;
import mesor.menu.adicionar.JanelaAdicionarSistema;
import mesor.menu.adicionar.JanelaAdicionarInterventor;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.*;

import javax.swing.*;
import mesor.menu.DialogoAviso;
import mesor.menu.Janela;
import mesor.menu.SeletorArquivo;
import mesor.menu.Zip;
import mesor.menu.alterar.JanelaAlterarInterventor;
import mesor.menu.painel.taxonomia.PainelEquipamento;
import mesor.menu.painel.taxonomia.PainelEquipe;
import mesor.menu.painel.taxonomia.PainelInterventor;
import mesor.sql.Consulta;
import mesor.sql.Query;

/**
 * MenuPrincipal.java
 * 
 * @version 1.0 1/2/2017
 * @author Rubens Jr
 */
public class MenuPrincipal extends JFrame {
    
    private JMenuBar barraMenu;
    
    private JMenu menuArquivo, menuVisualizar, menuAdicionar, menuAlterar, menuAjuda;
    private JMenuItem arquivoAlterarBanco, arquivoPrepararBanco, arquivoObterBanco;
    private JMenuItem adicionarDemanda, adicionarEquipamento,
                adicionarIntervencao, adicionarInterventor, adicionarEquipe,
                adicionarSistema;
    private JMenuItem alterarDemanda, alterarEquipamento, 
                alterarIntervencao, alterarInterventor, alterarEquipe,
                alterarSistema;
    private JMenuItem ajudaConteudo, ajudaSobre;
    private JMenuItem visualEquipamento, visualEquipe, visualInterventor;
        
    private final String eventSelected = "ActionEvent";

    public static final PainelPrincipal PAINEL_PRINCIPAL = new PainelPrincipal();
    
    private JMenuBar criaBarraMenu() {
        // Cria a barra de menu
        barraMenu = new JMenuBar();
        barraMenu.setMaximumSize(new Dimension(56, 32769));
        barraMenu.setMinimumSize(new Dimension(0,2));
        barraMenu.setPreferredSize(new Dimension(56,21));
        barraMenu.setBorder(BorderFactory.createEmptyBorder());
        barraMenu.setOpaque(true);
        barraMenu.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        
        /* Cria os menus e define os atalhos do teclado. */
        menuArquivo = new JMenu("Arquivo");
        menuArquivo.setMnemonic(KeyEvent.VK_A);
        
        menuAdicionar = new JMenu("Adicionar");
        menuAdicionar.setMnemonic(KeyEvent.VK_D);
        
        menuAlterar = new JMenu("Editar");
        menuAlterar.setMnemonic(KeyEvent.VK_L);
        
        menuVisualizar = new JMenu("Visualizar");
        menuVisualizar.setMnemonic(KeyEvent.VK_V);
        
        menuAjuda = new JMenu("Ajuda");
        menuAjuda.setMnemonic(KeyEvent.VK_D);
        
        /**      
         * 1. Legenda exibida quando cursor do mouse passar sobre o componente:
         * 
         * menuArquivo.setToolTipText("Arquivo");
         * 
         * 2. Definir o atalho do teclado:
         * 
         * novoEquipamento.setAccelerator(KeyStroke.getKeyStroke(
         *              KeyEvent.VK_N, ActionEvent.CTRL_MASK));
         * 
         * 3. Adiciona um separador de itens ao menu:
         * 
         * menuArquivo.addSeparator(); 
         */
        
        // Cria os itens do menu "Arquivo"
        arquivoAlterarBanco = new JMenuItem("Alterar banco de dados");
        arquivoPrepararBanco = new JMenuItem("Preparar para enviar banco de dados..."); // exibir lista de bancos para seleção, e apos a seleção perguntar onde quer salvar. Dai configura o arquivo zip e o usuario se destinará até ele para envia-lo
        arquivoObterBanco = new JMenuItem("Obter banco de dados..."); // exibir navegador de arquivos para seleção
        
        // Cria os itens do menu "Adicionar"
        adicionarDemanda = new JMenuItem("Demanda", KeyEvent.VK_D);
        adicionarEquipamento = new JMenuItem("Equipamento", KeyEvent.VK_E);        
        adicionarIntervencao = new JMenuItem("Intervenção", KeyEvent.VK_I);
        adicionarInterventor = new JMenuItem("Interventor", KeyEvent.VK_N);
        adicionarEquipe = new JMenuItem("Equipe de Intervenção", KeyEvent.VK_Q);
        adicionarSistema = new JMenuItem("Sistema", KeyEvent.VK_S);
          
        // Cria os itens do menu "Alterar"
        alterarDemanda = new JMenuItem("Demanda", KeyEvent.VK_D);
        alterarEquipamento = new JMenuItem("Equipamento", KeyEvent.VK_E);
        alterarIntervencao = new JMenuItem("Intervenção", KeyEvent.VK_I);
        alterarInterventor = new JMenuItem("Interventor", KeyEvent.VK_N);
        alterarEquipe = new JMenuItem("Equipe de Intervenção", KeyEvent.VK_Q);
        alterarSistema = new JMenuItem("Sistema", KeyEvent.VK_S);
        
        // Cria os itens do menu "Visualizar"
        visualEquipamento = new JMenuItem("Equipamentos", KeyEvent.VK_E);
        visualEquipe = new JMenuItem("Equipes de Intervenção", KeyEvent.VK_Q);
        visualInterventor = new JMenuItem("Interventores", KeyEvent.VK_I);
        
        // Cria os itens do menu "Ajuda"
        ajudaConteudo = new JMenuItem("Conteúdo da ajuda", KeyEvent.VK_C);
        ajudaSobre = new JMenuItem("Sobre", KeyEvent.VK_S);
        
        // Adiciona o ActionListener aos itens
        arquivoAlterarBanco.addActionListener(new banco());
        arquivoPrepararBanco.addActionListener(new banco());
        arquivoObterBanco.addActionListener(new banco());
        
        adicionarEquipamento.addActionListener(new adicionarEquipamento());
        adicionarIntervencao.addActionListener(new adicionarIntervencao());
        adicionarInterventor.addActionListener(new adicionarInterverntor());
        adicionarDemanda.addActionListener(new adicionarDemanda());
        adicionarEquipe.addActionListener(new adicionarEquipe());
        adicionarSistema.addActionListener(new adicionarSistema());
        
        alterarEquipamento.addActionListener(new alterarEquipamento());
        alterarDemanda.addActionListener(new alterarDemanda());
        alterarIntervencao.addActionListener(new alterarIntervencao());
        alterarInterventor.addActionListener(new alterarInterventor());
        alterarEquipe.addActionListener(new alterarEquipe());
        alterarSistema.addActionListener(new alterarSistema());
        
        visualEquipamento.addActionListener(new visualizar("Equipamento"));
        visualEquipe.addActionListener(new visualizar("Equipe"));
        visualInterventor.addActionListener(new visualizar("Interventor"));
        
        ajudaConteudo.addActionListener(new ajuda("Conteudo"));
        ajudaSobre.addActionListener(new ajuda("Sobre"));
        
        // Adiciona o item ao submenu "Arquivo"
        menuArquivo.add(arquivoAlterarBanco);
        menuArquivo.add(arquivoPrepararBanco);
        menuArquivo.add(arquivoObterBanco);
        
        // Adiciona o item ao submenu "Adicionar"
        menuAdicionar.add(adicionarDemanda);
        menuAdicionar.add(adicionarEquipamento);
        menuAdicionar.add(adicionarEquipe);
        menuAdicionar.add(adicionarIntervencao);
        menuAdicionar.add(adicionarInterventor);
        menuAdicionar.add(adicionarSistema);
        
        // Adiciona o item ao submenu "Alterar"
        menuAlterar.add(alterarDemanda);
        menuAlterar.add(alterarEquipamento);
        menuAlterar.add(alterarEquipe);
        menuAlterar.add(alterarIntervencao);
        menuAlterar.add(alterarInterventor);
        menuAlterar.add(alterarSistema);
        
        // Adiciona o item ao submenu "Visualizar"
        menuVisualizar.add(visualEquipamento);
        menuVisualizar.add(visualEquipe);
        menuVisualizar.add(visualInterventor);
        
        // Adiciona o item ao submenu "Ajuda"
        menuAjuda.add(ajudaConteudo);
        menuAjuda.add(ajudaSobre);
        
        // Adiciona o menu "Arquivo" à barra de menus
        barraMenu.add(menuArquivo);
        barraMenu.add(menuAdicionar);
        barraMenu.add(menuAlterar);
        barraMenu.add(menuVisualizar);
        barraMenu.add(menuAjuda);

        return barraMenu;
    }
    
//    private JPanel criaMenuSistema() {
//        
//    }
    
    /**
     * Cria o conteiner que guarda os componentes menores do menu principal.
     * 
     * @return Um Conteiner
     */
    public Container criaConteiner() {
//        JPanel conteiner = new JPanel();
//
//        conteiner.setLayout(new BorderLayout());
//        conteiner.setOpaque(true);
//        conteiner.add(PAINEL_PRINCIPAL);
        return PAINEL_PRINCIPAL;
    }
    
    /**
     * Exibe o menu principal.
     */
    public static void mostraMenuPrincipal() {
//        /* Conecta ao banco de dados */
//        Consulta.conectar();
        
        JFrame menuprincipal = new JFrame("MESOR Intervention Manager");
        menuprincipal.setLayout(new BorderLayout());
        menuprincipal.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        MenuPrincipal menuPrincipal = new MenuPrincipal();
        
        menuprincipal.setJMenuBar(menuPrincipal.criaBarraMenu());
        menuprincipal.setContentPane(menuPrincipal.criaConteiner());
        
        /**
         * Para adicionar algum painel ao menu principal, usa-se
         * "menuprincipal.add(menuPrincipal.metodoAdicionar());"
         * onde "metodoAdicionar()" é um método que retorne "JPanel"
         */
        
        menuprincipal.setIconImage(Janela.criarIcon("/res/icone/mesor.jpg").getImage());
//        menuprincipal.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        menuprincipal.setSize(1000,700);
        menuprincipal.setVisible(true);
        menuprincipal.setResizable(true);        
        menuprincipal.setLocationRelativeTo(null);
    }
    
    // -------------------------------------------------------------------------
    // Classes privadas.
    // -------------------------------------------------------------------------
    
    private class banco implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent event) {
            
            // indica se o botao de selecao de diretorio foi selecionado
            int op = 0;
            
            // PREPARAR PARA ENVIAR
            if (event.getSource() ==  arquivoPrepararBanco) {
                // configurar diretorios e preparar arquivo zip para o ususario anexar a um email                
                System.out.println("arquivoPrepararBanco");
                
                // Pega a pasta "data" do MySQL
                Object dataDir = Query.getQueryResultAsObject("SELECT @@datadir");
                
                // cria o seletor de pastas
                JFileChooser fc = new JFileChooser((String) dataDir);
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setApproveButtonText("Selecionar banco de dados");
                
                // Novo seletor de arquivo para escolher o caminho destino
                // mostra o dialog para selecionar o diretorio
                op = fc.showDialog(MenuPrincipal.this, null);
                
                // Quando a pasta for selecionada
                if(op == JFileChooser.APPROVE_OPTION) {
                    // referencia o item selecionado
                    File diretorioOrigem = fc.getCurrentDirectory();
                    
//                    // Seleciona a pasta destino do arquivo zipado
//                    JFileChooser fc2 = new JFileChooser();
//                    fc2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//                    fc2.setApproveButtonText("Selecionar pasta destino");
//                    
//                    int op2 = 0;
//                    op2 = fc2.showDialog(fc, null);
//                    
//                    if(op2 == JFileChooser.APPROVE_OPTION) {
//                        // Pega o diretorio destino
//                        File diretorioDestino = fc2.getCurrentDirectory();
                        
                        try {
                            // Criar aquivo zip do banco escolhido na pasta destino selecionada
                            Zip.ziparDiretorio(diretorioOrigem, null);
                        } catch (IOException ex) {
                            DialogoAviso.show("IOException em Zip.ziparDiretorio()");
                            Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                        }
//                    }
                }                
            } else if (event.getSource() ==  arquivoObterBanco) {
                // configurar diretorios para incluir banco obtido por email a pasta data do MySQL
                System.out.println("arquivoObterBanco");

                // cria o seletor de pastas
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setApproveButtonText("Selecionar pasta");
                
                // mostra o dialog para selecionar o diretorio
                op = fc.showDialog(MenuPrincipal.this, null);
            }
            
            
            
            
//            // cria o seletor de pastas
//            JFileChooser fc = new JFileChooser();
//            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//            fc.setApproveButtonText("Selecionar pasta");
//            
//            // indica se o botao de selecao de diretorio foi selecionado
//            int op = 0;
//                        
//            // mostra o dialog para selecionar o diretorio
//            op = fc.showDialog(MenuPrincipal.this, null);
//            if(op == JFileChooser.APPROVE_OPTION) {
//                // referencia o item selecionado
//                File file = fc.getSelectedFile();
//                File diretorio = fc.getCurrentDirectory();
//
//                // pega o nome da pasta selecionada como o nome do novo banco
//                String NEW_BD_NAME = file.getName();
//
//                // ALTERAR BANCO
//                if(event.getSource() == arquivoAlterarBanco) {                
//                    System.out.println("arquivoAlterarBanco");             
//
//                // altera o nome do banco 
////                Consulta.alterarBanco(NEW_BD_NAME);
//
//                // atualiza comboboxs
////                PainelPrincipal.cbxSQL.atualizarModelo();
//                }
//                // OBTER BANCO
//                else if (event.getSource() == arquivoObterBanco) {
//                    // configurar diretorios para incluir banco obtido por email a pasta data do MySQL
//                    System.out.println("arquivoObterBanco");
//                    
//                    // Pega a pasta "data" do MySQL
//                    Object dataDir = Query.getQueryResultAsObject("SELECT @@datadir");
//                }
//                // SALVAR PRA ENVIAR BANCO
//                else if (event.getSource() ==  arquivoPrepararBanco) {
//                    // configurar diretorios e preparar arquivo zip para o ususario anexar a um email                
//                    System.out.println("arquivoPrepararBanco");
//                    
//                    // pega o path da pasta
//                    String path = diretorio.getAbsolutePath();                    
//                    System.out.println(path);
//                    
////                    try {
////                        FileInputStream in = new FileInputStream(path.concat("\\"));
////                    } catch (FileNotFoundException ex) {
////                        Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
////                    }
////                    
////                    ZipOutputStream out = new ZipOutputStream(new FileOutputStream("C:/"));
//                    
//                    
//                }                
//            }
        }
    }

    private class adicionarEquipamento implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ADICIONAR EQUIPAMENTO.");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAdicionarEquipamento janela;
                janela = new JanelaAdicionarEquipamento();
                janela.mostrar("Adicionar unidade de equipamento", 0);
            }
        }
    }
    
    private class adicionarIntervencao implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ADICIONAR INTERVENÇÃO.");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAdicionarIntervencao janela;
                janela = new JanelaAdicionarIntervencao();
                janela.mostrar("Adicionar intervenção", 0);
            }
        }
    }
    
    private class adicionarInterverntor implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ADICIONAR INTERVENTOR.");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAdicionarInterventor janela = new JanelaAdicionarInterventor();
                janela.mostrar("Adicionar interventor");
            }
        }
    }
    
    private class adicionarDemanda implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ADICIONAR DEMANDA.");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAdicionarDemanda janela = new JanelaAdicionarDemanda();
                janela.mostrar("Adicionar demanda", 0);
            }
        }
    }
    
    private class adicionarEquipe implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ADICIONAR EQUIPE DE INTERVENÇÃO.");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAdicionarEquipe janela = new JanelaAdicionarEquipe();
                janela.mostrar("Adicionar equipe de intervenção");
            }
        }
    }
    
    private class adicionarSistema implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ADICIONAR SISTEMA.");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAdicionarSistema janela;
                janela = new JanelaAdicionarSistema();
                janela.mostrar("Adicionar sistema");
            }
        }
    }
    
    private class alterarEquipamento implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ALTERAR EQUIPAMENTO.");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAlterarEquipamento janela;
                janela = new JanelaAlterarEquipamento();
                janela.mostrar("Alterar/Excluir equipamento", 0);
            }
        }
    }
    
    private class alterarIntervencao implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ALTERAR INTERVENÇÃO.");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAlterarIntervencao janela;
                janela = new JanelaAlterarIntervencao();
                janela.mostrar("Alterar/Excluir intervenção", 0);
            }
        }
    }
    
    private class alterarInterventor implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ALTERAR INTERVENTOR.");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAlterarInterventor janela;
                janela = new JanelaAlterarInterventor();
                janela.mostrar("Alterar/Excluir interventor");
            }
        }
    }
    
    private class alterarDemanda implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ALTERAR DEMANDA.");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAlterarDemanda janela = new JanelaAlterarDemanda();
                janela.mostrar("Alterar/Excluir demanda", 0);
            }
        }
    }
    
    private class alterarEquipe implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ALTERAR EQUIPE.");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAlterarEquipe janela = new JanelaAlterarEquipe();
                janela.mostrar("Alterar/Excluir equipe de intervenção");
            }
        }
    }    
    
    private class alterarSistema implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ALTERAR SISTEMA.");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAlterarSistema janela = new JanelaAlterarSistema();
                janela.mostrar("Alterar/Excluir sistema");
            }
        }
    }
    
    private class ajuda implements ActionListener {
        
        private final String tipoAjuda;
        
        public ajuda(String t) {
            tipoAjuda = t;
        }
        
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: CONTEÚDO DA AJUDA.");
            if (eventSelected.equals("ActionEvent")) {
                
                File htmlFile;
                try {
                    htmlFile = new File(Toolkit.getDefaultToolkit().getClass().getResource("/res/ajuda/ajuda" + tipoAjuda + ".html").toURI());
                    Desktop.getDesktop().browse(htmlFile.toURI());
                } catch (URISyntaxException ex) {
                    DialogoAviso.show(ex.getLocalizedMessage());
                    Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    DialogoAviso.show(ex.getLocalizedMessage());
                    Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /**
     * Classe que lida com a ação dos itens do menu Visualizar
     */
    private class visualizar implements ActionListener {
        
        private final String tipoVis;
        private Component cmp;
        private String titulo;
        
        /**
         * Construtor
         * @param t tipo de elemento a ser visualizado. Pode ser "Equipamento",
         * "Equipe" ou "Interventor"
         */
        public visualizar(String t) {
            tipoVis = t;
        }
        
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: VISUALIZAR.");
            if (eventSelected.equals("ActionEvent")) {
                
                switch(tipoVis) {
                    case "Equipamento":
                        PainelEquipamento pnlEqp = new PainelEquipamento(
                                    PainelPrincipal.pnlAbaNordeste.getWidth(),
                                    PainelPrincipal.pnlAbaNordeste.getHeight());
                        cmp = pnlEqp.painelTabelas();
                        titulo = "Equipamentos";
                        break;
                    case "Equipe": 
                        PainelEquipe pnlEq = new PainelEquipe(
                                    PainelPrincipal.pnlAbaNordeste.getWidth(),
                                    PainelPrincipal.pnlAbaNordeste.getHeight());
                        cmp = pnlEq.painelTabelas();
                        titulo = "Equipes de Intervenção";
                        break;
                    case "Interventor": 
                        PainelInterventor pnlInt = new PainelInterventor(
                                    PainelPrincipal.pnlAbaNordeste.getWidth(),
                                    PainelPrincipal.pnlAbaNordeste.getHeight());
                        titulo = "Interventores";
                        cmp = pnlInt.painelTabelas();
                        break;
                }
                
                // Adiciona uma aba ao painel nordeste com o conteudo selecionado
                PainelPrincipal.pnlAbaNordeste.addTab(titulo, null, cmp, titulo);
            }
        }
    }
    
    /**
     * Executa o programa.
     * @param args 
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        //UIManager.put("swing.boldMetal", Boolean.FALSE);
        mostraMenuPrincipal();
        //SeletorArquivo sl = new SeletorArquivo();
        }
}