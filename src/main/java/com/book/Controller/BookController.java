package com.book.Controller;

import com.book.Entity.Book;
import com.book.Repository.BookRepository;
import com.book.ResponseMessage;
import com.book.Service.BookService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RestController
public class BookController {

    private static Logger log = LoggerFactory.getLogger(BookController.class);

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookService bookService;
//    public BookController(BookService bookService) {
//        this.bookService = bookService;
//    }

    public String move(MultipartFile file) throws IOException {
        Path temp = Files.move(Paths.get("C:\\Users\\dartmedia\\Documents\\Office\\Excel\\" + file.getOriginalFilename()),
                Paths.get("C:\\Users\\dartmedia\\Documents\\Office\\Excel\\tes\\"+ file.getOriginalFilename()));

        if(temp != null)
        {
            System.out.println("File renamed and moved successfully");
        }
        else
        {
            System.out.println("Failed to move the file");
        }

        return temp.toString();
    }

    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadfile(@RequestParam("file") MultipartFile file) throws IOException{
        String message = "";
        log.info("get Upload Start");

        try {
          bookService.save(file);
          move(file);

          message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return new ResponseEntity<>(bookService.result, HttpStatus.OK);
      }catch (Exception e) {
          message = "Could not upload the file: " + file.getOriginalFilename() + "!";
          return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
      }

    }

    @GetMapping("/list")
    public ResponseEntity<?> findall() {
        log.info("get List Start");
        try {
            List<Book> books = bookRepository.findAll();
            bookService.gets();
            System.out.println(bookService.result);

            if (books.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(bookService.result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private int convertStringToInt(String str) {
        int result = 0;

        if (str == null || str.isEmpty() || str.trim().isEmpty()) {
            return result;
        }

        result = Integer.parseInt(str);

        return result;
    }

    private String getCellValue(Row row, int cellNo) {
        DataFormatter formatter = new DataFormatter();

        Cell cell = row.getCell(cellNo);

        return formatter.formatCellValue(cell);
    }
}
