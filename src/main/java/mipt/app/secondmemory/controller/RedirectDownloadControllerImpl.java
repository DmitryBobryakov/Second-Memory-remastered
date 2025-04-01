package mipt.app.secondmemory.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.service.FilesServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;



@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/second-memory")
public class RedirectDownloadControllerImpl implements RedirectDownloadController {
    private final FilesServiceImpl filesService;
    public ModelAndView download(String bucketName, HttpServletRequest request) throws Exception {
        String key = new AntPathMatcher()
                .extractPathWithinPattern(
                        request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString(),
                        request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString());
        return filesService.download(bucketName, key);
    }
}
