#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/commands/command'

module Limelight
  module Commands

    # See the following usage summary.
    #
    #  Usage: limelight pack <production_path>
    #      Pack a limelight production into a .llp file.
    #      options:
    #      -h, --help                       Prints this usage summary.
    #
    class PackCommand < Command

      install_as "pack"

      def self.description #:nodoc:
        return "Pack a limelight production into a .llp file."
      end

      protected ###########################################

      def parameter_description #:nodoc:
        return "<production_path>"
      end

      def parse_remainder(args) #:nodoc:
        @production_path = args.shift
        raise "Missing production path" if @production_path.nil?
      end

      def process #:nodoc:
        packer = Limelight::Util::Packer.new
        packer.pack(@production_path)
      end

    end

  end


end
