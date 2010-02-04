package limelight.ui.model.inputs;

import limelight.ui.model.inputs.keyProcessors.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.Assert.assertEquals;

@RunWith(Suite.class)
@Suite.SuiteClasses({KeyProcessorSuite.StandardKeyProcessorTest.class, KeyProcessorSuite.CmdKeyProcessorTest.class, KeyProcessorSuite.ShiftKeyProcessorTest.class,
    KeyProcessorSuite.ShiftCmdKeyProcessorTest.class, KeyProcessorSuite.AltKeyProcessorTest.class, KeyProcessorSuite.AltCmdKeyProcessorTest.class,
    KeyProcessorSuite.AltShiftKeyProcessorTest.class, KeyProcessorSuite.AltShiftCmdKeyProcessorTest.class, KeyProcessorSuite.SelectionKeyProcessorTest.class,
    KeyProcessorSuite.SelectionCmdKeyProcessorTest.class, KeyProcessorSuite.SelectionShiftKeyProcessorTest.class, KeyProcessorSuite.SelectionShiftCmdKeyProcessorTest.class,
    KeyProcessorSuite.SelectionAltKeyProcessorTest.class, KeyProcessorSuite.SelectionAltCmdKeyProcessorTest.class, KeyProcessorSuite.SelectionAltShiftKeyProcessorTest.class,
    KeyProcessorSuite.SelectionAltShiftCmdKeyProcessorTest.class})
public class KeyProcessorSuite
{
  public static final int SELECTION_START_INDEX = 4;

  public static TextModel boxInfo;
  public static TextInputPanel boxPanel;
  public static KeyProcessor processor;
  public static TextModelAsserter asserter;
  public static MockKeyEvent mockEvent;
  public static int modifier;


  public static void standardSetUp()
  {
    setUp();
    boxInfo.setSelectionIndex(0);
    boxInfo.selectionOn = false;
  }

  private static void setUp()
  {
    boxPanel = new TextBox2Panel();
    boxInfo = boxPanel.getModelInfo();
    asserter = new TextModelAsserter(boxInfo);

    boxInfo.setText("Here are four words");
    boxInfo.setCursorIndex(1);
  }

  private static void selectionSetUp()
  {
    setUp();
    boxInfo.setSelectionIndex(SELECTION_START_INDEX);
    boxInfo.selectionOn = true;
  }

  public static class MockKeyEvent extends KeyEvent
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

  public static class TextModelAsserter
  {
    TextModel boxInfo;

    public TextModelAsserter(TextModel boxInfo)
    {
      this.boxInfo = boxInfo;
    }


    public void assertSelection(int cursorIndex, int selectionIndex, boolean selectionOn)
    {
      assertEquals(cursorIndex, boxInfo.getCursorIndex());
      assertEquals(selectionIndex, boxInfo.getSelectionIndex());
      assertEquals(selectionOn, boxInfo.selectionOn);
    }

    public void assertTextState(int cursorIndex, String text)
    {
      assertEquals(cursorIndex, boxInfo.getCursorIndex());
      assertEquals(text, boxInfo.getText().toString());
      if (!boxInfo.selectionOn)
        assertEquals(0, boxInfo.getSelectionIndex());
    }
  }

