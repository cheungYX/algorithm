//选择排序
public class Solution {
    /*
     * @param A: an integer array
     * @return: 
     */
    public void sortIntegers(int[] A) {
        // write your code here
        for (int i = 0; i < A.length; i++) {
            int minIdx = i;
            for (int j = i; j < A.length; j++) {
                if (A[j] < A[minIdx]) {
                    minIdx = j;
                }
            }
            int tmp = A[i];
            A[i] = A[minIdx];
            A[minIdx] = tmp;
        }
    }
}


//选择排序2
public class Solution {
    /*
     * @param A: an integer array
     * @return: 
     */
    public void sortIntegers(int[] A) {
        // write your code here
        for (int i = 0; i < A.length; i++) {
            for (int j = i + 1; j < A.length; j++) {
                if (A[i] > A[j]) {
                    int tmp = A[i];
                    A[i] = A[j];
                    A[j] = tmp;
                }
            }
        }
    }
}

//插入排序
public class Solution {
    /*
     * @param A: an integer array
     * @return: 
     */
    public void sortIntegers(int[] A) {
        // write your code here
        for (int i = 0; i < A.length; i++) {
            int newVal = A[i];
            int j = i - 1;

            while (j >= 0 && A[j] > newVal) {
                A[j + 1] = A[j];
                j--;
            }
            A[j + 1] = newVal;
        }
    }
}

//冒泡排序
public class Solution {
    /*
     * @param A: an integer array
     * @return: 
     */
    public void sortIntegers(int[] A) {
        // write your code here
        while (true) {
            boolean exchange = false;
            for (int i = 0; i < A.length - 1; i++) {
                if (A[i] > A[i + 1]) {
                    int tmp = A[i];
                    A[i] = A[i + 1];
                    A[i + 1] = tmp;
                    exchange = true;
                }
            }
            if (!exchange) {
                break;
            }
        }
    }
}