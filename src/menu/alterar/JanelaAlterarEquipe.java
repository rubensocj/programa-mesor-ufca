package menu.alterar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import menu.adicionar.JanelaAdicionarEquipe;
import menu.JanelaAdicionarAlterar;
import menu.PainelEquipe;

import conexaoJavaSql.Lista;
import intervencao.Interventor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * JanelaAlterarEquipe.java
 *
 * @version 1.0 16/02/2017
 * @author Rubens Jr
 */
public class JanelaAlterarEquipe extends JanelaAdicionarEquipe {

    private JPanel pnlTabela;
    
    private JScrollPane dPane;
    
    private PainelEquipe pnlEqpGeral, pnlEqpInterventor;
    
    private Lista listaSQL;
    
    /**
     * Construtores.
     */
     public JanelaAlterarEquipe() {        
        /* Cria os botões antes de montar o painel Principal */
        criarBotoesOpcoes();
        criarBotoesAlterar();
     }
    
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
                dialog.dispose();
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
                            local + "\\ajuda\\janelaAdicionarEquipamento.html");
                try {
                    p.setPage(ajudaHTML.toURL());
                } catch (IOException ex) {ex.printStackTrace();}
                
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
    public void criarBotoesAlterar() {
        /**
         * Botão "Escolher" define o texto dos JTextFields de acordo com o
         * selecionado nas tabelas.
         */
        btnEscolher = new JButton();
        btnEscolher.addActionListener(new Escolher());
        btnEscolher.setPreferredSize(new Dimension(35, 25));
        btnEscolher.setIcon(
                    new ImageIcon(local + "\\icone\\seta_dupla_direita.png"));
        
        /**
         * Botão "Voltar" limpa o texto dos JTextFields e cancela a alteração.
         */
        btnVoltar = new JButton();
        btnVoltar.addActionListener(new Voltar());
        btnVoltar.setPreferredSize(new Dimension(35, 25));
        btnVoltar.setIcon(
                    new ImageIcon(local + "\\icone\\seta_dupla_esquerda.png"));
        
        /* Botão "Salvar alterações" executa a operação */
        btnSalvarAlteracao = new JButton("Salvar alterações");
        btnSalvarAlteracao.setPreferredSize(new Dimension(150,20));
        btnSalvarAlteracao.addActionListener(new Salvar());
    }
    
    @Override
    public JPanel montarPainelPrincipal() {
        /**
         * Painel que agrupa o painel com os botões "Escolher" e "Voltar", e o
         * painel de alteração completo (com o botão).
         */
        pnlAlterar = new JPanel(new FlowLayout());
        pnlAlterar.add(painelListas());
        pnlAlterar.add(painelInterventor());
        
        inicializarMapaComponentes();
        mapearComponentes();
        
        /* Desabilita a edição */
        habilitarEdicao(false);
        
        /**
         * Painel final: painel com abas e tabelas no lado esquerdo e painel
         * de escolha e alteração completo à direita.
         */
        pnlPrincipal = new JPanel(new BorderLayout());
        pnlPrincipal.add(painelTabelaEquipeGeral(), BorderLayout.WEST);
        pnlPrincipal.add(pnlAlterar, BorderLayout.EAST);        
        return pnlPrincipal;
    }
    
    @Override
    public JPanel painelTabelaEquipe() {
        pnlEqpInterventor = new PainelEquipe(PainelEquipe.Tabela.interventor);
        pnlEqpInterventor.tamanhoDaTabela(new Dimension(350, 200));
        pnlEqpInterventor.atualizarAparenciaDaTabela();
        pnlEqpInterventor.habilitarTabela(false);
        /**
         * MouseListener define as informações da linha selecionada como texto
         * dos jtextfields do painelInterventor.
         */
        pnlEqpInterventor.tabEquipe.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(pnlEqpInterventor.tabEquipe.getSelectedRowCount() == 1) {
                    tfdNome.setText(String.valueOf(
                        pnlEqpInterventor.tabEquipe.getValueAt(
                            pnlEqpInterventor.tabEquipe.getSelectedRow(),2)));
                    tfdSexo.setText(String.valueOf(
                        pnlEqpInterventor.tabEquipe.getValueAt(
                            pnlEqpInterventor.tabEquipe.getSelectedRow(),3)));
                    tfdNascimento.setText(String.valueOf(
                        pnlEqpInterventor.tabEquipe.getValueAt(
                            pnlEqpInterventor.tabEquipe.getSelectedRow(),4)));
                    tfdAdmissao.setText(String.valueOf(
                        pnlEqpInterventor.tabEquipe.getValueAt(
                            pnlEqpInterventor.tabEquipe.getSelectedRow(),5)));
                    tfdCargo.setText(String.valueOf(
                        pnlEqpInterventor.tabEquipe.getValueAt(
                            pnlEqpInterventor.tabEquipe.getSelectedRow(),6)));
                    tfdFormacao.setText(String.valueOf(
                        pnlEqpInterventor.tabEquipe.getValueAt(
                            pnlEqpInterventor.tabEquipe.getSelectedRow(),7)));
                    tfdRemuneracao.setText(String.valueOf(
                        pnlEqpInterventor.tabEquipe.getValueAt(
                            pnlEqpInterventor.tabEquipe.getSelectedRow(),8)));
                    tfdEstadoCivil.setText(String.valueOf(
                        pnlEqpInterventor.tabEquipe.getValueAt(
                            pnlEqpInterventor.tabEquipe.getSelectedRow(),9)));
                    tfdEndereco.setText(String.valueOf(
                        pnlEqpInterventor.tabEquipe.getValueAt(
                            pnlEqpInterventor.tabEquipe.getSelectedRow(),10)));
                    tfdCidade.setText(String.valueOf(
                        pnlEqpInterventor.tabEquipe.getValueAt(
                            pnlEqpInterventor.tabEquipe.getSelectedRow(),11)));
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
        
        JPanel pnlSalvarAlteracao = new JPanel(new FlowLayout(2));
        pnlSalvarAlteracao.add(btnSalvarAlteracao);
        
        JPanel pnlInterventor = new JPanel(new BorderLayout());
        pnlInterventor.add(pnlEqpInterventor.painelTabelas(),
                                                        BorderLayout.NORTH);
        pnlInterventor.add(pnlSalvarAlteracao, BorderLayout.CENTER);
        
        return pnlInterventor;
    }
    
    @Override
    public void mapearComponentes() {
        mapaComponentes.put(0, tfdObjEspecificos);
        mapaComponentes.put(1, tfdHabRequeridas);
        mapaComponentes.put(2, tfdExpRequeridas);
        mapaComponentes.put(3, listaObjEspecificos);
        mapaComponentes.put(4, listaHabRequeridas);
        mapaComponentes.put(5, listaExpRequeridas);
        mapaComponentes.put(6, lblNome);
        mapaComponentes.put(7, tfdNome);
        mapaComponentes.put(8, lblSexo);
        mapaComponentes.put(9, tfdSexo);
        mapaComponentes.put(10, lblNascimento);
        mapaComponentes.put(11, tfdNascimento);
        mapaComponentes.put(12, lblAdmissao);
        mapaComponentes.put(13, tfdAdmissao);
        mapaComponentes.put(14, lblCargo);
        mapaComponentes.put(15, tfdCargo);
        mapaComponentes.put(16, lblFormacao);
        mapaComponentes.put(17, tfdFormacao);
        mapaComponentes.put(18, lblRemuneracao);
        mapaComponentes.put(19, tfdRemuneracao);
        mapaComponentes.put(20, lblEstadoCivil);
        mapaComponentes.put(21, tfdEstadoCivil);
        mapaComponentes.put(22, lblEndereco);
        mapaComponentes.put(23, tfdEndereco);
        mapaComponentes.put(24, lblCidade);
        mapaComponentes.put(25, tfdCidade);
        mapaComponentes.put(26, btnAddExperiencia);
        mapaComponentes.put(27, btnAddHabilidade);
        mapaComponentes.put(28, btnAddInterventor);
        mapaComponentes.put(29, btnAddObjetivo);
        mapaComponentes.put(30, btnRemExperiencia);
        mapaComponentes.put(31, btnRemHabilidade);
        mapaComponentes.put(32, btnRemObjetivo);
        mapaComponentes.put(33, btnRemInterventor);
        mapaComponentes.put(34, pnlEqpInterventor.tabEquipe);
        mapaComponentes.put(35, tfdObjetivoGeral);
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
    
    @Override
    public void habilitarEdicao(boolean escolha) {
        if(escolha == true) {
            for(int i = 0; i <= 35; i++) {
                mapaComponentes.get(i).setEnabled(true); // Habilita.
            }
        } else {
            for(int i = 0; i <= 35; i++) {
                mapaComponentes.get(i).setEnabled(false); // Desabilita.
            }
        }
        
        edicaoPermitida = escolha;
    }
        
    // -------------------------------------------------------------------------
    // Métodos privados.
    // -------------------------------------------------------------------------
    
    /**
     * Monta o painel da tabela com informações gerais da equipe de intervenção.
     * 
     * @return Um JPanel.
     */
    private JPanel painelTabelaEquipeGeral() {
        /* Painel com os botões de escolher e voltar */
        JPanel pnlBtnAlterar = new JPanel(new BorderLayout(0,10));
        pnlBtnAlterar.add(btnEscolher, BorderLayout.NORTH);
        pnlBtnAlterar.add(btnVoltar, BorderLayout.CENTER);
        
        pnlEqpGeral = new PainelEquipe(PainelEquipe.Tabela.equipe);
        pnlEqpGeral.tamanhoDaTabela(new Dimension(300,350));
        pnlEqpGeral.atualizarAparenciaDaTabela();
        
        JLabel lblEquipeGeral = new JLabel("Selecione uma equipe: ");
        
        JPanel pnlEquipeGeral1 = new JPanel(new BorderLayout());
        pnlEquipeGeral1.add(lblEquipeGeral, BorderLayout.NORTH);
        pnlEquipeGeral1.add(pnlEqpGeral.painelTabelas(), BorderLayout.CENTER);
        
        JPanel pnlEquipeGeral2 = new JPanel(new FlowLayout());
        pnlEquipeGeral2.add(pnlEquipeGeral1);
        pnlEquipeGeral2.add(pnlBtnAlterar);
        return pnlEquipeGeral2;
    }
    
    // -------------------------------------------------------------------------
    // Classes.
    // -------------------------------------------------------------------------
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionEscolher
     */
    private class Escolher extends JanelaAdicionarAlterar.ActionEscolher {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(pnlEqpGeral.tabEquipe.getSelectedRowCount() != 0) {
                habilitarEdicao(true);
                pnlEqpInterventor.habilitarTabela(true);
                pnlEqpGeral.habilitarTabela(false);
                
                modeloListaObjEspecificos.removeAllElements();
                modeloListaExpRequeridas.removeAllElements();
                modeloListaHabRequeridas.removeAllElements();
                
                
                /** Objetivo geral */
                tfdObjetivoGeral.setText(String.valueOf(
                            pnlEqpGeral.tabEquipe.getValueAt(
                                pnlEqpGeral.tabEquipe.getSelectedRow(), 1)));
                
                String tabela;
                String idEquipe = String.valueOf(
                            pnlEqpGeral.tabEquipe.getValueAt(
                                    pnlEqpGeral.tabEquipe.getSelectedRow(), 0));
                
                try {
                    listaSQL = new Lista();
                    
                    /** Lista Objetivos específicos */
                    tabela = "objetivos_especificos";
                    listaSQL.setQuery(
                        "SELECT " + tabela + ".descricao " +
                        "FROM equipe, " + tabela + " " +
                        "WHERE " + tabela + ".id_equipe = " + idEquipe + " " +
                        "GROUP BY " + tabela + ".id;");
                    
                    for(int i = 0; i < listaSQL.toVector().size(); i++) {
                        modeloListaObjEspecificos.addElement(
                                    listaSQL.toVector().get(i));
                    }
                    
                    /** Lista Experiências requeridas */
                    tabela = "experiencias_requeridas";
                    listaSQL.setQuery(
                        "SELECT " + tabela + ".descricao " +
                        "FROM equipe, " + tabela + " " +
                        "WHERE " + tabela + ".id_equipe = " + idEquipe + " " +
                        "GROUP BY " + tabela + ".id;");
                    
                    for(int i = 0; i < listaSQL.toVector().size(); i++) {
                        modeloListaExpRequeridas.addElement(
                                    listaSQL.toVector().get(i));
                    }
                    
                    /** Lista Habilidades requeridas */
                    tabela = "habilidades_requeridas";
                    listaSQL.setQuery(
                        "SELECT " + tabela + ".descricao " +
                        "FROM equipe, " + tabela + " " +
                        "WHERE " + tabela + ".id_equipe = " + idEquipe + " " +
                        "GROUP BY " + tabela + ".id;");
                    
                    for(int i = 0; i < listaSQL.toVector().size(); i++) {
                        modeloListaHabRequeridas.addElement(
                                    listaSQL.toVector().get(i));
                    }
                    
                    /** Tabela de interventores */
                    pnlEqpInterventor.buscarEquipeInterventor(idEquipe);
                    pnlEqpInterventor.atualizarAparenciaDaTabela();
                } catch(SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionSalvar
     */
    private class Salvar extends JanelaAdicionarAlterar.ActionSalvar {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(edicaoPermitida == true) {
                confirmar();
            }
        }
    }
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionVoltar
     */
    private class Voltar extends JanelaAdicionarAlterar.ActionVoltar {
        @Override
        public void actionPerformed(ActionEvent event) {
            habilitarEdicao(false);
            tfdObjetivoGeral.setText("");
            pnlEqpInterventor.habilitarTabela(false);
            pnlEqpGeral.habilitarTabela(true);
            
            modeloListaObjEspecificos.removeAllElements();
            modeloListaExpRequeridas.removeAllElements();
            modeloListaHabRequeridas.removeAllElements();
            
            try {
                pnlEqpInterventor.reiniciarTabela();
            } catch(SQLException ex) {
                ex.printStackTrace();
            }
            pnlEqpInterventor.atualizarAparenciaDaTabela();
        }
    }
}