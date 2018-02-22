
# Mainframe-CDC-File

This application polls a directory and sends new JSON files contents to MongoDB.
The file source provides the contents of a File as a byte array by default.

## Options

The **mainframe-cdc-file** has the following options:


- **mongodb.collection**:: The MongoDB collection to store data to *($$String$$, default: `**contrat**`)*
- **spring.data.mongodb.uri**:: Mongo database URI. default: `**localhost:27017**`)*
- **mainframe.directory**:: The directory to poll $$ *($$String$$, default: `**<none>**`)*


//end::ref-doc[]

## Build

```
$ mvn clean package

```

## Run

```
$ java -jar mainframe-cdc-file.jar --mainframe.directory= --spring.data.mongodb.uri= --mongodb.collection=
```





