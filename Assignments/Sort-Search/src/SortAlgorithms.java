import java.util.Arrays;

public class SortAlgorithms {
    static int[] insertionSort(int[] datas) {
        for (int j=1; j < datas.length; j++) {
            int key = datas[j];
            int i = j-1;

            while (i >= 0 && datas[i] > key) {
                datas[i+1] = datas[i];
                i--;
            }
            datas[i+1] = key;
        }
        return datas;
    }

    static int[] mergeSort(int[] datas) {
        int size = datas.length;

        if (size <= 1) return datas;
        int[] leftArray = Arrays.copyOfRange(datas, 0, size/2);
        int[] rightArray = Arrays.copyOfRange(datas, size/2, size);

        leftArray = mergeSort(leftArray);
        rightArray = mergeSort(rightArray);

        return merge(leftArray, rightArray);

    }

    static int[] merge(int[] left, int[] right) {
        int[] mergeArray = new int[left.length + right.length];

        int leftIndex = 0, rightIndex = 0, mergeIndex = 0;

        while (leftIndex < left.length || rightIndex < right.length) {
            if (leftIndex == left.length || (rightIndex < right.length && left[leftIndex] > right[rightIndex])) {
                mergeArray[mergeIndex++] = right[rightIndex++];
            } else if (rightIndex == right.length || (leftIndex < left.length && left[leftIndex] <= right[rightIndex])) {
                mergeArray[mergeIndex++] = left[leftIndex++];
            }
        }

        return mergeArray;
    }

    static  int[] countingSort(int[] datas) {
        int maxValue = Utilities.findMax(datas);

        int[] count = new int[maxValue + 1];
        int[] output = new int[datas.length];

        for (int i : datas) count[i]++;
        for (int i = 1; i < maxValue+1; i++)
            count[i] = count[i] + count[i - 1];

        for (int i = datas.length-1; i >= 0; i--) {
            int j = datas[i];
            count[j]--;
            output[count[j]] = datas[i];
        }
        return output;
    }
}