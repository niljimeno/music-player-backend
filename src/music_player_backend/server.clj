(ns music-player-backend.server
  (:require [clojure.java.io :as io]))

(defn respond
  "Template for HTTP responses"
  [body & {:keys [status headers] :or {status 200
                                       headers {"content-type" "text/plain"}}}]
  {:status status
   :headers (-> headers
                (assoc "Access-Control-Allow-Origin" "*")
                (assoc "Access-Control-Allow-Methods" "GET,POST,PUT,DELETE,OPTIONS,PATCH")
                (assoc "Access-Control-Allow-Headers" "Content-Type, Authorization"))
   :body body})

(defn- get-extension
  [filename]
  (->> filename
       reverse
       (take-while (partial not= \.))
       reverse))

(defn- get-content-type
  [filename]
  (println "have" filename "and" (get-extension filename))
  (println "filename is" (apply str (get-extension filename)))
  (-> (get-extension filename)
      (#(apply str %))
      (case
       "html" "text/html"
       "css" "text/css"
       "js" "application/javascript"
       "text/plain")))

(defn serve-static
  [uri]
  (let [f (io/file (str \. uri))]
    (if (.exists f)
      (respond f
               :status 200
               :headers {"content-type" (get-content-type uri)})
      {:status 404
       :body "not found"})))
