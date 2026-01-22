(ns music-player-backend.routes.search
  (:require [music-player-backend.server :as server]
            [music-player-backend.yt-dlp :as yt-dlp]))

(defn route
  "Route to search for songs"
  [data]
  (let [query (:query data)
        query-results (yt-dlp/search query)]
    (server/respond query-results)))

