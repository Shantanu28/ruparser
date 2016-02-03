/*****************************************************************************************
 * Source File: ConversionUtil.java
 ****************************************************************************************/
package net.ruready.parser.port.output.mathml.assembler;

import java.util.Hashtable;

import net.ruready.common.rl.CommonNames;
import net.ruready.common.util.XmlUtil;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryFunction;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.arithmetic.entity.numericalvalue.RealValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstant;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstant;
import net.ruready.parser.logical.entity.value.RelationOperation;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.service.exception.MathParserException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utilities related to converting parser text syntax to MathML content.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E University
 *         of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07
 *         Continuing Education , University of Utah . All copyrights reserved. U.S.
 *         Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 14, 2007
 */
class ConversionUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ConversionUtil.class);

	// Useful conversion tables
	private static Hashtable<ComplexConstant, String> complexConstant2ElementMap;

	private static Hashtable<RealConstant, String> realConstant2ElementMap;

	private static Hashtable<UnaryOperation, String> unaryOp2ElementMap;

	private static Hashtable<BinaryOperation, String> binaryOp2ElementMap;

	private static Hashtable<BinaryFunction, String> binaryFunc2ElementMap;

	private static Hashtable<BinaryFunction, String> binaryFuncArg02ElementMap;

	private static Hashtable<RelationOperation, String> relationOp2ElementMap;

	private static Hashtable<MathValueID, String> multinaryLogicalOp2ElementMap;

	// MathML tag representing a numerical value
	private static final String ELEMENT_NUMBER = "cn";

	// MathML tag representing a variable
	private static final String ELEMENT_VARIABLE = "ci";

	// Operation application MathML tag
	private static final String ELEMENT_APPLY = "apply";

	// Static initializations
	static
	{
		complexConstant2ElementMap = ConversionUtil.initComplexConstant2ElementMap();
		realConstant2ElementMap = ConversionUtil.initRealConstant2ElementMap();
		unaryOp2ElementMap = ConversionUtil.initUnaryOp2ElementMap();
		binaryOp2ElementMap = ConversionUtil.initBinaryOp2ElementMap();
		binaryFunc2ElementMap = ConversionUtil.initBinaryFunc2ElementMap();
		binaryFuncArg02ElementMap = ConversionUtil.initBinaryFuncArg02ElementMap();
		relationOp2ElementMap = ConversionUtil.initRelationOp2ElementMap();
		multinaryLogicalOp2ElementMap = ConversionUtil
				.initMultinaryLogicalOp2ElementMap();
	}

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private ConversionUtil()
	{

	}

	// ========================= METHODS ===================================

	/**
	 * Wrap an expression body with a MathML expression element.
	 * 
	 * @param expressionBody
	 *            expression body
	 * @return expression body wrapped with a MathML expression element
	 */
	public static StringBuffer generateExpressionElement(StringBuffer expressionBody)
	{
		final String tagName = "math";
		return XmlUtil.fullTag(tagName, expressionBody);
	}

	/**
	 * Convert a numerical value into a MathML element.
	 * 
	 * @param opToken
	 *            Numerical value math token.
	 * @return corresponding MathML element
	 */
	public static StringBuffer generateNumericalElement(MathToken opToken)
	{
		return XmlUtil.fullTag(ConversionUtil.ELEMENT_NUMBER, new StringBuffer(opToken
				.getValue().toString()));
	}

	/**
	 * Convert a symbolic variable into a MathML element.
	 * 
	 * @param variable
	 *            Symbolic variable's name
	 * @return corresponding MathML element
	 */
	public static StringBuffer generateVariableElement(String variable)
	{
		return XmlUtil.fullTag(ConversionUtil.ELEMENT_VARIABLE,
				new StringBuffer(variable));
	}

	/**
	 * Convert a complex constant into a MathML element.
	 * 
	 * @param complexConstant
	 *            Real constant
	 * @return corresponding MathML element
	 */
	public static StringBuffer generateComplexConstantElement(
			ComplexConstant complexConstant)
	{
		String tagName = ConversionUtil.complexConstant2ElementMap.get(complexConstant);
		return XmlUtil.emptyTag(tagName);
	}

	/**
	 * Convert a real constant into a MathML element.
	 * 
	 * @param realConstant
	 *            Real constant
	 * @return corresponding MathML element
	 */
	public static StringBuffer generateRealConstantElement(RealConstant realConstant)
	{
		String tagName = ConversionUtil.realConstant2ElementMap.get(realConstant);
		return XmlUtil.emptyTag(tagName);
	}

	/**
	 * Convert a complex constant into a MathML element. This is identical to variable
	 * treatment.
	 * 
	 * @param complexConstant
	 *            Complex constant
	 * @return corresponding MathML element
	 */
	public static StringBuffer generateConstantElement(ComplexConstant complexConstant)
	{
		return ConversionUtil.generateVariableElement(complexConstant.toString());
	}

	/**
	 * Perform an arithmetic unary/binary/multi-nary operation/function of arguments and
	 * output the resulting MathML content language XML string.
	 * 
	 * @param opToken
	 *            arithmetic operation math token.
	 * @param args
	 *            list of arguments (really, their XML string representations)
	 * @return a new MathML content string value representing opToken(args) /** Generate a
	 */
	public static StringBuffer applyOperationElement(MathToken opToken,
			StringBuffer... args)
	{
		switch (opToken.getValueID())
		{
			// ======================================
			// Arithmetic parser operations
			// ======================================
			case ARITHMETIC_UNARY_OPERATION:
			{
				return ConversionUtil.applyUnaryOpElement((UnaryOperation) opToken
						.getValue(), args[0]);
			}

			case ARITHMETIC_BINARY_OPERATION:
			{
				return ConversionUtil.applyBinaryOpElement((BinaryOperation) opToken
						.getValue(), args[0], args[1]);
			}

			case ARITHMETIC_BINARY_FUNCTION:
			{
				return ConversionUtil.applyBinaryFuncElement((BinaryFunction) opToken
						.getValue(), args[0], args[1]);
			}

				// ======================================
				// Logical parser operations
				// ======================================
			case LOGICAL_RELATION_OPERATION:
			{
				return ConversionUtil.applyLogicalOpElement((RelationOperation) opToken
						.getValue(), args[0], args[1]);
			}

			case LOGICAL_RESPONSE:
			{
				// Do not wrap a single relation with a vector tag (hence the "true" flag)
				return ConversionUtil.applyLogicalOpElement(opToken.getValueID(), true,
						args);
			}

			default:
			{
				throw new MathParserException(opToken.toString(),
						"Unsupported <apply> for opToken " + opToken + " type "
								+ opToken.getValueID());
			}
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Initialize a map of a complex constant to a MathML content element.
	 * 
	 * @return map of complex constants to a MathML content elements
	 */
	private static Hashtable<ComplexConstant, String> initComplexConstant2ElementMap()
	{
		Hashtable<ComplexConstant, String> map = new Hashtable<ComplexConstant, String>();

		map.put(ComplexConstant.I, "imaginaryi");
		map.put(ComplexConstant.J, "imaginaryi");

		return map;
	}

	/**
	 * Initialize a map of a real constant to a MathML content element.
	 * 
	 * @return map of real constants to a MathML content elements
	 */
	private static Hashtable<RealConstant, String> initRealConstant2ElementMap()
	{
		Hashtable<RealConstant, String> map = new Hashtable<RealConstant, String>();

		map.put(RealConstant.E, "exponentiale");
		map.put(RealConstant.PI, "pi");
		map.put(RealConstant.GAMMA, "eulergamma");

		return map;
	}

	/**
	 * Initialize a map of a unary function/operation to a MathML content element.
	 * 
	 * @return map of unary function/operations to a MathML content elements
	 */
	private static Hashtable<UnaryOperation, String> initUnaryOp2ElementMap()
	{
		Hashtable<UnaryOperation, String> map = new Hashtable<UnaryOperation, String>();

		map.put(UnaryOperation.ABS, "abs");
		map.put(UnaryOperation.ARCCOS, "arccos");
		map.put(UnaryOperation.ARCCOSH, "arccosh");
		map.put(UnaryOperation.ARCSIN, "arcsin");
		map.put(UnaryOperation.ARCSINH, "arcsinh");
		map.put(UnaryOperation.ARCTAN, "arctan");
		map.put(UnaryOperation.ARCTANH, "arctanh");
		map.put(UnaryOperation.CBRT, CommonNames.MISC.EMPTY_STRING); // Special:
		// equivalent
		// to binary root(3,x)
		map.put(UnaryOperation.CEILING, "ceiling");
		map.put(UnaryOperation.COS, "cos");
		map.put(UnaryOperation.COSH, "cosh");
		map.put(UnaryOperation.COT, "cot");
		map.put(UnaryOperation.CSC, "csc");
		// map.put(UnaryOp.DIVIDE, "divide"); // Unary / not allowed in MathML
		// map.put(UnaryOp.EMPTY, CommonNames.MISC.EMPTY_STRING); // a fictitious part
		// of the parser syntax
		map.put(UnaryOperation.EXP, "exp");
		map.put(UnaryOperation.FACTORIAL, "factorial");
		map.put(UnaryOperation.FLOOR, "floor");
		map.put(UnaryOperation.LN, "ln");
		map.put(UnaryOperation.LOG, "log");
		map.put(UnaryOperation.MINUS, "minus"); // Unary - = multinary - in MathML
		// map.put(UnaryOp.PLUS, "plus"); // Unary + not allowed in MathML
		map.put(UnaryOperation.ROOT, "root"); // (Equivalent to sqrt in MathML)
		map.put(UnaryOperation.SEC, "sec");
		// map.put(UnaryOp.SGN, "sgn"); // Sign not supported by MathML
		map.put(UnaryOperation.SIN, "sin");
		map.put(UnaryOperation.SINH, "sinh");
		map.put(UnaryOperation.SQRT, "root"); // Equivalent to root
		map.put(UnaryOperation.TAN, "tan");
		map.put(UnaryOperation.TANH, "tanh");
		// map.put(UnaryOp.TIMES, "*"); // Unary * not allowed in MathML

		return map;
	}

	/**
	 * Initialize a map of a binary operation to a MathML content element.
	 * 
	 * @return map of binary operations to a MathML content elements
	 */
	private static Hashtable<BinaryOperation, String> initBinaryOp2ElementMap()
	{
		Hashtable<BinaryOperation, String> map = new Hashtable<BinaryOperation, String>();

		map.put(BinaryOperation.PLUS, "plus");
		map.put(BinaryOperation.MINUS, "minus");
		map.put(BinaryOperation.TIMES, "times");
		map.put(BinaryOperation.DIVIDE, "divide");
		map.put(BinaryOperation.POWER, "power");
		map.put(BinaryOperation.MOD, "rem");

		return map;
	}

	/**
	 * Initialize a map of a binary function to a MathML content element.
	 * 
	 * @return map of binary functions to a MathML content elements
	 */
	private static Hashtable<BinaryFunction, String> initBinaryFunc2ElementMap()
	{
		Hashtable<BinaryFunction, String> map = new Hashtable<BinaryFunction, String>();

		map.put(BinaryFunction.LOG, "log");
		map.put(BinaryFunction.REM, "rem");
		map.put(BinaryFunction.ROOT, "root");

		return map;
	}

	/**
	 * Initialize a map of the first argument of a binary function to a MathML content
	 * element, for special binary functions (e.g. root, whose first argument corresponds
	 * to a degree MathML element).
	 * 
	 * @return map of binary functions to a MathML content elements
	 */
	private static Hashtable<BinaryFunction, String> initBinaryFuncArg02ElementMap()
	{
		Hashtable<BinaryFunction, String> map = new Hashtable<BinaryFunction, String>();

		map.put(BinaryFunction.LOG, "logbase");
		map.put(BinaryFunction.ROOT, "degree");

		return map;
	}

	/**
	 * Initialize a map of a relational operation to a MathML content element.
	 * 
	 * @return map of relational operations to a MathML content elements
	 */
	private static Hashtable<RelationOperation, String> initRelationOp2ElementMap()
	{
		Hashtable<RelationOperation, String> map = new Hashtable<RelationOperation, String>();

		map.put(RelationOperation.ASSIGN, "equivalent"); // This tag means logically
		// equivalent (MathML 2.0)
		map.put(RelationOperation.EQ, "eq");
		map.put(RelationOperation.GE, "geq");
		map.put(RelationOperation.GT, "gt");
		map.put(RelationOperation.IF, "implies");
		map.put(RelationOperation.LE, "leq");
		map.put(RelationOperation.LT, "lt");
		map.put(RelationOperation.NE, "neq");

		return map;
	}

	/**
	 * Initialize a map of a logical operation to a MathML content element.
	 * 
	 * @return map of relational operations to a MathML content elements
	 */
	private static Hashtable<MathValueID, String> initMultinaryLogicalOp2ElementMap()
	{
		Hashtable<MathValueID, String> map = new Hashtable<MathValueID, String>();

		map.put(MathValueID.LOGICAL_RESPONSE, "vector");

		return map;
	}

	/**
	 * Convert a unary operation into a MathML element.
	 * 
	 * @param unaryOp
	 *            unary operation
	 * @return corresponding MathML element
	 */
	private static StringBuffer generateUnaryOpElement(UnaryOperation unaryOp)
	{
		String tagName = ConversionUtil.unaryOp2ElementMap.get(unaryOp);
		return XmlUtil.emptyTag(tagName);
	}

	/**
	 * Convert a binary operation into a MathML element.
	 * 
	 * @param binaryOp
	 *            binary operation
	 * @return corresponding MathML element
	 */
	private static StringBuffer generateBinaryOpElement(BinaryOperation binaryOp)
	{
		String tagName = ConversionUtil.binaryOp2ElementMap.get(binaryOp);
		return XmlUtil.emptyTag(tagName);
	}

	/**
	 * Convert a binary function into a MathML element.
	 * 
	 * @param binaryFunc
	 *            binary function
	 * @return corresponding MathML element
	 */
	private static StringBuffer generateBinaryFuncElement(BinaryFunction binaryFunc)
	{
		String tagName = ConversionUtil.binaryFunc2ElementMap.get(binaryFunc);
		return XmlUtil.emptyTag(tagName);
	}

	/**
	 * Convert a relational operation into a MathML element.
	 * 
	 * @param relationOp
	 *            relational operation
	 * @return corresponding MathML element
	 */
	private static StringBuffer generateRelationOperationElement(
			RelationOperation relationOp)
	{
		String tagName = ConversionUtil.relationOp2ElementMap.get(relationOp);
		return XmlUtil.emptyTag(tagName);
	}

	/**
	 * Perform an arithmetic unary operation to arguments and output the resulting MathML
	 * content language XML string.
	 * 
	 * @param unaryOp
	 *            unary operation
	 * @param arg0
	 *            sole argument string representation
	 * @return a new MathML content string value representing arg0 unaryOp arg1
	 */
	private static StringBuffer applyUnaryOpElement(UnaryOperation unaryOp,
			StringBuffer arg0)
	{
		switch (unaryOp)
		{
			// Deal with special cases first; otherwise run the default section
			case CBRT:
			{
				// parser cbrt(x) is equivalent to MathML root(3,x)
				return ConversionUtil.applyBinaryFuncElement(BinaryFunction.ROOT,
						ConversionUtil
								.generateNumericalElement(new MathToken(
										CommonNames.MISC.INVALID_VALUE_INTEGER,
										new RealValue(3.0))), arg0);
			}

			default:
			{
				StringBuffer s = new StringBuffer(XmlUtil
						.openTag(ConversionUtil.ELEMENT_APPLY));
				// Append the function element
				s.append(ConversionUtil.generateUnaryOpElement(unaryOp));
				// Append the arguments
				s.append(arg0);
				s.append(XmlUtil.closeTag(ConversionUtil.ELEMENT_APPLY));
				return s;
			}
		} // switch (unaryOp)
	}

	/**
	 * Perform an arithmetic binary operation to arguments and output the resulting MathML
	 * content language XML string.
	 * 
	 * @param binaryOp
	 *            binary operation
	 * @param arg0
	 *            first argument string representation
	 * @param arg1
	 *            second argument string representation
	 * @return a new MathML content string value representing arg0 binaryOp arg1
	 */
	private static StringBuffer applyBinaryOpElement(BinaryOperation binaryOp,
			StringBuffer arg0, StringBuffer arg1)
	{
		StringBuffer s = new StringBuffer(XmlUtil.openTag(ConversionUtil.ELEMENT_APPLY));
		// Append the function element
		s.append(ConversionUtil.generateBinaryOpElement(binaryOp));
		// Append the two arguments in order
		s.append(arg0);
		s.append(arg1);
		s.append(XmlUtil.closeTag(ConversionUtil.ELEMENT_APPLY));
		return s;
	}

	/**
	 * Perform an arithmetic binary function to arguments and output the resulting MathML
	 * content language XML string.
	 * 
	 * @param binaryFunc
	 *            binary function
	 * @param arg0
	 *            first argument string representation. May be wrapped with a special tag
	 *            for some functions (e.g. for the root function: degree element wraps
	 *            this argument)
	 * @param arg1
	 *            second argument string representation
	 * @return a new MathML content string value representing arg0 binaryFunc arg1
	 */
	private static StringBuffer applyBinaryFuncElement(BinaryFunction binaryFunc,
			StringBuffer arg0, StringBuffer arg1)
	{
		StringBuffer s = new StringBuffer(XmlUtil.openTag(ConversionUtil.ELEMENT_APPLY));
		// Append the function element
		s.append(ConversionUtil.generateBinaryFuncElement(binaryFunc));
		// Decide whether to wrap the first argument
		String arg0Element = ConversionUtil.binaryFuncArg02ElementMap.get(binaryFunc);
		// Append the two arguments in order; potentially wrap the first one
		// with the arg0Element tag
		s.append((arg0Element != null) ? XmlUtil.fullTag(arg0Element, arg0) : arg0);
		s.append(arg1);
		s.append(XmlUtil.closeTag(ConversionUtil.ELEMENT_APPLY));
		return s;
	}

	/**
	 * Apply a logical relation operation to arguments and output the resulting MathML
	 * content language XML string.
	 * 
	 * @param relationOp
	 *            relational operation
	 * @param arg0
	 *            first argument string representation. May be wrapped with a special tag
	 *            for some functions (e.g. for the root function: degree element wraps
	 *            this argument)
	 * @param arg1
	 *            second argument string representation
	 * @return a new MathML content string value representing arg0 binaryFunc arg1
	 */
	private static StringBuffer applyLogicalOpElement(final RelationOperation relationOp,
			final StringBuffer arg0, final StringBuffer arg1)
	{
		StringBuffer s = new StringBuffer(XmlUtil.openTag(ConversionUtil.ELEMENT_APPLY));
		// Append the function element
		s.append(ConversionUtil.generateRelationOperationElement(relationOp));
		// Append the two arguments in order
		s.append(arg0);
		s.append(arg1);
		s.append(XmlUtil.closeTag(ConversionUtil.ELEMENT_APPLY));
		return s;
	}

	/**
	 * Apply a logical operation to arguments and output the resulting MathML content
	 * language XML string.
	 * 
	 * @param logicalOperationType
	 *            type of logical operation to perform
	 * @param noWrapOfOneArg
	 *            if true, a one-argument list won't be wrapped with any logical operation
	 *            tag.
	 * @param args
	 *            argument list's string representations.
	 * @param
	 * @return a new MathML content string value representing logicalOperationType(args)
	 */
	private static StringBuffer applyLogicalOpElement(
			final MathValueID logicalOperationType, final boolean noWrapOfOneArg,
			final StringBuffer... args)
	{
		// The one-argument list case
		if (noWrapOfOneArg && args.length == 1)
		{
			return args[0];
		}

		// Create the logical operation's tag
		String tagName = multinaryLogicalOp2ElementMap.get(logicalOperationType);
		StringBuffer s = new StringBuffer(XmlUtil.openTag(tagName));
		// Append the arguments in order
		for (StringBuffer arg : args)
		{
			s.append(arg);
		}
		s.append(XmlUtil.closeTag(tagName));
		return s;
	}
}
