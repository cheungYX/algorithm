public class Solution {
    /**
     * @param nums : Give an array numbers of n integer
     * @return : Find all unique triplets in the array which gives the sum of zero.
     */
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> results = new ArrayList<>();
        
        if (nums == null || nums.length < 3) {
            return results;
        }
        
        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 2; i++) {
            // skip duplicate triples with the same first numebr
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            int left = i + 1, right = nums.length - 1;
            int target = -nums[i];
            
            twoSum(nums, left, right, target, results);
        }
        
        return results;
    }
    
    public void twoSum(int[] nums,
                       int left,
                       int right,
                       int target,
                       List<List<Integer>> results) {
        while (left < right) {
            if (nums[left] + nums[right] == target) {
                ArrayList<Integer> triple = new ArrayList<>();
                triple.add(-target);
                triple.add(nums[left]);
                triple.add(nums[right]);
                results.add(triple);
                
                left++;
                right--;
                // skip duplicate pairs with the same left
                while (left < right && nums[left] == nums[left - 1]) {
                    left++;
                }
                // skip duplicate pairs with the same right
                while (left < right && nums[right] == nums[right + 1]) {
                    right--;
                }
            } else if (nums[left] + nums[right] < target) {
                left++;
            } else {
                right--;
            }
        }
    }
}

// 算法集训营版本
public class Solution {
    /**
     * @param nums : Give an array numbers of n integer
     * @return : Find all unique triplets in the array which gives the sum of zero.
     */
    List<List<Integer>> results = new ArrayList<>();
    
    // This is soring results using 1st number as 1st key, 2nd number as 2nd key...
    class ListComparator<T extends Comparable<T>> implements Comparator<List<Integer>> {
      @Override
      public int compare(List<Integer> a, List<Integer> b) {
        for (int i = 0; i < Math.min(a.size(), b.size()); i++) {
          int c = a.get(i).compareTo(b.get(i));
          if (c != 0) {
            return c;
          }
        }
        return Integer.compare(a.size(), b.size());
      }
    }
    
    public List<List<Integer>> threeSum(int[] A) {
        if (A == null || A.length < 3) {
            return results;
        }
        
        Arrays.sort(A);
        // enumerate c, the maximum element
        int i;
        int n = A.length;
        for (i = 2; i < n; ++i) {
            // c is always the last in a bunch of duplicates
            if (i + 1 < n && A[i + 1] == A[i]) {
                continue;
            }
            
            // want to find all pairs of A[j]+A[k]=-A[i], such that
            // j < k < i
            twoSum(A, i - 1, -A[i]);
        }
        
        Collections.sort(results, new ListComparator<>());
        
        return results;
    }
    
    // find all unique pairs such that A[i]+A[j]=S, and i < j <= right
    private void twoSum(int[] A, int right, int S) {
        int i, j;
        j = right;
        for (i = 0; i <= right; ++i) {
            // A[i] must be the first in its duplicates
            if (i > 0 && A[i] == A[i - 1]) {
                continue;
            }
            
            while (j > i && A[j] > S - A[i]) {
                --j;
            }
            
            if (i >= j) {
                break;
            }
            
            if (A[i] + A[j] == S) {
                List<Integer> t = new ArrayList<>();
                t.add(A[i]);
                t.add(A[j]);
                t.add(-S); // t.add(A[right+1])
                results.add(t);
            }
        }
    }
}