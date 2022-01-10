chcp 65001
d:
cd D:\Develop\workspace\MARU_APP\bin

java -DCP_CONF=../conf -Dlogback.configurationFile=../conf/logback.xml -Dfile.encoding=UTF-8 -Xms256m -Xmx1024m -Xss128k -XX:+AggressiveOpts -XX:+UseParallelGC -XX:+UseBiasedLocking -XX:NewSize=64m -cp ../lib/*;../lib/apache/*;../lib/tomcat/*;../lib/spring/*;../classes com.pgmate.lib.tomcat.Tomcat8
