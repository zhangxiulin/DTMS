package zh.shawn.project.pure.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/9/28 14:24
 */
public class ClassMapUtils {

    private Logger log = LoggerFactory.getLogger(ClassMapUtils.class);

    public ClassMapUtils() {
    }

    public <T> T parseToObject(Map<String, Object> data, T t) {
        Field[] fs = t.getClass().getDeclaredFields();
        Field[] var7 = fs;
        int var6 = fs.length;

        for(int var5 = 0; var5 < var6; ++var5) {
            Field f = var7[var5];

            try {
                if (data.containsKey(f.getName())) {
                    f.setAccessible(true);
                    f.set(t, data.get(f.getName()));
                }
            } catch (Exception var9) {
                this.log.error("数据转换失败。");
            }
        }

        return t;
    }

    public <T> Map<String, Object> parseToMap(Class<T> clazz, T t) {
        Field[] fs = clazz.getDeclaredFields();
        Map<String, Object> map = new HashMap(fs.length);
        if (clazz.getName().toUpperCase().endsWith("PK")) {
            return new HashMap();
        } else {
            Field[] var8 = fs;
            int var7 = fs.length;

            for(int var6 = 0; var6 < var7; ++var6) {
                Field f = var8[var6];

                try {
                    if (!f.getName().toUpperCase().endsWith("PK")) {
                        if (!f.getName().equals("CA\tSE_INSENSITIVE_ORDER") && !f.getName().equals("serialPersistentFields") && !f.getName().equals("serialVersionUID") && !f.getName().equals("HASHING_SEED") && !f.getName().equals("value")) {
                            f.setAccessible(true);

                            try {
                                map.put(f.getName(), f.get(t));
                            } catch (Exception var16) {
                                map.put(f.getName(), "");
                            }
                        }
                    } else {
                        f.setAccessible(true);
                        Field[] fs2 = f.getType().getDeclaredFields();
                        Field[] var13 = fs2;
                        int var12 = fs2.length;

                        for(int var11 = 0; var11 < var12; ++var11) {
                            Field f2 = var13[var11];
                            if (!f2.getName().equals("CASE_INSENSITIVE_ORDER") && !f2.getName().equals("serialPersistentFields") && !f2.getName().equals("serialVersionUID") && !f2.getName().equals("HASHING_SEED") && !f2.getName().equals("value")) {
                                f2.setAccessible(true);

                                try {
                                    map.put(f.getName() + "_" + f2.getName(), f2.get(f.get(t)));
                                } catch (Exception var15) {
                                    map.put(f.getName() + "_" + f2.getName(), "");
                                }
                            }
                        }
                    }
                } catch (Exception var17) {
                    this.log.debug(f.getName() + ": 对象映射失败。", var17);
                }
            }

            return map;
        }
    }

}
