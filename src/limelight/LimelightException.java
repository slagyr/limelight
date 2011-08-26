//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight;

public class LimelightException extends RuntimeException
{
  public LimelightException(String message)
  {
    super(message);
  }

  public LimelightException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public LimelightException(Exception cause)
  {
    super(cause);
  }
}
