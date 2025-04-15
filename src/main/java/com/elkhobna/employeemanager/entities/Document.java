package com.elkhobna.employeemanager.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Entity(name = "document_")
public class Document {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @Column(nullable=false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Lob
    private byte [] image;

    @Transient
    private String imageBase64;
    private String imageName;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private Category category;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private Employee employee;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public @NotNull Category getCategory() {
        return category;
    }

    public void setCategory(@NotNull Category category) {
        this.category = category;
    }

    public @NotNull Employee getEmployee() {
        return employee;
    }

    public void setEmployee(@NotNull Employee employee) {
        this.employee = employee;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageBase64() {
        return new String(image);
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
        this.setImage(imageBase64.getBytes(StandardCharsets.UTF_8));
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
