;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.theater
  (:use [limelight.clojure.stage :only (new-stage)]))

(deftype Theater [peer production]
  limelight.model.api.TheaterProxy
  (buildStage [this name options]
    @(.peer (new-stage this name options))))
