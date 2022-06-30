package com.book.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String no;
    public String Nama;
    public String Buku;
    public String Tahun;
    public String Tanggal;

    public Book(int id, String nama, String buku, String tahun, String Tanggal, String no) {
        this.id = id;
        Nama = nama;
        Buku = buku;
        Tahun = tahun;
        this.Tanggal = Tanggal;
        this.no = no;
    }
}
