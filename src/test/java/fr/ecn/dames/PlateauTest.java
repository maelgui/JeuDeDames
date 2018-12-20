package fr.ecn.dames;

import org.junit.Test;

import java.awt.event.MouseEvent;

import static java.awt.event.MouseEvent.MOUSE_PRESSED;
import static org.junit.Assert.*;

public class PlateauTest {

    @Test
    public void initialisation() {
        Plateau plateau = new Plateau();
        plateau.initialisation();
        boolean grilleCorrect = true;

        for (int i = 0; i < 3; i++) {
            for (int j = i%2; j < 10; j+=2) {
                grilleCorrect = grilleCorrect &&
                        (plateau.getCase(i, j).containsPion() && plateau.getCase(i, j).getPion().getCouleur() == Couleur.NOIR) &&
                        (plateau.getCase(plateau.TAILLE-i-1, plateau.TAILLE-j-1).containsPion() && plateau.getCase(plateau.TAILLE-i-1, plateau.TAILLE-j-1).getPion().getCouleur() == Couleur.BLANC);
            }
        }

        assertTrue(grilleCorrect);
        assertEquals(Couleur.NOIR, plateau.getTour());
    }

    /**
     * Test de déplacement de pion blanc possible dans une grille sans obstruction
     */
    @Test
    public void getPossibilitesDeplacerPion1() {
        Plateau plateau = new Plateau();

        Pion pion = plateau.newPion(Couleur.BLANC);
        plateau.addPion(5,5, pion);

        Pion pionGauche = plateau.newPion(Couleur.BLANC);
        plateau.addPion(4,0, pionGauche);

        Pion pionDroite = plateau.newPion(Couleur.BLANC);
        plateau.addPion(5,9, pionDroite);

        assertFalse(plateau.getPossibilitesDeplacerPion(pion).isEmpty());
        assertFalse(plateau.getPossibilitesDeplacerPion(pionGauche).isEmpty());
        assertFalse(plateau.getPossibilitesDeplacerPion(pionDroite).isEmpty());
    }

    /**
     * Test de déplacement de pion blanc impossible dans une grille sans obstruction
     */
    @Test
    public void getPossibilitesDeplacerPion2() {
        Plateau plateau = new Plateau();

        Pion pionHaut = plateau.newPion(Couleur.BLANC);
        plateau.addPion(0,1, pionHaut);

        assertTrue(plateau.getPossibilitesDeplacerPion(pionHaut).isEmpty());
    }

    /**
     * Test de déplacement de pion blanc impossible dans une grille avec obstruction
     */
    @Test
    public void getPossibilitesDeplacerPion3() {
        Plateau plateau = new Plateau();

        Pion pion = plateau.newPion(Couleur.BLANC);
        plateau.addPion(5,5, pion);
        plateau.addPion(4,4, plateau.newPion(Couleur.BLANC));
        plateau.addPion(4,6, plateau.newPion(Couleur.BLANC));

        Pion pionGauche = plateau.newPion(Couleur.BLANC);
        plateau.addPion(6,0, pionGauche);
        plateau.addPion(5,1, plateau.newPion(Couleur.BLANC));

        Pion pionDroite = plateau.newPion(Couleur.BLANC);
        plateau.addPion(5,9, pionDroite);
        plateau.addPion(4,8, plateau.newPion(Couleur.BLANC));

        assertTrue(plateau.getPossibilitesDeplacerPion(pion).isEmpty());
        assertTrue(plateau.getPossibilitesDeplacerPion(pionGauche).isEmpty());
        assertTrue(plateau.getPossibilitesDeplacerPion(pionDroite).isEmpty());
    }

    /**
     * Test de déplacement de pion NOIR possible dans une grille sans obstruction
     */
    @Test
    public void getPossibilitesDeplacerPion4() {
        Plateau plateau = new Plateau();
        plateau.changerTour();

        Pion pion = plateau.newPion(Couleur.NOIR);
        plateau.addPion(5,5, pion);

        Pion pionGauche = plateau.newPion(Couleur.NOIR);
        plateau.addPion(4,0, pionGauche);

        Pion pionDroite = plateau.newPion(Couleur.NOIR);
        plateau.addPion(5,9, pionDroite);

        assertFalse(plateau.getPossibilitesDeplacerPion(pion).isEmpty());
        assertFalse(plateau.getPossibilitesDeplacerPion(pionGauche).isEmpty());
        assertFalse(plateau.getPossibilitesDeplacerPion(pionDroite).isEmpty());
    }

