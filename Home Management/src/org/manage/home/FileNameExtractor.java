package org.manage.home;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;

import org.manage.home.mail.Messages;

public class FileNameExtractor extends Observable implements Runnable {

	private String url;
	private StringBuffer output;

	public FileNameExtractor(String url) {
		this.url = url;
	}

	@Override
	public void run() {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(Messages.ext_youtube_dl + " --get-filename -o '%(title)s' " + url);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
		output = new StringBuffer();
		String s = null;
		try {
			while ((s = stdInput.readLine()) != null) {
				output.append(s);
			}
			setChanged();
			notifyObservers();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public StringBuffer getOutput() {
		return output;
	}

	public void setOutput(StringBuffer output) {
		this.output = output;
	}

}
