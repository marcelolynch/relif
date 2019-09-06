java -jar grammar\jflex-full-1.7.0.jar -nobak grammar\lexer.flex -d .
java -jar grammar\java-cup-11b.jar -destdir . grammar\parser.cup