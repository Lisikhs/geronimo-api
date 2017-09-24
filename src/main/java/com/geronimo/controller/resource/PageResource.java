package com.geronimo.controller.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Iterator;
import java.util.List;


/**
 * A resource class for rendering HATEOAS information about page of results
 */
@XmlRootElement(name = "page")
@NoArgsConstructor
public class PageResource<T> extends ResourceSupport implements Page<T> {

    private static final String PAGE_PARAM = "page";
    private static final String SIZE_PARAM = "size";

    private PageImpl<T> page;

    public PageResource(PageImpl<T> page) {
        super();
        this.page = page;
        addPreviousLink();
        addNextLink();
        addFirstLink();
        addLastLink();
        addSelfLink();
    }

    public PageImpl<T> getPage() {
        return page;
    }

    public void setPage(PageImpl<T> page) {
        this.page = page;
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

    @XmlElement
    @Override
    public int getNumber() {
        return page.getNumber();
    }

    @XmlElement
    @Override
    public int getSize() {
        return page.getSize();
    }

    @XmlElement
    @Override
    public int getNumberOfElements() {
        return page.getNumberOfElements();
    }

    @XmlElement
    @Override
    public List<T> getContent() {
        return page.getContent();
    }

    @Override
    public boolean hasContent() {
        return page.hasContent();
    }

    @XmlElement
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

    @JsonIgnore
    @Override
    public boolean hasNext() {
        return page.hasNext();
    }

    @JsonIgnore
    @Override
    public boolean hasPrevious() {
        return page.hasPrevious();
    }

    @JsonIgnore
    @Override
    public Pageable nextPageable() {
        return page.nextPageable();
    }

    @JsonIgnore
    @Override
    public Pageable previousPageable() {
        return page.previousPageable();
    }

    @XmlElement
    @Override
    public int getTotalPages() {
        return page.getTotalPages();
    }

    @XmlElement
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
