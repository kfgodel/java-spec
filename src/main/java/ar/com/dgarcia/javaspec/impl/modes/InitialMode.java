package ar.com.dgarcia.javaspec.impl.modes;

import ar.com.dgarcia.javaspec.api.contexts.TestContext;
import ar.com.dgarcia.javaspec.api.exceptions.FailingRunnable;
import ar.com.dgarcia.javaspec.api.exceptions.SpecException;
import ar.com.dgarcia.javaspec.api.variable.Let;
import ar.com.dgarcia.javaspec.impl.context.typed.TypedContextFactory;
import ar.com.dgarcia.javaspec.impl.model.SpecTree;
import ar.com.dgarcia.javaspec.impl.model.impl.SpecTreeDefinition;
import ar.com.dgarcia.javaspec.impl.parser.SpecStack;

import java.util.function.Consumer;

/**
 * Date: 30/03/19 - 16:36
 */
public class InitialMode<T extends TestContext> implements ApiMode<T> {

  private SpecTreeDefinition tree;
  private T typedContext;

  public static <T extends TestContext> ApiMode<T> create(Class<T> expectedContextType) {
    InitialMode initialMode = new InitialMode();
    initialMode.tree = SpecTreeDefinition.create();
    initialMode.typedContext = TypedContextFactory.createInstanceOf(expectedContextType, initialMode.tree.getSharedContext());
    return initialMode;
  }

  @Override
  public SpecTree getTree() {
    return tree;
  }

  @Override
  public void beforeEach(Runnable aCodeBlock) {
    throw new SpecException("A before each block can't be defined outside the method define");
  }

  @Override
  public void afterEach(Runnable aCodeBlock) {
    throw new SpecException("A after each block can't be defined outside the method define");
  }

  @Override
  public void it(String testName, Runnable aTestCode) {
    throw new SpecException("A test can't be defined outside the method define");
  }

  @Override
  public void it(String testName) {
    throw new SpecException("A test can't be defined outside the method define");
  }

  @Override
  public void xit(String testName, Runnable aTestCode) {
    throw new SpecException("A test can't be ignored outside the method define");
  }

  @Override
  public <X extends Throwable> void itThrows(Class<X> expectedExceptionType, String testNameSuffix, FailingRunnable<X> aTestCode, Consumer<X> exceptionAssertions) throws SpecException {
    throw new SpecException("A test can't be defined outside the method define");
  }

  @Override
  public void describe(String aGroupName, Runnable aGroupDefinition) {
    throw new SpecException("A group can't be defined outside the method define");
  }

  @Override
  public void describe(Class<?> aClass, Runnable aGroupDefinition) {
    throw new SpecException("A group can't be defined outside the method define");
  }

  @Override
  public void xdescribe(String aGroupName, Runnable aGroupDefinition) {
    throw new SpecException("A group can't be ignored outside the method define");
  }

  @Override
  public void xdescribe(Class<?> aGroupName, Runnable aGroupDefinition) {
    throw new SpecException("A group can't be ignored outside the method define");
  }

  @Override
  public T context() {
    return typedContext;
  }

  @Override
  public <X> Let<X> localLet(String variableName) {
    return Let.create(variableName, this::context);
  }

  @Override
  public void given(Runnable setupCode) {
    throw new SpecException("A setup block can't be defined outside the method define");
  }

  @Override
  public void when(Runnable exerciseCode) {
    throw new SpecException("An exercise block can't be defined outside the method define");
  }

  @Override
  public void then(Runnable assertionCode) {
    throw new SpecException("An assertion block can't be defined outside the method define");
  }

  @Override
  public void itThen(String testName, Runnable assertionCode) {
    throw new SpecException("A test can't be defined outside the method define");
  }

  @Override
  public void executeAsGivenWhenThenTest() {
    throw new SpecException("A test can't be run outside the method define");
  }

  public SpecStack createStack() {
    return this.getTree().createStack();
  }

  public DefinitionMode<T> changeToDefinition() {
    return DefinitionMode.create(this);
  }

  @Override
  public ApiMode<T> changeToRunning() {
    throw new SpecException("An initial mode can change to running without a define mode");
  }
}
