#!/usr/bin/env bash

for ((i=1;i<=365;i++))
do
let b=i+1
PNAME_DAY1=p`date -d "$i day" +%Y%m%d`;DATE_DAY1=`date -d "$b day" +%Y-%m-%d`;
#echo $PNAME_DAY1;echo $DATE_DAY1;
mysql -h10.100.5.41 -ugaren -pgaren1234 -e "alter table vayne.monitor_data_records ADD PARTITION ( PARTITION $PNAME_DAY1 VALUES LESS THAN (TO_DAYS('$DATE_DAY1')));"
done