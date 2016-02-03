/*****************************************************************************************
 * Source File: PlotTree.java
 ****************************************************************************************/
package net.ruready.parser.port.output.image.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import net.ruready.common.draw.Circle;
import net.ruready.common.draw.Triplet;
import net.ruready.common.math.basic.IntegerPair;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.tree.ListTreeNode;
import net.ruready.parser.arithmetic.entity.mathvalue.ParenthesisValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.rounder.NumericalRounder;
import net.ruready.parser.arithmetic.entity.numericalvalue.rounder.NumericalRounderFactory;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.port.output.manager.TargetPrinter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PlotTree extends JPanel implements TargetPrinter
{
	// ========================= CONSTANTS =================================

	// ----------------------------------
	// Public constants
	// ----------------------------------

	// Maximum sizes for the chart. Not strictly enforced, though.
	public static final int MAX_WIDTH = 450;

	public static final int MAX_HEIGHT = 500;

	// ----------------------------------
	// Private constants
	// ----------------------------------

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1;

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(PlotTree.class);

	// Generates node colors
	private static final AbstractColorFactory colorFactory = new DefaultColorFactory();

	// Size of circle drawn per node
	private static final int CIRCLE_SIZE = 15;

	// Text font size for node data
	private static final int FONT_SIZE = 20;

	// Maximum string size to show for a node
	private static final int MAX_STRING_SIZE = 3;

	// Default unit length on the chart
	private static final Point DEFAULT_UNIT = new Point(30, 40);

	// Minimum allowed unit length on the chart.
	// Strictly reinforced.
	private static final Point MIN_UNIT = new Point((int) java.lang.Math
			.ceil(3 * CIRCLE_SIZE), (int) java.lang.Math.ceil(3 * CIRCLE_SIZE));

	// Tolerance for rounding numbers when printing nodes
	private static final double TOL = 1e-3;

	// ========================= FIELDS ====================================

	// ----------------------------------
	// Required input
	// ----------------------------------

	// target whose syntax tree is drawn
	private final MathTarget target;

	// Output stream to write to
	private BufferedImage output;

	// ----------------------------------
	// Useful local variables
	// ----------------------------------

	// Syntax tree to plot
	private final SyntaxTreeNode tree;

	// List of tokens from the target
	// private final List<Token> tokens;

	// Graphics panel
	private Graphics2D g;

	// non-dimensional coordinates to draw nodes at.
	// first two are the left and right extension edges
	// of a node, the the third one is the depth.
	private ListTreeNode<Triplet> coords = new ListTreeNode<Triplet>(null);

	// Counter for leaves encountered in the tree
	// during pre-traversal in prepareCoords()
	private int leafCounter;

	// Tree depth at a node
	private int depth;

	// Width of tree (non-dimensional)
	private int treeWidth;

	// Height of tree (non-dimensional)
	private int treeHeight;

	// Actual unit size chosen based on max width and
	// minimum unit size
	private Point unit;

	// Width of chart
	private int chartWidth;

	// Height of chart
	private int chartHeight;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Create an initialize tree charter.
	 * 
	 * @param target
	 *            target whose syntax tree is plotted
	 */
	public PlotTree(MathTarget target)
	{
		this.target = target;
		this.tree = target.getSyntax();
		// this.tokens = target.getTokens();

		logger.debug("=============== PLOTTING TREE ===============");
		logger.debug("tree " + tree);
		logger.debug(target.toStringDetailed());

		// Create and cache the image
		this.drawTree();
	}

	// ========================= IMPLEMENTATION: TargetPrinter =============

	/**
	 * @see net.ruready.parser.port.output.exports.ParserOutputPort#getOutput()
	 */
	public final BufferedImage getOutput()
	{
		return output;
	}

	// ========================= PUBLIC METHODS ============================

	/**
	 * Prepare node drawing coordinates and compute optimal unit length and chart size.
	 * 
	 * @param t
	 *            tree to draw
	 * @return size of chart
	 */
	public IntegerPair getChartSize()
	{
		// logger.debug("prepare()");
		depth = 0;
		coords = this.prepareCoords(tree);
		unit = this.prepareUnit();
		// logger.debug("chartWidth "+chartWidth+
		// " chartHeight "+chartHeight);
		return new IntegerPair(chartWidth, chartHeight);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Draw a tree chart, redirect output to an output stream. prepare() should be called
	 * before this method.
	 * 
	 * @param out
	 *            output stream to write chart to
	 * @param tree
	 *            the tree to draw
	 */
	private void drawTree()
	{
		// Walk on tree and compute
		// non-dimensional coordinates to draw nodes at
		this.getChartSize();
		try
		{
			// Create output image
			output = new BufferedImage(chartWidth, chartHeight,
					BufferedImage.TYPE_INT_ARGB_PRE);
			g = (Graphics2D) output.getGraphics();
			// White background, black pen color
			g.setColor(new Color(255, 255, 255));
			g.fillRect(0, 0, chartWidth, chartHeight);
			g.setColor(Color.BLACK);
			// Font font = new Font("Arial", 0, 20);
			// g.setFont(font);

			// Draw the tree
			drawTree(tree, coords);

			// Write png image to output stream
			// logger.debug("Writing png image to out stream");
			// ImageIO.write(img, "png", output);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void drawTree(SyntaxTreeNode currentTree, ListTreeNode<Triplet> currentCoords)
	{
		// Get this node's data
		int nChilds = currentTree.getNumChildren();
		MathToken node = currentTree.getData();
		Triplet coord = currentCoords.getData();
		Point thisPoint = nodePoint(coord, unit);
		// logger.debug("node " + node + " thisPoint " + thisPoint);

		// Draw lines from this node to all its children
		// logger.debug("drawing lines, t="+t.getData());
		for (int i = 0; i < nChilds; i++)
		{
			Triplet childCoord = currentCoords.getChild(i).getData();
			Point childPoint = nodePoint(childCoord, unit);
			// logger.debug("i="+i+" childPoint="+childPoint+"
			// thisPoint="+thisPoint);
			g.draw(new Line2D.Double(childPoint, thisPoint));
		}
		// logger.debug("visitTo("+t.getData()+")");

		// Draw this node
		drawNode(thisPoint, node);

		// Process children (pre-traversal order)
		for (int i = 0; i < nChilds; i++)
		{
			this.drawTree(currentTree.getChild(i), currentCoords.getChild(i));
		}
	}

	/**
	 * Print an html-encoded, abbreviated string representing this operation. This is used
	 * in tree plots.
	 * 
	 * @return html-encoded abbreviated string representing this operation
	 */
	@SuppressWarnings("unchecked")
	private String encodeNode(MathToken mt)
	{
		switch (mt.getValueID())
		{
			case ARITHMETIC_NUMBER:
			{
				// Make sure that d is printed as an integer
				// if it is [close to] one.
				NumericalValue numericalValue = (NumericalValue) mt.getValue();
				ArithmeticMode arithmeticMode = target.getArithmeticMode();
				NumericalRounder<NumericalValue> rounder = new NumericalRounderFactory()
						.createType(arithmeticMode);
				// NumericalFormat<NumericalValue> formatter = new
				// NumericalFormatFactory()
				// .createType(arithmeticMode);
				// return formatter.format(rounder.round(numericalValue, TOL));
				return rounder.round(numericalValue, TOL).toString();
			}

			case ARITHMETIC_CONSTANT:
			{
				// Print the constant's string representation, not value
				return CommonNames.MISC.EMPTY_STRING + mt.getValue();
			}

				// case RELATION_OP:
			case ARITHMETIC_PARENTHESIS:
			{
				return CommonNames.MISC.EMPTY_STRING + ParenthesisValue.PARENTHESIS;
			}

			case DISCARDED:
			{
				String s = "Discarded";
				s = s.substring(0, Math.min(MAX_STRING_SIZE, s.length()))
						+ ((s.length() > MAX_STRING_SIZE) ? "."
								: CommonNames.MISC.EMPTY_STRING);
				return s;
			}

			default:
			{
				// All other token types: print value of
				// token, or if no value (discarded tokens)
				// use "Discarded".
				String s;
				if (mt.getValue() == null)
				{
					// Something went wrong during parsing,
					// display a question mark...
					s = "?";
				}
				else
				{
					s = mt.getValue().toString(); // mt.getToken(0).sval();
				}
				s = s.substring(0, Math.min(MAX_STRING_SIZE, s.length()))
						+ ((s.length() > MAX_STRING_SIZE) ? "."
								: CommonNames.MISC.EMPTY_STRING);
				return s;
			}

		} // switch (mt.getValueID())
	}

	/**
	 * Compute non-dimensional coordinates to draw nodes at. Bottom left corner of tree is
	 * at (0,0). Distance between nodes is 1 in both x and y.
	 * 
	 * @param t
	 *            tree to draw
	 * @return non-dimensional coordinates to draw nodes at
	 */
	private ListTreeNode<Triplet> prepareCoords(SyntaxTreeNode t)
	{
		// logger.debug("prepareCoords(), t = "+t+" depth "+depth);
		ListTreeNode<Triplet> coordsBeingBuilt = new ListTreeNode<Triplet>(null);
		// left edge of of a node's x-domain ("extension")
		int left;
		// right edge of of a node's x-domain ("extension")
		int right;
		if (depth == 0)
		{
			// This is the root node, initialize counters
			leafCounter = -1;
			treeWidth = 0;
			treeHeight = 0;
		}

		int nChilds = t.getNumChildren();
		if (nChilds == 0)
		{
			// This is a leaf, extension size is 0
			leafCounter++;
			left = leafCounter;
			right = leafCounter;
		}
		else
		{
			// This is a node, process children first
			// (pre-traversal order)
			depth++;
			for (int i = 0; i < nChilds; i++)
			{
				// logger.debug("Child #" + i + ": " + t.getChild(i));
				coordsBeingBuilt.addChild(this.prepareCoords(t.getChild(i)));
			}
			depth--;
			// Compute extension coordinates from first
			// and last childs
			left = (coordsBeingBuilt.getChild(0)).getData().getX();
			right = (coordsBeingBuilt.getChild(nChilds - 1)).getData().getY();
		}

		// Set coordinate of this node
		Triplet tp = new Triplet(left, right, depth);
		// logger.debug("Node " + t.getData() + " Location = " + tp);
		coordsBeingBuilt.setData(tp);
		// Update width, height
		if (right >= treeWidth)
		{
			treeWidth = right + 1;
		}
		if (depth >= treeHeight)
		{
			treeHeight = depth + 1;
		}
		// logger.debug("prepareCoords() end");
		return coordsBeingBuilt;
	}

	/**
	 * Compute draw position from based on the tree width and height and chart & unit
	 * constraints.
	 * 
	 * @return optimal unit length
	 */
	private Point prepareUnit()
	{
		Point unitBeingBuilt = DEFAULT_UNIT;

		int proposedWidth = treeWidth * unitBeingBuilt.x;
		if (proposedWidth > MAX_WIDTH)
		{
			// Too big, Rescale chart to MAX_WIDTH
			unitBeingBuilt.x = (int) java.lang.Math.floor((unitBeingBuilt.x * MAX_WIDTH)
					/ proposedWidth);
		}
		if (unitBeingBuilt.x < MIN_UNIT.x)
		{
			// Unit too small, rescale back to minimum unitBeingBuilt,
			// even if chart is going to be too big.
			unitBeingBuilt.x = MIN_UNIT.x;
		}

		int proposedHeight = treeHeight * unitBeingBuilt.y;
		if (proposedHeight > MAX_HEIGHT)
		{
			// Too big, Rescale chart to MAX_WIDTH
			unitBeingBuilt.y = (int) java.lang.Math.floor((unitBeingBuilt.y * MAX_HEIGHT)
					/ proposedHeight);
		}
		if (unitBeingBuilt.y < MIN_UNIT.y)
		{
			// Unit too small, rescale back to minimum unitBeingBuilt,
			// even if chart is going to be too big.
			unitBeingBuilt.y = MIN_UNIT.y;
		}

		// Make sure unitBeingBuilt is even
		unitBeingBuilt = new Point(2 * ((int) java.lang.Math.ceil(unitBeingBuilt.x / 2)),
				2 * ((int) java.lang.Math.ceil(unitBeingBuilt.y / 2)));

		// Compute chart width and height
		chartWidth = treeWidth * unitBeingBuilt.x;
		chartHeight = treeHeight * unitBeingBuilt.y;

		return unitBeingBuilt;
	}

	/**
	 * Draw a math token (inscribed in a circle, with bg/text colors matched to its
	 * status).
	 * 
	 * @param pen
	 *            location to draw the token
	 * @param mt
	 *            math token
	 */
	private void drawNode(Point pen, MathToken mt)
	{
		// logger.debug("drawNode("+mt+" "+mt.getStatus()+")");
		// ==== TESTING - NO NODE BACKGROUND COLORS ====
		// final MathTokenStatus status = MathTokenStatus.DISCARDED;
		final MathTokenStatus status = mt.getStatus();
		Circle circle = new Circle(pen.x, pen.y, CIRCLE_SIZE, colorFactory
				.textColor(status), colorFactory.backgroundColor(status));
		circle.draw(g);
		Color oldColor = g.getColor();
		g.setColor(colorFactory.textColor(status));
		String s = encodeNode(mt);

		// Offset for writing text inside the circle
		// representing this node
		int offsetY = 6;
		int offsetX = 0;
		int fontSize = FONT_SIZE;

		switch (s.length())
		{
			case 1:
			{
				offsetX = 5;
				fontSize = FONT_SIZE;
				break;
			}
			case 2:
			{
				offsetX = 7;
				fontSize = FONT_SIZE - 2;
				break;
			}
			case 3:
			{
				offsetX = 9;
				fontSize = FONT_SIZE - 4;
				break;
			}
			default:
			{
				offsetX = 11;
				fontSize = FONT_SIZE - 6;
				break;
			}
		}

		switch (mt.getValueID())
		{
			case ARITHMETIC_PARENTHESIS:
			{
				fontSize = FONT_SIZE;
				// offsetY -= 3;
				offsetX += 1;
				break;
			}
			default:
			{
				break;
			}
		}

		g.setFont(new Font("Arial", Font.BOLD, fontSize));
		g.drawString(s, pen.x - offsetX, pen.y + offsetY);
		g.setColor(oldColor);
	}

	// ========================= STATIC METHODS ============================

	/**
	 * Compute drawing point from a triplet (left, right, depth).
	 * 
	 * @param t
	 *            triplet representing a tree node, (left, right, depth).
	 * @param u
	 *            unit length in x and y
	 * @return drawing point
	 */
	private static Point nodePoint(Triplet t, Point u)
	{
		double nonDimX = 0.5 * (t.getX() + t.getY() + 1);
		double nonDimY = t.getZ() + 0.5;
		int x = (int) java.lang.Math.round(nonDimX * u.x);
		int y = (int) java.lang.Math.round(nonDimY * u.y);
		return new Point(x, y);
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return Returns the chartHeight.
	 */
	public int getChartHeight()
	{
		return chartHeight;
	}

	/**
	 * @return Returns the chartWidth.
	 */
	public int getChartWidth()
	{
		return chartWidth;
	}

	/**
	 * @return Returns the unit.
	 */
	public Point getUnit()
	{
		return unit;
	}

}
