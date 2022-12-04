package com.example.demo.Repositories;

import com.example.demo.Entities.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    public Folder findById(long id);

    public List<Folder> findAllByOrderByLabelDesc();

}
