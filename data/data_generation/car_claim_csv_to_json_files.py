import csv
import json

count = 0

def rowToJSON(row):
    return {
        "claim_id": row["claim_id"],
        "policy_id": row["policy_id"],
        "claim_date": row["claim_date"],
        "settled_date": row["settled_date"],
        "claim_amount": row["claim_amount"],
        "settled_amount": row["settled_amount"],
        "claim_reason": row["claim_reason"],
        "last_change": row["last_change"]
    }

with open('output/car_insurance_claim.csv', 'r') as csvfile:
    spamreader = csv.DictReader(csvfile, delimiter=',', quotechar='|')
    for row in spamreader:
        if count == 1000:
            break
        else:
            count = count + 1

        with open('../../mainframe_offloading/mainframe-simulator/sample-data/mainframe/claim/' + row["claim_id"] + ".json", 'w') as outfile:
            json.dump(rowToJSON(row), outfile)


