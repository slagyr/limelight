package limelight.ui.painting;

import limelight.ui.MockGraphics;
import limelight.ui.model.MockPropablePanel;
import org.junit.Assert;
import org.junit.Test;

public class DefaultPainterTest extends Assert
{
  @Test
  public void shouldPaint() throws Exception
  {
    MockPainter mockBackgroundPainter = new MockPainter();
    BackgroundPainter.instance = mockBackgroundPainter;
    MockPainter mockBorderPainter = new MockPainter();
    BorderPainter.instance = mockBorderPainter;
    MockPropablePanel panel = new MockPropablePanel();
    MockGraphics graphics = new MockGraphics();

    DefaultPainter.instance.paint(graphics, panel);

    assertEquals(true, mockBackgroundPainter.painted);
    assertEquals(true, mockBorderPainter.painted);
  }
}
