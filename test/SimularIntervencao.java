
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rubens Oliveira da Cunha Júnior
 */
public class SimularIntervencao {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Charset charset = Charset.forName("utf-8");
        Path file = Paths.get("C:\\Users\\Rubens Jr\\Desktop\\po.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
            
            int i;
            String s = "";
            String preventiva = "Preventiva", corretiva = "Corretiva", categoria = "";
            
            int j, k = 1;
            for(j = 0; j <= 3; j++) {
                if(j == 0) { // Janeiro
                    i = 1;
                    while(i <= 31) {
                        double r = Math.random();
                        if(r <= 0.5) {
                            categoria = preventiva;
                        } else {
                            categoria = corretiva;
                        }

                        int mes, dia, hora, min;

                        if(i/2 == 0) { min = 15; } else { min = 40;}

                        if(r <= 0.3) {
                            hora = (int) (10*Math.random());
                        } else {
                            hora = (int) (10 + 10*Math.random());
                        }

                        Timestamp inicio = new Timestamp(117, j, i, hora, min, 0, 0);

                        s = String.valueOf(k) + "," + categoria +
                                    "," + "Modificação" + "," + 
                                    "" + inicio + "," + "" + inicio + "," +
                                    "" + String.valueOf(3) + "," +
                                    "" + String.valueOf(15) + "," +
                                    "NULL" + "," +
                                    "NULL" + "," +
                                    "NULL" + "";
                        
                        writer.write(s, 0, s.length());
                        writer.newLine();
                        i++;
                        k++;
                    }
                } else if(j == 1) { // Fevereiro
                    i = 1;
                    while(i <= 28) {
                        double r = Math.random();
                        if(r <= 0.5) {
                            categoria = preventiva;
                        } else {
                            categoria = corretiva;
                        }

                        int mes, dia, hora, min;

                        if(i/2 == 0) { min = 15; } else { min = 40;}

                        if(r <= 0.3) {
                            hora = (int) (10*Math.random());
                        } else {
                            hora = (int) (10 + 10*Math.random());
                        }

                        Timestamp inicio = new Timestamp(117, j, i, hora, min, 0, 0);

                        s = String.valueOf(k) + "," + categoria +
                                    "," + "Modificação" + "," + 
                                    "" + inicio + "," + "" + inicio + "," +
                                    "" + String.valueOf(3) + "," +
                                    "" + String.valueOf(15) + "," +
                                    "NULL" + "," +
                                    "NULL" + "," +
                                    "NULL" + "";
                        
                        writer.write(s, 0, s.length());
                        writer.newLine();
                        i++;
                        k++;
                    }
                } else if(j == 2) {
                    i = 1;
                    while(i <= 31) {
                        double r = Math.random();
                        if(r <= 0.5) {
                            categoria = preventiva;
                        } else {
                            categoria = corretiva;
                        }

                        int mes, dia, hora, min;

                        if(i/2 == 0) { min = 15; } else { min = 40;}

                        if(r <= 0.3) {
                            hora = (int) (10*Math.random());
                        } else {
                            hora = (int) (10 + 10*Math.random());
                        }

                        Timestamp inicio = new Timestamp(117, j, i, hora, min, 0, 0);

                        s = String.valueOf(k) + "," + categoria +
                                    "," + "Modificação" + "," + 
                                    "" + inicio + "," + "" + inicio + "," +
                                    "" + String.valueOf(3) + "," +
                                    "" + String.valueOf(15) + "," +
                                    "NULL" + "," +
                                    "NULL" + "," +
                                    "NULL" + "";
                        
                        writer.write(s, 0, s.length());
                        writer.newLine();
                        i++;
                        k++;
                    }
                }
            }
        } catch (IOException x) {
        System.err.format("IOException: %s%n", x);
        }
    }
}
