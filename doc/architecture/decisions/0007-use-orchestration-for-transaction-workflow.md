# 7. Use Orchestration for Transaction Workflow

Date: 2022-04-17

## Status

Accepted

## Context

The Transaction workflow is the workflow that takes as input a transaction and decides wheter or not is should be accepted or rejected. This workflow can either be choreograped or orchestrated.

## Decision

This workflow will be orchestrated by the Transaction Service.

The most important reason for why this workflow should be centralised is the semantic complexity contained in the workflow. This semantic complexity arises due to the amount of business logic contained in the reasoning for when a transaction should and should not be accepted, the high frequency at which this business logic can change, and the number of different services that need to be queried to fulfil the workflow. Having this workflow centralised allows us to contain the change scope within one service. 

## Consequences

Mainly the coupling and the Transaction service as a central point of failure. 
TODO: flesh out
