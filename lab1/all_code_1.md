# 10-Spark SQL 入门

这里存放着"10-Spark SQL 入门"实验的所有代码

## 2.2 WordCount 案例实战

```scala
sc.textFile("/home/hadoop/data/wordcount.txt").flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).foreach(println)
```

## 四、Spark SQL 的简单使用

person.json 的例子：

```scala
import org.apache.spark.sql.SparkSession

val spark = SparkSession.builder().appName("Spark_SQL_First").getOrCreate()
//加入隐式转换，将RDD转换为DataFrame
import spark.implicits._
//读取json文件
val df = spark.read.json("/home/hadoop/data/person.json")
df.show()
df.printSchema()
//使用select函数查询特定的列，格式$"列名"
//将age列的值加1
df.select($"name", $"age" + 1).show()
//过滤年龄大于21岁的
df.filter($"age" > 21).show()
//按照年龄进行分组
df.groupBy("age").count().show()
df.createOrReplaceTempView("people")
val sqlDF = spark.sql("SELECT * FROM people")
sqlDF.show()
//选择年龄小于20岁的
spark.sql("SELECT * FROM people where age<20").show()
```

## 五、DataFrames&DataSets

```scala
import org.apache.spark.sql.SparkSession
//创建Spark Session
val spark = SparkSession.builder().appName("Spark_SQL_First").getOrCreate()
//读取json文件
val df = spark.read.json("/home/hadoop/data/person.json")
df.show()
//使用collect以Array形式返回DataFrame的所有Rows
df.collect()
//使用count返回DataFrame的所有Rows数目
df.count()
//使用first与head返回第一行数据
df.first()
df.head()
//使用take函数取前N条数据，这里取前两条
df.take(2)
//使用columns函数查询列名，传入参数为列的索引，从0开始
df.columns(0)
df.columns(1)
//使用dtypes函数以Array形式返回全部的列名和数据结构
df.dtypes
//也可以指定参数，参数为列名的索引，从0开始，返回指定索引的数据
df.dtypes(0)
df.dtypes(1)
//使用explain打印执行计划到控制台
df.explain()
//使用persist函数数据持久化
df.persist()
//打印树形结构的schema
df.printSchema()
//使用指定SQL进行过滤
df.filter($"age">20).show()
//使用sort按照age排序
df.sort("age").show()
//使用Limit限制打印的条数
df.limit(2).show()
```

person2.json 的例子：

```scala
val df2=spark.read.json("/home/hadoop/data/person2.json")
df.intersect(df2).show()
```

people.txt 的例子：

```scala
//将一个RDD隐式转换为一个DataFrame
import spark.implicits._
//从文本文件中创建一个RDD，并且将其转换为DataFrame
case class Person(name:String,age:Int)
val peopleDF = spark.sparkContext.textFile("/home/hadoop/data/people.txt").map(_.split(",")).map(attr => Person(attr(0), attr(1).trim.toInt)).toDF()
//注册一张临时表，表名为people
peopleDF.createOrReplaceTempView("people")
//使用SQL语句查询13到19岁之间的people
val teenagersDF = spark.sql("SELECT name, age FROM people WHERE age BETWEEN 13 AND 19")
//通过索引查询一条记录
teenagersDF.map(person => "Name: " + person(0)).show()
//也可以通过字段名来查询
teenagersDF.map(person => "Name: " + person.getAs[String]("name")).show()
//使用隐式转换ncoder[Map[String, Any]] = ExpressionEncoder()
implicit val mapEncoder = org.apache.spark.sql.Encoders.kryo[Map[String, Any]]
//将所有列按照list指定的元素放入一个map中
teenagersDF.map(person => person.getValuesMap[Any](List("name", "age"))).collect()
```

```scala
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row
//创建一个RDD
val peopleRDD = spark.sparkContext.textFile("/home/hadoop/data/people.txt")
// 创建一个包含Schema的字符串
val schemaString = "name age"
//创建一个StructType类型的Schema
val fields = schemaString.split(" ").map(fieldName => StructField(fieldName,StringType, nullable = true))
val schema = StructType(fields)
// 将RDD转换为Row
val rowRDD = peopleRDD.map(_.split(",")).map(attributes => Row(attributes(0), attributes(1).trim))
// 使用Row和Schema创建一个DataFrame
val peopleDF = spark.createDataFrame(rowRDD, schema)
//创建一张临时表
peopleDF.createOrReplaceTempView("people")
val results = spark.sql("SELECT name FROM people")
//展示查询的第一个字段的结果
results.map(attributes => "Name: " + attributes(0)).show()
```

```scala
//创建一个scala样例类，类似于java bean
case class Person(name: String, age: Long)
//创建DataSet
val caseClassDS = Seq(Person("Andy", 32)).toDS()
caseClassDS.show()
//使用普通的列表来创建一个DataSet
val seqDS = Seq(1, 2, 3).toDS()
//使用map操作对每个元素迭代计算
seqDS.map(_ *2).collect()
//将DataFrame转换为DataSet
val personDS = spark.read.json("/home/hadoop/data/person.json").as[Person]
personDS.show()
```