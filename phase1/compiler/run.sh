#!/bin/bash
javac Main.java
java Main
jflex Lexer.jflex
javac Lexer.java
java Lexer processed.txt