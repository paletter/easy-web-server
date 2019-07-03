package com.paletter.easy.web.server.dept.accept;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public class AcceptWorker implements Runnable {

	private Selector selector;
	
	public AcceptWorker() {
		selector = Selector.open();
		server.register(selector, SelectionKey.OP_ACCEPT);
	}

	@Override
	public void run() {
		
	}

}
