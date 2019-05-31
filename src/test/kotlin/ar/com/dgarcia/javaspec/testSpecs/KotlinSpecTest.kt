package ar.com.dgarcia.javaspec.testSpecs

import ar.com.dgarcia.javaspec.api.JavaSpecRunner
import ar.com.dgarcia.kotlinspec.api.KotlinSpec
import ar.com.dgarcia.kotlinspec.api.variable.Let
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith

@RunWith(JavaSpecRunner::class)
class KotlinSpecTest : KotlinSpec() {
  override fun define() {
  val word: Let<String> by let()

    describe("when the word is Hello") {
      word.set { "Hello" }

      it("is shorter than 6 characters") {
        assertThat(word.get().length).isLessThan(6)
      }
    }

    describe("when the word is goodbye") {
      word.set { "Goodbye" }

      it("is longer than 6 characters") {
        assertThat(word.get().length).isGreaterThan(6)
      }
    }
  }
}