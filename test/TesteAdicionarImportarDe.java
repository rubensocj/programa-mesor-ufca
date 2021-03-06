/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Rubens Oliveira da Cunha Júnior
 */
public class TesteAdicionarImportarDe {
    
    private static Path caminho;
    private static Object[][] stringArray;
    private static Object[][] corpoTabela;
    private static JTable tabela;
    private static DefaultTableModel modelo;
    private static Object[] cabecalho;
    public static enum Cabecalho {SISTEMA, UNIDADE, DEMANDA, INTERVENCAO, PRIMEIRA_LINHA}
    static File file;
    static Files files;
    static FileSystem fileSystem;
    static List<String> list;
    static BasicFileAttributes info;
    
    /**
     * Construtor
     */
    public TesteAdicionarImportarDe() {}
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /* Métodos setters */
    /**
     * Define o caminho do arquivo.
     * 
     * @param caminho 
     */
    private static void setPath(String caminho) {
        TesteAdicionarImportarDe.caminho = Paths.get(caminho);
    }
    
    /**
     * Define a lista de string deste objeto na formatação passada como
     * parâmetro.
     * 
     * @param charset
     * @return Uma lista de string.
     * @throws IOException 
     */
    public List<String> setLista(Charset charset) 
                throws IOException {
        TesteAdicionarImportarDe.list = Files.readAllLines(TesteAdicionarImportarDe.caminho, charset);
        return list;
    }
    
    /* Métodos getters */
    public Path getPath(){
        return caminho;
    }
    
    /**
     * Monta um array bidimensional com os valores importados do csv.
     * 
     * @return Um array bidimensional.
     * @throws IOException 
     */
    private static void toStringArray() throws IOException {
        TesteAdicionarImportarDe.stringArray = new String[TesteAdicionarImportarDe.list.size()][];
        int a = 0;
        for(String l:TesteAdicionarImportarDe.list) {
            stringArray[a] = l.split(",");
            a++;
        }
        
//        return stringArray;
    }
    
    /**
     * Monta a JTable com os dados do arquivo .csv.
     * 
     * @param cabecalho
     * @return Uma tabela.
     */
    public JTable toTable(TesteAdicionarImportarDe.Cabecalho cabecalho) {
        switch(cabecalho) {
            case SISTEMA: TesteAdicionarImportarDe.cabecalho = new Object[] {"Descrição"};
                break;
                
            case UNIDADE: TesteAdicionarImportarDe.cabecalho = new Object[] {
                "Classe","Tipo", "Fabricante", "Identificação", "Categoria",
                "Localização", "Data de aquisição", "Data de aquisição",
                "Modo operacional", "Data início de operação"};
                break;
                
            case PRIMEIRA_LINHA: TesteAdicionarImportarDe.cabecalho = stringArray[0];
                corpoTabela = new Object[][] {stringArray[1], stringArray[2],
                                stringArray[3], stringArray[4], stringArray[5]};
                break;
                
            default: break;
        }
        
        modelo = new DefaultTableModel(TesteAdicionarImportarDe.corpoTabela,
                    TesteAdicionarImportarDe.cabecalho);
        TesteAdicionarImportarDe.tabela = new JTable(modelo);
        
        return tabela;
    }
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        TesteAdicionarImportarDe ts = new TesteAdicionarImportarDe();
        TesteAdicionarImportarDe.setPath("C:\\Users\\Rubens Jr\\Documents\\NetBeansProjects\\Programa\\parametersAndICs_table.txt");
        ts.setLista(Charset.defaultCharset());
        
        
        System.out.println(TesteAdicionarImportarDe.fileSystem);
        System.out.println(TesteAdicionarImportarDe.caminho.getFileName());
        
//        ts.list.add("g, h, i");
//        Files.write(ts.caminho, ts.list, Charset.defaultCharset());

//        System.out.println(ts.info.creationTime());
//        System.out.println(ts.info.lastAccessTime());
//        System.out.println(ts.info.lastModifiedTime());
//        System.out.println(ts.info.isDirectory());
//        System.out.println(ts.info.size());

//        String[][] a = new String[][] {new String[] {"a", "b"}, new String[] {"c", "d"}};
//        String[][] a = new String[][]{};
//        a[0] = new String[] {"a", "b"};
//        a[1] = new String[] {"c", "d"};
//        System.out.println(Arrays.toString(a[0]));
//        try {
////            for(String l:ts.list) System.out.println(l);
////            System.out.println(ts.list.get(1));
////            System.out.println("#########");
//            System.out.println(Arrays.deepToString(ts.toStringArray()));
//        } catch (Exception ex) {}
//        

        toStringArray();
        
        JFrame frm = new JFrame("Teste tabela importar");
        JScrollPane scr = new JScrollPane(ts.toTable(Cabecalho.PRIMEIRA_LINHA),
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scr.setPreferredSize(new Dimension(500,500));
        JPanel pnl = new JPanel(new FlowLayout());
        pnl.add(scr);
                    
        frm.add(pnl);
        frm.pack();
        frm.setVisible(true);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }

}
