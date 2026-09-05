package com.chambule.controle_gastos.entities;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;
    private String email;
    private  String password;
    private LocalDate creationDate;

    public User(){}

    public User(Long Id, String name, String email, String password, LocalDate creationDate) {
        this.Id = Id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.creationDate = creationDate;
    }

    @OneToMany(mappedBy = "user")
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Launch> launches = new HashSet<>();


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Set<Launch> getLaunches() {
        return launches;
    }

    @PrePersist
    private void creationDate(){
        this.creationDate = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(Id, user.Id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(Id);
    }
}
