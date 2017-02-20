/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.text.StyledEditorKit;

/**
 * Autor Rubens Oliveira da Cunha Júnior
 */
public class DataFormatada {
    
    private final Calendar calendarioGregoriano;
    private final JComboBox cbxDia, cbxMes, cbxAno;
    private final Object[] valoresDia, valoresMes, valoresAno;
    
    // Construtor
    public DataFormatada() {
        
        // Inicializa o calendário com ano, mês e dia atuais
        calendarioGregoriano = new GregorianCalendar();
        calendarioGregoriano.setLenient(true);
        
        // Object[] com os dias válidos
        valoresDia = new Object[calendarioGregoriano.getMaximum(Calendar.DAY_OF_MONTH)];
        for(int i = 1; i <= calendarioGregoriano.getMaximum(Calendar.DAY_OF_MONTH); i++) {
            valoresDia[i - 1] = i;
        }
        
        // Inicializa o Object[] valoresAno
        // Preenche valoresAno com os anos de 1950 até o ano atual
        valoresAno = new Object[(calendarioGregoriano.get(Calendar.YEAR) - 1950) + 1];
        for(int i = 1950; i <= calendarioGregoriano.get(Calendar.YEAR); i++) {
            valoresAno[i - 1950] = i;
        }
        // Object[] com os meses válidos
        // Abreviação dos nomes dos meses conforme o local
//        String[] keys = new String[12];
//        calendarioGregoriano.getDisplayNames(
//                    Calendar.MONTH,
//                    Calendar.SHORT_STANDALONE,
//                    Locale.getDefault()).keySet().toArray(keys);
//        
//        int[] values = new int[12];
//        int i = 0;
//        while(i < 12) {
//            values[i] = calendarioGregoriano.getDisplayNames(
//                    Calendar.MONTH,
//                    Calendar.SHORT_STANDALONE,
//                    Locale.getDefault()).get(keys[i]);
//            i++;
//        }
//        
//        int j, k = 0;
//        valoresMes = new Object[12];
//        for(i = 0; i <= 11; i++) {
//            for(j = 0; j <= 11; j++) {
//                if(values[j] == i) {
//                    valoresMes[i] = keys[j].toUpperCase();
//                    break;
//                }
//            }
//            
//        }
        
        valoresMes = new Object[] {"01", "02", "03", "04", "05", "06",
                                    "07", "08", "09", "10", "11", "12"};
        
        // Código que gera os meses de acordo com o local (BUGANDO)
        // Repete alfuns meses
        /*
         * while(i < 12) {
         *     calendarioGregoriano.set(Calendar.MONTH, i);
         *     valoresMes[i] = calendarioGregoriano.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
         *     i++;
         * }
         **/
        
        // JComboBox Dia
        cbxDia = new JComboBox();
        cbxDia.setPreferredSize(new Dimension(40,20));
        cbxDia.setModel(new DefaultComboBoxModel(valoresDia));
        cbxDia.setMaximumRowCount(10);
        //cbxDia.setSelectedIndex(calendarioGregoriano.get(Calendar.DAY_OF_MONTH) - 1);
        cbxDia.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                calendarioGregoriano.set(Calendar.DATE, cbxDia.getSelectedIndex() + 1);
            }
        });
        
        // JComboBox Mês
        cbxMes = new JComboBox();
        cbxMes.setPreferredSize(new Dimension(55,20));
        cbxMes.setModel(new DefaultComboBoxModel(valoresMes));
        cbxMes.setMaximumRowCount(12);
        //cbxMes.setSelectedIndex(calendarioGregoriano.get(Calendar.MONTH));
        cbxMes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == 1) {
                    System.out.println("e.getSource: " + e.getSource());
                    System.out.println("e.getStateChange: " + e.getStateChange());
                    System.out.println("e.getItem: " + e.getItem());
                    System.out.println("index selecionado: " + cbxMes.getSelectedIndex());
                    calendarioGregoriano.set(Calendar.MONTH, Integer.parseInt(String.valueOf(e.getItem())) - 1);
                    System.out.println("mes: " + calendarioGregoriano.get(Calendar.MONTH));
                }
            }
        });
        
        // JComboBox Ano
        cbxAno = new JComboBox();
        cbxAno.setPreferredSize(new Dimension(55,20));
        cbxAno.setModel(new DefaultComboBoxModel(valoresAno));
        cbxAno.setMaximumRowCount(10);
        //cbxAno.setSelectedItem(calendarioGregoriano.get(Calendar.YEAR));
        cbxAno.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                calendarioGregoriano.set(Calendar.YEAR, cbxAno.getSelectedIndex() + 1951);
            }
        });
    }
    
    /*
     * Método que retorna um JPanel com o objeto HoraFormatada adicionado.
    */
    public JPanel adicionar() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        
        painel.add(cbxDia);
        painel.add(cbxMes);
        painel.add(cbxAno);
        //painel.add(calendario.btnCalendario);
        
        return painel;
    }
    
    public String getDataString(String formato) {
        int ano = cbxAno.getSelectedIndex() + 1951;
        int mes = cbxMes.getSelectedIndex() - 1;
        int dia = (int) cbxDia.getSelectedItem();
        
        Calendar calendario = new GregorianCalendar();
        calendario.set(Calendar.YEAR, ano);
        calendario.set(Calendar.MONTH, mes);
        calendario.set(Calendar.DAY_OF_MONTH, dia);
        
//        calendarioGregoriano.set(Calendar.YEAR, cbxAno.getSelectedIndex() + 1950);
//        calendarioGregoriano.set(Calendar.MONTH, cbxMes.getSelectedIndex() + 1);
//        calendarioGregoriano.set(Calendar.DAY_OF_MONTH, cbxDia.getSelectedIndex() + 1);
        String data = null;
        if(formato.equals("yyyy-MM-dd")) {
            data = String.valueOf(calendarioGregoriano.get(Calendar.YEAR)) + "-" +
                    String.valueOf(calendarioGregoriano.get(Calendar.MONTH)) + "-" +
                        String.valueOf(calendarioGregoriano.get(Calendar.DAY_OF_MONTH));
        
        }
        return data;
    }
}
