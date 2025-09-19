import java.util.ArrayList;

public class MinHeap {
    ArrayList<No> heap;
    private int size;

    public MinHeap() {
        heap = new ArrayList<>();
        this.size = 0;
    }

    public No parent(No i) return heap.get((i - 1) / 2); 
    public No left(No i) return heap.get(2 * i + 1); 
    public No right(No i) return heap.get(2 * i + 2); 

    public int getSize() return size;

    public boolean isLeaf(int i) return (i >= size / 2) && (i < size); 

    public void swap(int i, int j) {
        No temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);      
    }

    public void insert(No element) {
        heap.add(element);
        size++;
        int current = size - 1;

        while (current > 0 && heap.get(current).compareTo(parent(current)) < 0) {
            swap(current, (current - 1) / 2);
            current = (current - 1) / 2;
        }
    }

    public No remove() {
        if (size == 0) return null;
        No popped = heap.get(0);
        heap.set(0, heap.get(size - 1));
        heap.remove(size - 1);
        size--;
        minHeapify(0);
        return popped;
    }

    public heapify(int i) {
        if (!isLeaf(i)) {
            No left = left(i);
            No right = right(i);
            No menor = heap.get(i);

            if (left != null && left.compareTo(menor) < 0) menor = left;
            if (right != null && right.compareTo(menor) < 0) menor = right;

            if (menor != heap.get(i)) {
                int menorIndex = heap.indexOf(menor);
                swap(i, menorIndex);
                minHeapify(menorIndex);
            }
        }
    }


}
