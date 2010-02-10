package limelight.ui.model.inputs;

import limelight.ui.TextLayoutImpl;
import limelight.ui.TypedLayout;
import limelight.ui.model.TextPanel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TextAreaModelTest
{
  TextModel modelInfo;
  TextAreaModel areaInfo;
  TextInputPanel panel;

  @Before
  public void setUp()
  {
    panel = new TextArea2Panel();
    modelInfo = panel.getModelInfo();
    areaInfo = new TextAreaModel(panel);
    modelInfo.setText("I took the one less traveled by. And that has made all the difference");
  }

  @Test
  public void canCalculateTheXPositionForTheCursorFromAString()
  {
    int width = modelInfo.getWidthDimension(new TextLayoutImpl("ABC", modelInfo.font, TextPanel.getRenderContext()));
    int expectedX = width + modelInfo.SIDE_TEXT_MARGIN;

    int x = modelInfo.getXPosFromText("ABC");

    assertEquals(expectedX, x);
  }

  @Test
  public void canCalculateTheXPositionFromTheCursorIndex()
  {
    int width2 = modelInfo.getWidthDimension(new TextLayoutImpl("by. And that has made", modelInfo.font, TextPanel.getRenderContext()));

    int firstIndex = modelInfo.getXPosFromIndex(0);
    int newLinePosition = modelInfo.getXPosFromIndex(29);
    int secondLineStringPosition = modelInfo.getXPosFromIndex(50);

    assertEquals(modelInfo.SIDE_TEXT_MARGIN, firstIndex);
    assertEquals(modelInfo.SIDE_TEXT_MARGIN, newLinePosition);
    assertEquals(width2 + modelInfo.SIDE_TEXT_MARGIN, secondLineStringPosition);
  }

  @Test
  public void canCalculateTheYPositionForTheCursorViaAnIndex()
  {
    int expectedYForOneLine = TextModel.TOP_MARGIN;
    int expectedYForTwoLines = 18;
    int expectedYForThreeLines = expectedYForTwoLines * 2 - TextModel.TOP_MARGIN;

    int y1 = modelInfo.getYPosFromIndex(0);
    int y2 = modelInfo.getYPosFromIndex(50);
    int y3 = modelInfo.getYPosFromIndex(65);

    assertEquals(expectedYForOneLine, y1);
    assertEquals(expectedYForTwoLines, y2);
    assertEquals(expectedYForThreeLines, y3);
  }

  @Test
  public void willAddAnotherLineToTheYPositionIfTheLastCharacterBeforeCursorIsAReturn()
  {
    int expectedYForTwoLines = 18;
    int expectedYForThreeLines = 32;
    modelInfo.setText("some text\nmore text\n");

    int y = modelInfo.getYPosFromIndex(10);
    int y2 = modelInfo.getYPosFromIndex(20);

    assertEquals(expectedYForTwoLines, y);
    assertEquals(expectedYForThreeLines,y2 );
  }

  @Test
  public void willPutTheCursorOnTheLeftIfTheLastCharacterBeforeCursorIsAReturn()
  {
    int expectedX = TextModel.SIDE_TEXT_MARGIN;
    modelInfo.setText("some text\n");

    int x = modelInfo.getXPosFromIndex(modelInfo.cursorIndex);

    assertEquals(expectedX, x);
  }

  @Test
  public void canGetTheHeightOfTheCurrentLine()
  {
    int expectedHeight = (int) (modelInfo.getHeightDimension(new TextLayoutImpl("A", modelInfo.font, TextPanel.getRenderContext())) + .5);
    modelInfo.setCursorIndex(50);

    int height = modelInfo.getHeightOfCurrentLine();

    assertEquals(expectedHeight, height);
  }

  @Test
  public void shouldBeAbleToFindNewLineIndices()
  {
    ArrayList<Integer> indices = areaInfo.findReturnCharIndices("this\nIs\nA new \rline");

    assertEquals(3, indices.size());
    assertEquals(4, (int) indices.get(0));
    assertEquals(7, (int) indices.get(1));
    assertEquals(14, (int) indices.get(2));
  }

  @Test
  public void canSpiltTextIntoMultipleLinesBasedOffReturnKeys()
  {
    ArrayList<TypedLayout> textLayouts = areaInfo.parseTextForMultipleLayouts("this is the first line\nthis is the second line");

    assertEquals(2, textLayouts.size());
    assertEquals("this is the first line\n", textLayouts.get(0).getText());
    assertEquals("this is the second line", textLayouts.get(1).getText());
  }

  @Test
  public void canSplitTextIntoMultipleLinesFromThePanelWidth()
  {
    ArrayList<TypedLayout> textLayouts = areaInfo.parseTextForMultipleLayouts("This here is the first full line. This is the second line");

    assertEquals(3, textLayouts.size());
    assertEquals("This here is the first full ", textLayouts.get(0).getText());
    assertEquals("line. This is the second ", textLayouts.get(1).getText());
  }

  @Test
  public void willStoreATextLayoutForEachLine()
  {
    areaInfo.setText("This is more than 1 line\rand should be 2 lines");

    ArrayList<TypedLayout> textLayouts = areaInfo.getTextLayouts();
    assertEquals(2, textLayouts.size());
    assertEquals("This is more than 1 line\r", textLayouts.get(0).getText());
    assertEquals("and should be 2 lines", textLayouts.get(1).getText());
  }

  @Test
  public void willMakeANewLineForEveryReturnCharacterRegardlessOfTheLineItIsCurrentlyOn()
  {
    areaInfo.setText("This is going to be a very large amount of text. It seems to be the only way to make sure this works. Here\nis\n\nsome new lines");

    areaInfo.getTextLayouts();

    assertEquals(8, areaInfo.textLayouts.size());
  }

  @Test
  public void canCalculateTheTextModelsDimensions()
  {
    modelInfo.setText("line 1\nline 2");
    Dimension dim = modelInfo.calculateTextDimensions();
    assertEquals(29, dim.width);
    assertEquals(28, dim.height);
  }

  @Test
  public void canTellIfTheTextPanelIsFull()
  {
    modelInfo.setText("line\nline\nline\nline\nline\nline\nline\nline\n");
    assertTrue(modelInfo.isBoxFull());
  }

  @Test
  public void canGetASelectedRegion()
  {
    modelInfo.setSelectionIndex(0);
    modelInfo.selectionOn = true;
    modelInfo.setCursorIndex(5);

    ArrayList<Rectangle> regions = modelInfo.getSelectionRegions();

    assertEquals(3, regions.get(0).x);
    assertEquals(0, regions.get(0).y);
    assertEquals(modelInfo.getXPosFromIndex(modelInfo.getCursorIndex()) -3, regions.get(0).width);
    assertEquals(modelInfo.getTotalHeightOfLineWithLeadingMargin(modelInfo.getLineNumberOfIndex(5)), regions.get(0).height);
  }

  @Test
  public void canGetAMultiLinedSelectedRegion()
  {
    modelInfo.setSelectionIndex(2);
    modelInfo.selectionOn = true;
    modelInfo.setText("This is\nMulti Lined.");
    modelInfo.setCursorIndex(10);

    ArrayList<Rectangle> regions = modelInfo.getSelectionRegions();

    assertEquals(2, regions.size());
    assertEquals(modelInfo.getXPosFromIndex(2), regions.get(0).x);
    assertEquals(0, regions.get(0).y);
    assertEquals(panel.getWidth() - modelInfo.getXPosFromIndex(2), regions.get(0).width);
    assertEquals(modelInfo.getTotalHeightOfLineWithLeadingMargin(modelInfo.getLineNumberOfIndex(2)), regions.get(0).height);

    assertEquals(3, regions.get(1).x);
    assertEquals(modelInfo.getTotalHeightOfLineWithLeadingMargin(modelInfo.getLineNumberOfIndex(2)), regions.get(1).y);
    assertEquals(modelInfo.getXPosFromIndex(10) -3, regions.get(1).width);
    assertEquals(modelInfo.getTotalHeightOfLineWithLeadingMargin(modelInfo.getLineNumberOfIndex(10)), regions.get(1).height);
  }

  @Test
  public void canGetTheLastCharacterInALine()
  {
    modelInfo.setText("This is\nMulti Lined.\nWith Many Lines");

    assertEquals(20, modelInfo.getIndexOfLastCharInLine(1));
  }

  @Test
  public void canGetTheLastCharacterOfTheText()
  {
    modelInfo.setText("This is\nMulti Lined.\nWith Many Lines");

    assertEquals(36, modelInfo.getIndexOfLastCharInLine(2));
  }

}
