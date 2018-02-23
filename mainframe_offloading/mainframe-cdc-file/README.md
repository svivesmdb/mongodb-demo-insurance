
# mainframe-cdc-file

This application polls a directory and sends new JSON files contents to MongoDB.

## Options

The **mainframe-cdc-file** has the following options:


- **mongodb.collection**:: The MongoDB collection to store data to *(default: `**contrat**`)
- **spring.data.mongodb.uri**:: Mongo database URI. default: `**localhost:27017**`)
- **mainframe.directory**:: The directory to poll $$ *(default: `**<none>**`)*



## Build

```
$ mvn clean package

```

## Run

```
$ java -jar target/mainframe-cdc-file-0.0.1.jar --mainframe.directory= --spring.data.mongodb.uri= --mongodb.collection=
```





