(ns music-player-backend.db-test
  (:require [clojure.test :refer :all]
            [music-player-backend.db :as db]
            [clojure.java.shell :as sh]))

(deftest datatest
  (sh/sh "rm" "-f" "./db/database.db")
  (println "deleted database")
  (db/set-db)
  (println "set'd database")

  (db/crud :playlists
           :insert {:name "Geronima"
                    :userId 0})
  (db/crud :playlists
           :insert {:name "Alvaro"
                    :userId 0})
  (db/crud :playlists
           :update {:id 1
                    :new {:name "Geronimo"}})
  (println (db/crud :playlists
                    :read)))

