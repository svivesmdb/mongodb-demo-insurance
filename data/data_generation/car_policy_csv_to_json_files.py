import csv
import json

count = 0

def rowToJSON(row):
    return {
        "policy_id": row["policy_id"],
        "customer_id": row["customer_id"],
        "cover_start": row["cover_start"],
        "car_model": row["car_model"],
        "max_covered": row["max_covered"],
        "last_ann_premium_gross": row["last_ann_premium_gross"],
        "last_change": row["last_change"]
    }

with open('output/car_insurance_policy.csv', 'r') as csvfile:
    spamreader = csv.DictReader(csvfile, delimiter=',', quotechar='|')
    for row in spamreader:
        if count == 1000:
            break
        else:
            count = count + 1

        with open('../../mainframe_offloading/mainframe-simulator/sample-data/mainframe/policy/' + row["policy_id"] + ".json", 'w') as outfile:
            json.dump(rowToJSON(row), outfile)


