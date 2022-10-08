package com.xiaoxiong.bio.tomcat.servlet;

import com.xiaoxiong.bio.tomcat.http.GPRequest;
import com.xiaoxiong.bio.tomcat.http.GPResponse;
import com.xiaoxiong.bio.tomcat.http.GPServlet;

public class FirstServlet extends GPServlet {

	public void doGet(GPRequest request, GPResponse response) throws Exception {
		this.doPost(request, response);
	}

	public void doPost(GPRequest request, GPResponse response) throws Exception {
		response.write("This is First Serlvet");
	}

}
