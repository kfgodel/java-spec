package ar.com.dgarcia.javaspec.testSpecs;

import org.junit.runner.RunWith;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;

/**
 * This class serves as input spec for parser test
 * Created by kfgodel on 12/07/14.
 */
@RunWith(JavaSpecRunner.class)
public class OneRootTestSpec extends JavaSpec {


    @Override
    public void define() {
        it("only test", ()->{

        });
    }
}