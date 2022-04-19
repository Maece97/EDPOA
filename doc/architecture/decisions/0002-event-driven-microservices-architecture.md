# 2. Event-driven Microservices Architecture

Date: 2022-04-17

## Status

Accepted

## Context

The system is made up of five distinct bounded contexts. The way that these bounded contexts function together to fulfil the system requirements can be based on many different architectures.

## Decision

The system will follow the Event-Driven Microservices architecture.

The Event-Driven Microservices architecture suits our system's driving architectural characteristics particularly well. Elasticity and fault tolerance are two of the systems top 3 driving characteristics and agility and evolvability are two of the systems other driving characteristics. These are all characteristics that the Event-Driven Microservices architecture excels at. Lastly, by employing microservices we can partition the bounded contexts into separete services. This is extremely important as security is one of the system's driving characteristics, and some bounded contexts have significantly higher security requirements than others (e.g. the PIN context). 

We do not expect to have a single monolithic database, so this is not a concern.

## Consequences

There is a considerable amount of communication between bounded contexts. This could cause responsiveness and performance issues due to added latency. The responsiveness issues can be partly mitigated by using ascynronous communication between services. The performance issues could be solved by using a Spaced-based architecture. However, the large amounts of data that would need to be stored in memory make this architecture unfeasable for the system. 
