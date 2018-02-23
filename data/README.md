# Data used in Demo for Insurance Sector

Sample data is provided for home insurances and car insurances. The generators create >100k customers, insurances and claims. There is also a sample dump available (final view consisting of customers as well as their home and car insurances. 

Contents of this directory:

* Generate data:
	* Customers
	* Home insurance policies and claims
	* Car insurances policies and claims

* Data Ingestion:
	* CDC-based ingestion of customers, home insurance policies and their respective claims from Oracle (based on last_change timestamps).
	* CDC-based ingestion of customers, car insurance policies and their respective claims from Oracle (based on last_change timestamps).
	* Example for batch-integration of home and car insurances (simpler configuration scripts).

* Dump of example collections insurance.customer (incl. index on customer_id)

### Some TODOs to make it beautiful

[ ] Store the last change information in a subdocument to make the main document easy to read
[ ] Some timezone issues when Oracle, MongoDB and the scripts are running in different timezones
[ ] Changes in Customers will always be overridden, so store at least a history of the changes in the customer

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 

### Prerequisites

* MongoDB
* Oracle 11g (Vagrant file provided)
* Data Generation: Python, Faker, mimesis (and some samples for home insurances)

### Installing

#### Data Generator

The demo is based on example data that is publicly available. Home insurance data is taken from [Kaggle: 2007-2012 polices of a Home Insurance company](https://www.kaggle.com/ycanario/home-insurance). Car insurance data is based on some claims provided by [EmcienScan](http://www.scan-support.com/help/sample-data-sets). The direct download is also available: [Automobile Insurance claims including location, policy type and claim amount](http://dyzz9obi78pm5.cloudfront.net/app/image/id/560ec66d32131c9409f2ba54/n/Auto_Insurance_Claims_Sample.csv). Some slight modifications have been performed in order to provide a more complex relational structure and showcase some real-world challenges.

The following steps need to be performed. Some modifications in the data generator can be performed in order to internationalize the demo.

1. Generate the customer data.
 - The generator can be internationalized by providing different locales to the faker instances as well as the mimesis instances of Address and Person.
 - The data is stored in a CSV file in the output directory. It is used later on the generate the policies.
```
python faker_customers_to_csv.py
```

2. Generate the home insurance data.
 - It takes the input of home_insurance.csv (a dataset from kaggle) and the previously generated customer CSV file.
 - The output is written to an Oracle instance as well as CSV files.
 - The customer lookup is simple (and unfortunately slow ~20 minutes) - any improvements welcome.
```
python faker_home_insurance.py
```

3. Generate the car insurance data.
 - It takes the input of home_insurance.csv (a dataset from kaggle) and the previously generated customer CSV file.
 - The output is written to an Oracle instance as well as CSV files.
 - The customer lookup is simple (and unfortunately slow ~10 minutes) - any improvements welcome.
```
python faker_car_insurance.py
```

4. Create Triggers in Oracle to reflect Change Data Capture.
 - Execute the SQL code provided in home_oracle_triggers_and_keys.sql as well as car_oracle_triggers_and_keys.sql. These scripts will create triggers that update the last_change timestamp for each insert or update.

### Data Loader

Once the data is loaded into the Oracle database, we need to load the data into MongoDB. For simplicity purposes, we leverage [MongoSyphon](https://github.com/johnlpage/MongoSyphon) for now. It is a very leightweight data loader for MongoDB leveraging several best practices how to load data (for the do's and dont's in loading data have a look at the presentation [ETL for Pros: Getting Data Into MongoDB](https://explore.mongodb.com/vidyard-all-players/andre-spiegel-2) by André Spiegel) and easy creation of document structures. 

This demo uses a slightly modified version of MongoSyphon (please find it in the data_loader directory). Please modify the connection strings in the JSON configuration files to MongoDB as well as to Oracle. After that, import the customers first and the home insurance policies and claims afterwards. **Important: In order to have an efficient import process, there has to be an index on the customer_id.**

```
# Create the index in the customer collection via the MongoDB Shell
db.customer.createIndex({customer_id:1})

# Import the home insurance customers via the command line (will perform inserts)
java -jar MongoSyphon.jar -c "home_insurance_customers_cdc.json"

# Import the home insurance policies via the command line (will perform updates with $push)
java -jar MongoSyphon.jar -c "home_insurance_policies_cdc.json
```

If the car insurance policies should also be loaded into MongoDB, please execute the following steps to enrich the single customer view.

```
# Import the car insurance customers via the command line (they overlap with the home insurance customers, 
# so it will perform upserts on the customer)
java -jar MongoSyphon.jar -c "car_insurance_customers_cdc.json"

# Import the car insurance policies via the command line (will perform updates with $push)
java -jar MongoSyphon.jar -c "car_insurance_policies_cdc.json
```

**Optional:**
If you want to work with the MongoDB dump, you can simply import it via mongorestore:
```
mongorestore -d insurance --gzip dump/insurance/
```
