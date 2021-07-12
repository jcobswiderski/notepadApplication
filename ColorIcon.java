package exc12;

import javax.swing.*;
import java.awt.*;

public class ColorIcon implements Icon {

    private Color color;
    int x, y;

    public ColorIcon(Color color) {
        this.color = color;
        this.x = 20;
        this.y = 4;
    }

    public ColorIcon(Color color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        g.fillOval(this.x, this.y, 10, 10);
    }

    @Override
    public int getIconWidth() {
        return 10;
    }

    @Override
    public int getIconHeight() {
        return 10;
    }
}
