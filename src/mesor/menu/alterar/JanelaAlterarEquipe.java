package mesor.menu.alterar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dialog;
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

import mesor.menu.adicionar.JanelaAdicionarEquipe;
import mesor.menu.Janela;
import mesor.menu.painel.taxonomia.PainelEquipe;

import mesor.sql.Lista;
import mesor.intervencao.Interventor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import mesor.intervencao.Equipe;
import mesor.menu.DialogoAviso;
import mesor.menu.DialogoConfirma;
import mesor.menu.painel.taxonomia.PainelInterventor;
import mesor.sql.Query;

/**
 * JanelaAlterarEquipe.java
 *
 * @version 1.0 16/02/2017
 * @author Rubens Jr
 */
public class JanelaAlterarEquipe extends JanelaAdicionarEquipe {

    private JPanel pnlTabela;
    
    private JScrollPane dPane;
    
    private PainelEquipe pnlEqpGeral;
    private PainelInterventor pnlInterventor;
    
    private Lista listaSqlInterventor;
    private Lista listaSqlObjetivos, listaSqlIdObj;
    private Lista listaSqlHabilidades, listaSqlIdHab;
    private Lista listaSqlExperiencias, ListaSqlIdExp;
    
    private final ArrayList intvAntigo = new ArrayList<>();
    private final ArrayList intvNovo = new ArrayList<>();
    
