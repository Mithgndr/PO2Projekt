package ProjektPO2;

import ProjektPO2.Users.Uzytkownik;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CzytelnikGUI {
    private JPanel panel1;
    private JFrame frame;
    private JTable table;
    private Biblioteka biblioteka;
    private Uzytkownik uzytkownik;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = screenSize.width;
    private int screenHight = screenSize.height;
    private Font font = new Font("Arial", Font.PLAIN, 12);
    private Font font_bold = new Font("Arial", Font.BOLD, 12);

    public CzytelnikGUI(Uzytkownik uzytkownik) {
        this.uzytkownik = uzytkownik;
        this.biblioteka = new Biblioteka();

        // Wczytaj dane z pliku (np. JSON)
        biblioteka.wczytajZPliku("dane.json");

        // Inicjalizacja GUI
        initGUI();
    }

    private void initGUI() {
        frame = new JFrame("System Zarządzania Biblioteką - Czytelnik");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenWidth / 2, screenHight / 2 );
        frame.setLocation(screenWidth / 4, screenHight / 4);
        frame.setLayout(new BorderLayout());

        // Pobierz listę książek z biblioteki
        List<Ksiazka> ksiazki = biblioteka.getKsiazki();

        // Kolumny tabeli
        String[] columnNames = {"Tytuł", "Autor", "Kategoria", "Dostępność", "Rezerwuj", "Wypożycz", "Oddaj", "Anuluj Rezerwację"};
        DefaultTableModel tableModel = new DefaultTableModel(0, columnNames.length) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Komórki z przyciskami mogą być edytowalne
                return column >= 3;
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
                    "Wypożycz",
                    "Oddaj",
                    "Anuluj"
            };
            tableModel.addRow(rowData);
        }

        // Tworzenie tabeli
        table = new JTable(tableModel);
        table.setFont(font);
        table.getTableHeader().setFont(font_bold);

        // Dodanie renderera/przycisków do kolumn akcji
        table.getColumn("Rezerwuj").setCellRenderer(new ButtonRenderer());
        table.getColumn("Rezerwuj").setCellEditor(new ButtonEditor(new JCheckBox(), "Rezerwuj"));

        table.getColumn("Wypożycz").setCellRenderer(new ButtonRenderer());
        table.getColumn("Wypożycz").setCellEditor(new ButtonEditor(new JCheckBox(), "Wypożycz"));

        table.getColumn("Oddaj").setCellRenderer(new ButtonRenderer());
        table.getColumn("Oddaj").setCellEditor(new ButtonEditor(new JCheckBox(), "Oddaj"));

        table.getColumn("Anuluj Rezerwację").setCellRenderer(new ButtonRenderer());
        table.getColumn("Anuluj Rezerwację").setCellEditor(new ButtonEditor(new JCheckBox(), "Anuluj"));

        // Scrollowalny panel dla tabeli
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Wyświetlenie GUI
        frame.setVisible(true);
    }

    // Metoda obsługująca konkretne akcje
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

        // Zawsze zapisujemy zmiany po akcji
        biblioteka.zapiszDoPliku("dane.json");
    }




    // Renderer przycisków w tabeli
    public class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }

    // Edytor przycisków w tabeli
    public class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox, String defaultLabel) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
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