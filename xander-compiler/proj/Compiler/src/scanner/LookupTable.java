package scanner;

import java.util.Hashtable;

public class LookupTable extends Hashtable {
	public LookupTable() {
        super();
        this.put("array", TokenTypes.ARRAY);
        this.put("if", TokenTypes.IF);
        this.put("else", TokenTypes.ELSE);
        this.put("while", TokenTypes.WHILE);
        this.put("do", TokenTypes.DO);
        this.put("or", TokenTypes.OR);
        this.put("mod", TokenTypes.MOD);
        this.put("div", TokenTypes.DIV);
        this.put("and", TokenTypes.AND);
        this.put("var", TokenTypes.VAR);
        this.put("integer", TokenTypes.INTEGER);
        this.put("real", TokenTypes.REAL);
        this.put("function", TokenTypes.FUNCTION);
        this.put("begin", TokenTypes.BEGIN);
        this.put("end", TokenTypes.END);
        this.put("of", TokenTypes.OF);
        this.put("then", TokenTypes.THEN);
        this.put("procedure", TokenTypes.PROCEDURE);
        this.put("program", TokenTypes.PROGRAM);
        this.put("not", TokenTypes.NOT);
        
        this.put(";", TokenTypes.SEMICOLON);
        this.put(".", TokenTypes.PERIOD);
        this.put(",", TokenTypes.COMMA);
        this.put(":", TokenTypes.COLON);
        this.put("(", TokenTypes.LEFT_PAREN);
        this.put(")", TokenTypes.RIGHT_PAREN);
        this.put("[", TokenTypes.LEFT_BRACKET);
        this.put("]", TokenTypes.RIGHT_BRACKET);
        this.put("{", TokenTypes.LEFT_CURL_BRACKET);
        this.put("}", TokenTypes.RIGHT_CURL_BRACKET);
        this.put("=", TokenTypes.EQUALS);
        this.put("<", TokenTypes.LESS_THAN);
        this.put(">", TokenTypes.GREATER_THAN);
        this.put("+", TokenTypes.PLUS);
        this.put("-", TokenTypes.MINUS);
        this.put("*", TokenTypes.MULTIPLY);
        this.put("/", TokenTypes.DIVIDE);
        this.put("<=", TokenTypes.LESS_THAN_EQUAL);
        this.put(">=", TokenTypes.GREATER_THAN_EQUAL);
        this.put("<>", TokenTypes.NOTEQUAL);
        this.put(":=", TokenTypes.ASSIGN);
    }
}
