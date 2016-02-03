/*****************************************************************************************
 * Source File: ParserNames.java
 ****************************************************************************************/
package net.ruready.parser.rl;

import java.util.Arrays;
import java.util.List;

import net.ruready.common.math.real.RealUtil;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.common.rl.CommonNames;

/**
 * This interface centralizes constants, labels and names used throughout the
 * parser component. All strings referring to attributes should of course be
 * different from each other.
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
 * Please contact these numbers immediately if you receive this file without
 * permission from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Aug 3, 2007
 */
public interface ParserNames
{
	// ========================= CONSTANTS =================================

	// If no trailing slash is attached, file names are relative to the
	// "classes" directory of the application.

	// -----------------------------------------------------------------------
	// Resource locators & Properties
	// -----------------------------------------------------------------------
	public interface RESOURCE_LOCATOR
	{
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Specific RLs of this project
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

		/**
		 * For parser component; only logger and parser services are available
		 * here.
		 */
		static final String PARSER_CONFIG_FILE = "ResourceLocator_Parser.properties";
	}

	/**
	 * -----------------------------------------------------------------------<br>
	 * Reserved words. Cannot be variable names; must be lower case.
	 * -----------------------------------------------------------------------
	 */
	public interface RESERVED_WORDS
	{
		/**
		 * Response root node label.
		 */
		static final String RESPONSE = "response";

		/**
		 * Statement root node label Not currently in use, but may be in the
		 * future.
		 */
		static final String STATEMENT = "statement";

		/**
		 * Arithmetic expression root node label Not currently in use, but may
		 * be in the future.
		 */
		static final String ARITHMETIC_EXPRESSION = "expression";

		/**
		 * Dummy node generated for single expressions to make them look like
		 * relations.
		 */
		static final String EMPTY = "empty";

		/**
		 * List of all reserved words. All must be lower case.
		 */
		static final List<String> ALL = Arrays.asList(RESPONSE, STATEMENT,
				ARITHMETIC_EXPRESSION, EMPTY);
	}

	// -----------------------------------------------------------------------
	// Control options
	// -----------------------------------------------------------------------
	public interface OPTIONS
	{

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Knobs
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

		/**
		 * Seems too small for Java double arithmetic, but safe nevertheless.
		 */
		static final double MACHINE_PRECISION = RealUtil.MACHINE_DOUBLE_ERROR;

		// Highest allowed #digits
		static final int MAX_DIGITS = (int) -java.lang.Math
				.floor(java.lang.Math.log10(MACHINE_PRECISION));

		// Default precision tolerance
		static final double DEFAULT_PRECISION_TOL = MACHINE_PRECISION;

		// erroneous-element sub-total score weight in the score formula;
		// must be in [0,1]. Optimized in a previous validation study.
		static final double DEFAULT_ERROR_WEIGHT = 0.7;

		// Edit distance minimization
		public interface EDIT_DISTANCE
		{
			// Cost function parameters
			public interface COST
			{
				// Cost of adding a new node or deleting an existing node
				static final double INSERT_DELETE = 1.0;

				// Cost of deleting an existing node
				// static final double DELETE = 1.0;

				// Nodes are equal (up to a tolerance)
				// Note: is normally 0.
				static final double EQUAL = 0.0;

				// Nodes are unequal but have the same type.
				static final double UNEQUAL_SAME_TYPE = 0.5;

				// Nodes are unequal, have the same type, and are operations
				static final double UNEQUAL_SAME_TYPE_OPERATION = 0.1;

				// Nodes are unequal and have different types
				static final double UNEQUAL_DIFFERENT_TYPE = 0.5;

				// Same as UNEQUAL_DIFFERENT_TYPE, and one of the tokens is an
				// operation
				static final double UNEQUAL_DIFFERENT_TYPE_OPERATION = 0.1;

				// One node is fictitious and the other is not
				static final double UNEQUAL_FICTITIOUS = 1.0;
			}
		}

		// Minimum number of RC iterations in the analysis phase
		static final int DEFAULT_MIN_ANALYSIS_ITERATIONS = 3;

		// Maximum number of RC iterations in the analysis phase
		static final int DEFAULT_MAX_ANALYSIS_ITERATIONS = 8;

		// Minimum reduction factor in edit distance for RC loop break
		// as soon as min # iterations is attained
		static final double EDIT_DISTANCE_MIN_REDUCTION_FACTOR = 0.8;
	}

