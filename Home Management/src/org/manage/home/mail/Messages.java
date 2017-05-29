package org.manage.home.mail;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.manage.home.mail.messages"; //$NON-NLS-1$
	public static String mailer_from;
	public static String mailer_to;
	public static String mailer_smtp_host;
	public static String mailer_smtp_port;
	public static String mailer_smtp_socket_port;
	public static String mailer_stmp_auth;
	public static String ext_youtube_dl;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
