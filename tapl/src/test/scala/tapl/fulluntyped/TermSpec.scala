package tapl.fulluntyped
package tapl.fulluntyped

import org.scalatest.FunSpec

class TermSpec extends FunSpec {
  import DeBruĳn._
  import Term.{λ, symToVar}
  import NamelessTerm.{λĳ, intToIndex}

  describe("λ") {
    it("creates abstractions") {
      val body = Var('x)
      val nice = λ('x) { body }
      val ugly = Abs(Var('x), body)
      assert(nice === ugly)
    }
    it("creates curried abstractions when given several parameters") {
      val body = App(Var('x), Var('z))
      val nice = λ('x, 'y, 'z) { body }
      val ugly = Abs(Var('x), Abs(Var('y), Abs(Var('z), body)))
      assert(nice === ugly)
    }
  }

  describe("λĳ") {
    it("creates nameless abstractions") {
      val body = NApp(Index(1), NApp(Index(0), Index(1)))
      val nice = λĳ { body }
      val ugly = NAbs(body)
      assert(nice === ugly)
    }
  }

  describe("removeNames") {
    it("removes names") {
      val nameful  = λ('x, 'y) { 'x $ ('x $ 'y) } $ λ('x) { 'a $ 'x }
      val nameless = λĳ { λĳ { 1 $ (1 $ 0)  }} $ λĳ { 1 $ 0 }
      val result = removeNames(List('a), nameful)
      assert(result === nameless)
    }
  }
}


