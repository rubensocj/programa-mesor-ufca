/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.table.*;

/**
 * Autor Rubens Oliveira da Cunha Júnior
 */
public class Calendario {
    
    private final JPanel pnlCalendario;
    private JPanel pnlAno, pnlTabelaCalendario;
    
    private final JLabel lblMesEAno;
    
    private JButton btnAnterior, btnSeguinte;
    public final JButton btnCalendario;
    
    private Object[][] dias;
    private final Object[] nomesDosDias;
    
    private final int mesInicial;
    public int dia, mes, ano;
    private int diaDoMes, linhaDia, colunaDia;
    
    private final GregorianCalendar calendario;
    private JTable tabela;
    
    private DefaultTableModel modeloTabelaCalendario;
    private DefaultTableCellRenderer renderCabecalho, renderCelula;
    
    public final JPopupMenu pupCalendario;
    
    public Calendario() {
        // Inicializa o calendário
        calendario = new GregorianCalendar();
        
        // Dados iniciais (Pega a data atual)
        // Seta o ano
        ano = calendario.get(Calendar.YEAR);
        // Mês, 0 = Jan, 1 = Fev, ..., 11 = Dez
        mes = calendario.get(Calendar.MONTH);
        // Mês atual, variável final
        // Valor fixo usado para selecionar automaticamente o dia atual
        mesInicial = calendario.get(Calendar.MONTH);
        // Dia selecionado na tabela
        dia = calendario.get(Calendar.DAY_OF_MONTH);
        // Linha da tabela em que se encontra o dia atual
        linhaDia = calendario.get(Calendar.WEEK_OF_MONTH);
        // Coluna da tabela em que se encontra o dia atual
        colunaDia = calendario.get(Calendar.DAY_OF_WEEK);
        
        // Inicializa o Object[][] dias
        dias = new Object[6][7];
        // Nomes dos dias da semana
        nomesDosDias = new Object[] {"D", "S", "T", "Q", "Q", "S", "S"};
        
        // JLabel com mês e ano
        lblMesEAno = new JLabel();
        lblMesEAno.setHorizontalAlignment(JLabel.CENTER);
        lblMesEAno.setText(calendario.getDisplayName(Calendar.MONTH,
                        Calendar.LONG, Locale.getDefault()) + ", " + ano);
        
        // Botão invoca o calendário
        btnCalendario = new JButton();
        btnCalendario.setIcon(new ImageIcon("C:\\Users\\Rubens Jr\\Documents\\NetBeansProjects\\Programa\\icone\\ds.png"));
        btnCalendario.setPreferredSize(new Dimension(24,24));
        btnCalendario.setOpaque(true);
        btnCalendario.setBackground(new Color(255,204,153));
        btnCalendario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pupCalendario.show(btnCalendario, 0, 24);
                // Seleciona automativamente a célula com o dia atual se o mês for o atual
                // Caso contrário, não seleciona nenhuma célula automaticamente
                if(mes == mesInicial) {                
                    tabela.setRowSelectionInterval(linhaDia - 1, linhaDia - 1);
                    tabela.setColumnSelectionInterval(colunaDia - 1, colunaDia - 1);
                }}});
        
        // Painel final
        pnlCalendario = new JPanel(new BorderLayout());
        pnlCalendario.add(painelData(), BorderLayout.NORTH);
        pnlCalendario.add(painelTabelaCalendario(), BorderLayout.CENTER);
        
        // PopUp menu que mostra o painel calendário
        pupCalendario = new JPopupMenu();
        pupCalendario.add(pnlCalendario);
        pupCalendario.setInvoker(btnCalendario);
    }
    
    // Painel com nomde do mês e o ano
    private JPanel painelData() {

        // Painel que abriga os botões "<<" e ">>", o mês e o ano
        pnlAno = new JPanel(new BorderLayout());
        
        // Botões "<<" e ">>"
        btnAnterior = new JButton();
        btnAnterior.setIcon(new ImageIcon("C:\\Users\\Rubens Jr\\Documents\\NetBeansProjects\\Programa\\icone\\seta_esquerda.png"));
        btnAnterior.setPreferredSize(new Dimension(16,16));
        btnAnterior.setActionCommand("-1");
        btnAnterior.addActionListener(new ActionAlterarData());
        
        btnSeguinte = new JButton();
        btnSeguinte.setIcon(new ImageIcon("C:\\Users\\Rubens Jr\\Documents\\NetBeansProjects\\Programa\\icone\\seta_direita.png"));
        btnSeguinte.setPreferredSize(new Dimension(16,16));
        btnSeguinte.setActionCommand("1");
        btnSeguinte.addActionListener(new ActionAlterarData());

        pnlAno.add(btnAnterior, BorderLayout.WEST);
        pnlAno.add(lblMesEAno, BorderLayout.CENTER);
        pnlAno.add(btnSeguinte, BorderLayout.EAST);
        return pnlAno;
    }

    // Retorna um JPanel com o calendário
    private JPanel painelTabelaCalendario() {
        
        modeloTabelaCalendario = new DefaultTableModel(
                    calendario(mes, ano), nomesDosDias)
        {
            // Sobrescreve o método booleano isCellEditable
            @Override
            public boolean isCellEditable(int row, int column) {
                //todas as células não são editáveis
                return false;
            }
        };
        
        // Cria a tabela
        tabela = new JTable(modeloTabelaCalendario);
        tabela.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    Object diaSelecionado = tabela.getValueAt(
                                tabela.getSelectedRow(), tabela.getSelectedColumn());
                    if(diaSelecionado.getClass() == Integer.class) {
                        dia = (int) diaSelecionado;
                    } else {
                        dia = 0;
                    }
                    System.out.println(dia);
                    pupCalendario.setVisible(false);
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}});
        
        // Inicializa a tabela
        tabela.setRowSelectionAllowed(true);
        tabela.setColumnSelectionAllowed(true);        
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabela.setCellSelectionEnabled(true);
        tabela.setShowGrid(true);
        tabela.setGridColor(Color.WHITE);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabela.getTableHeader().setResizingAllowed(false);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        
        // Cria e customiza o renderizador das células
        renderCelula = new DefaultTableCellRenderer();
        renderCelula.setHorizontalAlignment(JLabel.CENTER);
        renderCelula.setBackground(new Color(224, 224, 224));
        // Cria e customiza o renderizador dos cabeçalhos das colunas
        renderCabecalho = new DefaultTableCellRenderer();
        renderCabecalho.setHorizontalAlignment(JLabel.CENTER);
        renderCabecalho.setBackground(Color.DARK_GRAY);
        renderCabecalho.setForeground(Color.WHITE);
        
        for(int i = 0; i < tabela.getColumnModel().getColumnCount(); i++) {
            TableColumn coluna = tabela.getColumnModel().getColumn(i);
            coluna.setCellRenderer(renderCelula);
            coluna.setHeaderRenderer(renderCabecalho);
            coluna.setPreferredWidth(20);
            coluna.setResizable(false);
        }
        
        pnlTabelaCalendario = new JPanel(new BorderLayout());
        pnlTabelaCalendario.add(tabela.getTableHeader(), BorderLayout.NORTH);
        pnlTabelaCalendario.add(tabela, BorderLayout.CENTER);
        
        return pnlTabelaCalendario;
    }
    
    private class ActionAlterarData implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            int acao = Integer.parseInt(event.getActionCommand());
            
            // 0 < Mês < 11
            mes = mes + acao;
            
            // Se o mês era Janeiro (0), e recebeu a ação "mês anterior" (-1)
            if(mes < 0) {
                // O mês agora é Dezembro do ano anterior (ano -1)
                mes = 11;
                ano = ano - 1;
            }
            // Se o mês era Dezembro (11), e recebeu a ação "mês seguinte" (+1)
            else if(mes > 11) {
                // O mês agora é Janeiro do ano seguinte (ano +1)
                mes = 0;
                ano = ano + 1;
            }            
            
            // Atualiza o novo mês do objeto calendario
            calendario.set(Calendar.MONTH, mes);
            
            // Atualiza o JLabel
            lblMesEAno.setText(calendario.getDisplayName(Calendar.MONTH,
                        Calendar.LONG, Locale.getDefault()) + ", " + ano);
            
            // Altera o modelo da Tabela com os novs valores de mês e ano
            modeloTabelaCalendario.setDataVector(calendario(mes, ano), nomesDosDias);
            tabela.setModel(modeloTabelaCalendario);
            
            // Seleciona automativamente a célula com o dia atual se o mês for o atual
            // Caso contrário, não seleciona nenhuma célula automaticamente
            if(mes == mesInicial) {                
                tabela.setRowSelectionInterval(linhaDia - 1, linhaDia - 1);
                tabela.setColumnSelectionInterval(colunaDia - 1, colunaDia - 1);
            }
            
            // Atualiza os renderizadores de célula e cabeçalho
            for(int i = 0; i < tabela.getColumnModel().getColumnCount(); i++) {
                TableColumn coluna = tabela.getColumnModel().getColumn(i);
                coluna.setCellRenderer(renderCelula);
                coluna.setHeaderRenderer(renderCabecalho);
                coluna.setPreferredWidth(20);
                coluna.setResizable(false);
            } // Fim do loop for
        } // Fim do ActioPerformed
    } // Fim do ActionAlterarData
    
    // Monta o calendário conforme mês e ano informados
    private Object[][] calendario(int month, int year) {
        diaDoMes = 1;       // Dia do mês, primeiro = 1
        int semanaDoMes;    // Semana do mês, primeira = 1
        
        // Converte em int[][]
        int[][] diasInteger = new int[6][7];
        
        // Seta os valores iniciais
        calendario.set(Calendar.YEAR, year);
        calendario.set(Calendar.MONTH, month);        
        calendario.set(Calendar.DAY_OF_MONTH, diaDoMes); // 1º
            
        // Começa o loop na primeira semana
        semanaDoMes = calendario.get(Calendar.WEEK_OF_MONTH);

        // Enquanto a semana for igual a "semanaDoMes", o loop roda em todas
        // as semanas do mês, começando da primeira, 1
        while(semanaDoMes == calendario.get(Calendar.WEEK_OF_MONTH)) {
            
            // Pega o dia da semana referente ao dia od mês especificado
            int diaDaSemana = calendario.get(Calendar.DAY_OF_WEEK);

            // Adiciona o dia ao array
            diasInteger[semanaDoMes - 1][diaDaSemana - 1] = calendario.get(Calendar.DAY_OF_MONTH);

            // Passa para o próximo dia do mês
            diaDoMes++;            
            calendario.set(Calendar.DAY_OF_MONTH, diaDoMes); // 1º

            // Seta a nova semana
            semanaDoMes = calendario.get(Calendar.WEEK_OF_MONTH);
            
            // Se trocar de mês, quebra o loop
            if(calendario.get(Calendar.MONTH) != mes) break;
        }
        
        // Converte em Object[][] novamente
        dias = to2DObjectArray(diasInteger);
        return dias;        
    }
    
    // Converte Object[][] em int[][]
    private int[][] to2DIntegerArray(Object[][] object) {
        int[][] integerArray = new int[object[0].length][object[1].length];
        
        for(int i = 0; i <= object[0].length; i++) {
            for(int j = 0; j <= object[1].length; j++) {
                integerArray[i][j] = (int) object[i][j];
            }
        }
        
        return integerArray;
    }
    
    // Converte int[][] em Object[][]
    private Object[][] to2DObjectArray(int[][] integer) {
        Object[][] objectArray = new Object[integer[0].length - 1][integer[1].length];
        
        for(int i = 1; i < integer[0].length; i++) {
            for(int j = 1; j < integer[1].length + 1; j++) {
                if(integer[i - 1][j - 1] == 0) {
                    objectArray[i - 1][j - 1] = "";
                } else {
                    objectArray[i - 1][j - 1] = (Object) integer[i - 1][j - 1];
                }
            }
        }
        
        return objectArray;
    }
    
} // Fim da classe Calendario
    

