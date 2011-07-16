package org.yeshira.web;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.servlet.MultiPartFilter;

public class Runner {
	public static void main(String[] args) throws Exception {
		/**
		 * Server initialization
		 */
		Server server = new Server(9091);

		
		/**
		 * Root context initialization
		 */
		Context root = new Context(server, "/", Context.DEFAULT);
		/**
		 * MultiPartFilter initialization
		 */
		FilterHolder fh = new FilterHolder(MultiPartFilter.class);
		fh.setInitParameter("deleteFiles", "true");
		root.addFilter(fh, "/", Handler.DEFAULT);
		
		/**
		 * Default servlet initialization
		 */
//		ServletHolder sh = new ServletHolder(BuildServlet.class);
//		root.addServlet(sh, "/");
		WebAppContext wac = new WebAppContext("Direct App Web Module", "/");
//		wac.setWar(war)
		
//		server.add
       
		/**
		 * Start the server
		 */
        server.start();
        // Wait on server thread, so program will exit only when server is shutdown
        server.join();
	}

}
