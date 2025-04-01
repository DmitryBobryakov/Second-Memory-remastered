package mipt.app.secondmemory.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/second-memory")
@Tag(name = "File API", description = "Управление файлами")
public interface RedirectDownloadController {
    @GetMapping("/files/download/{bucketName}")
    ModelAndView download(@PathVariable(name = "bucketName") String bucketName, HttpServletRequest request) throws Exception;

}
