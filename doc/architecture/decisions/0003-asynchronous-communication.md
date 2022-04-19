# 3. Asynchronous Communication

Date: 2022-04-17

## Status

Accepted

## Context

The system will be implemented using the Event-driven microservices architecture. Communication between services could either be done synchronously or asynchronously. 

## Decision

Communication withing the system will be asynchronous.

Asynchronous communication suits the system better than synchronous communication. This will facilitate looser coupling between services.

Elasticity, one of the system's top 3 driving architectural characteristics, will be positively impacted as individual services can be scaled up and down as needed.

Fault tolerence (another of the system's top 3 driving architectural characteristics) will also improve as services do not need to wait for other services to respond before responding themselves.

## Consequences

Asynchronous communication makes error handling more complex. At this moment in time we do not see this coming a problem. However, if the error handling becomes overly complex, then we might need to implement the workflow event pattern in order to combat this increased complexity.
