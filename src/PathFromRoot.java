public class PathFromRoot {
    /**
     * Function for checking if there is a path of a certain given String
     * (Case sensitive) in the current binary tree
     *
     * @param root Starting root of the binary tree
     * @param str the relevant string to check if a path of exists in the tree
     * @return boolean value according to the result
     */
    public static boolean doesPathExist(BinNode<Character> root, String str) {
        if (str.length() == 0) {
            return true;
        }
        if (root.getData() != str.charAt(0)) {
            return false;
        }
        String copy = str.substring(1);
        return doesPathExist(root.getLeft(), copy) || doesPathExist(root.getRight(), copy);
    }

}
