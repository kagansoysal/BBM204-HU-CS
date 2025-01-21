public class SearchAlgorithms {

    static int linearSearch(int[] datas, int value) {
        for (int i = 0; i < datas.length; i++) {
            if (datas[i] == value) return i;
        }
        return -1;
    }

    static int binarySearch(int[] datas, int value) {
        int low = 0;
        int high = datas.length - 1;

        while (high - low > 1) {
            int mid = (high + low) / 2;

            if (datas[mid] < value) low = mid + 1;
            else high = mid;
        }

        if (datas[low] == value) return low;
        else if (datas[high] == value) return high;

        return -1;
    }
}