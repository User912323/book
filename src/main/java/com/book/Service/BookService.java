package com.book.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.book.Entity.Book;
import com.book.Repository.BookRepository;
import com.book.Store;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class BookService {
    @Autowired
    BookRepository repository;

    @Async("asyncExecutor")
    public CompletableFuture<Store> save(MultipartFile file) {
        try {
            List<Store> stores = new ArrayList<>();
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());

            // Read student data form excel file sheet1.
            XSSFSheet worksheet = workbook.getSheetAt(0);
            for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
                if (index > 0) {
                    XSSFRow row = worksheet.getRow(index);
                    Store store = new Store();

                    store.no = getCellValue(row, 0);
                    store.Nama = getCellValue(row, 1);
                    store.Buku = getCellValue(row, 2);
                    store.Tahun = getCellValue(row, 3);
                    store.Tanggal = getCellValue(row, 4);

                    stores.add(store);
                }
            }

            List<Book> entities = new ArrayList<>();
            if (stores.size() > 0) {
                stores.forEach(x->{
                    Book entity = new Book();
                    entity.no = x.no;
                    entity.Nama = x.Nama;
                    entity.Buku = x.Buku;
                    entity.Tahun = x.Tahun;
                    entity.Tanggal = String.valueOf(x.Tanggal);

                    entities.add(entity);
                });

                repository.saveAll(entities);
            }

        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
        return null;
    }

//    public ByteArrayInputStream load() {
//        List<Book> tutorials = repository.findAll();
//
//        ByteArrayInputStream in = ExcelHelper.tutorialsToExcel(tutorials);
//        return in;
//    }

    public List<Book> getAllTutorials() {
        return repository.findAll();
    }
    public List<Store> result = new ArrayList<>();

    @Async("asyncExecutor")
    public CompletableFuture<Store> gets()
    {
        List<Book> books = repository.findAll();

        if (books != null && books.size() > 0){
            books.forEach(x->{
                Store item = new Store();
                item.id = x.id;
                item.no = x.no;
                item.Nama = x.Nama;
                item.Buku = x.Buku;
                item.Tahun = x.Tahun;
                item.Tanggal = x.Tanggal;

                result.add(item);
            });
        }
        return null;
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
