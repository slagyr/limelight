package limelight.ui.model.inputs.painting;

import limelight.Context;
import limelight.KeyboardFocusManager;
import limelight.ui.MockGraphics;
import limelight.ui.Painter;
import limelight.ui.api.MockProp;
import limelight.ui.model.MockDrawable;
import limelight.ui.model.MockRootPanel;
import limelight.ui.model.PropPanel;
import limelight.ui.model.inputs.TextBoxPanel;
import limelight.ui.painting.BorderPainter;
import limelight.ui.painting.MockPainter;
import org.junit.*;

import java.awt.*;

public class TextPanelBorderPainterTest extends Assert
{
  private static Painter realBorderPainter;
  private MockRootPanel root;

  @BeforeClass
  public static void recordPainters()
  {
    realBorderPainter = BorderPainter.instance;
  }

  @AfterClass
  public static void restorePainters()
  {
    BorderPainter.instance = realBorderPainter;
  }

  private MockDrawable normalDrawable;
  private MockDrawable focusDrawable;

  private PropPanel parent;
  private Painter painter;
  private MockGraphics graphics;
  private TextBoxPanel panel;

  @Before
  public void setUp() throws Exception
  {
    root = new MockRootPanel();
    parent = new PropPanel(new MockProp());
    root.add(parent);
    panel = new TextBoxPanel();
    parent.add(panel);
    graphics = new MockGraphics();

    TextPanelBorderPainter.normalBorder = normalDrawable = new MockDrawable();
    TextPanelBorderPainter.focusedBorder = focusDrawable = new MockDrawable();

    painter = TextPanelBorderPainter.instance;
    parent.getStyle().setBorderColor("transparent");
  }

  private void assertDrawn(MockDrawable normalDrawable, Graphics expectedGraphics, int expectedX, int expectedY, int expectedWidth, int expectedHeight)
  {
    assertEquals(expectedGraphics, normalDrawable.drawnGraphics2D);
    assertEquals(expectedX, normalDrawable.drawnX);
    assertEquals(expectedY, normalDrawable.drawnY);
    assertEquals(expectedWidth, normalDrawable.drawnWidth);
    assertEquals(expectedHeight, normalDrawable.drawnHeight);
  }

  private void assertNotDrawn(MockDrawable normalDrawable)
  {
    assertEquals(null, normalDrawable.drawnGraphics2D);
    assertEquals(0, normalDrawable.drawnX);
    assertEquals(0, normalDrawable.drawnY);
    assertEquals(0, normalDrawable.drawnWidth);
    assertEquals(0, normalDrawable.drawnHeight);
  }

  @Test
  public void willUseBothBackgroundsWhenFocused()
  {
    root.getKeyListener().focusOn(panel);
//    Context.instance().keyboardFocusManager.focusPanel(panel);

    painter.paint(graphics, parent);

    assertDrawn(normalDrawable, graphics, 0, 0, parent.getWidth(), parent.getHeight());
    assertDrawn(focusDrawable, graphics, 0, 0, parent.getWidth(), parent.getHeight());
  }

  @Test
  public void willOnlyUseNormalBackgroundIfNotFocused()
  {
    painter.paint(graphics, parent);

    assertDrawn(normalDrawable, graphics, 0, 0, parent.getWidth(), parent.getWidth());
    assertNotDrawn(focusDrawable);
  }

  @Test
  public void shouldDelegateToDefaultBorderPainterIfBorderColorIsSpecified() throws Exception
  {
    MockPainter defaultBorderPainter = new MockPainter();
    BorderPainter.instance = defaultBorderPainter;
    parent.getStyle().setTopBorderColor("blue");

    painter.paint(graphics, parent);

    assertNotDrawn(normalDrawable);
    assertNotDrawn(focusDrawable);
    assertEquals(true, defaultBorderPainter.painted);
  }

}
