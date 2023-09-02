create table user (
                      id varchar(50),
                      avatar varchar(50),
                      full_name varchar(50),
                      username varchar(50),
                      password varchar(100),
                      roles varchar(50),
                      status tinyint,
                      created_at timestamp,
                      updated_at timestamp,
                      deleted_at timestamp,
                      created_by varchar(50),
                      updated_by varchar(50),
                      deleted_by varchar(50),
                      primary key(id)
);