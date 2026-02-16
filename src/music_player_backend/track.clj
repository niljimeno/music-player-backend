(ns music-player-backend.track
  (:require [music-player-backend.server :as server]
            [music-player-backend.db-mg :as db]))

(defn add [data]
  (try
    (let [track (db/get-track-by-url (:user_id data) (:url data))]

      (if (nil? track)
        (let [result (db/insert-data "tracks" data)
              id (str (:_id result))]

          (println "Track added - id:" id)
          (server/respond id :status 200))

        (server/respond "Track already exists" :status 409)))

    (catch Exception e
      (println "Error adding track:" (.getMessage e))
      (server/respond "Error adding track" :status 400))))

(defn edit [data]
  (try
    (let [track (db/get-track (:user_id data) (:id data))]

      (if (nil? track)
        (server/respond "Track not found" :status 404)

        (let [result (db/update-data "tracks" (:id data) {:name (:name data)})]
          (println "Updated track" (:name track) "->" (:name data) "-" result)
          (server/respond "Track updated - id:" (str (:_id track)) :status 200))))

    (catch Exception e
      (println "Error updating track:" (.getMessage e))
      (server/respond "Error updating track" :status 400))))

(defn delete [data]
  (try
    (let [track (db/get-track (:user_id data) (:id data))]

      (if (nil? track)
        (server/respond "Track not found" :status 404)

        (let [result (db/remove-data "tracks" (:id data))]
          (println "Removed track:" track "-" result)
          (server/respond "Track removed - id:" (str (:_id track))  :status 200))))

    (catch Exception e
      (println "Error removed track:" (.getMessage e))
      (server/respond "Error removed track" :status 400))))