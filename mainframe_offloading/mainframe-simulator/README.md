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

* Get the motor insurance policies by calling `http://localhost:8080/contracts/motor`. This will return all files under `sample-data/motor`s
