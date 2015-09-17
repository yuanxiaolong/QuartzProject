use vayne;

drop table if exists records_id;
create table records_id(id int);
insert into records_id values(0);

drop PROCEDURE if exists merge_records;
delimiter $$
create PROCEDURE merge_records(period int)
begin
  declare begin_id int default 0;
  declare end_id int default 0;

  DECLARE exit handler for SQLEXCEPTION
  begin
    rollback;
    select 100 as errcode;
  end;

  select id into begin_id from records_id limit 1;
  select max(id) into end_id from monitor_data_records;

  start TRANSACTION ;
    insert into monitor_data_records_merge(buss_no, data_type, total_rows, merge_timestamp)
    select buss_no, data_type, sum(total_rows) as total_rows,
    from_unixtime(floor(unix_timestamp(send_timestamp)/60/period)*60*period,"%Y-%m-%d %H:%i:%S") as send_timestamp
    from monitor_data_records
    where id>begin_id and id<=end_id
    GROUP by buss_no, data_type, floor(unix_timestamp(send_timestamp)/60/period)
    on duplicate key update total_rows=total_rows+values(total_rows);
    update records_id set id=end_id;
  commit;
  select 0 as errcode;
end $$
delimiter ;