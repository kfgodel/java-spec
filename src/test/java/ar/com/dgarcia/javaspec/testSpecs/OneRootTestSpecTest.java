package ar.com.dgarcia.javaspec.testSpecs;

import ar.com.dgarcia.javaspec.api.TestContext;
import org.junit.runner.RunWith;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;

/**
 * This class serves as input spec for parser test
 * Created by kfgodel on 12/07/14.
 */
@RunWith(JavaSpecRunner.class)
public class OneRootTestSpecTest extends JavaSpec<TestContext> {


    @Override
    public void define() {
        it("only test", ()->{

        });
    }
}