	// -----------------------------------------------------------------------
	// A parser that recognizes syntax tree toString()s and converts them
	// back to syntax trees - compiler
	// -----------------------------------------------------------------------
	public interface TREE_STRING_COMPILER
	{
		// Fence for children branch repetition
		static final Token CHILDREN_FENCE = new Token("CHILDREN_FENCE");
	}

	// -----------------------------------------------------------------------
	// Arithmetic compiler (performs arithmetic expression matching) constants
	// -----------------------------------------------------------------------
	public interface ARITHMETIC_COMPILER
	{
		// Fence for multiple occurances of unary +- (signOp)
		static final Token SIGNOP_FENCE = new Token("SIGNOP_FENCE");

		// Fence for multiple-nary operations (+\-, *\/)
		static final String MULTINARYOP_FENCE_STRING = "MULTINARYOP_FENCE";

		// Syntax symbol conventions
		static final String PARENTHESIS_OPEN = "(";

		static final String PARENTHESIS_CLOSE = ")";

		static final String UNARY_OP_OPEN = "(";

		static final String UNARY_OP_CLOSE = ")";

		static final String BINARY_FUNC_OPEN = "(";

		static final String BINARY_FUNC_CLOSE = ")";

		static final String BINARY_FUNC_SEPARATOR = ",";

		static final int TEST = ParserNames.ARITHMETIC_COMPILER.TEST;

		/**
		 * This symbol indicates that implicit multiplication is allowed in the
		 * expression.
		 */
		static final char MULT_NOT_REQUIRED = '!';

		/**
		 * This symbol indicates that multiplication symbols are always required
		 * in the expression.
		 */
		static final char MULT_REQUIRED = '*';
	}

	// -----------------------------------------------------------------------
	// Logical compiler (performs logico-arithmetic expression matching)
	// constants
	// -----------------------------------------------------------------------
	public interface LOGICAL_COMPILER
	{
		// Statement separator syntax convention
		static final String STATEMENT_SEPARATOR = ",";

		// Fence for multiple occurances of unary +- (signOp)
		static final Token STATEMENT_FENCE = new Token("SIGNOP_FENCE");

	}

	// -----------------------------------------------------------------------
	// Numerical evaluation phase constants
	// -----------------------------------------------------------------------
	public interface EVALUATOR
	{
		static final int MAX_VARIABLES = 10;

		// Maximum # samples for each variable
		static final int MAX_SAMPLES_PER_VARIABLE = 12;

		// The total # of tensor-product samples is not to exceed this
		static final int MAX_SAMPLES = 1000;
	}

	// -----------------------------------------------------------------------
	// Absolute canonicalization names
	// -----------------------------------------------------------------------
	public interface ABSOLUTE_CANONICALIZATION
	{
		// Redundancy removal handler name
		static final String REDUNANCY_REMOVER = "Redunancy remover";

		// Association switch handler name
		static final String ASSOCIATION_SWITCH = "Association switch";

		// Binary plys redundancy removal handler name
		static final String REDUNANCY_REMOVER_BINARY_PLUS = "Redunancy remover of Binary +";

		// Unary/binary switch handler name
		static final String UNARY_SWITCH = "Unary <-> binary switch";

		// Unary->multinary conversion handler name
		static final String UNARY2MULTINARY = "Unary -> Multinary conversion";

		// Binary->multinary conversion handler name
		static final String BINARY2MULTINARY = "Binary -> Multinary conversion";

		// Multinary collapsing handler name
		static final String MULTINARY_COLLAPSER = "Multinary collapsing + children sorting";

		// Relation transposer
		static final String RELATION_TRANSPOSER = "Relation transposer";
	}

	// -----------------------------------------------------------------------
	// Relative canonicalization names
	// -----------------------------------------------------------------------
	public interface RELATIVE_CANONICALIZATION
	{
		// Bogus handler name
		static final String BOGUS = "Bogus";

		// Commutative multi-nary operation children branch sorting handler name
		static final String SORT_CHIDLREN = "Sort children";
	}

	// -----------------------------------------------------------------------
	// Parser input/output ports
	// -----------------------------------------------------------------------
	public interface PORT
	{
		// -----------------------------------------------------------------------
		// Parser input ports
		// -----------------------------------------------------------------------
		public interface INPUT
		{

		}

		// -----------------------------------------------------------------------
		// Parser output ports
		// -----------------------------------------------------------------------
		public interface OUTPUT
		{
			// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			// ASCII Output port (prints highlighted ASCII string)
			// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			public interface ASCII
			{
				// Wrap each token's status string with these strings
				static final String PARENTHESIS_OPEN = "(";

				static final String PARENTHESIS_CLOSE = ")";

				// Print this separator between highlighted tokens
				static final String SEPARATOR = " ";
			}

			// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			// HTML Output port (prints highlighted HTML string)
			// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			public interface HTML
			{
				// This will be prepended to the class element names of the
				// HTML highlighting span tags. In a web setting, the CSS must
				// declare MTS_<MathTokenStatus.name().toLowerCase()> for all
				// MathTokenStatus types.
				// @see {@link http://foldoc.org/foldoc.cgi?prepend}
				static final String MATH_TOKEN_STATUS_PREFIX = "mts"
						+ CommonNames.MISC.SEPARATOR;

				/**
				 * Print this separator between highlighted tokens.
				 */
				static final String SEPARATOR = "&nbsp;";
			}
		}
	}

	// -----------------------------------------------------------------------
	// Parser request name conventions
	// -----------------------------------------------------------------------
	public interface REQUEST
	{

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Request-scope attribute name conventions
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		interface ATTRIBUTE
		{
			public interface MULTI
			{
				// Refers to multiple objects with the same functionality
				// in the same request (e.g. reference vs. response syntax
				// trees)

				// Prefixes for reference, response data
				static final String REFERENCE_NAME = "Reference";

				static final String RESPONSE_NAME = "Response";
			}

			public interface TOKENIZER
			{
				// Common attribute name prefix for all tokenizer-related
				// attributes
				static final String PREFIX = "tokenizer";

				// Math tokenizer object
				static final String TOKENIZER = PREFIX + "Math";
			}

			public interface ASSEMBLY
			{
				// Common attribute name prefix for all assembly-related
				// attributes
				static final String PREFIX = "assembly";

				// Element assembly created using a tokenizer
				// Type: MathAssembly
				static final String TOKENIZER = PREFIX + "Tokenizer";

				// Element assembly modified during the parser's matching phase
				// Type: MathAssembly
				// static final String MATCHING = PREFIX + "Matching";
			}

			public interface ARITHMETIC
			{
				// Common attribute name prefix for all arithmetic
				// parser-related attributes
				static final String PREFIX = "arithmetic";

				// Arithmetic parser object prepared by the arithmetic compiler
				// Type: ArithmeticParser
				static final String PARSER = PREFIX + "Parser";

				// Did arithmetic matching find a complete match
				static final String COMPLETE_MATCH = PREFIX + "CompleteMatch";
			}

			public interface LOGICAL
			{
				// Common attribute name prefix for all logical
				// parser-related attributes
				static final String PREFIX = "logical";

				// Logical parser object prepared by the logical compiler
				// Type: ArithmeticParser
				static final String PARSER = PREFIX + "Parser";

				// Did logical matching find a complete match
				static final String COMPLETE_MATCH = PREFIX + "CompleteMatch";
			}

			public interface PARAMETRIC_EVALUATION
			{
				// Common attribute name prefix for all parametric evaluation
				// parser-related attributes
				static final String PREFIX = "param";

				// Arithmetic parser object prepared by the parametric
				// evaluation compiler. Type: ParametricEvaluationParser
				static final String PARSER = PREFIX + "Parser";

				// Evaluated string
				static final String EVAL_STRING = PREFIX + "EvaluatedString";
			}

			public interface TARGET
			{
				// Common attribute name prefix for all target
				// parser-related attributes
				static final String PREFIX = "target";

				// The target produced by the arithmetic and logical parsers
				static final String MATH = PREFIX + "Math";

				// Two targets required by relative processes (e.g. relative
				// canonicalization)
				static final String REFERENCE = PREFIX
						+ ParserNames.REQUEST.ATTRIBUTE.MULTI.REFERENCE_NAME;

				static final String RESPONSE = PREFIX
						+ ParserNames.REQUEST.ATTRIBUTE.MULTI.RESPONSE_NAME;

				static final String PARAMETRIC_EVALUATION = PREFIX
						+ "ParametricEvaluation";
			}

			public interface SYNTAX_TREE
			{
				// A syntax tree to be processed in all single-tree handlers
				// (e.g. absolute canonicalization, evaluation)
				// static final String SINGLE =
				// "absolutelyCanonicalizerTree";

				// Common attribute name prefix for all syntax tree related
				// parser-related attributes
				static final String PREFIX = "syntaxTree";

				// Two syntax trees (reference, response) to be numerically
				// compared and relatively canonicalized with respect to each
				// other

				static final String REFERENCE = PREFIX
						+ ParserNames.REQUEST.ATTRIBUTE.MULTI.REFERENCE_NAME;

				static final String RESPONSE = PREFIX
						+ ParserNames.REQUEST.ATTRIBUTE.MULTI.RESPONSE_NAME;
			}

			public interface EVALUATOR
			{
				// Common attribute name prefix for all evaluation-related
				// attributes
				static final String PREFIX = "evaluator";

				// Numerical evaluation result
				static final String RESULT = PREFIX + "Result";

				// Variable configuration for which the reference, response are
				// different (or equal, if they are equivalent expressions)
				static final String SAMPLE = PREFIX + "Samples";
			}

			public interface ANALYSIS
			{
				// Common attribute name prefix for all analysis-related
				// attributes
				static final String PREFIX = "analysis";

				// Currently active marker (only one can exist at one time)
				static final String LATEST_MARKER = PREFIX + "Latest"
						+ "Marker";

				// Element marker result object
				static final String LATEST_RESULT = PREFIX + "Latest"
						+ "Result";
			}

			public interface MARKER
			{
				// Common attribute name prefix for all marker-related
				// attributes
				static final String PREFIX = "marker";

				// Marker type to use in the next request handling by a
				// ATPMEditDistanceHandler
				static final String MARKER_TYPE = PREFIX + "MarkerType";

				// Element marker result object
				static final String ELEMENTS_RESULT = PREFIX + "Elements"
						+ "Result";

				// Tree pattern marker result object
				static final String ATPM_RESULT = PREFIX + "ATPM" + "Result";

				// Edit distance computer required for relative
				// canonicalization
				static final String EDIT_DISTANCE_COMPUTER = PREFIX + "EDC";

				// Response target after marker processing
				static final String RESPONSE_TARGET = PREFIX + "ResponseTarget";
			}

			// Relative Canonicalization
			public interface RELATIVE_CANONICALIZATION
			{
				static final String SUB_PREFIX = "relCan";

				// =====================
				// RC loop controls
				// =====================
				public interface RC_LOOP
				{
					static final String SUB_SUB_PREFIX = RELATIVE_CANONICALIZATION.SUB_PREFIX
							+ "RCLoop";

					// loop counter
					static final String COUNTER = RELATIVE_CANONICALIZATION.SUB_PREFIX
							+ "Counter";

					// Min # iterations
					static final String MIN_ITERATIONS = RELATIVE_CANONICALIZATION.SUB_PREFIX
							+ "MinIterations";

					// Max # iterations
					static final String MAX_ITERATIONS = RELATIVE_CANONICALIZATION.SUB_PREFIX
							+ "MaxIterations";

					// Iteration index of best results
					static final String BEST_ITERATION = RELATIVE_CANONICALIZATION.SUB_PREFIX
							+ "BestIteration";

					// Best ED calculated to date
					static final String BEST_EDIT_DISTANCE = RELATIVE_CANONICALIZATION.SUB_PREFIX
							+ "BestEditDistance";

					// Best response target calculated to date
					static final String BEST_RESPONSE_TARGET = RELATIVE_CANONICALIZATION.SUB_PREFIX
							+ "BestResponseTarget";

					// Best analysis result calculated to date
					static final String BEST_ANALYSIS_RESULT = RELATIVE_CANONICALIZATION.SUB_PREFIX
							+ "AnalysisResult";
				}
			}

			public interface SCORE
			{
				// Common attribute name prefix for all scoring-related
				// attributes
				static final String PREFIX = "score";

				// final response score
				static final String SCORE = PREFIX + "Score";
			}

			// Parser I/O ports
			public interface PORT
			{
				// Parser input ports
				public interface INPUT
				{

				}

				// Parser output ports
				public interface OUTPUT
				{
					public interface ASCII
					{
						// Common attribute name prefix for all
						// ASCII-printer-related
						// attributes
						static final String PREFIX = "portOutputAscii";

						// Output string
						static final String STRING = PREFIX + "String";
					}

					public interface HTML
					{
						// Common attribute name prefix for all
						// HTML-printer-related
						// attributes
						static final String PREFIX = "portOutputHtml";

						// Output string
						static final String STRING = PREFIX + "String";

						// Output reference string
						static final String REFERENCE_STRING = STRING
								+ "Reference";

						// Output response string
						static final String RESPONSE_STRING = PREFIX
								+ "Response";
					}

					public interface IMAGE
					{
						// Common attribute name prefix for all
						// HTML-printer-related
						// attributes
						static final String PREFIX = "portOutputImage";

						// Output syntax tree image for reference string
						static final String TREE_REFERENCE = PREFIX
								+ "TreeImage"
								+ ParserNames.REQUEST.ATTRIBUTE.MULTI.REFERENCE_NAME;

						// Output syntax tree image for response string
						static final String TREE_RESPONSE = PREFIX
								+ "TreeImage"
								+ ParserNames.REQUEST.ATTRIBUTE.MULTI.RESPONSE_NAME;

						// Output syntax tree image size for reference string
						static final String TREE_SIZE_REFERENCE = PREFIX
								+ "TreeImageSize"
								+ ParserNames.REQUEST.ATTRIBUTE.MULTI.REFERENCE_NAME;

						// Output syntax tree image size for response string
						static final String TREE_SIZE_RESPONSE = PREFIX
								+ "TreeImageSize"
								+ ParserNames.REQUEST.ATTRIBUTE.MULTI.RESPONSE_NAME;
					}

					public interface MATHML
					{
						/**
						 * Common attribute name prefix for all
						 * MATHML-export-related attributes.
						 */
						static final String PREFIX = "portOutputMathML";

						/**
						 * Output string in MathML format.
						 */
						static final String STRING = PREFIX + "String";
					}

				}

			}

			// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			// Request-scope tokens
			// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			public interface TOKEN
			{
				/**
				 * Prefix for tokens that indicate that a certain item type's
				 * drop down menu populated.
				 */
				static final String FOUND_ITEM_PREFIX = "found";
			}

		}

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Error message attribute names
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		public interface ERROR
		{

		}
	} // interface REQUEST

