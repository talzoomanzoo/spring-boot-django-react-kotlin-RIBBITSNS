package com.zosh.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor

public class Twit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id") // 외래키를 매핑할 때 사용; name 속성에는 매핑할 외래키 이름 지정
    @JsonIgnoreProperties(value = {"twit", "likes", "user"})
    private User user;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade= CascadeType.DETACH)
    @JsonBackReference
    @JoinColumn(name = "com_id") // 외래키를 매핑할 때 사용; name 속성에는 매핑할 외래키 이름 지정
    @JsonIgnoreProperties(value = {"twit", "likes", "user"})
    private Community community;
    
    @Column(name = "com_name")
    private String comName;

//    private Long userId;
//    
//    private Long comId;

    @Column(nullable = false)
    private String content;
 
    @JsonIgnore
    @OneToMany(cascade = CascadeType.DETACH)
    //mappedBy = "twit", 
    @JsonManagedReference
    //@JsonIgnoreProperties(value = {"twit", "likes"})
    @JoinColumn(name = "twit_id")
    @JsonIgnoreProperties(value = {"twit", "likes", "user"})
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "twit", cascade = CascadeType.ALL) // 붙는 엔티티가 List 1, 상대가 M
    private List<Notification> notifications = new ArrayList<>(); // 여러개의 twit에 하나의 like 리스트

    @OneToMany(cascade = {CascadeType.ALL, CascadeType.REMOVE})
    @JoinColumn(name = "twit_id")
    @JsonIgnore
    @JsonManagedReference
    @JsonIgnoreProperties(value = {"twit", "likes", "user"})
    private List<Twit> replyTwits = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.DETACH)
    @JsonIgnoreProperties(value = {"twit", "likes", "user"})
    private List<User> retwitUser = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY) // 붙는 엔티티가 M, 상대가 1
    @JsonBackReference
    @JsonIgnore
    @JsonIgnoreProperties(value = {"twit", "likes", "user"})
    private Twit replyFor; // 하나의 트윗에 대해 M개의 댓글, Twit의 내용을 넣기 위해 사용

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime retwitAt;
    private String editedAt; 
    
    @Column(nullable = false)
    private int viewCount;
    private String image; 
    private String video;
    private String thumbnail;
    
    @Column(nullable = false)
    private boolean isEdited = false; 
    private String location;
    
    private boolean isCom;
    private boolean isReply; 
    private boolean isTwit; 
    private boolean is_liked = false;
    private boolean is_notification = false; 
    private boolean is_retwit = false;
    private String ethicrate; //윤리수치 저장
    private int ethiclabel; //수치중 가장 큰것을 라벨값으로 저장
    private int ethicrateMAX;// 최고수치 저장
}
