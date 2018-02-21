/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mesor.menu;

import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.swing.*;

/**
 * Autor Rubens Oliveira da Cunha Júnior
 */
public class HoraFormatada extends JPanel {
    
    private final JLabel separador;
    private final Object[] valoresHora, valoresMinuto;
    private final JComboBox cb1;
    private final JComboBox cb2;
    private final JPanel painel;
    
    public HoraFormatada() {
        separador = new JLabel(":");
        
        // Object[] com as horas válidas
        valoresHora = new Object[] {" ",
            "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
            "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        
        // Object[] com os minutos válidos
        valoresMinuto = new Object[] {" ",
            "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
            "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
            "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35",
            "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47",
            "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"};
        
        cb1 = new JComboBox();
        cb1.setPreferredSize(new Dimension(40,20));
        cb1.setModel(new DefaultComboBoxModel(valoresHora));
        cb1.setMaximumRowCount(13);
        
        cb2 = new JComboBox();
        cb2.setPreferredSize(new Dimension(40,20));
        cb2.setModel(new DefaultComboBoxModel(valoresMinuto));
        cb2.setMaximumRowCount(13);
        
        painel = new JPanel(new FlowLayout());
        
        painel.add(cb1);
        painel.add(separador);
        painel.add(cb2);
    }
    
    /*
     * Método que retorna um JPanel com o objeto HoraFormatada adicionado.
    */
    public JPanel adicionar() {        
        return painel;
    }
    
    public boolean eValido() {
        return !(cb1.getSelectedIndex() == 0 || cb2.getSelectedIndex() == 0);
    }
    
    public String getHora() {
        if(this.eValido()) {
            return cb1.getSelectedItem().toString() + ":" + 
                        cb2.getSelectedItem().toString() + ":00";
        }
        else return "erro de formatação de hora.";
    }
    
    public JComboBox getJComboBoxHora() {
        return cb1;
    }
    
    public JComboBox getJComboBoxMinuto() {
        return cb2;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        cb1.setEnabled(enabled);
        cb2.setEnabled(enabled);
    }
    
    /**
     * Método estático público que calcula a data atual e retorna como string
     * 
     * @return String com a data de hoje
     */
    public static String hoje() {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        String ano = String.valueOf(c.get(Calendar.YEAR));
        String dia = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String mes;
        
        int i = c.get(Calendar.MONTH) + 1;
        mes = (i < 10) ? "0".concat(String.valueOf(i)) : String.valueOf(i);
        
        String hoje = dia + mes + ano;
        return hoje;
    }
}
