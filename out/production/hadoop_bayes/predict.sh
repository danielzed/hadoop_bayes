#!/bin/sh
rm pre.jar
jar cf pre.jar Predict*.class Util.class
hadoop jar pre.jar Prediction
