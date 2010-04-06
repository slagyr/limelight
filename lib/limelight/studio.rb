#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

raise "studio.rb is present for solely to document it's Java counterpart limelight.Studio.  This file should NOT be loaded in the Ruby runtime."

module Limelight

  # A Studio creates Productions.  There is only one instance of Studio per Limelight runtime.  All open productions
  # are opened and tracked by the studio.
  #
  class Studio

    # Opens the production at the specified path.
    #
    def open(production_path)
    end

    # Returns the production with the specified name. If the studio never opened a production by that name, nil
    # will be returned.
    #
    def get(name)
    end

    # Returns true if all of the open productions allow closing.
    #
    def should_allow_shutdown
    end

    # If allowed (should_allow_shutdown), this will close all open productions and shutdown the limelight runtime.
    #
    def shutdown
    end

    # Called when a production is closed to notify the studio of the event. Developers should not need to call this method.
    #
    def production_closed(production)
    end

    # Returns a list of all the productions
    #
    def productions
    end

    # Returns the utilities production; a production used by limelight.
    #
    def utilities_production
    end

    # Returns true if the Studio has been shutdown.  ie. The procees is exiting.
    #
    def shutdown?
    end
  end

end