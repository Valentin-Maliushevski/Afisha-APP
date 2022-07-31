package com.itacademy.dto;

import java.util.List;

public class CustomPage<T>{

  //Номер страницы
  private int number;

  //Размер страницы
  private int size;

  //Количество страниц
  private int totalPages;

  //Количество записей
  private long totalElements;

  //Количество элементов на странице
  private int numberOfElements;

  //Признак является ли элемент первым
  private boolean isFirstPage;

  //Признак является ли элемент последним
  private boolean isLastPage;

  private List<T> content;

  public CustomPage() {
  }

  public int getNumber() {
    return number;
  }

  public int getSize() {
    return size;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public long getTotalElements() {
    return totalElements;
  }

  public int getNumberOfElements() {
    return numberOfElements;
  }

  public boolean isFirstPage() {
    return isFirstPage;
  }

  public boolean isLastPage() {
    return isLastPage;
  }

  public List<T> getContent() {
    return content;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  public void setTotalElements(long totalElements) {
    this.totalElements = totalElements;
  }

  public void setNumberOfElements(int numberOfElements) { this.numberOfElements = numberOfElements; }

  public void setFirstPage(boolean firstPage) {
    isFirstPage = firstPage;
  }

  public void setLastPage(boolean lastPage) {
    isLastPage = lastPage;
  }

  public void setContent(List<T> content) {
    this.content = content;
  }

}
