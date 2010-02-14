package limelight.ui.model.inputs;

import limelight.ui.TextLayoutImpl;
import limelight.ui.model.TextPanel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TextBoxModelTest
{
  TextModel boxModel;
  TextInputPanel panel;

  @Before
  public void setUp()
  {
    panel = new TextBox2Panel();
    boxModel = panel.getModelInfo();
    boxModel.setText("Bob Dole likes to hear Bob Dole say 'Bob Dole'  ");
  }

  @Test
  public void canCalcTheXPosForCursorFromStringWithOffset()
  {
    int width = boxModel.getWidthDimension(new TextLayoutImpl("ABC", boxModel.font, TextPanel.getRenderContext()));
    int expectedX = width + TextModel.SIDE_TEXT_MARGIN;
    boxModel.xOffset = 10;

    int x = boxModel.getXPosFromText("ABC");

    assertEquals(expectedX - 10, x);
  }

  @Test
  public void canCalculateTheXPositionForTheCursorFromAString()
  {
    int width = boxModel.getWidthDimension(new TextLayoutImpl("ABC", boxModel.font, TextPanel.getRenderContext()));
    int expectedX = width + TextModel.SIDE_TEXT_MARGIN;

    int x = boxModel.getXPosFromText("ABC");

    assertEquals(expectedX, x);
  }

  @Test
  public void canTellIfTheTextPanelIsFull()
  {
    assertTrue(boxModel.isBoxFull());
  }

  @Test
  public void canTellIfTheCursorIsAtACriticalEdge()
  {
    boxModel.calculateTextXOffset(panel.getWidth(), boxModel.calculateTextDimensions().width);

    assertFalse(boxModel.isCursorAtCriticalEdge(boxModel.cursorIndex));

    boxModel.setCursorIndex(0);

    assertTrue(boxModel.isCursorAtCriticalEdge(boxModel.cursorIndex));

    boxModel.calculateTextXOffset(panel.getWidth(), boxModel.calculateTextDimensions().width);
    boxModel.setCursorIndex(boxModel.text.length() -5);

    assertTrue(boxModel.isCursorAtCriticalEdge(boxModel.cursorIndex));
  }

  @Test
  public void canShiftTheCursorAndTextRightAboutHalfTheDistanceOfTheBoxWidthIfCriticallyLeft()
  {
    boxModel.setCursorIndex(10);
    int offset = boxModel.calculateTextDimensions().width - panel.getWidth();
    boxModel.xOffset = offset;

    assertTrue(boxModel.isCursorAtCriticalEdge(boxModel.cursorIndex));

    boxModel.shiftOffset();
    assertTrue(boxModel.xOffset <= offset / 2 + 5 && boxModel.xOffset != 0);
  }

  @Test
  public void canShiftTheCursorAndTextLeftIfCriticallyRight()
  {
    boxModel.setCursorIndex(0);
    boxModel.xOffset = 0;

    boxModel.setCursorIndex(30);

    assertTrue(boxModel.isCursorAtCriticalEdge(boxModel.cursorIndex));

    boxModel.shiftOffset();

    assertTrue(boxModel.xOffset > 0);

  }

  @Test
  public void canCutTheXOffsetInHalfWhenTheCursorIsOnTheLeftEdge()
  {
    boxModel.calculateTextXOffset(panel.getWidth(), boxModel.calculateTextDimensions().width);
    int offset = boxModel.xOffset;
    boxModel.setCursorIndex(0);

    boxModel.calculateTextXOffset(panel.getWidth(), boxModel.calculateTextDimensions().width);

    assertTrue(boxModel.xOffset <= offset / 2 + 2);

    boxModel.setText("hi");
    boxModel.calculateTextXOffset(panel.getWidth(), boxModel.calculateTextDimensions().width);
    assertTrue(boxModel.xOffset == 0);

  }

  @Test
  public void canCalculateTheTextModelsDimensions()
  {
    boxModel.setText("");
    Dimension dim = boxModel.calculateTextDimensions();
    assertEquals(null, dim);

    boxModel.setText("X");
    dim = boxModel.calculateTextDimensions();
    assertEquals(8, dim.width);
    assertEquals(14, dim.height);
  }


  @Test
  public void canGetTheSelectedRegion()
  {
    boxModel.setSelectionIndex(0);
    boxModel.selectionOn = true;

    Rectangle region = boxModel.getSelectionRegions().get(0);

    assertEquals(0, region.x);
    assertEquals(TextModel.TOP_MARGIN, region.y);
    assertEquals(boxModel.getXPosFromIndex(boxModel.getCursorIndex()), region.width);
    assertEquals(boxModel.getPanelHeight() - TextModel.TOP_MARGIN * 2, region.height);
  }


  @Test
  public void willRecalculateXOffsetIfTextIsFullWhenGettingSelection()
  {
    boxModel.setSelectionIndex(boxModel.getText().length());
    boxModel.setCursorIndex(10);
    boxModel.selectionOn = true;
    boxModel.xOffset = 100;

    boxModel.getSelectionRegions();

    assertTrue(boxModel.xOffset > 0 && boxModel.xOffset < 100);
  }

  @Test
  public void willAlwaysReturnZeroForTheLineNumber()
  {
    assertEquals(0, boxModel.getLineNumberOfIndex(boxModel.cursorIndex));
  }

}
