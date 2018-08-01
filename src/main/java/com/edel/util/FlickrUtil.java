package com.edel.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.springframework.stereotype.Component;

import com.edel.services.FlickrService;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.util.AuthStore;
import com.flickr4java.flickr.util.FileAuthStore;

/**
 * Utility singleton class for Flickr. Allows to get instance of Flickr, Auth, and email.
 * @author Edel
 *
 */

@Component
public class FlickrUtil {
	
	private Flickr flickr;
	private String email;
	private String userId;
	
	private static FlickrUtil instance = new FlickrUtil();
	private	final Logger log = Logger.getLogger(FlickrUtil.class);
	
	private FlickrUtil() {
		flickr = getFlickrInstance();
		email = getEmailProperties();
	}
	
	public static FlickrUtil getInstance() {
		return instance;
	}
	
	public Flickr getFlickr() {
		return flickr;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getUserId() {
		return userId;
	}
	
	/**
	 * Returns the email from the config file.
	 * @return String
	 */
	private String getEmailProperties() {
		String email = "";
		
		Properties prop = new Properties();
		try {
			log.info("Loading email from properties file...");
			prop.load(FlickrService.class.getClassLoader().getResourceAsStream("config.properties"));
			email = prop.getProperty("email");
			log.info("Email loaded.");
		} catch (IOException e) {
			log.error("IOException - " + e.getMessage());
		}
		
		return email;
	}

	/**
	 * Returns the Flickr instance with correct key and secret.
	 * @return Flickr
	 */
	private Flickr getFlickrInstance() {
		Properties prop = new Properties();
		Flickr flickr = null;
		try {
			log.info("Loading config from properties file...");
			prop.load(FlickrService.class.getClassLoader().getResourceAsStream("config.properties"));
			log.info("Config loaded.");
			
			String apiKey = prop.getProperty("apiKey");
			String secret = prop.getProperty("secret");

			flickr = new Flickr(apiKey, secret, new REST());
			log.info("Created Flickr instance from properties");
		} catch (IOException e) {
			log.error("IOException - " + e.getMessage());
		}
		return flickr;
	}
	
	/**
	 * Returns PhotosInterface from Flickr instance.
	 * @return PhotosInterface
	 */
	public PhotosInterface getPhotosInterface() {
		return flickr.getPhotosInterface();
	}
	
	/**
	 * Returns PhotosetsInterface from Flickr instance.
	 * @return PhotosetsInterface
	 */
	public PhotosetsInterface getPhotosetsInterface() {
		return flickr.getPhotosetsInterface();
	}
	
	/**
	 * Authorizes edel-read. Used for methods that need authorization.
	 * @return Auth or null
	 */
	public Auth authEdelRead() {
		log.info("Authorize read permission for edel-read...");
		File file = new File("src/main/resources/auth/edel-read");
		return authorize(getFlickr(), file, getEmail(), Permission.READ);
	}

	/**
	 * Authenticates and saves them for future use.
	 * @param flickr Flickr instance
	 * @param authDirectory File directory where to be saved
	 * @param email Email to search for user
	 * @return Auth
	 */
	private Auth authorize(Flickr flickr, File authDirectory, String email, Permission permission) {
		Auth auth = null;
		try {
			log.info("Begin authorization process...");
			AuthStore authStore = new FileAuthStore(authDirectory);
			auth = authStore.retrieve(flickr.getPeopleInterface().findByEmail(email).getId());
			if (auth != null) {
				RequestContext.getRequestContext().setAuth(auth);
				log.info("User authorized.");
				return auth;
			}

			log.info("Registering new user...");
			AuthInterface authInterface = flickr.getAuthInterface();
			Token accessToken = authInterface.getRequestToken();

			String url = authInterface.getAuthorizationUrl(accessToken, permission);
			System.out.println("Please visit the following URL to get your authorization token:");
			System.out.println();
			System.out.println(url);
			System.out.println();

			Scanner sc = new Scanner(System.in);
			System.out.print("Enter your token: ");
			String tokenKey = sc.nextLine();

			Token requestToken = authInterface.getAccessToken(accessToken, new Verifier(tokenKey));

			sc.close();

			auth = authInterface.checkToken(requestToken);
			RequestContext.getRequestContext().setAuth(auth);
			authStore.store(auth);

			log.info("New user registered with " + permission.toString() + " permission.");
			log.info("User stored as " + authDirectory.getName() + " in " + authDirectory.getPath());
		} catch (IOException e) {
			log.error("IOException - " + e.getMessage());
		} catch (FlickrException e) {
			log.error("FlickrException - " + e.getMessage());
		}
		return auth;
	}
	
}
