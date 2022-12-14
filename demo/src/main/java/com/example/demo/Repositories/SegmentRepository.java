package com.example.demo.Repositories;

import com.example.demo.Entities.Segment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SegmentRepository extends JpaRepository<Segment, Long> {

    public Segment findById(long id);
}
