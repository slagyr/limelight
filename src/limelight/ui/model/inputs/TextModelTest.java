//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.text.TextLocation;
import limelight.util.Box;
import org.junit.Before;
import org.junit.Test;

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
    model.copyText("This Text");
    String clipboard = model.getClipboardContents();
    assertEquals("This Text", clipboard);
  }

  @Test
  public void canCopyPasteAndCut()
  {
    model.setText("Some Text");
    model.setCaretIndex(0);
    model.startSelection(TextLocation.at(0, 4));
    model.copySelection();
    String clipboard = model.getClipboardContents();
    assertEquals("Some", clipboard);

    model.pasteClipboard();
    assertEquals("SomeSome Text", model.getText());

    model.setCaretIndex(0);
    model.setSelectionIndex(8);
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

}
