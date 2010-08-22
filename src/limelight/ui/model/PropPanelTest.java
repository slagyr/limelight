//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.styles.*;
import limelight.ui.Panel;
import limelight.ui.api.MockProp;
import limelight.ui.*;
import limelight.ui.model.inputs.ScrollBarPanel;
import limelight.util.Box;
import limelight.Context;
import limelight.caching.SimpleCache;
import limelight.audio.MockAudioPlayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.font.FontRenderContext;
import java.awt.event.*;

public class PropPanelTest extends Assert
{
  private MockProp prop;
  private PropPanel panel;
  private ScreenableStyle style;
  private ScenePanel root;
  private RichStyle style1;
  private RichStyle style2;
  private RichStyle style3;
  private RichStyle style4;
  private RichStyle style5;

  @Before
  public void setUp() throws Exception
  {
    root = new ScenePanel(new MockProp());
    root.setFrame(new MockPropFrame());
    prop = new MockProp();
    panel = new PropPanel(prop);
    style = panel.getStyle();
    root.add(panel);


    Context.instance().bufferedImageCache = new SimpleCache<Panel, BufferedImage>();
  }
  
  @Test
  public void shouldCreatesItsStyleInsteadOfGettingItFromProp() throws Exception
  {
    panel = new PropPanel(null);
    
    assertNotNull(panel.getStyle());
    assertEquals(ScreenableStyle.class, panel.getStyle().getClass());
  }

  @Test
  public void shouldOnlyPaintWhenLaidOut() throws Exception
  {
    MockAfterPaintAction paintAction = new MockAfterPaintAction();
    panel.setAfterPaintAction(paintAction);

    panel.paintOn(new MockGraphics());
    assertEquals(false, paintAction.invoked);

    panel.doLayout();
    panel.paintOn(new MockGraphics());
    assertEquals(true, paintAction.invoked);
  }

  @Test
  public void shouldConstructor() throws Exception
  {
    assertSame(prop, panel.getProp());
    assertEquals(TempTextAccessor.class, panel.getTextAccessor().getClass());
  }

  @Test
  public void shouldText() throws Exception
  {
    panel.setText("blah");
    assertEquals("blah", panel.getText());
    assertEquals("blah", panel.getTextAccessor().getText());

    panel.getTextAccessor().setText(panel, "foo");
    assertEquals("foo", panel.getText());
  }

  @Test
  public void shouldSettingTextShouldLeadToLayout() throws Exception
  {
    panel.resetLayout();
    panel.setText("Some Text");
  }

  @Test
  public void shouldRactanglesAreCached() throws Exception
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

  @Test
  public void shouldIsFloater() throws Exception
  {
    assertEquals(false, panel.isFloater());
    panel.getStyle().setFloat("on");
    assertEquals(true, panel.isFloater());
    panel.getStyle().setFloat("off");
    assertEquals(false, panel.isFloater());
  }

  @Test
  public void shouldAfterPaintAction() throws Exception
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

  @Test
  public void shouldHasChangesWhenaStyleIsChanged() throws Exception
  {
    style.setWidth("100%");

    assertEquals(true, panel.needsLayout());
  }

  @Test
  public void shouldHasChangesWhenaTextIsChanged() throws Exception
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

  @Test
  public void shouldAddingScrollBarChangesChildConsumableArea() throws Exception
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

  @Test
  public void shouldGetOwnerOfPointGivesPriorityToScrollBars() throws Exception
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

  @Test
  public void shouldGetOwnerOfPointGivesPriorityToFloaters() throws Exception
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

  @Test
  public void shouldVerticalMouseWheelMovement() throws Exception
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

