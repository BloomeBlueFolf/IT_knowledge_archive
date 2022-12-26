package com.example.demo.Entities;

import com.example.demo.Security.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "folder")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min=1, max=25)
    private String label;

    @OneToMany(mappedBy = "folder", orphanRemoval = true)
    private List<Chapter> chapters = new LinkedList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "username")
    private User user;

    public Folder(){};

    public Folder(String label) {
        this.label = label;
    }

    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Folder{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", chapters=" + chapters +
                ", user=" + user +
                '}';
    }

    public void assignChapterToFolder(Chapter chapter){
        this.chapters.add(chapter);
        chapter.setFolder(this);
    }
}
