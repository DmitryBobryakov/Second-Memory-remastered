package mipt.app.secondmemory.controller.file;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/second-memory")
@Tag(name = "Download File", description = "Скачивание файла")
public interface RedirectFileDownloadController {
  @GetMapping("/files/download/{bucketName}")
  ModelAndView download(
      @PathVariable(name = "bucketName") String bucketName, HttpServletRequest request)
      throws Exception;
}
