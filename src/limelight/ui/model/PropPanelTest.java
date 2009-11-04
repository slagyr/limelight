//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.api.MockProp;
import limelight.ui.*;
import limelight.ui.model.inputs.ScrollBarPanel;
import limelight.ui.painting.BorderPainter;
import limelight.ui.painting.BackgroundPainter;
import limelight.styles.FlatStyle;
import limelight.styles.ScreenableStyle;
import limelight.styles.Style;
import limelight.styles.StyleDescriptor;
import limelight.styles.styling.StaticDimensionAttribute;
import limelight.styles.styling.SimpleIntegerAttribute;
import limelight.styles.styling.SimplePercentageAttribute;
import limelight.styles.styling.StaticPixelsAttribute;
import limelight.util.Box;
import limelight.Context;
import limelight.caching.SimpleCache;
import limelight.audio.MockAudioPlayer;
import junit.framework.TestCase;

import javax.swing.*;
import java.util.LinkedList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.font.FontRenderContext;
import java.awt.event.*;

public class PropPanelTest extends TestCase
{
  private MockProp prop;
  private PropPanel panel;
  private ScreenableStyle style;
  private RootPanel root;

  public void setUp() throws Exception
  {
    root = new RootPanel(new MockPropFrame());

    prop = new MockProp();
    style = prop.getStyle();
    panel = new PropPanel(prop);
    root.setPanel(panel);
  }

  public void testOnlyPaintWhenLaidOut() throws Exception
  {
    MockAfterPaintAction paintAction = new MockAfterPaintAction();
    panel.setAfterPaintAction(paintAction);

    panel.paintOn(new MockGraphics());
    assertEquals(false, paintAction.invoked);

    panel.doLayout();
    panel.paintOn(new MockGraphics());
    assertEquals(true, paintAction.invoked);
  }

