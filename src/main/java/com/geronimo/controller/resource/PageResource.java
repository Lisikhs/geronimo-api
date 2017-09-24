package com.geronimo.controller.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Iterator;
import java.util.List;


/**
 * A resource class for rendering HATEOAS information about page of results
 */
public class PageResource<T> extends ResourceSupport implements Page<T> {

    private static final String PAGE_PARAM = "page";
    private static final String SIZE_PARAM = "size";

    private final Page<T> page;

    public PageResource(Page<T> page, String pageParam,
                        String sizeParam) {
        super();
        this.page = page;
        addPreviousLink(page, pageParam, sizeParam);
        addNextLink(page, pageParam, sizeParam);
        addFirstLink(page, pageParam, sizeParam);
        addLastLink(page, pageParam, sizeParam);
        addSelfLink(page, pageParam, sizeParam);
    }

    public PageResource(Page<T> page) {
        super();
        this.page = page;
        addPreviousLink(page, PAGE_PARAM, SIZE_PARAM);
        addNextLink(page, PAGE_PARAM, SIZE_PARAM);
        addFirstLink(page, PAGE_PARAM, SIZE_PARAM);
        addLastLink(page, PAGE_PARAM, SIZE_PARAM);
        addSelfLink(page, PAGE_PARAM, SIZE_PARAM);
    }

    private void addPreviousLink(Page<T> page, String pageParam,
                                 String sizeParam) {
        if (page.hasPrevious()) {
            Link link = buildPageLink(pageParam, page.getNumber() - 1, sizeParam, page.getSize(), Link.REL_PREVIOUS);
            add(link);
        }
    }

    private void addNextLink(Page<T> page, String pageParam, String sizeParam) {
        if (page.hasNext()) {
            Link link = buildPageLink(pageParam, page.getNumber() - 1, sizeParam, page.getSize(), Link.REL_NEXT);
            add(link);
        }
    }

    private void addFirstLink(Page<T> page, String pageParam, String sizeParam) {
        if (page.getNumber() != 0) {
            Link link = buildPageLink(pageParam, 0, sizeParam, page.getSize(), Link.REL_FIRST);
            add(link);
        }
    }

    private void addLastLink(Page<T> page, String pageParam, String sizeParam) {
        if (page.getTotalPages() != 0 && page.getNumber() != page.getTotalPages() - 1) {
            Link link = buildPageLink(pageParam, page.getTotalPages() - 1, sizeParam, page.getSize(), Link.REL_LAST);
            add(link);
        }
    }

    private void addSelfLink(Page<T> page, String pageParam, String sizeParam) {
        Link link = buildPageLink(pageParam, page.getNumber(), sizeParam, page.getSize(), Link.REL_SELF);
        add(link);
    }

    private Link buildPageLink(String pageParam, int page, String sizeParam, int size, String rel) {
        String path = createBuilder()
                .queryParam(pageParam, page)
                .queryParam(sizeParam, size)
                .build()
                .toUriString();
        return new Link(path, rel);
    }

    private ServletUriComponentsBuilder createBuilder() {
        return ServletUriComponentsBuilder.fromCurrentRequestUri();
    }

    @Override
    public int getNumber() {
        return page.getNumber();
    }

    @Override
    public int getSize() {
        return page.getSize();
    }

    @Override
    public int getNumberOfElements() {
        return page.getNumberOfElements();
    }

    @Override
    public List<T> getContent() {
        return page.getContent();
    }

    @Override
    public boolean hasContent() {
        return page.hasContent();
    }

    @Override
    public Sort getSort() {
        return page.getSort();
    }

    @JsonIgnore
    @Override
    public boolean isFirst() {
        return page.isFirst();
    }

    @JsonIgnore
    @Override
    public boolean isLast() {
        return page.isLast();
    }

    @Override
    public boolean hasNext() {
        return page.hasNext();
    }

    @Override
    public boolean hasPrevious() {
        return page.hasPrevious();
    }

    @Override
    public Pageable nextPageable() {
        return page.nextPageable();
    }

    public Pageable previousPageable() {
        return page.previousPageable();
    }

    @Override
    public int getTotalPages() {
        return page.getTotalPages();
    }

    @Override
    public long getTotalElements() {
        return page.getTotalElements();
    }

    @Override
    public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
        return page.map(converter);
    }

    @Override
    public Iterator<T> iterator() {
        return page.iterator();
    }
}
