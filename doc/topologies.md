# Topology Descriptions

## Transaction Preprocessing Topology

TODO JONAS - Remember to embed the image in text

## Transaction Postprocessing Topology

![Transaction Postprocessing Topology - Diagram](diagrams/topologies/transaction_postprocessing_topology_diagram.png)

## Fraud Preprocessing Topology

![Fraud Preprocessing Topology - Diagram](diagrams/topologies/fraud_preprocessing_topology_diagram.png)

- **Custom timestamp extraction**: We extract the event time from the transaction event in order to use that time for the windowing. We believe the even time semantics to best fit our use case since we want to know about transactions that occurred at similar times, regardless of when they were ingested or processed. We use the partition time as a fallback when extracting the event time fails. 
- **Map**: The first operation is to map the incoming transactions. This is done because the incoming transactions do not have any key. Therefore, we map the transactions such that their key is the card number used in the transaction. This then allows us to group by the card number when creating the aggregation later on.
TODO THE KEY IS NOT EMPTY, WHAT IS IT?
- **Windowed aggregation**: We then use a windowed aggregation to monitor if a card number (**groupByKey**) has a large number of transactions (**count**) in a short period of time (more than 5 transactions within 60 seconds). Here we use **tumbling window type**.
- **Suppress**: We suppress the aggregation for 5 seconds in order to allow for out of order events to be accounted for. This could be adjusted based on average delays in transactions but we believe 5 seconds to be a safe first estimate 
- **Filter**: We filter out counts that are lower than 5. This is because to constitute a significant number of transactions during a 60 second window we need at least 5 transactions. 
- **Interactive queries**: We expose the aggregation results via an http endpoint. This allows administrators of the to query the system and get reports of cards that have had events of a large number of transactions within a short period of time

TODO KRIS - Remember trade-offs -> window type - Remember to embed the image in text

Trade-off: Partition stuff on the slides

Also look at the considerations on the slides