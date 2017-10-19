(ns eSQL-Test (:require [eSQL :refer :all]))

(use 'clojure.test)


;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

;#1
(deftest Test_WHERE_Clause_with_:or_keyword_VerticalTable
  (let [SELECT [:ObjectName :ObjectPropetyType :ObjectPropetyValue :TestCase2]
        FROM (excelTable "Moon.xlsx" "MoonSheetVertical" true)
        WHERE {:or #{[:ObjectPropetyType "OPT41"] [:ObjectPropetyValue "OPY61"]}}
        ExpectedResult [[:ObjectName :ObjectPropetyType :ObjectPropetyValue :TestCase2]
                        ["ON31" "OPT31" "OPY61" "TC231"]
                        ["ON41" "OPT41" "OPY41" "TC241"]
                        ["ON51" "OPT51" "OPY61" "TC211"]
                        ["ON61" "OPT41" "OPY61" "TC261"]
                        ["ON81" "OPT41" "OPY61" "TC211"]]]
        (is (= (=> :SELECT SELECT :FROM FROM :WHERE WHERE) ExpectedResult))
    ))

;#2
(deftest Test_WHERE_Clause_with_:and_keyword_HorizontalTable
  (let [SELECT [:ObjectName :ObjectPropetyType :ObjectPropetyValue :TestCase1]
        FROM (excelTable "Moon.xlsx" "MoonSheetHorizontal" false)
        WHERE {:and #{[:ObjectPropetyValue "OPY60"] [:TestCase1 "TC130"]}}
        ExpectedResult [[:ObjectName :ObjectPropetyType :ObjectPropetyValue :TestCase1]
                        ["ON30" "OPT30" "OPY60" "TC130"]]]
    (is (= (=> :SELECT SELECT :FROM FROM :WHERE WHERE) ExpectedResult))
    ))

;#3
(deftest Test_WHERE_Clause_with_single_Column_value_pair_VerticalTable
  (let [SELECT [:ObjectName :ObjectPropetyValue :TestCase2]
        FROM (excelTable "Moon.xlsx" "MoonSheetVertical" true)
        WHERE {:ObjectPropetyValue "OPY61"}
        ExpectedResult [[:ObjectName :ObjectPropetyValue :TestCase2] ["ON31" "OPY61" "TC231"]
                        ["ON51" "OPY61" "TC211"]
                        ["ON61" "OPY61" "TC261"]
                        ["ON81" "OPY61" "TC211"]]]
    (is (= (=> :SELECT SELECT :FROM FROM :WHERE WHERE) ExpectedResult))
    ))

;#4
(deftest Test_without_WHERE_Clause_HorizontalTable
  (let [SELECT :*
        FROM (excelTable "Moon.xlsx" "MoonSheetHorizontal" false)
        ExpectedResult [[:Page :ObjectName :ObjectPropetyType :ObjectPropetyValue :TestCase1 :TestCase2]
                        ["P10" "ON10" "OPT10" "OPY10" "TC130" "TC210"]
                        ["P70" "ON20" "OPT20" "OPY20" "TC120" "TC220"]
                        ["P30" "ON30" "OPT30" "OPY60" "TC130" "TC230"]
                        ["P70" "ON40" "OPT40" "OPY40" "TC140" "TC240"]
                        ["P50" "ON50" "OPT50" "OPY60" "TC150" "TC210"]
                        ["P30" "ON60" "OPT40" "OPY60" "TC160" "TC260"]
                        ["P70" "ON70" "OPT70" "OPY70" "TC130" "TC270"]
                        ["P80" "ON80" "OPT40" "OPY60" "TC180" "TC210"]
                        ["P90" "ON90" "OPT90" "OPY90" "TC190" "TC290"]]]
    (is (= (=> :SELECT SELECT :FROM FROM) ExpectedResult))
    ))



;+++++Exception Testing ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

;#1
(deftest Test_exception-incorrect_SELECT_keyword-It_should_be_:SELECT
  (let [SELECT [:ObjectName :ObjectPropetyType :ObjectPropetyValue :TestCase1]
        FROM (excelTable "Moon.xlsx" "MoonSheetHorizontal" false)
        WHERE {:and #{[:ObjectPropetyValue "OPY60"] [:TestCase1 "TC130"]}}]
    (is (thrown-with-msg? Exception #"incorrect SELECT keyword - It should be :SELECT"
                          (=> :SELE SELECT :FROM FROM :WHERE WHERE)))
    ))

;#2
(deftest Test_exception-incorrect_syntax_of_SELECT_input_-_It_should_be_:*_or_Vector
  (let [SELECT :ObjectName
        FROM (excelTable "Moon.xlsx" "MoonSheetHorizontal" false)
        WHERE {:and #{[:ObjectPropetyValue "OPY60"] [:TestCase1 "TC130"]}}]
    (is (thrown-with-msg? Exception #"incorrect syntax of SELECT input - It should be :asterisk or Vector"
                          (=> :SELECT SELECT :FROM FROM :WHERE WHERE)))
    ))


;#3
(deftest Test_exception-SELECT_contain_duplicate_columns
  (let [SELECT [:ObjectName :ObjectPropetyType :ObjectPropetyType :TestCase1]
        FROM (excelTable "Moon.xlsx" "MoonSheetHorizontal" false)
        WHERE {:and #{[:ObjectPropetyValue "OPY60"] [:TestCase1 "TC130"]}}]
    (is (thrown-with-msg? Exception #"SELECT contain duplicate columns"
                          (=> :SELECT SELECT :FROM FROM :WHERE WHERE)))
    ))

;#4
(deftest Test_exception-incorrect_FROM_keyword_-_It_should_be_:FROM
  (let [SELECT [:ObjectName :ObjectPropetyType :ObjectPropetyValue :TestCase1]
        FROM (excelTable "Moon.xlsx" "MoonSheetHorizontal" false)
        WHERE {:and #{[:ObjectPropetyValue "OPY60"] [:TestCase1 "TC130"]}}]
    (is (thrown-with-msg? Exception #"incorrect FROM keyword - It should be :FROM"
                          (=> :SELECT SELECT :WRONG FROM :WHERE WHERE)))
    ))

;#5
(deftest Test_exception-incorrect_syntax_of_FROM_input_-_It_should_be_collection
  (let [SELECT [:ObjectName :ObjectPropetyType :ObjectPropetyValue :TestCase1]
        FROM "ArvinderTable"
        WHERE {:and #{[:ObjectPropetyValue "OPY60"] [:TestCase1 "TC130"]}}]
    (is (thrown-with-msg? Exception #"incorrect syntax of FROM input - It should be collection"
                          (=> :SELECT SELECT :FROM FROM :WHERE WHERE)))
    ))

;#6
(deftest Test_exception-FROM_contain_duplicate_columns
  (let [SELECT [:ObjectName :ObjectPropetyType :ObjectPropetyValue :TestCase1]
        FROM [[:Page :ObjectName :ObjectName :ObjectPropetyType :ObjectPropetyValue :TestCase1 :TestCase2]
              ["P10" "ON10" "ON10" "OPT10" "OPY10" "TC130" "TC210"]
              ["P70" "ON20" "ON20" "OPT20" "OPY20" "TC120" "TC220"]]
        WHERE {:and #{[:ObjectPropetyValue "OPY60"] [:TestCase1 "TC130"]}}]
    (is (thrown-with-msg? Exception #"FROM contain duplicate columns"
                          (=> :SELECT SELECT :FROM FROM :WHERE WHERE)))
    ))

;#7
(deftest Test_exception-SELECT_contain_columns_which_are_not_present_in_FROM_table
  (let [SELECT [:ObjectName :ObjectPropetyType :ObjectPropetyValue :UselessColumn]
        FROM (excelTable "Moon.xlsx" "MoonSheetHorizontal" false)
        WHERE {:and #{[:ObjectPropetyValue "OPY60"] [:TestCase1 "TC130"]}}]
    (is (thrown-with-msg? Exception #"SELECT contain columns which are not present in FROM table"
                          (=> :SELECT SELECT :FROM FROM :WHERE WHERE)))
    ))

;#8
(deftest Test_exception-incorrect_WHERE_keyword_-_It_should_be_:WHERE_or_nil
  (let [SELECT [:ObjectName :ObjectPropetyType :ObjectPropetyValue]
        FROM (excelTable "Moon.xlsx" "MoonSheetHorizontal" false)
        WHERE {:and #{[:ObjectPropetyValue "OPY60"] [:TestCase1 "TC130"]}}]
    (is (thrown-with-msg? Exception #"incorrect WHERE keyword - It should be :WHERE or nil"
                          (=> :SELECT SELECT :FROM FROM :WRONG WHERE)))
    ))

;#9
(deftest Test_exception-incorrect_syntax_of_WHERE_input_-Since_:WHERE_keyword_is_nil_therefore_WHERE_input_should_be_nil
  (let [SELECT [:ObjectName :ObjectPropetyType :ObjectPropetyValue]
        FROM (excelTable "Moon.xlsx" "MoonSheetHorizontal" false)
        WHERE {:and #{[:ObjectPropetyValue "OPY60"] [:TestCase1 "TC130"]}}]
    (is (thrown-with-msg? Exception #"incorrect syntax of WHERE input -Since :WHERE keyword is nil therefore WHERE input should be nil"
                          (=> :SELECT SELECT :FROM FROM nil WHERE)))
    ))

;#10
(deftest Test_exception-incorrect_syntax_of_WHERE_input_-_It_should_be_map
  (let [SELECT [:ObjectName :ObjectPropetyType :ObjectPropetyValue]
        FROM (excelTable "Moon.xlsx" "MoonSheetHorizontal" false)
        WHERE :ObjectName]
    (is (thrown-with-msg? Exception #"incorrect syntax of WHERE input - It should be map"
                          (=> :SELECT SELECT :FROM FROM :WHERE WHERE)))
    ))

;#11
(deftest Test_exception-incorrect_syntax_of_WHERE_input_-_It_should_be_map_and_should_contain_one_item_only
  (let [SELECT [:ObjectName :ObjectPropetyType :ObjectPropetyValue]
        FROM (excelTable "Moon.xlsx" "MoonSheetHorizontal" false)
        WHERE {:and #{[:ObjectPropetyValue "OPY60"] [:TestCase1 "TC130"]} :or [:ObjectPropetyValue "OPY60"]}]
    (is (thrown-with-msg? Exception #"incorrect syntax of WHERE input - It should be map and should contain one item only"
                          (=> :SELECT SELECT :FROM FROM :WHERE WHERE)))
    ))

;#12
(deftest Test_exception-incorrect_syntax_of_WHERE_input_-_It_should_contain_either_:or_or_:and_key_or_column_value_pair_where_column_name_should_be_present_in_From_table
  (let [SELECT [:ObjectName :ObjectPropetyType :ObjectPropetyValue]
        FROM (excelTable "Moon.xlsx" "MoonSheetHorizontal" false)
        WHERE {:after #{[:ObjectPropetyValue "OPY60"] [:TestCase1 "TC130"]}}]
    (is (thrown-with-msg? Exception #"incorrect syntax of WHERE input - It should contain either :or or :and key or column value pair where column name should be present in From table"
                          (=> :SELECT SELECT :FROM FROM :WHERE WHERE)))
    ))

;#13
(deftest Test_exception-incorrect_syntax_of_WHERE_input_-_If_it_contain_:or_or_:and_key_then_key_value_should_be_set
  (let [SELECT [:ObjectName :ObjectPropetyType :ObjectPropetyValue]
        FROM (excelTable "Moon.xlsx" "MoonSheetHorizontal" false)
        WHERE {:and [[:ObjectPropetyValue "OPY60"] [:TestCase1 "TC130"]]}]
    (is (thrown-with-msg? Exception #"incorrect syntax of WHERE input - If it contain :or or :and key then key value should be set"
                          (=> :SELECT SELECT :FROM FROM :WHERE WHERE)))
    ))

;#14
(deftest Test_exception-unknown_extra_syntax_in_query
  (let [SELECT [:ObjectName :ObjectPropetyType :ObjectPropetyValue]
        FROM (excelTable "Moon.xlsx" "MoonSheetHorizontal" false)
        WHERE {:and #{[:ObjectPropetyValue "OPY60"] [:TestCase1 "TC130"]}}]
    (is (thrown-with-msg? Exception #"unknown extra syntax in query"
                          (=> :SELECT SELECT :FROM FROM :WHERE WHERE :Extra "Syntax")))
    ))

(run-tests 'eSQL-Test)
