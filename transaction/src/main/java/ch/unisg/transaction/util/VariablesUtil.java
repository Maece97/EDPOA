package ch.unisg.transaction.util;

import ch.unisg.transaction.dto.PinCheckDto;
import lombok.experimental.UtilityClass;
import ch.unisg.transaction.dto.CamundaMessageDto;
import ch.unisg.transaction.dto.MessageProcessDto;
import org.apache.kafka.common.protocol.types.Field;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class VariablesUtil {

    public <T> Map<String, Object> toVariableMap(T object) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Map<String, Object> variables = new HashMap<>();
        BeanInfo info = Introspector.getBeanInfo(object.getClass());
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
            Method reader = pd.getReadMethod();
            if (reader != null && !pd.getName().equals("class")) {
                Object value = reader.invoke(object);
                if(value != null) {
                    variables.put(pd.getName(), reader.invoke(object));
                }
            }
        }
        return  variables;
    }

    public <T> CamundaMessageDto buildCamundaMessageDto(String businessKey, Map<String, Object> variablesMap){
        return CamundaMessageDto.builder().correlationId(businessKey)
                .dto(PinCheckDto.builder().pin((String)variablesMap.get("pin")).cardNumber((String)variablesMap.get("cardNumber") ).build()).build();
    }


}
