For the project to work, we need to make sure Avro is setup correctly.

1. Make sure the docker-compose file is running which includes the schema registry.

2. When starting it for the first time, we need to add the schemas found here to the registry for that use the following two commands:

```
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"schema": "{\"namespace\":\"ch.unisg.model\",\"name\":\"Transaction\",\"type\":\"record\",\"fields\":[{\"name\":\"id\",\"type\":\"string\"},{\"name\":\"timestamp\",\"type\":\"string\"},{\"name\":\"status\",\"type\":\"string\"},{\"name\":\"exchangeRate\",\"type\":\"string\"},{\"name\":\"checkResult\",\"type\":\"string\"},{\"name\":\"cardNumber\",\"type\":\"string\"},{\"name\":\"amount\",\"type\":\"string\"},{\"name\":\"currency\",\"type\":\"string\"},{\"name\":\"country\",\"type\":\"string\"},{\"name\":\"merchant\",\"type\":\"string\"},{\"name\":\"merchantCategory\",\"type\":\"string\"},{\"name\":\"pin\",\"type\":\"string\"},{\"name\":\"tries\",\"type\":\"string\"}]}"}' http://localhost:8081/subjects/Transaction/versions
```

```
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"schema": "{\"namespace\":\"ch.unisg.model\",\"name\":\"FilteredTransaction\",\"type\":\"record\",\"fields\":[{\"name\":\"id\",\"type\":\"string\"},{\"name\":\"timestamp\",\"type\":\"string\"},{\"name\":\"status\",\"type\":\"string\"},{\"name\":\"exchangeRate\",\"type\":\"string\"},{\"name\":\"checkResult\",\"type\":\"string\"},{\"name\":\"cardNumber\",\"type\":\"string\"},{\"name\":\"amount\",\"type\":\"string\"},{\"name\":\"currency\",\"type\":\"string\"},{\"name\":\"country\",\"type\":\"string\"},{\"name\":\"merchant\",\"type\":\"string\"},{\"name\":\"merchantCategory\",\"type\":\"string\"},{\"name\":\"tries\",\"type\":\"string\"}]}"}' http://localhost:8081/subjects/FilteredTransaction/versions
```

3. To use the schemas in the services we can download them by using the following command:

```
./mvnw schema-registry:download
```

4. To generate the classes defined by the schema use:

```
./mvnw clean install
```

**Note**: Step 3+4 are not needed when there is no change to the avro schemas, as the downloaded avro schemas and classes are managed within the git repository.
