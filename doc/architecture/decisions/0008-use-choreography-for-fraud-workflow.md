# 8. Use Choreography for Fraud Workflow

Date: 2022-04-17

## Status

Accepted

## Context

The Fraud Detection workflow is the workflow that takes as input an accepted transaction and decides whether the transaction is fraudulent or not. This workflow can either be orchestrated or choreographed.

## Decision

The Fraud Detection workflow will be choreographed.

The most important reason for this is that the workflow is not very complex and does not have many error conditions. Moreover, the chain of events that occur in the workflow are unlikely to change. Thus an orhestrator would not provide a large benefit. 

## Consequences

This workflow will benefit from the service decoupling and scalability and responsiveness improvements. However, these are not critical for this workflow. 

The most important drawback of having this workflow choreographed is the loss of visability of the workflow and state management becoming harder. However, this should not be a significant issue.