    /**
     * Test de déplacement de pion NOIR impossible dans une grille sans obstruction
     */
    @Test
    public void getPossibilitesDeplacerPion5() {
        Plateau plateau = new Plateau();
        plateau.changerTour();

        Pion pionHaut = plateau.newPion(Couleur.NOIR);
        plateau.addPion(9,1, pionHaut);

        assertTrue(plateau.getPossibilitesDeplacerPion(pionHaut).isEmpty());
    }

    /**
     * Test de déplacement de pion NOIR impossible dans une grille avec obstruction
     */
    @Test
    public void getPossibilitesDeplacerPion6() {
        Plateau plateau = new Plateau();
        plateau.changerTour();

        Pion pion = plateau.newPion(Couleur.NOIR);
        plateau.addPion(3,5, pion);
        plateau.addPion(4,4, plateau.newPion(Couleur.NOIR));
        plateau.addPion(4,6, plateau.newPion(Couleur.NOIR));

        Pion pionGauche = plateau.newPion(Couleur.NOIR);
        plateau.addPion(4,0, pionGauche);
        plateau.addPion(5,1, plateau.newPion(Couleur.NOIR));

        Pion pionDroite = plateau.newPion(Couleur.NOIR);
        plateau.addPion(3,9, pionDroite);
        plateau.addPion(4,8, plateau.newPion(Couleur.NOIR));

        assertTrue(plateau.getPossibilitesDeplacerPion(pion).isEmpty());
        assertTrue(plateau.getPossibilitesDeplacerPion(pionGauche).isEmpty());
        assertTrue(plateau.getPossibilitesDeplacerPion(pionDroite).isEmpty());
    }

    // BLANC peut manger 2 NOIR
    @Test
    public void getPossibilitesMangerPion1() {
        Plateau plateau = new Plateau();

        Pion pion = plateau.newPion(Couleur.BLANC);
        plateau.addPion(5,5, pion);

        plateau.addPion(4,4, plateau.newPion(Couleur.NOIR));
        plateau.addPion(4,6, plateau.newPion(Couleur.NOIR));

        assertEquals(2, plateau.getPossibilitesMangerPion(pion).size());
    }

    // NOIR peut manger 2 BLANC
    @Test
    public void getPossibilitesMangerPion2() {
        Plateau plateau = new Plateau();

        plateau.changerTour();

        Pion pion = plateau.newPion(Couleur.NOIR);
        plateau.addPion(5,5, pion);

        plateau.addPion(4,4, plateau.newPion(Couleur.BLANC));
        plateau.addPion(4,6, plateau.newPion(Couleur.BLANC));

        assertEquals(2, plateau.getPossibilitesMangerPion(pion).size());
    }

    // Dans les coins
    @Test
    public void getPossibilitesMangerPion3() {
        Plateau plateau = new Plateau();

        Pion pion1 = plateau.newPion(Couleur.BLANC);
        plateau.addPion(1,0, pion1);

        Pion pion2 = plateau.newPion(Couleur.BLANC);
        plateau.addPion(0,9, pion2);

        Pion pion3 = plateau.newPion(Couleur.BLANC);
        plateau.addPion(9,8, pion3);

        Pion pion4 = plateau.newPion(Couleur.BLANC);
        plateau.addPion(9,0, pion4);

        assertTrue(plateau.getPossibilitesMangerPion(pion1).isEmpty());
        assertTrue(plateau.getPossibilitesMangerPion(pion2).isEmpty());
        assertTrue(plateau.getPossibilitesMangerPion(pion3).isEmpty());
        assertTrue(plateau.getPossibilitesMangerPion(pion4).isEmpty());

    }

    @Test
    public void creerDame() {
        Plateau plateau = new Plateau();

        Pion pion = plateau.newPion(Couleur.NOIR);
        plateau.addPion(8,1, pion);

        plateau.changerTour();

        MouseEvent event = new MouseEvent(pion,45, 0, MOUSE_PRESSED, 0, 60, 1, false);
        plateau.mouseClicked(event);

        event = new MouseEvent(plateau.getCase(9, 0),45, 0, MOUSE_PRESSED, 0, 60, 1, false);
        plateau.mouseClicked(event);

        assertTrue(pion.isDame());
    }


