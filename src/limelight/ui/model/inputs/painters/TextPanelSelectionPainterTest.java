package limelight.ui.model.inputs.painters;

import limelight.ui.model.inputs.TextModel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

public class TextPanelSelectionPainterTest extends AbstractTextPanelPainterTest
{
  @Before
  public void setUp()
  {
    testClassInit();
    painter = TextPanelSelectionPainter.instance;
    boxInfo.setSelectionOn(true);
    boxInfo.setSelectionIndex(6);
  }

  @Test
  public void willNotPaintIfSelectionIsOff()
  {
    boxInfo.setSelectionOn(false);

    painter.paint(graphics, boxInfo);

    assertEquals(true, graphics.filledShapes.isEmpty());
  }

  @Test
  public void willNotPaintIfThereIsNoText()
  {
    boxInfo.setText(null);

    painter.paint(graphics, boxInfo);

    assertEquals(true, graphics.filledShapes.isEmpty());
  }

  @Test
  public void willFillABoxAroundSelectedText()
  {
    boxInfo.setCursorAndSelectionStartX();
    int start = boxInfo.getCursorX();
    int width = boxInfo.getSelectionStartX() - start;

    painter.paint(graphics, boxInfo);

    testBox = graphics.filledShapes.getLast().shape.getBounds();
    assertTestBoxSize(start, TextModel.TOP_MARGIN, width, boxInfo.getPanelHeight() - TextModel.TOP_MARGIN * 2);
  }
  
  protected void assertTestBoxSize(int x1, int y1, int x2, int y2)
  {
    assertEquals(x1, testBox.x);
    assertEquals(y1, testBox.y);
    assertEquals(x2, testBox.width);
    assertEquals(y2, testBox.height);
  }

  @Test
  public void willFillTheBoxCyan()
  {
    painter.paint(graphics, boxInfo);

    assertEquals(Color.cyan, graphics.filledShapes.getLast().color);
  }

//    @Test
//    public void willFillMultipleBoxesForMultiLinedSelectedText()
//    {
//      panel = new TextArea2Panel();
//      MockPanel parent = new MockPanel();
//      parent.add(panel);
//      parent.setSize(150, 28);
//      panel.doLayout();
//      boxInfo = panel.getModelInfo();
//      boxInfo.setText("This is some\nMulti lined text");
//      boxInfo.setSelectionOn(true);
//      boxInfo.setSelectionIndex(2);
//      boxInfo.setCursorIndex(18);
//      boxInfo.getTextLayouts();
//      painter.boxInfo = boxInfo;
//      painter.paint(graphics);
//
//      testBox = graphics.filledShapes.get(0).shape.getBounds();
//      int startX = boxInfo.getXPosFromIndex(boxInfo.selectionIndex);
//      assertTestBoxSize(startX, TextModel.TOP_MARGIN, panel.getWidth() - startX - TextModel.SIDE_TEXT_MARGIN, boxInfo.getTotalHeightOfLineWithLeadingMargin(0));
//
//      int endX = boxInfo.getXPosFromIndex(boxInfo.cursorIndex) - TextModel.SIDE_TEXT_MARGIN;
//      testBox = graphics.filledShapes.get(1).shape.getBounds();
//      assertTestBoxSize(TextModel.SIDE_TEXT_MARGIN, boxInfo.getTotalHeightOfLineWithLeadingMargin(0) + TextModel.TOP_MARGIN, endX, boxInfo.getTotalHeightOfLineWithLeadingMargin(1));
//    }
}
