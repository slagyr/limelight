#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/commands/command'

module Limelight
  module Commands

    # Prints the current Limelight version
    #
    class VersionCommand < Command

      install_as "--version"

      def self.description #:nodoc:
        return "Prints the current Limelight version"
      end

      protected ###########################################

      def process #:nodoc:
        require 'limelight/version'
        puts "Limelight, version #{VERSION::STRING}"
      end

    end

  end


end
