// $ANTLR null /home/alessandro/opt/antlr/ddl3/ddl3.g 2021-03-11 11:26:35
package it.cnr.istc.pst.platinum.ai.lang.ddl.v3.parser;


import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

@SuppressWarnings("all")
public class ddl3Lexer extends Lexer {
	public static final int EOF=-1;
	public static final int T__9=9;
	public static final int T__10=10;
	public static final int T__11=11;
	public static final int T__12=12;
	public static final int T__13=13;
	public static final int T__14=14;
	public static final int T__15=15;
	public static final int T__16=16;
	public static final int T__17=17;
	public static final int T__18=18;
	public static final int T__19=19;
	public static final int T__20=20;
	public static final int T__21=21;
	public static final int T__22=22;
	public static final int T__23=23;
	public static final int T__24=24;
	public static final int T__25=25;
	public static final int T__26=26;
	public static final int T__27=27;
	public static final int T__28=28;
	public static final int T__29=29;
	public static final int T__30=30;
	public static final int T__31=31;
	public static final int T__32=32;
	public static final int T__33=33;
	public static final int T__34=34;
	public static final int T__35=35;
	public static final int T__36=36;
	public static final int T__37=37;
	public static final int T__38=38;
	public static final int T__39=39;
	public static final int T__40=40;
	public static final int T__41=41;
	public static final int T__42=42;
	public static final int T__43=43;
	public static final int T__44=44;
	public static final int T__45=45;
	public static final int T__46=46;
	public static final int T__47=47;
	public static final int T__48=48;
	public static final int T__49=49;
	public static final int T__50=50;
	public static final int T__51=51;
	public static final int T__52=52;
	public static final int T__53=53;
	public static final int T__54=54;
	public static final int T__55=55;
	public static final int T__56=56;
	public static final int T__57=57;
	public static final int T__58=58;
	public static final int T__59=59;
	public static final int T__60=60;
	public static final int T__61=61;
	public static final int T__62=62;
	public static final int T__63=63;
	public static final int T__64=64;
	public static final int T__65=65;
	public static final int T__66=66;
	public static final int T__67=67;
	public static final int T__68=68;
	public static final int T__69=69;
	public static final int T__70=70;
	public static final int T__71=71;
	public static final int T__72=72;
	public static final int T__73=73;
	public static final int T__74=74;
	public static final int T__75=75;
	public static final int T__76=76;
	public static final int T__77=77;
	public static final int T__78=78;
	public static final int T__79=79;
	public static final int T__80=80;
	public static final int T__81=81;
	public static final int COMMENT=4;
	public static final int ID=5;
	public static final int INT=6;
	public static final int VarID=7;
	public static final int WS=8;

	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public ddl3Lexer() {} 
	public ddl3Lexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public ddl3Lexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "/home/alessandro/opt/antlr/ddl3/ddl3.g"; }

	// $ANTLR start "T__9"
	public final void mT__9() throws RecognitionException {
		try {
			int _type = T__9;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:2:6: ( '!' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:2:8: '!'
			{
			match('!'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__9"

	// $ANTLR start "T__10"
	public final void mT__10() throws RecognitionException {
		try {
			int _type = T__10;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:3:7: ( '!=' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:3:9: '!='
			{
			match("!="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__10"

	// $ANTLR start "T__11"
	public final void mT__11() throws RecognitionException {
		try {
			int _type = T__11;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:4:7: ( '(' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:4:9: '('
			{
			match('('); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__11"

	// $ANTLR start "T__12"
	public final void mT__12() throws RecognitionException {
		try {
			int _type = T__12;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:5:7: ( ')' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:5:9: ')'
			{
			match(')'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__12"

	// $ANTLR start "T__13"
	public final void mT__13() throws RecognitionException {
		try {
			int _type = T__13;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:6:7: ( '*' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:6:9: '*'
			{
			match('*'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__13"

	// $ANTLR start "T__14"
	public final void mT__14() throws RecognitionException {
		try {
			int _type = T__14;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:7:7: ( '+' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:7:9: '+'
			{
			match('+'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__14"

	// $ANTLR start "T__15"
	public final void mT__15() throws RecognitionException {
		try {
			int _type = T__15;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:8:7: ( ',' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:8:9: ','
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__15"

	// $ANTLR start "T__16"
	public final void mT__16() throws RecognitionException {
		try {
			int _type = T__16;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:9:7: ( '-' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:9:9: '-'
			{
			match('-'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__16"

	// $ANTLR start "T__17"
	public final void mT__17() throws RecognitionException {
		try {
			int _type = T__17;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:10:7: ( '.' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:10:9: '.'
			{
			match('.'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__17"

	// $ANTLR start "T__18"
	public final void mT__18() throws RecognitionException {
		try {
			int _type = T__18;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:11:7: ( ':' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:11:9: ':'
			{
			match(':'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__18"

	// $ANTLR start "T__19"
	public final void mT__19() throws RecognitionException {
		try {
			int _type = T__19;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:12:7: ( ';' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:12:9: ';'
			{
			match(';'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__19"

	// $ANTLR start "T__20"
	public final void mT__20() throws RecognitionException {
		try {
			int _type = T__20;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:13:7: ( '<' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:13:9: '<'
			{
			match('<'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__20"

	// $ANTLR start "T__21"
	public final void mT__21() throws RecognitionException {
		try {
			int _type = T__21;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:14:7: ( '<=' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:14:9: '<='
			{
			match("<="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__21"

	// $ANTLR start "T__22"
	public final void mT__22() throws RecognitionException {
		try {
			int _type = T__22;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:15:7: ( '=' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:15:9: '='
			{
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__22"

	// $ANTLR start "T__23"
	public final void mT__23() throws RecognitionException {
		try {
			int _type = T__23;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:16:7: ( '>' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:16:9: '>'
			{
			match('>'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__23"

	// $ANTLR start "T__24"
	public final void mT__24() throws RecognitionException {
		try {
			int _type = T__24;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:17:7: ( '>=' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:17:9: '>='
			{
			match(">="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__24"

	// $ANTLR start "T__25"
	public final void mT__25() throws RecognitionException {
		try {
			int _type = T__25;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:18:7: ( '?' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:18:9: '?'
			{
			match('?'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__25"

	// $ANTLR start "T__26"
	public final void mT__26() throws RecognitionException {
		try {
			int _type = T__26;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:19:7: ( 'AFTER' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:19:9: 'AFTER'
			{
			match("AFTER"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__26"

	// $ANTLR start "T__27"
	public final void mT__27() throws RecognitionException {
		try {
			int _type = T__27;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:20:7: ( 'AFTER-END' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:20:9: 'AFTER-END'
			{
			match("AFTER-END"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__27"

	// $ANTLR start "T__28"
	public final void mT__28() throws RecognitionException {
		try {
			int _type = T__28;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:21:7: ( 'AT' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:21:9: 'AT'
			{
			match("AT"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__28"

	// $ANTLR start "T__29"
	public final void mT__29() throws RecognitionException {
		try {
			int _type = T__29;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:22:7: ( 'AT-END' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:22:9: 'AT-END'
			{
			match("AT-END"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__29"

	// $ANTLR start "T__30"
	public final void mT__30() throws RecognitionException {
		try {
			int _type = T__30;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:23:7: ( 'AT-START' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:23:9: 'AT-START'
			{
			match("AT-START"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__30"

	// $ANTLR start "T__31"
	public final void mT__31() throws RecognitionException {
		try {
			int _type = T__31;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:24:7: ( 'BEFORE' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:24:9: 'BEFORE'
			{
			match("BEFORE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__31"

	// $ANTLR start "T__32"
	public final void mT__32() throws RecognitionException {
		try {
			int _type = T__32;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:25:7: ( 'BEFORE-START' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:25:9: 'BEFORE-START'
			{
			match("BEFORE-START"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__32"

	// $ANTLR start "T__33"
	public final void mT__33() throws RecognitionException {
		try {
			int _type = T__33;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:26:7: ( 'BOUNDED' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:26:9: 'BOUNDED'
			{
			match("BOUNDED"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__33"

	// $ANTLR start "T__34"
	public final void mT__34() throws RecognitionException {
		try {
			int _type = T__34;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:27:7: ( 'COMPONENT' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:27:9: 'COMPONENT'
			{
			match("COMPONENT"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__34"

	// $ANTLR start "T__35"
	public final void mT__35() throws RecognitionException {
		try {
			int _type = T__35;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:28:7: ( 'COMP_TYPE' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:28:9: 'COMP_TYPE'
			{
			match("COMP_TYPE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__35"

	// $ANTLR start "T__36"
	public final void mT__36() throws RecognitionException {
		try {
			int _type = T__36;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:29:7: ( 'CONSUMPTION' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:29:9: 'CONSUMPTION'
			{
			match("CONSUMPTION"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__36"

	// $ANTLR start "T__37"
	public final void mT__37() throws RecognitionException {
		try {
			int _type = T__37;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:30:7: ( 'CONTAINS' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:30:9: 'CONTAINS'
			{
			match("CONTAINS"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__37"

	// $ANTLR start "T__38"
	public final void mT__38() throws RecognitionException {
		try {
			int _type = T__38;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:31:7: ( 'CONTAINS-END' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:31:9: 'CONTAINS-END'
			{
			match("CONTAINS-END"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__38"

	// $ANTLR start "T__39"
	public final void mT__39() throws RecognitionException {
		try {
			int _type = T__39;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:32:7: ( 'CONTAINS-START' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:32:9: 'CONTAINS-START'
			{
			match("CONTAINS-START"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__39"

	// $ANTLR start "T__40"
	public final void mT__40() throws RecognitionException {
		try {
			int _type = T__40;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:33:7: ( 'ConsumableResource' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:33:9: 'ConsumableResource'
			{
			match("ConsumableResource"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__40"

	// $ANTLR start "T__41"
	public final void mT__41() throws RecognitionException {
		try {
			int _type = T__41;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:34:7: ( 'DOMAIN' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:34:9: 'DOMAIN'
			{
			match("DOMAIN"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__41"

	// $ANTLR start "T__42"
	public final void mT__42() throws RecognitionException {
		try {
			int _type = T__42;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:35:7: ( 'DURING' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:35:9: 'DURING'
			{
			match("DURING"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__42"

	// $ANTLR start "T__43"
	public final void mT__43() throws RecognitionException {
		try {
			int _type = T__43;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:36:7: ( 'END-END' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:36:9: 'END-END'
			{
			match("END-END"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__43"

	// $ANTLR start "T__44"
	public final void mT__44() throws RecognitionException {
		try {
			int _type = T__44;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:37:7: ( 'END-START' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:37:9: 'END-START'
			{
			match("END-START"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__44"

	// $ANTLR start "T__45"
	public final void mT__45() throws RecognitionException {
		try {
			int _type = T__45;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:38:7: ( 'ENDS-AT' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:38:9: 'ENDS-AT'
			{
			match("ENDS-AT"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__45"

	// $ANTLR start "T__46"
	public final void mT__46() throws RecognitionException {
		try {
			int _type = T__46;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:39:7: ( 'ENDS-DURING' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:39:9: 'ENDS-DURING'
			{
			match("ENDS-DURING"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__46"

	// $ANTLR start "T__47"
	public final void mT__47() throws RecognitionException {
		try {
			int _type = T__47;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:40:7: ( 'EQUALS' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:40:9: 'EQUALS'
			{
			match("EQUALS"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__47"

	// $ANTLR start "T__48"
	public final void mT__48() throws RecognitionException {
		try {
			int _type = T__48;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:41:7: ( 'ESTA_LIGHT' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:41:9: 'ESTA_LIGHT'
			{
			match("ESTA_LIGHT"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__48"

	// $ANTLR start "T__49"
	public final void mT__49() throws RecognitionException {
		try {
			int _type = T__49;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:42:7: ( 'ESTA_LIGHT_MAX_CONSUMPTION' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:42:9: 'ESTA_LIGHT_MAX_CONSUMPTION'
			{
			match("ESTA_LIGHT_MAX_CONSUMPTION"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__49"

	// $ANTLR start "T__50"
	public final void mT__50() throws RecognitionException {
		try {
			int _type = T__50;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:43:7: ( 'EnumerationParameterType' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:43:9: 'EnumerationParameterType'
			{
			match("EnumerationParameterType"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__50"

	// $ANTLR start "T__51"
	public final void mT__51() throws RecognitionException {
		try {
			int _type = T__51;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:44:7: ( 'FINISHED-BY' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:44:9: 'FINISHED-BY'
			{
			match("FINISHED-BY"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__51"

	// $ANTLR start "T__52"
	public final void mT__52() throws RecognitionException {
		try {
			int _type = T__52;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:45:7: ( 'FINISHES' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:45:9: 'FINISHES'
			{
			match("FINISHES"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__52"

	// $ANTLR start "T__53"
	public final void mT__53() throws RecognitionException {
		try {
			int _type = T__53;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:46:7: ( 'FLEXIBLE' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:46:9: 'FLEXIBLE'
			{
			match("FLEXIBLE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__53"

	// $ANTLR start "T__54"
	public final void mT__54() throws RecognitionException {
		try {
			int _type = T__54;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:47:7: ( 'INF' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:47:9: 'INF'
			{
			match("INF"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__54"

	// $ANTLR start "T__55"
	public final void mT__55() throws RecognitionException {
		try {
			int _type = T__55;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:48:7: ( 'MEETS' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:48:9: 'MEETS'
			{
			match("MEETS"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__55"

	// $ANTLR start "T__56"
	public final void mT__56() throws RecognitionException {
		try {
			int _type = T__56;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:49:7: ( 'MET-BY' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:49:9: 'MET-BY'
			{
			match("MET-BY"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__56"

	// $ANTLR start "T__57"
	public final void mT__57() throws RecognitionException {
		try {
			int _type = T__57;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:50:7: ( 'NumericParameterType' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:50:9: 'NumericParameterType'
			{
			match("NumericParameterType"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__57"

	// $ANTLR start "T__58"
	public final void mT__58() throws RecognitionException {
		try {
			int _type = T__58;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:51:7: ( 'OVERLAPPED-BY' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:51:9: 'OVERLAPPED-BY'
			{
			match("OVERLAPPED-BY"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__58"

	// $ANTLR start "T__59"
	public final void mT__59() throws RecognitionException {
		try {
			int _type = T__59;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:52:7: ( 'OVERLAPS' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:52:9: 'OVERLAPS'
			{
			match("OVERLAPS"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__59"

	// $ANTLR start "T__60"
	public final void mT__60() throws RecognitionException {
		try {
			int _type = T__60;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:53:7: ( 'PAR_TYPE' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:53:9: 'PAR_TYPE'
			{
			match("PAR_TYPE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__60"

	// $ANTLR start "T__61"
	public final void mT__61() throws RecognitionException {
		try {
			int _type = T__61;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:54:7: ( 'PROBLEM' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:54:9: 'PROBLEM'
			{
			match("PROBLEM"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__61"

	// $ANTLR start "T__62"
	public final void mT__62() throws RecognitionException {
		try {
			int _type = T__62;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:55:7: ( 'PRODUCTION' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:55:9: 'PRODUCTION'
			{
			match("PRODUCTION"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__62"

	// $ANTLR start "T__63"
	public final void mT__63() throws RecognitionException {
		try {
			int _type = T__63;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:56:7: ( 'REQUIREMENT' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:56:9: 'REQUIREMENT'
			{
			match("REQUIREMENT"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__63"

	// $ANTLR start "T__64"
	public final void mT__64() throws RecognitionException {
		try {
			int _type = T__64;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:57:7: ( 'RenewableResource' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:57:9: 'RenewableResource'
			{
			match("RenewableResource"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__64"

	// $ANTLR start "T__65"
	public final void mT__65() throws RecognitionException {
		try {
			int _type = T__65;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:58:7: ( 'START-END' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:58:9: 'START-END'
			{
			match("START-END"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__65"

	// $ANTLR start "T__66"
	public final void mT__66() throws RecognitionException {
		try {
			int _type = T__66;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:59:7: ( 'START-START' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:59:9: 'START-START'
			{
			match("START-START"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__66"

	// $ANTLR start "T__67"
	public final void mT__67() throws RecognitionException {
		try {
			int _type = T__67;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:60:7: ( 'STARTED-BY' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:60:9: 'STARTED-BY'
			{
			match("STARTED-BY"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__67"

	// $ANTLR start "T__68"
	public final void mT__68() throws RecognitionException {
		try {
			int _type = T__68;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:61:7: ( 'STARTS' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:61:9: 'STARTS'
			{
			match("STARTS"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__68"

	// $ANTLR start "T__69"
	public final void mT__69() throws RecognitionException {
		try {
			int _type = T__69;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:62:7: ( 'STARTS-AT' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:62:9: 'STARTS-AT'
			{
			match("STARTS-AT"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__69"

	// $ANTLR start "T__70"
	public final void mT__70() throws RecognitionException {
		try {
			int _type = T__70;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:63:7: ( 'STARTS-DURING' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:63:9: 'STARTS-DURING'
			{
			match("STARTS-DURING"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__70"

	// $ANTLR start "T__71"
	public final void mT__71() throws RecognitionException {
		try {
			int _type = T__71;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:64:7: ( 'SYNCHRONIZE' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:64:9: 'SYNCHRONIZE'
			{
			match("SYNCHRONIZE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__71"

	// $ANTLR start "T__72"
	public final void mT__72() throws RecognitionException {
		try {
			int _type = T__72;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:65:7: ( 'SimpleGroundStateVariable' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:65:9: 'SimpleGroundStateVariable'
			{
			match("SimpleGroundStateVariable"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__72"

	// $ANTLR start "T__73"
	public final void mT__73() throws RecognitionException {
		try {
			int _type = T__73;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:66:7: ( 'SingletonStateVariable' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:66:9: 'SingletonStateVariable'
			{
			match("SingletonStateVariable"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__73"

	// $ANTLR start "T__74"
	public final void mT__74() throws RecognitionException {
		try {
			int _type = T__74;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:67:7: ( 'TEMPORAL_MODULE' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:67:9: 'TEMPORAL_MODULE'
			{
			match("TEMPORAL_MODULE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__74"

	// $ANTLR start "T__75"
	public final void mT__75() throws RecognitionException {
		try {
			int _type = T__75;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:68:7: ( 'VALUE' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:68:9: 'VALUE'
			{
			match("VALUE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__75"

	// $ANTLR start "T__76"
	public final void mT__76() throws RecognitionException {
		try {
			int _type = T__76;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:69:7: ( '[' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:69:9: '['
			{
			match('['); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__76"

	// $ANTLR start "T__77"
	public final void mT__77() throws RecognitionException {
		try {
			int _type = T__77;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:70:7: ( ']' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:70:9: ']'
			{
			match(']'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__77"

	// $ANTLR start "T__78"
	public final void mT__78() throws RecognitionException {
		try {
			int _type = T__78;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:71:7: ( 'c' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:71:9: 'c'
			{
			match('c'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__78"

	// $ANTLR start "T__79"
	public final void mT__79() throws RecognitionException {
		try {
			int _type = T__79;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:72:7: ( 'u' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:72:9: 'u'
			{
			match('u'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__79"

	// $ANTLR start "T__80"
	public final void mT__80() throws RecognitionException {
		try {
			int _type = T__80;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:73:7: ( '{' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:73:9: '{'
			{
			match('{'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__80"

	// $ANTLR start "T__81"
	public final void mT__81() throws RecognitionException {
		try {
			int _type = T__81;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:74:7: ( '}' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:74:9: '}'
			{
			match('}'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__81"

	// $ANTLR start "ID"
	public final void mID() throws RecognitionException {
		try {
			int _type = ID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:193:4: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | '@' )* )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:193:6: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | '@' )*
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:193:30: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | '@' )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0=='-'||(LA1_0 >= '0' && LA1_0 <= '9')||(LA1_0 >= '@' && LA1_0 <= 'Z')||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:
					{
					if ( input.LA(1)=='-'||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= '@' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop1;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ID"

	// $ANTLR start "VarID"
	public final void mVarID() throws RecognitionException {
		try {
			int _type = VarID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:195:7: ( '?' ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | '@' )* )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:195:9: '?' ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | '@' )*
			{
			match('?'); 
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:195:37: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | '@' )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( (LA2_0=='-'||(LA2_0 >= '0' && LA2_0 <= '9')||(LA2_0 >= '@' && LA2_0 <= 'Z')||LA2_0=='_'||(LA2_0 >= 'a' && LA2_0 <= 'z')) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:
					{
					if ( input.LA(1)=='-'||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= '@' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop2;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "VarID"

	// $ANTLR start "INT"
	public final void mINT() throws RecognitionException {
		try {
			int _type = INT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:197:5: ( ( '0' .. '9' )+ )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:197:7: ( '0' .. '9' )+
			{
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:197:7: ( '0' .. '9' )+
			int cnt3=0;
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( ((LA3_0 >= '0' && LA3_0 <= '9')) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt3 >= 1 ) break loop3;
					EarlyExitException eee = new EarlyExitException(3, input);
					throw eee;
				}
				cnt3++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INT"

	// $ANTLR start "COMMENT"
	public final void mCOMMENT() throws RecognitionException {
		try {
			int _type = COMMENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:199:9: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '/*' ( options {greedy=false; } : . )* '*/' )
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0=='/') ) {
				int LA7_1 = input.LA(2);
				if ( (LA7_1=='/') ) {
					alt7=1;
				}
				else if ( (LA7_1=='*') ) {
					alt7=2;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 7, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 7, 0, input);
				throw nvae;
			}

			switch (alt7) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:199:11: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
					{
					match("//"); 

					// /home/alessandro/opt/antlr/ddl3/ddl3.g:199:16: (~ ( '\\n' | '\\r' ) )*
					loop4:
					while (true) {
						int alt4=2;
						int LA4_0 = input.LA(1);
						if ( ((LA4_0 >= '\u0000' && LA4_0 <= '\t')||(LA4_0 >= '\u000B' && LA4_0 <= '\f')||(LA4_0 >= '\u000E' && LA4_0 <= '\uFFFF')) ) {
							alt4=1;
						}

						switch (alt4) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:
							{
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop4;
						}
					}

					// /home/alessandro/opt/antlr/ddl3/ddl3.g:199:30: ( '\\r' )?
					int alt5=2;
					int LA5_0 = input.LA(1);
					if ( (LA5_0=='\r') ) {
						alt5=1;
					}
					switch (alt5) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:199:30: '\\r'
							{
							match('\r'); 
							}
							break;

					}

					match('\n'); 
					_channel=HIDDEN;
					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:199:62: '/*' ( options {greedy=false; } : . )* '*/'
					{
					match("/*"); 

					// /home/alessandro/opt/antlr/ddl3/ddl3.g:199:67: ( options {greedy=false; } : . )*
					loop6:
					while (true) {
						int alt6=2;
						int LA6_0 = input.LA(1);
						if ( (LA6_0=='*') ) {
							int LA6_1 = input.LA(2);
							if ( (LA6_1=='/') ) {
								alt6=2;
							}
							else if ( ((LA6_1 >= '\u0000' && LA6_1 <= '.')||(LA6_1 >= '0' && LA6_1 <= '\uFFFF')) ) {
								alt6=1;
							}

						}
						else if ( ((LA6_0 >= '\u0000' && LA6_0 <= ')')||(LA6_0 >= '+' && LA6_0 <= '\uFFFF')) ) {
							alt6=1;
						}

						switch (alt6) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:199:95: .
							{
							matchAny(); 
							}
							break;

						default :
							break loop6;
						}
					}

					match("*/"); 

					_channel=HIDDEN;
					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMENT"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:201:4: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:201:6: ( ' ' | '\\t' | '\\r' | '\\n' )
			{
			if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			_channel=HIDDEN;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	@Override
	public void mTokens() throws RecognitionException {
		// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:8: ( T__9 | T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | ID | VarID | INT | COMMENT | WS )
		int alt8=78;
		alt8 = dfa8.predict(input);
		switch (alt8) {
			case 1 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:10: T__9
				{
				mT__9(); 

				}
				break;
			case 2 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:15: T__10
				{
				mT__10(); 

				}
				break;
			case 3 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:21: T__11
				{
				mT__11(); 

				}
				break;
			case 4 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:27: T__12
				{
				mT__12(); 

				}
				break;
			case 5 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:33: T__13
				{
				mT__13(); 

				}
				break;
			case 6 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:39: T__14
				{
				mT__14(); 

				}
				break;
			case 7 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:45: T__15
				{
				mT__15(); 

				}
				break;
			case 8 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:51: T__16
				{
				mT__16(); 

				}
				break;
			case 9 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:57: T__17
				{
				mT__17(); 

				}
				break;
			case 10 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:63: T__18
				{
				mT__18(); 

				}
				break;
			case 11 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:69: T__19
				{
				mT__19(); 

				}
				break;
			case 12 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:75: T__20
				{
				mT__20(); 

				}
				break;
			case 13 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:81: T__21
				{
				mT__21(); 

				}
				break;
			case 14 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:87: T__22
				{
				mT__22(); 

				}
				break;
			case 15 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:93: T__23
				{
				mT__23(); 

				}
				break;
			case 16 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:99: T__24
				{
				mT__24(); 

				}
				break;
			case 17 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:105: T__25
				{
				mT__25(); 

				}
				break;
			case 18 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:111: T__26
				{
				mT__26(); 

				}
				break;
			case 19 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:117: T__27
				{
				mT__27(); 

				}
				break;
			case 20 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:123: T__28
				{
				mT__28(); 

				}
				break;
			case 21 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:129: T__29
				{
				mT__29(); 

				}
				break;
			case 22 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:135: T__30
				{
				mT__30(); 

				}
				break;
			case 23 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:141: T__31
				{
				mT__31(); 

				}
				break;
			case 24 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:147: T__32
				{
				mT__32(); 

				}
				break;
			case 25 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:153: T__33
				{
				mT__33(); 

				}
				break;
			case 26 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:159: T__34
				{
				mT__34(); 

				}
				break;
			case 27 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:165: T__35
				{
				mT__35(); 

				}
				break;
			case 28 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:171: T__36
				{
				mT__36(); 

				}
				break;
			case 29 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:177: T__37
				{
				mT__37(); 

				}
				break;
			case 30 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:183: T__38
				{
				mT__38(); 

				}
				break;
			case 31 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:189: T__39
				{
				mT__39(); 

				}
				break;
			case 32 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:195: T__40
				{
				mT__40(); 

				}
				break;
			case 33 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:201: T__41
				{
				mT__41(); 

				}
				break;
			case 34 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:207: T__42
				{
				mT__42(); 

				}
				break;
			case 35 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:213: T__43
				{
				mT__43(); 

				}
				break;
			case 36 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:219: T__44
				{
				mT__44(); 

				}
				break;
			case 37 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:225: T__45
				{
				mT__45(); 

				}
				break;
			case 38 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:231: T__46
				{
				mT__46(); 

				}
				break;
			case 39 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:237: T__47
				{
				mT__47(); 

				}
				break;
			case 40 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:243: T__48
				{
				mT__48(); 

				}
				break;
			case 41 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:249: T__49
				{
				mT__49(); 

				}
				break;
			case 42 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:255: T__50
				{
				mT__50(); 

				}
				break;
			case 43 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:261: T__51
				{
				mT__51(); 

				}
				break;
			case 44 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:267: T__52
				{
				mT__52(); 

				}
				break;
			case 45 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:273: T__53
				{
				mT__53(); 

				}
				break;
			case 46 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:279: T__54
				{
				mT__54(); 

				}
				break;
			case 47 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:285: T__55
				{
				mT__55(); 

				}
				break;
			case 48 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:291: T__56
				{
				mT__56(); 

				}
				break;
			case 49 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:297: T__57
				{
				mT__57(); 

				}
				break;
			case 50 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:303: T__58
				{
				mT__58(); 

				}
				break;
			case 51 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:309: T__59
				{
				mT__59(); 

				}
				break;
			case 52 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:315: T__60
				{
				mT__60(); 

				}
				break;
			case 53 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:321: T__61
				{
				mT__61(); 

				}
				break;
			case 54 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:327: T__62
				{
				mT__62(); 

				}
				break;
			case 55 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:333: T__63
				{
				mT__63(); 

				}
				break;
			case 56 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:339: T__64
				{
				mT__64(); 

				}
				break;
			case 57 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:345: T__65
				{
				mT__65(); 

				}
				break;
			case 58 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:351: T__66
				{
				mT__66(); 

				}
				break;
			case 59 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:357: T__67
				{
				mT__67(); 

				}
				break;
			case 60 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:363: T__68
				{
				mT__68(); 

				}
				break;
			case 61 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:369: T__69
				{
				mT__69(); 

				}
				break;
			case 62 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:375: T__70
				{
				mT__70(); 

				}
				break;
			case 63 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:381: T__71
				{
				mT__71(); 

				}
				break;
			case 64 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:387: T__72
				{
				mT__72(); 

				}
				break;
			case 65 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:393: T__73
				{
				mT__73(); 

				}
				break;
			case 66 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:399: T__74
				{
				mT__74(); 

				}
				break;
			case 67 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:405: T__75
				{
				mT__75(); 

				}
				break;
			case 68 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:411: T__76
				{
				mT__76(); 

				}
				break;
			case 69 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:417: T__77
				{
				mT__77(); 

				}
				break;
			case 70 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:423: T__78
				{
				mT__78(); 

				}
				break;
			case 71 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:429: T__79
				{
				mT__79(); 

				}
				break;
			case 72 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:435: T__80
				{
				mT__80(); 

				}
				break;
			case 73 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:441: T__81
				{
				mT__81(); 

				}
				break;
			case 74 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:447: ID
				{
				mID(); 

				}
				break;
			case 75 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:450: VarID
				{
				mVarID(); 

				}
				break;
			case 76 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:456: INT
				{
				mINT(); 

				}
				break;
			case 77 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:460: COMMENT
				{
				mCOMMENT(); 

				}
				break;
			case 78 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:1:468: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA8 dfa8 = new DFA8(this);
	static final String DFA8_eotS =
		"\1\uffff\1\51\11\uffff\1\53\1\uffff\1\55\1\57\17\44\2\uffff\1\113\1\114"+
		"\16\uffff\1\44\1\117\31\44\2\uffff\2\44\1\uffff\15\44\1\176\40\44\1\uffff"+
		"\17\44\1\u00b2\23\44\1\u00c7\15\44\1\u00d7\1\44\1\uffff\1\u00d9\1\44\1"+
		"\u00dc\6\44\1\u00e3\1\u00e4\4\44\1\u00e9\4\44\1\uffff\1\u00ee\11\44\1"+
		"\u00fa\4\44\1\uffff\1\44\1\uffff\2\44\1\uffff\1\u0102\5\44\2\uffff\1\u0108"+
		"\1\44\1\u010a\1\44\1\uffff\4\44\1\uffff\3\44\1\u0115\7\44\1\uffff\5\44"+
		"\1\u0123\1\44\1\uffff\3\44\1\u0129\1\44\1\uffff\1\44\1\uffff\4\44\1\u0130"+
		"\1\u0131\2\44\1\u0134\1\u0135\1\uffff\14\44\1\u0142\1\uffff\1\44\1\u0144"+
		"\1\u0145\2\44\1\uffff\1\44\1\u014a\4\44\2\uffff\2\44\2\uffff\3\44\1\u0154"+
		"\2\44\1\u0157\5\44\1\uffff\1\44\2\uffff\4\44\1\uffff\1\44\1\u0164\4\44"+
		"\1\u0169\2\44\1\uffff\1\44\1\u016d\1\uffff\6\44\1\u0174\3\44\1\u0178\1"+
		"\44\1\uffff\1\44\1\u017b\2\44\1\uffff\1\u017e\1\44\1\u0180\1\uffff\1\44"+
		"\1\u0182\3\44\1\u0186\1\uffff\1\u0187\2\44\1\uffff\2\44\1\uffff\2\44\1"+
		"\uffff\1\44\1\uffff\1\44\1\uffff\3\44\2\uffff\5\44\1\u0198\1\44\1\u019a"+
		"\3\44\1\u019e\4\44\1\uffff\1\44\1\uffff\3\44\1\uffff\7\44\1\u01ae\7\44"+
		"\1\uffff\4\44\1\u01ba\2\44\1\u01bd\3\44\1\uffff\2\44\1\uffff\7\44\1\u01ca"+
		"\4\44\1\uffff\5\44\1\u01d4\3\44\1\uffff\1\44\1\u01d9\2\44\1\uffff\1\u01dc"+
		"\1\u01dd\2\uffff";
	static final String DFA8_eofS =
		"\u01de\uffff";
	static final String DFA8_minS =
		"\1\11\1\75\11\uffff\1\75\1\uffff\1\75\1\101\1\106\1\105\2\117\1\116\1"+
		"\111\1\116\1\105\1\165\1\126\1\101\1\105\1\124\1\105\1\101\2\uffff\2\55"+
		"\16\uffff\1\124\1\55\1\106\1\125\1\115\1\156\1\115\1\122\1\104\1\125\1"+
		"\124\1\165\1\116\1\105\1\106\1\105\1\155\1\105\1\122\1\117\1\121\1\156"+
		"\1\101\1\116\1\155\1\115\1\114\2\uffff\2\105\1\uffff\1\117\1\116\1\120"+
		"\1\123\1\163\1\101\1\111\1\55\2\101\1\155\1\111\1\130\1\55\1\124\1\55"+
		"\1\145\1\122\1\137\1\102\1\125\1\145\1\122\1\103\1\160\1\147\1\120\1\125"+
		"\1\122\1\116\1\124\1\122\1\104\1\117\1\125\1\101\1\165\1\111\1\116\1\105"+
		"\1\55\1\114\1\137\1\145\1\123\1\111\1\uffff\1\123\1\102\1\162\1\114\1"+
		"\124\1\114\1\125\1\111\1\167\1\124\1\110\2\154\1\117\1\105\1\55\1\104"+
		"\1\101\2\105\1\116\1\124\1\115\1\111\1\155\1\116\1\107\1\116\1\124\1\101"+
		"\1\123\1\114\1\162\1\110\1\102\1\55\1\131\1\151\1\101\1\131\1\105\1\103"+
		"\1\122\1\141\1\55\1\122\2\145\1\122\1\55\1\105\1\uffff\1\55\1\122\1\55"+
		"\1\104\1\105\1\131\1\120\1\116\1\141\2\55\1\104\1\101\1\124\1\125\1\55"+
		"\1\111\1\141\1\105\1\114\1\uffff\1\55\1\143\2\120\1\115\1\124\1\105\1"+
		"\142\1\105\1\104\1\55\1\117\1\107\1\164\1\101\1\uffff\1\116\1\uffff\1"+
		"\124\1\123\1\uffff\1\55\1\116\1\120\1\124\1\123\1\142\2\uffff\1\55\1\122"+
		"\1\55\1\122\1\uffff\1\107\1\164\1\104\1\105\1\uffff\2\120\1\105\1\55\1"+
		"\111\1\115\1\154\1\116\1\124\1\55\1\101\1\uffff\1\116\1\162\1\157\1\114"+
		"\1\104\1\55\1\124\1\uffff\1\124\1\105\1\111\1\55\1\154\1\uffff\1\124\1"+
		"\uffff\1\111\1\110\1\151\3\55\1\141\1\105\2\55\1\uffff\1\117\1\105\1\145"+
		"\1\104\1\101\1\102\1\124\1\125\1\111\1\157\1\156\1\137\1\55\1\uffff\1"+
		"\101\2\55\1\117\1\105\1\uffff\1\145\1\55\1\116\1\124\1\157\1\102\2\uffff"+
		"\1\162\1\104\2\uffff\2\116\1\122\1\55\1\122\1\131\1\55\1\122\1\132\1\165"+
		"\1\123\1\115\1\uffff\1\122\2\uffff\2\116\1\124\1\122\1\uffff\1\107\1\55"+
		"\1\156\1\131\1\141\2\55\1\124\1\145\1\uffff\1\124\1\55\1\uffff\1\111\1"+
		"\105\1\156\1\164\1\117\1\124\1\55\1\104\1\101\1\145\1\55\1\115\1\uffff"+
		"\1\120\1\55\1\155\1\102\1\uffff\1\55\1\163\1\55\1\uffff\1\116\1\55\1\144"+
		"\1\141\1\104\1\55\1\uffff\1\55\1\122\1\163\1\uffff\1\101\1\141\1\uffff"+
		"\1\145\1\131\1\uffff\1\157\1\uffff\1\107\1\uffff\1\123\1\164\1\125\2\uffff"+
		"\1\124\1\157\1\130\1\162\1\164\1\55\1\165\1\55\1\164\1\145\1\114\1\55"+
		"\1\165\1\137\1\141\1\145\1\uffff\1\162\1\uffff\1\141\1\126\1\105\1\uffff"+
		"\1\162\1\103\1\155\1\162\1\143\1\164\1\141\1\55\1\143\1\117\1\145\1\124"+
		"\2\145\1\162\1\uffff\1\145\1\116\1\164\1\171\1\55\1\126\1\151\1\55\1\123"+
		"\1\145\1\160\1\uffff\2\141\1\uffff\1\125\1\162\1\145\1\162\1\142\1\115"+
		"\1\124\1\55\1\151\1\154\1\120\1\171\1\uffff\1\141\1\145\1\124\1\160\1"+
		"\142\1\55\1\111\1\145\1\154\1\uffff\1\117\1\55\1\145\1\116\1\uffff\2\55"+
		"\2\uffff";
	static final String DFA8_maxS =
		"\1\175\1\75\11\uffff\1\75\1\uffff\1\75\1\172\1\124\1\117\1\157\1\125\1"+
		"\156\1\114\1\116\1\105\1\165\1\126\1\122\1\145\1\151\1\105\1\101\2\uffff"+
		"\2\172\16\uffff\1\124\1\172\1\106\1\125\1\116\1\156\1\115\1\122\1\104"+
		"\1\125\1\124\1\165\1\116\1\105\1\106\1\124\1\155\1\105\1\122\1\117\1\121"+
		"\1\156\1\101\1\116\1\156\1\115\1\114\2\uffff\1\105\1\123\1\uffff\1\117"+
		"\1\116\1\120\1\124\1\163\1\101\1\111\1\123\2\101\1\155\1\111\1\130\1\172"+
		"\1\124\1\55\1\145\1\122\1\137\1\104\1\125\1\145\1\122\1\103\1\160\1\147"+
		"\1\120\1\125\1\122\1\116\1\124\1\122\1\104\1\137\1\125\1\101\1\165\1\111"+
		"\1\116\1\123\1\55\1\114\1\137\1\145\1\123\1\111\1\uffff\1\123\1\102\1"+
		"\162\1\114\1\124\1\114\1\125\1\111\1\167\1\124\1\110\2\154\1\117\1\105"+
		"\1\172\1\104\1\101\2\105\1\116\1\124\1\115\1\111\1\155\1\116\1\107\1\116"+
		"\1\124\1\104\1\123\1\114\1\162\1\110\1\102\1\172\1\131\1\151\1\101\1\131"+
		"\1\105\1\103\1\122\1\141\1\123\1\122\2\145\1\122\1\172\1\105\1\uffff\1"+
		"\172\1\122\1\172\1\104\1\105\1\131\1\120\1\116\1\141\2\172\1\104\1\101"+
		"\1\124\1\125\1\172\1\111\1\141\1\105\1\114\1\uffff\1\172\1\143\2\120\1"+
		"\115\1\124\1\105\1\142\1\123\1\104\1\172\1\117\1\107\1\164\1\101\1\uffff"+
		"\1\116\1\uffff\1\124\1\123\1\uffff\1\172\1\116\1\120\1\124\1\123\1\142"+
		"\2\uffff\1\172\1\122\1\172\1\122\1\uffff\1\107\1\164\1\123\1\105\1\uffff"+
		"\1\120\1\123\1\105\1\172\1\111\1\115\1\154\1\116\1\124\1\55\1\104\1\uffff"+
		"\1\116\1\162\1\157\1\114\1\104\1\172\1\124\1\uffff\1\124\1\105\1\111\1"+
		"\172\1\154\1\uffff\1\124\1\uffff\1\111\1\110\1\151\1\55\2\172\1\141\1"+
		"\105\2\172\1\uffff\1\117\1\105\1\145\1\104\1\101\1\102\1\124\1\125\1\111"+
		"\1\157\1\156\1\137\1\172\1\uffff\1\101\2\172\1\117\1\123\1\uffff\1\145"+
		"\1\172\1\116\1\124\1\157\1\102\2\uffff\1\162\1\104\2\uffff\2\116\1\122"+
		"\1\172\1\122\1\131\1\172\1\122\1\132\1\165\1\123\1\115\1\uffff\1\122\2"+
		"\uffff\2\116\1\124\1\122\1\uffff\1\107\1\172\1\156\1\131\1\141\1\55\1"+
		"\172\1\124\1\145\1\uffff\1\124\1\172\1\uffff\1\111\1\105\1\156\1\164\1"+
		"\117\1\124\1\172\1\104\1\101\1\145\1\172\1\115\1\uffff\1\120\1\172\1\155"+
		"\1\102\1\uffff\1\172\1\163\1\172\1\uffff\1\116\1\172\1\144\1\141\1\104"+
		"\1\172\1\uffff\1\172\1\122\1\163\1\uffff\1\101\1\141\1\uffff\1\145\1\131"+
		"\1\uffff\1\157\1\uffff\1\107\1\uffff\1\123\1\164\1\125\2\uffff\1\124\1"+
		"\157\1\130\1\162\1\164\1\172\1\165\1\172\1\164\1\145\1\114\1\172\1\165"+
		"\1\137\1\141\1\145\1\uffff\1\162\1\uffff\1\141\1\126\1\105\1\uffff\1\162"+
		"\1\103\1\155\1\162\1\143\1\164\1\141\1\172\1\143\1\117\1\145\1\124\2\145"+
		"\1\162\1\uffff\1\145\1\116\1\164\1\171\1\172\1\126\1\151\1\172\1\123\1"+
		"\145\1\160\1\uffff\2\141\1\uffff\1\125\1\162\1\145\1\162\1\142\1\115\1"+
		"\124\1\172\1\151\1\154\1\120\1\171\1\uffff\1\141\1\145\1\124\1\160\1\142"+
		"\1\172\1\111\1\145\1\154\1\uffff\1\117\1\172\1\145\1\116\1\uffff\2\172"+
		"\2\uffff";
	static final String DFA8_acceptS =
		"\2\uffff\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\uffff\1\16\21\uffff"+
		"\1\104\1\105\2\uffff\1\110\1\111\1\112\1\114\1\115\1\116\1\2\1\1\1\15"+
		"\1\14\1\20\1\17\1\113\1\21\33\uffff\1\106\1\107\2\uffff\1\24\56\uffff"+
		"\1\56\63\uffff\1\22\24\uffff\1\57\17\uffff\1\103\1\uffff\1\25\2\uffff"+
		"\1\27\6\uffff\1\41\1\42\4\uffff\1\47\4\uffff\1\60\13\uffff\1\74\7\uffff"+
		"\1\31\5\uffff\1\43\1\uffff\1\45\12\uffff\1\65\15\uffff\1\26\5\uffff\1"+
		"\35\6\uffff\1\54\1\55\2\uffff\1\63\1\64\14\uffff\1\23\1\uffff\1\32\1\33"+
		"\4\uffff\1\44\11\uffff\1\71\2\uffff\1\75\14\uffff\1\50\4\uffff\1\66\3"+
		"\uffff\1\73\6\uffff\1\34\3\uffff\1\46\2\uffff\1\53\2\uffff\1\67\1\uffff"+
		"\1\72\1\uffff\1\77\3\uffff\1\30\1\36\20\uffff\1\62\1\uffff\1\76\3\uffff"+
		"\1\37\17\uffff\1\102\13\uffff\1\70\2\uffff\1\40\14\uffff\1\61\11\uffff"+
		"\1\101\4\uffff\1\52\2\uffff\1\100\1\51";
	static final String DFA8_specialS =
		"\u01de\uffff}>";
	static final String[] DFA8_transitionS = {
			"\2\47\2\uffff\1\47\22\uffff\1\47\1\1\6\uffff\1\2\1\3\1\4\1\5\1\6\1\7"+
			"\1\10\1\46\12\45\1\11\1\12\1\13\1\14\1\15\1\16\1\uffff\1\17\1\20\1\21"+
			"\1\22\1\23\1\24\2\44\1\25\3\44\1\26\1\27\1\30\1\31\1\44\1\32\1\33\1\34"+
			"\1\44\1\35\4\44\1\36\1\uffff\1\37\1\uffff\1\44\1\uffff\2\44\1\40\21\44"+
			"\1\41\5\44\1\42\1\uffff\1\43",
			"\1\50",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\52",
			"",
			"\1\54",
			"\32\56\4\uffff\1\56\1\uffff\32\56",
			"\1\60\15\uffff\1\61",
			"\1\62\11\uffff\1\63",
			"\1\64\37\uffff\1\65",
			"\1\66\5\uffff\1\67",
			"\1\70\2\uffff\1\71\1\uffff\1\72\32\uffff\1\73",
			"\1\74\2\uffff\1\75",
			"\1\76",
			"\1\77",
			"\1\100",
			"\1\101",
			"\1\102\20\uffff\1\103",
			"\1\104\37\uffff\1\105",
			"\1\106\4\uffff\1\107\17\uffff\1\110",
			"\1\111",
			"\1\112",
			"",
			"",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\115",
			"\1\116\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\120",
			"\1\121",
			"\1\122\1\123",
			"\1\124",
			"\1\125",
			"\1\126",
			"\1\127",
			"\1\130",
			"\1\131",
			"\1\132",
			"\1\133",
			"\1\134",
			"\1\135",
			"\1\136\16\uffff\1\137",
			"\1\140",
			"\1\141",
			"\1\142",
			"\1\143",
			"\1\144",
			"\1\145",
			"\1\146",
			"\1\147",
			"\1\150\1\151",
			"\1\152",
			"\1\153",
			"",
			"",
			"\1\154",
			"\1\155\15\uffff\1\156",
			"",
			"\1\157",
			"\1\160",
			"\1\161",
			"\1\162\1\163",
			"\1\164",
			"\1\165",
			"\1\166",
			"\1\167\45\uffff\1\170",
			"\1\171",
			"\1\172",
			"\1\173",
			"\1\174",
			"\1\175",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\177",
			"\1\u0080",
			"\1\u0081",
			"\1\u0082",
			"\1\u0083",
			"\1\u0084\1\uffff\1\u0085",
			"\1\u0086",
			"\1\u0087",
			"\1\u0088",
			"\1\u0089",
			"\1\u008a",
			"\1\u008b",
			"\1\u008c",
			"\1\u008d",
			"\1\u008e",
			"\1\u008f",
			"\1\u0090",
			"\1\u0091",
			"\1\u0092",
			"\1\u0093\17\uffff\1\u0094",
			"\1\u0095",
			"\1\u0096",
			"\1\u0097",
			"\1\u0098",
			"\1\u0099",
			"\1\u009a\15\uffff\1\u009b",
			"\1\u009c",
			"\1\u009d",
			"\1\u009e",
			"\1\u009f",
			"\1\u00a0",
			"\1\u00a1",
			"",
			"\1\u00a2",
			"\1\u00a3",
			"\1\u00a4",
			"\1\u00a5",
			"\1\u00a6",
			"\1\u00a7",
			"\1\u00a8",
			"\1\u00a9",
			"\1\u00aa",
			"\1\u00ab",
			"\1\u00ac",
			"\1\u00ad",
			"\1\u00ae",
			"\1\u00af",
			"\1\u00b0",
			"\1\u00b1\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u00b3",
			"\1\u00b4",
			"\1\u00b5",
			"\1\u00b6",
			"\1\u00b7",
			"\1\u00b8",
			"\1\u00b9",
			"\1\u00ba",
			"\1\u00bb",
			"\1\u00bc",
			"\1\u00bd",
			"\1\u00be",
			"\1\u00bf",
			"\1\u00c0\2\uffff\1\u00c1",
			"\1\u00c2",
			"\1\u00c3",
			"\1\u00c4",
			"\1\u00c5",
			"\1\u00c6",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u00c8",
			"\1\u00c9",
			"\1\u00ca",
			"\1\u00cb",
			"\1\u00cc",
			"\1\u00cd",
			"\1\u00ce",
			"\1\u00cf",
			"\1\u00d0\27\uffff\1\u00d1\15\uffff\1\u00d2",
			"\1\u00d3",
			"\1\u00d4",
			"\1\u00d5",
			"\1\u00d6",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u00d8",
			"",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u00da",
			"\1\u00db\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u00dd",
			"\1\u00de",
			"\1\u00df",
			"\1\u00e0",
			"\1\u00e1",
			"\1\u00e2",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u00e5",
			"\1\u00e6",
			"\1\u00e7",
			"\1\u00e8",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u00ea",
			"\1\u00eb",
			"\1\u00ec",
			"\1\u00ed",
			"",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u00ef",
			"\1\u00f0",
			"\1\u00f1",
			"\1\u00f2",
			"\1\u00f3",
			"\1\u00f4",
			"\1\u00f5",
			"\1\u00f6\15\uffff\1\u00f7",
			"\1\u00f8",
			"\1\u00f9\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u00fb",
			"\1\u00fc",
			"\1\u00fd",
			"\1\u00fe",
			"",
			"\1\u00ff",
			"",
			"\1\u0100",
			"\1\u0101",
			"",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u0103",
			"\1\u0104",
			"\1\u0105",
			"\1\u0106",
			"\1\u0107",
			"",
			"",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u0109",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u010b",
			"",
			"\1\u010c",
			"\1\u010d",
			"\1\u010e\16\uffff\1\u010f",
			"\1\u0110",
			"",
			"\1\u0111",
			"\1\u0112\2\uffff\1\u0113",
			"\1\u0114",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u0116",
			"\1\u0117",
			"\1\u0118",
			"\1\u0119",
			"\1\u011a",
			"\1\u011b",
			"\1\u011c\2\uffff\1\u011d",
			"",
			"\1\u011e",
			"\1\u011f",
			"\1\u0120",
			"\1\u0121",
			"\1\u0122",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u0124",
			"",
			"\1\u0125",
			"\1\u0126",
			"\1\u0127",
			"\1\u0128\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u012a",
			"",
			"\1\u012b",
			"",
			"\1\u012c",
			"\1\u012d",
			"\1\u012e",
			"\1\u012f",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u0132",
			"\1\u0133",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\u0136",
			"\1\u0137",
			"\1\u0138",
			"\1\u0139",
			"\1\u013a",
			"\1\u013b",
			"\1\u013c",
			"\1\u013d",
			"\1\u013e",
			"\1\u013f",
			"\1\u0140",
			"\1\u0141",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\u0143",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u0146",
			"\1\u0147\15\uffff\1\u0148",
			"",
			"\1\u0149",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u014b",
			"\1\u014c",
			"\1\u014d",
			"\1\u014e",
			"",
			"",
			"\1\u014f",
			"\1\u0150",
			"",
			"",
			"\1\u0151",
			"\1\u0152",
			"\1\u0153",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u0155",
			"\1\u0156",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u0158",
			"\1\u0159",
			"\1\u015a",
			"\1\u015b",
			"\1\u015c",
			"",
			"\1\u015d",
			"",
			"",
			"\1\u015e",
			"\1\u015f",
			"\1\u0160",
			"\1\u0161",
			"",
			"\1\u0162",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\u0163\1\uffff\32\44",
			"\1\u0165",
			"\1\u0166",
			"\1\u0167",
			"\1\u0168",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u016a",
			"\1\u016b",
			"",
			"\1\u016c",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\u016e",
			"\1\u016f",
			"\1\u0170",
			"\1\u0171",
			"\1\u0172",
			"\1\u0173",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u0175",
			"\1\u0176",
			"\1\u0177",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u0179",
			"",
			"\1\u017a",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u017c",
			"\1\u017d",
			"",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u017f",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\u0181",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u0183",
			"\1\u0184",
			"\1\u0185",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u0188",
			"\1\u0189",
			"",
			"\1\u018a",
			"\1\u018b",
			"",
			"\1\u018c",
			"\1\u018d",
			"",
			"\1\u018e",
			"",
			"\1\u018f",
			"",
			"\1\u0190",
			"\1\u0191",
			"\1\u0192",
			"",
			"",
			"\1\u0193",
			"\1\u0194",
			"\1\u0195",
			"\1\u0196",
			"\1\u0197",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u0199",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u019b",
			"\1\u019c",
			"\1\u019d",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u019f",
			"\1\u01a0",
			"\1\u01a1",
			"\1\u01a2",
			"",
			"\1\u01a3",
			"",
			"\1\u01a4",
			"\1\u01a5",
			"\1\u01a6",
			"",
			"\1\u01a7",
			"\1\u01a8",
			"\1\u01a9",
			"\1\u01aa",
			"\1\u01ab",
			"\1\u01ac",
			"\1\u01ad",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u01af",
			"\1\u01b0",
			"\1\u01b1",
			"\1\u01b2",
			"\1\u01b3",
			"\1\u01b4",
			"\1\u01b5",
			"",
			"\1\u01b6",
			"\1\u01b7",
			"\1\u01b8",
			"\1\u01b9",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u01bb",
			"\1\u01bc",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u01be",
			"\1\u01bf",
			"\1\u01c0",
			"",
			"\1\u01c1",
			"\1\u01c2",
			"",
			"\1\u01c3",
			"\1\u01c4",
			"\1\u01c5",
			"\1\u01c6",
			"\1\u01c7",
			"\1\u01c8",
			"\1\u01c9",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u01cb",
			"\1\u01cc",
			"\1\u01cd",
			"\1\u01ce",
			"",
			"\1\u01cf",
			"\1\u01d0",
			"\1\u01d1",
			"\1\u01d2",
			"\1\u01d3",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u01d5",
			"\1\u01d6",
			"\1\u01d7",
			"",
			"\1\u01d8",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\u01da",
			"\1\u01db",
			"",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"\1\44\2\uffff\12\44\6\uffff\33\44\4\uffff\1\44\1\uffff\32\44",
			"",
			""
	};

	static final short[] DFA8_eot = DFA.unpackEncodedString(DFA8_eotS);
	static final short[] DFA8_eof = DFA.unpackEncodedString(DFA8_eofS);
	static final char[] DFA8_min = DFA.unpackEncodedStringToUnsignedChars(DFA8_minS);
	static final char[] DFA8_max = DFA.unpackEncodedStringToUnsignedChars(DFA8_maxS);
	static final short[] DFA8_accept = DFA.unpackEncodedString(DFA8_acceptS);
	static final short[] DFA8_special = DFA.unpackEncodedString(DFA8_specialS);
	static final short[][] DFA8_transition;

	static {
		int numStates = DFA8_transitionS.length;
		DFA8_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA8_transition[i] = DFA.unpackEncodedString(DFA8_transitionS[i]);
		}
	}

	protected class DFA8 extends DFA {

		public DFA8(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 8;
			this.eot = DFA8_eot;
			this.eof = DFA8_eof;
			this.min = DFA8_min;
			this.max = DFA8_max;
			this.accept = DFA8_accept;
			this.special = DFA8_special;
			this.transition = DFA8_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T__9 | T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | ID | VarID | INT | COMMENT | WS );";
		}
	}

}
