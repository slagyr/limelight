//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;
import limelight.ui.api.MockProp;
import limelight.ui.model.inputs.ScrollBarPanel;
import limelight.ui.painting.Border;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PropPanelLayoutTest extends TestCase
{
  private PropPanel parent;
  private PropPanelLayout layout;
  private int scrollGirth;
  private RootPanel root;

  public void setUp() throws Exception
  {
    root = new RootPanel(new MockPropFrame());

    parent = new PropPanel(new MockProp());
    root.setPanel(parent);
    parent.getStyle().setWidth("100");
    parent.getStyle().setHeight("100");


    layout = PropPanelLayout.instance;
    scrollGirth = new JScrollBar(JScrollBar.VERTICAL).getPreferredSize().width;
  }

  public void testLayoutWithOneFullSizedChild() throws Exception
  {
    PropPanel child = addChildWithSize(parent, "100", "100");

    layout.doLayout(parent);

    assertTrue(child.isLaidOut());
    assertEquals(new Point(0, 0), child.getLocation());
  }

  public void testLayoutWithTwoChildrenSameRow() throws Exception
  {
    PropPanel child1 = addChildWithSize(parent, "50", "50");
    PropPanel child2 = addChildWithSize(parent, "50", "50");

    layout.doLayout(parent);

    assertTrue(child1.isLaidOut());
    assertTrue(child2.isLaidOut());
    assertEquals(new Point(0, 0), child1.getLocation());
    assertEquals(new Point(50, 0), child2.getLocation());
  }

  public void testLayoutWrappingToNewRow() throws Exception
  {
    PropPanel child1 = addChildWithSize(parent, "50", "50");
    PropPanel child2 = addChildWithSize(parent, "50", "50");
    PropPanel child3 = addChildWithSize(parent, "50", "50");

    layout.doLayout(parent);

    assertEquals(new Point(0, 0), child1.getLocation());
    assertEquals(new Point(50, 0), child2.getLocation());
    assertEquals(new Point(0, 50), child3.getLocation());
  }

  public void testLayoutWithOneChildAlignedBottomRight() throws Exception
  {
    parent.getProp().getStyle().setVerticalAlignment("bottom");
    parent.getProp().getStyle().setHorizontalAlignment("right");
    PropPanel child = addChildWithSize(parent, "50", "50");

    layout.doLayout(parent);

    assertTrue(child.isLaidOut());
    assertEquals(new Point(50, 50), child.getLocation());
  }

  public void testLayoutWithTwoChildrenAlignedCenterCenter() throws Exception
  {
    parent.getProp().getStyle().setVerticalAlignment("center");
    parent.getProp().getStyle().setHorizontalAlignment("center");
    PropPanel child1 = addChildWithSize(parent, "25", "50");
    PropPanel child2 = addChildWithSize(parent, "25", "50");

    layout.doLayout(parent);

    assertEquals(new Point(25, 25), child1.getLocation());
    assertEquals(new Point(50, 25), child2.getLocation());
  }

  public void testAutoSize() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "auto", "auto");
    addChildWithSize(panel, "50", "50");

    layout.doLayout(parent);

    assertEquals(50, panel.getWidth());
    assertEquals(50, panel.getHeight());
  }

  public void testAutoWidthOnly() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "auto", "100");
    addChildWithSize(panel, "50", "50");

    layout.doLayout(parent);

    assertEquals(50, panel.getWidth());
    assertEquals(100, panel.getHeight());
  }

  public void testAutoHeightOnly() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "70%", "auto");
    addChildWithSize(panel, "50", "50");

    layout.doLayout(parent);

    assertEquals(70, panel.getWidth());
    assertEquals(50, panel.getHeight());
  }

  public void testAutoSizeIncludesMarginPaddingAndBorder() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "auto", "auto");
    panel.getStyle().setTopMargin("1");
    panel.getStyle().setTopBorderWidth("2");
    panel.getStyle().setTopPadding("3");
    panel.getStyle().setRightMargin("4");
    panel.getStyle().setRightBorderWidth("5");
    panel.getStyle().setRightPadding("6");
    panel.getStyle().setBottomMargin("7");
    panel.getStyle().setBottomBorderWidth("8");
    panel.getStyle().setBottomPadding("9");
    panel.getStyle().setLeftMargin("10");
    panel.getStyle().setLeftBorderWidth("11");
    panel.getStyle().setLeftPadding("12");
    MockPropablePanel child = new MockPropablePanel();
    child.prepForSnap(50, 50);
    panel.add(child);

    layout.doLayout(parent);

    assertEquals(98, panel.getWidth());
    assertEquals(80, panel.getHeight());
  }

  public void testAutoSizeWithChildrenCentered() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "auto", "auto");
    panel.getStyle().setHorizontalAlignment("center");
    panel.getStyle().setVerticalAlignment("center");
    panel.getStyle().setMargin("10");
    MockPropablePanel child = new MockPropablePanel();
    child.prepForSnap(50, 50);
    panel.add(child);

    layout.doLayout(parent);

    assertEquals(10, child.getX());
    assertEquals(10, child.getY());
    assertEquals(70, panel.getWidth());
    assertEquals(70, panel.getHeight());
  }

  public void testAutoSizingWithNoChildren() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "auto", "auto");

    layout.doLayout(parent);

    assertEquals(0, panel.getWidth());
    assertEquals(0, panel.getHeight());
  }

  public void testAutoSizeWithMinDimensions() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "auto", "auto");
    panel.getStyle().setMinWidth("20");
    panel.getStyle().setMinHeight("15");
    PropPanel child = addChildWithSize(panel, "5", "5");

    layout.doLayout(parent);

    assertEquals(20, panel.getWidth());
    assertEquals(15, panel.getHeight());
  }

  public void testAutoDimensionsWithMaxHeightAndWidth() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "auto", "auto");
    panel.getStyle().setMaxWidth("75");
    panel.getStyle().setMaxHeight("86");
    PropPanel child = addChildWithSize(panel, "90", "90");

    layout.doLayout(parent);

    assertEquals(75, panel.getWidth());
    assertEquals(86, panel.getHeight());
  }

  public void testFloatersAreNoConsideredInDeterminingScrollMode() throws Exception
  {
    PropPanel child = addChildWithSize(parent, "50", "50");
    PropPanel floater = addChildWithSize(parent, "500", "500");
    floater.getStyle().setFloat("on");

    layout.doLayout(parent);

    assertEquals(2, parent.getChildren().size());
    assertSame(child, parent.getChildren().get(0));
    assertSame(floater, parent.getChildren().get(1));
  }

  public void testFloatersDoNotInfluenceAutoSize() throws Exception
  {

    PropPanel panel = addChildWithSize(parent, "auto", "auto");
    PropPanel child = addChildWithSize(panel, "50", "50");
    PropPanel floater = addChildWithSize(panel, "500", "500");
    floater.getStyle().setFloat("on");

    layout.doLayout(parent);

    assertEquals(50, panel.getWidth());
    assertEquals(50, panel.getHeight());
  }

  public void testFloatersAreLaidOut() throws Exception
  {
    PropPanel floater = addChildWithSize(parent, "50", "50");
    floater.getStyle().setFloat(true);
    PropPanel child = addChildWithSize(parent, "50", "50");

    layout.doLayout(parent);

    assertEquals(true, floater.isLaidOut());
    assertEquals(true, child.isLaidOut());
    assertEquals(new Point(0, 0), child.getLocation());
  }

  public void testAddingScrollBars() throws Exception
  {
    assertEquals(0, parent.getChildren().size());
    assertEquals(null, parent.getVerticalScrollbar());
    assertEquals(null, parent.getHorizontalScrollbar());
    parent.getStyle().setScrollbars("on");

    layout.establishScrollBars(parent);

    assertEquals(2, parent.getChildren().size());
    assertNotNull(parent.getVerticalScrollbar());
    assertNotNull(parent.getHorizontalScrollbar());
  }

  public void testRemovingScrollBars() throws Exception
  {
    parent.getStyle().setScrollbars("on");
    layout.establishScrollBars(parent);
    assertEquals(2, parent.getChildren().size());
    assertNotNull(parent.getVerticalScrollbar());
    assertNotNull(parent.getHorizontalScrollbar());

    parent.getStyle().setScrollbars("off");
    layout.establishScrollBars(parent);
    assertEquals(0, parent.getChildren().size());
    assertNull(parent.getVerticalScrollbar());
    assertNull(parent.getHorizontalScrollbar());
  }

  public void testScrollBarLayout() throws Exception
  {
    parent.getStyle().setScrollbars("on");

    layout.doLayout(parent);

    ScrollBarPanel verticalScrollBar = parent.getVerticalScrollbar();
    assertEquals(100 - scrollGirth, verticalScrollBar.getX());
    assertEquals(0, verticalScrollBar.getY());

    ScrollBarPanel horizontalScrollBar = parent.getHorizontalScrollbar();
    assertEquals(0, horizontalScrollBar.getX());
    assertEquals(100 - scrollGirth, horizontalScrollBar.getY());
  }

  public void testAutoSizingWithNoChildrenAndScrollBars() throws Exception
  {
    PropPanel panel = new PropPanel(new MockProp());
    parent.add(panel);
    panel.getStyle().setWidth("auto");
    panel.getStyle().setHeight("auto");
    panel.getStyle().setScrollbars("on");

    layout.doLayout(parent);

    assertEquals(0, panel.getWidth());
    assertEquals(0, panel.getHeight());
  }

  public void testScrollBarsDontGetLayoutLikeOtherProps() throws Exception
  {
    parent.getStyle().setScrollbars("on");
    layout.doLayout(parent);

    PropPanel panel = addChildWithSize(parent, "100%", "100%");
    layout.doLayout(parent);

    assertEquals(0, panel.getX());
    assertEquals(0, panel.getY());
  }

  public void testScrollAdjusting() throws Exception
  {
    parent.getStyle().setScrollbars("on");
    layout.doLayout(parent);

    PropPanel panel = addChildWithSize(parent, "200", "300");
    layout.doLayout(parent);

    assertEquals(200, parent.getHorizontalScrollbar().getMaximumValue());
    assertEquals(100 - scrollGirth, parent.getHorizontalScrollbar().getVisibleAmount());
  }

  public void testLayoutRowsWithScrollOffsets() throws Exception
  {
    parent.getStyle().setScrollbars("on");
    PropPanel panel = addChildWithSize(parent, "200", "300");
    layout.doLayout(parent);

    parent.getVerticalScrollbar().setValue(100);
    parent.getHorizontalScrollbar().setValue(50);
    layout.doLayout(parent);

    assertEquals(-50, panel.getX());
    assertEquals(-100, panel.getY());
  }

  public void testBorderGetUpdatedOnLayout() throws Exception
  {
    parent.setSize(100, 100);
    Border border = parent.getBorderShaper();

    parent.getStyle().setBorderWidth("21");
    layout.doLayout(parent);

    assertEquals(21, border.getTopWidth());
  }

  public void testPanelKnownWhenItsLaidOut() throws Exception
  {
    assertEquals(false, parent.isLaidOut());

    layout.doLayout(parent);

    assertEquals(true, parent.isLaidOut());
  }

  public void testPanelMarkedAsDirty() throws Exception
  {
    root.getAndClearDirtyRegions(new ArrayList<Rectangle>());

    layout.doLayout(parent);

    assertEquals(true, root.dirtyRegionsContains(parent.getAbsoluteBounds()));
  }

  public void testPanelNoLongerNeedsLayout() throws Exception
  {
    assertEquals(true, parent.needsLayout());

    layout.doLayout(parent);

    assertEquals(false, parent.needsLayout());
  }

  public void testShouldOverideEverything() throws Exception
  {
    assertEquals(true, layout.overides(null));
  }

  public void testShouldLayoutChildrenWithPercentageDimensionsWhenSizeChanges() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "100%", "100%");
    layout.doLayout(parent);

    parent.getStyle().setWidth(200);
    parent.getStyle().setHeight(300);
    layout.doLayout(parent);

    assertEquals(200, panel.getWidth());
    assertEquals(300, panel.getHeight());
  }

  public void testShouldLayoutChildrenWithAutoDimensionsWhenSizeChanges() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "auto", "auto");
    PropPanel child = addChildWithSize(panel, "100%", "100%");
    layout.doLayout(parent);

    parent.getStyle().setWidth(200);
    parent.getStyle().setHeight(300);
    layout.doLayout(parent);

    assertEquals(200, panel.getWidth());
    assertEquals(300, panel.getHeight());
    assertEquals(200, child.getWidth());
    assertEquals(300, child.getHeight());
  }

  private PropPanel addChildWithSize(BasePanel parent, String width, String height)
  {
    PropPanel panel = new PropPanel(new MockProp());
    panel.getStyle().setWidth(width);
    panel.getStyle().setHeight(height);
    parent.add(panel);
    return panel;
  }

  public void testShouldVerticallyAlignChildrenWithinARow() throws Exception
  {
    parent.getStyle().setVerticalAlignment("bottom");
    PropPanel panel1 = addChildWithSize(parent, "50%", "100%");
    PropPanel panel2 = addChildWithSize(parent, "50%", "50%");

    layout.doLayout(parent);

    assertEquals(0, panel1.getY());
    assertEquals(50, panel2.getY());
  }

  public void testRowWithOneGreedyProp() throws Exception
  {
    PropPanel panel1 = addChildWithSize(parent, "20", "100");
    PropPanel panel2 = addChildWithSize(parent, "greedy", "100");

    layout.doLayout(parent);

    assertEquals(20, panel1.getWidth());
    assertEquals(80, panel2.getWidth());
  }

  public void testRowWithTwoGreedyProp() throws Exception
  {
    PropPanel inert = addChildWithSize(parent, "20", "100");
    PropPanel greedy1 = addChildWithSize(parent, "greedy", "100");
    PropPanel greedy2 = addChildWithSize(parent, "greedy", "100");

    layout.doLayout(parent);

    assertEquals(20, inert.getWidth());
    assertEquals(40, greedy1.getWidth());
    assertEquals(40, greedy2.getWidth());
  }

  public void testOnePropWithGreedyHeight() throws Exception
  {
    PropPanel panel1 = addChildWithSize(parent, "100", "20");
    PropPanel panel2 = addChildWithSize(parent, "100", "greedy");

    layout.doLayout(parent);

    assertEquals(20, panel1.getHeight());
    assertEquals(80, panel2.getHeight());
  }

  public void testTwoPropsWithGreedyHeightInDifferentRows() throws Exception
  {
    PropPanel panel1 = addChildWithSize(parent, "100", "20");
    PropPanel greedy1 = addChildWithSize(parent, "100", "greedy");
    PropPanel greedy2 = addChildWithSize(parent, "100", "greedy");

    layout.doLayout(parent);

    assertEquals(20, panel1.getHeight());
    assertEquals(40, greedy1.getHeight());
    assertEquals(40, greedy2.getHeight());
  }

  public void testGreedyWidthWithAutoHeight() throws Exception
  {
    PropPanel greedy1 = addChildWithSize(parent, "greedy", "auto");
    PropPanel child = addChildWithSize(greedy1, "100%", "100%");

    layout.doLayout(parent);

    assertEquals(100, child.getWidth());
    assertEquals(100, child.getHeight());
  }

  public void testAutoWidthWithGreedyHeight() throws Exception
  {
    addChildWithSize(parent, "auto", "greedy");

    try
    {
      layout.doLayout(parent);
      fail("should throw exception");
    }
    catch(Exception e)
    {
      assertEquals("A greedy height is not allowed with auto width.", e.getMessage());
    }
  }

  public void testAutoHeightIsCollapsedWhenContainingChildren() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "100%", "auto");
    PropPanel child = addChildWithSize(panel, "100%", "auto");
    addChildWithSize(child, "100%", "14");

    layout.doLayout(parent);

    assertEquals(100, panel.getWidth());
    assertEquals(14, panel.getHeight());
  }

  public void testPreferredSize() throws Exception
  {
    parent.getStyle().setWidth("100");
    parent.getStyle().setHeight("200");

    layout.snapToSize(parent, false);

    assertEquals(100, parent.getWidth());
    assertEquals(200, parent.getHeight());
  }

  public void testSizeUsingAutoWidthAndHeight() throws Exception
  {
    root.setSize(100, 100);
    parent.getStyle().setWidth("auto");
    parent.getStyle().setHeight("auto");
    layout.snapToSize(parent, false);
    assertEquals(100, parent.getWidth());
    assertEquals(100, parent.getHeight());

    parent.getStyle().setWidth("auto");
    parent.getStyle().setHeight("50");
    layout.snapToSize(parent, false);
    assertEquals(100, parent.getWidth());
    assertEquals(50, parent.getHeight());

    parent.getStyle().setWidth("42%");
    parent.getStyle().setHeight("auto");
    layout.snapToSize(parent, false);
    assertEquals(42, parent.getWidth());
    assertEquals(100, parent.getHeight());
  }

  public void testSnapToSizeWithMaxSizeAgainstAutoSizing() throws Exception
  {
    root.setSize(100, 100);
    parent.getStyle().setWidth("auto");
    parent.getStyle().setHeight("auto");
    parent.getStyle().setMaxWidth("75");
    parent.getStyle().setMaxHeight("82");

    layout.snapToSize(parent, false);

    assertEquals(75, parent.getWidth());
    assertEquals(82, parent.getHeight());
  }

  public void testSnapToSizeWithMaxSizeAgainstPercentageSizing() throws Exception
  {
    root.setSize(100, 100);
    parent.getStyle().setWidth("90%");
    parent.getStyle().setHeight("90%");
    parent.getStyle().setMaxWidth("75");
    parent.getStyle().setMaxHeight("82");

    layout.snapToSize(parent, false);

    assertEquals(75, parent.getWidth());
    assertEquals(82, parent.getHeight());
  }

  public void testSnapToSizeWithMinSizeAgainstPercentageSizing() throws Exception
  {
    root.setSize(100, 100);
    parent.getStyle().setWidth("20%");
    parent.getStyle().setHeight("20%");
    parent.getStyle().setMinWidth("42");
    parent.getStyle().setMinHeight("51");

    layout.snapToSize(parent, false);

    assertEquals(42, parent.getWidth());
    assertEquals(51, parent.getHeight());
  }
}
