import java.util.Arrays;

public class RangeMinimumQuery {
    
    // Square Root Decomposition
    static class RangeMinimumQuerySqrtDecomposition {
        private int[] arr;
        private int[] blockMins;
        private int blockSize;

        public RangeMinimumQuerySqrtDecomposition(int[] arr) {
            this.arr = arr;
            this.blockSize = (int) Math.ceil(Math.sqrt(arr.length));
            this.blockMins = new int[(arr.length + blockSize - 1) / blockSize];
            Arrays.fill(blockMins, Integer.MAX_VALUE);
            preprocess();
        }

        private void preprocess() {
            for (int i = 0; i < arr.length; i++) {
                int blockIndex = i / blockSize;
                blockMins[blockIndex] = Math.min(blockMins[blockIndex], arr[i]);
            }
        }

        public int query(int left, int right) {
            int min = Integer.MAX_VALUE;
            int startBlock = left / blockSize;
            int endBlock = right / blockSize;

            if (startBlock == endBlock) {
                for (int i = left; i <= right; i++) {
                    min = Math.min(min, arr[i]);
                }
            } else {
                for (int i = left; i < (startBlock + 1) * blockSize; i++) {
                    min = Math.min(min, arr[i]);
                }
                for (int i = startBlock + 1; i < endBlock; i++) {
                    min = Math.min(min, blockMins[i]);
                }
                for (int i = endBlock * blockSize; i <= right; i++) {
                    min = Math.min(min, arr[i]);
                }
            }
            return min;
        }
    }

    // Sparse Table
    static class RangeMinimumQuerySparseTable {
        private int[][] st;
        private int[] log;
        private int n;

        public RangeMinimumQuerySparseTable(int[] arr) {
            n = arr.length;
            int maxLog = (int) (Math.log(n) / Math.log(2)) + 1;
            st = new int[n][maxLog];
            log = new int[n + 1];

            for (int i = 2; i <= n; i++) {
                log[i] = log[i / 2] + 1;
            }

            for (int i = 0; i < n; i++) {
                st[i][0] = arr[i];
            }

            for (int j = 1; (1 << j) <= n; j++) {
                for (int i = 0; i + (1 << j) <= n; i++) {
                    st[i][j] = Math.min(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
                }
            }
        }

        public int query(int left, int right) {
            int j = log[right - left + 1];
            return Math.min(st[left][j], st[right - (1 << j) + 1][j]);
        }
    }

    public static void main(String[] args) {
        int[] array = {1, 3, 2, 7, 9, 11, 2, 6};

        // Square Root Decomposition Test
        RangeMinimumQuerySqrtDecomposition rmqSqrt = new RangeMinimumQuerySqrtDecomposition(array);
        System.out.println("Square Root Decomposition:");
        System.out.println(rmqSqrt.query(1, 5)); // Output: 2
        System.out.println(rmqSqrt.query(0, 3)); // Output: 1
        System.out.println(rmqSqrt.query(3, 7)); // Output: 2

        // Sparse Table Test
        RangeMinimumQuerySparseTable rmqSparse = new RangeMinimumQuerySparseTable(array);
        System.out.println("Sparse Table:");
        System.out.println(rmqSparse.query(1, 5)); // Output: 2
        System.out.println(rmqSparse.query(0, 3)); // Output: 1
        System.out.println(rmqSparse.query(3, 7)); // Output: 2
    }
}
