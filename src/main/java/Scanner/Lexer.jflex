package Scanner;

import java_cup.sym;
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
    public static final int T_KEYWORD = 0;
    public static final int T_OPERATOR = 1;
    public static final int T_INTLITERAL = 2;
    public static final int T_DOUBLELITERAL = 3;
    public static final int T_BOOLEANLITERAL = 4;
    public static final int T_ID = 5;
    public static final int T_STRINGLITERAL = 6;

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
Identifier={Letter}({Digit} | {Letter} | {Underline})*
DoubleLiteral = {DoubleNormal} | {DoubleScientific}
IntLiteral = {Hexadecimal} | {Decimal}
BooleanLiteral = "true" | "false"
Comment = {SingleLineComment} | {MultiLineComment}

DoubleNormal = ({Digit}+\.{Digit}* | {Digit}*\.{Digit}+)
DoubleScientific = {DoubleNormal}[Ee][+-]?{Decimal}
Decimal=({Digit})+
Hexadecimal= 0[xX]({Hexadigit})+
SingleLineComment = \/\/([^\r\n])*
MultiLineComment= \/\*( [^*] | (\*+[^*/]) )*\*+\/
Whitespace={EndLine} | [ \t\f]

Letter=[A-Za-z]
Hexadigit = {Digit} | [a-fA-F]
Digit=[0-9]
Underline="_"
EndLine = \n|\r|\r\n
StringCharset=[^\n\r\"\\]+

/* states or xstates*/
%state STRING

%%

/* keywords */
<YYINITIAL> {
    "__func__"      { return symbol( T_KEYWORD, yytext()); }
    "__line__"      { return symbol( T_KEYWORD, yytext()); }
    "bool"          { return symbol( T_KEYWORD, yytext()); }
    "break"         { return symbol( T_KEYWORD, yytext()); }
    "btoi"          { return symbol( T_KEYWORD, yytext()); }
    "class"         { return symbol( T_KEYWORD, yytext()); }
    "continue"      { return symbol( T_KEYWORD, yytext()); }
    "dtoi"          { return symbol( T_KEYWORD, yytext()); }
    "else"          { return symbol( T_KEYWORD, yytext()); }
    "for"           { return symbol( T_KEYWORD, yytext()); }
    "if"            { return symbol( T_KEYWORD, yytext()); }
    "important"     { return symbol( T_KEYWORD, yytext()); }
    "int"           { return symbol( T_KEYWORD, yytext()); }
    "NewArray"      { return symbol( T_KEYWORD, yytext()); }
    "null"          { return symbol( T_KEYWORD, yytext()); }
    "Print"         { return symbol( T_KEYWORD, yytext()); }
    "private"       { return symbol( T_KEYWORD, yytext()); }
    "public"        { return symbol( T_KEYWORD, yytext()); }
    "ReadInteger"   { return symbol( T_KEYWORD, yytext()); }
    "ReadLine"      { return symbol( T_KEYWORD, yytext()); }
    "return"        { return symbol( T_KEYWORD, yytext()); }
    "string"        { return symbol( T_KEYWORD, yytext()); }
    "this"          { return symbol( T_KEYWORD, yytext()); }
    "void"          { return symbol( T_KEYWORD, yytext()); }
    "while"         { return symbol( T_KEYWORD, yytext()); }
}

/* operators */
<YYINITIAL> {
    "+"     { return symbol(T_OPERATOR, yytext()); }
    "-"     { return symbol(T_OPERATOR, yytext()); }
    "*"     { return symbol(T_OPERATOR, yytext()); }
    "/"     { return symbol(T_OPERATOR, yytext()); }
    "%"     { return symbol(T_OPERATOR, yytext()); }
    "<"     { return symbol(T_OPERATOR, yytext()); }
    "<="    { return symbol(T_OPERATOR, yytext()); }
    ">"     { return symbol(T_OPERATOR, yytext()); }
    ">="    { return symbol(T_OPERATOR, yytext()); }
    "="     { return symbol(T_OPERATOR, yytext()); }
    "+="    { return symbol(T_OPERATOR, yytext()); }
    "-="    { return symbol(T_OPERATOR, yytext()); }
    "*="    { return symbol(T_OPERATOR, yytext()); }
    "/="    { return symbol(T_OPERATOR, yytext()); }
    "=="    { return symbol(T_OPERATOR, yytext()); }
    "!="    { return symbol(T_OPERATOR, yytext()); }
    "&&"    { return symbol(T_OPERATOR, yytext()); }
    "||"    { return symbol(T_OPERATOR, yytext()); }
    "!"     { return symbol(T_OPERATOR, yytext()); }
    ";"     { return symbol(T_OPERATOR, yytext()); }
    ","     { return symbol(T_OPERATOR, yytext()); }
    "."     { return symbol(T_OPERATOR, yytext()); }
    "["     { return symbol(T_OPERATOR, yytext()); }
    "]"     { return symbol(T_OPERATOR, yytext()); }
    "("     { return symbol(T_OPERATOR, yytext()); }
    ")"     { return symbol(T_OPERATOR, yytext()); }
    "{"     { return symbol(T_OPERATOR, yytext()); }
    "}"     { return symbol(T_OPERATOR, yytext()); }
}

/* Identifier, literal and ignored rules*/

<YYINITIAL> {
    {IntLiteral}        {return symbol(T_INTLITERAL, yytext()); } 
    {DoubleLiteral}     {return symbol(T_DOUBLELITERAL, yytext()); }
    {BooleanLiteral}    {return symbol(T_BOOLEANLITERAL, yytext()); }
    {Identifier}        {return symbol(T_ID, yytext()); }
    {Whitespace}        { /* ignore */}
    {Comment}           { /* ignore */}
}

/* string rules */

<YYINITIAL> {

    "\"" { string.setLength(0);
            yybegin(STRING); }
}

<STRING> {
    "\"" { yybegin(YYINITIAL);
            return symbol(T_STRINGLITERAL, string.toString()); }

    {StringCharset} {string.append(yytext()); }

    "\\t" {string.append('\t'); }
    "\\n" {string.append('\n'); }
    "\\r" {string.append('\r'); }
    "\\\"" {string.append('\"'); }
    "\\\\" {string.append('\\'); }
}

/* error fallback */
[^] { throw new Error("Illegal character <" + yytext() + ">"); }