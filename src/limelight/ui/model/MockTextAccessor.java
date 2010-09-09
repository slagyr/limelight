package limelight.ui.model;

public class MockTextAccessor implements TextAccessor
{
  public boolean markAsDirtyCalled;
  public boolean markAsNeedingLayoutCalled;

  public void setText(String text, PropablePanel panel)
  {
  }

  public String getText()
  {
    return null;
  }

  public void markAsDirty()
  {
    markAsDirtyCalled = true;
  }

  public void markAsNeedingLayout()
  {
    markAsNeedingLayoutCalled = true;
  }

  public boolean hasFocus()
  {
    return false;
  }
}
