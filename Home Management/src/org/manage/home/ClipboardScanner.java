package org.manage.home;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Observable;

public class ClipboardScanner extends Observable implements Runnable, ClipboardOwner {

	private String url;
	private Clipboard clipboard;

	public ClipboardScanner() {
		this.url = "";
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		clipboard = toolkit.getSystemClipboard();

	}

	@Override
	public void run() {
		try {
			while (true) {

				url = (String) clipboard.getData(DataFlavor.stringFlavor);
				if (url.startsWith("http")) {
					clipboard.setContents(new StringSelection(""), this);
					setChanged();
					notifyObservers();
				}
				Thread.sleep(1000);
			}
		} catch (UnsupportedFlavorException | IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO Auto-generated method stub
		
	}

}
