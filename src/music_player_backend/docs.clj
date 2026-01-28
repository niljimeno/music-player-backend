(ns music-player-backend.docs
  (:require
   [music-player-backend.server :as server]
   [ring.swagger.swagger2 :as rs]
   [clojure.data.json :as json]
   [schema.core :as s]))

(s/defschema User {:id s/Str,
                   :name s/Str
                   :address {:street s/Str
                             :city (s/enum :tre :hki)}})
(s/defschema Login
  {:username s/Str,
   :password s/Str})

(def ^:private schema
  {:info {:version "1.0.0"
          :title "Eilous"
          :description "Music player backend"}
          ; :termsOfService "http://helloreverb.com/terms/"
          ; :contact {:name "My API Team"
          ;           :email "foo@example.com"
          ;           :url "http://www.metosin.fi"}
          ; :license {:name "Eclipse Public License"
          ;           :url "http://www.eclipse.org/legal/epl-v10.html"}}

   :securityDefinitions {:jwt {:type "apiKey"
                               :name "Authorization"
                               :in "header"}}
   :security [{:jwt []}]

   :tags [{:name "user"
           :description "User stuff"}]
   :paths {"/login" {:get {:security []
                           :parameters {:body Login}}}
           "/song" {:get {:parameters {:query {:url s/Str}}}}
           ; "/user/:id" {:post {:summary "User Api"
           ;                     :description "User Api description"
           ;                     :tags ["user"]
           ;                     :parameters {:path {:id s/Str}
           ;                                  :body User}
           ;                     :responses {200 {:schema User
           ;                                      :description "Found it!"}
           ;                                 404 {:description "Ohnoes."}}}}}})
           }})

(def ^:private data
  (-> schema
      rs/swagger-json
      s/with-fn-validation
      json/write-str))

(defn route
  []
  (server/respond data
                  :headers {:content-type "application/json"}))

