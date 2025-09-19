public class No implements Comparable<No> {
    char caractere;
    int frequencia;
    No esquerda, direita;

    public No(char caractere, int frequencia) {
        this.caractere = caractere;
        this.frequencia = frequencia;
        this.esquerda = null;
        this.direita = null;
    }

    @Override
    public int compareTo(No outroNo) {
        return this.frequencia - outroNo.frequencia;
    }
}
