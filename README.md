# EDPOA

## General Description 
Our system is a credit card transaction processing system that both processes transactions coming in, and detects fraudulent transactions. Below you will find a basic system overview:
$Jonas: Clearify which is the Transaction and which the Fraud Workflow in the diagram. pretty important to get the stuff below
TODO Add link to system diagram

## Running the system 

### Services

TODO Add all services and ports and then add the ports to the instructions below

| Name                | Port |
| ------------------- | ---- |
| transaction         | 8100 |
| model               | 8101 |
| fraud investigation | 8102 |
| transaction dispute | 8103 |
| pin                 | 8105 |
| blocking-rules      | 8108 |
| card                | 8109 |

### How to run
1. Start all services

2. Add a card to the database by sending the following request to http://localhost:TODO


```json
{
  “cardNumber”:“123456”,
  “limit”:“1100”
}
```

Note: You can also update the limit of a card by sending the same request with an existing card number and a new limit

3. To start the whole process send the following request to http://localhost:TODO
Note:the pin is incorrect so the transaction will not be accepted. The correct pin would be 1234

```json
{
 “messageName” : “Transaction”,
 “businessKey” : “93421e4e351351ddadadssd12e12",
 “processVariables” : {
  “amount” : {“value” : “100", “type”: “String”},
  “pin”: {“value”:“18234”, “type”:“String”},
  “cardNumber”: {“value”:“123456", “type”:“String”},
  “country”:{“value”:“GER”,“type”:“String”},
  “merchant”:{“value”:“Migros”,“type”:“String”},
  “merchantCategory”:{“value”:“Bitcoin”,“type”:“String”},
  “currency”:{“value”:“EUR”,“type”:“String”},
  “tries”:{“value”:“0”,“type”:“String”}
 }
}
```

**Important**: the business key needs to be unique among all running process instances because it is used to correlate a
Kafka response to the correct service


## Concepts Covered

This section describes how our system implements the concepts covered in the first six lectures of the course. For a discussion of the architectural decisions surrounding the adoptions of these concepts and their trade-offs, please refer to our architectural decision records (ADRs).
$Jonas: Link to ADRs
### Lecture 1
- **Event-driven Architecture**
  - **Event Notification**: Our system employs the Event Notification pattern most notably in the Fraud Detection Workflow. Here, the three services consume and react to events in a choreographed manner. The Fraud Detection Service consumes events about accepted transactions, produced by the Transaction Service. It then reacts by checking if the accepted transaction has a high likelihood of being fraudulent. If so, then it produces a Potential Fraud event, which is consumed by the Fraud Investigation Service. The Fraud Investigation Service reacts by having a customer service representative investigate the nature of the transaction. If the transaction is indeed fraudulent then the Fraud Investigation Service produces a Fraud Dispute and a corresponding event. This event is picked up by the Transaction Dispute Service. This service marks the transaction as fraudulent and produces a Confirmed Fraud event which is finally consumed by the Fraud Detection Service. The Fraud Detection Service reacts by incorporating the newly confirmed fraud into its machine learning process.
  - **Event-carried State Transfer**: Our system implements this pattern in the way that the Card Service communicates all card changes to the Transaction Service via events. The Transaction Service then stores these details in a persisted database $Jonas:Öhm...actually not-we dont have a db so that it can refer to them when deciding on whether to accept a transaction or not. 
  - **Kafka**: All of our internal events are published via Kafka. The Transaction workflow uses five topics. The Transaction Service uses one topic to send Check PIN commands to the PIN Service and another topic to listen to PIN Check responses. A similar topic structure is used to communicate with the Transaction Blocking Rules service. A single topic is then used by the Card Service to produce events when a card limit is updated. TODO Add how it works in the Fraud workflow.

### Lecture 2
- **Choreography**: The Fraud Detection Workflow is coordinated via Choreography. This is already described in the section about the Event Notification pattern above.
Stateless Choreography: The Fraud Detection Workflow follows the Stateless Choreography pattern as the workflow state is not explicitly stored in any one service and is not sent via the events. Rather it it implied by the event itself. For example, we know a transaction is confirmed as fraud once the Fraud Investigation Service emits the Confirmed Fraud event containing the transaction ID. We do not need to stamp the event with any further workflow information or keep track of the status of the workflow in any of the other services.  
- **Kafka with Spring**: We used Spring Kafka in our system to develop our Kafka producers and consumers. $Jonas: Marcel used Cloud Streams afaik

### Lecture 3: 
- **Orchestration**: $Jonas: If not in the ADRs:mention why we are using orchestration here and choreo earlier(complexity....)The Transaction Workflow is orchestrated locally within the Transaction Service. This service encapsulates the domain knowledge required to know when a transaction should be accepted and when it should be rejected, and no other domain knowledge. For example, the Transaction Service knows that some transactions need to be accompanied by a valid PIN entry while others aren’t. It does not however need to know how to check if the PIN is valid. This is the responsibility of the PIN service. Whenever a transaction comes in that does require the PIN to be checked, the Transaction Service will command the PIN Service to validate the PIN entry sent with the transaction. 

- **Process Engines - Camunda**: We modelled the orchestration of the Transaction Workflow using Camunda. The following features of Camunda are utilised within our system: 

  - **Durable State**: The engine keeps track of the running process instances. This is - among others - necessary to be able to correlate incoming Kafka messages with the correct instance.
  - **Scheduling**: The process features a timeout functionality. If the authentication process takes too long, it is cancelled. This offers the chance to react accordingly - in our case with a stateful retry.
  - **Support for Human / Machine Collaboration**: The process includes a „user task” for reentering the pin. Camunda provides a handy option here to provide a UI for interaction with the user.
  - **Tooling**: The Camunda modeler was used to create the BPMN diagram. Other than that, the Camunda libraries were used to connect to the services via the service tasks.

### Lecture 4:
- **Events and Commands**: The Transaction Workflow is characterised by the Transaction Service issuing commands, such as the Check PIN command to the PIN Service, and receiving the response via events, such as the Correct PIN event. The Fraud Detection workflow only employs events however. This matches the two workflows being orchestrated and choreographed respectively. 

### Lecture 5: 
- **Stateful resilience patterns**:
  - **Stateful retry**: When the Transaction workflow times out before all external services have responded then the whole process gets aborted and retried. This works since all of the operations taken during execution of the process are *idempotent*
  - **Human intervention**: When the PIN service replies that the PIN entered was incorrect, the Transaction service prompts a human to try re-entering a correct PIN.

## Architectural Decisions and Trade-Offs
You can find our ADRs here 

TODO add link

## Editorial Notes

In this section we explain some things and decisions that might not be clear from the other sections or documents.

- The Card service only contains the card limit in our implementation. In practice this service should also contain more information on the card and more business logic about how that information can change. For example, it should contain the status of the card (open, closed, etc.) and business logic on how these can change. 

## Diagrams
See system diagrams under TODO add links

## Results

### Assignment 1 - Experiment with Kafka:

## Reflections
Marcel is indeed the 🐐 

Use this to outline where we maybe made mistakes in the design eg breaking boundaries 

## Responsibilities 
TL;DR We all did everything together, and I mean EVERYTHING 😈

Editorial note: we believe that the work was evenly distributed and that each of the group members had about the same workload. 




