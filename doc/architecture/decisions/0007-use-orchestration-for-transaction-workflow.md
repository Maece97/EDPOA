# 7. Use Orchestration for Transaction Workflow

Date: 2022-04-17

## Status

Accepted

## Context

The Transaction workflow is the workflow that takes as input a transaction and decides whether or not it should be accepted or rejected. This workflow can either be choreographed or orchestrated.

## Decision

This workflow will be orchestrated by the Transaction Service.

The most important reason why this workflow should be centralised is the semantic complexity contained in the workflow. This semantic complexity arises due to the amount of business logic contained in the reasoning for when a transaction should and should not be accepted, the high frequency at which this business logic can change, and the number of different services that need to be queried to fulfil the workflow. Having this workflow centralised allows us to contain the change scope within one service. 

## Consequences

Having an orchestrator does introduce a central point of failure - which in this case would be the transaction service. However, a choreographed version of this workflow would probably have this central point of failure, as well as the workflow being very complex, and it would probably have to follow the front controller pattern. 

