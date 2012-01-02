;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.production-spec
  (:use
    [speclj.core]
    [limelight.clojure.spec-helper]
    [limelight.clojure.production])
  (:import
    [limelight.clojure.production Production]
    [limelight.clojure.theater Theater]))

(defn actions-for [production event-class]
  (-> production
    (._peer)
    (.getEventHandler)
    (.getActions event-class)))

(describe "Production"

  (it "has limelight lineage"
    (should (isa? Production limelight.model.api.ProductionProxy)))

  (it "can be contructed"
    (let [production (Production. :peer :theater nil)]
      (should= :peer (._peer production))
      (should= :theater (._theater production))))

  (context ", when fully constructed,"
    (with peer-production (limelight.model.FakeProduction. "Mock"))
    (with production (new-production @peer-production))
    (with fs (limelight.io.FakeFileSystem/installed))

    (it "connects with the peer production"
      (should= @peer-production (._peer @production))
      (should= @production (.getProxy (._peer @production))))

    (it "creates a theater"
      (should= limelight.clojure.theater.Theater (type @(._theater @production)))
      (should= (.getTheater @peer-production) (._peer @(._theater @production)))
      (should= @production (._production @(._theater @production))))

    (unless-headless

      (it "can load stages"
        (.createTextFile @fs "/Mock/stages.clj" "(stage \"One\")")
        (.loadStages @production)
        (should= 1 (count (.getStages (.getTheater @peer-production)))))
      )
    )

  (context ", when illuminated,"
    (with peer-production (limelight.model.FakeProduction. "MockProduction"))
    (with production (new-production @peer-production))
    (with fs (limelight.io.FakeFileSystem/installed))

    (for [[name event] {
      "production-created" (limelight.model.events.ProductionCreatedEvent.)
      "production-loaded" (limelight.model.events.ProductionLoadedEvent.)
      "production-opened" (limelight.model.events.ProductionOpenedEvent.)
      "production-closing" (limelight.model.events.ProductionClosingEvent.)
      "production-closed" (limelight.model.events.ProductionClosedEvent.)
      }]
      (it (str "supports the " name " event")
        (.createTextFile @fs "/MockProduction/production.clj" (str "(on-" name " [e] (def *message* :" name "))"))
        (.illuminate @production)
        (should= 1 (count (actions-for @production (class event))))
        (should-not-throw (.dispatch event (._peer @production)))
        (should= (keyword name) @(ns-resolve (._ns @production) '*message*))))
    )
  )

(run-specs)