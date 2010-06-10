//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.MockPanel;
import limelight.ui.TextLayoutImpl;
import limelight.ui.model.TextPanel;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class TextModelTest
{
  TextModel model;
  TextInputPanel panel;

  @Before
  public void setUp()
  {
    panel = new TextBox2Panel();
    MockPanel parent = new MockPanel();
    parent.setSize(150, 28);
    parent.add(panel);
    panel.doLayout();
    model = panel.getModelInfo();
    model.setText("Bob Dole likes to hear Bob Dole say 'Bob Dole'  ");
  }

  @Test
  public void canSetACopyOfTheLastLayedoutText()
  {
    model.setLastLayedOutText("text");

    assertEquals("text", model.getLastLayedOutText());
  }

  @Test
  public void canCompareCurrentTextToLastLayedOutText()
  {
    model.setText("some text");
    model.setLastLayedOutText("some other text");

    assertEquals(true, model.isThereSomeDifferentText());

    model.setLastLayedOutText("some text");

    assertEquals(false, model.isThereSomeDifferentText());
  }

  @Test
  public void willSetLastLayedOutTextWhenGettingLayout()
  {
    model.setLastLayedOutText("nothing");
    model.setText("something");

    model.getTextLayouts();

    assertEquals("something", model.getLastLayedOutText());
  }

  @Test
  public void canCalculateTheTerminatingSpaceWidth()
  {
    int width = model.getTerminatingSpaceWidth(model.getText());
    int width2 = model.getTerminatingSpaceWidth("No Terminating Space");

    assertEquals(6, width);
    assertEquals(0, width2);
  }

  @Test
  public void canCalculateTheXPositionFromTheCursorIndex()
  {
    assertEquals(0, model.getXPosFromIndex(0));
    assertEquals(widthOf("B"), model.getXPosFromIndex(1));
    assertEquals(widthOf("Bo"), model.getXPosFromIndex(2));
    assertEquals(widthOf("Bob"), model.getXPosFromIndex(3));
    assertEquals(widthOf("Bob") + model.spaceWidth(), model.getXPosFromIndex(4));
  }

  private int widthOf(String text)
  {
    return model.getWidthDimension(new TextLayoutImpl(text, model.getFont(), TextPanel.getRenderContext()));
  }

  @Test
  public void canCalculateTheYPositionFromAnIndex()
  {
    assertEquals(0, model.getYPosFromIndex(0));
  }

  @Test
  public void canGetTheCurrentLine()
  {
    model.setCursorIndex(0);
    int expectedLine = 0;

    int line = model.getLineNumberOfIndex(model.getCursorIndex());

    assertEquals(expectedLine, line);
  }

  @Test
  public void canGetTheHeightForTheCurrentLine()
  {
    int expectedHeight = (int) (model.getHeightDimension(model.getTextLayouts().get(0)) + .5);

    int currentLineHeight = model.getHeightOfCurrentLine();

    assertEquals(expectedHeight, currentLineHeight);
  }

  @Test
  public void canMakeUseOfTheClipboard()
  {
    model.copyText("This Text");
    String clipboard = model.getClipboardContents();
    assertEquals("This Text", clipboard);
  }

  @Test
  public void canCopyPasteAndCut()
  {
    model.setText("Some Text");
    model.setCursorIndex(0);
    model.setSelectionIndex(4);
    model.setSelectionOn(true);
    model.copySelection();
    String clipboard = model.getClipboardContents();
    assertEquals("Some", clipboard);

    model.pasteClipboard();
    assertEquals("SomeSome Text", model.getText());

    model.setCursorIndex(0);
    model.setSelectionIndex(8);
    model.cutSelection();
    assertEquals("SomeSome", model.getClipboardContents());
    assertEquals(" Text", model.getText());
  }

  @Test
  public void willRememberTheLastCursorIndex()
  {
    model.setCursorIndex(3);
    model.setCursorIndex(5);

    assertEquals(3, model.getLastCursorIndex());
  }

  @Test
  public void canGetTheLastCharacterInALine()
  {
    assertEquals(model.getText().length(), model.getIndexOfLastCharInLine(0));
  }

}
