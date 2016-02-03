/*****************************************************************************************
 * Source File: DefaultMathValueArbiter.java
 ****************************************************************************************/

package net.ruready.parser.math.entity.visitor;

import net.ruready.common.misc.Utility;
import net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticLiteralValue;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryFunction;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.MultinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.ParenthesisValue;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.Variable;
import net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.IntegerValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.RationalValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.RealValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstantValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstantValue;
import net.ruready.parser.logical.entity.value.EmptyValue;
import net.ruready.parser.logical.entity.value.RelationOperation;
import net.ruready.parser.logical.entity.value.ResponseValue;
import net.ruready.parser.math.entity.value.MathValue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utilities related to {@link AbstractMathValueVisitor} types.
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
 * @version Jul 17, 2007
 */
public class MathValueVisitorUtil implements Utility
{
	// ========================= CONSTANTS =================================

	// For logging printouts
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(MathValueVisitorUtil.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private MathValueVisitorUtil()
	{

	}

	// ========================= STATIC METHODS ============================

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>).
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	@Deprecated
	public static <V extends MathValue> void visit(V visitable,
			AbstractMathValueVisitor visitor)
	{
		// It seems like casting is necessary, because there is no obvious way
		// to dynamically cast to the correct visitor type at run-time without
		// complicating the Visitor's or Arbiter's interfaces.
		if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			visitor.visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			visitor.visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>).
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	@Deprecated
	public static void genericAccept(MathValue visitable, AbstractMathValueVisitor visitor)
	{
		// It seems like casting is necessary, because there is no obvious way
		// to dynamically cast to the correct visitor type at run-time without
		// complicating the Visitor's or Arbiter's interfaces.
		if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			visitor.visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			visitor.visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}

	// ------------------------- ADD ALL MathValue TYPES HERE --------------

