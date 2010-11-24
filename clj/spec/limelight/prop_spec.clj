(ns limelight.prop-spec
  (:use
    [speclj.core]
    [limelight.prop])
  (:import
    [limelight.prop Prop]))

(describe "Prop"

  (it "has limelight lineage"
    (should (isa? Prop limelight.model.api.PropProxy)))

  (it "can be created"
    (let [prop (new-prop (hash-map :name "Bill"))]
      (should-not= nil @(.peer prop))
      (should= limelight.ui.model.PropPanel (type @(.peer prop)))))
  )




