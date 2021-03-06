package info.kfgodel.jspek.ignored;

import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.JavaSpecRunner;
import info.kfgodel.jspek.api.contexts.TestContext;
import info.kfgodel.jspek.api.variable.Variable;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class verifies that a failed test doesn't stop an after block from being executed.
 * <p>
 * This test cannot be run as part of the suits because it fails one of the tests
 * <p>
 * Created by kfgodel on 28/05/16.
 */
@RunWith(JavaSpecRunner.class)
public class AfterBlockExecutedEvenIfTestFailsTest extends JavaSpec<TestContext> {
  @Override
  public void define() {
    Variable<Integer> counter = Variable.of(0);
    xdescribe("when an after block is defined", () -> {
      afterEach(() -> {
        counter.storeSumWith(1);
      });

      it("even if a test fails", () -> {
        throw new RuntimeException("failing");
      });

      it("its after block gets executed", () -> {
        assertThat(counter.get()).isEqualTo(1);
      });
    });

  }
}