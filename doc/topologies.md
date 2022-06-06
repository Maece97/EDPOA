# Topology Descriptions

## Transaction Preprocessing Topology

TODO JONAS - Remember to embed the image in text

## Transaction Postprocessing Topology

![Transaction Postprocessing Topology - Diagram](diagrams/topologies/transaction_postprocessing_topology_diagram.png)
### Topology Description
- **All Transactions**: All transactions (accepted and rejected) arrive via Kafka to the Transaction Postprocessing Service. Here they are consumed via a **KStream**.
- **Content Filter**: Transactions coming from the Transaction Service contain information (e.g. PIN) which we don't need in the Fraud Detection workflow. This service removes this unnecessary information from the event.
- **Interactive Queries**: The transactions are quariable via an http endpoint. This allows system users to query information on which transactions got accepted/rejected by the transaction service.

### Other considerations
- We are using **Avro** here to have a shared understand of the Transaction Object between the different Services.
  - The Avro schemas are stored in a avro repository.
## Fraud Preprocessing Topology

![Fraud Preprocessing Topology - Diagram](diagrams/topologies/fraud_preprocessing_topology_diagram.png)

### Topology Description
- **Filtered Transactions**: The content-filtered transactions arrive via Kafka to the Fraud Preprocessing Service. Here they are consumed via a **KStream**.
  - **Custom timestamp extraction**: We extract the event time from the transaction event in order to use that time for the windowing. We believe the even time semantics to best fit our use case since we want to know about transactions that occurred at similar times, regardless of when they were ingested or processed. We use the partition time as a fallback when extracting the event time fails. 
- **Map**: The first operation is to map the incoming transactions. This is done because the incoming transactions do not have any key. Therefore, we map the transactions such that their key is the card number used in the transaction. This then allows us to group by the card number when creating the aggregation later on.
TODO THE KEY IS NOT EMPTY, WHAT IS IT?
- **Windowed Aggregation**: We then use a windowed aggregation to monitor if a card number (**groupByKey**) has a large number of transactions (**count**) in a short period of time (more than 5 transactions within 60 seconds). Here we use **tumbling window type**.
- **Suppress**: We suppress the aggregation for 5 seconds in order to allow for out of order events to be accounted for. This could be adjusted based on average delays in transactions but we believe 5 seconds to be a safe first estimate 
- **Filter**: We filter out counts that are lower than 5. This is because to constitute a significant number of transactions during a 60 second window we need at least 5 transactions. 
- **Interactive Queries**: We expose the aggregation results via an http endpoint. This allows administrators of the to query the system and get reports of cards that have had events of a large number of transactions within a short period of time

### Trade-Offs

TODO KRIS - Remember trade-offs -> window type - Remember to embed the image in text

Trade-off: Partition stuff on the slides

Also look at the considerations on the slides

## Avro Discussion

TODO Kris talk to Marcel about final Avro implementations and add here discussion of where we use it and why

Trade-offs: 
- Avro gives an easy option to manage TransferObjects. But also adds complexity and depenedability on the avro framework.
- We decided that we want to use Avro in the second part as we share a common Transaction Class over multiple servers. Avro helps as to sync this over all servers, without changing each service individually when the Transaction Class changes.
