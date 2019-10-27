use hive;

DROP TABLE IF EXISTS `week_cnt`;

create table week_cnt(
week int(10),
cnt int(10)
)charset utf8;

DROP TABLE IF EXISTS `movie_rate`;

create table movie_rate(
movieId int(10),
rate int(10)
)charset utf8;

spark-submit --master local[2] \
--class com.shiyanlou.shinelon.ETLJob.AnalyzeMovie \
/home/hadoop/jars/MovieAnalyze-1.0-SNAPSHOT.jar \
/home/hadoop/data/movie.txt
