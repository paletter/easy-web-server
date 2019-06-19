package com.paletter.easy.web.server.support;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

import com.paletter.easy.web.server.utils.LogUtil;

public class SelectorHandler {

	private boolean isOver = false;
	
	public SelectorHandler(Selector selector, Handler handler) {

		try {
			
			while(!isOver) {

				while(!isOver && selector.select() > 0) {
					
					Iterator<SelectionKey> ite = selector.selectedKeys().iterator();
					
					while(!isOver && ite.hasNext()) {
						
						try {
								
							SelectionKey key = ite.next();
							
							if (!key.isValid()) continue;

							// Do Handle
							isOver = handler.doHandle(key, ite);
							
						} catch (Throwable e) {
							LogUtil.error("", e);
						}
					}
				}
			}
			
		} catch (Exception e) {
			LogUtil.error("", e);
		}
	}
	
	public interface Handler {
		
		public boolean doHandle(SelectionKey key, Iterator<SelectionKey> ite);
	}
}