    @Test
    public void changerTour() {
        Plateau plateau = new Plateau();

        plateau.addPion(4,4, plateau.newPion(Couleur.NOIR));
        plateau.changerTour();

        assertEquals(Couleur.NOIR, plateau.getTour());
        assertEquals(Etat.DEPLACER, plateau.getEtat());

        plateau.addPion(3,3, plateau.newPion(Couleur.BLANC));
        plateau.changerTour();

        assertEquals(Couleur.BLANC, plateau.getTour());
        assertEquals(Etat.MANGER, plateau.getEtat());
    }

    /**
     * Test des évènements pour l'action déplacer
     */
    @Test
    public void mouseClicked1() {
        Plateau plateau = new Plateau();

        Pion pion = plateau.newPion(Couleur.NOIR);
        plateau.addPion(5,5, pion);

        plateau.changerTour();

        MouseEvent event = new MouseEvent(pion,45, 0, MOUSE_PRESSED, 0, 60, 1, false);
        plateau.mouseClicked(event);

        assertEquals(2, plateau.getCasePossible().size());

        event = new MouseEvent(plateau.getCase(6,4),45, 0, MOUSE_PRESSED, 0, 60, 1, false);
        plateau.mouseClicked(event);

        assertTrue(plateau.getCase(6,4).containsPion());
        assertFalse(plateau.getCase(5, 5).containsPion());
        assertEquals(0, plateau.getCasePossible().size());
        assertEquals(pion, plateau.getCase(6,4).getPion());
    }

    /**
     * Test des évènements pour l'action manger
     */
    @Test
    public void mouseClicked2() {
        Plateau plateau = new Plateau();

        Pion pion = plateau.newPion(Couleur.NOIR);
        plateau.addPion(5,5, pion);
        plateau.addPion(4,4, plateau.newPion(Couleur.BLANC));
        plateau.addPion(4,2, plateau.newPion(Couleur.BLANC));

        plateau.changerTour();

        // On selectionne le pion qui doit manger
        MouseEvent event = new MouseEvent(pion,45, 0, MOUSE_PRESSED, 0, 60, 1, false);
        plateau.mouseClicked(event);

        // On selectionne la case d'arrivé
        event = new MouseEvent(plateau.getCase(3,3),45, 0, MOUSE_PRESSED, 0, 60, 1, false);
        plateau.mouseClicked(event);


        assertTrue(plateau.getCase(3,3).containsPion());
        assertFalse(plateau.getCase(4, 4).containsPion());
        assertFalse(plateau.getCase(5, 5).containsPion());
        assertEquals(1, plateau.getCasePossible().size());

        // On selectionne la deuxieme case d'arrivé
        event = new MouseEvent(plateau.getCase(5,1),45, 0, MOUSE_PRESSED, 0, 60, 1, false);
        plateau.mouseClicked(event);

        assertTrue(plateau.getCase(5,1).containsPion());
        assertFalse(plateau.getCase(3, 3).containsPion());
        assertFalse(plateau.getCase(4, 2).containsPion());
        assertEquals(0, plateau.getCasePossible().size());
    }

    @Test
    public void afficherPossibilitesDeplacer() {
        Plateau plateau = new Plateau();

        Pion dame = plateau.newPion(Couleur.BLANC);
        dame.setDame(true);
        plateau.addPion(5, 5, dame);
        plateau.addPion(1, 9, plateau.newPion(Couleur.BLANC));

        plateau.setPionActif(dame);

        plateau.afficherPossibilitesDeplacer();

        assertEquals(16, plateau.getCasePossible().size());
    }

    @Test
    public void afficherPossibilitesManger() {
        Plateau plateau = new Plateau();

        Pion dame = plateau.newPion(Couleur.BLANC);
        dame.setDame(true);
        plateau.addPion(1, 1, dame);
        plateau.addPion(5, 5, plateau.newPion(Couleur.NOIR));
        plateau.addPion(9, 9, plateau.newPion(Couleur.NOIR));

        plateau.setPionActif(dame);

        plateau.afficherPossibilitesManger();

        assertEquals(3, plateau.getCasePossible().size());
    }

    @Test
    public void mouseEvents() {
        Plateau plateau = new Plateau();

        MouseEvent event = new MouseEvent(plateau.getCase(5, 5),45, 0, MOUSE_PRESSED, 0, 60, 1, false);

        plateau.mouseEntered(event);
        plateau.mouseExited(event);
        plateau.mousePressed(event);
        plateau.mouseReleased(event);
    }

}