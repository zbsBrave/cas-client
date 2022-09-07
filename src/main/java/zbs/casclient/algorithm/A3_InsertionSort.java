package zbs.casclient.algorithm;

import java.util.Arrays;

/**
 * @author zbs
 * @since 2022/9/7 11:30
 */
public class A3_InsertionSort {
    public static void main(String[] args) {
        int[] arr = {1,2,2,5,6,7,7,9,354,6,0,545,32};
        insertionSort(arr);
        System.out.println(Arrays.toString(arr));
    }
    
    public static void insertionSort(int[] arr){
        for(int p = 1; p < arr.length; p++){
            int temp = arr[p];
            int i = p;
            for(; i > 0 && arr[i-1] > temp; i--){
                arr[i] = arr[i-1];
            }
            arr[i] = temp;
        }
    }
}
