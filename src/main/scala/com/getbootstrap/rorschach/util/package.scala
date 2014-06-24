package com.getbootstrap.rorschach

import java.nio.charset.Charset
import scala.collection.mutable
import scala.util.Try
import spray.http.Uri

package object util {
  private val utf8 = Charset.forName("UTF-8")

  implicit class Utf8String(str: String) {
    def utf8Bytes: Array[Byte] = str.getBytes(utf8)
  }

  implicit class Utf8ByteArray(bytes: Array[Byte]) {
    def utf8String: Try[String] = Try { new String(bytes, utf8) }
  }

  implicit class RichStack[T](stack: mutable.Stack[T]) {
    def popOption(): Option[T] = Try{ stack.pop() }.toOption
    def topOption: Option[T] = Try{ stack.top }.toOption
  }

  implicit class RichUri(uri: Uri) {
    import spray.http.Uri.NamedHost
    import spray.http.Uri.Query.{Empty=>EmptyQuery}

    def isHttp = uri.scheme == "http" || uri.scheme == "https"
    def lacksUserInfo = uri.authority.userinfo.isEmpty
    def lacksNonDefaultPort = uri.authority.port <= 0
    def hasNamedHost = uri.authority.host.isInstanceOf[NamedHost]
    def isSafe = uri.isHttp && uri.lacksUserInfo && uri.hasNamedHost && uri.lacksNonDefaultPort && uri.isAbsolute
    def withoutQuery = uri.withQuery(EmptyQuery)
  }
}
