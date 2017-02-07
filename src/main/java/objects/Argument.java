package objects;

import spark.utils.ClassUtils;

import java.util.ArrayList;
import java.util.List;

public class Argument {
    public String name;
    public Class type;
    public String value = null;

    public static List<String> primitiveTypeList = new ArrayList<>();
    static {
        // TODO : make sure this is everything
        primitiveTypeList.add("int");
        primitiveTypeList.add("float");
        primitiveTypeList.add("boolean");
    }

    public Argument(String name, Class type) {
        this(name, type, null);
    }

    public Argument(String name, String type) {
        this(name, getClassByName(type), null);
    }

    public static Class getClassByName(String type) {
        if (primitiveTypeList.contains(type))
            return ClassUtils.resolvePrimitiveClassName(type);
        else {
            try {
                return Class.forName(type);
            }
            catch (Exception e) {
                return null;
            }
        }

    }

    public Argument(String name, Class type, String value) {
        if (type == null)
            return;

        this.name = name;
        this.type = type;
        this.value = value;
    }

    public Argument(String name, String type, String value) {
        this(name, ClassUtils.resolvePrimitiveClassName(type), value);
    }
}
