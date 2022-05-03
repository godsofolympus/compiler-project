package compiler.scanner;

import java_cup.runtime.*;
import compiler.parser.sym;

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
    StringBuffer string = new StringBuffer();
    
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
DoubleNormal = ({Digit}+\.{Digit}*)
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
StringCharset=([^\n\r\"\\]+ | {EsapeCharset})
EsapeCharset=("\\t" | "\\n" | "\\r" | "\\\"" | "\\'" | "\\\\")

/* states or xstates*/
%state STRING

%%

/* keywords */
<YYINITIAL> {
    "void"          { return symbol( sym.VOID }
    "int"          { return symbol( sym.INT }
    "double"          { return symbol( sym.DOUBLE }
    "bool"          { return symbol( sym.BOOL }
    "string"          { return symbol( sym.STRING }
    "class"          { return symbol( sym.CLASS }
    "interface"          { return symbol( sym.INTERFACE }
    "null"          { return symbol( sym.NULL }
    "this"          { return symbol( sym.THIS }
    "extends"          { return symbol( sym.EXTENDS }
    "implements"          { return symbol( sym.IMPLEMENTS }
    "for"          { return symbol( sym.FOR }
    "while"          { return symbol( sym.WHILE }
    "if"          { return symbol( sym.IF }
    "else"          { return symbol( sym.ELSE }
    "return"          { return symbol( sym.RETURN }
    "break"          { return symbol( sym.BREAK }
    "continue"          { return symbol( sym.CONTINUE }
    "new"          { return symbol( sym.NEW }
    "NewArray"          { return symbol( sym.NewArray }
    "Print"          { return symbol( sym.Print }
    "ReadInteger"          { return symbol( sym.ReadInteger }
    "ReadLine"          { return symbol( sym.ReadLine }
    "dtoi"          { return symbol( sym.DTOI }
    "itod"          { return symbol( sym.ITOD }
    "btoi"          { return symbol( sym.BTOI }
    "itob"          { return symbol( sym.ITOB }
    "private"          { return symbol( sym.PRIVATE }
    "protected"          { return symbol( sym.PROTECTED }
    "public"          { return symbol( sym.PUBLIC }
    "import"          { return symbol( sym.IMPORT }

}

/* operators */
<YYINITIAL> {
    "+"     { return symbol( sym.PLUS }
    "-"     { return symbol( sym.MINUS }
    "*"     { return symbol( sym.MULTIPLY }
    "/"     { return symbol( sym.DIVIDE }
    "%"     { return symbol( sym.MOD }
    "<"     { return symbol( sym.LESS }
    "<="    { return symbol( sym.LESS_EQUAL }
    ">"     { return symbol( sym.GREATER }
    ">="    { return symbol( sym.GREATER_EQUAL }
    "="     { return symbol( sym.EQUAL }
    "=="    { return symbol( sym.EQUAL_EQUAL }
    "!="    { return symbol( sym.NOT_EQUAL }
    "&&"    { return symbol( sym.AND_AND }
    "||"    { return symbol( sym.OR_OR }
    "!"     { return symbol( sym.NOT }
    ";"     { return symbol( sym.SEMICOLON }
    ","     { return symbol( sym.COMMA }
    "."     { return symbol( sym.DOT }
    "["     { return symbol( sym.BRACKETS_LEFT }
    "]"     { return symbol( sym.BRACKETS_RIGHT }
    "("     { return symbol( sym.PARANTHESIS_LEFT }
    ")"     { return symbol( sym.PARANTHESIS_RIGHT }
    "{"     { return symbol( sym.CURLY_BRACKETS_LEFT }
    "}"     { return symbol( sym.CURLY_BRACKETS_RIGHT }
}

/* Identifier, literal and ignored rules*/

<YYINITIAL> {
    {IntLiteral}        {return symbol(sym.INT_CONSTANT, Integer.parseInt(yytext())); } 
    {DoubleLiteral}     {return symbol(sym.DOUBLE_CONSTANT, Double.parseDouble(yytext())); }
    {BooleanLiteral}    {return symbol(sym.BOOL_CONSTANT, Boolean.ParseBoolean(yytext())); }
    {Identifier}        {return symbol(IDENTIFIER, yytext()); }
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
            return symbol(sym.STRING_CONSTANT, string.toString()); }

    {StringCharset} {string.append(yytext()); }
}

<<EOF>>     { return symbol(sym.EOF); }

/* error fallback */
[^] { throw new Error("Illegal character <" + yytext() + ">"); }