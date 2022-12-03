package com.example.demo.Entities;

import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "folder")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String label = "";

    @OneToMany(mappedBy = "folder", orphanRemoval = true)
    private List<Chapter> chapters = new LinkedList<>();

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

    @Override
    public String toString() {
        return "Folder{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", chapters=" + chapters +
                '}';
    }

    public void assignChapterToFolder(Chapter chapter){
        this.chapters.add(chapter);
        chapter.setFolder(this);
    }
}
