create table post (
                      id varchar(50),
                      image varchar(50),
                      content longtext,
                      status tinyint,
                      created_at timestamp,
                      updated_at timestamp,
                      deleted_at timestamp,
                      created_by varchar(50),
                      updated_by varchar(50),
                      deleted_by varchar(50),
                      user_id varchar(50),
                      primary key(id)
);