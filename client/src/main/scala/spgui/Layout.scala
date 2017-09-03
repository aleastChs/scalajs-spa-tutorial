package spgui

import java.util.UUID

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import spgui.circuit.{OpenWidget, SPGUICircuit}
import spgui.menu.SPMenu
import spgui.dashboard.Dashboard

object Layout {
  val widgetsConnection = SPGUICircuit.connect(x => (x.openWidgets.xs, x.globalState))
  val menuConnection = SPGUICircuit.connect(x => x.settings)

  val component = ScalaComponent.builder[Unit]("Layout")
    .render(_ =>
    <.div(
      ^.className := GlobalCSS.layout.htmlClass,
      menuConnection(SPMenu(_)),
      widgetsConnection(Dashboard(_))
    )
  )
    .build

  def apply() = component()
}
