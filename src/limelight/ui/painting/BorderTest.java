//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.painting;

import junit.framework.TestCase;
import limelight.styles.FlatStyle;
import limelight.styles.Style;
import limelight.util.Box;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;

public class BorderTest extends TestCase
{
  private Style style;
  private Border border;
  private Box insideMargin;

  public void setUp() {
    style = new FlatStyle();
    insideMargin = new Box(0, 0, 100, 200);
  }

  private void verifyLine(Line2D line, int x1, int y1, int x2, int y2) {
    assertEquals(x1, line.getX1(), 0.1);
    assertEquals(y1, line.getY1(), 0.1);
    assertEquals(x2, line.getX2(), 0.1);
    assertEquals(y2, line.getY2(), 0.1);
  }

  public void testBorderConstruction() throws Exception
  {
    border = new Border(style, insideMargin);
    assertSame(style, border.getStyle());
  }

  public void testNotHaveBordersWhenBorderWidthIsZero() throws Exception
  {
    style.setBorderWidth("0");
    border = new Border(style, insideMargin);

    assertEquals(false, border.hasTopBorder());
    assertEquals(false, border.hasRightBorder());
    assertEquals(false, border.hasBottomBorder());
    assertEquals(false, border.hasLeftBorder());

    style.setBorderWidth(null);
    border = new Border(style, insideMargin);

    assertEquals(false, border.hasTopBorder());
    assertEquals(false, border.hasRightBorder());
    assertEquals(false, border.hasBottomBorder());
    assertEquals(false, border.hasLeftBorder());
  }
  
  public void testHasBordersForAnyBorderWhoseWidthIsNotZero() throws Exception
  {
    style.setBorderWidth("1");
    style.setTopBorderWidth("0");
    border = new Border(style, insideMargin);

    assertEquals(false, border.hasTopBorder());
    assertEquals(true, border.hasRightBorder());
    assertEquals(true, border.hasBottomBorder());
    assertEquals(true, border.hasLeftBorder());
  }

  public void testGetLinesFromBorderWithOnePixelWidthAllAround() throws Exception
  {
    style.setBorderWidth("1");
    border = new Border(style, insideMargin);

    verifyLine(border.getTopLine(), 0, 0, 99, 0);
    verifyLine(border.getRightLine(), 99, 0, 99, 199);
    verifyLine(border.getBottomLine(), 99, 199, 0, 199);
    verifyLine(border.getLeftLine(), 0, 199, 0, 0);
  }

  public void testGetLinesFromBorderWithOnePixelWidthAllAroundAndSomeMargin() throws Exception
  {
    style.setBorderWidth("1");
    insideMargin = new Box(20, 30, 100, 200);
    border = new Border(style, insideMargin);

    int right = insideMargin.width - 1 + 20;
    int bottom = insideMargin.height - 1 + 30;
    verifyLine(border.getTopLine(), 20, 30, right, 30);
    verifyLine(border.getRightLine(), right, 30, right, bottom);
    verifyLine(border.getBottomLine(), right, bottom, 20, bottom);
    verifyLine(border.getLeftLine(), 20, bottom, 20, 30);
  }

  public void testGetLinesFromBorderWithWideEvenBorder() throws Exception
  {
    int borderWidth = 4;
    style.setBorderWidth("" + borderWidth);
    border = new Border(style, insideMargin);

    verifyLine(border.getTopLine(), 2, 2, 98, 2);
    verifyLine(border.getRightLine(), 98, 2, 98, 198);
    verifyLine(border.getBottomLine(), 98, 198, 2, 198);
    verifyLine(border.getLeftLine(), 2, 198, 2, 2);
  }

  public void testGetLinesFromBorderWithWideOddBorder() throws Exception
  {
    int borderWidth = 5;
    style.setBorderWidth("" + borderWidth);
    border = new Border(style, insideMargin);

    verifyLine(border.getTopLine(), 2, 2, 97, 2);
    verifyLine(border.getRightLine(), 97, 2, 97, 197);
    verifyLine(border.getBottomLine(), 97, 197, 2, 197);
    verifyLine(border.getLeftLine(), 2, 197, 2, 2);
  }

  public void testGetLinesForBordersWithZeroWidth() throws Exception
  {
    style.setBorderWidth("0");
    border = new Border(style, insideMargin);

    verifyLine(border.getTopLine(), 0, 0, 100, 0);
    verifyLine(border.getRightLine(), 100, 0, 100, 200);
    verifyLine(border.getBottomLine(), 100, 200, 0, 200);
    verifyLine(border.getLeftLine(), 0, 200, 0, 0);
  }

