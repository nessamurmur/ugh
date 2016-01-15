package com.niftyn8.app

import org.json4s.{Formats, DefaultFormats}
import org.scalatra.json.NativeJsonSupport
import org.scalatra.swagger.{Swagger, SwaggerSupport}
import org.scalatra._

case class Issue(repo: String, text: String, time_wasted: Int,
                 id: String = java.util.UUID.randomUUID.toString, reporter: String)
case class IssueWrapper(issue: Issue)

case class IssueData(issues: List[Issue]) {
  def +(issue: Issue): IssueData = IssueData(issue :: issues)

  def -(issue: Issue): IssueData = IssueData(issues diff List(issue))

  def find(id: String): Option[Issue] = issues find(_.id == id)
}

class IssuesController(implicit val swagger: Swagger) extends UghServlet with NativeJsonSupport with SwaggerSupport {
  override protected val applicationName = Option("Issues")
  protected val applicationDescription = "The issues API exposes operations for tracking problems you hit during development."

  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  protected var issueData = IssueData(List())

  val getIssues =
    (apiOperation[List[Issue]]("getIssues")
      summary "Show all issues")

  get("/", operation(getIssues)) {
    issueData
  }

  val createIssue =
    (apiOperation[IssueWrapper]("createIssue")
      summary "Creates a new issue"
      parameter queryParam[String]("text").description("A body of text describing, in as much detail as possible, the problem and the resolution if found.")
      parameter queryParam[String]("repo").description("A link to the version control repository where the problem is located.")
      parameter queryParam[Int]("time_wasted").description("Time in seconds wasted on the problem.")
      parameter queryParam[String]("reporter").description("A username for the person reporterd the problem."))

  post("/", operation(createIssue)) {
    val issue = parsedBody.extractOpt[Issue]
    issue match {
      case Some(i) =>
        issueData = issueData + i
        IssueWrapper(i)
      case None =>
        UnprocessableEntity("You were missing one or more required params")
    }
  }

  val findIssue =
    (apiOperation[IssueWrapper]("findIssue")
      summary "Find an issue by ID"
      notes "The API will provide you with an ID when you create an issue. You can use that ID at this endpoint to fetch details about the issue."
      parameter pathParam[String]("id").description("A uuid for a particular issue"))

  get("/:id", operation(findIssue)) {
    val issue = issueData.find(params("id"))
    issue match {
      case Some(i) => IssueWrapper(i)
      case None => NotFound("No issue could be found by that ID")
    }
  }

  val deleteIssue =
    (apiOperation[IssueData]("deleteIssue")
      summary "Delete a particular issue by ID"
      parameter pathParam[String]("id").description("A uuid for a particular issue"))


  delete("/:id", operation(deleteIssue)) {
    val issue = issueData.find(params("id"))
    issue match {
      case Some(i) =>
        issueData = issueData - i
        issueData
      case None => NotFound("No issue could be found by that ID")
    }
  }
}
