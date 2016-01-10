package com.niftyn8.app

case class Issue(repo: String, text: String, time_wasted: Int,
                 id: String = java.util.UUID.randomUUID.toString, reporter: String)
case class IssueError(error: String = "No issue could be found by that slug.")
case class IssueWrapper(issue: Issue)

case class IssueData(issues: List[Issue]) {
  def +(issue: Issue): IssueData = IssueData(issue :: issues)

  def -(issue: Issue): IssueData = IssueData(issues diff List(issue))

  def find(id: String): Option[Issue] = issues find(_.id == id)
}

class IssuesController extends UghJsonServlet {
  protected var issueData = IssueData(List())

  get("/") {
    issueData
  }

  post("/") {
    val issue = parsedBody.extract[Issue]
    issueData = issueData + issue
    issueData
  }

  get("/:id") {
    val issue = issueData.find(params("id"))
    issue match {
      case Some(i) => IssueWrapper(i)
      case None => IssueError()
    }
  }

  delete("/:id") {
    val issue = issueData.find(params("id"))
    issue match {
      case Some(i) => {
        issueData = issueData - i
        issueData
      }
      case None => IssueError()
    }
  }
}
