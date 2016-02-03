/*****************************************************************************************
 * Source File: TestingNames.java
 ****************************************************************************************/
package test.ruready.parser.rl;

import java.io.File;

/**
 * This interface centralizes constants, labels and names used throughout the testing
 * component.
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
public interface TestingNames
{
	// ========================= CONSTANTS =================================

	// If no trailing slash is attached, file names are relative to the
	// "classes" directory of the application.

	// -----------------------------------------------------------------------
	// Chain pattern
	// -----------------------------------------------------------------------
	public interface CHAIN
	{

		// -----------------------------------------------------------------------
		// Chain request name conventions
		// -----------------------------------------------------------------------
		public interface REQUEST
		{

			// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			// Request-scope attribute name conventions
			// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			public interface ATTRIBUTE
			{

				// Purchasing amount in the company CoR test
				static final String AMOUNT = "amount";

				// Who approved the purchasing amount
				static final String APPROVED = "approved";
			}

			// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			// Error message attribute names
			// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			public interface ERROR
			{

			}
		}
	}

	// -----------------------------------------------------------------------
	// Test data file names
	// -----------------------------------------------------------------------
	public interface FILE
	{

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Test file reader parameter name conventions
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		public interface PARAMS
		{
			// Stop on first error found in the file or not; boolean
			static final String STOP_ON_FIRST_ERROR = "stopOnFirstError";
		}

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Directory names
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// test home directory; relative to project directory
		public interface DIR
		{
			static final String TEST = "data" + File.separator + "test";

			// ============================
			// Parser Component
			// ============================
			// Parser test suite directory structure
			public interface PARSER
			{
				static final String PARSER = TEST + File.separator + "parser";

				// Directories of each parser module tests; relative to project
				// directory

				// Arithmetic parser
				static final String ARITHMETIC = PARSER + File.separator + "arithmetic";

				// Logical parser
				static final String LOGICAL = PARSER + File.separator + "logical";

				// Parametric evaluation parser
				static final String PARAMETRIC_EVALUATION = PARSER + File.separator
						+ "param";

				// Absolute canonicalization
				static final String ABSOLUTE = PARSER + File.separator + "absolute";

				// Markers
				static final String MARKER = PARSER + File.separator + "marker";

				// Analysis phase
				static final String ANALYSIS = PARSER + File.separator + "analysis";

				// ATPM algorithms
				static final String ATPM = PARSER + File.separator + "atpm";

				// Numerical evaluation and comparison
				static final String EVALUATOR = PARSER + File.separator + "evaluator";

				// Relative canonicalization
				static final String RELATIVE = PARSER + File.separator + "relative";

				// Parser input/output port tests
				public interface PORT
				{
					// Port main test directory
					static final String PORT = PARSER + File.separator + "port";

					// Input port tests
					public interface INPUT
					{
						// Input port test directory
						static final String INPUT = PORT + File.separator + "input";

						// Input Port: MathML input
						static final String MATHML = INPUT + File.separator + "mathml";
					}

					// Output port tests
					public interface OUTPUT
					{
						// Output port test directory
						static final String OUTPUT = PORT + File.separator + "output";

						// Output Port: text printers
						static final String TEXT = OUTPUT + File.separator + "text";

						// Output Port: MathML input
						static final String MATHML = OUTPUT + File.separator + "mathml";
					}
				}

				// Converters: string -> tree
				public interface CONVERTER
				{
					// Converter main test directory
					static final String CONVERTER = PARSER + File.separator + "converter";

					// Syntax tree converter tests
					public interface SYNTAX
					{
						// Input port test directory
						static final String SYNTAX = CONVERTER + File.separator
								+ "syntax";
					}

					// Word tree converter tests
					public interface WORD
					{
						// Input port test directory
						static final String WORD = CONVERTER + File.separator + "word";
					}

					// Matrix reader
					public interface MATRIX
					{
						// Input port test directory
						static final String MATRIX = CONVERTER + File.separator
								+ "matrix";
					}
				}

				// Exports/prepared processors test directory
				static final String EXPORTS = PARSER + File.separator + "exports";

			} // interface DIR.PARSER

		} // interface DIR

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Test data file names
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

		// ============================
		// Parser Component
		// ============================
		// Parser test suite directory structure
		public interface PARSER
		{
			// Arithmetic parser test file names
			public interface ARITHMETIC
			{
				// Arithmetic matching data file name
				static final String MATCHER = DIR.PARSER.ARITHMETIC + File.separator
						+ "ArithmeticMatcher.dat";

				// Arithmetic matching syntax tree structure data file name
				static final String MATCHER_SYNTAX = DIR.PARSER.ARITHMETIC
						+ File.separator + "ArithmeticMatcherSyntax.dat";
			}

			// Logical parser test file names
			public interface LOGICAL
			{
				// Arithmetic matching data file name
				static final String MATCHER = DIR.PARSER.LOGICAL + File.separator
						+ "LogicalMatcher.dat";

				// Arithmetic matching syntax tree structure data file name
				static final String MATCHER_SYNTAX = DIR.PARSER.LOGICAL + File.separator
						+ "LogicalMatcherSyntax.dat";
			}

			// Numerical evaluation test file naes
			public interface EVALUATOR
			{
				public interface ARITHMETIC
				{
					// Numerical evaluation data file name
					static final String EVALUATOR = DIR.PARSER.EVALUATOR + File.separator
							+ "ArithmeticEvaluator.dat";

					// Numerical comparison data file name
					static final String COMPARISON = DIR.PARSER.EVALUATOR
							+ File.separator + "ArithmeticNumericalComparison.dat";
				}

				public interface LOGICAL
				{
					// Numerical evaluation data file name
					static final String EVALUATOR = DIR.PARSER.EVALUATOR + File.separator
							+ "LogicalEvaluator.dat";

					// Numerical comparison data file name
					static final String COMPARISON = DIR.PARSER.EVALUATOR
							+ File.separator + "LogicalNumericalComparison.dat";
				}
			}

			// Parametric evaluation parser test file names
			public interface PARAMETRIC_EVALUATION
			{
				// Arithmetic matching data file name
				static final String PARAMETRIC_EVALUATION = DIR.PARSER.PARAMETRIC_EVALUATION
						+ File.separator + "ParametricEvaluation.dat";
			}

			// Absolute canonicalization test file naes
			public interface ABSOLUTE
			{
				// Redundancy remover file name
				static final String REDUNDANCY_REMOVER = DIR.PARSER.ABSOLUTE
						+ File.separator + "RedundancyRemover.dat";

				// Association switch file name
				static final String ASSOCIATION_SWITCH = DIR.PARSER.ABSOLUTE
						+ File.separator + "AssociationSwitch.dat";

				// Binary + Redundancy remover file name
				static final String REDUNDANCY_REMOVER_BINARY_PLUS = DIR.PARSER.ABSOLUTE
						+ File.separator + "RedundancyRemoverBinaryPlus.dat";

				// Unary switch file name
				static final String UNARY_SWITCH = DIR.PARSER.ABSOLUTE + File.separator
						+ "UnarySwitch.dat";

				// Unary -> multinary converter file name
				static final String UNARY2MULTINARY = DIR.PARSER.ABSOLUTE
						+ File.separator + "Unary2MultinaryConverter.dat";

				// Binary -> multinary converter file name
				static final String BINARY2MULTINARY = DIR.PARSER.ABSOLUTE
						+ File.separator + "Binary2MultinaryConverter.dat";

				// Multinary collapser file name
				static final String MULTINARY_COLLAPSER = DIR.PARSER.ABSOLUTE
						+ File.separator + "MultinaryCollapser.dat";

				// Relation transposer file name
				static final String RELATION_TRANSPOSER = DIR.PARSER.ABSOLUTE
						+ File.separator + "RelationTransposer.dat";

				// Testing the entire absolute canonicalization phase - file
				// name
				static final String ABSOLUTE = DIR.PARSER.ABSOLUTE + File.separator
						+ "AbsoluteCanonicalization.dat";
			}

			// Relative canonicalization names
			public interface RELATIVE
			{
				// Bogus handler test data file name
				static final String BOGUS = DIR.PARSER.RELATIVE + File.separator
						+ "Bogus.dat";

				// Commutative multi-nary operation children branch sorting
				// handler test data file name
				static final String SORT_CHIDLREN = DIR.PARSER.RELATIVE + File.separator
						+ "SortChildren.dat";

				// Computing a tree's commutative depth -
				// test data file name
				static final String TREE_COMMUTATIVE_DEPTH = DIR.PARSER.RELATIVE
						+ File.separator + "TreeCommutativeDepth.dat";
			}

			// ATPM test file names
			public interface ATPM
			{
				// String tree edit distance & nodal mapping computation test
				// file name
				static final String EDIT_DISTANCE_STRING = DIR.PARSER.ATPM
						+ File.separator + "EditDistanceString.dat";

				// Syntax tree edit distance & nodal mapping computation test
				// file name
				static final String EDIT_DISTANCE_SYNTAX = DIR.PARSER.ATPM
						+ File.separator + "EditDistanceSyntax.dat";
			}

			// Marker test file naes
			public interface MARKER
			{
				// Element marker test file name
				static final String ELEMENT = DIR.PARSER.MARKER + File.separator
						+ "ElementMarker.dat";
			}

			// Parser input/output port test file names
			public interface PORT
			{
				// Input port tests
				public interface INPUT
				{
					// Input port: MathML content input source (port) file name
					static final String MATHML = DIR.PARSER.PORT.INPUT.MATHML
							+ File.separator + "MathMLInputPort.dat";
				}

				// Output port tests
				public interface OUTPUT
				{
					// Output port: ASCII printer file name
					static final String ASCII_PRINTER = DIR.PARSER.PORT.OUTPUT.TEXT
							+ File.separator + "AsciiPrinter.dat";

					// Output port: HTML printer file name
					static final String HTML_PRINTER = DIR.PARSER.PORT.OUTPUT.TEXT
							+ File.separator + "HtmlPrinter.dat";

					// Input port: MathML content output port file name
					static final String MATHML = DIR.PARSER.PORT.OUTPUT.MATHML
							+ File.separator + "MathMLOutputPort.dat";
				}
			}

			// Converters: string -> tree
			public interface TREE
			{
				// Syntax tree conversions
				public interface SYNTAX
				{
					static final String SYNTAX_PARSER = DIR.PARSER.CONVERTER.SYNTAX.SYNTAX
							+ File.separator + "SyntaxTreeNodeParser.dat";
				}

				// Word tree conversions
				public interface WORD
				{
					static final String WORD_PARSER = DIR.PARSER.CONVERTER.WORD.WORD
							+ File.separator + "WordTreeNodeParser.dat";
				}
			}

			// Exports/prepared processors test file names
			public interface EXPORTS
			{
				// Math parser demo processor file name
				static final String MATH_PARSER_PROCESSOR = DIR.PARSER.EXPORTS
						+ File.separator + "MathParserDemoProcessor.dat";
			}
		} // interface FILE.PARSER
	} // interface FILE

	// -----------------------------------------------------------------------
	// String conventions in test data files
	// -----------------------------------------------------------------------
	public interface STRING
	{
		// For reference vs. response expression tests - separator
		static final String REFERENCE_RESPONSE_SEPARATOR = "~";
	}
}
