package zh.shawn.project.framework.commons.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtils {

    private Logger log = LoggerFactory.getLogger(ClassUtils.class);

    public ClassUtils() {
    }

    public boolean validateObjectType(Object obj, Class<?> objType) {
        return obj.getClass().getName().equals(objType.getName());
    }

    public Object parseObject(Object obj, Class<?> type) throws Exception {
        if (type.getName().equals(Integer.TYPE.getName())) {
            return Integer.valueOf(obj.toString());
        } else if (type.getName().equals(Boolean.TYPE.getName())) {
            return Boolean.valueOf(obj.toString());
        } else if (type.getName().equals(Double.TYPE.getName())) {
            return Double.valueOf(obj.toString());
        } else if (type.getName().equals(Short.TYPE.getName())) {
            return Short.valueOf(obj.toString());
        } else if (type.getName().equals(Long.TYPE.getName())) {
            return Long.valueOf(obj.toString());
        } else {
            return type.getName().equals(Float.TYPE.getName()) ? Float.valueOf(obj.toString()) : obj.toString();
        }
    }

    public List<Class<?>> findJarClasses(String jarPath) throws Exception {
        this.log.debug("开始扫描jar包中的类：" + jarPath);
        File jarFile = new File(jarPath);
        if (jarFile.exists() && jarFile.isFile()) {
            if (!jarFile.canRead()) {
                this.log.error("此jar包不可访问。" + jarPath);
                throw new Exception("此jar包不可访问。" + jarPath);
            } else {
                Enumeration<JarEntry> jes = (new JarFile(jarFile)).entries();
                ClassLoader loader = new URLClassLoader(new URL[]{new URL("file:" + jarPath)});
                ArrayList clazzs = new ArrayList();

                while(jes.hasMoreElements()) {
                    JarEntry je = (JarEntry)jes.nextElement();
                    if (je.getName().endsWith(".class")) {
                        loader.loadClass(je.getName().replace("/", ".").substring(0, je.getName().length() - 6));
                        Class<?> c = Class.forName(je.getName().replace("/", ".").substring(0, je.getName().length() - 6));
                        clazzs.add(c);
                    }
                }

                loader = null;
                jes = null;
                this.log.debug("扫描到此jar包下拥有class文件数为：" + clazzs.size());
                return clazzs;
            }
        } else {
            this.log.error("此路径下的jar包不存在。" + jarPath);
            throw new Exception("此路径下的jar包不存在。" + jarPath);
        }
    }

    /**
     *@Description: 加载jar包中的类，在不单单依赖JDK的时候，需要把依赖的其他jar包添加
     * 到添加到 URLClassLoader 中去构建运行环境，这样就不会出现异常了
     *@params: [dependentJarPaths, jarPath]
     *@return: java.util.List<java.lang.Class<?>>
     */
    public List<Class<?>> findJarClasses(String[] dependentJarPaths, String jarPath) throws Exception {
        this.log.debug("开始扫描jar包中的类：" + jarPath);
        this.log.debug("依赖jar包：" + jarPath);
        int dependentLen = dependentJarPaths.length;
        URL[] urlArr = new URL[dependentLen + 1];
        if (dependentLen > 0){
            for (int i = 0, len = dependentLen; i < len; i++){
                String dependentJarPath = dependentJarPaths[i];
                URL url = new URL("file:" + dependentJarPath);
                urlArr[i] = url;
            }
        }


        File jarFile = new File(jarPath);
        if (jarFile.exists() && jarFile.isFile()) {
            if (!jarFile.canRead()) {
                this.log.error("此jar包不可访问。" + jarPath);
                throw new Exception("此jar包不可访问。" + jarPath);
            } else {
                urlArr[dependentLen] = new URL("file:" + jarPath);
                ClassLoader loader = new URLClassLoader(urlArr);

                Enumeration<JarEntry> jes = (new JarFile(jarFile)).entries();
                ArrayList clazzs = new ArrayList();

                while(jes.hasMoreElements()) {
                    JarEntry je = (JarEntry)jes.nextElement();
                    if (je.getName().endsWith(".class")) {
                        loader.loadClass(je.getName().replace("/", ".").substring(0, je.getName().length() - 6));
                        Class<?> c = Class.forName(je.getName().replace("/", ".").substring(0, je.getName().length() - 6));
                        clazzs.add(c);
                    }
                }

                loader = null;
                jes = null;
                this.log.debug("扫描到此jar包下拥有class文件数为：" + clazzs.size());
                return clazzs;
            }
        } else {
            this.log.error("此路径下的jar包不存在。" + jarPath);
            throw new Exception("此路径下的jar包不存在。" + jarPath);
        }
    }

    public List<Class<?>> findClasses(String packageName) throws Exception {
        this.log.debug("开始扫描包中的类：" + packageName);
        ArrayList clazzs = new ArrayList();

        try {
            String resourceName = packageName.replaceAll("\\.", "/");
            this.log.debug(resourceName + "," + "file:" + ClassUtils.class.getResource("/").getPath());
            URL url = new URL("file:" + ClassUtils.class.getResource("/").getPath() + File.separator + resourceName);
            File urlFile = new File(url.toURI());
            this.log.debug(urlFile.getAbsolutePath());
            List<Class<?>> tcs = this.findAllClasses(packageName, urlFile);
            if (tcs != null && tcs.size() > 0) {
                clazzs.addAll(tcs);
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        this.log.debug("共计扫描类：" + clazzs.size());
        return clazzs;
    }

    public List<Class<?>> findClassesByAPathWithAllPath(String packageName, String path) throws Exception {
        this.log.debug("开始扫描包中的类：" + path);
        ArrayList clazzs = new ArrayList();

        try {
            File urlFile = new File(path);
            this.log.debug(urlFile.getAbsolutePath());
            List<Class<?>> tcs = this.findAllClasses(packageName, urlFile);
            if (tcs != null && tcs.size() > 0) {
                clazzs.addAll(tcs);
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        this.log.debug("共计扫描类：" + clazzs.size());
        return clazzs;
    }

    public List<Class<?>> findClassesByAPath(String packageName, String path) throws Exception {
        this.log.debug("开始扫描包中的类：" + packageName);
        ArrayList clazzs = new ArrayList();

        try {
            String resourceName = packageName.replaceAll("\\.", "/");
            this.log.debug(resourceName + "," + "file:" + ClassUtils.class.getResource("/").getPath());
            File urlFile = new File(path + File.separator + resourceName);
            this.log.debug(urlFile.getAbsolutePath());
            List<Class<?>> tcs = this.findAllClasses(packageName, urlFile);
            if (tcs != null && tcs.size() > 0) {
                clazzs.addAll(tcs);
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        this.log.debug("共计扫描类：" + clazzs.size());
        return clazzs;
    }

    public List<Class<?>> findClasses(String packageName, String source) throws Exception {
        this.log.debug("开始扫描包中的类：" + packageName);
        ArrayList clazzs = new ArrayList();

        try {
            String resourceName = packageName.replaceAll("\\.", "/");
            this.log.debug(resourceName + "," + "file:" + Thread.currentThread().getContextClassLoader().getResource(source).getPath());
            URL url = new URL("file:" + Thread.currentThread().getContextClassLoader().getResource(source).getPath() + File.separator + resourceName);
            File urlFile = new File(url.toURI());
            this.log.debug(urlFile.getAbsolutePath());
            List<Class<?>> tcs = this.findAllClasses(packageName, urlFile);
            if (tcs != null && tcs.size() > 0) {
                clazzs.addAll(tcs);
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        this.log.debug("共计扫描类：" + clazzs.size());
        return clazzs;
    }

    protected List<Class<?>> findAllClasses(String packName, File packFile) throws Exception {
        List<Class<?>> clazzs = new ArrayList();
        int i;
        if (packFile.exists() && packFile.isDirectory()) {
            File[] fs = packFile.listFiles();
            File[] var8 = fs;
            int var7 = fs.length;

            for(i = 0; i < var7; ++i) {
                File f = var8[i];
                new ArrayList();
                List<Class<?>> tcs = this.findAllClasses(packName + "." + f.getName(), f);
                if (tcs != null && tcs.size() > 0) {
                    clazzs.addAll(tcs);
                }
            }
        }

        if (packFile.exists() && packFile.isFile()) {
            String[] ss = packName.split("[.]");
            StringBuilder sb = new StringBuilder();

            for(i = 0; i < ss.length - 1; ++i) {
                sb.append(ss[i]);
                sb.append(".");
            }

            Class<?> clazz = Class.forName(sb.substring(0, sb.length() - 1));
            clazzs.add(clazz);
        }

        return clazzs;
    }

}
