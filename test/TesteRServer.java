/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.*;
import org.rosuda.JRI.*;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author Rubens Oliveira da Cunha Júnior
 */
public class TesteRServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        RConnection connection = null;
//        RVector v = new RVector();
//        v.add(1);
//        v.add(2);
//        v.add(3);
//        v.add(4);
//        System.loadLibrary("jri");
//        String vetor = "c(1,2,3)";
//        Rengine eng = new Rengine();
//        eng.eval("meanVal = var(" + vetor + ")");
//        double var = eng.eval("meanVal").asDouble();
//        System.out.println("* JRI: " + var);
        
        try {
            /* Create a connection to Rserve instance running
             * on default port 6311
             */
            connection = new RConnection();
            
            String vector = "c(1,2,3,4)";
            connection.eval("meanVal = var(" + vector + ")");
            double mean = connection.eval("meanVal").asDouble();
            System.out.println("The mean of given vector is = " + mean);
         } catch (RserveException | REXPMismatchException e) {
             e.printStackTrace();
         }finally{
             connection.close();
         }
    }
}
