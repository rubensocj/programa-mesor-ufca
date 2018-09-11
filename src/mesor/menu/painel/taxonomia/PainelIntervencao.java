package mesor.menu.painel.taxonomia;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.sql.SQLException;

import mesor.sql.ModeloTabela;
import mesor.intervencao.Intervencao;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import mesor.menu.DialogoAviso;

/**
 * PainelIntervencao.java
 * 
 * @version 1.0 5/2/2017
 * @author Rubens Jr
 */
public class PainelIntervencao {
    
    public JTable tabIntervencao;
    
    private final DefaultTableCellRenderer render;
    private ModeloTabela modelo;
        
    public Intervencao intervencao = new Intervencao();
    
    private final JPanel pnlInt;
    private final JPanel pnlIntFinal;
    private JPanel pnlSelecionadosExcluir = new JPanel(new BorderLayout());
    
    private final JScrollPane iPane;
    private final JButton btnExcluir = new JButton("Excluir item");
    private String query;
    
    /**
     * Construtor.
     */
    public PainelIntervencao() {
        
        /**
         * Renderizador de célula da tabela.
         */
        render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        
        tabIntervencao = new JTable(
                    new ModeloTabela("SELECT * FROM intervencao"));
        atualizarAparenciaDaTabela();
        
        // Adiciona os MOUSELISTENER às painelTabelas.
        tabIntervencao.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(tabIntervencao.getSelectedRowCount() == 1 && 
                            tabIntervencao.isEnabled()) {
                    
                    intervencao.setIdBD((int) tabIntervencao.getValueAt(
                                tabIntervencao.getSelectedRow(), 0));
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}});
        
        /**
         * Cria o JScrollPane da tabela.
         */
        iPane = new JScrollPane(tabIntervencao,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabIntervencao.setFillsViewportHeight(true);
        
        pnlInt = new JPanel(new FlowLayout());
        pnlInt.add(iPane);
        pnlInt.setOpaque(true);
        
        // PainelIntervenção final
        pnlIntFinal = new JPanel(new BorderLayout());
        pnlIntFinal.add(pnlInt, BorderLayout.CENTER);
        pnlIntFinal.add(pnlSelecionadosExcluir, BorderLayout.PAGE_END);
        pnlIntFinal.setOpaque(true);  
    }
    
    public PainelIntervencao(int nivel, String id) {
        
        /** Define o nivel taxonomico da consulta SQL e o id do item */
        setNivelTaxonomico(nivel, id);
        
        /** Renderizador de célula da tabela */
        render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        
        tabIntervencao = new JTable(new ModeloTabela(query));
        atualizarAparenciaDaTabela();
        
        /**
         * Cria o JScrollPane da tabela.
         */
        iPane = new JScrollPane(tabIntervencao,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabIntervencao.setFillsViewportHeight(true);
        
        pnlInt = new JPanel(new FlowLayout());
        pnlInt.add(iPane);
        pnlInt.setOpaque(true);
        
        // PainelIntervenção final
        pnlIntFinal = new JPanel(new BorderLayout());
        pnlIntFinal.add(pnlInt, BorderLayout.CENTER);
        pnlIntFinal.add(pnlSelecionadosExcluir, BorderLayout.PAGE_END);
        pnlIntFinal.setOpaque(true);
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /**
     * Define o nível taxonômico da tabela do banco de dados. Determina sobre 
     * qual tabela a consulta por demandas será realizada: 
     * sistema, unidade, subunidade, etc.
     * 
     * @param nivel
     * @param id 
     */
    private void setNivelTaxonomico(int nivel, String id) {
        String n = null;
        switch(nivel) {
            case 2: n = "id_unidade"; break;
            case 3: n = "id_subunidade"; break;
            case 4: n = "id_componente"; break;
            case 5: n = "id_parte"; break;
        }
        
        setQuery("SELECT * FROM intervencao WHERE " + n + " = " + id);
    }
    
    /**
     * Define a consulta sql a ser realizada
     * @param q 
     */
    private void setQuery(String q) {
        query = q;
    }
    
    /**
     * Define se este painel tem a opção de excluir intervenções
     */
    public void setEditavel() {
        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tabIntervencao.getSelectedRowCount() == 1) {
                    try {
                        intervencao.sqlExcluir();
                        reiniciarTabela();
                        atualizarAparenciaDaTabela();
                    } catch(SQLException ex) {
                        DialogoAviso.show(ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    // Bip do mouse ao clicar no botão
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        
        // Adiciona o botão "Excluir" ao painel de selecionados, no fim do
        // painel principal.
        JPanel pnlBtnExcluir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtnExcluir.add(btnExcluir);
        pnlSelecionadosExcluir.add(pnlBtnExcluir, BorderLayout.EAST);
    }
    
    /**
     * 
     * @return Um JPanel com a tabela.
     */
    public JPanel painelTabelas() {
        return pnlIntFinal;
    }
    
    /**
     * 
     * @return Um JPanel com a tabela.
     */
    public JTable getTabela() {
        return tabIntervencao;
    }
    
    /**
     * Reinicia a tabela através de uma consulta SQL {@code SELECT * FROM
     * demanda}.
     * 
     * @throws SQLException 
     */
    public void reiniciarTabela() throws SQLException {
        modelo = (ModeloTabela) tabIntervencao.getModel();
        modelo.setQuery("SELECT * FROM intervencao");
    }
    
    /**
     * Atualiza parâmetros da tabela como renderizador das células e largura das
     * colunas.
     */
    public void atualizarAparenciaDaTabela() {

        tabIntervencao.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabIntervencao.getTableHeader().setReorderingAllowed(false);
        tabIntervencao.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tabIntervencao.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tabIntervencao.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabIntervencao.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabIntervencao.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabIntervencao.getColumnModel().getColumn(3).setPreferredWidth(160);
        tabIntervencao.getColumnModel().getColumn(4).setPreferredWidth(160);
        tabIntervencao.getColumnModel().getColumn(5).setPreferredWidth(70);
        tabIntervencao.getColumnModel().getColumn(6).setPreferredWidth(100);
        tabIntervencao.getColumnModel().getColumn(7).setPreferredWidth(100);
        tabIntervencao.getColumnModel().getColumn(8).setPreferredWidth(60);
        tabIntervencao.getColumnModel().getColumn(9).setPreferredWidth(100);

        tabIntervencao.getColumnModel().getColumn(0).setCellRenderer(render);
        tabIntervencao.getColumnModel().getColumn(3).setCellRenderer(render);
        tabIntervencao.getColumnModel().getColumn(5).setCellRenderer(render);
        tabIntervencao.getColumnModel().getColumn(6).setCellRenderer(render);
        tabIntervencao.getColumnModel().getColumn(7).setCellRenderer(render);
        tabIntervencao.getColumnModel().getColumn(8).setCellRenderer(render);
        tabIntervencao.getColumnModel().getColumn(9).setCellRenderer(render);
    }
    
    /**
     * Habilita ou desabilita a tabela. Permite a seleção de linhas e altera a
     * cor da fonte dependendo do seu estado.
     * 
     * @param habilita - booleano
     */
    public void habilitarTabela(boolean habilita) {
        tabIntervencao.setEnabled(habilita);
        //tabIntervencao.setRowSelectionAllowed(habilita);
        
        if(habilita == true) {
            tabIntervencao.setForeground(Color.BLACK);
        } else if(habilita == false) {
            tabIntervencao.setForeground(Color.LIGHT_GRAY);
        }
    }
    
    /**
     * Define o tamanho da tabela, alterando o tamanho preferencial do
     * JScrollPane que a recebe.
     * 
     * @param dim - objeto da classe Dimension
     */
    public void tamanhoDaTabela(Dimension dim) {
        iPane.setPreferredSize(dim);
    }
}