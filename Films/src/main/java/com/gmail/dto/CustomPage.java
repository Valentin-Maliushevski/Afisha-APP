package com.gmail.dto;

import java.util.List;

public class CustomPage<T>{

  private int number;

  private int size;

  private int totalPages;

  private long totalElements;

  private int numberOfElements;

  private boolean isFirstPage;

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
