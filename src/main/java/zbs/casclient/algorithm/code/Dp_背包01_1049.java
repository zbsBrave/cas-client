package zbs.casclient.algorithm.code;

import java.util.Arrays;

/**
 * 最后一块石头的重量
 *
 * @author zbs
 * @since 2022/9/15 17:21
 */
public class Dp_背包01_1049 {
//    有一堆石头，用整数数组stones 表示。其中stones[i] 表示第 i 块石头的重量。
//
//    每一回合，从中选出任意两块石头，然后将它们一起粉碎。假设石头的重量分别为x 和y，且x <= y。那么粉碎的可能结果如下：
//
//    如果x == y，那么两块石头都会被完全粉碎；
//    如果x != y，那么重量为x的石头将会完全粉碎，而重量为y的石头新重量为y-x。
//    最后，最多只会剩下一块 石头。返回此石头 最小的可能重量 。如果没有石头剩下，就返回 0。

//    输入：stones = [2,7,4,1,8,1]
//    输出：1
//    解释：
//    组合 2 和 4，得到 2，所以数组转化为 [2,7,1,8,1]，
//    组合 7 和 8，得到 1，所以数组转化为 [2,1,1,1]，
//    组合 2 和 1，得到 1，所以数组转化为 [1,1,1]，
//    组合 1 和 1，得到 0，所以数组转化为 [1]，这就是最优值。


    public static void main(String[] args) {
//        int[] stones = new int[]{2,7,4,1,8,1};
        int[] stones = new int[]{31, 26, 33, 21, 40};

        System.out.println(lastStoneWeightII(stones));
    }

    public static int lastStoneWeightII(int[] stones) {
        //本质可以看出两堆石头，一边为正，一边为负，要使它们的和最小
        //最小值是0，即没堆石头的容量 = sum/2
        //为什么不能用sum作为容量呢？因为stones中的石头可为正或者负，但是现在只考虑了正数，这也可以用来判断能否使用背包算法
        int sum = Arrays.stream(stones).sum();
        int len = stones.length;
        int size = sum / 2;
        //dp[i][j] 前i个物品，不超过容量j，所能达到的最大容量
        int[][] dp = new int[len + 1][size + 1];
        for (int i = 1; i <= len; i++) {
            int cur = stones[i-1];
            for (int j = 0; j <= size; j++) {
                if (j >= cur) {
                    dp[i][j] = Math.max(dp[i-1][j], dp[i-1][j-cur] + cur);
                }else {
                    dp[i][j] = dp[i-1][j];
                }
            }
        }
        return sum - 2*dp[len][size];
    }
}
