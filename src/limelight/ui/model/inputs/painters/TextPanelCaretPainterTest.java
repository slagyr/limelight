package limelight.ui.model.inputs.painters;

import limelight.ui.MockGraphics;
import limelight.ui.MockPanel;
import limelight.ui.model.inputs.MockTextModel;
import limelight.ui.model.inputs.TextBox2Panel;
import limelight.ui.model.inputs.TextPanelPainter;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

public class TextPanelCaretPainterTest
{
  private TextBox2Panel panel;
  private MockPanel parent;
  private MockTextModel boxInfo;
  private MockGraphics graphics;
  private TextPanelPainter painter;

  @Before
  public void setUp()
  {
    panel = new TextBox2Panel();
    parent = new MockPanel();
    parent.add(panel);
    parent.setSize(150, 28);
    panel.doLayout();
    boxInfo = new MockTextModel(panel);
    boxInfo.addLayout("Some Text");
    boxInfo.setCaretIndex(4);
    graphics = new MockGraphics();
    panel.setCursorOn(true);
    painter = TextPanelCaretPainter.instance;
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

    int expectedX = boxInfo.getXPosFromIndex(boxInfo.getCaretIndex());
    assertEquals(expectedX, graphics.drawnLines.get(0).x1);
    assertEquals(expectedX, graphics.drawnLines.get(0).x2);
  }

  @Test
  public void willPaintTheCursorBlack()
  {
    painter.paint(graphics, boxInfo);

    assertEquals(Color.black, graphics.getColor());
  }

  @Test
  public void shouldApplyOffsetsToCursor() throws Exception
  {
    boxInfo.setOffset(20, 30);

    painter.paint(graphics, boxInfo);

    MockGraphics.DrawnLine line = graphics.drawnLines.get(0);
    assertEquals(20, line.x1);
    assertEquals(30, line.y1);
  }
}
