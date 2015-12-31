package ar.com.dgarcia.javaspec.api;

import ar.com.dgarcia.javaspec.impl.model.SpecTree;
import ar.com.dgarcia.javaspec.impl.parser.DefinitionInterpreter;
import ar.com.dgarcia.javaspec.impl.parser.ExecutionInterpreter;

/**
 * This class is the extension point to add testing expressiveness with Java Specs.<br>
 * The method idiom is copied from: http://jasmine.github.io/2.0/introduction.html.<br>
 * Created by kfgodel on 12/07/14.
 */
public abstract class JavaSpec<T extends TestContext> implements JavaSpecApi<T> {

  private JavaSpecApi<T> currentInterpreter;

  /**
   * Starting method to define the specs.<br>
   * This method must be extended by subclasses and define any spec as calls to describe() and it()
   */
  public abstract void define();

  /**
   * Creates a spec definition tree with the specification defined by the user in the subclass
   */
  public SpecTree defineTree() {
    DefinitionInterpreter<T> definitionInterpreter = DefinitionInterpreter.create(this.getClass());

    // Needs to be instance variable to be accesed in the define method
    this.currentInterpreter = definitionInterpreter;
    this.define();
    // We lock every method call after definition, to prevent inadverted user errors
    this.currentInterpreter = ExecutionInterpreter.create(definitionInterpreter);

    return definitionInterpreter.getSpecTree();
  }

  @Override
  public T context() {
    return currentInterpreter.context();
  }

  @Override
  public void beforeEach(Runnable aCodeBlock) {
    currentInterpreter.beforeEach(aCodeBlock);
  }

  @Override
  public void afterEach(Runnable aCodeBlock) {
    currentInterpreter.afterEach(aCodeBlock);
  }

  @Override
  public void it(String testName, Runnable aTestCode) {
    currentInterpreter.it(testName, aTestCode);
  }

  @Override
  public void it(String testName) {
    currentInterpreter.it(testName);
  }

  @Override
  public void xit(String testName, Runnable aTestCode) {
    currentInterpreter.xit(testName, aTestCode);
  }

  @Override
  public void describe(String aGroupName, Runnable aGroupDefinition) {
    currentInterpreter.describe(aGroupName, aGroupDefinition);
  }

  @Override
  public void xdescribe(String aGroupName, Runnable aGroupDefinition) {
    currentInterpreter.xdescribe(aGroupName, aGroupDefinition);
  }
}