  public void testConstructor() throws Exception
  {
    assertSame(prop, panel.getProp());
    assertEquals(TextPaneTextAccessor.class, panel.getTextAccessor().getClass());
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

  public void testIsFloater() throws Exception
  {
    assertEquals(false, panel.isFloater());
    panel.getStyle().setFloat("on");
    assertEquals(true, panel.isFloater());
    panel.getStyle().setFloat("off");
    assertEquals(false, panel.isFloater());
  }

  public void testAfterPaintAction() throws Exception
  {
    MockAfterPaintAction action = new MockAfterPaintAction();
    panel.setAfterPaintAction(action);
    panel.setSize(100, 100);
    MockGraphics mockGraphics = new MockGraphics();
    mockGraphics.setClip(0, 0, 100, 100);
    panel.doLayout();
    panel.paintOn(mockGraphics);

    assertEquals(true, action.invoked);
  }

  public void testHasChangesWhenaStyleIsChanged() throws Exception
  {
    style.setWidth("100%");

    assertEquals(true, panel.needsLayout());
  }

  public void testHasChangesWhenaTextIsChanged() throws Exception
  {
    TextPanel.staticFontRenderingContext = new FontRenderContext(new AffineTransform(), true, true);
    panel.doLayout();

    panel.setText("blah");
    assertEquals(true, panel.needsLayout());

    panel.doLayout();
    panel.setText("blah");
    assertEquals(false, panel.needsLayout());

    panel.setText("new text");
    assertEquals(true, panel.needsLayout());
  }

  public void testAddingScrollBarChangesChildConsumableArea() throws Exception
  {
    int scrollWidth = new JScrollBar(JScrollBar.VERTICAL).getPreferredSize().width;
    style.setMargin("0");
    style.setPadding("0");
    style.setBorderWidth("0");
    panel.setSize(100, 100);

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
    panel.setSize(100, 100);

    MockPanel child = new MockPanel();
    child.setSize(100, 100);
    child.setLocation(0, 0);
    panel.add(child);

    panel.addVerticalScrollBar();
    ScrollBarPanel vertical = panel.getVerticalScrollbar();
    vertical.setSize(15, 100);
    vertical.setLocation(85, 0);
    panel.addHorizontalScrollBar();
    ScrollBarPanel horizontal = panel.getHorizontalScrollbar();
    horizontal.setSize(100, 15);
    horizontal.setLocation(0, 85);

    assertSame(child, panel.getOwnerOfPoint(new Point(0, 0)));
    assertSame(child, panel.getOwnerOfPoint(new Point(50, 50)));
    assertSame(vertical, panel.getOwnerOfPoint(new Point(90, 50)));
    assertSame(horizontal, panel.getOwnerOfPoint(new Point(50, 90)));
  }

  public void testGetOwnerOfPointGivesPriorityToFloaters() throws Exception
  {
    panel.setSize(100, 100);

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

  public void testVerticalMouseWheelMovement() throws Exception
  {
    panel.addVerticalScrollBar();
    panel.getVerticalScrollbar().configure(100, 200);
    panel.addHorizontalScrollBar();
    panel.getHorizontalScrollbar().configure(100, 200);

    int modifer = 0;
    int scrollAmount = 8;
    int wheelRotation = 2;
    MouseWheelEvent e = new MouseWheelEvent(root.getContentPane(), 1, 2, modifer, 4, 5, 6, false, 7, scrollAmount, wheelRotation);

    panel.mouseWheelMoved(e);

    assertEquals(16, panel.getVerticalScrollbar().getScrollBar().getValue());
    assertEquals(0, panel.getHorizontalScrollbar().getScrollBar().getValue());
  }

  public void testHorizontalMouseWheelMovement() throws Exception
  {
    panel.addVerticalScrollBar();
    panel.getVerticalScrollbar().configure(100, 200);
    panel.addHorizontalScrollBar();
    panel.getHorizontalScrollbar().configure(100, 200);

    int modifer = 1;
    int scrollAmount = 8;
    int wheelRotation = 2;
    MouseWheelEvent e = new MouseWheelEvent(root.getContentPane(), 1, 2, modifer, 4, 5, 6, false, 7, scrollAmount, wheelRotation);

    panel.mouseWheelMoved(e);

    assertEquals(0, panel.getVerticalScrollbar().getScrollBar().getValue());
    assertEquals(16, panel.getHorizontalScrollbar().getScrollBar().getValue());
  }

  public void testPlayAudioFile() throws Exception
  {
    MockAudioPlayer audioPlayer = new MockAudioPlayer();
    Context.instance().audioPlayer = audioPlayer;

    panel.playSound("blah");

    assertEquals("blah", audioPlayer.playedFile);
  }

  public void testFocusGained() throws Exception
  {
    FocusEvent event = new FocusEvent(new JPanel(), 1);
    panel.focusGained(event);

    assertNotNull(prop.gainedFocus);
    assertSame(event, prop.gainedFocus);
  }

  public void testFocusLost() throws Exception
  {
    FocusEvent event = new FocusEvent(new JPanel(), 1);
    panel.focusLost(event);

    assertNotNull(prop.lostFocus);
    assertSame(event, prop.lostFocus);
  }

  public void testKeyPressedEvent() throws Exception
  {
    KeyEvent event = new KeyEvent(new JPanel(), 1, 2, 3, 4, 'a');
    panel.keyPressed(event);

    assertSame(event, prop.pressedKey);
  }

  public void testKeyTypedEvent() throws Exception
  {
    KeyEvent event = new KeyEvent(new JPanel(), 1, 2, 3, 4, 'a');
    panel.keyTyped(event);

    assertSame(event, prop.typedKey);
  }

  public void testKeyReleasedEvent() throws Exception
  {
    KeyEvent event = new KeyEvent(new JPanel(), 1, 2, 3, 4, 'a');
    panel.keyReleased(event);

    assertSame(event, prop.releasedKey);
  }

  public void testButtonPressedEvent() throws Exception
  {
    ActionEvent event = new ActionEvent(new JPanel(), 1, "blah");
    panel.buttonPressed(event);

    assertSame(event, prop.pressedButton);
  }

  public void testValueChangedEvent() throws Exception
  {
    Object event = "blah";
    panel.valueChanged(event);

    assertSame(event, prop.changedValue);
  }

  public void testHoverOnWithHoverStyle() throws Exception
  {
    MouseEvent event = new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false);
    prop.hoverStyle = new FlatStyle();

    panel.mouseEntered(event);

    assertEquals(Cursor.HAND_CURSOR, root.getContentPane().getCursor().getType());
    assertSame(prop.hoverStyle, style.getScreen());
  }

  public void testHoverOnWithoutHoverStyle() throws Exception
  {
    MouseEvent event = new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false);
    prop.hoverStyle = null;

    panel.mouseEntered(event);

    assertEquals(Cursor.DEFAULT_CURSOR, root.getContentPane().getCursor().getType());
    assertEquals(null, style.getScreen());
  }

  public void testHoverOffWithHoverStyle() throws Exception
  {
    MouseEvent event = new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false);
    prop.hoverStyle = new FlatStyle();

