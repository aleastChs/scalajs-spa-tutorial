package spgui.widgets.sopmaker

import java.util.UUID
import japgolly.scalajs.react._

//import japgolly.scalajs.react.vdom.all.{ a, h1, h2, href, div, className, onClick, br, key }
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.vdom.all.svg
//import paths.mid.Bezier
//import paths.mid.Rectangle

//import spgui.components.DragAndDrop.{ OnDragMod, OnDropMod, DataOnDrag, OnDataDrop }

import scalacss.ScalaCssReact._
import java.util.UUID

sealed trait RenderNode {
  val w: Float
  val h: Float
}

sealed trait RenderGroup extends RenderNode {
  val children: List[RenderNode]
}

case class RenderParallel(w: Float, h:Float, children: List[RenderNode])            extends RenderGroup
case class RenderAlternative(w: Float, h:Float, children: List[RenderNode])         extends RenderGroup
case class RenderArbitrary(w: Float, h:Float, children: List[RenderNode])           extends RenderGroup
case class RenderSometimeSequence(w: Float, h:Float, children: List[RenderNode])    extends RenderGroup
case class RenderOther(w: Float, h:Float, children: List[RenderNode])               extends RenderGroup
case class RenderSequence(w: Float, h:Float, children: List[RenderSequenceElement]) extends RenderGroup
case class RenderSequenceElement(w: Float, h:Float, self: RenderNode)               extends RenderNode



object SopMakerWidget {

  val parallelBarHeight = 12f
  val opHeight = 80f
  val opWidth = 80f
  val opSpacingX = 10f
  val opSpacingY = 10f

  case class Hover (
                     fromId: UUID = null,
                     toId: UUID = null,
                     currentlyDragging: Boolean = false
                   )

  case class State(hover: Hover)


  private class Backend($: BackendScope[Unit, State]) {

    /*
     val eventHandler = BackendCommunication.getMessageObserver(
     mess => {
     println("[SopMaker] Got event " + mess.toString)
     },
     "events"
     )
     */

    def render(state: State) = {
      //println(state.sop)
      <.div(
        SopMakerCSS.noSelect,

        // hover logic debug divs
        <.div(
          if (state.hover.fromId != null) state.hover.fromId.toString else "nop"),
        <.div(if (state.hover.toId != null) state.hover.toId.toString else "nop"),
        <.div(state.hover.currentlyDragging.toString),
        //<.div(state.sop.toString),

        svg.svg(
          ^.onMouseDownCapture --> handleMouseDown(state.hover),
          ^.onMouseUp --> handleMouseUp(state.hover),
          ^.onMouseLeave --> handleMouseLeftWidget(state.hover)
        )
      )
    }

    // def handleDrag(drag: String)(e: ReactDragEventFromInput): Callback = {
    //   Callback({
    //     e.dataTransfer.setData("json", drag)
    //   })
    // }

    // def handleDrop(drop: String)(e: ReactDragEvent): Callback = {
    //   val drag = e.dataTransfer.getData("json")
    //   Callback.log("Dropping " + drag + " onto " + drop)
    // }

    val paddingTop = 40f
    val paddingLeft = 40f

    def op(opId: UUID, opname: String, x: Float, y: Float) =
      svg.svg(
        ^.onMouseOver --> handleMouseOver(opId),
        ^.onMouseLeave --> handleMouseLeave(opId),
        SopMakerCSS.sopComponent,
        ^.draggable := false,
        svg.svg(
          ^.draggable := false,
          svg.width := opWidth.toInt,
          svg.height := opHeight.toInt,
          svg.x := x.toInt,
          svg.y := y.toInt,
          svg.rect(
            ^.draggable := false,
            svg.x := 0,
            svg.y := 0,
            svg.width := opWidth.toInt,
            svg.height := opHeight.toInt,
            svg.rx := 6, svg.ry := 6,
            svg.fill := "white",
            svg.stroke := "black",
            svg.strokeWidth := 1
          ),
          svg.svg(
            ^.draggable := false,
            SopMakerCSS.opText,
            svg.text(
              svg.x := "50%",
              svg.y := "50%",
              svg.textAnchor := "middle",
              svg.dy := ".3em", opname
            )
          )
        )
      )

    def parallelBars(x: Float, y: Float, w: Float) =
      svg.svg(
        SopMakerCSS.sopComponent,
        svg.width := "100%",
        svg.height := "100%",
        svg.svg(
          SopMakerCSS.sopComponent,
          svg.width := w.toInt,
          svg.height := 12,
          svg.rect(
            svg.x := (x + opWidth / 2).toInt,
            svg.y := y.toInt,
            svg.width := w.toInt,
            svg.height := 4,
            svg.fill := "black",
            svg.strokeWidth := 1
          ),
          svg.rect(
            svg.x := (x + opWidth / 2).toInt,
            svg.y := y.toInt + 8,
            svg.width := w.toInt,
            svg.height := 4,
            svg.fill := "black",
            svg.strokeWidth := 1
          )
        )
      )

    def handleMouseOver(zoneId: UUID): Callback = {
      $.modState(s =>
        s.copy(hover = s.hover.copy(toId = zoneId))
      )
    }

    def handleMouseLeave(zoneId: UUID): Callback = {
      $.modState(s =>
        if (!s.hover.currentlyDragging) s.copy(hover = s.hover.copy(toId = null))
        else s
      )
    }

    def handleMouseDown(h: Hover): Callback = {
      $.modState(s =>
        s.copy(
          hover = s.hover.copy(
            currentlyDragging = (s.hover.toId != null),
            fromId = h.toId
          )
        )
      )
    }

    def handleMouseUp(h: Hover): Callback = {
      Callback({
        $.modState(s => {
          s.copy(
            hover = s.hover.copy(
              toId = null,
              currentlyDragging = false
            ))
        }).runNow()
        println("dragged " + h.fromId + " onto " + h.toId)
      })
    }

    def handleMouseLeftWidget(h: Hover): Callback = {
      Callback({
        $.modState(s => s.copy(hover = Hover())).runNow()
        println("resetting hover state")
      })
    }

    def getRenderTree(node: RenderNode, xOffset: Float, yOffset: Float): TagMod = {
      node match {
        case n: RenderParallel => {
          var w = 0f
          svg.svg(
            parallelBars(xOffset - n.w / 2 + opSpacingX / 2, yOffset, n.w - opSpacingX),
            n.children.collect { case e: RenderNode => {
              val child = getRenderTree(
                e,
                xOffset + w + e.w / 2 - n.w / 2 + opSpacingX,
                yOffset + parallelBarHeight + opSpacingY
              )
              w += e.w
              child
            }
            }.toTagMod,
            parallelBars(
              xOffset - n.w / 2 + opSpacingX / 2,
              yOffset + n.h - parallelBarHeight - opSpacingY,
              n.w - opSpacingX)
          )
        }
        case n: RenderSequence => getRenderSequence(n, xOffset, yOffset)
      }
    }

    def getRenderSequence(seq: RenderSequence, xOffset: Float, yOffset: Float): TagMod = {
      var h = yOffset
      seq.children.collect { case q: RenderSequenceElement => {
        h += q.h
        getRenderTree(q.self, xOffset, h - q.h)
      }
      }.toTagMod
    }


    def onUnmount() = Callback {
      println("Unmounting sopmaker")
    }
  }

  private val component = ScalaComponent.builder[Unit]("SopMakerWidget")
    .initialState(State(hover = Hover()))
    .renderBackend[Backend]
    .componentWillUnmount(_.backend.onUnmount())
    .build

  def apply() = spgui.SPWidget(spwb => component())
}
