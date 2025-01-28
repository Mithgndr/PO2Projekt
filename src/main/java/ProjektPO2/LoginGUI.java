package ProjektPO2;

import ProjektPO2.Users.Uzytkownik;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class LoginGUI {
    private JButton btnLogin;
    private JLabel lblLogin;
    private JLabel lblPassword;
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JPanel loginPanel;
    private JFrame loginFrame;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = screenSize.width;
    private int screenHeight = screenSize.height;

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 23456;
    private static final int CONNECTION_TIMEOUT = 100000; // Timeout w milisekundach


    public JFrame getLoginFrame() { return loginFrame; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }

    public LoginGUI() {
        initLoginGUI();
    }

    private void initLoginGUI() {
        Font font = new Font("Arial", Font.BOLD, 24);
        loginFrame = new JFrame("System zarządzania biblioteką");
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

        try {
            try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
                socket.setSoTimeout(CONNECTION_TIMEOUT);

                try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    out.println("LOGIN;" + loginText + ";" + passwordText);

                    String serverResponse = in.readLine();

                    if (serverResponse != null && serverResponse.startsWith("SUCCESS")) {

                        Biblioteka biblioteka = new Biblioteka();
                        String[] responseParts = serverResponse.split(";");
                        String rola = responseParts[1];

                        loginFrame.dispose();
                        if ("BIBLIOTEKARZ".equals(rola)) {
                            bibliotekarzZalogowany(biblioteka, out, in);
                            Uzytkownik aktywnyUzytkownik = new Uzytkownik(biblioteka.znajdzUzytkownika(loginText));
                            new BibliotekaGUI(biblioteka, aktywnyUzytkownik);

                        } else if ("CZYTELNIK".equals(rola)) {
                            czytelnikZalogowany(biblioteka, out, in);

                        } else {
                            JOptionPane.showMessageDialog(loginFrame, "Nieznana rola użytkownika.", "Błąd", JOptionPane.ERROR_MESSAGE);
                            txtPassword.setText("");
                        }
                    } else {
                        JOptionPane.showMessageDialog(loginFrame, "Nieprawidłowe dane logowania", "Błąd", JOptionPane.ERROR_MESSAGE);
                        txtPassword.setText("");
                    }

                } catch (SocketTimeoutException ex) {
                    JOptionPane.showMessageDialog(loginFrame, "Połączenie z serwerem przekroczyło limit czasu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(loginFrame, "Błąd połączenia z serwerem.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }

            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        loginFrame.dispose();
    }


    void bibliotekarzZalogowany(Biblioteka biblioteka, PrintWriter out, BufferedReader in) throws  IOException {
        wczytajKsiazki(biblioteka, out, in);
        wczytajUzytkownikow(biblioteka, out, in);
        Biblioteka.setNextNumerKarty(Integer.parseInt(in.readLine()));
    }


    void czytelnikZalogowany(Biblioteka biblioteka, PrintWriter out, BufferedReader in) throws IOException {
        wczytajKsiazki(biblioteka, out, in);
        wczytajKsiazkiUzytkownika(biblioteka, in);
    }


    void wczytajKsiazki(Biblioteka biblioteka, PrintWriter out, BufferedReader in) throws  IOException {
        String books = in.readLine();
        while (books != null && !books.equals("END")) {
            try {
                String[] book = books.split("\\$");
                Ksiazka ksiazka = new Ksiazka(book);
                biblioteka.dodajKsiazke(ksiazka);
            } catch (Exception e) {
                System.err.println("Nie udało sie dodać książki: " + books);
            }
            books = in.readLine();
        }
    }

    void wczytajUzytkownikow(Biblioteka biblioteka, PrintWriter out, BufferedReader in) throws IOException {
        String users = in.readLine();
        if (users.startsWith("START_USERS")) {
            users = in.readLine();
            while (users != null && !users.equals("END_USERS")) {
                try {
                    String[] user = new String[6];
                    String[] userSplit = users.split("#");

                    System.arraycopy(userSplit, 0, user, 0, userSplit.length);

                    Uzytkownik uzytkownik = new Uzytkownik(user);


                    int i = 0;
                    if (user[4] != null && user[4].contains(";")) {
                        String[] wypozyczoneKsiazkiData = user[4].split(";");
                        while (i < wypozyczoneKsiazkiData.length) {
                            uzytkownik.wypozyczKsiazke(wypozyczoneKsiazkiData[i]);
                            i++;
                        }
                    } else if (user[4] != null) {
                        uzytkownik.wypozyczKsiazke(user[4]);
                    }

                    i = 0;
                    if (user[5] != null && user[5].contains(";")) {
                        String[] zarezerwowaneKsiazkiData = user[5].split(";");
                        while (i < zarezerwowaneKsiazkiData.length) {
                            uzytkownik.zarezerwujKsiazke(zarezerwowaneKsiazkiData[i]);
                            i++;
                        }
                    } else if (user[5] != null) {
                        uzytkownik.zarezerwujKsiazke(user[5]);
                    }

                    biblioteka.dodajUzytkownika(uzytkownik);
                    users = in.readLine();
                } catch (Exception e) {
                    System.err.println("Nie udało się dodać użytkownika: " + users);
                }
            }
        }
    }

    public void wczytajKsiazkiUzytkownika(Biblioteka biblioteka, BufferedReader in) throws IOException {
        String user = in.readLine();
        if (user.startsWith("START_USER")) {
            String userData0 = user.replace("START_USER~", "");
            String[] userData = new String[6];
            String[] userDataSplit = userData0.split("#");


            for (int i = 0; i < userDataSplit.length; i++) {
                userData[i] = userDataSplit[i];
            }
            for (int i = userDataSplit.length; i < userData.length; i++) {
                userData[i] = null;
            }

            Uzytkownik uzytkownik = new Uzytkownik(userData);
            biblioteka.dodajUzytkownika(uzytkownik.getImie(), uzytkownik.getNazwisko(), uzytkownik.getNrKarty(), uzytkownik.getHaslo(), uzytkownik.getRola());

            int i = 0;
            if (userData[4] != null && userData[4].contains(";")) {
                String[] wypozyczoneKsiazkiData = userData[4].split(";");
                while (i < wypozyczoneKsiazkiData.length) {
                    uzytkownik.wypozyczKsiazke(wypozyczoneKsiazkiData[i]);
                    i++;
                }
            } else if (userData[4] != null) {
                uzytkownik.wypozyczKsiazke(userData[4]);
            }

            i = 0;
            if (userData[5] != null && userData[5].contains(";")) {
                String[] zarezerwowaneKsiazkiData = userData[5].split(";");
                while (i < zarezerwowaneKsiazkiData.length) {
                    uzytkownik.zarezerwujKsiazke(zarezerwowaneKsiazkiData[i]);
                    i++;
                }
            } else if (userData[5] != null) {
                uzytkownik.zarezerwujKsiazke(userData[5]);
            }

            new WyswietlKsiazkiGUI(biblioteka, uzytkownik);

        } else if (in.readLine().equals("END_USER")) {
            System.out.println("END_USER");
        }
    }
}