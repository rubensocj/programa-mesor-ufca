/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package r;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import java.io.File;
import java.io.IOException;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * TabelaParametrosEICs.java
 *
 * @version 1.0 21/10/2017
 * @author Rubens Júnior
 */
public class TabelaParametrosEICs extends JTable {
    
    private static JTable tabParametros;
    private static DefaultTableModel modelo;
    
    private static JScrollPane scrTabela;
    
    private static Object[][] stringArray;
    private static Object[][] corpoTabela;
    private static Object[] cabecalho;
    
    private static Path caminho;
    private static File file;
    private static List<String> list;
    
    public TabelaParametrosEICs() {
        try {
            // Path gerado concatenando o CamadaR.R_PATH com o nome do arquivo
//            setPath(CamadaR.R_PATH.concat("\\parametersAndICs_table.txt"));
            setPath(("parametersAndICs_table.txt"));
            
            setModel(criarModeloTabela());
            
            // Desabilita a seleção de linhas
            // Desabilita a reorganização das colunas
            getTableHeader().setReorderingAllowed(false);
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            setRowSelectionAllowed(false);
            setCellSelectionEnabled(false);

            // CellRender: alinhamento centralizado do texto das colunas
            DefaultTableCellRenderer render = new DefaultTableCellRenderer();
            render.setHorizontalAlignment(JLabel.CENTER);
            getColumnModel().getColumn(1).setCellRenderer(render);
            getColumnModel().getColumn(2).setCellRenderer(render);
            getColumnModel().getColumn(3).setCellRenderer(render);
            getColumnModel().getColumn(4).setCellRenderer(render);
            getColumnModel().getColumn(5).setCellRenderer(render);
            getColumnModel().getColumn(6).setCellRenderer(render);
            getColumnModel().getColumn(7).setCellRenderer(render);
            getColumnModel().getColumn(8).setCellRenderer(render);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /**
     * Define o caminho do arquivo.
     * 
     * @param caminho 
     */
    private static void setPath(String path) throws IOException {
        caminho = Paths.get(path);
        
        setLista();
        toStringArray();
    }
    
    /**
     * Define a lista de string deste objeto na formatação passada como
     * parâmetro.
     * 
     * @return Uma lista de string.
     * @throws IOException 
     */
    private static void setLista() throws IOException {
        list = Files.readAllLines(caminho, Charset.defaultCharset());
    }
    
    /**
     * Monta um array bidimensional com os valores importados do csv.
     * 
     * @throws IOException 
     */
    private static void toStringArray() throws IOException {
        stringArray = new String[list.size()][];
        int a = 0;
        for(String l:list) {
            stringArray[a] = l.split(",");
            a++;
        }
    }
    
    /**
     * Define o modelo da tabela e cria a JTable com o conteúdo do txt
     */
    private static DefaultTableModel criarModeloTabela() {
        cabecalho = stringArray[0];
        corpoTabela = new Object[][] {stringArray[1], stringArray[2],
                        stringArray[3], stringArray[4], stringArray[5]};
        
        // TableModel define as células não editáveis
        modelo = new DefaultTableModel(corpoTabela, cabecalho) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }     
        };
//        tabParametros = new JTable(modelo);
//        
//        // Desabilita a seleção de linhas
//        // Desabilita a reorganização das colunas
//        tabParametros.getTableHeader().setReorderingAllowed(false);
//        tabParametros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        tabParametros.setRowSelectionAllowed(false);
//        tabParametros.setCellSelectionEnabled(false);
//        
//        // CellRender: alinhamento centralizado do texto das colunas
//        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
//        render.setHorizontalAlignment(JLabel.CENTER);
//        tabParametros.getColumnModel().getColumn(1).setCellRenderer(render);
//        tabParametros.getColumnModel().getColumn(2).setCellRenderer(render);
//        tabParametros.getColumnModel().getColumn(3).setCellRenderer(render);
//        tabParametros.getColumnModel().getColumn(4).setCellRenderer(render);
//        tabParametros.getColumnModel().getColumn(5).setCellRenderer(render);
//        tabParametros.getColumnModel().getColumn(6).setCellRenderer(render);
//        tabParametros.getColumnModel().getColumn(7).setCellRenderer(render);
//        tabParametros.getColumnModel().getColumn(8).setCellRenderer(render);
//        
        return modelo;
    }
    
    /**
     * Cria a JTable com os dados importados do txt
     * 
     * @return Uma JTable.
     */
    public static JTable getTabelaICsEParameters() {
        try {
            // Path gerado concatenando o CamadaR.R_PATH com o nome do arquivo
//            setPath(CamadaR.R_PATH.concat("\\parametersAndICs_table.txt"));
            setPath(("parametersAndICs_table.txt"));
            
            setLista();
            toStringArray();
            criarModeloTabela();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return tabParametros;
    }
    
    /**
     * Cria a JTable importada do arquivo txt e a adiciona a um JScrollPane
     * 
     * @param largura
     * @param altura
     * @return Um JScrollPane com a tabela dos parâmetros e ICs.
     */
    public static JScrollPane getPainelICsEParameters(int largura, int altura) {
        getTabelaICsEParameters();
        
        scrTabela = new JScrollPane(tabParametros, 
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrTabela.setPreferredSize(new Dimension(largura, altura));
        
        return scrTabela;
    }
}