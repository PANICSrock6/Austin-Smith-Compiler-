package parser;

import java.io.File;
import java.util.Hashtable;

import scanner.Scanner;
import scanner.TokenTypes;
import scanner.LookupTable;
import scanner.NextTokenReturnValue;

public class Parser {
	
	private Scanner scanner;
	private TokenTypes currentToken;
	
	/**
	 * Constructor for new Parser object
	 * @param filename
	 */
	public Parser( String filename) {
        File input = new File( filename);
        Hashtable symbols = new LookupTable();
        scanner = new Scanner( input, symbols);
        
        scanner.nextToken();
        // presume this succeeds
        currentToken = scanner.getToken();
    }
	
	/**
	 * Parses a pascal program.
	 */
	public void program() {
		System.out.println("program");
		match(TokenTypes.PROGRAM);
		match(TokenTypes.ID);
		match(TokenTypes.SEMICOLON);
		declarations();
		subprogram_declarations();
		compound_statement();
		match(TokenTypes.PERIOD);
	}
	
	/**
	 * Identifier list. parses from one to any number of identifiers separated by commas
	 */
	public void identifier_list() {
		System.out.println("identifier_list");
		match( TokenTypes.ID);
		while( currentToken == TokenTypes.COMMA) {
			match( TokenTypes.COMMA);
			identifier_list();
		}
	}
	
	/**
	 * Parses var declarations, can also be empty.
	 */
	public void declarations() {
		System.out.println("declarations");
		if( currentToken == TokenTypes.VAR) {
			match( TokenTypes.VAR);
			identifier_list();
			match( TokenTypes.COLON);
			type();
			match( TokenTypes.SEMICOLON);
			declarations();
		}
		else {
			
		}
	}
	
	/**
	 * Parses arrays and standard types.
	 */
	public void type() {
		System.out.println("type");
		if( currentToken == TokenTypes.ARRAY) {
			match( TokenTypes.ARRAY);
			match( TokenTypes.LEFT_BRACKET);
			match( TokenTypes.NUMBER);
			match( TokenTypes.COLON);
			match( TokenTypes.NUMBER);
			match( TokenTypes.RIGHT_BRACKET);
			match( TokenTypes.OF);
			standard_type();
		}
		else {
			standard_type();
		}
	}
	
	/**
	 * Parses standard types integer and real.
	 */
	public void standard_type() {
		System.out.println("standard type");
		if( currentToken == TokenTypes.INTEGER) {
			match( TokenTypes.INTEGER);
		}
		else if ( currentToken == TokenTypes.REAL) {
			match( TokenTypes.REAL);
		}
		else {
			error();
		}
	}
	
	/**
	 * Parses any number of subprogram declarations
	 */
	public void subprogram_declarations() {
		System.out.println("subprogram declarations");
		while( currentToken == TokenTypes.FUNCTION) {
			subprogram_declaration();
			match( TokenTypes.SEMICOLON);
		}
	}
	
	/**
	 * Parses the contents of a subprogram declaration
	 */
	public void subprogram_declaration() {
		System.out.println("subprogram declaration");
		subprogram_head();
		declarations();
		subprogram_declarations();
		compound_statement();
	}
	
	/**
	 * Parses subprogram head containing a function or procedure declaration.
	 */
	public void subprogram_head() {
		System.out.println("subprogram head");
		if( currentToken == TokenTypes.FUNCTION) {
			match( TokenTypes.FUNCTION);
			match( TokenTypes.ID);
			arguments();
			match( TokenTypes.COLON);
			standard_type();
			match( TokenTypes.SEMICOLON);
		}
		else if( currentToken == TokenTypes.PROCEDURE) {
			match( TokenTypes.PROCEDURE);
			match( TokenTypes.ID);
			arguments();
			match( TokenTypes.SEMICOLON);
		}
		else {
			error();
		}
	}
	
