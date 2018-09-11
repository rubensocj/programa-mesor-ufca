package mesor.menu.alterar;

import mesor.equipamento.Sistema;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import mesor.menu.Janela;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.border.BevelBorder;
import mesor.intervencao.Equipe;
import mesor.menu.DialogoAviso;
import mesor.menu.DialogoConfirma;

import mesor.menu.adicionar.JanelaAdicionarSistema;
import mesor.menu.painel.taxonomia.PainelSistema;

/**
 * JanelaAdicionarEquipamento.Java
 * 
 * @version 1.0 31/01/2017
 * @author Rubens Jr
 */
public class JanelaAlterarSistema extends JanelaAdicionarSistema {
    
    public PainelSistema pnlSistema;
    private JButton btnExcluir;
    
    /**
     * Construtor.
     */
    public JanelaAlterarSistema() {
        pnlSistema = new PainelSistema();
        pnlSistema.tamanhoDaTabela(new Dimension(290, 250));
        pnlSistema.habilitarTabela(true);
        
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
        btn2 = criarBotaoAjuda();
        options = new Object[] {this.btn1, this.btn2};
        
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
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
        
        /* Botão "Salvar alterações" executa a operação */
        btnExcluir = new JButton("Excluir");
        btnExcluir.setPreferredSize(new Dimension(100,20));
        btnExcluir.addActionListener(new Excluir());
    }
    
    @Override
    public JPanel montarPainelPrincipal() {
        /**
         * Painel com os botões de escolher e voltar.
         */
        JPanel pnlBtnAlterar = new JPanel(new BorderLayout(0,10));
        pnlBtnAlterar.add(btnEscolher, BorderLayout.NORTH);
        pnlBtnAlterar.add(btnVoltar, BorderLayout.CENTER);
        
        JPanel pnlAlterar1 = new JPanel(new FlowLayout());
        pnlAlterar1.add(painelGeral());
        
        JPanel pnlSalvarAlteracao = new JPanel(new FlowLayout(2));
        pnlSalvarAlteracao.add(btnSalvarAlteracao);
        pnlSalvarAlteracao.add(btnExcluir);
        
        JPanel pnlAlterar2 = new JPanel(new BorderLayout());
        pnlAlterar2.add(pnlAlterar1, BorderLayout.NORTH);
        pnlAlterar2.add(pnlSalvarAlteracao, BorderLayout.CENTER);
        
        pnlAlterar = new JPanel(new FlowLayout());
        pnlAlterar.add(pnlBtnAlterar);
        pnlAlterar.add(pnlAlterar2);
        
        /**
         * Painel final: painel com tabelas no lado esquerdo e painel de escolha
         * e alteração completo à direita.
         */
        pnlPrincipal = new JPanel(new BorderLayout());
        pnlPrincipal.add(pnlSistema.painelTabelas(), BorderLayout.WEST);
        pnlPrincipal.add(pnlAlterar, BorderLayout.EAST);
        
        return pnlPrincipal;
    }
    
    @Override
    public void confirmar() {
        sistema = new Sistema(tfdSisNome.getText(), txaSisDescricao.getText());
        sistema.setIdBD((int) pnlSistema.tab.getValueAt(
                    pnlSistema.tab.getSelectedRow(), 0));
        sistema.sqlAlterar();
    }
    
    @Override
    public void limparTexto() {
        tfdSisNome.setText("");
        txaSisDescricao.setText("");
    }

    private class Excluir implements ActionListener {

        public Excluir() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Se um sistema estiver selecionado e permitida a edição
            if(pnlSistema.tab.getSelectedRowCount()== 1) {
                JButton btnOk = new JButton("Excluir");
                JButton btnCan = new JButton("Cancelar");
                
                // Cria e mostra diálogo de confirmação com mensagem e botões definidos
                DialogoConfirma d = new DialogoConfirma(
                            "Tem certeza que deseja excluir este sistema "
                            + "Esta ação não pode ser desfeita.");
                
                btnOk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Cria objeto 
                        Sistema s = new Sistema((int) pnlSistema.tab.getValueAt(
                                    pnlSistema.tab.getSelectedRow(), 0));
                        
                        // Exclui a equipe e desvincula os interventores
                        s.sqlExcluir();
                        
                        // Dispóe o diálogo
                        d.dispose();
                        
                        habilitarEdicao(false);
                        pnlSistema.reiniciarTabela();
                        pnlSistema.atualizarAparenciaDaTabela();
                        pnlSistema.tab.clearSelection();
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
    
    // -------------------------------------------------------------------------
    // Classes.
    // -------------------------------------------------------------------------
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionEscolher
     */
    private class Escolher extends Janela.ActionEscolher {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(pnlSistema.tab.getSelectedRowCount() == 1) {
                tfdSisNome.setText(String.valueOf(
                    pnlSistema.tab.getValueAt(
                        pnlSistema.tab.getSelectedRow(), 1)));
                
                String descricao = "";
                if(pnlSistema.tab.getValueAt(
                        pnlSistema.tab.getSelectedRow(), 2) != null) {
                
                    descricao = String.valueOf(
                        pnlSistema.tab.getValueAt(
                            pnlSistema.tab.getSelectedRow(), 2));
                }
                
                txaSisDescricao.setText(descricao);
                
                pnlSistema.habilitarTabela(false);
            }
        }
    }
    
    /**
     * Atualiza o texto no JTextField e JTextArea com as informações vindas do
     * sistema selecionado na tabela.
     */
    public class ItemEventSistema implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
//                try {
//                    tfdSisNome.setText(LOCAL);
//                    pnlUnidade.buscarUnidade(cbxSistema.getSelectedIndex());
//                } catch(SQLException ex) { ex.printStackTrace();}
            }
        }
    }
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionSalvar
     */
    private class Salvar extends Janela.ActionSalvar {
        @Override
        public void actionPerformed(ActionEvent e) {
            String mensagemErro = "";
            if(tfdSisNome.getText().isEmpty()) {
                mensagemErro = "Informe nome para o sistema.";                
            }

            /* Se não houver erro, executa a operação */
            if(mensagemErro.isEmpty()) {
                confirmar();
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
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionVoltar
     */
    private class Voltar extends Janela.ActionVoltar {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(pnlSistema.tab.getSelectedRowCount() == 1) {
                pnlSistema.tab.clearSelection();
                pnlSistema.habilitarTabela(true);
                limparTexto();
            }
        }
    }
}
