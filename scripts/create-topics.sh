echo "Waiting for Kafka to come online..."
cub kafka-ready -b kafka:9092 1 20
# create the src-topic topic
kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic incoming-transactions \
  --replication-factor 1 \
  --partitions 1 \
  --create
# create the out-topic topic
kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic preprocessed-transactions \
  --replication-factor 1 \
  --partitions 1 \
  --create
# create the transaction-filtered topic
kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic transaction-filtered \
  --replication-factor 1 \
  --partitions 1 \
  --create
# create the transaction-fraud-preprocessed topic
kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic transaction-fraud-preprocessed \
  --replication-factor 1 \
  --partitions 1 \
  --create
# Test topic
kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic src-topic \
  --replication-factor 1 \
  --partitions 1 \
  --create
sleep infinity









