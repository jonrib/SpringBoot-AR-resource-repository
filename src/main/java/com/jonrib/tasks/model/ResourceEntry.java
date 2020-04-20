package com.jonrib.tasks.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "entries")
public class ResourceEntry {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String title;
	private String description;
	@ElementCollection
	private Set<String> tags;
	private String category;
	private boolean isPrivate;
	
	@ManyToMany
	private Set<User> author;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<PreviewImage> images;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<ResourceFile> files;
	@ManyToMany
	private Set<User> editors;
	@ManyToMany
	private Set<User> readers;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<History> histories;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Comment> comments;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set<String> getTags() {
		return tags;
	}
	public void setTags(Set<String> tags) {
		this.tags = tags;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Set<PreviewImage> getImages() {
		return images;
	}
	public void setImages(Set<PreviewImage> images) {
		this.images = images;
	}
	public Set<ResourceFile> getFiles() {
		return files;
	}
	public void setFiles(Set<ResourceFile> files) {
		this.files = files;
	}
	public boolean isPrivate() {
		return isPrivate;
	}
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	public Set<User> getEditors() {
		return editors;
	}
	public void setEditors(Set<User> editors) {
		this.editors = editors;
	}
	public Set<User> getReaders() {
		return readers;
	}
	public void setReaders(Set<User> readers) {
		this.readers = readers;
	}
	public Set<User> getAuthor() {
		return author;
	}
	public void setAuthor(Set<User> author) {
		this.author = author;
	}
	public Set<History> getHistories() {
		return histories;
	}
	public void setHistories(Set<History> histories) {
		this.histories = histories;
	}
	public Set<Comment> getComments() {
		return comments;
	}
	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
}
