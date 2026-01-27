(ns music-player-backend.yt-dlp
  (:require [clojure.java.shell :refer [sh]]))

(defn search [query]
  (:out (sh "yt-dlp"
            (str "ytsearch5:" "\"" query "\"")
            "--get-id"
            "--get-title"
            "--no-warnings")))

(defn download-song [path yt-url]
  (:out (sh "yt-dlp"
            "-x"
            "--extract-audio"
            "--write-thumbnail"
            "-o"
            path
            yt-url)))

