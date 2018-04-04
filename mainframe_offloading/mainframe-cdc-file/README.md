
# mainframe-cdc-file

This application polls a directory and sends new JSON files contents to MongoDB.

## Options


By default the service will start listening on the default mongodb port: 27017 on localhost
However this can be changed with the following options:

 - **spring.data.mongodb.database**:: The MongoDB database name (default: `**mainframe**`)
 - **spring.data.mongodb.host**:: Mongo database Host. (default: `**localhost**`)
 - **spring.data.mongodb.host**:: MongoDB port. (default: `**27017**`)
 - **mainframe.directory**:: The directory to poll *(default: `**<none>**`)*
 
Within that mongoDB instance, the service will look into a colloction named: **home** for policies related to home    and in collection **motor** for policies related to motors.

## Build

```
$ mvn clean package

```

## Run

Assuming you have mongodb running locally on localhost:27017

```
$ java -jar target/mainframe-cdc-file-0.0.1.jar --mainframe.directory=/data/mainframe
```


Assuming you have mongodb running on host: 52.10.10.10 and port 27000

```
 $ java -jar target/mainframe-cdc-file-0.0.1.jar --mainframe.directory=/data/mainframe --spring.data.mongodb.host=52.10.10.10 --spring.data.mongodb.port=27000
```



