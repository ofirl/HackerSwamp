package objects;

import spark.utils.ClassUtils;

public class Argument {
    public String name;
    public Class type;

    public Argument(String name, Class type) {
        this.name = name;
        this.type = type;
    }

    public Argument(String name, String type) {
        this.name = name;
        this.type = ClassUtils.resolvePrimitiveClassName(type);
    }
}
