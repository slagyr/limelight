package limelight;

public class MockBlock implements Block
{
  public FlatStyle style;
  public String text;
  public String name;

  public MockBlock()
  {
    style = new FlatStyle();
  }

  public Panel getPanel()
  {
    return null;
  }

  public FlatStyle getStyle()
  {
    return style;
  }

  public String getName()
  {
    return name;
  }

  public String getText()
  {
    return text;
  }

  public void setText(String value)
  {
    text = value;
  }

  public void mouseClicked()
  {
  }

  public void hoverOn()
  {
  }

  public void mouseEntered()
  {
  }

  public void mouseExited()
  {
  }

  public void hoverOff()
  {
  }
}
