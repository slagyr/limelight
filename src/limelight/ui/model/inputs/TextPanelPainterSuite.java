package limelight.ui.model.inputs;

import limelight.ui.MockGraphics;
import limelight.ui.MockTextLayout;
import limelight.ui.TypedLayout;
import limelight.ui.model.TextPanel;
import limelight.ui.model.inputs.painters.TextPanelBackgroundPainter;
import limelight.ui.model.inputs.painters.TextPanelCursorPainter;
import limelight.ui.model.inputs.painters.TextPanelSelectionPainter;
import limelight.ui.model.inputs.painters.TextPanelTextPainter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Suite.class)
@Suite.SuiteClasses({TextPanelPainterSuite.CursorPainter.class, TextPanelPainterSuite.BoxPainter.class, TextPanelPainterSuite.SelectionPainter.class,
    TextPanelPainterSuite.TextPainter.class})
public class TextPanelPainterSuite
{
  static TextPanelPainter painter;
  static TextModel boxInfo;
  static TextInputPanel panel;
  static MockGraphics graphics;
  static Rectangle testBox;

  protected static void testClassInit()
  {
    panel = new TextBox2Panel();
    boxInfo = panel.getModelInfo();
    boxInfo.setText("Some Text");
    boxInfo.setCursorIndex(4);
    graphics = new MockGraphics();
    panel.cursorOn = true;
  }

  protected static void assertTestBoxSize(int x1, int y1, int x2, int y2)
  {
    assertEquals(x1, testBox.x);
    assertEquals(y1, testBox.y);
    assertEquals(x2, testBox.width);
    assertEquals(y2, testBox.height);
  }


  public static class CursorPainter
  {
    @Before
    public void setUp()
    {
      testClassInit();
      painter = new TextPanelCursorPainter(boxInfo);
    }

    @Test
    public void willNotPaintIfTheCursorIsOff()
    {
      panel.cursorOn = false;

      painter.paint(graphics);

      assertTrue(graphics.drawnLines.isEmpty());
    }

    @Test
    public void willPaintIfTheCursorIsOn()
    {
      painter.paint(graphics);

      assertFalse(graphics.drawnLines.isEmpty());
    }

    @Test
    public void willPaintTheCursorAtTheCursorX()
    {
      painter.paint(graphics);

      int expectedX = boxInfo.getXPosFromIndex(boxInfo.getCursorIndex());
      assertEquals(expectedX, graphics.drawnLines.get(0).x1);
      assertEquals(expectedX, graphics.drawnLines.get(0).x2);
    }

    @Test
    public void willPaintTheCursorBlack()
    {
      painter.paint(graphics);

      assertEquals(Color.black, graphics.getColor());
    }
  }

  public static class BoxPainter
  {

    @Before
    public void setUp()
    {
      testClassInit();
      painter = new TextPanelBackgroundPainter(boxInfo);
    }

    @Test
    public void willFillARectangleAccordingToThePaintableRegionBounds()
    {
      panel.resetPaintableRegion();
      panel.setPaintableRegion(4);
      panel.setPaintableRegion(0);

      painter.paint(graphics);

      testBox = graphics.filledShapes.get(0).shape.getBounds();
      assertTestBoxSize(3, TextModel.TOP_MARGIN, boxInfo.getXPosFromIndex(4) - TextModel.SIDE_TEXT_MARGIN, boxInfo.getPanelHeight() - TextModel.TOP_MARGIN *2);
    }

    @Test
    public void willFillTheRectangleWithTheCorrectColor()
    {
      painter.paint(graphics);

      assertEquals(Color.white, graphics.filledShapes.get(0).color);
    }

    @Test
    public void willOutLineTheBox()
    {
      painter.paint(graphics);

      testBox = graphics.drawnShapes.get(0).shape.getBounds();
      assertTestBoxSize(0, 0, boxInfo.getPanelWidth() - 1, boxInfo.getPanelHeight() - 1);
    }

    @Test
    public void willOutLineGreenIfFocused()
    {
      boxInfo.myPanel.focused = true;

      painter.paint(graphics);

      assertEquals(Color.green, graphics.drawnShapes.get(0).color);
    }

