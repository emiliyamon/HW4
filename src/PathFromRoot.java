public class PathFromRoot {
    public static boolean doesPathExist(BinNode<Character> root, String str) {
        if (str.length() == 0) {
            return true;
        }
        if (root.getData() != str.charAt(0)) {
            return false;
        }
        String copy = str.substring(1);
        return doesPathExist(root.getLeft(), copy) && doesPathExist(root.getRight(), copy);
    }

}
