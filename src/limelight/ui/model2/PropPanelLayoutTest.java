package limelight.ui.model2;

import junit.framework.TestCase;

import java.awt.*;

import limelight.ui.api.MockProp;
import limelight.ui.api.MockStage;
import limelight.ui.MockPanel;

import javax.swing.*;

public class PropPanelLayoutTest extends TestCase
{
  private PropPanel parent;
  private PropPanelLayout layout;
  private RootPanel root;

  public void setUp() throws Exception
  {
    root = new RootPanel(new MockFrame());

    parent = new PropPanel(new MockProp());
    root.setPanel(parent);
    parent.getProp().getStyle().setWidth("100");
    parent.getProp().getStyle().setHeight("100");


    layout = new PropPanelLayout(parent);
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
}
