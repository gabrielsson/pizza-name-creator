package com.gabrielsson

import io.quarkus.cache.Cache
import io.quarkus.runtime.annotations.RegisterForReflection
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.metrics.{MetricRegistry, Tag}

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import scala.io.Source

@ApplicationScoped
@RegisterForReflection(fields = false, methods = true)
class PizzaNameScalaService {

  @Inject var registry: MetricRegistry = null
  val cities: List[String] = initCities

  import io.quarkus.cache.CacheName

  import javax.inject.Inject

  @Inject
  @CacheName("pizza-names")
  var cache: Cache = null

  def nameForList(ingredients: Array[String]): Uni[String] = {
    registry.counter("pizza.menu.pizza.counter").inc()
    ingredients.foreach(s => registry.counter("pizza.menu.ingredient.generated.counter", new Tag("ingredient", s)).inc())
    cache.get(ingredients.mkString, (k: String) => cities.drop((cities.size * Math.random).toInt).head)
  }


  private def initCities = {
    val source = Source.fromResource("cities.dat")("UTF-8")
    val lines = source.getLines.toList
    source.close
    lines
  }

}
