package com.communal.leetcode;

import java.util.LinkedList;
import java.util.PriorityQueue;

/*
  937. 重新排列日志文件
  给你一个日志数组 logs。每条日志都是以空格分隔的字串，其第一个字为字母与数字混合的 标识符
  思路：利用自定义排序，实现小顶堆排序
 */
public class ReorderDataLogFiles {

    static class Solution {
        public String[] reorderLogFiles(String[] logs) {
            if (logs.length == 0){
                return new String[0];
            }
            // 保存相对顺序
            LinkedList<String> digitStrs = new LinkedList<>();
            PriorityQueue<String> strs = new PriorityQueue<>(this::compare);
            for (int i = 0; i < logs.length; i++) {
                String[] strArr = logs[i].split(" ");
                // 区分字符串 or 数字
                    if (Character.isDigit(strArr[1].charAt(0))){
                        digitStrs.add(logs[i]);
                    }
                    else {
                        strs.add(logs[i]);
                    }
            }
            String[] orderStr = new String[logs.length];
            int j = 0;
            for (int i = 0; i < logs.length; i++) {
                if (!strs.isEmpty()) {
                    orderStr[i] = strs.poll();
                }else {
                    orderStr[i] = digitStrs.get(j++);
                }
            }

            return orderStr;
        }

        // 自定义排序规则
        public int compare(String b, String a){
            // 取出最大的
            String[] strArr = b.split(" ");
            String[] maxStrArr = a.split(" ");
            int flagLen_A = maxStrArr[0].length();
            int flagLen_B = strArr[0].length();

            // 截取队列里最大字符串
            String maxStrSub_A = a.substring(flagLen_A+1);
            String maxStrSub_B = b.substring(flagLen_B+1);
            if (maxStrSub_B.compareTo(maxStrSub_A) <  0){
                return -1;
            }else if (maxStrSub_B.compareTo(maxStrSub_A) > 0){
                return 1;
            }else {
                if (strArr[0].compareTo(maxStrArr[0]) < 0){
                    return -1;
                }else if (strArr[0].compareTo(maxStrArr[0]) > 0){
                    return 1;
                }else {
                    return 0;
                }
            }
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        String[] logs = new String[]{"dig1 8 1 5 1","let1 art can","dig2 3 6","let2 own kit dig","let3 art zero"};
        solution.reorderLogFiles(logs);
    }
}


