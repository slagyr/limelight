;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.production-spec
  (:use
    [speclj.core]
    [limelight.clojure.spec-helper]
    [limelight.clojure.production]
    [limelight.clojure.core :only (children peer build backstage)]
    [limelight.clojure.util :only (->options)])
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
    (let [production (Production. :peer :theater nil nil)]
      (should= :peer (._peer production))
      (should= :theater (._theater production))))

  (context "when fully constructed,"
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

    (it "has a namespace"
      (should-not= nil (._ns @production))
      (should= true (.startsWith (.getName (.getName @(._ns @production))) "limelight.dynamic.production-")))

    (it "has a class loader"
      (.createTextFile @fs "/Mock/lib/foo.jar" "foo")
      (.createTextFile @fs "/Mock/lib/bar.jar" "bar")
      (let [loader (._loader @production)
            urls (seq (.getURLs loader))]
        (should-not= nil loader)
        (should= 3 (count urls))
        (should= "file:/Mock/src/" (str (first urls)))
        (should= "file:/Mock/lib/bar.jar" (str (second urls)))
        (should= "file:/Mock/lib/foo.jar" (str (nth urls 2)))))

    (unless-headless

      (it "can load stages"
        (.createTextFile @fs "/Mock/stages.clj" "(stage \"One\")")
        (.loadStages @production)
        (should= 1 (count (.getStages (.getTheater @peer-production)))))
      )

    (it "loads a scene"
      (.createTextFile @fs "/Mock/Scene/props.clj" "[:one]")
      (let [result (.loadScene @production "Scene" {"path" "Scene"})]
        (.illuminate (peer result))
        (should= 1 (count (children result)))
        (should= "one" (name (first (children result))))))

    (it "include prod path with loading scene"
      (.createTextFile @fs "/Mock/Scene/props.clj" "[:one]")
      (let [opts (atom nil)]
        (binding [build (fn [scene props-src & args] (reset! opts (->options args)))]
          (.loadScene @production "Scene" {"path" "Scene"}))
        (should= "Mock" (:root-path @opts))))

    (it "prop-params are used in prop-building when loading a scene"
      (.createTextFile @fs "/Mock/Scene/props.clj" "[(:pname *context*)]")
      (let [result (.loadScene @production "Scene" {"path" "Scene" :prop-params {:pname "foo"}})]
        (.illuminate (peer result))
        (should= 1 (count (children result)))
        (should= "foo" (name (first (children result))))))

    (it "backstage values are merged with prop-params and used in prop-building"
      (.createTextFile @fs "/Mock/Scene/props.clj" "[:p {:backstage *context*}]")
      (let [result (.loadScene @production "Scene" {"path" "Scene" :prop-params {:foo "bar" :fizz "bang"} :backstage {:foo "BAR" :qux "BAZ"}})]
        (.illuminate (peer result))
        (let [child (first (children result))]
          (should= "bar" (:foo (backstage child)))
          (should= "bang" (:fizz (backstage child)))
          (should= "BAZ" (:qux (backstage child))))))

    (for [[name event] {
      "production-created" (limelight.model.events.ProductionCreatedEvent.)
      "production-loaded" (limelight.model.events.ProductionLoadedEvent.)
      "production-opened" (limelight.model.events.ProductionOpenedEvent.)
      "production-closing" (limelight.model.events.ProductionClosingEvent.)
      "production-closed" (limelight.model.events.ProductionClosedEvent.)
      }]
      (it (str "supports the " name " event")
        (.createTextFile @fs "/Mock/production.clj" (str "(on-" name " [e] (def *message* :" name "))"))
        (.illuminate @production)
        (should= 1 (count (actions-for @production (class event))))
        (should-not-throw (.dispatch event (._peer @production)))
        (should= (keyword name) @(ns-resolve @(._ns @production) '*message*))))

    (it "the production events can use helper fns"
      (.createTextFile @fs "/Mock/production.clj" "(def red :red)")
      (.createTextFile @fs "/Mock/styles.clj" "[\"foo\" :background-color red]")
      (.illuminate @production)
      (let [peer-prod (peer @production)
            styles (.loadStyles @production peer-prod {})]
        (should= "#ff0000ff" (.getBackgroundColor (.get styles "foo")))))

    )
  )

(run-specs)