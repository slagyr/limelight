(ns limelight.util)

(defn read-src [src-path src-content]
  (let [rdr (-> (java.io.StringReader. src-content) (clojure.lang.LineNumberingPushbackReader.))
        parent-path (.parentPath (limelight.Context/fs) src-path)
        src-filename (.filename (limelight.Context/fs) src-path)]
    (clojure.lang.Compiler/load rdr parent-path src-filename)))