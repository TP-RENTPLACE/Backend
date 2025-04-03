package kattsyn.dev.rentplace.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private long imageId;

    @Column(name = "file_name", length = 400)
    private String fileName;
    @Column(name = "original_file_name")
    private String originalFileName;
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "additional_path")
    private String additionalPath;
    @Column(name = "size")
    private long size;
    @Column(name = "is_preview_image")
    private boolean isPreviewImage;

}
