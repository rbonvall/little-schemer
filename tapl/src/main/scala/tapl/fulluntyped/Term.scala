package tapl.fulluntyped

sealed trait Term {
  def $(t: Term) = App(this, t)
}
object Term {
  implicit def symToVar(x: Symbol): Var = Var(x)
  def λ(x: Symbol, xs: Symbol*)(body: Term) =
    Abs(x, xs.foldRight (body) { (x, acc) ⇒ Abs(x, acc) })
}
case class Var(name: Symbol)       extends Term
case class Abs(v: Var, body: Term) extends Term
case class App(f: Term, arg: Term) extends Term


sealed trait NamelessTerm {
  def $(t: NamelessTerm) = NApp(this, t)
}
object NamelessTerm {
  def λĳ(body: NamelessTerm) = NAbs(body)
  implicit def intToIndex(i: Int): Index = Index(i)
}
case class Index(i: Int) extends NamelessTerm
case class NAbs(body: NamelessTerm) extends NamelessTerm
case class NApp(fn: NamelessTerm, arg: NamelessTerm) extends NamelessTerm


object DeBruĳn {

  def removeNames(Γ: List[Symbol], t: Term): NamelessTerm =
    t match {
      case Var(x)         ⇒ Index(Γ indexOf x)
      case Abs(Var(x), t) ⇒ NAbs(removeNames(x :: Γ, t))
      case App(f, t)      ⇒ NApp(removeNames(Γ, f), removeNames(Γ, t))
    }

}
