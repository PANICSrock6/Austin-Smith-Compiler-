package scanner;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.Hashtable;

public class Scanner {
	private static final int START_STATE = 0;
    private static final int IN_NUMBER_STATE = 1;
    private static final int IN_ID_STATE = 2;
    private static final int IN_LESS_THAN_STATE = 3;
    private static final int IN_GREATER_THAN_STATE = 4;
    private static final int IN_COLON_STATE = 5;
    private static final int ERROR_STATE = 50;
    private static final int SYMBOL_COMPLETE_STATE = 51;
    private static final int NUMBER_COMPLETE_STATE = 52;
    private static final int ID_COMPLETE_STATE = 53;
    private static final int SYMBOL_COMPLETE_PUSHBACK_STATE = 54;
    
    public static final int TOKEN_AVAILABLE = 0;
    public static final int TOKEN_NOT_AVAILABLE = 1;
    public static final int INPUT_COMPLETE = 2;

    private int[][] transitionTable;  // table[state][char]
    private TokenTypes token;
    private Object attribute;
    private PushbackReader inputReader;
    private Hashtable symbolTable;
    
    /**
     * Constructs a scanner with the given input file and symbol table.
     * @param inputFile		the file to scan
     * @param symbolTable		the symbol table to reference 
     */
    public Scanner( File inputFile, Hashtable symbolTable) {
        this.symbolTable = symbolTable;
        try {
            this.inputReader = new PushbackReader( new FileReader(inputFile));
        }
        catch( Exception e) {
            e.printStackTrace();
            System.exit( 1);
        }
        this.transitionTable = createTransitionTable();
    }
    
    /**
     * Gets attribute.
     * @return attribute
     */
    public Object getAttribute() { return( this.attribute);}
    
    /**
     * Gets token.
     * @return token
     */
    public TokenTypes getToken() { return( this.token);}
    
    /**
     * Gets the next token.
     * @return The next token
     */
    public NextTokenReturnValue nextToken() {
        int currentState = START_STATE;
        StringBuilder lexeme = new StringBuilder("");
        while( currentState < ERROR_STATE ) {
            char currentChar = '\0';
            try {
                currentChar = (char) inputReader.read();
            }
            catch( Exception e) {
                // End of stream here.
                System.out.println("Should return input complete");
                return( NextTokenReturnValue.INPUT_COMPLETE);
            }
            //System.out.println("current char is actually " + (int)currentChar);
            if( currentChar == 65535) return( NextTokenReturnValue.INPUT_COMPLETE); // end of file
            if( currentChar > 127) return( NextTokenReturnValue.TOKEN_NOT_AVAILABLE);
            //System.out.print("State:" + currentState + " Char: " + currentChar + " (" + (int)currentChar + ")");
            int nextState = transitionTable[currentState][currentChar];
            //System.out.println(" Next State: " + nextState + " current lexeme: ["+ lexeme.toString() + "]");
            switch( nextState) {
                // Start State
                case START_STATE:
                    lexeme = new StringBuilder("");
                    break;
                                    
                // find a number (an int at this point)
                case IN_NUMBER_STATE:
                    lexeme.append( currentChar);
                    break;
                
                // number is complete     
                case NUMBER_COMPLETE_STATE:
                    try {
                        inputReader.unread(currentChar);
                    }
                    catch( IOException ioe) {}
                    token = TokenTypes.NUMBER;
                    attribute = lexeme.toString();
                    break;
                    
                // find an id    
                case IN_ID_STATE:
                	lexeme.append( currentChar);
                	break;
                	
                // id complete	
                case ID_COMPLETE_STATE:
                	 try {
                         inputReader.unread(currentChar);
                     }
                	 catch( IOException ioe) {}
                	 token = (TokenTypes)symbolTable.get( lexeme.toString());
                	 if (token == null) {
                		 token = TokenTypes.ID;
                	 }
                	 attribute = lexeme.toString();
                     break;
                    
                // find a less than symbol     
                case IN_LESS_THAN_STATE:
                	lexeme.append( currentChar);
                	break;
                
                // find a greater than symbol
                case IN_GREATER_THAN_STATE:
                	lexeme.append( currentChar);
                	break;
                	
                // find a colon	
                case IN_COLON_STATE:
                	lexeme.append( currentChar);
                	break;
                    
                // symbol complete	
                case SYMBOL_COMPLETE_STATE:
                    // After a single symbol, just push out the single lexeme
                    lexeme.append( currentChar);
                    token = (TokenTypes)symbolTable.get( lexeme.toString());
                    attribute = lexeme.toString();
                    break;
                
                // symbol complete pushback, used when colon, less than, or greater than 
                    //are not followed by expected symbol    
                case SYMBOL_COMPLETE_PUSHBACK_STATE:
                	try {
                        inputReader.unread(currentChar);
                    }
               	 	catch( IOException ioe) {}
                	token = (TokenTypes)symbolTable.get( lexeme.toString());
                    attribute = lexeme.toString();
                    break;
                    
                // error state
                case ERROR_STATE:
                    return( NextTokenReturnValue.TOKEN_NOT_AVAILABLE);
            } // end switch
            currentState = nextState;
        } // end while currentState <= ERROR_STATE
        return( NextTokenReturnValue.TOKEN_AVAILABLE);
    }

