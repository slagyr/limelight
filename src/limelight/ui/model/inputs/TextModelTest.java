package limelight.ui.model.inputs;

import limelight.ui.TextLayoutImpl;
import limelight.ui.TypedLayout;
import limelight.ui.model.TextPanel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
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
    boxModel = panel.getBoxInfo();
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
  public void canConcatTheLayoutsToGetTheTotalText()
  {
    boxModel.textLayouts = new ArrayList<TypedLayout>();
    boxModel.textLayouts.add(new TextLayoutImpl("This is the first line ", boxModel.font, TextPanel.getRenderContext()));
    boxModel.textLayouts.add(new TextLayoutImpl("This is the second line", boxModel.font, TextPanel.getRenderContext()));

    String concatenatedText = boxModel.concatenateAllLayoutText();

    assertEquals("This is the first line This is the second line",concatenatedText);
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
  public void shouldBeAbleToFindNewLineIndices()
  {
    ArrayList<Integer> indices = boxModel.findNewLineIndices("this\nIs\nA new \rline");

    assertEquals(3, indices.size());
    assertEquals(4, (int)indices.get(0));
    assertEquals(7, (int)indices.get(1));
    assertEquals(14, (int)indices.get(2));
  }

  @Test
  public void canSpiltTextIntoMultipleLinesBasedOffReturnKeys()
  {
    ArrayList<TypedLayout> textLayouts = boxModel.parseTextForMultipleLayouts("this is the first line\nthis is the second line");

    assertEquals(2, textLayouts.size());
    assertEquals("this is the first line\n", textLayouts.get(0).getText());
    assertEquals("this is the second line", textLayouts.get(1).getText());
  }

  @Test
  public void canSplitTextIntoMultipleLinesFromThePanelWidth()
  {
   ArrayList<TypedLayout> textLayouts = boxModel.parseTextForMultipleLayouts("This here is the first full line. This is the second line");

    assertEquals(2, textLayouts.size());
    assertEquals("This here is the first full line. ", textLayouts.get(0).getText());
    assertEquals("This is the second line", textLayouts.get(1).getText());
  }

  @Test
  public void willStoreATextLayoutForEachLine()
  {
    boxModel.setText("This text is more than 1 line\rand should be 2 lines");

    ArrayList<TypedLayout> textLayouts = boxModel.getTextLayouts();
    assertEquals(2, textLayouts.size());
    assertEquals("This text is more than 1 line\r", textLayouts.get(0).getText());
    assertEquals("and should be 2 lines", textLayouts.get(1).getText());
  }

}
