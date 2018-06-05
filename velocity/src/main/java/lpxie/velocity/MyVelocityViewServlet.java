package lpxie.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.servlet.VelocityViewServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by lpxie on 2017/1/20.
 */
public class MyVelocityViewServlet extends VelocityViewServlet {
    @Override
    protected Template handleRequest(HttpServletRequest request, HttpServletResponse response, Context ctx) throws Exception {
        ctx.put("fullName", "lpxie");
        request.setAttribute("secondName","xili");
        return getTemplate("velocity/test.vm");
    }
}
