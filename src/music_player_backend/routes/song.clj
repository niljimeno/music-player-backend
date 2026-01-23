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
                   (drop-while #(not (= % \/)))
                   (drop 1) ;; drop the /
                   (#(do (println "Jodete alvaro" %)
                         %))
                   (apply str))
        thumbnail (do (println "Working with" dlp-output)
                      (->> (re-seq #"\[info\] Writing video thumbnail(.*)"
                                   dlp-output)
                           (#(do (println "Got" (drop-while (fn [x] (not (= x \/))) (first (first %))))
                                 %))
                           first first
                           (drop-while #(not (= % \/)))
                           (drop 1) ;; drop the /
                           (apply str)))]
    {:audio (do (println "We start with" (String. audio))
                (if (some? (re-find #"the file is already in a common audio format" (String. audio)))
                  (->> (String. audio)
                       (#(do (println "We are at" %)
                             %))
                       reverse
                       (drop-while #(not (= % \;)))
                       (drop 1)
                       reverse
                       (apply str))
                  (do (println "We give up with" (String. audio))
                      (String. audio))))
     :thumbnail (String. thumbnail)
     :zip (do (println "Zip section with" thumbnail)
              (->> thumbnail
                   reverse
                   (drop-while #(not (= % \.))) ;; drop extension
                   reverse
                   (apply str)
                   (#(str % "zip"))))}))

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
        filenames (get-output-names (yt-dlp/download-song "resources/%(title)s.%(ext)s" url))
        path "resources/"]

    (try
      (println "zip"
               (str path (:zip filenames))
               (str path (:audio filenames))
               (str path (:thumbnail filenames)))
      (sh/sh "zip"
             (str path (:zip filenames))
             (str path (:audio filenames))
             (str path (:thumbnail filenames)))

      (println "Now, respond.")

      (let [response (server/respond
                      (-> (str path (:zip filenames))
                          io/file
                          io/input-stream)
                      :headers (octet-headers (str path (:zip filenames))))]
        ;; (sh/sh "rm"
        ;;        "-f"
        ;;        (str path (:zip filenames))
        ;;        (str path (:audio filenames))
        ;;        (str path (:thumbnail filenames)))

        response)

      (catch Exception e
        (println "An error ocurred: " (.getMessage e))
        (sh/sh "rm"
               "-f"
               (str path (:zip filenames))
               (str path (:audio filenames))
               (str path (:thumbnail filenames)))))))
