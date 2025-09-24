import java.util.ArrayList;

public class MinHeap {
    ArrayList<No> heap;

    public MinHeap() {
        heap = new ArrayList<>();
    }

    public int getSize() {
        return heap.size();
    }

    private void swap(int index1, int index2) {
        No temp = heap.get(index1);
        heap.set(index1, heap.get(index2));
        heap.set(index2, temp);
    }

    /**
     Insere um novo nó mantendo a estrutura do MinHeap */
    public void inserir(No i) {
        heap.add(i);

        // Pegar posição do nó a ser inserido e seu pai
        int indexInsercao = heap.size() - 1;
        int parentIndex = (indexInsercao - 1) / 2;

        // Loop para Manter a estrutura do MinHeap
        while (indexInsercao > 0 && heap.get(indexInsercao).compareTo(heap.get(parentIndex)) < 0) {
            swap(indexInsercao, parentIndex);

            indexInsercao = parentIndex;
            parentIndex = (indexInsercao - 1) / 2;
        }

    }


    /** Reorganiza o heap */
    private void siftDown(int index) {
        int menorIndex = index;

        while (true) {
            int esquerdaIndex = 2 * index + 1;
            int direitaIndex = 2 * index + 2;

            // Verifica se o filho da esquerda existe e se é menor que o nó atual.
            if (esquerdaIndex < heap.size() && heap.get(esquerdaIndex).compareTo(heap.get(menorIndex)) < 0) {
                menorIndex = esquerdaIndex;
            }

            // Verifica se o filho da direita existe e se é menor que o menor encontrado até agora.
            if (direitaIndex < heap.size() && heap.get(direitaIndex).compareTo(heap.get(menorIndex)) < 0) {
                menorIndex = direitaIndex;
            }

            // Se o menor índice ainda for o nó atual, a propriedade do heap está satisfeita.
            if (menorIndex == index) {
                break;
            } else {
                // Caso contrário, troca com o menor filho e continua o processo a partir da nova posição.
                swap(index, menorIndex);
                index = menorIndex;
            }
        }
    }

    /** Remove e retorna o nó de menor frequência */
    public No removeMenor() {
        if (heap.isEmpty()) {
            throw new RuntimeException("O heap está vazio.");
        }

        No noRemovido = heap.get(0);

        No ultimoElemento = heap.remove(heap.size() - 1);

        // Se o heap não ficou vazio após a remoção,
        // coloca o último elemento na raiz e reorganiza.
        if (!heap.isEmpty()) {
            heap.set(0, ultimoElemento);
            siftDown(0);
        }

        return noRemovido;
    }


}
