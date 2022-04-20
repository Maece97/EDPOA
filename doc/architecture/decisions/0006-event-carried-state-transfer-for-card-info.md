# 6. Event-carried State Transfer for Card Information

Date: 2022-04-17

## Status

Accepted

## Context

Whether a transaction is accepted or not depends on a number of attributes of the card that was used (e.g. whether it's limit has been reached). The Transaction service thus needs this information to decide whether to accept a transaction, but this information is managed by the Card service. The Transaction service could either query the Card service everytime it needs this information (i.e. for every incoming transaction) or the Event-carried State Transfer pattern could be implemented where the Card service communicates updates and the Transaction service could keep a copy of the data it needs. 

## Decision

The Event-carried State Transfer pattern will be implemented. 

The most important reason for this is that it improves performance. Since the Transaction service will have the information stored locally, it does not have to query the Card service which eliminates the latency added by interservice communication. 

Another important factor in this decision is that card information does not update very frequently, so instead of the Transaction service querying the Card service for every transaction, perhaps millions of times per day, the Card service only has to communicate changes which are maybe in the low thousands per day. This also menas that the Card service does not have to scale with the Transaction service. 

## Consequences

The biggest consequence of this decision is that it will introduce eventual consistency. There is a chance that the information that the Transaction service uses will go stale and be out of sync for a moment. However, based on domain knowledge, the impact of this should be much smaller than the impact of the perfomance gains. 
