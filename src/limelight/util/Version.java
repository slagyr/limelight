//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

public class Version
{
  private int major;
  private int minor;
  private int patch;

  public Version(String versionString)
  {
    try
    {
      String[] bits = versionString.split("\\.");
      if(bits.length > 3)
        throw new Exception("too many numbers");
      major = Integer.parseInt(bits[0]);
      minor = Integer.parseInt(bits[1]);
      patch = Integer.parseInt(bits[2]);
    }
    catch(Exception e)
    {
      throw new RuntimeException("Could not parse version number: " + versionString);
    }
  }

  public int getMajor()
  {
    return major;
  }

  public int getMinor()
  {
    return minor;
  }

  public int getPatch()
  {
    return patch;
  }

  public boolean equals(Object obj)
  {
    if(!(obj instanceof Version))
      return false;

    Version that = (Version) obj;
    return this.major == that.major && this.minor == that.minor && this.patch == that.patch;
  }

  public boolean isLessThan(Version that)
  {
    if(this.major < that.major)
      return true;
    else if(this.major == that.major && this.minor < that.minor)
      return true;
    else if(this.minor == that.minor && this.patch < that.patch)
      return true;
    else
      return false;
  }

  public boolean isLessThanOrEqual(Version that)
  {
    return this.isLessThan(that) || this.equals(that);
  }

  public boolean isGreaterThan(Version that)
  {
    if(this.major > that.major)
      return true;
    else if(this.major == that.major && this.minor > that.minor)
      return true;
    else if(this.minor == that.minor && this.patch > that.patch)
      return true;
    else
      return false;
  }

  public boolean isGreaterThanOrEqual(Version that)
  {
    return this.isGreaterThan(that) || this.equals(that);
  }
}
