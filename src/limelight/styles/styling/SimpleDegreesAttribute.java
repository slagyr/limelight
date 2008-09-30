package limelight.styles.styling;

public class SimpleDegreesAttribute extends SimpleIntegerAttribute
{
  public SimpleDegreesAttribute(int degrees)
  {
    super(degrees);
  }

  public int getDegrees()
  {
    return getValue();
  }
}
