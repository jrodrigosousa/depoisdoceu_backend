package br.com.solverapps.depoisdoceu.security;


import java.util.ArrayList;
import java.util.List;

class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode() {}
      TreeNode(int val) { this.val = val; }
      TreeNode(int val, TreeNode left, TreeNode right) {
          this.val = val;
          this.left = left;
          this.right = right;
      }
  }

class Solution {
    public List<Integer> inorderTraversal(TreeNode root) {
        if(root==null)
            return new ArrayList();
        List retorno = new ArrayList();
        retorno.addAll(inorderTraversal(root.left));
        retorno.add(root.val);
        retorno.addAll(inorderTraversal(root.right));
        return retorno;

    }

    public boolean isSameTree(TreeNode p, TreeNode q) {
        if(p==null && q==null)
            return true;
        if(p==null || q==null)
            return false;

        if(p.val!=q.val)
            return false;

        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    public boolean isSameSimetric(TreeNode p, TreeNode q) {
        if(p==null && q==null)
            return true;
        if(p==null || q==null)
            return false;

        if(p.val!=q.val)
            return false;

        return isSameSimetric(p.left, q.right) && isSameTree(p.right, q.left);
    }

    public boolean isSymmetric(TreeNode root) {
        if(root==null)
            return true;
        return isSameSimetric(root.left, root.right);
    }

    public boolean hasPathSum(TreeNode root, int targetSum) {
        if(root==null)
            return false;
        if(targetSum==root.val && root.left==null && root.right==null)
            return true;
        else
            return hasPathSum(root.right, targetSum-root.val)
                    ||hasPathSum(root.left, targetSum-root.val);

    }

    public static void main(String[] args){
        TreeNode root = new TreeNode(1, new TreeNode(1, new TreeNode(1, null, null), null), new TreeNode(1, null, new TreeNode(1, null, null)));

        Solution solution = new Solution();

        System.out.println(solution.hasPathSum(root, 3));
    }
}
