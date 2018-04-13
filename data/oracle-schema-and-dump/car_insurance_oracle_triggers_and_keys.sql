ALTER TABLE CAR_CLAIM ADD CONSTRAINT PK_CAR_CLAIM PRIMARY KEY (CLAIM_ID);
ALTER TABLE CAR_CUSTOMER ADD CONSTRAINT PK_CAR_CUSTOMER PRIMARY KEY (CUSTOMER_ID);
ALTER TABLE CAR_POLICY ADD CONSTRAINT PK_CAR_POLICY PRIMARY KEY (POLICY_ID);

ALTER TABLE CAR_CLAIM ADD CONSTRAINT FK_C_CLAIM_POLICY FOREIGN KEY (POLICY_ID) REFERENCES CAR_POLICY (POLICY_ID);
ALTER TABLE CAR_POLICY ADD CONSTRAINT FK_C_POLICY_CUSTOMER FOREIGN KEY (CUSTOMER_ID) REFERENCES CAR_CUSTOMER (CUSTOMER_ID);

CREATE OR REPLACE TRIGGER trg_car_c_last_update
              BEFORE INSERT OR UPDATE ON car_customer
                 FOR EACH ROW
   BEGIN
      :NEW.last_change := sysdate;
   END;
/

CREATE OR REPLACE TRIGGER trg_car_p_last_update
              BEFORE INSERT OR UPDATE ON car_policy
                 FOR EACH ROW
   BEGIN
      :NEW.last_change := sysdate;
   END;
/

CREATE OR REPLACE TRIGGER trg_car_cl_last_update
              BEFORE INSERT OR UPDATE ON car_claim
                 FOR EACH ROW
   BEGIN
      :NEW.last_change := sysdate;
   END;
/

