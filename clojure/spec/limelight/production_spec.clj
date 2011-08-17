(ns limelight.production-spec
  (:use
    [speclj.core]
    [limelight.production])
  (:import
    [limelight.production Production]
    [limelight.casting CastingDirector]))

(defn actions-for [production event-class]
  (-> production
    (.peer)
    (.getEventHandler)
    (.getActions event-class)))

(describe "Production"

  (it "has limelight lineage"
    (should (isa? Production limelight.model.api.ProductionProxy)))

  (it "can be contructed"
    (let [production (Production. :peer :theater nil)]
      (should= :peer (.peer production))
      (should= :theater (.theater production))))

  (context ", when fully constructed,"
    (with peer-production (limelight.model.FakeProduction. "Mock"))
    (with production (new-production @peer-production))
    (with fs (limelight.io.FakeFileSystem/installed))

    (it "connects with the peer production"
      (should= @peer-production (.peer @production))
      (should= @production (.getProxy (.peer @production))))

    (it "creates a theater"
      (should= limelight.theater.Theater (type @(.theater @production)))
      (should= (.getTheater @peer-production) (.peer @(.theater @production)))
      (should= @production (.production @(.theater @production))))

    (it "can load stages"
      (.createTextFile @fs "/Mock/stages.clj" "(stage \"One\")")
      (.loadStages @production)
      (should= 1 (count (.getStages (.getTheater @peer-production)))))
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
        (should-not-throw (.dispatch event (.peer @production)))
        (should= (keyword name) @(ns-resolve (.ns @production) '*message*))))
    )
  )

(run-specs)