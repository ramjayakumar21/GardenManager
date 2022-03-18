package ui;

import model.PlantBed;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import java.awt.Component;

public class PlantBedRenderer extends DefaultListCellRenderer {
    public PlantBedRenderer(){   }

    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value,index,isSelected,cellHasFocus);
        PlantBed pb = (PlantBed)value;
        setText(pb.getName());
        return this;
    }
}
