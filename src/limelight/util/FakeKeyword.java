package limelight.util;

public class FakeKeyword
{
  public String value;

  public FakeKeyword(String value)
  {
    this.value = value;
  }

  @Override
  public String toString()
  {
    return ":" + value;
  }
}
