package com.edel.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.edel.entities.DateEntity;
import com.edel.entities.PhotoEntity;
import com.edel.entities.PhotosetEntity;
import com.edel.entities.SizeEntity;
import com.edel.util.FlickrUtil;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.Size;
import com.flickr4java.flickr.photosets.Photoset;

/**
 * Provides service implementation to retrieve information such as photo information.
 * @author Edel
 *
 */

@Service
public class FlickrService {
	
	private final String[] labelList = {"Thumbnail", "Square", "Small", "Medium", "Large", "Original", "Large Square", "Small 320",
            "Medium 640", "Medium 800", "Large 1600", "Large 2048", "Site MP4", "Video Player", "Video Original", "Mobile MP4", "HD MP4"};
	
	private FlickrUtil util = FlickrUtil.getInstance();
	private	final Logger log = Logger.getLogger(FlickrService.class);
	
	/**
	 * Returns List of Photo based on Photoset given.
	 * Returns empty list if no photos available.
	 * @param photoset Photoset Object
	 * @return List
	 */
	public List<PhotoEntity> getPhotosFromPhotoset(PhotosetEntity photoset) {
		List<PhotoEntity> list = new ArrayList<PhotoEntity>();
		try {
			log.info("Retrieving photos from photoset...");
			PhotoList<Photo> photoList = util
										.getPhotosetsInterface()
										.getPhotos(photoset.getId(), 0, 0);
			
			if (photoList.size() > 0) {
				for (Photo p : photoList) {
					PhotoEntity entity = new PhotoEntity();
					entity.setId(p.getId());
					entity.setSecret(p.getSecret());
					entity.setServer(p.getServer());
					entity.setFarm(Integer.parseInt(p.getFarm()));
					entity.setTitle(p.getTitle());
					entity.setSizes(new ArrayList<SizeEntity>());
					list.add(entity);
				}
			}
			
			if (list.size() > 0) {
				log.info("Photos retrieved.");
			} else {
				log.warn("No photos retrieved.");
			}
		} catch (FlickrException e) {
			log.error("Flickr Exception - " + e.getMessage());
		}
		return list;
	}
	
	/**
	 * Returns PhotoEntity from Photoset based on photo ID or Title.
	 * Returns null if no photo available.
	 * @param photoset PhotosetEntity Object
	 * @return List
	 */
	public PhotoEntity getPhotoFromPhotosetByIdOrTitle(PhotosetEntity photoset, String photoIdOrTitle) {
		List<PhotoEntity> photos = new ArrayList<PhotoEntity>();
		PhotoEntity photo = null;
		try {
			// Get list of photos from photoset
			photos = getPhotosFromPhotoset(photoset);
			
			// If valid Photo ID, get photo by ID. Else, get photo by Title.
			if (isValidPhotoId(photoIdOrTitle)) {
				log.info("Searching for photo ID: " + photoIdOrTitle);
				for (PhotoEntity p : photos) {
					if (p.getId().equals(photoIdOrTitle)) {
						photo = p;
						break;
					}
				}
			} else {
				log.info("Searching for photo Title: " + photoIdOrTitle);
				for (PhotoEntity p : photos) {
					if (p.getTitle().toLowerCase().equals(photoIdOrTitle.toLowerCase())) {
						photo = p;
						break;
					}
				}
			}
			
			if (photo != null) {
				log.info("Photo found.");
				return photo;
			} else {
				log.warn("Photo not found.");
			}
			
		} catch (Exception e) {
			log.error("Exception - " + e.getMessage());
		}
		return null;
	}

	/**
	 * Returns a list of DateEntity that contains all dates
	 * of an image. It gets the image dates based on the photo ID.
	 * Returns empty list if no dates available.
	 * @param photoId ID of image
	 * @return List of DateEntity
	 */
	public List<DateEntity> getPhotoDates(Photo photo) {
		List<DateEntity> list = new ArrayList<DateEntity>();
		
		DateEntity dates = new DateEntity();
		
		try {
			log.info("Retrieving dates from photo: " + photo.getId());
			
//			dates.setLastupdate(util.getFlickr().getPhotosInterface().getPhoto(photoId).getLastUpdate().toString());
//			dates.setPosted(util.getFlickr().getPhotosInterface().getPhoto(photoId).getDatePosted().toString());
//			dates.setTaken(util.getFlickr().getPhotosInterface().getPhoto(photoId).getDateTaken().toString());
//			dates.setTakengranularity(util.getFlickr().getPhotosInterface().getPhoto(photoId).getTakenGranularity());
			
//			Photo photo = util.getFlickr().getPhotosInterface().getInfo(id, secret);
			dates.setLastupdate(photo.getLastUpdate().toString());
			dates.setPosted(photo.getDatePosted().toString());
			dates.setTaken(photo.getDateTaken().toString());
			dates.setTakengranularity(photo.getTakenGranularity());
			
			list.add(dates);
			
			if (list.size() > 0) {
				log.info("Dates retrieved.");
			}
		} catch (Exception e) {
			log.error("Flickr Exception - " + e.getMessage());
		}
		
		return list;
	}

