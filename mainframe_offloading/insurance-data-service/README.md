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
> db.customer.count()
0
> db.customer.insert({
    "customer_id" : "C000030006aaa",
    "address" : {
        "street" : "Tiergartenstraße 462",
        "zip" : "48555",
        "city" : "Buchloe",
        "nationality" : "Germany"
    },
    "date_of_birth" : "1933-06-13",
    "email" : "cheapest1927@yandex.com",
    "first_name" : "Lucia",
    "gender" : "F",
    "job" : "Buchhalter / Buchhalterin",
    "last_change_home_customer" : "2018-02-23 16:36:30",
    "last_name" : "Hoover",
    "marital_status" : "WIDOWED",
    "number_children" : NumberDecimal("2"),
    "phone" : "+49-03-291-58969",
    "home_insurance" : [
        {
            "policy_id" : "P000014084",
            "quote_day" : ISODate("1999-04-03T22:00:00Z"),
            "cover_start" : ISODate("1999-04-30T22:00:00Z"),
            "last_annual_premium_gross" : NumberDecimal("202.15"),
            "policy_status" : "Live",
            "last_change" : "2018-02-23 16:14:24",
            "coverage" : [
                {
                    "type" : "BUILDING",
                    "sum_insured" : NumberDecimal("1000000"),
                    "malus_bonus" : NumberDecimal("6"),
                    "last_change" : "2018-02-23 16:14:24"
                },
                {
                    "type" : "PERSONAL_ITEMS",
                    "sum_insured" : NumberDecimal("50000"),
                    "malus_bonus" : NumberDecimal("6"),
                    "last_change" : "2018-02-23 16:14:24"
                }
            ],
            "options" : [
                {
                    "legal_addon_pre_renewal" : "N",
                    "legal_addon_post_renewal" : "N",
                    "home_addon_pre_renewal" : "Y",
                    "home_addon_post_renewal" : "N",
                    "garden_addon_pre_renewal" : "N",
                    "garden_addon_post_renewal" : "N",
                    "keycare_addon_pre_renewal" : "N",
                    "keycare_addon_post_renewal" : "N",
                    "last_change" : "2018-02-23 16:14:24"
                }
            ],
            "risk" : [
                {
                    "risk_classification_building" : NumberDecimal("1"),
                    "risk_classification_personal" : NumberDecimal("2"),
                    "appropriate_alarm" : "N",
                    "appropriate_locks" : "N",
                    "number_bedrooms" : NumberDecimal("2"),
                    "roof_construction" : NumberDecimal("11"),
                    "wall_construction" : NumberDecimal("15"),
                    "flooding" : "Y",
                    "listed_property" : NumberDecimal("3"),
                    "max_days_unoccupied" : NumberDecimal("0"),
                    "neighborhood_watch" : "N",
                    "occupation_status" : "PH",
                    "ownership_type" : NumberDecimal("8"),
                    "paying_guests" : NumberDecimal("0"),
                    "property_type" : NumberDecimal("19"),
                    "last_change" : "2018-02-23 16:14:24"
                }
            ],
            "claim" : [
                {
                    "claim_date" : ISODate("2001-04-27T22:00:00Z"),
                    "settled_date" : ISODate("2001-07-12T22:00:00Z"),
                    "claim_type" : "BUIDLING",
                    "claim_amount" : NumberDecimal("17370.62"),
                    "settled_amount" : NumberDecimal("17370.62"),
                    "claim_reason" : "WIND",
                    "last_change" : "2018-02-23 16:14:24"
                },
                {
                    "claim_date" : ISODate("2007-01-19T23:00:00Z"),
                    "settled_date" : ISODate("2007-05-28T22:00:00Z"),
                    "claim_type" : "BUIDLING",
                    "claim_amount" : NumberDecimal("31842.09"),
                    "settled_amount" : NumberDecimal("31842.09"),
                    "claim_reason" : "LIGHTNING",
                    "last_change" : "2018-02-23 16:14:24"
                }
            ]
        },
        {
            "policy_id" : "P000099399",
            "quote_day" : ISODate("2005-11-28T23:00:00Z"),
            "cover_start" : ISODate("2005-11-30T23:00:00Z"),
            "last_annual_premium_gross" : NumberDecimal("202.06"),
            "policy_status" : "Lapsed",
            "last_change" : "2018-02-23 16:19:32",
            "coverage" : [
                {
                    "type" : "BUILDING",
                    "sum_insured" : NumberDecimal("1000000"),
                    "malus_bonus" : NumberDecimal("1"),
                    "last_change" : "2018-02-23 16:19:32"
                },
                {
                    "type" : "PERSONAL_ITEMS",
                    "sum_insured" : NumberDecimal("50000"),
                    "malus_bonus" : NumberDecimal("1"),
                    "last_change" : "2018-02-23 16:19:32"
                }
            ],
            "options" : [
                {
                    "legal_addon_pre_renewal" : "Y",
                    "legal_addon_post_renewal" : "Y",
                    "home_addon_pre_renewal" : "N",
                    "home_addon_post_renewal" : "N",
                    "garden_addon_pre_renewal" : "N",
                    "garden_addon_post_renewal" : "N",
                    "keycare_addon_pre_renewal" : "N",
                    "keycare_addon_post_renewal" : "N",
                    "last_change" : "2018-02-23 16:19:32"
                }
            ],
            "risk" : [
                {
                    "risk_classification_building" : NumberDecimal("15"),
                    "risk_classification_personal" : NumberDecimal("7"),
                    "appropriate_alarm" : "N",
                    "appropriate_locks" : "Y",
                    "number_bedrooms" : NumberDecimal("2"),
                    "roof_construction" : NumberDecimal("11"),
                    "wall_construction" : NumberDecimal("15"),
                    "flooding" : "Y",
                    "listed_property" : NumberDecimal("3"),
                    "max_days_unoccupied" : NumberDecimal("30"),
                    "neighborhood_watch" : "N",
                    "occupation_status" : "PH",
                    "ownership_type" : NumberDecimal("8"),
                    "paying_guests" : NumberDecimal("0"),
                    "property_type" : NumberDecimal("10"),
                    "last_change" : "2018-02-23 16:19:32"
                }
            ],
            "claim" : [
                {
                    "claim_date" : ISODate("2016-01-12T23:00:00Z"),
                    "settled_date" : ISODate("2016-04-08T22:00:00Z"),
                    "claim_type" : "BUIDLING",
                    "claim_amount" : NumberDecimal("17483.83"),
                    "settled_amount" : NumberDecimal("17483.83"),
                    "claim_reason" : "LIGHTNING",
                    "last_change" : "2018-02-23 16:19:32"
                }
            ]
        }
    ],
    "last_change_home_policy" : "2018-02-23 16:19:32",
    "last_change_car_customer" : "2018-02-23 16:32:14",
    "car_insurance" : [
        {
            "policy_id" : "PC_000000529",
            "cover_start" : ISODate("2008-03-09T23:00:00Z"),
            "last_annual_premium_gross" : NumberDecimal("169.4"),
            "car_model" : "Alfa Romeo Alfasud",
            "max_covered" : NumberDecimal("50000000"),
            "claim" : [
                {
                    "claim_date" : ISODate("2009-07-01T22:00:00Z"),
                    "settled_date" : ISODate("2009-09-18T22:00:00Z"),
                    "claim_amount" : NumberDecimal("766.67"),
                    "settled_amount" : NumberDecimal("766.67"),
                    "claim_reason" : "HAIL",
                    "last_change" : "2018-02-23 16:19:38"
                },
                {
                    "claim_date" : ISODate("2014-03-07T23:00:00Z"),
                    "settled_date" : ISODate("2014-04-19T22:00:00Z"),
                    "claim_amount" : NumberDecimal("908.2"),
                    "settled_amount" : NumberDecimal("908.2"),
                    "claim_reason" : "HAIL",
                    "last_change" : "2018-02-23 16:19:38"
                }
            ]
        }
    ],
    "last_change_car_policy" : "2018-02-23 16:19:38"
});
> db.customer.count()
1
>
```

When you run the application as described above, you can curl the endpoint and will receive the policy you have inserted.

```
maven-container$ java -jar target/insurance-data-service-0.0.1.jar --spring.data.mongodb.host=172.17.0.2 --spring.data.mongodb.port=27017 --spring.data.mongodb.database=insurance-data
host$ curl http://localhost:8080/v2/customer
[{"_id":{"timestamp":1523006513,"machineIdentifier":12759474,"processIdentifier":-11940,"counter":2944687,"time":1523006513000,"date":"2018-04-06T09:21:53.000+0000","timeSecond":1523006513},"customer_id":"C000030006aaa","address":{"street":"Tiergartenstraße 462","zip":"48555","city":"Buchloe","nationality":"Germany"},"date_of_birth":"1933-06-13","email":"cheapest1927@yandex.com","first_name":"Lucia","gender":"F","job":"Buchhalter / Buchhalterin","last_change_home_customer":"2018-02-23 16:36:30","last_name":"Hoover","marital_status":"WIDOWED","number_children":{"high":3476778912330022912,"low":2,"negative":false,"naN":false,"infin....
-- OUTPUT CROPPED --
host$
```