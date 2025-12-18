package KCCRS;

public class Sorting {

    public static void mergeSort(Complaint[] arr, java.util.Comparator<Complaint> cmp){
        if(arr.length <= 1) return;

        Complaint[] aux = java.util.Arrays.copyOf(arr, arr.length);
        mergeSortHelper(arr, aux, 0, arr.length-1, cmp);
    }

    private static void mergeSortHelper(Complaint[] arr, Complaint[] aux, int lo, int hi, java.util.Comparator<Complaint> cmp){

        if(lo >= hi) return;

        int mid = lo + (hi - lo)/2;

        mergeSortHelper(arr, aux, lo, mid, cmp);
        mergeSortHelper(arr, aux, mid+1, hi, cmp);

        int i = lo, j = mid+1, k = lo;

        while(i<=mid || j<=hi){
            if(i > mid) aux[k++] = arr[j++];
            else if(j > hi) aux[k++] = arr[i++];
            else if(cmp.compare(arr[i], arr[j]) <= 0) aux[k++] = arr[i++];
            else aux[k++] = arr[j++];
        }

        for(k=lo; k<=hi; k++) arr[k] = aux[k];
    }

    public static void insertionSort(Complaint[] arr, java.util.Comparator<Complaint> cmp){
        for(int i=1; i<arr.length; i++){
            Complaint key = arr[i];
            int j = i - 1;
            while(j >= 0 && cmp.compare(arr[j], key) > 0){
                arr[j+1] = arr[j];
                j--;
            }
            arr[j+1] = key;
        }
    }
}