(ns music-player-backend.routes.song
  (:require [music-player-backend.server :as server]
            [music-player-backend.yt-dlp :as yt-dlp]
            [music-player-backend.json :as json]
            [clojure.java.shell :as sh]
            [clojure.java.io :as io]))

(defn get-filename
  [dlp-output]
  (->> (re-seq #"\[ExtractAudio\] Destination: resources\/(.*)"
               dlp-output)
       first first
       (drop-while #(not (= % \/)))
       (drop 1)
       (apply str)))

(defn octet-headers
  [filepath]
  {"content-type" "application/octet-stream"
   "content-disposition" (str "attachment; filename=\""
                              filepath
                              "\"")})

(defn route
  "Route to download a song"
  [req]
  (let [data (json/json-from-request req)
        url (:url data)
        filename (get-filename (yt-dlp/download-song "resources/%(title)s.%(ext)s" url))
        filepath (str "resources/" filename)
        response (server/respond
                  (-> filepath
                      io/file
                      io/input-stream)
                  :headers (octet-headers filepath))]
    (sh/sh "rm"
           "-f"
           filepath)

    response))
