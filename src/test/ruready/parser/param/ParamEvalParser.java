package test.ruready.parser.param;

/**
 * This class provides a parser that replaces parameter-dependent logical and mathematical
 * expressions with symbols by their numerical value. It uses MathParser to evaluation the
 * expression. The symbols must be assigned numerical values.
 * 
 * <pre>
 *                paramStr       = element*;
 *                element        = '#' controlledExpr '#' | Word | Num | '-' | '.';
 *         	   controlledExpr = (see ArithmeticParser rules)
 * </pre>
 * 
 * Word can contain anything except '#', '&' and '&tilde;', and can start with anything
 * except the usual number state tokens, '#' and '&tilde;'. An exception is that if a
 * sequence "&#" is found (as in "&#8730;"), it is regarded as a symbol to allow for HTML
 * number-encoded symbols to be correctly parsed.
 * <p>
 * controlExpr is identical to the <code>parser()</code> method in
 * <code>ArithmeticParser</code>.
 * <p>
 * These rules recognize that an expression must appear within '#' quotes. Symbol must not
 * be '#'. This is taken care of by the tokenizer.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p>
 * This file is part of the RUReady Program software.<br>
 * Contact: Nava L. Livne <code>&lt;nlivne@aoce.utah.edu&gt;</code><br>
 * Academic Outreach and Continuing Education (AOCE)<br>
 * 1901 East South Campus Dr., Room 2197-E<br>
 * University of Utah, Salt Lake City, UT 84112-9399<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Oct 10, 2007
 */
public class ParamEvalParser
{
	// TODO: re-factor the code below to parametricEvaluationParser and RTs

	//
	// /**
	// * Evaluate a logical/arithmetic expressions within a string for specific
	// * symbol values. The result is a floating point number embedded in place
	// of
	// * the expressions with '#' quotes in the original string.
	// *
	// * @param s
	// * the string to evaluate.
	// * @param symbolValues
	// * map of RHS symbols to their values.
	// * @return The evaluated string.
	// * @exception MathParserException
	// * if this parser does not recognize a given sub-expression
	// * of <code>s</code> as a valid mathematical expression.
	// */
	// public static String eval(String s, HashMap<String, Value> symbolValues,
	// Mesh mesh,
	// IntegerArray orderMC, HttpSession session) throws MathParserException
	// {
	// // Initialize and customize the tokenizer
	// Tokenizer t = ParamEvalParser.tokenizer();
	// t.setString(s);
	// MathAssembly in = new MathAssembly(t);
	//
	// ParamEvalResult sol = new ParamEvalResult(symbolValues, mesh, orderMC,
	// session);
	// in.setTarget(sol);
	// // logger.debug("########### PARSING begin ##############");
	// // logger.debug("symbolValues " + symbolValues);
	// // logger.debug("in = " + in);
	// /*
	// * int i = 0; while (in.hasMoreElements()) { Token tok =
	// * (Token)in.nextElement(); logger.debug(i+" '" +tok+"'
	// * "+tok.ttype()); i++; } in.unget(in.elementsConsumed());
	// */
	// // Parse
	// // Assembly out = start().bestMatch(in);
	// Assembly out = start().completeMatch(in);
	// // logger.debug("out = " + out);
	// if (out == null) {
	// throw new MathParserException("Improperly formed arithmetic expression");
	// }
	// // logger.out(Logger.Level.DEBUG, "out = "+out);
	//
	// // Post processing: compose the evaluated string
	// // from the assembly's stack
	// /*
	// * String snew = CommonNames.MISC.EMPTY_STRING; while (!out.stackIsEmpty()) { Object
	// a = out.pop();
	// * snew = snew + a; }
	// */
	// ParamEvalResult target = (ParamEvalResult) out.getTarget();
	// String snew = target.getEvalString();
	// // logger.debug("snew = " + snew);
	// // logger.debug("########### PARSING end ##############");
	// return snew;
	// }
	//
	// // =========== TESTING ===========
	// public static void main(String[] args) throws IOException
	// {
	// // String originalText = "&#8730;#b#&#8730;&#8730;";
	// // String originalText = "The expression can be written
	// // as\n<code>|-#a#-#b#|</code>.#b#.#b#.-";
	// String originalText = "~1~ or ~3~ &#8730;Hi 1<#a\n+b# 333 44.05
	// #@3@a^2#";
	// // Tokenizer t = new Tokenizer(">give 2receive.");
	// // Tokenizer t = new Tokenizer("~1~ Hi 1 <");
	// Tokenizer t = ParamEvalParser.tokenizer();
	// t.setString(originalText);
	// logger.debug("Original text: " + originalText);
	// Token tok = t.nextToken();
	// int i = 0;
	// while (tok.ttype() != Token.TT_EOF) {
	// logger.debug(i + ": " + tok + " " + tok.ttype());
	// tok = t.nextToken();
	// i++;
	// }
	//
	// Logger.setDefaultPrint(true);
	// ValueMap paramValues = new ValueMap();
	// paramValues.put("a", new ComplexValue(16.0));
	// paramValues.put("b", new ComplexValue(18.0));
	// IntegerArray orderMC = new IntegerArray(true, " ", "3 1 4 2");
	// logger.debug("array " + orderMC.getArray());
	// logger.debug("reverse " + orderMC.getReverse());
	// Mesh paramMesh = new Mesh(paramValues, ArithmeticMode.COMPLEX);
	// // String originalText = "~=-1~= Hello, #a+b#";
	// String evalText = null;
	// try {
	// evalText = ParamEvalParser.eval(originalText, paramValues, paramMesh,
	// orderMC, null);
	// }
	// catch (MathParserException e) {
	// logger.error("Parametric string '" + originalText
	// + "' is an illegal expression");
	// }
	// if (Logger.logging)
	// logger.debug("Original: " + originalText);
	// if (Logger.logging)
	// logger.debug("Evaluated: " + evalText);
	// }
}
