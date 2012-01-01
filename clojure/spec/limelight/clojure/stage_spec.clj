;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.stage-spec
  (:use
    [speclj.core]
    [limelight.clojure.spec-helper]
    [limelight.clojure.stage])
  (:import [limelight.clojure.stage Stage]))

(describe "Stage"
  (it "has limelight lineage"
    (should (isa? Stage limelight.model.api.StageProxy)))

  (unless-headless

    (it "can be created with all the right values"
      (let [stage (new-stage :theater "Bill" {})]
        (should= limelight.ui.model.FramedStage (type @(.peer stage)))
        (should= stage (.getProxy @(.peer stage)))
        (should= "Bill" (.getName @(.peer stage)))
        (should= :theater (.theater stage))))

    (it "uses the options map passed in constructor function"
      (let [stage (new-stage :theater "George" (hash-map :title "Dr. Mario"))]
        (should= "George" (.getName @(.peer stage)))
        (should= "Dr. Mario" (.getTitle @(.peer stage)))))
    )
  )
