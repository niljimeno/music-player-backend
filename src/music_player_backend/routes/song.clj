(ns music-player-backend.routes.song
  (:require [music-player-backend.server :as server]
            [music-player-backend.json :as json]
            [music-player-backend.yt-dlp :as yt-dlp]))

(defn route [req]
  (let [url (json/value-from-request :url req)]
    (yt-dlp/download-song "song" url)
    (server/respond "Song downloaded but not sent")))
