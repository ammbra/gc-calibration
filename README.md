# GC Calibration content

This repo contains 2 Quarkus applications that can be run with any JDK>20.
When building the parent `pom.xml`, each application is packaged and a container image is built and pushed to a container registry.

Note: change the container image registry with one where you have access to build and push.

## Package applications, build and push their container images

Make sure you are in the root of this repo and issue the following command:

```shell
mvn verify 
```
## Deploy the applications

Each folder contains a `k8s` folder containing a Kubernetes resources necessary for a minimum deploy.

You can run:

```shell
kubectl apply -f big-prime/k8s/kubernetes-default.yml
kubectl apply -f continuous-editor/k8s/kubernetes-default.yml
```

Once you deployed the applications, you can change the JVM parameters using each `k8s/gc.sh` scripts.

## Use k6 for load testing the applications

You can use the k6 scripts available in `big-prime/k6` and `continuous-editor/k6` to load test the deployed applications.
I used [testkube](https://testkube.io/) to run those tests in Kubernetes cluster.
