package limelight.ui.model2;

import limelight.ui.api.MockProp;
import limelight.ui.Painter;
import limelight.ui.MockGraphics;
import limelight.ui.MockPanel;
import limelight.ui.model2.inputs.ScrollBarPanel;
import limelight.ui.painting.BorderPainter;
import limelight.ui.painting.BackgroundPainter;
import limelight.ui.painting.Border;
import limelight.ui.painting.PaintAction;
import limelight.styles.FlatStyle;
import limelight.util.Box;
import junit.framework.TestCase;

import javax.swing.*;
import java.util.LinkedList;
import java.awt.*;

public class PropPanelTest extends TestCase
{
  private MockProp prop;
  private PropPanel panel;
  private FlatStyle style;
  private RootPanel root;

  public void setUp() throws Exception
  {
    root = new RootPanel(new MockFrame());

    prop = new MockProp();
    style = prop.getStyle();
    panel = new PropPanel(prop);
    root.setPanel(panel);
  }

  public void testConstructor() throws Exception
  {
    assertSame(prop, panel.getProp());
    assertEquals(TextPaneTextAccessor.class, panel.getTextAccessor().getClass());
    assertNotNull(panel.getLayout());
  }

  public void testPainters() throws Exception
  {
    LinkedList<Painter> painters = panel.getPainters();

    assertEquals(2, painters.size());
    assertEquals(BackgroundPainter.class, painters.get(0).getClass());
    assertEquals(BorderPainter.class, painters.get(1).getClass());
  }

  public void testText() throws Exception
  {
    panel.setText("blah");
    assertEquals("blah", panel.getText());
    assertEquals("blah", panel.getTextAccessor().getText());

    panel.getTextAccessor().setText("foo");
    assertEquals("foo", panel.getText());
  }

  public void testPreferredSize() throws Exception
  {
    style.setWidth("100");
    style.setHeight("200");

    panel.snapToSize();

    assertEquals(100, panel.getWidth());
    assertEquals(200, panel.getHeight());
  }

  public void testSizeUsingAutoWidthAndHeight() throws Exception
  {
    root.setSize(100, 100);
    style.setWidth("auto");
    style.setHeight("auto");
    panel.snapToSize();
    assertEquals(100, panel.getWidth());
    assertEquals(100, panel.getHeight());

    style.setWidth("auto");
    style.setHeight("50");
    panel.snapToSize();
    assertEquals(100, panel.getWidth());
    assertEquals(50, panel.getHeight());

    style.setWidth("42%");
    style.setHeight("auto");
    panel.snapToSize();
    assertEquals(42, panel.getWidth());
    assertEquals(100, panel.getHeight());
  }

  public void testRactanglesAreCached() throws Exception
  {
    Box rectangle = panel.getBoundingBox();
    Box insideMargins = panel.getBoxInsideMargins();
    Box insideBorders = panel.getBoxInsideBorders();
    Box insidePadding = panel.getBoxInsidePadding();

    assertSame(rectangle, panel.getBoundingBox());
    assertSame(insideMargins, panel.getBoxInsideMargins());
    assertSame(insideBorders, panel.getBoxInsideBorders());
    assertSame(insidePadding, panel.getBoxInsidePadding());

    panel.setSize(123, 456);

    assertNotSame(rectangle, panel.getBoundingBox());
    assertNotSame(insideMargins, panel.getBoxInsideMargins());
    assertNotSame(insideBorders, panel.getBoxInsideBorders());
    assertNotSame(insidePadding, panel.getBoxInsidePadding());
  }

  public void testBorderGetUpdatedOnLayout() throws Exception
  {
    root.setSize(100, 100);
    Border border = panel.getBorderShaper();
    style.flushChanges();

    style.setBorderWidth("21");
    panel.doLayout();

    assertEquals(21, border.getTopWidth());
  }

  public void testIsFloater() throws Exception
  {
    assertEquals(false, panel.isFloater());
    panel.getStyle().setFloat("on");
    assertEquals(true, panel.isFloater());
    panel.getStyle().setFloat("off");
    assertEquals(false, panel.isFloater());
  }

  private boolean invoked;

