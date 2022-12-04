package com.example.demo.Services;

import com.example.demo.Entities.Chapter;
import com.example.demo.Entities.Segment;

import java.util.List;

public interface SegmentService {

    public List<Segment> getAllSegments();

    public Segment getSegment(long id);

    public void saveSegment(Segment segment);

    public void deleteSegment(Segment segment);

    public void assignSegmentToChapter(Segment segment, Chapter chapter);
}
