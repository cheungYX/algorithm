public class Solution {
    /**
     * @param nums: an array
     * @param k: an integer
     * @return: the maximum average value
     */
    public double findMaxAverage(int[] nums, int k) {
        // Write your code here
        int Max=Integer.MIN_VALUE;
        int sum=0,start=0,end=k-1;
        while(end<nums.length){
            sum=0;
            for(int i=start;i<=end;i++)
                sum+=nums[i];
            if(sum>Max)
                Max=sum;
            start++;
            end++;
        }
        return (double)Max/k;
    }
}