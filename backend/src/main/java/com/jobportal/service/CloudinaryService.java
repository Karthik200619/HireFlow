package com.jobportal.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadProfileImage(MultipartFile file, Long userId) {
        validateImage(file);
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "hireflow/profile-images",
                            "public_id", "user-" + userId,
                            "overwrite", true,
                            "resource_type", "image"
                    )
            );
            Object secureUrl = result.get("secure_url");
            if (secureUrl == null) throw new IllegalStateException("Cloudinary did not return a secure URL");
            return secureUrl.toString();
        } catch (IOException e) {
            throw new IllegalStateException("Could not upload profile image to Cloudinary", e);
        }
    }

    public String uploadCompanyImage(MultipartFile file, Long id) {
        validateCompanyImage(file);
        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "hireflow/company-images", "public_id", "company-" + id, "overwrite", true, "resource_type", "image"));
            Object secureUrl=result.get("secure_url");
            if(secureUrl==null) throw new IllegalStateException("Cloudinary did not return a secure URL");
            return secureUrl.toString();
        } catch(IOException e){throw new IllegalStateException("Could not upload company image to Cloudinary",e);}
    }
    private void validateCompanyImage(MultipartFile file){
        if(file==null||file.isEmpty()) throw new IllegalArgumentException("Company image is empty");
        if(file.getSize()>5*1024*1024) throw new IllegalArgumentException("Company image must be 5 MB or smaller");
        String t=file.getContentType();
        if(t==null||!(t.equalsIgnoreCase("image/jpeg")||t.equalsIgnoreCase("image/png")||t.equalsIgnoreCase("image/webp"))) throw new IllegalArgumentException("Only JPG, PNG, and WEBP company images are allowed");
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Profile image is empty");
        if (file.getSize() > 5 * 1024 * 1024) throw new IllegalArgumentException("Profile image must be 5 MB or smaller");
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equalsIgnoreCase("image/jpeg") ||
                contentType.equalsIgnoreCase("image/png") ||
                contentType.equalsIgnoreCase("image/webp"))) {
            throw new IllegalArgumentException("Only JPG, PNG, and WEBP profile images are allowed");
        }
    }
}
