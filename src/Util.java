import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    //训练及测试选取的类别
    public static String[] CLASS_NAMES={"ALB","ARG","AUSTR","BELG","BRAZ","CANA"};
    //public static String[] CLASS_NAMES={"ALB"};
    //训练输入父目录
    public static String INPUT_PATH="/home/danielzhang/IdeaProjects/hadoop_bayes/NBCorpus/Country/";
    //类别统计输出目录
    public static String OUTPUT_PATH="/home/danielzhang/IdeaProjects/hadoop_bayes/outcome/";
    //class-term统计输出目录
    public static String OUTPUT_PATH1="/home/danielzhang/IdeaProjects/hadoop_bayes/outcome1/";
    //最终TEST测试分类结果输出目录
    public static String OUTPUT_PATH2="/home/danielzhang/IdeaProjects/hadoop_bayes/outcome2/";
    //Test测试分类输入文件父目录，具体测试文件在该目录下具体类别的子目录
    public static String INPUT_PATH_TEST="/home/danielzhang/IdeaProjects/hadoop_bayes/NBCorpus/Test/";

    //生成limit下随机20%比例的int list
    private static ArrayList<Integer> getRandomList(int limit){
        Hashtable<Integer,Integer> table=new Hashtable();
        ArrayList<Integer> result=new ArrayList<Integer>();
        Random random=new Random();
        while(result.size()<=(limit-1)/5-1){
            int a=random.nextInt(limit);
            if(table.getOrDefault(a,0)==0){
                result.add(a);
                table.put(a,1);
            }
        }
        return result;
    }
    public static void createRandomTest(){
        //随机按比例从INPUT——PATH中抽取文件，并放到INPUT_PATH_TEST对应类别的目录下，
        //如果之前目录已经存在并且其中有文件，则将文件返回对应类别的input_path中
        //否则，建立文件夹
        for(String classname:CLASS_NAMES){
            File files=new File(INPUT_PATH_TEST+classname);
            if(!files.exists()){
                files.mkdir();
            }else{
                for(File file:files.listFiles()){
                    file.renameTo(new File(INPUT_PATH+classname+"/"+file.getName()));
                }
            }
            File original_files=new File(INPUT_PATH+classname);
            File[] listFiles=original_files.listFiles();
            for(Object o:getRandomList(listFiles.length)){
                int index=(int)o;
                File target=listFiles[index];
                target.renameTo(new File(INPUT_PATH_TEST+classname+"/"+target.getName()));
            }

        }
    }
    //从路径中抽取类别名称
    private static Pattern classnamePattern=Pattern.compile("Country/(.*)/");
    public static String getClassname(String text){
        Matcher matcher=classnamePattern.matcher(text);
        if(matcher.find()){
            return matcher.group(1);
        }
        return null;
    }
    private static Pattern testClassnamePattern=Pattern.compile("Test/(.*)/");
    public static String getTestClassname(String text){
        Matcher matcher=testClassnamePattern.matcher(text);
        System.out.println(text);
        if(matcher.find()){
            return matcher.group(1);
        }
        return null;
    }
    //从路径中抽取文件docid
    private static Pattern filenamePattern=Pattern.compile("/(\\w*).txt");
    public static String getFilename(String text){
        Matcher matcher=filenamePattern.matcher(text);
        if(matcher.find()){
            return matcher.group(1);
        }
        return null;
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
