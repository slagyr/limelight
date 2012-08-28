(defproject limelight/limelight-clj "0.6.16"
  :description "FIXME: write"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [limelight "0.6.19"]]
  :repositories {"local" "file://m2"}
  :java-source-paths ["src/"]

  ; leiningen 2
  :profiles {:dev {:dependencies [[speclj "2.3.1"]]}}
  :test-paths ["spec/"]
  :plugins [[speclj "2.3.1"]])
