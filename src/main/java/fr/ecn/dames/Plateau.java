package fr.ecn.dames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.abs;

public class Plateau extends JPanel implements MouseListener {

    public static final int TAILLE = 10;

    private Pion pionActif;
    private List<Case> casePossible;

    private Couleur tour;

    private Etat etat;

    public Plateau() {
        this.pionActif = null;
        this.casePossible = new LinkedList<>();
        this.tour = Couleur.BLANC;
        this.setPreferredSize(new Dimension(500, 500));
        this.setLayout(new GridLayout(TAILLE, TAILLE));

        //Construction du plateau
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                if((j+i) % 2 == 1)
                    this.add(newCase(i, j, Couleur.BLANC));
                else
                    this.add(newCase(i, j, Couleur.NOIR));
            }
        }
    }

    public void initialisation() {
        //Ajout des pions
        for (int i = 0; i < 3; i++) {
            for (int j = i%2; j < 10; j+=2) {
                getCase(i, j).setPion(newPion(Couleur.NOIR));
                getCase(TAILLE-i-1, TAILLE-j-1).setPion(newPion(Couleur.BLANC));
            }
        }

        changerTour();
    }

    public Pion newPion(Couleur couleur) {
        Pion pion = new Pion(couleur);
        pion.addMouseListener(this);
        return pion;
    }

    public void addPion(int i, int j, Pion pion) {
        getCase(i, j).setPion(pion);
    }

    private Case newCase(int i, int j, Couleur couleur) {
        Case case1 = new Case(i, j, couleur);
        case1.addMouseListener(this);
        return case1;
    }

    public Case getCase(int i, int j) {
        return (Case) getComponent(i*TAILLE+j);
    }

    private void afficherPionMangeur() {
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                Pion pion = getCase(i, j).getPion();
                if(pion != null && pion.getCouleur() == tour && (!getPossibilitesMangerPion(pion).isEmpty() || !getPossibilitesMangerDame(pion).isEmpty())) {
                    this.casePossible.add(getCase(i, j));
                    getCase(i, j).setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
                }
            }

        }
    }

    public void afficherPionADeplacer() {
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                Pion pion = getCase(i, j).getPion();
                if(pion != null && pion.getCouleur() == tour && (!getPossibilitesDeplacerPion(pion).isEmpty() || !getPossibilitesDeplacerDame(pion).isEmpty())) {
                    this.casePossible.add(getCase(i, j));
                    getCase(i, j).setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
                }
            }

        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getSource() instanceof Pion && casePossible.contains(((Pion) mouseEvent.getSource()).getParent())) {
            pionActif = (Pion) mouseEvent.getSource();
            clearPossibilites();
            ((Case) pionActif.getParent()).setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
            if(etat == Etat.DEPLACER) {
                afficherPossibilitesDeplacer();
            }
            else { // Etat.MANGER
                afficherPossibilitesManger();
            }
        }
        else if(mouseEvent.getSource() instanceof Case && casePossible.contains(mouseEvent.getSource())) {
            deplacer((Case) mouseEvent.getSource());
            if(etat == Etat.MANGER) {
                afficherPossibilitesManger();
            }
            if(casePossible.isEmpty()) {
                changerTour();
            }
        }
    }

    public void changerTour() {
        if (tour == Couleur.BLANC) {
            tour = Couleur.NOIR;
        }
        else {
            tour = Couleur.BLANC;
        }

        // On affiche les pions qui peuvent manger
        afficherPionMangeur();
        this.etat = Etat.MANGER;

        // Si on ne peut pas manger d'autres pions
        if(casePossible.isEmpty()) {
            afficherPionADeplacer();
            this.etat = Etat.DEPLACER;
        }
    }

    private void deplacer(Case target) {
        Case caseActive = (Case) pionActif.getParent();

        // Si on était en train de manger
        if(etat == Etat.MANGER) {
            Case case1 = getCaseAManger(target);
            if (case1 != null) {
                case1.setPion(null);
            }
        }

        // Si on a fait une dame
        if(((target.getI() == 9 && pionActif.getCouleur() == Couleur.NOIR) || (target.getI() == 0 && pionActif.getCouleur() == Couleur.BLANC)) && !pionActif.isDame()) {
            pionActif.setDame(true);
            etat = Etat.TRANSFORMATION;
        }

        // Déplacement
        target.setPion(pionActif);
        caseActive.setPion(null);

        // On efface tout
        clearPossibilites();
        caseActive.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        this.validate();
        this.repaint();
    }

    private Case getCaseAManger(Case destination) {
        Case caseActive = (Case) pionActif.getParent();

        int dirI = (destination.getI() - caseActive.getI())/caseActive.distance(destination);
        int dirJ = (destination.getJ() - caseActive.getJ())/caseActive.distance(destination);

        for (int k = 1; k < caseActive.distance(destination); k++) {
            if(getCase(caseActive.getI()+k*dirI, caseActive.getJ()+k*dirJ).containsPion()) {
                return getCase(caseActive.getI()+k*dirI, caseActive.getJ()+k*dirJ);
            }
        }

        return null;
    }

    private void clearPossibilites() {
        for (Case case1 : casePossible) {
            case1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        }
        casePossible.clear();
    }

    public List<Case> getPossibilitesMangerDame(Pion pion) {
        LinkedList<Case> casesPossibles = new LinkedList<>();

        if(pion.isDame()) {
            for (Direction direction : Direction.values()) {
                int i = ((Case) pion.getParent()).getI() + direction.getI();
                int j = ((Case) pion.getParent()).getJ() + direction.getJ();

                boolean aManger = false;

                while (i < TAILLE && j < TAILLE && i >= 0 && j >= 0) {
                    if(!aManger && getCase(i,j).containsPion() && getCase(i,j).getPion().getCouleur() != pion.getCouleur()) {
                        aManger = true;
                    }
                    else if(getCase(i,j).containsPion()) {
                        break;
                    }
                    else if(aManger) {
                        casesPossibles.add(getCase(i,j));
                    }

                    i += direction.getI();
                    j += direction.getJ();
                }
            }
        }

        return casesPossibles;
    }

    public List<Case> getPossibilitesMangerPion(Pion pion) {
        LinkedList<Case> casesPossibles = new LinkedList<>();

        int i = ((Case) pion.getParent()).getI();
        int j = ((Case) pion.getParent()).getJ();

        for (Direction d : Direction.values()) {
            if (i + 2*d.getI() < 10 && i + 2*d.getI() >= 0 &&
                    j + 2*d.getJ() < 10 && j + 2*d.getJ() >= 0 &&
                    getCase(i + d.getI(), j + d.getJ()).containsPion() && !getCase(i + 2*d.getI(), j + 2*d.getJ()).containsPion() &&
                    getCase(i + d.getI(), j + d.getJ()).getPion().getCouleur() != pion.getCouleur()) {

                casesPossibles.add(getCase(i + 2*d.getI(), j + 2*d.getJ()));
            }
        }

        return casesPossibles;
    }

    public void afficherPossibilitesManger() {
        if(pionActif.isDame()) {
            casePossible = getPossibilitesMangerDame(pionActif);
        }
        else {
            casePossible = getPossibilitesMangerPion(pionActif);
        }

        afficherPossibilites(Color.CYAN);
    }

    private void afficherPossibilites(Color color) {
        for (Case case1 : casePossible) {
            case1.setBorder(BorderFactory.createLineBorder(color, 2));
        }
    }

    public void afficherPossibilitesDeplacer() {
        if(pionActif.isDame()) {
            casePossible = getPossibilitesDeplacerDame(pionActif);
        }
        else {
            casePossible = getPossibilitesDeplacerPion(pionActif);
        }
        afficherPossibilites(Color.CYAN);
    }

    public List<Case> getPossibilitesDeplacerPion(Pion pion) {
        LinkedList<Case> casesPossibles = new LinkedList<>();

        int i = ((Case) pion.getParent()).getI();
        int j = ((Case) pion.getParent()).getJ();

        for (Direction d : Direction.values()) {
            if (i + d.getI() < 10 && i + d.getI() >= 0 && j + d.getJ() < 10 && j + d.getJ() >= 0 && !getCase(i + d.getI(), j + d.getJ()).containsPion() && d.getCouleur() == pion.getCouleur()) {
                casesPossibles.add(getCase(i + d.getI(), j + d.getJ()));
            }
        }

        return casesPossibles;
    }

    public List<Case> getPossibilitesDeplacerDame(Pion pion) {
        LinkedList<Case> casesPossibles = new LinkedList<>();

        if(pion.isDame()) {
            for (Direction direction : Direction.values()) {
                int i = ((Case) pion.getParent()).getI() + direction.getI();
                int j = ((Case) pion.getParent()).getJ() + direction.getJ();

                while (i < TAILLE && j < TAILLE && i >= 0 && j >= 0) {
                    if (getCase(i, j).containsPion()) {
                        break;
                    }
                    casesPossibles.add(getCase(i, j));

                    i += direction.getI();
                    j += direction.getJ();
                }
            }
        }

        return casesPossibles;
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        // Do nothing because implement in mouseClicked event
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        // Do nothing because implement in mouseClicked event
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        // Do nothing because implement in mouseClicked event
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        // Do nothing because implement in mouseClicked event
    }

    public void setPionActif(Pion pionActif) {
        this.pionActif = pionActif;
    }

    public List<Case> getCasePossible() {
        return casePossible;
    }

    public Couleur getTour() {
        return tour;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }
}