    /**
     * Construtores.
     */
     public JanelaAlterarEquipe() {
//        modeloListaExpRequeridas.addListDataListener(new ListaListener());

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
                            LOCAL + "\\ajuda\\janelaAdicionarEquipamento.html");
                try {
                    p.setPage(ajudaHTML.toURL());
                } catch (IOException ex) {
                    DialogoAviso.show(ex.getMessage());
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
    public void criarBotoesAlterar() {
        /**
         * Botão "Escolher" define o texto dos JTextFields de acordo com o
         * selecionado nas tabelas.
         */
        btnEscolher = new JButton();
        btnEscolher.addActionListener(new Escolher());
        btnEscolher.setPreferredSize(new Dimension(35, 25));
        btnEscolher.setIcon(Janela.criarIcon("/res/icone/seta_dupla_direita.png"));
        
        /**
         * Botão "Voltar" limpa o texto dos JTextFields e cancela a alteração.
         */
        btnVoltar = new JButton();
        btnVoltar.addActionListener(new Voltar());
        btnVoltar.setPreferredSize(new Dimension(35, 25));
        btnVoltar.setIcon(Janela.criarIcon("/res/icone/seta_dupla_esquerda.png"));
        
        /* Botão "Salvar alterações" executa a operação */
        btnSalvarAlteracao = new JButton("Salvar alterações");
        btnSalvarAlteracao.setPreferredSize(new Dimension(150,20));
        btnSalvarAlteracao.addActionListener(new Salvar());
    }
    
    @Override
    public JPanel montarPainelPrincipal() {
        /* Painel com os botões de escolher e voltar */
        JPanel pnlBtnAlterar = new JPanel(new BorderLayout(0,5));
        pnlBtnAlterar.add(btnEscolher, BorderLayout.NORTH);
        pnlBtnAlterar.add(btnVoltar, BorderLayout.CENTER);
        
        JPanel pnlSalvarAlteracao = new JPanel(new FlowLayout(2));
        pnlSalvarAlteracao.add(btnSalvarAlteracao);
        /**
         * Painel de alteração completo, que agrupa o painel de alterações do
         * interventor e o painel com o botão que salva as alterações feitas.
         */
        JPanel pnlAlterarEquipe = new JPanel(new BorderLayout());
        pnlAlterarEquipe.add(painelExcluirEquipe(), BorderLayout.NORTH);
        pnlAlterarEquipe.add(pnlSalvarAlteracao, BorderLayout.CENTER);
        
        /**
         * Painel que agrupa o painel com os botões "Escolher" e "Voltar", e o
         * painel de alteração completo (com o botão).
         */
        pnlAlterar = new JPanel(new FlowLayout());
        pnlAlterar.add(pnlAlterarEquipe);
        
        pnlEqpGeral = new PainelEquipe(312, 200);
        JPanel pnlEqp = new JPanel(new FlowLayout());
        pnlEqp.add(pnlEqpGeral.painelTabelas());
        pnlEqp.add(pnlBtnAlterar);
        
        pnlIntv = new PainelInterventor(400, 300);
        pnlIntv.tabInterventor.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JPanel pnlPnlIntv = new JPanel(new FlowLayout());
        pnlPnlIntv.add(pnlIntv.painelTabelas());

        inicializarMapaComponentes();
        mapearComponentes();
        
        /* Desabilita a edição */
        habilitarEdicao(false);
        
        /**
         * Painel final: painel com abas e tabelas no lado esquerdo e painel
         * de escolha e alteração completo à direita.
         */
        pnlPrincipal = new JPanel(new BorderLayout());
        pnlPrincipal.add(pnlEqp, "West");
        pnlPrincipal.add(pnlAlterar, "Center");        
        pnlPrincipal.add(pnlPnlIntv, "East");        
        return pnlPrincipal;
    }
    
    @Override
    public void confirmar() {
        int idEquipe = (int) pnlEqpGeral.tabEquipe.getValueAt(pnlEqpGeral.tabEquipe.getSelectedRow(), 0);

        // Define o objetivo geral da equipe
        equipe.setIdBD(idEquipe);
        if(!tfdObjetivoGeral.getText().equals(equipe.getObjetivoGeral())) {
            equipe.setObjetivoGeral(tfdObjetivoGeral.getText());
            equipe.sqlAlterarObjetivoGeral();
        }
        
        // Experiencias
        List l1 = listaSqlExperiencias.toList(); // Antigo
        List l2 = toList(modeloListaExpRequeridas); // Novo
        if(l1.size() == l2.size()) {    // Tamanhos iguais
            if(!l1.containsAll(l2)) {
                // Conteúdo diferente
                for(int er = 0; er < l2.size(); er++) {
                    equipe.setExperiencia((String) modeloListaExpRequeridas.getElementAt(er),
                                Integer.parseInt((String) ListaSqlIdExp.get(er)));
                }
                equipe.sqlAlterarExperiencia();
            }
        } else {    // Tamanhos diferentes
            equipe.sqlExcluirExperiencia();
            for(int er = 0; er < l2.size(); er++) {
                equipe.setExperiencia((String) modeloListaExpRequeridas.getElementAt(er));
            }
            equipe.sqlInserirExperiencia();
        }
        
        // Objetivos
        l1 = listaSqlObjetivos.toList(); // Antigo
        l2 = toList(modeloListaObjEspecificos); // Novo
        if(l1.size() == l2.size()) {    // Tamanhos iguais
            if(!l1.containsAll(l2)) {
                // Conteúdo diferente
                for(int er = 0; er < l2.size(); er++) {
                    equipe.setObjetivoEspecifico((String) modeloListaObjEspecificos.getElementAt(er),
                                Integer.parseInt((String) listaSqlIdObj.get(er)));
                }
                equipe.sqlAlterarObjetivo();
            }
        } else {    // Tamanhos diferentes
            equipe.sqlExcluirObjetivo();
            for(int er = 0; er < l2.size(); er++) {
                equipe.setObjetivoEspecifico((String) modeloListaObjEspecificos.getElementAt(er));
            }
            equipe.sqlInserirObjetivo();
        }
        
        // Habilidades
        l1 = listaSqlHabilidades.toList(); // Antigo
        l2 = toList(modeloListaHabRequeridas); // Novo
        if(l1.size() == l2.size()) {    // Tamanhos iguais
            if(!l1.containsAll(l2)) {
                // Conteúdo diferente
                for(int er = 0; er < l2.size(); er++) {
                    equipe.setHabilidade((String) modeloListaHabRequeridas.getElementAt(er),
                                Integer.parseInt((String) listaSqlIdHab.get(er)));
                }
                equipe.sqlAlterarHabilidade();
            }
        } else {    // Tamanhos diferentes
            equipe.sqlExcluirHabilidade();
            for(int er = 0; er < l2.size(); er++) {
                equipe.setHabilidade((String) modeloListaHabRequeridas.getElementAt(er));
            }
            equipe.sqlInserirHabilidade();
        }
        
        // Interventores
        l1 = listaSqlInterventor.toList(); // Antigo
        int linhas = pnlIntv.tabInterventor.getSelectedRowCount();
        
        for(int i = 0; i < linhas; i++) {
            intvNovo.add(pnlIntv.tabInterventor.getValueAt(pnlIntv.tabInterventor.getSelectedRows()[i], 0));
        }
        
        if(l1.size() == linhas) {    // Tamanhos iguais
            if(!l1.containsAll(intvNovo)) {
                // Conteúdo diferente
                for(int er = 0; er < intvNovo.size(); er++) {
                    // Substitui os interventores
                    Interventor it1 = new Interventor();
                    it1.setIdBD((int) intvNovo.get(er));
                    equipe.setInterventor(it1);
                    equipe.sqlAlterarInterventor(it1, Integer.parseInt((String) listaSqlInterventor.get(er)));
                }
            }
        } else {    // Tamanhos diferentes
            equipe.sqlExcluirInterventor();
            
            // Define os interventores
            if(pnlIntv.tabInterventor.getSelectedRowCount() != 0) {
                // Define os interventores da equipe
                for(int it = 0; it < pnlIntv.tabInterventor.getSelectedRowCount(); it++) {
                    // Cria um objeto da classe Interventor para cada linha 
                    // selecionada na tabela. Pega pelo id.
                    Interventor it2 = new Interventor();
                    it2.setIdBD((int) pnlIntv.tabInterventor.getValueAt(
                                pnlIntv.tabInterventor.getSelectedRows()[it], 0));
                    // Adiciona o interventor ao Vetor de interventores.
                    equipe.setInterventor(it2);
                }
                
                equipe.sqlInserirInterventor();
            }
        }
        
        pnlEqpGeral.reiniciarTabela();
        pnlEqpGeral.atualizarAparenciaDaTabela();
        
        modeloListaExpRequeridas.clear();
        modeloListaHabRequeridas.clear();
        modeloListaObjEspecificos.clear();
        
        equipe.getListaExperiencias().clear();
        equipe.getListaHabilidades().clear();
        equipe.getListaObjetivos().clear();
        equipe.getListaInterventor().clear();
        
        tfdObjetivoGeral.setText("");
        
        pnlIntv.limpaSelecao();
    }
    
    @Override
    public void mapearComponentes() {
        mapaComponentes.put(0, tfdObjEspecificos);
        mapaComponentes.put(1, tfdHabRequeridas);
        mapaComponentes.put(2, tfdExpRequeridas);
        mapaComponentes.put(3, listaObjEspecificos);
        mapaComponentes.put(4, listaHabRequeridas);
        mapaComponentes.put(5, listaExpRequeridas);
        mapaComponentes.put(6, tfdObjetivoGeral);
        mapaComponentes.put(7, pnlIntv.painelTabelas());
        mapaComponentes.put(8, btnAddExperiencia);
        mapaComponentes.put(9, btnAddHabilidade);
        mapaComponentes.put(10, btnAddObjetivo);
        mapaComponentes.put(11, btnRemObjetivo);
        mapaComponentes.put(12, btnRemExperiencia);
        mapaComponentes.put(13, btnRemHabilidade);
        mapaComponentes.put(14, btnRemEquipe);
        mapaComponentes.put(15, btnSalvarAlteracao);
        mapaComponentes.put(16, btnRenoExperiencia);
        mapaComponentes.put(17, btnRenoHabilidade);
        mapaComponentes.put(18, btnRenoObjetivo);
    }
    
    @Override
    public void habilitarEdicao(boolean escolha) {
        for(int i = 0; i <= 18; i++) {
            mapaComponentes.get(i).setEnabled(escolha); // Habilita.
            pnlIntv.habilitarTabela(escolha);
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
     * @deprecated 
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

    private Component painelExcluirEquipe() {
        // Botão "Remover" interventor.
        btnRemEquipe = new JButton("Remover");
        btnRemEquipe.setPreferredSize(new Dimension(90, 20));
        btnRemEquipe.addActionListener(new Excluir());

        // Painel com botões.
        pnlBtnEqp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtnEqp.add(btnRemEquipe);
        
        // Painel "Interventor".
        JPanel pnlEqp4 = new JPanel(new BorderLayout());
        pnlEqp4.setOpaque(true);
        pnlEqp4.add(painelListas(), BorderLayout.NORTH);
        pnlEqp4.add(pnlBtnEqp, BorderLayout.CENTER);
        
        return pnlEqp4;
    }

    private class ListaListener implements ListDataListener {

        public ListaListener() {
        }

        @Override
        public void intervalAdded(ListDataEvent e) {
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
        }
    }
    
    // -------------------------------------------------------------------------
    // Classes privadas.
    // -------------------------------------------------------------------------
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionEscolher
     */
    private class Escolher extends Janela.ActionEscolher {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(pnlEqpGeral.tabEquipe.getSelectedRowCount() != 0) {
                habilitarEdicao(true);
                
                modeloListaObjEspecificos.removeAllElements();
                modeloListaExpRequeridas.removeAllElements();
                modeloListaHabRequeridas.removeAllElements();
                pnlIntv.tabInterventor.clearSelection();
                
                int r = pnlEqpGeral.tabEquipe.getSelectedRow();
                
                /** Objetivo geral */
                tfdObjetivoGeral.setText(String.valueOf(
                            pnlEqpGeral.tabEquipe.getValueAt(r, 1)));
                equipe.setObjetivoGeral(tfdObjetivoGeral.getText());
                
                String tabela;
                String idEquipe = String.valueOf(
                            pnlEqpGeral.tabEquipe.getValueAt(r, 0));
                
                try {
                    listaSqlInterventor = new Lista();
                    listaSqlObjetivos = new Lista();
                    listaSqlExperiencias = new Lista();
                    listaSqlHabilidades = new Lista();
                    
                    listaSqlIdObj = new Lista();
                    ListaSqlIdExp = new Lista();
                    listaSqlIdHab = new Lista();
                    
                    /** Lista Objetivos específicos */
                    tabela = "objetivos_especificos";
                    listaSqlObjetivos.setQuery(
                        "SELECT " + tabela + ".descricao " +
                        "FROM equipe, " + tabela + " " +
                        "WHERE " + tabela + ".id_equipe = " + idEquipe + " " +
                        "GROUP BY " + tabela + ".id;");
                    listaSqlIdObj.setQuery(
                        "SELECT " + tabela + ".id " +
                        "FROM equipe, " + tabela + " " +
                        "WHERE " + tabela + ".id_equipe = " + idEquipe + " " +
                        "GROUP BY " + tabela + ".id;");
                    
                    for(int i = 0; i < listaSqlObjetivos.toVector().size(); i++) {
                        modeloListaObjEspecificos.addElement(
                                    listaSqlObjetivos.toVector().get(i));
                        equipe.setObjetivoEspecifico(listaSqlObjetivos.getString(i), Integer.parseInt((String)listaSqlIdObj.get(i)));
                    }
                    
                    /** Lista Experiências requeridas */
                    tabela = "experiencias_requeridas";
                    listaSqlExperiencias.setQuery(
                        "SELECT " + tabela + ".descricao " +
                        "FROM equipe, " + tabela + " " +
                        "WHERE " + tabela + ".id_equipe = " + idEquipe + " " +
                        "GROUP BY " + tabela + ".id;");
                    ListaSqlIdExp.setQuery(
                        "SELECT " + tabela + ".id " +
                        "FROM equipe, " + tabela + " " +
                        "WHERE " + tabela + ".id_equipe = " + idEquipe + " " +
                        "GROUP BY " + tabela + ".id;");
                    
                    for(int i = 0; i < listaSqlExperiencias.toVector().size(); i++) {
                        modeloListaExpRequeridas.addElement(
                                    listaSqlExperiencias.toVector().get(i));
                        equipe.setExperiencia(listaSqlExperiencias.getString(i), Integer.parseInt((String)ListaSqlIdExp.get(i)));
                    }
                    
                    /** Lista Habilidades requeridas */
                    tabela = "habilidades_requeridas";
                    listaSqlHabilidades.setQuery(
                        "SELECT " + tabela + ".descricao " +
                        "FROM equipe, " + tabela + " " +
                        "WHERE " + tabela + ".id_equipe = " + idEquipe + " " +
                        "GROUP BY " + tabela + ".id;");
                    listaSqlIdHab.setQuery(
                        "SELECT " + tabela + ".id " +
                        "FROM equipe, " + tabela + " " +
                        "WHERE " + tabela + ".id_equipe = " + idEquipe + " " +
                        "GROUP BY " + tabela + ".id;");
                    
                    for(int i = 0; i < listaSqlHabilidades.toVector().size(); i++) {
                        modeloListaHabRequeridas.addElement(
                                    listaSqlHabilidades.toVector().get(i));
                        equipe.setHabilidade(listaSqlHabilidades.getString(i), Integer.parseInt((String)listaSqlIdHab.get(i)));
                    }
                    
                    /** Lista Habilidades requeridas */
                    tabela = "interventor";
                    listaSqlInterventor.setQuery(
                        "SELECT " + tabela + ".id " +
                        "FROM equipe, aux_equipe, " + tabela + " " +
                        "WHERE equipe.id = " + idEquipe + " AND aux_equipe.id_equipe = equipe.id " + 
                        "AND aux_equipe.id_interventor = " + tabela + ".id " +
                        "GROUP BY " + tabela + ".id;");
                    
                    for(int i = 0; i < listaSqlInterventor.toVector().size(); i++) {
                        for(int j = 0; j < pnlIntv.tabInterventor.getRowCount(); j++) {
                            if(String.valueOf(pnlIntv.tabInterventor.getValueAt(j, 0)).equals(listaSqlInterventor.toVector().get(i))) {
                                pnlIntv.tabInterventor.addRowSelectionInterval(j, j);
                                break;
                            }
                        }
                    }
                } catch(SQLException ex) {
                    DialogoAviso.show(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    }
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionVoltar
     */
    private class Voltar extends Janela.ActionVoltar {
        @Override
        public void actionPerformed(ActionEvent event) {
            tfdObjetivoGeral.setText("");
            
            modeloListaObjEspecificos.removeAllElements();
            modeloListaExpRequeridas.removeAllElements();
            modeloListaHabRequeridas.removeAllElements();
            pnlIntv.tabInterventor.clearSelection();
            
            habilitarEdicao(false);
        }
    }
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionSalvar
     */
    private class Salvar extends Janela.ActionSalvar {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(pnlEqpGeral.tabEquipe.getSelectedRowCount() == 1 && edicaoPermitida == true) {
                confirmar();
            }
        }
    }

    /**
     * Classe privada que implementa ActionListener. Remove a equipe selecionada
     */
    private class Excluir implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Se uma equipe estiver selecionada e permitida a edição
            if(pnlEqpGeral.tabEquipe.getSelectedRowCount() == 1) {
                JButton btnOk = new JButton("Excluir");
                JButton btnCan = new JButton("Cancelar");
                
                // Cria e mostra diálogo de confirmação com mensagem e botões definidos
                DialogoConfirma d = new DialogoConfirma(
                            "Tem certeza que deseja excluir esta equipe? "
                            + "Esta ação não pode ser desfeita.");
                
                btnOk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Cria objeto equipe
                        Equipe eq = new Equipe();
                        eq.setIdBD((int) pnlEqpGeral.tabEquipe.getValueAt(
                                    pnlEqpGeral.tabEquipe.getSelectedRow(), 0));
                        
                        // Exclui a equipe e desvincula os interventores
                        eq.sqlExcluir();
                        
                        // Dispóe o diálogo
                        d.dispose();
                        
                        habilitarEdicao(false);
                        pnlEqpGeral.atualizarAparenciaDaTabela();
                        pnlEqpGeral.tabEquipe.clearSelection();
                        pnlIntv.tabInterventor.clearSelection();
                    }
                });
                
                btnCan.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        d.dispose();
                    }
                });
                
                d.setOptions(new Object[] {btnOk, btnCan});
                d.show();
            }
        }
    }
    
    private boolean verificarListas(List l1, List l2) {
        boolean b = true;
        if(l1.size() == l2.size()) {
            
            
        }
        
        if(l1.containsAll(l2)) {
            
        }
        
        return b;
    }
    
    /**
     * 
     * @param m
     * @return Um objeto List a partir do DefaultListModel
     */
    private List toList(DefaultListModel m) {
        List<Object> l = new ArrayList<>();
        
        for(int i = 0; i < m.size(); i++) {
            l.add(m.get(i));
        }
        
        return l;
    }
}