    assertEquals(16, panel.getVerticalScrollbar().getValue());
    assertEquals(0, panel.getHorizontalScrollbar().getValue());
  }

  @Test
  public void shouldHorizontalMouseWheelMovement() throws Exception
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

    assertEquals(0, panel.getVerticalScrollbar().getValue());
    assertEquals(16, panel.getHorizontalScrollbar().getValue());
  }

  @Test
  public void shouldPlayAudioFile() throws Exception
  {
    MockAudioPlayer audioPlayer = new MockAudioPlayer();
    Context.instance().audioPlayer = audioPlayer;

    panel.playSound("blah");

    assertEquals("blah", audioPlayer.playedFile);
  }

  @Test
  public void shouldFocusGained() throws Exception
  {
    FocusEvent event = new FocusEvent(new JPanel(), 1);
    panel.focusGained(event);

    assertNotNull(prop.gainedFocus);
    assertSame(event, prop.gainedFocus);
  }

  @Test
  public void shouldFocusLost() throws Exception
  {
    FocusEvent event = new FocusEvent(new JPanel(), 1);
    panel.focusLost(event);

    assertNotNull(prop.lostFocus);
    assertSame(event, prop.lostFocus);
  }

  @Test
  public void shouldKeyPressedEvent() throws Exception
  {
    KeyEvent event = new KeyEvent(new JPanel(), 1, 2, 3, 4, 'a');
    panel.keyPressed(event);

    assertSame(event, prop.pressedKey);
  }

  @Test
  public void shouldKeyTypedEvent() throws Exception
  {
    KeyEvent event = new KeyEvent(new JPanel(), 1, 2, 3, 4, 'a');
    panel.keyTyped(event);

    assertSame(event, prop.typedKey);
  }

  @Test
  public void shouldKeyReleasedEvent() throws Exception
  {
    KeyEvent event = new KeyEvent(new JPanel(), 1, 2, 3, 4, 'a');
    panel.keyReleased(event);

    assertSame(event, prop.releasedKey);
  }

  @Test
  public void shouldButtonPressedEvent() throws Exception
  {
    ActionEvent event = new ActionEvent(new JPanel(), 1, "blah");
    panel.buttonPressed(event);

    assertSame(event, prop.pressedButton);
  }

  @Test
  public void shouldValueChangedEvent() throws Exception
  {
    Object event = "blah";
    panel.valueChanged(event);

    assertSame(event, prop.changedValue);
  }

  @Test
  public void shouldHoverOnWithHoverStyle() throws Exception
  {
    panel.getHoverStyle().setCuror("hand");
    MouseEvent event = new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false);

    panel.mouseEntered(event);

    assertEquals(Cursor.HAND_CURSOR, root.getContentPane().getCursor().getType());
    assertSame(panel.getHoverStyle(), style.getScreen());
  }

  @Test
  public void shouldHoverOffWithHoverStyle() throws Exception
  {
    MouseEvent event = new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false);
    prop.hoverStyle = new FlatStyle();

    panel.mouseEntered(event);
    panel.mouseExited(event);

    assertEquals(Cursor.DEFAULT_CURSOR, root.getContentPane().getCursor().getType());
    assertEquals(null, style.getScreen());
  }

  @Test
  public void shouldHoverOffWithoutHoverStyle() throws Exception
  {
    MouseEvent event = new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false);
    prop.hoverStyle = null;

    panel.mouseEntered(event);
    panel.mouseExited(event);

    assertEquals(Cursor.DEFAULT_CURSOR, root.getContentPane().getCursor().getType());
    assertEquals(null, style.getScreen());
  }

  @Test
  public void shouldHoverOffWhenHoverStyledWasRemovedFromProp() throws Exception
  {
    MouseEvent event = new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false);
    prop.hoverStyle = new FlatStyle();

    panel.mouseEntered(event);
    prop.hoverStyle = null;
    panel.mouseExited(event);

    assertEquals(Cursor.DEFAULT_CURSOR, root.getContentPane().getCursor().getType());
    assertEquals(null, style.getScreen());
  }

  @Test
  public void shouldRequiredLayoutTriggeredWhilePerformingLayoutStillGetsRegistered() throws Exception
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

  @Test
  public void shouldScrollbarsDontGetRemovedOnRemoveAll() throws Exception
  {
    panel.addVerticalScrollBar();
    panel.addHorizontalScrollBar();
    panel.add(new PropPanel(new MockProp()));

    panel.removeAll();
    
    assertEquals(2, panel.children.size());
    assertEquals(panel.getVerticalScrollbar(), panel.children.get(0));
    assertEquals(panel.getHorizontalScrollbar(), panel.children.get(1));
  }

  @Test
  public void shouldHaveListOfStyles() throws Exception
  {
    panel.setStyles("one, two, three");

    assertEquals("one, two, three", panel.getStyles());
  }

  private void buildStyles()
  {
    style1 = new RichStyle();
    style2 = new RichStyle();
    style3 = new RichStyle();
    style4 = new RichStyle();
    style5 = new RichStyle();
    root.getStylesStore().put("one", style1);
    root.getStylesStore().put("two", style2);
    root.getStylesStore().put("three", style3);
    root.getStylesStore().put("one.hover", style4);
    root.getStylesStore().put("two.hover", style5);
  }

  @Test
  public void shouldExtendStylesUponIllumination() throws Exception
  {
    buildStyles();

    panel.setStyles("one, two, three");
    panel.illuminate();

    assertEquals(true, panel.getStyle().hasExtension(style1));
    assertEquals(true, panel.getStyle().hasExtension(style2));
    assertEquals(true, panel.getStyle().hasExtension(style3));
  }

  @Test
  public void shouldExtendHoverStylesUponIllumination() throws Exception
  {
    buildStyles();

    panel.setStyles("one, two");
    panel.illuminate();

    assertEquals(true, panel.getStyle().hasExtension(style1));
    assertEquals(true, panel.getStyle().hasExtension(style2));
    assertEquals(false, panel.getStyle().hasExtension(style3));    
    assertEquals(true, panel.getHoverStyle().hasExtension(style4));
    assertEquals(true, panel.getHoverStyle().hasExtension(style5));
  }

  @Test
  public void shouldClearExtensionUponDellumination() throws Exception
  {
    buildStyles();
    panel.setStyles("one, two, three");
    panel.illuminate();
    panel.delluminate();

    assertEquals(false, panel.getStyle().hasExtension(style1));
    assertEquals(false, panel.getStyle().hasExtension(style2));
    assertEquals(false, panel.getStyle().hasExtension(style3));
    assertEquals(false, panel.getHoverStyle().hasExtension(style4));
    assertEquals(false, panel.getHoverStyle().hasExtension(style5));
  }

  @Test
  public void shouldHaveHandCursorWhenHoverStyleIsSpecified() throws Exception
  {
    buildStyles();
    panel.setStyles("one");
    panel.illuminate();

    assertEquals("hand", panel.getStyle().getCursor());
  }
}
