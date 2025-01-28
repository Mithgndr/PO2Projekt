package ProjektPO2;

import ProjektPO2.Users.CustomArrayList;
import ProjektPO2.Users.Uzytkownik;
import ProjektPO2.LoginGUI;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.net.*;
import java.util.Objects;


public class WyswietlKsiazkiGUI extends LoginGUI {
    private JPanel panel1;
    private JPanel panelMain;
    private JFrame frame;
    private JTable table;
    private final Biblioteka biblioteka;
    private final Uzytkownik uzytkownik;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final int screenWidth = screenSize.width;
    private final int screenHight = screenSize.height;
    private final Font font = new Font("Arial", Font.PLAIN, 20);
    private final Font font_bold = new Font("Arial", Font.BOLD, 20);
    private final BufferedReader in;
    private final PrintWriter out;

    private static final String SERVER_ADDRESS = "localhost"; // Możesz zmienić na adres IP serwera
    private static final int SERVER_PORT = 23456;

    public WyswietlKsiazkiGUI(Biblioteka biblioteka, Uzytkownik uzytkownik) throws IOException {
        this.uzytkownik = uzytkownik;
        this.biblioteka = biblioteka;
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // Inicjalizacja GUI
        initGUI();
    }

    private void initGUI() throws IOException {
        getLoginFrame().dispose();

        frame = new JFrame("System Zarządzania Biblioteką");
        frame.setVisible(true);

        if (uzytkownik.getRola().equals(Rola.CZYTELNIK)) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        if (uzytkownik.getRola().equals(Rola.BIBLIOTEKARZ)) {
            frame.setAlwaysOnTop(true);
        }
        frame.setSize(screenWidth / 2, screenHight / 2);
        frame.setLocation(screenWidth / 4, screenHight / 4);
        frame.setLayout(new BorderLayout());

        // Panel z przyciskami wyboru
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout());

        JButton btnAllBooks = new JButton("Wyświetl wszystkie książki");
        btnAllBooks.setFont(font_bold);
        btnAllBooks.addActionListener(e -> pokazDostepneKsiazki());
        menuPanel.add(btnAllBooks);

        JButton btnUserBooks = new JButton("Twoje książki");
        btnUserBooks.setFont(font_bold);
        btnUserBooks.addActionListener(e -> pokazKsiazkiUzytkownika());
        menuPanel.add(btnUserBooks);

        frame.add(menuPanel, BorderLayout.NORTH);

        panelMain = new JPanel(new BorderLayout());
        frame.add(panelMain, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void pokazDostepneKsiazki() {
        panelMain.removeAll();

        // Pobierz listę wszystkich książek
        List<Ksiazka> ksiazki = biblioteka.getKsiazki();

        // Kolumny tabeli
        String[] columnNames = {"Tytuł", "Autor", "Kategoria", "Dostępność", "Rezerwuj", "Wypożycz"};
        DefaultTableModel tableModel = new DefaultTableModel(0, columnNames.length) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Komórki z przyciskami mogą być edytowalne
                return column >= 4;
            }
        };
        tableModel.setColumnIdentifiers(columnNames);

        // Dodanie danych do modelu tabeli
        for (Ksiazka ksiazka : ksiazki) {
            Object[] rowData = {
                    ksiazka.getTytul(),
                    ksiazka.getAutor(),
                    ksiazka.getKategoria(),
                    ksiazka.getCzydostepna() ? "Dostępna" : "Niedostępna",
                    "Rezerwuj",
                    "Wypożycz"
            };
            tableModel.addRow(rowData);
        }

        // Tworzenie tabeli
        table = new JTable(tableModel);
        table.setFont(font);
        table.getTableHeader().setFont(font_bold);
        table.setRowHeight(30);

        // Dodanie renderera/przycisków do kolumn akcji
        table.getColumn("Rezerwuj").setCellRenderer(new ButtonRenderer());
        table.getColumn("Rezerwuj").setCellEditor(new ButtonEditor(new JCheckBox(), "Rezerwuj"));

        table.getColumn("Wypożycz").setCellRenderer(new ButtonRenderer());
        table.getColumn("Wypożycz").setCellEditor(new ButtonEditor(new JCheckBox(), "Wypożycz"));

        // Dodanie tabeli do scrollowanego panelu
        JScrollPane scrollPane = new JScrollPane(table);
        panelMain.add(scrollPane, BorderLayout.CENTER);

