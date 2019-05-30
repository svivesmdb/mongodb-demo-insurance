
# Get the file that defines what ports and how much data to generate
source ./configparams.sh

HOW_MANY_CUSTOMERS_TO_GENERATE=2500
HOW_MANY_HOME_INSURANCES=2500
HOW_MANY_CAR_INSURANCES=900

# Create customer first and save them on to a CSV
cd ../mongodb-demo-insurance/data/data_generation/

#echo "Current dir is "
#pwd

python3 faker_customers_to_csv.py $HOW_MANY_CUSTOMERS_TO_GENERATE

# Generate home insurances
python3 faker_home_insurance.py $HOW_MANY_HOME_INSURANCES $POSTGRES_PORT

# Generate car insurances
python3 faker_car_insurance.py $HOW_MANY_CAR_INSURANCES $ORACLE_PORT

