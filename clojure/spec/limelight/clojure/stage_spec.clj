;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.stage-spec
  (:use
    [speclj.core]
    [limelight.clojure.spec-helper]
    [limelight.clojure.stage]
    [limelight.clojure.core :only (theater)]))

(describe "Stage"
  (it "has limelight lineage"
    (should (isa? limelight.clojure.stage.Stage limelight.model.api.StageProxy)))

  (unless-headless

    (it "can be created with all the right values"
      (let [stage (new-stage :theater "Bill" {})]
        (should= limelight.ui.model.FramedStage (type @(._peer stage)))
        (should= stage (.getProxy @(._peer stage)))
        (should= "Bill" (.getName @(._peer stage)))
        (should= :theater (._theater stage))))

    (it "uses the options map passed in constructor function"
      (let [stage (new-stage :theater "George" (hash-map :title "Dr. Mario"))]
        (should= "George" (.getName @(._peer stage)))
        (should= "Dr. Mario" (.getTitle @(._peer stage)))))
    )
  )
