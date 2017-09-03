package spgui.circuit

import diode._
import java.util.UUID
import spgui.theming.Theming.Theme

// state
case class SPGUIModel(
  openWidgets: OpenWidgets = OpenWidgets(),
  widgetData: WidgetData = WidgetData(Map()),
  settings: Settings = Settings()
)
case class OpenWidgets(xs: Map[UUID, OpenWidget] = Map())
case class OpenWidget(id: UUID, layout: WidgetLayout, widgetType: String)
case class WidgetLayout(x: Int, y: Int, w: Int, h: Int, collapsedHeight: Int = 1)
case class WidgetData(xs: Map[UUID, Unit])

case class Settings(
  theme: Theme = Theme(),
  showHeaders: Boolean = true 
)




// actions
case class AddWidget(widgetType: String, width: Int = 2, height: Int = 2, id: UUID = UUID.randomUUID()) extends Action
case class CloseWidget(id: UUID) extends Action
case class CollapseWidgetToggle(id: UUID) extends Action
case object CloseAllWidgets extends Action
case class UpdateWidgetData(id: UUID, data: String) extends Action
case class UpdateLayout(id: UUID, newLayout: WidgetLayout) extends Action
case class SetLayout(layout: Map[UUID, WidgetLayout]) extends Action
case class SetTheme(theme: Theme) extends Action
case object ToggleHeaders extends Action

// used when failing to retrieve a state from browser storage
object InitialState {
  def apply() = SPGUIModel()
}
