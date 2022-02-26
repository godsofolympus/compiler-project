import java_cup.runtime.*;

%%

/* options */
%class Lexer
%public
%unicode
%cup
%line
%column

/* copied code */
%{
    StringBuffer string = new StringBuffer(); /* used for detecting string literals*/

    /* used for cup*/
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);  
    }
%}

/* macros */
letter=[A-Za-z]
digit=[0-9]
hexadigit = {digit} | [a-fA-F]
floatingpoint = ({digit}+\.{digit}*)
underline="_"
endLine = \n|\r|\r\n
whitespace={endline} | [\t\f]
identifier={letter}({digit} | {letter} | {underline})*
decimal=({digit})+
hexadecimal= 0[xX]({hexadigit})+
scientificFloat = {floatingpoint}[Ee][+-]?{decimal}
floatingPointAll = {floatingpoint} | {scientificFloat}
singleLineComment = \/\/([^\r\n])*
multiLineComment= \/\*( [^*] | (\*+[^*/]) )*\*+\/

StringChar=[^\n\r\"\\]+

/* states or xstates*/
%state STRING

%%

/* keywords */
<YYINITIAL> {
    "__func__" { return symbol(sym.FUNC); }
    "__line__" { return symbol(sym.LINE); }
    "bool" { return symbol(sym.BOOL); }
    "break" { return symbol(sym.BREAK); }
    "btoi" { return symbol(sym.BTOI); }
    "class" { return symbol(sym.CLASS); }
    "continue" { return symbol(sym.CONTINUE); }
    /* define is not present since the preprocessor stage */
    "dtoi" { return symbol(sym.DTOI); }
    "else" { return symbol(sym.ELSE); }
    "for" { return symbol(sym.FOR); }
    "if" { return symbol(sym.IF); }
    "important" { return symbol(sym.IMPORTANT); }
    "int" { return symbol(sym.ITOB); }
    "NewArray" { return symbol(sym.NEW_ARRAY); }
    "null" { return symbol(sym.NULL); }
    "Print" { return symbol(sym.PRINT); }
    "private" { return symbol(sym.PRIVATE); }
    "public" { return symbol(sym.PUBLIC); }
    "ReadInteger" { return symbol(sym.READ_INTEGER); }
    "ReadLine" { return symbol(sym.READ_LINE); }
    "return" { return symbol(sym.RETURN); }
    "string" { return symbol(sym.STRING); }
    "this" { return symbol(sym.THIS); }
    "void" { return symbol(sym.VOID); }
    "while" { return symbol(sym.WHILE); }
}

/* operators */
<YYINITIAL> {
    "+" {return symbol(sym.PLUS); }
    "-" {return symbol(sym.MINUS); }
    "*" {return symbol(sym.MULTIPLY); }
    "/" {return symbol(sym.DIVIDE); }
    "%" {return symbol(sym.MOD); }
    "<" {return symbol(sym.LESS); }
    "<=" {return symbol(sym.LESS_EQUAL); }
    ">" {return symbol(sym.GREATER); }
    ">=" {return symbol(sym.GREATER_EQUAL); }
    "=" {return symbol(sym.EQUAL); }
    "+=" {return symbol(sym.PLUS_EQUAL); }
    "-=" {return symbol(sym.MINUS_EQUAL); }
    "*=" {return symbol(sym.MULTIPLY_EQUAL); }
    "/=" {return symbol(sym.DIVIDE_EQUAL); }
    "==" {return symbol(sym.EQUAL_EQUAL); }
    "!=" {return symbol(sym.NOT_EQUAL); }
    "&&" {return symbol(sym.AND_AND); }
    "||" {return symbol(sym.OR_OR); }
    "!" {return symbol(sym.NOT); }
    ";" {return symbol(sym.SEMICOLON); }
    "," {return symbol(sym.COMMA); }
    "." {return symbol(sym.DOT); }
    "[" {return symbol(sym.SQUARE_BRACKET_LEFT); }
    "]" {return symbol(sym.SQUARE_BRACKET_RIGHT); }
    "(" {return symbol(sym.PARENTHESIS_LEFT); }
    ")" {return symbol(sym.PARENTHESIS_RIGHT); }
    "{" {return symbol(sym.CURLY_BRACKET_LEFT); }
    "}" {return symbol(sym.CURLY_BRACKET_RIGHT); }
}

/* identifier rules*/

<YYINITIAL> {
    
}

/* string rules */

<YYINITIAL> {

    "\"" { string.setLength(0);
            yybegin(STRING); }
}

<STRING> {
    "\"" { yybegin(YYINITIAL);
            return symbol(sym.STRING_LITERAL,
            string.toString()); }

    {StringChar} {string.append(yytext()); }

    "\t" {string.append('\t'); }
    "\n" {string.append('\n'); }
    "\r" {string.append('\r'); }
    "\\\"" {string.append('\"'); }
    "\\" {string.append('\\'); }
}

/* error fallback */
