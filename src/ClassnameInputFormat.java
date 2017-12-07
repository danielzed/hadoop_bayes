import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

public class ClassnameInputFormat extends FileInputFormat<NullWritable,Text>{
    @Override
    public RecordReader<NullWritable,Text> createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) {
        ClassnameRecordReader reader=new ClassnameRecordReader();
        reader.initialize(inputSplit,taskAttemptContext);
        return reader;
    }
}
