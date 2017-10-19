(ns eSQL_V1
  (:import excel_Reader_POI.excelReader))

(defn SQL? [[SELECT x FROM y WHERE z & REST]]
  (and (= SELECT :SELECT) (or (= x :*) (if (vector? x) (if (= (count x) (count (set x))) true false) false))
       (= FROM :FROM) (if (coll? y)
                        (if (= (count (first y)) (count (set (first y))))
                          true false)
                        false)
       (if (vector? x)
         (if (= (filter #(get (set (first y)) %) x) (apply list x))
           true false)
         (if (= x :*)
           true false))
       (or
         (and (= WHERE :WHERE) (if (map? z)
                                 (if (and (= (count z) 1) (or (contains? z :and) (contains? z :or) ))
                                   true false)
                                 false))
         (and (= WHERE nil) (= z nil)))
       (= (count REST) 0)))

(defn => [& SQL]
  (let [SQL SQL
        {:keys [SELECT FROM WHERE]} SQL
        throwException (fn [] "Incorrect Syntax of SQL Statement")]
    (if (SQL? SQL)
      (->> (rest FROM)
           (map #(zipmap (first FROM) %))
           (filter
             #(cond
                (contains? WHERE :and) (reduce
                                         (fn [x y] (and x (= (% (get y 0)) (get y 1))))
                                         true (:and WHERE))
                (contains? WHERE :or) (reduce
                                        (fn [x y] (or x (= (% (get y 0)) (get y 1))))
                                        false (:or WHERE))
                (= WHERE nil) true))
           (map #(reduce (fn [x y] (conj x [y (% y)])) {} (if (not (= SELECT :*)) SELECT (first FROM))))
           (map #(vals %))
           (into [(if (not (= SELECT :*)) SELECT (first FROM))]))
      (throwException))))


(defn excelTable [excelFile excelSheet Transpose?]
  (let [excelReaderObj (excelReader. excelFile)
        javaList (.getTable excelReaderObj excelSheet Transpose?)
        Column (into [] (map #(keyword %) (first javaList)))
        Data (rest javaList)]
    (apply vector (conj (map #(into [] %) Data) Column))))

;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


;(def SELECT [:B :D :F :A])
;(def SELECT :*)

#_(def FROM [[:A :B :C :D :E :F]
             ["a1" "b1" "c1" "d1" "e1" "f1"]
             ["a2" "b2" "c2" "d2" "e2" "f2"]
             ["a3" "b3" "c3" "d3" "e3" "f3"]
             ["a4" "b4" "c4" "d4" "e4" "f4"]])

;(def WHERE {:and #{[:B "b2"] [:D "d2"]}})
;(def WHERE {:or #{[:B "b2"] [:D "d3"] [:E "e1"]}})


(def SELECT [:ObjectName :ObjectPropetyValue :TestCase2])
(def FROM (excelTable "Moon.xlsx" "MoonSheetHorizontal" false))
;(def FROM (excelTable "Moon.xlsx" "MoonSheetVertical" true))
;(def WHERE {:or #{[:ObjectPropetyValue "OPY60"]}})
;(def WHERE {:or #{[:ObjectPropetyValue "OPY61"]}})
(def WHERE {:or #{[:ObjectPropetyValue "OPY61"]}})
(println (=> :SELECT SELECT :FROM FROM :WHERE WHERE))


;(println (=> :SELECT SELECT :FROM FROM))
