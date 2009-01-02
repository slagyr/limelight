require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/client/playbills'

describe Limelight::Client::Playbills do

  before(:each) do
    @xml = <<END
<?xml version="1.0" encoding="UTF-8"?>
<playbills type="array">
  <playbill>
    <author>Jim Weirich and Micah Martin</author>
    <created-at type="datetime">2009-01-02T17:07:26Z</created-at>
    <description>The classic game of memory.</description>
    <id type="integer">1</id>
    <name>simon</name>
    <size>270418</size>
    <title>Limelight Simon</title>
    <updated-at type="datetime">2009-01-02T17:07:26Z</updated-at>
    <version>1.0</version>
    <llp-path>/playbills/1.llp</llp-path>
    <thumbnail-path>/playbills/1.thumbnail</thumbnail-path>
  </playbill>
</playbills>
END

    @response = mock("response")
  end

  it "should translate xml with 1 playbill" do
    playbills = Limelight::Client::Playbills.from_xml(@xml)
    playbills.length.should == 1

    simon = playbills[0]
    simon.name.should == "simon"
    simon.title.should == "Limelight Simon"
    simon.size.should == "270418"
    simon.updated_at.year.should == 2009
    simon.created_at.year.should == 2009
    simon.thumbnail_path.should == "/playbills/1.thumbnail"
    simon.llp_path.should == "/playbills/1.llp"
    simon.author.should == "Jim Weirich and Micah Martin"
    simon.description.should == "The classic game of memory."
  end

  it "should get all the playbills from a URL" do
    Net::HTTP.should_receive(:get_response).with(URI.parse("http://localhost:3000/playbills.xml")).and_return(@response)
    @response.should_receive(:body).and_return(@xml)

    playbills = Limelight::Client::Playbills.from_url("http://localhost:3000/playbills.xml")

    playbills.length.should == 1
    playbills[0].uri.host.should == "localhost"
    playbills[0].uri.port.should == 3000
  end

  it "should get the thumbnail for a playbill" do
    playbill = Limelight::Client::Playbill.new(URI.parse("http://localhost:3000/playbills.xml"))
    playbill.thumbnail_path = "/playbills/1.thumbnail"

    Net::HTTP.should_receive(:get_response).with(URI.parse("http://localhost:3000/playbills/1.thumbnail")).and_return(@response)
    @response.should_receive(:body).and_return("blah")

    playbill.thumbnail.should == "blah"
  end

  it "should get the thumbnail for a playbill" do
    playbill = Limelight::Client::Playbill.new(URI.parse("http://localhost:3000/playbills.xml"))
    playbill.llp_path = "/playbills/1.llp"

    Net::HTTP.should_receive(:get_response).with(URI.parse("http://localhost:3000/playbills/1.llp")).and_return(@response)
    @response.should_receive(:body).and_return("blah")

    playbill.llp.should == "blah"
  end

end