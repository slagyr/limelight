package limelight.ui.model.inputs;

import limelight.Context;
import limelight.styles.Style;
import limelight.ui.model.BasePanel;
import limelight.ui.model.PropPanel;
import limelight.ui.model.StageFrame;
import limelight.ui.model.TextAccessor;
import limelight.util.Box;

import java.awt.datatransfer.ClipboardOwner;
import java.awt.event.FocusEvent;

public abstract class TextInputPanel extends BasePanel implements TextAccessor, InputPanel, ClipboardOwner

{
  protected TextModel boxState = new PlainTextModel(this);
  protected boolean focused;
  protected boolean cursorOn;
  protected Thread cursorThread;
  protected int cursorCycleTime = 500;
  protected boolean clickWasInBox;

  public void setParent(limelight.ui.Panel panel)
  {
    if (panel == null)
      Context.instance().keyboardFocusManager.focusFrame((StageFrame) getRoot().getStageFrame());
    super.setParent(panel);
    if (panel instanceof PropPanel)
    {
      PropPanel propPanel = (PropPanel) panel;
      propPanel.sterilize();
      propPanel.setTextAccessor(this);
    }
  }

  public Box getBoxInsidePadding()
  {
    return getBoundingBox();
  }

  public Box getChildConsumableArea()
  {
    return getBoundingBox();
  }

  public Style getStyle()
  {
    return getParent().getStyle();
  }

  public void setText(String text)
  {
    this.boxState.text = new StringBuffer(text);
    boxState.cursorIndex = text.length();
  }

  public String getText()
  {
    if (boxState.text == null)
      return null;
    return boxState.text.toString();
  }

    public void focusGained(FocusEvent e)
  {
    focused = true;
    markAsDirty();
    startCursor();
    super.focusGained(e);
  }

  public void focusLost(FocusEvent e)
  {
    focused = false;
    stopCursor();
    markAsDirty();
    super.focusLost(e);

  }

    private void startCursor()
  {
    if (cursorThread == null || !cursorThread.isAlive())
    {
      cursorThread = new Thread(new Runnable()
      {
        public void run()
        {
          while (focused)
          {
            cursorOn = !cursorOn;
            markAsDirty();

            try
            {
              Thread.sleep(cursorCycleTime);
            }
            catch (InterruptedException e)
            {
              e.printStackTrace();
            }
          }
        }
      });
      cursorThread.start();
    }
  }

  private void stopCursor()
  {
    cursorOn = false;
  }

}
