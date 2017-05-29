package org.manage.home;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;

import org.manage.home.mail.Messages;

public class VideoInformationExtractor extends Observable implements Runnable {

	private String url;
	private StringBuffer output;

	public VideoInformationExtractor(String url) {
		this.url = url;
	}

	@Override
	public void run() {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(Messages.ext_youtube_dl + " -F " + url);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		output = new StringBuffer();
		output.append("Here is the standard output of the command:\n");
		String s = null;
		try {
			while ((s = stdInput.readLine()) != null) {
				output.append(s);
				output.append("\n");
			}
			output.append("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
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
