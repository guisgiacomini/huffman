// Alice de Oliveira Duarte - 10419323
// Pedro Roberto Fernandes Noronha - 10443434
// Guilherme Silveira Giacomini - 10435311
// Carlos Eduardo Diniz de Almeida - 10444407

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

    public No(char caractere, int frequencia, No esquerda, No direita) {
        this.caractere = caractere; 
        this.frequencia = frequencia;
        this.esquerda = esquerda;
        this.direita = direita;
    }

    public boolean ehFolha() {
        return esquerda == null && direita == null;
    }

    @Override
    public int compareTo(No outroNo) {
        return this.frequencia - outroNo.frequencia;
    }
}
