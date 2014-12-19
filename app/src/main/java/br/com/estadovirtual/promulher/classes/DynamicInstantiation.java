package br.com.estadovirtual.promulher.classes;

/**
 * Created by Phil on 27/11/2014.
 */
public class DynamicInstantiation {

    public Object createInstance(String className) throws Exception {
        Class c = Class.forName(className);
        return c.newInstance();
    }
}
