package ProjektPO2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LoginGUI {
    private JButton btnLogin;
    private JLabel lblLogin;
    private JLabel lblPassword;
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JPanel loginPanel;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = screenSize.width;
    private int screenHight = screenSize.height;
    private Biblioteka biblioteka = new Biblioteka();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }

    public LoginGUI() {
        biblioteka.wczytajZPliku("dane.json");
        initLoginGUI();
    }
    private void initLoginGUI() {
        JFrame loginFrame = new JFrame("System zarządzania biblioteką");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 200);
        loginFrame.setLocation(screenWidth/2 - 200, screenHight/2 - 100);

        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);

        btnLogin.addActionListener(e -> {
            if (txtLogin.getText().equals(biblioteka.znajdzUzytkownika(txtLogin.getText()).getNrKarty()) && new String(txtPassword.getPassword()).equals(biblioteka.znajdzUzytkownika(txtLogin.getText()).getHaslo())) {
                loginFrame.dispose();
                BibliotekaGUI bibliotekaGUI = new BibliotekaGUI();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Nieprawidłowe dane logowania", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
