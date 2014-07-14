package ar.com.dgarcia.javaspec.testSpecs;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.runner.RunWith;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.dgarcia.javaspec.api.Variable;

/**
 * This class serves as input spec for parser test
 * Created by kfgodel on 12/07/14.
 */
@RunWith(JavaSpecRunner.class)
public class AfterUsedInOneTestSpec extends JavaSpec {
    @Override
    public void define() {
        Variable<Object> foo = Variable.create();

        afterEach(()-> {
            foo.storeSumWith("appended text");
        });

        it("test with after", ()-> {
            assertThat(foo.get()).isNull();
            foo.set("a text");
        });

    }
}