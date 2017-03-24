package menu.adicionar;

import equipamento.Sistema;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import menu.JanelaAdicionarAlterar;

import java.io.File;
import java.io.IOException;

import javax.swing.border.BevelBorder;

/**
 * JanelaAdicionarEquipamento.Java
 * 
 * @version 1.0 31/01/2017
 * @author Rubens Jr
 */
public class JanelaAdicionarSistema extends JanelaAdicionarAlterar {
    
    public JPanel pnSisGeral;    
    public JLabel lblSisNome, lblSisDescricao;
    public JTextField tfdSisNome;
    public JTextArea txaSisDescricao;
    public Sistema sistema;
    
    /**
     * Construtor.
     */
    public JanelaAdicionarSistema() {}
    
    // -------------------------------------------------------------------------
    // Métodos sobrepostos.
    // -------------------------------------------------------------------------
    
    @Override
    public void criarBotoesOpcoes() {
        btn1 = new JButton("Confirmar");
        btn2 = new JButton("Cancelar");
        btn3 = new JButton("Ajuda");
        options = new Object[] {this.btn1, this.btn2, this.btn3};
        
        btn1.addActionListener(new Salvar());
        
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("opção CANCELAR selecionada");
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
    public JPanel montarPainelPrincipal() {
        /* Cria os botões antes de montar o painel Principal */
        criarBotoesOpcoes();
        
        pnlPrincipal = new JPanel(new FlowLayout());
        pnlPrincipal.add(painelGeral());
        
        return pnlPrincipal;
    }
    
    @Override
    public void confirmar() {
        sistema = new Sistema(tfdSisNome.getText(), txaSisDescricao.getText());
        sistema.sqlInserir();
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /**
     * Monta a área para inserção de informações gerais.
     * 
     * @return JPanel.
     */
    public JPanel painelGeral() {
        lblSisNome = new JLabel("Nome: *");
        lblSisDescricao = new JLabel("Descrição: ");
        
        tfdSisNome = new JTextField(25);
        txaSisDescricao = new JTextArea(5, 25);
        txaSisDescricao.setBorder(
                    BorderFactory.createSoftBevelBorder(BevelBorder.LOWERED));
        txaSisDescricao.setLineWrap(true);
        
        // Adiciona os componentes ao JPanel.
        JPanel pnlSistema1 = new JPanel(new BorderLayout(0,3));
        pnlSistema1.add(lblSisNome, BorderLayout.NORTH);
        pnlSistema1.add(lblSisDescricao, BorderLayout.CENTER);
        
        JPanel pnlSistema2 = new JPanel(new BorderLayout(0,3));
        pnlSistema2.add(pnlSistema1, BorderLayout.NORTH);
        
        JPanel pnlSistema3 = new JPanel(new BorderLayout(0,3));
        pnlSistema3.add(tfdSisNome, BorderLayout.NORTH);
        pnlSistema3.add(txaSisDescricao, BorderLayout.CENTER);
        
        JPanel pnlSistema4 = new JPanel(new GridLayout(0,1));
        pnlSistema4.add(pnlSistema2);
        
        JPanel pnlSistema5 = new JPanel(new GridLayout(0,1));
        pnlSistema5.add(pnlSistema3);
        
        pnSisGeral = new JPanel(new BorderLayout(3,0));
        pnSisGeral.add(pnlSistema4, BorderLayout.WEST);
        pnSisGeral.add(pnlSistema5, BorderLayout.EAST);
        
        pnSisGeral.setOpaque(true);
        return pnSisGeral;
    }
    
    // -------------------------------------------------------------------------
    // Classes.
    // -------------------------------------------------------------------------
    
    /** 
     * {@inheritDoc}
     * @see menu.JanelaAdicionarAlterar.ActionSalvar
     */
    private class Salvar extends JanelaAdicionarAlterar.ActionSalvar {
        @Override
        public void actionPerformed(ActionEvent e) {
            String mensagemErro = "";
            if(tfdSisNome.getText().isEmpty()) {
                mensagemErro = "Informe nome para o sistema.";                
            }

            /* Se não houver erro, executa a operação */
            if(mensagemErro.isEmpty()) {
                confirmar();
                dialog.dispose();
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
}
