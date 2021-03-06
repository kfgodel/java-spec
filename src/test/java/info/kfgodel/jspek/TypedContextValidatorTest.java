package info.kfgodel.jspek;

import info.kfgodel.jspek.api.contexts.TestContext;
import info.kfgodel.jspek.api.exceptions.SpecException;
import info.kfgodel.jspek.impl.context.typed.TypedContextValidator;
import org.junit.Test;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

/**
 * This type verifies that typed context validator works as expected
 * Created by kfgodel on 22/07/14.
 */
public class TypedContextValidatorTest {


  @Test
  public void itShouldAllowTestContextAsInterface() {
    TypedContextValidator.create(TestContext.class).validate();
  }

  public interface NoMethodDefinitionInterface extends TestContext {

  }

  @Test
  public void itShouldAllowNoMethodDefinition() {
    TypedContextValidator.create(NoMethodDefinitionInterface.class).validate();
  }

  public interface MissingGetDeclarationInterface extends TestContext {
    void letFoo(Supplier<Integer> definition);
  }


  @Test
  public void itShouldDetectMissingGetDeclaration() {
    try {
      TypedContextValidator.create(MissingGetDeclarationInterface.class).validate();
      failBecauseExceptionWasNotThrown(SpecException.class);
    } catch (SpecException e) {
      assertThat(e).hasMessage("Variable [foo] doesn't have a matching getter method in [MissingGetDeclarationInterface]");
    }
  }

  public interface MissingLetDeclarationInterface extends TestContext {
    Integer getFoo();
  }

  @Test
  public void itShouldDetectMissingLetDeclaration() {
    try {
      TypedContextValidator.create(MissingLetDeclarationInterface.class).validate();
      failBecauseExceptionWasNotThrown(SpecException.class);
    } catch (SpecException e) {
      assertThat(e).hasMessage("Variable [foo] doesn't have a matching setter method in [MissingLetDeclarationInterface]");
    }
  }

  public interface PrefixedLetAndUnPrefixedGetInterface extends TestContext {
    void letFoo(Supplier<Integer> definition);

    Integer foo();
  }

  @Test
  public void itShouldAllowPrefixedLetAndUnPrefixedGet() {
    TypedContextValidator.create(PrefixedLetAndUnPrefixedGetInterface.class).validate();
  }

  public interface UnPrefixedLetAndPrefixedGetInterface extends TestContext {
    void foo(Supplier<Integer> definition);

    Integer getFoo();
  }

  @Test
  public void itShouldAllowUnPrefixedLetAndPrefixedGet() {
    TypedContextValidator.create(PrefixedLetAndUnPrefixedGetInterface.class).validate();
  }

  public interface MoreThanOneArgumentInterface extends TestContext {
    void foo(Supplier<Integer> definition, String unnecessary);

    Integer foo();
  }

  @Test
  public void itShouldRejectMoreThanOneArgument() {
    try {
      TypedContextValidator.create(MoreThanOneArgumentInterface.class).validate();
      failBecauseExceptionWasNotThrown(SpecException.class);
    } catch (SpecException e) {
      assertThat(e).hasMessage("Method [foo] declared in [MoreThanOneArgumentInterface] does not conform to get or let operation signatures [no arg | void, Supplier]");
    }
  }

  public interface ArgumentDifferentThanSupplierInterface extends TestContext {
    void foo(Function<Integer, String> definition);

    Integer foo();
  }

  @Test
  public void itShouldRejectArgumentDifferentThanSupplier() {
    try {
      TypedContextValidator.create(ArgumentDifferentThanSupplierInterface.class).validate();
      failBecauseExceptionWasNotThrown(SpecException.class);
    } catch (SpecException e) {
      assertThat(e).hasMessage("Method [foo] declared in [ArgumentDifferentThanSupplierInterface] does not conform to get or let operation signatures [no arg | void, Supplier]");
    }
  }

  public interface MissingArgumentForLetInterface extends TestContext {
    void letFoo();

    Integer foo();
  }

  @Test
  public void itShouldDetectMissingArgumentForLet() {
    try {
      TypedContextValidator.create(MissingArgumentForLetInterface.class).validate();
      failBecauseExceptionWasNotThrown(SpecException.class);
    } catch (SpecException e) {
      assertThat(e).hasMessage("Method [letFoo] declared in [MissingArgumentForLetInterface] does not conform to get or let operation signatures [no arg | void, Supplier]");
    }
  }

  public interface ExtraArgumentForGetInterface extends TestContext {
    void letFoo(Supplier<Integer> definition);

    Integer foo(String extra);
  }

  @Test
  public void itShouldDetectExtraArgumentForGet() {
    try {
      TypedContextValidator.create(ExtraArgumentForGetInterface.class).validate();
      failBecauseExceptionWasNotThrown(SpecException.class);
    } catch (SpecException e) {
      assertThat(e).hasMessage("Method [foo] declared in [ExtraArgumentForGetInterface] does not conform to get or let operation signatures [no arg | void, Supplier]");
    }
  }

  public interface DuplicateLetInterface extends TestContext {
    void letFoo(Supplier<Integer> definition);

    void foo(Supplier<Integer> definition);

    Integer foo();
  }

  @Test
  public void itShouldDetectDuplicateLet() {
    try {
      TypedContextValidator.create(DuplicateLetInterface.class).validate();
      failBecauseExceptionWasNotThrown(SpecException.class);
    } catch (SpecException e) {
      assertThat(e).hasMessage("Let operation for variable [foo] is duplicated");
    }
  }

  public interface DuplicateGetInterface extends TestContext {
    void letFoo(Supplier<Integer> definition);

    Integer foo();

    Integer getFoo();
  }

  @Test
  public void itShouldDetectDuplicateGet() {
    try {
      TypedContextValidator.create(DuplicateGetInterface.class).validate();
      failBecauseExceptionWasNotThrown(SpecException.class);
    } catch (SpecException e) {
      assertThat(e).hasMessage("Get operation for variable [foo] is duplicated");
    }
  }


  public interface MismatchBetweenGetAndLetTypeDefinitionInterface extends TestContext {
    void foo(Supplier<Integer> definition);

    String foo();
  }

  @Test
  public void itShouldDetectMismatchBetweenGetAndLetTypeDefinition() {
    try {
      TypedContextValidator.create(MismatchBetweenGetAndLetTypeDefinitionInterface.class).validate();
      failBecauseExceptionWasNotThrown(SpecException.class);
    } catch (SpecException e) {
      assertThat(e).hasMessage("Variable [foo] has mismatching type definitions in get [java.lang.String] and let [java.lang.Integer] operations");
    }
  }


  public interface DifferentGetAndLetTypesIfAssignableInterface extends TestContext {
    void foo(Supplier<Integer> definition);

    Number foo();
  }

  @Test
  public void itShouldAllowDifferentGetAndLetTypesIfAssignable() {
    TypedContextValidator.create(DifferentGetAndLetTypesIfAssignableInterface.class).validate();
  }


  public interface TypeBetweenGetAndLetIfWildcardsInterface extends TestContext {
    void foo(Supplier<? super Integer> definition);

    <T extends Number> T foo();
  }

  @Test
  public void itShouldIgnoreTypeBetweenGetAndLetIfWildcards() {
    TypedContextValidator.create(DifferentGetAndLetTypesIfAssignableInterface.class).validate();
  }

  public interface PartialDefinitionInParentTypeInterface extends MissingGetDeclarationInterface {
    Integer foo();
  }


  @Test
  public void itShouldAllowPartialDefinitionInParentType() {
    TypedContextValidator.create(PartialDefinitionInParentTypeInterface.class).validate();
  }


}
