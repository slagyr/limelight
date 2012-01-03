;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.prop-building-spec
  (:use
    [speclj.core]
    [limelight.clojure.spec-helper]
    [limelight.clojure.core]
    [limelight.clojure.prop-building]
    [limelight.clojure.scene :only (new-scene)]
    [limelight.clojure.production :only (new-production)]))

(defn illuminate [scene]
  (.setProduction @(._peer scene) (._peer (new-production (limelight.model.FakeProduction. "Mock Production"))))
  (.illuminate @(._peer scene)))

(describe "prop-building"

  (before-all (boot-limelight))
  (with root (new-scene {}))

  (it "builds with no props"
    (let [scene (build-props @root "" "props.clj")]
      (should= 0 (count (children scene)))))

  (it "with one prop"
    (let [scene (build-props @root "[:one]" "props.clj")]
      (should= 1 (count (children scene)))))

  (it "with two prop"
    (let [scene (build-props @root "[:one][:two]" "props.clj")]
      (should= 2 (count (children scene)))))

  (it "nested props"
    (let [scene (build-props @root "[:one [:two]]" "props.clj")]
      (should= 1 (count (children scene)))
      (should= 1 (count (children (first (children scene)))))))

  (it "with options and a child prop"
    (let [scene (build-props @root "[:one {:text \"Number ONE!\"} [:two]]" "props.clj")]
      (should= 1 (count (children scene)))
      (should= 1 (count (children (first (children scene)))))))

  (it "with two children"
    (let [scene (build-props @root "[:one {:text \"Number ONE!\"} [:two] [:three]]" "props.clj")]
      (should= 2 (count (children (first (children scene)))))))

  (it "with illumination"
    (let [scene (build-props @root "[:one {:text \"Number ONE!\"} [:two]]" "props.clj")]
      (illuminate scene)
      (should= "one" (.getName @(._peer (first (children scene)))))
      (should= "Number ONE!" (.getText @(._peer (first (children scene)))))))

  (it "with dynamic code"
    (let [scene (build-props @root "[:one (for [name [:two :three]] [name])]" "props.clj")]
      (should= 2 (count (children (first (children scene)))))))

  (it "adds the props in the right order"
    (let [scene (build-props @root "[:one][:two][:three]" "props.clj")]
      (illuminate scene)
      (should= "one" (name (first (children scene))))
      (should= "two" (name (second (children scene))))
      (should= "three" (name (nth (children scene) 2)))))

  (it "allows events in options"
    (System/setProperty "foo.bar" "nothing")
    (let [scene (build-props @root "[:one {:id :one :on-focus-lost (fn [_] (System/setProperty \"foo.bar\" \"fizz-bang\"))}]", "prop.clj")
          _ (illuminate scene)
          prop (first (children scene))
          panel (.getPeer prop)]
      (.dispatch (limelight.ui.events.panel.FocusLostEvent.) panel)
      (should= "fizz-bang" (System/getProperty "foo.bar"))))

  (context "with filesystem"

    (with fs (limelight.io.FakeFileSystem/installed))

    (it "renders installs external props"
      (.createTextFile @fs "/path_to/prod/include.clj" "[:foo]")
      (let [scene (build-props @root "[:one (install \"include.clj\")]" "props.clj" :root-path "/path_to/prod")]
        (illuminate scene)
        (should= 1 (count (children @root)))
        (should= 1 (count (children (first (children @root)))))
        (should= "foo" (name (first (children (first (children @root))))))))

    (it "does install with params"
      (.createTextFile @fs "/path_to/prod/include.clj" "[:foo {:id (:the-id *context*)}]")
      (let [scene (build-props @root "[:one (install \"include.clj\" :the-id \"FOO\")]" "props.clj" :root-path "/path_to/prod")]
        (illuminate scene)
        (should= 1 (count (children @root)))
        (should= 1 (count (children (first (children @root)))))
        (should= "FOO" (id (first (children (first (children @root))))))))

    (it "install needs a root-path"
      (try
        (build-props @root "[:one (install \"include.clj\")]" "props.clj")
        (should-fail "Exception expected")
        (catch limelight.LimelightException e
          (should= true (.contains (.getMessage e) "Can't install props without a :root-path")))))
    )
  )

(run-specs)
