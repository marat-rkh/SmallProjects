import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassCreator {
    private String buffer = "";
    private Class<?> clazz;
    private String shift = "";

    public void create(Class<?> c, String path) {
        clazz = c;
        getPackage();
        buffer += "\n";
        buffer += modifiersToString(clazz.getModifiers());
        buffer += (" " + clazz.getName());
        buffer += (" ");
        buffer += (clazz.getSuperclass() != null ? "extends " + clazz.getSuperclass().getName() + " " : "");
        getInterfaces();
        buffer += ("{\n");
        shift = "   ";
        getFields();
        buffer += "\n";
        getConstructors();
        buffer += "\n";
        getMethods();
        buffer += ("}\n");
        System.out.println(buffer);
        try(FileWriter fileWriter = new FileWriter(path + '/' + clazz.getName() + ".java")) {
            fileWriter.write(buffer);
        } catch (IOException e) {
            System.out.println("IO error occurred. Details: " + e.getMessage());
        }
    }

    private void getPackage() {
        buffer += "package ";
        Package pkg = clazz.getPackage();
        if(pkg != null) {
            buffer += (pkg.getName() + ";\n");
        }
    }

    private String modifiersToString(int modifiers) {
        return String.format("%s", Modifier.toString(modifiers));
    }

    private void getInterfaces() {
        Class<?> [] interfaces = clazz.getInterfaces();
        if(interfaces.length != 0) {
            buffer += " implements ";
            for (int i = 0; i < interfaces.length; i++) {
                buffer += interfaces[i].getName();
                buffer += (i == interfaces.length - 1) ? "" : ", ";
            }
        }
    }

    private void getFields() {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            buffer += shift;
            getField(field);
        }
    }

    public void getField(Field field) {
        buffer += (modifiersToString(field.getModifiers()) + " ");
        buffer += (getType(field.getType()) + " ");
        buffer += (field.getName() + ";\n");
    }

//    private void setFinalValue(Field field) {
//        if(Modifier.isFinal(field.getModifiers())) {
//            buffer += " = ";
//            if (field.getType().isPrimitive()) {
//                if (field.getType().getName().compareTo("boolean") == 0) {
//                    buffer += "false";
//                } else {
//                    buffer += "0";
//                }
//            } else {
//                buffer += "null";
//            }
//        }
//    }

    public void getConstructors() {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            getConstructor(constructor);
        }
    }

    private void getConstructor(Constructor<?> constructor) {
        buffer += shift;
        buffer += (modifiersToString(constructor.getModifiers()) + " ");
        buffer += (constructor.getName() + " ");
        buffer += "(";
        getArgs(constructor.getParameterTypes());
        buffer += ") ";
        getExceptions(constructor.getExceptionTypes());
        buffer += "{\n";
        buffer += shift;
        buffer += "}\n";
    }

    public void getMethods() {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            getMethod(method);
        }
    }

    private void getMethod(Method method) {
        buffer += shift;
        buffer += (modifiersToString(method.getModifiers()) + " ");
        buffer += (getType(method.getReturnType()) + " ");
        buffer += (method.getName() + " ");
        buffer += "(";
        getArgs(method.getParameterTypes());
        buffer += ") ";
        getExceptions(method.getExceptionTypes());
        buffer += "{\n";
        buffer += shift;
        setBody(method.getReturnType());
        buffer += (shift + "}\n");
    }

    private void getArgs(Class<?>[] argTypes) {
        int argNum = 0;
        for(int i = 0; i < argTypes.length; i++) {
            buffer += (modifiersToString(argTypes[i].getModifiers()) + " " +  getType(argTypes[i]) + " arg" + argNum);
            buffer += (i == argTypes.length - 1) ? "" : ", ";
        }
    }

    private void getExceptions(Class<?>[] exceptTypes) {
        if(exceptTypes.length != 0) {
            buffer += "throws ";
            for(int i = 0; i < exceptTypes.length; i++) {
                buffer += (exceptTypes[i].getName());
                buffer += (i == exceptTypes.length - 1) ? "" : ", ";
            }
        }
    }

    private void setBody(Class<?> returnType) {
        buffer += (shift + "return ");
        if(returnType.isPrimitive()) {
            if(returnType.getName().compareTo("void") == 0) {
                buffer += ";\n";
            } else if(returnType.getName().compareTo("boolean") == 0) {
                buffer += "false;\n";
            } else {
                buffer += "0;\n";
            }
        } else {
            buffer += "null;\n";
        }
    }

    private String getType(Class<?> type) {
        if(type.isArray()) {
            return (type.getComponentType().getName() + "[]");
        } else {
            return type.getName();
        }
    }
}