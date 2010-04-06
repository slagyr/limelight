#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/commands/command'

module Limelight
  module Commands

    # See the following usage summary.
    #
    #  Usage: limelight unpack <production_path>
    #      Unpack a limelight production into the tmp directory.
    #      options:
    #      -h, --help                       Prints this usage summary.
    #
    class UnpackCommand < Command

      install_as "unpack"

      def self.description #:nodoc:
        return "Unpack a limelight production into the tmp directory."
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
        Main.initialize_temp_directory
        packer = Limelight::Util::Packer.new
        location = packer.unpack(@production_path)
        puts "Production was unpacked to: #{location}"
      end

    end
  end
end