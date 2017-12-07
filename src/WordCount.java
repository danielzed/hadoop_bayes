import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, IntWritable>{

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                context.write(word, one);
            }
        }
    }

    public static class ClassTermMapper extends
            Mapper<Text,Text,Text,IntWritable>{
        private final static IntWritable one=new IntWritable(1);
        private Text word=new Text();

        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            word.set(key.toString()+"&"+value.toString());
            context.write(word,one);
        }
    }

    public static class IntSumReducer
            extends Reducer<Text,IntWritable,Text,IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }
    public static void main(String[] args) throws Exception {
        //classname count 作业配置
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "class count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setInputFormatClass(ClassnameInputFormat.class);
        //如果output_path存在，则要删除，在执行
        File out=new File(Util.OUTPUT_PATH);
        if(out.exists()){
            Util.deleteDir(out);
        }
        for(String classname:Util.CLASS_NAMES){
            FileInputFormat.addInputPath(job, new Path(Util.INPUT_PATH+classname));
        }
        FileOutputFormat.setOutputPath(job, new Path(Util.OUTPUT_PATH));
        //System.exit(job.waitForCompletion(true) ? 0 : 1);
        job.waitForCompletion(true);

        //class-term count 作业配置
        Job job1=Job.getInstance(conf,"class-term count");
        job1.setJarByClass(WordCount.class);
        job1.setMapperClass(ClassTermMapper.class);
        job1.setCombinerClass(IntSumReducer.class);
        job1.setReducerClass(IntSumReducer.class);
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(IntWritable.class);
        job1.setInputFormatClass(ClassTermInputFormat.class);
        File out1=new File(Util.OUTPUT_PATH1);
        if(out1.exists()){
            Util.deleteDir(out1);
        }
        for(String classname:Util.CLASS_NAMES){
            FileInputFormat.addInputPath(job1, new Path(Util.INPUT_PATH+classname));
        }
        FileOutputFormat.setOutputPath(job1,new Path(Util.OUTPUT_PATH1));
        job1.waitForCompletion(true);
    }
}