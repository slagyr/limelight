package limelight.ui.model;

import junit.framework.TestCase;

import java.awt.*;

import limelight.ui.api.MockProp;
import limelight.ui.model.inputs.ScrollBarPanel;
import limelight.ui.model.updates.LayoutAndPaintUpdate;

import javax.swing.*;

public class PropPanelLayoutTest extends TestCase
{
  private PropPanel parent;
  private PropPanelLayout layout;
  private RootPanel root;
  private int scrollGirth;


  public void setUp() throws Exception
  {
    root = new RootPanel(new MockFrame());

    parent = new PropPanel(new MockProp());
    root.setPanel(parent);
    parent.getStyle().setWidth("100");
    parent.getStyle().setHeight("100");


    layout = new PropPanelLayout(parent);
    scrollGirth = new JScrollBar(JScrollBar.VERTICAL).getPreferredSize().width;
  }

  public void testLayoutWithOneFullSizedChild() throws Exception
  {
    MockPropablePanel child = createAndAddChildWithSize(100, 100);

    layout.doLayout();

    assertTrue(child.wasLaidOut);
    assertEquals(new Point(0, 0), child.getLocation());
  }

  public void testLayoutWithTwoChildrenSameRow() throws Exception
  {
    MockPropablePanel child1 = createAndAddChildWithSize(50, 50);
    MockPropablePanel child2 = createAndAddChildWithSize(50, 50);

    layout.doLayout();

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

    layout.doLayout();

    assertEquals(new Point(0, 0), child1.getLocation());
    assertEquals(new Point(50, 0), child2.getLocation());
    assertEquals(new Point(0, 50), child3.getLocation());
  }

  public void testLayoutWithOneChildAlignedBottomRight() throws Exception
  {
    parent.getProp().getStyle().setVerticalAlignment("bottom");
    parent.getProp().getStyle().setHorizontalAlignment("right");
    MockPropablePanel child = createAndAddChildWithSize(50, 50);

    layout.doLayout();

    assertTrue(child.wasLaidOut);
    assertEquals(new Point(50, 50), child.getLocation());
  }

  public void testLayoutWithTwoChildrenAlignedCenterCenter() throws Exception
  {
    parent.getProp().getStyle().setVerticalAlignment("center");
    parent.getProp().getStyle().setHorizontalAlignment("center");
    MockPropablePanel child1 = createAndAddChildWithSize(25, 50);
    MockPropablePanel child2 = createAndAddChildWithSize(25, 50);

    layout.doLayout();

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

    layout.doLayout();

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

    layout.doLayout();

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

    layout.doLayout();

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

    layout.doLayout();

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

    layout.doLayout();

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

    layout.doLayout();

    assertEquals(0, panel.getWidth());
    assertEquals(0, panel.getHeight());
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

    layout.doLayout();

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

    layout.doLayout();

    assertEquals(50, panel.getWidth());
    assertEquals(50, panel.getHeight());
  }
  
  public void testFloatersAreLaidOut() throws Exception
  {
    MockPropablePanel floater = createAndAddChildWithSize(50, 50);
    floater.floater = true;
    floater.getStyle().setX("2");
    floater.getStyle().setY("3");
    MockPropablePanel child = createAndAddChildWithSize(50, 50);

    layout.doLayout();

    assertTrue(floater.wasLaidOut);
    assertTrue(child.wasLaidOut);
    assertEquals(new Point(2, 3), floater.getLocation());
    assertEquals(new Point(0, 0), child.getLocation());
  }
  
  public void testAddingScrollBars() throws Exception
  {
    assertEquals(0, parent.getChildren().size());
    assertEquals(null, parent.getVerticalScrollBar());
    assertEquals(null, parent.getHorizontalScrollBar());
    parent.getStyle().setScrollBars("on");

    layout.establishScrollBars();

    assertEquals(2, parent.getChildren().size());
    assertNotNull(parent.getVerticalScrollBar());
    assertNotNull(parent.getHorizontalScrollBar());
  }

