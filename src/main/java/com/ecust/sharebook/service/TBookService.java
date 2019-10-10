package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.BookInf;
import org.springframework.stereotype.Service;


public interface TBookService {
    BookInf selectByIsbn(String isbn);
}