	/**
	 * Returns a list of SizeEntity.
	 * @param sizes Collection of Size
	 * @return List of SizeEntity
	 */
	public List<SizeEntity> getPhotoSizes(String photoId) {
		List<SizeEntity> list = new ArrayList<SizeEntity>();
		
		try {
			log.info("Retrieving sizes from photo...");
			
			Collection<Size> sizes = util.getFlickr().getPhotosInterface().getSizes(photoId);
			
			if (sizes.size() > 0) {
				for (Size s : sizes) {
					SizeEntity size = new SizeEntity();
					size.setHeight(s.getHeight());
					size.setWidth(s.getWidth());
					size.setMedia(s.getMedia().toString());
					size.setUrl(s.getUrl());
					size.setSource(s.getSource());
					
					// Set the label name based on index position
					for (int i = 0; i < labelList.length; i++) {
						if (i == s.getLabel()) {
							size.setLabel(labelList[i]);
						}
					}
					
					list.add(size);
				}
				
				if (list.size() > 0) {
					log.info("Photo sizes retrieved.");
				} else {
					log.warn("Photo sizes not retrieved.");
				}
			}
		} catch (FlickrException e) {
			log.error("Flickr Exception - " + e.getMessage());
		}
		
		return list;
	}

	/**
	 * Returns Photoset by ID or Title. Returns null if not found.
	 * @param title Title of Photoset
	 * @return Photoset
	 */
	public PhotosetEntity getPhotosetByIdOrTitle(String idOrTitle) throws NullPointerException {
		Photoset photoset = null;
		PhotosetEntity photosetEntity = null;
		try {
			log.info("Retrieving all photosets...");
			Collection<Photoset> photosets = util.getFlickr()
											.getPhotosetsInterface()
											.getList(util.getUserId())
											.getPhotosets();
			
			// If no photosets found, do nothing. Else, search for photoset by ID or Title.
			if (photosets.size() <= 0) {
				log.warn("No photosets retrieved!");
			} else {
				log.info("Photosets retrieved.");
				
				// If valid number, check photoset by ID. Else, check photoset by title.
				if (isValidNumber(idOrTitle)) {
					log.info("Retrieving Photoset by ID: " + idOrTitle);
					for (Photoset p : photosets) {
						if (idOrTitle.equals(p.getId())) {
							photoset = p;
							break;
						}
					}
				} else {
					log.info("Retrieving Photoset by title: " + idOrTitle);
					for (Photoset p : photosets) {
						if (idOrTitle.toLowerCase().equals(p.getTitle().toLowerCase())) {
							photoset = p;
							break;
						}
					}
				}
				
				// Check if photoset is found
				if (photoset != null) {
					photosetEntity = new PhotosetEntity();
					photosetEntity.setId(photoset.getId());
					photosetEntity.setTitle(photoset.getTitle());
					photosetEntity.setDescription(photoset.getDescription());
					photosetEntity.setDateCreated(photoset.getDateCreate());
					photosetEntity.setDateUpdated(photoset.getDateUpdate());
					log.info("Photoset retrieved.");
					return photosetEntity;
				} else {
					log.warn("Photoset " + idOrTitle + " not retrieved.");
				}
			}
		} catch (FlickrException e) {
			log.error("Flickr Exception - " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Return true or false if string is a valid number.
	 * Checks that the string only contains numbers.
	 * @param id String representation of a number
	 * @return Boolean
	 */
	public boolean isValidNumber(String id) {
		String regex = "[0-9]+";
		
		Pattern pattern = Pattern.compile(regex);
		
		Matcher matcher = pattern.matcher(id);
		
		if (matcher.find()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Return true or false if string is a valid number.
	 * Checks that the string only contains numbers.
	 * @param id String representation of a number
	 * @return Boolean
	 */
	public boolean isValidPhotoId(String id) {
		String regex = "[0-9]{11,}";
		
		Pattern pattern = Pattern.compile(regex);
		
		Matcher matcher = pattern.matcher(id);
		
		if (matcher.find()) {
			return true;
		}
		
		return false;
	}

}
