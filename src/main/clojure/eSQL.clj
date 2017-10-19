(ns eSQL
  (:import excel_Reader_POI.excelReader))

(defn SQL? [[SELECT select FROM from WHERE where & REST]]
  (cond
    (not (= SELECT :SELECT))
    "incorrect SELECT keyword - It should be :SELECT"

    (not (or (= select :*) (vector? select)))
    "incorrect syntax of SELECT input - It should be :asterisk or Vector"

    (not (if (vector? select) (= (count select) (count (set select))) true))
    "SELECT contain duplicate columns"

    (not (= FROM :FROM))
    "incorrect FROM keyword - It should be :FROM"

    (not (coll? from))
    "incorrect syntax of FROM input - It should be collection"

    (not (= (count (first from)) (count (set (first from)))))
    "FROM contain duplicate columns"

    (not (if (vector? select) (= (filter #(get (set (first from)) %) select) (apply list select)) true))
    "SELECT contain columns which are not present in FROM table"

    (not (or (= WHERE :WHERE) (= WHERE nil)))
    "incorrect WHERE keyword - It should be :WHERE or nil"

    (not (if (= WHERE nil) (= where nil) true))
    "incorrect syntax of WHERE input -Since :WHERE keyword is nil therefore WHERE input should be nil"

    (not (if (= WHERE :WHERE) (map? where) true))
    "incorrect syntax of WHERE input - It should be map"

    (not (if (= WHERE :WHERE) (= (count where) 1) true))
    "incorrect syntax of WHERE input - It should be map and should contain one item only"

    (not (if (= WHERE :WHERE) (or (contains? where :and) (contains? where :or) (not (= (count (filter #(% where) (first from))) 0))) true))
    "incorrect syntax of WHERE input - It should contain either :or or :and key or column value pair where column name should be present in From table"

    (not (if (= WHERE :WHERE) (or (if (contains? where :and) (set? (:and where)) true) (if (contains? where :or) (set? (:or where)))) true))
    "incorrect syntax of WHERE input - If it contain :or or :and key then key value should be set"

    (not (= (count REST) 0))
    "unknown extra syntax in query"
    ))

(defn => [& SQL]
  (let [SQL SQL
        {:keys [SELECT FROM WHERE]} SQL]
    (if (nil? (SQL? SQL))
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
                (= WHERE nil) true
                :else (= (% (first (keys WHERE))) (WHERE (first (keys WHERE))))    ))

           (map #(reduce (fn [x y] (conj x [y (% y)])) {} (if (not (= SELECT :*)) SELECT (first FROM))))
           (map #(apply vector (vals %)))
           (into [(if (not (= SELECT :*)) SELECT (first FROM))]))
      (throw (Exception. (SQL? SQL))) )))

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


;(def SELECT [:ObjectName :ObjectPropetyValue :TestCase2])
;(def SELECT :*)
;(def FROM (excelTable "Moon.xlsx" "MoonSheetHorizontal" false))
;(def FROM (excelTable "Moon.xlsx" "MoonSheetVertical" true))
;(def WHERE {:or #{[:ObjectPropetyValue "OPY60"]}})
;(def WHERE {:or #{[:ObjectPropetyValue "OPY61"]}})
;(def WHERE {:ObjectPropetyValue "OPY60"})
;(def WHERE {:ObjectPropetyValue "OPY61"})
;(println (=> :SELECT SELECT :FROM FROM :WHERE WHERE))
;(println (=> :SELECT SELECT :FROM FROM))
