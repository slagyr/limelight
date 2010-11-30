#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight

  class Main

    def self.run(args)
      Java::limelight.CmdLineMain.main(args)
    end

  end


end
