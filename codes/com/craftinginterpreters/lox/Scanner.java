package com.craftinginterpreters.lox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.craftinginterpreters.lox.TokenType.*;

class Scanner {
    private final String sourse;
    private final List<Token> tokens=new ArrayList<>();
    private int start=0;
    private int current=0;
    private int line=1;
    Scanner(String sourse){
        this.sourse=sourse;
    }

    List<Token> scanTokens(){
        while (!isAtEnd()){
            start=current;
            scanToken();
        }
        tokens.add(new Token(EOF,"",null,line));
        return tokens;
    }

    private void scanToken(){
        char c=advance();
        switch(c){
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;
            case '\n':
                line++;
                break;
            case '"': string(); break;
            default:
            Lox.error(line,"Unexpected character.");
            break;
        }
    }

    private void string(){
        while (peek()!='"' && !isAtEnd()){
            if(peek()=='\n') line++;
            advance();
        }
        if(isAtEnd()){
            Lox.error(line,"Unterminated string.");
            return;
        }
        advance();
        String value = sourse.substring(start+1,current-1);
        addToken(STRING,value);
    }

    private char peek(){
        if(isAtEnd()) return '\0';
        return sourse.charAt(current);
    }

    private boolean match(char expected){
        if(isAtEnd()) return false;
        if (sourse.charAt(current)!=expected) return false;
        current++;
        return true;
    }

    private boolean isAtEnd(){
        return current >= sourse.length();
    }

    private char advance(){
        return sourse.charAt(current++);
    }

    private void addToken(TokenType type){
        addToken(type,null);
    }

    private void addToken(TokenType type, Object literal){
        String text = sourse.substring(start,current);
        tokens.add(new Token(type,text,literal,line));
    }

}