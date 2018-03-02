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

