(ns .temp)

(def todayDate (java.util.Date.))

(println (.getTime todayDate))

(println (doto (java.util.HashMap.) (.put "a" 1) (.put "b" 2)))