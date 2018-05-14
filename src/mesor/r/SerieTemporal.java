/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mesor.r;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import mesor.sql.Consulta;

/**
 * SerieTemporal.java
 *
 * @version 1.0 05/09/2017
 * @author Rubens Júnior
 */
public class SerieTemporal {

    // Séries intermediárias usadas nos cálculos. Resultados da consulta SQL
    private static Timestamp[] serieTimestampSQL = null;
    private static String[] serieTipoSQL = null;
    
    // Séries formatadas, resultantes dos cálculos.
    private static Object[] serieTempoEntreJava = null;
    private static String[] serieTipoJava = null;
    
    // Séries finais, formatadas para entrada no código R. Tipo String
    public static String[] serieTempoEntreR = null;
    public static String[] serieTipoR= null;
    
    private static Object[] dadosSQL = null;
    private static Object[] dadosTempoEntre = null;
    
    // Fatores para conversão de milisegundos para segundo, minuto, hora e dia
    private static double FATOR = 0;
    private static final double F1 = 0.001; // ms->s
    private static final double F2 = 0.0166666666666667; // s->min / min->h
    private static final double F3 = 0.0416666666666667; // h->dia
    public static enum Tempo {SEGUNDOS, MINUTOS, HORAS, DIAS}
    
    private static PreparedStatement obterSerieTemporal = null;
    private static ResultSet resultadoConsulta = null;
    
    public SerieTemporal(int[] n) {
        inicializarPreparedStatementSQL(n);
        executarConsultaSQL();
        calcularTempoEntre(Tempo.SEGUNDOS);
    }
    
    // -------------------------------------------------------------------------
    // Métodos privados.
    // -------------------------------------------------------------------------
    
    /**
     * Inicializa o preparedStatement com uma consulta referente ao nível 
     * taxonômico n informado.
     * 
     * @param n posição no array do nível taxônomico selecionado. n = 0 é o id do sistema
     */
    private static void inicializarPreparedStatementSQL(int[] n) {
        
        // Verifica qual nível taxonômico do item selecionado
        int i = 0;
        while(n[i] != 0) {
            i++;
        }
        
        String campo = "";
        switch(i - 1) {
            case 1 : campo = "id_unidade"; break;
            case 2 : campo = "id_subunidade"; break;
            case 3 : campo = "id_componente"; break;
            case 4 : campo = "id_parte"; break;
        }
        try {
            obterSerieTemporal = Consulta.connection.prepareStatement(
                    "SELECT intervencao.inicio, intervencao.categoria " +
                    "FROM intervencao, sistema WHERE intervencao." + campo + " = " + 
                    String.valueOf(n[i- 1]) + " AND sistema.id = " + 
                    String.valueOf(n[0]) + ";");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Executa a consulta SQL e obtém arrays de Timestamp (início) e 
     * String (categoria), gravando no Object[2] dados
     */
    private static void executarConsultaSQL() {        
        try {
            // Executa a consulta
            resultadoConsulta = obterSerieTemporal.executeQuery();
            
            // Posiciona o cursor na última linha do ResultSet
            // Inicializa os Arrays com o mesmo comprimento do ResultSet
            resultadoConsulta.last();
            serieTimestampSQL = new Timestamp[resultadoConsulta.getRow()];
            serieTipoSQL = new String[resultadoConsulta.getRow()];
            
            // Posiciona o cursor no início
            // Define as primeiras entradas dos Arrays antes de iniciar o laço
            resultadoConsulta.first();
            serieTimestampSQL[0] = resultadoConsulta.getTimestamp(1);
            serieTipoSQL[0] = resultadoConsulta.getString(2);

            // Laço while para preenchimento dos Arrays
            int i = 1;
            while(resultadoConsulta.next()) {
                serieTimestampSQL[i] = resultadoConsulta.getTimestamp(1);
                serieTipoSQL[i] = resultadoConsulta.getString(2);
                i++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        // Inicializa o Array bidimensional com as colunas incicio e categoria
        dadosSQL = new Object[] {serieTimestampSQL, serieTipoSQL};
    }
    
    /**
     * Cria o Array bidimensional final com os dados formatados, descartando-se
     * as primeiras entradas. Calcula o tempo entre na unidade de medida
     * passada como parâmetro do método.
     * 
     * @param t 
     */
    private static void calcularTempoEntre(Tempo t) {
        // Descarta a primeira observação da série
        serieTempoEntreJava = new Object[serieTimestampSQL.length - 1];
        serieTipoJava = new String[serieTipoSQL.length - 1];
        
        int i = 1;
        
        // Cálculos conforme a unidade de medida de tempo dada
        switch(t) {
            case SEGUNDOS:
                // Usa o F1 para converter para SEGUNDOS
                FATOR = F1;
                break;
            case MINUTOS:
                // Usa o F1*F2 para converter para MINUTOS
                FATOR = F1*F2;
                break;
            case HORAS:
                // Usa o F1*F2*F2 para converter para HORAS
                FATOR = F1*F2*F2;
                break;
            case DIAS:
                // Usa o F1*F2*F2*F3 para converter para DIAS
                FATOR = F1*F2*F2*F3;
                break;
            default: break;
        }
        
        while(i < serieTimestampSQL.length) {
            // Pega o tempo entre intervenções em milisegundos através
            // da subtração da OBSERVAÇÃO(i) - OBSERVAÇÃO(i - 1)
            // Usa o FATOR para converter para a unidade desejada
            // Guarda no Array serieTempoEntre
            serieTempoEntreJava[i - 1] = (Object) ((serieTimestampSQL[i].getTime() -
                        serieTimestampSQL[i - 1].getTime())*FATOR);

            // Guarda os tipos de intervenção descartando a 1ª entrada
            serieTipoJava[i - 1] = serieTipoSQL[i];

            i++;
        }

        // Laço alternativo para conferir os resultados da operação
//                double[] teste = new double[serieTimestamp.length - 1];
//                for(i = 1; i <serieTimestamp.length; i++) {
//                    Timestamp teste1 = serieTimestamp[i-1];
//                    Timestamp teste2 = serieTimestamp[i];
//                    long time1 = teste1.getTime();
//                    long time2 = teste2.getTime();
//                    long time3 = time2 - time1;
//                    double segundos = time3*F1;
//                    teste[i - 1] = segundos;
//                }
//                int j=0;
//                while(j<serieTempoEntre.length) {
//                    if(serieTempoEntre[j] != teste[j]) {
//                        System.out.println("*------- ERRO");
//                        break;
//                    }
//                    j++;
//                }

//                double minutos = segundos*F2;
//                double horas = minutos*F2;
//                double dias = horas*F3;

        // Monta o Array bidimensional final com os dados formatados
        dadosTempoEntre = new Object[] {serieTempoEntreJava, serieTipoJava};
        
        // Converte a série de tempos entre para Array de String
        serieTempoEntreR = new String[serieTempoEntreJava.length];
        i = 0;
        for(i = 0; i < serieTempoEntreJava.length; i++) {
            serieTempoEntreR[i] = String.valueOf(serieTempoEntreJava[i]);
        }
    }
    
    // -------------------------------------------------------------------------
    // Métodos públicos.
    // -------------------------------------------------------------------------
    
    /**
     * Prepara o campo TEMPOS ENTRE String[] para entrada no código R
     * @return 
     */
    public String[] getStringArrayTempos() {
        return serieTempoEntreR;
    }
    
    /**
     * Prepara o campo TIPOS String[] para entrada no código R
     * @return 
     */
    public String[] getStringArrayTipos() {
        return serieTipoJava;
    }
}