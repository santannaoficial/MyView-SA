package myview;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * Autor: Matheus Amorim da Cruz & Pedro Sant'Anna
 */

/*
 * Classe criada para colocar cor amarela nas linhas de obras cadastradas que possuam o campo de favorito selecionado como "sim".
 */


public class CorFavorito2 extends DefaultTableCellRenderer {
    public CorFavorito2(){};

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        JLabel label = (JLabel)  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
        Color background = Color.WHITE;
        Object objeto = table.getValueAt(row, 3);
            try{
                String status = objeto.toString();
                if(status.equals("Sim")){
                    background = Color.YELLOW;
                }            
            }
            catch(Exception e){
                System.out.println(e);
            }
        label.setBackground(background);  
        return label;
    }
}