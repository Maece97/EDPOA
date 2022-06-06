# 9. Caching Exchange Rates and Card Status in the Preprocessing Service


Date: 2022-06-06

## Status

Accepted

## Context

In the Preprocessing service, the amount for every transaction is converted into USD
to be able to work with a standardized currency throughout the fraud detection process.
Furthermore, the transaction is enriched with the current status of the respective card to block
any transactions from inactive cards right away.
The current exchange rates are retrieved from a third party API and the card status is updated
via our card service and also stored there.
To get a hand on the data we could either query it for every incoming transaction or
cache it in the preprocessing service.
## Decision
The data is cached in the Preprocessing Service for three main reasons.

The most important reason is that it improves performance. Since the Preprocessing service
will have the information stored locally, it does not have to query the exchange rates API and
the card service which saves the latency added by communication.

The second important factor is that neither the exchange rates nor the card status update very frequently.
The API only updates the exchange rates once per day. Therefore, it does not make sense for the Preprocessing
Service to query the information for every transaction, as it most likely is redundand.


Lastly, it increases the fault tolerance of our system. As we rely on a third party API for the
exchange rates, it might happen that their service is down. This is outside our control.
Since the exchange rates are stored locally, the system is reluctant towards such outages as we
can work with the cached data.


## Consequences
The major consequence of this decision is that it will introduce eventual consistency. There is
a chance that the cached that is outdated at a given point in time, mostly in case the other services are down.
As the Preprocessing Service is part of the transaction service, for which responsiveness and performance
are the main non-functional properties, the performance gains are valued more than up-to-date data.