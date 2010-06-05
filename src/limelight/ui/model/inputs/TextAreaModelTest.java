//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.MockPanel;
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
    MockPanel parent = new MockPanel();
    parent.add(panel);
    parent.setSize(150, 75);
    panel.doLayout();
    modelInfo = panel.getModelInfo();
    areaInfo = new TextAreaModel(panel);
    modelInfo.setText("I took the one less traveled by. And that has made all the difference");
  }

  @Test
  public void canCalculateTheXPositionForTheCursorFromAString()
  {
    int width = modelInfo.getWidthDimension(new TextLayoutImpl("ABC", modelInfo.getFont(), TextPanel.getRenderContext()));
    int expectedX = width + modelInfo.SIDE_TEXT_MARGIN;

    int x = modelInfo.getXPosFromText("ABC");

    assertEquals(expectedX, x);
  }

  @Test
  public void canCalculateTheXPositionFromTheCursorIndex()
  {
    TextLayoutImpl line = new TextLayoutImpl(modelInfo.getText().substring(0, 5), modelInfo.getFont(), TextPanel.getRenderContext());
    int width1 = TextModel.SIDE_TEXT_MARGIN + modelInfo.getWidthDimension(line);
    int firstIndex = modelInfo.getXPosFromIndex(0);
    int secondIndex = modelInfo.getXPosFromIndex(5);

    assertEquals(modelInfo.SIDE_TEXT_MARGIN, firstIndex);
    assertEquals(width1, secondIndex);
  }

  @Test
  public void canCalculateTheYPositionForTheCursorViaAnIndex()
  {
    assertEquals(TextModel.TOP_MARGIN, modelInfo.getYPosFromIndex(0));
    assertEquals(21, modelInfo.getYPosFromIndex(45));
    assertEquals(21 * 2 - TextModel.TOP_MARGIN, modelInfo.getYPosFromIndex(65));
  }

  @Test
  public void canCalculateAYOffsetIfTheTextIsTooBigForTheTextAreaAndTheCursorIsInAHiddenLine()
  {
    modelInfo.setText("hi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\n");

    modelInfo.calculateYOffset();

    assertEquals(true, modelInfo.yOffset > 0);

    modelInfo.setCursorIndex(2);

    modelInfo.calculateYOffset();

    assertEquals(0, modelInfo.yOffset);
  }

  @Test
  public void willOnlyShiftYOffsetIfCursorIsAtTheTopOrBottomEdge()
  {
    modelInfo.setText("hi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\n");
    modelInfo.setCursorIndex(10);

    assertEquals(- TextModel.TOP_MARGIN, modelInfo.calculateYOffset());

    modelInfo.setCursorIndex(20);

    int staticYOffset = modelInfo.calculateYOffset();
    assertEquals(true, staticYOffset > 0);

    modelInfo.setCursorIndex(10);

    assertEquals(staticYOffset, modelInfo.calculateYOffset());

    modelInfo.setCursorIndex(0);

    assertEquals(- TextModel.TOP_MARGIN, modelInfo.calculateYOffset());

  }

  @Test
  public void willAddAnotherLineToTheYPositionIfTheLastCharacterBeforeCursorIsAReturn()
  {
    int expectedYForTwoLines = 21;
    int expectedYForThreeLines = 35;
    modelInfo.setText("some text\nmore text\n");

    int y = modelInfo.getYPosFromIndex(10);
    int y2 = modelInfo.getYPosFromIndex(20);

    assertEquals(expectedYForTwoLines, y);
    assertEquals(expectedYForThreeLines, y2);
  }

  @Test
  public void willPutTheCursorOnTheLeftIfTheLastCharacterBeforeCursorIsAReturn()
  {
    int expectedX = TextModel.SIDE_TEXT_MARGIN;
    modelInfo.setText("some text\n");

    int x = modelInfo.getXPosFromIndex(modelInfo.getCursorIndex());

    assertEquals(expectedX, x);
  }

  @Test
  public void canGetTheHeightOfTheCurrentLine()
  {
    int expectedHeight = (int) (modelInfo.getHeightDimension(new TextLayoutImpl("A", modelInfo.getFont(), TextPanel.getRenderContext())) + .5);
    modelInfo.setCursorIndex(50);

    int height = modelInfo.getHeightOfCurrentLine();

    assertEquals(expectedHeight, height);
  }

  @Test
  public void shouldBeAbleToFindNewLineIndices()
  {
    ArrayList<Integer> indices = areaInfo.findNewLineCharIndices("this\nIs\nA new \rline");

    assertEquals(3, indices.size());
    assertEquals(4, (int) indices.get(0));
    assertEquals(7, (int) indices.get(1));
    assertEquals(14, (int) indices.get(2));
  }

  @Test
  public void canSpiltTextIntoMultipleLinesBasedOffReturnKeys()
  {
    areaInfo.setText("this is the first line\nthis is the second line");
    ArrayList<TypedLayout> textLayouts = areaInfo.parseTextForMultipleLayouts();

    assertEquals(2, textLayouts.size());
    assertEquals("this is the first line\n", textLayouts.get(0).getText());
    assertEquals("this is the second line", textLayouts.get(1).getText());
  }

  @Test
  public void canSplitTextIntoMultipleLinesFromThePanelWidth()
  {
    areaInfo.setText("This here is the first full line. This is the second line");
    ArrayList<TypedLayout> textLayouts = areaInfo.parseTextForMultipleLayouts();

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
  public void willStoreATextLayoutForAnEmptyLine()
  {
    areaInfo.setText("This has an empty line\n");
    ArrayList<TypedLayout> textLayouts = areaInfo.getTextLayouts();
    assertEquals(2, textLayouts.size());
    assertEquals("", textLayouts.get(1).getText());
  }

  @Test
  public void willMakeANewLineForEveryReturnCharacterRegardlessOfTheLineItIsCurrentlyOn()
  {
    modelInfo.setText("This is going to be a very large amount of text. It seems to be the only way to make sure this works. Here\nis\n\nsome new lines");

    modelInfo.getTextLayouts();

    assertEquals(8, modelInfo.textLayouts.size());
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
    assertEquals(true, modelInfo.isBoxFull());
  }

  @Test
  public void canGetASelectedRegion()
  {
    modelInfo.setSelectionIndex(0);
    modelInfo.setSelectionOn(true);
    modelInfo.setCursorIndex(5);

    ArrayList<Rectangle> regions = modelInfo.getSelectionRegions();

    assertEquals(TextModel.SIDE_TEXT_MARGIN, regions.get(0).x);
    assertEquals(TextModel.TOP_MARGIN, regions.get(0).y);
    assertEquals(modelInfo.getXPosFromIndex(modelInfo.getCursorIndex()) - TextModel.SIDE_TEXT_MARGIN, regions.get(0).width);
    assertEquals(modelInfo.getTotalHeightOfLineWithLeadingMargin(modelInfo.getLineNumberOfIndex(5)), regions.get(0).height);
  }

  @Test
  public void canGetAMultiLinedSelectedRegion()
  {
    modelInfo.setSelectionIndex(2);
    modelInfo.setSelectionOn(true);
    modelInfo.setText("This is\nMulti Lined.");
    modelInfo.setCursorIndex(10);

    ArrayList<Rectangle> regions = modelInfo.getSelectionRegions();

    assertEquals(2, regions.size());
    assertEquals(modelInfo.getXPosFromIndex(2), regions.get(0).x);
    assertEquals(TextModel.TOP_MARGIN, regions.get(0).y);
    assertEquals(panel.getWidth() - modelInfo.getXPosFromIndex(2) - TextModel.SIDE_TEXT_MARGIN, regions.get(0).width);
    assertEquals(modelInfo.getTotalHeightOfLineWithLeadingMargin(modelInfo.getLineNumberOfIndex(2)), regions.get(0).height);

    assertEquals(TextModel.SIDE_TEXT_MARGIN, regions.get(1).x);
    assertEquals(modelInfo.getTotalHeightOfLineWithLeadingMargin(modelInfo.getLineNumberOfIndex(2)) + TextModel.TOP_MARGIN, regions.get(1).y);
    assertEquals(modelInfo.getXPosFromIndex(10) - TextModel.SIDE_TEXT_MARGIN, regions.get(1).width);
    assertEquals(modelInfo.getTotalHeightOfLineWithLeadingMargin(modelInfo.getLineNumberOfIndex(10)), regions.get(1).height);
  }

  @Test
  public void canGetAMultiLinedSelectedRegionWithAYOffset()
  {
    modelInfo.setText("line\nline\nline\nline\nline\nline\nline\nline\nline");
    modelInfo.setSelectionOn(true);
    modelInfo.setCursorIndex(modelInfo.getText().length() - 3);
    modelInfo.setSelectionIndex(modelInfo.getCursorIndex() - 15);

    ArrayList<Rectangle> regions = modelInfo.getSelectionRegions();
    assertEquals(true, modelInfo.getYPosFromIndex(modelInfo.getCursorIndex()) > panel.getHeight());
    assertEquals(true, regions.get(regions.size() -1).y < panel.getHeight());

  }

  @Test
  public void willReturnProperSelectedRegionsWhenThereIAYOffest()
  {
    areaInfo.setText("line\nline\nline\nline\nline\nline\nline\nline\n");
    areaInfo.setCursorIndex(areaInfo.getCursorIndex() - 1);
    areaInfo.setSelectionIndex(areaInfo.getCursorIndex() - 2);

    ArrayList<Rectangle> regions = areaInfo.getSelectionRegions();
    
    assertEquals(true, areaInfo.getYPosFromIndex(areaInfo.getCursorIndex()) > panel.getHeight());
    assertEquals(true, regions.get(regions.size() -1).y < panel.getHeight());
    assertEquals(areaInfo.getYPosFromIndex(areaInfo.getCursorIndex()) - areaInfo.calculateYOffset() - TextModel.TOP_MARGIN, regions.get(regions.size() - 1).y);
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
