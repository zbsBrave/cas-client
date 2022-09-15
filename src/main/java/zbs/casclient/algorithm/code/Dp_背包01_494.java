package zbs.casclient.algorithm.code;

/**
 * 01背包：目标和
 * @author zbs
 * @since 2022/9/15 10:43
 */
public class Dp_背包01_494 {
//    给你一个整数数组 nums 和一个整数 target 。
//
//    向数组中的每个整数前添加'+' 或 '-' ，然后串联起所有整数，可以构造一个 表达式 ：
//
//    例如，nums = [2, 1] ，可以在 2 之前添加 '+' ，在 1 之前添加 '-' ，然后串联起来得到表达式 "+2-1" 。
//    返回可以通过上述方法构造的、运算结果等于 target 的不同 表达式 的数目。

//    输入：nums = [1,1,1,1,1], target = 3
//    输出：5
//    解释：一共有 5 种方法让最终目标和为 3 。
//            -1 + 1 + 1 + 1 + 1 = 3
//            +1 - 1 + 1 + 1 + 1 = 3
//            +1 + 1 - 1 + 1 + 1 = 3
//            +1 + 1 + 1 - 1 + 1 = 3
//            +1 + 1 + 1 + 1 - 1 = 3
    public static void main(String[] args) {
        // 1 2 1   0
//        int[] nums = new int[]{1,1,1,1,1};
//        int target = 3;
        int[] nums = new int[]{1,2,1};
        int target = 0;
        System.out.println(findTargetSumWays(nums,target));

    }


    public static int findTargetSumWays(int[] nums, int target) {
        //ps:本题有个bug，nums都是正整数(题干未说明，但是负数没法通过官方题解)，但是target可以是负整数，比如 nums = {100}, target = -200
        //将nums分为两部分：正数x，负数y
        // x + (-y) = target
        // x + y = sum
        // 所以-> 2x = target + sum
        // 所以-> 2y = sum - target
        // 应该取哪个作为容量呢，x还是y？都可以，选更小一点的可以减少循环次数
        // 我这里直接以x做为容量，以上可知：1，target+sum不能为奇数，因为2x肯定是偶数。 2，x = (target+sum)/2
        int sum = 0;
        for(int n : nums){
            sum+=n;
        }
        if(sum < target || sum < -target){
            //target可能为负数
            return 0;
        }
        int total = sum + target;
        if((total & 1) == 1){
            //奇数时
            return 0;
        }
        int size = total/2;//背包容量
        int[][] dp = new int[nums.length + 1][size + 1];
        dp[0][0] = 1;
        for(int i =1; i<=nums.length; i++){
            for(int j=0; j<=size; j++){
                if(j >= nums[i-1]){
                    dp[i][j] = dp[i-1][j] + dp[i-1][j-nums[i-1]];
                }else {
                    dp[i][j] = dp[i-1][j];
                }
            }
        }
        return dp[nums.length][size];
    }
}
