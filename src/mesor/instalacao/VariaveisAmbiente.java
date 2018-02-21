/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mesor.instalacao;

import java.util.Map;
import java.lang.ProcessBuilder;

/**
 *
 * @author Rubens Oliveira da Cunha Júnior
 */
public class VariaveisAmbiente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ProcessBuilder p = new ProcessBuilder("cmd.exe","cd\\","set");
        
//        Map<String, String> amb = System.getenv();
        Map<String, String> amb = p.environment();
        for(String nomeAmb : amb.keySet()) {
            System.out.format("%s=%s%n", nomeAmb, amb.get(nomeAmb));
        }
    }
}
