package zbs.casclient.algorithm;

/**
 * @author zbs
 * @since 2022/9/7 9:42
 */
public class Util {
    public static void swap(int[] arr, int i, int j) {
        int t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }
}
