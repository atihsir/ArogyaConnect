// MinHeap.java
public class MinHeap {
    private Reminder[] heap;
    private int size;

    public MinHeap() {
        this.heap = new Reminder[10]; // Initial size
        this.size = 0;
    }

    public void insert(Reminder reminder) {
        if (size >= heap.length) {
            // Resize the array if needed
            Reminder[] newHeap = new Reminder[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, heap.length);
            heap = newHeap;
        }
        heap[size] = reminder;
        size++;
        bubbleUp(size - 1);
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap[index].time.isAfter(heap[parentIndex].time)) {
                break;
            }
            Reminder temp = heap[index];
            heap[index] = heap[parentIndex];
            heap[parentIndex] = temp;
            index = parentIndex;
        }
    }

    public Reminder extractMin() {
        if (size == 0) {
            return null;
        }
        Reminder min = heap[0];
        heap[0] = heap[size - 1];
        size--;
        bubbleDown(0);
        return min;
    }

    public Reminder peekMin() {
        if (size == 0) {
            return null;
        }
        return heap[0]; // Return the min element without removing it
    }

    private void bubbleDown(int index) {
        while (index < size) {
            int leftChildIndex = index * 2 + 1;
            int rightChildIndex = index * 2 + 2;
            int smallestIndex = index;

            if (leftChildIndex < size && heap[leftChildIndex].time.isBefore(heap[smallestIndex].time)) {
                smallestIndex = leftChildIndex;
            }

            if (rightChildIndex < size && heap[rightChildIndex].time.isBefore(heap[smallestIndex].time)) {
                smallestIndex = rightChildIndex;
            }

            if (smallestIndex == index) {
                break;
            }

            Reminder temp = heap[index];
            heap[index] = heap[smallestIndex];
            heap[smallestIndex] = temp;
            index = smallestIndex;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }
}


