package zbs.casclient.algorithm;

import java.util.Arrays;

/**
 * 冒泡算法
 * @author zbs
 * @since 2022/9/7 9:41
 */
public class A1_BubbleSort {
    public static void main(String[] args) {
        int[] arr = {2,9,0,7,1,32,2,7,7};
        bubbleSort1(arr);
        System.out.println(Arrays.toString(arr));
    }
    
    public static void bubbleSort(int[] arr){
        for(int p = arr.length -1; p > 0; p--){
            for(int i = 0; i < p; i++){
                if(arr[i] > arr[i+1]) Util.swap(arr, i, i+1);
            }
        }
    }
    //优化：当遍历一遍没有发生交换时，其实已经排好序了
    public static void bubbleSort1(int[] arr){
        for(int p = arr.length -1; p > 0; p--){
            boolean flag = true;
            for(int i= 0; i< p; i++){
                if(arr[i] > arr[i+1]){
                    Util.swap(arr, i, i+1);
                    flag = false;
                }
            }
            //未发生交换，直接返回
            if(flag){
                return;
            }
        }
    }
}
