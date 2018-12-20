package fr.ecn.dames;

import javax.swing.*;
import java.awt.*;

public class Pion extends JPanel {
    private Couleur couleur;

    private boolean dame;

    private final transient Image img = Toolkit.getDefaultToolkit().getImage("crown.png");

    public Pion(Couleur couleur) {
        this.couleur = couleur;
        this.dame = false;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (couleur == Couleur.BLANC) {
            g.setColor(Color.LIGHT_GRAY);

        } else if (couleur == Couleur.NOIR) {
            g.setColor(Color.DARK_GRAY);

        }

        g.fillOval(5, 5, getWidth()-10, getHeight()-10);
        if(dame) {
            g.drawImage(img, 10, 10, getWidth() - 20, getHeight() - 20, this);
        }
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public boolean isDame() {
        return dame;
    }

    public void setDame(boolean dame) {
        this.dame = dame;
    }
}
