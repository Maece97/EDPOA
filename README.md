# EDPOA

## General Description

TODO JONAS Update diagram and text

Our system is a credit card transaction processing system that both processes transactions coming in and detects fraudulent transactions. Below you will find a basic system overview. The services highlighted in blue make up the Transaction workflow, while the services highlighted in green make up the Fraud Detection workflow. Blue arrows indicate synchronous communication, and black arrows asynchronous communication:

![System Overview - Diagram](doc/diagrams/System%20Overview.png)

## Service Descriptions

TODO JONAS 1-2 sentences about each service
## Running the system

TODO Marcel update this 
### Services

| Name                | Port |
| ------------------- | ---- |
| transaction         | 8080 |
| transaction-faker   | 8100 |
| model               | 8101 |
| fraud-investigation | 8102 |
| transaction-dispute | 8103 |
| exchange-rates      | 8104 |
| pin                 | 8105 |
| blocking-rules      | 8108 |
| card                | 8109 |

### How to run

Example requests for Postman can be found [here](doc/EDPO%20Showcase.postman_collection.json).

1. Start Kafka with `docker-compose -f docker-compose-kafka.yml up`

2. Start all the Spring Boot services

NEW 2. Send a fake transaction: localhost:8100/faker/transactions. Body is the amount of transactions

```
1
```

3. Add a card to the database by sending the following request to localhost:8109/limit/update

```json
{
  "cardNumber": "123456",
  "limit": "1100"
}
```

Note: You can also update the limit of a card by sending the same request with an existing card number and a new limit

4. To start the whole process send the following request to localhost:8080/engine-rest/message
   Note:the pin is incorrect so the transaction will not be accepted. The correct pin would be 1234

```json
{
  "messageName": "Transaction",
  "businessKey": "93421e4e351351ddadadssd12e12",
  "processVariables": {
    "amount": { "value": "100", "type": "String" },
    "pin": { "value": "18234", "type": "String" },
    "cardNumber": { "value": "123456", "type": "String" },
    "country": { "value": "GER", "type": "String" },
    "merchant": { "value": "Migros", "type": "String" },
    "merchantCategory": { "value": "Bitcoin", "type": "String" },
    "currency": { "value": "EUR", "type": "String" },
    "tries": { "value": "0", "type": "String" }
  }
}
```

**Important**: the business key needs to be unique among all running process instances because it is used to correlate a
Kafka response to the correct service.

## Concepts Covered

This section describes how our system implements the concepts covered in the first six lectures of the course. For a discussion of the architectural decisions surrounding the adoption of these concepts and their trade-offs, please refer to our architectural decision records (ADRs). Note that some relevant ADRs are linked where they directly address the concepts discussed here. However, some ADRs are not linked below, but all can be found [here](doc/architecture/decisions/).

### Assignment 1

### Lecture 1

- **Event-driven Architecture** [ADR: Event-driven Microservices Architecture](doc/architecture/decisions/0002-event-driven-microservices-architecture.md).
  - **Event Notification**: Our system employs the Event Notification pattern, most notably in the Fraud Detection Workflow. Here, the three services consume and react to events in a choreographed manner. The Fraud Detection Service consumes events about accepted transactions produced by the Transaction Service. It then reacts by checking if the accepted transaction has a high likelihood of being fraudulent. If so, then it produces a Potential Fraud event, which is consumed by the Fraud Investigation Service. The Fraud Investigation Service reacts by having a customer service representative investigate the nature of the transaction. If the transaction is indeed fraudulent, then the Fraud Investigation Service produces a Fraud Dispute and a corresponding event. This event is picked up by the Transaction Dispute Service. This service marks the transaction as fraudulent and produces a Confirmed Fraud event which is finally consumed by the Fraud Detection Service. The Fraud Detection Service reacts by incorporating the newly confirmed fraud into its machine learning process.
  - **Event-carried State Transfer** [ADR: Event-carried State Transfer for Card Info](doc/architecture/decisions/0006-event-carried-state-transfer-for-card-info.md): Our system implements this pattern in the way that the Card Service communicates all card changes to the Transaction Service via events. The Transaction Service then stores these details in a persisted database (in practice, although our version just stores them in memory)
