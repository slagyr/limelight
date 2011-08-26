//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

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
