/*
package com.ctrip.lpxie;

import com.ctrip.lpxie.basement.filter.LoginServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

*/
/**
 * Created by lpxie on 2016/8/19.
 *//*

public class JettyLauncher {
    public static final int PORT = 8080;
    public static final String CONTEXT = "/";

    private static final String DEFAULT_WEBAPP_PATH = "src/main/webapp";
    */
/**
     * �������ڿ������е��Ե�Jetty Server, ��src/main/webappΪWebӦ��Ŀ¼.
     *//*

    public static Server createServerInSource(int port, String contextPath) {
        Server server = new Server();
        // ������JVM�˳�ʱ�ر�Jetty�Ĺ��ӡ�
        server.setStopAtShutdown(true);

        //����http��������
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        // ���Windows���ظ�����Jetty��Ȼ������˿ڳ�ͻ������.
        connector.setReuseAddress(false);
        server.setConnectors(new Connector[]{connector});

        WebAppContext webContext = new WebAppContext(DEFAULT_WEBAPP_PATH, contextPath);
        webContext.setContextPath("/");
        webContext.setDescriptor("src/main/webapp/WEB-INF/web.xml");
        // ����webapp��λ��
        webContext.setResourceBase(DEFAULT_WEBAPP_PATH);
        webContext.setClassLoader(Thread.currentThread().getContextClassLoader());
        webContext.addServlet(LoginServlet.class,"/");

        server.setHandler(webContext);
        return server;
    }

    */
/**
     * ����jetty����
     * @param port
     * @param context
     *//*

    public void startJetty(int port,String context){
        final Server server = JettyLauncher.createServerInSource(PORT, CONTEXT);
        try {
            server.stop();
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    public static void main(String[] args) {
        new JettyLauncher().startJetty(8080, "");
    }
}
*/