  public void testHasCorners() throws Exception
  {
    style.setBorderWidth("1");
    style.setRoundedCornerRadius("0");
    border = new Border(style, insideMargin);

    assertEquals(false, border.hasTopRightCorner());
    assertEquals(false, border.hasBottomRightCorner());
    assertEquals(false, border.hasBottomLeftCorner());
    assertEquals(false, border.hasTopLeftCorner());

    style.setRoundedCornerRadius(null);
    border = new Border(style, insideMargin);

    assertEquals(false, border.hasTopRightCorner());
    assertEquals(false, border.hasBottomRightCorner());
    assertEquals(false, border.hasBottomLeftCorner());
    assertEquals(false, border.hasTopLeftCorner());

    style.setRoundedCornerRadius("10");
    border = new Border(style, insideMargin);

    assertEquals(true, border.hasTopRightCorner());
    assertEquals(true, border.hasBottomRightCorner());
    assertEquals(true, border.hasBottomLeftCorner());
    assertEquals(true, border.hasTopLeftCorner());
  }

  public void testGetLinesAndCornersWithSomeRadiusNoMarginAndWidthOfOne() throws Exception
  {
    style.setBorderWidth("1");
    style.setMargin("0");
    style.setRoundedCornerRadius("10");
    border = new Border(style, insideMargin);

    verifyLine(border.getTopLine(), 10, 0, 89, 0);
    verifyLine(border.getRightLine(), 99, 10, 99, 189);
    verifyLine(border.getBottomLine(), 89, 199, 10, 199);
    verifyLine(border.getLeftLine(), 0, 189, 0, 10);

    verifyArc(border.getTopRightArc(), 90, -90, 79, 0, 20, 20);
    verifyArc(border.getBottomRightArc(), 0, -90, 79, 179, 20, 20);
    verifyArc(border.getBottomLeftArc(), 270, -90, 0, 179, 20, 20);
    verifyArc(border.getTopLeftArc(), 180, -90, 0, 0, 20, 20);
  }

  public void testGetLinesAndCornersEvenWidth() throws Exception
  {
    style.setBorderWidth("4");
    style.setMargin("0");
    style.setRoundedCornerRadius("10");
    border = new Border(style, insideMargin);

    verifyLine(border.getTopLine(), 12, 2, 88, 2);
    verifyLine(border.getRightLine(), 98, 12, 98, 188);
    verifyLine(border.getBottomLine(), 88, 198, 12, 198);
    verifyLine(border.getLeftLine(), 2, 188, 2, 12);

    verifyArc(border.getTopRightArc(), 90, -90, 79, 2, 18, 18);
    verifyArc(border.getBottomRightArc(), 0, -90, 79, 179, 18, 18);
    verifyArc(border.getBottomLeftArc(), 270, -90, 2, 179, 18, 18);
    verifyArc(border.getTopLeftArc(), 180, -90, 2, 2, 18, 18);
  }

  private void verifyArc(Arc2D arc, int startAngle, int extent, int x, int y, int width, int height)
  {
    assertEquals(Arc2D.OPEN, arc.getArcType());
    assertEquals(startAngle, arc.getAngleStart(), 0.1);
    assertEquals(extent, arc.getAngleExtent(), 0.1);
    assertEquals(x, arc.getBounds().x, 0.1);
    assertEquals(y, arc.getBounds().y, 0.1);
    assertEquals(width, arc.getBounds().width, 0.1);
    assertEquals(height, arc.getBounds().height, 0.1);
  }

  public void testGettingShapeInsideBorderWithNoCorners() throws Exception
  {
    style.setBorderWidth("1");
    style.setRoundedCornerRadius("0");
    border = new Border(style, insideMargin);
    double[] coords = new double[6];

    Shape inside = border.getShapeInsideBorder();
    AffineTransform transform = new AffineTransform();
    PathIterator iterator = inside.getPathIterator(transform);

    assertEquals(PathIterator.SEG_MOVETO, iterator.currentSegment(coords));
    assertEquals(1, coords[0], 0.1);
    assertEquals(1, coords[1], 0.1);
    iterator.next();
    checkLineSegment(coords, iterator, 99, 1);
    checkLineSegment(coords, iterator, 99, 199);
    checkLineSegment(coords, iterator, 99, 199);
    checkLineSegment(coords, iterator, 1, 199);
    checkLineSegment(coords, iterator, 1, 199);
    checkLineSegment(coords, iterator, 1, 1);
    checkLineSegment(coords, iterator, 1, 1);
    iterator.next();
    assertEquals(true, iterator.isDone());
  }

