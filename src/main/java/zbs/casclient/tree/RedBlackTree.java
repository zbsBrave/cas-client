package zbs.casclient.tree;

/**
 * @author zbs
 * @since 2022/9/1 9:46
 */
public class RedBlackTree {
    
    
    private class Node{
        private int data;
        private Node parent, left, right;
        //true：红色，false：黑色
        private boolean color = true;
    }
    
    /**
     * 左旋，
     * @param root 左旋后的根节点
     * @author zbs
     */
    private Node rotateLeft(Node root){
        //根节点root的右节点 当做 新的根节点x
        Node x = root.right;
        x.color = root.color;
        
        //把root 设置为 x的左节点
        x.left = root;
        
        //把root的右节点 设置为 x的左节点
        root.right = x.left;
        root.color = true;
        
        return x;
    }
}
