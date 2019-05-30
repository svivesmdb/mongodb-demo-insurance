# -*- coding: utf-8 -*-

#
### 2 ### Run AFTER the faker_customers_csv -- CONNECTS TO POSTGRES
#

# POSTGRES - Database server authentication details
host='localhost'
port=32769
user='pguser'
schema = 'insurance'
password='mypass'
write_to_csv_too=False

import sys
NUMBER_HOME_INSURANCES_TO_GENERATE = 500
if len(sys.argv) > 1 and sys.argv[1]:
    NUMBER_HOME_INSURANCES_TO_GENERATE = int(sys.argv[1])
if len(sys.argv) > 2 and sys.argv[2]:
    port = sys.argv[2]

from datetime import datetime
from dateutil.relativedelta import relativedelta
import random
import csv
import pymongo
from pymongo.errors import BulkWriteError
from faker import Factory
fake = Factory.create('es_ES')

from sqlalchemy import create_engine
import sqlalchemy

import logging

import pandas as pd
from pandas.io import sql

import os
import errno

logging.basicConfig()
logging.getLogger('sqlalchemy').setLevel(logging.ERROR)

cstr = 'postgres://{user}:{password}@{host}:{port}/insurance'.format(
    user=user,
    password=password,
    host=host,
    port=port
)

postgres_engine = create_engine(
    cstr,
    convert_unicode=False,
    pool_recycle=10,
    pool_size=50
)

os.environ['NLS_LANG'] = ".AL32UTF8"

next_claim_number = 0

