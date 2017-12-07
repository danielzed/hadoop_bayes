import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class ClassnameRecordReader extends RecordReader<NullWritable,Text>{
    private FileSplit fileSplit;
    private Configuration conf;
    private boolean flag=true;
    private Text value=new Text();
    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) {
        fileSplit=(FileSplit)inputSplit;
        conf=taskAttemptContext.getConfiguration();
    }

    @Override
    public boolean nextKeyValue() {
        //获取filesplit路径中的类名，提取出来给value
        if(flag){
            String path=fileSplit.getPath().toString();
            value.set(Util.getClassname(path));
            flag=false;
            return true;
        }
        return false;
    }

    @Override
    public NullWritable getCurrentKey() {
        return null;
    }

    @Override
    public Text getCurrentValue() {
        return value;
    }

    @Override
    public float getProgress() {
        return flag?1:0;
    }

    @Override
    public void close() {

    }
}
