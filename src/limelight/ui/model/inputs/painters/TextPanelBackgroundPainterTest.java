package limelight.ui.model.inputs.painters;

import limelight.ui.model.MockDrawable;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

public class TextPanelBackgroundPainterTest extends AbstractTextPanelPainterTest
{
  private MockDrawable normalDrawable;
  private MockDrawable focusDrawable;

  @Before
  public void setUp()
  {
    testClassInit();

    TextPanelBackgroundPainter.normalBorder = normalDrawable = new MockDrawable();
    TextPanelBackgroundPainter.focusedBorder = focusDrawable = new MockDrawable();

    painter = TextPanelBackgroundPainter.instance;
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
    boxInfo.getPanel().setFocused(true);

    painter.paint(graphics, boxInfo);

    assertDrawn(normalDrawable, graphics, 0, 0, boxInfo.getPanelWidth(), boxInfo.getPanelHeight());
    assertDrawn(focusDrawable, graphics, 0, 0, boxInfo.getPanelWidth(), boxInfo.getPanelHeight());
  }

  @Test
  public void willOnlyUseNormalBackgroundIfNotFocused()
  {
    boxInfo.getPanel().setFocused(false);

    painter.paint(graphics, boxInfo);

    assertDrawn(normalDrawable, graphics, 0, 0, boxInfo.getPanelWidth(), boxInfo.getPanelHeight());
    assertNotDrawn(focusDrawable);
  }

}
