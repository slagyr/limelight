;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.theater
  (:use
    [limelight.clojure.stage :only (new-stage)]))

(deftype Theater [_peer _production]
  limelight.model.api.TheaterProxy
  (buildStage [this name options] @(._peer (new-stage this name options)))

  limelight.clojure.core.ProductionSource
  (production [this] _production)
  )

(defn new-theater [peer production]
  (let [theater (Theater. peer production)]
    (.setProxy peer theater)
    theater))