  public void testRemovingScrollBars() throws Exception
  {
    parent.getStyle().setScrollBars("on");
    layout.establishScrollBars();
    assertEquals(2, parent.getChildren().size());
    assertNotNull(parent.getVerticalScrollBar());
    assertNotNull(parent.getHorizontalScrollBar());

    parent.getStyle().setScrollBars("off");
    layout.establishScrollBars();
    assertEquals(0, parent.getChildren().size());
    assertNull(parent.getVerticalScrollBar());
    assertNull(parent.getHorizontalScrollBar());
  }
  
  public void testScrollBarLayout() throws Exception
  {
    parent.getStyle().setScrollBars("on");

    layout.doLayout();

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
    panel.getStyle().setScrollBars("on");

    layout.doLayout();

    assertEquals(0, panel.getWidth());
    assertEquals(0, panel.getHeight());
  }

  public void testScrollBarsDontGetLayoutLikeOtherProps() throws Exception
  {
    parent.getStyle().setScrollBars("on");
    layout.doLayout();

    PropPanel panel = new PropPanel(new MockProp());
    panel.getStyle().setWidth("100%");
    panel.getStyle().setHeight("100%");
    parent.add(panel);
    layout.doLayout();

    assertEquals(0, panel.getX());
    assertEquals(0, panel.getY());
  }

  public void testScrollAdjusting() throws Exception
  {
    parent.getStyle().setScrollBars("on");
    layout.doLayout();

    PropPanel panel = new PropPanel(new MockProp());
    panel.getStyle().setWidth("200");
    panel.getStyle().setHeight("300");
    parent.add(panel);
    layout.doLayout();

    assertEquals(200, parent.getHorizontalScrollBar().getMaximumValue());
    assertEquals(100 - scrollGirth, parent.getHorizontalScrollBar().getVisibleAmount());
  }

  public void testLayoutRowsWithScrollOffsets() throws Exception
  {
    parent.getStyle().setScrollBars("on");
    PropPanel panel = new PropPanel(new MockProp());
    panel.getStyle().setWidth("200");
    panel.getStyle().setHeight("300");
    parent.add(panel);
    layout.doLayout();

    parent.getVerticalScrollBar().setValue(100);
    parent.getHorizontalScrollBar().setValue(50);
    layout.doLayout();

    assertEquals(-50, panel.getX());
    assertEquals(-100, panel.getY());
  }
  
  public void testParentNeedsUpdateWhenSizeGoesToZero() throws Exception
  {
    PropPanel child = new PropPanel(new MockProp());
    parent.add(child);
    parent.resetNeededUpdate();
    child.setSize(50, 50);
    child.getStyle().setHeight("auto");

    child.doLayout();

    assertEquals(0, child.getWidth());
    assertEquals(0, child.getHeight());
    assertEquals(true, parent.needsUpdating());
    assertEquals(LayoutAndPaintUpdate.class, parent.getNeededUpdate().getClass());
  }

  public void testParentShouldNotBeUodatedWhenSizeIsAlreadyZero() throws Exception
  {
    PropPanel child = new PropPanel(new MockProp());
    parent.add(child);
    parent.resetNeededUpdate();
    child.setSize(0, 0);
    child.getStyle().setHeight("auto");

    child.doLayout();

    assertEquals(false, parent.needsUpdating());
  }

  public void testShouldEnlargePropsWithZeroDimensionsWhenItHasChildren() throws Exception
  {
    PropPanel child = new PropPanel(new MockProp());
    parent.add(child);
    parent.resetNeededUpdate();
    parent.setSize(100,100);
    child.setSize(0, 0);
    child.getStyle().setHeight("auto");
    child.getStyle().flushChanges();
    PropPanel grandChild1 = new PropPanel(new MockProp());
    grandChild1.getStyle().setWidth("50");
    grandChild1.getStyle().setHeight("50");
    PropPanel grandChild2 = new PropPanel(new MockProp());
    grandChild2.getStyle().setWidth("50");
    grandChild2.getStyle().setHeight("50");
    child.add(grandChild1);
    child.add(grandChild2);

    child.doLayout();

    assertEquals(100, child.getWidth());
    assertEquals(50, child.getHeight());
    assertEquals(true, parent.needsUpdating());
  }
}
