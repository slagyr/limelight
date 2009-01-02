require 'rexml/document'
require 'uri'
require 'net/http'

module Limelight
  module Client

    class Playbills

      def self.from_xml(xml, uri=nil)
        playbills = []
        doc = REXML::Document.new(xml)
        doc.root.each_element do |playbill_element|
          playbills << Playbill.from_element(playbill_element, uri)
        end
        return playbills
      end

      def self.from_url(url)
        uri = URI.parse(url)
        response = Net::HTTP.get_response(uri)
        return from_xml(response.body, uri)
      end

    end

    class Playbill

      def self.from_element(element, uri=nil)
        playbill = Playbill.new(uri)
        playbill.populate_from_element(element)
        return playbill
      end

      def populate_from_element(element)
        element.each_element do |element|
          name = element.name.gsub("-", "_")
          setter_sym = "#{name}=".to_sym
          self.send(setter_sym, coerce_value(element))
        end
      end

      attr_accessor :author, :created_at, :description, :id, :name, :size, :title, :updated_at, :version, :llp_path, :thumbnail_path
      attr_reader :uri

      def initialize(uri=nil)
        @uri = uri
      end

      def thumbnail
        return pull(thumbnail_path)
      end

      def llp
        return pull(llp_path)
      end

      private #############################################

      def coerce_value(element)
        value = element.text
        type_attribute = element.attribute("type")
        return value if type_attribute.nil?
        case type_attribute.value
        when "datetime"
          value = DateTime.parse(value)
        when "integer"
          value = value.to_i
        end
        return value
      end

      def pull(path)
        new_uri = uri.dup
        new_uri.path = path
        response = Net::HTTP.get_response(new_uri)
        return response.body
      end

    end

  end
end