
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author Rubens Oliveira da Cunha Júnior
 */
public class InstallR {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JButton btn1 = new JButton("Instalar");
        JButton btn2 = new JButton("Cancelar");
        Object[] options = new Object[] {btn1, btn2};
        
        JOptionPane mPane = new JOptionPane();
        mPane.setMessage(System.getenv("num") + " R installed version(s) " + 
                    "was found. Would you like to install R-3.4.3?");
        mPane.setOptions(options);
        mPane.setMessageType(JOptionPane.WARNING_MESSAGE);

        JDialog mDialog = mPane.createDialog(mPane, "Aviso");
                
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("INSTALAR selecionado");
                try {
                    Runtime.getRuntime().exec(
                            "cmd.exe /c start R-3.4.3-win.exe /SILENT exit");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                
                mDialog.dispose();
            }
        });
        
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("CANCELAR selecionado");
                mDialog.dispose();
            }
        });
                
        mDialog.pack();
        mDialog.setVisible(true);
        mDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}
