/*****************************************************************************************
 * Source File: ConverterTarget.java
 ****************************************************************************************/
package net.ruready.parser.port.input.mathml.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import net.ruready.common.text.TextUtil;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.options.exports.ParserOptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A target object constructed by the MathML-to-math-parser converter. Contains a
 * manipulatable stack of mathematical elements that contains functions, operators and
 * arguments during parsing of a MathML expression.
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
 * @version Oct 5, 2007
 */
class ConverterTarget
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ConverterTarget.class);

	// ========================= FIELDS ====================================

	// -------------------------------------------------
	// Required input
	// -------------------------------------------------
	// Parser control options
	private final ParserOptions options;

	/**
	 * a placeholder that keeps track of consumption progress: a stack used during parsing
	 * a MathML content to stack functions, operators and arguments.
	 */
	private final Stack<MathElement> stack = new Stack<MathElement>();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an initial converter target with an empty assembly.
	 * 
	 * @param options
	 *            control options object
	 */
	public ConverterTarget(final ParserOptions options)
	{
		super();
		this.options = options;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer s = TextUtil.emptyStringBuffer();
		s.append(stack);
		return s.toString();
	}

	// ========================= METHODS ===================================

	/**
	 * Pop an element from the stack
	 * 
	 * @return the top element on the stack
	 * @see java.util.Stack#pop()
	 */
	public MathElement pop()
	{
		return stack.pop();
	}

	/**
	 * @param item
	 * @return
	 * @see java.util.Stack#push(java.lang.Object)
	 */
	public MathElement push(MathElement item)
	{
		return stack.push(item);
	}

	/**
	 * @return
	 * @see java.util.Vector#isEmpty()
	 */
	public boolean isEmpty()
	{
		return stack.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.Stack#peek()
	 */
	public MathElement peek()
	{
		return stack.peek();
	}

	/**
	 * @param d
	 * @return
	 * @see net.ruready.parser.options.exports.ParserOptions#createValue(double)
	 */
	public NumericalValue createValue(double d) throws NumberFormatException
	{
		return options.createValue(d);
	}

	/**
	 * @return
	 * @see net.ruready.parser.options.exports.ParserOptions#getArithmeticMode()
	 */
	public ArithmeticMode getArithmeticMode()
	{
		return options.getArithmeticMode();
	}

	/**
	 * Returns a list of the elements on the stack that appear before a specified fence.
	 * <p>
	 * Sometimes a parser will recognize a list from within a pair of parentheses or
	 * brackets. The parser can mark the beginning of the list with a fence, and then
	 * retrieve all the items that come after the fence with this method.
	 * 
	 * @param assembly
	 *            a assembly whose stack should contain some number of items above a fence
	 *            marker
	 * @param object
	 *            the fence, a marker of where to stop popping the stack
	 * @return the list the elements above the specified fence
	 */
	public List<MathElement> elementsAbove(MathElement fence)
	{
		List<MathElement> items = new ArrayList<MathElement>();

		while (!this.isEmpty())
		{
			MathElement top = this.pop();
			if (top.equals(fence))
			{
				break;
			}
			items.add(top);
		}

		return items;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the options
	 */
	public ParserOptions getOptions()
	{
		return options;
	}
}
