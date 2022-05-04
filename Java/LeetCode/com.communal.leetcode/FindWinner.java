package com.communal.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 1823. 找出游戏的获胜者
 *
 */
public class FindWinner {

    static class Solution {
        public int findTheWinner(int n, int k) {
            List<Integer> arr = new ArrayList<>(n);
            for (int i = 0; i < n ; i++) {
                arr.add(i+1);
            }
            int j = 0;
            while (arr.size() != 1){
                // 前进 k 步, 因为自身也算，实际前进 k -1 步
                j  = (k -1) + j;
                // 步长超过数组长度
                if (j >= arr.size()){
                    // 规化
                    j = j % arr.size();
                }
                // 删除，被淘汰
                arr.remove(j);
            }

            return arr.get(0);
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.findTheWinner(6, 5);
    }
}
