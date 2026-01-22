(ns music-player-backend.core
  (:gen-class)
  (:require [aleph.http :as http]
            [music-player-backend.db :as db]
            [music-player-backend.router :as router]
            [clojure.java.shell :as sh]))

(defn -main
  "Main function to run the program"
  [& _]
  (db/set-db)
  (sh/sh "mkdir" "-p" "resources")
  (println "Server up and running")
  (http/start-server router/handler {:host "0.0.0.0" :port 8080}))
