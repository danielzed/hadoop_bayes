import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;
import org.apache.hadoop.util.LineReader;

import java.io.IOException;
import java.rmi.server.ExportException;

public class ClassTermRecordReader extends RecordReader<Text,Text>{
    private Text key=new Text();
    private Text value=new Text();
    FileSplit fileSplit;
    Configuration conf;
    LineRecordReader reader=new LineRecordReader();
    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException{
        fileSplit=(FileSplit)inputSplit;
        conf=taskAttemptContext.getConfiguration();
        reader.initialize(inputSplit,taskAttemptContext);
    }

    @Override
    public boolean nextKeyValue() throws IOException{
        if(reader.nextKeyValue()){
            key.set(Util.getClassname(fileSplit.getPath().toString()));
            value.set(reader.getCurrentValue());
            return true;
        }
        return false;
    }

    @Override
    public Text getCurrentKey() {
        return key;
    }

    @Override
    public Text getCurrentValue() {
        return value;
    }

    @Override
    public float getProgress() {
        try{
            return reader.getProgress();
        }catch (IOException e){

        }
        return 0;
    }

    @Override
    public void close() {

    }
}
