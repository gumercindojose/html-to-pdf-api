package com.publico.htmltopdfapi.controller;

import com.publico.htmltopdfapi.service.GenerateService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PdfController {

    // MUDE PARA O SEU REPOSITÓRIO REAL
    private static final String SOURCE_CODE_URL = "https://github.com/seuusuario/html-to-pdf-service";
    
    private final GenerateService generateService;

    public PdfController(GenerateService generateService) {
        this.generateService = generateService;
    }

    @PostMapping(value = "/html-to-pdf", produces = "application/pdf")
    public ResponseEntity<byte[]> convertHtmlToPdf(@RequestBody String html) {

        try {
            byte[] pdfBytes = generateService.generatePdf(html);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment().filename("document.pdf").build());
            headers.set("X-Source-Code", SOURCE_CODE_URL);
            headers.set("Link", "<" + SOURCE_CODE_URL + ">; rel=\"sourcecode\"");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<String> home() {
        String body = """
                <h1>HTML to PDF Service</h1>
                <p>Envie um POST com HTML para <code>/api/html-to-pdf</code></p>
                <p><strong>Código-fonte:</strong> <a href="%s">%s</a></p>
                <p>Powered by Playwright - Suporte completo a CSS moderno (Flexbox, Grid, Gradientes, Animações)</p>
                """.formatted(SOURCE_CODE_URL, SOURCE_CODE_URL);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Source-Code", SOURCE_CODE_URL);

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }
}
