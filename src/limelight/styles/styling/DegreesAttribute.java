package limelight.styles.styling;

public class DegreesAttribute extends IntegerAttribute
{
  public DegreesAttribute(int degrees)
  {
    super(degrees);
  }

  public int getDegrees()
  {
    return getValue();
  }
}
