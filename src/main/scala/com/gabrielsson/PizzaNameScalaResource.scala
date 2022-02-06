package com.gabrielsson

import org.eclipse.microprofile.metrics.annotation.Timed

import javax.inject.Inject
import javax.ws.rs._
import javax.ws.rs.core.MediaType


@Path("/name")
class PizzaNameScalaResource  {

  @Inject var service: PizzaNameScalaService = null

  @POST
  @Timed
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  def name(ingredients: Array[Array[String]]): Array[String] = {
    ingredients.map(service.nameForList(_))
      .map(uni => uni.await().indefinitely())
  }
}