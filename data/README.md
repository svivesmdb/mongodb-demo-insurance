# Data used in Demo for Insurance Sector

Sample data is provided for home insurances and car insurances. The generators create >100k customers, insurances and claims. There is also a sample dump available (final view consisting of customers as well as their home and car insurances). 

Contents of this directory:

- Data Dumps:
  - MongoDB: Full customer documents, incl. car and home insurances
  - MongoDB: Customer documents - only car insurances
  - Oracle: Car Insurances
  - Oracle: Home Insurances

- Generate data in the following order:
  1. Customers
  2. Home insurance policies and claims
  3. Car insurances policies and claims

- Data Ingestion via MongoSyphon:
  - CDC-based ingestion of customers, home insurance policies and their respective claims from Oracle (based on last_change timestamps).
  - CDC-based ingestion of customers, car insurance policies and their respective claims from Oracle (based on last_change timestamps).
  - Example for batch-integration of home and car insurances (simpler configuration scripts).

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 

### Prerequisites

* MongoDB
* Oracle 11g (Vagrant file provided)
* Data Generation: Python, Faker, mimesis

### Importing the data dumps (without generating new data)

#### MongoDB

In order to have an initial data set in MongoDB to run the demo, please import the full data dump. In case of demonstrating just the mainframe offloading scenario, the car insurance only dump is sufficient. Execute the following command in the directory of the full or car insurance dump:

```
# Import into local MongoDB running on port 27017:
mongorestore -d insurance --gzip --drop

# Import into MongoDB Atlas:
mongorestore -d insurance --gzip --drop --uri "mongodb://<USER>:<PASSWORD>@atlas-host1:27017,atlas-host2:27017,atlas-host3:27017/<DATABASE>?ssl=true&replicaSet=myAtlasRS&authSource=admin"
```

The number of documents to be restored:

* Car Insurance only: 65406
* Car and Home Insurance: 111445

#### Oracle

The schemata and insert statements for the Oracle data are available. Please execute the DDL scripts defined in ```oracle-schema-and-dump```:

* car_insurance_oracle_schema.ddl: The schema for car insurances
* car_insurance_oracle_inserts.zip: The insert statements for data
* car_insurance_oracle_triggers_and_keys.sql: Primary and Foreign Keys as well as Triggers for upates of last_change_dates
* home_insurance_oracle_schema.ddl: The schema for home insurances
* home_insurance_oracle_inserts.zip: The insert statements for data
* home_insurance_oracle_triggers_and_keys.sql: Primary and Foreign Keys as well as Triggers for upates of last_change_dates

### Data Generation

The data does not need to be regenerated for the demo execution. The import of dumps if fine - but maybe you want to change some data for additional test purposes.

#### Data Generator

The demo is based on example data that is publicly available. Home insurance data is taken from [Kaggle: 2007-2012 polices of a Home Insurance company](https://www.kaggle.com/ycanario/home-insurance). Car insurance data is based on some claims provided by [EmcienScan](http://www.scan-support.com/help/sample-data-sets). The direct download is also available: [Automobile Insurance claims including location, policy type and claim amount](http://dyzz9obi78pm5.cloudfront.net/app/image/id/560ec66d32131c9409f2ba54/n/Auto_Insurance_Claims_Sample.csv). Some slight modifications have been performed in order to provide a more complex relational structure and showcase some real-world challenges.

The following steps need to be performed. Some modifications in the data generator can be done in order to internationalize the demo.

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
   - The output is written to an Oracle instance as well as CSV files.
   - The customer lookup is simple (and unfortunately slow ~10 minutes) - any improvements welcome.
   ```
   python faker_car_insurance.py
   ```

4. Create Primary and Foregin Keys as well as Triggers in Oracle to reflect Change Data Capture.
   - Execute the SQL code provided in ```home_insurance_oracle_triggers_and_keys.sql``` as well as ```car_insurance_oracle_triggers_and_keys.sql``` in the ```oracle-schema-and-dump``` directory . These scripts will create triggers that update the last_change timestamp for each insert or update.

5. **Optional:** The output directory contains the generated CSV files for each table in Oracle. These files are also used to initialize the sample data of the mainframe simulator. If regenerated, please run ```car_claim_csv_to_json_files.py```, ```car_customer_csv_to_json_files.py``` and ```car_policy_csv_to_json_files.py``` to provide a fresh data basis for the mainframe simulator.

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
