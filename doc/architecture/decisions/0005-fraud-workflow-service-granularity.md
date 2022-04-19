# 5. Fraud Workflow Service Granularity

Date: 2022-04-17

## Status

Accepted

## Context

The three bounded contexts that make up the Fraud Detection Workflow, namely the Fraud Detection Context, the Fraud Investigation context, and the Transaction Dispute Context can all be implemented as consolidated service(s) or as three seperate services.

## Decision

All three bounded contexts will be implemented as separete services.

Most importanly, the three services need to scale differently. Every accepted transaction needs to be ran through the Fraud Detection Service. However, only a small portion of these transactions will be flagged as potential fraud and continue to the Fraud Investigation service, which in turn will only send a portion of those transaction to the Transaction Dispute service as a Fraud Dispute. 

Secondly, the three contexts encaptulate different functionality and thus have different reasons to change. The Transaction Dispute service differs quite significantly from the other two as it needs to handle many types of disputes other than just fraud. Thus it will be involved in many other workflows. 

Lastly, the Fraud Detection service will contain a machine learning model that will be redployed quite frequently. Thus it is better to keep this in a separete service so that the other contexts do no need to be redeployed with it.

## Consequences

One consequence of this decision is the added latency from the interservice communication. However, this is not a huge concern as this process occurs offline and includes many manual steps (in the Fraud Investigation service and Transaction Dispute service). Thus the milliseconds added by interservice communication will not have a significant effect on the efficiency of the process. 