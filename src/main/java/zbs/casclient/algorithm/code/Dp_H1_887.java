package zbs.casclient.algorithm.code;

/**
 * 扔鸡蛋
 *
 * @author zbs
 * @since 2022/9/13 12:13
 */
public class Dp_H1_887 {
//    给你 k 枚相同的鸡蛋，并可以使用一栋从第 1 层到第 n 层共有 n 层楼的建筑。
//
//    已知存在楼层 f ，满足0 <= f <= n ，任何从 高于 f 的楼层落下的鸡蛋都会碎，从 f 楼层或比它低的楼层落下的鸡蛋都不会破。
//
//    每次操作，你可以取一枚没有碎的鸡蛋并把它从任一楼层 x 扔下（满足1 <= x <= n）。如果鸡蛋碎了，你就不能再次使用它。如果某枚鸡蛋扔下后没有摔碎，则可以在之后的操作中 重复使用 这枚鸡蛋。
//
//    请你计算并返回要确定 f 确切的值 的 最小操作次数 是多少？

    public static void main(String[] args) {
        System.out.println(superEggDrop(3,14));
    }

    public static int superEggDrop(int k, int n) {
        //1,2=2  2,6=3 3,14=4  2,7=4  2,9=4  3,14=4
        int[][] dp = new int[k + 1][n + 1];
        for(int i =1; i <=k; i++){
            for(int j=1; j<=n; j++){
                if(i == 1){
                    //1,当k=1，即只有一个鸡蛋，最小移动次数=楼层n
                    dp[i][j] = j;
                    continue;
                }
                if(j == 1){
                    //2,当只有一层楼时，只需移动一次
                    dp[i][j] = 1;
                    continue;
                }
                //3,判断鸡蛋的个数足以使用二分法，可以直接判断最小移动次数=log2(N)
                int x = log2(j);
                if(i > x){
                    dp[i][j] = x+1;
                    continue;
                }
                //1 2 3 4 5 6 7 8 9
                //1 2 3 4 5 6 7  8 9 10 11 12 13 14
                //1 2 3 4 5 6 7  8 9 10 11 12 13 14
                
                //4,不足以使用二分法，怎么判断dp[i][j]的最小值呢：我们遍历第一层到第j层，取其中最小值
//                for(int mid = 1; mid <=j; mid++){
//                    int cur = Math.max(dp[i-1][mid-1], dp[i][j-mid]) + 1;
//                    if(mid == 1){
//                        dp[i][j] = cur;
//                    }else {
//                        dp[i][j] = Math.min(dp[i][j] , cur);
//                    }
//                }
                
                //上面代码的复杂度是kNN，时间太长
                //使用二分法
                dp[i][j] = j;
                int l = 1, r = j;
                while (l < r){
                    int mid = (l + r)/2;
                    if(dp[i-1][mid-1] < dp[i][j-mid]){
                        l = mid + 1;
                    }else {
                        r = mid;
                    }
                }
                dp[i][j] = Math.min(dp[i][j], Math.max(dp[i-1][l-1], dp[i][j-l]) + 1);
                
            }
        }

        return dp[k][n];

    }
    
    public static int log2(int x){
        // n,k-1  n+1,k
        return (int)(Math.log(x)/Math.log(2));
    }
    

}
