DROP TABLE mail_log IF EXISTS;

CREATE TABLE mail_log (
  id         INTEGER PRIMARY KEY,
  gw_message_id  VARCHAR(50),
  original_message_id VARCHAR(50),
  sender_email VARCHAR(50),
  return_path  VARCHAR(50),
  received_time_stamp TIMESTAMP,
  last_status VARCHAR(20)
);