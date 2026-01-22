(ns music-player-backend.db-test
  (:require [clojure.test :refer :all]
            [music-player-backend.db :as db]
            [clojure.java.shell :as sh]))

(deftest datatest
  (sh/sh "rm" "-f" "./db/database.db")
  (println "deleted database")
  (db/set-db)
  (println "set'd database")
  (db/update-playlist {:id 3
                       :name "Geronima"
                       :userId 0})
  (db/update-playlist {:id 3
                       :name "Geronimo"
                       :userId 0}))
