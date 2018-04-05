
# mainframe-cdc-file

This application polls a directory and sends new JSON files contents to MongoDB.

## Options


By default the service will start listening on the default mongodb port: 27017 on localhost
However this can be changed with the following options:

 - **spring.data.mongodb.database**:: The MongoDB database name (default: `**mainframe**`)
 - **spring.data.mongodb.host**:: Mongo database Host. (default: `**localhost**`)
 - **spring.data.mongodb.host**:: MongoDB port. (default: `**27017**`)
 - **inbound.read.path**:: The directory to poll *(default: `**<none>**`)*
 
Within that mongoDB instance, the service will look into a colloction named: **customer** for customer/policies/claims info.


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



## Build

```
$ mvn clean package

```

## Run

Assuming you have mongodb running locally on localhost:27017

```
$ java -jar target/mainframe-cdc-file-0.0.1.jar --inbound.read.path=/data/mainframe
```


Assuming you have mongodb running on host: 52.10.10.10 and port 27000

```
 $ java -jar target/mainframe-cdc-file-0.0.1.jar --inbound.read.path=/data/mainframe --spring.data.mongodb.host=52.10.10.10 --spring.data.mongodb.port=27000
```