	/**
	 * Parses the arguments list.
	 */
	public void arguments() {
		System.out.println("arguments");
		if( currentToken == TokenTypes.LEFT_PAREN) {
			match( TokenTypes.LEFT_PAREN);
			parameter_list();
			match( TokenTypes.RIGHT_PAREN);
		}
		else {
			
		}
	}
	
	/**
	 * Parses parameter list.
	 */
	public void parameter_list() {
		System.out.println("parameter list");
		identifier_list();
		match( TokenTypes.COLON);
		type();
		while( currentToken == TokenTypes.SEMICOLON) {
			match( TokenTypes.SEMICOLON);
			identifier_list();
			match( TokenTypes.COLON);
			type();
		}
	}
	
	/**
	 * Parses compound statement
	 */
	public void compound_statement() {
		System.out.println("compound statements");
		match( TokenTypes.BEGIN);
		optional_statements();
		match( TokenTypes.END);
	}
	
	/**
	 * Parses optional statements.
	 */
	public void optional_statements() {
		System.out.println("optional statements");
		if( currentToken == TokenTypes.ID || currentToken == TokenTypes.BEGIN || currentToken == TokenTypes.IF || currentToken == TokenTypes.WHILE) {
			statement_list();
		}
		else {
			
		}
	}
	
	/**
	 * Parses statement list, any number of statements separated by semicolons.
	 */
	public void statement_list() {
		System.out.println("statement list");
		statement();
		while( currentToken == TokenTypes.SEMICOLON) {
			match( TokenTypes.SEMICOLON);
			statement();
		}
	}
	
	/**
	 * Parses a statement 
	 */
	public void statement() {
		System.out.println("statement");
		if( currentToken == TokenTypes.ID) {
			variable();
			match( TokenTypes.ASSIGN);
			expression();
		}
		else if( currentToken == TokenTypes.BEGIN) {
			compound_statement();
		}
		else if( currentToken == TokenTypes.IF) {
			match( TokenTypes.IF);
			expression();
			match( TokenTypes.THEN);
			statement();
			match( TokenTypes.ELSE);
			statement();
		}
		else if( currentToken == TokenTypes.WHILE) {
			match( TokenTypes.WHILE);
			expression();
			match( TokenTypes.DO);
			statement();
		}
	}
	
	/**
	 * Parses variable
	 */
	public void variable() {
		System.out.println("variable");
		match( TokenTypes.ID);
		if( currentToken == TokenTypes.LEFT_BRACKET) {
			match( TokenTypes.LEFT_BRACKET);
			expression();
			match( TokenTypes.RIGHT_BRACKET);
		}
	}
	
	/**
	 * Parses a procedure statement.
	 */
	public void procedure_statement() {
		System.out.println("procedure statement");
		match( TokenTypes.ID);
		if( currentToken == TokenTypes.LEFT_PAREN) {
			match( TokenTypes.LEFT_PAREN);
			expression_list();
			match( TokenTypes.RIGHT_PAREN);
		}
	}
	
	/**
	 * Parses an expression list.
	 */
	public void expression_list() {
		System.out.println("expression list");
		expression();
		while( currentToken == TokenTypes.COMMA) {
			expression();
		}
	}
	
	/**
	 * Parses an expression.
	 */
	public void expression() {
        System.out.println("expression");
        term();
        while( currentToken == TokenTypes.MINUS || currentToken == TokenTypes.PLUS) {
            addop();
            term();
        }
    }
	
	/**
	 * Parses a simple expression.
	 */
	public void simple_expression() {
		if( currentToken == TokenTypes.PLUS || currentToken == TokenTypes.MINUS) {
			sign();
			term();
			simple_part();
		}
		else {
			term();
			simple_part();
		}
	}
	
	/**
	 * Parses simple part.
	 */
	public void simple_part() {
		if( currentToken == TokenTypes.PLUS || currentToken == TokenTypes.MINUS) {
			addop();
			term();
			simple_part();
		}
		else {
			
		}
	}
	
