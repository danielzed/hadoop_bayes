import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    //public static String[] CLASS_NAMES={"ALB","ARG","AUSTR","BELG","BRAZ","CANA"};
    public static String[] CLASS_NAMES={"AFGH"};
    private static Pattern classnamePattern=Pattern.compile("Country/(.*)/");
    public static String getClassname(String text){
        Matcher matcher=classnamePattern.matcher(text);
        if(matcher.find()){
            return matcher.group(1);
        }
        return null;
    }
    public static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
