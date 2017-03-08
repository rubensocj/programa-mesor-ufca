package menu;

import menu.adicionar.*;
import menu.alterar.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * MenuPrincipal.java
 * 
 * @version 1.0 1/2/2017
 * @author Rubens Jr
 */
public class MenuPrincipal extends JFrame {
    
    private JMenuBar barraMenu;
    private JMenu menuAdicionar, menuAlterar;
    private JMenuItem novaDemanda, novoEquipamento, novaIntervencao, novaEquipe;
    private JMenuItem alterarDemanda, alterarEquipamento, alterarIntervencao,
                alterarEquipe;
        
    private final String eventSelected = "ActionEvent";

    public JMenuBar criaBarraMenu(){
        // Cria a barra de menu
        barraMenu = new JMenuBar();
        barraMenu.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        /**
         * Cria os menus e define os atalhos do teclado.
         */
        menuAdicionar = new JMenu("Adicionar");
        menuAdicionar.setMnemonic(KeyEvent.VK_A);
        
        menuAlterar = new JMenu("Alterar");
        menuAlterar.setMnemonic(KeyEvent.VK_L);
        
        /**      
         * Legenda exibida quando cursor do mouse é colocado sobre o componente
         * menuArquivo.setToolTipText("Arquivo");
         */
        
        // Cria os itens do menu "Adicionar"
        novaDemanda = new JMenuItem("Demanda", KeyEvent.VK_D);
        novoEquipamento = new JMenuItem("Equipamento", KeyEvent.VK_E);
//        novoEquipamento.setAccelerator(KeyStroke.getKeyStroke(
//                                        KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        
        novaIntervencao = new JMenuItem("Intervenção", KeyEvent.VK_I);
        novaEquipe = new JMenuItem("Equipe de Intervenção", KeyEvent.VK_T);
                
        // Adiciona o ActionListener aos itens
        novoEquipamento.addActionListener(new novoEquipamento());
        novaIntervencao.addActionListener(new novaIntervencao());
        novaDemanda.addActionListener(new novaDemanda());
        novaEquipe.addActionListener(new novaEquipe());
        
        // Cria os itens do menu "Alterar"
        alterarDemanda = new JMenuItem("Demanda", KeyEvent.VK_D);
        alterarEquipamento = new JMenuItem("Equipamento", KeyEvent.VK_E);
        alterarIntervencao = new JMenuItem("Intervenção", KeyEvent.VK_I);
        alterarEquipe = new JMenuItem("Equipe de Intervenção", KeyEvent.VK_Q);

        // Adiciona o ActionListener aos itens
        alterarEquipamento.addActionListener(new alterarEquipamento());
        alterarDemanda.addActionListener(new alterarDemanda());
        alterarIntervencao.addActionListener(new alterarIntervencao());
        alterarEquipe.addActionListener(new alterarEquipe());
        
        // Adiciona o item ao submenu "Adicionar"
        menuAdicionar.add(novaDemanda);
        menuAdicionar.add(novoEquipamento);
        menuAdicionar.add(novaEquipe);
        menuAdicionar.add(novaIntervencao);
        
        // Adiciona o item ao submenu "Alterar"
        menuAlterar.add(alterarDemanda);
        menuAlterar.add(alterarEquipamento);
        menuAlterar.add(alterarIntervencao);
        menuAlterar.add(alterarEquipe);
                
        // Adiciona o item (submenu) "Novo..." ao menu "Arquivo"
        //menuNovo.add(arquivoNovo);
                
        /**
         * Adiciona um separador de itens ao menu
         * menuArquivo.addSeparator(); 
         */
        
        // Adiciona o menu "Arquivo" à barra de menus
        barraMenu.add(menuAdicionar);
        barraMenu.add(menuAlterar);

        return barraMenu;
    }
        
    public Container criaConteiner() {
        JPanel conteiner = new JPanel();

        conteiner.setLayout(new BorderLayout());
        conteiner.setOpaque(true);
        return conteiner;
    }
    
    public static void mostraMenuPrincipal() {
        JFrame menuprincipal = new JFrame("Menu principal");
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
        
        menuprincipal.setSize(300,150);
        menuprincipal.setVisible(true);
        menuprincipal.setResizable(true);        
        menuprincipal.setLocationRelativeTo(null);
    }
    
    class novaDemanda implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: NOVA DEMANDA selecionado");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAdicionarDemanda janela = new JanelaAdicionarDemanda();
                janela.mostrar("Adicionar demanda", 0);
            }
        }
    }
    
    class novoEquipamento implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: NOVO EQUIPAMENTO selecionado");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAdicionarEquipamento janela;
                janela = new JanelaAdicionarEquipamento();
                janela.mostrar("Adicionar unidade de equipamento", 0);
            }
        }
    }
    
    class novaIntervencao implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: NOVA INTERVENÇÃO selecionado");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAdicionarIntervencao janela;
                janela = new JanelaAdicionarIntervencao();
                janela.mostrar("Adicionar intervenção", 0);
            }
        }
    }
    
    class novaEquipe implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: NOVA EQUIPE DE INTERVENÇÃO selecionado");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAdicionarEquipe janela = new JanelaAdicionarEquipe();
                janela.mostrar("Adicionar equipe de intervenção", 0);
            }
        }
    }
    
    class alterarEquipamento implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ALTERAR EQUIPAMENTO selecionado");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAlterarEquipamento janela;
                janela = new JanelaAlterarEquipamento();
                janela.mostrar("Alterar equipamento", 0);
            }
        }
    }
    
    class alterarDemanda implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ALTERAR DEMANDA selecionado");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAlterarDemanda janela = new JanelaAlterarDemanda();
                janela.mostrar("Alterar demanda", 0);
            }
        }
    }
    
    class alterarIntervencao implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ALTERAR INTERVENÇÃO selecionado");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAlterarIntervencao janela;
                janela = new JanelaAlterarIntervencao();
                janela.mostrar("Alterar intervenção", 0);
            }
        }
    }
    
    class alterarEquipe implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ALTERAR EQUIPE selecionado");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAlterarEquipe janela = new JanelaAlterarEquipe();
                janela.mostrar("Alterar equipe de intervenção", 0);
            }
        }
    }
    
    // Executa o programa
    public static void main(String[] args) {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        mostraMenuPrincipal();
        }
}