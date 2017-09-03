//package spgui
//
//import japgolly.scalajs.react._
//import japgolly.scalajs.react.vdom.html_<^._
//
//import scala.util.Try
//import spgui.circuit._
//import java.util.UUID
//
//
//case class SPWidgetBase(id: UUID, s: String) {
//
//  def updateWidgetData(data: String): Unit = {
//    SPGUICircuit.dispatch(UpdateWidgetData(id, data))
//  }
//
//  def getWidgetData = {
//    SPGUICircuit.zoom(_.widgetData.xs.get(id)).value.getOrElse("")
//  }
//
//  def openNewWidget(widgetType: String, initialData: String = "") = {
//    val w = AddWidget(widgetType = widgetType)
//    val d = UpdateWidgetData(w.id, initialData)
//
//    SPGUICircuit.dispatch(d)
//    SPGUICircuit.dispatch(w)
//  }
//
//  def closeSelf() = SPGUICircuit.dispatch(CloseWidget(id))
//
//}
//
//object SPWidget {
//  case class Props(spwb: SPWidgetBase, renderWidget: SPWidgetBase => VdomElement)
//  private val component = ScalaComponent.builder[Props]("SpWidgetComp")
//    .render_P(p => p.renderWidget(p.spwb))
//    .build
//
//  def apply(renderWidget: SPWidgetBase => VdomElement): SPWidgetBase => VdomElement =
//    spwb => component(Props(spwb, renderWidget))
//}
//
//
//object SPWidgetBaseTest {
//  def apply() = SPWidget{spwb =>
//    def saveOnChange(e: ReactEventFromInput): Callback =
//      Callback.log("Save On Change in SPWidgetBaseTest")
//
//    def copyMe(): Callback = {
//      Callback.log("Copy me in SPWidgetBaseTest")
//    }
//
//
//    <.div(
//      <.h3("This is a sample with id " + spwb.id),
//      <.label("My Data"),
//      <.input(
//        ^.tpe := "text",
//        ^.defaultValue := "default value aleast input",
//        ^.onChange ==> saveOnChange
//      ),
//      <.button("Copy me", ^.onClick --> copyMe()),
//      <.button("Kill me", ^.onClick --> Callback(spwb.closeSelf()))
//    )
//  }
//}

package spgui

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import spgui.circuit._
import java.util.UUID


case class SPWidgetBase(id: UUID, frontEndState: GlobalState) {

  def updateWidgetData(string: String): Unit = {
    SPGUICircuit.dispatch(UpdateWidgetData(id, string))
  }

  def getWidgetData: String = {
    SPGUICircuit.zoom(_.widgetData.xs.get(id)).value.getOrElse("")
  }

  def updateGlobalState(state: GlobalState): Unit = {
    SPGUICircuit.dispatch(UpdateGlobalState(state))
  }

  def openNewWidget(widgetType: String, initialString: String = "") = {
    val w = AddWidget(widgetType = widgetType)
    val d = UpdateWidgetData(w.id, initialString)

    SPGUICircuit.dispatch(d)
    SPGUICircuit.dispatch(w)
  }

  def closeSelf() = SPGUICircuit.dispatch(CloseWidget(id))

}

object SPWidget {
  case class Props(spwb: SPWidgetBase, renderWidget: SPWidgetBase => VdomElement)
  private val component = ScalaComponent.builder[Props]("SpWidgetComp")
    .render_P(p => p.renderWidget(p.spwb))
    .build

  def apply(renderWidget: SPWidgetBase => VdomElement): SPWidgetBase => VdomElement =
    spwb => component(Props(spwb, renderWidget))
}


object SPWidgetBaseTest {
  def apply() = SPWidget{spwb =>
    def saveOnChange(e: ReactEventFromInput): Callback =
      Callback(spwb.updateWidgetData(e.target.value))

    def copyMe(): Callback = {
      val d = spwb.getWidgetData
      Callback(spwb.openNewWidget(
        "SPWBTest", d)
      )
    }


    <.div(
      <.h3("This is a sample with id " + spwb.id),
      <.label("My Data"),
      <.input(
        ^.tpe := "text",
        ^.defaultValue := spwb.getWidgetData,
        ^.onChange ==> saveOnChange
      ),
      <.button("Copy me", ^.onClick --> copyMe()),
      <.button("Kill me", ^.onClick --> Callback(spwb.closeSelf()))
    )
  }
}