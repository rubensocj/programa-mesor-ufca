
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import conexaoJavaSql.Consulta;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rubens Oliveira da Cunha Júnior
 */
public class Teste2 {
    //private final Consulta consulta;
    //private final int n;
    
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
    String dataString = "13/02/2017";
    Date dataDate1, dataDate2;
    Date dataAquisicao, dataInicioOperacao;   

    public Teste2() {
        try {
            dataDate1 = format2.parse(dataString);
            
        } catch (ParseException ex) {
            Logger.getLogger(Teste2.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(dataDate1.getTime());
        //consulta = new Consulta();
        //n = consulta.insertUnidade("Eletroeletrônico", "Condicionador de ar",
        //            "Split", "Consul", "012CZA55", "Sala 32", dataAquisicao,
        //            "Contínuo", dataInicioOperacao, 1200, 11);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Teste2 tst = new Teste2();
    }
}
