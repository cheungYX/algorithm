# Elasticsearch 核心技术与实战
# 0.0 概述
* Lucene base
* 高可用 & 水平扩展
* Hot & Warm构架
* RESTful API
* JDBC & ODBC
* 海量数据的分布式存储及集群管理
  - 服务与数据的高可用，水平扩展
* 近实时搜索，性能卓越
  - 结构化/全文/地理位置/自动完成
* 海量数据的近实时分析
  - 聚合功能 
* Elastic Stack 生态圈
  - 可视化   kibana
  - 存储计算 Elasticsearch
  - 数据抓取 Logstash Beat
  - 商业包   X-Pack(安全,告警,监控,机械学习)
  - 云服务 https://www.elastic.co/guide/en/cloud-on-k8s/current/index.html
* [Elasticsearch Certification](https://training.elastic.co/exam/elastic-certified-engineer)
* 分开源版本和Basic版本
  - 部分X-Pack功能支持免费使用
* Elasticsearch与数据库的集成
  - APP => 数据库 (同步机制)=> Elasticsearch
* 指标分析/日志分析
  - beats => redis/kafka/RabbitMQ => logstash => Elasticsearch => kibana/Grafana 


# 0.1 安装
* Elasticsearch
```
//启动单节点
bin/elasticsearch -E node.name=node0 -E cluster.name=geektime -E path.data=node0_data
//安装插件
bin/elasticsearch-plugin install analysis-icu

//查看插件
bin/elasticsearch-plugin list
//查看安装的插件
GET http://localhost:9200/_cat/plugins?v

//start multi-nodes Cluster
bin/elasticsearch -E node.name=node0 -E cluster.name=geektime -E path.data=node0_data
bin/elasticsearch -E node.name=node1 -E cluster.name=geektime -E path.data=node1_data
bin/elasticsearch -E node.name=node2 -E cluster.name=geektime -E path.data=node2_data
bin/elasticsearch -E node.name=node3 -E cluster.name=geektime -E path.data=node3_data

ps | grep elasticsearch
kill pid
```
jvm.options

* kibana
```
// 启动 kibana
bin/kibana

// 查看插件
bin/kibana-plugin list
```
* logstash


# 1.0 基本概念
## 1.1 索引，文档和 REST API
* 索引/类型/文档(index/type/id)
* 索引
  - 索引是文档的容器,是一类文档的结合
  - index体现了逻辑空间的概念,每个索引都有自己的Mapping定义
  - Shard体现了物理空间的概念,索引数据分散在shard上
  - Mapping用于定义包含文档的字段名和字段类型
  - Setting定义不同的数据分布
  - Type已被废除
* 文档
  - 文档是所有可搜索数据的最小单位
  - 文档会被序列化成json格式, json对象由字段组成，对应字段类型
  - 每一个文档都有一个Unique ID
  - 文档的元数据

## 1.2 节点，集群分片和副本
* 节点
  - 本质上是一个JAVA进程
  - 生产环境一般建议只运行一个实例
  - 每一个节点都有名字 -E node.name=node1
  - 每一个节点启动之后,会分配一个UID,保存在data目录下
  - Master Node & Master-eligible nodes
    - 每一个节点启动后默认是Master-eligible
    - Master-eligible节点可以参加选主流程，成为Master节点
    - 每个节点上都保存了集群的状态，只有Master节点才能修改
    - Cluster State: 节点信息,Mapping Setting,分片路由
  - Data Node & Coordinating Node
    - Data Node:保存分片数据的节点，在数据扩展上起到了关键作用
    - Coordinating Node: 接受Client请求后分发到合适的节点,最终把结果汇到一起
    - 每个节点默认都起到了Coordinating Node的职责
  - 其它节点类型
    - Hot & Warm Node: 不同磁盘硬件配置的Data Note,降低集群部署的成本
    - Machine Leaning Node
    - Tribe Node， 5.3开始使用Cross Cluster Search
* 分片
  - Primary Shard(主分片)
    - 解决水平扩展问题,通过主分片,可以将数据分布到集群内的所有节点上
    - 主分片数在index创建时指定,后续不允许修改,除非reindex
    - 过小导致后续无法增加节点实现水平扩展，单分片数据量过大导致数据分配耗时
    - 过大影响搜索结果的相关性打分,资源浪费
    - 单个存储30G内来计算需要的分片数
  - Replica Shard(副本)
    - 可动态调整
    - 通过增加副本，一定程度上提高服务的可用性(读取的吞吐)

## 1.3 文档的curd
| Index  | PUT my_index/_doc/1 {"user":"mike", "comment":"You Konw ..."}     | 
| ------ |  :--------------------------------------------------------------  |
| Create | POST my_index/_doc  {"user":"mike", "comment":"You Konw ..."}     |
| Read   | Get my_index/_doc/1                                               |   
| Update | Post my_index/_update/1 {"user":"mike", "comment":"You Konw ..."} |
| Delete | Delete my_index/_doc/1                                            |

* index: 不存在就索引新文档，存在就更新
* Bulk
  ```
  POST _bulk
  { "index" : { "_index" : "test", "_id" : "1" } }
  { "field1" : "value1" }
  { "delete" : { "_index" : "test", "_id" : "2" } }
  { "create" : { "_index" : "test2", "_id" : "3" } }
  { "field1" : "value3" }
  { "update" : {"_id" : "1", "_index" : "test"} }
  { "doc" : {"field2" : "value2"} }
  ```
* mget
  ```
  GET /_mget
  {
      "docs" : [
          {
              "_index" : "test",
              "_id" : "1"
          },
          {
              "_index" : "test",
              "_id" : "2"
          }
      ]
  }
  ```
* msearch
  ```
  POST kibana_sample_data_ecommerce/_msearch
  {}
  {"query" : {"match_all" : {}},"size":1}
  {"index" : "kibana_sample_data_flights"}
  {"query" : {"match_all" : {}},"size":2}
  ```
## 1.4 倒排索引
* 单词词典(Tern Dictionary)
  - 记录单词与倒排列表的关系
  - B+ or 哈希来实现
* 倒排列表(Posting List)
  - 文档ID
  - 词频TF
  - 位置(Position): 文档中的分词位置,用于语句搜索(phrase query)
  - 偏移(offset): 高亮显示

## 1.5 Analyzer进行分词
* 把全文本转换成一系列单词(Term/token)的过程
* CharacterFilters(原始文本处理) => Tokenizer(按规则切分单词) => TokenFilters(单词加工,小写同意词等)
* Simple Analyzer
  – 按照非字母切分（符号被过滤），小写处理
* Stop Analyzer
  – 小写处理，停用词过滤（the，a，is）
* Whitespace Analyzer
  – 按照空格切分，不转小写
* Keyword Analyzer 
  – 不分词，直接将输入当作输出
* icu_analyzer(亚洲文字)
* 中文分词
  - IKAnalyzer
  - 
* Language
  – 提供了30多种常见语言的分词器 
```
Get /_analyze
curl -XGET 'localhost:9200/_analyze?analyzer=jp_search_analyzer' -d '5ヶ月'
```

# 2.0 Search API
* 指定索引
  ```
  /_search 集群上所以索引
  /index1/_search
  /index1,index2/_search
  /index*/_search
  ```
* Response
  - took: 整个搜索请求耗费了多少毫秒
  - total: 符合条件的总文档数
  - hits: 结果集，默认为前10个文档
  - _index, _id, _score, _souce


## 2.1 URI Search
```
curl -XGET "localhost:9200/index/_search?q=field1:hoge&profile=true
```
* 泛查询，正对_all,所有字段
  - GET /index/_search?q=2012 
* 指定字段
  - GET /index/_search?q=title:2012&sort=year:desc&from=0&size=10&timeout=1s 
* Term v.s Phrase
  - Term: GET /movies/_search?q=title:Beautiful Mind => Beautiful OR Mind
  - Phrase: GET /movies/_search?q=title:"Beautiful Mind" => Beautiful AND Mind
* 分组，Bool查询
  - GET /movies/_search?q=title:(Beautiful Mind)
* Bool
  - AND / OR / NOT && / || / ！
  - + must
  - - must_not
  - title:(+matrix -reloaded)
* Range []闭区间, {}开区间
  - year:{2019 TO 2018}
  - year:[* TO 2018]
* 算数符号
  - year:>2010
  - year:(>2010 && <=2018)
  - year:(+>2010 +<=2018)
* 通配符查询
  - title:be*
* 正则表达
  - title:[bt]oy 
* 模糊匹配与近似查询
  - title:befutifl~1
  - title:"lord rings"~2
* [文档](https://www.elastic.co/guide/en/elasticsearch/reference/7.0/search-uri-request.html)


## 2.2 Request Body & Query DSL
* match 分词查询
* tern 不分词精确匹配
  - term主要用于精确匹配哪些值，比如数字，日期，布尔值或 not_analyzed 的字符串
* paging
  ```
  curl -XGET "localhost:9200/index/_search" -H 'Content-Type: application/json' -d'
  {
    "from": 10,
    "size": 30,
    "query": {
      "match_all": {}
    }
  }'
  ```
* sort
  ```
  POST kibana_sample_data_ecommerce/_search
  {
    "sort":[{"order_date":"desc"}],
    "query":{
      "match_all": {}
    }
  }
  ```
* range
  - gt :: 大于
  - gte:: 大于等于
  - lt :: 小于
  - lte:: 小于等于
  ```
  GET books/_search
  {
    "_source": ["title", "publish_time"],
    "query": {
      "range": {
        "publish_time": {
          "gte": "2016-1-1",
          "lte": "2016-12-31",
          "format": "yyyy-MM-dd"
        }
      }
    }
  }
  ```
* source filtering
  ```
  POST kibana_sample_data_ecommerce/_search
  {
    "_source":["order_date"],
    "query":{
      "match_all": {}
    }
  }
  ```
* 脚本字段
  ```
  GET kibana_sample_data_ecommerce/_search
  {
    "script_fields": {
      "new_field": {
        "script": {
          "lang": "painless",
          "source": "doc['order_date'].value+'hello'"
        }
      }
    },
    "query": {
      "match_all": {}
    }
  }
  ```
* match
  ```
  POST movies/_search
  {
    "query": {
      "match": {
        "title": {
          "query": "last christmas",
          "operator": "and"
        }
      }
    }
  }
  ```
* match_phrase 自带了 operator 属性的值为 and 的 match
  ```
  POST movies/_search
  {
    "query": {
      "match_phrase": {
        "title":{
          "query": "one love",
          "slop": 1
  
        }
      }
    }
  }
  ```
* perfix
  ```
  GET books/_search
  {
    "_source": "description", 
    "query": {
      "prefix": {
        "description": "wi"
      }
    }
  }
  ```
* [文档](https://www.elastic.co/guide/en/elasticsearch/reference/7.0/search-request-body.html)

## 2.3 Query String & Simple Query String
* Query string
  ```
  POST users/_search
  {
    "query": {
      "query_string": {
        "default_field": "name",
        "query": "Ruan AND Yiming"
      }
    }
  }
  ```
* Simple Query String
  - 类似Query String但是会忽略错误语法
  - 不支持AND OR NOT
  - Term之间默认的关系是OR，可以指定Operator
  - + 替代 AND, | 替代 OR, - 替代 NOT
  ```
  POST users/_search
  {
    "query": {
      "simple_query_string": {
        "query": "Ruan Yiming",
        "fields": ["name"],
        "default_operator": "AND"
      }
    }
  }
  ```

# 3.0 Mapping
* 预定义字段的类型以及相关属性 solr schema
  ```
  {
      "mappings": {
          "my_type": {
          //true:表示自动识别新字段并创建索引，false:不自动索引新字段，strict:遇到未知字段，抛异常，不能存入
              "dynamic":      "strict", 
              
                //动态模板
               "dynamic_templates": [
                      { "stash_template": {
                        "path_match":  "stash.*",
                        "mapping": {
                          "type":           "string",
                          "index":       "not_analyzed"
                        }
                      }}
                    ],
              //属性列表
              "properties": {
                  //一个strign类型的字段
                  "title":  { "type": "string"},
                  
                  "stash":  {
                      "type":     "object",
                      "dynamic":  true 
                  }
              }
          }
      }
  }
  ```
* 类型
  - String 
  - text, keyword
  - long, integer, short, byte, double, float
  - date
  - boolean
  - binary
  - object, nested
  - geo-point, geo-sharp
  - ip, competion
* 属性
  - index_name 
  - anylyzer
  - store 显示存储
  - boost
  - null_value
  - include_in_all
  - format
  ```
  {
      "mappings": {
          "my_type": {
          //true:表示自动识别新字段并创建索引，false:不自动索引新字段，strict:遇到未知字段，抛异常，不能存入
              "dynamic":      "strict", 
              
                //动态模板
               "dynamic_templates": [
                      { "stash_template": {
                        "path_match":  "stash.*",
                        "mapping": {
                          "type":           "string",
                          "index":       "not_analyzed"
                        }
                      }}
                    ],
              //属性列表
              "properties": {
                  //一个strign类型的字段
                  "title":  { "type": "string"},
                  
                  "stash":  {
                      "type":     "object",
                      "dynamic":  true 
                  }
              }
          }
      }
  }
  ```

## 3.1 Dynamic Mapping
* 在写入文档的时候,如果索引不存在会自动创建索引
* 无需手动定义Mapping,Elasticsearch会自动根据文档信,推算出字段的类型
* 但是有时候会推算的不准确,例如地理位置信息
* 当类型如果设置不对时,会导致一些功能无法正常运行,例如Range查询
* 后期修改Mapping的字段类型
  - 新增字段
    - Dynamic: true,  一旦有新增字段的文档写入,Mapping也同时被更新
    - Dynamic: false, Mapping不会被更新,新增字段的数据无法被索引,但是信息会出现在_source中
    - Dynamic: Strict, 文档写入失败
  - 对已有字段,一旦已有数据写入,就不再支持修改字段定义

## 3.2 显式Mapping
* 推荐步骤
  - 创建一个临时的index,写入一些样本数据
  - 通过访问Mapping API 获得该文件的动态Mapping定义
  - 修改后使用该配置创建你的索引
  - 删除临时索引
* null_value
  - 只要keyword类型支持设定null_value
  ```
  PUT users
  {
      "mappings" : {
        "properties" : {
          "firstName" : {
            "type" : "text"
          },
          "lastName" : {
            "type" : "text"
          },
          "mobile" : {
            "type" : "keyword",
            "null_value": "NULL"
          }
  
        }
      }
  }
  ```
* _all在7中被copy_to所替代
* es中不提供专门的数组类型,但是任何字段,都可以包含多个相同类型的数值

## 3.3 多字段特性
* 实现精确匹配
  - 增加一个keyword字段
* 使用不同的analyzer
  - 不同语言
  - pinyin 字段的搜索
  - 还支持为搜索和索引指定不同的analyzer
* Exact Values(精确值) vs Full Text
  - Exact Values: keyword
  - Full Text: text
* 自定义分词
  - Character Filter
    - 增加及替换字符
    - 可配置多个Character Filter
    - 会影响Tokenizer的position和offset信息
    - 自带: HTML strip, Mapping, Pattern replace
  - Tokenizer
    - 切词
    - 可用java开发插件实现自己的Tokenizer
  - Token Filter
    - 将Tokenizer输出的词(term)进行增删该
  ```
  "jp_search_analyzer" : {
      "type" : "custom",
      "tokenizer" : "kuromoji_user_dict",
      "filter": [ "jp_search_stop_filter", "synonym_series_filter", "jp_synonym_filter", "jp_synonym_search_filter" ],
      "char_filter": ["jp_mapping", "jp_mapping2", "number_norm"]
  },
  "jp_index_analyzer" : {
      "type" : "custom",
      "tokenizer" : "kuromoji_user_dict",
      "filter": [ "jp_search_stop_filter", "synonym_series_filter", "jp_synonym_filter" ],
      "char_filter": ["jp_mapping", "jp_mapping2", "number_norm", "url_filter", "user_filter"]
  },
  ```

## 3.4 Index Template和 Dynamic Template
* Index Template
  - 帮助你设定Mappings和Settings并按照一定的规则自动匹配到新创建的索引之上
  - 模版仅在一个索引被新创建时,才会产生作用，修改模版不会影响已创建的索引
  - 可以设定多个索引模版,这些设置会被merge在一起
  - 可以指定order的数值,控制merging的过程
  ```
  PUT /_template/template_test
  {
      "index_patterns" : ["test*"],
      "order" : 1,
      "settings" : {
        "number_of_shards": 1,
          "number_of_replicas" : 2
      },
      "mappings" : {
        "date_detection": false,
        "numeric_detection": true
      }
  }
  ```
* Index Template工作方式
  - 应用Elasticsearch默认的setting和mapping
  - 应用order数值低的Index Template中的设定
  - 应用order数值高的Index Template中的设定
  - 应用创建索引时,用户所指定的settings和mappings,并覆盖之前模版中的设定
* Dynamic Template
  - 根据Elasticsearch识别的数据类型,结合字段名称,来动态设定字段类型
  - 所有的字符串类型都设定成keyword,或者关闭keyword字段
  - is开头的字段都设置成boolean
  - long_开头的都设置成long类型
  ```
  {
    "mappings": {
      "dynamic_templates": [
              {
          "strings_as_boolean": {
            "match_mapping_type":   "string",
            "match":"is*",
            "mapping": {
              "type": "boolean"
            }
          }
        },
        {
          "strings_as_keywords": {
            "match_mapping_type":   "string",
            "mapping": {
              "type": "keyword"
            }
          }
        }
      ]
    }
  }
  ```
* [Index Template文档](https://www.elastic.co/guide/en/elasticsearch/reference/7.1/indices-templates.html)
* [Dynamic Template文档](https://www.elastic.co/guide/en/elasticsearch/reference/7.1/dynamic-mapping.html)

## 3.5 聚合分析(Aggregation)
* 通过聚合,可以得到一个数据的概览
* 高性能,只需要一条语句就能从Elasticsearch得到分析结果,无需在客户端实现
* Bucket Aggregation
  - 一些满足特定条件的文档的集合
  - Term & Range
* Metric Aggregation
  - 一些数学运算，可以多文档字段进行统计分析
  - 同样也支持在脚本(painless script)产生的结果之上进行计算
  - 大多数metric是数学计算,仅输出一个值 min / max / sum / avg / cardinality
  - 部分metric支持输出多个数值 stats / percentiles / percentiles_ranks
* Pipeline Aggregation
  - 对其它的聚合结果进行二次聚合
* Matrix Aggregation
  - 支持对多个字段的操作并提供一个结果矩阵
  ```
  {
    "size": 0,
    "aggs":{
      "flight_dest":{
        "terms":{
          "field":"DestCountry"
        },
        "aggs":{
          "avg_price":{
            "avg":{
              "field":"AvgTicketPrice"
            }
          },
          "max_price":{
            "max":{
              "field":"AvgTicketPrice"
            }
          },
          "min_price":{
            "min":{
              "field":"AvgTicketPrice"
            }
          }
        }
      }
    }
  }
  ```
* [search-aggregations文档](https://www.elastic.co/guide/en/elasticsearch/reference/7.1/search-aggregations.html)

# 4.0 搜索与分词
## 4.1 Term与Full Text
* Keyword vs Text
* Term
  - 表达语意的最小单位
  - Term Query / Range Query / Exists Query / Prefix Query / Wildcard Query
  - 对输入不会做分词,会将输入作为一个整体,在倒排索引中查找准确的词项,并进行相关度算分
  - 可以通过 Constant score将查询换成一个Filtering,避免算分,并利用缓存,提高性能
  ```
  POST /products/_search
  {
    //"explain": true,
    "query": {
      "term": {
        "productID.keyword": {
          "value": "XHDK-A-1293-#fJ3"
        }
      }
    }
  }
  
  POST /products/_search
  {
    "explain": true,
    "query": {
      "constant_score": {
        "filter": {
          "term": {
            "productID.keyword": "XHDK-A-1293-#fJ3"
          }
        }
  
      }
    }
  }
  ```
* Full Text
  - Match Query / Match Phrase Query / Query String Query
  - 索引和搜索时都会进行分词
  - 查询会对每个词项逐个进行底层的查询,在将结果进行合并,并为每一个文档生成一个算分
  - Precision & Recall

## 4.2 结构化搜索(Structured search)
* 指对于结构化数据的搜索
  - 日期,bool和数字都是结构化的
  - 文本也可以是结构化的
  - 对于有精准的格式的结构化数据,我们可以进行逻辑操作,包括比较范围或判定大小
    - gt 大于
    - lt 小于
    - gte 大于等于
    - lte 小于等于
  - 结构化的文本可以做精确匹配或部分匹配, Term / Prifix
  - 结构化结果只有是或否两个值
  - 处理多值字段，term 查询是包含，而不是等于
  ```
  #数字类型 Term
  POST products/_search
  {
    "profile": "true",
    "explain": true,
    "query": {
      "term": {
        "price": 30
      }
    }
  }
  
  #数字类型 terms
  POST products/_search
  {
    "query": {
      "constant_score": {
        "filter": {
          "terms": {
            "price": [
              "20",
              "30"
            ]
          }
        }
      }
    }
  }
  
  #数字 Range 查询
  GET products/_search
  {
      "query" : {
          "constant_score" : {
              "filter" : {
                  "range" : {
                      "price" : {
                          "gte" : 20,
                          "lte"  : 30
                      }
                  }
              }
          }
      }
  }
  
  # 日期 range
  POST products/_search
  {
      "query" : {
          "constant_score" : {
              "filter" : {
                  "range" : {
                      "date" : {
                        "gte" : "now-1y"
                      }
                  }
              }
          }
      }
  }
  #exists查询
  POST products/_search
  {
    "query": {
      "constant_score": {
        "filter": {
          "exists": {
            "field": "date"
          }
        }
      }
    }
  }
  ```

## 4.3 搜索的相关性算分
* 相关性 Relevance
  - TF-IDF, BM 25
* 词频 Term Frequency
* 逆文档频率 IDF
* score(q,d) = coord(q,d) * queryNorm(q) * E(tf(t in d)) * idf(t)2 * boost(t) * norm(t,d))
* BM 25
  - 和TF-IDF相比,当TF无限增加时,BM 25算分会趋于一个数值
* explain API
  - "explain": true
* Boosting Relevance

## 4.4 Query & Filtering与多字符串多字段查询
* bool查询
  - 一个或多个查询字句的组合
  - must 必须匹配,贡献算分
  - should 选择性匹配,贡献算分
  - must_not Filter Context,查询字句,必须不能匹配
  - filter Filter Context,必须匹配,但是不贡献算分
  - 同一层级下的竞争字段具有相同的权重
  - 通过嵌套bool查询可以改变对算分的影响
```
{
  "query": {
    "bool" : {
      "must" : {
        "term" : { "price" : "30" }
      },
      "filter": {
        "term" : { "avaliable" : "true" }
      },
      "must_not" : {
        "range" : {
          "price" : { "lte" : 10 }
        }
      },
      "should" : [
        { "term" : { "productID.keyword" : "JODL-X-1937-#pV7" } },
        { "term" : { "productID.keyword" : "XHDK-A-1293-#fJ3" } }
      ],
      "minimum_should_match" :1
    }
  }
}
```

## 4.5 单字符串多字段查询:Dis Max Query
* Disjunction Max Query
  - 将任何与任一查询匹配的文档作为结果返回,采用字段上最匹配的评分最终评分返回
* tie_breaker调整
  - 获得最佳匹配语句的评分_score
  - 将其它匹配语句的评分与tie_breaker调整相乘
  - 对以上评分求和并规范化
```
{
    "query": {
        "dis_max": {
            "queries": [
                { "match": { "title": "Quick pets" }},
                { "match": { "body":  "Quick pets" }}
            ],
            "tie_breaker": 0.2
        }
    }
}
```

## 4.6 Multi Match
* 最佳字段 Best Fields
  - 当字段之间相互竞争有相互关联,如title和body,评分来自最匹配字段
* 多数字段 Most Fields
  - 处理英文内容时,一种常见的手段是,在主字段抽取词干加入同义词,以匹配更多文档
  - 相同的文本加入子字段,以提供更加精确的匹配
* 混合字段 Cross Fields
  - 对于某些实体,如人名,地址,需要在多个字段中确定信息,每个字段只能作为整体的一部分,希望在任何这些列出的字段中找到尽可能多的词
```
{
  "query": {
    "multi_match": {
      "type": "best_fields",
      "query": "Quick pets",
      "fields": ["*_title","body"],
      "tie_breaker": 0.2,
      "minimum_should_match": "20%"
    }
  }
}
```

## 4.7 中文分词
* 混合语言
  - 语言识别 => 不同语言不同索引
* [Elasticsearch IK 分词插件](https://github.com/medcl/elasticsearch-analysis-ik/releases)
* [Elasticsearch hanlp 分词插件](https://github.com/KennFalcon/elasticsearch-analysis-hanlp)
* [分词算法综述](https://zhuanlan.zhihu.com/p/50444885)
* [中科院计算所](http://ictclas.nlpir.org/nlpir/)
* [ansj 分词器](https://github.com/NLPchina/ansj_seg)
* [哈工大的 LTP](https://github.com/HIT-SCIR/ltp)
* [清华大学 THULAC](https://github.com/thunlp/THULAC)
* [斯坦福分词器](https://nlp.stanford.edu/software/segmenter.shtml)
* [Hanlp 分词器](https://github.com/hankcs/HanLP)
* [结巴分词](https://github.com/yanyiwu/cppjieba)
* [KCWS 分词器 (字嵌入 +Bi-LSTM+CRF)](https://github.com/koth/kcws)
* [ZPar](https://github.com/frcchang/zpar/releases)
* [IKAnalyzer](https://github.com/wks/ik-analyzer)

## 4.8 Search template & Index Alias
* 解耦程序 & 搜索DSL
  * boost只需要更新template无需修改前端
  ```
  POST _scripts/tmdb
  {
    "script": {
      "lang": "mustache",
      "source": {
        "_source": [
          "title","overview"
        ],
        "size": 20,
        "query": {
          "multi_match": {
            "query": "{{q}}",
            "fields": ["title","overview"]
          }
        }
      }
    }
  }
  
  // 使用template查询
  POST tmdb/_search/template
  {
      "id":"tmdb",
      "params": {
          "q": "basketball with cartoon aliens"
      }
  }
  ```
* Index Alias
  ```
  curl -XPOST ’http://localhost:9200/_aliases’ -d ’{
      "actions" : [
        { "remove": {"index": "index_v1", "alias": "index" } },
        { "add" :   {"index": "index_v2", "alias": "index" } }
  ] }’
  ```  

## 4.9 Function Score Query
* 在查询结束后,对每一个文档进行一系列的重新算分,根据生成的分数进行排序
* 默认记分值函数
  - Weight: 为每一个文档设置一个简单而不被规范化的权重
  - Field Value Factor: 使用该数值来修改_score, 例如将"热度"和"点赞数"作为算分的参考因素
  - Random Score: 为每一个用户使用一个不同的,随机算分结果
  - 衰减函数: 以某个字段的值为标准,距离某个值越近,得分约高
  - Script Score: 自定义脚本完全控制所有逻辑
* modifier: log1p 平滑度处理
* Boost Mode
  - Multiply: 算分与函数值的乘积
  - Sum: 算分与函数的和
  - Min / Max: 算分与函数去 最大/最小
  - Replace: 使用函数值取代算分
* 随机函数
  - 场景: 网站的广告需要提高展现率
  - 不同用户看到不同的排名,但是同一个用户访问时结国相对一致
  - seed值一致返回结果就一致
```
POST /blogs/_search
{
  "query": {
    "function_score": {
      "query": {
        "multi_match": {
          "query":    "popularity",
          "fields": [ "title", "content" ]
        }
      },
      "field_value_factor": {
        "field": "votes",
        "modifier": "log1p" ,
        "factor": 0.1
      },
      "boost_mode": "sum",
      "max_boost": 3
    }
  }
}

POST /blogs/_search
{
  "query": {
    "function_score": {
      "random_score": {
        "seed": 911119
      }
    }
  }
}
```

## 4.10 Term & Phrase Suggest
* 搜索建议: Suggest API
* 原理: 将输入的文本分解为Token, 然后在索引的字典里查找相似的Term并返回
* Term
  - Suggestion Mode
    - Missing 索引中已经存在就不提供意见
    - Popular 推荐出现频率更高的词
    - Always 无论是否存在都提供建议
  ```
  POST /articles/_search
  {
  
    "suggest": {
      "term-suggestion": {
        "text": "lucen rock",
        "term": {
          "suggest_mode": "always",
          "field": "body",
        }
      }
    }
  }
  ```
* Phrase Suggester
  - Suggest Mode
  - Max Errors: 最多可拼错的Terms数
  - Confidence: 限制返回结果数,默认 1
  ```
  POST /articles/_search
  {
    "suggest": {
      "my-suggestion": {
        "text": "lucne and elasticsear rock hello world ",
        "phrase": {
          "field": "body",
          "max_errors":2,
          "confidence":0,
          "direct_generator":[{
            "field":"body",
            "suggest_mode":"always"
          }],
          "highlight": {
            "pre_tag": "<em>",
            "post_tag": "</em>"
          }
        }
      }
    }
  }
  ```

## 4.11 自动补全与上下文提示
* Completion Suggester
* 对性能要求比较苛刻
* 原理: 将Analyze的数据编码成FST和索引一起存放,FST会被ES加载进内存
* FST只能用于前缀查找
* 必要步骤
  - Mapping 定义type: completion
  ```
  PUT articles
  {
    "mappings": {
      "properties": {
        "title_completion":{
          "type": "completion"
        }
      }
    }
  }
  // 查询
  POST articles/_search?pretty
  {
    "size": 0,
    "suggest": {
      "article-suggester": {
        "prefix": "elk ",
        "completion": {
          "field": "title_completion"
        }
      }
    }
  }
  ```
* Context Suggester
  - completion suggester 的扩展
  - 可以在搜索中加入更多上下文信息
  - 不同的category给予不同的suggester
  ```
  PUT comments/_mapping
  {
    "properties": {
      "comment_autocomplete":{
        "type": "completion",
        "contexts":[{
          "type":"category",
          "name":"comment_category"
        }]
      }
    }
  }
  
  POST comments/_doc
  {
    "comment":"I love the star war movies",
    "comment_autocomplete":{
      "input":["star wars"],
      "contexts":{
        "comment_category":"movies"
      }
    }
  }
  
  POST comments/_doc
  {
    "comment":"Where can I find a Starbucks",
    "comment_autocomplete":{
      "input":["starbucks"],
      "contexts":{
        "comment_category":"coffee"
      }
    }
  }
  
  
  POST comments/_search
  {
    "suggest": {
      "MY_SUGGESTION": {
        "prefix": "sta",
        "completion":{
          "field":"comment_autocomplete",
          "contexts":{
            "comment_category":"coffee"
          }
        }
      }
    }
  }  
  ```
* 精准度
  - Completion > Phrase > Term
* 照会率
  - Term > Phrase > Completion
* 性能
  - Completion > Phrase > Term

# 5.0 分布式特性以及分布式搜索的机制
## 5.1 配置跨集群搜索
* 水平扩展的痛点
  - 单集群
    - 水平扩展时,节点数不能无限增加
    - 单集群的meta信息(节点,索引,集群状态)过多,会导致更新压力变大,单个Active Master会成为性能瓶颈,导致整个集群无法工作
  - 早期版本
    - 通过Tribe Node可以实现多集群访问的需求,但存在一定的问题
    - Tribe Node会以 Client Node的方式加入每个集群,集群中Master节点的任务更需要Tribe Node的回应才能继续
    - Tribe Node不保存 Cluster State信息,一旦重启,初始化很慢
    - 当多个集群存在索引重名的情况时,只能设置一种Prefer规则
* 跨集群搜索Cross Cluster Search
  - Tribe Node已被Deprecated
  - 允许任何节点扮演federated节点,以轻量方式,将搜索请求进行代理
  - 不需要以client Node的形式加入其它集群
  ```
  curl -XPUT "http://localhost:9200/_cluster/settings" -H 'Content-Type: application/json' -d'
  {"persistent":{"cluster":{"remote":{"cluster0":{"seeds":["127.0.0.1:9300"],"transport.ping_schedule":"30s"},"cluster1":{"seeds":["127.0.0.1:9301"],"transport.compress":true,"skip_unavailable":true},"cluster2":{"seeds":["127.0.0.1:9302"]}}}}}'
  
  curl -XPUT "http://localhost:9201/_cluster/settings" -H 'Content-Type: application/json' -d'
  {"persistent":{"cluster":{"remote":{"cluster0":{"seeds":["127.0.0.1:9300"],"transport.ping_schedule":"30s"},"cluster1":{"seeds":["127.0.0.1:9301"],"transport.compress":true,"skip_unavailable":true},"cluster2":{"seeds":["127.0.0.1:9302"]}}}}}'

  curl -XPUT "http://localhost:9202/_cluster/settings" -H 'Content-Type: application/json' -d'
  {"persistent":{"cluster":{"remote":{"cluster0":{"seeds":["127.0.0.1:9300"],"transport.ping_schedule":"30s"},"cluster1":{"seeds":["127.0.0.1:9301"],"transport.compress":true,"skip_unavailable":true},"cluster2":{"seeds":["127.0.0.1:9302"]}}}}}'
  ```

## 5.2 集群分布式模型
* 分布式特性
  - 存储的水平扩容,支持PB级数据
  - 高可用,部分节点停止服务,整个集群的服务不受影响
* Elasticsearch 的分布式架构
  - 不同的集群通过不同的名字来区分,默认名"elasticsearch"
  - 通过修改配置文件,或者在命令行中 -E cluster.name=hokehoke 进行设定
* 节点
  - 本质上是一个JAVA进程
  - 生产环境一般建议只运行一个实例
  - 每一个节点都有名字 -E node.name=node1
  - 每一个节点启动之后,会分配一个UID,保存在data目录下
* Coordinating Node
  - 处理请求的节点
  - 路由请求到正确的节点,例如创建索引的请求,需要路由到Master节点
  - 所有节点默认都是Coordinating Node
  - 通过将其他类型设置成False, 使其成为Dedicated Coordinating Node
* Data Node
  - 可以保存数据的节点,叫做Data Node
  - 节点启动后,默认就是数据节点,可以设置node.data:false禁止
  - 职责: 保存分片数据,在数据扩展上起到了至关重要的作用(由Master Node决定如何分片)
  - 通过增加数据节点可以解决数据水平扩展和解决数据单点问题
* Master Node
  - 职责: 处理创建,删除索引等请求.决定分片被分配到哪个节点,维护并且更新 Cluster State
  - Master节点非常重要,在部署上需要考虑解决单点问题
  - 为一个集群设置多个Master节点,每个节点只承担Master的单一角色
  - 一个集群,支持配置多个Master Eligible节点,这些节点可以在必要时参与主流程,成为Master节点
  - 每个节点启动后默认就是一个Master Eligible节点
  - 可以设置 node.master: false禁止
  - 集群内第一个Master Eligible节点启动时候,它会将自己选举成Master节点
* 集群状态
  - 集群状态信息(Cluster State)维护了一个集群中必要的信息
    - 所有节点的信息
    - 所有的索引和其相关的 Mapping与 Setting信息
    - 分片的路由信息
  - 每个节点上都保存了集群的状态信息
  - 但是只有Master节点才能修改集群的状态信息,并负责同步给其他节点
* Master Eligible Nodes 选主过程
  - 相互Ping对方,Node id 低的会成为被选举的节点
  - 其他节点会加入集群,但是不承担Master节点的角色,一旦发现被选中的主节点丢失,就会选出新的Master节点
* 脑裂问题
  - Quorum = (Master 数 / 2) + 1
  - 当3个 Master Eligible时,设置 discovery.zen.minimum_maser_nodes = 2
  - 7.0以后不需要设置

## 5.3 分片与集群的故障转移
* 分片是Elasticsearch分布式存储的基石
* 通过主分片,将数据分布在所有节点上
* 主分片在创建时指定,后续默认不能修改,如要修改需要重新创建索引
* Replica Shard: 数据可用性
  - 一旦主分片丢失, 副本分片可以Promote成主分片
  - 可以动态调整, 每个节点上都有完备的数据
  - 可以通过增加副本分片提升系统的读取性能
* 分片数设定
  - 主分片数过小: 如果索引增长很快,集群无法通过增加节点实现对这个索引的数据扩展
  - 主分片数过大: 导致单个shard容量很小,引发单个节点有过多的分片,影响性能
  - 副本分片过多: 降低集群整体的写入能力
* health API
  - Green: 健康状态, 所有主分片和副本分片都能用  
  - Yellow: 亚健康状态, 所有主分片可用, 部分副本分片不可用
  - Red: 不健康状态, 部分主分片不可用

## 5.4 文档分布式存储
* 文档会存储在具体的某个主分片和副本分片上
* 文档到分片的映射算法shard = hash(_routing) % number_of_primary_shards
  - Hash算法确保文档会均匀的分布在所用分片上,充分利用硬件资源
  - 默认的_routing值是文档id
  - 可以自行设置_routing数值
  - 这就是primary不能随便修改的原因

## 5.5 分片及其生命周期
* ES的最小工作单元是分片, 相当与Lucene的index
* 倒排索引采用Immutable design, 一旦生成不可修改
  - 无需考虑并发写文件的问题,避免锁带来的性能问题
  - 有效利用文件系统缓存
  - 缓存容易生成和维护, 数据可被压缩
  - 如果需要一个新的文档可被搜索, 需要重建整个索引
* Lucene index
  - 单个倒排索引被称为Segment, Segment是自包含不可变的
  - 多个Segment组成index, 对于ES的shard
  - 新文档写入时会生成新的Segment，查询时会查询所有Segment并对结果汇总
  - commit point记录所有Segment信息
  - 删除文档的信息保存在.del文件中
* Refresh
  - 将index buffer写入Segment的操作叫做Refresh，Refresh不执行fsync操作
  - Refresh频率默认一秒一次,可通过index.refresh_interal配置
  - Refresh后数据就可以被搜索到了
  - 系统有大量的数据写入,就会产生很多的Segment
  - index buffer被占满后时，会触发Refresh，默认是JVM的10%
* Transaction log
  - Segment 写入磁盘的过程相对耗时, 借助文件系统缓存, Refresh时先将Segment写入缓存以开放查询
  - 为了保证数据不丢失, index文档时同时写Transaction log,高版本开始Transaction log默认落盘
  - 每个分片有一个Transaction log
  - 在ES Refresh时, index buffer会被清空,Transaction log不会清空
* Flush
  - 调用Refresh, index Buffer清空并且Refresh
  - 调用fsync将缓存Segment写入磁盘
  - 清空Transaction log
  - 默认30分钟一次
  - Transaction log满也会触发(512MB)
* Merge
  - Segment文件很多需要被定期合并
  -  删除.del记录的已被删除文档
  - ES 和 luence会自动merge
  - POST my_index/forcemerge

## 5.6 剖析分布式查询及相关性算分
* ES的搜索分Query 和 Fetch 两阶段
  - 1. 节点收到请求后会以Coordinating 节点的身份发送查询请求 
  - 2. 被选中的分片执行查询, 进行排序,然后每个分片都会返回from + size 个排序后的文档id和排序值给Coordinating节点
  - 3. Coordinating Node 会将Query阶段, 从每个分片获取的排序后的文档id列表进行重新排序
  - 4. 以mulit get 请求的方式到相应的分片获取详细的文档数据
* 性能问题
  - 每个分片上需要查的文档数 = from + size
  - 最终 Coordinating Node 需要处理: number_of_shard * (from + size)
  - 深度分页
* 相关性算分
  - 每个分片都基于自己的分片上的数据进行相关度计算,因此导致算分偏离
  - 数据量不大的情况,可将主分片数设为1
  - 数据量足够大,只要保证文档均匀分散在各个分片上,结果一般不会出现偏差
  - 使用 DFS Query than Fetch, _search?search_type=dfs_query_than_fetch,消耗cpu和内存,不推荐使用


## 5.7 排序及Doc Values&Fielddata
* 默认采用相关性算分对结果进行降序排序
* 可以通过设定sorting参数,自行设定排序
* 不指定_score, 算分为null
* text默认不能进行排序, fielddata=true
* ES的两种排序实现
  - Field data
  - Doc Values(列式存储, 对text无效)
* 关闭Doc Values
  - 默认启用,可通过Mapping设置关闭
  - 关闭可增加索引速度，减少磁盘空间
  - 重新打开需要重建索引
  - 明确不需要进行排序和聚合分析时推荐关闭

|          |     Doc Values            |    Field data             |
| -------- | :-------------------------| :------------------------ |
| 何时创建 | 索引时,和倒排索引一起     | 搜索时动态创建            |
| 创建位置 | 磁盘文件                  | JVM Heap                  |
| 优点     | 避免大量内存使用          | 索引速度快,不占用额外磁盘 |
| 缺点     | 降低索引速度,额外磁盘空间 | 动态开销快占用过多JVMHeap |
| 缺省值   | ES 2.x 之后               | ES 1.x 及之前             |

## 5.8 分页与遍历：From, Size, Search After & Scroll API
* 默认情况下,按照相关性算分排序,返回前10条
* 分布式系统的深度分页问题
  - From 990, size 10会在每一个分片上都先获取1000个文档然后通过 Coordinating Node 聚合所有结果再排序选取
  - 页数越深,占用内存越多,为避免过多内存开销,ES默认限定10000个文档
* Search After
  - 避免深度分页性能问题,可以实时获取下一页文档信息
  - 不支持指定页数From,只能往下翻
  - 进一步搜索需要指定sort,并保证值是唯一的(可以通过加入_id保证唯一性)
  - sort[ {"age": "desc"}, {"_id": "asc"}]
  - 然后使用上一次,最后一个文档的sort值进行查询
  - 原理: 通过唯一排序值定位,将每次要处理的文档数控制在size
  ```
  POST users/_search
  {
      "size": 1,
      "query": {
          "match_all": {}
      },
      "search_after":
          [ 
            10,
            "ZQ0vYGsBrR8X3IP75QqX"
          ],
      "sort": [
          {"age": "desc"} ,
          {"_id": "asc"}    
      ]
  }
  ```
* Scroll API
  - 创建一个快照，有新的数据写入后无法被查询到
  - 每次查询后输入上一次的 Scroll id
  ```
  POST /users/_search?scroll=5m
  {
      "size": 1,
      "query": {
          "match_all" : {
          }
      }
  }
  
  
  POST users/_doc
  {"name":"user5","age":50}
  POST /_search/scroll
  {
      "scroll" : "1m",
      "scroll_id" : "DXF1ZXJ5QW5kRmV0Y2gBAAAAAAAAAWAWbWdoQXR2d3ZUd2kzSThwVTh4bVE0QQ=="
  }
  ```
* 不同的搜索类型和使用场景
  - Regular: 需要获取顶部部分文档
  - Scroll: 需要获取全部文档，例如导出全部数据
  - Pagination: From和size，如果需要深度分页则使用search_after

## 5.9 处理并发读写操作
* ES采用乐观并非控制
  - 假设冲突不会发生
* ES的文档是不可变的
  - 如果更新一个文档，就会将该文档标记为删除
  - 同时增加一个全新的文档，文档的version字段加1
* 内部版本控制
  - if_seq_no + if_primary_term
* 外部版本控制
  - version + version_type = external

```
PUT products/_doc/1?if_seq_no=1&if_primary_term=1
{
  "title":"iphone",
  "count":100
}

PUT products/_doc/1?version=30000&version_type=external
{
  "title":"iphone",
  "count":100
}
```

# 6.0 聚合分析
## 6.1 Bucket & Metric
* Aggregation语法
  - 属于search的一部分,建议size指定为0
  - 自定义聚合名
  - 可包含多个同级的聚合查询
* Metric Aggregation
  - 单值分析: 只输出一个分析结果, min,max,avg,sum, Cardinality
  - 多值分析: 输出多个分析结果, stats, extended stats, percentile, percentile rank, top hits
* Bucket Aggregation
  - 按照一定的规则,将文档分配到不同的桶中,从而达到分类的目的
  - Terms, 数字类型的 Range / Data Range / Histogram / Data Histogram
  - 支持嵌套,在桶里再做分桶
* Terms Aggregation
  - 字段需要打开fielddata
  - keyword默认支持doc_values
  - Text需要在mapping中enable
* [Metric](https://www.elastic.co/guide/en/elasticsearch/reference/7.1/search-aggregations-metrics.html)
* [Bucket](https://www.elastic.co/guide/en/elasticsearch/reference/7.1/search-aggregations-bucket.html)

## 6.2 Pipeline
* 支持对聚合分析的结果，再次进行聚合分析
  - 结果和其他的聚合同级
  - 通过buckets_path指定路径
  ```
  POST employees/_search
  {
    "size": 0,
    "aggs": {
      "jobs": {
        "terms": {
          "field": "job.keyword",
          "size": 10
        },
        "aggs": {
          "avg_salary": {
            "avg": {
              "field": "salary"
            }
          }
        }
      },
      "min_salary_by_job":{
        "min_bucket": {
          "buckets_path": "jobs>avg_salary"
        }
      }
    }
  }
  ```
* Sibling 结果和现有分析结果同级
  - Max, Min, Avg & Sum Bucket
  - Stats, Extended Status BUcket
  - Percentiles Bucket
* Parent 结果内嵌到现有的聚合分析结果之中
  - Derivative (求异)
  - Cumultive Sum (累计求和)
  - Moving Function (滑动窗口)
* [pipeline](https://www.elastic.co/guide/en/elasticsearch/reference/7.1/search-aggregations-pipeline.html)

## 6.3 作用范围与排序
* ES聚合分析的默认作用范围是query的查询结果集
* 同时ES还支持以下方式改变聚合的作用范围
  - Filter
  - Post Filter
  - Global: 忽略query条件而进行aggregation
* 排序
  - 指定order, 按照count和key进行排序
  - 默认情况按照count降序排序
  - 指定size就能返回相应的桶

## 6.4 原理与精准度问题
* 数据量,精准度,实时性只能选2
* 当数据量变大,分片变多,ES会牺牲一定的精准度来保证实时性
* Terms Aggregation 的返回值中有两个特殊数值
  - doc_count_error_upper_bound: 被遗漏的term分桶,包含的文档可能有最大值
  - sum_other_doc_count: 除了返回结果的term以外,其他term的文档总数
* 如何解决Terms不准问题
  - 数据量不大的时候设置Primary shard为1
  - 在分布式数据上设置shard_size参数提高精确度,但会对系统性能有一定的损耗
  - shard size = size * 1.5 + 10
  ```
  GET my_flights/_search
  {
    "size": 0,
    "aggs": {
      "weather": {
        "terms": {
          "field":"OriginWeather",
          "size":3,
          "shard_size":4,
          "show_term_doc_count_error":true
        }
      }
    }
  }
  ```

# 7.0 数据建模
## 7.1 Nested 对象
* 关系性数据库的范式化设计(Normalization)
  - 范式化设计的主要目标是减少不必要的更新
  - 副作用:慢查询,数据越范式化就需要越多的join
  - 范式化节约了存储空间，但存储空间越来越便宜
  - 范式化简化了更新，但数据读取操作可能更慢
* 反范式化设计 Denormalization
  - 数据Flattening，不使用关联关系，而是在文档中保存冗余的数据拷贝
  - 无需jion操作，数据读取性能好
  - ES通过压缩_source字段，

## 7.2 文档的子父关系 
## 7.3 Update By Query & Reindex API
## 7.4 Ingest Pipeline & Painless Script
## 7.5 数据建模最近实践

# 8.0 数据保护
## 8.1 集群身份认证与用户鉴权
* ES在默认安装之后不提供任何形式的安全防护
* 错误的配置信息导致公网可以访问ES集群
  - server.host 0.0.0.0
* 数据安全性的基本需求
  - 身份认证
  - 用户鉴权
  - 传输加密
  - 日志审计
* 一些免费的方案
  - Nginx 反向代理
  - [Search Guard](https://search-guard.com)
  - [ReadOnly REST](https://github.com/sscarduzio/elasticsearch-readonlyrest-plugin)
  - [X-Pack Basic版](https://www.elastic.co/what-is/elastic-stack-security)
* Authentication 身份认证
  - 认证体系: 提供用户名和密码, 提供秘匙或kerberos票据
  - Realms: X-Pack中的认证服务
    - 内置Realms(免费): File / Native
    - 外部Realms(收费): LDAP / Active Directory / PKI / SAML / Kerberos
* RBAC 用户鉴权
  - Role Based Access Control
  - User: The authenticated user
  - Role: A named set of permissions
  - Permission: A set of one or more privileges against a secured resource
  - Privilege: A named group of 1 or more actions that user may execute against a secured resource
* Privilege
  - Cluster Privilege: all / monitor / manager / manage_index / manage_index_template / manage_rollup
  - Indices Privilege: all / create_index / delete / delete_index / index / manage / read / write / view_index_metadata
* 内置用户和角色
  - elastic / kibana / logstash_system / beats_system / apm_system / Remote_monitoring_user
* 使用Security API创建用户
  ```
  POST /_security/user/jacknich
  {
    "password" : "jA&*021",
    "roles" : [ "admin", "other_role1"],
    "full_name" : "Jack NIcholson",
    "email" : "jacknich@xample.com",
    "metadata" : {
      "intelligence" : 7
    }
  }
  ```
* 开启并配置 X-Pack的认证与鉴权
  ```
  bin/elasticsearch -E node.name=node0 -E cluster.name=geektime -E path.data=node0_data -E http.port=9200 -E xpack.security.enabled=true
  
  #运行密码设定的命令，设置ES内置用户及其初始密码。
  bin/elasticsearch-setup-passwords interactive
  
  # 修改 kibana.yml
  elasticsearch.username: "kibana"
  elasticsearch.password: "changeme"
  
  #启动。使用用户名，elastic，密码elastic
  ./bin/kibana
  ```

# 监控
* _cluster/health
* yellow
  * 所有的主分片已经分片了，但至少还有一个副本是缺失的。不会有数据丢失，所以搜索结果依然是完整的。不过，你的高可用性在某种程度上被弱化。如果 更多的 分片消失，你就会丢数据了。把 yellow 想象成一个需要及时调查的警告。
* red
  * 至少一个主分片（以及它的全部副本）都在缺失中。这意味着你在缺少数据：搜索只能返回部分数据，而分配到这个分片上的写入请求会返回一个异常。 
* GET _cluster/health?level=indices
* GET _nodes/stats
* GET my_index/_stats
* GET my_index,another_index/_stats 
* GET _all/_stats 
* GET /_cat/shards 查看所有分片状态

# config
```
# ======================== Elasticsearch Configuration =========================
#
# NOTE: Elasticsearch comes with reasonable defaults for most settings.
# Before you set out to tweak and tune the configuration, make sure you
# understand what are you trying to accomplish and the consequences.
#
# The primary way of configuring a node is via this file. This template lists
# the most important settings you may want to configure for a production cluster.
#
# Please see the documentation for further information on configuration options:
# <http://www.elastic.co/guide/en/elasticsearch/reference/current/setup-configuration.html>
#
# ---------------------------------- Cluster -----------------------------------
#
# Use a descriptive name for your cluster:
# 集群名称，默认是elasticsearch
# cluster.name: my-application
#
# ------------------------------------ Node ------------------------------------
#
# Use a descriptive name for the node:
# 节点名称，默认从elasticsearch-2.4.3/lib/elasticsearch-2.4.3.jar!config/names.txt中随机选择一个名称
# node.name: node-1
#
# Add custom attributes to the node:
# 
# node.rack: r1
#
# node.name: "node1"
# 是否有资格成为主节点
# node.master: true
# 是否存储索引数据
# node.data: true
# 默认索引分片数
# index.number_of_shards: 3
# 默认索引副本数
# index.number_of_replicas: 1
# ----------------------------------- Paths ------------------------------------
#
# Path to directory where to store the data (separate multiple locations by comma):
# 可以指定es的数据存储目录，默认存储在es_home/data目录下
# path.data: /path/to/data
#
# Path to log files:
# 可以指定es的日志存储目录，默认存储在es_home/logs目录下
# path.logs: /path/to/logs
#
# ----------------------------------- Memory -----------------------------------
#
# Lock the memory on startup:
# 锁定物理内存地址，防止elasticsearch内存被交换出去,也就是避免es使用swap交换分区
# bootstrap.memory_lock: true
#
#
#
# 确保ES_HEAP_SIZE参数设置为系统可用内存的一半左右
# Make sure that the `ES_HEAP_SIZE` environment variable is set to about half the memory
# available on the system and that the owner of the process is allowed to use this limit.
# 
# 当系统进行内存交换的时候，es的性能很差
# Elasticsearch performs poorly when the system is swapping the memory.
#
# ---------------------------------- Network -----------------------------------
#
#
# 为es设置ip绑定，默认是127.0.0.1，也就是默认只能通过127.0.0.1 或者localhost才能访问
# es1.x版本默认绑定的是0.0.0.0 所以不需要配置，但是es2.x版本默认绑定的是127.0.0.1，需要配置
# Set the bind address to a specific IP (IPv4 or IPv6):
#
# network.host: 192.168.0.1
#
#
# 为es设置自定义端口，默认是9200
# 注意：在同一个服务器中启动多个es节点的话，默认监听的端口号会自动加1：例如：9200，9201，9202...
# Set a custom port for HTTP:
#
# http.port: 9200
#
# For more information, see the documentation at:
# <http://www.elastic.co/guide/en/elasticsearch/reference/current/modules-network.html>
#
# --------------------------------- Discovery ----------------------------------
#
# 当启动新节点时，通过这个ip列表进行节点发现，组建集群
# 默认节点列表：
# 127.0.0.1，表示ipv4的回环地址。
#	[::1]，表示ipv6的回环地址
#
# 在es1.x中默认使用的是组播(multicast)协议，默认会自动发现同一网段的es节点组建集群，
# 在es2.x中默认使用的是单播(unicast)协议，想要组建集群的话就需要在这指定要发现的节点信息了。
# 注意：如果是发现其他服务器中的es服务，可以不指定端口[默认9300]，如果是发现同一个服务器中的es服务，就需要指定端口了。
# Pass an initial list of hosts to perform discovery when new node is started:
# 
# The default list of hosts is ["127.0.0.1", "[::1]"]
#
# discovery.zen.ping.unicast.hosts: ["host1", "host2"]
#
#
#
#
# 通过配置这个参数来防止集群脑裂现象 (集群总节点数量/2)+1
# Prevent the "split brain" by configuring the majority of nodes (total number of nodes / 2 + 1):
#
# discovery.zen.minimum_master_nodes: 3
#
# For more information, see the documentation at:
# <http://www.elastic.co/guide/en/elasticsearch/reference/current/modules-discovery.html>
#
# ---------------------------------- Gateway -----------------------------------
#
# Block initial recovery after a full cluster restart until N nodes are started:
# 一个集群中的N个节点启动后,才允许进行数据恢复处理，默认是1
# gateway.recover_after_nodes: 3
#
# For more information, see the documentation at:
# <http://www.elastic.co/guide/en/elasticsearch/reference/current/modules-gateway.html>
#
# ---------------------------------- Various -----------------------------------
# 在一台服务器上禁止启动多个es服务
# Disable starting multiple nodes on a single system:
#
# node.max_local_storage_nodes: 1
#
# 设置是否可以通过正则或者_all删除或者关闭索引库，默认true表示必须需要显式指定索引库名称
# 生产环境建议设置为true，删除索引库的时候必须显式指定，否则可能会误删索引库中的索引库。
# Require explicit names when deleting indices:
#
# action.destructive_requires_name: true
```
# 便利工具
## 命令
```
curl -XGET 'localhost:9200/_analyze?analyzer=jp_search_analyzer' -d '5ヶ月'
curl -XPUT "http://localhost:9200/lumine_search_all_stg_01_v20181128" -d @settings_20181128.json -H "Content-Type: application/json"; echo
```
```
curl -XPOST ’http://localhost:9200/_aliases’ -d ’{
    "actions" : [
      { "add" : { "index" : "bookpass_search_v20190625", "alias" : "bookpass_search" } }
] }’
```

## reroute
```
#!/usr/bin/env python
#name: recovery.py

import requests
import json
host = "http://localhost:9200/_cluster/allocation/explain"
s= requests.Session()
def reroute_shard(index,shard,node):
    data = {
    "commands" : [
        {
          "allocate_stale_primary" : {
              "index" : index, "shard" : shard, "node" : node, "accept_data_loss": True
          }
        }
    ]
   }
    print data
    url = "http://localhost:9200/_cluster/reroute"
    res = s.post(url,json=data)
    print res

def get_node(line):
    if "UNASSIGNED" in line:
        line = line.split()
        index = line[0]
        shard = line[1]
        if line[2] != "p":
            return
        body = {
           "index": index,
           "shard": shard,
           "primary": True
               }
        res = s.get(host, json = body)
        for store in res.json().get("node_allocation_decisions"):
            if store.get("store").get("allocation_id"):
               node_name = store.get("node_name")
               reroute_shard(index,shard,node_name)
    else:
        return

with open("shards", 'rb') as f:
    map(get_node,f)
```
