package fr.ecn.dames;

import org.junit.Test;

import static org.junit.Assert.*;

public class PionTest {

    @Test
    public void couleurPion() {
        boolean correct = true;

        Plateau plateau = new Plateau();
        for (int i = 0; i < plateau.TAILLE/2; i++) {
            for (int j = 0; j < plateau.TAILLE; j++) {
                if(plateau.getCase(i, j).containsPion()) {
                    correct = correct && plateau.getCase(i, j).getPion().getCouleur().equals(Couleur.NOIR);
                }
            }
        }

        for (int i = plateau.TAILLE/2; i < plateau.TAILLE; i++) {
            for (int j = 0; j < plateau.TAILLE; j++) {
                if(plateau.getCase(i, j).containsPion()) {
                    correct = correct && plateau.getCase(i, j).getPion().getCouleur().equals(Couleur.BLANC);
                }
            }
        }

        assertTrue(correct);
    }
}