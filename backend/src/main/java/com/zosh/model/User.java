package com.zosh.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
    
    private String education;
    
    private boolean req_user;
    
    private boolean login_with_google;
    
    private boolean is_req_user=false;
    
//    private boolean hasFollowedLists;
    
    private LocalDateTime joinedAt; 
    
//    @ManyToMany(mappedBy = "retwitUser",cascade = CascadeType.ALL)
//    private List<Twit> retwits = new ArrayList<>();
    
    @JsonIgnore // 특정 필드 위에 사용하면 해당 필드는 JSON 직렬화 과정에서 무시되어 출력되지 않음
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)//, fetch = FetchType.LAZY)
    private List<Twit> twit = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)//, fetch = FetchType.LAZY)
    private List<Like> likes  = new ArrayList<>();
    
    @JsonIgnore
    @Embedded
    private Varification verification;
    
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.DETACH)
    private List<User> followers=new ArrayList<>();
    
    @JsonIgnore
    //@ManyToMany(mappedBy = "followers")
    //@ManyToMany(cascade = CascadeType.ALL) // followers와 followings 테이블 분리를 통해 null칸 폐기
    @ManyToMany(cascade = CascadeType.DETACH)
    private List<User> followings=new ArrayList<>();
    
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.DETACH)
    private List<ListModel> followedLists=new ArrayList<>();
    
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.DETACH)
    private List<FollowTwit> followTwit= new ArrayList<>();
    
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.DETACH)
    private List<Community> followedComs=new ArrayList<>();
    
}
