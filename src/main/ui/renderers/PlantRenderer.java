package ui.renderers;

import model.Plant;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import java.awt.Component;

public class PlantRenderer extends DefaultListCellRenderer {
    public PlantRenderer(){   }

    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value,index,isSelected,cellHasFocus);
        Plant p = (Plant)value;
        setText(p.getName());
        return this;
    }
}
