//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.PercentageValue;

import java.text.DecimalFormat;


public class SimplePercentageValue implements PercentageValue
{
  public static final DecimalFormat format = new DecimalFormat("0.##");

  private final double percentage;

  public SimplePercentageValue(double percentage)
  {
    this.percentage = percentage;
  }

  public double getPercentage()
  {
    return percentage;
  }

  public String toString()
  {
    return format.format(percentage) + "%"; 
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof SimplePercentageValue)
    {
      double otherPercentage = ((SimplePercentageValue) obj).percentage;
      return Math.abs(percentage - otherPercentage) < 0.01;
    }
    return false;
  }
}
