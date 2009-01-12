require 'limelight/data'
require 'limelight/limelight_exception'
require 'uri'
require 'ftools'
require 'net/http'
require 'net/https'

module Limelight
  module Util

    class Downloader

      def initialize
        Data.establish_data_dirs
      end

      def download(resource)
        uri = URI.parse(resource)
        case uri.scheme
        when nil, "file"
          download_file(uri)
        when 'http', 'https'
          download_http(uri)
        else
          raise LimelightException.new("Download failed.  Unhandled URI scheme: #{uri.scheme}")
        end
      end

      def download_file(uri)
        raise LimelightException.new("Download failed.  Not found: #{uri.path}") if !File.exists?(uri.path)
        filename = File.basename(uri.path)
        destination = find_unused_downloads_filename(filename)
        File.copy(uri.path, destination)
        return destination
      end

      def download_http(uri)
        filename = File.basename(uri.path)
        destination = find_unused_downloads_filename(filename)

        file = File.open(destination, 'w')
        begin
          success = true
          copy_from_http(uri, file)
        rescue Exception => e
          success = false
          raise LimelightException.new(e.to_s)
        ensure
          file.close
          File.delete(destination) unless success
        end

        return destination
      end


      private #############################################

      def find_unused_downloads_filename(filename)
        default = File.join(Data.downloads_dir, filename)
        attempt = default
        prefix = 1
        while (File.exists?(attempt))
          attempt = "#{default}.#{postfix}"
          prefix += 1
        end
        return attempt
      end

      def copy_from_http(uri, file)
        http = Net::HTTP.new(uri.host, uri.port)
        http.use_ssl = true if uri.port.to_s == "443"
        http.request_get(uri.path) do |response|
          raise "Download failed.  #{response.code}: #{response.message}" if response.code.to_s != "200"
          response.read_body do |segment|
            file.write(segment)
          end
        end
      end

    end

  end
end