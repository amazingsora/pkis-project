echo start time: $(date)
day=`date +%Y%m%d`
cd /APPKIS/JAR
java -cp "pkis-backend-1.0.0.jar:/APPKIS/INCLUDE/*" com.tradevan.pkis.backend.batch.SampleBatch
echo end time: $(date)

