package com.example.demo.Controllers;

import com.example.demo.Entities.Segment;
import com.example.demo.ImageUtils;
import com.example.demo.Impls.ChapterServiceImpl;
import com.example.demo.Impls.SegmentServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SegmentController {

    @Autowired
    private ChapterServiceImpl chapterService;

    @Autowired
    private SegmentServiceImpl segmentService;

    @GetMapping("/user/segments")
    public String showSegments(Model model,
                               @RequestParam ("id") long id){

        model.addAttribute(("chapter"), chapterService.getChapter(id));
        model.addAttribute(("segmentList"), segmentService.findSegmentsOrderedByDbIndex(id));
        return "segments";
    }

    @GetMapping("/user/segment/addContent")
    public String addSegment(Model model,
                             @RequestParam ("id") long id){

        Segment segment = new Segment();
        model.addAttribute(("segment"), segment);
        model.addAttribute(("id"), id);
        return "addSegmentForm";
    }

    @PostMapping("/user/segment/addContent")
    public String addSegment(@Valid @ModelAttribute ("segment") Segment segment,
                             BindingResult result,
                             @RequestParam ("id") long id,
                             @RequestParam ("file") MultipartFile file,
                             RedirectAttributes redirectAttributes,
                             Model model){

        if(result.hasErrors()){
            Segment segment2 = new Segment();
            model.addAttribute(("segment"), segment);
            model.addAttribute(("id"), id);
            return "addSegmentForm";
        }

        segmentService.assignSegmentToChapter(segment, chapterService.getChapter(id), file);
        redirectAttributes.addAttribute(("id"), id);
        return "redirect:/user/segments?addingSuccess";
    }

    @GetMapping("/user/segment/image")
    public ResponseEntity<?> getImage(@RequestParam ("id") long id){

        Segment segment = segmentService.getSegment(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(segment.getFileType()))
                .body(ImageUtils.decompressImage(segment.getImage()));
    }

    @GetMapping("/user/segment/delete")
    public String deleteSegment(@RequestParam ("id") long id,
                                @RequestParam ("chapterId") long chapterId,
                                RedirectAttributes redirectAttributes){

        segmentService.deleteSegment(segmentService.getSegment(id));
        redirectAttributes.addAttribute(("id"), chapterId);
        return "redirect:/user/segments";
    }

    @GetMapping("/user/segment/edit")
    public String editSegment(Model model,
                              @RequestParam ("id") long id){

        Segment editedSegment = segmentService.getSegment(id);
        model.addAttribute(("segment"), editedSegment);
        return "EditSegmentForm";
    }

    @PostMapping("/user/segment/edit")
    public String editSegment(@Valid @ModelAttribute ("segment") Segment segment,
                              BindingResult result,
                              @RequestParam ("id") long id,
                              RedirectAttributes redirectAttributes,
                              Model model){

        if(result.hasErrors()){
            Segment editedSegment = segmentService.getSegment(id);
            model.addAttribute(("segment"), editedSegment);
            return "EditSegmentForm";
        }

        segmentService.editSegment(segment, id);
        redirectAttributes.addAttribute(("id"), segmentService.getSegment(id).getChapter().getId());
        return "redirect:/user/segments?editingSuccess";
    }

    @GetMapping("/user/segment/swapWithPrevious")
    public ModelAndView swapWithPrevious(@RequestParam ("id") long id,
                                         @RequestParam ("segmentId") long segmentId,
                                         RedirectAttributes redirectAttributes){

        segmentService.swapWithPreviousSegment(segmentService.getSegment(segmentId));
        redirectAttributes.addAttribute(("id"), id);
        return new ModelAndView("redirect:/user/segments");
    }

    @GetMapping("/user/segment/swapWithFollowing")
    public ModelAndView swapWithFollowing(@RequestParam ("id") long id,
                                   @RequestParam ("segmentId") long segmentId,
                                    RedirectAttributes redirectAttributes){

        segmentService.swapWithFollowingSegment(segmentService.getSegment(segmentId));
        redirectAttributes.addAttribute(("id"), id);
        return new ModelAndView("redirect:/user/segments");
    }

    @GetMapping("/user/segment/createPdf")
    public String createPdf(Model model,
                            @RequestParam ("id") long id){

        segmentService.createPdf(id);
        model.addAttribute(("chapter"), chapterService.getChapter(id));
        model.addAttribute(("segmentList"), segmentService.findSegmentsOrderedByDbIndex(id));
        return "segments";
    }
}