- **Kafka**: All of our internal events are published via Kafka. The Transaction workflow uses five topics. The Transaction Service uses one topic to send Check PIN commands to the PIN Service and another topic to listen to PIN Check responses. A similar topic structure is used to communicate with the Transaction Blocking Rules service. A single topic is then used by the Card Service to produce events when a card limit is updated. There are three topics which are all one way. They are used to notify other services about "possible fraud detected", "create dispute", "new confirmed fraud". Furthermore, there is a topic to notify the model about new transactions. This is where we transfer from the Camunda orchestrated workflow to the choreographed.

### Lecture 2

- **Choreography** [ADR: Use Choreography for Fraud Workflow](doc/architecture/decisions/0008-use-choreography-for-fraud-workflow.md): The Fraud Detection Workflow is coordinated via Choreography. This is already described in the section about the Event Notification pattern above.
  Stateless Choreography: The Fraud Detection Workflow follows the Stateless Choreography pattern as the workflow state is not explicitly stored in any one service and is not sent via the events. Rather it is implied by the event itself. For example, we know a transaction is confirmed as a fraud once the Fraud Investigation Service emits the Confirmed Fraud event containing the transaction ID. We do not need to stamp the event with any further workflow information or keep track of the status of the workflow in any of the other services.
- **Cloud Streams**: We used loud Streams in our system to develop our Kafka producers and consumers.

### Lecture 3:

- **Orchestration** [ADR: Use Orchestration for Transaction Workflow](doc/architecture/decisions/0007-use-orchestration-for-transaction-workflow.md): The Transaction Workflow is orchestrated locally within the Transaction Service. This service encapsulates the domain knowledge required to know when a transaction should be accepted and when it should be rejected, and no other domain knowledge. For example, the Transaction Service knows that some transactions need to be accompanied by a valid PIN entry while others aren’t. It does not, however need to know how to check if the PIN is valid. This is the responsibility of the PIN service. Whenever a transaction comes in that does require the PIN to be checked, the Transaction Service will command the PIN Service to validate the PIN entry sent with the transaction.

- **Process Engines - Camunda**: We modelled the orchestration of the Transaction Workflow using Camunda. The following features of Camunda are utilised within our system:

  - **Durable State**: The engine keeps track of the running process instances. This is - among others - necessary to be able to correlate incoming Kafka messages with the correct instance.
  - **Scheduling**: The process features a timeout functionality. If the authentication process takes too long, it is cancelled. This offers the chance to react accordingly - in our case, with a stateful retry.
  - **Support for Human / Machine Collaboration**: The process includes a „user task" for re-entering the pin. Camunda provides a handy option here to provide a UI for interaction with the user.
  - **Tooling**: The Camunda modeller was used to create the BPMN diagram. Other than that, the Camunda libraries were used to connect to the services via the service tasks.

### Lecture 4:

- **Events and Commands**: The Transaction Workflow is characterised by the Transaction Service issuing commands, such as the Check PIN command to the PIN Service, and receiving the response via events, such as the Correct PIN event. The Fraud Detection workflow only employs events, however. This matches the two workflows being orchestrated and choreographed, respectively.

### Lecture 5:

- **Stateful resilience patterns**:
  - **Stateful retry**: When the Transaction workflow times out before all external services have responded, then the whole process gets aborted and retried. This works since all of the operations taken during the execution of the process are _idempotent_. Please note that this does not fit the domain problem fully. However, we did add it in order to experiment with the course concepts and the features offered by Camunda.
  - **Human intervention**: When the PIN service replies that the PIN entered was incorrect, the Transaction service prompts a human to try re-entering a correct PIN. Please note that this does not fit the domain problem fully. However, we did add it in order to experiment with the course concepts and the features offered by Camunda.
