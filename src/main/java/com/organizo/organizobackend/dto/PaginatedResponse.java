package com.organizo.organizobackend.dto;

import java.util.List;

/**
 * Wrapper genérico para respostas paginadas.
 * Evita cachear diretamente PageImpl no Redis e gera JSON simples.
 */
public class PaginatedResponse<T> {

    /** Lista de itens desta página */
    private List<T> content;
    /** Número da página atual (0‑based) */
    private int pageNumber;
    /** Tamanho máximo da página */
    private int pageSize;
    /** Total de elementos em toda a consulta */
    private long totalElements;
    /** Total de páginas disponíveis */
    private int totalPages;

    public PaginatedResponse() { }

    public PaginatedResponse(List<T> content,
                             int pageNumber,
                             int pageSize,
                             long totalElements,
                             int totalPages) {
        this.content       = content;
        this.pageNumber    = pageNumber;
        this.pageSize      = pageSize;
        this.totalElements = totalElements;
        this.totalPages    = totalPages;
    }

    // — GETTERS e SETTERS —
    public List<T> getContent()          {
        return content;
    }
    public void setContent(List<T> c)    {
        this.content = c;
    }

    public int getPageNumber()           {
        return pageNumber;
    }

    public void setPageNumber(int p)     {
        this.pageNumber = p;
    }

    public int getPageSize()             {
        return pageSize;
    }

    public void setPageSize(int s)       {
        this.pageSize = s;
    }

    public long getTotalElements()       {
        return totalElements;
    }

    public void setTotalElements(long t) {
        this.totalElements = t;
    }

    public int getTotalPages()           {
        return totalPages;
    }

    public void setTotalPages(int t)     {
        this.totalPages = t;
    }
}
