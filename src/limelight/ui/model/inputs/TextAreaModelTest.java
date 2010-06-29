//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.MockPanel;
import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.TextLayoutImpl;
import limelight.ui.TypedLayout;
import limelight.ui.model.TextPanel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class TextAreaModelTest
{
  private TextAreaModel model;
  private TextInputPanel panel;

  @Before
  public void setUp()
  {
    panel = new TextArea2Panel();
    MockPanel parent = new MockPanel();
    parent.add(panel);
    parent.setSize(150, 75);
    panel.doLayout();
    model = (TextAreaModel)panel.getModel();
    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);
    model.setText("I took the one less traveled by. And that has made all the difference");
  }

  @Test
  public void canCalculateTheXPositionForTheCursorFromAString()
  {
    int x = model.getXPosFromText("ABC");

    assertEquals(30, x);
  }

  @Test
  public void canCalculateTheXPositionFromTheCursorIndex()
  {
    assertEquals(0, model.getXPosFromIndex(0));
    assertEquals(50, model.getXPosFromIndex(5));
  }

  @Test
  public void canCalculateTheYPositionForTheCursorViaAnIndex()
  {
    assertEquals(0, model.getYPosFromIndex(0));
    assertEquals(6, model.getYPosFromIndex(45));
    assertEquals(12, model.getYPosFromIndex(65));
  }

  @Test
  public void canCalculateAYOffsetIfTheTextIsTooBigForTheTextAreaAndTheCursorIsInAHiddenLine()
  {
    model.setText("hi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\n");

    assertEquals(-84, model.getYOffset());

    model.setCaretIndex(2);

    assertEquals(0, model.getYOffset());
  }

  @Test
  public void willOnlyShiftYOffsetIfCursorIsAtTheTopOrBottomEdge()
  {
    model.setText("hi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\n");
    model.setCaretIndex(0);
    model.setCaretIndex(10);

    assertEquals(0, model.getYOffset());

    model.setCaretIndex(20);

    assertEquals(0, model.getYOffset());

    model.setCaretIndex(0);

    assertEquals(0, model.getYOffset());
  }

  @Test
  public void willAddAnotherLineToTheYPositionIfTheLastCharacterBeforeCursorIsAReturn()
  {
    model.setText("some text\nmore text\n");

    assertEquals(6, model.getYPosFromIndex(10));
    assertEquals(12, model.getYPosFromIndex(20));
  }

  @Test
  public void willPutTheCursorOnTheLeftIfTheLastCharacterBeforeCursorIsAReturn()
  {
    model.setText("some text\n");

    assertEquals(0, model.getXPosFromIndex(model.getCaretIndex()));
  }

  @Test
  public void canGetTheHeightOfTheCurrentLine()
  {
    model.setCaretIndex(50);

    assertEquals(10, model.getHeightOfCurrentLine());
  }

  @Test
  public void shouldBeAbleToFindNewLineIndices()
  {
    ArrayList<Integer> indices = model.findNewLineCharIndices("this\nIs\nA new \rline");

    assertEquals(3, indices.size());
    assertEquals(4, (int) indices.get(0));
    assertEquals(7, (int) indices.get(1));
    assertEquals(14, (int) indices.get(2));
  }

  @Test
  public void canSpiltTextIntoMultipleLinesBasedOffReturnKeys()
  {
    model.setText("this is the first line\nthis is the second line");
    ArrayList<TypedLayout> textLayouts = model.parseTextForMultipleLayouts();

    assertEquals(2, textLayouts.size());
    assertEquals("this is the first line\n", textLayouts.get(0).getText());
    assertEquals("this is the second line", textLayouts.get(1).getText());
  }

  @Test
  public void canSplitTextIntoMultipleLinesFromThePanelWidth()
  {
    model.setText("This here is the first full line. This is the second line");
    ArrayList<TypedLayout> textLayouts = model.parseTextForMultipleLayouts();

    assertEquals(2, textLayouts.size());
    assertEquals("This here is the first full line.", textLayouts.get(0).getText().trim());
    assertEquals("This is the second line", textLayouts.get(1).getText().trim());
  }

  @Test
  public void willStoreATextLayoutForEachLine()
  {
    model.setText("This is more than 1 line\rand should be 2 lines");

    ArrayList<TypedLayout> textLayouts = model.getTypedLayouts();
    assertEquals(2, textLayouts.size());
    assertEquals("This is more than 1 line\r", textLayouts.get(0).getText());
    assertEquals("and should be 2 lines", textLayouts.get(1).getText());
  }

  @Test
  public void willStoreATextLayoutForAnEmptyLine()
  {
    model.setText("This has an empty line\n");
    ArrayList<TypedLayout> textLayouts = model.getTypedLayouts();
    assertEquals(2, textLayouts.size());
    assertEquals("", textLayouts.get(1).getText());
  }

  @Test
  public void willMakeANewLineForEveryReturnCharacterRegardlessOfTheLineItIsCurrentlyOn()
  {
    model.setText("This is going to be a very large amount of text. It seems to be the only way to make sure this works. Here\nis\n\nsome new lines");

    model.getTypedLayouts();

    assertEquals(7, model.typedLayouts.size());
  }

  @Test
  public void canCalculateTheTextModelsDimensions()
  {
    model.setText("line 1\nline 2");
    assertEquals(70, model.getTextDimensions().width);
    assertEquals(20, model.getTextDimensions().height);
  }

  @Test
  public void canTellIfTheTextPanelIsFull()
  {
    model.setText("line\nline\nline\nline\nline\nline\nline\nline\n");
    assertEquals(true, model.isBoxFull());
  }

  @Test
  public void canGetASelectedRegion()
  {
    model.setSelectionIndex(0);
    model.setSelectionOn(true);
    model.setCaretIndex(5);

    ArrayList<Rectangle> regions = model.getSelectionRegions();

    assertEquals(0, regions.get(0).x);
    assertEquals(0, regions.get(0).y);
    assertEquals(model.getXPosFromIndex(model.getCaretIndex()), regions.get(0).width);
    assertEquals(model.getTotalHeightOfLineWithLeadingMargin(model.getLineNumberOfIndex(5)), regions.get(0).height);
  }

  @Test
  public void canGetAMultiLinedSelectedRegion()
  {
    model.setSelectionIndex(2);
    model.setSelectionOn(true);
    model.setText("This is\nMulti Lined.");
    model.setCaretIndex(10);

    ArrayList<Rectangle> regions = model.getSelectionRegions();

    assertEquals(2, regions.size());
    assertEquals(model.getXPosFromIndex(2), regions.get(0).x);
    assertEquals(0, regions.get(0).y);
    assertEquals(panel.getWidth() - model.getXPosFromIndex(2), regions.get(0).width);
    assertEquals(model.getTotalHeightOfLineWithLeadingMargin(model.getLineNumberOfIndex(2)), regions.get(0).height);

    assertEquals(0, regions.get(1).x);
    assertEquals(model.getTotalHeightOfLineWithLeadingMargin(model.getLineNumberOfIndex(2)), regions.get(1).y);
    assertEquals(model.getXPosFromIndex(10), regions.get(1).width);
    assertEquals(model.getTotalHeightOfLineWithLeadingMargin(model.getLineNumberOfIndex(10)), regions.get(1).height);
  }

  @Test
  public void canGetAMultiLinedSelectedRegionWithAYOffset()
  {
    model.setText("line\nline\nline\nline\nline\nline\nline\nline\nline");
    model.setSelectionOn(true);
    model.setCaretIndex(model.getText().length() - 3);
    model.setSelectionIndex(model.getCaretIndex() - 15);

    ArrayList<Rectangle> regions = model.getSelectionRegions();
    assertEquals(48, model.getYPosFromIndex(model.getCaretIndex()));
    assertEquals(48, regions.get(regions.size() -1).y);
  }

  @Test
  public void willReturnProperSelectedRegionsWhenThereIAYOffest()
  {
    model.setText("line\nline\nline\nline\nline\nline\nline\nline\n");
    model.setCaretIndex(model.getCaretIndex() - 1);
    model.setSelectionIndex(model.getCaretIndex() - 2);

    ArrayList<Rectangle> regions = model.getSelectionRegions();
    
    assertEquals(42, model.getYPosFromIndex(model.getCaretIndex()));
    assertEquals(42, regions.get(regions.size() -1).y);
  }

  @Test
  public void canGetTheLastCharacterInALine()
  {
    model.setText("This is\nMulti Lined.\nWith Many Lines");

    assertEquals(20, model.getIndexOfLastCharInLine(1));
  }

  @Test
  public void canGetTheLastCharacterOfTheText()
  {
    model.setText("This is\nMulti Lined.\nWith Many Lines");

    assertEquals(36, model.getIndexOfLastCharInLine(2));
  }

}
