package com.communal.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

//给你 root1 和 root2 这两棵二叉搜索树。请你返回一个列表，其中包含 两棵树 中的所有整数并按 升序 排序。

public class TwoBinarySearchTrees {
    public class TreeNode {
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


    static class Solution {

        public List<Integer> getAllElements(TreeNode root1, TreeNode root2) {
            PriorityQueue<Integer> minHeap = new PriorityQueue<>();
            getElements(root1, minHeap);
            getElements(root2, minHeap);
            List<Integer> list = new ArrayList<>();
            int size = minHeap.size();
            for (int i = 0; i < size; i++){
                list.add(minHeap.poll());
            }
            return list;
        }

        public void getElements(TreeNode root, PriorityQueue<Integer> elements){
            if (root == null){
                return;
            }
            getElements(root.left, elements);
            elements.offer(root.val);
            getElements(root.right, elements);
        }
    }
}
