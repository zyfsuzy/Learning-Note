package com.communal.leetcode;

import java.util.Arrays;
/*
713. 乘积小于 K 的子数组
给你一个整数数组 nums 和一个整数 k ，请你返回子数组内所有元素的乘积严格小于 k 的连续子数组的数目
 */
public class SubarrayProductLessThanK {

    static class Solution {
        public int numSubarrayProductLessThanK(int[] nums, int k) {
            if (k ==0) return 0;
            int size = nums.length;
            // 用于记录乘机
            int[] dp = new int[size];
            int count = 0;
            for (int i = 0; i < size ; i++) {
                if (nums[i] < k){
                    count++;
                }
                for (int j = i + 1; j < size && dp[j-1] < k; j++){
                    dp[j] = dp[j-1] * nums[j];
                    if (dp[j] < k && dp[j] != 0){
                        count++;
                    }
                }
            }

            return count;
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] a = new int[]{10,5,2,6};
        solution.numSubarrayProductLessThanK(a, 100);
    }
}
