package limelight.ui.model.inputs;

import java.awt.event.KeyEvent;
import java.awt.font.TextHitInfo;

public abstract class KeyProcessor
{
  protected TextModel boxInfo;

  public KeyProcessor(TextModel boxInfo)
  {
    this.boxInfo = boxInfo;
  }

  public abstract void processKey(KeyEvent event);

  protected boolean isACharacter(int keyCode)
  {
    return (keyCode > 40 && keyCode < 100 || keyCode == 222 || keyCode == 32);
  }

  protected void insertCharIntoTextBox(char c)
  {
    boxInfo.text.insert(boxInfo.getCursorIndex(), c);
    boxInfo.setCursorIndex(boxInfo.getCursorIndex() + 1);
  }


  protected boolean isMoveRightEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_RIGHT && boxInfo.getCursorIndex() < boxInfo.getText().length();
  }

  protected boolean isMoveLeftEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_LEFT && boxInfo.getCursorIndex() > 0;
  }

  protected boolean isMoveUpEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_UP && boxInfo.getLineNumberOfIndex(boxInfo.cursorIndex) > 0;
  }

  protected boolean isMoveDownEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_DOWN && boxInfo.getLineNumberOfIndex(boxInfo.cursorIndex) < boxInfo.textLayouts.size() - 1;
  }

  protected void initSelection()
  {
    boxInfo.selectionOn = true;
    boxInfo.setSelectionIndex(boxInfo.getCursorIndex());
  }

  protected int findNearestWordToTheLeft()
  {
    return boxInfo.findWordsLeftEdge(boxInfo.getCursorIndex() - 1);
  }

  protected int findNearestWordToTheRight()
  {
    return findNextWordSkippingSpaces(boxInfo.findWordsRightEdge(boxInfo.getCursorIndex()));
  }

  private int findNextWordSkippingSpaces(int startIndex)
  {
    for (int i = startIndex; i <= boxInfo.getText().length() - 1; i++)
    {
      if (boxInfo.getText().charAt(i - 1) == ' ' && boxInfo.getText().charAt(i) != ' ')
        return i;
    }
    return boxInfo.getText().length();
  }

  protected void selectAll()
  {
    boxInfo.selectionOn = true;
    boxInfo.setCursorIndex(boxInfo.getText().length());
    boxInfo.setSelectionIndex(0);
  }


  protected void moveCursorUpALine()
  {
    if(boxInfo.getLastKeyPressed() == KeyEvent.VK_DOWN) {
      boxInfo.setCursorIndex(boxInfo.getLastCursorIndex());
      return;
    }
    int currentLine = boxInfo.getLineNumberOfIndex(boxInfo.cursorIndex);
    int charCount = 0;
    for(int i = 0; i < currentLine -1;i++)
      charCount += boxInfo.textLayouts.get(i).getText().length();
    int xPos = boxInfo.getXPosFromIndex(boxInfo.cursorIndex);
    TextHitInfo hitInfo = boxInfo.textLayouts.get(currentLine - 1).hitTestChar(xPos, 5);
    boxInfo.setCursorIndex(hitInfo.getCharIndex() + charCount);
  }

  protected void moveCursorDownALine()
  {
    if(boxInfo.getLastKeyPressed() == KeyEvent.VK_UP) {
      boxInfo.setCursorIndex(boxInfo.getLastCursorIndex());
      return;
    }
    int currentLine = boxInfo.getLineNumberOfIndex(boxInfo.cursorIndex);
    int charCount = 0;
    for(int i = 0; i <= currentLine;i++)
      charCount += boxInfo.textLayouts.get(i).getText().length();
    int xPos = boxInfo.getXPosFromIndex(boxInfo.cursorIndex);
    TextHitInfo hitInfo = boxInfo.textLayouts.get(currentLine + 1).hitTestChar(xPos, 5);
    boxInfo.setCursorIndex(hitInfo.getCharIndex() + charCount);
  }

  protected boolean isAnExtraKey(int keyCode)
  {
    return keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_TAB;
  }
}

