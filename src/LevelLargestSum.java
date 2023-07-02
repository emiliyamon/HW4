import java.util.ArrayDeque;

public class LevelLargestSum {
    public static int getLevelWithLargestSum(BinNode<Integer> root) {
        if (root == null) {
            return -1;
        }

        int maxLevel = 0;
        int maxSum = Integer.MIN_VALUE;

        ArrayDeque<BinNode<Integer>> deque = new ArrayDeque<>();
        deque.offer(root);
        int currentLevel = 0;

        while (!deque.isEmpty()) {
            int currentLevelSize = deque.size();
            int currentSum = 0;

            for (int i = 0; i < currentLevelSize; i++) {
                BinNode<Integer> node = deque.poll();
                currentSum += node.getData();

                if (node.getLeft() != null) {
                    deque.offer(node.getLeft());
                }

                if (node.getRight() != null) {
                    deque.offer(node.getRight());
                }
            }

            if (currentSum > maxSum) {
                maxSum = currentSum;
                maxLevel = currentLevel;
            }

            currentLevel++;

        }

        return maxLevel;
    }

}

