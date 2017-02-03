package objects;

import spark.utils.ClassUtils;

public class Argument {
    public String name;
    public Class type;
    public String value = null;

    public Argument(String name, Class type) {
        this(name, type, null);
    }

    public Argument(String name, String type) {
        // TODO : check for primitive/non-primitive type and invoke the needed search
        this(name, ClassUtils.resolvePrimitiveClassName(type), null);
    }

    public Argument(String name, Class type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public Argument(String name, String type, String value) {
        this(name, ClassUtils.resolvePrimitiveClassName(type), value);
    }
}
