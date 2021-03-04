echo start time: $(date)
day=`date +%Y%m%d`
cd /APPKIS/INCLUDE
java -cp "pkis-backend-1.0.0.jar:/APPKIS/INCLUDE/*" com.tradevan.pkis.backend.batch.ImportStatisticalReportBatch
echo end time: $(date)

