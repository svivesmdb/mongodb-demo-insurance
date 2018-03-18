## Development mode

You can easily run the application using docker

Notes:
* Assumption is that the first command is executed from the directory `<repo-root>/mainframe_offloading/mainframe-simulator`

```
host$ docker run --rm -it -p 8080:8080 -p 8001:8001 -v $(pwd):/home/app maven:3.5-jdk-8 /bin/bash
container$ cd /home/app
container$ mvn spring-boot:run
```
In case you want to debug run the application with

```
mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8001
```

instead and attach a remote debugger from your IDE.

## Accessing the service

```
http://localhost:8080/policies //returns all policies
http://localhost:8080/policies?type=motor //returns all policies of type motor
http://localhost:8080/policies?type=home //returns all policies of type home
http://localhost:8080/policies/{policyID}?type=motor //requires the type to be set, because policyIDs are not unique across types
http://localhost:8080/policies/{policyID}?type=home //requires the type to be set, because policyIDs are not unique across types
```
