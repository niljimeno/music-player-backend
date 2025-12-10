(ns music-player-backend.core
  (:gen-class)
  (:require [aleph.http :as http]
            [manifold.deferred :as d]
            [music-player-backend.yt-dlp :as yt-dlp]
            [music-player-backend.db :as db]
            [music-player-backend.router :as router]))

(defn -main
  "Comment example"
  [& args]
  (db/run-example)
  (println "Server up and running")
  (http/start-server router/handler {:host "0.0.0.0" :port 8080}))
