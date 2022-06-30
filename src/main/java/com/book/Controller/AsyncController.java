package com.book.Controller;

import com.book.Service.BookService;
import com.book.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class AsyncController {
    private static Logger log = LoggerFactory.getLogger(AsyncController.class);

    @Autowired
    private BookService service;

    @RequestMapping(value = "/testAsynch", method = RequestMethod.POST)
    public ResponseEntity<?> testAsynch(@RequestParam("file") MultipartFile file) throws InterruptedException, ExecutionException {
        try {
            log.info("testAsynch Start");

            CompletableFuture<Store> getlist = service.gets();

            CompletableFuture<Store> uploadlist = service.save(file);

            // Wait until they are all done
            CompletableFuture.allOf(getlist).join();

            log.info("EmployeeAddress--> " + getlist.get());
            log.info("EmployeeAddress--> " + uploadlist.get());
            return new ResponseEntity<>(service.result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
