# Insurance data service

This service exposes the insurance data sitting in a MongoDB database via HTTP APIs.

In order to follow the instructions you only need to have Docker installed (no need to install/configure maven, java, mongodb on your host)

## Build the service

Your current directory (pwd) is assumed to be the directory named insurance-data-service containing the pom.xml.

```
host$ docker run --name maven-container --rm -it -p 8080:8080 -p 8001:8001 -w /home/app -v $(pwd):/home/app maven:3.5-jdk-8 /bin/bash
maven-container$ mvn clean package
```

## Run

In case you have no MongoDB instance running please refer to the instructions at [Prepare ephemeral MongoDB](#Prepare-ephemeral-MongoDB)

By default, the service will try to connect to a mongodb instance with the following properties:

- **spring.data.mongodb.database**:: The MongoDB database name (default: `**insurance-data**`)
- **spring.data.mongodb.host**:: MongoDB host (default: `**localhost**`)
- **spring.data.mongodb.port**:: MongoDB port (default: `**27017**`)

Within that database, the service will look into a collection named: **home** for policies related to home and in collection **motor** for policies related to motors.

In the following command all options are specified explicitly

```
maven-container$ java -jar target/insurance-data-service-0.0.1.jar --spring.data.mongodb.host=172.17.0.2 --spring.data.mongodb.port=27017 --spring.data.mongodb.database=insurance-data
```

NOTE: The contract Service should point to the same database that was configured when setting up the mainframe-cdc process.

## Prepare ephemeral MongoDB

The following way can be used to quickly spin up a MongoDB instance to run the application. Please read through https://hub.docker.com/_/mongo/ to know about the limitations.

Run a mongodb instance in background

```
host$ docker run --name mainframe-offloading-data -d mongo:3.6
```

Get the IP of that mongodb instance, so it can be accessed from the host or other containers

```
host$ docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mainframe-offloading-data
172.17.0.2
```

Connect to the mongodb with mongo-cli

```
host$ docker run --name mongo-cli-container -it --rm mongo:3.6 /bin/bash
mongo-cli-container$ mongo --host 172.17.0.2
>
```

Then insert a sample policy

```
> use insurance-data
switched to db insurance-data
> db.motor.count()
0
> db.motor.insertOne({"max_coverd":"10000000.0","car_model":"MazdaMX-5","cover_start":"2008-02-2407:11:57.000000","last_ann_premium_gross":"1793.8422460579327","customer_id":"C000140819","last_change":"2018-02-2316:19:38.041704","policy_id":"PC_000000001"})
{
	"acknowledged" : true,
	"insertedId" : ObjectId("5ac49567de76f9e5042fdc2c")
}
> db.motor.count()
1
>
```

When you run the application as described above, you can curl the endpoint and will receive the policy you have inserted.

```
maven-container$ java -jar target/insurance-data-service-0.0.1.jar --spring.data.mongodb.host=172.17.0.2 --spring.data.mongodb.port=27017 --spring.data.mongodb.database=insurance-data
host$ curl http://localhost:8080/policies
[{"_id":{"timestamp":1522832743,"machineIdentifier":14579449,"processIdentifier":-6908,"counter":3136556,"time":1522832743000,"date":"2018-04-04T09:05:43.000+0000","timeSecond":1522832743},"max_coverd":"10000000.0","car_model":"Mazda MX-5","cover_start":"2008-02-24 07:11:57.000000","last_ann_premium_gross":"1793.8422460579327","customer_id":"C000140819","last_change":"2018-02-23 16:19:38.041704","policy_id":"PC_000000001"}]
host$
```