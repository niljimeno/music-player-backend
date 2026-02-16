(ns music-player-backend.docs
  (:require
   [music-player-backend.server :as server]
   [ring.swagger.swagger2 :as rs]
   [clojure.data.json :as json]
   [schema.core :as s]))

(s/defschema Playlist
  {:id s/Str
   :name s/Str})

(s/defschema PlaylistPost
  (dissoc Playlist :id))

(s/defschema Delete
  {:id s/Str})

(s/defschema Track
  {:id s/Str
   :name s/Str
   :url s/Str})

(s/defschema TrackPost
  (dissoc Track :id))

(s/defschema Login
  {:username s/Str,
   :password s/Str})

(s/defschema UpdatePlaylist
  {:id s/Str
   :track-ids [s/Str]})

(def ^:private schema
  {:info {:version "1.0.0"
          :title "Eilous"
          :description "Music player backend"}

   :securityDefinitions {:jwt {:type "apiKey"
                               :name "Authorization"
                               :in "header"}
                         :master-key {:type "apiKey"
                                      :name "Authorization"
                                      :in "header"}}

   :security [{:jwt []}]

   :tags [{:name "user"}
          {:name "api"}
          {:name "database"}]
   :paths {"/register" {:post {:security [{:master-key []}]
                               :tags ["user"]
                               :parameters {:body Login}}}
           "/login" {:post {:security []
                            :tags ["user"]
                            :parameters {:body Login}}}

           "/song" {:post {:tags ["api"]
                           :parameters {:body {:url s/Str}}}}
           "/search" {:post {:tags ["api"]
                             :parameters {:body {:query s/Str}}}}

           "/playlist" {:post {:tags ["database"]
                               :parameters {:body PlaylistPost}}
                        :put {:tags ["database"]
                              :parameters {:body Playlist}}
                        :delete {:tags ["database"]
                                 :parameters {:body Delete}}}

           "/track" {:post {:tags ["database"]
                            :parameters {:body TrackPost}}
                     :put {:tags ["database"]
                           :parameters {:body Track}}
                     :delete {:tags ["database"]
                              :parameters {:body Delete}}}

           "/backup" {:get {:tags ["database"]}}
                      
           "/playlist/tracks" {:post {:tags ["database"]
                                      :parameters {:body UpdatePlaylist}}}}})

(def ^:private data
  (-> schema
      rs/swagger-json
      s/with-fn-validation
      json/write-str))

(defn route
  []
  (server/respond data
                  :headers {:content-type "application/json"}))

