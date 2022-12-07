package com.example.demo.Impls;

import com.example.demo.Entities.Chapter;
import com.example.demo.Entities.Segment;
import com.example.demo.ImageUtils;
import com.example.demo.Repositories.ChapterRepository;
import com.example.demo.Repositories.SegmentRepository;
import com.example.demo.Services.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public void assignSegmentToChapter(Segment segment, Chapter chapter, MultipartFile file) {
        if (file.getContentType().contains("image")) {
            try {
                segment.setFileType(file.getContentType());
                segment.setImage(ImageUtils.compressImage(file.getBytes()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            chapter.getSegments().add(segment);
            segment.setChapter(chapter);
            segmentRepository.save(segment);
            chapterRepository.save(chapter);
    }

    @Override
    public void editSegment(Segment segment, long editedSegmentId) {
        Segment editedSegment = segmentRepository.findById(editedSegmentId);
        editedSegment.setText(segment.getText());
        segmentRepository.save(editedSegment);
    }
}
