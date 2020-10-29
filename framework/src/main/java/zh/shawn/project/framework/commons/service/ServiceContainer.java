package zh.shawn.project.framework.commons.service;

import zh.shawn.project.framework.commons.service.container.Container;
import zh.shawn.project.framework.commons.service.core.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceContainer extends Container {

    private static Map<String, Service> SERVICE_POOL = new ConcurrentHashMap<>(200);

    public ServiceContainer() {
        super.setContainerName("service");
    }

    public boolean containsService(String serviceName) {
        return SERVICE_POOL.containsKey(serviceName);
    }

    public Service get(String serviceName) {
        return (Service)SERVICE_POOL.get(serviceName);
    }

    public String[] listServices(){
        Set<String> keySet = SERVICE_POOL.keySet();
        if (keySet != null && keySet.size() > 0){
            String[] keyArr = new String[keySet.size()];
            return keySet.toArray(keyArr);
        }
        return new String[0];
    }

    public void update(Service service) {
        SERVICE_POOL.put(service.getAction(), service);
    }

    public int countLayer() {
        return SERVICE_POOL.size();
    }

    public static Container getContainer() {
        return new ServiceContainer();
    }

}
