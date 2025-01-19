package ProjektPO2;

import ProjektPO2.Users.Uzytkownik;

import javax.swing.*;
import java.awt.*;

public class LoginGUI {
    private JButton btnLogin;
    private JLabel lblLogin;
    private JLabel lblPassword;
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JPanel loginPanel;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = screenSize.width;
    private int screenHeight = screenSize.height;
    private Biblioteka biblioteka = new Biblioteka();

    private Uzytkownik aktywnyUzytkownik = null;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }

    public LoginGUI() {
        biblioteka.wczytajZPliku("dane.json");
        initLoginGUI();
    }

    private void initLoginGUI() {
        Font font = new Font("Arial", Font.BOLD, 24);
        JFrame loginFrame = new JFrame("System zarządzania biblioteką");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 200);
        loginFrame.setLocation(screenWidth / 2 - 200, screenHeight / 2 - 100);

        loginFrame.setFont(font);
        loginPanel.setFont(font);
        lblLogin.setFont(font);
        lblPassword.setFont(font);
        btnLogin.setFont(font);
        txtLogin.setFont(font);
        txtPassword.setFont(font);

        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);

        btnLogin.addActionListener(e -> loginAction(loginFrame));

        txtLogin.addActionListener(e -> loginAction(loginFrame));

        txtPassword.addActionListener(e -> loginAction(loginFrame));
    }

    void loginAction(JFrame loginFrame) {
        String loginText = txtLogin.getText();
        String passwordText = new String(txtPassword.getPassword());

        Uzytkownik uzytkownik = biblioteka.znajdzUzytkownika(loginText);

        if (uzytkownik != null && passwordText.equals(uzytkownik.getHaslo())) {
            Rola rola = uzytkownik.getRola();
            System.out.println("Rola użytkownika: '" + rola + "'");
            if (rola == Rola.BIBLIOTEKARZ) {
                loginFrame.dispose();
                new BibliotekaGUI(uzytkownik);
            } else if (rola == Rola.CZYTELNIK) {
                loginFrame.dispose();
                new CzytelnikGUI(uzytkownik);
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Nieznana rola użytkownika.", "Błąd", JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
            }
        } else {
            JOptionPane.showMessageDialog(loginFrame, "Nieprawidłowe dane logowania", "Błąd", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }
    }
    public boolean czyJestZalogowany() {
        return aktywnyUzytkownik != null;
    }


    public Uzytkownik getAktywnyUzytkownik() {
        return aktywnyUzytkownik;
    }

}
