(ns limelight.util)

(defn read-src [src-path src-content]
  (let [rdr (-> (java.io.StringReader. src-content) (clojure.lang.LineNumberingPushbackReader.))
        parent-path (limelight.io.FileUtil/parentPath src-path)
        src-filename (limelight.io.FileUtil/filename src-path)]
    (clojure.lang.Compiler/load rdr parent-path src-filename)))