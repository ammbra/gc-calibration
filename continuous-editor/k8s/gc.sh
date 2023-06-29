## g1gc tunning
kubectl set env deployment/continuous-editor JDK_JAVA_OPTIONS="-XX:ParallelGCThreads=8 -XX:ConcGCThreads=2" -n gc

## parallel gc
kubectl set env deployment/continuous-editor JDK_JAVA_OPTIONS="-XX:+UseParallelGC" -n gc
kubectl set env deployment/continuous-editor JDK_JAVA_OPTIONS="-XX:+UseParallelGC -XX:GCTimeRatio=5" -n gc

## zgc
kubectl set env deployment/continuous-editor JDK_JAVA_OPTIONS="-XX:+UseZGC" -n gc
kubectl set env deployment/continuous-editor JDK_JAVA_OPTIONS="-XX:+UseZGC -Xmx2816m" -n gc

