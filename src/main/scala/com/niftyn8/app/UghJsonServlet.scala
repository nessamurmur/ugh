package com.niftyn8.app

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._

class UghJsonServlet extends UghStack with JacksonJsonSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }
}