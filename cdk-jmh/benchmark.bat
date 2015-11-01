echo cdk-1.5.12-SNAPSHOT
call mvn clean install -P cdk-1.5.12-SNAPSHOT
@echo copy
call copy target\benchmark*.jar /Y

echo "cdk-1.5.11"
call mvn clean install package -P cdk-1.5.11
echo copy
call copy target\benchmark*.jar /Y

echo "run cdk-1.5.11 benchmark"
call java -jar benchmarks-1.5.11.jar > benchmarks-1.5.11.txt
echo "run cdk-1.5.12-SNAPSHOT benchmark"
call java -jar benchmarks-1.5.12-SNAPSHOT.jar > benchmarks-1.5.12-SNAPSHOT.txt