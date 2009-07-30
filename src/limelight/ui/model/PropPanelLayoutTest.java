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
    root = new RootPanel(new MockStageFrame());

    parent = new PropPanel(new MockProp());
    root.setPanel(parent);
    parent.getStyle().setWidth("100");
    parent.getStyle().setHeight("100");


    layout = new PropPanelLayout();
    scrollGirth = new JScrollBar(JScrollBar.VERTICAL).getPreferredSize().width;
  }

  public void testLayoutWithOneFullSizedChild() throws Exception
  {
    MockPropablePanel child = createAndAddChildWithSize(100, 100);

    layout.doLayout(parent);

    assertTrue(child.wasLaidOut);
    assertEquals(new Point(0, 0), child.getLocation());
  }

  public void testLayoutWithTwoChildrenSameRow() throws Exception
  {
    MockPropablePanel child1 = createAndAddChildWithSize(50, 50);
    MockPropablePanel child2 = createAndAddChildWithSize(50, 50);

    layout.doLayout(parent);

    assertTrue(child1.wasLaidOut);
    assertTrue(child2.wasLaidOut);
    assertEquals(new Point(0, 0), child1.getLocation());
    assertEquals(new Point(50, 0), child2.getLocation());
  }

  public void testLayoutWrappingToNewRow() throws Exception
  {
    MockPropablePanel child1 = createAndAddChildWithSize(50, 50);
    MockPropablePanel child2 = createAndAddChildWithSize(50, 50);
    MockPropablePanel child3 = createAndAddChildWithSize(50, 50);

    layout.doLayout(parent);

    assertEquals(new Point(0, 0), child1.getLocation());
    assertEquals(new Point(50, 0), child2.getLocation());
    assertEquals(new Point(0, 50), child3.getLocation());
  }

  public void testLayoutWithOneChildAlignedBottomRight() throws Exception
  {
    parent.getProp().getStyle().setVerticalAlignment("bottom");
    parent.getProp().getStyle().setHorizontalAlignment("right");
    MockPropablePanel child = createAndAddChildWithSize(50, 50);

    layout.doLayout(parent);

    assertTrue(child.wasLaidOut);
    assertEquals(new Point(50, 50), child.getLocation());
  }

  public void testLayoutWithTwoChildrenAlignedCenterCenter() throws Exception
  {
    parent.getProp().getStyle().setVerticalAlignment("center");
    parent.getProp().getStyle().setHorizontalAlignment("center");
    MockPropablePanel child1 = createAndAddChildWithSize(25, 50);
    MockPropablePanel child2 = createAndAddChildWithSize(25, 50);

    layout.doLayout(parent);

    assertEquals(new Point(25, 25), child1.getLocation());
    assertEquals(new Point(50, 25), child2.getLocation());
  }

  private MockPropablePanel createAndAddChildWithSize(int width, int height) throws SterilePanelException
  {
    MockPropablePanel child = new MockPropablePanel();
    child.prepForSnap(width, height);
    parent.add(child);
    return child;
  }

  public void testAutoSize() throws Exception
  {
    PropPanel panel = new PropPanel(new MockProp());
    parent.add(panel);
    panel.getStyle().setWidth("auto");
    panel.getStyle().setHeight("auto");
    MockPropablePanel child = new MockPropablePanel();
    child.prepForSnap(50, 50);
    panel.add(child);

    layout.doLayout(parent);

    assertEquals(50, panel.getWidth());
    assertEquals(50, panel.getHeight());
  }

  public void testAutoWidthOnly() throws Exception
  {
    PropPanel panel = new PropPanel(new MockProp());
    parent.add(panel);
    panel.getStyle().setWidth("auto");
    panel.getStyle().setHeight("100");
    MockPropablePanel child = new MockPropablePanel();
    child.prepForSnap(50, 50);
    panel.add(child);

    layout.doLayout(parent);

    assertEquals(50, panel.getWidth());
    assertEquals(100, panel.getHeight());
  }

  public void testAutoHeightOnly() throws Exception
  {
    PropPanel panel = new PropPanel(new MockProp());
    parent.add(panel);
    panel.getStyle().setWidth("70%");
    panel.getStyle().setHeight("auto");
    MockPropablePanel child = new MockPropablePanel();
    child.prepForSnap(50, 50);
    panel.add(child);

    layout.doLayout(parent);

    assertEquals(70, panel.getWidth());
    assertEquals(50, panel.getHeight());
  }

  public void testAutoSizeIncludesMarginPaddingAndBorder() throws Exception
  {
    PropPanel panel = new PropPanel(new MockProp());
    parent.add(panel);
    panel.getStyle().setWidth("auto");
    panel.getStyle().setHeight("auto");
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
    PropPanel panel = new PropPanel(new MockProp());
    parent.add(panel);
    panel.getStyle().setWidth("auto");
    panel.getStyle().setHeight("auto");
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
    PropPanel panel = new PropPanel(new MockProp());
    parent.add(panel);
    panel.getStyle().setWidth("auto");
    panel.getStyle().setHeight("auto");

    layout.doLayout(parent);

    assertEquals(0, panel.getWidth());
    assertEquals(0, panel.getHeight());
  }

  public void testAutoSizeWithMinDimensions() throws Exception
  {
    PropPanel panel = new PropPanel(new MockProp());
    parent.add(panel);
    panel.getStyle().setWidth("auto");
    panel.getStyle().setHeight("auto");
    panel.getStyle().setMinWidth("20");
    panel.getStyle().setMinHeight("15");
    MockPropablePanel child = new MockPropablePanel();
    child.prepForSnap(5, 5);
    panel.add(child);

    layout.doLayout(parent);

    assertEquals(20, panel.getWidth());
    assertEquals(15, panel.getHeight());
  }

  public void testAutoDimensionsWithMaxHeightAndWidth() throws Exception
  {
    PropPanel panel = new PropPanel(new MockProp());
    parent.add(panel);
    panel.getStyle().setWidth("auto");
    panel.getStyle().setHeight("auto");
    panel.getStyle().setMaxWidth("75");
    panel.getStyle().setMaxHeight("86");
    MockPropablePanel child = new MockPropablePanel();
    child.prepForSnap(90, 90);
    panel.add(child);

    layout.doLayout(parent);

    assertEquals(75, panel.getWidth());
    assertEquals(86, panel.getHeight());
  }

  public void testFloatersAreNoConsideredInDeterminingScrollMode() throws Exception
  {
    MockPropablePanel child = new MockPropablePanel();
    child.prepForSnap(50, 50);
    MockPropablePanel floater = new MockPropablePanel();
    floater.getStyle().setFloat("on");
    floater.prepForSnap(500, 500);
    parent.add(child);
    parent.add(floater);

    layout.doLayout(parent);

    assertEquals(2, parent.getChildren().size());
    assertSame(child, parent.getChildren().get(0));
    assertSame(floater, parent.getChildren().get(1));
  }

  public void testFloatersDoNotInfluenceAutoSize() throws Exception
  {
    PropPanel panel = new PropPanel(new MockProp());
    parent.add(panel);
    panel.getStyle().setWidth("auto");
    panel.getStyle().setHeight("auto");
    MockPropablePanel child = new MockPropablePanel();
    child.prepForSnap(50, 50);
    panel.add(child);
    MockPropablePanel floater = new MockPropablePanel();
    floater.floater = true;
    floater.prepForSnap(500, 500);
    panel.add(floater);

    layout.doLayout(parent);

    assertEquals(50, panel.getWidth());
    assertEquals(50, panel.getHeight());
  }

  public void testFloatersAreLaidOut() throws Exception
  {
    MockPropablePanel floater = createAndAddChildWithSize(50, 50);
    floater.floater = true;
    MockPropablePanel child = createAndAddChildWithSize(50, 50);

    layout.doLayout(parent);

    assertEquals(true, floater.wasLaidOut);
    assertEquals(true, child.wasLaidOut);
    assertEquals(new Point(0, 0), child.getLocation());
  }

  public void testAddingScrollBars() throws Exception
  {
    assertEquals(0, parent.getChildren().size());
    assertEquals(null, parent.getVerticalScrollBar());
    assertEquals(null, parent.getHorizontalScrollBar());
    parent.getStyle().setScrollbars("on");

    layout.establishScrollBars(parent);

    assertEquals(2, parent.getChildren().size());
    assertNotNull(parent.getVerticalScrollBar());
    assertNotNull(parent.getHorizontalScrollBar());
  }

  public void testRemovingScrollBars() throws Exception
  {
    parent.getStyle().setScrollbars("on");
    layout.establishScrollBars(parent);
    assertEquals(2, parent.getChildren().size());
    assertNotNull(parent.getVerticalScrollBar());
    assertNotNull(parent.getHorizontalScrollBar());

    parent.getStyle().setScrollbars("off");
    layout.establishScrollBars(parent);
    assertEquals(0, parent.getChildren().size());
    assertNull(parent.getVerticalScrollBar());
    assertNull(parent.getHorizontalScrollBar());
  }

  public void testScrollBarLayout() throws Exception
  {
    parent.getStyle().setScrollbars("on");

    layout.doLayout(parent);

    ScrollBarPanel verticalScrollBar = parent.getVerticalScrollBar();
    assertEquals(100 - scrollGirth, verticalScrollBar.getX());
    assertEquals(0, verticalScrollBar.getY());

    ScrollBarPanel horizontalScrollBar = parent.getHorizontalScrollBar();
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

    PropPanel panel = new PropPanel(new MockProp());
    panel.getStyle().setWidth("100%");
    panel.getStyle().setHeight("100%");
    parent.add(panel);
    layout.doLayout(parent);

    assertEquals(0, panel.getX());
    assertEquals(0, panel.getY());
  }

  public void testScrollAdjusting() throws Exception
  {
    parent.getStyle().setScrollbars("on");
    layout.doLayout(parent);

    PropPanel panel = new PropPanel(new MockProp());
    panel.getStyle().setWidth("200");
    panel.getStyle().setHeight("300");
    parent.add(panel);
    layout.doLayout(parent);

    assertEquals(200, parent.getHorizontalScrollBar().getMaximumValue());
    assertEquals(100 - scrollGirth, parent.getHorizontalScrollBar().getVisibleAmount());
  }

  public void testLayoutRowsWithScrollOffsets() throws Exception
  {
    parent.getStyle().setScrollbars("on");
    PropPanel panel = new PropPanel(new MockProp());
    panel.getStyle().setWidth("200");
    panel.getStyle().setHeight("300");
    parent.add(panel);
    layout.doLayout(parent);

    parent.getVerticalScrollBar().setValue(100);
    parent.getHorizontalScrollBar().setValue(50);
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
}
