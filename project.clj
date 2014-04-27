(defproject nado-helper "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
      [org.clojure/clojure "1.5.1"]
      [clj-redis "0.0.12"]
      [java-ascii-table "1.0"]
      ]
  :main ^:skip-aot nado-helper.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :repositories [["local" "file:///Users/bluven/.local_repo"]])
