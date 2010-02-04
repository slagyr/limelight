package limelight.ui.model.inputs;

import limelight.ui.TextLayoutImpl;
import limelight.ui.TypedLayout;
import limelight.ui.model.TextPanel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TextModelTest
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
  public void canSetACopyOfTheLastLayedoutText()
  {
    boxModel.setLastLayedOutText("text");

    assertEquals("text", boxModel.getLastLayedOutText());
  }

  @Test
  public void canCompareCurrentTextToLastLayedOutText()
  {
    boxModel.setText("some text");
    boxModel.setLastLayedOutText("some other text");

    assertTrue(boxModel.isThereSomeDifferentText());

    boxModel.setLastLayedOutText("some text");

    assertFalse(boxModel.isThereSomeDifferentText());
  }

  @Test
  public void willSetLastLayedOutTextWhenGettingLayout()
  {
    boxModel.setLastLayedOutText("nothing");
    boxModel.setText("something");

    boxModel.getTextLayouts();

    assertEquals("something", boxModel.getLastLayedOutText());
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
  public void canCalculateTheXPositionFromTheCursorIndex()
  {
    int width = boxModel.getWidthDimension(new TextLayoutImpl("Bob", boxModel.font, TextPanel.getRenderContext()));
    int width2 = boxModel.getWidthDimension(new TextLayoutImpl("Bob", boxModel.font, TextPanel.getRenderContext()));

    int x = boxModel.getXPosFromIndex(0);
    int x2 = boxModel.getXPosFromIndex(3);
    int x3 = boxModel.getXPosFromIndex(4);

    assertEquals(boxModel.SIDE_TEXT_MARGIN, x);
    assertEquals(width + boxModel.SIDE_TEXT_MARGIN, x2);
    assertEquals(width2 + boxModel.SIDE_TEXT_MARGIN + 3, x3);
  }

  @Test
  public void canCalculateTheYPositionFromAnIndex()
  {
    int expectedY = TextModel.TOP_MARGIN;

    assertEquals(expectedY, boxModel.getYPosFromIndex(0));
  }

  @Test
  public void canGetTheCurrentLine()
  {
    boxModel.setCursorIndex(0);
    int expectedLine = 0;

    int line = boxModel.getCurrentLineNumber();

    assertEquals(expectedLine, line);
  }

  @Test
  public void canGetTheHeightForTheCurrentLine()
  {
    int expectedHeight = (int) (boxModel.getHeightDimension(boxModel.getTextLayouts().get(0)) + .5);

    int currentLineHeight = boxModel.getHeightOfCurrentLine();

    assertEquals(expectedHeight, currentLineHeight);
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

}
