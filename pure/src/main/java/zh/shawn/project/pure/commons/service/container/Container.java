package zh.shawn.project.pure.commons.service.container;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/9/28 16:15
 */
public class Container <E extends RuntimeElement, G extends RuntimeGroup>  {

    private String containerName;

    public Container() {
    }

    public String getContainerName() {
        return this.containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

}
