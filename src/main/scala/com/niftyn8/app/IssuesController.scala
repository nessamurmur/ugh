package com.niftyn8.app

case class Issue(slug: String, name: String)
case class IssueError(error: String = "No issue could be found by that slug.")
case class IssueWrapper(issue: Issue)

case class IssueData(issues: List[Issue]) {
  def all(): List[Issue] = issues

  def +(issue: Issue): IssueData = IssueData(all :+ issue)

  def find(slug: String): Option[Issue] = all() find(_.slug == slug)
}

class IssuesController extends UghJsonServlet {
  protected var issueData = IssueData(List())

  get("/") {
    issueData.all()
  }

  post("/") {
    val issue = parsedBody.extract[Issue]
    issueData = issueData + issue
    issueData
  }

  get("/:slug") {
    val issue = issueData.find(params("slug"))
    issue match {
      case Some(i) => IssueWrapper(i)
      case None => IssueError()
    }
  }
}
