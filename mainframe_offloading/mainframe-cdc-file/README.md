# mainframe-cdc-file

This application polls the `mainframe-simulator` directory and adds the content of new JSON files to a MongoDB collection named `customer`.

## Build the application

Using docker (preferred)

```
host$ cd <root>/mainframe_offloading/mainframe-cdc-file
host$ docker run --name maven-container --rm -w /home/app -v $(pwd):/home/app maven:3.5-jdk-8 /bin/bash -c "mvn clean package"
```

With Maven 3.5 and JDK 8 installed locally

```
host$ cd <repo-root>/mainframe_offloading/mainframe-cdc-file
host$ mvn clean package
```

## Run the application

You have to set the following environment variables before running the service

 - **SPRING_DATA_MONGODB_DATABASE**:: The MongoDB database name
 - **SPRING_DATA_MONGODB_URI**:: MongoDB URI
 - **INBOUND_READ_PATH**:: The directory to poll

### Run the application on the host

```
host$ export SPRING_DATA_MONGODB_DATABASE=insurance
host$ export SPRING_DATA_MONGODB_URI=mongodb+srv://<username>:<password>@<host>
host$ export INBOUND_READ_PATH=<repo-root>/mainframe-offloading/mainframe-simulator/sample-data/mainframe/
host$ java -jar target/mainframe-cdc-file-0.0.1.jar
```

### Run the application with docker

Build the image

```
host$ cd <repo-root>/mainframe_offloading/mainframe-cdc-file
host$ docker build -t mainframe-cdc .
```

Run the container

```
host$ cd <repo-root>/mainframe_offloading
docker run  --rm \
--name mainframe-cdc \
-v $(pwd)/mainframe-simulator/sample-data:/home/insurance-data \
-e SPRING_DATA_MONGODB_DATABASE=insurance \
-e SPRING_DATA_MONGODB_URI=mongodb+srv://insurance:demo@insurancecluster-b6yo1.mongodb.net \
-e INBOUND_READ_PATH=/home/insurance-data/mainframe \
mainframe-cdc
```

## Example

Within that mongoDB instance, the service will look into a collection named: **customer** for customer/policies/claims info.

Below is an example of final document in MongoDB
 ```
 {
     "_id" : ObjectId("5a90373e58a14585815053af"),
     "customer_id" : "C000017274",
     "address" : {
         "street" : "Scherfweg 1273",
         "zip" : "4018",
         "city" : "Traiskirchen",
         "nationality" : "Austria"
     },
     "date_of_birth" : "1927-01-11",
     "email" : "jackal1980@outlook.com",
     "first_name" : "Griswold",
     "gender" : "M",
     "job" : "Dolmetscher / Dolmetscherin",
     "last_change_home_customer" : "2018-02-23 16:36:30",
     "last_name" : "Urbansky",
     "marital_status" : "WIDOWED",
     "number_children" : NumberDecimal("3"),
     "phone" : "+43-623-4790653",
     "home_insurance" : [
         {
             "policy_id" : "P000021801",
             "quote_day" : ISODate("1999-11-12T23:00:00Z"),
             "cover_start" : ISODate("1999-11-30T23:00:00Z"),
             "last_annual_premium_gross" : NumberDecimal("271.41"),
             "policy_status" : "Live",
             "last_change" : "2018-02-23 16:14:45",
             "coverage" : [
                 {
                     "type" : "BUILDING",
                     "sum_insured" : NumberDecimal("1000000"),
                     "malus_bonus" : NumberDecimal("6"),
                     "last_change" : "2018-02-23 16:14:45"
                 },
                 {
                     "type" : "PERSONAL_ITEMS",
                     "sum_insured" : NumberDecimal("75000"),
                     "malus_bonus" : NumberDecimal("6"),
                     "last_change" : "2018-02-23 16:14:45"
                 }
             ],
             "options" : [
                 {
                     "legal_addon_pre_renewal" : "Y",
                     "legal_addon_post_renewal" : "Y",
                     "home_addon_pre_renewal" : "Y",
                     "home_addon_post_renewal" : "N",
                     "garden_addon_pre_renewal" : "N",
                     "garden_addon_post_renewal" : "N",
                     "keycare_addon_pre_renewal" : "Y",
                     "keycare_addon_post_renewal" : "Y",
                     "last_change" : "2018-02-23 16:14:45"
                 }
             ],
             "risk" : [
                 {
                     "risk_classification_building" : NumberDecimal("25"),
                     "risk_classification_personal" : NumberDecimal("9"),
                     "appropriate_alarm" : "Y",
                     "appropriate_locks" : "Y",
                     "number_bedrooms" : NumberDecimal("2"),
                     "roof_construction" : NumberDecimal("11"),
                     "wall_construction" : NumberDecimal("15"),
                     "flooding" : "Y",
                     "listed_property" : NumberDecimal("3"),
                     "max_days_unoccupied" : NumberDecimal("0"),
                     "neighborhood_watch" : "N",
                     "occupation_status" : "PH",
                     "ownership_type" : NumberDecimal("3"),
                     "paying_guests" : NumberDecimal("0"),
                     "property_type" : NumberDecimal("7"),
                     "last_change" : "2018-02-23 16:14:45"
                 }
             ],
             "claim" : [
                 {
                     "claim_date" : ISODate("2000-11-03T23:00:00Z"),
                     "settled_date" : ISODate("2000-11-08T23:00:00Z"),
                     "claim_type" : "BUIDLING",
                     "claim_amount" : NumberDecimal("93373.4"),
                     "settled_amount" : NumberDecimal("93373.4"),
                     "claim_reason" : "FIRE",
                     "last_change" : "2018-02-23 16:14:45"
                 },
                 {
                     "claim_date" : ISODate("2005-12-19T23:00:00Z"),
                     "settled_date" : ISODate("2006-04-30T22:00:00Z"),
                     "claim_type" : "PERSONAL_ITEMS",
                     "claim_amount" : NumberDecimal("7064.07"),
                     "settled_amount" : NumberDecimal("7064.07"),
                     "claim_reason" : "FIRE",
                     "last_change" : "2018-02-23 16:14:45"
                 }
             ]
         }
     ],
     "last_change_home_policy" : "2018-02-23 16:14:45"
 }

 ```

