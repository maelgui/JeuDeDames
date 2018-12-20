package fr.ecn.dames;

public enum Direction {
    NORTHEAST(-1, +1, Couleur.BLANC),
    SOUTHEAST(+1, +1, Couleur.NOIR),
    SOUTHWEST(+1, -1, Couleur.NOIR),
    NORTHWEST(-1, -1, Couleur.BLANC);

    private final int i;
    private final int j;
    private final Couleur couleur;

    Direction(int i, int j, Couleur couleur) {
        this.i = i;
        this.j = j;
        this.couleur = couleur;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public Couleur getCouleur() {
        return couleur;
    }


}
