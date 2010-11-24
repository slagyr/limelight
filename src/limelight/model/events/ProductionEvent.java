//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model.events;

import limelight.events.Event;
import limelight.model.Production;

public class ProductionEvent extends Event
{
  private Production production;

  public void dispatch(Production production)
  {
    subject = this.production = production;
    production.getEventHandler().dispatch(this);
  }

  public Production getProduction()
  {
    return production;
  }
}
