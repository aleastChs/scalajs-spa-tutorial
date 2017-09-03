package spgui.widgets.akuten

import spgui.widgets.API_Patient

/**
  * Created by kristofer on 2017-05-02.
  */
object PatientModel {
  import rx._
  implicit val ctx: Ctx.Owner = Ctx.Owner.safe()

  val model = new PatienModel

}

class PatienModel {
  import rx._
  implicit val ctx: Ctx.Owner = Ctx.Owner.safe()

  val upd = Var(Map[String, API_Patient.Patient]())
  val pats = Var(Map[String, API_Patient.Patient]())
  val prev = Var(Map[String, API_Patient.Patient]())

  val checkPrev = Rx {
    val u = upd()
    val p = prev()
    if (u != p) {  // verkar inte fungera då det alltid är skillnad...
      pats() = u
      prev() = u
    }
  }
}