- **Sagas**:
  - **Parallel Saga**: The Transaction workflow would fall under the Parallel Saga pattern due to its characteristics (asynchronous communication, eventual consistency, orchestrated coordination). However, as there is not really a distributed transaction that requires actions to be reversed, this does not have a large impact on the implementation.
  - **Anthology Saga**: The Fraud Detection workflow would fall under the Anthology Saga pattern due to its characteristics (asynchronous communication, eventual consistency, choreographed coordination). However, the same applies here. As there is not really a distributed transaction that requires actions to be reversed, this does not have a large impact on the implementation.

### Assignment 2

### Lecture 8

- **Event Processing Design Patterns**
  - **Single-Event Processing**: The Transaction Postprocessing service employs this pattern when it filters the content of the transactions before passing them on to the Fraud Detection workflow. 
  - **Processing with Local State**: The Fraud Preprocessing service follows this pattern to enable windowed aggregations of the transaction stream
  - **Stream-Table Join**: TODO Jonas
- **Schema Registry-aware Avro Serdes**: Our system uses Schema Registry-aware Avro Serdes in order to share the Transaction class which is used by multiple services within out system. The usage of Avro and its trade-offs are discussed further within the [topology description](doc/topologies.md).

### Lecture 9

- **KStreams, KTables, Global KTables**: See [topology description](doc/topologies.md).
- **Interactive queries**: See [topology description](doc/topologies.md).

### Lecture 9

- **Time semantics**: Our transaction event has a timestamp embedded within it. This serves as the event time. The event time is in fact extracted using a custom timestamp extractor in the fraud preprocessing topology. For more information see [topology description](doc/topologies.md).
- **Windowed aggregation**: See [topology description](doc/topologies.md).

## Topologies

See [topology description](doc/topologies.md)

## Architectural Decisions and Trade-Offs

You can find our ADRs [here](doc/architecture/decisions/).

TODO Jonas ADR on caching 

TODO Kris ADR on new service granularities 

## Diagrams

- [Architecture Characteristics Worksheet](doc/diagrams/Architecture%20Characteristics%20Worksheet.pdf)
- [Bounded Contexts Diagram](doc/diagrams/Bounded%20Contexts%20-%20EDPO.pdf)
- [System Overview](doc/diagrams/System%20Overview.png)
- [Transaction Workflow BPMN](doc/diagrams/Transaction%20Workflow%20BPMN.png)

TODO Kris Add all topologies etc to list - at very end

TODO Jonas update BPMN

## Results

### Assignment 1 - Experiment with Kafka:

For our system, we decided to investigate two aspects of Kafka and how they can lead to data loss. These two aspects are consumer lag and offset misconfigurations.

- **The Risk of Data Loss Due to Consumer Lag**:
  Consumer lag occurs when producers write messages to a topic at a faster rate than consumers consume them. This is highly relevant to our system due to the highly elastic nature of credit card transactions - where there can be large spikes in the number of incoming transactions. When consumer lag occurs, you will notice that the difference between the offset of the latest message and the consumer offset grows larger; in other words, the backlog of unprocessed messages starts growing. If this backlog becomes large enough, it can happen that messages get lost due to them becoming older than the retention policy allows for or the storage size limit being reached before they get processed. This would then potentially lead to those messages getting lost. To experiment with this concept, you can create a simple producer that produces messages very fast and a simple consumer that takes long to process each message before consuming the next one. If you then set the retention policy to some small amount of time, you will see that messages will start to get deleted before they are consumed.

