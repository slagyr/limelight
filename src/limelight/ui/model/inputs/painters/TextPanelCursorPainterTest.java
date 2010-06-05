package limelight.ui.model.inputs.painters;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

public class TextPanelCursorPainterTest extends AbstractTextPanelPainterTest
{
  @Before
  public void setUp()
  {
    testClassInit();
    painter = TextPanelCursorPainter.instance;
  }

  @Test
  public void willNotPaintIfTheCursorIsOff()
  {
    panel.setCursorOn(false);

    painter.paint(graphics, boxInfo);

    assertEquals(true, graphics.drawnLines.isEmpty());
  }

  @Test
  public void willPaintIfTheCursorIsOn()
  {
    painter.paint(graphics, boxInfo);

    assertEquals(false, graphics.drawnLines.isEmpty());
  }

  @Test
  public void willPaintTheCursorAtTheCursorX()
  {
    painter.paint(graphics, boxInfo);

    int expectedX = boxInfo.getXPosFromIndex(boxInfo.getCursorIndex());
    assertEquals(expectedX, graphics.drawnLines.get(0).x1);
    assertEquals(expectedX, graphics.drawnLines.get(0).x2);
  }

  @Test
  public void willPaintTheCursorBlack()
  {
    painter.paint(graphics, boxInfo);

    assertEquals(Color.black, graphics.getColor());
  }
}