    /**
     * Creates the transition table specified by the DFA.
     * @return transition table
     */
    private int[][] createTransitionTable() {
        return new int[][]
    {
        // State 0: Start
        {
           50,50,50,50,50,50,50,50,50, 0, 0,50,50, 0,50,50, //   0 -  15 (00-0f)
           50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50, //  16 -  31 (10-1f)
            0,50,50,50,50,50,50,50,51,51,51,51,51,51,51,51, //  32 -  47 (20-2f)
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,5,51, 3,51, 4,50, //  48 -  63 (30-3f)
           50, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, //  64 -  79 (40-4f)
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,51,50,51,50,50, //  80 -  95 (50-5f)
           50, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, //  96 - 111 (60-6f)
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,51,50,51,50,50  // 112 - 127 (70-7f)
        },
        // State 1: In Number
        {
           52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52, //   0 -  15 (00-0f)
           52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52, //  16 -  31 (10-1f)
           52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52, //  32 -  47 (20-2f)
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,52,52,52,52,52,52, //  48 -  63 (30-3f)
           52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52, //  64 -  79 (40-4f)
           52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52, //  80 -  95 (50-5f)
           52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52, //  96 - 111 (60-6f)
           52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52  // 112 - 127 (70-7f)
        },
        // State 2: In ID
        {
           53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53, //   0 -  15 (00-0f)
           53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53, //  16 -  31 (10-1f)
           53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53, //  32 -  47 (20-2f)
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,53,53,53,53,53,53, //  48 -  63 (30-3f)
           50, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, //  64 -  79 (40-4f)
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,53,53,53,53,53, //  80 -  95 (50-5f)
           53, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, //  96 - 111 (60-6f)
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,53,53,53,53,53  // 112 - 127 (70-7f)
        },
        // State 3: In Less Than
        {
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //   0 -  15 (00-0f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //  16 -  31 (10-1f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //  32 -  47 (20-2f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,51,51,54, //  48 -  63 (30-3f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //  64 -  79 (40-4f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //  80 -  95 (50-5f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //  96 - 111 (60-6f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54  // 112 - 127 (70-7f)
        },
        // State 4: In Greater Than
        {
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //   0 -  15 (00-0f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //  16 -  31 (10-1f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //  32 -  47 (20-2f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,51,54,54, //  48 -  63 (30-3f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //  64 -  79 (40-4f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //  80 -  95 (50-5f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //  96 - 111 (60-6f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54  // 112 - 127 (70-7f)
        },
        // State 5: In Colon
        {
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //   0 -  15 (00-0f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //  16 -  31 (10-1f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //  32 -  47 (20-2f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,51,54,54, //  48 -  63 (30-3f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //  64 -  79 (40-4f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //  80 -  95 (50-5f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54, //  96 - 111 (60-6f)
        	54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54  // 112 - 127 (70-7f)
        }
    };
    }
}
