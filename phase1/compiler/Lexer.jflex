/* imports (user code): only java_cup.runtime is needed */
import java_cup.runtime.*;

%%

/* options */

/* name of the output class */
%class Lexer
%public
%unicode


/* needed for cup compatibility */
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


/* states or xstates,YYINITIAL is predefined*/
%state STRING
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

%%

/* rules 
format:
<STRING> {
    expr1 { action1 }
    expr2 { action2 }
}
the longest match will take  precedence.
*/

/* keywords */

/* other rules */

<YYINITIAL> {
    \" { string.setLength(0); yybegin(STRING); }
}

/* error fallback */
