# 20-Spark SQL 进阶

这里存放着"20-Spark SQL 进阶"实验的所有代码

## 二、课前复习

```scala
//创建两个样例类进行匹配文本文件   
case class Emp(empId:Int,name:String,age:Int,depId:String)
case class Dep(depId:String,name:String)

val emp=spark.read.text("/home/hadoop/data/emp.txt").map(_.toString().split(" ")).map(attr=>Emp(attr(0).replace("[","").toInt,attr(1),attr(2).toInt,attr(3).replace("]","")))

val dep=spark.read.text("/home/hadoop/data/dep.txt").map(_.toString().split(" ")).map(attr=>Dep(attr(0).replace("[",""),attr(1).replace("]","")))
emp.join(dep,"depId").show()
```

## 三、 Hive 概述与搭建使用

### 3.6 Hive 的简单使用

```sql 
hive > create table student(id int,name string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ' ';
hive > load data local inpath '/home/hadoop/data/student.txt' into table student;
hive > select * from student;
```

## 四、 Spark SQL 整合 Hive

```shell
# 拷贝配置文件
$ cp /opt/hive-2.3.3/conf/hive-site.xml /opt/spark-2.3.1-bin-hadoop2.6/conf/
# 拷贝jar包
$ cp /opt/src/mysql-connector-java-5.1.46/mysql-connector-java-5.1.46.jar /opt/spark-2.3.1-bin-hadoop2.6/jars/
# 本地模式启动，启动两个线程
$ spark-shell --master local[2]
```

```scala
import spark.implicits._
import org.apache.spark.sql.SparkSession
val sqlContext=new org.apache.spark.sql.hive.HiveContext(sc)
sqlContext.table("student").show()
val spark = SparkSession.builder().appName("hiveContext").getOrCreate()
spark.sql("select * from default.student").show()
select * from default.student;
```

### 4.5 thriftserver 的使用

```shell
# 启动thriftserver服务测试
$ $SPARK_HOME/sbin/start-thriftserver.sh
$ jps -m
# -n 指定用户名
$ $SPARK_HOME/bin/beeline -u jdbc:hive2://localhost:10000 -n hadoop
$ show tables;
```

## 五、 Spark SQL 操作多数据源

```scala
val df=spark.read.json("file:///home/hadoop/data/person.json")
df.write.parquet("hdfs://127.0.0.1:9000/user/spark/data/person.parquet")
df.show()
val df=spark.read.parquet("hdfs://127.0.0.1:9000/user/spark/data/person.parquet")
df.printSchema()
df.show()
```

people.txt 例子相关代码：

```scala
val peopleDF=spark.read.text("file:///home/hadoop/data/people.txt")

peopleDF.write.parquet("hdfs://127.0.0.1:9000/user/spark/data/people.parquet")

val parquetFile=spark.read.parquet("hdfs://127.0.0.1:9000/user/spark/data/people.parquet")
//将DataFrame注册为临时表，提供SQL查询使用
parquetFile.createOrReplaceTempView("people")

spark.sql("select * from people").show()
```

```scala
val df=spark.read.json("file:///home/hadoop/data/person.json")
df.show()
```

```scala
case class Emp(empId:Int,name:String,age:Int,depId:String)
case class Dep(depId:String,name:String)

val emp=spark.read.text("/home/hadoop/data/emp.txt").map(_.toString().split(" ")).map(attr=>Emp(attr(0).replace("[","").toInt,attr(1),attr(2).toInt,attr(3).replace("]","")))

emp.show()
```

### 5.4 JDBC 访问 Mysql

```
$ mysql -u hive -p  #输入密码：hive
```

```sql
use hive;
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bname` varchar(20) NOT NULL,
  `bprice` double NOT NULL,
  `bauthor` varchar(10) NOT NULL,
  `bbuy` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

INSERT INTO `book` VALUES ('1', 'haihdia', '25.3', 'daifh', '1');
INSERT INTO `book` VALUES ('2', 'dhih', '54', 'diadfs', '0');
INSERT INTO `book` VALUES ('3', 'sahifhi', '56', 'hiafsf', '1');
INSERT INTO `book` VALUES ('4', 'daiff', '45.3', 'aaaa', '1');
INSERT INTO `book` VALUES ('5', 'hfihf', '53.6', 'ddd', '0');
INSERT INTO `book` VALUES ('6', 'csafa', '56.1', 'jjjj', '1');
INSERT INTO `book` VALUES ('7', 'saff', '45.2', 'afsfgg', '0');
```

```scala
import java.util.Properties
val pro=new Properties
pro.setProperty("driver","com.mysql.jdbc.Driver")
pro.setProperty("user","hive")
pro.setProperty("password","hive")
val jdbcDF=spark.read.jdbc("jdbc:mysql://127.0.0.1:3306/hive","book",pro)
jdbcDF.show()
```


