package limelight.ui.model.inputs;

import java.awt.event.KeyEvent;

public abstract class KeyProcessor
{
   protected TextModel boxInfo;

  public KeyProcessor(TextModel boxInfo){
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
    for (int i = startIndex;i <= boxInfo.getText().length() -1;i++)
    {
      if(boxInfo.getText().charAt(i-1) == ' ' && boxInfo.getText().charAt(i) != ' ')
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
}

