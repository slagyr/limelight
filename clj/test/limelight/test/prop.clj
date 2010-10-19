(ns limelight.test.prop
  (:use
    [limelight.prop]
    [lazytest.describe :only (describe testing it with given)]
    [lazytest.context :only (fn-context)]))

(describe "Prop"
  (testing "lineage"
    (it "implements the API" (some #{limelight.model.api.PropProxy} (supers limelight.prop.Prop))))

  (testing "creation"
    (given [prop (new-prop (hash-map :name "Bill"))]
      (it "has a peer" (not (nil? @(.peer prop))))
      (it "peer is PropPanel" (= limelight.ui.model.PropPanel (type @(.peer prop)))))))


