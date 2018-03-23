import csv
import json

count = 0

def rowToJSON(row):
    return {
        "customer_id": row["customer_id"],
        "first_name": row["first_name"],
        "last_name": row["last_name"],
        "gender": row["gender"],
        "job": row["job"],
        "email": row["email"],
        "phone": row["phone"],
        "number_children": row["number_children"],
        "marital_status": row["marital_status"],
        "date_of_birth": row["date_of_birth"],
        "street": row["street"],
        "zip": row["zip"],
        "city": row["city"],
        "country_code": row["country_code"],
        "nationality": row["nationality"],
        "last_change": row["last_change"]
    }

with open('output/car_insurance_customer.csv', 'rb') as csvfile:
    spamreader = csv.DictReader(csvfile, delimiter=',', quotechar='|')
    for row in spamreader:
        if count == 100:
            break
        else:
            count = count + 1

        with open('../../mainframe_offloading/mainframe-simulator/sample-data/customer/' + row["customer_id"] + ".json", 'w') as outfile:
            json.dump(rowToJSON(row), outfile)


