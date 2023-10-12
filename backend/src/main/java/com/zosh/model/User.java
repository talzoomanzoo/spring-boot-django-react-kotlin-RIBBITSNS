package com.zosh.model;

<<<<<<< HEAD
=======
import java.time.LocalDateTime;
>>>>>>> 6ae8e9f75dbac32ee0f1ba08023b9b4a9a6289f3
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
<<<<<<< HEAD
=======
import com.fasterxml.jackson.annotation.JsonManagedReference;
>>>>>>> 6ae8e9f75dbac32ee0f1ba08023b9b4a9a6289f3

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
<<<<<<< HEAD
=======
import jakarta.persistence.FetchType;
>>>>>>> 6ae8e9f75dbac32ee0f1ba08023b9b4a9a6289f3
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    private String location;

    private String website;

    private String birthDate;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private String mobile;

    private String image;

    private String backgroundImage;
    
    private String bio;
    
<<<<<<< HEAD
=======
    private String education;
    
>>>>>>> 6ae8e9f75dbac32ee0f1ba08023b9b4a9a6289f3
    private boolean req_user;
    
    private boolean login_with_google;
    
    private boolean is_req_user=false;
    
<<<<<<< HEAD
//    @ManyToMany(mappedBy = "retwitUser",cascade = CascadeType.ALL)
//    private List<Twit> retwits = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Twit> twit = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
=======
    private LocalDateTime joinedAt; 
    
//    @ManyToMany(mappedBy = "retwitUser",cascade = CascadeType.ALL)
//    private List<Twit> retwits = new ArrayList<>();
    
    @JsonIgnore // 특정 필드 위에 사용하면 해당 필드는 JSON 직렬화 과정에서 무시되어 출력되지 않음
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Twit> twit = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
>>>>>>> 6ae8e9f75dbac32ee0f1ba08023b9b4a9a6289f3
    private List<Like> likes  = new ArrayList<>();
    
    @Embedded
    private Varification verification;
    
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
<<<<<<< HEAD
    private List<User>followers=new ArrayList<>();
    
    @JsonIgnore
    @ManyToMany(mappedBy = "followers")
    private List<User>followings=new ArrayList<>();
    
=======
    private List<User> followers=new ArrayList<>();
    
    @JsonIgnore
    //@ManyToMany(mappedBy = "followers")
    @ManyToMany(cascade = CascadeType.ALL) // followers와 followings 테이블 분리를 통해 null칸 폐기
    private List<User> followings=new ArrayList<>();
>>>>>>> 6ae8e9f75dbac32ee0f1ba08023b9b4a9a6289f3
    
}