def main():
    num_gen = NUMBER_HOME_INSURANCES_TO_GENERATE
    
    print("Starting to generate " + str( NUMBER_HOME_INSURANCES_TO_GENERATE )+ " home insurances for customers")

    ls_dates = [fake.date_time_between(start_date="-10y", end_date="now", tzinfo=None) for i in range(0,num_gen)]
    ls_dates.sort()

    sql.execute('CREATE SCHEMA IF NOT EXISTS insurance', postgres_engine)
    sql.execute('DROP TABLE IF EXISTS insurance.policy', postgres_engine)
    sql.execute('DROP TABLE IF EXISTS insurance.policy_coverage', postgres_engine)
    sql.execute('DROP TABLE IF EXISTS insurance.policy_risk', postgres_engine)
    sql.execute('DROP TABLE IF EXISTS insurance.policy_option', postgres_engine)
    sql.execute('DROP TABLE IF EXISTS insurance.claim', postgres_engine)
    sql.execute('DROP TABLE IF EXISTS insurance.customer', postgres_engine)

    ls_customers = []
    customer_csvfile = open('output/customers.csv', newline='\n')
    customer_reader = csv.reader(customer_csvfile, delimiter=',', quotechar='"')
    # skip header
    next(customer_reader)
    for customer in customer_reader:
        customer[9] = datetime.strptime(customer[9], '%Y-%m-%d')
        customer.append(customer[9] + relativedelta(years=18))
        customer.append(customer[9] + relativedelta(years=75))
        ls_customers.append(customer)

    customer_csvfile.close()

    csvfile = open('home_insurance.csv', newline='\n')
    reader = csv.reader(csvfile, delimiter=',', quotechar='"')

    ls_policies = []
    ls_policy_coverage = []
    ls_policy_risk = []
    ls_policy_options = []

    ls_used_customer_ids = []

    ls_claims = []

    i = 0
    # skip header
    next(reader)
    for row in reader:
        if (i % 1000) == 0:
            print ("Generating home insurance data for customer number: " + str(i))

        if (i < num_gen):
            if (row[1] != '' and row[8] != '' and row[12] != ''):
                s_policy_number = policy_number(i)
                d_quote_date = ls_dates[i]
                d_cover_start = (ls_dates[i] + relativedelta(months=1)).replace(day=1)
                
                s_customer_id = get_customer(i, d_quote_date, ls_customers)[0]
                if not s_customer_id in ls_used_customer_ids:
                    ls_used_customer_ids.append(s_customer_id)
                ls_used_customer_ids.sort()
                
                try:
                    ls_policies.append((s_policy_number, s_customer_id, d_quote_date, d_cover_start, row[62], row[63], datetime.today()))
                    ls_policy_coverage.append((s_policy_number, 'BUILDING', row[7], row[9], row[10], datetime.today())) #row[16], 
                    ls_policy_coverage.append((s_policy_number, 'PERSONAL_ITEMS', row[11], row[13], row[14], datetime.today())) #row[15], 
                    ls_policy_risk.append((s_policy_number, row[8], row[12], row[24], row[25], row[26], row[27], row[28], row[29], row[30], row[31], row[32], row[33], row[34], row[35], row[36], row[37], int(row[40]) if row[40] != '' else -1, datetime.today()))
                    ls_policy_options.append((s_policy_number, row[44], row[45], row[46], row[47], row[48], row[49], row[50], row[51], datetime.today()))
                except Exception(e):
                    print(e)
                    pass

                if d_cover_start < (datetime.today() + relativedelta(years=-3)):
                    generate_claims(s_policy_number, d_cover_start, row[7], row[9], row[11], row[13], ls_claims)
  
            i = i+1
        else:
            break

    csvfile.close()

    ls_customers_to_db = []
    for s_customer_id in ls_used_customer_ids:
        i_customer_id = int(s_customer_id[1:]) - 1
        customer = ls_customers[i_customer_id]
        ls_customers_to_db.append((customer[0], customer[2], customer[3], customer[1], customer[4], customer[5], customer[6], customer[7], customer[8], customer[9], customer[10], customer[11], customer[12], customer[13], customer[14], datetime.today() ))

    columns_policy = ['policy_id', 'customer_id', 'quote_day', 'cover_start', 'last_ann_premium_gross', 'policy_status', 'last_change']
    df_policies = pd.DataFrame(ls_policies, columns=columns_policy)
    
    columns_policy_coverage = ['policy_id', 'type', 'coverage', 'sum_insured', 'malus_bonus', 'last_change']
    df_policy_coverage =  pd.DataFrame(ls_policy_coverage, columns=columns_policy_coverage)
    
    columns_policy_risk = ['policy_id', 'rsk_classif_bldg', 'rsk_classif_prsnl', 'appr_alarm', 'appr_locks', 'bedrooms', 'roof_cnstrctn', 'wall_constrctn', 'flooding', 'listed', 'max_days_unocc', 'neigh_watch', 'occ_stats', 'ownership_type', 'paying_guests', 'prop_type', 'safe_installed', 'yearbuilt', 'last_change']
    df_policy_risk =  pd.DataFrame(ls_policy_risk, columns=columns_policy_risk)
    
    columns_policy_options = ['policy_id', 'lgl_addon_pre_ren', 'lgl_addon_post_ren', 'home_em_addon_pre_ren', 'home_em_addon_post_ren', 'garden_addon_pre_ren', 'garden_addon_post_ren', 'keycare_addon_pre_ren', 'keycare_addon_post_ren', 'last_change']
    df_policy_options =  pd.DataFrame(ls_policy_options, columns=columns_policy_options)
    
    columns_customer_home_insurance = ['customer_id', 'first_name', 'last_name', 'gender', 'job', 'email', 'phone', 'number_children', 'marital_status', 'date_of_birth', 'street', 'zip', 'city', 'country_code', 'nationality', 'last_change']

    df_customers_to_db = pd.DataFrame(ls_customers_to_db, columns=columns_customer_home_insurance)
    
    columns_claim = ['claim_id', 'policy_id', 'claim_date', 'settled_date', 'claim_type', 'claim_amount', 'settled_amount', 'claim_reason', 'last_change']
    df_claims = pd.DataFrame(ls_claims, columns=columns_claim)

    delete_file('output/home_insurance_policy.csv')
    
    if write_to_csv_too:
        print("CSV: Writing to the home_insurance_policy")
        df_policies.to_csv('output/home_insurance_policy.csv', sep=',', index=False, header=columns_policy)

    print("SQL: Writing to the policy table")
    df_policies.to_sql('policy', postgres_engine, schema=schema, index=False, chunksize=1000, dtype= {
            'policy_id': sqlalchemy.types.String(12),
            'customer_id': sqlalchemy.types.String(12),
            'quote_day': sqlalchemy.types.Date,
            'cover_start': sqlalchemy.types.Date,
            'last_ann_premium_gross': sqlalchemy.types.Numeric(precision=10, scale=2),
            'policy_status': sqlalchemy.types.String(100),
            'last_change': sqlalchemy.dialects.postgresql.TIMESTAMP
        })
    
    if write_to_csv_too:
        delete_file('output/home_insurance_policy_coverage.csv')
        print("CSV: Writing to the home_insurance_policy_coverage ")
        df_policy_coverage.to_csv('output/home_insurance_policy_coverage.csv', sep=',', index=False, header=columns_policy_coverage)

    print("SQL: Writing to the policy_coverage table")
    df_policy_coverage.to_sql('policy_coverage', postgres_engine, schema=schema, index=False, chunksize=1000, dtype= {
            'policy_id': sqlalchemy.types.String(12),
            'type': sqlalchemy.types.String(20),
            'coverage': sqlalchemy.types.String(1), 
            'sum_insured': sqlalchemy.types.Numeric(precision=30, scale=2), 
            'malus_bonus': sqlalchemy.types.Numeric(precision=10, scale=0),
            'last_change': sqlalchemy.dialects.postgresql.TIMESTAMP
        })


    if write_to_csv_too:
        delete_file('output/home_insurance_policy_risk.csv')
        print("CSV: Writing to the home_insurance_policy_risk")
        df_policy_risk.to_csv('output/home_insurance_policy_risk.csv', sep=',', index=False, header=columns_policy_risk)

    print("SQL: Writing to the policy_risk table")
    df_policy_risk.to_sql('policy_risk', postgres_engine, schema=schema, index=False, chunksize=1000, dtype= {
            'policy_id': sqlalchemy.types.String(12),
            'rsk_classif_bldg': sqlalchemy.types.Numeric(precision=10, scale=0), 
            'rsk_classif_prsnl': sqlalchemy.types.Numeric(precision=10, scale=0), 
            'appr_alarm': sqlalchemy.types.String(1), 
            'appr_locks': sqlalchemy.types.String(1), 
            'bedrooms': sqlalchemy.types.Numeric(precision=10, scale=0), 
            'roof_cnstrctn': sqlalchemy.types.Numeric(precision=10, scale=0), 
            'wall_constrctn': sqlalchemy.types.Numeric(precision=10, scale=0), 
            'flooding': sqlalchemy.types.String(1), 
            'listed': sqlalchemy.types.Numeric(precision=10, scale=0), 
            'max_days_unocc': sqlalchemy.types.Numeric(precision=10, scale=0), 
            'neigh_watch': sqlalchemy.types.String(1), 
            'occ_stats': sqlalchemy.types.String(2), 
            'ownership_type': sqlalchemy.types.Numeric(precision=10, scale=0), 
            'paying_guests': sqlalchemy.types.Numeric(precision=10, scale=0), 
            'prop_type': sqlalchemy.types.Numeric(precision=10, scale=0), 
            'safe_installed': sqlalchemy.types.String(1), 
            'yearbuilt': sqlalchemy.types.Numeric(precision=10, scale=0),
            'last_change': sqlalchemy.dialects.postgresql.TIMESTAMP
        })

    if write_to_csv_too:
        delete_file('output/home_insurance_policy_option.csv')
        print("CSV: Writing to the home_insurance_policy_option")
        df_policy_options.to_csv('output/home_insurance_policy_option.csv', sep=',', index=False, header=columns_policy_options)

    print("SQL: Writing to the policy_option table")
    df_policy_options.to_sql('policy_option', postgres_engine, schema=schema, index=False, chunksize=1000, dtype= {
            'policy_id': sqlalchemy.types.String(12),
            'lgl_addon_pre_ren': sqlalchemy.types.String(1), 
            'lgl_addon_post_ren': sqlalchemy.types.String(1), 
            'home_em_addon_pre_ren': sqlalchemy.types.String(1), 
            'home_em_addon_post_ren': sqlalchemy.types.String(1), 
            'garden_addon_pre_ren': sqlalchemy.types.String(1), 
            'garden_addon_post_ren': sqlalchemy.types.String(1), 
            'keycare_addon_pre_ren': sqlalchemy.types.String(1), 
            'keycare_addon_post_ren': sqlalchemy.types.String(1),
            'last_change': sqlalchemy.dialects.postgresql.TIMESTAMP
        })  

    if write_to_csv_too:
        delete_file('output/home_insurance_customer.csv')
        print("CSV: Writing to the home_insurance_customer")
        df_customers_to_db.to_csv('output/home_insurance_customer.csv', sep=',', index=False, header=columns_customer_home_insurance)

    print("SQL: Writing to the customer table")
    df_customers_to_db.to_sql('customer', postgres_engine, schema=schema, index=False, chunksize=1000, dtype= {
            'customer_id': sqlalchemy.types.String(12), 
            'first_name': sqlalchemy.types.String(150), 
            'last_name': sqlalchemy.types.String(150), 
            'gender': sqlalchemy.types.String(1), 
            'job': sqlalchemy.types.String(150), 
            'email': sqlalchemy.types.String(100), 
            'phone': sqlalchemy.types.String(40), 
            'number_children': sqlalchemy.types.Numeric(precision=4, scale=0),
            'marital_status': sqlalchemy.types.String(12), 
            'date_of_birth': sqlalchemy.types.Date, 
            'street': sqlalchemy.types.String(120), 
            'zip': sqlalchemy.types.String(10), 
            'city': sqlalchemy.types.String(100), 
            'country_code': sqlalchemy.types.String(2), 
            'nationality': sqlalchemy.types.String(20),
            'last_change': sqlalchemy.dialects.postgresql.TIMESTAMP
        })  
    
    if write_to_csv_too:
        delete_file('output/home_insurance_claim.csv')
        print("CSV: Writing to the home_insurance_claim")
        df_claims.to_csv('output/home_insurance_claim.csv', sep=',', index=False, header=columns_claim)

    print("SQL: Writing to the claim table")
    df_claims.to_sql('claim', postgres_engine, schema=schema, index=False, chunksize=1000, dtype= {
            'claim_id': sqlalchemy.types.String(12),
            'policy_id': sqlalchemy.types.String(12),
            'claim_date': sqlalchemy.types.Date, 
            'settled_date': sqlalchemy.types.Date, 
            'claim_type': sqlalchemy.types.String(20), 
            'claim_amount': sqlalchemy.types.Numeric(precision=30, scale=2), 
            'settled_amount': sqlalchemy.types.Numeric(precision=30, scale=2), 
            'claim_reason': sqlalchemy.types.String(30),
            'last_change': sqlalchemy.dialects.postgresql.TIMESTAMP
        })   
    
    print("Finished!")

