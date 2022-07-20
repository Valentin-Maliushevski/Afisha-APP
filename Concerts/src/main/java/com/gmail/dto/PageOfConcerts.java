package com.gmail.dto;

import com.gmail.dao.entity.Concert;
import java.util.List;

public class PageOfConcerts {

  private int number;

  private int size;

  private int total_pages;

  private int total_elements;

  private List<Concert> films;

  private boolean isFirstPage;

  private boolean isLastPage;

  private int number_of_element;
}
