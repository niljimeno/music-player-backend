(ns music-player-backend.routes.song
  (:require [music-player-backend.server :as server]
            [music-player-backend.json :as json]
            [music-player-backend.yt-dlp :as yt-dlp]
            [clojure.java.io :as io]))

(defn get-filename
  [dlp-output]
  (->> (re-seq #"\[ExtractAudio\] Destination: resources\/(.*)"
               dlp-output)
       first first
       (drop-while #(not (= % \/)))
       (drop 1)
       (apply str)))

(defn route
  "Route to download a song"
  [req]
  (let [url (json/value-from-request :url req)
        filename (get-filename (yt-dlp/download-song "resources/%(title)s.%(ext)s" url))
        filepath (str "resources/" filename)]
    (server/respond
     (-> filepath
         io/file
         io/input-stream)
     :headers {"content-type" "application/octet-stream"
               "content-disposition" (str "attachment; filename=\""
                                          filepath
                                          "\"")})))