  private void checkLineSegment(double[] coords, PathIterator iterator, double x, double y)
  {
    iterator.next();
    assertEquals(PathIterator.SEG_LINETO, iterator.currentSegment(coords));
    assertEquals(x, coords[0], 0.1);
    assertEquals(y, coords[1], 0.1);
  }
//
//  public void testGettingShapeInsideBorderWithCorners() throws Exception
//  {
//    style.setBorderWidth("1");
//    style.setRoundedCornerRadius("10");
//    border = new Border(style, insideMargin);
//    double[] coords = new double[6];
//
//    Shape inside = border.getShapeInsideBorder();
//    AffineTransform transform = new AffineTransform();
//    PathIterator iterator = inside.getPathIterator(transform);
//
//    assertEquals(PathIterator.SEG_MOVETO, iterator.currentSegment(coords));
//    assertEquals(11, coords[0], 0.1);
//    assertEquals(1, coords[1], 0.1);
//    checkLineSegment(coords, iterator, 89, 1);
//    checkLineSegment(coords, iterator, 89.5, 0);
//    checkCubicSegment(coords, iterator, 94.7, 0, 99, 9.5);
//    checkLineSegment(coords, iterator, 99, 11);
//    checkLineSegment(coords, iterator, 99, 189);
//    checkLineSegment(coords, iterator, 99.5, 190);
//    checkCubicSegment(coords, iterator, 99.5, 195.25, 90, 199.5);
//    checkLineSegment(coords, iterator, 89, 199);
//    checkLineSegment(coords, iterator, 11, 199);
//    checkLineSegment(coords, iterator, 9.5, 199);
//    checkCubicSegment(coords, iterator, 4.25, 199, 0, 189.5);
//    checkLineSegment(coords, iterator, 1, 189);
//    checkLineSegment(coords, iterator, 1, 11);
//    checkLineSegment(coords, iterator, 0, 9.5);
//    checkCubicSegment(coords, iterator, 0, 4.25, 9.5, 0);
//    checkLineSegment(coords, iterator, 11, 1);
//    iterator.next();
//    assertEquals(true, iterator.isDone());
//  }

  private void checkCubicSegment(double[] coords, PathIterator iterator, double x1, double y1, double x3, double y3)
  {
    iterator.next();
    assertEquals(PathIterator.SEG_CUBICTO, iterator.currentSegment(coords));
    assertEquals(x1, coords[0], 0.1);
    assertEquals(y1, coords[1], 0.1);
    assertEquals(x3, coords[4], 0.1);
    assertEquals(y3, coords[5], 0.1);
  }

  public void testUpdateDimentions() throws Exception
  {
    style.setBorderWidth("1");
    style.setRoundedCornerRadius("1");
    border = new Border(style, insideMargin);

    style.setTopBorderWidth("2");
    border.updateDimentions();
    assertEquals(2, border.getTopWidth());

    style.setRightBorderWidth("2");
    border.updateDimentions();
    assertEquals(2, border.getRightWidth());

    style.setBottomBorderWidth("2");
    border.updateDimentions();
    assertEquals(2, border.getBottomWidth());

    style.setLeftBorderWidth("2");
    border.updateDimentions();
    assertEquals(2, border.getLeftWidth());

    style.setTopRightBorderWidth("2");
    border.updateDimentions();
    assertEquals(2, border.getTopRightWidth());

    style.setTopRightRoundedCornerRadius("2");
    border.updateDimentions();
    assertEquals(2, border.getTopRightRadius());

    style.setBottomRightBorderWidth("2");
    border.updateDimentions();
    assertEquals(2, border.getBottomRightWidth());

    style.setBottomRightRoundedCornerRadius("2");
    border.updateDimentions();
    assertEquals(2, border.getBottomRightRadius());

    style.setBottomLeftBorderWidth("2");
    border.updateDimentions();
    assertEquals(2, border.getBottomLeftWidth());

    style.setBottomLeftRoundedCornerRadius("2");
    border.updateDimentions();
    assertEquals(2, border.getBottomLeftRadius());

    style.setTopLeftBorderWidth("2");
    border.updateDimentions();
    assertEquals(2, border.getTopLeftWidth());

    style.setTopLeftRoundedCornerRadius("2");
    border.updateDimentions();
    assertEquals(2, border.getTopLeftRadius());
  }
}
