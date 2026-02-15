(ns music-player-backend.track
  (:require [music-player-backend.server :as server]
            [music-player-backend.db :as db]
            [clojure.java.jdbc :as jdbc]))

(defn add-track [data]
  (try
    (let [song (jdbc/query db/db ["SELECT name FROM songs
                                   WHERE userid = ? AND url = ?" (:userid data) (:url data)])]

      (if (empty? song)
        (let [result (db/crud :songs
                              :insert data)
              id (-> result first first second str)]
          (println "Track added - id:" id)
          (server/respond id :status 200))

        (server/respond "Track already exists" :status 409)))

    (catch Exception e
      (println "Error adding track:" (.getMessage e))
      (server/respond "Error adding track" :status 400))))

(defn update-track [data]
  (try
    (let [song (jdbc/query db/db ["SELECT name FROM songs
                                   WHERE userid = ? AND id = ?" (:userid data) (:id data)])]

      (if (empty? song)
        (server/respond "Track does not exist" :status 404)

        (let [result (db/crud :songs
                              :update data)]
          (println "Updated track" song "->" (:name data) "-" result)
          (server/respond "Track updated" :status 200))))

    (catch Exception e
      (println "Error updating track:" (.getMessage e))
      (server/respond "Error updating track" :status 400))))

(defn delete-track [data]
  (try
    (let [song (jdbc/query db/db ["SELECT name FROM songs
                                   WHERE userid = ? AND id = ?" (:userid data) (:id data)])]

      (if (empty? song)
        (server/respond "Track does not exist" :status 404)

        (let [result (db/crud :songs
                              :delete data)]
          (println "Removed track:" song "-" result)
          (server/respond "Track removed:" :status 200))))

    (catch Exception e
      (println "Error removed track:" (.getMessage e))
      (server/respond "Error removed track" :status 400))))