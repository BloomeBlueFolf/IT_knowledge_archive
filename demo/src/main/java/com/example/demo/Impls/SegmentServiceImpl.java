package com.example.demo.Impls;

import com.example.demo.Entities.Chapter;
import com.example.demo.Entities.Segment;
import com.example.demo.Repositories.ChapterRepository;
import com.example.demo.Repositories.SegmentRepository;
import com.example.demo.Services.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SegmentServiceImpl implements SegmentService {

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private ChapterRepository chapterRepository;


    @Override
    public List<Segment> getAllSegments() {
        return segmentRepository.findAll();
    }

    @Override
    public Segment getSegment(long id) {
        return segmentRepository.findById(id);
    }

    @Override
    public void saveSegment(Segment segment) {
        segmentRepository.save(segment);
    }

    @Override
    public void deleteSegment(Segment segment) {
        segmentRepository.delete(segment);
    }

    @Override
    public void assignSegmentToChapter(Segment segment, Chapter chapter) {
        chapter.getSegments().add(segment);
        segment.setChapter(chapter);
        segmentRepository.save(segment);
        chapterRepository.save(chapter); //checking if necessary
    }
}
