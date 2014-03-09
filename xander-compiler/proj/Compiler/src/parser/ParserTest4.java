package parser;

public class ParserTest4 {

	public static void main(String[] args) {
		Parser parseError3 = new Parser( "parserTestError3.txt");
	    System.out.println("Begin error test one, missing d in 'end'. Error expected in compound_statement()");
	    parseError3.program();
	    System.out.println("End error test three");

	}

}
