package ProjektPO2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class WyswietlKsiazkiGUI {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = screenSize.width;
    private int screenHight = screenSize.height;
    private JPanel panel1;

    void wyswietlKsiazkiGUI(Biblioteka biblioteka) {
        JFrame ksiazkiFrame = new JFrame("Lista Książek");
        ksiazkiFrame.setSize(600, 400);
        ksiazkiFrame.setLocation(screenWidth/2-280, screenHight/2-180);

        String[] columnNames = {"Tytuł", "Autor", "Kategoria", "Dostępność"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Ksiazka k : biblioteka.getKsiazki()) {
            String dostepnosc = k.getCzydostepna() ? "Dostępna" : "Niedostępna";
            tableModel.addRow(new Object[]{k.getTytul(), k.getAutor(), k.getKategoria(), dostepnosc});
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        ksiazkiFrame.add(scrollPane);
        ksiazkiFrame.setVisible(true);
    }

}