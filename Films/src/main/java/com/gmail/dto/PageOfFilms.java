package com.gmail.dto;

import com.gmail.dao.entity.Film;
import java.util.List;

public class PageOfFilms {

  private int number;

  private int size;

  private int total_pages;

  private int total_elements;

  private List<Film> films;

  private boolean isFirstPage;

  private boolean isLastPage;

  private int number_of_element;
}
