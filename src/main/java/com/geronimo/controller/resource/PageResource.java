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

    public PageResource(Page<T> page) {
        super();
        this.page = page;
        addPreviousLink();
        addNextLink();
        addFirstLink();
        addLastLink();
        addSelfLink();
    }

    private void addPreviousLink() {
        if (page.hasPrevious()) {
            add(buildPageLink(page.getNumber() - 1, page.getSize(), Link.REL_PREVIOUS));
        }
    }

    private void addNextLink() {
        if (page.hasNext()) {
            add(buildPageLink(page.getNumber() - 1, page.getSize(), Link.REL_NEXT));
        }
    }

    private void addFirstLink() {
        if (page.getNumber() != 0) {
            add(buildPageLink(0, page.getSize(), Link.REL_FIRST));
        }
    }

    private void addLastLink() {
        if (page.getTotalPages() != 0 && page.getNumber() != page.getTotalPages() - 1) {
            add(buildPageLink(page.getTotalPages() - 1, page.getSize(), Link.REL_LAST));
        }
    }

    private void addSelfLink() {
        add(buildPageLink(page.getNumber(), page.getSize(), Link.REL_SELF));
    }

    private Link buildPageLink(int page, int size, String rel) {
        String path = createBuilder()
                .queryParam(PAGE_PARAM, page)
                .queryParam(SIZE_PARAM, size)
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
