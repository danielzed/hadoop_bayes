#!/bin/sh
rm cc.jar
jar cf cc.jar WordCount*.class IntSum*.class Tokenizer*.class Classname*.class Util.class
hadoop jar cc.jar WordCount
