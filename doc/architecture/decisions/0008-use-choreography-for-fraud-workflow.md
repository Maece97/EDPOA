# 8. Use Choreography for Fraud Workflow

Date: 2022-04-17

## Status

Accepted

## Context

The Fraud Detection workflow is the workflow that takes as input an accepted transaction and decides whether the transaction is fraudulent or not. This workflow can either be orchestrated or choreographed.

## Decision

The Fraud Detection workflow will be choreographed.

The most important reason for this is that the workflow is not very complex and does not have many error conditions. Thus, even though the worflow does not need the performance advantages of choreography, an orchestrator would not provide a large benefit. 

## Consequences

The most important drawback of having this workflow choreographed is that it could be hard to keep track of the status of a transaction within the workflow. 

TODO add more advantages/disadvantages