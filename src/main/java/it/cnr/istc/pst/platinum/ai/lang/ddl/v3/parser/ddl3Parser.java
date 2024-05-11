// $ANTLR null /home/alessandro/opt/antlr/ddl3/ddl3.g 2021-03-11 11:26:35
package it.cnr.istc.pst.platinum.ai.lang.ddl.v3.parser;


import java.util.HashMap;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.BitSet;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.RewriteEarlyExitException;
import org.antlr.runtime.tree.RewriteRuleSubtreeStream;
import org.antlr.runtime.tree.RewriteRuleTokenStream;
import org.antlr.runtime.tree.TreeAdaptor;


@SuppressWarnings("all")
public class ddl3Parser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "COMMENT", "ID", "INT", "VarID", 
		"WS", "'!'", "'!='", "'('", "')'", "'*'", "'+'", "','", "'-'", "'.'", 
		"':'", "';'", "'<'", "'<='", "'='", "'>'", "'>='", "'?'", "'AFTER'", "'AFTER-END'", 
		"'AT'", "'AT-END'", "'AT-START'", "'BEFORE'", "'BEFORE-START'", "'BOUNDED'", 
		"'COMPONENT'", "'COMP_TYPE'", "'CONSUMPTION'", "'CONTAINS'", "'CONTAINS-END'", 
		"'CONTAINS-START'", "'ConsumableResource'", "'DOMAIN'", "'DURING'", "'END-END'", 
		"'END-START'", "'ENDS-AT'", "'ENDS-DURING'", "'EQUALS'", "'ESTA_LIGHT'", 
		"'ESTA_LIGHT_MAX_CONSUMPTION'", "'EnumerationParameterType'", "'FINISHED-BY'", 
		"'FINISHES'", "'FLEXIBLE'", "'INF'", "'MEETS'", "'MET-BY'", "'NumericParameterType'", 
		"'OVERLAPPED-BY'", "'OVERLAPS'", "'PAR_TYPE'", "'PROBLEM'", "'PRODUCTION'", 
		"'REQUIREMENT'", "'RenewableResource'", "'START-END'", "'START-START'", 
		"'STARTED-BY'", "'STARTS'", "'STARTS-AT'", "'STARTS-DURING'", "'SYNCHRONIZE'", 
		"'SimpleGroundStateVariable'", "'SingletonStateVariable'", "'TEMPORAL_MODULE'", 
		"'VALUE'", "'['", "']'", "'c'", "'u'", "'{'", "'}'"
	};
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
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public ddl3Parser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public ddl3Parser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
		this.state.ruleMemo = new HashMap[164+1];


	}

	protected TreeAdaptor adaptor = new CommonTreeAdaptor();

	public void setTreeAdaptor(TreeAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	public TreeAdaptor getTreeAdaptor() {
		return adaptor;
	}
	@Override public String[] getTokenNames() { return ddl3Parser.tokenNames; }
	@Override public String getGrammarFileName() { return "/home/alessandro/opt/antlr/ddl3/ddl3.g"; }


	public static class ddl_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "ddl"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:9:1: ddl : ( domain ( problem )? -> ^( 'DOMAIN' domain ( problem )? ) | problem -> ^( 'PROBLEM' problem ) );
	public final ddl3Parser.ddl_return ddl() throws RecognitionException {
		ddl3Parser.ddl_return retval = new ddl3Parser.ddl_return();
		retval.start = input.LT(1);
		int ddl_StartIndex = input.index();

		Object root_0 = null;

		ParserRuleReturnScope domain1 =null;
		ParserRuleReturnScope problem2 =null;
		ParserRuleReturnScope problem3 =null;

		RewriteRuleSubtreeStream stream_problem=new RewriteRuleSubtreeStream(adaptor,"rule problem");
		RewriteRuleSubtreeStream stream_domain=new RewriteRuleSubtreeStream(adaptor,"rule domain");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 1) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:9:5: ( domain ( problem )? -> ^( 'DOMAIN' domain ( problem )? ) | problem -> ^( 'PROBLEM' problem ) )
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==41) ) {
				alt2=1;
			}
			else if ( (LA2_0==61) ) {
				alt2=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 2, 0, input);
				throw nvae;
			}

			switch (alt2) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:9:7: domain ( problem )?
					{
					pushFollow(FOLLOW_domain_in_ddl42);
					domain1=domain();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_domain.add(domain1.getTree());
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:9:14: ( problem )?
					int alt1=2;
					int LA1_0 = input.LA(1);
					if ( (LA1_0==61) ) {
						alt1=1;
					}
					switch (alt1) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:9:15: problem
							{
							pushFollow(FOLLOW_problem_in_ddl45);
							problem2=problem();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_problem.add(problem2.getTree());
							}
							break;

					}

					// AST REWRITE
					// elements: problem, domain, 41
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 9:25: -> ^( 'DOMAIN' domain ( problem )? )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:9:27: ^( 'DOMAIN' domain ( problem )? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(41, "41"), root_1);
						adaptor.addChild(root_1, stream_domain.nextTree());
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:9:45: ( problem )?
						if ( stream_problem.hasNext() ) {
							adaptor.addChild(root_1, stream_problem.nextTree());
						}
						stream_problem.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:9:59: problem
					{
					pushFollow(FOLLOW_problem_in_ddl63);
					problem3=problem();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_problem.add(problem3.getTree());
					// AST REWRITE
					// elements: problem, 61
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 9:67: -> ^( 'PROBLEM' problem )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:9:69: ^( 'PROBLEM' problem )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(61, "61"), root_1);
						adaptor.addChild(root_1, stream_problem.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 1, ddl_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "ddl"


	public static class domain_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "domain"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:11:1: domain : 'DOMAIN' ID '{' temporal_module ( domain_element )* '}' -> ^( ID temporal_module ( domain_element )* ) ;
	public final ddl3Parser.domain_return domain() throws RecognitionException {
		ddl3Parser.domain_return retval = new ddl3Parser.domain_return();
		retval.start = input.LT(1);
		int domain_StartIndex = input.index();

		Object root_0 = null;

		Token string_literal4=null;
		Token ID5=null;
		Token char_literal6=null;
		Token char_literal9=null;
		ParserRuleReturnScope temporal_module7 =null;
		ParserRuleReturnScope domain_element8 =null;

		Object string_literal4_tree=null;
		Object ID5_tree=null;
		Object char_literal6_tree=null;
		Object char_literal9_tree=null;
		RewriteRuleTokenStream stream_80=new RewriteRuleTokenStream(adaptor,"token 80");
		RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_41=new RewriteRuleTokenStream(adaptor,"token 41");
		RewriteRuleSubtreeStream stream_domain_element=new RewriteRuleSubtreeStream(adaptor,"rule domain_element");
		RewriteRuleSubtreeStream stream_temporal_module=new RewriteRuleSubtreeStream(adaptor,"rule temporal_module");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 2) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:12:2: ( 'DOMAIN' ID '{' temporal_module ( domain_element )* '}' -> ^( ID temporal_module ( domain_element )* ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:12:4: 'DOMAIN' ID '{' temporal_module ( domain_element )* '}'
			{
			string_literal4=(Token)match(input,41,FOLLOW_41_in_domain80); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_41.add(string_literal4);

			ID5=(Token)match(input,ID,FOLLOW_ID_in_domain82); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID5);

			char_literal6=(Token)match(input,80,FOLLOW_80_in_domain84); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_80.add(char_literal6);

			pushFollow(FOLLOW_temporal_module_in_domain86);
			temporal_module7=temporal_module();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_temporal_module.add(temporal_module7.getTree());
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:12:36: ( domain_element )*
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( ((LA3_0 >= 34 && LA3_0 <= 35)||LA3_0==60||LA3_0==71) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:12:36: domain_element
					{
					pushFollow(FOLLOW_domain_element_in_domain88);
					domain_element8=domain_element();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_domain_element.add(domain_element8.getTree());
					}
					break;

				default :
					break loop3;
				}
			}

			char_literal9=(Token)match(input,81,FOLLOW_81_in_domain91); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_81.add(char_literal9);

			// AST REWRITE
			// elements: ID, temporal_module, domain_element
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 12:56: -> ^( ID temporal_module ( domain_element )* )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:12:58: ^( ID temporal_module ( domain_element )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLDomain(stream_ID.nextToken()), root_1);
				adaptor.addChild(root_1, stream_temporal_module.nextTree());
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:12:90: ( domain_element )*
				while ( stream_domain_element.hasNext() ) {
					adaptor.addChild(root_1, stream_domain_element.nextTree());
				}
				stream_domain_element.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 2, domain_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "domain"


	public static class temporal_module_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "temporal_module"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:14:1: temporal_module : 'TEMPORAL_MODULE' ID '=' range ',' INT ';' -> ^( ID range INT ) ;
	public final ddl3Parser.temporal_module_return temporal_module() throws RecognitionException {
		ddl3Parser.temporal_module_return retval = new ddl3Parser.temporal_module_return();
		retval.start = input.LT(1);
		int temporal_module_StartIndex = input.index();

		Object root_0 = null;

		Token string_literal10=null;
		Token ID11=null;
		Token char_literal12=null;
		Token char_literal14=null;
		Token INT15=null;
		Token char_literal16=null;
		ParserRuleReturnScope range13 =null;

		Object string_literal10_tree=null;
		Object ID11_tree=null;
		Object char_literal12_tree=null;
		Object char_literal14_tree=null;
		Object INT15_tree=null;
		Object char_literal16_tree=null;
		RewriteRuleTokenStream stream_22=new RewriteRuleTokenStream(adaptor,"token 22");
		RewriteRuleTokenStream stream_15=new RewriteRuleTokenStream(adaptor,"token 15");
		RewriteRuleTokenStream stream_19=new RewriteRuleTokenStream(adaptor,"token 19");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_74=new RewriteRuleTokenStream(adaptor,"token 74");
		RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
		RewriteRuleSubtreeStream stream_range=new RewriteRuleSubtreeStream(adaptor,"rule range");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 3) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:15:2: ( 'TEMPORAL_MODULE' ID '=' range ',' INT ';' -> ^( ID range INT ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:15:4: 'TEMPORAL_MODULE' ID '=' range ',' INT ';'
			{
			string_literal10=(Token)match(input,74,FOLLOW_74_in_temporal_module113); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_74.add(string_literal10);

			ID11=(Token)match(input,ID,FOLLOW_ID_in_temporal_module115); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID11);

			char_literal12=(Token)match(input,22,FOLLOW_22_in_temporal_module117); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_22.add(char_literal12);

			pushFollow(FOLLOW_range_in_temporal_module119);
			range13=range();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_range.add(range13.getTree());
			char_literal14=(Token)match(input,15,FOLLOW_15_in_temporal_module121); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_15.add(char_literal14);

			INT15=(Token)match(input,INT,FOLLOW_INT_in_temporal_module123); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_INT.add(INT15);

			char_literal16=(Token)match(input,19,FOLLOW_19_in_temporal_module125); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_19.add(char_literal16);

			// AST REWRITE
			// elements: INT, range, ID
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 15:47: -> ^( ID range INT )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:15:49: ^( ID range INT )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLTemporalModule(stream_ID.nextToken()), root_1);
				adaptor.addChild(root_1, stream_range.nextTree());
				adaptor.addChild(root_1, stream_INT.nextNode());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 3, temporal_module_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "temporal_module"


	public static class domain_element_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "domain_element"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:17:1: domain_element : ( parameter_type | component_type | component | timeline_synchronization );
	public final ddl3Parser.domain_element_return domain_element() throws RecognitionException {
		ddl3Parser.domain_element_return retval = new ddl3Parser.domain_element_return();
		retval.start = input.LT(1);
		int domain_element_StartIndex = input.index();

		Object root_0 = null;

		ParserRuleReturnScope parameter_type17 =null;
		ParserRuleReturnScope component_type18 =null;
		ParserRuleReturnScope component19 =null;
		ParserRuleReturnScope timeline_synchronization20 =null;


		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 4) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:18:2: ( parameter_type | component_type | component | timeline_synchronization )
			int alt4=4;
			switch ( input.LA(1) ) {
			case 60:
				{
				alt4=1;
				}
				break;
			case 35:
				{
				alt4=2;
				}
				break;
			case 34:
				{
				alt4=3;
				}
				break;
			case 71:
				{
				alt4=4;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 4, 0, input);
				throw nvae;
			}
			switch (alt4) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:18:4: parameter_type
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_parameter_type_in_domain_element146);
					parameter_type17=parameter_type();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, parameter_type17.getTree());

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:18:21: component_type
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_component_type_in_domain_element150);
					component_type18=component_type();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, component_type18.getTree());

					}
					break;
				case 3 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:18:38: component
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_component_in_domain_element154);
					component19=component();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, component19.getTree());

					}
					break;
				case 4 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:18:50: timeline_synchronization
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_timeline_synchronization_in_domain_element158);
					timeline_synchronization20=timeline_synchronization();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, timeline_synchronization20.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 4, domain_element_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "domain_element"


	public static class parameter_type_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "parameter_type"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:21:1: parameter_type : ( numeric_parameter_type | enumeration_parameter_type );
	public final ddl3Parser.parameter_type_return parameter_type() throws RecognitionException {
		ddl3Parser.parameter_type_return retval = new ddl3Parser.parameter_type_return();
		retval.start = input.LT(1);
		int parameter_type_StartIndex = input.index();

		Object root_0 = null;

		ParserRuleReturnScope numeric_parameter_type21 =null;
		ParserRuleReturnScope enumeration_parameter_type22 =null;


		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 5) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:22:2: ( numeric_parameter_type | enumeration_parameter_type )
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==60) ) {
				int LA5_1 = input.LA(2);
				if ( (LA5_1==57) ) {
					alt5=1;
				}
				else if ( (LA5_1==50) ) {
					alt5=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 5, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 5, 0, input);
				throw nvae;
			}

			switch (alt5) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:22:4: numeric_parameter_type
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_numeric_parameter_type_in_parameter_type168);
					numeric_parameter_type21=numeric_parameter_type();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_parameter_type21.getTree());

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:22:29: enumeration_parameter_type
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enumeration_parameter_type_in_parameter_type172);
					enumeration_parameter_type22=enumeration_parameter_type();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enumeration_parameter_type22.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 5, parameter_type_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "parameter_type"


	public static class numeric_parameter_type_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "numeric_parameter_type"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:24:1: numeric_parameter_type : 'PAR_TYPE' 'NumericParameterType' ID '=' '[' number ',' number ']' ';' -> ^( ID number number ) ;
	public final ddl3Parser.numeric_parameter_type_return numeric_parameter_type() throws RecognitionException {
		ddl3Parser.numeric_parameter_type_return retval = new ddl3Parser.numeric_parameter_type_return();
		retval.start = input.LT(1);
		int numeric_parameter_type_StartIndex = input.index();

		Object root_0 = null;

		Token string_literal23=null;
		Token string_literal24=null;
		Token ID25=null;
		Token char_literal26=null;
		Token char_literal27=null;
		Token char_literal29=null;
		Token char_literal31=null;
		Token char_literal32=null;
		ParserRuleReturnScope number28 =null;
		ParserRuleReturnScope number30 =null;

		Object string_literal23_tree=null;
		Object string_literal24_tree=null;
		Object ID25_tree=null;
		Object char_literal26_tree=null;
		Object char_literal27_tree=null;
		Object char_literal29_tree=null;
		Object char_literal31_tree=null;
		Object char_literal32_tree=null;
		RewriteRuleTokenStream stream_22=new RewriteRuleTokenStream(adaptor,"token 22");
		RewriteRuleTokenStream stream_77=new RewriteRuleTokenStream(adaptor,"token 77");
		RewriteRuleTokenStream stream_57=new RewriteRuleTokenStream(adaptor,"token 57");
		RewriteRuleTokenStream stream_15=new RewriteRuleTokenStream(adaptor,"token 15");
		RewriteRuleTokenStream stream_19=new RewriteRuleTokenStream(adaptor,"token 19");
		RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_76=new RewriteRuleTokenStream(adaptor,"token 76");
		RewriteRuleSubtreeStream stream_number=new RewriteRuleSubtreeStream(adaptor,"rule number");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 6) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:25:2: ( 'PAR_TYPE' 'NumericParameterType' ID '=' '[' number ',' number ']' ';' -> ^( ID number number ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:25:4: 'PAR_TYPE' 'NumericParameterType' ID '=' '[' number ',' number ']' ';'
			{
			string_literal23=(Token)match(input,60,FOLLOW_60_in_numeric_parameter_type182); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_60.add(string_literal23);

			string_literal24=(Token)match(input,57,FOLLOW_57_in_numeric_parameter_type184); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_57.add(string_literal24);

			ID25=(Token)match(input,ID,FOLLOW_ID_in_numeric_parameter_type186); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID25);

			char_literal26=(Token)match(input,22,FOLLOW_22_in_numeric_parameter_type188); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_22.add(char_literal26);

			char_literal27=(Token)match(input,76,FOLLOW_76_in_numeric_parameter_type190); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_76.add(char_literal27);

			pushFollow(FOLLOW_number_in_numeric_parameter_type192);
			number28=number();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_number.add(number28.getTree());
			char_literal29=(Token)match(input,15,FOLLOW_15_in_numeric_parameter_type194); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_15.add(char_literal29);

			pushFollow(FOLLOW_number_in_numeric_parameter_type196);
			number30=number();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_number.add(number30.getTree());
			char_literal31=(Token)match(input,77,FOLLOW_77_in_numeric_parameter_type198); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_77.add(char_literal31);

			char_literal32=(Token)match(input,19,FOLLOW_19_in_numeric_parameter_type200); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_19.add(char_literal32);

			// AST REWRITE
			// elements: number, ID, number
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 25:75: -> ^( ID number number )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:25:77: ^( ID number number )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLNumericParameterType(stream_ID.nextToken()), root_1);
				adaptor.addChild(root_1, stream_number.nextTree());
				adaptor.addChild(root_1, stream_number.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 6, numeric_parameter_type_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "numeric_parameter_type"


	public static class enumeration_parameter_type_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "enumeration_parameter_type"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:27:1: enumeration_parameter_type : 'PAR_TYPE' 'EnumerationParameterType' ID '=' '{' ID ( ',' ID )* '}' ';' -> ^( ID ( ID )* ) ;
	public final ddl3Parser.enumeration_parameter_type_return enumeration_parameter_type() throws RecognitionException {
		ddl3Parser.enumeration_parameter_type_return retval = new ddl3Parser.enumeration_parameter_type_return();
		retval.start = input.LT(1);
		int enumeration_parameter_type_StartIndex = input.index();

		Object root_0 = null;

		Token string_literal33=null;
		Token string_literal34=null;
		Token ID35=null;
		Token char_literal36=null;
		Token char_literal37=null;
		Token ID38=null;
		Token char_literal39=null;
		Token ID40=null;
		Token char_literal41=null;
		Token char_literal42=null;

		Object string_literal33_tree=null;
		Object string_literal34_tree=null;
		Object ID35_tree=null;
		Object char_literal36_tree=null;
		Object char_literal37_tree=null;
		Object ID38_tree=null;
		Object char_literal39_tree=null;
		Object ID40_tree=null;
		Object char_literal41_tree=null;
		Object char_literal42_tree=null;
		RewriteRuleTokenStream stream_22=new RewriteRuleTokenStream(adaptor,"token 22");
		RewriteRuleTokenStream stream_15=new RewriteRuleTokenStream(adaptor,"token 15");
		RewriteRuleTokenStream stream_80=new RewriteRuleTokenStream(adaptor,"token 80");
		RewriteRuleTokenStream stream_19=new RewriteRuleTokenStream(adaptor,"token 19");
		RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
		RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_50=new RewriteRuleTokenStream(adaptor,"token 50");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 7) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:28:2: ( 'PAR_TYPE' 'EnumerationParameterType' ID '=' '{' ID ( ',' ID )* '}' ';' -> ^( ID ( ID )* ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:28:4: 'PAR_TYPE' 'EnumerationParameterType' ID '=' '{' ID ( ',' ID )* '}' ';'
			{
			string_literal33=(Token)match(input,60,FOLLOW_60_in_enumeration_parameter_type222); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_60.add(string_literal33);

			string_literal34=(Token)match(input,50,FOLLOW_50_in_enumeration_parameter_type224); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_50.add(string_literal34);

			ID35=(Token)match(input,ID,FOLLOW_ID_in_enumeration_parameter_type226); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID35);

			char_literal36=(Token)match(input,22,FOLLOW_22_in_enumeration_parameter_type228); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_22.add(char_literal36);

			char_literal37=(Token)match(input,80,FOLLOW_80_in_enumeration_parameter_type230); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_80.add(char_literal37);

			ID38=(Token)match(input,ID,FOLLOW_ID_in_enumeration_parameter_type232); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID38);

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:28:56: ( ',' ID )*
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( (LA6_0==15) ) {
					alt6=1;
				}

				switch (alt6) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:28:57: ',' ID
					{
					char_literal39=(Token)match(input,15,FOLLOW_15_in_enumeration_parameter_type235); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_15.add(char_literal39);

					ID40=(Token)match(input,ID,FOLLOW_ID_in_enumeration_parameter_type237); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(ID40);

					}
					break;

				default :
					break loop6;
				}
			}

			char_literal41=(Token)match(input,81,FOLLOW_81_in_enumeration_parameter_type241); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_81.add(char_literal41);

			char_literal42=(Token)match(input,19,FOLLOW_19_in_enumeration_parameter_type243); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_19.add(char_literal42);

			// AST REWRITE
			// elements: ID, ID
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 28:74: -> ^( ID ( ID )* )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:28:76: ^( ID ( ID )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLEnumerationParameterType(stream_ID.nextToken()), root_1);
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:28:110: ( ID )*
				while ( stream_ID.hasNext() ) {
					adaptor.addChild(root_1, stream_ID.nextNode());
				}
				stream_ID.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 7, enumeration_parameter_type_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "enumeration_parameter_type"


	public static class temporal_relation_type_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "temporal_relation_type"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:31:1: temporal_relation_type : ( 'MEETS' -> ^( 'MEETS' ) | 'MET-BY' -> ^( 'MET-BY' ) | 'BEFORE' range -> ^( 'BEFORE' range ) | 'AFTER' range -> ^( 'AFTER' range ) | 'EQUALS' -> ^( 'EQUALS' ) | 'STARTS' -> ^( 'STARTS' ) | 'STARTED-BY' -> ^( 'STARTED-BY' ) | 'FINISHES' -> ^( 'FINISHES' ) | 'FINISHED-BY' -> ^( 'FINISHED-BY' ) | 'DURING' range range -> ^( 'DURING' range range ) | 'CONTAINS' range range -> ^( 'CONTAINS' range range ) | 'OVERLAPS' range -> ^( 'OVERLAPS' range ) | 'OVERLAPPED-BY' range -> ^( 'OVERLAPPED-BY' range ) | 'STARTS-AT' -> ^( 'STARTS-AT' ) | 'ENDS-AT' -> ^( 'ENDS-AT' ) | 'AT-START' -> ^( 'AT-START' ) | 'AT-END' -> ^( 'AT-END' ) | 'BEFORE-START' range -> ^( 'BEFORE-START' range ) | 'AFTER-END' range -> ^( 'AFTER-END' range ) | 'START-START' range -> ^( 'START-START' range ) | 'START-END' range -> ^( 'START-END' range ) | 'END-START' range -> ^( 'END-START' range ) | 'END-END' range -> ^( 'END-END' range ) | 'CONTAINS-START' range range -> ^( 'CONTAINS-START' range range ) | 'CONTAINS-END' range range -> ^( 'CONTAINS-END' range range ) | 'STARTS-DURING' range range -> ^( 'STARTS-DURING' range range ) | 'ENDS-DURING' range range -> ^( 'ENDS-DURING' range range ) );
	public final ddl3Parser.temporal_relation_type_return temporal_relation_type() throws RecognitionException {
		ddl3Parser.temporal_relation_type_return retval = new ddl3Parser.temporal_relation_type_return();
		retval.start = input.LT(1);
		int temporal_relation_type_StartIndex = input.index();

		Object root_0 = null;

		Token string_literal43=null;
		Token string_literal44=null;
		Token string_literal45=null;
		Token string_literal47=null;
		Token string_literal49=null;
		Token string_literal50=null;
		Token string_literal51=null;
		Token string_literal52=null;
		Token string_literal53=null;
		Token string_literal54=null;
		Token string_literal57=null;
		Token string_literal60=null;
		Token string_literal62=null;
		Token string_literal64=null;
		Token string_literal65=null;
		Token string_literal66=null;
		Token string_literal67=null;
		Token string_literal68=null;
		Token string_literal70=null;
		Token string_literal72=null;
		Token string_literal74=null;
		Token string_literal76=null;
		Token string_literal78=null;
		Token string_literal80=null;
		Token string_literal83=null;
		Token string_literal86=null;
		Token string_literal89=null;
		ParserRuleReturnScope range46 =null;
		ParserRuleReturnScope range48 =null;
		ParserRuleReturnScope range55 =null;
		ParserRuleReturnScope range56 =null;
		ParserRuleReturnScope range58 =null;
		ParserRuleReturnScope range59 =null;
		ParserRuleReturnScope range61 =null;
		ParserRuleReturnScope range63 =null;
		ParserRuleReturnScope range69 =null;
		ParserRuleReturnScope range71 =null;
		ParserRuleReturnScope range73 =null;
		ParserRuleReturnScope range75 =null;
		ParserRuleReturnScope range77 =null;
		ParserRuleReturnScope range79 =null;
		ParserRuleReturnScope range81 =null;
		ParserRuleReturnScope range82 =null;
		ParserRuleReturnScope range84 =null;
		ParserRuleReturnScope range85 =null;
		ParserRuleReturnScope range87 =null;
		ParserRuleReturnScope range88 =null;
		ParserRuleReturnScope range90 =null;
		ParserRuleReturnScope range91 =null;

		Object string_literal43_tree=null;
		Object string_literal44_tree=null;
		Object string_literal45_tree=null;
		Object string_literal47_tree=null;
		Object string_literal49_tree=null;
		Object string_literal50_tree=null;
		Object string_literal51_tree=null;
		Object string_literal52_tree=null;
		Object string_literal53_tree=null;
		Object string_literal54_tree=null;
		Object string_literal57_tree=null;
		Object string_literal60_tree=null;
		Object string_literal62_tree=null;
		Object string_literal64_tree=null;
		Object string_literal65_tree=null;
		Object string_literal66_tree=null;
		Object string_literal67_tree=null;
		Object string_literal68_tree=null;
		Object string_literal70_tree=null;
		Object string_literal72_tree=null;
		Object string_literal74_tree=null;
		Object string_literal76_tree=null;
		Object string_literal78_tree=null;
		Object string_literal80_tree=null;
		Object string_literal83_tree=null;
		Object string_literal86_tree=null;
		Object string_literal89_tree=null;
		RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
		RewriteRuleTokenStream stream_44=new RewriteRuleTokenStream(adaptor,"token 44");
		RewriteRuleTokenStream stream_67=new RewriteRuleTokenStream(adaptor,"token 67");
		RewriteRuleTokenStream stream_45=new RewriteRuleTokenStream(adaptor,"token 45");
		RewriteRuleTokenStream stream_68=new RewriteRuleTokenStream(adaptor,"token 68");
		RewriteRuleTokenStream stream_46=new RewriteRuleTokenStream(adaptor,"token 46");
		RewriteRuleTokenStream stream_47=new RewriteRuleTokenStream(adaptor,"token 47");
		RewriteRuleTokenStream stream_69=new RewriteRuleTokenStream(adaptor,"token 69");
		RewriteRuleTokenStream stream_26=new RewriteRuleTokenStream(adaptor,"token 26");
		RewriteRuleTokenStream stream_27=new RewriteRuleTokenStream(adaptor,"token 27");
		RewriteRuleTokenStream stream_29=new RewriteRuleTokenStream(adaptor,"token 29");
		RewriteRuleTokenStream stream_70=new RewriteRuleTokenStream(adaptor,"token 70");
		RewriteRuleTokenStream stream_51=new RewriteRuleTokenStream(adaptor,"token 51");
		RewriteRuleTokenStream stream_52=new RewriteRuleTokenStream(adaptor,"token 52");
		RewriteRuleTokenStream stream_30=new RewriteRuleTokenStream(adaptor,"token 30");
		RewriteRuleTokenStream stream_31=new RewriteRuleTokenStream(adaptor,"token 31");
		RewriteRuleTokenStream stream_32=new RewriteRuleTokenStream(adaptor,"token 32");
		RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
		RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_37=new RewriteRuleTokenStream(adaptor,"token 37");
		RewriteRuleTokenStream stream_59=new RewriteRuleTokenStream(adaptor,"token 59");
		RewriteRuleTokenStream stream_38=new RewriteRuleTokenStream(adaptor,"token 38");
		RewriteRuleTokenStream stream_39=new RewriteRuleTokenStream(adaptor,"token 39");
		RewriteRuleTokenStream stream_42=new RewriteRuleTokenStream(adaptor,"token 42");
		RewriteRuleTokenStream stream_65=new RewriteRuleTokenStream(adaptor,"token 65");
		RewriteRuleTokenStream stream_43=new RewriteRuleTokenStream(adaptor,"token 43");
		RewriteRuleSubtreeStream stream_range=new RewriteRuleSubtreeStream(adaptor,"rule range");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 8) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:32:2: ( 'MEETS' -> ^( 'MEETS' ) | 'MET-BY' -> ^( 'MET-BY' ) | 'BEFORE' range -> ^( 'BEFORE' range ) | 'AFTER' range -> ^( 'AFTER' range ) | 'EQUALS' -> ^( 'EQUALS' ) | 'STARTS' -> ^( 'STARTS' ) | 'STARTED-BY' -> ^( 'STARTED-BY' ) | 'FINISHES' -> ^( 'FINISHES' ) | 'FINISHED-BY' -> ^( 'FINISHED-BY' ) | 'DURING' range range -> ^( 'DURING' range range ) | 'CONTAINS' range range -> ^( 'CONTAINS' range range ) | 'OVERLAPS' range -> ^( 'OVERLAPS' range ) | 'OVERLAPPED-BY' range -> ^( 'OVERLAPPED-BY' range ) | 'STARTS-AT' -> ^( 'STARTS-AT' ) | 'ENDS-AT' -> ^( 'ENDS-AT' ) | 'AT-START' -> ^( 'AT-START' ) | 'AT-END' -> ^( 'AT-END' ) | 'BEFORE-START' range -> ^( 'BEFORE-START' range ) | 'AFTER-END' range -> ^( 'AFTER-END' range ) | 'START-START' range -> ^( 'START-START' range ) | 'START-END' range -> ^( 'START-END' range ) | 'END-START' range -> ^( 'END-START' range ) | 'END-END' range -> ^( 'END-END' range ) | 'CONTAINS-START' range range -> ^( 'CONTAINS-START' range range ) | 'CONTAINS-END' range range -> ^( 'CONTAINS-END' range range ) | 'STARTS-DURING' range range -> ^( 'STARTS-DURING' range range ) | 'ENDS-DURING' range range -> ^( 'ENDS-DURING' range range ) )
			int alt7=27;
			switch ( input.LA(1) ) {
			case 55:
				{
				alt7=1;
				}
				break;
			case 56:
				{
				alt7=2;
				}
				break;
			case 31:
				{
				alt7=3;
				}
				break;
			case 26:
				{
				alt7=4;
				}
				break;
			case 47:
				{
				alt7=5;
				}
				break;
			case 68:
				{
				alt7=6;
				}
				break;
			case 67:
				{
				alt7=7;
				}
				break;
			case 52:
				{
				alt7=8;
				}
				break;
			case 51:
				{
				alt7=9;
				}
				break;
			case 42:
				{
				alt7=10;
				}
				break;
			case 37:
				{
				alt7=11;
				}
				break;
			case 59:
				{
				alt7=12;
				}
				break;
			case 58:
				{
				alt7=13;
				}
				break;
			case 69:
				{
				alt7=14;
				}
				break;
			case 45:
				{
				alt7=15;
				}
				break;
			case 30:
				{
				alt7=16;
				}
				break;
			case 29:
				{
				alt7=17;
				}
				break;
			case 32:
				{
				alt7=18;
				}
				break;
			case 27:
				{
				alt7=19;
				}
				break;
			case 66:
				{
				alt7=20;
				}
				break;
			case 65:
				{
				alt7=21;
				}
				break;
			case 44:
				{
				alt7=22;
				}
				break;
			case 43:
				{
				alt7=23;
				}
				break;
			case 39:
				{
				alt7=24;
				}
				break;
			case 38:
				{
				alt7=25;
				}
				break;
			case 70:
				{
				alt7=26;
				}
				break;
			case 46:
				{
				alt7=27;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 7, 0, input);
				throw nvae;
			}
			switch (alt7) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:32:4: 'MEETS'
					{
					string_literal43=(Token)match(input,55,FOLLOW_55_in_temporal_relation_type264); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_55.add(string_literal43);

					// AST REWRITE
					// elements: 55
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 32:12: -> ^( 'MEETS' )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:32:14: ^( 'MEETS' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_55.nextToken()), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:33:4: 'MET-BY'
					{
					string_literal44=(Token)match(input,56,FOLLOW_56_in_temporal_relation_type277); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_56.add(string_literal44);

					// AST REWRITE
					// elements: 56
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 33:13: -> ^( 'MET-BY' )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:33:15: ^( 'MET-BY' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_56.nextToken()), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:34:4: 'BEFORE' range
					{
					string_literal45=(Token)match(input,31,FOLLOW_31_in_temporal_relation_type290); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_31.add(string_literal45);

					pushFollow(FOLLOW_range_in_temporal_relation_type292);
					range46=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range46.getTree());
					// AST REWRITE
					// elements: range, 31
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 34:19: -> ^( 'BEFORE' range )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:34:21: ^( 'BEFORE' range )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_31.nextToken()), root_1);
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 4 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:35:4: 'AFTER' range
					{
					string_literal47=(Token)match(input,26,FOLLOW_26_in_temporal_relation_type307); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_26.add(string_literal47);

					pushFollow(FOLLOW_range_in_temporal_relation_type309);
					range48=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range48.getTree());
					// AST REWRITE
					// elements: 26, range
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 35:18: -> ^( 'AFTER' range )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:35:20: ^( 'AFTER' range )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_26.nextToken()), root_1);
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 5 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:36:4: 'EQUALS'
					{
					string_literal49=(Token)match(input,47,FOLLOW_47_in_temporal_relation_type324); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_47.add(string_literal49);

					// AST REWRITE
					// elements: 47
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 36:13: -> ^( 'EQUALS' )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:36:15: ^( 'EQUALS' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_47.nextToken()), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 6 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:37:4: 'STARTS'
					{
					string_literal50=(Token)match(input,68,FOLLOW_68_in_temporal_relation_type337); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_68.add(string_literal50);

					// AST REWRITE
					// elements: 68
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 37:13: -> ^( 'STARTS' )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:37:15: ^( 'STARTS' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_68.nextToken()), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 7 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:38:4: 'STARTED-BY'
					{
					string_literal51=(Token)match(input,67,FOLLOW_67_in_temporal_relation_type350); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_67.add(string_literal51);

					// AST REWRITE
					// elements: 67
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 38:17: -> ^( 'STARTED-BY' )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:38:19: ^( 'STARTED-BY' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_67.nextToken()), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 8 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:39:4: 'FINISHES'
					{
					string_literal52=(Token)match(input,52,FOLLOW_52_in_temporal_relation_type363); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_52.add(string_literal52);

					// AST REWRITE
					// elements: 52
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 39:15: -> ^( 'FINISHES' )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:39:17: ^( 'FINISHES' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_52.nextToken()), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 9 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:40:4: 'FINISHED-BY'
					{
					string_literal53=(Token)match(input,51,FOLLOW_51_in_temporal_relation_type376); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_51.add(string_literal53);

					// AST REWRITE
					// elements: 51
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 40:18: -> ^( 'FINISHED-BY' )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:40:20: ^( 'FINISHED-BY' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_51.nextToken()), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 10 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:41:4: 'DURING' range range
					{
					string_literal54=(Token)match(input,42,FOLLOW_42_in_temporal_relation_type389); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_42.add(string_literal54);

					pushFollow(FOLLOW_range_in_temporal_relation_type391);
					range55=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range55.getTree());
					pushFollow(FOLLOW_range_in_temporal_relation_type393);
					range56=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range56.getTree());
					// AST REWRITE
					// elements: range, range, 42
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 41:25: -> ^( 'DURING' range range )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:41:27: ^( 'DURING' range range )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_42.nextToken()), root_1);
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 11 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:42:4: 'CONTAINS' range range
					{
					string_literal57=(Token)match(input,37,FOLLOW_37_in_temporal_relation_type410); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_37.add(string_literal57);

					pushFollow(FOLLOW_range_in_temporal_relation_type412);
					range58=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range58.getTree());
					pushFollow(FOLLOW_range_in_temporal_relation_type414);
					range59=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range59.getTree());
					// AST REWRITE
					// elements: 37, range, range
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 42:27: -> ^( 'CONTAINS' range range )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:42:29: ^( 'CONTAINS' range range )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_37.nextToken()), root_1);
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 12 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:43:4: 'OVERLAPS' range
					{
					string_literal60=(Token)match(input,59,FOLLOW_59_in_temporal_relation_type431); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_59.add(string_literal60);

					pushFollow(FOLLOW_range_in_temporal_relation_type433);
					range61=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range61.getTree());
					// AST REWRITE
					// elements: range, 59
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 43:21: -> ^( 'OVERLAPS' range )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:43:23: ^( 'OVERLAPS' range )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_59.nextToken()), root_1);
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 13 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:44:4: 'OVERLAPPED-BY' range
					{
					string_literal62=(Token)match(input,58,FOLLOW_58_in_temporal_relation_type448); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(string_literal62);

					pushFollow(FOLLOW_range_in_temporal_relation_type450);
					range63=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range63.getTree());
					// AST REWRITE
					// elements: 58, range
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 44:26: -> ^( 'OVERLAPPED-BY' range )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:44:28: ^( 'OVERLAPPED-BY' range )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_58.nextToken()), root_1);
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 14 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:45:4: 'STARTS-AT'
					{
					string_literal64=(Token)match(input,69,FOLLOW_69_in_temporal_relation_type465); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_69.add(string_literal64);

					// AST REWRITE
					// elements: 69
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 45:16: -> ^( 'STARTS-AT' )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:45:18: ^( 'STARTS-AT' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_69.nextToken()), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 15 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:46:4: 'ENDS-AT'
					{
					string_literal65=(Token)match(input,45,FOLLOW_45_in_temporal_relation_type478); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_45.add(string_literal65);

					// AST REWRITE
					// elements: 45
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 46:14: -> ^( 'ENDS-AT' )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:46:16: ^( 'ENDS-AT' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_45.nextToken()), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 16 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:47:4: 'AT-START'
					{
					string_literal66=(Token)match(input,30,FOLLOW_30_in_temporal_relation_type491); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_30.add(string_literal66);

					// AST REWRITE
					// elements: 30
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 47:15: -> ^( 'AT-START' )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:47:17: ^( 'AT-START' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_30.nextToken()), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 17 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:48:4: 'AT-END'
					{
					string_literal67=(Token)match(input,29,FOLLOW_29_in_temporal_relation_type504); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_29.add(string_literal67);

					// AST REWRITE
					// elements: 29
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 48:13: -> ^( 'AT-END' )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:48:15: ^( 'AT-END' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_29.nextToken()), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 18 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:49:4: 'BEFORE-START' range
					{
					string_literal68=(Token)match(input,32,FOLLOW_32_in_temporal_relation_type517); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_32.add(string_literal68);

					pushFollow(FOLLOW_range_in_temporal_relation_type519);
					range69=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range69.getTree());
					// AST REWRITE
					// elements: range, 32
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 49:25: -> ^( 'BEFORE-START' range )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:49:27: ^( 'BEFORE-START' range )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_32.nextToken()), root_1);
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 19 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:50:4: 'AFTER-END' range
					{
					string_literal70=(Token)match(input,27,FOLLOW_27_in_temporal_relation_type534); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_27.add(string_literal70);

					pushFollow(FOLLOW_range_in_temporal_relation_type536);
					range71=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range71.getTree());
					// AST REWRITE
					// elements: range, 27
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 50:22: -> ^( 'AFTER-END' range )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:50:24: ^( 'AFTER-END' range )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_27.nextToken()), root_1);
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 20 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:51:4: 'START-START' range
					{
					string_literal72=(Token)match(input,66,FOLLOW_66_in_temporal_relation_type551); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_66.add(string_literal72);

					pushFollow(FOLLOW_range_in_temporal_relation_type553);
					range73=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range73.getTree());
					// AST REWRITE
					// elements: 66, range
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 51:24: -> ^( 'START-START' range )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:51:26: ^( 'START-START' range )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_66.nextToken()), root_1);
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 21 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:52:4: 'START-END' range
					{
					string_literal74=(Token)match(input,65,FOLLOW_65_in_temporal_relation_type568); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_65.add(string_literal74);

					pushFollow(FOLLOW_range_in_temporal_relation_type570);
					range75=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range75.getTree());
					// AST REWRITE
					// elements: 65, range
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 52:22: -> ^( 'START-END' range )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:52:24: ^( 'START-END' range )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_65.nextToken()), root_1);
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 22 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:53:4: 'END-START' range
					{
					string_literal76=(Token)match(input,44,FOLLOW_44_in_temporal_relation_type585); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_44.add(string_literal76);

					pushFollow(FOLLOW_range_in_temporal_relation_type587);
					range77=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range77.getTree());
					// AST REWRITE
					// elements: range, 44
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 53:22: -> ^( 'END-START' range )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:53:24: ^( 'END-START' range )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_44.nextToken()), root_1);
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 23 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:54:4: 'END-END' range
					{
					string_literal78=(Token)match(input,43,FOLLOW_43_in_temporal_relation_type602); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_43.add(string_literal78);

					pushFollow(FOLLOW_range_in_temporal_relation_type604);
					range79=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range79.getTree());
					// AST REWRITE
					// elements: range, 43
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 54:20: -> ^( 'END-END' range )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:54:22: ^( 'END-END' range )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_43.nextToken()), root_1);
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 24 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:55:4: 'CONTAINS-START' range range
					{
					string_literal80=(Token)match(input,39,FOLLOW_39_in_temporal_relation_type619); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_39.add(string_literal80);

					pushFollow(FOLLOW_range_in_temporal_relation_type621);
					range81=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range81.getTree());
					pushFollow(FOLLOW_range_in_temporal_relation_type623);
					range82=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range82.getTree());
					// AST REWRITE
					// elements: range, 39, range
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 55:33: -> ^( 'CONTAINS-START' range range )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:55:35: ^( 'CONTAINS-START' range range )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_39.nextToken()), root_1);
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 25 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:56:4: 'CONTAINS-END' range range
					{
					string_literal83=(Token)match(input,38,FOLLOW_38_in_temporal_relation_type640); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_38.add(string_literal83);

					pushFollow(FOLLOW_range_in_temporal_relation_type642);
					range84=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range84.getTree());
					pushFollow(FOLLOW_range_in_temporal_relation_type644);
					range85=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range85.getTree());
					// AST REWRITE
					// elements: range, range, 38
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 56:31: -> ^( 'CONTAINS-END' range range )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:56:33: ^( 'CONTAINS-END' range range )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_38.nextToken()), root_1);
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 26 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:57:4: 'STARTS-DURING' range range
					{
					string_literal86=(Token)match(input,70,FOLLOW_70_in_temporal_relation_type661); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_70.add(string_literal86);

					pushFollow(FOLLOW_range_in_temporal_relation_type663);
					range87=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range87.getTree());
					pushFollow(FOLLOW_range_in_temporal_relation_type665);
					range88=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range88.getTree());
					// AST REWRITE
					// elements: 70, range, range
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 57:32: -> ^( 'STARTS-DURING' range range )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:57:34: ^( 'STARTS-DURING' range range )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_70.nextToken()), root_1);
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 27 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:58:4: 'ENDS-DURING' range range
					{
					string_literal89=(Token)match(input,46,FOLLOW_46_in_temporal_relation_type682); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_46.add(string_literal89);

					pushFollow(FOLLOW_range_in_temporal_relation_type684);
					range90=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range90.getTree());
					pushFollow(FOLLOW_range_in_temporal_relation_type686);
					range91=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range91.getTree());
					// AST REWRITE
					// elements: range, range, 46
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 58:30: -> ^( 'ENDS-DURING' range range )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:58:32: ^( 'ENDS-DURING' range range )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelationType(stream_46.nextToken()), root_1);
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_1, stream_range.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 8, temporal_relation_type_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "temporal_relation_type"


	public static class parameter_constraint_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "parameter_constraint"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:61:1: parameter_constraint : ( numeric_parameter_constraint | enumeration_parameter_constraint );
	public final ddl3Parser.parameter_constraint_return parameter_constraint() throws RecognitionException {
		ddl3Parser.parameter_constraint_return retval = new ddl3Parser.parameter_constraint_return();
		retval.start = input.LT(1);
		int parameter_constraint_StartIndex = input.index();

		Object root_0 = null;

		ParserRuleReturnScope numeric_parameter_constraint92 =null;
		ParserRuleReturnScope enumeration_parameter_constraint93 =null;


		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 9) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:62:2: ( numeric_parameter_constraint | enumeration_parameter_constraint )
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( (LA8_0==VarID) ) {
				switch ( input.LA(2) ) {
				case 22:
					{
					switch ( input.LA(3) ) {
					case VarID:
						{
						int LA8_5 = input.LA(4);
						if ( (synpred35_ddl3()) ) {
							alt8=1;
						}
						else if ( (true) ) {
							alt8=2;
						}

						}
						break;
					case ID:
						{
						alt8=2;
						}
						break;
					case INT:
					case 14:
					case 16:
					case 54:
						{
						alt8=1;
						}
						break;
					default:
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 8, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case 10:
					{
					switch ( input.LA(3) ) {
					case VarID:
						{
						int LA8_7 = input.LA(4);
						if ( (synpred35_ddl3()) ) {
							alt8=1;
						}
						else if ( (true) ) {
							alt8=2;
						}

						}
						break;
					case ID:
						{
						alt8=2;
						}
						break;
					case INT:
					case 14:
					case 16:
					case 54:
						{
						alt8=1;
						}
						break;
					default:
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 8, 4, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case 20:
				case 21:
				case 23:
				case 24:
					{
					alt8=1;
					}
					break;
				default:
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 8, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}
			else if ( (LA8_0==INT||LA8_0==14||LA8_0==16||LA8_0==54) ) {
				alt8=1;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 8, 0, input);
				throw nvae;
			}

			switch (alt8) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:62:4: numeric_parameter_constraint
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_numeric_parameter_constraint_in_parameter_constraint708);
					numeric_parameter_constraint92=numeric_parameter_constraint();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_parameter_constraint92.getTree());

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:62:35: enumeration_parameter_constraint
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enumeration_parameter_constraint_in_parameter_constraint712);
					enumeration_parameter_constraint93=enumeration_parameter_constraint();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enumeration_parameter_constraint93.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 9, parameter_constraint_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "parameter_constraint"


	public static class numeric_parameter_constraint_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "numeric_parameter_constraint"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:64:1: numeric_parameter_constraint : ( numeric_comparison_lvalue '=' numeric_comparison_rvalue -> ^( '=' numeric_comparison_lvalue numeric_comparison_rvalue ) | numeric_comparison_lvalue '<' numeric_comparison_rvalue -> ^( '<' numeric_comparison_lvalue numeric_comparison_rvalue ) | numeric_comparison_lvalue '>' numeric_comparison_rvalue -> ^( '>' numeric_comparison_lvalue numeric_comparison_rvalue ) | numeric_comparison_lvalue '<=' numeric_comparison_rvalue -> ^( '<=' numeric_comparison_lvalue numeric_comparison_rvalue ) | numeric_comparison_lvalue '>=' numeric_comparison_rvalue -> ^( '>=' numeric_comparison_lvalue numeric_comparison_rvalue ) | numeric_comparison_lvalue '!=' numeric_comparison_rvalue -> ^( '!=' numeric_comparison_lvalue numeric_comparison_rvalue ) );
	public final ddl3Parser.numeric_parameter_constraint_return numeric_parameter_constraint() throws RecognitionException {
		ddl3Parser.numeric_parameter_constraint_return retval = new ddl3Parser.numeric_parameter_constraint_return();
		retval.start = input.LT(1);
		int numeric_parameter_constraint_StartIndex = input.index();

		Object root_0 = null;

		Token char_literal95=null;
		Token char_literal98=null;
		Token char_literal101=null;
		Token string_literal104=null;
		Token string_literal107=null;
		Token string_literal110=null;
		ParserRuleReturnScope numeric_comparison_lvalue94 =null;
		ParserRuleReturnScope numeric_comparison_rvalue96 =null;
		ParserRuleReturnScope numeric_comparison_lvalue97 =null;
		ParserRuleReturnScope numeric_comparison_rvalue99 =null;
		ParserRuleReturnScope numeric_comparison_lvalue100 =null;
		ParserRuleReturnScope numeric_comparison_rvalue102 =null;
		ParserRuleReturnScope numeric_comparison_lvalue103 =null;
		ParserRuleReturnScope numeric_comparison_rvalue105 =null;
		ParserRuleReturnScope numeric_comparison_lvalue106 =null;
		ParserRuleReturnScope numeric_comparison_rvalue108 =null;
		ParserRuleReturnScope numeric_comparison_lvalue109 =null;
		ParserRuleReturnScope numeric_comparison_rvalue111 =null;

		Object char_literal95_tree=null;
		Object char_literal98_tree=null;
		Object char_literal101_tree=null;
		Object string_literal104_tree=null;
		Object string_literal107_tree=null;
		Object string_literal110_tree=null;
		RewriteRuleTokenStream stream_22=new RewriteRuleTokenStream(adaptor,"token 22");
		RewriteRuleTokenStream stream_23=new RewriteRuleTokenStream(adaptor,"token 23");
		RewriteRuleTokenStream stream_24=new RewriteRuleTokenStream(adaptor,"token 24");
		RewriteRuleTokenStream stream_20=new RewriteRuleTokenStream(adaptor,"token 20");
		RewriteRuleTokenStream stream_21=new RewriteRuleTokenStream(adaptor,"token 21");
		RewriteRuleTokenStream stream_10=new RewriteRuleTokenStream(adaptor,"token 10");
		RewriteRuleSubtreeStream stream_numeric_comparison_rvalue=new RewriteRuleSubtreeStream(adaptor,"rule numeric_comparison_rvalue");
		RewriteRuleSubtreeStream stream_numeric_comparison_lvalue=new RewriteRuleSubtreeStream(adaptor,"rule numeric_comparison_lvalue");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 10) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:65:2: ( numeric_comparison_lvalue '=' numeric_comparison_rvalue -> ^( '=' numeric_comparison_lvalue numeric_comparison_rvalue ) | numeric_comparison_lvalue '<' numeric_comparison_rvalue -> ^( '<' numeric_comparison_lvalue numeric_comparison_rvalue ) | numeric_comparison_lvalue '>' numeric_comparison_rvalue -> ^( '>' numeric_comparison_lvalue numeric_comparison_rvalue ) | numeric_comparison_lvalue '<=' numeric_comparison_rvalue -> ^( '<=' numeric_comparison_lvalue numeric_comparison_rvalue ) | numeric_comparison_lvalue '>=' numeric_comparison_rvalue -> ^( '>=' numeric_comparison_lvalue numeric_comparison_rvalue ) | numeric_comparison_lvalue '!=' numeric_comparison_rvalue -> ^( '!=' numeric_comparison_lvalue numeric_comparison_rvalue ) )
			int alt9=6;
			switch ( input.LA(1) ) {
			case VarID:
				{
				switch ( input.LA(2) ) {
				case 22:
					{
					alt9=1;
					}
					break;
				case 20:
					{
					alt9=2;
					}
					break;
				case 23:
					{
					alt9=3;
					}
					break;
				case 21:
					{
					alt9=4;
					}
					break;
				case 24:
					{
					alt9=5;
					}
					break;
				case 10:
					{
					alt9=6;
					}
					break;
				default:
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 9, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case 14:
			case 16:
				{
				int LA9_2 = input.LA(2);
				if ( (LA9_2==INT) ) {
					int LA9_3 = input.LA(3);
					if ( (LA9_3==13) ) {
						int LA9_11 = input.LA(4);
						if ( (LA9_11==VarID) ) {
							switch ( input.LA(5) ) {
							case 22:
								{
								alt9=1;
								}
								break;
							case 20:
								{
								alt9=2;
								}
								break;
							case 23:
								{
								alt9=3;
								}
								break;
							case 21:
								{
								alt9=4;
								}
								break;
							case 24:
								{
								alt9=5;
								}
								break;
							case 10:
								{
								alt9=6;
								}
								break;
							default:
								if (state.backtracking>0) {state.failed=true; return retval;}
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++) {
										input.consume();
									}
									NoViableAltException nvae =
										new NoViableAltException("", 9, 12, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 9, 11, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 9, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA9_2==54) ) {
					int LA9_4 = input.LA(3);
					if ( (LA9_4==13) ) {
						int LA9_11 = input.LA(4);
						if ( (LA9_11==VarID) ) {
							switch ( input.LA(5) ) {
							case 22:
								{
								alt9=1;
								}
								break;
							case 20:
								{
								alt9=2;
								}
								break;
							case 23:
								{
								alt9=3;
								}
								break;
							case 21:
								{
								alt9=4;
								}
								break;
							case 24:
								{
								alt9=5;
								}
								break;
							case 10:
								{
								alt9=6;
								}
								break;
							default:
								if (state.backtracking>0) {state.failed=true; return retval;}
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++) {
										input.consume();
									}
									NoViableAltException nvae =
										new NoViableAltException("", 9, 12, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 9, 11, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 9, 4, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 9, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case INT:
				{
				int LA9_3 = input.LA(2);
				if ( (LA9_3==13) ) {
					int LA9_11 = input.LA(3);
					if ( (LA9_11==VarID) ) {
						switch ( input.LA(4) ) {
						case 22:
							{
							alt9=1;
							}
							break;
						case 20:
							{
							alt9=2;
							}
							break;
						case 23:
							{
							alt9=3;
							}
							break;
						case 21:
							{
							alt9=4;
							}
							break;
						case 24:
							{
							alt9=5;
							}
							break;
						case 10:
							{
							alt9=6;
							}
							break;
						default:
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 9, 12, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 9, 11, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 9, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 54:
				{
				int LA9_4 = input.LA(2);
				if ( (LA9_4==13) ) {
					int LA9_11 = input.LA(3);
					if ( (LA9_11==VarID) ) {
						switch ( input.LA(4) ) {
						case 22:
							{
							alt9=1;
							}
							break;
						case 20:
							{
							alt9=2;
							}
							break;
						case 23:
							{
							alt9=3;
							}
							break;
						case 21:
							{
							alt9=4;
							}
							break;
						case 24:
							{
							alt9=5;
							}
							break;
						case 10:
							{
							alt9=6;
							}
							break;
						default:
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 9, 12, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 9, 11, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 9, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 9, 0, input);
				throw nvae;
			}
			switch (alt9) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:65:4: numeric_comparison_lvalue '=' numeric_comparison_rvalue
					{
					pushFollow(FOLLOW_numeric_comparison_lvalue_in_numeric_parameter_constraint722);
					numeric_comparison_lvalue94=numeric_comparison_lvalue();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_comparison_lvalue.add(numeric_comparison_lvalue94.getTree());
					char_literal95=(Token)match(input,22,FOLLOW_22_in_numeric_parameter_constraint724); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_22.add(char_literal95);

					pushFollow(FOLLOW_numeric_comparison_rvalue_in_numeric_parameter_constraint726);
					numeric_comparison_rvalue96=numeric_comparison_rvalue();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_comparison_rvalue.add(numeric_comparison_rvalue96.getTree());
					// AST REWRITE
					// elements: 22, numeric_comparison_lvalue, numeric_comparison_rvalue
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 65:60: -> ^( '=' numeric_comparison_lvalue numeric_comparison_rvalue )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:65:62: ^( '=' numeric_comparison_lvalue numeric_comparison_rvalue )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLNumericParameterConstraint(stream_22.nextToken()), root_1);
						adaptor.addChild(root_1, stream_numeric_comparison_lvalue.nextTree());
						adaptor.addChild(root_1, stream_numeric_comparison_rvalue.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:66:4: numeric_comparison_lvalue '<' numeric_comparison_rvalue
					{
					pushFollow(FOLLOW_numeric_comparison_lvalue_in_numeric_parameter_constraint743);
					numeric_comparison_lvalue97=numeric_comparison_lvalue();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_comparison_lvalue.add(numeric_comparison_lvalue97.getTree());
					char_literal98=(Token)match(input,20,FOLLOW_20_in_numeric_parameter_constraint745); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_20.add(char_literal98);

					pushFollow(FOLLOW_numeric_comparison_rvalue_in_numeric_parameter_constraint747);
					numeric_comparison_rvalue99=numeric_comparison_rvalue();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_comparison_rvalue.add(numeric_comparison_rvalue99.getTree());
					// AST REWRITE
					// elements: 20, numeric_comparison_rvalue, numeric_comparison_lvalue
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 66:60: -> ^( '<' numeric_comparison_lvalue numeric_comparison_rvalue )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:66:62: ^( '<' numeric_comparison_lvalue numeric_comparison_rvalue )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLNumericParameterConstraint(stream_20.nextToken()), root_1);
						adaptor.addChild(root_1, stream_numeric_comparison_lvalue.nextTree());
						adaptor.addChild(root_1, stream_numeric_comparison_rvalue.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:67:4: numeric_comparison_lvalue '>' numeric_comparison_rvalue
					{
					pushFollow(FOLLOW_numeric_comparison_lvalue_in_numeric_parameter_constraint764);
					numeric_comparison_lvalue100=numeric_comparison_lvalue();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_comparison_lvalue.add(numeric_comparison_lvalue100.getTree());
					char_literal101=(Token)match(input,23,FOLLOW_23_in_numeric_parameter_constraint766); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_23.add(char_literal101);

					pushFollow(FOLLOW_numeric_comparison_rvalue_in_numeric_parameter_constraint768);
					numeric_comparison_rvalue102=numeric_comparison_rvalue();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_comparison_rvalue.add(numeric_comparison_rvalue102.getTree());
					// AST REWRITE
					// elements: 23, numeric_comparison_rvalue, numeric_comparison_lvalue
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 67:60: -> ^( '>' numeric_comparison_lvalue numeric_comparison_rvalue )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:67:62: ^( '>' numeric_comparison_lvalue numeric_comparison_rvalue )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLNumericParameterConstraint(stream_23.nextToken()), root_1);
						adaptor.addChild(root_1, stream_numeric_comparison_lvalue.nextTree());
						adaptor.addChild(root_1, stream_numeric_comparison_rvalue.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 4 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:68:4: numeric_comparison_lvalue '<=' numeric_comparison_rvalue
					{
					pushFollow(FOLLOW_numeric_comparison_lvalue_in_numeric_parameter_constraint785);
					numeric_comparison_lvalue103=numeric_comparison_lvalue();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_comparison_lvalue.add(numeric_comparison_lvalue103.getTree());
					string_literal104=(Token)match(input,21,FOLLOW_21_in_numeric_parameter_constraint787); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_21.add(string_literal104);

					pushFollow(FOLLOW_numeric_comparison_rvalue_in_numeric_parameter_constraint789);
					numeric_comparison_rvalue105=numeric_comparison_rvalue();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_comparison_rvalue.add(numeric_comparison_rvalue105.getTree());
					// AST REWRITE
					// elements: 21, numeric_comparison_lvalue, numeric_comparison_rvalue
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 68:61: -> ^( '<=' numeric_comparison_lvalue numeric_comparison_rvalue )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:68:63: ^( '<=' numeric_comparison_lvalue numeric_comparison_rvalue )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLNumericParameterConstraint(stream_21.nextToken()), root_1);
						adaptor.addChild(root_1, stream_numeric_comparison_lvalue.nextTree());
						adaptor.addChild(root_1, stream_numeric_comparison_rvalue.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 5 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:69:4: numeric_comparison_lvalue '>=' numeric_comparison_rvalue
					{
					pushFollow(FOLLOW_numeric_comparison_lvalue_in_numeric_parameter_constraint806);
					numeric_comparison_lvalue106=numeric_comparison_lvalue();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_comparison_lvalue.add(numeric_comparison_lvalue106.getTree());
					string_literal107=(Token)match(input,24,FOLLOW_24_in_numeric_parameter_constraint808); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_24.add(string_literal107);

					pushFollow(FOLLOW_numeric_comparison_rvalue_in_numeric_parameter_constraint810);
					numeric_comparison_rvalue108=numeric_comparison_rvalue();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_comparison_rvalue.add(numeric_comparison_rvalue108.getTree());
					// AST REWRITE
					// elements: numeric_comparison_lvalue, 24, numeric_comparison_rvalue
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 69:61: -> ^( '>=' numeric_comparison_lvalue numeric_comparison_rvalue )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:69:63: ^( '>=' numeric_comparison_lvalue numeric_comparison_rvalue )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLNumericParameterConstraint(stream_24.nextToken()), root_1);
						adaptor.addChild(root_1, stream_numeric_comparison_lvalue.nextTree());
						adaptor.addChild(root_1, stream_numeric_comparison_rvalue.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 6 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:70:4: numeric_comparison_lvalue '!=' numeric_comparison_rvalue
					{
					pushFollow(FOLLOW_numeric_comparison_lvalue_in_numeric_parameter_constraint827);
					numeric_comparison_lvalue109=numeric_comparison_lvalue();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_comparison_lvalue.add(numeric_comparison_lvalue109.getTree());
					string_literal110=(Token)match(input,10,FOLLOW_10_in_numeric_parameter_constraint829); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_10.add(string_literal110);

					pushFollow(FOLLOW_numeric_comparison_rvalue_in_numeric_parameter_constraint831);
					numeric_comparison_rvalue111=numeric_comparison_rvalue();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_comparison_rvalue.add(numeric_comparison_rvalue111.getTree());
					// AST REWRITE
					// elements: numeric_comparison_rvalue, 10, numeric_comparison_lvalue
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 70:61: -> ^( '!=' numeric_comparison_lvalue numeric_comparison_rvalue )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:70:63: ^( '!=' numeric_comparison_lvalue numeric_comparison_rvalue )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLNumericParameterConstraint(stream_10.nextToken()), root_1);
						adaptor.addChild(root_1, stream_numeric_comparison_lvalue.nextTree());
						adaptor.addChild(root_1, stream_numeric_comparison_rvalue.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 10, numeric_parameter_constraint_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "numeric_parameter_constraint"


	public static class numeric_comparison_lvalue_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "numeric_comparison_lvalue"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:72:1: numeric_comparison_lvalue : ( VarID | number '*' ^ VarID );
	public final ddl3Parser.numeric_comparison_lvalue_return numeric_comparison_lvalue() throws RecognitionException {
		ddl3Parser.numeric_comparison_lvalue_return retval = new ddl3Parser.numeric_comparison_lvalue_return();
		retval.start = input.LT(1);
		int numeric_comparison_lvalue_StartIndex = input.index();

		Object root_0 = null;

		Token VarID112=null;
		Token char_literal114=null;
		Token VarID115=null;
		ParserRuleReturnScope number113 =null;

		Object VarID112_tree=null;
		Object char_literal114_tree=null;
		Object VarID115_tree=null;

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 11) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:73:2: ( VarID | number '*' ^ VarID )
			int alt10=2;
			int LA10_0 = input.LA(1);
			if ( (LA10_0==VarID) ) {
				alt10=1;
			}
			else if ( (LA10_0==INT||LA10_0==14||LA10_0==16||LA10_0==54) ) {
				alt10=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 10, 0, input);
				throw nvae;
			}

			switch (alt10) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:73:4: VarID
					{
					root_0 = (Object)adaptor.nil();


					VarID112=(Token)match(input,VarID,FOLLOW_VarID_in_numeric_comparison_lvalue852); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					VarID112_tree = (Object)adaptor.create(VarID112);
					adaptor.addChild(root_0, VarID112_tree);
					}

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:73:12: number '*' ^ VarID
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_number_in_numeric_comparison_lvalue856);
					number113=number();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, number113.getTree());

					char_literal114=(Token)match(input,13,FOLLOW_13_in_numeric_comparison_lvalue858); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal114_tree = (Object)adaptor.create(char_literal114);
					root_0 = (Object)adaptor.becomeRoot(char_literal114_tree, root_0);
					}

					VarID115=(Token)match(input,VarID,FOLLOW_VarID_in_numeric_comparison_lvalue861); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					VarID115_tree = (Object)adaptor.create(VarID115);
					adaptor.addChild(root_0, VarID115_tree);
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 11, numeric_comparison_lvalue_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "numeric_comparison_lvalue"


	public static class numeric_comparison_rvalue_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "numeric_comparison_rvalue"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:75:1: numeric_comparison_rvalue : first_numeric_comparison_rvalue ( other_numeric_comparison_rvalues )* ;
	public final ddl3Parser.numeric_comparison_rvalue_return numeric_comparison_rvalue() throws RecognitionException {
		ddl3Parser.numeric_comparison_rvalue_return retval = new ddl3Parser.numeric_comparison_rvalue_return();
		retval.start = input.LT(1);
		int numeric_comparison_rvalue_StartIndex = input.index();

		Object root_0 = null;

		ParserRuleReturnScope first_numeric_comparison_rvalue116 =null;
		ParserRuleReturnScope other_numeric_comparison_rvalues117 =null;


		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 12) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:76:2: ( first_numeric_comparison_rvalue ( other_numeric_comparison_rvalues )* )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:76:4: first_numeric_comparison_rvalue ( other_numeric_comparison_rvalues )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_first_numeric_comparison_rvalue_in_numeric_comparison_rvalue870);
			first_numeric_comparison_rvalue116=first_numeric_comparison_rvalue();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, first_numeric_comparison_rvalue116.getTree());

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:76:36: ( other_numeric_comparison_rvalues )*
			loop11:
			while (true) {
				int alt11=2;
				int LA11_0 = input.LA(1);
				if ( (LA11_0==14||LA11_0==16) ) {
					alt11=1;
				}

				switch (alt11) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:76:37: other_numeric_comparison_rvalues
					{
					pushFollow(FOLLOW_other_numeric_comparison_rvalues_in_numeric_comparison_rvalue873);
					other_numeric_comparison_rvalues117=other_numeric_comparison_rvalues();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, other_numeric_comparison_rvalues117.getTree());

					}
					break;

				default :
					break loop11;
				}
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 12, numeric_comparison_rvalue_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "numeric_comparison_rvalue"


	public static class first_numeric_comparison_rvalue_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "first_numeric_comparison_rvalue"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:78:1: first_numeric_comparison_rvalue : ( VarID | number '*' ^ VarID | number );
	public final ddl3Parser.first_numeric_comparison_rvalue_return first_numeric_comparison_rvalue() throws RecognitionException {
		ddl3Parser.first_numeric_comparison_rvalue_return retval = new ddl3Parser.first_numeric_comparison_rvalue_return();
		retval.start = input.LT(1);
		int first_numeric_comparison_rvalue_StartIndex = input.index();

		Object root_0 = null;

		Token VarID118=null;
		Token char_literal120=null;
		Token VarID121=null;
		ParserRuleReturnScope number119 =null;
		ParserRuleReturnScope number122 =null;

		Object VarID118_tree=null;
		Object char_literal120_tree=null;
		Object VarID121_tree=null;

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 13) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:79:2: ( VarID | number '*' ^ VarID | number )
			int alt12=3;
			switch ( input.LA(1) ) {
			case VarID:
				{
				alt12=1;
				}
				break;
			case 14:
			case 16:
				{
				int LA12_2 = input.LA(2);
				if ( (LA12_2==INT) ) {
					int LA12_3 = input.LA(3);
					if ( (LA12_3==13) ) {
						alt12=2;
					}
					else if ( (LA12_3==EOF||LA12_3==14||LA12_3==16||LA12_3==19) ) {
						alt12=3;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 12, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA12_2==54) ) {
					int LA12_4 = input.LA(3);
					if ( (LA12_4==13) ) {
						alt12=2;
					}
					else if ( (LA12_4==EOF||LA12_4==14||LA12_4==16||LA12_4==19) ) {
						alt12=3;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 12, 4, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 12, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case INT:
				{
				int LA12_3 = input.LA(2);
				if ( (LA12_3==13) ) {
					alt12=2;
				}
				else if ( (LA12_3==EOF||LA12_3==14||LA12_3==16||LA12_3==19) ) {
					alt12=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 12, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 54:
				{
				int LA12_4 = input.LA(2);
				if ( (LA12_4==13) ) {
					alt12=2;
				}
				else if ( (LA12_4==EOF||LA12_4==14||LA12_4==16||LA12_4==19) ) {
					alt12=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 12, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 12, 0, input);
				throw nvae;
			}
			switch (alt12) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:79:4: VarID
					{
					root_0 = (Object)adaptor.nil();


					VarID118=(Token)match(input,VarID,FOLLOW_VarID_in_first_numeric_comparison_rvalue884); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					VarID118_tree = (Object)adaptor.create(VarID118);
					adaptor.addChild(root_0, VarID118_tree);
					}

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:79:12: number '*' ^ VarID
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_number_in_first_numeric_comparison_rvalue888);
					number119=number();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, number119.getTree());

					char_literal120=(Token)match(input,13,FOLLOW_13_in_first_numeric_comparison_rvalue890); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal120_tree = (Object)adaptor.create(char_literal120);
					root_0 = (Object)adaptor.becomeRoot(char_literal120_tree, root_0);
					}

					VarID121=(Token)match(input,VarID,FOLLOW_VarID_in_first_numeric_comparison_rvalue893); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					VarID121_tree = (Object)adaptor.create(VarID121);
					adaptor.addChild(root_0, VarID121_tree);
					}

					}
					break;
				case 3 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:79:32: number
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_number_in_first_numeric_comparison_rvalue897);
					number122=number();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, number122.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 13, first_numeric_comparison_rvalue_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "first_numeric_comparison_rvalue"


	public static class other_numeric_comparison_rvalues_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "other_numeric_comparison_rvalues"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:81:1: other_numeric_comparison_rvalues : ( '+' | '-' ) ^ numeric_comparison_value ;
	public final ddl3Parser.other_numeric_comparison_rvalues_return other_numeric_comparison_rvalues() throws RecognitionException {
		ddl3Parser.other_numeric_comparison_rvalues_return retval = new ddl3Parser.other_numeric_comparison_rvalues_return();
		retval.start = input.LT(1);
		int other_numeric_comparison_rvalues_StartIndex = input.index();

		Object root_0 = null;

		Token set123=null;
		ParserRuleReturnScope numeric_comparison_value124 =null;

		Object set123_tree=null;

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 14) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:82:2: ( ( '+' | '-' ) ^ numeric_comparison_value )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:82:4: ( '+' | '-' ) ^ numeric_comparison_value
			{
			root_0 = (Object)adaptor.nil();


			set123=input.LT(1);
			set123=input.LT(1);
			if ( input.LA(1)==14||input.LA(1)==16 ) {
				input.consume();
				if ( state.backtracking==0 ) root_0 = (Object)adaptor.becomeRoot((Object)adaptor.create(set123), root_0);
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			pushFollow(FOLLOW_numeric_comparison_value_in_other_numeric_comparison_rvalues915);
			numeric_comparison_value124=numeric_comparison_value();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_comparison_value124.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 14, other_numeric_comparison_rvalues_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "other_numeric_comparison_rvalues"


	public static class numeric_comparison_value_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "numeric_comparison_value"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:84:1: numeric_comparison_value : ( VarID | INT '*' ^ VarID | INT );
	public final ddl3Parser.numeric_comparison_value_return numeric_comparison_value() throws RecognitionException {
		ddl3Parser.numeric_comparison_value_return retval = new ddl3Parser.numeric_comparison_value_return();
		retval.start = input.LT(1);
		int numeric_comparison_value_StartIndex = input.index();

		Object root_0 = null;

		Token VarID125=null;
		Token INT126=null;
		Token char_literal127=null;
		Token VarID128=null;
		Token INT129=null;

		Object VarID125_tree=null;
		Object INT126_tree=null;
		Object char_literal127_tree=null;
		Object VarID128_tree=null;
		Object INT129_tree=null;

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 15) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:85:2: ( VarID | INT '*' ^ VarID | INT )
			int alt13=3;
			int LA13_0 = input.LA(1);
			if ( (LA13_0==VarID) ) {
				alt13=1;
			}
			else if ( (LA13_0==INT) ) {
				int LA13_2 = input.LA(2);
				if ( (LA13_2==13) ) {
					alt13=2;
				}
				else if ( (LA13_2==EOF||LA13_2==14||LA13_2==16||LA13_2==19) ) {
					alt13=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 13, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 13, 0, input);
				throw nvae;
			}

			switch (alt13) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:85:4: VarID
					{
					root_0 = (Object)adaptor.nil();


					VarID125=(Token)match(input,VarID,FOLLOW_VarID_in_numeric_comparison_value924); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					VarID125_tree = (Object)adaptor.create(VarID125);
					adaptor.addChild(root_0, VarID125_tree);
					}

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:85:12: INT '*' ^ VarID
					{
					root_0 = (Object)adaptor.nil();


					INT126=(Token)match(input,INT,FOLLOW_INT_in_numeric_comparison_value928); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					INT126_tree = (Object)adaptor.create(INT126);
					adaptor.addChild(root_0, INT126_tree);
					}

					char_literal127=(Token)match(input,13,FOLLOW_13_in_numeric_comparison_value930); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal127_tree = (Object)adaptor.create(char_literal127);
					root_0 = (Object)adaptor.becomeRoot(char_literal127_tree, root_0);
					}

					VarID128=(Token)match(input,VarID,FOLLOW_VarID_in_numeric_comparison_value933); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					VarID128_tree = (Object)adaptor.create(VarID128);
					adaptor.addChild(root_0, VarID128_tree);
					}

					}
					break;
				case 3 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:85:29: INT
					{
					root_0 = (Object)adaptor.nil();


					INT129=(Token)match(input,INT,FOLLOW_INT_in_numeric_comparison_value937); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					INT129_tree = (Object)adaptor.create(INT129);
					adaptor.addChild(root_0, INT129_tree);
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 15, numeric_comparison_value_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "numeric_comparison_value"


	public static class enumeration_parameter_constraint_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "enumeration_parameter_constraint"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:87:1: enumeration_parameter_constraint : ( VarID '=' enumeration_comparison_rvalue -> ^( '=' VarID enumeration_comparison_rvalue ) | VarID '!=' enumeration_comparison_rvalue -> ^( '!=' VarID enumeration_comparison_rvalue ) );
	public final ddl3Parser.enumeration_parameter_constraint_return enumeration_parameter_constraint() throws RecognitionException {
		ddl3Parser.enumeration_parameter_constraint_return retval = new ddl3Parser.enumeration_parameter_constraint_return();
		retval.start = input.LT(1);
		int enumeration_parameter_constraint_StartIndex = input.index();

		Object root_0 = null;

		Token VarID130=null;
		Token char_literal131=null;
		Token VarID133=null;
		Token string_literal134=null;
		ParserRuleReturnScope enumeration_comparison_rvalue132 =null;
		ParserRuleReturnScope enumeration_comparison_rvalue135 =null;

		Object VarID130_tree=null;
		Object char_literal131_tree=null;
		Object VarID133_tree=null;
		Object string_literal134_tree=null;
		RewriteRuleTokenStream stream_22=new RewriteRuleTokenStream(adaptor,"token 22");
		RewriteRuleTokenStream stream_VarID=new RewriteRuleTokenStream(adaptor,"token VarID");
		RewriteRuleTokenStream stream_10=new RewriteRuleTokenStream(adaptor,"token 10");
		RewriteRuleSubtreeStream stream_enumeration_comparison_rvalue=new RewriteRuleSubtreeStream(adaptor,"rule enumeration_comparison_rvalue");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 16) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:88:2: ( VarID '=' enumeration_comparison_rvalue -> ^( '=' VarID enumeration_comparison_rvalue ) | VarID '!=' enumeration_comparison_rvalue -> ^( '!=' VarID enumeration_comparison_rvalue ) )
			int alt14=2;
			int LA14_0 = input.LA(1);
			if ( (LA14_0==VarID) ) {
				int LA14_1 = input.LA(2);
				if ( (LA14_1==22) ) {
					alt14=1;
				}
				else if ( (LA14_1==10) ) {
					alt14=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 14, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 14, 0, input);
				throw nvae;
			}

			switch (alt14) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:88:4: VarID '=' enumeration_comparison_rvalue
					{
					VarID130=(Token)match(input,VarID,FOLLOW_VarID_in_enumeration_parameter_constraint946); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_VarID.add(VarID130);

					char_literal131=(Token)match(input,22,FOLLOW_22_in_enumeration_parameter_constraint948); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_22.add(char_literal131);

					pushFollow(FOLLOW_enumeration_comparison_rvalue_in_enumeration_parameter_constraint950);
					enumeration_comparison_rvalue132=enumeration_comparison_rvalue();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_enumeration_comparison_rvalue.add(enumeration_comparison_rvalue132.getTree());
					// AST REWRITE
					// elements: 22, VarID, enumeration_comparison_rvalue
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 88:44: -> ^( '=' VarID enumeration_comparison_rvalue )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:88:46: ^( '=' VarID enumeration_comparison_rvalue )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLEnumerationParameterConstraint(stream_22.nextToken()), root_1);
						adaptor.addChild(root_1, stream_VarID.nextNode());
						adaptor.addChild(root_1, stream_enumeration_comparison_rvalue.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:88:126: VarID '!=' enumeration_comparison_rvalue
					{
					VarID133=(Token)match(input,VarID,FOLLOW_VarID_in_enumeration_parameter_constraint966); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_VarID.add(VarID133);

					string_literal134=(Token)match(input,10,FOLLOW_10_in_enumeration_parameter_constraint968); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_10.add(string_literal134);

					pushFollow(FOLLOW_enumeration_comparison_rvalue_in_enumeration_parameter_constraint970);
					enumeration_comparison_rvalue135=enumeration_comparison_rvalue();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_enumeration_comparison_rvalue.add(enumeration_comparison_rvalue135.getTree());
					// AST REWRITE
					// elements: 10, VarID, enumeration_comparison_rvalue
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 88:167: -> ^( '!=' VarID enumeration_comparison_rvalue )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:88:169: ^( '!=' VarID enumeration_comparison_rvalue )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLEnumerationParameterConstraint(stream_10.nextToken()), root_1);
						adaptor.addChild(root_1, stream_VarID.nextNode());
						adaptor.addChild(root_1, stream_enumeration_comparison_rvalue.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 16, enumeration_parameter_constraint_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "enumeration_parameter_constraint"


	public static class enumeration_comparison_rvalue_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "enumeration_comparison_rvalue"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:90:1: enumeration_comparison_rvalue : ( VarID | ID );
	public final ddl3Parser.enumeration_comparison_rvalue_return enumeration_comparison_rvalue() throws RecognitionException {
		ddl3Parser.enumeration_comparison_rvalue_return retval = new ddl3Parser.enumeration_comparison_rvalue_return();
		retval.start = input.LT(1);
		int enumeration_comparison_rvalue_StartIndex = input.index();

		Object root_0 = null;

		Token set136=null;

		Object set136_tree=null;

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 17) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:91:2: ( VarID | ID )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:
			{
			root_0 = (Object)adaptor.nil();


			set136=input.LT(1);
			if ( input.LA(1)==ID||input.LA(1)==VarID ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set136));
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 17, enumeration_comparison_rvalue_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "enumeration_comparison_rvalue"


	public static class component_type_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "component_type"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:94:1: component_type : ( simple_ground_state_variable_component_type | singleton_state_variable_component_type | renewable_resource_component_type | consumable_resource_component_type );
	public final ddl3Parser.component_type_return component_type() throws RecognitionException {
		ddl3Parser.component_type_return retval = new ddl3Parser.component_type_return();
		retval.start = input.LT(1);
		int component_type_StartIndex = input.index();

		Object root_0 = null;

		ParserRuleReturnScope simple_ground_state_variable_component_type137 =null;
		ParserRuleReturnScope singleton_state_variable_component_type138 =null;
		ParserRuleReturnScope renewable_resource_component_type139 =null;
		ParserRuleReturnScope consumable_resource_component_type140 =null;


		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 18) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:95:2: ( simple_ground_state_variable_component_type | singleton_state_variable_component_type | renewable_resource_component_type | consumable_resource_component_type )
			int alt15=4;
			int LA15_0 = input.LA(1);
			if ( (LA15_0==35) ) {
				switch ( input.LA(2) ) {
				case 72:
					{
					alt15=1;
					}
					break;
				case 73:
					{
					alt15=2;
					}
					break;
				case 64:
					{
					alt15=3;
					}
					break;
				case 40:
					{
					alt15=4;
					}
					break;
				default:
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 15, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 15, 0, input);
				throw nvae;
			}

			switch (alt15) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:95:4: simple_ground_state_variable_component_type
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_ground_state_variable_component_type_in_component_type1006);
					simple_ground_state_variable_component_type137=simple_ground_state_variable_component_type();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_ground_state_variable_component_type137.getTree());

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:95:50: singleton_state_variable_component_type
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_singleton_state_variable_component_type_in_component_type1010);
					singleton_state_variable_component_type138=singleton_state_variable_component_type();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, singleton_state_variable_component_type138.getTree());

					}
					break;
				case 3 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:95:92: renewable_resource_component_type
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_renewable_resource_component_type_in_component_type1014);
					renewable_resource_component_type139=renewable_resource_component_type();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, renewable_resource_component_type139.getTree());

					}
					break;
				case 4 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:95:128: consumable_resource_component_type
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_consumable_resource_component_type_in_component_type1018);
					consumable_resource_component_type140=consumable_resource_component_type();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, consumable_resource_component_type140.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 18, component_type_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "component_type"


	public static class simple_ground_state_variable_component_type_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "simple_ground_state_variable_component_type"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:98:1: simple_ground_state_variable_component_type : 'COMP_TYPE' 'SimpleGroundStateVariable' ID '(' simple_ground_state_variable_component_decision_type ( ',' simple_ground_state_variable_component_decision_type )* ')' '{' ( simple_ground_state_variable_transition_constraint )* '}' -> ^( ID ( simple_ground_state_variable_component_decision_type )+ ( simple_ground_state_variable_transition_constraint )* ) ;
	public final ddl3Parser.simple_ground_state_variable_component_type_return simple_ground_state_variable_component_type() throws RecognitionException {
		ddl3Parser.simple_ground_state_variable_component_type_return retval = new ddl3Parser.simple_ground_state_variable_component_type_return();
		retval.start = input.LT(1);
		int simple_ground_state_variable_component_type_StartIndex = input.index();

		Object root_0 = null;

		Token string_literal141=null;
		Token string_literal142=null;
		Token ID143=null;
		Token char_literal144=null;
		Token char_literal146=null;
		Token char_literal148=null;
		Token char_literal149=null;
		Token char_literal151=null;
		ParserRuleReturnScope simple_ground_state_variable_component_decision_type145 =null;
		ParserRuleReturnScope simple_ground_state_variable_component_decision_type147 =null;
		ParserRuleReturnScope simple_ground_state_variable_transition_constraint150 =null;

		Object string_literal141_tree=null;
		Object string_literal142_tree=null;
		Object ID143_tree=null;
		Object char_literal144_tree=null;
		Object char_literal146_tree=null;
		Object char_literal148_tree=null;
		Object char_literal149_tree=null;
		Object char_literal151_tree=null;
		RewriteRuleTokenStream stream_11=new RewriteRuleTokenStream(adaptor,"token 11");
		RewriteRuleTokenStream stream_12=new RewriteRuleTokenStream(adaptor,"token 12");
		RewriteRuleTokenStream stream_35=new RewriteRuleTokenStream(adaptor,"token 35");
		RewriteRuleTokenStream stream_15=new RewriteRuleTokenStream(adaptor,"token 15");
		RewriteRuleTokenStream stream_80=new RewriteRuleTokenStream(adaptor,"token 80");
		RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
		RewriteRuleTokenStream stream_72=new RewriteRuleTokenStream(adaptor,"token 72");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleSubtreeStream stream_simple_ground_state_variable_component_decision_type=new RewriteRuleSubtreeStream(adaptor,"rule simple_ground_state_variable_component_decision_type");
		RewriteRuleSubtreeStream stream_simple_ground_state_variable_transition_constraint=new RewriteRuleSubtreeStream(adaptor,"rule simple_ground_state_variable_transition_constraint");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 19) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:99:2: ( 'COMP_TYPE' 'SimpleGroundStateVariable' ID '(' simple_ground_state_variable_component_decision_type ( ',' simple_ground_state_variable_component_decision_type )* ')' '{' ( simple_ground_state_variable_transition_constraint )* '}' -> ^( ID ( simple_ground_state_variable_component_decision_type )+ ( simple_ground_state_variable_transition_constraint )* ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:99:4: 'COMP_TYPE' 'SimpleGroundStateVariable' ID '(' simple_ground_state_variable_component_decision_type ( ',' simple_ground_state_variable_component_decision_type )* ')' '{' ( simple_ground_state_variable_transition_constraint )* '}'
			{
			string_literal141=(Token)match(input,35,FOLLOW_35_in_simple_ground_state_variable_component_type1028); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_35.add(string_literal141);

			string_literal142=(Token)match(input,72,FOLLOW_72_in_simple_ground_state_variable_component_type1030); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_72.add(string_literal142);

			ID143=(Token)match(input,ID,FOLLOW_ID_in_simple_ground_state_variable_component_type1032); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID143);

			char_literal144=(Token)match(input,11,FOLLOW_11_in_simple_ground_state_variable_component_type1034); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_11.add(char_literal144);

			pushFollow(FOLLOW_simple_ground_state_variable_component_decision_type_in_simple_ground_state_variable_component_type1036);
			simple_ground_state_variable_component_decision_type145=simple_ground_state_variable_component_decision_type();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_ground_state_variable_component_decision_type.add(simple_ground_state_variable_component_decision_type145.getTree());
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:99:104: ( ',' simple_ground_state_variable_component_decision_type )*
			loop16:
			while (true) {
				int alt16=2;
				int LA16_0 = input.LA(1);
				if ( (LA16_0==15) ) {
					alt16=1;
				}

				switch (alt16) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:99:105: ',' simple_ground_state_variable_component_decision_type
					{
					char_literal146=(Token)match(input,15,FOLLOW_15_in_simple_ground_state_variable_component_type1039); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_15.add(char_literal146);

					pushFollow(FOLLOW_simple_ground_state_variable_component_decision_type_in_simple_ground_state_variable_component_type1041);
					simple_ground_state_variable_component_decision_type147=simple_ground_state_variable_component_decision_type();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_simple_ground_state_variable_component_decision_type.add(simple_ground_state_variable_component_decision_type147.getTree());
					}
					break;

				default :
					break loop16;
				}
			}

			char_literal148=(Token)match(input,12,FOLLOW_12_in_simple_ground_state_variable_component_type1045); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_12.add(char_literal148);

			char_literal149=(Token)match(input,80,FOLLOW_80_in_simple_ground_state_variable_component_type1047); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_80.add(char_literal149);

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:99:172: ( simple_ground_state_variable_transition_constraint )*
			loop17:
			while (true) {
				int alt17=2;
				int LA17_0 = input.LA(1);
				if ( (LA17_0==75) ) {
					alt17=1;
				}

				switch (alt17) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:99:173: simple_ground_state_variable_transition_constraint
					{
					pushFollow(FOLLOW_simple_ground_state_variable_transition_constraint_in_simple_ground_state_variable_component_type1050);
					simple_ground_state_variable_transition_constraint150=simple_ground_state_variable_transition_constraint();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_simple_ground_state_variable_transition_constraint.add(simple_ground_state_variable_transition_constraint150.getTree());
					}
					break;

				default :
					break loop17;
				}
			}

			char_literal151=(Token)match(input,81,FOLLOW_81_in_simple_ground_state_variable_component_type1054); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_81.add(char_literal151);

			// AST REWRITE
			// elements: simple_ground_state_variable_component_decision_type, ID, simple_ground_state_variable_transition_constraint
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 99:230: -> ^( ID ( simple_ground_state_variable_component_decision_type )+ ( simple_ground_state_variable_transition_constraint )* )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:99:232: ^( ID ( simple_ground_state_variable_component_decision_type )+ ( simple_ground_state_variable_transition_constraint )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLSimpleGroundStateVariableComponentType(stream_ID.nextToken()), root_1);
				if ( !(stream_simple_ground_state_variable_component_decision_type.hasNext()) ) {
					throw new RewriteEarlyExitException();
				}
				while ( stream_simple_ground_state_variable_component_decision_type.hasNext() ) {
					adaptor.addChild(root_1, stream_simple_ground_state_variable_component_decision_type.nextTree());
				}
				stream_simple_ground_state_variable_component_decision_type.reset();

				// /home/alessandro/opt/antlr/ddl3/ddl3.g:99:336: ( simple_ground_state_variable_transition_constraint )*
				while ( stream_simple_ground_state_variable_transition_constraint.hasNext() ) {
					adaptor.addChild(root_1, stream_simple_ground_state_variable_transition_constraint.nextTree());
				}
				stream_simple_ground_state_variable_transition_constraint.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 19, simple_ground_state_variable_component_type_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "simple_ground_state_variable_component_type"


	public static class simple_ground_state_variable_component_decision_type_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "simple_ground_state_variable_component_decision_type"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:101:1: simple_ground_state_variable_component_decision_type : ID '(' ')' -> ^( ID ) ;
	public final ddl3Parser.simple_ground_state_variable_component_decision_type_return simple_ground_state_variable_component_decision_type() throws RecognitionException {
		ddl3Parser.simple_ground_state_variable_component_decision_type_return retval = new ddl3Parser.simple_ground_state_variable_component_decision_type_return();
		retval.start = input.LT(1);
		int simple_ground_state_variable_component_decision_type_StartIndex = input.index();

		Object root_0 = null;

		Token ID152=null;
		Token char_literal153=null;
		Token char_literal154=null;

		Object ID152_tree=null;
		Object char_literal153_tree=null;
		Object char_literal154_tree=null;
		RewriteRuleTokenStream stream_11=new RewriteRuleTokenStream(adaptor,"token 11");
		RewriteRuleTokenStream stream_12=new RewriteRuleTokenStream(adaptor,"token 12");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 20) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:102:2: ( ID '(' ')' -> ^( ID ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:102:4: ID '(' ')'
			{
			ID152=(Token)match(input,ID,FOLLOW_ID_in_simple_ground_state_variable_component_decision_type1081); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID152);

			char_literal153=(Token)match(input,11,FOLLOW_11_in_simple_ground_state_variable_component_decision_type1083); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_11.add(char_literal153);

			char_literal154=(Token)match(input,12,FOLLOW_12_in_simple_ground_state_variable_component_decision_type1085); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_12.add(char_literal154);

			// AST REWRITE
			// elements: ID
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 102:15: -> ^( ID )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:102:17: ^( ID )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLSimpleGroundStateVariableComponentDecisionType(stream_ID.nextToken()), root_1);
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 20, simple_ground_state_variable_component_decision_type_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "simple_ground_state_variable_component_decision_type"


	public static class simple_ground_state_variable_transition_constraint_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "simple_ground_state_variable_transition_constraint"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:104:1: simple_ground_state_variable_transition_constraint : 'VALUE' simple_ground_state_variable_component_decision range ( 'MEETS' '{' ( simple_ground_state_variable_transition_element ';' )* '}' )? -> ^( 'VALUE' simple_ground_state_variable_component_decision range ( simple_ground_state_variable_transition_element )* ) ;
	public final ddl3Parser.simple_ground_state_variable_transition_constraint_return simple_ground_state_variable_transition_constraint() throws RecognitionException {
		ddl3Parser.simple_ground_state_variable_transition_constraint_return retval = new ddl3Parser.simple_ground_state_variable_transition_constraint_return();
		retval.start = input.LT(1);
		int simple_ground_state_variable_transition_constraint_StartIndex = input.index();

		Object root_0 = null;

		Token string_literal155=null;
		Token string_literal158=null;
		Token char_literal159=null;
		Token char_literal161=null;
		Token char_literal162=null;
		ParserRuleReturnScope simple_ground_state_variable_component_decision156 =null;
		ParserRuleReturnScope range157 =null;
		ParserRuleReturnScope simple_ground_state_variable_transition_element160 =null;

		Object string_literal155_tree=null;
		Object string_literal158_tree=null;
		Object char_literal159_tree=null;
		Object char_literal161_tree=null;
		Object char_literal162_tree=null;
		RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
		RewriteRuleTokenStream stream_80=new RewriteRuleTokenStream(adaptor,"token 80");
		RewriteRuleTokenStream stream_19=new RewriteRuleTokenStream(adaptor,"token 19");
		RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
		RewriteRuleTokenStream stream_75=new RewriteRuleTokenStream(adaptor,"token 75");
		RewriteRuleSubtreeStream stream_simple_ground_state_variable_transition_element=new RewriteRuleSubtreeStream(adaptor,"rule simple_ground_state_variable_transition_element");
		RewriteRuleSubtreeStream stream_range=new RewriteRuleSubtreeStream(adaptor,"rule range");
		RewriteRuleSubtreeStream stream_simple_ground_state_variable_component_decision=new RewriteRuleSubtreeStream(adaptor,"rule simple_ground_state_variable_component_decision");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 21) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:105:2: ( 'VALUE' simple_ground_state_variable_component_decision range ( 'MEETS' '{' ( simple_ground_state_variable_transition_element ';' )* '}' )? -> ^( 'VALUE' simple_ground_state_variable_component_decision range ( simple_ground_state_variable_transition_element )* ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:105:4: 'VALUE' simple_ground_state_variable_component_decision range ( 'MEETS' '{' ( simple_ground_state_variable_transition_element ';' )* '}' )?
			{
			string_literal155=(Token)match(input,75,FOLLOW_75_in_simple_ground_state_variable_transition_constraint1102); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_75.add(string_literal155);

			pushFollow(FOLLOW_simple_ground_state_variable_component_decision_in_simple_ground_state_variable_transition_constraint1104);
			simple_ground_state_variable_component_decision156=simple_ground_state_variable_component_decision();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_ground_state_variable_component_decision.add(simple_ground_state_variable_component_decision156.getTree());
			pushFollow(FOLLOW_range_in_simple_ground_state_variable_transition_constraint1106);
			range157=range();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_range.add(range157.getTree());
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:105:66: ( 'MEETS' '{' ( simple_ground_state_variable_transition_element ';' )* '}' )?
			int alt19=2;
			int LA19_0 = input.LA(1);
			if ( (LA19_0==55) ) {
				alt19=1;
			}
			switch (alt19) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:105:67: 'MEETS' '{' ( simple_ground_state_variable_transition_element ';' )* '}'
					{
					string_literal158=(Token)match(input,55,FOLLOW_55_in_simple_ground_state_variable_transition_constraint1109); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_55.add(string_literal158);

					char_literal159=(Token)match(input,80,FOLLOW_80_in_simple_ground_state_variable_transition_constraint1111); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_80.add(char_literal159);

					// /home/alessandro/opt/antlr/ddl3/ddl3.g:105:79: ( simple_ground_state_variable_transition_element ';' )*
					loop18:
					while (true) {
						int alt18=2;
						int LA18_0 = input.LA(1);
						if ( (LA18_0==ID||LA18_0==20) ) {
							alt18=1;
						}

						switch (alt18) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:105:80: simple_ground_state_variable_transition_element ';'
							{
							pushFollow(FOLLOW_simple_ground_state_variable_transition_element_in_simple_ground_state_variable_transition_constraint1114);
							simple_ground_state_variable_transition_element160=simple_ground_state_variable_transition_element();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_simple_ground_state_variable_transition_element.add(simple_ground_state_variable_transition_element160.getTree());
							char_literal161=(Token)match(input,19,FOLLOW_19_in_simple_ground_state_variable_transition_constraint1116); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_19.add(char_literal161);

							}
							break;

						default :
							break loop18;
						}
					}

					char_literal162=(Token)match(input,81,FOLLOW_81_in_simple_ground_state_variable_transition_constraint1120); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_81.add(char_literal162);

					}
					break;

			}

			// AST REWRITE
			// elements: simple_ground_state_variable_transition_element, 75, simple_ground_state_variable_component_decision, range
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 105:140: -> ^( 'VALUE' simple_ground_state_variable_component_decision range ( simple_ground_state_variable_transition_element )* )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:105:142: ^( 'VALUE' simple_ground_state_variable_component_decision range ( simple_ground_state_variable_transition_element )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLSimpleGroundStateVariableTransitionConstraint(stream_75.nextToken()), root_1);
				adaptor.addChild(root_1, stream_simple_ground_state_variable_component_decision.nextTree());
				adaptor.addChild(root_1, stream_range.nextTree());
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:105:256: ( simple_ground_state_variable_transition_element )*
				while ( stream_simple_ground_state_variable_transition_element.hasNext() ) {
					adaptor.addChild(root_1, stream_simple_ground_state_variable_transition_element.nextTree());
				}
				stream_simple_ground_state_variable_transition_element.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 21, simple_ground_state_variable_transition_constraint_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "simple_ground_state_variable_transition_constraint"


	public static class simple_ground_state_variable_transition_element_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "simple_ground_state_variable_transition_element"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:107:1: simple_ground_state_variable_transition_element : simple_ground_state_variable_component_decision ;
	public final ddl3Parser.simple_ground_state_variable_transition_element_return simple_ground_state_variable_transition_element() throws RecognitionException {
		ddl3Parser.simple_ground_state_variable_transition_element_return retval = new ddl3Parser.simple_ground_state_variable_transition_element_return();
		retval.start = input.LT(1);
		int simple_ground_state_variable_transition_element_StartIndex = input.index();

		Object root_0 = null;

		ParserRuleReturnScope simple_ground_state_variable_component_decision163 =null;


		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 22) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:108:2: ( simple_ground_state_variable_component_decision )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:108:4: simple_ground_state_variable_component_decision
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_simple_ground_state_variable_component_decision_in_simple_ground_state_variable_transition_element1148);
			simple_ground_state_variable_component_decision163=simple_ground_state_variable_component_decision();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_ground_state_variable_component_decision163.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 22, simple_ground_state_variable_transition_element_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "simple_ground_state_variable_transition_element"


	public static class singleton_state_variable_component_type_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "singleton_state_variable_component_type"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:111:1: singleton_state_variable_component_type : 'COMP_TYPE' 'SingletonStateVariable' ID '(' singleton_state_variable_component_decision_type ( ',' singleton_state_variable_component_decision_type )* ')' '{' ( singleton_state_variable_transition_constraint )* '}' -> ^( ID ( singleton_state_variable_component_decision_type )+ ( singleton_state_variable_transition_constraint )* ) ;
	public final ddl3Parser.singleton_state_variable_component_type_return singleton_state_variable_component_type() throws RecognitionException {
		ddl3Parser.singleton_state_variable_component_type_return retval = new ddl3Parser.singleton_state_variable_component_type_return();
		retval.start = input.LT(1);
		int singleton_state_variable_component_type_StartIndex = input.index();

		Object root_0 = null;

		Token string_literal164=null;
		Token string_literal165=null;
		Token ID166=null;
		Token char_literal167=null;
		Token char_literal169=null;
		Token char_literal171=null;
		Token char_literal172=null;
		Token char_literal174=null;
		ParserRuleReturnScope singleton_state_variable_component_decision_type168 =null;
		ParserRuleReturnScope singleton_state_variable_component_decision_type170 =null;
		ParserRuleReturnScope singleton_state_variable_transition_constraint173 =null;

		Object string_literal164_tree=null;
		Object string_literal165_tree=null;
		Object ID166_tree=null;
		Object char_literal167_tree=null;
		Object char_literal169_tree=null;
		Object char_literal171_tree=null;
		Object char_literal172_tree=null;
		Object char_literal174_tree=null;
		RewriteRuleTokenStream stream_11=new RewriteRuleTokenStream(adaptor,"token 11");
		RewriteRuleTokenStream stream_12=new RewriteRuleTokenStream(adaptor,"token 12");
		RewriteRuleTokenStream stream_35=new RewriteRuleTokenStream(adaptor,"token 35");
		RewriteRuleTokenStream stream_15=new RewriteRuleTokenStream(adaptor,"token 15");
		RewriteRuleTokenStream stream_80=new RewriteRuleTokenStream(adaptor,"token 80");
		RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_73=new RewriteRuleTokenStream(adaptor,"token 73");
		RewriteRuleSubtreeStream stream_singleton_state_variable_component_decision_type=new RewriteRuleSubtreeStream(adaptor,"rule singleton_state_variable_component_decision_type");
		RewriteRuleSubtreeStream stream_singleton_state_variable_transition_constraint=new RewriteRuleSubtreeStream(adaptor,"rule singleton_state_variable_transition_constraint");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 23) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:112:2: ( 'COMP_TYPE' 'SingletonStateVariable' ID '(' singleton_state_variable_component_decision_type ( ',' singleton_state_variable_component_decision_type )* ')' '{' ( singleton_state_variable_transition_constraint )* '}' -> ^( ID ( singleton_state_variable_component_decision_type )+ ( singleton_state_variable_transition_constraint )* ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:112:4: 'COMP_TYPE' 'SingletonStateVariable' ID '(' singleton_state_variable_component_decision_type ( ',' singleton_state_variable_component_decision_type )* ')' '{' ( singleton_state_variable_transition_constraint )* '}'
			{
			string_literal164=(Token)match(input,35,FOLLOW_35_in_singleton_state_variable_component_type1158); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_35.add(string_literal164);

			string_literal165=(Token)match(input,73,FOLLOW_73_in_singleton_state_variable_component_type1160); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_73.add(string_literal165);

			ID166=(Token)match(input,ID,FOLLOW_ID_in_singleton_state_variable_component_type1162); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID166);

			char_literal167=(Token)match(input,11,FOLLOW_11_in_singleton_state_variable_component_type1164); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_11.add(char_literal167);

			pushFollow(FOLLOW_singleton_state_variable_component_decision_type_in_singleton_state_variable_component_type1166);
			singleton_state_variable_component_decision_type168=singleton_state_variable_component_decision_type();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_singleton_state_variable_component_decision_type.add(singleton_state_variable_component_decision_type168.getTree());
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:112:97: ( ',' singleton_state_variable_component_decision_type )*
			loop20:
			while (true) {
				int alt20=2;
				int LA20_0 = input.LA(1);
				if ( (LA20_0==15) ) {
					alt20=1;
				}

				switch (alt20) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:112:98: ',' singleton_state_variable_component_decision_type
					{
					char_literal169=(Token)match(input,15,FOLLOW_15_in_singleton_state_variable_component_type1169); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_15.add(char_literal169);

					pushFollow(FOLLOW_singleton_state_variable_component_decision_type_in_singleton_state_variable_component_type1171);
					singleton_state_variable_component_decision_type170=singleton_state_variable_component_decision_type();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_singleton_state_variable_component_decision_type.add(singleton_state_variable_component_decision_type170.getTree());
					}
					break;

				default :
					break loop20;
				}
			}

			char_literal171=(Token)match(input,12,FOLLOW_12_in_singleton_state_variable_component_type1175); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_12.add(char_literal171);

			char_literal172=(Token)match(input,80,FOLLOW_80_in_singleton_state_variable_component_type1177); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_80.add(char_literal172);

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:112:161: ( singleton_state_variable_transition_constraint )*
			loop21:
			while (true) {
				int alt21=2;
				int LA21_0 = input.LA(1);
				if ( (LA21_0==75) ) {
					alt21=1;
				}

				switch (alt21) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:112:162: singleton_state_variable_transition_constraint
					{
					pushFollow(FOLLOW_singleton_state_variable_transition_constraint_in_singleton_state_variable_component_type1180);
					singleton_state_variable_transition_constraint173=singleton_state_variable_transition_constraint();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_singleton_state_variable_transition_constraint.add(singleton_state_variable_transition_constraint173.getTree());
					}
					break;

				default :
					break loop21;
				}
			}

			char_literal174=(Token)match(input,81,FOLLOW_81_in_singleton_state_variable_component_type1184); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_81.add(char_literal174);

			// AST REWRITE
			// elements: ID, singleton_state_variable_component_decision_type, singleton_state_variable_transition_constraint
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 112:215: -> ^( ID ( singleton_state_variable_component_decision_type )+ ( singleton_state_variable_transition_constraint )* )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:112:217: ^( ID ( singleton_state_variable_component_decision_type )+ ( singleton_state_variable_transition_constraint )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLSingletonStateVariableComponentType(stream_ID.nextToken()), root_1);
				if ( !(stream_singleton_state_variable_component_decision_type.hasNext()) ) {
					throw new RewriteEarlyExitException();
				}
				while ( stream_singleton_state_variable_component_decision_type.hasNext() ) {
					adaptor.addChild(root_1, stream_singleton_state_variable_component_decision_type.nextTree());
				}
				stream_singleton_state_variable_component_decision_type.reset();

				// /home/alessandro/opt/antlr/ddl3/ddl3.g:112:314: ( singleton_state_variable_transition_constraint )*
				while ( stream_singleton_state_variable_transition_constraint.hasNext() ) {
					adaptor.addChild(root_1, stream_singleton_state_variable_transition_constraint.nextTree());
				}
				stream_singleton_state_variable_transition_constraint.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 23, singleton_state_variable_component_type_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "singleton_state_variable_component_type"


	public static class singleton_state_variable_component_decision_type_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "singleton_state_variable_component_decision_type"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:114:1: singleton_state_variable_component_decision_type : ID '(' ( ID ( ',' ID )* )? ')' -> ^( ID ( ID )* ) ;
	public final ddl3Parser.singleton_state_variable_component_decision_type_return singleton_state_variable_component_decision_type() throws RecognitionException {
		ddl3Parser.singleton_state_variable_component_decision_type_return retval = new ddl3Parser.singleton_state_variable_component_decision_type_return();
		retval.start = input.LT(1);
		int singleton_state_variable_component_decision_type_StartIndex = input.index();

		Object root_0 = null;

		Token ID175=null;
		Token char_literal176=null;
		Token ID177=null;
		Token char_literal178=null;
		Token ID179=null;
		Token char_literal180=null;

		Object ID175_tree=null;
		Object char_literal176_tree=null;
		Object ID177_tree=null;
		Object char_literal178_tree=null;
		Object ID179_tree=null;
		Object char_literal180_tree=null;
		RewriteRuleTokenStream stream_11=new RewriteRuleTokenStream(adaptor,"token 11");
		RewriteRuleTokenStream stream_12=new RewriteRuleTokenStream(adaptor,"token 12");
		RewriteRuleTokenStream stream_15=new RewriteRuleTokenStream(adaptor,"token 15");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 24) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:115:2: ( ID '(' ( ID ( ',' ID )* )? ')' -> ^( ID ( ID )* ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:115:4: ID '(' ( ID ( ',' ID )* )? ')'
			{
			ID175=(Token)match(input,ID,FOLLOW_ID_in_singleton_state_variable_component_decision_type1211); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID175);

			char_literal176=(Token)match(input,11,FOLLOW_11_in_singleton_state_variable_component_decision_type1213); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_11.add(char_literal176);

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:115:11: ( ID ( ',' ID )* )?
			int alt23=2;
			int LA23_0 = input.LA(1);
			if ( (LA23_0==ID) ) {
				alt23=1;
			}
			switch (alt23) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:115:12: ID ( ',' ID )*
					{
					ID177=(Token)match(input,ID,FOLLOW_ID_in_singleton_state_variable_component_decision_type1216); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(ID177);

					// /home/alessandro/opt/antlr/ddl3/ddl3.g:115:15: ( ',' ID )*
					loop22:
					while (true) {
						int alt22=2;
						int LA22_0 = input.LA(1);
						if ( (LA22_0==15) ) {
							alt22=1;
						}

						switch (alt22) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:115:16: ',' ID
							{
							char_literal178=(Token)match(input,15,FOLLOW_15_in_singleton_state_variable_component_decision_type1219); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_15.add(char_literal178);

							ID179=(Token)match(input,ID,FOLLOW_ID_in_singleton_state_variable_component_decision_type1221); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_ID.add(ID179);

							}
							break;

						default :
							break loop22;
						}
					}

					}
					break;

			}

			char_literal180=(Token)match(input,12,FOLLOW_12_in_singleton_state_variable_component_decision_type1227); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_12.add(char_literal180);

			// AST REWRITE
			// elements: ID, ID
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 115:31: -> ^( ID ( ID )* )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:115:33: ^( ID ( ID )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLSingletonStateVariableComponentDecisionType(stream_ID.nextToken()), root_1);
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:115:86: ( ID )*
				while ( stream_ID.hasNext() ) {
					adaptor.addChild(root_1, stream_ID.nextNode());
				}
				stream_ID.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 24, singleton_state_variable_component_decision_type_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "singleton_state_variable_component_decision_type"


	public static class singleton_state_variable_transition_constraint_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "singleton_state_variable_transition_constraint"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:117:1: singleton_state_variable_transition_constraint : 'VALUE' singleton_state_variable_component_decision range 'MEETS' '{' ( singleton_state_variable_transition_element ';' )* '}' -> ^( 'VALUE' singleton_state_variable_component_decision range ( singleton_state_variable_transition_element )* ) ;
	public final ddl3Parser.singleton_state_variable_transition_constraint_return singleton_state_variable_transition_constraint() throws RecognitionException {
		ddl3Parser.singleton_state_variable_transition_constraint_return retval = new ddl3Parser.singleton_state_variable_transition_constraint_return();
		retval.start = input.LT(1);
		int singleton_state_variable_transition_constraint_StartIndex = input.index();

		Object root_0 = null;

		Token string_literal181=null;
		Token string_literal184=null;
		Token char_literal185=null;
		Token char_literal187=null;
		Token char_literal188=null;
		ParserRuleReturnScope singleton_state_variable_component_decision182 =null;
		ParserRuleReturnScope range183 =null;
		ParserRuleReturnScope singleton_state_variable_transition_element186 =null;

		Object string_literal181_tree=null;
		Object string_literal184_tree=null;
		Object char_literal185_tree=null;
		Object char_literal187_tree=null;
		Object char_literal188_tree=null;
		RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
		RewriteRuleTokenStream stream_80=new RewriteRuleTokenStream(adaptor,"token 80");
		RewriteRuleTokenStream stream_19=new RewriteRuleTokenStream(adaptor,"token 19");
		RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
		RewriteRuleTokenStream stream_75=new RewriteRuleTokenStream(adaptor,"token 75");
		RewriteRuleSubtreeStream stream_singleton_state_variable_component_decision=new RewriteRuleSubtreeStream(adaptor,"rule singleton_state_variable_component_decision");
		RewriteRuleSubtreeStream stream_singleton_state_variable_transition_element=new RewriteRuleSubtreeStream(adaptor,"rule singleton_state_variable_transition_element");
		RewriteRuleSubtreeStream stream_range=new RewriteRuleSubtreeStream(adaptor,"rule range");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 25) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:118:2: ( 'VALUE' singleton_state_variable_component_decision range 'MEETS' '{' ( singleton_state_variable_transition_element ';' )* '}' -> ^( 'VALUE' singleton_state_variable_component_decision range ( singleton_state_variable_transition_element )* ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:118:4: 'VALUE' singleton_state_variable_component_decision range 'MEETS' '{' ( singleton_state_variable_transition_element ';' )* '}'
			{
			string_literal181=(Token)match(input,75,FOLLOW_75_in_singleton_state_variable_transition_constraint1247); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_75.add(string_literal181);

			pushFollow(FOLLOW_singleton_state_variable_component_decision_in_singleton_state_variable_transition_constraint1249);
			singleton_state_variable_component_decision182=singleton_state_variable_component_decision();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_singleton_state_variable_component_decision.add(singleton_state_variable_component_decision182.getTree());
			pushFollow(FOLLOW_range_in_singleton_state_variable_transition_constraint1251);
			range183=range();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_range.add(range183.getTree());
			string_literal184=(Token)match(input,55,FOLLOW_55_in_singleton_state_variable_transition_constraint1253); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_55.add(string_literal184);

			char_literal185=(Token)match(input,80,FOLLOW_80_in_singleton_state_variable_transition_constraint1255); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_80.add(char_literal185);

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:118:74: ( singleton_state_variable_transition_element ';' )*
			loop24:
			while (true) {
				int alt24=2;
				int LA24_0 = input.LA(1);
				if ( ((LA24_0 >= ID && LA24_0 <= VarID)||LA24_0==14||LA24_0==16||LA24_0==20||LA24_0==54) ) {
					alt24=1;
				}

				switch (alt24) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:118:75: singleton_state_variable_transition_element ';'
					{
					pushFollow(FOLLOW_singleton_state_variable_transition_element_in_singleton_state_variable_transition_constraint1258);
					singleton_state_variable_transition_element186=singleton_state_variable_transition_element();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_singleton_state_variable_transition_element.add(singleton_state_variable_transition_element186.getTree());
					char_literal187=(Token)match(input,19,FOLLOW_19_in_singleton_state_variable_transition_constraint1260); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_19.add(char_literal187);

					}
					break;

				default :
					break loop24;
				}
			}

			char_literal188=(Token)match(input,81,FOLLOW_81_in_singleton_state_variable_transition_constraint1264); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_81.add(char_literal188);

			// AST REWRITE
			// elements: singleton_state_variable_component_decision, range, 75, singleton_state_variable_transition_element
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 118:129: -> ^( 'VALUE' singleton_state_variable_component_decision range ( singleton_state_variable_transition_element )* )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:118:131: ^( 'VALUE' singleton_state_variable_component_decision range ( singleton_state_variable_transition_element )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLSingletonStateVariableTransitionConstraint(stream_75.nextToken()), root_1);
				adaptor.addChild(root_1, stream_singleton_state_variable_component_decision.nextTree());
				adaptor.addChild(root_1, stream_range.nextTree());
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:118:238: ( singleton_state_variable_transition_element )*
				while ( stream_singleton_state_variable_transition_element.hasNext() ) {
					adaptor.addChild(root_1, stream_singleton_state_variable_transition_element.nextTree());
				}
				stream_singleton_state_variable_transition_element.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 25, singleton_state_variable_transition_constraint_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "singleton_state_variable_transition_constraint"


	public static class singleton_state_variable_transition_element_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "singleton_state_variable_transition_element"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:120:1: singleton_state_variable_transition_element : ( singleton_state_variable_component_decision | parameter_constraint );
	public final ddl3Parser.singleton_state_variable_transition_element_return singleton_state_variable_transition_element() throws RecognitionException {
		ddl3Parser.singleton_state_variable_transition_element_return retval = new ddl3Parser.singleton_state_variable_transition_element_return();
		retval.start = input.LT(1);
		int singleton_state_variable_transition_element_StartIndex = input.index();

		Object root_0 = null;

		ParserRuleReturnScope singleton_state_variable_component_decision189 =null;
		ParserRuleReturnScope parameter_constraint190 =null;


		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 26) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:121:2: ( singleton_state_variable_component_decision | parameter_constraint )
			int alt25=2;
			int LA25_0 = input.LA(1);
			if ( (LA25_0==ID||LA25_0==20) ) {
				alt25=1;
			}
			else if ( ((LA25_0 >= INT && LA25_0 <= VarID)||LA25_0==14||LA25_0==16||LA25_0==54) ) {
				alt25=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 25, 0, input);
				throw nvae;
			}

			switch (alt25) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:121:4: singleton_state_variable_component_decision
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_singleton_state_variable_component_decision_in_singleton_state_variable_transition_element1290);
					singleton_state_variable_component_decision189=singleton_state_variable_component_decision();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, singleton_state_variable_component_decision189.getTree());

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:121:50: parameter_constraint
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_parameter_constraint_in_singleton_state_variable_transition_element1294);
					parameter_constraint190=parameter_constraint();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, parameter_constraint190.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 26, singleton_state_variable_transition_element_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "singleton_state_variable_transition_element"


	public static class renewable_resource_component_type_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "renewable_resource_component_type"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:124:1: renewable_resource_component_type : 'COMP_TYPE' 'RenewableResource' ID '(' positive_number ')' -> ^( ID positive_number ) ;
	public final ddl3Parser.renewable_resource_component_type_return renewable_resource_component_type() throws RecognitionException {
		ddl3Parser.renewable_resource_component_type_return retval = new ddl3Parser.renewable_resource_component_type_return();
		retval.start = input.LT(1);
		int renewable_resource_component_type_StartIndex = input.index();

		Object root_0 = null;

		Token string_literal191=null;
		Token string_literal192=null;
		Token ID193=null;
		Token char_literal194=null;
		Token char_literal196=null;
		ParserRuleReturnScope positive_number195 =null;

		Object string_literal191_tree=null;
		Object string_literal192_tree=null;
		Object ID193_tree=null;
		Object char_literal194_tree=null;
		Object char_literal196_tree=null;
		RewriteRuleTokenStream stream_11=new RewriteRuleTokenStream(adaptor,"token 11");
		RewriteRuleTokenStream stream_12=new RewriteRuleTokenStream(adaptor,"token 12");
		RewriteRuleTokenStream stream_35=new RewriteRuleTokenStream(adaptor,"token 35");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_64=new RewriteRuleTokenStream(adaptor,"token 64");
		RewriteRuleSubtreeStream stream_positive_number=new RewriteRuleSubtreeStream(adaptor,"rule positive_number");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 27) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:125:2: ( 'COMP_TYPE' 'RenewableResource' ID '(' positive_number ')' -> ^( ID positive_number ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:125:4: 'COMP_TYPE' 'RenewableResource' ID '(' positive_number ')'
			{
			string_literal191=(Token)match(input,35,FOLLOW_35_in_renewable_resource_component_type1304); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_35.add(string_literal191);

			string_literal192=(Token)match(input,64,FOLLOW_64_in_renewable_resource_component_type1306); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_64.add(string_literal192);

			ID193=(Token)match(input,ID,FOLLOW_ID_in_renewable_resource_component_type1308); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID193);

			char_literal194=(Token)match(input,11,FOLLOW_11_in_renewable_resource_component_type1310); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_11.add(char_literal194);

			pushFollow(FOLLOW_positive_number_in_renewable_resource_component_type1312);
			positive_number195=positive_number();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_positive_number.add(positive_number195.getTree());
			char_literal196=(Token)match(input,12,FOLLOW_12_in_renewable_resource_component_type1314); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_12.add(char_literal196);

			// AST REWRITE
			// elements: positive_number, ID
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 125:63: -> ^( ID positive_number )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:125:65: ^( ID positive_number )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLRenewableResourceComponentType(stream_ID.nextToken()), root_1);
				adaptor.addChild(root_1, stream_positive_number.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 27, renewable_resource_component_type_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "renewable_resource_component_type"


	public static class consumable_resource_component_type_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "consumable_resource_component_type"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:128:1: consumable_resource_component_type : 'COMP_TYPE' 'ConsumableResource' ID '(' positive_number ',' positive_number ',' positive_number ')' -> ^( ID positive_number positive_number positive_number ) ;
	public final ddl3Parser.consumable_resource_component_type_return consumable_resource_component_type() throws RecognitionException {
		ddl3Parser.consumable_resource_component_type_return retval = new ddl3Parser.consumable_resource_component_type_return();
		retval.start = input.LT(1);
		int consumable_resource_component_type_StartIndex = input.index();

		Object root_0 = null;

		Token string_literal197=null;
		Token string_literal198=null;
		Token ID199=null;
		Token char_literal200=null;
		Token char_literal202=null;
		Token char_literal204=null;
		Token char_literal206=null;
		ParserRuleReturnScope positive_number201 =null;
		ParserRuleReturnScope positive_number203 =null;
		ParserRuleReturnScope positive_number205 =null;

		Object string_literal197_tree=null;
		Object string_literal198_tree=null;
		Object ID199_tree=null;
		Object char_literal200_tree=null;
		Object char_literal202_tree=null;
		Object char_literal204_tree=null;
		Object char_literal206_tree=null;
		RewriteRuleTokenStream stream_11=new RewriteRuleTokenStream(adaptor,"token 11");
		RewriteRuleTokenStream stream_12=new RewriteRuleTokenStream(adaptor,"token 12");
		RewriteRuleTokenStream stream_35=new RewriteRuleTokenStream(adaptor,"token 35");
		RewriteRuleTokenStream stream_15=new RewriteRuleTokenStream(adaptor,"token 15");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_40=new RewriteRuleTokenStream(adaptor,"token 40");
		RewriteRuleSubtreeStream stream_positive_number=new RewriteRuleSubtreeStream(adaptor,"rule positive_number");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 28) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:129:2: ( 'COMP_TYPE' 'ConsumableResource' ID '(' positive_number ',' positive_number ',' positive_number ')' -> ^( ID positive_number positive_number positive_number ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:129:4: 'COMP_TYPE' 'ConsumableResource' ID '(' positive_number ',' positive_number ',' positive_number ')'
			{
			string_literal197=(Token)match(input,35,FOLLOW_35_in_consumable_resource_component_type1334); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_35.add(string_literal197);

			string_literal198=(Token)match(input,40,FOLLOW_40_in_consumable_resource_component_type1336); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_40.add(string_literal198);

			ID199=(Token)match(input,ID,FOLLOW_ID_in_consumable_resource_component_type1338); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID199);

			char_literal200=(Token)match(input,11,FOLLOW_11_in_consumable_resource_component_type1340); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_11.add(char_literal200);

			pushFollow(FOLLOW_positive_number_in_consumable_resource_component_type1342);
			positive_number201=positive_number();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_positive_number.add(positive_number201.getTree());
			char_literal202=(Token)match(input,15,FOLLOW_15_in_consumable_resource_component_type1344); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_15.add(char_literal202);

			pushFollow(FOLLOW_positive_number_in_consumable_resource_component_type1346);
			positive_number203=positive_number();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_positive_number.add(positive_number203.getTree());
			char_literal204=(Token)match(input,15,FOLLOW_15_in_consumable_resource_component_type1348); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_15.add(char_literal204);

			pushFollow(FOLLOW_positive_number_in_consumable_resource_component_type1350);
			positive_number205=positive_number();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_positive_number.add(positive_number205.getTree());
			char_literal206=(Token)match(input,12,FOLLOW_12_in_consumable_resource_component_type1352); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_12.add(char_literal206);

			// AST REWRITE
			// elements: ID, positive_number, positive_number, positive_number
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 129:104: -> ^( ID positive_number positive_number positive_number )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:129:106: ^( ID positive_number positive_number positive_number )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLConsumableResourceComponentType(stream_ID.nextToken()), root_1);
				adaptor.addChild(root_1, stream_positive_number.nextTree());
				adaptor.addChild(root_1, stream_positive_number.nextTree());
				adaptor.addChild(root_1, stream_positive_number.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 28, consumable_resource_component_type_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "consumable_resource_component_type"


	public static class component_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "component"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:132:1: component : 'COMPONENT' ID '{' ( timeline )* '}' ':' ID ';' -> ^( ID ID ( timeline )* ) ;
	public final ddl3Parser.component_return component() throws RecognitionException {
		ddl3Parser.component_return retval = new ddl3Parser.component_return();
		retval.start = input.LT(1);
		int component_StartIndex = input.index();

		Object root_0 = null;

		Token string_literal207=null;
		Token ID208=null;
		Token char_literal209=null;
		Token char_literal211=null;
		Token char_literal212=null;
		Token ID213=null;
		Token char_literal214=null;
		ParserRuleReturnScope timeline210 =null;

		Object string_literal207_tree=null;
		Object ID208_tree=null;
		Object char_literal209_tree=null;
		Object char_literal211_tree=null;
		Object char_literal212_tree=null;
		Object ID213_tree=null;
		Object char_literal214_tree=null;
		RewriteRuleTokenStream stream_34=new RewriteRuleTokenStream(adaptor,"token 34");
		RewriteRuleTokenStream stream_18=new RewriteRuleTokenStream(adaptor,"token 18");
		RewriteRuleTokenStream stream_80=new RewriteRuleTokenStream(adaptor,"token 80");
		RewriteRuleTokenStream stream_19=new RewriteRuleTokenStream(adaptor,"token 19");
		RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleSubtreeStream stream_timeline=new RewriteRuleSubtreeStream(adaptor,"rule timeline");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 29) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:133:2: ( 'COMPONENT' ID '{' ( timeline )* '}' ':' ID ';' -> ^( ID ID ( timeline )* ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:133:4: 'COMPONENT' ID '{' ( timeline )* '}' ':' ID ';'
			{
			string_literal207=(Token)match(input,34,FOLLOW_34_in_component1376); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_34.add(string_literal207);

			ID208=(Token)match(input,ID,FOLLOW_ID_in_component1378); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID208);

			char_literal209=(Token)match(input,80,FOLLOW_80_in_component1380); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_80.add(char_literal209);

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:133:23: ( timeline )*
			loop26:
			while (true) {
				int alt26=2;
				int LA26_0 = input.LA(1);
				if ( (LA26_0==33||(LA26_0 >= 48 && LA26_0 <= 49)||LA26_0==53) ) {
					alt26=1;
				}

				switch (alt26) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:133:24: timeline
					{
					pushFollow(FOLLOW_timeline_in_component1383);
					timeline210=timeline();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_timeline.add(timeline210.getTree());
					}
					break;

				default :
					break loop26;
				}
			}

			char_literal211=(Token)match(input,81,FOLLOW_81_in_component1387); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_81.add(char_literal211);

			char_literal212=(Token)match(input,18,FOLLOW_18_in_component1389); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_18.add(char_literal212);

			ID213=(Token)match(input,ID,FOLLOW_ID_in_component1391); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID213);

			char_literal214=(Token)match(input,19,FOLLOW_19_in_component1393); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_19.add(char_literal214);

			// AST REWRITE
			// elements: ID, timeline, ID
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 133:49: -> ^( ID ID ( timeline )* )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:133:51: ^( ID ID ( timeline )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLComponent(stream_ID.nextToken()), root_1);
				adaptor.addChild(root_1, stream_ID.nextNode());
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:133:73: ( timeline )*
				while ( stream_timeline.hasNext() ) {
					adaptor.addChild(root_1, stream_timeline.nextTree());
				}
				stream_timeline.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 29, component_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "component"


	public static class timeline_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "timeline"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:135:1: timeline : ( 'ESTA_LIGHT' ID '(' ( parameter ( ',' parameter )* )? ')' -> ^( 'ESTA_LIGHT' ID ( parameter )* ) | 'BOUNDED' ID '(' ( parameter ( ',' parameter )* )? ')' -> ^( 'BOUNDED' ID ( parameter )* ) | 'FLEXIBLE' ID '(' ( parameter ( ',' parameter )* )? ')' -> ^( 'FLEXIBLE' ID ( parameter )* ) | 'ESTA_LIGHT_MAX_CONSUMPTION' ID '(' ( parameter ( ',' parameter )* )? ')' -> ^( 'ESTA_LIGHT_MAX_CONSUMPTION' ID ( parameter )* ) );
	public final ddl3Parser.timeline_return timeline() throws RecognitionException {
		ddl3Parser.timeline_return retval = new ddl3Parser.timeline_return();
		retval.start = input.LT(1);
		int timeline_StartIndex = input.index();

		Object root_0 = null;

		Token string_literal215=null;
		Token ID216=null;
		Token char_literal217=null;
		Token char_literal219=null;
		Token char_literal221=null;
		Token string_literal222=null;
		Token ID223=null;
		Token char_literal224=null;
		Token char_literal226=null;
		Token char_literal228=null;
		Token string_literal229=null;
		Token ID230=null;
		Token char_literal231=null;
		Token char_literal233=null;
		Token char_literal235=null;
		Token string_literal236=null;
		Token ID237=null;
		Token char_literal238=null;
		Token char_literal240=null;
		Token char_literal242=null;
		ParserRuleReturnScope parameter218 =null;
		ParserRuleReturnScope parameter220 =null;
		ParserRuleReturnScope parameter225 =null;
		ParserRuleReturnScope parameter227 =null;
		ParserRuleReturnScope parameter232 =null;
		ParserRuleReturnScope parameter234 =null;
		ParserRuleReturnScope parameter239 =null;
		ParserRuleReturnScope parameter241 =null;

		Object string_literal215_tree=null;
		Object ID216_tree=null;
		Object char_literal217_tree=null;
		Object char_literal219_tree=null;
		Object char_literal221_tree=null;
		Object string_literal222_tree=null;
		Object ID223_tree=null;
		Object char_literal224_tree=null;
		Object char_literal226_tree=null;
		Object char_literal228_tree=null;
		Object string_literal229_tree=null;
		Object ID230_tree=null;
		Object char_literal231_tree=null;
		Object char_literal233_tree=null;
		Object char_literal235_tree=null;
		Object string_literal236_tree=null;
		Object ID237_tree=null;
		Object char_literal238_tree=null;
		Object char_literal240_tree=null;
		Object char_literal242_tree=null;
		RewriteRuleTokenStream stream_11=new RewriteRuleTokenStream(adaptor,"token 11");
		RewriteRuleTokenStream stream_33=new RewriteRuleTokenStream(adaptor,"token 33");
		RewriteRuleTokenStream stream_12=new RewriteRuleTokenStream(adaptor,"token 12");
		RewriteRuleTokenStream stream_15=new RewriteRuleTokenStream(adaptor,"token 15");
		RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
		RewriteRuleTokenStream stream_49=new RewriteRuleTokenStream(adaptor,"token 49");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_53=new RewriteRuleTokenStream(adaptor,"token 53");
		RewriteRuleSubtreeStream stream_parameter=new RewriteRuleSubtreeStream(adaptor,"rule parameter");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 30) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:2: ( 'ESTA_LIGHT' ID '(' ( parameter ( ',' parameter )* )? ')' -> ^( 'ESTA_LIGHT' ID ( parameter )* ) | 'BOUNDED' ID '(' ( parameter ( ',' parameter )* )? ')' -> ^( 'BOUNDED' ID ( parameter )* ) | 'FLEXIBLE' ID '(' ( parameter ( ',' parameter )* )? ')' -> ^( 'FLEXIBLE' ID ( parameter )* ) | 'ESTA_LIGHT_MAX_CONSUMPTION' ID '(' ( parameter ( ',' parameter )* )? ')' -> ^( 'ESTA_LIGHT_MAX_CONSUMPTION' ID ( parameter )* ) )
			int alt35=4;
			switch ( input.LA(1) ) {
			case 48:
				{
				alt35=1;
				}
				break;
			case 33:
				{
				alt35=2;
				}
				break;
			case 53:
				{
				alt35=3;
				}
				break;
			case 49:
				{
				alt35=4;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 35, 0, input);
				throw nvae;
			}
			switch (alt35) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:4: 'ESTA_LIGHT' ID '(' ( parameter ( ',' parameter )* )? ')'
					{
					string_literal215=(Token)match(input,48,FOLLOW_48_in_timeline1416); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_48.add(string_literal215);

					ID216=(Token)match(input,ID,FOLLOW_ID_in_timeline1418); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(ID216);

					char_literal217=(Token)match(input,11,FOLLOW_11_in_timeline1420); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_11.add(char_literal217);

					// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:24: ( parameter ( ',' parameter )* )?
					int alt28=2;
					int LA28_0 = input.LA(1);
					if ( (LA28_0==ID||LA28_0==9||LA28_0==25||(LA28_0 >= 78 && LA28_0 <= 79)) ) {
						alt28=1;
					}
					switch (alt28) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:25: parameter ( ',' parameter )*
							{
							pushFollow(FOLLOW_parameter_in_timeline1423);
							parameter218=parameter();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_parameter.add(parameter218.getTree());
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:35: ( ',' parameter )*
							loop27:
							while (true) {
								int alt27=2;
								int LA27_0 = input.LA(1);
								if ( (LA27_0==15) ) {
									alt27=1;
								}

								switch (alt27) {
								case 1 :
									// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:36: ',' parameter
									{
									char_literal219=(Token)match(input,15,FOLLOW_15_in_timeline1426); if (state.failed) return retval; 
									if ( state.backtracking==0 ) stream_15.add(char_literal219);

									pushFollow(FOLLOW_parameter_in_timeline1428);
									parameter220=parameter();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) stream_parameter.add(parameter220.getTree());
									}
									break;

								default :
									break loop27;
								}
							}

							}
							break;

					}

					char_literal221=(Token)match(input,12,FOLLOW_12_in_timeline1434); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_12.add(char_literal221);

					// AST REWRITE
					// elements: parameter, 48, ID
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 136:58: -> ^( 'ESTA_LIGHT' ID ( parameter )* )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:60: ^( 'ESTA_LIGHT' ID ( parameter )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTimeline(stream_48.nextToken()), root_1);
						adaptor.addChild(root_1, stream_ID.nextNode());
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:91: ( parameter )*
						while ( stream_parameter.hasNext() ) {
							adaptor.addChild(root_1, stream_parameter.nextTree());
						}
						stream_parameter.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:105: 'BOUNDED' ID '(' ( parameter ( ',' parameter )* )? ')'
					{
					string_literal222=(Token)match(input,33,FOLLOW_33_in_timeline1451); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_33.add(string_literal222);

					ID223=(Token)match(input,ID,FOLLOW_ID_in_timeline1453); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(ID223);

					char_literal224=(Token)match(input,11,FOLLOW_11_in_timeline1455); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_11.add(char_literal224);

					// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:122: ( parameter ( ',' parameter )* )?
					int alt30=2;
					int LA30_0 = input.LA(1);
					if ( (LA30_0==ID||LA30_0==9||LA30_0==25||(LA30_0 >= 78 && LA30_0 <= 79)) ) {
						alt30=1;
					}
					switch (alt30) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:123: parameter ( ',' parameter )*
							{
							pushFollow(FOLLOW_parameter_in_timeline1458);
							parameter225=parameter();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_parameter.add(parameter225.getTree());
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:133: ( ',' parameter )*
							loop29:
							while (true) {
								int alt29=2;
								int LA29_0 = input.LA(1);
								if ( (LA29_0==15) ) {
									alt29=1;
								}

								switch (alt29) {
								case 1 :
									// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:134: ',' parameter
									{
									char_literal226=(Token)match(input,15,FOLLOW_15_in_timeline1461); if (state.failed) return retval; 
									if ( state.backtracking==0 ) stream_15.add(char_literal226);

									pushFollow(FOLLOW_parameter_in_timeline1463);
									parameter227=parameter();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) stream_parameter.add(parameter227.getTree());
									}
									break;

								default :
									break loop29;
								}
							}

							}
							break;

					}

					char_literal228=(Token)match(input,12,FOLLOW_12_in_timeline1469); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_12.add(char_literal228);

					// AST REWRITE
					// elements: 33, parameter, ID
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 136:156: -> ^( 'BOUNDED' ID ( parameter )* )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:158: ^( 'BOUNDED' ID ( parameter )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTimeline(stream_33.nextToken()), root_1);
						adaptor.addChild(root_1, stream_ID.nextNode());
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:186: ( parameter )*
						while ( stream_parameter.hasNext() ) {
							adaptor.addChild(root_1, stream_parameter.nextTree());
						}
						stream_parameter.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:200: 'FLEXIBLE' ID '(' ( parameter ( ',' parameter )* )? ')'
					{
					string_literal229=(Token)match(input,53,FOLLOW_53_in_timeline1486); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_53.add(string_literal229);

					ID230=(Token)match(input,ID,FOLLOW_ID_in_timeline1488); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(ID230);

					char_literal231=(Token)match(input,11,FOLLOW_11_in_timeline1490); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_11.add(char_literal231);

					// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:218: ( parameter ( ',' parameter )* )?
					int alt32=2;
					int LA32_0 = input.LA(1);
					if ( (LA32_0==ID||LA32_0==9||LA32_0==25||(LA32_0 >= 78 && LA32_0 <= 79)) ) {
						alt32=1;
					}
					switch (alt32) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:219: parameter ( ',' parameter )*
							{
							pushFollow(FOLLOW_parameter_in_timeline1493);
							parameter232=parameter();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_parameter.add(parameter232.getTree());
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:229: ( ',' parameter )*
							loop31:
							while (true) {
								int alt31=2;
								int LA31_0 = input.LA(1);
								if ( (LA31_0==15) ) {
									alt31=1;
								}

								switch (alt31) {
								case 1 :
									// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:230: ',' parameter
									{
									char_literal233=(Token)match(input,15,FOLLOW_15_in_timeline1496); if (state.failed) return retval; 
									if ( state.backtracking==0 ) stream_15.add(char_literal233);

									pushFollow(FOLLOW_parameter_in_timeline1498);
									parameter234=parameter();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) stream_parameter.add(parameter234.getTree());
									}
									break;

								default :
									break loop31;
								}
							}

							}
							break;

					}

					char_literal235=(Token)match(input,12,FOLLOW_12_in_timeline1504); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_12.add(char_literal235);

					// AST REWRITE
					// elements: parameter, 53, ID
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 136:252: -> ^( 'FLEXIBLE' ID ( parameter )* )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:254: ^( 'FLEXIBLE' ID ( parameter )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTimeline(stream_53.nextToken()), root_1);
						adaptor.addChild(root_1, stream_ID.nextNode());
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:283: ( parameter )*
						while ( stream_parameter.hasNext() ) {
							adaptor.addChild(root_1, stream_parameter.nextTree());
						}
						stream_parameter.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 4 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:297: 'ESTA_LIGHT_MAX_CONSUMPTION' ID '(' ( parameter ( ',' parameter )* )? ')'
					{
					string_literal236=(Token)match(input,49,FOLLOW_49_in_timeline1521); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_49.add(string_literal236);

					ID237=(Token)match(input,ID,FOLLOW_ID_in_timeline1523); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(ID237);

					char_literal238=(Token)match(input,11,FOLLOW_11_in_timeline1525); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_11.add(char_literal238);

					// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:333: ( parameter ( ',' parameter )* )?
					int alt34=2;
					int LA34_0 = input.LA(1);
					if ( (LA34_0==ID||LA34_0==9||LA34_0==25||(LA34_0 >= 78 && LA34_0 <= 79)) ) {
						alt34=1;
					}
					switch (alt34) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:334: parameter ( ',' parameter )*
							{
							pushFollow(FOLLOW_parameter_in_timeline1528);
							parameter239=parameter();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_parameter.add(parameter239.getTree());
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:344: ( ',' parameter )*
							loop33:
							while (true) {
								int alt33=2;
								int LA33_0 = input.LA(1);
								if ( (LA33_0==15) ) {
									alt33=1;
								}

								switch (alt33) {
								case 1 :
									// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:345: ',' parameter
									{
									char_literal240=(Token)match(input,15,FOLLOW_15_in_timeline1531); if (state.failed) return retval; 
									if ( state.backtracking==0 ) stream_15.add(char_literal240);

									pushFollow(FOLLOW_parameter_in_timeline1533);
									parameter241=parameter();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) stream_parameter.add(parameter241.getTree());
									}
									break;

								default :
									break loop33;
								}
							}

							}
							break;

					}

					char_literal242=(Token)match(input,12,FOLLOW_12_in_timeline1539); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_12.add(char_literal242);

					// AST REWRITE
					// elements: ID, parameter, 49
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 136:367: -> ^( 'ESTA_LIGHT_MAX_CONSUMPTION' ID ( parameter )* )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:369: ^( 'ESTA_LIGHT_MAX_CONSUMPTION' ID ( parameter )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTimeline(stream_49.nextToken()), root_1);
						adaptor.addChild(root_1, stream_ID.nextNode());
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:136:416: ( parameter )*
						while ( stream_parameter.hasNext() ) {
							adaptor.addChild(root_1, stream_parameter.nextTree());
						}
						stream_parameter.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 30, timeline_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "timeline"


	public static class timeline_synchronization_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "timeline_synchronization"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:140:1: timeline_synchronization : 'SYNCHRONIZE' ID '.' ID '{' ( synchronization )+ '}' -> ^( 'SYNCHRONIZE' ID ID ( synchronization )+ ) ;
	public final ddl3Parser.timeline_synchronization_return timeline_synchronization() throws RecognitionException {
		ddl3Parser.timeline_synchronization_return retval = new ddl3Parser.timeline_synchronization_return();
		retval.start = input.LT(1);
		int timeline_synchronization_StartIndex = input.index();

		Object root_0 = null;

		Token string_literal243=null;
		Token ID244=null;
		Token char_literal245=null;
		Token ID246=null;
		Token char_literal247=null;
		Token char_literal249=null;
		ParserRuleReturnScope synchronization248 =null;

		Object string_literal243_tree=null;
		Object ID244_tree=null;
		Object char_literal245_tree=null;
		Object ID246_tree=null;
		Object char_literal247_tree=null;
		Object char_literal249_tree=null;
		RewriteRuleTokenStream stream_17=new RewriteRuleTokenStream(adaptor,"token 17");
		RewriteRuleTokenStream stream_80=new RewriteRuleTokenStream(adaptor,"token 80");
		RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
		RewriteRuleTokenStream stream_71=new RewriteRuleTokenStream(adaptor,"token 71");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleSubtreeStream stream_synchronization=new RewriteRuleSubtreeStream(adaptor,"rule synchronization");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 31) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:141:2: ( 'SYNCHRONIZE' ID '.' ID '{' ( synchronization )+ '}' -> ^( 'SYNCHRONIZE' ID ID ( synchronization )+ ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:141:4: 'SYNCHRONIZE' ID '.' ID '{' ( synchronization )+ '}'
			{
			string_literal243=(Token)match(input,71,FOLLOW_71_in_timeline_synchronization1563); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_71.add(string_literal243);

			ID244=(Token)match(input,ID,FOLLOW_ID_in_timeline_synchronization1565); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID244);

			char_literal245=(Token)match(input,17,FOLLOW_17_in_timeline_synchronization1567); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_17.add(char_literal245);

			ID246=(Token)match(input,ID,FOLLOW_ID_in_timeline_synchronization1569); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID246);

			char_literal247=(Token)match(input,80,FOLLOW_80_in_timeline_synchronization1571); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_80.add(char_literal247);

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:141:32: ( synchronization )+
			int cnt36=0;
			loop36:
			while (true) {
				int alt36=2;
				int LA36_0 = input.LA(1);
				if ( (LA36_0==75) ) {
					alt36=1;
				}

				switch (alt36) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:141:33: synchronization
					{
					pushFollow(FOLLOW_synchronization_in_timeline_synchronization1574);
					synchronization248=synchronization();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_synchronization.add(synchronization248.getTree());
					}
					break;

				default :
					if ( cnt36 >= 1 ) break loop36;
					if (state.backtracking>0) {state.failed=true; return retval;}
					EarlyExitException eee = new EarlyExitException(36, input);
					throw eee;
				}
				cnt36++;
			}

			char_literal249=(Token)match(input,81,FOLLOW_81_in_timeline_synchronization1578); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_81.add(char_literal249);

			// AST REWRITE
			// elements: ID, ID, 71, synchronization
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 141:55: -> ^( 'SYNCHRONIZE' ID ID ( synchronization )+ )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:141:57: ^( 'SYNCHRONIZE' ID ID ( synchronization )+ )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLTimelineSynchronization(stream_71.nextToken()), root_1);
				adaptor.addChild(root_1, stream_ID.nextNode());
				adaptor.addChild(root_1, stream_ID.nextNode());
				if ( !(stream_synchronization.hasNext()) ) {
					throw new RewriteEarlyExitException();
				}
				while ( stream_synchronization.hasNext() ) {
					adaptor.addChild(root_1, stream_synchronization.nextTree());
				}
				stream_synchronization.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 31, timeline_synchronization_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "timeline_synchronization"


	public static class synchronization_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "synchronization"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:143:1: synchronization : 'VALUE' component_decision '{' ( synchronization_element )* '}' -> ^( 'VALUE' component_decision ( synchronization_element )* ) ;
	public final ddl3Parser.synchronization_return synchronization() throws RecognitionException {
		ddl3Parser.synchronization_return retval = new ddl3Parser.synchronization_return();
		retval.start = input.LT(1);
		int synchronization_StartIndex = input.index();

		Object root_0 = null;

		Token string_literal250=null;
		Token char_literal252=null;
		Token char_literal254=null;
		ParserRuleReturnScope component_decision251 =null;
		ParserRuleReturnScope synchronization_element253 =null;

		Object string_literal250_tree=null;
		Object char_literal252_tree=null;
		Object char_literal254_tree=null;
		RewriteRuleTokenStream stream_80=new RewriteRuleTokenStream(adaptor,"token 80");
		RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
		RewriteRuleTokenStream stream_75=new RewriteRuleTokenStream(adaptor,"token 75");
		RewriteRuleSubtreeStream stream_synchronization_element=new RewriteRuleSubtreeStream(adaptor,"rule synchronization_element");
		RewriteRuleSubtreeStream stream_component_decision=new RewriteRuleSubtreeStream(adaptor,"rule component_decision");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 32) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:144:2: ( 'VALUE' component_decision '{' ( synchronization_element )* '}' -> ^( 'VALUE' component_decision ( synchronization_element )* ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:144:4: 'VALUE' component_decision '{' ( synchronization_element )* '}'
			{
			string_literal250=(Token)match(input,75,FOLLOW_75_in_synchronization1604); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_75.add(string_literal250);

			pushFollow(FOLLOW_component_decision_in_synchronization1606);
			component_decision251=component_decision();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_component_decision.add(component_decision251.getTree());
			char_literal252=(Token)match(input,80,FOLLOW_80_in_synchronization1608); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_80.add(char_literal252);

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:144:35: ( synchronization_element )*
			loop37:
			while (true) {
				int alt37=2;
				int LA37_0 = input.LA(1);
				if ( ((LA37_0 >= ID && LA37_0 <= VarID)||LA37_0==14||LA37_0==16||(LA37_0 >= 26 && LA37_0 <= 27)||(LA37_0 >= 29 && LA37_0 <= 32)||(LA37_0 >= 37 && LA37_0 <= 39)||(LA37_0 >= 42 && LA37_0 <= 47)||(LA37_0 >= 51 && LA37_0 <= 52)||(LA37_0 >= 54 && LA37_0 <= 56)||(LA37_0 >= 58 && LA37_0 <= 59)||(LA37_0 >= 65 && LA37_0 <= 70)) ) {
					alt37=1;
				}

				switch (alt37) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:144:36: synchronization_element
					{
					pushFollow(FOLLOW_synchronization_element_in_synchronization1611);
					synchronization_element253=synchronization_element();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_synchronization_element.add(synchronization_element253.getTree());
					}
					break;

				default :
					break loop37;
				}
			}

			char_literal254=(Token)match(input,81,FOLLOW_81_in_synchronization1615); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_81.add(char_literal254);

			// AST REWRITE
			// elements: synchronization_element, 75, component_decision
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 144:66: -> ^( 'VALUE' component_decision ( synchronization_element )* )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:144:68: ^( 'VALUE' component_decision ( synchronization_element )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLSynchronization(stream_75.nextToken()), root_1);
				adaptor.addChild(root_1, stream_component_decision.nextTree());
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:144:117: ( synchronization_element )*
				while ( stream_synchronization_element.hasNext() ) {
					adaptor.addChild(root_1, stream_synchronization_element.nextTree());
				}
				stream_synchronization_element.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 32, synchronization_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "synchronization"


	public static class component_decision_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "component_decision"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:146:1: component_decision : ( simple_ground_state_variable_component_decision | singleton_state_variable_component_decision | renewable_resource_component_decision | consumable_resource_component_decision );
	public final ddl3Parser.component_decision_return component_decision() throws RecognitionException {
		ddl3Parser.component_decision_return retval = new ddl3Parser.component_decision_return();
		retval.start = input.LT(1);
		int component_decision_StartIndex = input.index();

		Object root_0 = null;

		ParserRuleReturnScope simple_ground_state_variable_component_decision255 =null;
		ParserRuleReturnScope singleton_state_variable_component_decision256 =null;
		ParserRuleReturnScope renewable_resource_component_decision257 =null;
		ParserRuleReturnScope consumable_resource_component_decision258 =null;


		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 33) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:147:2: ( simple_ground_state_variable_component_decision | singleton_state_variable_component_decision | renewable_resource_component_decision | consumable_resource_component_decision )
			int alt38=4;
			alt38 = dfa38.predict(input);
			switch (alt38) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:147:4: simple_ground_state_variable_component_decision
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_ground_state_variable_component_decision_in_component_decision1639);
					simple_ground_state_variable_component_decision255=simple_ground_state_variable_component_decision();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_ground_state_variable_component_decision255.getTree());

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:147:54: singleton_state_variable_component_decision
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_singleton_state_variable_component_decision_in_component_decision1643);
					singleton_state_variable_component_decision256=singleton_state_variable_component_decision();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, singleton_state_variable_component_decision256.getTree());

					}
					break;
				case 3 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:147:100: renewable_resource_component_decision
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_renewable_resource_component_decision_in_component_decision1647);
					renewable_resource_component_decision257=renewable_resource_component_decision();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, renewable_resource_component_decision257.getTree());

					}
					break;
				case 4 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:147:140: consumable_resource_component_decision
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_consumable_resource_component_decision_in_component_decision1651);
					consumable_resource_component_decision258=consumable_resource_component_decision();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, consumable_resource_component_decision258.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 33, component_decision_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "component_decision"


	public static class instantiated_component_decision_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "instantiated_component_decision"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:149:1: instantiated_component_decision : ID ( '<' parameter ( ',' parameter )* '>' )? ID '.' ID '.' component_decision ( 'AT' range range range )? -> ^( ID ID ID component_decision ( parameter )* ( range range range )? ) ;
	public final ddl3Parser.instantiated_component_decision_return instantiated_component_decision() throws RecognitionException {
		ddl3Parser.instantiated_component_decision_return retval = new ddl3Parser.instantiated_component_decision_return();
		retval.start = input.LT(1);
		int instantiated_component_decision_StartIndex = input.index();

		Object root_0 = null;

		Token ID259=null;
		Token char_literal260=null;
		Token char_literal262=null;
		Token char_literal264=null;
		Token ID265=null;
		Token char_literal266=null;
		Token ID267=null;
		Token char_literal268=null;
		Token string_literal270=null;
		ParserRuleReturnScope parameter261 =null;
		ParserRuleReturnScope parameter263 =null;
		ParserRuleReturnScope component_decision269 =null;
		ParserRuleReturnScope range271 =null;
		ParserRuleReturnScope range272 =null;
		ParserRuleReturnScope range273 =null;

		Object ID259_tree=null;
		Object char_literal260_tree=null;
		Object char_literal262_tree=null;
		Object char_literal264_tree=null;
		Object ID265_tree=null;
		Object char_literal266_tree=null;
		Object ID267_tree=null;
		Object char_literal268_tree=null;
		Object string_literal270_tree=null;
		RewriteRuleTokenStream stream_23=new RewriteRuleTokenStream(adaptor,"token 23");
		RewriteRuleTokenStream stream_15=new RewriteRuleTokenStream(adaptor,"token 15");
		RewriteRuleTokenStream stream_17=new RewriteRuleTokenStream(adaptor,"token 17");
		RewriteRuleTokenStream stream_28=new RewriteRuleTokenStream(adaptor,"token 28");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_20=new RewriteRuleTokenStream(adaptor,"token 20");
		RewriteRuleSubtreeStream stream_parameter=new RewriteRuleSubtreeStream(adaptor,"rule parameter");
		RewriteRuleSubtreeStream stream_range=new RewriteRuleSubtreeStream(adaptor,"rule range");
		RewriteRuleSubtreeStream stream_component_decision=new RewriteRuleSubtreeStream(adaptor,"rule component_decision");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 34) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:150:2: ( ID ( '<' parameter ( ',' parameter )* '>' )? ID '.' ID '.' component_decision ( 'AT' range range range )? -> ^( ID ID ID component_decision ( parameter )* ( range range range )? ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:150:4: ID ( '<' parameter ( ',' parameter )* '>' )? ID '.' ID '.' component_decision ( 'AT' range range range )?
			{
			ID259=(Token)match(input,ID,FOLLOW_ID_in_instantiated_component_decision1660); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID259);

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:150:7: ( '<' parameter ( ',' parameter )* '>' )?
			int alt40=2;
			int LA40_0 = input.LA(1);
			if ( (LA40_0==20) ) {
				alt40=1;
			}
			switch (alt40) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:150:8: '<' parameter ( ',' parameter )* '>'
					{
					char_literal260=(Token)match(input,20,FOLLOW_20_in_instantiated_component_decision1663); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_20.add(char_literal260);

					pushFollow(FOLLOW_parameter_in_instantiated_component_decision1665);
					parameter261=parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_parameter.add(parameter261.getTree());
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:150:22: ( ',' parameter )*
					loop39:
					while (true) {
						int alt39=2;
						int LA39_0 = input.LA(1);
						if ( (LA39_0==15) ) {
							alt39=1;
						}

						switch (alt39) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:150:23: ',' parameter
							{
							char_literal262=(Token)match(input,15,FOLLOW_15_in_instantiated_component_decision1668); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_15.add(char_literal262);

							pushFollow(FOLLOW_parameter_in_instantiated_component_decision1670);
							parameter263=parameter();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_parameter.add(parameter263.getTree());
							}
							break;

						default :
							break loop39;
						}
					}

					char_literal264=(Token)match(input,23,FOLLOW_23_in_instantiated_component_decision1674); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_23.add(char_literal264);

					}
					break;

			}

			ID265=(Token)match(input,ID,FOLLOW_ID_in_instantiated_component_decision1678); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID265);

			char_literal266=(Token)match(input,17,FOLLOW_17_in_instantiated_component_decision1680); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_17.add(char_literal266);

			ID267=(Token)match(input,ID,FOLLOW_ID_in_instantiated_component_decision1682); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID267);

			char_literal268=(Token)match(input,17,FOLLOW_17_in_instantiated_component_decision1684); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_17.add(char_literal268);

			pushFollow(FOLLOW_component_decision_in_instantiated_component_decision1686);
			component_decision269=component_decision();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_component_decision.add(component_decision269.getTree());
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:150:78: ( 'AT' range range range )?
			int alt41=2;
			int LA41_0 = input.LA(1);
			if ( (LA41_0==28) ) {
				alt41=1;
			}
			switch (alt41) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:150:79: 'AT' range range range
					{
					string_literal270=(Token)match(input,28,FOLLOW_28_in_instantiated_component_decision1689); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_28.add(string_literal270);

					pushFollow(FOLLOW_range_in_instantiated_component_decision1691);
					range271=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range271.getTree());
					pushFollow(FOLLOW_range_in_instantiated_component_decision1693);
					range272=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range272.getTree());
					pushFollow(FOLLOW_range_in_instantiated_component_decision1695);
					range273=range();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range.add(range273.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: range, component_decision, ID, ID, parameter, range, ID, range
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 150:104: -> ^( ID ID ID component_decision ( parameter )* ( range range range )? )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:150:106: ^( ID ID ID component_decision ( parameter )* ( range range range )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLInstantiatedComponentDecision(stream_ID.nextToken()), root_1);
				adaptor.addChild(root_1, stream_ID.nextNode());
				adaptor.addChild(root_1, stream_ID.nextNode());
				adaptor.addChild(root_1, stream_component_decision.nextTree());
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:150:170: ( parameter )*
				while ( stream_parameter.hasNext() ) {
					adaptor.addChild(root_1, stream_parameter.nextTree());
				}
				stream_parameter.reset();

				// /home/alessandro/opt/antlr/ddl3/ddl3.g:150:183: ( range range range )?
				if ( stream_range.hasNext()||stream_range.hasNext()||stream_range.hasNext() ) {
					adaptor.addChild(root_1, stream_range.nextTree());
					adaptor.addChild(root_1, stream_range.nextTree());
					adaptor.addChild(root_1, stream_range.nextTree());
				}
				stream_range.reset();
				stream_range.reset();
				stream_range.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 34, instantiated_component_decision_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "instantiated_component_decision"


	public static class parameter_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "parameter"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:152:1: parameter : ( ID | '!' | '?' | 'c' | 'u' );
	public final ddl3Parser.parameter_return parameter() throws RecognitionException {
		ddl3Parser.parameter_return retval = new ddl3Parser.parameter_return();
		retval.start = input.LT(1);
		int parameter_StartIndex = input.index();

		Object root_0 = null;

		Token set274=null;

		Object set274_tree=null;

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 35) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:153:2: ( ID | '!' | '?' | 'c' | 'u' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:
			{
			root_0 = (Object)adaptor.nil();


			set274=input.LT(1);
			if ( input.LA(1)==ID||input.LA(1)==9||input.LA(1)==25||(input.LA(1) >= 78 && input.LA(1) <= 79) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set274));
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 35, parameter_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "parameter"


	public static class simple_ground_state_variable_component_decision_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "simple_ground_state_variable_component_decision"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:155:1: simple_ground_state_variable_component_decision : ( '<' parameter ( ',' parameter )* '>' )? ID '(' ')' -> ^( ID ( parameter )* ) ;
	public final ddl3Parser.simple_ground_state_variable_component_decision_return simple_ground_state_variable_component_decision() throws RecognitionException {
		ddl3Parser.simple_ground_state_variable_component_decision_return retval = new ddl3Parser.simple_ground_state_variable_component_decision_return();
		retval.start = input.LT(1);
		int simple_ground_state_variable_component_decision_StartIndex = input.index();

		Object root_0 = null;

		Token char_literal275=null;
		Token char_literal277=null;
		Token char_literal279=null;
		Token ID280=null;
		Token char_literal281=null;
		Token char_literal282=null;
		ParserRuleReturnScope parameter276 =null;
		ParserRuleReturnScope parameter278 =null;

		Object char_literal275_tree=null;
		Object char_literal277_tree=null;
		Object char_literal279_tree=null;
		Object ID280_tree=null;
		Object char_literal281_tree=null;
		Object char_literal282_tree=null;
		RewriteRuleTokenStream stream_11=new RewriteRuleTokenStream(adaptor,"token 11");
		RewriteRuleTokenStream stream_23=new RewriteRuleTokenStream(adaptor,"token 23");
		RewriteRuleTokenStream stream_12=new RewriteRuleTokenStream(adaptor,"token 12");
		RewriteRuleTokenStream stream_15=new RewriteRuleTokenStream(adaptor,"token 15");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_20=new RewriteRuleTokenStream(adaptor,"token 20");
		RewriteRuleSubtreeStream stream_parameter=new RewriteRuleSubtreeStream(adaptor,"rule parameter");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 36) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:156:2: ( ( '<' parameter ( ',' parameter )* '>' )? ID '(' ')' -> ^( ID ( parameter )* ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:156:4: ( '<' parameter ( ',' parameter )* '>' )? ID '(' ')'
			{
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:156:4: ( '<' parameter ( ',' parameter )* '>' )?
			int alt43=2;
			int LA43_0 = input.LA(1);
			if ( (LA43_0==20) ) {
				alt43=1;
			}
			switch (alt43) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:156:5: '<' parameter ( ',' parameter )* '>'
					{
					char_literal275=(Token)match(input,20,FOLLOW_20_in_simple_ground_state_variable_component_decision1761); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_20.add(char_literal275);

					pushFollow(FOLLOW_parameter_in_simple_ground_state_variable_component_decision1763);
					parameter276=parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_parameter.add(parameter276.getTree());
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:156:19: ( ',' parameter )*
					loop42:
					while (true) {
						int alt42=2;
						int LA42_0 = input.LA(1);
						if ( (LA42_0==15) ) {
							alt42=1;
						}

						switch (alt42) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:156:20: ',' parameter
							{
							char_literal277=(Token)match(input,15,FOLLOW_15_in_simple_ground_state_variable_component_decision1766); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_15.add(char_literal277);

							pushFollow(FOLLOW_parameter_in_simple_ground_state_variable_component_decision1768);
							parameter278=parameter();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_parameter.add(parameter278.getTree());
							}
							break;

						default :
							break loop42;
						}
					}

					char_literal279=(Token)match(input,23,FOLLOW_23_in_simple_ground_state_variable_component_decision1772); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_23.add(char_literal279);

					}
					break;

			}

			ID280=(Token)match(input,ID,FOLLOW_ID_in_simple_ground_state_variable_component_decision1776); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID280);

			char_literal281=(Token)match(input,11,FOLLOW_11_in_simple_ground_state_variable_component_decision1778); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_11.add(char_literal281);

			char_literal282=(Token)match(input,12,FOLLOW_12_in_simple_ground_state_variable_component_decision1780); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_12.add(char_literal282);

			// AST REWRITE
			// elements: ID, parameter
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 156:53: -> ^( ID ( parameter )* )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:156:55: ^( ID ( parameter )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLSimpleGroundStateVariableComponentDecision(stream_ID.nextToken()), root_1);
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:156:107: ( parameter )*
				while ( stream_parameter.hasNext() ) {
					adaptor.addChild(root_1, stream_parameter.nextTree());
				}
				stream_parameter.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 36, simple_ground_state_variable_component_decision_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "simple_ground_state_variable_component_decision"


	public static class singleton_state_variable_component_decision_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "singleton_state_variable_component_decision"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:158:1: singleton_state_variable_component_decision : ( '<' parameter ( ',' parameter )* '>' )? ID '(' ( par_value ( ',' par_value )* )? ')' -> ^( ID ( par_value )* '(' ( parameter )* ) ;
	public final ddl3Parser.singleton_state_variable_component_decision_return singleton_state_variable_component_decision() throws RecognitionException {
		ddl3Parser.singleton_state_variable_component_decision_return retval = new ddl3Parser.singleton_state_variable_component_decision_return();
		retval.start = input.LT(1);
		int singleton_state_variable_component_decision_StartIndex = input.index();

		Object root_0 = null;

		Token char_literal283=null;
		Token char_literal285=null;
		Token char_literal287=null;
		Token ID288=null;
		Token char_literal289=null;
		Token char_literal291=null;
		Token char_literal293=null;
		ParserRuleReturnScope parameter284 =null;
		ParserRuleReturnScope parameter286 =null;
		ParserRuleReturnScope par_value290 =null;
		ParserRuleReturnScope par_value292 =null;

		Object char_literal283_tree=null;
		Object char_literal285_tree=null;
		Object char_literal287_tree=null;
		Object ID288_tree=null;
		Object char_literal289_tree=null;
		Object char_literal291_tree=null;
		Object char_literal293_tree=null;
		RewriteRuleTokenStream stream_11=new RewriteRuleTokenStream(adaptor,"token 11");
		RewriteRuleTokenStream stream_23=new RewriteRuleTokenStream(adaptor,"token 23");
		RewriteRuleTokenStream stream_12=new RewriteRuleTokenStream(adaptor,"token 12");
		RewriteRuleTokenStream stream_15=new RewriteRuleTokenStream(adaptor,"token 15");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_20=new RewriteRuleTokenStream(adaptor,"token 20");
		RewriteRuleSubtreeStream stream_par_value=new RewriteRuleSubtreeStream(adaptor,"rule par_value");
		RewriteRuleSubtreeStream stream_parameter=new RewriteRuleSubtreeStream(adaptor,"rule parameter");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 37) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:159:2: ( ( '<' parameter ( ',' parameter )* '>' )? ID '(' ( par_value ( ',' par_value )* )? ')' -> ^( ID ( par_value )* '(' ( parameter )* ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:159:4: ( '<' parameter ( ',' parameter )* '>' )? ID '(' ( par_value ( ',' par_value )* )? ')'
			{
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:159:4: ( '<' parameter ( ',' parameter )* '>' )?
			int alt45=2;
			int LA45_0 = input.LA(1);
			if ( (LA45_0==20) ) {
				alt45=1;
			}
			switch (alt45) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:159:5: '<' parameter ( ',' parameter )* '>'
					{
					char_literal283=(Token)match(input,20,FOLLOW_20_in_singleton_state_variable_component_decision1802); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_20.add(char_literal283);

					pushFollow(FOLLOW_parameter_in_singleton_state_variable_component_decision1804);
					parameter284=parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_parameter.add(parameter284.getTree());
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:159:19: ( ',' parameter )*
					loop44:
					while (true) {
						int alt44=2;
						int LA44_0 = input.LA(1);
						if ( (LA44_0==15) ) {
							alt44=1;
						}

						switch (alt44) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:159:20: ',' parameter
							{
							char_literal285=(Token)match(input,15,FOLLOW_15_in_singleton_state_variable_component_decision1807); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_15.add(char_literal285);

							pushFollow(FOLLOW_parameter_in_singleton_state_variable_component_decision1809);
							parameter286=parameter();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_parameter.add(parameter286.getTree());
							}
							break;

						default :
							break loop44;
						}
					}

					char_literal287=(Token)match(input,23,FOLLOW_23_in_singleton_state_variable_component_decision1813); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_23.add(char_literal287);

					}
					break;

			}

			ID288=(Token)match(input,ID,FOLLOW_ID_in_singleton_state_variable_component_decision1817); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID288);

			char_literal289=(Token)match(input,11,FOLLOW_11_in_singleton_state_variable_component_decision1819); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_11.add(char_literal289);

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:159:49: ( par_value ( ',' par_value )* )?
			int alt47=2;
			int LA47_0 = input.LA(1);
			if ( (LA47_0==VarID) ) {
				alt47=1;
			}
			switch (alt47) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:159:50: par_value ( ',' par_value )*
					{
					pushFollow(FOLLOW_par_value_in_singleton_state_variable_component_decision1822);
					par_value290=par_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_par_value.add(par_value290.getTree());
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:159:60: ( ',' par_value )*
					loop46:
					while (true) {
						int alt46=2;
						int LA46_0 = input.LA(1);
						if ( (LA46_0==15) ) {
							alt46=1;
						}

						switch (alt46) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:159:61: ',' par_value
							{
							char_literal291=(Token)match(input,15,FOLLOW_15_in_singleton_state_variable_component_decision1825); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_15.add(char_literal291);

							pushFollow(FOLLOW_par_value_in_singleton_state_variable_component_decision1827);
							par_value292=par_value();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_par_value.add(par_value292.getTree());
							}
							break;

						default :
							break loop46;
						}
					}

					}
					break;

			}

			char_literal293=(Token)match(input,12,FOLLOW_12_in_singleton_state_variable_component_decision1833); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_12.add(char_literal293);

			// AST REWRITE
			// elements: 11, parameter, par_value, ID
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 159:83: -> ^( ID ( par_value )* '(' ( parameter )* )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:159:85: ^( ID ( par_value )* '(' ( parameter )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLSingletonStateVariableComponentDecision(stream_ID.nextToken()), root_1);
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:159:134: ( par_value )*
				while ( stream_par_value.hasNext() ) {
					adaptor.addChild(root_1, stream_par_value.nextTree());
				}
				stream_par_value.reset();

				adaptor.addChild(root_1, stream_11.nextNode());
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:159:149: ( parameter )*
				while ( stream_parameter.hasNext() ) {
					adaptor.addChild(root_1, stream_parameter.nextTree());
				}
				stream_parameter.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 37, singleton_state_variable_component_decision_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "singleton_state_variable_component_decision"


	public static class renewable_resource_component_decision_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "renewable_resource_component_decision"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:161:1: renewable_resource_component_decision : ( '<' parameter ( ',' parameter )* '>' )? 'REQUIREMENT' '(' par_value ')' -> ^( 'REQUIREMENT' par_value ( parameter )* ) ;
	public final ddl3Parser.renewable_resource_component_decision_return renewable_resource_component_decision() throws RecognitionException {
		ddl3Parser.renewable_resource_component_decision_return retval = new ddl3Parser.renewable_resource_component_decision_return();
		retval.start = input.LT(1);
		int renewable_resource_component_decision_StartIndex = input.index();

		Object root_0 = null;

		Token char_literal294=null;
		Token char_literal296=null;
		Token char_literal298=null;
		Token string_literal299=null;
		Token char_literal300=null;
		Token char_literal302=null;
		ParserRuleReturnScope parameter295 =null;
		ParserRuleReturnScope parameter297 =null;
		ParserRuleReturnScope par_value301 =null;

		Object char_literal294_tree=null;
		Object char_literal296_tree=null;
		Object char_literal298_tree=null;
		Object string_literal299_tree=null;
		Object char_literal300_tree=null;
		Object char_literal302_tree=null;
		RewriteRuleTokenStream stream_11=new RewriteRuleTokenStream(adaptor,"token 11");
		RewriteRuleTokenStream stream_23=new RewriteRuleTokenStream(adaptor,"token 23");
		RewriteRuleTokenStream stream_12=new RewriteRuleTokenStream(adaptor,"token 12");
		RewriteRuleTokenStream stream_15=new RewriteRuleTokenStream(adaptor,"token 15");
		RewriteRuleTokenStream stream_63=new RewriteRuleTokenStream(adaptor,"token 63");
		RewriteRuleTokenStream stream_20=new RewriteRuleTokenStream(adaptor,"token 20");
		RewriteRuleSubtreeStream stream_par_value=new RewriteRuleSubtreeStream(adaptor,"rule par_value");
		RewriteRuleSubtreeStream stream_parameter=new RewriteRuleSubtreeStream(adaptor,"rule parameter");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 38) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:162:2: ( ( '<' parameter ( ',' parameter )* '>' )? 'REQUIREMENT' '(' par_value ')' -> ^( 'REQUIREMENT' par_value ( parameter )* ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:162:4: ( '<' parameter ( ',' parameter )* '>' )? 'REQUIREMENT' '(' par_value ')'
			{
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:162:4: ( '<' parameter ( ',' parameter )* '>' )?
			int alt49=2;
			int LA49_0 = input.LA(1);
			if ( (LA49_0==20) ) {
				alt49=1;
			}
			switch (alt49) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:162:5: '<' parameter ( ',' parameter )* '>'
					{
					char_literal294=(Token)match(input,20,FOLLOW_20_in_renewable_resource_component_decision1860); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_20.add(char_literal294);

					pushFollow(FOLLOW_parameter_in_renewable_resource_component_decision1862);
					parameter295=parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_parameter.add(parameter295.getTree());
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:162:19: ( ',' parameter )*
					loop48:
					while (true) {
						int alt48=2;
						int LA48_0 = input.LA(1);
						if ( (LA48_0==15) ) {
							alt48=1;
						}

						switch (alt48) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:162:20: ',' parameter
							{
							char_literal296=(Token)match(input,15,FOLLOW_15_in_renewable_resource_component_decision1865); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_15.add(char_literal296);

							pushFollow(FOLLOW_parameter_in_renewable_resource_component_decision1867);
							parameter297=parameter();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_parameter.add(parameter297.getTree());
							}
							break;

						default :
							break loop48;
						}
					}

					char_literal298=(Token)match(input,23,FOLLOW_23_in_renewable_resource_component_decision1871); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_23.add(char_literal298);

					}
					break;

			}

			string_literal299=(Token)match(input,63,FOLLOW_63_in_renewable_resource_component_decision1875); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_63.add(string_literal299);

			char_literal300=(Token)match(input,11,FOLLOW_11_in_renewable_resource_component_decision1877); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_11.add(char_literal300);

			pushFollow(FOLLOW_par_value_in_renewable_resource_component_decision1879);
			par_value301=par_value();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_par_value.add(par_value301.getTree());
			char_literal302=(Token)match(input,12,FOLLOW_12_in_renewable_resource_component_decision1881); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_12.add(char_literal302);

			// AST REWRITE
			// elements: parameter, 63, par_value
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 162:74: -> ^( 'REQUIREMENT' par_value ( parameter )* )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:162:76: ^( 'REQUIREMENT' par_value ( parameter )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLRenewableResourceComponentDecision(stream_63.nextToken()), root_1);
				adaptor.addChild(root_1, stream_par_value.nextTree());
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:162:141: ( parameter )*
				while ( stream_parameter.hasNext() ) {
					adaptor.addChild(root_1, stream_parameter.nextTree());
				}
				stream_parameter.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 38, renewable_resource_component_decision_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "renewable_resource_component_decision"


	public static class consumable_resource_component_decision_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "consumable_resource_component_decision"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:164:1: consumable_resource_component_decision : ( consumable_resource_production_component_decision | consumable_resource_consumption_component_decision );
	public final ddl3Parser.consumable_resource_component_decision_return consumable_resource_component_decision() throws RecognitionException {
		ddl3Parser.consumable_resource_component_decision_return retval = new ddl3Parser.consumable_resource_component_decision_return();
		retval.start = input.LT(1);
		int consumable_resource_component_decision_StartIndex = input.index();

		Object root_0 = null;

		ParserRuleReturnScope consumable_resource_production_component_decision303 =null;
		ParserRuleReturnScope consumable_resource_consumption_component_decision304 =null;


		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 39) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:165:2: ( consumable_resource_production_component_decision | consumable_resource_consumption_component_decision )
			int alt50=2;
			alt50 = dfa50.predict(input);
			switch (alt50) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:165:4: consumable_resource_production_component_decision
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_consumable_resource_production_component_decision_in_consumable_resource_component_decision1905);
					consumable_resource_production_component_decision303=consumable_resource_production_component_decision();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, consumable_resource_production_component_decision303.getTree());

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:165:56: consumable_resource_consumption_component_decision
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_consumable_resource_consumption_component_decision_in_consumable_resource_component_decision1909);
					consumable_resource_consumption_component_decision304=consumable_resource_consumption_component_decision();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, consumable_resource_consumption_component_decision304.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 39, consumable_resource_component_decision_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "consumable_resource_component_decision"


	public static class consumable_resource_production_component_decision_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "consumable_resource_production_component_decision"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:167:1: consumable_resource_production_component_decision : ( '<' parameter ( ',' parameter )* '>' )? 'PRODUCTION' '(' par_value ')' -> ^( 'PRODUCTION' par_value ( parameter )* ) ;
	public final ddl3Parser.consumable_resource_production_component_decision_return consumable_resource_production_component_decision() throws RecognitionException {
		ddl3Parser.consumable_resource_production_component_decision_return retval = new ddl3Parser.consumable_resource_production_component_decision_return();
		retval.start = input.LT(1);
		int consumable_resource_production_component_decision_StartIndex = input.index();

		Object root_0 = null;

		Token char_literal305=null;
		Token char_literal307=null;
		Token char_literal309=null;
		Token string_literal310=null;
		Token char_literal311=null;
		Token char_literal313=null;
		ParserRuleReturnScope parameter306 =null;
		ParserRuleReturnScope parameter308 =null;
		ParserRuleReturnScope par_value312 =null;

		Object char_literal305_tree=null;
		Object char_literal307_tree=null;
		Object char_literal309_tree=null;
		Object string_literal310_tree=null;
		Object char_literal311_tree=null;
		Object char_literal313_tree=null;
		RewriteRuleTokenStream stream_11=new RewriteRuleTokenStream(adaptor,"token 11");
		RewriteRuleTokenStream stream_23=new RewriteRuleTokenStream(adaptor,"token 23");
		RewriteRuleTokenStream stream_12=new RewriteRuleTokenStream(adaptor,"token 12");
		RewriteRuleTokenStream stream_15=new RewriteRuleTokenStream(adaptor,"token 15");
		RewriteRuleTokenStream stream_62=new RewriteRuleTokenStream(adaptor,"token 62");
		RewriteRuleTokenStream stream_20=new RewriteRuleTokenStream(adaptor,"token 20");
		RewriteRuleSubtreeStream stream_par_value=new RewriteRuleSubtreeStream(adaptor,"rule par_value");
		RewriteRuleSubtreeStream stream_parameter=new RewriteRuleSubtreeStream(adaptor,"rule parameter");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 40) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:168:2: ( ( '<' parameter ( ',' parameter )* '>' )? 'PRODUCTION' '(' par_value ')' -> ^( 'PRODUCTION' par_value ( parameter )* ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:168:4: ( '<' parameter ( ',' parameter )* '>' )? 'PRODUCTION' '(' par_value ')'
			{
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:168:4: ( '<' parameter ( ',' parameter )* '>' )?
			int alt52=2;
			int LA52_0 = input.LA(1);
			if ( (LA52_0==20) ) {
				alt52=1;
			}
			switch (alt52) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:168:5: '<' parameter ( ',' parameter )* '>'
					{
					char_literal305=(Token)match(input,20,FOLLOW_20_in_consumable_resource_production_component_decision1921); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_20.add(char_literal305);

					pushFollow(FOLLOW_parameter_in_consumable_resource_production_component_decision1923);
					parameter306=parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_parameter.add(parameter306.getTree());
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:168:19: ( ',' parameter )*
					loop51:
					while (true) {
						int alt51=2;
						int LA51_0 = input.LA(1);
						if ( (LA51_0==15) ) {
							alt51=1;
						}

						switch (alt51) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:168:20: ',' parameter
							{
							char_literal307=(Token)match(input,15,FOLLOW_15_in_consumable_resource_production_component_decision1926); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_15.add(char_literal307);

							pushFollow(FOLLOW_parameter_in_consumable_resource_production_component_decision1928);
							parameter308=parameter();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_parameter.add(parameter308.getTree());
							}
							break;

						default :
							break loop51;
						}
					}

					char_literal309=(Token)match(input,23,FOLLOW_23_in_consumable_resource_production_component_decision1932); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_23.add(char_literal309);

					}
					break;

			}

			string_literal310=(Token)match(input,62,FOLLOW_62_in_consumable_resource_production_component_decision1936); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_62.add(string_literal310);

			char_literal311=(Token)match(input,11,FOLLOW_11_in_consumable_resource_production_component_decision1938); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_11.add(char_literal311);

			pushFollow(FOLLOW_par_value_in_consumable_resource_production_component_decision1940);
			par_value312=par_value();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_par_value.add(par_value312.getTree());
			char_literal313=(Token)match(input,12,FOLLOW_12_in_consumable_resource_production_component_decision1942); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_12.add(char_literal313);

			// AST REWRITE
			// elements: parameter, 62, par_value
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 168:73: -> ^( 'PRODUCTION' par_value ( parameter )* )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:168:75: ^( 'PRODUCTION' par_value ( parameter )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLConsumableResourceComponentDecision(stream_62.nextToken()), root_1);
				adaptor.addChild(root_1, stream_par_value.nextTree());
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:168:140: ( parameter )*
				while ( stream_parameter.hasNext() ) {
					adaptor.addChild(root_1, stream_parameter.nextTree());
				}
				stream_parameter.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 40, consumable_resource_production_component_decision_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "consumable_resource_production_component_decision"


	public static class consumable_resource_consumption_component_decision_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "consumable_resource_consumption_component_decision"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:170:1: consumable_resource_consumption_component_decision : ( '<' parameter ( ',' parameter )* '>' )? 'CONSUMPTION' '(' par_value ')' -> ^( 'CONSUMPTION' par_value ( parameter )* ) ;
	public final ddl3Parser.consumable_resource_consumption_component_decision_return consumable_resource_consumption_component_decision() throws RecognitionException {
		ddl3Parser.consumable_resource_consumption_component_decision_return retval = new ddl3Parser.consumable_resource_consumption_component_decision_return();
		retval.start = input.LT(1);
		int consumable_resource_consumption_component_decision_StartIndex = input.index();

		Object root_0 = null;

		Token char_literal314=null;
		Token char_literal316=null;
		Token char_literal318=null;
		Token string_literal319=null;
		Token char_literal320=null;
		Token char_literal322=null;
		ParserRuleReturnScope parameter315 =null;
		ParserRuleReturnScope parameter317 =null;
		ParserRuleReturnScope par_value321 =null;

		Object char_literal314_tree=null;
		Object char_literal316_tree=null;
		Object char_literal318_tree=null;
		Object string_literal319_tree=null;
		Object char_literal320_tree=null;
		Object char_literal322_tree=null;
		RewriteRuleTokenStream stream_11=new RewriteRuleTokenStream(adaptor,"token 11");
		RewriteRuleTokenStream stream_23=new RewriteRuleTokenStream(adaptor,"token 23");
		RewriteRuleTokenStream stream_12=new RewriteRuleTokenStream(adaptor,"token 12");
		RewriteRuleTokenStream stream_36=new RewriteRuleTokenStream(adaptor,"token 36");
		RewriteRuleTokenStream stream_15=new RewriteRuleTokenStream(adaptor,"token 15");
		RewriteRuleTokenStream stream_20=new RewriteRuleTokenStream(adaptor,"token 20");
		RewriteRuleSubtreeStream stream_par_value=new RewriteRuleSubtreeStream(adaptor,"rule par_value");
		RewriteRuleSubtreeStream stream_parameter=new RewriteRuleSubtreeStream(adaptor,"rule parameter");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 41) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:171:2: ( ( '<' parameter ( ',' parameter )* '>' )? 'CONSUMPTION' '(' par_value ')' -> ^( 'CONSUMPTION' par_value ( parameter )* ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:171:4: ( '<' parameter ( ',' parameter )* '>' )? 'CONSUMPTION' '(' par_value ')'
			{
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:171:4: ( '<' parameter ( ',' parameter )* '>' )?
			int alt54=2;
			int LA54_0 = input.LA(1);
			if ( (LA54_0==20) ) {
				alt54=1;
			}
			switch (alt54) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:171:5: '<' parameter ( ',' parameter )* '>'
					{
					char_literal314=(Token)match(input,20,FOLLOW_20_in_consumable_resource_consumption_component_decision1967); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_20.add(char_literal314);

					pushFollow(FOLLOW_parameter_in_consumable_resource_consumption_component_decision1969);
					parameter315=parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_parameter.add(parameter315.getTree());
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:171:19: ( ',' parameter )*
					loop53:
					while (true) {
						int alt53=2;
						int LA53_0 = input.LA(1);
						if ( (LA53_0==15) ) {
							alt53=1;
						}

						switch (alt53) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:171:20: ',' parameter
							{
							char_literal316=(Token)match(input,15,FOLLOW_15_in_consumable_resource_consumption_component_decision1972); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_15.add(char_literal316);

							pushFollow(FOLLOW_parameter_in_consumable_resource_consumption_component_decision1974);
							parameter317=parameter();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_parameter.add(parameter317.getTree());
							}
							break;

						default :
							break loop53;
						}
					}

					char_literal318=(Token)match(input,23,FOLLOW_23_in_consumable_resource_consumption_component_decision1978); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_23.add(char_literal318);

					}
					break;

			}

			string_literal319=(Token)match(input,36,FOLLOW_36_in_consumable_resource_consumption_component_decision1982); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_36.add(string_literal319);

			char_literal320=(Token)match(input,11,FOLLOW_11_in_consumable_resource_consumption_component_decision1984); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_11.add(char_literal320);

			pushFollow(FOLLOW_par_value_in_consumable_resource_consumption_component_decision1986);
			par_value321=par_value();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_par_value.add(par_value321.getTree());
			char_literal322=(Token)match(input,12,FOLLOW_12_in_consumable_resource_consumption_component_decision1988); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_12.add(char_literal322);

			// AST REWRITE
			// elements: parameter, 36, par_value
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 171:74: -> ^( 'CONSUMPTION' par_value ( parameter )* )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:171:76: ^( 'CONSUMPTION' par_value ( parameter )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLConsumableResourceComponentDecision(stream_36.nextToken()), root_1);
				adaptor.addChild(root_1, stream_par_value.nextTree());
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:171:142: ( parameter )*
				while ( stream_parameter.hasNext() ) {
					adaptor.addChild(root_1, stream_parameter.nextTree());
				}
				stream_parameter.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 41, consumable_resource_consumption_component_decision_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "consumable_resource_consumption_component_decision"


	public static class par_value_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "par_value"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:173:1: par_value : ( VarID ( '=' number )? -> ^( VarID ( number )? ) | VarID ( '=' ID )? -> ^( VarID ( ID )? ) );
	public final ddl3Parser.par_value_return par_value() throws RecognitionException {
		ddl3Parser.par_value_return retval = new ddl3Parser.par_value_return();
		retval.start = input.LT(1);
		int par_value_StartIndex = input.index();

		Object root_0 = null;

		Token VarID323=null;
		Token char_literal324=null;
		Token VarID326=null;
		Token char_literal327=null;
		Token ID328=null;
		ParserRuleReturnScope number325 =null;

		Object VarID323_tree=null;
		Object char_literal324_tree=null;
		Object VarID326_tree=null;
		Object char_literal327_tree=null;
		Object ID328_tree=null;
		RewriteRuleTokenStream stream_22=new RewriteRuleTokenStream(adaptor,"token 22");
		RewriteRuleTokenStream stream_VarID=new RewriteRuleTokenStream(adaptor,"token VarID");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleSubtreeStream stream_number=new RewriteRuleSubtreeStream(adaptor,"rule number");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 42) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:174:2: ( VarID ( '=' number )? -> ^( VarID ( number )? ) | VarID ( '=' ID )? -> ^( VarID ( ID )? ) )
			int alt57=2;
			int LA57_0 = input.LA(1);
			if ( (LA57_0==VarID) ) {
				int LA57_1 = input.LA(2);
				if ( (synpred101_ddl3()) ) {
					alt57=1;
				}
				else if ( (true) ) {
					alt57=2;
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 57, 0, input);
				throw nvae;
			}

			switch (alt57) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:174:4: VarID ( '=' number )?
					{
					VarID323=(Token)match(input,VarID,FOLLOW_VarID_in_par_value2010); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_VarID.add(VarID323);

					// /home/alessandro/opt/antlr/ddl3/ddl3.g:174:10: ( '=' number )?
					int alt55=2;
					int LA55_0 = input.LA(1);
					if ( (LA55_0==22) ) {
						alt55=1;
					}
					switch (alt55) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:174:11: '=' number
							{
							char_literal324=(Token)match(input,22,FOLLOW_22_in_par_value2013); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_22.add(char_literal324);

							pushFollow(FOLLOW_number_in_par_value2015);
							number325=number();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_number.add(number325.getTree());
							}
							break;

					}

					// AST REWRITE
					// elements: VarID, number
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 174:24: -> ^( VarID ( number )? )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:174:26: ^( VarID ( number )? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(stream_VarID.nextNode(), root_1);
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:174:34: ( number )?
						if ( stream_number.hasNext() ) {
							adaptor.addChild(root_1, stream_number.nextTree());
						}
						stream_number.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:174:45: VarID ( '=' ID )?
					{
					VarID326=(Token)match(input,VarID,FOLLOW_VarID_in_par_value2029); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_VarID.add(VarID326);

					// /home/alessandro/opt/antlr/ddl3/ddl3.g:174:51: ( '=' ID )?
					int alt56=2;
					int LA56_0 = input.LA(1);
					if ( (LA56_0==22) ) {
						alt56=1;
					}
					switch (alt56) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:174:52: '=' ID
							{
							char_literal327=(Token)match(input,22,FOLLOW_22_in_par_value2032); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_22.add(char_literal327);

							ID328=(Token)match(input,ID,FOLLOW_ID_in_par_value2034); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_ID.add(ID328);

							}
							break;

					}

					// AST REWRITE
					// elements: VarID, ID
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 174:61: -> ^( VarID ( ID )? )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:174:63: ^( VarID ( ID )? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(stream_VarID.nextNode(), root_1);
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:174:71: ( ID )?
						if ( stream_ID.hasNext() ) {
							adaptor.addChild(root_1, stream_ID.nextNode());
						}
						stream_ID.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 42, par_value_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "par_value"


	public static class synchronization_element_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "synchronization_element"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:176:1: synchronization_element : ( instantiated_component_decision ';' | parameter_constraint ';' | (from= ID )? temporal_relation_type to= ID ';' -> ^( ';' temporal_relation_type $to ( $from)? ) );
	public final ddl3Parser.synchronization_element_return synchronization_element() throws RecognitionException {
		ddl3Parser.synchronization_element_return retval = new ddl3Parser.synchronization_element_return();
		retval.start = input.LT(1);
		int synchronization_element_StartIndex = input.index();

		Object root_0 = null;

		Token from=null;
		Token to=null;
		Token char_literal330=null;
		Token char_literal332=null;
		Token char_literal334=null;
		ParserRuleReturnScope instantiated_component_decision329 =null;
		ParserRuleReturnScope parameter_constraint331 =null;
		ParserRuleReturnScope temporal_relation_type333 =null;

		Object from_tree=null;
		Object to_tree=null;
		Object char_literal330_tree=null;
		Object char_literal332_tree=null;
		Object char_literal334_tree=null;
		RewriteRuleTokenStream stream_19=new RewriteRuleTokenStream(adaptor,"token 19");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleSubtreeStream stream_temporal_relation_type=new RewriteRuleSubtreeStream(adaptor,"rule temporal_relation_type");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 43) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:177:2: ( instantiated_component_decision ';' | parameter_constraint ';' | (from= ID )? temporal_relation_type to= ID ';' -> ^( ';' temporal_relation_type $to ( $from)? ) )
			int alt59=3;
			switch ( input.LA(1) ) {
			case ID:
				{
				int LA59_1 = input.LA(2);
				if ( (LA59_1==ID||LA59_1==20) ) {
					alt59=1;
				}
				else if ( ((LA59_1 >= 26 && LA59_1 <= 27)||(LA59_1 >= 29 && LA59_1 <= 32)||(LA59_1 >= 37 && LA59_1 <= 39)||(LA59_1 >= 42 && LA59_1 <= 47)||(LA59_1 >= 51 && LA59_1 <= 52)||(LA59_1 >= 55 && LA59_1 <= 56)||(LA59_1 >= 58 && LA59_1 <= 59)||(LA59_1 >= 65 && LA59_1 <= 70)) ) {
					alt59=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 59, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case INT:
			case VarID:
			case 14:
			case 16:
			case 54:
				{
				alt59=2;
				}
				break;
			case 26:
			case 27:
			case 29:
			case 30:
			case 31:
			case 32:
			case 37:
			case 38:
			case 39:
			case 42:
			case 43:
			case 44:
			case 45:
			case 46:
			case 47:
			case 51:
			case 52:
			case 55:
			case 56:
			case 58:
			case 59:
			case 65:
			case 66:
			case 67:
			case 68:
			case 69:
			case 70:
				{
				alt59=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 59, 0, input);
				throw nvae;
			}
			switch (alt59) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:177:4: instantiated_component_decision ';'
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_instantiated_component_decision_in_synchronization_element2053);
					instantiated_component_decision329=instantiated_component_decision();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, instantiated_component_decision329.getTree());

					char_literal330=(Token)match(input,19,FOLLOW_19_in_synchronization_element2055); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal330_tree = (Object)adaptor.create(char_literal330);
					adaptor.addChild(root_0, char_literal330_tree);
					}

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:177:42: parameter_constraint ';'
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_parameter_constraint_in_synchronization_element2059);
					parameter_constraint331=parameter_constraint();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, parameter_constraint331.getTree());

					char_literal332=(Token)match(input,19,FOLLOW_19_in_synchronization_element2061); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal332_tree = (Object)adaptor.create(char_literal332);
					adaptor.addChild(root_0, char_literal332_tree);
					}

					}
					break;
				case 3 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:177:69: (from= ID )? temporal_relation_type to= ID ';'
					{
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:177:69: (from= ID )?
					int alt58=2;
					int LA58_0 = input.LA(1);
					if ( (LA58_0==ID) ) {
						alt58=1;
					}
					switch (alt58) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:177:70: from= ID
							{
							from=(Token)match(input,ID,FOLLOW_ID_in_synchronization_element2068); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_ID.add(from);

							}
							break;

					}

					pushFollow(FOLLOW_temporal_relation_type_in_synchronization_element2072);
					temporal_relation_type333=temporal_relation_type();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_temporal_relation_type.add(temporal_relation_type333.getTree());
					to=(Token)match(input,ID,FOLLOW_ID_in_synchronization_element2076); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(to);

					char_literal334=(Token)match(input,19,FOLLOW_19_in_synchronization_element2078); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_19.add(char_literal334);

					// AST REWRITE
					// elements: 19, from, temporal_relation_type, to
					// token labels: from, to
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleTokenStream stream_from=new RewriteRuleTokenStream(adaptor,"token from",from);
					RewriteRuleTokenStream stream_to=new RewriteRuleTokenStream(adaptor,"token to",to);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 177:113: -> ^( ';' temporal_relation_type $to ( $from)? )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:177:115: ^( ';' temporal_relation_type $to ( $from)? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelation(stream_19.nextToken()), root_1);
						adaptor.addChild(root_1, stream_temporal_relation_type.nextTree());
						adaptor.addChild(root_1, stream_to.nextNode());
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:177:170: ( $from)?
						if ( stream_from.hasNext() ) {
							adaptor.addChild(root_1, stream_from.nextNode());
						}
						stream_from.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 43, synchronization_element_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "synchronization_element"


	public static class problem_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "problem"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:179:1: problem : 'PROBLEM' ID '(' 'DOMAIN' ID ')' '{' ( problem_element )* '}' -> ^( ID ID ( problem_element )* ) ;
	public final ddl3Parser.problem_return problem() throws RecognitionException {
		ddl3Parser.problem_return retval = new ddl3Parser.problem_return();
		retval.start = input.LT(1);
		int problem_StartIndex = input.index();

		Object root_0 = null;

		Token string_literal335=null;
		Token ID336=null;
		Token char_literal337=null;
		Token string_literal338=null;
		Token ID339=null;
		Token char_literal340=null;
		Token char_literal341=null;
		Token char_literal343=null;
		ParserRuleReturnScope problem_element342 =null;

		Object string_literal335_tree=null;
		Object ID336_tree=null;
		Object char_literal337_tree=null;
		Object string_literal338_tree=null;
		Object ID339_tree=null;
		Object char_literal340_tree=null;
		Object char_literal341_tree=null;
		Object char_literal343_tree=null;
		RewriteRuleTokenStream stream_11=new RewriteRuleTokenStream(adaptor,"token 11");
		RewriteRuleTokenStream stream_12=new RewriteRuleTokenStream(adaptor,"token 12");
		RewriteRuleTokenStream stream_80=new RewriteRuleTokenStream(adaptor,"token 80");
		RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
		RewriteRuleTokenStream stream_61=new RewriteRuleTokenStream(adaptor,"token 61");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_41=new RewriteRuleTokenStream(adaptor,"token 41");
		RewriteRuleSubtreeStream stream_problem_element=new RewriteRuleSubtreeStream(adaptor,"rule problem_element");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 44) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:179:9: ( 'PROBLEM' ID '(' 'DOMAIN' ID ')' '{' ( problem_element )* '}' -> ^( ID ID ( problem_element )* ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:179:11: 'PROBLEM' ID '(' 'DOMAIN' ID ')' '{' ( problem_element )* '}'
			{
			string_literal335=(Token)match(input,61,FOLLOW_61_in_problem2103); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_61.add(string_literal335);

			ID336=(Token)match(input,ID,FOLLOW_ID_in_problem2105); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID336);

			char_literal337=(Token)match(input,11,FOLLOW_11_in_problem2107); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_11.add(char_literal337);

			string_literal338=(Token)match(input,41,FOLLOW_41_in_problem2109); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_41.add(string_literal338);

			ID339=(Token)match(input,ID,FOLLOW_ID_in_problem2111); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID339);

			char_literal340=(Token)match(input,12,FOLLOW_12_in_problem2113); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_12.add(char_literal340);

			char_literal341=(Token)match(input,80,FOLLOW_80_in_problem2115); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_80.add(char_literal341);

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:179:48: ( problem_element )*
			loop60:
			while (true) {
				int alt60=2;
				int LA60_0 = input.LA(1);
				if ( ((LA60_0 >= ID && LA60_0 <= VarID)||LA60_0==14||LA60_0==16||LA60_0==54) ) {
					alt60=1;
				}

				switch (alt60) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:179:48: problem_element
					{
					pushFollow(FOLLOW_problem_element_in_problem2117);
					problem_element342=problem_element();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_problem_element.add(problem_element342.getTree());
					}
					break;

				default :
					break loop60;
				}
			}

			char_literal343=(Token)match(input,81,FOLLOW_81_in_problem2120); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_81.add(char_literal343);

			// AST REWRITE
			// elements: ID, problem_element, ID
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 179:69: -> ^( ID ID ( problem_element )* )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:179:71: ^( ID ID ( problem_element )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLProblem(stream_ID.nextToken()), root_1);
				adaptor.addChild(root_1, stream_ID.nextNode());
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:179:91: ( problem_element )*
				while ( stream_problem_element.hasNext() ) {
					adaptor.addChild(root_1, stream_problem_element.nextTree());
				}
				stream_problem_element.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 44, problem_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "problem"


	public static class problem_element_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "problem_element"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:181:1: problem_element : ( instantiated_component_decision ';' |from= ID temporal_relation_type to= ID ';' -> ^( ';' temporal_relation_type $to ( $from)? ) | parameter_constraint ';' );
	public final ddl3Parser.problem_element_return problem_element() throws RecognitionException {
		ddl3Parser.problem_element_return retval = new ddl3Parser.problem_element_return();
		retval.start = input.LT(1);
		int problem_element_StartIndex = input.index();

		Object root_0 = null;

		Token from=null;
		Token to=null;
		Token char_literal345=null;
		Token char_literal347=null;
		Token char_literal349=null;
		ParserRuleReturnScope instantiated_component_decision344 =null;
		ParserRuleReturnScope temporal_relation_type346 =null;
		ParserRuleReturnScope parameter_constraint348 =null;

		Object from_tree=null;
		Object to_tree=null;
		Object char_literal345_tree=null;
		Object char_literal347_tree=null;
		Object char_literal349_tree=null;
		RewriteRuleTokenStream stream_19=new RewriteRuleTokenStream(adaptor,"token 19");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleSubtreeStream stream_temporal_relation_type=new RewriteRuleSubtreeStream(adaptor,"rule temporal_relation_type");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 45) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:182:2: ( instantiated_component_decision ';' |from= ID temporal_relation_type to= ID ';' -> ^( ';' temporal_relation_type $to ( $from)? ) | parameter_constraint ';' )
			int alt61=3;
			int LA61_0 = input.LA(1);
			if ( (LA61_0==ID) ) {
				int LA61_1 = input.LA(2);
				if ( (LA61_1==ID||LA61_1==20) ) {
					alt61=1;
				}
				else if ( ((LA61_1 >= 26 && LA61_1 <= 27)||(LA61_1 >= 29 && LA61_1 <= 32)||(LA61_1 >= 37 && LA61_1 <= 39)||(LA61_1 >= 42 && LA61_1 <= 47)||(LA61_1 >= 51 && LA61_1 <= 52)||(LA61_1 >= 55 && LA61_1 <= 56)||(LA61_1 >= 58 && LA61_1 <= 59)||(LA61_1 >= 65 && LA61_1 <= 70)) ) {
					alt61=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 61, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( ((LA61_0 >= INT && LA61_0 <= VarID)||LA61_0==14||LA61_0==16||LA61_0==54) ) {
				alt61=3;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 61, 0, input);
				throw nvae;
			}

			switch (alt61) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:182:4: instantiated_component_decision ';'
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_instantiated_component_decision_in_problem_element2142);
					instantiated_component_decision344=instantiated_component_decision();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, instantiated_component_decision344.getTree());

					char_literal345=(Token)match(input,19,FOLLOW_19_in_problem_element2144); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal345_tree = (Object)adaptor.create(char_literal345);
					adaptor.addChild(root_0, char_literal345_tree);
					}

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:182:42: from= ID temporal_relation_type to= ID ';'
					{
					from=(Token)match(input,ID,FOLLOW_ID_in_problem_element2150); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(from);

					pushFollow(FOLLOW_temporal_relation_type_in_problem_element2152);
					temporal_relation_type346=temporal_relation_type();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_temporal_relation_type.add(temporal_relation_type346.getTree());
					to=(Token)match(input,ID,FOLLOW_ID_in_problem_element2156); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(to);

					char_literal347=(Token)match(input,19,FOLLOW_19_in_problem_element2158); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_19.add(char_literal347);

					// AST REWRITE
					// elements: to, 19, from, temporal_relation_type
					// token labels: from, to
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleTokenStream stream_from=new RewriteRuleTokenStream(adaptor,"token from",from);
					RewriteRuleTokenStream stream_to=new RewriteRuleTokenStream(adaptor,"token to",to);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 182:83: -> ^( ';' temporal_relation_type $to ( $from)? )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:182:85: ^( ';' temporal_relation_type $to ( $from)? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new DDLTemporalRelation(stream_19.nextToken()), root_1);
						adaptor.addChild(root_1, stream_temporal_relation_type.nextTree());
						adaptor.addChild(root_1, stream_to.nextNode());
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:182:140: ( $from)?
						if ( stream_from.hasNext() ) {
							adaptor.addChild(root_1, stream_from.nextNode());
						}
						stream_from.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:182:149: parameter_constraint ';'
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_parameter_constraint_in_problem_element2179);
					parameter_constraint348=parameter_constraint();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, parameter_constraint348.getTree());

					char_literal349=(Token)match(input,19,FOLLOW_19_in_problem_element2181); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal349_tree = (Object)adaptor.create(char_literal349);
					adaptor.addChild(root_0, char_literal349_tree);
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 45, problem_element_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "problem_element"


	public static class range_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "range"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:184:1: range : '[' positive_number ',' positive_number ']' -> ^( '[' positive_number positive_number ) ;
	public final ddl3Parser.range_return range() throws RecognitionException {
		ddl3Parser.range_return retval = new ddl3Parser.range_return();
		retval.start = input.LT(1);
		int range_StartIndex = input.index();

		Object root_0 = null;

		Token char_literal350=null;
		Token char_literal352=null;
		Token char_literal354=null;
		ParserRuleReturnScope positive_number351 =null;
		ParserRuleReturnScope positive_number353 =null;

		Object char_literal350_tree=null;
		Object char_literal352_tree=null;
		Object char_literal354_tree=null;
		RewriteRuleTokenStream stream_77=new RewriteRuleTokenStream(adaptor,"token 77");
		RewriteRuleTokenStream stream_15=new RewriteRuleTokenStream(adaptor,"token 15");
		RewriteRuleTokenStream stream_76=new RewriteRuleTokenStream(adaptor,"token 76");
		RewriteRuleSubtreeStream stream_positive_number=new RewriteRuleSubtreeStream(adaptor,"rule positive_number");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 46) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:184:7: ( '[' positive_number ',' positive_number ']' -> ^( '[' positive_number positive_number ) )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:184:9: '[' positive_number ',' positive_number ']'
			{
			char_literal350=(Token)match(input,76,FOLLOW_76_in_range2189); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_76.add(char_literal350);

			pushFollow(FOLLOW_positive_number_in_range2191);
			positive_number351=positive_number();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_positive_number.add(positive_number351.getTree());
			char_literal352=(Token)match(input,15,FOLLOW_15_in_range2193); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_15.add(char_literal352);

			pushFollow(FOLLOW_positive_number_in_range2195);
			positive_number353=positive_number();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_positive_number.add(positive_number353.getTree());
			char_literal354=(Token)match(input,77,FOLLOW_77_in_range2197); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_77.add(char_literal354);

			// AST REWRITE
			// elements: 76, positive_number, positive_number
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 184:53: -> ^( '[' positive_number positive_number )
			{
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:184:55: ^( '[' positive_number positive_number )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new DDLRange(stream_76.nextToken()), root_1);
				adaptor.addChild(root_1, stream_positive_number.nextTree());
				adaptor.addChild(root_1, stream_positive_number.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 46, range_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "range"


	public static class positive_number_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "positive_number"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:186:1: positive_number : ( ( '+' )? INT -> ^( INT ) | ( '+' )? 'INF' -> ^( 'INF' ) );
	public final ddl3Parser.positive_number_return positive_number() throws RecognitionException {
		ddl3Parser.positive_number_return retval = new ddl3Parser.positive_number_return();
		retval.start = input.LT(1);
		int positive_number_StartIndex = input.index();

		Object root_0 = null;

		Token char_literal355=null;
		Token INT356=null;
		Token char_literal357=null;
		Token string_literal358=null;

		Object char_literal355_tree=null;
		Object INT356_tree=null;
		Object char_literal357_tree=null;
		Object string_literal358_tree=null;
		RewriteRuleTokenStream stream_14=new RewriteRuleTokenStream(adaptor,"token 14");
		RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
		RewriteRuleTokenStream stream_54=new RewriteRuleTokenStream(adaptor,"token 54");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 47) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:187:2: ( ( '+' )? INT -> ^( INT ) | ( '+' )? 'INF' -> ^( 'INF' ) )
			int alt64=2;
			switch ( input.LA(1) ) {
			case 14:
				{
				int LA64_1 = input.LA(2);
				if ( (LA64_1==INT) ) {
					alt64=1;
				}
				else if ( (LA64_1==54) ) {
					alt64=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case INT:
				{
				alt64=1;
				}
				break;
			case 54:
				{
				alt64=2;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 64, 0, input);
				throw nvae;
			}
			switch (alt64) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:187:4: ( '+' )? INT
					{
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:187:4: ( '+' )?
					int alt62=2;
					int LA62_0 = input.LA(1);
					if ( (LA62_0==14) ) {
						alt62=1;
					}
					switch (alt62) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:187:5: '+'
							{
							char_literal355=(Token)match(input,14,FOLLOW_14_in_positive_number2219); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_14.add(char_literal355);

							}
							break;

					}

					INT356=(Token)match(input,INT,FOLLOW_INT_in_positive_number2223); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_INT.add(INT356);

					// AST REWRITE
					// elements: INT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 187:15: -> ^( INT )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:187:17: ^( INT )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(stream_INT.nextNode(), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:187:26: ( '+' )? 'INF'
					{
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:187:26: ( '+' )?
					int alt63=2;
					int LA63_0 = input.LA(1);
					if ( (LA63_0==14) ) {
						alt63=1;
					}
					switch (alt63) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:187:27: '+'
							{
							char_literal357=(Token)match(input,14,FOLLOW_14_in_positive_number2233); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_14.add(char_literal357);

							}
							break;

					}

					string_literal358=(Token)match(input,54,FOLLOW_54_in_positive_number2237); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_54.add(string_literal358);

					// AST REWRITE
					// elements: 54
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 187:39: -> ^( 'INF' )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:187:41: ^( 'INF' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(stream_54.nextNode(), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 47, positive_number_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "positive_number"


	public static class number_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "number"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:189:1: number : ( ( sign )? INT -> ^( INT ( sign )? ) | ( sign )? 'INF' -> ^( 'INF' ( sign )? ) );
	public final ddl3Parser.number_return number() throws RecognitionException {
		ddl3Parser.number_return retval = new ddl3Parser.number_return();
		retval.start = input.LT(1);
		int number_StartIndex = input.index();

		Object root_0 = null;

		Token INT360=null;
		Token string_literal362=null;
		ParserRuleReturnScope sign359 =null;
		ParserRuleReturnScope sign361 =null;

		Object INT360_tree=null;
		Object string_literal362_tree=null;
		RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
		RewriteRuleTokenStream stream_54=new RewriteRuleTokenStream(adaptor,"token 54");
		RewriteRuleSubtreeStream stream_sign=new RewriteRuleSubtreeStream(adaptor,"rule sign");

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 48) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:189:8: ( ( sign )? INT -> ^( INT ( sign )? ) | ( sign )? 'INF' -> ^( 'INF' ( sign )? ) )
			int alt67=2;
			switch ( input.LA(1) ) {
			case 14:
			case 16:
				{
				int LA67_1 = input.LA(2);
				if ( (LA67_1==INT) ) {
					alt67=1;
				}
				else if ( (LA67_1==54) ) {
					alt67=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 67, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case INT:
				{
				alt67=1;
				}
				break;
			case 54:
				{
				alt67=2;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 67, 0, input);
				throw nvae;
			}
			switch (alt67) {
				case 1 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:189:10: ( sign )? INT
					{
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:189:10: ( sign )?
					int alt65=2;
					int LA65_0 = input.LA(1);
					if ( (LA65_0==14||LA65_0==16) ) {
						alt65=1;
					}
					switch (alt65) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:189:11: sign
							{
							pushFollow(FOLLOW_sign_in_number2251);
							sign359=sign();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_sign.add(sign359.getTree());
							}
							break;

					}

					INT360=(Token)match(input,INT,FOLLOW_INT_in_number2255); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_INT.add(INT360);

					// AST REWRITE
					// elements: INT, sign
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 189:22: -> ^( INT ( sign )? )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:189:24: ^( INT ( sign )? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(stream_INT.nextNode(), root_1);
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:189:30: ( sign )?
						if ( stream_sign.hasNext() ) {
							adaptor.addChild(root_1, stream_sign.nextTree());
						}
						stream_sign.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:189:41: ( sign )? 'INF'
					{
					// /home/alessandro/opt/antlr/ddl3/ddl3.g:189:41: ( sign )?
					int alt66=2;
					int LA66_0 = input.LA(1);
					if ( (LA66_0==14||LA66_0==16) ) {
						alt66=1;
					}
					switch (alt66) {
						case 1 :
							// /home/alessandro/opt/antlr/ddl3/ddl3.g:189:42: sign
							{
							pushFollow(FOLLOW_sign_in_number2270);
							sign361=sign();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_sign.add(sign361.getTree());
							}
							break;

					}

					string_literal362=(Token)match(input,54,FOLLOW_54_in_number2274); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_54.add(string_literal362);

					// AST REWRITE
					// elements: 54, sign
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 189:55: -> ^( 'INF' ( sign )? )
					{
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:189:57: ^( 'INF' ( sign )? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(stream_54.nextNode(), root_1);
						// /home/alessandro/opt/antlr/ddl3/ddl3.g:189:65: ( sign )?
						if ( stream_sign.hasNext() ) {
							adaptor.addChild(root_1, stream_sign.nextTree());
						}
						stream_sign.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 48, number_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "number"


	public static class sign_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "sign"
	// /home/alessandro/opt/antlr/ddl3/ddl3.g:191:1: sign : ( '+' | '-' );
	public final ddl3Parser.sign_return sign() throws RecognitionException {
		ddl3Parser.sign_return retval = new ddl3Parser.sign_return();
		retval.start = input.LT(1);
		int sign_StartIndex = input.index();

		Object root_0 = null;

		Token set363=null;

		Object set363_tree=null;

		try {
			if ( state.backtracking>0 && alreadyParsedRule(input, 49) ) { return retval; }

			// /home/alessandro/opt/antlr/ddl3/ddl3.g:191:6: ( '+' | '-' )
			// /home/alessandro/opt/antlr/ddl3/ddl3.g:
			{
			root_0 = (Object)adaptor.nil();


			set363=input.LT(1);
			if ( input.LA(1)==14||input.LA(1)==16 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set363));
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			if ( state.backtracking>0 ) { memoize(input, 49, sign_StartIndex); }

		}
		return retval;
	}
	// $ANTLR end "sign"

	// $ANTLR start synpred35_ddl3
	public final void synpred35_ddl3_fragment() throws RecognitionException {
		// /home/alessandro/opt/antlr/ddl3/ddl3.g:62:4: ( numeric_parameter_constraint )
		// /home/alessandro/opt/antlr/ddl3/ddl3.g:62:4: numeric_parameter_constraint
		{
		pushFollow(FOLLOW_numeric_parameter_constraint_in_synpred35_ddl3708);
		numeric_parameter_constraint();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred35_ddl3

	// $ANTLR start synpred77_ddl3
	public final void synpred77_ddl3_fragment() throws RecognitionException {
		// /home/alessandro/opt/antlr/ddl3/ddl3.g:147:4: ( simple_ground_state_variable_component_decision )
		// /home/alessandro/opt/antlr/ddl3/ddl3.g:147:4: simple_ground_state_variable_component_decision
		{
		pushFollow(FOLLOW_simple_ground_state_variable_component_decision_in_synpred77_ddl31639);
		simple_ground_state_variable_component_decision();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred77_ddl3

	// $ANTLR start synpred78_ddl3
	public final void synpred78_ddl3_fragment() throws RecognitionException {
		// /home/alessandro/opt/antlr/ddl3/ddl3.g:147:54: ( singleton_state_variable_component_decision )
		// /home/alessandro/opt/antlr/ddl3/ddl3.g:147:54: singleton_state_variable_component_decision
		{
		pushFollow(FOLLOW_singleton_state_variable_component_decision_in_synpred78_ddl31643);
		singleton_state_variable_component_decision();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred78_ddl3

	// $ANTLR start synpred101_ddl3
	public final void synpred101_ddl3_fragment() throws RecognitionException {
		// /home/alessandro/opt/antlr/ddl3/ddl3.g:174:4: ( VarID ( '=' number )? )
		// /home/alessandro/opt/antlr/ddl3/ddl3.g:174:4: VarID ( '=' number )?
		{
		match(input,VarID,FOLLOW_VarID_in_synpred101_ddl32010); if (state.failed) return;

		// /home/alessandro/opt/antlr/ddl3/ddl3.g:174:10: ( '=' number )?
		int alt88=2;
		int LA88_0 = input.LA(1);
		if ( (LA88_0==22) ) {
			alt88=1;
		}
		switch (alt88) {
			case 1 :
				// /home/alessandro/opt/antlr/ddl3/ddl3.g:174:11: '=' number
				{
				match(input,22,FOLLOW_22_in_synpred101_ddl32013); if (state.failed) return;

				pushFollow(FOLLOW_number_in_synpred101_ddl32015);
				number();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred101_ddl3

	// Delegated rules

	public final boolean synpred78_ddl3() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred78_ddl3_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred77_ddl3() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred77_ddl3_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred35_ddl3() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred35_ddl3_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred101_ddl3() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred101_ddl3_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}


	protected DFA38 dfa38 = new DFA38(this);
	protected DFA50 dfa50 = new DFA50(this);
	static final String DFA38_eotS =
		"\15\uffff";
	static final String DFA38_eofS =
		"\15\uffff";
	static final String DFA38_minS =
		"\2\5\1\13\2\uffff\1\17\1\7\2\5\1\0\1\uffff\1\17\1\uffff";
	static final String DFA38_maxS =
		"\1\77\1\117\1\13\2\uffff\1\27\1\14\1\117\1\77\1\0\1\uffff\1\27\1\uffff";
	static final String DFA38_acceptS =
		"\3\uffff\1\3\1\4\5\uffff\1\2\1\uffff\1\1";
	static final String DFA38_specialS =
		"\11\uffff\1\0\3\uffff}>";
	static final String[] DFA38_transitionS = {
			"\1\2\16\uffff\1\1\17\uffff\1\4\31\uffff\1\4\1\3",
			"\1\5\3\uffff\1\5\17\uffff\1\5\64\uffff\2\5",
			"\1\6",
			"",
			"",
			"\1\7\7\uffff\1\10",
			"\1\12\4\uffff\1\11",
			"\1\13\3\uffff\1\13\17\uffff\1\13\64\uffff\2\13",
			"\1\2\36\uffff\1\4\31\uffff\1\4\1\3",
			"\1\uffff",
			"",
			"\1\7\7\uffff\1\10",
			""
	};

	static final short[] DFA38_eot = DFA.unpackEncodedString(DFA38_eotS);
	static final short[] DFA38_eof = DFA.unpackEncodedString(DFA38_eofS);
	static final char[] DFA38_min = DFA.unpackEncodedStringToUnsignedChars(DFA38_minS);
	static final char[] DFA38_max = DFA.unpackEncodedStringToUnsignedChars(DFA38_maxS);
	static final short[] DFA38_accept = DFA.unpackEncodedString(DFA38_acceptS);
	static final short[] DFA38_special = DFA.unpackEncodedString(DFA38_specialS);
	static final short[][] DFA38_transition;

	static {
		int numStates = DFA38_transitionS.length;
		DFA38_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA38_transition[i] = DFA.unpackEncodedString(DFA38_transitionS[i]);
		}
	}

	protected class DFA38 extends DFA {

		public DFA38(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 38;
			this.eot = DFA38_eot;
			this.eof = DFA38_eof;
			this.min = DFA38_min;
			this.max = DFA38_max;
			this.accept = DFA38_accept;
			this.special = DFA38_special;
			this.transition = DFA38_transition;
		}
		@Override
		public String getDescription() {
			return "146:1: component_decision : ( simple_ground_state_variable_component_decision | singleton_state_variable_component_decision | renewable_resource_component_decision | consumable_resource_component_decision );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			TokenStream input = (TokenStream)_input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA38_9 = input.LA(1);
						 
						int index38_9 = input.index();
						input.rewind();
						s = -1;
						if ( (synpred77_ddl3()) ) {s = 12;}
						else if ( (synpred78_ddl3()) ) {s = 10;}
						 
						input.seek(index38_9);
						if ( s>=0 ) return s;
						break;
			}
			if (state.backtracking>0) {state.failed=true; return -1;}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 38, _s, input);
			error(nvae);
			throw nvae;
		}
	}

	static final String DFA50_eotS =
		"\10\uffff";
	static final String DFA50_eofS =
		"\10\uffff";
	static final String DFA50_minS =
		"\1\24\1\5\2\uffff\1\17\1\5\1\44\1\17";
	static final String DFA50_maxS =
		"\1\76\1\117\2\uffff\1\27\1\117\1\76\1\27";
	static final String DFA50_acceptS =
		"\2\uffff\1\1\1\2\4\uffff";
	static final String DFA50_specialS =
		"\10\uffff}>";
	static final String[] DFA50_transitionS = {
			"\1\1\17\uffff\1\3\31\uffff\1\2",
			"\1\4\3\uffff\1\4\17\uffff\1\4\64\uffff\2\4",
			"",
			"",
			"\1\5\7\uffff\1\6",
			"\1\7\3\uffff\1\7\17\uffff\1\7\64\uffff\2\7",
			"\1\3\31\uffff\1\2",
			"\1\5\7\uffff\1\6"
	};

	static final short[] DFA50_eot = DFA.unpackEncodedString(DFA50_eotS);
	static final short[] DFA50_eof = DFA.unpackEncodedString(DFA50_eofS);
	static final char[] DFA50_min = DFA.unpackEncodedStringToUnsignedChars(DFA50_minS);
	static final char[] DFA50_max = DFA.unpackEncodedStringToUnsignedChars(DFA50_maxS);
	static final short[] DFA50_accept = DFA.unpackEncodedString(DFA50_acceptS);
	static final short[] DFA50_special = DFA.unpackEncodedString(DFA50_specialS);
	static final short[][] DFA50_transition;

	static {
		int numStates = DFA50_transitionS.length;
		DFA50_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA50_transition[i] = DFA.unpackEncodedString(DFA50_transitionS[i]);
		}
	}

	protected class DFA50 extends DFA {

		public DFA50(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 50;
			this.eot = DFA50_eot;
			this.eof = DFA50_eof;
			this.min = DFA50_min;
			this.max = DFA50_max;
			this.accept = DFA50_accept;
			this.special = DFA50_special;
			this.transition = DFA50_transition;
		}
		@Override
		public String getDescription() {
			return "164:1: consumable_resource_component_decision : ( consumable_resource_production_component_decision | consumable_resource_consumption_component_decision );";
		}
	}

	public static final BitSet FOLLOW_domain_in_ddl42 = new BitSet(new long[]{0x2000000000000002L});
	public static final BitSet FOLLOW_problem_in_ddl45 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_problem_in_ddl63 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_41_in_domain80 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_domain82 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
	public static final BitSet FOLLOW_80_in_domain84 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
	public static final BitSet FOLLOW_temporal_module_in_domain86 = new BitSet(new long[]{0x1000000C00000000L,0x0000000000020080L});
	public static final BitSet FOLLOW_domain_element_in_domain88 = new BitSet(new long[]{0x1000000C00000000L,0x0000000000020080L});
	public static final BitSet FOLLOW_81_in_domain91 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_74_in_temporal_module113 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_temporal_module115 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_22_in_temporal_module117 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_module119 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_15_in_temporal_module121 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_INT_in_temporal_module123 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_temporal_module125 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_parameter_type_in_domain_element146 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_component_type_in_domain_element150 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_component_in_domain_element154 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_timeline_synchronization_in_domain_element158 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_parameter_type_in_parameter_type168 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enumeration_parameter_type_in_parameter_type172 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_60_in_numeric_parameter_type182 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_57_in_numeric_parameter_type184 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_numeric_parameter_type186 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_22_in_numeric_parameter_type188 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_76_in_numeric_parameter_type190 = new BitSet(new long[]{0x0040000000014040L});
	public static final BitSet FOLLOW_number_in_numeric_parameter_type192 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_15_in_numeric_parameter_type194 = new BitSet(new long[]{0x0040000000014040L});
	public static final BitSet FOLLOW_number_in_numeric_parameter_type196 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_77_in_numeric_parameter_type198 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_numeric_parameter_type200 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_60_in_enumeration_parameter_type222 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_50_in_enumeration_parameter_type224 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_enumeration_parameter_type226 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_22_in_enumeration_parameter_type228 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
	public static final BitSet FOLLOW_80_in_enumeration_parameter_type230 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_enumeration_parameter_type232 = new BitSet(new long[]{0x0000000000008000L,0x0000000000020000L});
	public static final BitSet FOLLOW_15_in_enumeration_parameter_type235 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_enumeration_parameter_type237 = new BitSet(new long[]{0x0000000000008000L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_enumeration_parameter_type241 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_enumeration_parameter_type243 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_55_in_temporal_relation_type264 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_56_in_temporal_relation_type277 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_31_in_temporal_relation_type290 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type292 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_26_in_temporal_relation_type307 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type309 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_47_in_temporal_relation_type324 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_68_in_temporal_relation_type337 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_67_in_temporal_relation_type350 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_52_in_temporal_relation_type363 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_51_in_temporal_relation_type376 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_42_in_temporal_relation_type389 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type391 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type393 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_37_in_temporal_relation_type410 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type412 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type414 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_59_in_temporal_relation_type431 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type433 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_58_in_temporal_relation_type448 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type450 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_69_in_temporal_relation_type465 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_45_in_temporal_relation_type478 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_30_in_temporal_relation_type491 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_29_in_temporal_relation_type504 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_32_in_temporal_relation_type517 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type519 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_27_in_temporal_relation_type534 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type536 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_66_in_temporal_relation_type551 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type553 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_65_in_temporal_relation_type568 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type570 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_44_in_temporal_relation_type585 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type587 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_43_in_temporal_relation_type602 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type604 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_39_in_temporal_relation_type619 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type621 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type623 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_38_in_temporal_relation_type640 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type642 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type644 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_70_in_temporal_relation_type661 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type663 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type665 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_46_in_temporal_relation_type682 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type684 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_temporal_relation_type686 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_parameter_constraint_in_parameter_constraint708 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enumeration_parameter_constraint_in_parameter_constraint712 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_comparison_lvalue_in_numeric_parameter_constraint722 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_22_in_numeric_parameter_constraint724 = new BitSet(new long[]{0x00400000000140C0L});
	public static final BitSet FOLLOW_numeric_comparison_rvalue_in_numeric_parameter_constraint726 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_comparison_lvalue_in_numeric_parameter_constraint743 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_20_in_numeric_parameter_constraint745 = new BitSet(new long[]{0x00400000000140C0L});
	public static final BitSet FOLLOW_numeric_comparison_rvalue_in_numeric_parameter_constraint747 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_comparison_lvalue_in_numeric_parameter_constraint764 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_23_in_numeric_parameter_constraint766 = new BitSet(new long[]{0x00400000000140C0L});
	public static final BitSet FOLLOW_numeric_comparison_rvalue_in_numeric_parameter_constraint768 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_comparison_lvalue_in_numeric_parameter_constraint785 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_21_in_numeric_parameter_constraint787 = new BitSet(new long[]{0x00400000000140C0L});
	public static final BitSet FOLLOW_numeric_comparison_rvalue_in_numeric_parameter_constraint789 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_comparison_lvalue_in_numeric_parameter_constraint806 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_24_in_numeric_parameter_constraint808 = new BitSet(new long[]{0x00400000000140C0L});
	public static final BitSet FOLLOW_numeric_comparison_rvalue_in_numeric_parameter_constraint810 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_comparison_lvalue_in_numeric_parameter_constraint827 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_10_in_numeric_parameter_constraint829 = new BitSet(new long[]{0x00400000000140C0L});
	public static final BitSet FOLLOW_numeric_comparison_rvalue_in_numeric_parameter_constraint831 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_VarID_in_numeric_comparison_lvalue852 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_number_in_numeric_comparison_lvalue856 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_13_in_numeric_comparison_lvalue858 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_VarID_in_numeric_comparison_lvalue861 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_first_numeric_comparison_rvalue_in_numeric_comparison_rvalue870 = new BitSet(new long[]{0x0000000000014002L});
	public static final BitSet FOLLOW_other_numeric_comparison_rvalues_in_numeric_comparison_rvalue873 = new BitSet(new long[]{0x0000000000014002L});
	public static final BitSet FOLLOW_VarID_in_first_numeric_comparison_rvalue884 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_number_in_first_numeric_comparison_rvalue888 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_13_in_first_numeric_comparison_rvalue890 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_VarID_in_first_numeric_comparison_rvalue893 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_number_in_first_numeric_comparison_rvalue897 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_other_numeric_comparison_rvalues906 = new BitSet(new long[]{0x00000000000000C0L});
	public static final BitSet FOLLOW_numeric_comparison_value_in_other_numeric_comparison_rvalues915 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_VarID_in_numeric_comparison_value924 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_numeric_comparison_value928 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_13_in_numeric_comparison_value930 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_VarID_in_numeric_comparison_value933 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_numeric_comparison_value937 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_VarID_in_enumeration_parameter_constraint946 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_22_in_enumeration_parameter_constraint948 = new BitSet(new long[]{0x00000000000000A0L});
	public static final BitSet FOLLOW_enumeration_comparison_rvalue_in_enumeration_parameter_constraint950 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_VarID_in_enumeration_parameter_constraint966 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_10_in_enumeration_parameter_constraint968 = new BitSet(new long[]{0x00000000000000A0L});
	public static final BitSet FOLLOW_enumeration_comparison_rvalue_in_enumeration_parameter_constraint970 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_ground_state_variable_component_type_in_component_type1006 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_singleton_state_variable_component_type_in_component_type1010 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_renewable_resource_component_type_in_component_type1014 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_consumable_resource_component_type_in_component_type1018 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_35_in_simple_ground_state_variable_component_type1028 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
	public static final BitSet FOLLOW_72_in_simple_ground_state_variable_component_type1030 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_simple_ground_state_variable_component_type1032 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_11_in_simple_ground_state_variable_component_type1034 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_simple_ground_state_variable_component_decision_type_in_simple_ground_state_variable_component_type1036 = new BitSet(new long[]{0x0000000000009000L});
	public static final BitSet FOLLOW_15_in_simple_ground_state_variable_component_type1039 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_simple_ground_state_variable_component_decision_type_in_simple_ground_state_variable_component_type1041 = new BitSet(new long[]{0x0000000000009000L});
	public static final BitSet FOLLOW_12_in_simple_ground_state_variable_component_type1045 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
	public static final BitSet FOLLOW_80_in_simple_ground_state_variable_component_type1047 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020800L});
	public static final BitSet FOLLOW_simple_ground_state_variable_transition_constraint_in_simple_ground_state_variable_component_type1050 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020800L});
	public static final BitSet FOLLOW_81_in_simple_ground_state_variable_component_type1054 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_simple_ground_state_variable_component_decision_type1081 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_11_in_simple_ground_state_variable_component_decision_type1083 = new BitSet(new long[]{0x0000000000001000L});
	public static final BitSet FOLLOW_12_in_simple_ground_state_variable_component_decision_type1085 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_75_in_simple_ground_state_variable_transition_constraint1102 = new BitSet(new long[]{0x0000000000100020L});
	public static final BitSet FOLLOW_simple_ground_state_variable_component_decision_in_simple_ground_state_variable_transition_constraint1104 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_simple_ground_state_variable_transition_constraint1106 = new BitSet(new long[]{0x0080000000000002L});
	public static final BitSet FOLLOW_55_in_simple_ground_state_variable_transition_constraint1109 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
	public static final BitSet FOLLOW_80_in_simple_ground_state_variable_transition_constraint1111 = new BitSet(new long[]{0x0000000000100020L,0x0000000000020000L});
	public static final BitSet FOLLOW_simple_ground_state_variable_transition_element_in_simple_ground_state_variable_transition_constraint1114 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_simple_ground_state_variable_transition_constraint1116 = new BitSet(new long[]{0x0000000000100020L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_simple_ground_state_variable_transition_constraint1120 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_ground_state_variable_component_decision_in_simple_ground_state_variable_transition_element1148 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_35_in_singleton_state_variable_component_type1158 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
	public static final BitSet FOLLOW_73_in_singleton_state_variable_component_type1160 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_singleton_state_variable_component_type1162 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_11_in_singleton_state_variable_component_type1164 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_singleton_state_variable_component_decision_type_in_singleton_state_variable_component_type1166 = new BitSet(new long[]{0x0000000000009000L});
	public static final BitSet FOLLOW_15_in_singleton_state_variable_component_type1169 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_singleton_state_variable_component_decision_type_in_singleton_state_variable_component_type1171 = new BitSet(new long[]{0x0000000000009000L});
	public static final BitSet FOLLOW_12_in_singleton_state_variable_component_type1175 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
	public static final BitSet FOLLOW_80_in_singleton_state_variable_component_type1177 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020800L});
	public static final BitSet FOLLOW_singleton_state_variable_transition_constraint_in_singleton_state_variable_component_type1180 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020800L});
	public static final BitSet FOLLOW_81_in_singleton_state_variable_component_type1184 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_singleton_state_variable_component_decision_type1211 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_11_in_singleton_state_variable_component_decision_type1213 = new BitSet(new long[]{0x0000000000001020L});
	public static final BitSet FOLLOW_ID_in_singleton_state_variable_component_decision_type1216 = new BitSet(new long[]{0x0000000000009000L});
	public static final BitSet FOLLOW_15_in_singleton_state_variable_component_decision_type1219 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_singleton_state_variable_component_decision_type1221 = new BitSet(new long[]{0x0000000000009000L});
	public static final BitSet FOLLOW_12_in_singleton_state_variable_component_decision_type1227 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_75_in_singleton_state_variable_transition_constraint1247 = new BitSet(new long[]{0x0000000000100020L});
	public static final BitSet FOLLOW_singleton_state_variable_component_decision_in_singleton_state_variable_transition_constraint1249 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_singleton_state_variable_transition_constraint1251 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_55_in_singleton_state_variable_transition_constraint1253 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
	public static final BitSet FOLLOW_80_in_singleton_state_variable_transition_constraint1255 = new BitSet(new long[]{0x00400000001140E0L,0x0000000000020000L});
	public static final BitSet FOLLOW_singleton_state_variable_transition_element_in_singleton_state_variable_transition_constraint1258 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_singleton_state_variable_transition_constraint1260 = new BitSet(new long[]{0x00400000001140E0L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_singleton_state_variable_transition_constraint1264 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_singleton_state_variable_component_decision_in_singleton_state_variable_transition_element1290 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_parameter_constraint_in_singleton_state_variable_transition_element1294 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_35_in_renewable_resource_component_type1304 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_64_in_renewable_resource_component_type1306 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_renewable_resource_component_type1308 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_11_in_renewable_resource_component_type1310 = new BitSet(new long[]{0x0040000000004040L});
	public static final BitSet FOLLOW_positive_number_in_renewable_resource_component_type1312 = new BitSet(new long[]{0x0000000000001000L});
	public static final BitSet FOLLOW_12_in_renewable_resource_component_type1314 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_35_in_consumable_resource_component_type1334 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_40_in_consumable_resource_component_type1336 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_consumable_resource_component_type1338 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_11_in_consumable_resource_component_type1340 = new BitSet(new long[]{0x0040000000004040L});
	public static final BitSet FOLLOW_positive_number_in_consumable_resource_component_type1342 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_15_in_consumable_resource_component_type1344 = new BitSet(new long[]{0x0040000000004040L});
	public static final BitSet FOLLOW_positive_number_in_consumable_resource_component_type1346 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_15_in_consumable_resource_component_type1348 = new BitSet(new long[]{0x0040000000004040L});
	public static final BitSet FOLLOW_positive_number_in_consumable_resource_component_type1350 = new BitSet(new long[]{0x0000000000001000L});
	public static final BitSet FOLLOW_12_in_consumable_resource_component_type1352 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_34_in_component1376 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_component1378 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
	public static final BitSet FOLLOW_80_in_component1380 = new BitSet(new long[]{0x0023000200000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_timeline_in_component1383 = new BitSet(new long[]{0x0023000200000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_component1387 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_18_in_component1389 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_component1391 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_component1393 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_48_in_timeline1416 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_timeline1418 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_11_in_timeline1420 = new BitSet(new long[]{0x0000000002001220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_timeline1423 = new BitSet(new long[]{0x0000000000009000L});
	public static final BitSet FOLLOW_15_in_timeline1426 = new BitSet(new long[]{0x0000000002000220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_timeline1428 = new BitSet(new long[]{0x0000000000009000L});
	public static final BitSet FOLLOW_12_in_timeline1434 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_33_in_timeline1451 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_timeline1453 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_11_in_timeline1455 = new BitSet(new long[]{0x0000000002001220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_timeline1458 = new BitSet(new long[]{0x0000000000009000L});
	public static final BitSet FOLLOW_15_in_timeline1461 = new BitSet(new long[]{0x0000000002000220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_timeline1463 = new BitSet(new long[]{0x0000000000009000L});
	public static final BitSet FOLLOW_12_in_timeline1469 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_53_in_timeline1486 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_timeline1488 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_11_in_timeline1490 = new BitSet(new long[]{0x0000000002001220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_timeline1493 = new BitSet(new long[]{0x0000000000009000L});
	public static final BitSet FOLLOW_15_in_timeline1496 = new BitSet(new long[]{0x0000000002000220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_timeline1498 = new BitSet(new long[]{0x0000000000009000L});
	public static final BitSet FOLLOW_12_in_timeline1504 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_49_in_timeline1521 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_timeline1523 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_11_in_timeline1525 = new BitSet(new long[]{0x0000000002001220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_timeline1528 = new BitSet(new long[]{0x0000000000009000L});
	public static final BitSet FOLLOW_15_in_timeline1531 = new BitSet(new long[]{0x0000000002000220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_timeline1533 = new BitSet(new long[]{0x0000000000009000L});
	public static final BitSet FOLLOW_12_in_timeline1539 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_71_in_timeline_synchronization1563 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_timeline_synchronization1565 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_17_in_timeline_synchronization1567 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_timeline_synchronization1569 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
	public static final BitSet FOLLOW_80_in_timeline_synchronization1571 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_synchronization_in_timeline_synchronization1574 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020800L});
	public static final BitSet FOLLOW_81_in_timeline_synchronization1578 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_75_in_synchronization1604 = new BitSet(new long[]{0xC000001000100020L});
	public static final BitSet FOLLOW_component_decision_in_synchronization1606 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
	public static final BitSet FOLLOW_80_in_synchronization1608 = new BitSet(new long[]{0x0DD8FCE1EC0140E0L,0x000000000002007EL});
	public static final BitSet FOLLOW_synchronization_element_in_synchronization1611 = new BitSet(new long[]{0x0DD8FCE1EC0140E0L,0x000000000002007EL});
	public static final BitSet FOLLOW_81_in_synchronization1615 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_ground_state_variable_component_decision_in_component_decision1639 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_singleton_state_variable_component_decision_in_component_decision1643 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_renewable_resource_component_decision_in_component_decision1647 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_consumable_resource_component_decision_in_component_decision1651 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_instantiated_component_decision1660 = new BitSet(new long[]{0x0000000000100020L});
	public static final BitSet FOLLOW_20_in_instantiated_component_decision1663 = new BitSet(new long[]{0x0000000002000220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_instantiated_component_decision1665 = new BitSet(new long[]{0x0000000000808000L});
	public static final BitSet FOLLOW_15_in_instantiated_component_decision1668 = new BitSet(new long[]{0x0000000002000220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_instantiated_component_decision1670 = new BitSet(new long[]{0x0000000000808000L});
	public static final BitSet FOLLOW_23_in_instantiated_component_decision1674 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_instantiated_component_decision1678 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_17_in_instantiated_component_decision1680 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_instantiated_component_decision1682 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_17_in_instantiated_component_decision1684 = new BitSet(new long[]{0xC000001000100020L});
	public static final BitSet FOLLOW_component_decision_in_instantiated_component_decision1686 = new BitSet(new long[]{0x0000000010000002L});
	public static final BitSet FOLLOW_28_in_instantiated_component_decision1689 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_instantiated_component_decision1691 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_instantiated_component_decision1693 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_range_in_instantiated_component_decision1695 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_20_in_simple_ground_state_variable_component_decision1761 = new BitSet(new long[]{0x0000000002000220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_simple_ground_state_variable_component_decision1763 = new BitSet(new long[]{0x0000000000808000L});
	public static final BitSet FOLLOW_15_in_simple_ground_state_variable_component_decision1766 = new BitSet(new long[]{0x0000000002000220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_simple_ground_state_variable_component_decision1768 = new BitSet(new long[]{0x0000000000808000L});
	public static final BitSet FOLLOW_23_in_simple_ground_state_variable_component_decision1772 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_simple_ground_state_variable_component_decision1776 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_11_in_simple_ground_state_variable_component_decision1778 = new BitSet(new long[]{0x0000000000001000L});
	public static final BitSet FOLLOW_12_in_simple_ground_state_variable_component_decision1780 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_20_in_singleton_state_variable_component_decision1802 = new BitSet(new long[]{0x0000000002000220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_singleton_state_variable_component_decision1804 = new BitSet(new long[]{0x0000000000808000L});
	public static final BitSet FOLLOW_15_in_singleton_state_variable_component_decision1807 = new BitSet(new long[]{0x0000000002000220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_singleton_state_variable_component_decision1809 = new BitSet(new long[]{0x0000000000808000L});
	public static final BitSet FOLLOW_23_in_singleton_state_variable_component_decision1813 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_singleton_state_variable_component_decision1817 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_11_in_singleton_state_variable_component_decision1819 = new BitSet(new long[]{0x0000000000001080L});
	public static final BitSet FOLLOW_par_value_in_singleton_state_variable_component_decision1822 = new BitSet(new long[]{0x0000000000009000L});
	public static final BitSet FOLLOW_15_in_singleton_state_variable_component_decision1825 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_par_value_in_singleton_state_variable_component_decision1827 = new BitSet(new long[]{0x0000000000009000L});
	public static final BitSet FOLLOW_12_in_singleton_state_variable_component_decision1833 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_20_in_renewable_resource_component_decision1860 = new BitSet(new long[]{0x0000000002000220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_renewable_resource_component_decision1862 = new BitSet(new long[]{0x0000000000808000L});
	public static final BitSet FOLLOW_15_in_renewable_resource_component_decision1865 = new BitSet(new long[]{0x0000000002000220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_renewable_resource_component_decision1867 = new BitSet(new long[]{0x0000000000808000L});
	public static final BitSet FOLLOW_23_in_renewable_resource_component_decision1871 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_63_in_renewable_resource_component_decision1875 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_11_in_renewable_resource_component_decision1877 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_par_value_in_renewable_resource_component_decision1879 = new BitSet(new long[]{0x0000000000001000L});
	public static final BitSet FOLLOW_12_in_renewable_resource_component_decision1881 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_consumable_resource_production_component_decision_in_consumable_resource_component_decision1905 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_consumable_resource_consumption_component_decision_in_consumable_resource_component_decision1909 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_20_in_consumable_resource_production_component_decision1921 = new BitSet(new long[]{0x0000000002000220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_consumable_resource_production_component_decision1923 = new BitSet(new long[]{0x0000000000808000L});
	public static final BitSet FOLLOW_15_in_consumable_resource_production_component_decision1926 = new BitSet(new long[]{0x0000000002000220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_consumable_resource_production_component_decision1928 = new BitSet(new long[]{0x0000000000808000L});
	public static final BitSet FOLLOW_23_in_consumable_resource_production_component_decision1932 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_consumable_resource_production_component_decision1936 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_11_in_consumable_resource_production_component_decision1938 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_par_value_in_consumable_resource_production_component_decision1940 = new BitSet(new long[]{0x0000000000001000L});
	public static final BitSet FOLLOW_12_in_consumable_resource_production_component_decision1942 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_20_in_consumable_resource_consumption_component_decision1967 = new BitSet(new long[]{0x0000000002000220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_consumable_resource_consumption_component_decision1969 = new BitSet(new long[]{0x0000000000808000L});
	public static final BitSet FOLLOW_15_in_consumable_resource_consumption_component_decision1972 = new BitSet(new long[]{0x0000000002000220L,0x000000000000C000L});
	public static final BitSet FOLLOW_parameter_in_consumable_resource_consumption_component_decision1974 = new BitSet(new long[]{0x0000000000808000L});
	public static final BitSet FOLLOW_23_in_consumable_resource_consumption_component_decision1978 = new BitSet(new long[]{0x0000001000000000L});
	public static final BitSet FOLLOW_36_in_consumable_resource_consumption_component_decision1982 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_11_in_consumable_resource_consumption_component_decision1984 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_par_value_in_consumable_resource_consumption_component_decision1986 = new BitSet(new long[]{0x0000000000001000L});
	public static final BitSet FOLLOW_12_in_consumable_resource_consumption_component_decision1988 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_VarID_in_par_value2010 = new BitSet(new long[]{0x0000000000400002L});
	public static final BitSet FOLLOW_22_in_par_value2013 = new BitSet(new long[]{0x0040000000014040L});
	public static final BitSet FOLLOW_number_in_par_value2015 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_VarID_in_par_value2029 = new BitSet(new long[]{0x0000000000400002L});
	public static final BitSet FOLLOW_22_in_par_value2032 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_par_value2034 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_instantiated_component_decision_in_synchronization_element2053 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_synchronization_element2055 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_parameter_constraint_in_synchronization_element2059 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_synchronization_element2061 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_synchronization_element2068 = new BitSet(new long[]{0x0D98FCE1EC000000L,0x000000000000007EL});
	public static final BitSet FOLLOW_temporal_relation_type_in_synchronization_element2072 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_synchronization_element2076 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_synchronization_element2078 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_61_in_problem2103 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_problem2105 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_11_in_problem2107 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_41_in_problem2109 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_problem2111 = new BitSet(new long[]{0x0000000000001000L});
	public static final BitSet FOLLOW_12_in_problem2113 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
	public static final BitSet FOLLOW_80_in_problem2115 = new BitSet(new long[]{0x00400000000140E0L,0x0000000000020000L});
	public static final BitSet FOLLOW_problem_element_in_problem2117 = new BitSet(new long[]{0x00400000000140E0L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_problem2120 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_instantiated_component_decision_in_problem_element2142 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_problem_element2144 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_problem_element2150 = new BitSet(new long[]{0x0D98FCE1EC000000L,0x000000000000007EL});
	public static final BitSet FOLLOW_temporal_relation_type_in_problem_element2152 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ID_in_problem_element2156 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_problem_element2158 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_parameter_constraint_in_problem_element2179 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_problem_element2181 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_76_in_range2189 = new BitSet(new long[]{0x0040000000004040L});
	public static final BitSet FOLLOW_positive_number_in_range2191 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_15_in_range2193 = new BitSet(new long[]{0x0040000000004040L});
	public static final BitSet FOLLOW_positive_number_in_range2195 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_77_in_range2197 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_14_in_positive_number2219 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_INT_in_positive_number2223 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_14_in_positive_number2233 = new BitSet(new long[]{0x0040000000000000L});
	public static final BitSet FOLLOW_54_in_positive_number2237 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sign_in_number2251 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_INT_in_number2255 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sign_in_number2270 = new BitSet(new long[]{0x0040000000000000L});
	public static final BitSet FOLLOW_54_in_number2274 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_parameter_constraint_in_synpred35_ddl3708 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_ground_state_variable_component_decision_in_synpred77_ddl31639 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_singleton_state_variable_component_decision_in_synpred78_ddl31643 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_VarID_in_synpred101_ddl32010 = new BitSet(new long[]{0x0000000000400002L});
	public static final BitSet FOLLOW_22_in_synpred101_ddl32013 = new BitSet(new long[]{0x0040000000014040L});
	public static final BitSet FOLLOW_number_in_synpred101_ddl32015 = new BitSet(new long[]{0x0000000000000002L});
}
