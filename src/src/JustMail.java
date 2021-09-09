package src;

import com.alee.laf.WebLookAndFeel;
import src.ui.Addressbook;
import src.ui.Main;

public class JustMail {

    public static Main mainForm = null;
    
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                WebLookAndFeel.install();
                mainForm = new Main();
                mainForm.setVisible(true);
            }
        });
    }
}