        // Odświeżenie panelu
        panelMain.revalidate();
        panelMain.repaint();
    }

    private void pokazKsiazkiUzytkownika() {
        panelMain.removeAll();

        List<Ksiazka> ksiazki = new CustomArrayList<>();

        // Pobierz książki wypożyczone/zarezerwowane przez użytkownika
        List<String> zarezerwowaneKsiazki = uzytkownik.getZarezerwowaneKsiazki();
        List<String> wypozyczoneKsiazki = uzytkownik.getWypozyczoneKsiazki();
        System.out.println(zarezerwowaneKsiazki);
        System.out.println(wypozyczoneKsiazki);


        int i = 0, j = 0;
        while (i < zarezerwowaneKsiazki.size() && zarezerwowaneKsiazki.get(i) != null) {
            if(!Objects.equals(zarezerwowaneKsiazki.get(i), "") && zarezerwowaneKsiazki.get(i) != null)
                ksiazki.add(new Ksiazka(biblioteka, zarezerwowaneKsiazki.get(i)));
            i++;
        }


        while (j < wypozyczoneKsiazki.size() && wypozyczoneKsiazki.get(j) != null) {
            if(!Objects.equals(wypozyczoneKsiazki.get(j), "") && wypozyczoneKsiazki.get(j) != null )
                ksiazki.add(new Ksiazka(biblioteka, wypozyczoneKsiazki.get(j)));
            j++;
        }

        // Kolumny tabeli
        String[] columnNames = {"Tytuł", "Autor", "Kategoria", "Status", "Oddaj", "Anuluj Rezerwację"};
        DefaultTableModel tableModel = new DefaultTableModel(0, columnNames.length) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Komórki z przyciskami mogą być edytowalne
                return column >= 4;
            }
        };
        tableModel.setColumnIdentifiers(columnNames);

        // Dodanie danych do modelu tabeli
        for (Ksiazka ksiazka : ksiazki) {
            String status = ksiazka.getCzydostepna() ? "Zarezerwowana" : "Wypożyczona";
            Object[] rowData = {
                    ksiazka.getTytul(),
                    ksiazka.getAutor(),
                    ksiazka.getKategoria(),
                    status,
                    "Oddaj",
                    "Anuluj"
            };
            tableModel.addRow(rowData);
        }

        // Tworzenie tabeli
        table = new JTable(tableModel);
        table.setFont(font);
        table.getTableHeader().setFont(font_bold);
        table.setRowHeight(30);

        // Dodanie renderera/przycisków do kolumn akcji
        table.getColumn("Oddaj").setCellRenderer(new ButtonRenderer());
        table.getColumn("Oddaj").setCellEditor(new ButtonEditor(new JCheckBox(), "Oddaj"));

        table.getColumn("Anuluj Rezerwację").setCellRenderer(new ButtonRenderer());
        table.getColumn("Anuluj Rezerwację").setCellEditor(new ButtonEditor(new JCheckBox(), "Anuluj"));

        // Dodanie tabeli do scrollowanego panelu
        JScrollPane scrollPane = new JScrollPane(table);
        panelMain.add(scrollPane, BorderLayout.CENTER);

        // Odświeżenie panelu
        panelMain.revalidate();
        panelMain.repaint();
    }

    private void wykonajAkcje(String akcja, String tytul) throws IOException {
        String nrKarty = uzytkownik.getNrKarty();
        boolean success;
        String serverResponse;

        switch (akcja) {
            case "Rezerwuj":
                biblioteka.zarezerwujKsiazke(nrKarty, tytul);
                out.println("RESERVE;" + nrKarty + ";" + tytul);
                serverResponse = in.readLine();
                success = "SUCCESS".equalsIgnoreCase(serverResponse);
                JOptionPane.showMessageDialog(frame, success ? "Książka została zarezerwowana!" : "Nie udało się zarezerwować książki.");

                break;
            case "Wypożycz":
                biblioteka.wypozyczKsiazke(nrKarty, tytul);
                out.println("BORROW;" + nrKarty + ";" + tytul);
                serverResponse = in.readLine();
                success = "SUCCESS".equalsIgnoreCase(serverResponse);
                JOptionPane.showMessageDialog(frame, success ? "Książka została wypożyczona!" : "Nie udało się wypożyczyć książki.");
                frame.dispose();
                super.wczytajKsiazkiUzytkownika(biblioteka, in);
                break;
            case "Oddaj":
                biblioteka.zwrocKsiazke(nrKarty, tytul);
                out.println("RETURN;" + nrKarty + ";" + tytul);
                serverResponse = in.readLine();
                success = "SUCCESS".equalsIgnoreCase(serverResponse);
                JOptionPane.showMessageDialog(frame, success ? "Książka została oddana!" : "Nie udało się oddać książki.");
                frame.dispose();
                super.wczytajKsiazkiUzytkownika(biblioteka, in);
                break;
            case "Anuluj":
                biblioteka.anulujRezerwacjeKsiazki(nrKarty, tytul);
                out.println("CANCEL;" + nrKarty + ";" + tytul);
                serverResponse = in.readLine();
                success = "SUCCESS".equalsIgnoreCase(serverResponse);
                JOptionPane.showMessageDialog(frame, success ? "Rezerwacja została anulowana!" : "Nie udało się anulować rezerwacji.");
                frame.dispose();
                super.wczytajKsiazkiUzytkownika(biblioteka, in);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + akcja);
        }
    }



    public class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(font_bold);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }

    public class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private String label;

        public ButtonEditor(JCheckBox checkBox, String defaultLabel) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setFont(font_bold);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = value == null ? "" : value.toString();
            button.setText(label);

            // Odczyt tytułu książki w wierszu
            String tytul = table.getValueAt(row, 0).toString();

            // Wywołanie odpowiedniej akcji
            try {
                wykonajAkcje(label, tytul);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}