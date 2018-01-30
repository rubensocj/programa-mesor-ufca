
import java.util.Arrays;
import r.CamadaR;
import sql.Consulta;
import r.SerieTemporal;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rubens Oliveira da Cunha Júnior
 */
public class TesteTempoEntre {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SerieTemporal st = new SerieTemporal("Subunidade", 15);
        System.out.println(Arrays.toString(st.getStringArrayTempos()));
//        CamadaR.rGetSeriesTemporais(st);
    }

}