  public static class StandardKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      standardSetUp();
      processor = new NormalKeyProcessor(boxInfo);
      modifier = 0;
    }

    @Test
    public void canProcessCharacters()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_A, 'a');

      processor.processKey(mockEvent);

      asserter.assertTextState(2, "Haere are four words");
    }

    @Test
    public void canProcessBackSpace()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_BACK_SPACE);

      processor.processKey(mockEvent);

      asserter.assertTextState(0, "ere are four words");
    }

    @Test
    public void canProcessRightArrow()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_RIGHT);

      processor.processKey(mockEvent);

      asserter.assertSelection(2, 0, false);
    }

    @Test
    public void canProcessLeftArrow()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_LEFT);

      processor.processKey(mockEvent);

      asserter.assertSelection(0, 0, false);
    }
  }

  public static class CmdKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      standardSetUp();
      processor = new CmdKeyProcessor(boxInfo);
      modifier = 4;
    }

    @Test
    public void canSelectAll()
    {
      boxInfo.setText("Bob");
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_A);

      processor.processKey(mockEvent);

      asserter.assertSelection(3, 0, true);
    }

    @Test
    public void canPasteAtCursor()
    {
      boxInfo.setText("Bob");
      boxInfo.copyText(" Dole");
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_V);

      processor.processKey(mockEvent);

      asserter.assertTextState(boxInfo.getText().length(), "Bob Dole");
    }

    @Test
    public void canProcessRightArrow()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_RIGHT);

      processor.processKey(mockEvent);

      asserter.assertSelection(boxInfo.getText().length(), 0, false);
    }

    @Test
    public void canProcessLeftArrow()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_LEFT);

      processor.processKey(mockEvent);

      asserter.assertSelection(0, 0, false);
    }
  }

  public static class ShiftKeyProcessorTest
  {

    @Before
    public void setUp()
    {
      standardSetUp();
      processor = new ShiftKeyProcessor(boxInfo);
      modifier = 1;
    }

    @Test
    public void canProcessCharacters()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_A, 'A');

      processor.processKey(mockEvent);

      asserter.assertTextState(2, "HAere are four words");
    }

    @Test
    public void canProcessRightArrowAndBeingSelection()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_RIGHT);

      processor.processKey(mockEvent);

      asserter.assertSelection(2, 1, true);
    }

    @Test
    public void canProcessLeftArrowAndBeginSelection()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_LEFT);

      processor.processKey(mockEvent);

      asserter.assertSelection(0, 1, true);
    }
  }

  public static class ShiftCmdKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      standardSetUp();
      processor = new ShiftCmdKeyProcessor(boxInfo);
      modifier = 5;
    }

    @Test
    public void canProcessRightArrowAndSelectToTheRightEdge()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_RIGHT);

      processor.processKey(mockEvent);

      asserter.assertSelection(boxInfo.getText().length(), 1, true);
    }

    @Test
    public void canProcessLeftArrowAndSelectToTheLeftEdge()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_LEFT);

      processor.processKey(mockEvent);

      asserter.assertSelection(0, 1, true);
    }
  }

  public static class AltKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      standardSetUp();
      processor = new AltKeyProcessor(boxInfo);
      modifier = 8;
    }

    @Test
    public void canProcessSpecialKeys()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_A,'å');

      processor.processKey(mockEvent);

      asserter.assertTextState(2,"Håere are four words");
    }

    @Test
    public void canProcessRightArrowAndJumpToTheNextWord()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_RIGHT);

      processor.processKey(mockEvent);

      assertEquals(5, boxInfo.getCursorIndex());
    }

    @Test
    public void canProcessRightArrowAndSkipOverExtraSpacesToNextWord()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_RIGHT);
      boxInfo.setText("Here are    many  spaces");
      boxInfo.setCursorIndex(5);

      processor.processKey(mockEvent);

      assertEquals(12, boxInfo.getCursorIndex());
    }

    @Test
    public void  canProcessRightArrowAndJumpToTheNextWordInATinyString()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_RIGHT);
      boxInfo.setText("H s");
      boxInfo.setCursorIndex(0);

      processor.processKey(mockEvent);

      assertEquals(2, boxInfo.getCursorIndex());
    }

    @Test
    public void canProcessLeftArrowAndJumpToThePreviousWord()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_LEFT);
      boxInfo.setCursorIndex(9);

      processor.processKey(mockEvent);

      assertEquals(5, boxInfo.getCursorIndex());
    }

    @Test
    public void canProcessLeftArrowAndSkipOverExtraSpacesToPreviousWord()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_LEFT);
      boxInfo.setText("Here are    many  spaces");
      boxInfo.setCursorIndex(12);

      processor.processKey(mockEvent);

      assertEquals(5, boxInfo.getCursorIndex());
    }
  }

  public static class AltCmdKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      standardSetUp();
      processor = new AltCmdKeyProcessor(boxInfo);
      modifier = 12;
    }

    @Test
    public void canProcessRightArrowAndNothingHappens()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_RIGHT);

      processor.processKey(mockEvent);

      asserter.assertSelection(1, 0, false);
    }

    @Test
    public void canProcessCharacterAndNothingHappens()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_A);

      processor.processKey(mockEvent);

      asserter.assertTextState(1, "Here are four words");
    }
  }

  public static class AltShiftKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      standardSetUp();
      processor = new AltShiftKeyProcessor(boxInfo);
      modifier = 9;
    }

    @Test
    public void canProcessSpecialKeys()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_A,'Å');

      processor.processKey(mockEvent);

      asserter.assertTextState(2,"HÅere are four words");
    }

    @Test
    public void canProcessRightArrowAndSelectToTheNextWord()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_RIGHT);

      processor.processKey(mockEvent);

      asserter.assertSelection(5, 1, true);
    }

    @Test
    public void canProcessLeftArrowAndSelectToThePreviousWord()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_LEFT);
      boxInfo.setCursorIndex(9);

      processor.processKey(mockEvent);

      asserter.assertSelection(5, 9, true);
    }
  }

  public static class AltShiftCmdKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      standardSetUp();
      processor = new AltShiftCmdKeyProcessor(boxInfo);
      modifier = 13;
    }

    @Test
    public void canProcessRightArrowAndNothingHappens()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_RIGHT);

      processor.processKey(mockEvent);

      asserter.assertSelection(1, 0, false);
    }

    @Test
    public void canProcessCharacterAndNothingHappens()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_A);

      processor.processKey(mockEvent);

      asserter.assertTextState(1, "Here are four words");
    }
  }

  public static class SelectionKeyProcessorTest
  {

    @Before
    public void setUp()
    {
      selectionSetUp();
      processor = new SelectionOnKeyProcessor(boxInfo);
      modifier =0;
    }


    @Test
    public void canProcessCharactersAndReplaceSelection()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_A,'a');

      processor.processKey(mockEvent);

      asserter.assertTextState(2, "Ha are four words");
    }

    @Test
    public void canProcessBackSpaceAndDeleteSelection()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_BACK_SPACE);

      processor.processKey(mockEvent);

      asserter.assertTextState(1, "H are four words");
    }

    @Test
    public void canProcessRightArrowAndEndSelection()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_RIGHT);

      processor.processKey(mockEvent);

      asserter.assertSelection(2, SELECTION_START_INDEX, false);
    }

    @Test
    public void canProcessLeftArrowAndEndSelection()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_LEFT);

      processor.processKey(mockEvent);

      asserter.assertSelection(0, SELECTION_START_INDEX, false);
    }
  }

  public static class SelectionCmdKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      selectionSetUp();
      processor = new SelectionOnCmdKeyProcessor(boxInfo);
      modifier = 4;
    }

    @Test
    public void canSelectAll()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_A);

      processor.processKey(mockEvent);

      asserter.assertSelection(boxInfo.getText().length(), 0, true);
    }

    @Test
    public void canPasteAtCursor()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_V);
      boxInfo.copyText("oot");

      processor.processKey(mockEvent);

      asserter.assertTextState(SELECTION_START_INDEX, "Hoot are four words");
      asserter.assertSelection(SELECTION_START_INDEX, 0, false);
    }

    @Test
    public void canCopySelectedText()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_C);

      processor.processKey(mockEvent);

      asserter.assertTextState(1, "Here are four words");
      asserter.assertSelection(1, SELECTION_START_INDEX, true);
      assertEquals("ere", boxInfo.getClipboardContents());
    }

    @Test
    public void canCutSelectedText()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_X);

      processor.processKey(mockEvent);

      asserter.assertTextState(1, "H are four words");
      asserter.assertSelection(1, 0, false);
      assertEquals("ere", boxInfo.getClipboardContents());
    }

    @Test
    public void canProcessRightArrowAndJumpToRightEdgeAndEndSelection()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_RIGHT);

      processor.processKey(mockEvent);

      asserter.assertSelection(boxInfo.getText().length(), SELECTION_START_INDEX, false);
    }

    @Test
    public void canProcessLeftArrowAndJumpToLeftEdgeAndEndSelection()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_LEFT);

      processor.processKey(mockEvent);

      asserter.assertSelection(0, SELECTION_START_INDEX, false);
    }

  }

  public static class SelectionShiftKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      selectionSetUp();
      processor = new SelectionOnShiftKeyProcessor(boxInfo);
      modifier = 1;
    }

    @Test
    public void canProcessRightArrowAndContinueSelectionToTheRight()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_RIGHT);

      processor.processKey(mockEvent);

      asserter.assertSelection(2, SELECTION_START_INDEX, true);
    }

    @Test
    public void canProcessLeftArrowAndContinueSelectionToTheLeft()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_LEFT);

      processor.processKey(mockEvent);

      asserter.assertSelection(0, SELECTION_START_INDEX, true);
    }

    @Test
    public void canProcessCharacterAndPlaceUppercaseCharAtCursorIndex()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_A,'A');

      processor.processKey(mockEvent);

      asserter.assertTextState(2, "HA are four words");
    }
  }

  public static class SelectionShiftCmdKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      selectionSetUp();
      processor = new SelectionOnShiftCmdKeyProcessor(boxInfo);
      modifier = 5;
    }

    @Test
    public void canProcessRightArrowAndContinueSelectionToTheRightEdge()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_RIGHT);

      processor.processKey(mockEvent);

      asserter.assertSelection(boxInfo.getText().length(), SELECTION_START_INDEX, true);
    }

    @Test
    public void canProcessLeftArrowAndContinueSelectionToTheLeftEdge()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_LEFT);
      boxInfo.setCursorIndex(2);

      processor.processKey(mockEvent);

      asserter.assertSelection(0, SELECTION_START_INDEX, true);
    }
  }

  public static class SelectionAltKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      selectionSetUp();
      processor = new SelectionOnAltKeyProcessor(boxInfo);
      modifier = 8;
    }

    @Test
    public void canProcessSpecialKeys()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_A,'å');

      processor.processKey(mockEvent);

      asserter.assertTextState(2,"Hå are four words");
    }

    @Test
    public void canProcessRightArrowAndMoveToStartOfNextWord()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_RIGHT);

      processor.processKey(mockEvent);

      asserter.assertSelection(5, SELECTION_START_INDEX, false);
    }

    @Test
    public void canProcessLeftArrowAndMoveToEndOfPreviousWord()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_LEFT);
      boxInfo.setCursorIndex(9);

      processor.processKey(mockEvent);

      asserter.assertSelection(5, 4, false);
    }
  }

  public static class SelectionAltCmdKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      selectionSetUp();
      processor = new SelectionOnAltCmdKeyProcessor(boxInfo);
      modifier = 12;
    }


    @Test
    public void canProcessCharacterAndDoNothing()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_A);

      processor.processKey(mockEvent);

      asserter.assertSelection(1, SELECTION_START_INDEX, true);
    }
  }

  public static class SelectionAltShiftKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      selectionSetUp();
      processor = new SelectionOnAltShiftKeyProcessor(boxInfo);
      modifier = 9;
    }

    @Test
    public void canProcessSpecialKeys()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_A,'Å');

      processor.processKey(mockEvent);

      asserter.assertTextState(2,"HÅ are four words");
    }

    @Test
    public void canProcessRightArrowAndSelectToStartOfNextWord()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_RIGHT);

      processor.processKey(mockEvent);

      asserter.assertSelection(5, SELECTION_START_INDEX, true);
    }

    @Test
    public void canProcessLeftArrowAndSelectToEndOfPreviousWord()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_LEFT);
      boxInfo.setCursorIndex(9);

      processor.processKey(mockEvent);

      asserter.assertSelection(5, 4, true);
    }
  }

  public static class SelectionAltShiftCmdKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      selectionSetUp();
      processor = new SelectionOnAltShiftCmdKeyProcessor(boxInfo);
      modifier = 13;
    }


    @Test
    public void canProcessCharacterAndDoNothing()
    {
      mockEvent = new MockKeyEvent(modifier,KeyEvent.VK_A);
      
      processor.processKey(mockEvent);

      asserter.assertSelection(1, SELECTION_START_INDEX, true);
    }
  }

}
