(ns music-player-backend.routes.song
  (:require [music-player-backend.server :as server]
            [music-player-backend.yt-dlp :as yt-dlp]
            [music-player-backend.json :as json]
            [clojure.java.shell :as sh]
            [clojure.java.io :as io]))

(defn get-output-names
  [dlp-output]
  ;;
  (let [audio (->> (re-seq #"\[ExtractAudio\] (.*)"
                           dlp-output)
                   first first
                   (drop-while (not= \/))
                   (drop 1) ;; drop the /
                   (apply str))

        thumbnail (do (println "Working with" dlp-output)
                      (->> (re-seq #"\[info\] Writing video thumbnail(.*)"
                                   dlp-output)
                           (#(do (println "Got"
                                          (drop-while
                                           (fn [x] (not (= x \/)))
                                           (first (first %))))
                                 %))
                           first first
                           (drop-while (not= \/))
                           (drop 1) ;; drop the /
                           (apply str)))]
    {:audio (if (some? (re-find #"the file is already in a common audio format" audio))
              (->> (String. audio)
                   reverse
                   (drop-while (not= \;))
                   (drop 1)
                   reverse
                   (apply str))
              audio)
     :thumbnail thumbnail
     :zip (->> thumbnail
               reverse
               (drop-while (not= \.)) ;; drop extension
               reverse
               (apply str)
               (#(str % "zip")))}))

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
        dlp-output (-> (yt-dlp/download-song "resources/%(title)s.%(ext)s" url)
                       (do (#(println "Haziendo tests" "Alex" %))
                           #(%)))
        filenames (get-output-names dlp-output)
        path "resources/"]

    (try
      (sh/sh "zip"
             (str path (:zip filenames))
             (str path (:audio filenames))
             (str path (:thumbnail filenames)))

      (let [response (server/respond
                      (-> (str path (:zip filenames))
                          io/file
                          io/input-stream)
                      :headers (octet-headers (str path (:zip filenames))))]
        (sh/sh "rm"
               "-f"
               (str path (:zip filenames))
               (str path (:audio filenames))
               (str path (:thumbnail filenames)))

        response)

      (catch Exception e
        (println "An error ocurred: " (.getMessage e))
        (sh/sh "rm"
               "-f"
               (str path (:zip filenames))
               (str path (:audio filenames))
               (str path (:thumbnail filenames)))))))