    @Test
    public void willOutLineGrayIfNotFocused()
    {
      boxInfo.myPanel.focused = false;

      painter.paint(graphics);

      assertEquals(Color.gray, graphics.drawnShapes.get(0).color);
    }
  }

  public static class SelectionPainter
  {

    @Before
    public void setUp()
    {
      testClassInit();
      painter = new TextPanelSelectionPainter(boxInfo);
      boxInfo.selectionOn = true;
      boxInfo.setSelectionIndex(6);
    }

    @Test
    public void willNotPaintIfSelectionIsOff()
    {
      boxInfo.selectionOn = false;

      painter.paint(graphics);

      assertTrue(graphics.filledShapes.isEmpty());
    }

    @Test
    public void willNotPaintIfThereIsNoText()
    {
      boxInfo.setText(null);

      painter.paint(graphics);

      assertTrue(graphics.filledShapes.isEmpty());
    }

    @Test
    public void willFillABoxAroundSelectedText()
    {
      boxInfo.setCursorAndSelectionStartX();
      int start = boxInfo.cursorX;
      int width = boxInfo.selectionStartX - start;

      painter.paint(graphics);

      testBox = graphics.filledShapes.getLast().shape.getBounds();
      assertTestBoxSize(start, TextPanelPainter.HEIGHT_MARGIN, width, boxInfo.getPanelHeight() - TextPanelPainter.HEIGHT_MARGIN * 2);
    }

    @Test
    public void willFillTheBoxCyan()
    {
      painter.paint(graphics);

      assertEquals(Color.cyan, graphics.filledShapes.getLast().color);
    }

    @Test
    public void willFillMultipleBoxesForMultiLinedSelectedText()
    {
      panel = new TextArea2Panel();
      boxInfo = panel.getModelInfo();
      boxInfo.setText("This is some\nMulti lined text");
      boxInfo.selectionOn = true;
      boxInfo.setSelectionIndex(2);
      boxInfo.setCursorIndex(18);
      boxInfo.getTextLayouts();
      painter.boxInfo = boxInfo;
      painter.paint(graphics);

      testBox = graphics.filledShapes.get(0).shape.getBounds();
      int startX = boxInfo.getXPosFromIndex(boxInfo.selectionIndex);
      assertTestBoxSize(startX,0,panel.getWidth() - startX, boxInfo.getTotalHeightOfLineWithLeadingMargin(0));
      
      int endX = boxInfo.getXPosFromIndex(boxInfo.cursorIndex) -3;
      testBox = graphics.filledShapes.get(1).shape.getBounds();
      assertTestBoxSize(3,boxInfo.getTotalHeightOfLineWithLeadingMargin(0),endX,boxInfo.getTotalHeightOfLineWithLeadingMargin(1));
    }
  }

  public static class TextPainter
  {
    TypedLayout layout;

    @Before
    public void setUp()
    {
      testClassInit();
      painter = new TextPanelTextPainter(boxInfo);
      layout = new MockTextLayout(boxInfo.getText(), boxInfo.font, TextPanel.getRenderContext());
      boxInfo.textLayouts = new ArrayList<TypedLayout>();
      boxInfo.textLayouts.add(layout);
      boxInfo.setLastLayedOutText("Some Text");      
    }

    @Test
    public void willNotPaintTextIfTextIsNull()
    {
      boxInfo.setText(null);

      painter.paint(graphics);

      assertFalse(layout.hasDrawn());
    }

    @Test
    public void willDrawTextToLayout()
    {
      painter.paint(graphics);

      assertTrue(layout.hasDrawn());
      assertEquals(boxInfo.getText(), layout.toString());
    }

    @Test
    public void willDrawTextToCorrectLocation()
    {
      painter.paint(graphics);

      assertEquals(3, (int)layout.getBounds().getX());
      assertEquals(13, (int)layout.getBounds().getY());
    }

    @Test
    public void willDrawTextWithTheRightColor()
    {
      painter.paint(graphics);

      assertEquals(Color.black, graphics.color);
    }
  }
}
