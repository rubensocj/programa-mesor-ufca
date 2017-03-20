package menu;

import conexaoSql.Lista;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;

/**
 * JanelaAdicionarAlterar.java
 *
 * @version 1.0 16/02/2017
 * @author Rubens Jr
 */
public abstract class JanelaAdicionarAlterar {

    public JTabbedPane tbdAbas;
    public JPanel pnlPrincipal;
    public JPanel pnlAlterar;
    
    public JButton btnEscolher, btnVoltar, btnBuscar, btnLimparBusca,
                btnSalvarAlteracao;
    
    public JOptionPane pane;
    public JDialog dialog;
    public JButton btn1, btn2, btn3;
    public Object[] options;
    
    /**
     * Mapeia os componentes - JTextFields, JLabels, JComboBoxes, etc.
     */
    public Map<Integer, Component> mapaComponentes;
    
    public boolean edicaoPermitida;
    public int campoEditavel;
    
    /**
     *
     */
    public static final String LOCAL = // Local no pc com arquivos do programa.
        "C:\\Users\\Rubens Jr\\Documents\\NetBeansProjects\\Programa";
    
    public JComboBox cbxSistema;
    private Lista lista;
    private JPanel pnlSistema;
    
    /**
     * Construtores.
     */
    public JanelaAdicionarAlterar() {}
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /**
     * Inicializa o objeto HashMap mapaComponentes. Este objeto mapeia
     * componentes - JTextFields, JLabels, JComboBoxes, etc. - utilizados para
     * a definição dos elementos - unidades, demandas, intervenções, etc. - e/ou
     * eventos - demandas e intervenções. É utilizado em janelas de alteração,
     * onde há necessidade de habilitar ou desabilitar certos componentes.
     */
    public final void inicializarMapaComponentes() {        
        mapaComponentes = new HashMap<>();
    }
    
    /**
     * Faz o mapeamento dos componentes - JTextFields, JLabels,
     * JComboBoxes, etc. - utilizados para a definição dos elementos - unidades,
     * demandas, intervenções, etc. - e/ou eventos - demandas e intervenções.
     */
    public void mapearComponentes() {}
    
    /**
     * Limpa o texto de JTextFields e JFormattedTextField, e as seleções nas
     * JComboBoxes e JCheckBoxes.
     */
    public void limparTexto() {}
    
    /**
     * Habilita ou desabilita componentes do painel de edições.
     * 
     * @param escolha 
     */
    public void habilitarEdicao(boolean escolha) {}
    
    /**
     * Inicializa os botões das opções principais da janela - confirmar,
     * cancelar, ajuda.
     */
    public void criarBotoesOpcoes() {}
    
    /**
     * Inicializa os botões de alterações - escolher, voltar, salvar alterações,
     * buscar e limpar busca.
     */
    public void criarBotoesAlterar() {}
    
    /**
     * Monta o painel para a seleção do sistema.
     * 
     * @return Um painel.
     * @throws SQLException 
     */
    public final JPanel painelSistema() throws SQLException {
        // Inicializa a lista com a consulta SQL e define o modelo da JComboBox
        try {
            lista = new Lista("SELECT descricao FROM sistema");
        } catch (SQLException ex) {ex.printStackTrace();}
        cbxSistema = new JComboBox(lista.toVector());
        cbxSistema.setPreferredSize(new Dimension(275, 20));
        
        JLabel lblSistema = new JLabel("Sistema: ");
        
        pnlSistema = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSistema.add(lblSistema);
        pnlSistema.add(cbxSistema);
        return pnlSistema;
    }
    
    /**
     * Monta o painel principal da janela.
     * 
     * @return Um JPanel.
     */
    public JPanel montarPainelPrincipal() { return pnlPrincipal; }
    
    /**
     * Monta o painel principal e mostra um JDialog como a janela para adição ou
     * alteração do elemento ou evento, com o título especificado.
     * 
     * Se um sistema for selecionado antes da chamada deste método, ele define
     * este sistema como o item o item selecionado na JComboBox
     * {@code cbxSistema}. Se nenhum sistema tiver sido especificado, então o
     * item selecionado na JComboBox será o primeiro - índice zero (nulo).
     *
     * @param titulo 
     * @param indiceSistema 
     */
    public final void mostrar(String titulo, int indiceSistema) {
        
        montarPainelPrincipal();
        
        cbxSistema.setSelectedIndex(indiceSistema);
        
        pane = new JOptionPane();
        pane.setMessage(pnlPrincipal);
        pane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
        pane.setOptions(options);
        
        dialog = pane.createDialog(pane, titulo);
        dialog.pack();
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    /**
     * Monta o painel principal e mostra um JDialog como a janela para adição ou
     * alteração do elemento ou evento, com o título especificado.
     * 
     * @param titulo 
     */
    public final void mostrar(String titulo) {
        
        montarPainelPrincipal();
        
        pane = new JOptionPane();
        pane.setMessage(pnlPrincipal);
        pane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
        pane.setOptions(options);
        
        dialog = pane.createDialog(pane, titulo);
        dialog.pack();
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    /**
     * Monta a área para a escolha do evento ou elemento a ser editado. Eventos
     * como demandas e intervenções podem ser selecionados a partir de uma busca
     * nos elementos - nas tabelas de unidade, subunidade, componente e parte -
     * ou diretamente na sua própria tabela.
     * 
     * @return Um JTabbedPane.
     */
    public JTabbedPane painelAbas() { return tbdAbas; }
    
    /**
     * Executa a operação - adição, alteração, exclusão, etc.
     */
    public void confirmar() {}
    
    // -------------------------------------------------------------------------
    // Classes abstratas.
    // -------------------------------------------------------------------------
    
    /**
     * Busca por eventos - demandas ou intervenções - no elemento - unidade,
     * subunidade, componente e parte - selecionado e os exibe na sua tabela.
     */
    public abstract class ActionBuscar implements ActionListener {}
    
    /**
     * Insere as informações do item selecionado nos JTextFields e 
     * JFormattedTextFields do painel de alterações.
     */
    public abstract class ActionEscolher implements ActionListener {}
    
    /**
     * Limpa a busca por eventos - demandas ou intervenções - realizada.
     * Exibe tabela com todos os eventos.
     */
    public abstract class ActionLimpar implements ActionListener {}
    
    /**
     * Verifica a existência erros nos dados informados pelo usuário. Caso haja,
     * exibe uma mensagem de erro. Caso contrário, executa a operação, chamando
     * o método confirmar().
     */
    public abstract class ActionSalvar implements ActionListener {}
    
    /**
     * Cancela a operação, limpa as seleções e desabilita campos de alteração.
     */
    public abstract class ActionVoltar implements ActionListener {}
}