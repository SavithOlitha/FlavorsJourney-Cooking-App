package com.example.cookingapp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true)
    private String email;

    private String password;
    private String bio;             // A short bio about the user
    private String profilePicUrl;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_watchlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private Set<Recipe> watchlist = new HashSet<>();

    // Getters and Setters
    public Set<Recipe> getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(Set<Recipe> watchlist) {
        this.watchlist = watchlist;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_saved_articles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id")
    )
    private Set<Article> savedArticles = new HashSet<>();

    // Users that this user is following
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_followings",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_id")
    )
    private Set<User> following = new HashSet<>();

    // Users that follow this user (inverse side)
    @ManyToMany(mappedBy = "following", fetch = FetchType.EAGER)
    private Set<User> followers = new HashSet<>();

    // Getter and Setter
    public Set<Article> getSavedArticles() {
        return savedArticles;
    }

    public void setSavedArticles(Set<Article> savedArticles) {
        this.savedArticles = savedArticles;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_cooking_watchlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "video_id")
    )
    private Set<CookingVideo> cookingWatchlist = new HashSet<>();

    // Add Getter & Setter
    public Set<CookingVideo> getCookingWatchlist() {
        return cookingWatchlist;
    }

    public void setCookingWatchlist(Set<CookingVideo> cookingWatchlist) {
        this.cookingWatchlist = cookingWatchlist;
    }


    // Default constructor
    public User() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public Set<User> getFollowing() {
        return following;
    }
    public void setFollowing(Set<User> following) {
        this.following = following;
    }
    public Set<User> getFollowers() {
        return followers;
    }
    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && id.equals(user.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