- **The Risk of Data Loss Due to Offset Misconfigurations**: In testing the aspect described above, we also discovered another interesting aspect of Kafka that can lead to data loss. When a consumer starts to read a partition that does not have a committed offset, or if the committed offset it has is invalid (e.g. because the record it points to has already been deleted), then Kafka assigns it one of three values based on the auto.offset.reset property. If this property is set to "latest" (which is the default), then the consumer will start reading from the latest message. If the value is "ealiest", then the consumer will start reading from the earliest valid offset for the partition (i.e. it will read all the messages in the partition). If the value is none, then an exception will be thrown when attempting to consume from a valid offset. The interesting thing is that the default value, "latest", can cause data loss. Consider the example where we are using this default value, and we are experiencing serious consumer lag. Consumer A is reading from partitions 1 and 2. Consumer A is so far behind that their last committed offset for partition 2 refers to a message that has already been deleted. Now, if a new consumer joins and during rebalancing gets assigned to partition 2, then the new consumer will start reading from the latest message. This means that all of the messages between the earliest and the latest message will not be read by the new consumer, and if this is not noticed by an administrator in time, these messages will eventually be deleted and lost. To experiment with this concept, set up the same scenario as described for the previous concept. Once the last committed offset is older than the oldest message retained in a partition, add a new consumer to the consumer group. If you are (un)lucky enough, then the new consumer will be assigned to this partition and will start reading from the latest message. You will notice that the earlier messages will be effectively ignored and eventually deleted.

## Reflections


 ### Assignment 1

This section will outline the learnings we have gained from designing and implementing our system.

- What we found out from working with Kafka is that it is very easy to set up, but you have to be careful to set it up right so that you don't experience unexpected consequences such as data loss. For example, you should have enough partitions in order to be able to scale your number of consumers to keep up with producers that produce faster than a single consumer/thread can keep up with. Moreover, you need to carefully read through the default configuration values before moving into production. Failing to do so can lead to the situation described in our Results section where old messages were ignored by a new consumer - a behaviour you wouldn't expect as the default one.
- Camunda is very handy. However, it does have a steep learning curve.
- Asynchronous workflows in Camunda need special care (concurrent process modification, timing issues, correlation of messages, etc.)
- Camunda does not use a new thread for parallel running activities (would have saved me probably a day of work :D)
- Special issues with async execution and Camunda: if the process instance is prematurely terminated (error etc.) and a message is correlated with this instance, it just crashes
- Mixing Spring-Kafka and Cloud Streams created some problems we did not think about in the beginning. With the fast configuration of Cloud Streams, it is easy to set up communication between services, but when accepting messages from Spring-Kafka, there is extra configuration needed to get the serialization process working properly.
- Pretty nice about Kafka (not necessarily useful or Kafka specific but came to my mind quite often): No need to worry about ports and to change the environment in different places.
- The transaction service implement some fairly complex logic with respect to the pin validation procedure. This is mainly because this part grew over time, starting from simply checking if the amount is high enough that a pin is required or not. An other option would have been to put this logic in the pin service, to ensure a clear separation of concerns and respect service boundaries. It would be worth considering this option for future refactoring.
- Differentiating between commands and events was more difficult than expected

### Assignment 2

- Avro:
  - Avro gives us a good way to share ObjectClasses between services with one single place of truth.
  - The first option is to share this TransferObjectClass is within the events which adds unnessesary size to the event, which is not acceptable for us as we will have a high volume of events.
  - The other option is to store the TransferOnjects in a Repository. This also means we need a repository to store this TransferObject and act as the single place of truth. Which means we add a service which other services depend on and can fail if the repository service is not reachable. This also adds complexity in deployments, as the Avro schemas need to be in sync on the repository and within the code, as we still wan't to use git for version managment.
  - We think the overhead and complexity which Avro adds is just not worth it. Overall, we could achive almost thesame with a simple shared library which contains the DTO.
- Kafka Streams:
  - Kafka streams enable fast possibilties to process events. But this abstraction layer also comes at a cost, as we lose some control over the underlying functionallity.
  - In our experience it made it hard to debug. It took a while until we descovered how to enable logging, and in the standard logging configuration the logs would spam every single small detail. Which was not helpful at all.
  - Versions of kafka stream older than 3.2.0 don't include arm binaries and therefore, don't support MacBooks with the M1 chip (Except when runnning the jar in Roseta [Hello IntelliJ Users :D]). M1 Macbooks are around for more than 1.5 years, and in my experience all other librarys I normaly work with updated their binaries within weeks or a few months. Kafka Streams 3.2.0 was recently release in May 2022, which makes us wonder about their generall update frequency and if this is acceptable for a library which is used at the core of the application. Switching to a different library or dropping it completly would mean rewriting almost every aspect of the event-processing services.
  - For people who are not used to a more functional programming approche it is a bit of a mindset change first, but this was not a to big of a challenge for as, as we are all experienced developer.