def policy_number(i):
    s_policy_number = str(i + 1)
    while len(s_policy_number) < 9:
        s_policy_number = '0' + s_policy_number
    s_policy_number = 'P' + s_policy_number

    return s_policy_number

def claim_number(i):
    s_claim_number = str(i + 1)
    while len(s_claim_number) < 9:
        s_claim_number = '0' + s_claim_number
    s_claim_number = 'CL_' + s_claim_number

    return s_claim_number

def get_customer(i, policy_date, ls_customers):
    r = random.randint(0,len(ls_customers) - 1)
    customer = ls_customers[r]
    # search backwards while the customer's birthday is not 20 years later than the policy sell date
    #                    birthday + 18                   birthday + 75
    cycles = 0
    while ((policy_date < customer[-2] or policy_date > customer[-1]) and cycles < 5000):
        cycles = cycles + 1
        if r >= 13:
            r = r - 13
            customer = ls_customers[r]
        else:
            r = len(ls_customers) - 1
            customer = ls_customers[r]

    return customer

def generate_claims(s_policy_number, d_cover_start, coverage_building, sum_insured_building, coverage_personal, sum_insured_personal, ls_claims):
    global next_claim_number

    num_claims_building = random.randint(0, 3)
    num_claims_personal = random.randint(0, 2)

    if coverage_building == 'Y':
        for i in range(0, num_claims_building):
            s_claim_number = claim_number(next_claim_number)
            next_claim_number = next_claim_number + 1
            
            d_claim_date = fake.date_between_dates(date_start=d_cover_start, date_end=datetime.today())
            d_date_settled = fake.date_between_dates(date_start=d_claim_date, date_end=(d_claim_date + relativedelta(months=5)))
            s_claim_type = 'BUIDLING'
            f_claim_amount = 0.0
            while f_claim_amount == 0.0:
                f_claim_amount = random.random()
            f_claim_amount = 110000 * f_claim_amount
            f_settled_amount = f_claim_amount if f_claim_amount <= float(sum_insured_building) else float(sum_insured_building)
            s_claim_rason = random.choice(['FLOOD', 'WIND', 'FIRE', 'LIGHTNING', 'THEFT', 'VANDALISM', 'EXPLOSION', 'WATER/HEATING/AC', 'ELECTRICAL_CURRENT'])

            ls_claims.append((s_claim_number, s_policy_number, d_claim_date, d_date_settled, s_claim_type, f_claim_amount, f_settled_amount, s_claim_rason, datetime.today()))

    if coverage_personal == 'Y':
        for i in range(0, num_claims_personal):
            s_claim_number = claim_number(next_claim_number)
            next_claim_number = next_claim_number + 1
            
            d_claim_date = fake.date_between_dates(date_start=d_cover_start, date_end=datetime.today())
            d_date_settled = fake.date_between_dates(date_start=(d_claim_date + relativedelta(months=1)), date_end=(d_claim_date + relativedelta(months=5)))
            s_claim_type = 'PERSONAL_ITEMS'
            f_claim_amount = 0.0
            while f_claim_amount == 0.0:
                f_claim_amount = random.random()
            f_claim_amount = 11000 * f_claim_amount
            f_settled_amount = f_claim_amount if f_claim_amount <= float(sum_insured_personal) else float(sum_insured_personal)
            s_claim_rason = random.choice(['FLOOD', 'WIND', 'FIRE', 'LIGHTNING', 'THEFT', 'VANDALISM', 'EXPLOSION', 'WATER/HEATING/AC', 'ELECTRICAL_CURRENT'])

            ls_claims.append((s_claim_number, s_policy_number, d_claim_date, d_date_settled, s_claim_type, f_claim_amount, f_settled_amount, s_claim_rason, datetime.today()))


def delete_file(filename):
    try:
        os.remove(filename)
    except OSError as e: 
        if e.errno != errno.ENOENT: # errno.ENOENT = no such file or directory
            raise # re-raise exception if a different error occurred

if __name__ == '__main__':
    main()
