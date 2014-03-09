package parser;

public class ParserTest2 {
	
	public static void main(String[] args) {
		Parser parseError = new Parser( "parserTestError.txt");
        System.out.println("Begin error test one, missing semicolon. Error expected in program function");
        parseError.program();
        System.out.println("End error test one");
	}
}
