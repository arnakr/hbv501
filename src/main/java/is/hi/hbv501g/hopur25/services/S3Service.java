/*
package is.hi.hbv501g.hopur25.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName = "recipedb-profile-pictures";

    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Uploads a file to S3.
     *
     * @param file The file to upload.
     * @return The URL of the uploaded file.
     * @throws IOException If an I/O error occurs.
     */
/*
    public String uploadFile(MultipartFile file) throws IOException {
        String key = UUID.randomUUID().toString() + "_" + file.getOriginalFilename(); // Unique filename

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            // Upload the file to S3
            s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

            // Return the URL of the uploaded file
            return getFileUrl(key);
        } catch (S3Exception e) {
            throw new IOException("Error uploading file to S3", e);
        }
    }

    /**
     * Generates a pre-signed URL for accessing the file.
     *
     * @param key The key of the file in S3.
     * @return The pre-signed URL for the file.
     */
/*
    public String getFileUrl(String key) {
        Instant expiration = Instant.now().plusSeconds(3600); // URL valid for 1 hour

        // Generate the pre-signed URL
        URL url = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key));
        return url.toString();
    }

    /**
     * Deletes a file from S3.
     *
     * @param key The key of the file to delete.
     */
/*
    public void deleteFile(String key) {
        s3Client.deleteObject(deleteObjectRequest -> deleteObjectRequest.bucket(bucketName).key(key));
    }
}
 */
