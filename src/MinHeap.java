import java.util.ArrayList;

// Alice de Oliveira Duarte - 10419323
// Pedro Roberto Fernandes Noronha - 10443434
// Guilherme Silveira Giacomini - 10435311
// Carlos Eduardo Diniz de Almeida -

// Min-heap usando ArrayList - menor element fica na root (index 0)
public class MinHeap {
    private final ArrayList<No> heap;

    // Cria heap vazio
    public MinHeap() {
        this.heap = new ArrayList<>();
    }

    // Insere node no heap
    public void insert(No node) {
        heap.add(node);
        siftUp(heap.size() - 1);
    }

    // Remove e retorna o min (root)
    public No removeMin() {
        if (heap.isEmpty()) {
            throw new IllegalStateException("Heap vazio");
        }
        No min = heap.get(0);
        No last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            siftDown(0);
        }
        return min;
    }

    // Retorna size do heap
    public int size() {
        return heap.size();
    }

    // Move element up para manter heap property
    private void siftUp(int idx) {
        while (idx > 0) {
            int pai = (idx - 1) / 2;
            No atual = heap.get(idx);
            No noPai = heap.get(pai);
            if (atual.compareTo(noPai) >= 0) {
                break;
            }
            // troca com o pai
            heap.set(idx, noPai);
            heap.set(pai, atual);
            idx = pai;
        }
    }

    // Move element down para manter propriedade do heap
    private void siftDown(int idx) {
        int size = heap.size();
        while (true) {
            int esquerda = 2 * idx + 1;
            int direita = 2 * idx + 2;
            int menor = idx;
            if (esquerda < size && heap.get(esquerda).compareTo(heap.get(menor)) < 0) {
                menor = esquerda;
            }
            if (direita < size && heap.get(direita).compareTo(heap.get(menor)) < 0) {
                menor = direita;
            }
            if (menor == idx) {
                break;
            }
            // troca idx com o filho menor
            No tmp = heap.get(idx);
            heap.set(idx, heap.get(menor));
            heap.set(menor, tmp);
            idx = menor;
        }
    }
}