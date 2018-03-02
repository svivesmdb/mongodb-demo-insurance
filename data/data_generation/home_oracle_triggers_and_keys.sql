CREATE OR REPLACE TRIGGER trg_c_last_update
              BEFORE INSERT OR UPDATE ON customer
                 FOR EACH ROW
   BEGIN
      :NEW.last_change := sysdate;
   END;
/

CREATE OR REPLACE TRIGGER trg_p_last_update
              BEFORE INSERT OR UPDATE ON policy
                 FOR EACH ROW
   BEGIN
      :NEW.last_change := sysdate;
   END;
/

CREATE OR REPLACE TRIGGER trg_pc_last_update
              BEFORE INSERT OR UPDATE ON policy_coverage
                 FOR EACH ROW
   BEGIN
      :NEW.last_change := sysdate;
   END;
/

CREATE OR REPLACE TRIGGER trg_po_last_update
              BEFORE INSERT OR UPDATE ON policy_option
                 FOR EACH ROW
   BEGIN
      :NEW.last_change := sysdate;
   END;
/   

CREATE OR REPLACE TRIGGER trg_pr_last_update
              BEFORE INSERT OR UPDATE ON policy_risk
                 FOR EACH ROW
   BEGIN
      :NEW.last_change := sysdate;
   END;
/  

CREATE OR REPLACE TRIGGER trg_cl_last_update
              BEFORE INSERT OR UPDATE ON claim
                 FOR EACH ROW
   BEGIN
      :NEW.last_change := sysdate;
   END;
/

--alter table "CAR_CUSTOMER" add constraint PK_CAR_CUSTOMER primary key("CUSTOMER_ID")    
