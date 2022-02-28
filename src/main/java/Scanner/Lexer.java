/* The following code was generated by JFlex 1.7.0 */

package Scanner;

import java_cup.sym;
import java_cup.runtime.*;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.7.0
 * from the specification file <tt>Lexer.jflex</tt>
 */
public class Lexer implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int STRING = 2;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1, 1
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\21\1\26\1\0\1\21\1\17\22\0\1\21\1\55\1\61"+
    "\2\0\1\54\1\57\1\0\1\54\1\54\1\20\1\13\1\54\1\13"+
    "\1\11\1\16\1\14\11\24\1\0\1\54\1\55\1\56\1\55\2\0"+
    "\1\43\3\23\1\12\1\23\2\22\1\50\2\22\1\52\1\22\1\41"+
    "\1\22\1\45\1\22\1\47\5\22\1\15\2\22\1\54\1\27\1\54"+
    "\1\0\1\25\1\0\1\6\1\33\1\31\1\36\1\4\1\5\1\51"+
    "\1\53\1\32\1\22\1\35\1\7\1\37\1\30\1\34\1\40\1\22"+
    "\1\2\1\10\1\1\1\3\1\46\1\42\1\15\1\44\1\22\1\54"+
    "\1\60\1\54\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uff92\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\6\2\2\3\1\4\1\3\2\5\1\4"+
    "\1\1\13\2\1\3\2\1\1\6\1\7\1\1\1\10"+
    "\7\2\1\11\1\0\1\5\2\0\3\2\1\12\14\2"+
    "\1\13\1\14\1\15\1\16\1\17\6\2\1\0\1\4"+
    "\3\0\15\2\1\20\2\2\1\0\1\11\2\0\11\2"+
    "\2\0\7\2\1\0\5\2\1\0\2\2\1\12\2\2";

  private static int [] zzUnpackAction() {
    int [] result = new int[129];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\62\0\144\0\226\0\310\0\372\0\u012c\0\u015e"+
    "\0\u0190\0\u01c2\0\u01f4\0\u0226\0\u0258\0\u028a\0\144\0\u02bc"+
    "\0\u02ee\0\u0320\0\u0352\0\u0384\0\u03b6\0\u03e8\0\u041a\0\u044c"+
    "\0\u047e\0\u04b0\0\u04e2\0\u0514\0\144\0\u0546\0\u0578\0\144"+
    "\0\u05aa\0\u05dc\0\144\0\u060e\0\u0640\0\u0672\0\u06a4\0\u06d6"+
    "\0\u0708\0\u073a\0\u076c\0\u079e\0\u07d0\0\u0802\0\u0834\0\u0866"+
    "\0\u0898\0\u08ca\0\372\0\u08fc\0\u092e\0\u0960\0\u0992\0\u09c4"+
    "\0\u09f6\0\u0a28\0\u0a5a\0\u0a8c\0\u0abe\0\u0af0\0\u0b22\0\144"+
    "\0\144\0\144\0\144\0\144\0\u0b54\0\u0b86\0\u0bb8\0\u0bea"+
    "\0\u0c1c\0\u0c4e\0\u0c80\0\u079e\0\u0cb2\0\u0ce4\0\u0d16\0\u0d48"+
    "\0\u0d7a\0\u0dac\0\u0dde\0\u0e10\0\u0e42\0\u0e74\0\u0ea6\0\u0ed8"+
    "\0\u0f0a\0\u0f3c\0\u0f6e\0\u0fa0\0\372\0\u0fd2\0\u1004\0\u1036"+
    "\0\u1036\0\u1068\0\u109a\0\u10cc\0\u10fe\0\u1130\0\u1162\0\u1194"+
    "\0\u11c6\0\u11f8\0\u122a\0\u125c\0\u128e\0\u12c0\0\u12f2\0\u1324"+
    "\0\u1356\0\u1388\0\u13ba\0\u13ec\0\u141e\0\u1450\0\u1482\0\u14b4"+
    "\0\u14e6\0\u1518\0\u154a\0\u157c\0\u15ae\0\u15e0\0\144\0\u1612"+
    "\0\u1644";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[129];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\1\4\1\5\1\6\1\7\1\10\2\6\1\11"+
    "\1\12\1\6\1\13\1\14\1\6\1\15\1\16\1\13"+
    "\1\17\2\6\1\20\1\21\1\17\1\3\1\22\1\23"+
    "\1\24\1\25\2\6\1\26\1\6\1\27\1\30\1\31"+
    "\2\6\1\32\1\33\1\34\4\6\1\35\2\13\1\36"+
    "\1\37\1\40\17\41\1\3\6\41\1\3\1\42\31\41"+
    "\1\43\63\0\1\6\1\44\6\6\1\0\1\6\1\0"+
    "\2\6\4\0\4\6\2\0\23\6\1\45\7\0\3\6"+
    "\1\46\4\6\1\0\1\6\1\0\2\6\4\0\4\6"+
    "\2\0\24\6\7\0\10\6\1\0\1\6\1\0\2\6"+
    "\4\0\4\6\2\0\24\6\7\0\6\6\1\47\1\6"+
    "\1\0\1\6\1\0\2\6\4\0\4\6\2\0\24\6"+
    "\7\0\5\6\1\50\2\6\1\0\1\6\1\0\2\6"+
    "\4\0\4\6\2\0\4\6\1\51\17\6\7\0\1\52"+
    "\7\6\1\0\1\6\1\0\2\6\4\0\4\6\2\0"+
    "\24\6\22\0\1\53\7\0\1\53\113\0\1\35\14\0"+
    "\1\53\2\0\1\20\1\54\6\0\1\20\53\0\1\55"+
    "\1\0\1\56\35\0\1\35\31\0\1\17\44\0\1\53"+
    "\2\0\1\20\7\0\1\20\62\0\1\57\35\0\2\6"+
    "\1\60\5\6\1\0\1\6\1\0\2\6\4\0\4\6"+
    "\2\0\24\6\7\0\6\6\1\61\1\6\1\0\1\6"+
    "\1\0\2\6\4\0\4\6\2\0\4\6\1\62\17\6"+
    "\7\0\4\6\1\63\3\6\1\0\1\6\1\0\2\6"+
    "\4\0\4\6\2\0\1\64\6\6\1\65\14\6\7\0"+
    "\1\66\1\67\6\6\1\0\1\6\1\0\2\6\4\0"+
    "\4\6\2\0\4\6\1\70\17\6\7\0\1\66\7\6"+
    "\1\0\1\6\1\0\2\6\4\0\4\6\2\0\24\6"+
    "\7\0\1\6\1\71\1\72\5\6\1\0\1\6\1\0"+
    "\2\6\4\0\4\6\2\0\24\6\7\0\3\6\1\73"+
    "\4\6\1\0\1\6\1\0\2\6\4\0\4\6\2\0"+
    "\24\6\7\0\10\6\1\0\1\6\1\0\2\6\4\0"+
    "\4\6\2\0\23\6\1\74\7\0\1\6\1\75\6\6"+
    "\1\0\1\6\1\0\2\6\4\0\4\6\2\0\24\6"+
    "\7\0\10\6\1\0\1\6\1\0\2\6\4\0\4\6"+
    "\2\0\4\6\1\76\17\6\7\0\3\6\1\77\4\6"+
    "\1\0\1\6\1\0\2\6\4\0\4\6\2\0\24\6"+
    "\65\0\1\35\62\0\1\35\1\0\17\41\1\0\6\41"+
    "\2\0\31\41\2\0\1\100\1\101\24\0\1\102\1\103"+
    "\30\0\1\104\1\0\2\6\1\105\5\6\1\0\1\6"+
    "\1\0\2\6\4\0\4\6\2\0\24\6\7\0\10\6"+
    "\1\0\1\6\1\0\2\6\4\0\4\6\2\0\2\6"+
    "\1\106\21\6\7\0\1\107\7\6\1\0\1\6\1\0"+
    "\2\6\4\0\4\6\2\0\24\6\7\0\7\6\1\110"+
    "\1\0\1\6\1\0\2\6\4\0\4\6\2\0\24\6"+
    "\7\0\6\6\1\111\1\6\1\0\1\6\1\0\2\6"+
    "\4\0\4\6\2\0\24\6\7\0\1\6\1\63\6\6"+
    "\1\0\1\6\1\0\2\6\4\0\4\6\2\0\24\6"+
    "\7\0\1\6\1\112\6\6\1\0\1\6\1\0\2\6"+
    "\4\0\4\6\2\0\24\6\12\0\1\113\5\0\1\113"+
    "\1\0\1\53\7\0\1\53\41\0\3\114\3\0\1\114"+
    "\1\0\1\114\6\0\2\114\4\0\1\114\1\0\1\114"+
    "\2\0\1\114\4\0\1\114\16\0\17\55\1\0\6\55"+
    "\1\0\33\55\20\56\1\115\41\56\5\0\1\116\1\0"+
    "\1\117\53\0\6\6\1\120\1\6\1\0\1\6\1\0"+
    "\2\6\4\0\4\6\2\0\24\6\7\0\5\6\1\121"+
    "\2\6\1\0\1\6\1\0\2\6\4\0\4\6\2\0"+
    "\24\6\7\0\10\6\1\0\1\6\1\0\2\6\4\0"+
    "\4\6\2\0\1\122\23\6\7\0\1\63\7\6\1\0"+
    "\1\6\1\0\2\6\4\0\4\6\2\0\24\6\7\0"+
    "\10\6\1\0\1\6\1\0\2\6\4\0\4\6\2\0"+
    "\10\6\1\123\13\6\7\0\10\6\1\0\1\6\1\0"+
    "\2\6\4\0\4\6\2\0\4\6\1\124\17\6\7\0"+
    "\3\6\1\125\4\6\1\0\1\6\1\0\2\6\4\0"+
    "\4\6\2\0\24\6\7\0\10\6\1\0\1\6\1\0"+
    "\2\6\4\0\4\6\2\0\4\6\1\120\17\6\7\0"+
    "\10\6\1\0\1\6\1\0\2\6\4\0\4\6\2\0"+
    "\2\6\1\126\21\6\7\0\10\6\1\0\1\6\1\0"+
    "\2\6\4\0\4\6\2\0\3\6\1\127\20\6\7\0"+
    "\10\6\1\0\1\6\1\0\2\6\4\0\4\6\2\0"+
    "\12\6\1\130\11\6\7\0\10\6\1\0\1\6\1\0"+
    "\2\6\4\0\4\6\2\0\2\6\1\131\21\6\7\0"+
    "\10\6\1\0\1\6\1\0\2\6\4\0\4\6\2\0"+
    "\2\6\1\132\21\6\7\0\10\6\1\0\1\6\1\0"+
    "\2\6\4\0\4\6\2\0\2\6\1\133\21\6\7\0"+
    "\5\6\1\134\2\6\1\0\1\6\1\0\2\6\4\0"+
    "\4\6\2\0\24\6\7\0\3\6\1\135\4\6\1\0"+
    "\1\6\1\0\2\6\4\0\4\6\2\0\24\6\7\0"+
    "\7\6\1\63\1\0\1\6\1\0\2\6\4\0\4\6"+
    "\2\0\24\6\7\0\2\6\1\136\5\6\1\0\1\6"+
    "\1\0\2\6\4\0\4\6\2\0\24\6\7\0\3\6"+
    "\1\63\4\6\1\0\1\6\1\0\2\6\4\0\4\6"+
    "\2\0\24\6\7\0\7\6\1\105\1\0\1\6\1\0"+
    "\2\6\4\0\4\6\2\0\24\6\7\0\10\6\1\0"+
    "\1\6\1\0\2\6\4\0\4\6\2\0\2\6\1\137"+
    "\21\6\21\0\1\140\1\141\7\0\1\141\35\0\16\56"+
    "\1\17\1\56\1\115\41\56\3\0\1\142\110\0\1\143"+
    "\30\0\6\6\1\63\1\6\1\0\1\6\1\0\2\6"+
    "\4\0\4\6\2\0\24\6\7\0\7\6\1\106\1\0"+
    "\1\6\1\0\2\6\4\0\4\6\2\0\24\6\7\0"+
    "\1\144\7\6\1\0\1\6\1\0\2\6\4\0\4\6"+
    "\2\0\24\6\7\0\10\6\1\0\1\6\1\0\2\6"+
    "\4\0\4\6\2\0\4\6\1\145\17\6\7\0\10\6"+
    "\1\0\1\6\1\0\2\6\4\0\4\6\2\0\2\6"+
    "\1\63\21\6\7\0\5\6\1\146\2\6\1\0\1\6"+
    "\1\0\2\6\4\0\4\6\2\0\24\6\7\0\10\6"+
    "\1\0\1\6\1\0\2\6\4\0\4\6\2\0\16\6"+
    "\1\147\5\6\7\0\6\6\1\150\1\6\1\0\1\6"+
    "\1\0\2\6\4\0\4\6\2\0\24\6\7\0\10\6"+
    "\1\0\1\6\1\0\2\6\4\0\4\6\2\0\13\6"+
    "\1\151\10\6\7\0\6\6\1\110\1\6\1\0\1\6"+
    "\1\0\2\6\4\0\4\6\2\0\24\6\7\0\10\6"+
    "\1\0\1\6\1\0\2\6\4\0\4\6\2\0\1\64"+
    "\23\6\7\0\10\6\1\0\1\6\1\0\2\6\4\0"+
    "\4\6\2\0\6\6\1\63\15\6\7\0\10\6\1\0"+
    "\1\6\1\0\2\6\4\0\4\6\2\0\6\6\1\152"+
    "\15\6\7\0\1\6\1\153\6\6\1\0\1\6\1\0"+
    "\2\6\4\0\4\6\2\0\24\6\7\0\10\6\1\0"+
    "\1\6\1\0\2\6\4\0\4\6\2\0\1\154\23\6"+
    "\22\0\1\141\7\0\1\141\65\0\1\155\61\0\1\156"+
    "\32\0\10\6\1\0\1\6\1\0\2\6\4\0\4\6"+
    "\2\0\2\6\1\157\21\6\7\0\1\6\1\160\6\6"+
    "\1\0\1\6\1\0\2\6\4\0\4\6\2\0\24\6"+
    "\7\0\10\6\1\0\1\6\1\0\2\6\4\0\4\6"+
    "\2\0\5\6\1\63\16\6\7\0\5\6\1\161\2\6"+
    "\1\0\1\6\1\0\2\6\4\0\4\6\2\0\24\6"+
    "\7\0\10\6\1\0\1\6\1\0\2\6\4\0\4\6"+
    "\2\0\2\6\1\162\21\6\7\0\1\6\1\163\6\6"+
    "\1\0\1\6\1\0\2\6\4\0\4\6\2\0\24\6"+
    "\7\0\10\6\1\0\1\6\1\0\2\6\4\0\4\6"+
    "\2\0\20\6\1\164\1\6\1\165\1\6\7\0\10\6"+
    "\1\0\1\6\1\0\2\6\4\0\4\6\2\0\1\63"+
    "\23\6\7\0\10\6\1\0\1\6\1\0\2\6\4\0"+
    "\4\6\2\0\21\6\1\63\2\6\37\0\1\166\34\0"+
    "\1\166\56\0\10\6\1\0\1\6\1\0\2\6\4\0"+
    "\4\6\2\0\1\167\23\6\7\0\1\170\7\6\1\0"+
    "\1\6\1\0\2\6\4\0\4\6\2\0\24\6\7\0"+
    "\1\110\7\6\1\0\1\6\1\0\2\6\4\0\4\6"+
    "\2\0\24\6\7\0\10\6\1\0\1\6\1\0\2\6"+
    "\4\0\4\6\2\0\1\6\1\63\22\6\7\0\1\6"+
    "\1\171\6\6\1\0\1\6\1\0\2\6\4\0\4\6"+
    "\2\0\24\6\7\0\10\6\1\0\1\6\1\0\2\6"+
    "\4\0\4\6\2\0\1\172\23\6\7\0\10\6\1\0"+
    "\1\6\1\0\2\6\4\0\4\6\2\0\2\6\1\173"+
    "\21\6\33\0\1\174\35\0\2\6\1\110\5\6\1\0"+
    "\1\6\1\0\2\6\4\0\4\6\2\0\24\6\7\0"+
    "\5\6\1\132\2\6\1\0\1\6\1\0\2\6\4\0"+
    "\4\6\2\0\24\6\7\0\5\6\1\175\2\6\1\0"+
    "\1\6\1\0\2\6\4\0\4\6\2\0\24\6\7\0"+
    "\1\176\7\6\1\0\1\6\1\0\2\6\4\0\4\6"+
    "\2\0\24\6\7\0\10\6\1\0\1\6\1\0\2\6"+
    "\4\0\4\6\2\0\1\110\23\6\33\0\1\177\35\0"+
    "\10\6\1\0\1\6\1\0\2\6\4\0\4\6\2\0"+
    "\14\6\1\63\7\6\7\0\3\6\1\200\4\6\1\0"+
    "\1\6\1\0\2\6\4\0\4\6\2\0\24\6\7\0"+
    "\10\6\1\0\1\6\1\0\2\6\4\0\4\6\2\0"+
    "\21\6\1\201\2\6\7\0\3\6\1\51\4\6\1\0"+
    "\1\6\1\0\2\6\4\0\4\6\2\0\24\6\6\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[5750];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\1\11\13\1\1\11\15\1\1\11\2\1\1\11"+
    "\2\1\1\11\10\1\1\0\1\1\2\0\20\1\5\11"+
    "\6\1\1\0\1\1\3\0\20\1\1\0\1\1\2\0"+
    "\11\1\2\0\7\1\1\0\5\1\1\0\2\1\1\11"+
    "\2\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[129];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true iff the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true iff the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;
  
  /** 
   * The number of occupied positions in zzBuffer beyond zzEndRead.
   * When a lead/high surrogate has been read from the input stream
   * into the final zzBuffer position, this will have a value of 1;
   * otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;

  /* user code: */
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


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Lexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x110000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 198) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzBuffer.length*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;
    int numRead = zzReader.read(zzBuffer, zzEndRead, requested);

    /* not supposed to occur according to specification of java.io.Reader */
    if (numRead == 0) {
      throw new java.io.IOException("Reader returned 0 characters. See JFlex examples for workaround.");
    }
    if (numRead > 0) {
      zzEndRead += numRead;
      /* If numRead == requested, we might have requested to few chars to
         encode a full Unicode character. We assume that a Reader would
         otherwise never return half characters. */
      if (numRead == requested) {
        if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        }
      }
      /* potentially more input available */
      return false;
    }

    /* numRead < 0 ==> end of stream */
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    zzFinalHighSurrogate = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
    if (zzBuffer.length > ZZ_BUFFERSIZE)
      zzBuffer = new char[ZZ_BUFFERSIZE];
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public java_cup.runtime.Symbol next_token() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      int zzCh;
      int zzCharCount;
      for (zzCurrentPosL = zzStartRead  ;
           zzCurrentPosL < zzMarkedPosL ;
           zzCurrentPosL += zzCharCount ) {
        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);
        zzCharCount = Character.charCount(zzCh);
        switch (zzCh) {
        case '\u000B':  // fall through
        case '\u000C':  // fall through
        case '\u0085':  // fall through
        case '\u2028':  // fall through
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn += zzCharCount;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
            zzDoEOF();
          { return new java_cup.runtime.Symbol(sym.EOF); }
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { throw new Error("Illegal character <" + yytext() + ">");
            } 
            // fall through
          case 17: break;
          case 2: 
            { return symbol(T_ID, yytext());
            } 
            // fall through
          case 18: break;
          case 3: 
            { return symbol(T_OPERATOR, yytext());
            } 
            // fall through
          case 19: break;
          case 4: 
            { return symbol(T_INTLITERAL, yytext());
            } 
            // fall through
          case 20: break;
          case 5: 
            { /* ignore */
            } 
            // fall through
          case 21: break;
          case 6: 
            { string.setLength(0);
            yybegin(STRING);
            } 
            // fall through
          case 22: break;
          case 7: 
            { string.append(yytext());
            } 
            // fall through
          case 23: break;
          case 8: 
            { yybegin(YYINITIAL);
            return symbol(T_STRINGLITERAL, string.toString());
            } 
            // fall through
          case 24: break;
          case 9: 
            { return symbol(T_DOUBLELITERAL, yytext());
            } 
            // fall through
          case 25: break;
          case 10: 
            { return symbol( T_KEYWORD, yytext());
            } 
            // fall through
          case 26: break;
          case 11: 
            { string.append('\t');
            } 
            // fall through
          case 27: break;
          case 12: 
            { string.append('\r');
            } 
            // fall through
          case 28: break;
          case 13: 
            { string.append('\\');
            } 
            // fall through
          case 29: break;
          case 14: 
            { string.append('\n');
            } 
            // fall through
          case 30: break;
          case 15: 
            { string.append('\"');
            } 
            // fall through
          case 31: break;
          case 16: 
            { return symbol(T_BOOLEANLITERAL, yytext());
            } 
            // fall through
          case 32: break;
          default:
            zzScanError(ZZ_NO_MATCH);
        }
      }
    }
  }


}
