package com.publico.htmltopdfapi.service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GenerateService {

    private final Playwright playwright;
    
    @Value("${pdf.format:A4}")
    private String pageFormat;
    
    @Value("${pdf.margin.top:20}")
    private String marginTop;
    
    @Value("${pdf.margin.bottom:20}")
    private String marginBottom;
    
    @Value("${pdf.margin.left:20}")
    private String marginLeft;
    
    @Value("${pdf.margin.right:20}")
    private String marginRight;
    
    @Value("${pdf.timeout:30000}")
    private int timeout;

    public GenerateService(Playwright playwright) {
        this.playwright = playwright;
    }

    public byte[] generatePdf(String htmlContent) throws IOException {
        Browser browser = null;
        Page page = null;
        
        try {
            // Criar browser Chromium headless
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(true));
            
            // Criar nova página
            page = browser.newPage();
            
            // Configurar timeout para carregamento
            page.setDefaultTimeout(timeout);
            
            // Definir conteúdo HTML
            page.setContent(htmlContent);
            
            // Aguardar carregamento completo (incluindo recursos externos)
            page.waitForLoadState();
            
            // Configurar opções do PDF
            Page.PdfOptions pdfOptions = new Page.PdfOptions()
                    .setFormat(pageFormat)
//                    .setMarginTop(Double.parseDouble(marginTop))
//                    .setMarginBottom(Double.parseDouble(marginBottom))
//                    .setMarginLeft(Double.parseDouble(marginLeft))
//                    .setMarginRight(Double.parseDouble(marginRight))
                    .setPrintBackground(true); // Importante para renderizar backgrounds CSS
            
            // Gerar PDF
            byte[] pdfBytes = page.pdf(pdfOptions);
            
            return pdfBytes;
            
        } finally {
            // Fechar recursos
            if (page != null) {
                page.close();
            }
            if (browser != null) {
                browser.close();
            }
        }
    }
}
