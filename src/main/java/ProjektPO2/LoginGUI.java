package ProjektPO2;

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
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 200);
        loginFrame.setLocation(screenWidth / 2 - 200, screenHeight / 2 - 100);

        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);

        btnLogin.addActionListener(e -> loginAction(loginFrame));

        txtLogin.addActionListener(e -> loginAction(loginFrame));

        txtPassword.addActionListener(e -> loginAction(loginFrame));
    }

    private void loginAction(JFrame loginFrame) {
        String loginText = txtLogin.getText();
        String passwordText = new String(txtPassword.getPassword());

        Uzytkownik uzytkownik = biblioteka.znajdzUzytkownika(loginText);

        if (uzytkownik != null && passwordText.equals(uzytkownik.getHaslo())) {
            Rola rola = uzytkownik.getRola();
            System.out.println("Rola użytkownika: '" + rola + "'");
            if (rola == Rola.BIBLIOTEKARZ) {
                loginFrame.dispose();
                new BibliotekaGUI();
            } else if (rola == Rola.CZYTELNIK) {
                loginFrame.dispose();
                new CzytelnikGUI(uzytkownik);
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Nieznana rola użytkownika.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(loginFrame, "Nieprawidłowe dane logowania", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
    public boolean czyJestZalogowany() {
        return aktywnyUzytkownik != null;
    }


    public Uzytkownik getAktywnyUzytkownik() {
        return aktywnyUzytkownik;
    }

}
