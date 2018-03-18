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

import mesor.menu.adicionar.JanelaAdicionarSistema;
import mesor.menu.painel.taxonomia.PainelSistema;

/**
 * JanelaAdicionarEquipamento.Java
 * 
 * @version 1.0 31/01/2017
 * @author Rubens Jr
 */
public class JanelaAlterarSistema extends JanelaAdicionarSistema {
    
    private final PainelSistema pnlSistema;
    
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
                            LOCAL + "\\ajuda\\janelaAdicionarEquipamento.html");
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
        sistema.setIdBD((int) pnlSistema.tabSistema.getValueAt(
                    pnlSistema.tabSistema.getSelectedRow(), 0));
        sistema.sqlAlterar();
    }
    
    @Override
    public void limparTexto() {
        tfdSisNome.setText("");
        txaSisDescricao.setText("");
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
            if(pnlSistema.tabSistema.getSelectedRowCount() == 1) {
                tfdSisNome.setText(String.valueOf(
                    pnlSistema.tabSistema.getValueAt(
                        pnlSistema.tabSistema.getSelectedRow(), 1)));
                
                String descricao = "";
                if(pnlSistema.tabSistema.getValueAt(
                        pnlSistema.tabSistema.getSelectedRow(), 2) != null) {
                
                    descricao = String.valueOf(
                        pnlSistema.tabSistema.getValueAt(
                            pnlSistema.tabSistema.getSelectedRow(), 2));
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
            if(pnlSistema.tabSistema.getSelectedRowCount() == 1) {
                pnlSistema.tabSistema.clearSelection();
                pnlSistema.habilitarTabela(true);
                limparTexto();
            }
        }
    }
}
