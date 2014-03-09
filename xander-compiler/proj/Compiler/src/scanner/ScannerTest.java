package scanner;

import java.io.File;
import java.util.Hashtable;

public class ScannerTest {

	public static void main(String[] args) {
		String filename = args[0];
        // TODO code application logic here
        File input = new File( filename);
        Hashtable symbols = new LookupTable();
        Scanner scan = new Scanner( input, symbols);
        while( scan.nextToken() != NextTokenReturnValue.INPUT_COMPLETE) {
            //System.out.println("Foo is " + foo);
            TokenTypes at = scan.getToken();
            Object attr = scan.getAttribute();
            System.out.println("Token: " + at + "   Attribute: [" + attr + "]");
        }
	}
}
