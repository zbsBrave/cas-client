package zbs.casclient.algorithm.code;

import java.util.Arrays;

/**
 * 零钱兑换
 *
 * @author zbs
 * @since 2022/9/13 10:44
 */
public class Dp1_322 {
    public static void main(String[] args) {
        //整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
        //计算并返回可以凑成总金额所需的 最少的硬币个数 。如果没有任何一种硬币组合能组成总金额，返回 -1
        int[] coins = new int[]{1, 2, 5};
        int amount = 11;
        System.out.println(coinChange(coins, amount));
    }

    public static int coinChange(int[] coins, int amount) {
        int unable = amount + 1;
        int[] dp = new int[unable];//有 0 - amount 个子解，即 amount+1
        Arrays.fill(dp, unable);
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (i < coin) continue;//币值比i大，无法组合
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
        return dp[amount] == unable ? -1 : dp[amount];
    }

}
