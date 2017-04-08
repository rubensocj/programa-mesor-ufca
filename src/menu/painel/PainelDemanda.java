package menu.painel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.sql.SQLException;

import conexaoSql.ModeloTabela;
import intervencao.Demanda;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * PainelDemanda.java
 * 
 * @version 1.0 2/2/2017
 * @author Rubens Jr
 */
public class PainelDemanda {
    
    public JTable tabDemanda;
    
    private final DefaultTableCellRenderer render;
    private ModeloTabela modelo;
    
    private final JPanel pnlDemFinal;
    private JPanel pnlSelecionadosExcluir = new JPanel(new BorderLayout());
    
    private final JScrollPane dPane;
    
    public Demanda demanda = new Demanda();
    
    private final JButton btnExcluir = new JButton("Excluir item");
    private final JPanel pnlDem;
    
    /**
     * Construtor.
     */
    public PainelDemanda() {
        
        /**
         * Renderizador de célula da tabela.
         */
        render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        
        /**
         * Cria a tabela com uma consulta em SQL.
         */
        try {
            tabDemanda = new JTable(new ModeloTabela("SELECT * FROM demanda"));
            atualizarAparenciaDaTabela();            
        } catch (SQLException ex) { ex.getErrorCode();}
        
        // Adiciona os MOUSELISTENER às painelTabelas.
        tabDemanda.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(tabDemanda.getSelectedRowCount() == 1 && 
                            tabDemanda.isEnabled()) {
                    
                    demanda.setIdBD((int) tabDemanda.getValueAt(
                                tabDemanda.getSelectedRow(), 0));
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
        dPane = new JScrollPane(tabDemanda,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabDemanda.setFillsViewportHeight(true);
        
        pnlDem = new JPanel(new FlowLayout());
        pnlDem.add(dPane);
        pnlDem.setOpaque(true);        
        
        // PainelDemanda final
        pnlDemFinal = new JPanel(new BorderLayout());
        pnlDemFinal.add(pnlDem, BorderLayout.CENTER);
        pnlDemFinal.add(pnlSelecionadosExcluir, BorderLayout.PAGE_END);
        pnlDemFinal.setOpaque(true);     
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /**
     */
    public void setEditavel() {
        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tabDemanda.getSelectedRowCount() == 1) {
                    try {
                        demanda.sqlExcluir();
                        reiniciarTabela();
                        atualizarAparenciaDaTabela();
                    } catch(SQLException ex) {ex.printStackTrace();}
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
        return pnlDemFinal;
    }
    
    /**
     * Reinicia a tabela através de uma consulta SQL {@code SELECT * FROM
     * demanda}.
     * 
     * @throws SQLException 
     */
    public void reiniciarTabela() throws SQLException {
        modelo = (ModeloTabela) tabDemanda.getModel();
        modelo.setQuery("SELECT * FROM demanda");
    }
    
    /**
     * Busca por demandas nos equipamentos selecionados, através de uma cosulta
     * SQL.
     * 
     * @param idUnidade
     * @param idSubunidade
     * @param idComponente
     * @param idParte
     * @throws SQLException 
     */
    public void buscarDemanda(Object idUnidade, Object idSubunidade,
                Object idComponente, Object idParte) throws SQLException {
        
        modelo = (ModeloTabela) tabDemanda.getModel();
        if(idSubunidade == null) {
            modelo.setQuery(
                "SELECT demanda.* FROM " +
                    "demanda, unidade " +
                "WHERE demanda.id_unidade = " + String.valueOf(idUnidade) + 
                " GROUP BY demanda.id;");
        }
        else if(idComponente == null) {
            modelo.setQuery(
                "SELECT demanda.* FROM " +
                    "demanda, unidade, subunidade " +
                "WHERE demanda.id_unidade = " + String.valueOf(idUnidade) +
                " AND demanda.id_subunidade = " + String.valueOf(idSubunidade) + 
                " GROUP BY demanda.id;");
        }
        else if(idParte == null) {
            modelo.setQuery(
                "SELECT demanda.* FROM " +
                    "demanda, unidade, subunidade, componente " +
                "WHERE demanda.id_unidade = " + String.valueOf(idUnidade) +
                " AND demanda.id_subunidade = " + String.valueOf(idSubunidade) +
                " AND demanda.id_componente = " + String.valueOf(idComponente) + 
                " GROUP BY demanda.id;");
        } 
        else {
            modelo.setQuery(
                "SELECT demanda.* FROM " +
                    "demanda, unidade, subunidade, componente, parte " +
                "WHERE demanda.id_unidade = " + String.valueOf(idUnidade) +
                " AND demanda.id_subunidade = " + String.valueOf(idSubunidade) +
                " AND demanda.id_componente = " + String.valueOf(idComponente) +
                " AND demanda.id_parte = " + String.valueOf(idParte) +
                " GROUP BY demanda.id;");
        }
    }
    
    /**
     * Atualiza parâmetros da tabela como renderizador das células e largura das
     * colunas.
     */
    public void atualizarAparenciaDaTabela() {

        tabDemanda.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabDemanda.getTableHeader().setReorderingAllowed(false);
        tabDemanda.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tabDemanda.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tabDemanda.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabDemanda.getColumnModel().getColumn(1).setPreferredWidth(160);
        tabDemanda.getColumnModel().getColumn(2).setPreferredWidth(60);
        tabDemanda.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabDemanda.getColumnModel().getColumn(4).setPreferredWidth(60);
        tabDemanda.getColumnModel().getColumn(5).setPreferredWidth(130);
        tabDemanda.getColumnModel().getColumn(6).setPreferredWidth(70);
        tabDemanda.getColumnModel().getColumn(7).setPreferredWidth(100);
        tabDemanda.getColumnModel().getColumn(8).setPreferredWidth(100);
        tabDemanda.getColumnModel().getColumn(9).setPreferredWidth(60);

        tabDemanda.getColumnModel().getColumn(0).setCellRenderer(render);
        tabDemanda.getColumnModel().getColumn(1).setCellRenderer(render);
        tabDemanda.getColumnModel().getColumn(2).setCellRenderer(render);
        tabDemanda.getColumnModel().getColumn(4).setCellRenderer(render);
        tabDemanda.getColumnModel().getColumn(6).setCellRenderer(render);
        tabDemanda.getColumnModel().getColumn(7).setCellRenderer(render);
        tabDemanda.getColumnModel().getColumn(8).setCellRenderer(render);
        tabDemanda.getColumnModel().getColumn(9).setCellRenderer(render);
    }
    
    /**
     * Habilita ou desabilita a tabela. Permite a seleção de linhas e altera a
     * cor da fonte dependendo do seu estado.
     * 
     * @param habilita - booleano
     */
    public void habilitarTabela(boolean habilita) {
        tabDemanda.setEnabled(habilita);
        
        if(habilita == true) {
            tabDemanda.setForeground(Color.BLACK);
        } else if(habilita == false) {
            tabDemanda.setForeground(Color.LIGHT_GRAY);
        }
    }
    
    /**
     * Define o tamanho da tabela, alterando o tamanho preferencial do
     * JScrollPane que a recebe.
     * 
     * @param dim - objeto da classe Dimension
     */
    public void tamanhoDaTabela(Dimension dim) {
        dPane.setPreferredSize(dim);
    }
}