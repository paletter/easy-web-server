package com.paletter.easy.web.server.http.request;

import java.net.Socket;

import com.paletter.easy.web.server.utils.LogUtil;
import com.paletter.easy.web.server.utils.StringUtils;
import com.paletter.iotool.IOReadTool;

public class RequestBIO extends Request {
	
	private Socket socket;
	
	public RequestBIO(Socket socket) {
		super();
		this.socket = socket;
	}

	public boolean parse() throws Exception {
		
		String reqContent = IOReadTool.readContent(socket.getInputStream());
		if (StringUtils.isNotEmpty(reqContent)) {
			LogUtil.printDebug("# Request Content: " + reqContent);
			
			parseHeader(reqContent);
			parseBody(reqContent);
			parseParameters();
		}
		return isParseSucc;
	}
}
