@echo off 
javac *.java
rmic RMIImpl
start rmiregistry