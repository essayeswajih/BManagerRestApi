package org.example.gestionfactureapi.Entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "pdf_data", nullable = false)
    private byte[] pdfData;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    public File(byte[] pdfData, String fileName, String contentType) {
        this.pdfData = pdfData;
        this.fileName = fileName;
        this.contentType = contentType;
    }
}