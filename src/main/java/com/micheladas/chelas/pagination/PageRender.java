package com.micheladas.chelas.pagination;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class PageRender<T> {

	private String url;
	private Page<T> page;
	private int totalPages;
	private int numElementsPerPage;
	private int currentPage;
	private List<PageItem> pages;

	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.pages = new ArrayList<PageItem>();

		numElementsPerPage = 5;
		totalPages = page.getTotalPages();
		currentPage = page.getNumber() + 1;

		int from, to;
		if (totalPages <= numElementsPerPage) {
			from = 1;
			to = totalPages;
		} else {
			if (currentPage <= numElementsPerPage / 2) {
				from = 1;
				to = numElementsPerPage;
			} else if (currentPage >= totalPages - numElementsPerPage / 2) {
				from = totalPages - numElementsPerPage + 1;
				to = numElementsPerPage;
			} else {
				from = currentPage - numElementsPerPage / 2;
				to = numElementsPerPage;
			}
		}

		for (int i = 0; i < to; i++) {
			pages.add(new PageItem(from + i, currentPage == from + i));
		}
	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public boolean isHasNext() {
		return page.hasNext();
	}

	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

}
