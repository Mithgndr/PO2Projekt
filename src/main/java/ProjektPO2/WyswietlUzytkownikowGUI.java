package ProjektPO2;

import ProjektPO2.Users.Uzytkownik;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class WyswietlUzytkownikowGUI {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = screenSize.width;
    private int screenHight = screenSize.height;

    void wyswietlUzytkownikowGUI(Biblioteka biblioteka) {
        JFrame ksiazkiFrame = new JFrame("Lista Użytkowników");
        Font font = new Font("Arial", Font.PLAIN, 20);
        Font font_bold = new Font("Arial", Font.BOLD, 20);
        ksiazkiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ksiazkiFrame.setSize(screenWidth / 2, screenHight / 2);
        ksiazkiFrame.setLocation(screenWidth / 4, screenHight / 4);

        // Definicje nagłówków tabeli
        String[] columnNames = {"Nr Karty", "Imię", "Nazwisko", "Zarezerwowane książki", "Wypożyczone książki"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Iteracja po użytkownikach i dodanie ich do modelu tabeli
        for (Uzytkownik u : biblioteka.getUzytkownicy()) {
            // Zbudowanie listy książek zarezerwowanych w formie nowych linii
            StringBuilder reservedBooksTitles = new StringBuilder();
            for (Ksiazka ksiazka : u.getZarezerwowaneKsiazki()) {
                if (reservedBooksTitles.length() > 0) {
                    reservedBooksTitles.append("\n");
                }
                reservedBooksTitles.append(ksiazka.getTytul());
            }

            // Zbudowanie listy książek wypożyczonych w formie nowych linii
            StringBuilder borrowedBooksTitles = new StringBuilder();
            for (Ksiazka ksiazka : u.getWypozyczoneKsiazki()) {
                if (borrowedBooksTitles.length() > 0) {
                    borrowedBooksTitles.append("\n");
                }
                borrowedBooksTitles.append(ksiazka.getTytul());
            }

            // Dodanie wiersza do modelu tabeli
            tableModel.addRow(new Object[]{
                    u.getNrKarty(),
                    u.getImie(),
                    u.getNazwisko(),
                    reservedBooksTitles.toString(),
                    borrowedBooksTitles.toString()
            });
        }

        // Tworzenie tabeli
        JTable table = new JTable(tableModel);
        table.setFont(font);
        table.getTableHeader().setFont(font_bold);

        // Ustal renderer dla pól, by umożliwić wieloliniowy tekst w komórkach
        table.getColumnModel().getColumn(3).setCellRenderer(new MultiLineCellRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new MultiLineCellRenderer());

        // Automatyczne dostosowywanie wysokości wierszy
        table.setRowHeight(30); // Wysokość domyślna (minimalna)
        for (int row = 0; row < table.getRowCount(); row++) {
            int rowHeight = table.getRowHeight();
            for (int column = 0; column < table.getColumnCount(); column++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }
            table.setRowHeight(row, rowHeight);
        }

        // Dodanie tabeli do scrollowanego panelu
        JScrollPane scrollPane = new JScrollPane(table);
        ksiazkiFrame.add(scrollPane);

        // Wyświetlenie okna
        ksiazkiFrame.setVisible(true);
    }

    // Customowy renderer do wyświetlenia wieloliniowych komórek tabeli
    public static class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {
        public MultiLineCellRenderer() {
            setLineWrap(false);
            setWrapStyleWord(false);
            setFont(new Font("Arial", Font.PLAIN, 16)); // Styl tekstu w komórce
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            setEditable(false); // Komórka nie może być edytowalna
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Dodatkowe odstępy
            return this;
        }
    }
}