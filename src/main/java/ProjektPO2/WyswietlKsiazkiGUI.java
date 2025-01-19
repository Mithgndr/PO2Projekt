package ProjektPO2;

import ProjektPO2.Users.Uzytkownik;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WyswietlKsiazkiGUI {
    private JPanel panel1;
    private JPanel panelMain;
    private JFrame frame;
    private JTable table;
    private Biblioteka biblioteka;
    private Uzytkownik uzytkownik;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = screenSize.width;
    private int screenHight = screenSize.height;
    private Font font = new Font("Arial", Font.PLAIN, 20);
    private Font font_bold = new Font("Arial", Font.BOLD, 20);

    public WyswietlKsiazkiGUI(Biblioteka biblioteka, Uzytkownik uzytkownik) {
        this.uzytkownik = uzytkownik;
        this.biblioteka = biblioteka;

        // Inicjalizacja GUI
        initGUI();
    }

    private void initGUI() {
        frame = new JFrame("System Zarządzania Biblioteką");
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

        // Panel na tabelę
        panelMain = new JPanel(new BorderLayout());
        frame.add(panelMain, BorderLayout.CENTER);

        // Wyświetl GUI
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

        // Pobierz książki wypożyczone/zarezerwowane przez użytkownika
        List<Ksiazka> zarezerwowaneKsiazki = biblioteka.getZarezerwowaneKsiazki(uzytkownik.getNrKarty());
        List<Ksiazka> wypozyczoneKsiazki = biblioteka.getWypozyczoneKsiazki(uzytkownik.getNrKarty());
        List<Ksiazka> ksiazki = new ArrayList<>();
        ksiazki.addAll(zarezerwowaneKsiazki);
        ksiazki.addAll(wypozyczoneKsiazki);

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

    private void wykonajAkcje(String akcja, String tytul) {
        String nrKarty = uzytkownik.getNrKarty();
        boolean success = false;

        switch (akcja) {
            case "Rezerwuj":
                success = biblioteka.zarezerwujKsiazke(nrKarty, tytul);
                JOptionPane.showMessageDialog(frame, success ? "Książka została zarezerwowana!" : "Nie udało się zarezerwować książki.");
                break;
            case "Wypożycz":
                success = biblioteka.wypozyczKsiazke(nrKarty, tytul);
                JOptionPane.showMessageDialog(frame, success ? "Książka została wypożyczona!" : "Nie udało się wypożyczyć książki.");
                break;
            case "Oddaj":
                success = biblioteka.zwrocKsiazke(nrKarty, tytul);
                JOptionPane.showMessageDialog(frame, success ? "Książka została oddana!" : "Nie udało się oddać książki.");
                break;
            case "Anuluj":
                success = biblioteka.anulujRezerwacjeKsiazki(nrKarty, tytul);
                JOptionPane.showMessageDialog(frame, success ? "Rezerwacja została anulowana!" : "Nie udało się anulować rezerwacji.");
                break;
        }

        // Zapisz zmiany do pliku
        biblioteka.zapiszDoPliku("dane.json");
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
        private JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox, String defaultLabel) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = value == null ? "" : value.toString();
            button.setText(label);
            isPushed = true;

            // Odczyt tytułu książki w wierszu
            String tytul = table.getValueAt(row, 0).toString();

            // Wywołanie odpowiedniej akcji
            wykonajAkcje(label, tytul);

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}