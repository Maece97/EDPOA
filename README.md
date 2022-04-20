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

## Diagrams
See system diagrams under TODO add links

## Results

### Assignment 1 - Experiment with Kafka:

For our system we decided to investigate two aspects of Kafka and how they can lead to data loss. These two aspects are consumer lag and offset misconfigurations.

- **The Risk of Data Loss Due to Consumer Lag**:
Consumer lag occurs when producers write messages to a topic at a faster rate than consumers consume them. This is highly relevant to our system due to the highly elastic nature of credit card transactions - where there can be large spikes in the number of incoming transactions. When consumer lag occurs, you will notice that the difference between the offset of the lastest message and the consumer offset grows larger, or in other words the backlog of unprocessed messages starts growing. If this backlog becomes large enough, it can happen that messages get lost due to them becoming older than the retention policy allows for or the storage size limit being reached before they get processed. This would then potentially lead to those messages getting lost. To experiment with this concept, you can create a simple producer that produces messages very fast and a simple consumer that takes long to process each message before consuming the next one. If you then set the retention policy to some small amount of time, you will see that messages will start to get deleted before they are consumed.

- **The Risk of Data Loss Due to Offset Misconfigurations**: In testing the aspect described above we also discovered another interesting aspect of Kafka that can lead to data loss. When a consumer starts to read a partition that does not have a committed offset, or if the committed offset it has is invalid (e.g. because the record it points to has already been deleted), then Kafka assigns it one of three values, based on the auto.offset.reset property. If this property is set to "latest" (which is the default), then the consumer will start reading from the latest message. If the value is "ealiest", then the consumer will start reading from the earliest valid offset for the partition (i.e. it will read all the messages in the partition). If the value is none, then an exception will be thrown when attempting to consume from a valid offset. The intersting thing is that the default value, "latest", can cause data loss. Consider the example where we are using this default value and we are experiencing serious consumer lag. Consumer A is reading from partitions 1 and 2. Consumer A is so far behind that their last committed offest for patition 2 refers to a message that has already been deleted. Now, if a new consumer join and during rebalancing gets assigned to parition 2, then the new consumer will start reading from the latest message. This means that all of the messages between the earliest and the latest message will not be read by the new consumer and if this is not noticed by an administrator in time these messages will eventually be deleted and lost. To experiment with this concept, set up the same scenario as described for the previous concept. Once the last committed offset is older than the oldest message retained in a partition, add a new consumer to the consumer group. If you are (un)lucky enough, then the new consumer will be assigned to this partition and will start reading from the latest message. You will notice that the ealier messages will be effectively ignored and eventually deleted.

## Reflections

This section will outline learnings we have gained from designing and implementing our system. 

- What we found out from working with Kafka is that it is very easy to set up, but you have to be careful to set it up right so that you don't experience unexpected consequences such as data loss. For example, you should have enough partitions in order to be able to scale your number of consumers to keep up with producers that produce faster than a sungle consumer/thread can keep up with. Moreover, you need to carefully read through the default configuration values before moving into production. Failing to do so can lead to the situation described in our Results section where old messages where ignored by a new consumer - a behaviour you wouldn't expect as the default one. 
- Camunda is very handy. However, it does have a steep learning curve. 
- Asynchronous workflows in Camunda need special care (concurrent process modification, timing issues, correlation of messages…)
- Camunda does not use a new thread for parallel running activities (would have saved me probably a day of work :D)
- Special issues with async execution and Camunda: if the process instance is prematurely terminated (error etc) and a message is correlated with this instance it just crashes
- Mixing Spring-Kafka and Cloud Streams did not work very well TODO EXPAND
- Pretty nice about Kafka (not necessarily useful or Kafka specific but came to my mind quite often): No need to worry about those shitty ports and changing the environment @ 128593 different places


## Editorial Notes

In this section we explain some things and decisions that might not be clear from the other sections or documents.

- The Card service only contains the card limit in our implementation. In practice this service should also contain more information on the card and more business logic about how that information can change. For example, it should contain the status of the card (open, closed, etc.) and business logic on how these can change. 

## Responsibilities 
TL;DR We all did everything together, and I mean EVERYTHING 😈

Editorial note: we believe that the work was evenly distributed and that each of the group members had about the same workload. 




