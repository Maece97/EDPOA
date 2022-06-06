# 10. Stream Processing Service Granularities

Date: 2022-06-01

## Status

Accepted

## Context

The Transaction Preprocessing, Transaction Postprocessing, and Fraud Preprocessing service can all be implemented within integrated services or a standalone microservices.

## Decision

All three services will be implemented as standalone microservices.

Firstly, the Transaction Preprocessing service should be implemented seperately because it is a part of our most important workflow (the Transaction workflow), unlike the other two stream processing services which occur a transaction has been accepted/rejected. Thus it has very high fault tolerence requirements as problems with this service might mean that customers experience disruptions in being able to use their cards. 

Secondly, the Transaction Postprocessing service should be implemented seperately from the Fraud Preprocessing service because the output of the Transaction Postprocessing is meant for all post-transaction workflow processing. Therefore, it has different domain requirements to the Fraud Preprocessing service. Similarly, the Fraud Preprocessing service would in practice most likely be maintained by the software team working on the fraud detection process while the Transaction Postprocessing service would most likely be maintained by another team. 

## Consequences

The most significant consequence of this decision is the increase in interservice communication. However, this is not a huge cause of concern to our system since most of this increase is in the Fraud Detection workflow, which we have already established does not have significant performance requirements.

