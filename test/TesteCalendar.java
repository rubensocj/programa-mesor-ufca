/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
/**
 *
 * @author Rubens Oliveira da Cunha Júnior
 */
public class TesteCalendar {

    /**
     * @param args the command line arguments
     */
          
    public static void main(String[] args) {
                
        // Array que recebe os dias
        int[][] dias = new int[6][7];
        
        // Dados iniciais
        int mes = 2;            // Mês do ano, 0 = Jan, 1 = Fev, ..., 11 = Dez
        int diaDoMes = 1;       // Dia do mês, primeiro = 1
        int semanaDoMes;        // Semana do mês, primeira = 1
                   
        // Inicializa o calendário
        Calendar calendario = new GregorianCalendar();
        // Seta o ano
        //calendario.set(Calendar.YEAR, 1900);
//        System.out.println(calendario.getDisplayNames(Calendar.MONTH, Calendar.SHORT_STANDALONE, Locale.getDefault()));
//        for(int i = 0; i <= 11; i++) {
//            calendario.set(Calendar.MONTH, i + 1);
//            System.out.println(calendario.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//        }
//        System.out.println("\n");
        System.out.println(calendario.getDisplayNames(Calendar.MONTH, Calendar.NARROW_FORMAT, Locale.getDefault()));
        int i = 0;
        String[] keys = new String[12];
//        while(i < 12) {
//            calendario.set(Calendar.MONTH, i);
//            calendario.getDisplayNames(
//                Calendar.MONTH, Calendar.SHORT_STANDALONE, Locale.getDefault()).keySet().toArray(keys);
//            i++;
//        }
        
        
        System.out.println(calendario.getDisplayNames(
                Calendar.MONTH, Calendar.SHORT_STANDALONE, Locale.getDefault()));
        
        // Seta os valores iniciais
        calendario.set(Calendar.MONTH, 0);
        System.out.println(calendario.getMaximum(Calendar.DAY_OF_MONTH));        
//        calendario.set(Calendar.DAY_OF_MONTH, diaDoMes); // 1º
//        System.out.println(calendario.getMaximum(Calendar.YEAR));
//        System.out.println(calendario.getMaximum(Calendar.YEAR));
        
        // Começa o loop na primeira semana
        semanaDoMes = calendario.get(Calendar.WEEK_OF_MONTH);

        // Enquanto a semana for igual a "semanaDoMes", o loop roda em todas
        // as semanas do mês, começando da primeira, 1
        while(semanaDoMes == calendario.get(Calendar.WEEK_OF_MONTH)) {
            
            // Pega o dia da semana referente ao dia od mês especificado
            int diaDaSemana = calendario.get(Calendar.DAY_OF_WEEK);

            // Adiciona o dia ao array
            dias[semanaDoMes - 1][diaDaSemana - 1] = calendario.get(Calendar.DAY_OF_MONTH);

            // Passa para o próximo dia do mês
            diaDoMes++;            
            calendario.set(Calendar.DAY_OF_MONTH, diaDoMes); // 1º

            // Seta a nova semana
            semanaDoMes = calendario.get(Calendar.WEEK_OF_MONTH);
            
            // Se trocar de mês, quebra o loop
            if(calendario.get(Calendar.MONTH) != mes) break;
        }

        // Printa o array
        System.out.println(Arrays.deepToString(dias));
        
        //printaCalendario(dias);
        
        //menu.Calendario testeCalendario = new menu.Calendario();
           
    }
    
    // Printa como calendario
    public static void printaCalendario(int[][] array) {
        String[] nomesDosDias = {"D", "S", "T", "Q", "Q", "S", "S"};
        
        System.out.println(Arrays.toString(nomesDosDias));
        for(int i = 0; i < array[0].length; i++) {
            System.out.println(Arrays.toString(array[i]));
        }            
    }
}
