package limelight.ui.model.inputs.painters;

import limelight.ui.MockTextLayout;
import limelight.ui.TypedLayout;
import limelight.ui.model.TextPanel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

public class TextPanelTextPainterTest extends AbstractTextPanelPainterTest
{
  private TypedLayout layout;

  @Before
  public void setUp()
  {
    testClassInit();
    painter = TextPanelTextPainter.instance;
    layout = new MockTextLayout(boxInfo.getText(), boxInfo.getFont(), TextPanel.getRenderContext());
    boxInfo.getTextLayouts().add(layout);
    boxInfo.setLastLayedOutText("Some Text");
  }

  @Test
  public void willNotPaintTextIfTextIsNull()
  {
    boxInfo.setText(null);

    painter.paint(graphics, boxInfo);

    assertEquals(false, layout.hasDrawn());
  }

  @Test
  public void willDrawTextToLayout()
  {
    painter.paint(graphics, boxInfo);

    assertEquals(true, layout.hasDrawn());
    assertEquals(boxInfo.getText(), layout.toString());
  }

//  @Test
//  public void willDrawTextToCorrectLocation()
//  {
//    painter.paint(graphics, boxInfo);
//
//    assertEquals(TextModel.SIDE_TEXT_MARGIN, (int) layout.getBounds().getX());
//    assertEquals(15, (int) layout.getBounds().getY());
//  }

  @Test
  public void willDrawTextWithTheRightColor()
  {
    painter.paint(graphics, boxInfo);

    assertEquals(Color.black, graphics.color);
  }
}
