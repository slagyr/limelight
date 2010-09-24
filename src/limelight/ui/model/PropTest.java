//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.LimelightException;
import limelight.model.MockProduction;
import limelight.styles.*;
import limelight.ui.Panel;
import limelight.model.api.MockCastingDirector;
import limelight.model.api.MockPropProxy;
import limelight.ui.*;
import limelight.ui.events.panel.MouseEnteredEvent;
import limelight.ui.events.panel.MouseExitedEvent;
import limelight.ui.events.panel.MouseWheelEvent;
import limelight.ui.model.inputs.ScrollBarPanel;
import limelight.util.Box;
import limelight.Context;
import limelight.caching.SimpleCache;
import limelight.audio.MockAudioPlayer;
import limelight.util.Util;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.font.FontRenderContext;
import java.util.List;

public class PropTest extends Assert
{
  private MockPropProxy prop;
  private Prop panel;
  private ScreenableStyle style;
  private Scene root;
  private RichStyle style1;
  private RichStyle style2;
  private RichStyle style3;
  private RichStyle style4;
  private RichStyle style5;
  private MockProduction production;
  private MockCastingDirector castingDirector;

  @Before
  public void setUp() throws Exception
  {
    root = new Scene(new MockPropProxy());
    prop = new MockPropProxy();
    panel = new Prop(prop);
    root.add(panel);
                                          
    production = new MockProduction();
    castingDirector = new MockCastingDirector();
    production.setCastingDirector(castingDirector);
    root.setProduction(production);
    root.setStage(new MockStage());
    style = panel.getStyle();

    Context.instance().bufferedImageCache = new SimpleCache<Panel, BufferedImage>();
  }

  @Test
  public void createsItsStyleInsteadOfGettingItFromProp() throws Exception
  {
    panel = new Prop(null);

    assertNotNull(panel.getStyle());
    assertEquals(ScreenableStyle.class, panel.getStyle().getClass());
  }

  @Test
  public void onlyPaintWhenLaidOut() throws Exception
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
  public void constructor() throws Exception
  {
    assertSame(prop, panel.getProp());
    assertEquals(TempTextAccessor.class, panel.getTextAccessor().getClass());
  }

  @Test
  public void marginedBoundsIncludeMarginButIgnoreLocation() throws Exception
  {
    panel.setLocation(3, 3);
    panel.setSize(50, 50);
    panel.getStyle().setMargin(5);

    final Box bounds = panel.getMarginedBounds();

    assertEquals(5, bounds.x);
    assertEquals(5, bounds.y);
    assertEquals(40, bounds.width);
    assertEquals(40, bounds.height);
  }

  @Test
  public void shouldText() throws Exception
  {
    panel.setText("blah");
    assertEquals("blah", panel.getText());
    assertEquals("blah", panel.getTextAccessor().getText());

    panel.getTextAccessor().setText("foo", panel);
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
    Box rectangle = panel.getBounds();
    Box insideMargins = panel.getMarginedBounds();
    Box insideBorders = panel.getBorderedBounds();
    Box insidePadding = panel.getPaddedBounds();

    assertSame(rectangle, panel.getBounds());
    assertSame(insideMargins, panel.getMarginedBounds());
    assertSame(insideBorders, panel.getBorderedBounds());
    assertSame(insidePadding, panel.getPaddedBounds());

    panel.setSize(123, 456);

    assertNotSame(rectangle, panel.getBounds());
    assertNotSame(insideMargins, panel.getMarginedBounds());
    assertNotSame(insideBorders, panel.getBorderedBounds());
    assertNotSame(insidePadding, panel.getPaddedBounds());
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
    style.setMargin("0");
    style.setPadding("0");
    style.setBorderWidth("0");
    panel.setSize(100, 100);

    panel.addVerticalScrollBar();
    assertEquals(100 - ScrollBarPanel.GIRTH, panel.getChildConsumableBounds().width);
    assertEquals(100, panel.getChildConsumableBounds().height);

    panel.addHorizontalScrollBar();
    assertEquals(100 - ScrollBarPanel.GIRTH, panel.getChildConsumableBounds().width);
    assertEquals(100 - ScrollBarPanel.GIRTH, panel.getChildConsumableBounds().height);

    panel.removeVerticalScrollBar();
    assertEquals(100, panel.getChildConsumableBounds().width);
    assertEquals(100 - ScrollBarPanel.GIRTH, panel.getChildConsumableBounds().height);

    panel.removeHorizontalScrollBar();
    assertEquals(100, panel.getChildConsumableBounds().width);
    assertEquals(100, panel.getChildConsumableBounds().height);
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

    new MouseWheelEvent(panel, modifer, null, 0, MouseWheelEvent.UNIT_SCROLL, scrollAmount, wheelRotation).dispatch(panel);

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
    new MouseWheelEvent(panel, modifer, null, 0, MouseWheelEvent.UNIT_SCROLL, scrollAmount, wheelRotation).dispatch(panel);

    assertEquals(0, panel.getVerticalScrollbar().getValue());
    assertEquals(16, panel.getHorizontalScrollbar().getValue());
  }