  public void testAfterPaintAction() throws Exception
  {
    invoked = false;
    PaintAction action = new PaintAction()
    {

      public void invoke(Graphics2D graphics)
      {
        invoked = true;
      }
    };

    panel.setAfterPaintAction(action);
    panel.setSize(100, 100);
    MockGraphics mockGraphics = new MockGraphics();
    mockGraphics.setClip(0, 0, 100, 100);
    panel.paintOn(mockGraphics);

    assertEquals(true, invoked);
  }

  public void testHasChangesWhenaStyleIsChanged() throws Exception
  {
    style.setWidth("100%");

    assertEquals(true, panel.hasChanges());
  }

  public void testHasChangesWhenaTextIsChanged() throws Exception
  {
    panel.setText("blah");
    assertEquals(true, panel.hasChanges());

    panel.resetChangeMarker();
    panel.setText("blah");
    assertEquals(false, panel.hasChanges());

    panel.setText("new text");
    assertEquals(true, panel.hasChanges());
  }
  
  public void testMarkingAsChagedAddsPanelToChangeSet() throws Exception
  {
    assertEquals(0, root.getChangedPanelCount());
    panel.markAsChanged();
    assertEquals(1, root.getChangedPanelCount());
    assertEquals(true, root.changedPanelsContains(panel));
  }

  public void testAddingScrollBarChangesChildConsumableArea() throws Exception
  {
    int scrollWidth = new JScrollBar(JScrollBar.VERTICAL).getPreferredSize().width;
    style.setWidth("100");
    style.setHeight("100");
    style.setMargin("0");
    style.setPadding("0");
    style.setBorderWidth("0");
    panel.snapToSize();

    panel.addVerticalScrollBar();
    assertEquals(100 - scrollWidth, panel.getChildConsumableArea().width);
    assertEquals(100, panel.getChildConsumableArea().height);

    panel.addHorizontalScrollBar();
    assertEquals(100 - scrollWidth, panel.getChildConsumableArea().width);
    assertEquals(100 - scrollWidth, panel.getChildConsumableArea().height);

    panel.removeVerticalScrollBar();
    assertEquals(100, panel.getChildConsumableArea().width);
    assertEquals(100 - scrollWidth, panel.getChildConsumableArea().height);

    panel.removeHorizontalScrollBar();
    assertEquals(100, panel.getChildConsumableArea().width);
    assertEquals(100, panel.getChildConsumableArea().height);
  }

  public void testGetOwnerOfPointGivesPriorityToScrollBars() throws Exception
  {
    style.setWidth("100");
    style.setHeight("100");
    panel.snapToSize();

    MockPanel child = new MockPanel();
    child.setSize(100, 100);
    child.setLocation(0, 0);
    panel.add(child);

    panel.addVerticalScrollBar();
    ScrollBarPanel vertical = panel.getVerticalScrollBar();
    vertical.setSize(15, 100);
    vertical.setLocation(85, 0);
    panel.addHorizontalScrollBar();
    ScrollBarPanel horizontal = panel.getHorizontalScrollBar();
    horizontal.setSize(100, 15);
    horizontal.setLocation(0, 85);

    assertSame(child, panel.getOwnerOfPoint(new Point(0, 0)));
    assertSame(child, panel.getOwnerOfPoint(new Point(50, 50)));
    assertSame(vertical, panel.getOwnerOfPoint(new Point(90, 50)));
    assertSame(horizontal, panel.getOwnerOfPoint(new Point(50, 90)));
  }

  public void testGetOwnerOfPointGivesPriorityToFloaters() throws Exception
  {
    style.setWidth("100");
    style.setHeight("100");
    panel.snapToSize();

    MockPanel child = new MockPanel();
    child.setSize(100, 100);
    child.setLocation(0, 0);
    panel.add(child);

    MockPropablePanel floater = new MockPropablePanel();
    floater.setSize(50, 50);
    floater.setLocation(25, 25);
    floater.floater = true;
    panel.add(floater);

    assertSame(child, panel.getOwnerOfPoint(new Point(0, 0)));
    assertSame(floater, panel.getOwnerOfPoint(new Point(50, 50)));
  }
}
