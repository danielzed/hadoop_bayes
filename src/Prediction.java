import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Prediction {
    private static Hashtable<String,Double> class_prob=new Hashtable();
    private static Hashtable<Map<String,String>,Double> class_term_prob=new Hashtable();
    private static Hashtable<String,Double> class_term_total=new Hashtable();
    Prediction() throws FileNotFoundException,IOException{
        //计算class_prob
        BufferedReader reader=new BufferedReader(new FileReader(new File(Util.OUTPUT_PATH+"part-r-00000")));
        double file_total=0;
        while(reader.ready()){
            String line=reader.readLine();
            String[] args=line.split("\t");
            file_total+=Double.valueOf(args[1]);
        }
        reader=new BufferedReader(new FileReader(new File(Util.OUTPUT_PATH+"part-r-00000")));
        while(reader.ready()){
            String line=reader.readLine();
            String[] args=line.split("\t");
            class_prob.put(args[0],Double.valueOf(args[1])/file_total);
            System.out.println(String.format(("%s:%f"),args[0],Double.valueOf(args[1])/file_total));
        }

        //计算class-term prob
        reader=new BufferedReader(new FileReader(new File(Util.OUTPUT_PATH1+"part-r-00000")));
        while(reader.ready()){
            String line=reader.readLine();
            String[] args=line.split("\t");
            double count=Double.valueOf(args[1]);
            String classname=args[0].split("&")[0];
            class_term_total.put(classname,class_term_total.getOrDefault(classname,0.0)+count);
        }
        reader=new BufferedReader(new FileReader(new File(Util.OUTPUT_PATH1+"part-r-00000")));
        while(reader.ready()){
            String line=reader.readLine();
            String[] args=line.split("\t");
            double count=Double.valueOf(args[1]);
            String classname=args[0].split("&")[0];
            String term=args[0].split("&")[1];
            Map<String,String> map=new HashMap();
            map.put(classname,term);
            class_term_prob.put(map,count/class_term_total.get(classname));
        }

    }
    public static double conditionalProbabilityForClass(String content,String classname){
        double result=0;
        String[] words=content.split("\n");
        for(String word:words){
            Map<String,String> map=new HashMap();
            map.put(classname,word);
            result+=Math.abs(Math.log(class_term_prob.getOrDefault(map,1.0)));
        }
        result+=Math.abs(Math.log(class_prob.get(classname)));
        return result;
    }
    private static class PredictionMapper extends Mapper<Text,Text,Text,Text>{
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            super.map(key, value, context);
        }
    }
    private static class PredictionReducer extends Reducer<Text,Text,Text,Text>{
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            super.reduce(key, values, context);
        }
    }
    public static void main(String[] args) throws Exception{
        //生成随机测试文件
        Util.createRandomTest();
        Prediction prediction=new Prediction();
        Configuration conf=new Configuration();
        Job job=Job.getInstance(conf,"prediction");
        job.setJarByClass(Prediction.class);
        job.setMapperClass(PredictionMapper.class);
        job.setReducerClass(PredictionReducer.class);
        job.setInputFormatClass(PredictTestInputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        File out=new File(Util.OUTPUT_PATH2);
        if(out.exists()){
            Util.deleteDir(out);
        }
        for(String classname:Util.CLASS_NAMES){
            FileInputFormat.addInputPath(job,new Path(Util.INPUT_PATH_TEST+classname));
        }
        FileOutputFormat.setOutputPath(job,new Path(Util.OUTPUT_PATH2));
        job.waitForCompletion(true);

        //测试conditionalProbabilityForClass函数是否可用
//        BufferedReader reader=new BufferedReader(new FileReader(new File(Util.INPUT_PATH_TEST+"1.txt")));
//        String content="";
//        while(reader.ready()){
//            content+=reader.readLine()+"\n";
//        }
//        for(String classname:Util.CLASS_NAMES){
//            System.out.println(String.format("%s prob:%f",classname,conditionalProbabilityForClass(content,classname)));
//        }
    }
}
