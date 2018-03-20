/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mesor.menu.alterar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import mesor.menu.Janela;
import static mesor.menu.Janela.LOCAL;
import mesor.menu.adicionar.JanelaAdicionarInterventor;
import mesor.menu.painel.taxonomia.PainelInterventor;

/**
 * JanelaAlterarInterventor.java
 *
 * @version 1.0 20/03/2018
 * @author Rubens Júnior
 */
public class JanelaAlterarInterventor extends JanelaAdicionarInterventor {

    /**
     * Construtores.
     */
    public JanelaAlterarInterventor() {       
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
                } catch (IOException ex) { ex.printStackTrace();
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
        /* Painel com os botões de escolher e voltar */
        JPanel pnlBtnAlterar = new JPanel(new BorderLayout(0,10));
        pnlBtnAlterar.add(btnEscolher, BorderLayout.NORTH);
        pnlBtnAlterar.add(btnVoltar, BorderLayout.CENTER);
        
        JPanel pnlSalvarAlteracao = new JPanel(new FlowLayout(2));
        pnlSalvarAlteracao.add(btnSalvarAlteracao);
        
        /**
         * Painel de alteração completo, que agrupa o painel de alterações do
         * interventor e o painel com o botão que salva as alterações feitas.
         */
        JPanel pnlAlterarInterventor = new JPanel(new BorderLayout());
        pnlAlterarInterventor.add(painelExcluirInterventor(), BorderLayout.NORTH);
        pnlAlterarInterventor.add(pnlSalvarAlteracao, BorderLayout.CENTER);
        
        inicializarMapaComponentes();
        mapearComponentes();
        
        /* Desabilita a edição */
        habilitarEdicao(false);
        
        /**
         * Painel que agrupa o painel com os botões "Escolher" e "Voltar", e o
         * painel de alteração completo (com o botão).
         */
        pnlAlterar = new JPanel(new FlowLayout());
        pnlAlterar.add(pnlBtnAlterar);
        pnlAlterar.add(pnlAlterarInterventor);
        
        // Painel Final
        pnlPrincipal = new JPanel(new FlowLayout());
        pnlIntv = new PainelInterventor();
        pnlPrincipal.add(pnlIntv.painelTabelas());
        pnlPrincipal.add(pnlAlterar);
        return pnlPrincipal;
    }
    
    @Override
    public void mapearComponentes() {
        /*Adiciona os componentes ao mapa */
        mapaComponentes.put(0, lblNome);
        mapaComponentes.put(1, tfdNome);
        mapaComponentes.put(2, lblSexo);
        mapaComponentes.put(3, cbxSexo);
        mapaComponentes.put(4, lblNascimento);
        mapaComponentes.put(5, tfdNascimento);
        mapaComponentes.put(6, lblAdmissao);
        mapaComponentes.put(7, tfdAdmissao);
        mapaComponentes.put(8, lblCargo);
        mapaComponentes.put(9, tfdCargo);
        mapaComponentes.put(10, lblFormacao);
        mapaComponentes.put(11, tfdFormacao);
        mapaComponentes.put(12, lblEsp);
        mapaComponentes.put(13, tfdEsp);
        mapaComponentes.put(14, lblRemuneracao);
        mapaComponentes.put(15, tfdRemuneracao);
        mapaComponentes.put(16, lblEstadoCivil);
        mapaComponentes.put(17, cbxECivil);
        mapaComponentes.put(18, lblEndereco);
        mapaComponentes.put(19, tfdEndereco);
        mapaComponentes.put(20, lblCidade);
        mapaComponentes.put(21, tfdCidade);
        mapaComponentes.put(22, lblEstado);
        mapaComponentes.put(23, cbxEstado);
        mapaComponentes.put(24, lblContato);
        mapaComponentes.put(25, tfdContato);
    }
    
    @Override
    public void habilitarEdicao(boolean escolha) {
        if(escolha == true) {
            for(int i = 0; i <= 25; i++) {
                mapaComponentes.get(i).setEnabled(true); // Habilita.
            }
        } else {
            for(int i = 0; i <= 25; i++) {
                mapaComponentes.get(i).setEnabled(false); // Desabilita.
            }
        }
        
        edicaoPermitida = escolha;
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------

    private class Escolher extends Janela.ActionEscolher {

        public Escolher() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(pnlIntv.tabInterventor.getSelectedRowCount() != 0){
                habilitarEdicao(true);
                int r = pnlIntv.tabInterventor.getSelectedRow();
                
                tfdNome.setText(
                        String.valueOf(pnlIntv.tabInterventor.getValueAt(r, 1)));
                
                if(pnlIntv.tabInterventor.getValueAt(r, 2) != null) {
                    cbxSexo.setSelectedItem(String.valueOf(
                                pnlIntv.tabInterventor.getValueAt(r, 2)));
                }
                
                if(pnlIntv.tabInterventor.getValueAt(r, 3) == null) {
                    tfdNascimento.setText("");
                } else {
                    Date dataDate;
                    dataDate = (Date) pnlIntv.tabInterventor.getValueAt(r, 3);
                    
                    SimpleDateFormat formatoData;
                    formatoData = new SimpleDateFormat("dd/MM/yyyy");
                    String dataAq = formatoData.format(dataDate);
                    tfdNascimento.setText(dataAq);
                }
                
                if(pnlIntv.tabInterventor.getValueAt(r, 4) == null) {
                    tfdAdmissao.setText("");
                } else {
                    Date dataDate;
                    dataDate = (Date) pnlIntv.tabInterventor.getValueAt(r, 4);
                    
                    SimpleDateFormat formatoData;
                    formatoData = new SimpleDateFormat("dd/MM/yyyy");
                    String dataAq = formatoData.format(dataDate);
                    tfdAdmissao.setText(dataAq);
                }
                
                tfdCargo.setText(
                        String.valueOf(pnlIntv.tabInterventor.getValueAt(r, 5)));
                tfdFormacao.setText(
                        String.valueOf(pnlIntv.tabInterventor.getValueAt(r, 6)));
                tfdEsp.setText(
                        String.valueOf(pnlIntv.tabInterventor.getValueAt(r, 7)));
                tfdRemuneracao.setText(
                        String.valueOf(pnlIntv.tabInterventor.getValueAt(r, 8)));
                
                if(pnlIntv.tabInterventor.getValueAt(r, 9) != null) {
                    cbxECivil.setSelectedItem(String.valueOf(
                                pnlIntv.tabInterventor.getValueAt(r, 9)));
                }
                    
                tfdEndereco.setText(
                        String.valueOf(pnlIntv.tabInterventor.getValueAt(r, 10)));
                tfdCidade.setText(
                        String.valueOf(pnlIntv.tabInterventor.getValueAt(r, 11)));
                
                if(pnlIntv.tabInterventor.getValueAt(r, 12) != null) {
                    cbxEstado.setSelectedItem(String.valueOf(
                                pnlIntv.tabInterventor.getValueAt(r, 12)));
                    
                tfdContato.setText(
                        String.valueOf(pnlIntv.tabInterventor.getValueAt(r, 13)));
                }
            }
        }
    }

    private class Voltar extends Janela.ActionVoltar {

        public Voltar() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }

    private class Salvar extends Janela.ActionSalvar {

        public Salvar() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }
    
}