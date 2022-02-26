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


/* states or xstates*/
%state STRING

%%

/* keywords */

/* other rules */

<YYINITIAL> {
    \" { string.setLength(0); yybegin(STRING); }
}

<STRING> {

}

/* error fallback */
