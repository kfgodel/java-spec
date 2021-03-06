package info.kfgodel.jspek.testSpecs;

import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.JavaSpecRunner;
import info.kfgodel.jspek.api.contexts.TestContext;
import info.kfgodel.jspek.api.exceptions.SpecException;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test verifies that null can be defined as a valid value for a context variable
 * Date: 17/12/18 - 23:09
 */
@RunWith(JavaSpecRunner.class)
public class UseNullAsValidValueTest extends JavaSpec<TestContext> {
  @Override
  public void define() {
    describe("a null valued variable", () -> {
      context().let("aVariable", () -> null);

      it("can be used on tests", () -> {
        assertThat(context().<Object>get("aVariable")).isNull();
      });
    });

    itThrows(SpecException.class, "when the variable is not defined and accessed", () -> {
      context().get("undefinedVariable");
    }, e -> {
      assertThat(e).hasMessage("Variable [undefinedVariable] must be defined before accessing it in current context[{}]");
    });
  }
}
