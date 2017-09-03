package spgui.widgets.charts

import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react._
import sp.domain._
import Logic._

import scalacss.ScalaCssReact._
import spgui.communication._
import spgui.widgets.{EricaLogic, ToAndFrom, API_Patient => apiPatient, API_PatientEvent => api}
import spgui.widgets.css.{WidgetStyles => Styles}

object PatientGanttWidget {

  private class Backend($: BackendScope[String, Map[String, apiPatient.Patient]]) {

    var patientObs = Option.empty[rx.Obs]

    def render(p: String, s: Map[String, apiPatient.Patient]) = {
      <.div(Styles.helveticaZ)
    }

    def onUnmount() = {
      println("Unmounting")
      patientObs.foreach(_.kill())
      Callback.empty
    }
  }

  private val ganttComponent = ScalaComponent.builder[String]("ganttComponent")
    .initialState(Map("-1" ->
      EricaLogic.dummyPatient))
    .renderBackend[Backend]
    // .componentDidMount(_.backend.getWidgetWidth())
    .componentDidMount(ctx => Callback.log("gantt Mounted!!!"))
    .componentWillUnmount(_.backend.onUnmount())
    .build


  def apply() = spgui.SPWidget(spwb => {
    ganttComponent("Hej gantt")
  })
}