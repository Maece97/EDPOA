# 4. Transaction Workflow Service Granularity

Date: 2022-04-17

## Status

Accepted

## Context

The four bounded contexts that make up the Transaction Workflow, namely the Transaction Context, the PIN context, the Card context, and the Transaction Blocking Rules Context can all be implemented as consolidated service(s) or as three seperate services.

## Decision

All four bounded contexts will be implemented as separete services.

Firstly, the four bounded contexts all have have distinct purposes and reasons to change. The Transaction context is all about the business logic of when a transaction should be accepted or rejected. The PIN context focuses on calculating the correctness of a PIN. The Card context contains business logic about card details and how these can change. Similarly, the Transaction Blocking Rules context contains business logic about temporary rules that block transactions and how these can be changed and by whom. 

Secondly, it is very important that the PIN context is kept separate from other contexts due to its very high security requirements.

Lastly, these contexts need different throughputs and thus the services containing them need to scale at different. Every transaction needs to go through the Transaction context, but not the others. 

## Consequences

The most significant consequence of this decision is the increase in interservice communication. This will impact performance as the Transaction service will have to wait for a response from the other services before accepting/rejecting some transactions. The addded latency of interservice communication will thus impact performance negatively.
