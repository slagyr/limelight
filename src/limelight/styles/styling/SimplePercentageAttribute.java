//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.PercentageAttribute;

import java.text.DecimalFormat;


public class SimplePercentageAttribute implements PercentageAttribute
{
  public static final DecimalFormat format = new DecimalFormat("0.##");

  private final double percentage;

  public SimplePercentageAttribute(double percentage)
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
    if(obj instanceof SimplePercentageAttribute)
    {
      double otherPercentage = ((SimplePercentageAttribute) obj).percentage;
      return Math.abs(percentage - otherPercentage) < 0.01;
    }
    return false;
  }
}
