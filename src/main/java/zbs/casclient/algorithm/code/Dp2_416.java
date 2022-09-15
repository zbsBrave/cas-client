package zbs.casclient.algorithm.code;

/**
 * @author zbs
 * @since 2022/9/14 16:56
 */
public class Dp2_416 {
    //    给你一个 只包含正整数 的 非空 数组 nums 。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
    public static void main(String[] args) {
        // 前i个物品 放入 容量j的背包 = max(第i个物品不放入背包， 第i个物品放入背包)
        // 不放入背包 = dp[i-1,j]
        // 放入背包   = dp[i-1, j- i的容量] + i的价值
        //dp[i,j] = Math.max(dp[i-1,j], dp[i-1, j - i的容量] + i的价值);
        
        int[] nums = new int[]{2, 2, 3, 7};
        System.out.println(canPartition(nums));
    }


    public static boolean canPartition(int[] nums) {
        if(nums.length < 2){
            return false;
        }
        int sum = 0;
        for(int n : nums){
            sum += n;
        }
        if((sum & 1) == 1){
            //奇数时肯定不能分割成两个元素和相等的子集
            return false;
        }
        //背包的容量=sum/2，物品的个数=nums.length
        boolean[][] dp = new boolean[nums.length][sum/2 + 1];
        for(int i=1; i < nums.length; i++){
            for(int j=0; j <= sum/2; j++){
                if(j == nums[i]){
                    //第i个物品刚好能装下
                    dp[i][j] = true;
                } else if(j > nums[i]){
                    //第i个物品<容量
                    dp[i][j] = dp[i-1][j] || dp[i-1][j-nums[i]];
                }else {
                    dp[i][j] = dp[i-1][j];
                }
            }
        }
        
        return dp[nums.length-1][sum/2];
    }
}
