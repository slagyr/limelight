//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

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
}
