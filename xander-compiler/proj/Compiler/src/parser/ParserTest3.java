package parser;

public class ParserTest3 {

	public static void main(String[] args) {
		Parser parseError2 = new Parser( "parserTestError2.txt");
	    System.out.println("Begin error test one, missing bracket in array. Error expected in type function");
	    parseError2.program();
	    System.out.println("End error test two");
	}

}
