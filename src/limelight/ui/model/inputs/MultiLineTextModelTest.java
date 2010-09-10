//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.text.TextLocation;
import limelight.ui.text.TypedLayout;
import limelight.util.Box;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
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

    assertEquals(-67, model.getYOffset());

    model.setCaretLocation(TextLocation.at(0, 2));

    assertEquals(0, model.getYOffset());
  }

  @Test
  public void willOnlyShiftYOffsetIfCursorIsAtTheTopOrBottomEdge()
  {
    model.setText("hi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\nhi\nbye\n");
    model.setCaretLocation(TextLocation.origin);
    model.setCaretLocation(TextLocation.at(3, 0));

    assertEquals(0, model.getYOffset());

    model.setCaretLocation(TextLocation.at(5, 3));

    assertEquals(0, model.getYOffset());

    model.setCaretLocation(TextLocation.origin);

    assertEquals(0, model.getYOffset());
  }

  @Test
  public void willPutTheCursorOnTheLeftIfTheLastCharacterBeforeCursorIsAReturn()
  {
    model.setText("some text\n");

    assertEquals(0, model.getX(model.getCaretLocation()));
  }

  @Test
  public void shouldBeAbleToFindNewLineIndices()
  {
    ArrayList<Integer> indices = Lineator.findNewLineCharIndices("this\nIs\nA new \nline");

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
    Lineator.parseTextForMultipleLayouts(model, lines);

    assertEquals(2, lines.size());
    assertEquals("this is the first line\n", lines.get(0).getText());
    assertEquals("this is the second line", lines.get(1).getText());
  }

  @Test
  public void canSplitTextIntoMultipleLinesFromThePanelWidth()
  {
    model.setText("This here is the first full line. This is the second line");
    ArrayList<TypedLayout> lines = new ArrayList<TypedLayout>();
    Lineator.parseTextForMultipleLayouts(model, lines);

    assertEquals(2, lines.size());
    assertEquals("This here is the first full line.", lines.get(0).getText().trim());
    assertEquals("This is the second line", lines.get(1).getText().trim());
  }

  @Test
  public void willStoreATextLayoutForEachLine()
  {
    model.setText("This is more than 1 line\r\nand should be 2 lines");

    ArrayList<TypedLayout> textLayouts = model.getLines();
    assertEquals(2, textLayouts.size());
    assertEquals("This is more than 1 line\r\n", textLayouts.get(0).getText());
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
    assertEquals(60, model.getTextDimensions().width);
    assertEquals(20, model.getTextDimensions().height);
  }

  @Test
  public void canGetAMultiLinedSelectedRegionWithAYOffset()
  {
    model.setText("line\nline\nline\nline\nline\nline\nline\nline\nline");
    model.startSelection(TextLocation.at(6, 1));
    model.setCaretLocation(TextLocation.at(8, 1));

    ArrayList<Box> regions = model.getSelectionRegions();
    assertEquals(65, regions.get(regions.size() -1).y);
  }

  @Test
  public void willReturnProperSelectedRegionsWhenThereIAYOffest()
  {
    model.setText("line\nline\nline\nline\nline\nline\nline\nline\n");
    model.setCaretLocation(TextLocation.at(7, 4));
    model.setSelectionLocation(TextLocation.at(7, 2));

    ArrayList<Box> regions = model.getSelectionRegions();

    assertEquals(54, regions.get(regions.size() -1).y);
  }

  @Test
  public void shouldGetCaretShape() throws Exception
  {
    model.setText("line 1\n line 2\n line 3");
    
    model.setCaretLocation(TextLocation.at(0, 3));
    assertEquals(new Box(30, 0, 1, 10), model.getCaretShape());

    model.setCaretLocation(TextLocation.at(1, 3));
    assertEquals(new Box(30, 11, 1, 10), model.getCaretShape());
    
    model.setCaretLocation(TextLocation.at(2, 3));
    assertEquals(new Box(30, 22, 1, 10), model.getCaretShape());
  }

  @Test
  public void shouldMoveTheCaretDown() throws Exception
  {
    model.setText("line 1\nline 2\nline 3");
    model.setCaretLocation(TextLocation.at(0, 2));
    
    model.moveCaretDownALine();
    assertEquals(TextLocation.at(1, 2), model.getCaretLocation());

    model.moveCaretDownALine();
    assertEquals(TextLocation.at(2, 2), model.getCaretLocation());

    model.moveCaretDownALine();
    assertEquals(TextLocation.at(2, 2), model.getCaretLocation());
  }

  @Test
  public void shouldRememberItsXpositionWhenMovingVerticallyBetweenLines() throws Exception
  {
    model.setText("Relatively long line\nshort line\ntiny");
    model.setCaretLocation(TextLocation.at(0, 20));

    model.moveCaretDownALine();
    assertEquals(TextLocation.at(1, 10), model.getCaretLocation());

    model.moveCaretDownALine();
    assertEquals(TextLocation.at(2, 4), model.getCaretLocation());

    model.moveCaretUpALine();
    assertEquals(TextLocation.at(1, 10), model.getCaretLocation());

    model.moveCaretUpALine();
    assertEquals(TextLocation.at(0, 20), model.getCaretLocation());
  }

  @Test
  public void shouldMoveCaret() throws Exception
  {
    model.setText("a\nbc\r\nd");

    assertEquals(TextLocation.at(2, 1), model.getCaretLocation());
    model.moveCaret(-1);
    assertEquals(TextLocation.at(2, 0), model.getCaretLocation());
    model.moveCaret(-1);
    assertEquals(TextLocation.at(1, 2), model.getCaretLocation());
    model.moveCaret(-1);
    assertEquals(TextLocation.at(1, 1), model.getCaretLocation());
    model.moveCaret(-1);
    assertEquals(TextLocation.at(1, 0), model.getCaretLocation());
    model.moveCaret(-1);
    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    model.moveCaret(-1);
    assertEquals(TextLocation.at(0, 0), model.getCaretLocation());
    model.moveCaret(-1);
    assertEquals(TextLocation.at(0, 0), model.getCaretLocation());

    model.moveCaret(1);
    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    model.moveCaret(1);
    assertEquals(TextLocation.at(1, 0), model.getCaretLocation());
    model.moveCaret(1);
    assertEquals(TextLocation.at(1, 1), model.getCaretLocation());
    model.moveCaret(1);
    assertEquals(TextLocation.at(1, 2), model.getCaretLocation());
    model.moveCaret(1);
    assertEquals(TextLocation.at(2, 0), model.getCaretLocation());
    model.moveCaret(1);
    assertEquals(TextLocation.at(2, 1), model.getCaretLocation());
    model.moveCaret(1);
    assertEquals(TextLocation.at(2, 1), model.getCaretLocation());
  }
  
  @Test
  public void shouldMoveCaretThroughNewlines() throws Exception
  {
    model.setText("a\n\n\nb");

    assertEquals(TextLocation.at(3, 1), model.getCaretLocation());
    model.moveCaret(-1);
    assertEquals(TextLocation.at(3, 0), model.getCaretLocation());
    model.moveCaret(-1);
    assertEquals(TextLocation.at(2, 0), model.getCaretLocation());
    model.moveCaret(-1);
    assertEquals(TextLocation.at(1, 0), model.getCaretLocation());
    model.moveCaret(-1);
    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    model.moveCaret(-1);
    assertEquals(TextLocation.at(0, 0), model.getCaretLocation());

    model.moveCaret(1);
    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    model.moveCaret(1);
    assertEquals(TextLocation.at(1, 0), model.getCaretLocation());
    model.moveCaret(1);
    assertEquals(TextLocation.at(2, 0), model.getCaretLocation());
    model.moveCaret(1);
    assertEquals(TextLocation.at(3, 0), model.getCaretLocation());
    model.moveCaret(1);
    assertEquals(TextLocation.at(3, 1), model.getCaretLocation());
  }

  @Test
  public void shouldSelectionRegionsWithCenteredText() throws Exception
  {
    container.getStyle().setHorizontalAlignment("center");
    model.setText("1 line\nand\nanother line\n.");
    model.startSelection(TextLocation.origin);

    ArrayList<Box> regions = model.getSelectionRegions();
  
    assertEquals(new Box(45, 0, 105, 10), regions.get(0));
    assertEquals(new Box(0, 11, 150, 10), regions.get(1));
    assertEquals(new Box(0, 22, 150, 10), regions.get(2));
    assertEquals(new Box(0, 33, 80, 10), regions.get(3));
  }

  @Test
  public void shouldGiveCaretShapeWithCenteredText() throws Exception
  {
    container.getStyle().setHorizontalAlignment("center");
    model.setText("1 line\nand\nanother line\n.");
                                                  
    model.setCaretLocation(TextLocation.at(0, 0));
    assertEquals(new Box(45, 0, 1, 10), model.getCaretShape());

    model.setCaretLocation(TextLocation.at(1, 0));
    assertEquals(new Box(60, 11, 1, 10), model.getCaretShape());

    model.setCaretLocation(TextLocation.at(2, 0));
    assertEquals(new Box(15, 22, 1, 10), model.getCaretShape());

    model.setCaretLocation(TextLocation.at(3, 0));
    assertEquals(new Box(70, 33, 1, 10), model.getCaretShape());
  }

  @Test
  public void shouldGetLocationAtPointWithCenteredText() throws Exception
  {
    container.getStyle().setHorizontalAlignment("center");
    model.setText("1 line\nand\nanother line\n.");
    
    assertEquals(TextLocation.at(0, 0), model.getLocationAt(new Point(45, 0)));
    assertEquals(TextLocation.at(1, 0), model.getLocationAt(new Point(60, 11)));
    assertEquals(TextLocation.at(2, 0), model.getLocationAt(new Point(15, 22)));
    assertEquals(TextLocation.at(3, 0), model.getLocationAt(new Point(70, 33)));
  }

  @Test
  public void sendingCareToEndOfLine() throws Exception
  {
    model.setText("line 1\nline 2 \nline 3\t\nline4");

    model.setCaretLocation(TextLocation.at(0, 1));
    model.sendCaretToEndOfLine();
    assertEquals(TextLocation.at(0, 6), model.getCaretLocation());
    
    model.setCaretLocation(TextLocation.at(1, 1));
    model.sendCaretToEndOfLine();
    assertEquals(TextLocation.at(1, 7), model.getCaretLocation());

    model.setCaretLocation(TextLocation.at(2, 1));
    model.sendCaretToEndOfLine();
    assertEquals(TextLocation.at(2, 7), model.getCaretLocation());
  }

}
