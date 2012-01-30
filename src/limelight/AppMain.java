//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight;

import javax.swing.*;
import java.io.*;

public class AppMain
{
  private static String startupProductionPath;

  public static void main(String[] args) throws Exception
  {
    new AppMain().start(args);
  }

  public void start(String[] args) throws Exception
  {
    try
    {
      Boot.boot("log-to-file", true);

      if(args.length > 0 && startupProductionPath == null)
        startupProductionPath = args[0];

      Context.instance().os.openProduction(getStartupProductionPath());
    }
    catch(Throwable e)
    {
      handleError(e);
    }
  }

  public static void handleError(Throwable e)
  {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(byteArrayOutputStream);
    e.printStackTrace(writer);
    writer.flush();
    JOptionPane.showMessageDialog(new JFrame(), new String(byteArrayOutputStream.toByteArray()), "Limelight Error", JOptionPane.WARNING_MESSAGE);
  }

  private String getStartupProductionPath()
  {
    String productionName = Context.instance().limelightHome + "/productions/playbills.lll";
    if(productionProvided())
      productionName = startupProductionPath;
    return productionName;
  }

  private boolean productionProvided()
  {
    return startupProductionPath != null;
  }

  public static void setStartupPath(String productionPath)
  {
    startupProductionPath = productionPath;
  }
}