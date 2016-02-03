/*******************************************************
 * Source File: MathTokenStatus.java
 *******************************************************/
package net.ruready.parser.math.entity;

import net.ruready.common.discrete.Identifier;

/**
 * Possible token stati: discarded, correct, wrong, etc. Heavily used by the
 * element and ATPM tree markers. Note: the toString() method must obey the
 * general contract of the getType() method.
 * <p>
 * Note 1: the numerical values of all IDs that appear in the main trees (i.e.
 * they are not extraenous or discarded) must be positive and running;
 * extraneous stati must have negative values. All values must be different.
 * <p>
 * Note 2: the toString() method should also uniquely identify a key.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Oct 27, 2005
 */
public enum MathTokenStatus implements Identifier
{
	// Must be the minimal negative status
	DISCARDED(-1)
	{
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return "D";
		}

		/**
		 * @see net.ruready.parser.math.entity.MathTokenStatus#isError()
		 */
		@Override
		public boolean isError()
		{
			return false;
		}
	},

	FICTITIOUS_CORRECT(-2)
	{
		// Fictitious nodes in trees that have an exact
		// match in the edit distance mapping
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return "F";
		}

		/**
		 * @see net.ruready.parser.math.entity.MathTokenStatus#isError()
		 */
		@Override
		public boolean isError()
		{
			return false;
		}
	},

	REDUNDANT(-3)
	{
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return "R";
		}

		/**
		 * @see net.ruready.parser.math.entity.MathTokenStatus#isError()
		 */
		@Override
		public boolean isError()
		{
			// Redundant elements are not considered errors-per-say
			return false;
		}
	},

	WRONG(0)
	{
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return "W";
		}

		/**
		 * @see net.ruready.parser.math.entity.MathTokenStatus#isError()
		 */
		@Override
		public boolean isError()
		{
			return true;
		}
	},

	UNRECOGNIZED(1)
	{
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return "U";
		}

		/**
		 * @see net.ruready.parser.math.entity.MathTokenStatus#isError()
		 */
		@Override
		public boolean isError()
		{
			return true;
		}
	},

	MISSING(2)
	{
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return "M";
		}

		/**
		 * @see net.ruready.parser.math.entity.MathTokenStatus#isError()
		 */
		@Override
		public boolean isError()
		{
			return true;
		}
	},

	CORRECT(3)
	{
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return "C";
		}

		/**
		 * @see net.ruready.parser.math.entity.MathTokenStatus#isError()
		 */
		@Override
		public boolean isError()
		{
			return false;
		}
	};

	// ========================= FIELDS ====================================

	private int value;

	// ========================= CONSTRUCTORS ==============================

	private MathTokenStatus(int value)
	{
		this.value = value;
	}

	// ========================= IMPLEMENTATION: Identifier ===================

	/**
	 * Return the object's type. This would normally be the object's
	 * <code>getClass().getSimpleName()</code>, but objects might be wrapped
	 * by Hibernate to return unexpected names. As a rule, the type string
	 * should not contain spaces and special characters.
	 */
	public String getType()
	{
		// The enumerated type's name satisfies all the type criteria, so use
		// it.
		return name();
	}

	// ========================= METHODS ===================================

	/**
	 * Does this status refer to an erroneous token.
	 * 
	 * @return a flag indicating whether this status refer to an erroneous token
	 */
	public abstract boolean isError();

	// ========================= STATIC METHODS ============================

	/**
	 * Return the number of types with non-negative values.
	 * 
	 * @return the number of types with non-negative values
	 */
	public static int numNonNegativeValues()
	{
		int count = 0;
		for (MathTokenStatus t : MathTokenStatus.values()) {
			if (t.getValue() >= 0) {
				count++;
			}
		}
		return count;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the value
	 */
	public int getValue()
	{
		return value;
	}
}
