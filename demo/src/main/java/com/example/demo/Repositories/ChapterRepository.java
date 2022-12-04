package com.example.demo.Repositories;

import com.example.demo.Entities.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    public Chapter findById(long id);

}
