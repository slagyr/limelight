#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/data'
require 'limelight/limelight_exception'
require 'uri'
require 'ftools'
require 'net/http'
require 'net/https'

module Limelight
  module Util

    class Downloader

      def self.download(resource)
        new.download(resource)
      end

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
        saved_filename = nil

        begin
          saved_filename = attempt_http_download(uri)
        rescue Exception => e
          raise LimelightException.new(e)
        end

        return saved_filename
      end


      private #############################################

      def find_unused_downloads_filename(filename)
        default = File.join(Data.downloads_dir, filename)
        extension = File.extname(default)
        filename_without_extension = default[0...(-1 * extension.length)]
        attempt = default
        postfix = 2
        while (File.exists?(attempt))
          attempt = "#{filename_without_extension}_#{postfix}#{extension}"
          postfix += 1
        end
        return attempt
      end

      def calculate_filename(uri, response)
        content_disposition = response['content-disposition']
        if content_disposition
          match = /filename="(.*)"/.match(content_disposition)
          return match[1] if match
        end
        return File.basename(uri.path)
      end

      def save_to_file(uri, response)
        filename = calculate_filename(uri, response)
        destination = find_unused_downloads_filename(filename)
        file = File.open(destination, 'w')

        begin
          response.read_body do |segment|
            file.write(segment)
          end
        ensure
          file.close
        end

        return destination
      end

      def attempt_http_download(uri, attempts = 5)
        http = Net::HTTP.new(uri.host, uri.port)
        http.use_ssl = true if uri.port.to_s == "443"
        http.request_get(uri.path) do |response|
          case response
          when Net::HTTPSuccess
            return save_to_file(uri, response)
          when Net::HTTPRedirection
            return attempt_http_download(URI.parse(response['location']), attempts - 1)
          else
            raise LimelightException.new("Download failed.  #{response.code}: #{response.message}")
          end
        end
      end

    end

  end
end