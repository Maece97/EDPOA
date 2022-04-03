package ch.unisg.lib.logger;

import java.util.UUID;

import lombok.Getter;

public class Context {

    public Context(String correlationId, String serviceName, String userId) {
        this.correlationId = correlationId;
        this.userId = userId;
        this.serviceName = serviceName;
    }

    public static Context createNewContextFactory(String serviceName) {
        return new Context(UUID.randomUUID().toString(), serviceName, null);
    }

    public static Context createContextFactory(String correlationId, String serviceName) {
        return new Context(correlationId, serviceName, null);
    }

    public static Context createContextWithUserFactory(String correlationId, String serviceName, String userId) {
        return new Context(correlationId, serviceName, userId);
    }
    
    @Getter
    private String correlationId;

    String userId;

    String serviceName;

}
