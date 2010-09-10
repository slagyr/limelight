//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.text.TextLocation;
import limelight.util.Box;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

import static junit.framework.Assert.assertEquals;

public class TextModelTest
{
  TextModel model;
  MockTextContainer container;

  @Before
  public void setUp()
  {
    container = new MockTextContainer();
    container.bounds = new Box(0, 0, 150, 20);
    model = new SingleLineTextModel(container);
    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);
    model.setText("Bob Dole likes to hear Bob Dole say 'Bob Dole'  ");
  }

  @Test
  public void canCalculateTheXPositionFromTheCursorIndex()
  {
    model.setCaretLocation(TextLocation.at(0, 0));
    assertEquals(0, model.getX(0));
    assertEquals(10, model.getX(1));
    assertEquals(20, model.getX(2));
    assertEquals(30, model.getX(3));
    assertEquals(40, model.getX(4));
  }

  @Test
  public void canMakeUseOfTheClipboard()
  {
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection("This Text"), model);
    String clipboard = model.getClipboardContents();
    assertEquals("This Text", clipboard);
  }

  @Test
  public void canCopyPasteAndCut()
  {
    model.setText("Some Text");
    model.setCaretLocation(TextLocation.origin);
    model.startSelection(TextLocation.at(0, 4));
    model.copySelection();
    String clipboard = model.getClipboardContents();
    assertEquals("Some", clipboard);

    model.pasteClipboard();
    assertEquals("SomeSome Text", model.getText());

    model.setCaretLocation(TextLocation.origin);
    model.startSelection(TextLocation.at(0, 8));
    model.cutSelection();
    assertEquals("SomeSome", model.getClipboardContents());
    assertEquals(" Text", model.getText());
  }
  
  @Test
  public void shouldGetAlignment() throws Exception
  {
    container.style.setVerticalAlignment("center");
    container.style.setHorizontalAlignment("center");
    assertEquals("center", model.getVerticalAlignment().toString());
    assertEquals("center", model.getHorizontalAlignment().toString());

    container.style.setVerticalAlignment("bottom");
    container.style.setHorizontalAlignment("right");
    assertEquals("bottom", model.getVerticalAlignment().toString());
    assertEquals("right", model.getHorizontalAlignment().toString());
  }

  @Test
  public void insertingSomethingWillDeleteASelectionIfOneExists() throws Exception
  {
    model.setText("abc123");
    model.startSelection(TextLocation.origin);
    model.setCaretLocation(TextLocation.at(0, 3));

    model.insertChar('0');

    assertEquals("0123", model.getText());
  }

  @Test
  public void startingASelection() throws Exception
  {
    model.setText("abc");

    model.startSelection(TextLocation.origin);

    assertEquals(true, model.hasSelection());
    assertEquals(true, model.isSelectionActivated());
    assertEquals("abc", model.getSelectedText());
  }
  
  @Test
  public void deactivatingSelection() throws Exception
  {
    model.setText("abc");
    model.startSelection(TextLocation.origin);

    model.deactivateSelection();

    assertEquals(false, model.hasSelection());
    assertEquals(false, model.isSelectionActivated());
    assertEquals("", model.getSelectedText());
  }
  
  @Test
  public void selectionActivatedAtCaretLocation() throws Exception
  {
    model.setText("abc");
    model.startSelection(model.getCaretLocation());

    assertEquals(false, model.hasSelection());
    assertEquals(true, model.isSelectionActivated());
    assertEquals("", model.getSelectedText());
  }

  @Test
  public void modelReportsAsChangedWhenTextIsSet() throws Exception
  {
    model.setText("blah");
    model.resetChangeFlag();
    assertEquals(false, model.hasChanged());

    model.setText("blah");
    assertEquals(false, model.hasChanged());

    model.setText("yum");
    assertEquals(true, model.hasChanged());
  }
  
  @Test
  public void modelReportsChangeWhenCharIsInserted() throws Exception
  {
    model.setText("blah");
    model.resetChangeFlag();
    model.insertChar('a');
    assertEquals(true, model.hasChanged());
  }
  
  @Test
  public void reportsChangeWhenSelectionIsDeleted() throws Exception
  {
    model.setText("blah");
    model.resetChangeFlag();
    model.selectAll();
    model.deleteSelection();
    assertEquals(true, model.hasChanged());
  }

  @Test
  public void reportsChangeWhenTextIsPasted() throws Exception
  {
    model.setText("blah");
    model.resetChangeFlag();
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection("yum"), model);
    model.pasteClipboard();
    assertEquals(true, model.hasChanged());
  }

  @Test
  public void nearestWordToTheRight()
  {
    model.setText("Here are four words");
    model.setCaretLocation(TextLocation.at(0, 1));
    Assert.assertEquals(TextLocation.at(0, 5), model.locateNearestWordToTheRight());
  }

  @Test
  public void nearestWordToTheRightStopsAtReturnCharacters()
  {
    model.setText("Here is some\ntext");
    model.setCaretLocation(TextLocation.at(0, 9));
    Assert.assertEquals(TextLocation.at(0, 13), model.locateNearestWordToTheRight());
  }

  @Test
  public void nearestWordToTheRightStopsAtReturnCharactersUnlessItIsAChainOfReturnChars()
  {
    model.setText("Here is some\n\n\ntext");
    model.setCaretLocation(TextLocation.at(0, 9));
    Assert.assertEquals(TextLocation.at(0, 15), model.locateNearestWordToTheRight());
  }

  @Test
  public void nearestWordToTheRightWontStopAtReturnCharactersIfPrecededByASpace()
  {
    model.setText("Here is some \ntext");
    model.setCaretLocation(TextLocation.at(0, 9));
    Assert.assertEquals(TextLocation.at(0, 14), model.locateNearestWordToTheRight());
  }
  
  @Test
  public void nearestWordToTheLeft()
  {
    model.setText("Here are four words");
    model.setCaretLocation(TextLocation.at(0, 9));
    Assert.assertEquals(TextLocation.at(0, 5), model.locateNearestWordToTheLeft());
  }

  @Test
  public void nearestWordToTheLeftSkipsOverExtraSpacesToPreviousWord()
  {
    model.setText("Here are    many  spaces");
    model.setCaretLocation(TextLocation.at(0, 12));
    Assert.assertEquals(TextLocation.at(0, 5), model.locateNearestWordToTheLeft());
  }

  @Test
  public void sendingCareToEndOfLine() throws Exception
  {
    model.setText("This is some text\n");
  }

}
