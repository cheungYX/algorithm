class Solution {
public:
    /*
     * @param numbers: Give an array numbers of n integer
     * @param target: An integer
     * @return: return the sum of the three integers, the sum closest target.
     */
    int threeSumClosest(vector<int> &numbers, int target) {
        // write your code here
        sort(numbers.begin(), numbers.end());
    	int size = numbers.size();
		int result = numbers[0] + numbers[1] + numbers[size-1];
		int temp = abs(target - result);
		
		for (int i = 0; i < size - 2; i++)
		{
		   if (i >=1 && numbers[i - 1] == numbers[i])
		   {
				continue;
		   }
		   int start = i + 1, end = size - 1;
		   while (start < end)
		   {
			   if (start > i + 1 && numbers[start] == numbers[start - 1])
			   {
				   start++;
				   continue;
			   }
			    int value = numbers[i] + numbers[start] + numbers[end];
			    if (value== target)
			   {
					return target;
			   }
				else if (value< target)
				{
					start++;
					if (abs(value- target) < temp)
					{
						result = value;
						temp = abs(value - target);
					}
			   }
				else if (value > target)
				{
					
					end--;
					if (abs(value - target) < temp)
					{
						result = value;
						temp = abs(value - target);
					}
				}   
		   }
		}
		return result;
    }
};