	// =====================================
	// Arithmetic values
	// =====================================

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>). Arbitrates between different visitor
	 * types (sub-classes of {@link AbstractMathValueVisitor}).
	 * <p>
	 * It seems like the casting here is necessary, because there is no obvious way to
	 * dynamically cast to the correct visitor type at run-time without complicating the
	 * Visitor's or Arbiter's interfaces.
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	public static void accept(ArithmeticLiteralValue visitable,
			AbstractMathValueVisitor visitor)
	{
		if (visitor instanceof CoarseGrainMathValueVisitor)
		{
			((CoarseGrainMathValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			((LogicalValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			((FullMathValueVisitor) visitor).visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>). Arbitrates between different visitor
	 * types (sub-classes of {@link AbstractMathValueVisitor}).
	 * <p>
	 * It seems like the casting here is necessary, because there is no obvious way to
	 * dynamically cast to the correct visitor type at run-time without complicating the
	 * Visitor's or Arbiter's interfaces.
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	public static void accept(BinaryFunction visitable, AbstractMathValueVisitor visitor)
	{
		if (visitor instanceof CoarseGrainMathValueVisitor)
		{
			((CoarseGrainMathValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			((LogicalValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			((FullMathValueVisitor) visitor).visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>). Arbitrates between different visitor
	 * types (sub-classes of {@link AbstractMathValueVisitor}).
	 * <p>
	 * It seems like the casting here is necessary, because there is no obvious way to
	 * dynamically cast to the correct visitor type at run-time without complicating the
	 * Visitor's or Arbiter's interfaces.
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	public static void accept(BinaryOperation visitable, AbstractMathValueVisitor visitor)
	{
		if (visitor instanceof CoarseGrainMathValueVisitor)
		{
			((CoarseGrainMathValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			((LogicalValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			((FullMathValueVisitor) visitor).visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>). Arbitrates between different visitor
	 * types (sub-classes of {@link AbstractMathValueVisitor}).
	 * <p>
	 * It seems like the casting here is necessary, because there is no obvious way to
	 * dynamically cast to the correct visitor type at run-time without complicating the
	 * Visitor's or Arbiter's interfaces.
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	public static void accept(ComplexConstantValue visitable,
			AbstractMathValueVisitor visitor)
	{
		if (visitor instanceof CoarseGrainMathValueVisitor)
		{
			((CoarseGrainMathValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			((LogicalValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			((FullMathValueVisitor) visitor).visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>). Arbitrates between different visitor
	 * types (sub-classes of {@link AbstractMathValueVisitor}).
	 * <p>
	 * It seems like the casting here is necessary, because there is no obvious way to
	 * dynamically cast to the correct visitor type at run-time without complicating the
	 * Visitor's or Arbiter's interfaces.
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	public static void accept(ComplexValue visitable, AbstractMathValueVisitor visitor)
	{
		if (visitor instanceof CoarseGrainMathValueVisitor)
		{
			((CoarseGrainMathValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			((LogicalValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			((FullMathValueVisitor) visitor).visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>). Arbitrates between different visitor
	 * types (sub-classes of {@link AbstractMathValueVisitor}).
	 * <p>
	 * It seems like the casting here is necessary, because there is no obvious way to
	 * dynamically cast to the correct visitor type at run-time without complicating the
	 * Visitor's or Arbiter's interfaces.
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	public static void accept(IntegerValue visitable, AbstractMathValueVisitor visitor)
	{
		if (visitor instanceof CoarseGrainMathValueVisitor)
		{
			((CoarseGrainMathValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			((LogicalValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			((FullMathValueVisitor) visitor).visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>). Arbitrates between different visitor
	 * types (sub-classes of {@link AbstractMathValueVisitor}).
	 * <p>
	 * It seems like the casting here is necessary, because there is no obvious way to
	 * dynamically cast to the correct visitor type at run-time without complicating the
	 * Visitor's or Arbiter's interfaces.
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	public static void accept(MultinaryOperation visitable, AbstractMathValueVisitor visitor)
	{
		if (visitor instanceof CoarseGrainMathValueVisitor)
		{
			((CoarseGrainMathValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			((LogicalValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			((FullMathValueVisitor) visitor).visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>). Arbitrates between different visitor
	 * types (sub-classes of {@link AbstractMathValueVisitor}).
	 * <p>
	 * It seems like the casting here is necessary, because there is no obvious way to
	 * dynamically cast to the correct visitor type at run-time without complicating the
	 * Visitor's or Arbiter's interfaces.
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	public static void accept(ParenthesisValue visitable, AbstractMathValueVisitor visitor)
	{
		if (visitor instanceof CoarseGrainMathValueVisitor)
		{
			((CoarseGrainMathValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			((LogicalValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			((FullMathValueVisitor) visitor).visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>). Arbitrates between different visitor
	 * types (sub-classes of {@link AbstractMathValueVisitor}).
	 * <p>
	 * It seems like the casting here is necessary, because there is no obvious way to
	 * dynamically cast to the correct visitor type at run-time without complicating the
	 * Visitor's or Arbiter's interfaces.
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	public static void accept(RealConstantValue visitable,
			AbstractMathValueVisitor visitor)
	{
		if (visitor instanceof CoarseGrainMathValueVisitor)
		{
			((CoarseGrainMathValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			((LogicalValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			((FullMathValueVisitor) visitor).visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>). Arbitrates between different visitor
	 * types (sub-classes of {@link AbstractMathValueVisitor}).
	 * <p>
	 * It seems like the casting here is necessary, because there is no obvious way to
	 * dynamically cast to the correct visitor type at run-time without complicating the
	 * Visitor's or Arbiter's interfaces.
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	public static void accept(RealValue visitable, AbstractMathValueVisitor visitor)
	{
		if (visitor instanceof CoarseGrainMathValueVisitor)
		{
			((CoarseGrainMathValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			((LogicalValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			((FullMathValueVisitor) visitor).visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>). Arbitrates between different visitor
	 * types (sub-classes of {@link AbstractMathValueVisitor}).
	 * <p>
	 * It seems like the casting here is necessary, because there is no obvious way to
	 * dynamically cast to the correct visitor type at run-time without complicating the
	 * Visitor's or Arbiter's interfaces.
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	public static void accept(RationalValue visitable, AbstractMathValueVisitor visitor)
	{
		if (visitor instanceof CoarseGrainMathValueVisitor)
		{
			((CoarseGrainMathValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			((LogicalValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			((FullMathValueVisitor) visitor).visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>). Arbitrates between different visitor
	 * types (sub-classes of {@link AbstractMathValueVisitor}).
	 * <p>
	 * It seems like the casting here is necessary, because there is no obvious way to
	 * dynamically cast to the correct visitor type at run-time without complicating the
	 * Visitor's or Arbiter's interfaces.
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	public static void accept(UnaryOperation visitable, AbstractMathValueVisitor visitor)
	{
		if (visitor instanceof CoarseGrainMathValueVisitor)
		{
			((CoarseGrainMathValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			((LogicalValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			((FullMathValueVisitor) visitor).visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>). Arbitrates between different visitor
	 * types (sub-classes of {@link AbstractMathValueVisitor}).
	 * <p>
	 * It seems like the casting here is necessary, because there is no obvious way to
	 * dynamically cast to the correct visitor type at run-time without complicating the
	 * Visitor's or Arbiter's interfaces.
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	public static void accept(Variable visitable, AbstractMathValueVisitor visitor)
	{
		if (visitor instanceof CoarseGrainMathValueVisitor)
		{
			((CoarseGrainMathValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			((LogicalValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			((FullMathValueVisitor) visitor).visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}

	// =====================================
	// Logical values
	// =====================================

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>). Arbitrates between different visitor
	 * types (sub-classes of {@link AbstractMathValueVisitor}).
	 * <p>
	 * It seems like the casting here is necessary, because there is no obvious way to
	 * dynamically cast to the correct visitor type at run-time without complicating the
	 * Visitor's or Arbiter's interfaces.
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	public static void accept(EmptyValue visitable, AbstractMathValueVisitor visitor)
	{
		if (visitor instanceof CoarseGrainMathValueVisitor)
		{
			((CoarseGrainMathValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			((LogicalValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			((FullMathValueVisitor) visitor).visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>). Arbitrates between different visitor
	 * types (sub-classes of {@link AbstractMathValueVisitor}).
	 * <p>
	 * It seems like the casting here is necessary, because there is no obvious way to
	 * dynamically cast to the correct visitor type at run-time without complicating the
	 * Visitor's or Arbiter's interfaces.
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	public static void accept(ResponseValue visitable, AbstractMathValueVisitor visitor)
	{
		if (visitor instanceof CoarseGrainMathValueVisitor)
		{
			((CoarseGrainMathValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			((LogicalValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			((FullMathValueVisitor) visitor).visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}

	/**
	 * Make a visitor object visit a visitable object using a call-back method (<code>visit()</code>
	 * calls <code>visitable.accept(this)</code>). Arbitrates between different visitor
	 * types (sub-classes of {@link AbstractMathValueVisitor}).
	 * <p>
	 * It seems like the casting here is necessary, because there is no obvious way to
	 * dynamically cast to the correct visitor type at run-time without complicating the
	 * Visitor's or Arbiter's interfaces.
	 * 
	 * @param visitable
	 *            object to be visited
	 * @param visitor
	 *            visitor object that visits visitable
	 */
	public static void accept(RelationOperation visitable, AbstractMathValueVisitor visitor)
	{
		if (visitor instanceof CoarseGrainMathValueVisitor)
		{
			((CoarseGrainMathValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof LogicalValueVisitor)
		{
			((LogicalValueVisitor) visitor).visit(visitable);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			((FullMathValueVisitor) visitor).visit(visitable);
		}
		else
		{
			visitor.visit(visitable);
		}
	}
}
