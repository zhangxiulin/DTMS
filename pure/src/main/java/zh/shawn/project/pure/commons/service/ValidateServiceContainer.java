package zh.shawn.project.pure.commons.service;

import zh.shawn.project.pure.commons.service.container.Container;

import java.util.*;


public class ValidateServiceContainer  extends Container {

    private static Map<String, List<ValidateConditions>> validate_pool = new HashMap(200);
    private static Map<String, String> class_pool = new HashMap(200);

    public ValidateServiceContainer() {
        super.setContainerName("validate");
    }

    public String getServiceName(String className) {
        return (String)class_pool.get(className);
    }

    public boolean containsClass(String className) {
        return class_pool.containsKey(className);
    }

    public boolean containsService(String serviceName) {
        return validate_pool.containsKey(serviceName);
    }

    public Collection<ValidateConditions> get(String serviceName) {
        return (Collection)validate_pool.get(serviceName);
    }

    public Collection<List<ValidateConditions>> listServicesCondtions() {
        return validate_pool.values();
    }

    public Collection<String> listServiceNames() {
        return validate_pool.keySet();
    }

    public void updateServiceCondition(String serviceName, ValidateConditions condition) {
        if (!validate_pool.containsKey(serviceName)) {
            validate_pool.put(serviceName, new ArrayList(20));
        }

        ((List)validate_pool.get(serviceName)).add(condition);
    }

    public void updateServiceConditions(String serviceName, List<ValidateConditions> conditions) {
        validate_pool.put(serviceName, conditions);
    }

    public int countServices() {
        return validate_pool.size();
    }

    public int countServiceConditions(String serviceName) {
        return validate_pool.containsKey(serviceName) ? ((List)validate_pool.get(serviceName)).size() : 0;
    }

    public static Container getContainer() {
        return new ValidateServiceContainer();
    }

}
