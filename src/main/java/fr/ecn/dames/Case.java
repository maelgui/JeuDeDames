package fr.ecn.dames;

import javax.swing.*;
import java.awt.*;

import static java.lang.Math.abs;

public class Case extends JPanel {

    private int i;
    private int j;
    private final Couleur couleur;

    private Pion pion;

    public Case(int i, int j, Couleur couleur) {
        this.i = i;
        this.j = j;
        this.couleur = couleur;
        this.pion = null;

        if(couleur == Couleur.NOIR) {
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        }

        setLayout(new GridLayout(1,0));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (couleur == Couleur.BLANC) {
            setBackground(Color.WHITE);

        } else if (couleur == Couleur.NOIR) {
            setBackground(Color.BLACK);
        }
    }

    public boolean containsPion() {
        return pion != null;
    }

    public void setPion(Pion pion) {
        if(pion != null) {
            this.add(pion);
        }
        else {
            this.remove(this.pion);
        }
        this.pion = pion;
    }

    public Pion getPion() {
        return pion;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public int distance(Case case1) {
        return abs(case1.getI()-this.getI());
    }
}
