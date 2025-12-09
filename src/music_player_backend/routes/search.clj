(ns music-player-backend.routes.search
  (:require [music-player-backend.server :as server]
            [music-player-backend.json :as json]
            [music-player-backend.yt-dlp :as yt-dlp]))

(defn route [req]
  (let [query (json/value-from-request :query req)]
    (let [query-results (yt-dlp/search query)]
      (server/respond query-results))))
