create table follower (
                          id varchar(50),
                          status tinyint,
                          created_at timestamp,
                          updated_at timestamp,
                          deleted_at timestamp,
                          created_by varchar(50),
                          updated_by varchar(50),
                          deleted_by varchar(50),
                          user_id varchar(50),
                          follower_id varchar(50),
                          primary key(id)
);