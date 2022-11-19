@echo off
javac ArithmeticInterface.java
javac ArithmeticServer.java
javac ArithmeticRMIImpl.java
rmic ArithmeticRMIImpl
javac CalculatorRMIClient.java
pause