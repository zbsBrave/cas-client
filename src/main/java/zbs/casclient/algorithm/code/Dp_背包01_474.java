package zbs.casclient.algorithm.code;

/**
 * 01背包：一和零
 * @author zbs
 * @since 2022/9/15 9:53
 */
public class Dp_背包01_474 {
//    给你一个二进制字符串数组 strs 和两个整数 m 和 n 。
//
//    请你找出并返回 strs 的最大子集的长度，该子集中 最多 有 m 个 0 和 n 个 1 。
//
//    如果 x 的所有元素也是 y 的元素，集合 x 是集合 y 的 子集 。

    public static void main(String[] args) {
        String[] strs = new String[]{"10", "0001", "111001", "1", "0"};
        System.out.println(findMaxForm(strs, 0, 1));
    }

    public static int findMaxForm(String[] strs, int m, int n) {
        int len = strs.length;
        //dp[i][j][k] 前i个元素，0的容量j，1的容量k， 最大数量
        int[][][] dp = new int[len + 1][m + 1][n + 1];
        for(int i=1; i<=len; i++){
            int[] size = get01(strs[i-1]);
            int size0 = size[0], size1 = size[1];
            for(int j=0; j<=m; j++){
                for(int k=0; k<=n; k++){
                    if(j>=size0 && k>=size1){
                        dp[i][j][k] = Math.max(dp[i-1][j][k], dp[i-1][j-size0][k-size1] + 1);
                    }else {
                        dp[i][j][k] = dp[i-1][j][k];
                    }
                }
            }
        }
        return dp[len][m][n];
    }
    public static int[] get01(String s){
        //size[0]代表o的数量，size[1]代表1的数量
        int[] size = new int[2];
        for(char c : s.toCharArray()){
            size[c - '0']++;
        }
        return size;
    }
}
