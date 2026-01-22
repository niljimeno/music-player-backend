(ns music-player-backend.routes.search
  (:require [music-player-backend.server :as server]
            [music-player-backend.yt-dlp :as yt-dlp]
            [music-player-backend.json :as json]))

(defn route
  "Route to search for songs"
  [req]
  (let [data (json/json-from-request req)
        query (:query data)
        query-results (yt-dlp/search query)]
    (server/respond query-results)))

