(defproject music-player-backend "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.2"]
                 [aleph "0.9.3"]
                 [environ "1.2.0"]
                 [buddy/buddy-sign "3.6.1-359"]
                 [buddy/buddy-hashers "2.0.167"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [org.clojure/data.json "2.5.1"]
                 [org.xerial/sqlite-jdbc "3.51.1.0"]
                 [metosin/ring-swagger "1.0.0"]
                 [metosin/ring-swagger-ui "5.20.0"]]
  :main ^:skip-aot music-player-backend.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
