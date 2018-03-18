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

import mesor.sql.Consulta;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import static mesor.menu.Janela.LOCAL;
import mesor.r.CamadaR;

/**
 * MenuPrincipal.java
 * 
 * @version 1.0 1/2/2017
 * @author Rubens Jr
 */
public class MenuPrincipal extends JFrame {
    
    private JMenuBar barraMenu;
    private JMenu menuAdicionar, menuAlterar;
    private JMenuItem adicionarDemanda, adicionarEquipamento,
                adicionarIntervencao, adicionarInterventor, adicionarEquipe,
                adicionarSistema;
    private JMenuItem alterarDemanda, alterarEquipamento, alterarIntervencao,
                alterarEquipe, alterarSistema;
        
    private final String eventSelected = "ActionEvent";

    /**
     *
     */
    public static final PainelPrincipal PAINEL_PRINCIPAL = new PainelPrincipal();
    
    private JMenuBar criaBarraMenu() {
        // Cria a barra de menu
        barraMenu = new JMenuBar();
        barraMenu.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        /* Cria os menus e define os atalhos do teclado. */
        menuAdicionar = new JMenu("Adicionar");
        menuAdicionar.setMnemonic(KeyEvent.VK_A);
        
        menuAlterar = new JMenu("Alterar/Excluir");
        menuAlterar.setMnemonic(KeyEvent.VK_L);
        
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
        alterarEquipe = new JMenuItem("Equipe de Intervenção", KeyEvent.VK_Q);
        alterarSistema = new JMenuItem("Sistema", KeyEvent.VK_S);
        
        // Adiciona o ActionListener aos itens
        adicionarEquipamento.addActionListener(new adicionarEquipamento());
        adicionarIntervencao.addActionListener(new adicionarIntervencao());
        adicionarInterventor.addActionListener(new adicionarInterverntor());
        adicionarDemanda.addActionListener(new adicionarDemanda());
        adicionarEquipe.addActionListener(new adicionarEquipe());
        adicionarSistema.addActionListener(new adicionarSistema());
        
        alterarEquipamento.addActionListener(new alterarEquipamento());
        alterarDemanda.addActionListener(new alterarDemanda());
        alterarIntervencao.addActionListener(new alterarIntervencao());
        alterarEquipe.addActionListener(new alterarEquipe());
        alterarSistema.addActionListener(new alterarSistema());
        
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
        menuAlterar.add(alterarSistema);
        
        // Adiciona o menu "Arquivo" à barra de menus
        barraMenu.add(menuAdicionar);
        barraMenu.add(menuAlterar);

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
        
        menuprincipal.setIconImage(
                    new ImageIcon(LOCAL + "\\icone\\mesor.jpg").getImage());
//        menuprincipal.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        menuprincipal.setSize(1000,700);
        menuprincipal.setVisible(true);
        menuprincipal.setResizable(true);        
        menuprincipal.setLocationRelativeTo(null);
    }
    
    // -------------------------------------------------------------------------
    // Classes privadas.
    // -------------------------------------------------------------------------
    
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
                janela.mostrar("Alterar intervenção", 0);
            }
        }
    }
    
    private class alterarInterventor implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ALTERAR INTERVENTOR.");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAlterarIntervencao janela;
                janela = new JanelaAlterarIntervencao();
                janela.mostrar("Alterar interventor", 0);
            }
        }
    }
    
    private class alterarDemanda implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ALTERAR DEMANDA.");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAlterarDemanda janela = new JanelaAlterarDemanda();
                janela.mostrar("Alterar demanda", 0);
            }
        }
    }
    
    private class alterarEquipe implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ALTERAR EQUIPE.");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAlterarEquipe janela = new JanelaAlterarEquipe();
                janela.mostrar("Alterar equipe de intervenção");
            }
        }
    }    
    
    private class alterarSistema implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("ActionEvent: ALTERAR SISTEMA.");
            if (eventSelected.equals("ActionEvent")) {
                JanelaAlterarSistema janela = new JanelaAlterarSistema();
                janela.mostrar("Alterar sistema");
            }
        }
    }
    
    /**
     * Executa o programa.
     * @param args 
     */
    public static void main(String[] args) {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
//        CamadaR.main(args);
        mostraMenuPrincipal();
        }
}