import scala.language.implicitConversions

import com.lynden.gmapsfx.{GoogleMapView, MapComponentInitializedListener}
import com.lynden.gmapsfx.javascript.`object`._

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Separator, Spinner}
import scalafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory
import scalafx.scene.layout._
import scalafx.scene.paint.Color._
import scalafx.scene.text.Text
import scalafx.util.StringConverter
import scalafx.util.converter.FormatStringConverter

import java.text.DecimalFormat

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction
import org.apache.commons.math3.analysis.UnivariateFunction

object Main extends JFXApp with MapComponentInitializedListener {

  val mapView: GoogleMapView = new GoogleMapView
  mapView.addMapInializedListener(this)

  val mapAnchor = new AnchorPane(mapView) {
    vgrow = Priority.Always
  }

  type JDouble = java.lang.Double
  val gradeSpinner = new Spinner[JDouble]() {
    editable = true
    valueFactory = new DoubleSpinnerValueFactory(0, 1, 1, .005) {
      converter = new StringConverter[JDouble] {
        val df = new DecimalFormat("#.##%")
        def fromString(s: String): JDouble = {
          if (s == null) null
          else {
            val ts = s.trim
            if (ts.length < 1) null
            else df.parse(ts).doubleValue
          }
        }
        def toString(d: JDouble): String = {
          if (d == null) null
          else df.format(d)
        }
      }
    }
  }

  stage = new PrimaryStage {
    title = "Grade Thing"
    scene = new Scene {
      root = new VBox {
        padding = Insets(5)
        spacing = 5
        children = Seq(
          new HBox {
            alignment = Pos.BaselineLeft
            spacing = 10
            children = Seq(
              new Text("Grade:"),
              gradeSpinner,
              new Button("Go!"))
          },
          new Separator,
          mapAnchor)
      }
    }
  }

  def mapInitialized(): Unit = {
    val mapOptions: MapOptions = new MapOptions()
      .mapType(MapTypeIdEnum.ROADMAP)
      .center((41.850,-87.650))
      .streetViewControl(false)
      .zoom(3)
    mapView.createMap(mapOptions)
  }

  val initialPoints: Array[(Double, Double)] = Array(
    (100.0, 47.61),
    ( 94.0, 32.78),
    ( 93.5, 29.77),
    ( 93.0, 29.42),
    ( 92.5, 31.78),
    ( 89.5, 25.67),
    ( 82.5, 19.43),
    ( 79.5, 14.53))

  val interpFunction: (Double => Double) = (new SplineInterpolator().interpolate _).tupled(initialPoints.sortBy(_._1).unzip)

  implicit class ApacheFunc2Func(apFunc: UnivariateFunction) extends (Double => Double) {
    def apply(v: Double): Double = apFunc.value(v)
  }
  implicit def tuple2LatLong[N <% Double](p: (N, N)): LatLong = new LatLong(p._1, p._2)
}
