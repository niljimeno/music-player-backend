(ns music-player-backend.db
  (:require [clojure.java.jdbc :as jbdc])
  (:gen-class))

(def testdata
  {:url "http://example.com",
   :title "SQLite Example",
   :body "Example using SQLite with Clojure"})

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "db/database.db"})

(defn initial-commands [] ;; no sabia que ponerle de nombre
  (jbdc/create-table-ddl :news
                         [[:timestamp :datetime :default :current_timestamp]
                          [:url :text]
                          [:title :text]
                          [:body :text]]
                         {:conditional? true})) ;; conditional: if not exists

(defn create-db
  "create db and table"
  []
  (try (jbdc/db-do-commands db
                            (initial-commands))
       (catch Exception e
         (println (.getMessage e)))))

(defn print-result-set
  "prints the result set in tabular form"
  [result-set]
  (doseq [row result-set]
    (println row)))

(defn output
  "execute query and return lazy sequence"
  []
  (jbdc/query db ["select * from news"]))

(defn run-example
  "Run SQLite example"
  []
  (println "Running database example")
  (create-db)
  (jbdc/insert! db :news testdata)
  (print-result-set (output)))
