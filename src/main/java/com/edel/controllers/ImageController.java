package com.edel.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edel.entities.PhotoEntity;
import com.edel.entities.PhotosetEntity;
import com.edel.entities.SizeEntity;
import com.edel.services.FlickrService;
import com.edel.util.FlickrUtil;

@RestController
@CrossOrigin(origins = "*")
public class ImageController {
	
	@Autowired
	private FlickrService service = new FlickrService();
	
	@Autowired
	private FlickrUtil util = FlickrUtil.getInstance();
	
	private	final static Logger log = Logger.getLogger(ImageController.class);
	
	/**
	 * Returns PhotosetEntity information with
	 * List of PhotoEntity based on Photoset ID or Title.
	 * Returns null Photoset if no Album exists or no photos available.
	 * @param idOrTitle
	 * @return
	 */
	@GetMapping("images/album/{idOrTitle}")
	private PhotosetEntity getAlbum(@PathVariable String idOrTitle) {
		List<PhotosetEntity> list = new ArrayList<PhotosetEntity>();
		PhotosetEntity photoset = null;
		
		log.info("Request to retrieve photos from photoset...");
		
		// Authorize edel-read or do not retrieve anything
		if (util.authEdelRead() != null) {
			
			photoset = service.getPhotosetByIdOrTitle(idOrTitle);
			
			// If photoset is not null, set the list of PhotoEntity
			if (photoset != null) {
				photoset.setPhotos(service.getPhotosFromPhotoset(photoset));
				list.add(photoset);
				log.info("Request completed with photoset.");
			} else {
				log.warn("Request completed with no photoset.");
			}
		} else {
			log.warn("Request completed with failed Authorization.");
		}
		
		return photoset;
	}
	
	/**
	 * Returns a List of PhotoEntity based on Photoset ID or Title.
	 * Used to generate URLs for different photo sizes.
	 * Returns empty list if no Album exists or no photos available.
	 * @param idOrTitle
	 * @return
	 */
	@GetMapping("images/album/{idOrTitle}/photos")
	private List<PhotoEntity> getImagesFromAlbum(@PathVariable String idOrTitle) {
		List<PhotoEntity> list = new ArrayList<PhotoEntity>();
		PhotosetEntity photoset = null;
		
		log.info("Request to retrieve photos from photoset...");
		
		// Authorize edel-read or do not retrieve anything
		if (util.authEdelRead() != null) {
			
			photoset = service.getPhotosetByIdOrTitle(idOrTitle);
			
			// If photoset is not null, set the list of PhotoEntity
			if (photoset != null) {
				list = service.getPhotosFromPhotoset(photoset);
				log.info("Request completed with photoset.");
			} else {
				log.warn("Request completed with no photoset.");
			}
		} else {
			log.warn("Request completed with failed Authorization.");
		}
		
		return list;
	}
	
	/**
	 * Returns a List of PhotoEntity based on Photoset ID or Title.
	 * Used to generate URLs for different photo sizes.
	 * Returns empty list if no Album exists or no photos available.
	 * @param idOrTitle
	 * @return
	 */
	@GetMapping("images/album/{albumIdOrTitle}/photos/{photoIdOrTitle}")
	private PhotoEntity getImageFromAlbumByIdOrTitle(@PathVariable String albumIdOrTitle,
															@PathVariable String photoIdOrTitle) {
		List<PhotoEntity> list = new ArrayList<PhotoEntity>();
		PhotosetEntity photoset = null;
		PhotoEntity photoEntity = null;
		
		log.info("Request to retrieve photos from photoset...");
		
		// Authorize edel-read or do not retrieve anything
		if (util.authEdelRead() != null) {
			
			photoset = service.getPhotosetByIdOrTitle(albumIdOrTitle);
			
			// If photoset is not null, set the list of PhotoEntity
			if (photoset != null) {
				photoEntity = service.getPhotoFromPhotosetByIdOrTitle(photoset, photoIdOrTitle);
				if (photoEntity != null) {
					list.add(photoEntity);
					log.info("Request completed with photoset.");
				} else {
					log.warn("Request completed with no photo.");
				}
				
			} else {
				log.warn("Request completed with no photoset.");
			}
		} else {
			log.warn("Request completed with failed Authorization.");
		}
		
		return photoEntity;
	}
	
	/**
	 * Returns a List of SizeEntity based on photo ID.
	 * Returns empty list if no sizes are available.
	 * @param photoId ID of the photo
	 * @return
	 */
	@GetMapping("image/sizes/{photoId}")
	private List<SizeEntity> getImageSizesById(@PathVariable String photoId) {
		List<SizeEntity> list = new ArrayList<SizeEntity>();
		
		log.info("Request to get sizes from photo: " + photoId);
		
		if (util.authEdelRead() != null) {			
			if (service.isValidPhotoId(photoId)) {
				list = service.getPhotoSizes(photoId);
				log.info("Request Completed with sizes..");
			} else {
				log.warn("Request completed with invalid photo ID.");
			}
		} else {
			log.warn("Request completed with failed Authorization.");
		}
		
		return list;
	}
	
	@PostMapping("auth/{authName}")
	private boolean authenticate(@PathVariable String authName) {
		log.info("Request to authorize...");
		if (authName.toLowerCase().equals("edel-read")) {
			if (util.authEdelRead() != null) {
				log.info("Request authorized.");
				return true;
			}
		}
		return false;
	}

}
