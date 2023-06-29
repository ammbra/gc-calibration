## g1gc tunning
kubectl set env deployment/big-prime JDK_JAVA_OPTIONS="-XX:MaxGCPauseMillis=100" -n gc

## parallel gc
kubectl set env deployment/big-prime JDK_JAVA_OPTIONS="-XX:+UseParallelGC" -n gc
kubectl set env deployment/big-prime JDK_JAVA_OPTIONS="-XX:+UseParallelGC -XX:MaxGCPauseMillis=100" -n gc

## zgc
kubectl set env deployment/big-prime JDK_JAVA_OPTIONS="-XX:+UseZGC" -n gc
kubectl set env deployment/big-prime JDK_JAVA_OPTIONS="-XX:+UseZGC -Xmx1024m" -n gc

