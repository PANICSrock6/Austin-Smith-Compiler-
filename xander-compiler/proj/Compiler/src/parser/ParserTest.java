package parser;

import parser.Parser;

public class ParserTest {
	
	 public static void main(String[] args) {
		 
	        Parser parse = new Parser( "parserTest.txt");
	        System.out.println("Begin test one");
	        parse.program();
	        System.out.println("End test one");
	        
	        System.out.println();
	        System.out.println();
	        
	        Parser parse2 = new Parser( "parserTest2.txt");
	        System.out.println("Begin test two");
	        parse2.program();
	        System.out.println("End test two");
	        
	        System.out.println();
	        System.out.println();
	        
	        Parser parse3 = new Parser( "parserTest3.txt");
	        System.out.println("Begin test three");
	        parse3.program();
	        System.out.println("End test three");
	    }
	
}
