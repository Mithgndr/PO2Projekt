package ProjektPO2;

import ProjektPO2.Users.Uzytkownik;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WyswietlKsiazkiGUI {
    private JPanel panel1;
    private Font font = new Font("Arial", Font.PLAIN, 12);
    private Font font_bold = new Font("Arial", Font.BOLD, 12);
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = screenSize.width;
    private int screenHight = screenSize.height;

    void wyswietlKsiazkiGUI(Biblioteka biblioteka, Uzytkownik uzytkownik) {
        JFrame ksiazkiFrame = new JFrame("Lista Książek");
        ksiazkiFrame.setSize(screenWidth / 2, screenHight / 2);
        ksiazkiFrame.setLocation(screenWidth / 2 - screenWidth / 4, screenHight / 2 - screenHight / 4);

        String[] columnNames = {"Tytuł", "Autor", "Kategoria", "Dostępność", "Rezerwuj", "Wypożycz", "Oddaj", "Anuluj Rezerwację"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Ksiazka k : biblioteka.getKsiazki()) {
            String dostepnosc = k.getCzydostepna() ? "Dostępna" : "Niedostępna";
            tableModel.addRow(new Object[]{k.getTytul(), k.getAutor(), k.getKategoria(), dostepnosc, "Rezerwuj", "Wypożycz", "Oddaj", "Anuluj Rezerwację"});
        }

        JTable table = new JTable(tableModel);
        table.setFont(font);
        table.getTableHeader().setFont(font_bold);
        table.setRowHeight(30);

        // Dodanie przycisków do kolumn
        addButtonToTableColumn(table, "Rezerwuj", (row) -> {
            Ksiazka k = biblioteka.getKsiazki().get(row);
            if (k.getCzyZarezerwowana()) {
                JOptionPane.showMessageDialog(ksiazkiFrame, "Książka jest już zarezerwowana!", "Błąd", JOptionPane.ERROR_MESSAGE);
            } else {
                k.setCzyZarezerwowana(true);
                JOptionPane.showMessageDialog(ksiazkiFrame, "Książka została zarezerwowana.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                biblioteka.zarezerwujKsiazke(uzytkownik.getNrKarty(), k.getTytul());
                biblioteka.zapiszDoPliku("dane.json");
            }
        });

        addButtonToTableColumn(table, "Wypożycz", (row) -> {
            Ksiazka k = biblioteka.getKsiazki().get(row);
            if (!k.getCzydostepna()) {
                JOptionPane.showMessageDialog(ksiazkiFrame, "Książka jest już wypożyczona!", "Błąd", JOptionPane.ERROR_MESSAGE);
            } else {
                k.setCzyDostepna(false);
                JOptionPane.showMessageDialog(ksiazkiFrame, "Książka została wypożyczona.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                table.getModel().setValueAt("Niedostępna", row, 3); // Aktualizacja kolumny "Dostępność"
                biblioteka.wypozyczKsiazke(uzytkownik.getNrKarty(), k.getTytul());
                biblioteka.zapiszDoPliku("dane.json");
            }
        });

        addButtonToTableColumn(table, "Oddaj", (row) -> {
            Ksiazka k = biblioteka.getKsiazki().get(row);
            if (k.getCzydostepna()) {
                JOptionPane.showMessageDialog(ksiazkiFrame, "Książka nie została wypożyczona!", "Błąd", JOptionPane.ERROR_MESSAGE);
            } else {
                k.setCzyDostepna(true);
                JOptionPane.showMessageDialog(ksiazkiFrame, "Książka została oddana.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                table.getModel().setValueAt("Dostępna", row, 3); // Aktualizacja kolumny "Dostępność"
                biblioteka.zwrocKsiazke(uzytkownik.getNrKarty(), k.getTytul());
                biblioteka.zapiszDoPliku("dane.json");
            }
        });

        addButtonToTableColumn(table, "Anuluj Rezerwację", (row) -> {
            Ksiazka k = biblioteka.getKsiazki().get(row);
            if (!k.getCzyZarezerwowana()) {
                JOptionPane.showMessageDialog(ksiazkiFrame, "Książka nie jest zarezerwowana!", "Błąd", JOptionPane.ERROR_MESSAGE);
            } else {
                k.setCzyZarezerwowana(false);
                JOptionPane.showMessageDialog(ksiazkiFrame, "Rezerwacja książki została anulowana.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                biblioteka.anulujRezerwacjeKsiazki(uzytkownik.getNrKarty(), k.getTytul());
                biblioteka.zapiszDoPliku("dane.json");
            }
        });

        // Dodanie tabeli do JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        ksiazkiFrame.add(scrollPane);
        ksiazkiFrame.setVisible(true);
    }

    // Metoda do dodania przycisków w kolumnie tabeli
    private void addButtonToTableColumn(JTable table, String columnName, RowAction action) {
        TableColumn column = table.getColumn(columnName);
        column.setCellRenderer(new ButtonRenderer());
        column.setCellEditor(new ButtonEditor(new JCheckBox(), action));
    }

    private interface RowAction {
        void execute(int row);
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Edytor przycisku
    private class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private final RowAction action;
        private int clickedRow;

        public ButtonEditor(JCheckBox checkBox, RowAction action) {
            super(checkBox);
            this.button = new JButton();
            this.button.setOpaque(true);
            this.action = action;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    action.execute(clickedRow); // Wykonanie akcji na wierszu
                    fireEditingStopped();       // Zatrzymanie edytowania
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            clickedRow = row;
            button.setText((value == null) ? "" : value.toString());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }
}