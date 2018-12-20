package fr.ecn.dames;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
       JFrame fenetre = new JFrame();
        fenetre.setTitle("Jeu de dames");
        fenetre.setLocationRelativeTo(null);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new OverlayLayout(panel));
        Plateau plateau = new Plateau();
        plateau.initialisation();
        panel.add(plateau);

        fenetre.add(panel);
        fenetre.pack();

        fenetre.setVisible(true);
    }
}