  @Test
  public void wheelEventsArePassedToParentIfTheresNoScrollbar() throws Exception
  {
    Prop child = new Prop(new MockPropProxy());
    panel.add(child);

    panel.addVerticalScrollBar();
    panel.getVerticalScrollbar().configure(100, 200);
    panel.addHorizontalScrollBar();
    panel.getHorizontalScrollbar().configure(100, 200);

    int modifer = 0;
    int scrollAmount = 8;
    int wheelRotation = 2;

    new MouseWheelEvent(child, modifer, null, 0, MouseWheelEvent.UNIT_SCROLL, scrollAmount, wheelRotation).dispatch(panel);

    assertEquals(16, panel.getVerticalScrollbar().getValue());
    assertEquals(0, panel.getHorizontalScrollbar().getValue());
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
  public void shouldHoverOnWithHoverStyle() throws Exception
  {
    panel.getHoverStyle().setCuror("hand");

    new MouseEnteredEvent(panel, 0, null, 0).dispatch(panel);

    assertEquals(Cursor.HAND_CURSOR, root.getCursor().getType());
    assertSame(panel.getHoverStyle(), style.getScreen());
  }

  @Test
  public void shouldHoverOffWithHoverStyle() throws Exception
  {
    prop.hoverStyle = new FlatStyle();

    new MouseEnteredEvent(panel, 0, null, 0).dispatch(panel);
    new MouseExitedEvent(panel, 0, null, 0).dispatch(panel);

    assertEquals(Cursor.DEFAULT_CURSOR, root.getCursor().getType());
    assertEquals(null, style.getScreen());
  }

  @Test
  public void shouldHoverOffWithoutHoverStyle() throws Exception
  {
    prop.hoverStyle = null;

    new MouseEnteredEvent(panel, 0, null, 0).dispatch(panel);
    new MouseExitedEvent(panel, 0, null, 0).dispatch(panel);

    assertEquals(Cursor.DEFAULT_CURSOR, root.getCursor().getType());
    assertEquals(null, style.getScreen());
  }

  @Test
  public void shouldHoverOffWhenHoverStyledWasRemovedFromProp() throws Exception
  {
    prop.hoverStyle = new FlatStyle();

    new MouseEnteredEvent(panel, 0, null, 0).dispatch(panel);
    prop.hoverStyle = null;
    new MouseExitedEvent(panel, 0, null, 0).dispatch(panel);

    assertEquals(Cursor.DEFAULT_CURSOR, root.getCursor().getType());
    assertEquals(null, style.getScreen());
  }

  @Test
  public void shouldRequiredLayoutTriggeredWhilePerformingLayoutStillGetsRegistered() throws Exception
  {
    for(int i = 0; i < 100; i++)
      panel.add(new Prop(new MockPropProxy()));
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
    panel.add(new Prop(new MockPropProxy()));

    panel.removeAll();

    assertEquals(2, panel.children.size());
    assertEquals(panel.getVerticalScrollbar(), panel.children.get(0));
    assertEquals(panel.getHorizontalScrollbar(), panel.children.get(1));
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
    root.delluminate();

    panel.addOptions(Util.toMap("styles", "one, two, three"));
    panel.illuminate();

    assertEquals(true, panel.getStyle().hasExtension(style1));
    assertEquals(true, panel.getStyle().hasExtension(style2));
    assertEquals(true, panel.getStyle().hasExtension(style3));
  }

  @Test
  public void shouldExtendHoverStylesUponIllumination() throws Exception
  {
    buildStyles();
    root.delluminate();

    panel.addOptions(Util.toMap("styles", "one, two"));
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
    root.delluminate();
    panel.addOptions(Util.toMap("styles", "one, two, three"));
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
    root.delluminate();
    panel.addOptions(Util.toMap("styles", "one"));
    panel.illuminate();

    assertEquals("hand", panel.getStyle().getCursor());
  }

  @Test
  public void cantSetIdToEmptyString() throws Exception
  {
    root.delluminate();
    panel.addOptions(Util.toMap("id", ""));
    panel.illuminate();
    assertEquals(null, panel.getId());
  }

  @Test
  public void settingIdViaOptions() throws Exception
  {
    root.delluminate();

    panel.addOptions(Util.toMap("id", "007"));
    panel.illuminate();

    assertEquals("007", panel.getId());
  }

  @Test
  public void namingViaOptions() throws Exception
  {
    root.delluminate();

    panel.addOptions(Util.toMap("name", "Bill"));
    panel.illuminate();

    assertEquals("Bill", panel.getName());
  }

  @Test
  public void stylesAreAddedInOrderAfterTheName() throws Exception
  {
    root.delluminate();
    final RichStyle billStyle = new RichStyle();
    final RichStyle oneStyle = new RichStyle();
    final RichStyle twoStyle = new RichStyle();
    root.getStylesStore().put("bill", billStyle);
    root.getStylesStore().put("one", oneStyle);
    root.getStylesStore().put("two", twoStyle);
    panel.addOptions(Util.toMap("name", "bill", "styles", "one, two"));

    panel.illuminate();

    final List<RichStyle> styleExtensions = panel.getStyle().getExtentions();
    assertEquals(3, styleExtensions.size());
    assertEquals(billStyle, styleExtensions.get(0));
    assertEquals(oneStyle, styleExtensions.get(1));
    assertEquals(twoStyle, styleExtensions.get(2));
  }

  @Test
  public void styleForNameIsAddedEvenIfThereAreNoSylesInOptions() throws Exception
  {
    root.delluminate();
    final RichStyle billStyle = new RichStyle();
    root.getStylesStore().put("bill", billStyle);
    panel.addOptions(Util.toMap("name", "bill"));

    panel.illuminate();

    final List<RichStyle> styleExtensions = panel.getStyle().getExtentions();
    assertEquals(1, styleExtensions.size());
    assertEquals(billStyle, styleExtensions.get(0));
  }

  @Test
  public void cantAddOptionsAfterIllumination() throws Exception
  {
    root.delluminate();
    panel.illuminate();

    try
    {
      panel.addOptions(Util.toMap("name", "bill"));
      fail("should have thrown exception");
    }
    catch(LimelightException e)
    {
      assertEquals("Cannot add options to an illuminated Prop", e.getMessage());
    }
  }

  @Test
  public void propHasPlayerCastedBasedOnName() throws Exception
  {
    root.delluminate();
    panel.addOptions(Util.toMap("name", "jumpy"));

    panel.illuminate();

    List<String> panelCastings = castingDirector.castings.get(panel.getProp());
    assertEquals(1, panelCastings.size());
    assertEquals("jumpy", panelCastings.get(0));
  }

  @Test
  public void propHasPlayerCastedBasedPlayersOption() throws Exception
  {
    root.delluminate();
    panel.addOptions(Util.toMap("name", "jumpy", "players", "itchy, scratchy"));

    panel.illuminate();

    List<String> panelCastings = castingDirector.castings.get(panel.getProp());
    assertEquals(3, panelCastings.size());
    assertEquals("jumpy", panelCastings.get(0));
    assertEquals("itchy", panelCastings.get(1));
    assertEquals("scratchy", panelCastings.get(2));
  }

  @Test
  public void leftOverOptionsArePassedToPropOnIllumination() throws Exception
  {
    root.delluminate();
    panel.addOptions(Util.toMap("name", "bill", "foo", "bar"));
    panel.addOptions(Util.toMap("fizz", "bang"));
    panel.illuminate();

    assertEquals(2, prop.appliedOptions.size());
    assertEquals("bar", prop.appliedOptions.get("foo"));
    assertEquals("bang", prop.appliedOptions.get("fizz"));
  }

  @Test
  public void findByName() throws Exception
  {
    root.delluminate();
    Prop foo1 = new Prop(new MockPropProxy(), Util.toMap("name", "foo"));
    Prop foo2 = new Prop(new MockPropProxy(), Util.toMap("name", "foo"));
    Prop bar = new Prop(new MockPropProxy(), Util.toMap("name", "bar"));
    panel.add(foo1);
    panel.add(foo2);
    panel.add(bar);
    panel.illuminate();

    List<Prop> foos = panel.findByName("foo");
    assertEquals(2, foos.size());
    assertEquals(true, foos.contains(foo1));
    assertEquals(true, foos.contains(foo2));

    List<Prop> bars = panel.findByName("bar");
    assertEquals(1, bars.size());
    assertEquals(true, bars.contains(bar));
  }
}
