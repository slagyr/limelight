package limelight.styles;

public abstract class StyleAttributeCompiler
{
  private String name;

  public abstract StyleAttribute compile(String value);

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }


  public InvalidStyleAttributeError makeError(String invalidValue)
  {
    return new InvalidStyleAttributeError("Invalid value '" + invalidValue + "' for " + name + " style attribute.");
  }
}
