package limelight.ui.model.inputs;

import limelight.ui.TextLayoutImpl;
import limelight.ui.model.TextPanel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TextModelTest
{
  TextModel boxModel;
  TextBox2Panel boxPanel;

  @Before
  public void setUp()
  {
    boxPanel = new TextBox2Panel();
    boxModel = boxPanel.getBoxInfo();
    boxModel.setText("Bob Dole likes to hear Bob Dole say 'Bob Dole'  ");
  }

  @Test
  public void canCalculateTheTerminatingSpaceWidth()
  {
    int width = boxModel.getTerminatingSpaceWidth(boxModel.getText());
    int width2 = boxModel.getTerminatingSpaceWidth("No Terminating Space");

    assertEquals(6, width);
    assertEquals(0, width2);
  }

  @Test
  public void canCalculateTheXPositionForTheCursorFromAString()
  {
    int width = boxModel.getWidthDimension(new TextLayoutImpl("ABC", boxModel.font, TextPanel.getRenderContext()));
    int expectedX = width + boxModel.LEFT_TEXT_MARGIN;

    int x = boxModel.getXPosFromTextLayout("ABC");

    assertEquals(expectedX, x);
  }

  @Test
  public void canCalcTheXPosForCursorFromStringWithOffset()
  {
    int width = boxModel.getWidthDimension(new TextLayoutImpl("ABC", boxModel.font, TextPanel.getRenderContext()));
    int expectedX = width + boxModel.LEFT_TEXT_MARGIN;
    boxModel.xOffset = 10;

    int x = boxModel.getXPosFromTextLayout("ABC");

    assertEquals(expectedX - 10, x);
  }

  @Test
  public void canCalculateTheXPositionFromTheCursorIndex()
  {
    int width = boxModel.getWidthDimension(new TextLayoutImpl("Bob", boxModel.font, TextPanel.getRenderContext()));
    int width2 = boxModel.getWidthDimension(new TextLayoutImpl("Bob", boxModel.font, TextPanel.getRenderContext()));

    int x = boxModel.getXPosFromIndex(0);
    int x2 = boxModel.getXPosFromIndex(3);
    int x3 = boxModel.getXPosFromIndex(4);

    assertEquals(boxModel.LEFT_TEXT_MARGIN, x);
    assertEquals(width + boxModel.LEFT_TEXT_MARGIN, x2);
    assertEquals(width2 + boxModel.LEFT_TEXT_MARGIN + 3, x3);
  }

  @Test
  public void canCalculateTheTextModelsDimensions()
  {
    boxModel.text = new StringBuffer("");
    Dimension dim = boxModel.calculateTextDimensions();
    assertEquals(null, dim);

    boxModel.text = new StringBuffer("X");
    dim = boxModel.calculateTextDimensions();
    assertEquals(8, dim.width);
    assertEquals(14, dim.height);
  }

  @Test
  public void canShiftTheCursorAndTextRight()
  {
    boxModel.xOffset = boxModel.calculateTextDimensions().width - boxPanel.getWidth();
    int offset = boxModel.xOffset;
    boxModel.setCursorIndex(boxModel.getText().indexOf("ar"));

    boxModel.shiftTextRight();
    assertTrue(boxModel.xOffset <= offset/ 2 + 5 && boxModel.xOffset != 0);
  }                                    

  @Test
  public void canCalculateTheXOffsetIfTheCursorIsAtTheRightEdge()
  {
    boxModel.calculateTextXOffset(boxPanel.getWidth(), boxModel.calculateTextDimensions().width);

    assertTrue(boxModel.xOffset > 0);
  }

  @Test
  public void canCutTheXOffsetInHalfWhenTheCursorIsOnTheLeftEdge()
  {
    boxModel.calculateTextXOffset(boxPanel.getWidth(), boxModel.calculateTextDimensions().width);
    int offset = boxModel.xOffset;
    boxModel.setCursorIndex(0);

    boxModel.calculateTextXOffset(boxPanel.getWidth(), boxModel.calculateTextDimensions().width);

    assertTrue(boxModel.xOffset <= offset / 2 + 2);

    boxModel.text = new StringBuffer("hi");
    boxModel.calculateTextXOffset(boxPanel.getWidth(), boxModel.calculateTextDimensions().width);
    assertTrue(boxModel.xOffset == 0);

  }

  @Test
  public void canMakeUseOfTheClipboard()
  {
    boxModel.copyText("This Text");
    String clipboard = boxModel.getClipboardContents();
    assertEquals("This Text", clipboard);
  }

  @Test
  public void canCopyPasteAndCut()
  {
    boxModel.setText("Some Text");
    boxModel.setCursorIndex(0);
    boxModel.setSelectionIndex(4);
    boxModel.selectionOn = true;
    boxModel.copySelection();
    String clipboard = boxModel.getClipboardContents();
    assertEquals("Some", clipboard);

    boxModel.pasteClipboard();
    assertEquals("SomeSome Text", boxModel.getText());

    boxModel.setCursorIndex(0);
    boxModel.setSelectionIndex(8);
    boxModel.cutSelection();
    assertEquals("SomeSome", boxModel.getClipboardContents());
    assertEquals(" Text", boxModel.getText());

  }
  
  @Test
  public void canGetTheSelectedRegion()
  {
    boxModel.setSelectionIndex(0);
    boxModel.selectionOn = true;
    
    Rectangle region = boxModel.getSelectionRegion();

    assertEquals(TextModel.LEFT_TEXT_MARGIN,region.x );
    assertEquals(TextModel.TOP_MARGIN, region.y);
    assertEquals(boxModel.getXPosFromIndex(boxModel.getCursorIndex()) - TextModel.LEFT_TEXT_MARGIN, region.width);
    assertEquals(boxModel.getPanelHeight() - TextModel.TOP_MARGIN * 2, region.height);
  }

}