	// -----------------------------------------------------------------------
	// Parser request handler name conventions
	// -----------------------------------------------------------------------
	public interface HANDLER
	{
		// Adapters
		public interface ADAPTER
		{
			static final String NAME = "[Adapter]";
		}

		// Decorators
		public interface DECORATOR
		{
			static final String NAME = "[Decorator]";
		}

		// Processors
		public interface PROCESSOR
		{
			static final String NAME = "[Processor]";
		}

		// Services
		public interface SERVICE
		{
			static final String NAME = "[Service]";
		}

		// I/O ports
		public interface PORT
		{
			// Input ports
			static final String INPUT = "[Input port]";

			// Output ports
			static final String OUTPUT = "[Output port]";
		}

		// Analysis procedures
		public interface ANALYSIS
		{
			// Absolute Canonicalization
			public interface ABSOLUTE_CANONICALIZATION
			{
				static final String NAME = "[AbsCan]";
			}

			// Relative Canonicalization
			public interface RELATIVE_CANONICALIZATION
			{
				static final String NAME = "[RelCan]";
			}
		}
	}

	// -----------------------------------------------------------------------
	// Application resource keys & messages used in error messages
	// -----------------------------------------------------------------------
	public interface KEY
	{
		// #################################################################
		// Math parser exception keys
		// #################################################################
		public interface MATH_EXCEPTION
		{
			static final String EXCEPTION = "error.MathParserException";

