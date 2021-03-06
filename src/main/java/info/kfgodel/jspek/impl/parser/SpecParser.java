package info.kfgodel.jspek.impl.parser;

import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.exceptions.SpecException;
import info.kfgodel.jspek.impl.model.SpecTree;
import info.kfgodel.jspek.impl.model.impl.SpecTreeDefinition;

import java.lang.reflect.InvocationTargetException;

/**
 * This type defines the parser that understands the definition of a JavaSpec subclass, and creates a model of the specs
 * Created by kfgodel on 12/07/14.
 */
public class SpecParser {

  public static SpecParser create() {
    SpecParser parser = new SpecParser();
    return parser;
  }

  public SpecTree parse(Class<? extends JavaSpec> specClass) {
    SpecTree createdTree = SpecTreeDefinition.create();
    JavaSpec createdSpec = instantiate(specClass);
    createdSpec.populate(createdTree);
    return createdTree;
  }

  /**
   * Creates the new instance using reflection on niladic constructor
   */
  private JavaSpec instantiate(Class<? extends JavaSpec> specClass) {
    try {
      JavaSpec createdInstance = specClass.getConstructor().newInstance();
      return createdInstance;
    } catch (InvocationTargetException e) {
      throw new SpecException("Failed to execute constructor for spec[" + specClass + "]: "
        + e.getCause().getMessage(), e);
    } catch (SecurityException e) {
      throw new SpecException("Security forbids instantiation for spec[" + specClass + "]", e);
    } catch (NoSuchMethodException e) {
      throw new SpecException("Missing empty constructor for new spec[" + specClass + "]", e);
    } catch (ExceptionInInitializerError e) {
      throw new SpecException("Constructor failed for new spec[" + specClass + "] instance", e);
    } catch (InstantiationException e) {
      throw new SpecException("Error creating the spec[" + specClass + "] instance", e);
    } catch (IllegalAccessException e) {
      throw new SpecException("Unable to access spec[" + specClass + "] constructor for new instance", e);
    }
  }
}
