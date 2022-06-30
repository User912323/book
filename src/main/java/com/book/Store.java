package com.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    public int id;
    public String no;
    public String Nama;
    public String Buku;
    public String Tahun;
    public String Tanggal;
}
