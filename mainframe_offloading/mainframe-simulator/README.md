## Options

 By default the service will start listening on the default mongodb port: 27017 on localhost
 However this can be changed with the following options:

  - **mainframe.repository.base**:: The directory to save the created policies/claims *(default: `**<none>**`)*
  - **mainframe.repository.cdc**:: The directory to save a copy of the json files for the cdc process *(default: `**<none>**`)* 


Assuming the following configurations are used, ulytimately the following foloder structure will be observed:

- **mainframe.repository.base=/mainframe**
- **mainframe.repository.cdc=/cdc**

```
├── cdc
│   ├── C000000001.json.processed
│   ├── claim
│   │   ├── CL_000000001.json.processed
│   │   └── CL_000000002.json.processed
│   └── policy
│       ├── PC_000000001.json.invalid
│       ├── PC_000000002.json.processed
│       ├── PC_000000003.json.processed
│       ├── PC_000000004.json.processed
│       └── PC_000000005.json.processed
└── mainframe
    ├── claim
    │   ├── CL_000000001.json
    │   └── CL_000000002.json
    └── policy
        ├── PC_000000001.json
        ├── PC_000000002.json
        ├── PC_000000003.json
        ├── PC_000000004.json
        └── PC_000000005.json

6 directories, 15 files


```

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


### Get the home insurance policies

Endpoint: `http://localhost:8080/policies?type=home&limit=100`


### Create a policy

Endpoint: `http://localhost:8080/policies?type=home`

A sampe POST Data will look like this:

```
{
    "customer_id": "C000000001",
    "address" : {
        "street" : "Scherfweg 1273",
        "zip" : "4018",
        "city": "Traiskirchen",
        "nationality" : "Austria"

    }
    "date_of_birth": "1927-01-11",
    "email": "jackal1980@outlook.com",
    "first_name": "Griswold",
    "gender": "M",
    "job": "Dolmetscher / Dolmetscherin",
    "last_name": "Urbansky",
    "marital_status": "WIDOWED",
    "number_children": 3,
    "phone":"+43-623-4790653"
}

```

And a reponse will simply be the same document with a policy ID

 ```
 {
     "policy_id": "PC_000000001",
     "customer_id": "C000000001",
     "address" : {
         "street" : "Scherfweg 1273",
         "zip" : "4018",
         "city": "Traiskirchen",
         "nationality" : "Austria"
 
     },
     "date_of_birth": "1927-01-11",
     "email": "jackal1980@outlook.com",
     "first_name": "Griswold",
     "gender": "M",
     "job": "Dolmetscher / Dolmetscherin",
     "last_name": "Urbansky",
     "marital_status": "WIDOWED",
     "number_children": 3,
     "phone":"+43-623-4790653"
 }
 
 ```

### Create a claim

Endpoint: `http://localhost:8080/policies/PC_000000001?type=home`

A sample post Data will look like this:

```
{
    "claim_type":"BUIDLING",
    "claim_amount":93373.4,
    "settled_amount":"93373.4",
    "claim_reason":"FIRE",
    "policy_id": "PC_000000001",
    "claim_date": "2018-02-23 16:14:45"
}
```


And a Return will look like this:

```
{
    "claim_id": "CL_000000002",
    "claim_type":"BUIDLING",
    "claim_amount":93373.4,
    "settled_amount":"93373.4",
    "claim_reason":"FIRE",
    "policy_id": "PC_000000001",
    "claim_date": "2018-02-23 16:14:45"
}
```