			static final String UNARY_OP = "error.MathParserException.unaryOp";

			static final String BINARY_FUNC = "error.MathParserException.binaryFunc";

			static final String COMPLEX_CONSTANT = "error.MathParserException.complexConstant";

			static final String REAL_CONSTANT = "error.MathParserException.realConstant";

			static final String CONSTANT = "error.MathParserException.constant";

			static final String IMPLICIT_MULTIPLICATION = "error.MathParserException.implicitMultiplication";

			static final String SEQUENCE_TRACK = "error.MathParserException.sequenceTrack";

			/**
			 * Cannot add a new variable.
			 */
			static final String INVALID_VARIABLE = "error.MathParserException.invalidVariable";

			static final String VARIABLE_NOT_FOUND = "error.MathParserException.variableNotFound";

			/**
			 * Unsupported operation on this domain of numbers (e.g. division on
			 * Integers).
			 */
			static final String UNSUPPORTED_OPERATION = "error.UnsupportedOpException.unsupportedOperation";

			/**
			 * Cannot numerically evaluate an expression.
			 */
			static final String INEVALUABLE = "error.UnsupportedOpException.inevaluable";

		}
	}

	// -----------------------------------------------------------------------
	// Application resource keys & messages used in error messages
	// -----------------------------------------------------------------------
	public interface PARAMETRIC_EVALUATION
	{
		// String to use for expressions that are not matched by the arithmetic
		// parsing during parametric string evaluation
		static final String UNKNOWN_EXPRESSION = "???";
	}
}
