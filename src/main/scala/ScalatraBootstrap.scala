import com.niftyn8.app._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  implicit val swagger = new IssuesSwagger()

  override def init(context: ServletContext) {
    context.mount(new IssuesController, "/issues/*")
    context.mount(new DocsController, "/docs/*")
  }
}
