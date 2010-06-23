//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.MockPanel;
import limelight.ui.model.inputs.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import static org.junit.Assert.assertEquals;

public class AbstractKeyProcessorTest
{
  public final int SELECTION_START_INDEX = 4;

  public TextModel modelInfo;
  public TextInputPanel panel;
  public KeyProcessor processor;
  public TextModelAsserter asserter;
  public MockKeyEvent mockEvent;
  public MockPanel parent;
  public int modifier;

  public void textBoxSetUp()
  {
    panel = new TextBox2Panel();
    parent = new MockPanel();
    parent.setSize(150, 28);
    setUp();
    modelInfo.setSelectionIndex(0);
    modelInfo.setSelectionOn(false);
  }

  public void textAreaSetUp()
  {
    panel = new TextArea2Panel();
    parent = new MockPanel();
    parent.setSize(150, 75);
    setUp();
    modelInfo.setSelectionIndex(0);
    modelInfo.setSelectionOn(false);
  }

  private void setUp()
  {
    modelInfo = panel.getModel();
    asserter = new TextModelAsserter(modelInfo);

    modelInfo.setText("Here are four words");
    modelInfo.setCaretIndex(1);

    parent.add(panel);
    panel.doLayout();
  }

  protected void selectionBoxSetUp()
  {
    panel = new TextBox2Panel();
    parent = new MockPanel();
    parent.setSize(150, 28);
    setUp();
    modelInfo.setSelectionIndex(SELECTION_START_INDEX);
    modelInfo.setSelectionOn(true);
  }

   protected void selectionAreaSetUp()
  {
    panel = new TextArea2Panel();
    parent = new MockPanel();
    parent.setSize(150, 75);
    setUp();
    modelInfo.setSelectionIndex(SELECTION_START_INDEX);
    modelInfo.setSelectionOn(true);
  }

  public class MockKeyEvent extends KeyEvent
  {

    public MockKeyEvent(int modifiers, int keyCode)
    {
      super(new Panel(), 1, 123456789l, modifiers, keyCode, ' ');
    }

    public MockKeyEvent(int modifiers, int keyCode, char c)
    {
      super(new Panel(), 1, 123456789l, modifiers, keyCode, c);
    }
  }

  public class TextModelAsserter
  {
    TextModel modelInfo;

    public TextModelAsserter(TextModel modelInfo)
    {
      this.modelInfo = modelInfo;
    }


    public void assertSelection(int cursorIndex, int selectionIndex, boolean selectionOn)
    {
      assertEquals(cursorIndex, modelInfo.getCaretIndex());
      assertEquals(selectionIndex, modelInfo.getSelectionIndex());
      assertEquals(selectionOn, modelInfo.isSelectionOn());
    }

    public void assertTextState(int cursorIndex, String text)
    {
      assertEquals(cursorIndex, modelInfo.getCaretIndex());
      assertEquals(text, modelInfo.getText().toString());
      if (!modelInfo.isSelectionOn())
        assertEquals(0, modelInfo.getSelectionIndex());
    }
  }

}