	/**
	 * Parses addop which includes +, -, and or.
	 */
	public void addop() {
        System.out.println("addop");
        if( currentToken == TokenTypes.MINUS) {
            match( TokenTypes.MINUS);
        }
        else if ( currentToken == TokenTypes.PLUS) {
            match( TokenTypes.PLUS);
        }
        else if( currentToken == TokenTypes.OR) {
        	match( TokenTypes.OR);
        }
    }
	
	/**
	 * Parses term.
	 */
	public void term() {
        System.out.println("term");
        factor();
        term_part();
    }
	
	/**
	 * Parses term part.
	 */
	public void term_part() {
		if( currentToken == TokenTypes.MULTIPLY || currentToken == TokenTypes.DIVIDE) {
			mulop();
			factor();
			term_part();
		}
		else {
			
		}
	}
	
	/**
	 * Parses mulop which includes /, and *.
	 */
	public void mulop() {
        System.out.println("mulop");
        if( currentToken == TokenTypes.DIVIDE) {
            match( TokenTypes.DIVIDE);
        }
        else if( currentToken == TokenTypes.MULTIPLY) {
            match( TokenTypes.MULTIPLY);
        }
        else if( currentToken == TokenTypes.DIV) {
        	match( TokenTypes.DIV);
        }
        else if( currentToken == TokenTypes.MOD) {
        	match( TokenTypes.MOD);
        }
        else if( currentToken == TokenTypes.AND) {
        	match( TokenTypes.AND);
        }
    }
	
	/**
	 * Parses factor
	 */
	public void factor() {
        System.out.println("factor");
        if( currentToken == TokenTypes.ID) {
        	match( TokenTypes.ID);
        	if( currentToken == TokenTypes.LEFT_BRACKET) {
        		match( TokenTypes.LEFT_BRACKET);
        		expression();
        		match( TokenTypes.RIGHT_BRACKET);
        	}
        	else if( currentToken == TokenTypes.LEFT_PAREN) {
        		match(TokenTypes.LEFT_PAREN);
        		expression_list();
        		match( TokenTypes.RIGHT_PAREN);
        	}
        	else {
        		
        	}
        }
        else if( currentToken == TokenTypes.NUMBER) {
            match( TokenTypes.NUMBER);
        }
        else if( currentToken == TokenTypes.LEFT_PAREN) {
        	match( TokenTypes.LEFT_PAREN);
        	expression();
        	match( TokenTypes.RIGHT_PAREN);
        }
        else if( currentToken == TokenTypes.NOT) {
        	
        }
        else {
            System.out.println("Error in Factor. Saw " + currentToken);
            error();
        }
    }
	
	/**
	 * Parses sign
	 */
	public void sign() {
		if( currentToken == TokenTypes.PLUS) {
			match( TokenTypes.PLUS);
		}
		else if( currentToken == TokenTypes.MINUS) {
			match( TokenTypes.MINUS);
		}
		else {
			error();
		}
	}
	
	/**
	 * Matches the expected token with the current token, errors if they don't match.
	 * @param expectedToken
	 */
	public void match( TokenTypes expectedToken) {
        System.out.println("match " + expectedToken + " with current " + currentToken + ":" + scanner.getAttribute());
        if( currentToken == expectedToken) {
            NextTokenReturnValue scanResult = scanner.nextToken();
            switch( scanResult) {
                case TOKEN_AVAILABLE:
                    currentToken = scanner.getToken();
                    break;
                case TOKEN_NOT_AVAILABLE: // Handle no token case here
                    break;
                case INPUT_COMPLETE: // Handle done case here
                    break;
            }
        }
        else {
            error();  // We don't match!
        }
    }
	
	/**
	 * Throws an error statement and exits.
	 */
	public void error() {
        System.out.println("Error");
        System.exit( 1);
    }
	
}
