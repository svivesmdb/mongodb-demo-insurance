import csv
import json

count = 0

def rowToJSON(row):
    return {
        "policy_id": row["policy_id"],
        "customer_id": row["customer_id"],
        "cover_start": row["cover_start"],
        "car_model": row["car_model"],
        "max_coverd": row["max_coverd"],
        "last_ann_premium_gross": row["last_ann_premium_gross"],
        "last_change": row["last_change"]
    }

with open('output/car_insurance_policy.csv', 'rb') as csvfile:
    spamreader = csv.DictReader(csvfile, delimiter=',', quotechar='|')
    for row in spamreader:
        if count == 1000:
            break
        else:
            count = count + 1

        with open('../../mainframe_offloading/mainframe-simulator/sample-data/motor/policies/' + row["policy_id"] + ".json", 'w') as outfile:
            json.dump(rowToJSON(row), outfile)


