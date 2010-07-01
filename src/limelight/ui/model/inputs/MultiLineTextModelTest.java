//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.MockPanel;
import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.text.TypedLayout;
import limelight.util.Box;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class MultiLineTextModelTest
{
  private MultiLineTextModel model;
  private MockTextContainer container;

  @Before
  public void setUp()
  {
    container = new MockTextContainer();
    container.bounds = new Box(0, 0, 150, 75);
    model = new MultiLineTextModel(container);
    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);
    model.setText("I took the one less traveled by. And that has made all the difference");
  }

  @Test
  public void canCalculateAYOffsetIfTheTextIsTooBigForTheTextAreaAndTheCursorIsInAHiddenLine()
  {
    model.setText("hi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\n");

    assertEquals(-71, model.getYOffset());

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
  public void willPutTheCursorOnTheLeftIfTheLastCharacterBeforeCursorIsAReturn()
  {
    model.setText("some text\n");

    assertEquals(0, model.getX(model.getCaretIndex()));
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
    ArrayList<TypedLayout> lines = new ArrayList<TypedLayout>();
    model.parseTextForMultipleLayouts(lines);

    assertEquals(2, lines.size());
    assertEquals("this is the first line\n", lines.get(0).getText());
    assertEquals("this is the second line", lines.get(1).getText());
  }

  @Test
  public void canSplitTextIntoMultipleLinesFromThePanelWidth()
  {
    model.setText("This here is the first full line. This is the second line");
    ArrayList<TypedLayout> lines = new ArrayList<TypedLayout>();
    model.parseTextForMultipleLayouts(lines);

    assertEquals(2, lines.size());
    assertEquals("This here is the first full line.", lines.get(0).getText().trim());
    assertEquals("This is the second line", lines.get(1).getText().trim());
  }

  @Test
  public void willStoreATextLayoutForEachLine()
  {
    model.setText("This is more than 1 line\rand should be 2 lines");

    ArrayList<TypedLayout> textLayouts = model.getLines();
    assertEquals(2, textLayouts.size());
    assertEquals("This is more than 1 line\r", textLayouts.get(0).getText());
    assertEquals("and should be 2 lines", textLayouts.get(1).getText());
  }

  @Test
  public void willStoreATextLayoutForAnEmptyLine()
  {
    model.setText("This has an empty line\n");
    ArrayList<TypedLayout> textLayouts = model.getLines();
    assertEquals(2, textLayouts.size());
    assertEquals("", textLayouts.get(1).getText());
  }

  @Test
  public void willMakeANewLineForEveryReturnCharacterRegardlessOfTheLineItIsCurrentlyOn()
  {
    model.setText("This is going to be a very large amount of text. It seems to be the only way to make sure this works. Here\nis\n\nsome new lines");

    model.getLines();

    assertEquals(7, model.getLines().size());
  }

  @Test
  public void canCalculateTheTextModelsDimensions()
  {
    model.setText("line 1\nline 2");
    assertEquals(70, model.getTextDimensions().width);
    assertEquals(20, model.getTextDimensions().height);
  }

  @Test
  public void canGetASelectedRegion()
  {
    model.setSelectionIndex(0);
    model.setSelectionOn(true);
    model.setCaretIndex(5);

    ArrayList<Box> regions = model.getSelectionRegions();

    assertEquals(0, regions.get(0).x);
    assertEquals(0, regions.get(0).y);
    assertEquals(model.getX(model.getCaretIndex()), regions.get(0).width);
    assertEquals(model.getTotalHeightOfLineWithLeadingMargin(model.getLineNumber(5)), regions.get(0).height);
  }

  @Test
  public void canGetAMultiLinedSelectedRegion()
  {
    model.setSelectionIndex(2);
    model.setSelectionOn(true);
    model.setText("This is\nMulti Lined.");
    model.setCaretIndex(10);

    ArrayList<Box> regions = model.getSelectionRegions();

    assertEquals(2, regions.size());
    assertEquals(model.getX(2), regions.get(0).x);
    assertEquals(0, regions.get(0).y);
    assertEquals(container.getWidth() - model.getX(2), regions.get(0).width);
    assertEquals(model.getTotalHeightOfLineWithLeadingMargin(model.getLineNumber(2)), regions.get(0).height);

    assertEquals(0, regions.get(1).x);
    assertEquals(model.getTotalHeightOfLineWithLeadingMargin(model.getLineNumber(2)), regions.get(1).y);
    assertEquals(model.getX(10), regions.get(1).width);
    assertEquals(model.getTotalHeightOfLineWithLeadingMargin(model.getLineNumber(10)), regions.get(1).height);
  }

  @Test
  public void canGetAMultiLinedSelectedRegionWithAYOffset()
  {
    model.setText("line\nline\nline\nline\nline\nline\nline\nline\nline");
    model.setSelectionOn(true);
    model.setCaretIndex(model.getText().length() - 3);
    model.setSelectionIndex(model.getCaretIndex() - 15);

    ArrayList<Box> regions = model.getSelectionRegions();
    assertEquals(123, regions.get(regions.size() -1).y);
  }

  @Test
  public void willReturnProperSelectedRegionsWhenThereIAYOffest()
  {
    model.setText("line\nline\nline\nline\nline\nline\nline\nline\n");
    model.setCaretIndex(model.getCaretIndex() - 1);
    model.setSelectionIndex(model.getCaretIndex() - 2);

    ArrayList<Box> regions = model.getSelectionRegions();

    assertEquals(91, regions.get(regions.size() -1).y);
  }

  @Test
  public void shouldGetCaretShape() throws Exception
  {
    model.setText("line 1\n line 2\n line 3");
    
    model.setCaretIndex(3);
    assertEquals(new Box(30, 0, 1, 10), model.getCaretShape());

    model.setCaretIndex(10);
    assertEquals(new Box(30, 13, 1, 10), model.getCaretShape());
    
    model.setCaretIndex(18);
    assertEquals(new Box(30, 26, 1, 10), model.getCaretShape());
  }

  @Test
  public void shouldMoveTheCaretDown() throws Exception
  {
    model.setText("line 1\nline 2\nline 3");
    model.setCaretIndex(2);
    
    model.moveCursorDownALine();
    assertEquals(9, model.getCaretIndex());

    model.moveCursorDownALine();
    assertEquals(16, model.getCaretIndex());

    model.moveCursorDownALine();
    assertEquals(16, model.getCaretIndex());
  }

  @Test
  public void shouldRememberItsXpositionWhenMovingVerticallyBetweenLines() throws Exception
  {
    model.setText("Relatively long line\nshort line\ntiny");
    model.setCaretIndex(20);

    model.moveCursorDownALine();
    assertEquals(31, model.getCaretIndex());

    model.moveCursorDownALine();
    assertEquals(36, model.getCaretIndex());

    model.moveCursorUpALine();
    assertEquals(31, model.getCaretIndex());

    model.moveCursorUpALine();
    assertEquals(20, model.getCaretIndex());
  }

}
