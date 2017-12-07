#!/bin/sh
cd out/production/hadoop_bayes
rm cc.jar
jar cf cc.jar WordCount*.class IntSum*.class Tokenizer*.class Classname*.class
hadoop jar cc.jar WordCount