    panel.mouseEntered(event);
    panel.mouseExited(event);

    assertEquals(Cursor.DEFAULT_CURSOR, root.getContentPane().getCursor().getType());
    assertEquals(null, style.getScreen());
  }

  public void testHoverOffWithoutHoverStyle() throws Exception
  {
    MouseEvent event = new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false);
    prop.hoverStyle = null;

    panel.mouseEntered(event);
    panel.mouseExited(event);

    assertEquals(Cursor.DEFAULT_CURSOR, root.getContentPane().getCursor().getType());
    assertEquals(null, style.getScreen());
  }

  public void testHoverOffWhenHoverStyledWasRemovedFromProp() throws Exception
  {
    MouseEvent event = new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false);
    prop.hoverStyle = new FlatStyle();

    panel.mouseEntered(event);
    prop.hoverStyle = null;
    panel.mouseExited(event);

    assertEquals(Cursor.DEFAULT_CURSOR, root.getContentPane().getCursor().getType());
    assertEquals(null, style.getScreen());
  }

  public void testWidthOrHeightChanges() throws Exception
  {
    panel.styleChanged(Style.WIDTH, new StaticDimensionAttribute(20));
    assertEquals(true, panel.sizeChangePending());
    panel.resetPendingSizeChange();
    assertEquals(false, panel.sizeChangePending());

    panel.styleChanged(Style.HEIGHT, new StaticDimensionAttribute(20));
    assertEquals(true, panel.sizeChangePending());
    panel.resetPendingSizeChange();
    assertEquals(false, panel.sizeChangePending());
  }

  public void testShouldPropagateConsumableAreaChangeForWidthChange() throws Exception
  {
    MockPanel child = new MockPanel();
    panel.add(child);

    panel.styleChanged(Style.WIDTH, new StaticDimensionAttribute(20));

    assertEquals(true, child.consumableAreaChangedCalled);
  }

  public void testShouldPropagateConsumableAreaChangeForBorderChange() throws Exception
  {
    MockPanel child = new MockPanel();
    panel.add(child);

    panel.styleChanged(Style.TOP_BORDER_WIDTH, new SimpleIntegerAttribute(5));

    assertEquals(true, child.consumableAreaChangedCalled);
  }

  public void testBorderStyleChanges() throws Exception
  {
    panel.getBorderShaper();
    checkBorderChanged(Style.TOP_BORDER_WIDTH);
    checkBorderChanged(Style.RIGHT_BORDER_WIDTH);
    checkBorderChanged(Style.BOTTOM_BORDER_WIDTH);
    checkBorderChanged(Style.LEFT_BORDER_WIDTH);
    checkBorderChanged(Style.TOP_RIGHT_BORDER_WIDTH);
    checkBorderChanged(Style.BOTTOM_RIGHT_BORDER_WIDTH);
    checkBorderChanged(Style.BOTTOM_LEFT_BORDER_WIDTH);
    checkBorderChanged(Style.TOP_LEFT_BORDER_WIDTH);
    checkBorderChanged(Style.TOP_RIGHT_ROUNDED_CORNER_RADIUS);
    checkBorderChanged(Style.BOTTOM_RIGHT_ROUNDED_CORNER_RADIUS);
    checkBorderChanged(Style.BOTTOM_LEFT_ROUNDED_CORNER_RADIUS);
    checkBorderChanged(Style.TOP_LEFT_ROUNDED_CORNER_RADIUS);
  }

  private void checkBorderChanged(StyleDescriptor styleDescriptor)
  {
    panel.styleChanged(styleDescriptor, new SimpleIntegerAttribute(5));
    assertEquals(true, panel.borderChanged());
    panel.doLayout();
    assertEquals(false, panel.borderChanged());
  }

  public void testMarginStyleChanges() throws Exception
  {
    panel.doLayout();
    assertEquals(false, panel.needsLayout());

    checkLayoutOnStyle(Style.TOP_MARGIN);
    checkLayoutOnStyle(Style.RIGHT_MARGIN);
    checkLayoutOnStyle(Style.BOTTOM_MARGIN);
    checkLayoutOnStyle(Style.LEFT_MARGIN);

    checkLayoutOnStyle(Style.TOP_PADDING);
    checkLayoutOnStyle(Style.RIGHT_PADDING);
    checkLayoutOnStyle(Style.BOTTOM_PADDING);
    checkLayoutOnStyle(Style.LEFT_PADDING);

//    checkLayoutOnStyle(Style.TOP_RIGHT_ROUNDED_CORNER_RADIUS);
//    checkLayoutOnStyle(Style.BOTTOM_RIGHT_ROUNDED_CORNER_RADIUS);
//    checkLayoutOnStyle(Style.BOTTOM_LEFT_ROUNDED_CORNER_RADIUS);
//    checkLayoutOnStyle(Style.TOP_LEFT_ROUNDED_CORNER_RADIUS);

    checkLayoutOnStyle(Style.TOP_BORDER_WIDTH);
    checkLayoutOnStyle(Style.TOP_RIGHT_BORDER_WIDTH);
    checkLayoutOnStyle(Style.RIGHT_BORDER_WIDTH);
    checkLayoutOnStyle(Style.BOTTOM_RIGHT_BORDER_WIDTH);
    checkLayoutOnStyle(Style.BOTTOM_BORDER_WIDTH);
    checkLayoutOnStyle(Style.BOTTOM_LEFT_BORDER_WIDTH);
    checkLayoutOnStyle(Style.LEFT_BORDER_WIDTH);
    checkLayoutOnStyle(Style.TOP_LEFT_BORDER_WIDTH);
  }

  private void checkLayoutOnStyle(StyleDescriptor styleDescriptor)
  {
    Box box = panel.getBoxInsidePadding();
    panel.styleChanged(styleDescriptor, new StaticPixelsAttribute(20));
    assertEquals(true, panel.needsLayout());
    panel.doLayout();
    assertNotSame(box, panel.getBoxInsidePadding());
    assertEquals(false, panel.needsLayout());
  }


  public void testShouldBuildBufferWhenStyleChanges() throws Exception
  {
    SimpleCache<limelight.ui.Panel, BufferedImage> cache = new SimpleCache<limelight.ui.Panel, BufferedImage>();
    Context.instance().bufferedImageCache = cache;
    cache.cache(panel, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));

    panel.styleChanged(Style.WIDTH, new StaticDimensionAttribute(20));
    assertEquals(null, cache.retrieve(panel));
  }

  public void testShouldNotBuildBufferWhenTransparencyChanges() throws Exception
  {
    SimpleCache<limelight.ui.Panel, BufferedImage> cache = new SimpleCache<limelight.ui.Panel, BufferedImage>();
    Context.instance().bufferedImageCache = cache;
    cache.cache(panel, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));

    panel.styleChanged(Style.TRANSPARENCY, new SimplePercentageAttribute(20));
    assertNotNull(cache.retrieve(panel));
  }

  public void testRequiredLayoutTriggeredWhilePerformingLayoutStillGetsRegistered() throws Exception
  {
    for(int i = 0; i < 100; i++)
      panel.add(new PropPanel(new MockProp()));
    panel.markAsNeedingLayout();
    Thread thread = new Thread(new Runnable()
    {
      public void run()
      {
        panel.doLayout();
      }
    });
    thread.start();

    while(panel.getChildren().get(0).needsLayout())
      Thread.yield();
    panel.markAsNeedingLayout();
    thread.join();

    assertEquals(true, panel.needsLayout());
  }

  public void testAlignmentChangeShouldInvokeLayout() throws Exception
  {
    panel.resetLayout();

    panel.styleChanged(Style.HORIZONTAL_ALIGNMENT, null);
    assertEquals(true, panel.needsLayout());

    panel.resetLayout();
    panel.styleChanged(Style.VERTICAL_ALIGNMENT, null);

    assertEquals(true, panel.needsLayout());
  }

  public void testChangingSizeToZeroWillReLayoutGrandDaddy() throws Exception
  {
    PropPanel child = new PropPanel(new MockProp());
    PropPanel grandChild = new PropPanel(new MockProp());
    panel.add(child);
    child.add(grandChild);
    child.resetLayout();
    panel.resetLayout();

    grandChild.styleChanged(Style.HEIGHT, new StaticDimensionAttribute(0));

    assertEquals(true, panel.needsLayout());
  }

  public void testScrollbarsDontGetRemovedOnRemoveAll() throws Exception
  {
    panel.addVerticalScrollBar();
    panel.addHorizontalScrollBar();
    panel.add(new PropPanel(new MockProp()));

    panel.removeAll();
    
    assertEquals(2, panel.children.size());
    assertEquals(panel.getVerticalScrollbar(), panel.children.get(0));
    assertEquals(panel.getHorizontalScrollbar(), panel.children.get(1));
  }


}
