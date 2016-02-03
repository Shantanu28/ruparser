/*****************************************************************************************
 * Source File: ApplyFuncHelper.java
 ****************************************************************************************/
package net.ruready.parser.port.input.mathml.entity;

import java.util.List;

import net.ruready.common.parser.xml.helper.GriddyHelper;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.text.TextUtil;
import net.ruready.parser.absolute.entity.OperationPrecedenceUtil;
import net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticLiteralValue;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryFunction;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.value.MathValue;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exception.MathParserException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;

/**
 * A helper that applies a function or operation to its operands, and pushes the result
 * onto the target's stack.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p>
 * This file is part of the RUReady Program software.<br>
 * Contact: Nava L. Livne <code>&lt;nlivne@aoce.utah.edu&gt;</code><br>
 * Academic Outreach and Continuing Education (AOCE)<br>
 * 1901 East South Campus Dr., Room 2197-E<br>
 * University of Utah, Salt Lake City, UT 84112-9359<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Jul 19, 2007
 */
class ApplyFuncHelper extends GriddyHelper
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ApplyFuncHelper.class);

	// Fence for this function application block
	public static final MathElement FENCE = new MathElement("APPLY_FENCE",
			new ArithmeticLiteralValue("APPLY_FENCE"));

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a helper that applies a function or operation to its operands.
	 * 
	 * @param tagName
	 *            final String tagName
	 */
	public ApplyFuncHelper(final String tagName)
	{
		super(tagName);
	}

	// ========================= IMPLEMENTATION: Helper ====================

	/**
	 * Stack a fence signifying that a function application block is starting. This fence
	 * is retrieved by {@link #endElement(Object)}.
	 * 
	 * @param atts
	 *            the attributes attached to the element
	 * @param target
	 * @see net.ruready.parser.core.xml.Helper#startElement(java.lang.Object)
	 */
	@Override
	public void processStartElement(final Attributes atts, final Object target)
	{
		// Cast to a friendlier version
		ConverterTarget myTarget = (ConverterTarget) target;

		// Stack the fence
		myTarget.push(ApplyFuncHelper.FENCE);
	}

	/**
	 * Create a math element to wrap the most recently pushed element on the target's
	 * stack.
	 * 
	 * @see net.ruready.parser.core.xml.Helper#endElement(java.lang.Object)
	 */
	@Override
	public void processEndElement(Object target)
	{
		// Cast to a friendlier version
		ConverterTarget myTarget = (ConverterTarget) target;

		// Pop all terms above the fence
		List<MathElement> rawElements = myTarget.elementsAbove(ApplyFuncHelper.FENCE);

		int numArgs = rawElements.size() - 1;
		MathElement funcElement = rawElements.get(numArgs);
		MathValue func = funcElement.getValue();

		// logger.debug("<<<<<< Applying function >>>>>>");
		// logger.debug("funcElement " + funcElement);
		// logger.debug("rawElements " + rawElements);
		// logger.debug("numArgs " + numArgs);

		// =================================================================
		// Apply the function and construct a new element for the result
		// =================================================================
		StringBuffer resultBuffer = TextUtil.emptyStringBuffer();

		if (numArgs == 0)
		{
			// No arguments
			throw new MathParserException(CommonNames.MISC.NONE, "Cannot apply function "
					+ funcElement + " to no arguments");
		} // numArgs == 0

		else if (numArgs == 1)
		{
			// A single argument
			switch (funcElement.getValueID())
			{
				case ARITHMETIC_BINARY_OPERATION:
				{
					// A unary operation, example: "-x".
					// Here the original function is funcElement that contains a
					// BINARY function; convert it to a unary function. Save the
					// new value in the func element so that the parent function
					// application runs correctly as well. The function's sole
					// argument is rawElement[0].
					func = ((BinaryOperation) func).unaryOpForm();
					if (func == null)
					{
						throw new MathParserException(null, "Cannot apply function "
								+ funcElement + " to one argument");
					}
					funcElement.setValue(func);
					resultBuffer.append(func.toString());
					resultBuffer.append(ApplyFuncHelper.appendSyntheticParentheses(
							funcElement, rawElements.get(0), 0));
					break;
				}

				case ARITHMETIC_UNARY_OPERATION:
				{
					// A single function, example: "f(x)".
					// Here the function is funcElement and the argument is
					// rawElement[0].
					resultBuffer.append(func.toString());
					resultBuffer.append(ParserNames.ARITHMETIC_COMPILER.PARENTHESIS_OPEN);
					resultBuffer.append(rawElements.get(0).getString());
					resultBuffer
							.append(ParserNames.ARITHMETIC_COMPILER.PARENTHESIS_CLOSE);
					break;
				}

				case ARITHMETIC_BINARY_FUNCTION:
				{
					// A binary function that also has a unary form and
					// didn't yet know whether it would have one or two args at
					// assembly time. Try to convert to unary form.
					func = ((BinaryFunction) func).unaryOpForm();
					if (func == null)
					{
						throw new MathParserException(null, "Cannot apply function "
								+ funcElement + " to one argument");
					}
					funcElement.setValue(func);
					resultBuffer.append(func.toString());
					resultBuffer.append(ParserNames.ARITHMETIC_COMPILER.PARENTHESIS_OPEN);
					resultBuffer.append(rawElements.get(0).getString());
					resultBuffer
							.append(ParserNames.ARITHMETIC_COMPILER.PARENTHESIS_CLOSE);
					break;
				}

				default:
				{
					throw new MathParserException(null, "Cannot apply function "
							+ funcElement + " to one argument");
				}
			} // switch
		} // numArgs == 1

		else if (numArgs >= 2)
		{
			// Two arguments
			switch (func.getIdentifier())
			{
				case ARITHMETIC_BINARY_OPERATION:
				case LOGICAL_RELATION_OPERATION:
				{
					// A binary or multi-nary operation, example: "x+y+z".
					// Here the function is funcElement and the arguments are
					// rawElement[numArgs-1]..rawElement[0]
					for (int i = numArgs - 1; i >= 0; i--)
					{
						resultBuffer.append(ApplyFuncHelper.appendSyntheticParentheses(
								funcElement, rawElements.get(i), numArgs - 1 - i));
						if (i > 0)
						{
							resultBuffer.append(func.toString());
						}
					}
					break;
				}

				case ARITHMETIC_BINARY_FUNCTION:
				{
					// A binary or multinary function, example: "f(x,y,z)".
					// Here the function is funcElement and the arguments are
					// rawElement[numArgs-1]..rawElement[0]

					// Re-order arguments when Literals are present. If a
					// <degree> or <logbase> Literals are found, move them to be
					// the first argument. There should be at most one Literal
					// per argument list.
					int last = numArgs - 1;
					int indexMisplaced = -1;
					for (int i = last; i >= 0; i--)
					{
						MathElement arg = rawElements.get(i);
						if ((arg.getValueID() == MathValueID.LITERAL) && (i != last))
						{
							// Found a misplaced Literal; move to the last
							// position
							// (=first arg of the function) of the arg list
							indexMisplaced = i;
							logger.debug("Found misplaced Literal at index "
									+ indexMisplaced);
							break;
						}
					}
					if (indexMisplaced >= 0)
					{
						// Swap foundMisplaced, last arguments
						MathElement temp = rawElements.get(last);
						rawElements.set(last, rawElements.get(indexMisplaced));
						rawElements.set(indexMisplaced, temp);
					}

					resultBuffer.append(func.toString());
					resultBuffer.append(ParserNames.ARITHMETIC_COMPILER.PARENTHESIS_OPEN);
					for (int i = numArgs - 1; i >= 0; i--)
					{
						resultBuffer.append(rawElements.get(i).getString());
						if (i > 0)
						{
							resultBuffer
									.append(ParserNames.ARITHMETIC_COMPILER.BINARY_FUNC_SEPARATOR);
						}
					}
					resultBuffer
							.append(ParserNames.ARITHMETIC_COMPILER.PARENTHESIS_CLOSE);
					break;
				}

				default:
				{
					throw new MathParserException(null, "Cannot apply function "
							+ funcElement + " to two arguments");
				}
			} // switch
		} // numArgs >= 2

		MathElement result = new MathElement(resultBuffer.toString(), funcElement
				.getValue());
		// logger.debug("result " + result);

		// Push the new element in place of the old latest element
		myTarget.push(result);
	}

	/**
	 * Decide whether to append synthetic parentheses around a child argument of a parent
	 * operation/function. Parentheses are appended if and only if they are not redundant
	 * for that child/parent pair.
	 * 
	 * @param parent
	 *            parent element
	 * @param child
	 *            child element
	 * @param childIndex
	 *            index of child element in the parent's children list
	 * @return child string representation (possibly including synthetic parentheses)
	 */
	private static StringBuffer appendSyntheticParentheses(MathElement parent,
			MathElement child, int childIndex)
	{
		boolean needParen = !OperationPrecedenceUtil.isParenAroundChildRedundant(parent
				.getValue(), child.getValue(), childIndex);
		logger.debug("Synthetic ( ): parent " + parent.getValue() + " child "
				+ child.getValue() + " need()? " + needParen);
		StringBuffer resultBuffer = TextUtil.emptyStringBuffer();
		if (needParen)
		{
			resultBuffer.append(ParserNames.ARITHMETIC_COMPILER.PARENTHESIS_OPEN);
		}
		resultBuffer.append(child.getString());
		if (needParen)
		{
			resultBuffer.append(ParserNames.ARITHMETIC_COMPILER.PARENTHESIS_CLOSE);
		}
		return resultBuffer;
	}
}