reflections and lessons learned:
  - Stream Processing is a great way to alter events in real time and gain insights in real time. It seems to be a great way to work with events. But I'm still not convinced that we need a library like kafka-streams to do this.
- General reflections or insights:
  - Stream Processing is a great way to alter events in real time and gain insights in real time. It seems to be a great way to work with events. But I'm still not convinced that we need a library like kafka-streams to do this. Using Kafka-streams adds a framework which is kinda irreplaceable without rewriting most part of the service. But overall, what we doing in stream processing is mostly just simple Object manipulations and storing some data in a Map like structure when using tables. I know, something super simple like Object manipulation can add a lot of boilerplate in strict languages like Java. Hence, the question arrises if Java is the right language for that task. Other programming languages like JavaScript excel in Object manipulation and have a lot of build in support for that. Therefore, I'm sure in a language like this we could archive the same result with probably even less lines than with kafka-streams. Without adding a huge library and abstraction on top.

TODO Jonas add your reflections

## Editorial Notes

In this section, we explain some things and decisions that might not be clear from the other sections or documents.

- The Card service only contains the card limit in our implementation. In practice, this service should also contain more information on the card and more business logic about how that information can change. For example, it should contain the status of the card (open, closed, etc.) and business logic on how these can change.

TODO Jonas

## Responsibilities

**Note: we believe that the work was evenly distributed and that each of the group members had about the same workload.**

### Assignment 1

- Architecture & Theory

  - Marcel:
    - Discussed the system structure and its fit with the course concepts
    - Reviewed the documentation and ADRs
  - Jonas:
    - Collected topics and concepts relevant to our project which need to be considered and might need an ADR
    - Discussed the system structure and its fit with the course
    - Reviewed the documentation and ADRs
  - Kristófer:
    - Collected topics and concepts relevant to our project which need to be considered and might need an ADR
    - Discussed the system structure and its fit with the course
    - Wrote all the documentation, including the ADRs
    - Conducted and analysed the Kafka experiments described in the Results section

- Implementation:
  - Marcel:
    - Setup Cloud Streams config to work properly
    - Wrote the code for the Fraud Detection Workflow
    - Setup project structure, services, GitHub repo
  - Jonas:
    - Wrote the code for the final Transaction Workflow, including all Spring services, as well as the design of the workflow in BPMN and its implementation in Camunda
  - Kristófer:
    - Wrote the initial version of the Transaction service before it was converted to the Camunda implementation

### Assignment 2

- Architecture & Theory

  - Marcel:
    - Discussed the system structure and its fit with the course concepts
    - Wrote the documentation for the Transaction Postprocessing topology and his reflections
    - Reviewed all the documentation and the ADRs
  - Jonas:
    - Collected topics and concepts relevant to our project which need to be considered and might need an ADR
    - Discussed the system structure and its fit with the course
    - Wrote the documentation for the Transaction Preprocessing topology and his reflections
    - Reviewed the documentation and the ADRs
    - Finalsed documentation
  - Kristófer:
    - Collected topics and concepts relevant to our project which need to be considered and might need an ADR
    - Discussed the system structure and its fit with the course
    - Wrote the documentation for the Fraud Preprocessing topology and his reflections
    - Finalsed documentation

- Implementation:
  - Marcel:
    - Set up the Avro registry and everything surrounding the usage of Avro
    - Integrated the microservices and tested the system flow
    - Wrote the code for the Transaction Postprocessing and the Transaction Faker services
  - Jonas:
    - Wrote the code for the Transaction Preprocessing service  
    - Integrated the Transaction Preprocessing service with the Transaction service (Camunda workflow)
  - Kristófer:
    - Wrote the code for the Fraud Preprocessing service 
